/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-04 Wolfgang M. Meier
 *  wolfgang@exist-db.org
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
package org.exist.xquery;

import org.exist.dom.QName;
import org.exist.memtree.DocumentImpl;
import org.exist.memtree.MemTreeBuilder;
import org.exist.memtree.NodeImpl;
import org.exist.xquery.util.ExpressionDumper;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceIterator;

/**
 * Represents a dynamic attribute constructor. The implementation differs from
 * AttributeConstructor as the evaluation is not controlled by the surrounding 
 * element. The attribute name as well as its value are only determined at evaluation time,
 * not at compile time.
 *  
 * @author wolf
 */
public class DynamicAttributeConstructor extends NodeConstructor {

    private Expression qnameExpr;
    private Expression valueExpr;
    
    /**
     * @param context
     */
    public DynamicAttributeConstructor(XQueryContext context) {
        super(context);
    }

    public void setNameExpr(Expression expr) {
        this.qnameExpr = new Atomize(context, expr);
    }
    
    public void setContentExpr(Expression expr) {
        this.valueExpr  = new Atomize(context, expr);
    }
    
    /* (non-Javadoc)
     * @see org.exist.xquery.Expression#analyze(org.exist.xquery.Expression)
     */
    public void analyze(Expression parent, int flags) throws XPathException {
        qnameExpr.analyze(this, flags);
        valueExpr.analyze(this, flags);
    }
    
    /* (non-Javadoc)
     * @see org.exist.xquery.Expression#eval(org.exist.xquery.value.Sequence, org.exist.xquery.value.Item)
     */
    public Sequence eval(Sequence contextSequence, Item contextItem)
            throws XPathException {
        MemTreeBuilder builder = context.getDocumentBuilder();
        context.proceed(this, builder);
        Sequence nameSeq = qnameExpr.eval(contextSequence, contextItem);
        if(nameSeq.getLength() != 1)
            throw new XPathException(getASTNode(), "The name expression should evaluate to a single value");
        QName qn = QName.parse(context, nameSeq.getStringValue());
        
        String value;
        Sequence valueSeq = valueExpr.eval(contextSequence, contextItem);
        if(valueSeq.getLength() == 0)
            value = "";
        else {
            StringBuffer buf = new StringBuffer();
            for(SequenceIterator i = valueSeq.iterate(); i.hasNext(); ) {
                Item next = i.nextItem();
                buf.append(next.getStringValue());
                if(i.hasNext())
                    buf.append(' ');
            }
            value = buf.toString();
        }
        int nodeNr = builder.addAttribute(qn, value);
        NodeImpl node = ((DocumentImpl)builder.getDocument()).getAttribute(nodeNr);
        return node;
    }

    /* (non-Javadoc)
     * @see org.exist.xquery.Expression#dump(org.exist.xquery.util.ExpressionDumper)
     */
    public void dump(ExpressionDumper dumper) {
        dumper.display("attribute { ");
        qnameExpr.dump(dumper);
        dumper.display(" } {");
        dumper.startIndent();
        valueExpr.dump(dumper);
        dumper.endIndent();
        dumper.nl().display("}");
    }
    
    public String toString() {
    	StringBuffer result = new StringBuffer();
    	result.append("attribute { ");
    	result.append(qnameExpr.toString());
    	result.append(" } { ");        
    	result.append(valueExpr.toString());        
    	result.append(" }");
    	return result.toString();
    }    
}
