/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-04 The eXist Project
 *  http://exist-db.org
 *  
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *  
 *  $Id$
 */
package org.exist.dom;

import org.exist.numbering.NodeId;
import org.exist.storage.Signatures;
import org.exist.util.ByteArrayPool;
import org.exist.util.ByteConversion;
import org.exist.util.UTF8;
import org.exist.util.XMLString;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class CDATASectionImpl extends CharacterDataImpl implements CDATASection {

    public CDATASectionImpl() {
        super(Node.CDATA_SECTION_NODE);
    }
    
    public CDATASectionImpl(NodeId nodeId, String data) {
        super(Node.CDATA_SECTION_NODE, nodeId, data);
    }

    public CDATASectionImpl(NodeId nodeId) {
        super(Node.CDATA_SECTION_NODE, nodeId);
    }
    
    public CDATASectionImpl( XMLString data ) {
        super( Node.CDATA_SECTION_NODE);
        this.cdata = data;
    }
    
    public int getChildCount() {
        return 0;
    }
    
    public Node getFirstChild() {
        return null;
    }
    
    public boolean hasChildNodes() {
        return false;
    }
    
    public String getWholeText() {
        return null;
    }

    public boolean isElementContentWhitespace() {
        return false;
    }

    public Text replaceWholeText(String content) throws DOMException {
        return null;
    }

    public Text splitText(int offset) throws DOMException {
        return null;
    }
    
    public byte[] serialize() {
        final int nodeIdLen = nodeId.size();
        byte[] data = ByteArrayPool.getByteArray(cdata.UTF8Size() + nodeIdLen + 3);
        data[0] = (byte) ( Signatures.Cdata << 0x5 );
        ByteConversion.shortToByte((short) nodeId.units(), data, 1);
        nodeId.serialize(data, 3);
        cdata.UTF8Encode(data, nodeIdLen + 3);
        return data;
    }
    
    public static StoredNode deserialize(byte[] data,
            int start,
            int len,
            DocumentImpl doc,
            boolean pooled) {
        int dlnLen = ByteConversion.byteToShort(data, start + 1);
        NodeId dln =
            doc.getBroker().getBrokerPool().getNodeFactory().createFromData(dlnLen, data, start + 3);
        CDATASectionImpl cdata = new CDATASectionImpl(dln);
        int nodeIdLen = dln.size();
        cdata.cdata = UTF8.decode(data, start + nodeIdLen + 3, len - nodeIdLen - 3);
        return cdata;
    }
}
