/*
 * eXist Open Source Native XML Database
 * Copyright (C) 2001-2017 The eXist Project
 * http://exist-db.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.exist.util.io;

import org.exist.test.ExistXmldbEmbeddedServer;
import org.exist.xmldb.EXistResource;
import org.exist.xmldb.ExtendedResource;
import org.exist.xmldb.LocalBinaryResource;
import org.exist.xquery.value.BinaryValue;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.BinaryResource;
import org.xmldb.api.modules.CollectionManagementService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.exist.TestUtils.getExistHomeFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FilterInputStreamCacheMonitorTest {

    @ClassRule
    public static ExistXmldbEmbeddedServer existXmldbEmbeddedServer = new ExistXmldbEmbeddedServer(false, true);

    private static String TEST_COLLECTION_NAME = "testFilterInputStreamCacheMonitor";

    @BeforeClass
    public static void setup() throws XMLDBException, IOException {
        final Collection testCollection = existXmldbEmbeddedServer.createCollection(existXmldbEmbeddedServer.getRoot(), TEST_COLLECTION_NAME);
        try(final EXistResource resource = (EXistResource)testCollection.createResource("icon.png", BinaryResource.RESOURCE_TYPE)) {
            final Optional<Path> icon = getExistHomeFile("icon.png");
            resource.setContent(icon.get());
            testCollection.storeResource(resource);
        }
        testCollection.close();
    }

    @AfterClass
    public static void cleanup() throws XMLDBException {
        final CollectionManagementService cms = (CollectionManagementService) existXmldbEmbeddedServer.getRoot().getService("CollectionManagementService", "1.0");
        cms.removeCollection(TEST_COLLECTION_NAME);
    }

    @Test
    public void binaryResult() throws XMLDBException, IOException {
        final FilterInputStreamCacheMonitor monitor = FilterInputStreamCacheMonitor.getInstance();

        // assert no binaries in use yet
        assertEquals(0, monitor.getActive().size());

        ResourceSet resourceSet = null;
        try {
            resourceSet = existXmldbEmbeddedServer.executeQuery(
                    "util:binary-doc('/db/" + TEST_COLLECTION_NAME + "/icon.png')");

            assertEquals(1, resourceSet.getSize());

            try (final EXistResource resource = (EXistResource)resourceSet.getResource(0)) {
                assertTrue(resource instanceof LocalBinaryResource);
                assertTrue(((ExtendedResource)resource).getExtendedContent() instanceof BinaryValue);

                // one active binary (as it is in the result set)
                assertEquals(1, monitor.getActive().size());
            }

            // assert no active binaries as we just closed the resource in the try-with-resources
            assertEquals(0, monitor.getActive().size());

        } finally {
            resourceSet.clear();
        }
    }

    @Test
    public void enclosedExpressionCleanup() throws XMLDBException, IOException {
        final FilterInputStreamCacheMonitor monitor = FilterInputStreamCacheMonitor.getInstance();

        // assert no binaries in use yet
        assertEquals(0, monitor.getActive().size());

        ResourceSet resourceSet = null;
        try {
            resourceSet = existXmldbEmbeddedServer.executeQuery(
                    "let $embedded := <logo><image>{util:binary-doc('/db/" + TEST_COLLECTION_NAME + "/icon.png')}</image></logo>\n" +
                            "return xmldb:store('/db/" + TEST_COLLECTION_NAME + "', 'icon.xml', $embedded)");

            assertEquals(1, resourceSet.getSize());
            try (final EXistResource resource = (EXistResource)resourceSet.getResource(0)) {
                assertFalse(resource instanceof LocalBinaryResource);

                // assert still no active binaries (because they have been cleaned up)
                assertEquals(0, monitor.getActive().size());
            }

        } finally {
            resourceSet.clear();
        }
    }

    @Test
    public void enclosedExpressionsCleanup() throws XMLDBException, IOException {
        final FilterInputStreamCacheMonitor monitor = FilterInputStreamCacheMonitor.getInstance();

        // assert no binaries in use yet
        assertEquals(0, monitor.getActive().size());

        ResourceSet resourceSet = null;
        try {
            resourceSet = existXmldbEmbeddedServer.executeQuery(
                    "let $bin := util:binary-doc('/db/" + TEST_COLLECTION_NAME + "/icon.png')\n" +
                    "let $embedded := <logo><image>{$bin}</image></logo>\n" +
                    "let $embedded-2 := <other>{$bin}</other>\n" +
                    "return xmldb:store('/db/" + TEST_COLLECTION_NAME + "', 'icon.xml', $embedded)");

            assertEquals(1, resourceSet.getSize());
            try (final EXistResource resource = (EXistResource)resourceSet.getResource(0)) {
                assertFalse(resource instanceof LocalBinaryResource);

                // assert still no active binaries (because they have been cleaned up)
                assertEquals(0, monitor.getActive().size());
            }

        } finally {
            resourceSet.clear();
        }
    }
}
