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
package org.exist.xquery;

import org.exist.dom.QName;
import org.exist.dom.QName.IllegalQNameException;
import org.exist.xquery.util.ExpressionDumper;
import org.exist.xquery.value.FunctionReference;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the XQuery 3.1 arrow operator.
 *
 * @author wolf
 */
public class ArrowOperator extends AbstractExpression {

    private Expression leftExpr;
    private FunctionCall fcall = null;
    private Expression funcSpec = null;
    private List<Expression> parameters;
    private AnalyzeContextInfo cachedContextInfo;

    public ArrowOperator(final XQueryContext context, final Expression leftExpr) throws
            XPathException {
        super(context);
        this.leftExpr = leftExpr;
    }

    public void setArrowFunction(final String fname, final List<Expression> params) throws XPathException {
        try {
            final QName name = QName.parse(context, fname, context.getDefaultFunctionNamespace());
            this.fcall = NamedFunctionReference.lookupFunction(this, context, name, params.size() + 1);
            this.parameters = params;
        } catch (final IllegalQNameException e) {
            throw new XPathException(ErrorCodes.XPST0081, "No namespace defined for prefix " + fname);
        }
    }

    public void setArrowFunction(final PathExpr funcSpec, final List<Expression> params) {
        this.funcSpec = funcSpec.simplify();
        this.parameters = params;
    }

    @Override
    public void analyze(final AnalyzeContextInfo contextInfo) throws XPathException {
        if(getContext().getXQueryVersion() < 31) {
            throw new XPathException(this,
                ErrorCodes.EXXQDY0003,
                "arrow operator is not available before XQuery 3.1");
        }
        this.cachedContextInfo = contextInfo;
        leftExpr.analyze(contextInfo);
        if (fcall != null) {
            fcall.analyze(contextInfo);
        }
        if (funcSpec != null) {
            funcSpec.analyze(contextInfo);
        }
    }

    @Override
    public Sequence eval(Sequence contextSequence, final Item contextItem) throws XPathException {
        if (contextItem != null) {
            contextSequence = contextItem.toSequence();
        }
        contextSequence = leftExpr.eval(contextSequence);

        final FunctionReference fref;
        if (fcall != null) {
            fref = new FunctionReference(fcall);
        } else {
            final Sequence funcSeq = funcSpec.eval(contextSequence, contextItem);
            if (funcSeq.getCardinality() != Cardinality.EXACTLY_ONE)
            {throw new XPathException(this, ErrorCodes.XPTY0004,
                    "Expected exactly one item for the function to be called, got " + funcSeq.getItemCount() +
                            ". Expression: " + ExpressionDumper.dump(funcSpec));}
            final Item item0 = funcSeq.itemAt(0);
            if (!Type.subTypeOf(item0.getType(), Type.FUNCTION_REFERENCE)) {
                throw new XPathException(this, ErrorCodes.XPTY0004,
                    "Type error: expected function, got " + Type.getTypeName(item0.getType()));
            }
            fref = (FunctionReference)item0;
        }
        final List<Expression> fparams = new ArrayList<>(parameters.size() + 1);
        fparams.add(new ContextParam(context, contextSequence));
        fparams.addAll(parameters);

        fref.setArguments(fparams);
        // need to create a new AnalyzeContextInfo to avoid memory leak
        // cachedContextInfo will stay in memory
        fref.analyze(new AnalyzeContextInfo(cachedContextInfo));
        // Evaluate the function
        final Sequence result = fref.eval(contextSequence);
        fref.resetState(false);
        return result;
    }

    @Override
    public int returnsType() {
        return fcall == null ? Type.ITEM : fcall.returnsType();
    }

    @Override
    public int getCardinality() {
        return fcall == null ? super.getCardinality() : fcall.getCardinality();
    }

    @Override
    public void dump(final ExpressionDumper dumper) {
        leftExpr.dump(dumper);
        dumper.display(" => ");
        if (fcall != null) {
            dumper.display(fcall.getFunction().getName()).display('(');
        } else {
            funcSpec.dump(dumper);
        }
        for (int i = 0; i < parameters.size(); i++) {
            if (i > 0) {
                dumper.display(", ");
                parameters.get(i).dump(dumper);
            }
        }
        dumper.display(')');
    }

    @Override
    public void resetState(boolean postOptimization) {
        super.resetState(postOptimization);
        leftExpr.resetState(postOptimization);
        if (fcall != null) {
            fcall.resetState(postOptimization);
        }
        if (funcSpec != null) {
            funcSpec.resetState(postOptimization);
        }
        for (Expression param: parameters) {
            param.resetState(postOptimization);
        }
    }

    private class ContextParam extends Function.Placeholder {

        private Sequence sequence;

        ContextParam(XQueryContext context, Sequence sequence) {
            super(context);
            this.sequence = sequence;
        }

        @Override
        public void analyze(AnalyzeContextInfo contextInfo) throws XPathException {
        }

        @Override
        public Sequence eval(Sequence contextSequence, Item contextItem) throws XPathException {
            return sequence;
        }

        @Override
        public int returnsType() {
            return sequence.getItemType();
        }

        @Override
        public void dump(ExpressionDumper dumper) {

        }
    }
}