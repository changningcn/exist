/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-07 The eXist Project
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
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * $Id$
 */
package org.exist.management;

import org.apache.log4j.Logger;
import org.exist.util.DatabaseConfigurationException;

import javax.management.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: wolf
 * Date: Jun 9, 2007
 * Time: 8:53:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class JMXAgent implements Agent {

    private final static Logger LOG = Logger.getLogger(JMXAgent.class);

    private static Agent agent = null;

    public static Agent getInstance() {
        if (agent == null)
            agent = new JMXAgent();
        return agent;
    }

    private MBeanServer server;

    public JMXAgent() {
        if (LOG.isDebugEnabled())
            LOG.debug("Creating the JMX MBeanServer.");

        ArrayList servers = MBeanServerFactory.findMBeanServer(null);
        if (servers.size() > 0)
            server = (MBeanServer) servers.get(0);
        else
            server = MBeanServerFactory.createMBeanServer();

//        try {
//            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://127.0.0.1:9999/server");
//            JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
//            cs.start();
//        } catch (IOException e) {
//            LOG.warn("ERROR: failed to initialize JMX connector: " + e.getMessage(), e);
//        }
        registerSystemMBeans();
    }

    public synchronized void registerSystemMBeans() {
        try {
            ObjectName name = new ObjectName("org.exist.management:type=LockManager");
            addMBean(name, new org.exist.management.LockManager());
        } catch (MalformedObjectNameException e) {
            LOG.warn("Exception while registering cache mbean.", e);
        } catch (DatabaseConfigurationException e) {
            LOG.warn("Exception while registering cache mbean.", e);
        }
    }

    public synchronized void addMBean(String name, Object mbean) throws DatabaseConfigurationException {
        try {
            addMBean(new ObjectName(name), mbean);
        } catch (MalformedObjectNameException e) {
            LOG.warn("Problem registering mbean: " + e.getMessage(), e);
            throw new DatabaseConfigurationException("Exception while registering JMX mbean: " + e.getMessage());
        }
    }

    private void addMBean(ObjectName name, Object mbean) throws DatabaseConfigurationException {
        try {
            server.registerMBean(mbean, name);
        } catch (InstanceAlreadyExistsException e) {
            LOG.warn("Problem registering mbean: " + e.getMessage(), e);
            throw new DatabaseConfigurationException("Exception while registering JMX mbean: " + e.getMessage());
        } catch (MBeanRegistrationException e) {
            LOG.warn("Problem registering mbean: " + e.getMessage(), e);
            throw new DatabaseConfigurationException("Exception while registering JMX mbean: " + e.getMessage());
        } catch (NotCompliantMBeanException e) {
            LOG.warn("Problem registering mbean: " + e.getMessage(), e);
            throw new DatabaseConfigurationException("Exception while registering JMX mbean: " + e.getMessage());
        }
    }
}
