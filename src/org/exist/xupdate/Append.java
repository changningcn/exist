package org.exist.xupdate;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.exist.EXistException;
import org.exist.dom.DocumentSet;
import org.exist.dom.NodeProxy;
import org.exist.dom.NodeSet;
import org.exist.security.Permission;
import org.exist.security.PermissionDeniedException;
import org.exist.security.User;
import org.exist.storage.BrokerPool;
import org.exist.util.XMLUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Append.java
 * 
 * @author Wolfgang Meier
 */
public class Append extends Modification {

	private final static Logger LOG = Logger.getLogger(Append.class);

	/**
	 * Constructor for Append.
	 * @param selectStmt
	 */
	public Append(BrokerPool pool, User user, String selectStmt) {
		super(pool, user, selectStmt);
	}
	/**
	 * @see org.exist.xupdate.Modification#process()
	 */
	public long process(DocumentSet docs)
		throws PermissionDeniedException, EXistException {
		System.out.println(XMLUtil.dump(content));
		NodeSet qr = select(docs);
		LOG.debug("select found " + qr.getLength() + " nodes for append");
		NodeProxy proxy;
		Node node;
		NodeList children = content.getChildNodes();
		int len = children.getLength();
		LOG.debug("found " + len + " nodes to append");
		for (Iterator i = qr.iterator(); i.hasNext();) {
			proxy = (NodeProxy) i.next();
			if (!proxy
				.doc
				.getCollection()
				.getPermissions()
				.validate(user, Permission.UPDATE))
				throw new PermissionDeniedException(
					"write access to collection denied; user="
						+ user.getName());
			if (!proxy.doc.getPermissions().validate(user, Permission.UPDATE))
				throw new PermissionDeniedException("permission to remove document denied");
			node = proxy.getNode();
			for (int j = 0; j < len; j++)
				node.appendChild(children.item(j));
		}
		return qr.getLength();
	}

	public String getName() {
		return "append";
	}

}
