// $ANTLR 2.7.4: "XQueryTree.g" -> "XQueryTreeParser.java"$

	package org.exist.xquery.parser;

	import antlr.debug.misc.*;
	import java.io.StringReader;
	import java.io.BufferedReader;
	import java.io.InputStreamReader;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Iterator;
	import java.util.Stack;
	import org.exist.storage.BrokerPool;
	import org.exist.storage.DBBroker;
	import org.exist.storage.analysis.Tokenizer;
	import org.exist.EXistException;
	import org.exist.dom.DocumentSet;
	import org.exist.dom.DocumentImpl;
	import org.exist.dom.QName;
	import org.exist.security.PermissionDeniedException;
	import org.exist.security.User;
	import org.exist.xquery.*;
	import org.exist.xquery.value.*;
	import org.exist.xquery.functions.*;

import antlr.TreeParser;
import antlr.Token;
import antlr.collections.AST;
import antlr.RecognitionException;
import antlr.ANTLRException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.collections.impl.BitSet;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;


/**
 * The tree parser: walks the AST created by {@link XQueryParser} and generates
 * an internal representation of the query in the form of XQuery expression objects.
 */
public class XQueryTreeParser extends antlr.TreeParser       implements XQueryTreeParserTokenTypes
 {

	private XQueryContext context;
	private ExternalModule myModule = null;
	protected ArrayList exceptions= new ArrayList(2);
	protected boolean foundError= false;

	public XQueryTreeParser(XQueryContext context) {
		this();
		this.context= context;
	}

	public ExternalModule getModule() {
		return myModule;
	}
	
	public boolean foundErrors() {
		return foundError;
	}

	public String getErrorMessage() {
		StringBuffer buf= new StringBuffer();
		for (Iterator i= exceptions.iterator(); i.hasNext();) {
			buf.append(((Exception) i.next()).toString());
			buf.append('\n');
		}
		return buf.toString();
	}

	public Exception getLastException() {
		return (Exception) exceptions.get(exceptions.size() - 1);
	}

	protected void handleException(Exception e) {
		foundError= true;
		exceptions.add(e);
	}

	private void throwException(XQueryAST ast, String message) throws XPathException {
		throw new XPathException(ast, message);
	}
	
	private static class ForLetClause {
		XQueryAST ast;
		String varName;
		SequenceType sequenceType= null;
		String posVar= null;
		Expression inputSequence;
		Expression action;
		boolean isForClause= true;
	}

	private static class FunctionParameter {
		String varName;
		SequenceType type= FunctionSignature.DEFAULT_TYPE;

		public FunctionParameter(String name) {
			this.varName= name;
		}
	}
public XQueryTreeParser() {
	tokenNames = _tokenNames;
}

	public final void xpointer(AST _t,
		PathExpr path
	) throws RecognitionException, XPathException {
		
		org.exist.xquery.parser.XQueryAST xpointer_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST nc = null;
		Expression step = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case XPOINTER:
			{
				AST __t2 = _t;
				org.exist.xquery.parser.XQueryAST tmp1_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,XPOINTER);
				_t = _t.getFirstChild();
				step=expr(_t,path);
				_t = _retTree;
				_t = __t2;
				_t = _t.getNextSibling();
				break;
			}
			case XPOINTER_ID:
			{
				AST __t3 = _t;
				org.exist.xquery.parser.XQueryAST tmp2_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,XPOINTER_ID);
				_t = _t.getFirstChild();
				nc = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,NCNAME);
				_t = _t.getNextSibling();
				_t = __t3;
				_t = _t.getNextSibling();
				
						Function fun= new FunId(context);
						List params= new ArrayList(1);
						params.add(new LiteralValue(context, new StringValue(nc.getText())));
						fun.setArguments(params);
						path.addPath(fun);
					
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (EXistException e) {
			handleException(e);
		}
		catch (PermissionDeniedException e) {
			handleException(e);
		}
		_retTree = _t;
	}
	
/**
 * Process a top-level expression like FLWOR, conditionals, comparisons etc.
 */
	public final Expression  expr(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		org.exist.xquery.parser.XQueryAST expr_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST c = null;
		org.exist.xquery.parser.XQueryAST astIf = null;
		org.exist.xquery.parser.XQueryAST someVarName = null;
		org.exist.xquery.parser.XQueryAST everyVarName = null;
		org.exist.xquery.parser.XQueryAST r = null;
		org.exist.xquery.parser.XQueryAST f = null;
		org.exist.xquery.parser.XQueryAST varName = null;
		org.exist.xquery.parser.XQueryAST posVar = null;
		org.exist.xquery.parser.XQueryAST l = null;
		org.exist.xquery.parser.XQueryAST letVarName = null;
		org.exist.xquery.parser.XQueryAST w = null;
		org.exist.xquery.parser.XQueryAST collURI = null;
		
			step= null;
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_castable:
		case LITERAL_cast:
		{
			step=typeCastExpr(_t,path);
			_t = _retTree;
			break;
		}
		case COMMA:
		{
			AST __t57 = _t;
			c = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,COMMA);
			_t = _t.getFirstChild();
			
						PathExpr left= new PathExpr(context);
						PathExpr right= new PathExpr(context);
					
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						SequenceConstructor sc= new SequenceConstructor(context);
						sc.setASTNode(c);
						sc.addPath(left);
						sc.addPath(right);
						path.addPath(sc);
						step = sc;
					
			_t = __t57;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_if:
		{
			AST __t58 = _t;
			astIf = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_if);
			_t = _t.getFirstChild();
			
						PathExpr testExpr= new PathExpr(context);
						PathExpr thenExpr= new PathExpr(context);
						PathExpr elseExpr= new PathExpr(context);
					
			step=expr(_t,testExpr);
			_t = _retTree;
			step=expr(_t,thenExpr);
			_t = _retTree;
			step=expr(_t,elseExpr);
			_t = _retTree;
			
						ConditionalExpression cond= new ConditionalExpression(context, testExpr, thenExpr, elseExpr);
						cond.setASTNode(astIf);
						path.add(cond);
						step = cond;
					
			_t = __t58;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_some:
		{
			AST __t59 = _t;
			org.exist.xquery.parser.XQueryAST tmp3_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_some);
			_t = _t.getFirstChild();
			
						List clauses= new ArrayList();
						PathExpr satisfiesExpr = new PathExpr(context);
					
			{
			_loop64:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==VARIABLE_BINDING)) {
					AST __t61 = _t;
					someVarName = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
					match(_t,VARIABLE_BINDING);
					_t = _t.getFirstChild();
					
										ForLetClause clause= new ForLetClause();
										PathExpr inputSequence = new PathExpr(context);
									
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case LITERAL_as:
					{
						AST __t63 = _t;
						org.exist.xquery.parser.XQueryAST tmp4_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
						match(_t,LITERAL_as);
						_t = _t.getFirstChild();
						sequenceType(_t,clause.sequenceType);
						_t = _retTree;
						_t = __t63;
						_t = _t.getNextSibling();
						break;
					}
					case QNAME:
					case PARENTHESIZED:
					case ABSOLUTE_SLASH:
					case ABSOLUTE_DSLASH:
					case WILDCARD:
					case PREFIX_WILDCARD:
					case FUNCTION:
					case UNARY_MINUS:
					case UNARY_PLUS:
					case VARIABLE_REF:
					case ELEMENT:
					case TEXT:
					case BEFORE:
					case AFTER:
					case ATTRIBUTE_TEST:
					case COMP_ELEM_CONSTRUCTOR:
					case COMP_ATTR_CONSTRUCTOR:
					case COMP_TEXT_CONSTRUCTOR:
					case COMP_COMMENT_CONSTRUCTOR:
					case COMP_PI_CONSTRUCTOR:
					case COMP_DOC_CONSTRUCTOR:
					case NCNAME:
					case EQ:
					case STRING_LITERAL:
					case LITERAL_element:
					case LCURLY:
					case COMMA:
					case STAR:
					case PLUS:
					case LITERAL_some:
					case LITERAL_every:
					case LITERAL_if:
					case LITERAL_return:
					case LITERAL_or:
					case LITERAL_and:
					case LITERAL_instance:
					case LITERAL_castable:
					case LITERAL_cast:
					case LT:
					case GT:
					case LITERAL_eq:
					case LITERAL_ne:
					case LITERAL_lt:
					case LITERAL_le:
					case LITERAL_gt:
					case LITERAL_ge:
					case NEQ:
					case GTEQ:
					case LTEQ:
					case LITERAL_is:
					case LITERAL_isnot:
					case ANDEQ:
					case OREQ:
					case LITERAL_to:
					case MINUS:
					case LITERAL_div:
					case LITERAL_idiv:
					case LITERAL_mod:
					case UNION:
					case LITERAL_intersect:
					case LITERAL_except:
					case SLASH:
					case DSLASH:
					case LITERAL_text:
					case LITERAL_node:
					case LITERAL_attribute:
					case LITERAL_comment:
					case 136:
					case SELF:
					case XML_COMMENT:
					case XML_PI:
					case AT:
					case PARENT:
					case LITERAL_child:
					case LITERAL_self:
					case LITERAL_descendant:
					case 148:
					case 149:
					case LITERAL_following:
					case LITERAL_parent:
					case LITERAL_ancestor:
					case 153:
					case 154:
					case DOUBLE_LITERAL:
					case DECIMAL_LITERAL:
					case INTEGER_LITERAL:
					case XML_CDATA:
					case LITERAL_preceding:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(_t);
					}
					}
					}
					step=expr(_t,inputSequence);
					_t = _retTree;
					
										clause.varName= someVarName.getText();
										clause.inputSequence= inputSequence;
										clauses.add(clause);
									
					_t = __t61;
					_t = _t.getNextSibling();
				}
				else {
					break _loop64;
				}
				
			} while (true);
			}
			step=expr(_t,satisfiesExpr);
			_t = _retTree;
			
						Expression action = satisfiesExpr;
						for (int i= clauses.size() - 1; i >= 0; i--) {
							ForLetClause clause= (ForLetClause) clauses.get(i);
							BindingExpression expr = new QuantifiedExpression(context, QuantifiedExpression.SOME);
							expr.setVariable(clause.varName);
							expr.setSequenceType(clause.sequenceType);
							expr.setInputSequence(clause.inputSequence);
							expr.setReturnExpression(action);
							satisfiesExpr= null;
							action= expr;
						}
						path.add(action);
						step = action;
					
			_t = __t59;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_every:
		{
			AST __t65 = _t;
			org.exist.xquery.parser.XQueryAST tmp5_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_every);
			_t = _t.getFirstChild();
			
						List clauses= new ArrayList();
						PathExpr satisfiesExpr = new PathExpr(context);
					
			{
			_loop70:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==VARIABLE_BINDING)) {
					AST __t67 = _t;
					everyVarName = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
					match(_t,VARIABLE_BINDING);
					_t = _t.getFirstChild();
					
										ForLetClause clause= new ForLetClause();
										PathExpr inputSequence = new PathExpr(context);
									
					{
					if (_t==null) _t=ASTNULL;
					switch ( _t.getType()) {
					case LITERAL_as:
					{
						AST __t69 = _t;
						org.exist.xquery.parser.XQueryAST tmp6_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
						match(_t,LITERAL_as);
						_t = _t.getFirstChild();
						sequenceType(_t,clause.sequenceType);
						_t = _retTree;
						_t = __t69;
						_t = _t.getNextSibling();
						break;
					}
					case QNAME:
					case PARENTHESIZED:
					case ABSOLUTE_SLASH:
					case ABSOLUTE_DSLASH:
					case WILDCARD:
					case PREFIX_WILDCARD:
					case FUNCTION:
					case UNARY_MINUS:
					case UNARY_PLUS:
					case VARIABLE_REF:
					case ELEMENT:
					case TEXT:
					case BEFORE:
					case AFTER:
					case ATTRIBUTE_TEST:
					case COMP_ELEM_CONSTRUCTOR:
					case COMP_ATTR_CONSTRUCTOR:
					case COMP_TEXT_CONSTRUCTOR:
					case COMP_COMMENT_CONSTRUCTOR:
					case COMP_PI_CONSTRUCTOR:
					case COMP_DOC_CONSTRUCTOR:
					case NCNAME:
					case EQ:
					case STRING_LITERAL:
					case LITERAL_element:
					case LCURLY:
					case COMMA:
					case STAR:
					case PLUS:
					case LITERAL_some:
					case LITERAL_every:
					case LITERAL_if:
					case LITERAL_return:
					case LITERAL_or:
					case LITERAL_and:
					case LITERAL_instance:
					case LITERAL_castable:
					case LITERAL_cast:
					case LT:
					case GT:
					case LITERAL_eq:
					case LITERAL_ne:
					case LITERAL_lt:
					case LITERAL_le:
					case LITERAL_gt:
					case LITERAL_ge:
					case NEQ:
					case GTEQ:
					case LTEQ:
					case LITERAL_is:
					case LITERAL_isnot:
					case ANDEQ:
					case OREQ:
					case LITERAL_to:
					case MINUS:
					case LITERAL_div:
					case LITERAL_idiv:
					case LITERAL_mod:
					case UNION:
					case LITERAL_intersect:
					case LITERAL_except:
					case SLASH:
					case DSLASH:
					case LITERAL_text:
					case LITERAL_node:
					case LITERAL_attribute:
					case LITERAL_comment:
					case 136:
					case SELF:
					case XML_COMMENT:
					case XML_PI:
					case AT:
					case PARENT:
					case LITERAL_child:
					case LITERAL_self:
					case LITERAL_descendant:
					case 148:
					case 149:
					case LITERAL_following:
					case LITERAL_parent:
					case LITERAL_ancestor:
					case 153:
					case 154:
					case DOUBLE_LITERAL:
					case DECIMAL_LITERAL:
					case INTEGER_LITERAL:
					case XML_CDATA:
					case LITERAL_preceding:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(_t);
					}
					}
					}
					step=expr(_t,inputSequence);
					_t = _retTree;
					
										clause.varName= everyVarName.getText();
										clause.inputSequence= inputSequence;
										clauses.add(clause);
									
					_t = __t67;
					_t = _t.getNextSibling();
				}
				else {
					break _loop70;
				}
				
			} while (true);
			}
			step=expr(_t,satisfiesExpr);
			_t = _retTree;
			
						Expression action = satisfiesExpr;
						for (int i= clauses.size() - 1; i >= 0; i--) {
							ForLetClause clause= (ForLetClause) clauses.get(i);
							BindingExpression expr = new QuantifiedExpression(context, QuantifiedExpression.EVERY);
							expr.setVariable(clause.varName);
							expr.setSequenceType(clause.sequenceType);
							expr.setInputSequence(clause.inputSequence);
							expr.setReturnExpression(action);
							satisfiesExpr= null;
							action= expr;
						}
						path.add(action);
						step = action;
					
			_t = __t65;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_return:
		{
			AST __t71 = _t;
			r = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_return);
			_t = _t.getFirstChild();
			
						List clauses= new ArrayList();
						Expression action= new PathExpr(context);
						action.setASTNode(r);
						PathExpr whereExpr= null;
						List orderBy= null;
					
			{
			int _cnt86=0;
			_loop86:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LITERAL_for:
				{
					AST __t73 = _t;
					f = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
					match(_t,LITERAL_for);
					_t = _t.getFirstChild();
					{
					int _cnt79=0;
					_loop79:
					do {
						if (_t==null) _t=ASTNULL;
						if ((_t.getType()==VARIABLE_BINDING)) {
							AST __t75 = _t;
							varName = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
							match(_t,VARIABLE_BINDING);
							_t = _t.getFirstChild();
							
														ForLetClause clause= new ForLetClause();
														clause.ast = f;
														PathExpr inputSequence= new PathExpr(context);
													
							{
							if (_t==null) _t=ASTNULL;
							switch ( _t.getType()) {
							case LITERAL_as:
							{
								AST __t77 = _t;
								org.exist.xquery.parser.XQueryAST tmp7_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
								match(_t,LITERAL_as);
								_t = _t.getFirstChild();
								clause.sequenceType= new SequenceType();
								sequenceType(_t,clause.sequenceType);
								_t = _retTree;
								_t = __t77;
								_t = _t.getNextSibling();
								break;
							}
							case QNAME:
							case PARENTHESIZED:
							case ABSOLUTE_SLASH:
							case ABSOLUTE_DSLASH:
							case WILDCARD:
							case PREFIX_WILDCARD:
							case FUNCTION:
							case UNARY_MINUS:
							case UNARY_PLUS:
							case VARIABLE_REF:
							case ELEMENT:
							case TEXT:
							case POSITIONAL_VAR:
							case BEFORE:
							case AFTER:
							case ATTRIBUTE_TEST:
							case COMP_ELEM_CONSTRUCTOR:
							case COMP_ATTR_CONSTRUCTOR:
							case COMP_TEXT_CONSTRUCTOR:
							case COMP_COMMENT_CONSTRUCTOR:
							case COMP_PI_CONSTRUCTOR:
							case COMP_DOC_CONSTRUCTOR:
							case NCNAME:
							case EQ:
							case STRING_LITERAL:
							case LITERAL_element:
							case LCURLY:
							case COMMA:
							case STAR:
							case PLUS:
							case LITERAL_some:
							case LITERAL_every:
							case LITERAL_if:
							case LITERAL_return:
							case LITERAL_or:
							case LITERAL_and:
							case LITERAL_instance:
							case LITERAL_castable:
							case LITERAL_cast:
							case LT:
							case GT:
							case LITERAL_eq:
							case LITERAL_ne:
							case LITERAL_lt:
							case LITERAL_le:
							case LITERAL_gt:
							case LITERAL_ge:
							case NEQ:
							case GTEQ:
							case LTEQ:
							case LITERAL_is:
							case LITERAL_isnot:
							case ANDEQ:
							case OREQ:
							case LITERAL_to:
							case MINUS:
							case LITERAL_div:
							case LITERAL_idiv:
							case LITERAL_mod:
							case UNION:
							case LITERAL_intersect:
							case LITERAL_except:
							case SLASH:
							case DSLASH:
							case LITERAL_text:
							case LITERAL_node:
							case LITERAL_attribute:
							case LITERAL_comment:
							case 136:
							case SELF:
							case XML_COMMENT:
							case XML_PI:
							case AT:
							case PARENT:
							case LITERAL_child:
							case LITERAL_self:
							case LITERAL_descendant:
							case 148:
							case 149:
							case LITERAL_following:
							case LITERAL_parent:
							case LITERAL_ancestor:
							case 153:
							case 154:
							case DOUBLE_LITERAL:
							case DECIMAL_LITERAL:
							case INTEGER_LITERAL:
							case XML_CDATA:
							case LITERAL_preceding:
							{
								break;
							}
							default:
							{
								throw new NoViableAltException(_t);
							}
							}
							}
							{
							if (_t==null) _t=ASTNULL;
							switch ( _t.getType()) {
							case POSITIONAL_VAR:
							{
								posVar = (org.exist.xquery.parser.XQueryAST)_t;
								match(_t,POSITIONAL_VAR);
								_t = _t.getNextSibling();
								clause.posVar= posVar.getText();
								break;
							}
							case QNAME:
							case PARENTHESIZED:
							case ABSOLUTE_SLASH:
							case ABSOLUTE_DSLASH:
							case WILDCARD:
							case PREFIX_WILDCARD:
							case FUNCTION:
							case UNARY_MINUS:
							case UNARY_PLUS:
							case VARIABLE_REF:
							case ELEMENT:
							case TEXT:
							case BEFORE:
							case AFTER:
							case ATTRIBUTE_TEST:
							case COMP_ELEM_CONSTRUCTOR:
							case COMP_ATTR_CONSTRUCTOR:
							case COMP_TEXT_CONSTRUCTOR:
							case COMP_COMMENT_CONSTRUCTOR:
							case COMP_PI_CONSTRUCTOR:
							case COMP_DOC_CONSTRUCTOR:
							case NCNAME:
							case EQ:
							case STRING_LITERAL:
							case LITERAL_element:
							case LCURLY:
							case COMMA:
							case STAR:
							case PLUS:
							case LITERAL_some:
							case LITERAL_every:
							case LITERAL_if:
							case LITERAL_return:
							case LITERAL_or:
							case LITERAL_and:
							case LITERAL_instance:
							case LITERAL_castable:
							case LITERAL_cast:
							case LT:
							case GT:
							case LITERAL_eq:
							case LITERAL_ne:
							case LITERAL_lt:
							case LITERAL_le:
							case LITERAL_gt:
							case LITERAL_ge:
							case NEQ:
							case GTEQ:
							case LTEQ:
							case LITERAL_is:
							case LITERAL_isnot:
							case ANDEQ:
							case OREQ:
							case LITERAL_to:
							case MINUS:
							case LITERAL_div:
							case LITERAL_idiv:
							case LITERAL_mod:
							case UNION:
							case LITERAL_intersect:
							case LITERAL_except:
							case SLASH:
							case DSLASH:
							case LITERAL_text:
							case LITERAL_node:
							case LITERAL_attribute:
							case LITERAL_comment:
							case 136:
							case SELF:
							case XML_COMMENT:
							case XML_PI:
							case AT:
							case PARENT:
							case LITERAL_child:
							case LITERAL_self:
							case LITERAL_descendant:
							case 148:
							case 149:
							case LITERAL_following:
							case LITERAL_parent:
							case LITERAL_ancestor:
							case 153:
							case 154:
							case DOUBLE_LITERAL:
							case DECIMAL_LITERAL:
							case INTEGER_LITERAL:
							case XML_CDATA:
							case LITERAL_preceding:
							{
								break;
							}
							default:
							{
								throw new NoViableAltException(_t);
							}
							}
							}
							step=expr(_t,inputSequence);
							_t = _retTree;
							
														clause.varName= varName.getText();
														clause.inputSequence= inputSequence;
														clauses.add(clause);
													
							_t = __t75;
							_t = _t.getNextSibling();
						}
						else {
							if ( _cnt79>=1 ) { break _loop79; } else {throw new NoViableAltException(_t);}
						}
						
						_cnt79++;
					} while (true);
					}
					_t = __t73;
					_t = _t.getNextSibling();
					break;
				}
				case LITERAL_let:
				{
					AST __t80 = _t;
					l = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
					match(_t,LITERAL_let);
					_t = _t.getFirstChild();
					{
					int _cnt85=0;
					_loop85:
					do {
						if (_t==null) _t=ASTNULL;
						if ((_t.getType()==VARIABLE_BINDING)) {
							AST __t82 = _t;
							letVarName = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
							match(_t,VARIABLE_BINDING);
							_t = _t.getFirstChild();
							
														ForLetClause clause= new ForLetClause();
														clause.ast = l;
														clause.isForClause= false;
														PathExpr inputSequence= new PathExpr(context);
													
							{
							if (_t==null) _t=ASTNULL;
							switch ( _t.getType()) {
							case LITERAL_as:
							{
								AST __t84 = _t;
								org.exist.xquery.parser.XQueryAST tmp8_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
								match(_t,LITERAL_as);
								_t = _t.getFirstChild();
								clause.sequenceType= new SequenceType();
								sequenceType(_t,clause.sequenceType);
								_t = _retTree;
								_t = __t84;
								_t = _t.getNextSibling();
								break;
							}
							case QNAME:
							case PARENTHESIZED:
							case ABSOLUTE_SLASH:
							case ABSOLUTE_DSLASH:
							case WILDCARD:
							case PREFIX_WILDCARD:
							case FUNCTION:
							case UNARY_MINUS:
							case UNARY_PLUS:
							case VARIABLE_REF:
							case ELEMENT:
							case TEXT:
							case BEFORE:
							case AFTER:
							case ATTRIBUTE_TEST:
							case COMP_ELEM_CONSTRUCTOR:
							case COMP_ATTR_CONSTRUCTOR:
							case COMP_TEXT_CONSTRUCTOR:
							case COMP_COMMENT_CONSTRUCTOR:
							case COMP_PI_CONSTRUCTOR:
							case COMP_DOC_CONSTRUCTOR:
							case NCNAME:
							case EQ:
							case STRING_LITERAL:
							case LITERAL_element:
							case LCURLY:
							case COMMA:
							case STAR:
							case PLUS:
							case LITERAL_some:
							case LITERAL_every:
							case LITERAL_if:
							case LITERAL_return:
							case LITERAL_or:
							case LITERAL_and:
							case LITERAL_instance:
							case LITERAL_castable:
							case LITERAL_cast:
							case LT:
							case GT:
							case LITERAL_eq:
							case LITERAL_ne:
							case LITERAL_lt:
							case LITERAL_le:
							case LITERAL_gt:
							case LITERAL_ge:
							case NEQ:
							case GTEQ:
							case LTEQ:
							case LITERAL_is:
							case LITERAL_isnot:
							case ANDEQ:
							case OREQ:
							case LITERAL_to:
							case MINUS:
							case LITERAL_div:
							case LITERAL_idiv:
							case LITERAL_mod:
							case UNION:
							case LITERAL_intersect:
							case LITERAL_except:
							case SLASH:
							case DSLASH:
							case LITERAL_text:
							case LITERAL_node:
							case LITERAL_attribute:
							case LITERAL_comment:
							case 136:
							case SELF:
							case XML_COMMENT:
							case XML_PI:
							case AT:
							case PARENT:
							case LITERAL_child:
							case LITERAL_self:
							case LITERAL_descendant:
							case 148:
							case 149:
							case LITERAL_following:
							case LITERAL_parent:
							case LITERAL_ancestor:
							case 153:
							case 154:
							case DOUBLE_LITERAL:
							case DECIMAL_LITERAL:
							case INTEGER_LITERAL:
							case XML_CDATA:
							case LITERAL_preceding:
							{
								break;
							}
							default:
							{
								throw new NoViableAltException(_t);
							}
							}
							}
							step=expr(_t,inputSequence);
							_t = _retTree;
							
														clause.varName= letVarName.getText();
														clause.inputSequence= inputSequence;
														clauses.add(clause);
													
							_t = __t82;
							_t = _t.getNextSibling();
						}
						else {
							if ( _cnt85>=1 ) { break _loop85; } else {throw new NoViableAltException(_t);}
						}
						
						_cnt85++;
					} while (true);
					}
					_t = __t80;
					_t = _t.getNextSibling();
					break;
				}
				default:
				{
					if ( _cnt86>=1 ) { break _loop86; } else {throw new NoViableAltException(_t);}
				}
				}
				_cnt86++;
			} while (true);
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_where:
			{
				w = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,LITERAL_where);
				_t = _t.getNextSibling();
				
								whereExpr= new PathExpr(context); 
								whereExpr.setASTNode(w);
							
				step=expr(_t,whereExpr);
				_t = _retTree;
				break;
			}
			case QNAME:
			case PARENTHESIZED:
			case ABSOLUTE_SLASH:
			case ABSOLUTE_DSLASH:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case UNARY_MINUS:
			case UNARY_PLUS:
			case VARIABLE_REF:
			case ELEMENT:
			case TEXT:
			case ORDER_BY:
			case BEFORE:
			case AFTER:
			case ATTRIBUTE_TEST:
			case COMP_ELEM_CONSTRUCTOR:
			case COMP_ATTR_CONSTRUCTOR:
			case COMP_TEXT_CONSTRUCTOR:
			case COMP_COMMENT_CONSTRUCTOR:
			case COMP_PI_CONSTRUCTOR:
			case COMP_DOC_CONSTRUCTOR:
			case NCNAME:
			case EQ:
			case STRING_LITERAL:
			case LITERAL_element:
			case LCURLY:
			case COMMA:
			case STAR:
			case PLUS:
			case LITERAL_some:
			case LITERAL_every:
			case LITERAL_if:
			case LITERAL_return:
			case LITERAL_or:
			case LITERAL_and:
			case LITERAL_instance:
			case LITERAL_castable:
			case LITERAL_cast:
			case LT:
			case GT:
			case LITERAL_eq:
			case LITERAL_ne:
			case LITERAL_lt:
			case LITERAL_le:
			case LITERAL_gt:
			case LITERAL_ge:
			case NEQ:
			case GTEQ:
			case LTEQ:
			case LITERAL_is:
			case LITERAL_isnot:
			case ANDEQ:
			case OREQ:
			case LITERAL_to:
			case MINUS:
			case LITERAL_div:
			case LITERAL_idiv:
			case LITERAL_mod:
			case UNION:
			case LITERAL_intersect:
			case LITERAL_except:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case LITERAL_attribute:
			case LITERAL_comment:
			case 136:
			case SELF:
			case XML_COMMENT:
			case XML_PI:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_descendant:
			case 148:
			case 149:
			case LITERAL_following:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 153:
			case 154:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			case XML_CDATA:
			case LITERAL_preceding:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ORDER_BY:
			{
				AST __t89 = _t;
				org.exist.xquery.parser.XQueryAST tmp9_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,ORDER_BY);
				_t = _t.getFirstChild();
				orderBy= new ArrayList(3);
				{
				int _cnt96=0;
				_loop96:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_tokenSet_0.member(_t.getType()))) {
						PathExpr orderSpecExpr= new PathExpr(context);
						step=expr(_t,orderSpecExpr);
						_t = _retTree;
						
												OrderSpec orderSpec= new OrderSpec(context, orderSpecExpr);
												int modifiers= 0;
												orderBy.add(orderSpec);
											
						{
						if (_t==null) _t=ASTNULL;
						switch ( _t.getType()) {
						case LITERAL_ascending:
						case LITERAL_descending:
						{
							{
							if (_t==null) _t=ASTNULL;
							switch ( _t.getType()) {
							case LITERAL_ascending:
							{
								org.exist.xquery.parser.XQueryAST tmp10_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
								match(_t,LITERAL_ascending);
								_t = _t.getNextSibling();
								break;
							}
							case LITERAL_descending:
							{
								org.exist.xquery.parser.XQueryAST tmp11_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
								match(_t,LITERAL_descending);
								_t = _t.getNextSibling();
								
																modifiers= OrderSpec.DESCENDING_ORDER;
																orderSpec.setModifiers(modifiers);
															
								break;
							}
							default:
							{
								throw new NoViableAltException(_t);
							}
							}
							}
							break;
						}
						case 3:
						case QNAME:
						case PARENTHESIZED:
						case ABSOLUTE_SLASH:
						case ABSOLUTE_DSLASH:
						case WILDCARD:
						case PREFIX_WILDCARD:
						case FUNCTION:
						case UNARY_MINUS:
						case UNARY_PLUS:
						case VARIABLE_REF:
						case ELEMENT:
						case TEXT:
						case BEFORE:
						case AFTER:
						case ATTRIBUTE_TEST:
						case COMP_ELEM_CONSTRUCTOR:
						case COMP_ATTR_CONSTRUCTOR:
						case COMP_TEXT_CONSTRUCTOR:
						case COMP_COMMENT_CONSTRUCTOR:
						case COMP_PI_CONSTRUCTOR:
						case COMP_DOC_CONSTRUCTOR:
						case NCNAME:
						case EQ:
						case STRING_LITERAL:
						case LITERAL_collation:
						case LITERAL_element:
						case LCURLY:
						case COMMA:
						case LITERAL_empty:
						case STAR:
						case PLUS:
						case LITERAL_some:
						case LITERAL_every:
						case LITERAL_if:
						case LITERAL_return:
						case LITERAL_or:
						case LITERAL_and:
						case LITERAL_instance:
						case LITERAL_castable:
						case LITERAL_cast:
						case LT:
						case GT:
						case LITERAL_eq:
						case LITERAL_ne:
						case LITERAL_lt:
						case LITERAL_le:
						case LITERAL_gt:
						case LITERAL_ge:
						case NEQ:
						case GTEQ:
						case LTEQ:
						case LITERAL_is:
						case LITERAL_isnot:
						case ANDEQ:
						case OREQ:
						case LITERAL_to:
						case MINUS:
						case LITERAL_div:
						case LITERAL_idiv:
						case LITERAL_mod:
						case UNION:
						case LITERAL_intersect:
						case LITERAL_except:
						case SLASH:
						case DSLASH:
						case LITERAL_text:
						case LITERAL_node:
						case LITERAL_attribute:
						case LITERAL_comment:
						case 136:
						case SELF:
						case XML_COMMENT:
						case XML_PI:
						case AT:
						case PARENT:
						case LITERAL_child:
						case LITERAL_self:
						case LITERAL_descendant:
						case 148:
						case 149:
						case LITERAL_following:
						case LITERAL_parent:
						case LITERAL_ancestor:
						case 153:
						case 154:
						case DOUBLE_LITERAL:
						case DECIMAL_LITERAL:
						case INTEGER_LITERAL:
						case XML_CDATA:
						case LITERAL_preceding:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(_t);
						}
						}
						}
						{
						if (_t==null) _t=ASTNULL;
						switch ( _t.getType()) {
						case LITERAL_empty:
						{
							org.exist.xquery.parser.XQueryAST tmp12_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
							match(_t,LITERAL_empty);
							_t = _t.getNextSibling();
							{
							if (_t==null) _t=ASTNULL;
							switch ( _t.getType()) {
							case LITERAL_greatest:
							{
								org.exist.xquery.parser.XQueryAST tmp13_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
								match(_t,LITERAL_greatest);
								_t = _t.getNextSibling();
								break;
							}
							case LITERAL_least:
							{
								org.exist.xquery.parser.XQueryAST tmp14_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
								match(_t,LITERAL_least);
								_t = _t.getNextSibling();
								
																modifiers |= OrderSpec.EMPTY_LEAST;
																orderSpec.setModifiers(modifiers);
															
								break;
							}
							default:
							{
								throw new NoViableAltException(_t);
							}
							}
							}
							break;
						}
						case 3:
						case QNAME:
						case PARENTHESIZED:
						case ABSOLUTE_SLASH:
						case ABSOLUTE_DSLASH:
						case WILDCARD:
						case PREFIX_WILDCARD:
						case FUNCTION:
						case UNARY_MINUS:
						case UNARY_PLUS:
						case VARIABLE_REF:
						case ELEMENT:
						case TEXT:
						case BEFORE:
						case AFTER:
						case ATTRIBUTE_TEST:
						case COMP_ELEM_CONSTRUCTOR:
						case COMP_ATTR_CONSTRUCTOR:
						case COMP_TEXT_CONSTRUCTOR:
						case COMP_COMMENT_CONSTRUCTOR:
						case COMP_PI_CONSTRUCTOR:
						case COMP_DOC_CONSTRUCTOR:
						case NCNAME:
						case EQ:
						case STRING_LITERAL:
						case LITERAL_collation:
						case LITERAL_element:
						case LCURLY:
						case COMMA:
						case STAR:
						case PLUS:
						case LITERAL_some:
						case LITERAL_every:
						case LITERAL_if:
						case LITERAL_return:
						case LITERAL_or:
						case LITERAL_and:
						case LITERAL_instance:
						case LITERAL_castable:
						case LITERAL_cast:
						case LT:
						case GT:
						case LITERAL_eq:
						case LITERAL_ne:
						case LITERAL_lt:
						case LITERAL_le:
						case LITERAL_gt:
						case LITERAL_ge:
						case NEQ:
						case GTEQ:
						case LTEQ:
						case LITERAL_is:
						case LITERAL_isnot:
						case ANDEQ:
						case OREQ:
						case LITERAL_to:
						case MINUS:
						case LITERAL_div:
						case LITERAL_idiv:
						case LITERAL_mod:
						case UNION:
						case LITERAL_intersect:
						case LITERAL_except:
						case SLASH:
						case DSLASH:
						case LITERAL_text:
						case LITERAL_node:
						case LITERAL_attribute:
						case LITERAL_comment:
						case 136:
						case SELF:
						case XML_COMMENT:
						case XML_PI:
						case AT:
						case PARENT:
						case LITERAL_child:
						case LITERAL_self:
						case LITERAL_descendant:
						case 148:
						case 149:
						case LITERAL_following:
						case LITERAL_parent:
						case LITERAL_ancestor:
						case 153:
						case 154:
						case DOUBLE_LITERAL:
						case DECIMAL_LITERAL:
						case INTEGER_LITERAL:
						case XML_CDATA:
						case LITERAL_preceding:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(_t);
						}
						}
						}
						{
						if (_t==null) _t=ASTNULL;
						switch ( _t.getType()) {
						case LITERAL_collation:
						{
							org.exist.xquery.parser.XQueryAST tmp15_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
							match(_t,LITERAL_collation);
							_t = _t.getNextSibling();
							collURI = (org.exist.xquery.parser.XQueryAST)_t;
							match(_t,STRING_LITERAL);
							_t = _t.getNextSibling();
							
														orderSpec.setCollation(collURI.getText());
													
							break;
						}
						case 3:
						case QNAME:
						case PARENTHESIZED:
						case ABSOLUTE_SLASH:
						case ABSOLUTE_DSLASH:
						case WILDCARD:
						case PREFIX_WILDCARD:
						case FUNCTION:
						case UNARY_MINUS:
						case UNARY_PLUS:
						case VARIABLE_REF:
						case ELEMENT:
						case TEXT:
						case BEFORE:
						case AFTER:
						case ATTRIBUTE_TEST:
						case COMP_ELEM_CONSTRUCTOR:
						case COMP_ATTR_CONSTRUCTOR:
						case COMP_TEXT_CONSTRUCTOR:
						case COMP_COMMENT_CONSTRUCTOR:
						case COMP_PI_CONSTRUCTOR:
						case COMP_DOC_CONSTRUCTOR:
						case NCNAME:
						case EQ:
						case STRING_LITERAL:
						case LITERAL_element:
						case LCURLY:
						case COMMA:
						case STAR:
						case PLUS:
						case LITERAL_some:
						case LITERAL_every:
						case LITERAL_if:
						case LITERAL_return:
						case LITERAL_or:
						case LITERAL_and:
						case LITERAL_instance:
						case LITERAL_castable:
						case LITERAL_cast:
						case LT:
						case GT:
						case LITERAL_eq:
						case LITERAL_ne:
						case LITERAL_lt:
						case LITERAL_le:
						case LITERAL_gt:
						case LITERAL_ge:
						case NEQ:
						case GTEQ:
						case LTEQ:
						case LITERAL_is:
						case LITERAL_isnot:
						case ANDEQ:
						case OREQ:
						case LITERAL_to:
						case MINUS:
						case LITERAL_div:
						case LITERAL_idiv:
						case LITERAL_mod:
						case UNION:
						case LITERAL_intersect:
						case LITERAL_except:
						case SLASH:
						case DSLASH:
						case LITERAL_text:
						case LITERAL_node:
						case LITERAL_attribute:
						case LITERAL_comment:
						case 136:
						case SELF:
						case XML_COMMENT:
						case XML_PI:
						case AT:
						case PARENT:
						case LITERAL_child:
						case LITERAL_self:
						case LITERAL_descendant:
						case 148:
						case 149:
						case LITERAL_following:
						case LITERAL_parent:
						case LITERAL_ancestor:
						case 153:
						case 154:
						case DOUBLE_LITERAL:
						case DECIMAL_LITERAL:
						case INTEGER_LITERAL:
						case XML_CDATA:
						case LITERAL_preceding:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(_t);
						}
						}
						}
					}
					else {
						if ( _cnt96>=1 ) { break _loop96; } else {throw new NoViableAltException(_t);}
					}
					
					_cnt96++;
				} while (true);
				}
				_t = __t89;
				_t = _t.getNextSibling();
				break;
			}
			case QNAME:
			case PARENTHESIZED:
			case ABSOLUTE_SLASH:
			case ABSOLUTE_DSLASH:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case UNARY_MINUS:
			case UNARY_PLUS:
			case VARIABLE_REF:
			case ELEMENT:
			case TEXT:
			case BEFORE:
			case AFTER:
			case ATTRIBUTE_TEST:
			case COMP_ELEM_CONSTRUCTOR:
			case COMP_ATTR_CONSTRUCTOR:
			case COMP_TEXT_CONSTRUCTOR:
			case COMP_COMMENT_CONSTRUCTOR:
			case COMP_PI_CONSTRUCTOR:
			case COMP_DOC_CONSTRUCTOR:
			case NCNAME:
			case EQ:
			case STRING_LITERAL:
			case LITERAL_element:
			case LCURLY:
			case COMMA:
			case STAR:
			case PLUS:
			case LITERAL_some:
			case LITERAL_every:
			case LITERAL_if:
			case LITERAL_return:
			case LITERAL_or:
			case LITERAL_and:
			case LITERAL_instance:
			case LITERAL_castable:
			case LITERAL_cast:
			case LT:
			case GT:
			case LITERAL_eq:
			case LITERAL_ne:
			case LITERAL_lt:
			case LITERAL_le:
			case LITERAL_gt:
			case LITERAL_ge:
			case NEQ:
			case GTEQ:
			case LTEQ:
			case LITERAL_is:
			case LITERAL_isnot:
			case ANDEQ:
			case OREQ:
			case LITERAL_to:
			case MINUS:
			case LITERAL_div:
			case LITERAL_idiv:
			case LITERAL_mod:
			case UNION:
			case LITERAL_intersect:
			case LITERAL_except:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case LITERAL_attribute:
			case LITERAL_comment:
			case 136:
			case SELF:
			case XML_COMMENT:
			case XML_PI:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_descendant:
			case 148:
			case 149:
			case LITERAL_following:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 153:
			case 154:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			case XML_CDATA:
			case LITERAL_preceding:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			step=expr(_t,(PathExpr) action);
			_t = _retTree;
			
						for (int i= clauses.size() - 1; i >= 0; i--) {
							ForLetClause clause= (ForLetClause) clauses.get(i);
							BindingExpression expr;
							if (clause.isForClause)
								expr= new ForExpr(context);
							else
								expr= new LetExpr(context);
							expr.setASTNode(clause.ast);
							expr.setVariable(clause.varName);
							expr.setSequenceType(clause.sequenceType);
							expr.setInputSequence(clause.inputSequence);
							expr.setReturnExpression(action);
							if (clause.isForClause)
								 ((ForExpr) expr).setPositionalVariable(clause.posVar);
							if (whereExpr != null) {
								expr.setWhereExpression(whereExpr);
								whereExpr= null;
							}
							action= expr;
						}
						if (orderBy != null) {
							OrderSpec orderSpecs[]= new OrderSpec[orderBy.size()];
							int k= 0;
							for (Iterator j= orderBy.iterator(); j.hasNext(); k++) {
								OrderSpec orderSpec= (OrderSpec) j.next();
								orderSpecs[k]= orderSpec;
							}
							((BindingExpression)action).setOrderSpecs(orderSpecs);
						}
						path.add(action);
						step = action;
					
			_t = __t71;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_instance:
		{
			AST __t97 = _t;
			org.exist.xquery.parser.XQueryAST tmp16_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_instance);
			_t = _t.getFirstChild();
			
						PathExpr expr = new PathExpr(context);
						SequenceType type= new SequenceType(); 
					
			step=expr(_t,expr);
			_t = _retTree;
			sequenceType(_t,type);
			_t = _retTree;
			
						step = new InstanceOfExpression(context, expr, type); 
						path.add(step);
					
			_t = __t97;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_or:
		{
			AST __t98 = _t;
			org.exist.xquery.parser.XQueryAST tmp17_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_or);
			_t = _t.getFirstChild();
			
						PathExpr left= new PathExpr(context);
						PathExpr right= new PathExpr(context);
					
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			_t = __t98;
			_t = _t.getNextSibling();
			
					OpOr or= new OpOr(context);
					or.addPath(left);
					or.addPath(right);
					path.addPath(or);
					step = or;
				
			break;
		}
		case LITERAL_and:
		{
			AST __t99 = _t;
			org.exist.xquery.parser.XQueryAST tmp18_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_and);
			_t = _t.getFirstChild();
			
						PathExpr left= new PathExpr(context);
						PathExpr right= new PathExpr(context);
					
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			_t = __t99;
			_t = _t.getNextSibling();
			
					OpAnd and= new OpAnd(context);
					and.addPath(left);
					and.addPath(right);
					path.addPath(and);
					step = and;
				
			break;
		}
		case UNION:
		{
			AST __t100 = _t;
			org.exist.xquery.parser.XQueryAST tmp19_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,UNION);
			_t = _t.getFirstChild();
			
						PathExpr left= new PathExpr(context);
						PathExpr right= new PathExpr(context);
					
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			_t = __t100;
			_t = _t.getNextSibling();
			
					Union union= new Union(context, left, right);
					path.add(union);
					step = union;
				
			break;
		}
		case LITERAL_intersect:
		{
			AST __t101 = _t;
			org.exist.xquery.parser.XQueryAST tmp20_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_intersect);
			_t = _t.getFirstChild();
			
						PathExpr left = new PathExpr(context);
						PathExpr right = new PathExpr(context);
					
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			_t = __t101;
			_t = _t.getNextSibling();
			
					Intersection intersect = new Intersection(context, left, right);
					path.add(intersect);
					step = intersect;
				
			break;
		}
		case LITERAL_except:
		{
			AST __t102 = _t;
			org.exist.xquery.parser.XQueryAST tmp21_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_except);
			_t = _t.getFirstChild();
			
						PathExpr left = new PathExpr(context);
						PathExpr right = new PathExpr(context);
					
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			_t = __t102;
			_t = _t.getNextSibling();
			
					Except intersect = new Except(context, left, right);
					path.add(intersect);
					step = intersect;
				
			break;
		}
		case ABSOLUTE_SLASH:
		{
			AST __t103 = _t;
			org.exist.xquery.parser.XQueryAST tmp22_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,ABSOLUTE_SLASH);
			_t = _t.getFirstChild();
			
						RootNode root= new RootNode(context);
						path.add(root);
					
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			case PARENTHESIZED:
			case ABSOLUTE_SLASH:
			case ABSOLUTE_DSLASH:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case UNARY_MINUS:
			case UNARY_PLUS:
			case VARIABLE_REF:
			case ELEMENT:
			case TEXT:
			case BEFORE:
			case AFTER:
			case ATTRIBUTE_TEST:
			case COMP_ELEM_CONSTRUCTOR:
			case COMP_ATTR_CONSTRUCTOR:
			case COMP_TEXT_CONSTRUCTOR:
			case COMP_COMMENT_CONSTRUCTOR:
			case COMP_PI_CONSTRUCTOR:
			case COMP_DOC_CONSTRUCTOR:
			case NCNAME:
			case EQ:
			case STRING_LITERAL:
			case LITERAL_element:
			case LCURLY:
			case COMMA:
			case STAR:
			case PLUS:
			case LITERAL_some:
			case LITERAL_every:
			case LITERAL_if:
			case LITERAL_return:
			case LITERAL_or:
			case LITERAL_and:
			case LITERAL_instance:
			case LITERAL_castable:
			case LITERAL_cast:
			case LT:
			case GT:
			case LITERAL_eq:
			case LITERAL_ne:
			case LITERAL_lt:
			case LITERAL_le:
			case LITERAL_gt:
			case LITERAL_ge:
			case NEQ:
			case GTEQ:
			case LTEQ:
			case LITERAL_is:
			case LITERAL_isnot:
			case ANDEQ:
			case OREQ:
			case LITERAL_to:
			case MINUS:
			case LITERAL_div:
			case LITERAL_idiv:
			case LITERAL_mod:
			case UNION:
			case LITERAL_intersect:
			case LITERAL_except:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case LITERAL_attribute:
			case LITERAL_comment:
			case 136:
			case SELF:
			case XML_COMMENT:
			case XML_PI:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_descendant:
			case 148:
			case 149:
			case LITERAL_following:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 153:
			case 154:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			case XML_CDATA:
			case LITERAL_preceding:
			{
				step=expr(_t,path);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t103;
			_t = _t.getNextSibling();
			break;
		}
		case ABSOLUTE_DSLASH:
		{
			AST __t105 = _t;
			org.exist.xquery.parser.XQueryAST tmp23_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,ABSOLUTE_DSLASH);
			_t = _t.getFirstChild();
			
						RootNode root= new RootNode(context);
						path.add(root);
					
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			case PARENTHESIZED:
			case ABSOLUTE_SLASH:
			case ABSOLUTE_DSLASH:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case UNARY_MINUS:
			case UNARY_PLUS:
			case VARIABLE_REF:
			case ELEMENT:
			case TEXT:
			case BEFORE:
			case AFTER:
			case ATTRIBUTE_TEST:
			case COMP_ELEM_CONSTRUCTOR:
			case COMP_ATTR_CONSTRUCTOR:
			case COMP_TEXT_CONSTRUCTOR:
			case COMP_COMMENT_CONSTRUCTOR:
			case COMP_PI_CONSTRUCTOR:
			case COMP_DOC_CONSTRUCTOR:
			case NCNAME:
			case EQ:
			case STRING_LITERAL:
			case LITERAL_element:
			case LCURLY:
			case COMMA:
			case STAR:
			case PLUS:
			case LITERAL_some:
			case LITERAL_every:
			case LITERAL_if:
			case LITERAL_return:
			case LITERAL_or:
			case LITERAL_and:
			case LITERAL_instance:
			case LITERAL_castable:
			case LITERAL_cast:
			case LT:
			case GT:
			case LITERAL_eq:
			case LITERAL_ne:
			case LITERAL_lt:
			case LITERAL_le:
			case LITERAL_gt:
			case LITERAL_ge:
			case NEQ:
			case GTEQ:
			case LTEQ:
			case LITERAL_is:
			case LITERAL_isnot:
			case ANDEQ:
			case OREQ:
			case LITERAL_to:
			case MINUS:
			case LITERAL_div:
			case LITERAL_idiv:
			case LITERAL_mod:
			case UNION:
			case LITERAL_intersect:
			case LITERAL_except:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case LITERAL_attribute:
			case LITERAL_comment:
			case 136:
			case SELF:
			case XML_COMMENT:
			case XML_PI:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_descendant:
			case 148:
			case 149:
			case LITERAL_following:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 153:
			case 154:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			case XML_CDATA:
			case LITERAL_preceding:
			{
				step=expr(_t,path);
				_t = _retTree;
				
								if (step instanceof LocationStep) {
									LocationStep s= (LocationStep) step;
									if (s.getAxis() == Constants.ATTRIBUTE_AXIS)
										// combines descendant-or-self::node()/attribute:*
										s.setAxis(Constants.DESCENDANT_ATTRIBUTE_AXIS);
									else
										s.setAxis(Constants.DESCENDANT_SELF_AXIS);
								} else
									step.setPrimaryAxis(Constants.DESCENDANT_SELF_AXIS);
							
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t105;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_to:
		{
			AST __t107 = _t;
			org.exist.xquery.parser.XQueryAST tmp24_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_to);
			_t = _t.getFirstChild();
			
						PathExpr start= new PathExpr(context);
						PathExpr end= new PathExpr(context);
						List args= new ArrayList(2);
						args.add(start);
						args.add(end);
					
			step=expr(_t,start);
			_t = _retTree;
			step=expr(_t,end);
			_t = _retTree;
			
						RangeExpression range= new RangeExpression(context);
						range.setArguments(args);
						path.addPath(range);
						step = range;
					
			_t = __t107;
			_t = _t.getNextSibling();
			break;
		}
		case EQ:
		case LT:
		case GT:
		case NEQ:
		case GTEQ:
		case LTEQ:
		{
			step=generalComp(_t,path);
			_t = _retTree;
			break;
		}
		case LITERAL_eq:
		case LITERAL_ne:
		case LITERAL_lt:
		case LITERAL_le:
		case LITERAL_gt:
		case LITERAL_ge:
		{
			step=valueComp(_t,path);
			_t = _retTree;
			break;
		}
		case BEFORE:
		case AFTER:
		case LITERAL_is:
		case LITERAL_isnot:
		{
			step=nodeComp(_t,path);
			_t = _retTree;
			break;
		}
		case ANDEQ:
		case OREQ:
		{
			step=fulltextComp(_t,path);
			_t = _retTree;
			break;
		}
		case PARENTHESIZED:
		case FUNCTION:
		case VARIABLE_REF:
		case ELEMENT:
		case TEXT:
		case COMP_ELEM_CONSTRUCTOR:
		case COMP_ATTR_CONSTRUCTOR:
		case COMP_TEXT_CONSTRUCTOR:
		case COMP_COMMENT_CONSTRUCTOR:
		case COMP_PI_CONSTRUCTOR:
		case COMP_DOC_CONSTRUCTOR:
		case STRING_LITERAL:
		case LCURLY:
		case XML_COMMENT:
		case XML_PI:
		case DOUBLE_LITERAL:
		case DECIMAL_LITERAL:
		case INTEGER_LITERAL:
		case XML_CDATA:
		{
			step=primaryExpr(_t,path);
			_t = _retTree;
			break;
		}
		case QNAME:
		case WILDCARD:
		case PREFIX_WILDCARD:
		case ATTRIBUTE_TEST:
		case NCNAME:
		case LITERAL_element:
		case SLASH:
		case DSLASH:
		case LITERAL_text:
		case LITERAL_node:
		case LITERAL_attribute:
		case LITERAL_comment:
		case 136:
		case SELF:
		case AT:
		case PARENT:
		case LITERAL_child:
		case LITERAL_self:
		case LITERAL_descendant:
		case 148:
		case 149:
		case LITERAL_following:
		case LITERAL_parent:
		case LITERAL_ancestor:
		case 153:
		case 154:
		case LITERAL_preceding:
		{
			step=pathExpr(_t,path);
			_t = _retTree;
			break;
		}
		case UNARY_MINUS:
		case UNARY_PLUS:
		case STAR:
		case PLUS:
		case MINUS:
		case LITERAL_div:
		case LITERAL_idiv:
		case LITERAL_mod:
		{
			step=numericExpr(_t,path);
			_t = _retTree;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final void xpath(AST _t,
		PathExpr path
	) throws RecognitionException, XPathException {
		
		org.exist.xquery.parser.XQueryAST xpath_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		context.setRootExpression(path);
		
		try {      // for error handling
			module(_t,path);
			_t = _retTree;
			
					context.resolveForwardReferences();
				
		}
		catch (RecognitionException e) {
			handleException(e);
		}
		catch (EXistException e) {
			handleException(e);
		}
		catch (PermissionDeniedException e) {
			handleException(e);
		}
		_retTree = _t;
	}
	
	public final void module(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		
		org.exist.xquery.parser.XQueryAST module_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST m = null;
		org.exist.xquery.parser.XQueryAST uri = null;
		Expression step = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case MODULE_DECL:
		{
			AST __t6 = _t;
			m = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,MODULE_DECL);
			_t = _t.getFirstChild();
			uri = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,STRING_LITERAL);
			_t = _t.getNextSibling();
			
						myModule = new ExternalModuleImpl(uri.getText(), m.getText());
						context.declareNamespace(m.getText(), uri.getText());
					
			_t = __t6;
			_t = _t.getNextSibling();
			prolog(_t,path);
			_t = _retTree;
			break;
		}
		case QNAME:
		case PARENTHESIZED:
		case ABSOLUTE_SLASH:
		case ABSOLUTE_DSLASH:
		case WILDCARD:
		case PREFIX_WILDCARD:
		case FUNCTION:
		case UNARY_MINUS:
		case UNARY_PLUS:
		case VARIABLE_REF:
		case ELEMENT:
		case TEXT:
		case VERSION_DECL:
		case NAMESPACE_DECL:
		case DEF_NAMESPACE_DECL:
		case DEF_COLLATION_DECL:
		case DEF_FUNCTION_NS_DECL:
		case GLOBAL_VAR:
		case FUNCTION_DECL:
		case BEFORE:
		case AFTER:
		case ATTRIBUTE_TEST:
		case COMP_ELEM_CONSTRUCTOR:
		case COMP_ATTR_CONSTRUCTOR:
		case COMP_TEXT_CONSTRUCTOR:
		case COMP_COMMENT_CONSTRUCTOR:
		case COMP_PI_CONSTRUCTOR:
		case COMP_DOC_CONSTRUCTOR:
		case NCNAME:
		case EQ:
		case STRING_LITERAL:
		case LITERAL_import:
		case LITERAL_xmlspace:
		case LITERAL_ordering:
		case LITERAL_construction:
		case LITERAL_element:
		case LCURLY:
		case COMMA:
		case STAR:
		case PLUS:
		case LITERAL_some:
		case LITERAL_every:
		case LITERAL_if:
		case LITERAL_return:
		case LITERAL_or:
		case LITERAL_and:
		case LITERAL_instance:
		case LITERAL_castable:
		case LITERAL_cast:
		case LT:
		case GT:
		case LITERAL_eq:
		case LITERAL_ne:
		case LITERAL_lt:
		case LITERAL_le:
		case LITERAL_gt:
		case LITERAL_ge:
		case NEQ:
		case GTEQ:
		case LTEQ:
		case LITERAL_is:
		case LITERAL_isnot:
		case ANDEQ:
		case OREQ:
		case LITERAL_to:
		case MINUS:
		case LITERAL_div:
		case LITERAL_idiv:
		case LITERAL_mod:
		case UNION:
		case LITERAL_intersect:
		case LITERAL_except:
		case SLASH:
		case DSLASH:
		case LITERAL_text:
		case LITERAL_node:
		case LITERAL_attribute:
		case LITERAL_comment:
		case 136:
		case SELF:
		case XML_COMMENT:
		case XML_PI:
		case AT:
		case PARENT:
		case LITERAL_child:
		case LITERAL_self:
		case LITERAL_descendant:
		case 148:
		case 149:
		case LITERAL_following:
		case LITERAL_parent:
		case LITERAL_ancestor:
		case 153:
		case 154:
		case DOUBLE_LITERAL:
		case DECIMAL_LITERAL:
		case INTEGER_LITERAL:
		case XML_CDATA:
		case LITERAL_preceding:
		{
			prolog(_t,path);
			_t = _retTree;
			step=expr(_t,path);
			_t = _retTree;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
	}
	
/**
 * Process the XQuery prolog.
 */
	public final void prolog(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		
		org.exist.xquery.parser.XQueryAST prolog_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST v = null;
		org.exist.xquery.parser.XQueryAST prefix = null;
		org.exist.xquery.parser.XQueryAST uri = null;
		org.exist.xquery.parser.XQueryAST defu = null;
		org.exist.xquery.parser.XQueryAST deff = null;
		org.exist.xquery.parser.XQueryAST defc = null;
		org.exist.xquery.parser.XQueryAST qname = null;
		org.exist.xquery.parser.XQueryAST e = null;
		org.exist.xquery.parser.XQueryAST i = null;
		org.exist.xquery.parser.XQueryAST pfx = null;
		org.exist.xquery.parser.XQueryAST moduleURI = null;
		org.exist.xquery.parser.XQueryAST at = null;
		Expression step = null;
		
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case VERSION_DECL:
		{
			AST __t9 = _t;
			v = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,VERSION_DECL);
			_t = _t.getFirstChild();
			
							if (!v.getText().equals("1.0"))
								throw new XPathException(v, "Wrong XQuery version: require 1.0");
						
			_t = __t9;
			_t = _t.getNextSibling();
			break;
		}
		case 3:
		case QNAME:
		case PARENTHESIZED:
		case ABSOLUTE_SLASH:
		case ABSOLUTE_DSLASH:
		case WILDCARD:
		case PREFIX_WILDCARD:
		case FUNCTION:
		case UNARY_MINUS:
		case UNARY_PLUS:
		case VARIABLE_REF:
		case ELEMENT:
		case TEXT:
		case NAMESPACE_DECL:
		case DEF_NAMESPACE_DECL:
		case DEF_COLLATION_DECL:
		case DEF_FUNCTION_NS_DECL:
		case GLOBAL_VAR:
		case FUNCTION_DECL:
		case BEFORE:
		case AFTER:
		case ATTRIBUTE_TEST:
		case COMP_ELEM_CONSTRUCTOR:
		case COMP_ATTR_CONSTRUCTOR:
		case COMP_TEXT_CONSTRUCTOR:
		case COMP_COMMENT_CONSTRUCTOR:
		case COMP_PI_CONSTRUCTOR:
		case COMP_DOC_CONSTRUCTOR:
		case NCNAME:
		case EQ:
		case STRING_LITERAL:
		case LITERAL_import:
		case LITERAL_xmlspace:
		case LITERAL_ordering:
		case LITERAL_construction:
		case LITERAL_element:
		case LCURLY:
		case COMMA:
		case STAR:
		case PLUS:
		case LITERAL_some:
		case LITERAL_every:
		case LITERAL_if:
		case LITERAL_return:
		case LITERAL_or:
		case LITERAL_and:
		case LITERAL_instance:
		case LITERAL_castable:
		case LITERAL_cast:
		case LT:
		case GT:
		case LITERAL_eq:
		case LITERAL_ne:
		case LITERAL_lt:
		case LITERAL_le:
		case LITERAL_gt:
		case LITERAL_ge:
		case NEQ:
		case GTEQ:
		case LTEQ:
		case LITERAL_is:
		case LITERAL_isnot:
		case ANDEQ:
		case OREQ:
		case LITERAL_to:
		case MINUS:
		case LITERAL_div:
		case LITERAL_idiv:
		case LITERAL_mod:
		case UNION:
		case LITERAL_intersect:
		case LITERAL_except:
		case SLASH:
		case DSLASH:
		case LITERAL_text:
		case LITERAL_node:
		case LITERAL_attribute:
		case LITERAL_comment:
		case 136:
		case SELF:
		case XML_COMMENT:
		case XML_PI:
		case AT:
		case PARENT:
		case LITERAL_child:
		case LITERAL_self:
		case LITERAL_descendant:
		case 148:
		case 149:
		case LITERAL_following:
		case LITERAL_parent:
		case LITERAL_ancestor:
		case 153:
		case 154:
		case DOUBLE_LITERAL:
		case DECIMAL_LITERAL:
		case INTEGER_LITERAL:
		case XML_CDATA:
		case LITERAL_preceding:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		{
		_loop27:
		do {
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NAMESPACE_DECL:
			{
				AST __t11 = _t;
				prefix = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
				match(_t,NAMESPACE_DECL);
				_t = _t.getFirstChild();
				uri = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,STRING_LITERAL);
				_t = _t.getNextSibling();
				context.declareNamespace(prefix.getText(), uri.getText());
				_t = __t11;
				_t = _t.getNextSibling();
				break;
			}
			case LITERAL_xmlspace:
			{
				AST __t12 = _t;
				org.exist.xquery.parser.XQueryAST tmp25_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,LITERAL_xmlspace);
				_t = _t.getFirstChild();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LITERAL_preserve:
				{
					org.exist.xquery.parser.XQueryAST tmp26_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,LITERAL_preserve);
					_t = _t.getNextSibling();
					context.setStripWhitespace(false);
					break;
				}
				case LITERAL_strip:
				{
					org.exist.xquery.parser.XQueryAST tmp27_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,LITERAL_strip);
					_t = _t.getNextSibling();
					context.setStripWhitespace(true);
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				_t = __t12;
				_t = _t.getNextSibling();
				break;
			}
			case LITERAL_ordering:
			{
				AST __t14 = _t;
				org.exist.xquery.parser.XQueryAST tmp28_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,LITERAL_ordering);
				_t = _t.getFirstChild();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LITERAL_ordered:
				{
					org.exist.xquery.parser.XQueryAST tmp29_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,LITERAL_ordered);
					_t = _t.getNextSibling();
					break;
				}
				case LITERAL_unordered:
				{
					org.exist.xquery.parser.XQueryAST tmp30_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,LITERAL_unordered);
					_t = _t.getNextSibling();
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				_t = __t14;
				_t = _t.getNextSibling();
				break;
			}
			case LITERAL_construction:
			{
				AST __t16 = _t;
				org.exist.xquery.parser.XQueryAST tmp31_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,LITERAL_construction);
				_t = _t.getFirstChild();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LITERAL_preserve:
				{
					org.exist.xquery.parser.XQueryAST tmp32_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,LITERAL_preserve);
					_t = _t.getNextSibling();
					break;
				}
				case LITERAL_strip:
				{
					org.exist.xquery.parser.XQueryAST tmp33_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,LITERAL_strip);
					_t = _t.getNextSibling();
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				_t = __t16;
				_t = _t.getNextSibling();
				break;
			}
			case DEF_NAMESPACE_DECL:
			{
				AST __t18 = _t;
				org.exist.xquery.parser.XQueryAST tmp34_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,DEF_NAMESPACE_DECL);
				_t = _t.getFirstChild();
				defu = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,STRING_LITERAL);
				_t = _t.getNextSibling();
				context.declareNamespace("", defu.getText());
				_t = __t18;
				_t = _t.getNextSibling();
				break;
			}
			case DEF_FUNCTION_NS_DECL:
			{
				AST __t19 = _t;
				org.exist.xquery.parser.XQueryAST tmp35_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,DEF_FUNCTION_NS_DECL);
				_t = _t.getFirstChild();
				deff = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,STRING_LITERAL);
				_t = _t.getNextSibling();
				context.setDefaultFunctionNamespace(deff.getText());
				_t = __t19;
				_t = _t.getNextSibling();
				break;
			}
			case DEF_COLLATION_DECL:
			{
				AST __t20 = _t;
				org.exist.xquery.parser.XQueryAST tmp36_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,DEF_COLLATION_DECL);
				_t = _t.getFirstChild();
				defc = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,STRING_LITERAL);
				_t = _t.getNextSibling();
				context.setDefaultCollation(defc.getText());
				_t = __t20;
				_t = _t.getNextSibling();
				break;
			}
			case GLOBAL_VAR:
			{
				AST __t21 = _t;
				qname = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
				match(_t,GLOBAL_VAR);
				_t = _t.getFirstChild();
				
								PathExpr enclosed= new PathExpr(context);
								SequenceType type= null;
							
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LITERAL_as:
				{
					AST __t23 = _t;
					org.exist.xquery.parser.XQueryAST tmp37_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,LITERAL_as);
					_t = _t.getFirstChild();
					type= new SequenceType();
					sequenceType(_t,type);
					_t = _retTree;
					_t = __t23;
					_t = _t.getNextSibling();
					break;
				}
				case QNAME:
				case PARENTHESIZED:
				case ABSOLUTE_SLASH:
				case ABSOLUTE_DSLASH:
				case WILDCARD:
				case PREFIX_WILDCARD:
				case FUNCTION:
				case UNARY_MINUS:
				case UNARY_PLUS:
				case VARIABLE_REF:
				case ELEMENT:
				case TEXT:
				case BEFORE:
				case AFTER:
				case ATTRIBUTE_TEST:
				case COMP_ELEM_CONSTRUCTOR:
				case COMP_ATTR_CONSTRUCTOR:
				case COMP_TEXT_CONSTRUCTOR:
				case COMP_COMMENT_CONSTRUCTOR:
				case COMP_PI_CONSTRUCTOR:
				case COMP_DOC_CONSTRUCTOR:
				case NCNAME:
				case EQ:
				case STRING_LITERAL:
				case LITERAL_element:
				case LCURLY:
				case COMMA:
				case STAR:
				case PLUS:
				case LITERAL_some:
				case LITERAL_every:
				case LITERAL_if:
				case LITERAL_return:
				case LITERAL_or:
				case LITERAL_and:
				case LITERAL_instance:
				case LITERAL_castable:
				case LITERAL_cast:
				case LT:
				case GT:
				case LITERAL_eq:
				case LITERAL_ne:
				case LITERAL_lt:
				case LITERAL_le:
				case LITERAL_gt:
				case LITERAL_ge:
				case NEQ:
				case GTEQ:
				case LTEQ:
				case LITERAL_is:
				case LITERAL_isnot:
				case ANDEQ:
				case OREQ:
				case LITERAL_to:
				case MINUS:
				case LITERAL_div:
				case LITERAL_idiv:
				case LITERAL_mod:
				case UNION:
				case LITERAL_intersect:
				case LITERAL_except:
				case SLASH:
				case DSLASH:
				case LITERAL_text:
				case LITERAL_node:
				case LITERAL_attribute:
				case LITERAL_comment:
				case 136:
				case SELF:
				case XML_COMMENT:
				case XML_PI:
				case AT:
				case PARENT:
				case LITERAL_child:
				case LITERAL_self:
				case LITERAL_descendant:
				case 148:
				case 149:
				case LITERAL_following:
				case LITERAL_parent:
				case LITERAL_ancestor:
				case 153:
				case 154:
				case DOUBLE_LITERAL:
				case DECIMAL_LITERAL:
				case INTEGER_LITERAL:
				case XML_CDATA:
				case LITERAL_preceding:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				e = _t==ASTNULL ? null : (org.exist.xquery.parser.XQueryAST)_t;
				step=expr(_t,enclosed);
				_t = _retTree;
				
								VariableDeclaration decl= new VariableDeclaration(context, qname.getText(), enclosed);
								decl.setSequenceType(type);
								decl.setASTNode(e);
								path.add(decl);
								if(myModule != null) {
									QName qn = QName.parse(context, qname.getText());
									myModule.declareVariable(qn, decl);
								}
							
				_t = __t21;
				_t = _t.getNextSibling();
				break;
			}
			case FUNCTION_DECL:
			{
				functionDecl(_t,path);
				_t = _retTree;
				break;
			}
			case LITERAL_import:
			{
				AST __t24 = _t;
				i = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
				match(_t,LITERAL_import);
				_t = _t.getFirstChild();
				
								String modulePrefix = null;
								String location = null;
							
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case NCNAME:
				{
					pfx = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,NCNAME);
					_t = _t.getNextSibling();
					modulePrefix = pfx.getText();
					break;
				}
				case STRING_LITERAL:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				moduleURI = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,STRING_LITERAL);
				_t = _t.getNextSibling();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case STRING_LITERAL:
				{
					at = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,STRING_LITERAL);
					_t = _t.getNextSibling();
					location = at.getText();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				
						                try {
									context.importModule(moduleURI.getText(), modulePrefix, location);
						                } catch(XPathException xpe) {
						                    xpe.prependMessage("error found while loading module " + modulePrefix + ": ");
						                    throw xpe;
						                }
							
				_t = __t24;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				break _loop27;
			}
			}
		} while (true);
		}
		_retTree = _t;
	}
	
/**
 * A sequence type declaration.
 */
	public final void sequenceType(AST _t,
		SequenceType type
	) throws RecognitionException, XPathException {
		
		org.exist.xquery.parser.XQueryAST sequenceType_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST t = null;
		org.exist.xquery.parser.XQueryAST qn1 = null;
		org.exist.xquery.parser.XQueryAST qn2 = null;
		
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ATOMIC_TYPE:
		{
			AST __t43 = _t;
			t = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,ATOMIC_TYPE);
			_t = _t.getFirstChild();
			
							QName qn= QName.parse(context, t.getText());
							int code= Type.getType(qn);
							if(!Type.subTypeOf(code, Type.ATOMIC))
								throw new XPathException(t, "Type " + qn.toString() + " is not an atomic type");
							type.setPrimaryType(code);
						
			_t = __t43;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_empty:
		{
			AST __t44 = _t;
			org.exist.xquery.parser.XQueryAST tmp38_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_empty);
			_t = _t.getFirstChild();
			
							type.setPrimaryType(Type.EMPTY);
							type.setCardinality(Cardinality.EMPTY);
						
			_t = __t44;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_item:
		{
			AST __t45 = _t;
			org.exist.xquery.parser.XQueryAST tmp39_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_item);
			_t = _t.getFirstChild();
			type.setPrimaryType(Type.ITEM);
			_t = __t45;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_node:
		{
			AST __t46 = _t;
			org.exist.xquery.parser.XQueryAST tmp40_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_node);
			_t = _t.getFirstChild();
			type.setPrimaryType(Type.NODE);
			_t = __t46;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_element:
		{
			AST __t47 = _t;
			org.exist.xquery.parser.XQueryAST tmp41_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_element);
			_t = _t.getFirstChild();
			type.setPrimaryType(Type.ELEMENT);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WILDCARD:
			{
				org.exist.xquery.parser.XQueryAST tmp42_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,WILDCARD);
				_t = _t.getNextSibling();
				break;
			}
			case QNAME:
			{
				qn1 = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,QNAME);
				_t = _t.getNextSibling();
				
									throwException(qn1, 
										"Tests of the form element(QName) are not yet supported within a sequence type");
								
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t47;
			_t = _t.getNextSibling();
			break;
		}
		case ATTRIBUTE_TEST:
		{
			AST __t49 = _t;
			org.exist.xquery.parser.XQueryAST tmp43_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,ATTRIBUTE_TEST);
			_t = _t.getFirstChild();
			type.setPrimaryType(Type.ATTRIBUTE);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WILDCARD:
			{
				org.exist.xquery.parser.XQueryAST tmp44_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,WILDCARD);
				_t = _t.getNextSibling();
				break;
			}
			case QNAME:
			{
				qn2 = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,QNAME);
				_t = _t.getNextSibling();
				
									throwException(qn2, 
										"Tests of the form attribute(QName) are not yet supported within a sequence type");
								
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t49;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_text:
		{
			AST __t51 = _t;
			org.exist.xquery.parser.XQueryAST tmp45_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_text);
			_t = _t.getFirstChild();
			type.setPrimaryType(Type.ITEM);
			_t = __t51;
			_t = _t.getNextSibling();
			break;
		}
		case 135:
		{
			AST __t52 = _t;
			org.exist.xquery.parser.XQueryAST tmp46_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,135);
			_t = _t.getFirstChild();
			type.setPrimaryType(Type.PROCESSING_INSTRUCTION);
			_t = __t52;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_comment:
		{
			AST __t53 = _t;
			org.exist.xquery.parser.XQueryAST tmp47_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_comment);
			_t = _t.getFirstChild();
			type.setPrimaryType(Type.COMMENT);
			_t = __t53;
			_t = _t.getNextSibling();
			break;
		}
		case 136:
		{
			AST __t54 = _t;
			org.exist.xquery.parser.XQueryAST tmp48_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,136);
			_t = _t.getFirstChild();
			type.setPrimaryType(Type.DOCUMENT);
			_t = __t54;
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case STAR:
		{
			org.exist.xquery.parser.XQueryAST tmp49_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,STAR);
			_t = _t.getNextSibling();
			type.setCardinality(Cardinality.ZERO_OR_MORE);
			break;
		}
		case PLUS:
		{
			org.exist.xquery.parser.XQueryAST tmp50_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,PLUS);
			_t = _t.getNextSibling();
			type.setCardinality(Cardinality.ONE_OR_MORE);
			break;
		}
		case QUESTION:
		{
			org.exist.xquery.parser.XQueryAST tmp51_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,QUESTION);
			_t = _t.getNextSibling();
			type.setCardinality(Cardinality.ZERO_OR_ONE);
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		_retTree = _t;
	}
	
/**
 * Parse a declared function.
 */
	public final void functionDecl(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		
		org.exist.xquery.parser.XQueryAST functionDecl_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST name = null;
		Expression step = null;
		
		AST __t29 = _t;
		name = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
		match(_t,FUNCTION_DECL);
		_t = _t.getFirstChild();
		PathExpr body= new PathExpr(context);
		
					QName qn= null;
					try {
						qn = QName.parse(context, name.getText());
					} catch(XPathException e) {
						// throw exception with correct source location
						e.setASTNode(name);
						throw e;
					}
					FunctionSignature signature= new FunctionSignature(qn);
					UserDefinedFunction func= new UserDefinedFunction(context, signature);
					func.setASTNode(name);
					List varList= new ArrayList(3);
				
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case VARIABLE_BINDING:
		{
			paramList(_t,varList);
			_t = _retTree;
			break;
		}
		case LCURLY:
		case LITERAL_as:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		
					SequenceType[] types= new SequenceType[varList.size()];
					int j= 0;
					for (Iterator i= varList.iterator(); i.hasNext(); j++) {
						FunctionParameter param= (FunctionParameter) i.next();
						types[j]= param.type;
						func.addVariable(param.varName);
					}
					signature.setArgumentTypes(types);
					context.declareFunction(func);
					if(myModule != null)
						myModule.declareFunction(func);
				
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		{
			AST __t32 = _t;
			org.exist.xquery.parser.XQueryAST tmp52_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_as);
			_t = _t.getFirstChild();
			SequenceType type= new SequenceType();
			sequenceType(_t,type);
			_t = _retTree;
			signature.setReturnType(type);
			_t = __t32;
			_t = _t.getNextSibling();
			break;
		}
		case LCURLY:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		AST __t33 = _t;
		org.exist.xquery.parser.XQueryAST tmp53_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
		match(_t,LCURLY);
		_t = _t.getFirstChild();
		step=expr(_t,body);
		_t = _retTree;
		func.setFunctionBody(body);
		_t = __t33;
		_t = _t.getNextSibling();
		_t = __t29;
		_t = _t.getNextSibling();
		_retTree = _t;
	}
	
/**
 * Parse params in function declaration.
 */
	public final void paramList(AST _t,
		List vars
	) throws RecognitionException, XPathException {
		
		org.exist.xquery.parser.XQueryAST paramList_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		
		param(_t,vars);
		_t = _retTree;
		{
		_loop36:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==VARIABLE_BINDING)) {
				param(_t,vars);
				_t = _retTree;
			}
			else {
				break _loop36;
			}
			
		} while (true);
		}
		_retTree = _t;
	}
	
/**
 * Single function param.
 */
	public final void param(AST _t,
		List vars
	) throws RecognitionException, XPathException {
		
		org.exist.xquery.parser.XQueryAST param_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST varname = null;
		
		AST __t38 = _t;
		varname = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
		match(_t,VARIABLE_BINDING);
		_t = _t.getFirstChild();
		
					FunctionParameter var= new FunctionParameter(varname.getText());
					vars.add(var);
				
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_as:
		{
			AST __t40 = _t;
			org.exist.xquery.parser.XQueryAST tmp54_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_as);
			_t = _t.getFirstChild();
			SequenceType type= new SequenceType();
			sequenceType(_t,type);
			_t = _retTree;
			_t = __t40;
			_t = _t.getNextSibling();
			var.type= type;
			break;
		}
		case 3:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		_t = __t38;
		_t = _t.getNextSibling();
		_retTree = _t;
	}
	
	public final Expression  typeCastExpr(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		org.exist.xquery.parser.XQueryAST typeCastExpr_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST castAST = null;
		org.exist.xquery.parser.XQueryAST t = null;
		org.exist.xquery.parser.XQueryAST castableAST = null;
		org.exist.xquery.parser.XQueryAST t2 = null;
		
			step= null;
			PathExpr expr= new PathExpr(context);
			int cardinality= Cardinality.EXACTLY_ONE;
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_cast:
		{
			AST __t204 = _t;
			castAST = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_cast);
			_t = _t.getFirstChild();
			step=expr(_t,expr);
			_t = _retTree;
			t = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,ATOMIC_TYPE);
			_t = _t.getNextSibling();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QUESTION:
			{
				org.exist.xquery.parser.XQueryAST tmp55_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,QUESTION);
				_t = _t.getNextSibling();
				cardinality= Cardinality.ZERO_OR_ONE;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			
						QName qn= QName.parse(context, t.getText());
						int code= Type.getType(qn);
						CastExpression castExpr= new CastExpression(context, expr, code, cardinality);
						castExpr.setASTNode(castAST);
						path.add(castExpr);
						step = castExpr;
					
			_t = __t204;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_castable:
		{
			AST __t206 = _t;
			castableAST = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_castable);
			_t = _t.getFirstChild();
			step=expr(_t,expr);
			_t = _retTree;
			t2 = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,ATOMIC_TYPE);
			_t = _t.getNextSibling();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QUESTION:
			{
				org.exist.xquery.parser.XQueryAST tmp56_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,QUESTION);
				_t = _t.getNextSibling();
				cardinality= Cardinality.ZERO_OR_ONE;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			
						QName qn= QName.parse(context, t2.getText());
						int code= Type.getType(qn);
						CastableExpression castExpr= new CastableExpression(context, expr, code, cardinality);
						castExpr.setASTNode(castAST);
						path.add(castExpr);
						step = castExpr;
					
			_t = __t206;
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  generalComp(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		org.exist.xquery.parser.XQueryAST generalComp_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST eq = null;
		org.exist.xquery.parser.XQueryAST neq = null;
		org.exist.xquery.parser.XQueryAST lt = null;
		org.exist.xquery.parser.XQueryAST lteq = null;
		org.exist.xquery.parser.XQueryAST gt = null;
		org.exist.xquery.parser.XQueryAST gteq = null;
		
			step= null;
			PathExpr left= new PathExpr(context);
			PathExpr right= new PathExpr(context);
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case EQ:
		{
			AST __t168 = _t;
			eq = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,EQ);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step= new GeneralComparison(context, left, right, Constants.EQ);
			step.setASTNode(eq);
						path.add(step);
					
			_t = __t168;
			_t = _t.getNextSibling();
			break;
		}
		case NEQ:
		{
			AST __t169 = _t;
			neq = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,NEQ);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step= new GeneralComparison(context, left, right, Constants.NEQ);
			step.setASTNode(neq);
						path.add(step);
					
			_t = __t169;
			_t = _t.getNextSibling();
			break;
		}
		case LT:
		{
			AST __t170 = _t;
			lt = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LT);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step= new GeneralComparison(context, left, right, Constants.LT);
			step.setASTNode(lt);
						path.add(step);
					
			_t = __t170;
			_t = _t.getNextSibling();
			break;
		}
		case LTEQ:
		{
			AST __t171 = _t;
			lteq = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LTEQ);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step= new GeneralComparison(context, left, right, Constants.LTEQ);
			step.setASTNode(lteq);
						path.add(step);
					
			_t = __t171;
			_t = _t.getNextSibling();
			break;
		}
		case GT:
		{
			AST __t172 = _t;
			gt = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,GT);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step= new GeneralComparison(context, left, right, Constants.GT);
			step.setASTNode(gt);
						path.add(step);
					
			_t = __t172;
			_t = _t.getNextSibling();
			break;
		}
		case GTEQ:
		{
			AST __t173 = _t;
			gteq = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,GTEQ);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step= new GeneralComparison(context, left, right, Constants.GTEQ);
			step.setASTNode(gteq);
						path.add(step);
					
			_t = __t173;
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  valueComp(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		org.exist.xquery.parser.XQueryAST valueComp_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST eq = null;
		org.exist.xquery.parser.XQueryAST ne = null;
		org.exist.xquery.parser.XQueryAST lt = null;
		org.exist.xquery.parser.XQueryAST le = null;
		org.exist.xquery.parser.XQueryAST gt = null;
		org.exist.xquery.parser.XQueryAST ge = null;
		
			step= null;
			PathExpr left= new PathExpr(context);
			PathExpr right= new PathExpr(context);
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_eq:
		{
			AST __t161 = _t;
			eq = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_eq);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step= new ValueComparison(context, left, right, Constants.EQ);
			step.setASTNode(eq);
						path.add(step);
					
			_t = __t161;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_ne:
		{
			AST __t162 = _t;
			ne = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_ne);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step= new ValueComparison(context, left, right, Constants.NEQ);
			step.setASTNode(ne);
						path.add(step);
					
			_t = __t162;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_lt:
		{
			AST __t163 = _t;
			lt = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_lt);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step= new ValueComparison(context, left, right, Constants.LT);
			step.setASTNode(lt);
						path.add(step);
					
			_t = __t163;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_le:
		{
			AST __t164 = _t;
			le = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_le);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step= new ValueComparison(context, left, right, Constants.LTEQ);
			step.setASTNode(le);
						path.add(step);
					
			_t = __t164;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_gt:
		{
			AST __t165 = _t;
			gt = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_gt);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step= new ValueComparison(context, left, right, Constants.GT);
			step.setASTNode(gt);
						path.add(step);
					
			_t = __t165;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_ge:
		{
			AST __t166 = _t;
			ge = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_ge);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step= new ValueComparison(context, left, right, Constants.GTEQ);
			step.setASTNode(ge);
						path.add(step);
					
			_t = __t166;
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  nodeComp(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		org.exist.xquery.parser.XQueryAST nodeComp_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST is = null;
		org.exist.xquery.parser.XQueryAST isnot = null;
		org.exist.xquery.parser.XQueryAST before = null;
		org.exist.xquery.parser.XQueryAST after = null;
		
			step= null;
			PathExpr left= new PathExpr(context);
			PathExpr right= new PathExpr(context);
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_is:
		{
			AST __t175 = _t;
			is = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_is);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step = new NodeComparison(context, left, right, Constants.IS);
			step.setASTNode(is);
						path.add(step);
					
			_t = __t175;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_isnot:
		{
			AST __t176 = _t;
			isnot = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_isnot);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step = new NodeComparison(context, left, right, Constants.ISNOT);
			step.setASTNode(isnot);
						path.add(step);
					
			_t = __t176;
			_t = _t.getNextSibling();
			break;
		}
		case BEFORE:
		{
			AST __t177 = _t;
			before = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,BEFORE);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step = new NodeComparison(context, left, right, Constants.BEFORE);
			step.setASTNode(before);
						path.add(step);
					
			_t = __t177;
			_t = _t.getNextSibling();
			break;
		}
		case AFTER:
		{
			AST __t178 = _t;
			after = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,AFTER);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			
						step = new NodeComparison(context, left, right, Constants.AFTER);
			step.setASTNode(after);
						path.add(step);
					
			_t = __t178;
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  fulltextComp(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		org.exist.xquery.parser.XQueryAST fulltextComp_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		
			step= null;
			PathExpr nodes= new PathExpr(context);
			PathExpr query= new PathExpr(context);
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ANDEQ:
		{
			AST __t158 = _t;
			org.exist.xquery.parser.XQueryAST tmp57_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,ANDEQ);
			_t = _t.getFirstChild();
			step=expr(_t,nodes);
			_t = _retTree;
			step=expr(_t,query);
			_t = _retTree;
			_t = __t158;
			_t = _t.getNextSibling();
			
					ExtFulltext exprCont= new ExtFulltext(context, Constants.FULLTEXT_AND);
					exprCont.setPath(nodes);
					exprCont.addTerm(query);
					path.addPath(exprCont);
				
			break;
		}
		case OREQ:
		{
			AST __t159 = _t;
			org.exist.xquery.parser.XQueryAST tmp58_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,OREQ);
			_t = _t.getFirstChild();
			step=expr(_t,nodes);
			_t = _retTree;
			step=expr(_t,query);
			_t = _retTree;
			_t = __t159;
			_t = _t.getNextSibling();
			
					ExtFulltext exprCont= new ExtFulltext(context, Constants.FULLTEXT_OR);
					exprCont.setPath(nodes);
					exprCont.addTerm(query);
					path.addPath(exprCont);
				
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
/**
 * Process a primary expression like function calls,
 * variable references, value constructors etc.
 */
	public final Expression  primaryExpr(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		org.exist.xquery.parser.XQueryAST primaryExpr_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST v = null;
		
			step = null;
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ELEMENT:
		case TEXT:
		case COMP_ELEM_CONSTRUCTOR:
		case COMP_ATTR_CONSTRUCTOR:
		case COMP_TEXT_CONSTRUCTOR:
		case COMP_COMMENT_CONSTRUCTOR:
		case COMP_PI_CONSTRUCTOR:
		case COMP_DOC_CONSTRUCTOR:
		case LCURLY:
		case XML_COMMENT:
		case XML_PI:
		case XML_CDATA:
		{
			step=constructor(_t,path);
			_t = _retTree;
			step=predicates(_t,step);
			_t = _retTree;
			
					path.add(step);
				
			break;
		}
		case PARENTHESIZED:
		{
			AST __t109 = _t;
			org.exist.xquery.parser.XQueryAST tmp59_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,PARENTHESIZED);
			_t = _t.getFirstChild();
			PathExpr pathExpr= new PathExpr(context);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			case PARENTHESIZED:
			case ABSOLUTE_SLASH:
			case ABSOLUTE_DSLASH:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case UNARY_MINUS:
			case UNARY_PLUS:
			case VARIABLE_REF:
			case ELEMENT:
			case TEXT:
			case BEFORE:
			case AFTER:
			case ATTRIBUTE_TEST:
			case COMP_ELEM_CONSTRUCTOR:
			case COMP_ATTR_CONSTRUCTOR:
			case COMP_TEXT_CONSTRUCTOR:
			case COMP_COMMENT_CONSTRUCTOR:
			case COMP_PI_CONSTRUCTOR:
			case COMP_DOC_CONSTRUCTOR:
			case NCNAME:
			case EQ:
			case STRING_LITERAL:
			case LITERAL_element:
			case LCURLY:
			case COMMA:
			case STAR:
			case PLUS:
			case LITERAL_some:
			case LITERAL_every:
			case LITERAL_if:
			case LITERAL_return:
			case LITERAL_or:
			case LITERAL_and:
			case LITERAL_instance:
			case LITERAL_castable:
			case LITERAL_cast:
			case LT:
			case GT:
			case LITERAL_eq:
			case LITERAL_ne:
			case LITERAL_lt:
			case LITERAL_le:
			case LITERAL_gt:
			case LITERAL_ge:
			case NEQ:
			case GTEQ:
			case LTEQ:
			case LITERAL_is:
			case LITERAL_isnot:
			case ANDEQ:
			case OREQ:
			case LITERAL_to:
			case MINUS:
			case LITERAL_div:
			case LITERAL_idiv:
			case LITERAL_mod:
			case UNION:
			case LITERAL_intersect:
			case LITERAL_except:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case LITERAL_attribute:
			case LITERAL_comment:
			case 136:
			case SELF:
			case XML_COMMENT:
			case XML_PI:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_descendant:
			case 148:
			case 149:
			case LITERAL_following:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 153:
			case 154:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			case XML_CDATA:
			case LITERAL_preceding:
			{
				step=expr(_t,pathExpr);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t109;
			_t = _t.getNextSibling();
			step=predicates(_t,pathExpr);
			_t = _retTree;
			path.add(step);
			break;
		}
		case STRING_LITERAL:
		case DOUBLE_LITERAL:
		case DECIMAL_LITERAL:
		case INTEGER_LITERAL:
		{
			step=literalExpr(_t,path);
			_t = _retTree;
			step=predicates(_t,step);
			_t = _retTree;
			path.add(step);
			break;
		}
		case VARIABLE_REF:
		{
			v = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,VARIABLE_REF);
			_t = _t.getNextSibling();
			
			step= new VariableReference(context, v.getText());
			step.setASTNode(v);
			
			step=predicates(_t,step);
			_t = _retTree;
			path.add(step);
			break;
		}
		case FUNCTION:
		{
			step=functionCall(_t,path);
			_t = _retTree;
			step=predicates(_t,step);
			_t = _retTree;
			path.add(step);
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  pathExpr(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		org.exist.xquery.parser.XQueryAST pathExpr_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST qn = null;
		org.exist.xquery.parser.XQueryAST nc1 = null;
		org.exist.xquery.parser.XQueryAST nc = null;
		org.exist.xquery.parser.XQueryAST n = null;
		org.exist.xquery.parser.XQueryAST qn2 = null;
		org.exist.xquery.parser.XQueryAST qn3 = null;
		org.exist.xquery.parser.XQueryAST attr = null;
		org.exist.xquery.parser.XQueryAST nc2 = null;
		org.exist.xquery.parser.XQueryAST nc3 = null;
		
			Expression rightStep= null;
			step= null;
			int axis= Constants.CHILD_AXIS;
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case QNAME:
		case WILDCARD:
		case PREFIX_WILDCARD:
		case ATTRIBUTE_TEST:
		case NCNAME:
		case LITERAL_element:
		case LITERAL_text:
		case LITERAL_node:
		case LITERAL_attribute:
		case LITERAL_comment:
		case 136:
		case LITERAL_child:
		case LITERAL_self:
		case LITERAL_descendant:
		case 148:
		case 149:
		case LITERAL_following:
		case LITERAL_parent:
		case LITERAL_ancestor:
		case 153:
		case 154:
		case LITERAL_preceding:
		{
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_attribute:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_descendant:
			case 148:
			case 149:
			case LITERAL_following:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 153:
			case 154:
			case LITERAL_preceding:
			{
				axis=forwardAxis(_t);
				_t = _retTree;
				break;
			}
			case QNAME:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case ATTRIBUTE_TEST:
			case NCNAME:
			case LITERAL_element:
			case LITERAL_text:
			case LITERAL_node:
			case LITERAL_comment:
			case 136:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			NodeTest test;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			{
				qn = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,QNAME);
				_t = _t.getNextSibling();
				
							QName qname= QName.parse(context, qn.getText());
							test= new NameTest(Type.ELEMENT, qname);
							if (axis == Constants.ATTRIBUTE_AXIS)
								test.setType(Type.ATTRIBUTE);
						
				break;
			}
			case PREFIX_WILDCARD:
			{
				AST __t114 = _t;
				org.exist.xquery.parser.XQueryAST tmp60_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,PREFIX_WILDCARD);
				_t = _t.getFirstChild();
				nc1 = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,NCNAME);
				_t = _t.getNextSibling();
				_t = __t114;
				_t = _t.getNextSibling();
				
							QName qname= new QName(nc1.getText(), null, null);
							qname.setNamespaceURI(null);
							test= new NameTest(Type.ELEMENT, qname);
							if (axis == Constants.ATTRIBUTE_AXIS)
								test.setType(Type.ATTRIBUTE);
						
				break;
			}
			case NCNAME:
			{
				AST __t115 = _t;
				nc = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
				match(_t,NCNAME);
				_t = _t.getFirstChild();
				org.exist.xquery.parser.XQueryAST tmp61_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,WILDCARD);
				_t = _t.getNextSibling();
				_t = __t115;
				_t = _t.getNextSibling();
				
							String namespaceURI= context.getURIForPrefix(nc.getText());
							QName qname= new QName(null, namespaceURI, nc.getText());
							test= new NameTest(Type.ELEMENT, qname);
							if (axis == Constants.ATTRIBUTE_AXIS)
								test.setType(Type.ATTRIBUTE);
						
				break;
			}
			case WILDCARD:
			{
				org.exist.xquery.parser.XQueryAST tmp62_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,WILDCARD);
				_t = _t.getNextSibling();
				
							if (axis == Constants.ATTRIBUTE_AXIS)
								test= new TypeTest(Type.ATTRIBUTE);
							else
								test= new TypeTest(Type.ELEMENT);
						
				break;
			}
			case LITERAL_node:
			{
				n = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,LITERAL_node);
				_t = _t.getNextSibling();
				
							if (axis == Constants.ATTRIBUTE_AXIS)
								throw new XPathException(n, "Cannot test for node() on the attribute axis");
							test= new AnyNodeTest(); 
						
				break;
			}
			case LITERAL_text:
			{
				org.exist.xquery.parser.XQueryAST tmp63_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,LITERAL_text);
				_t = _t.getNextSibling();
				
							if (axis == Constants.ATTRIBUTE_AXIS)
								throw new XPathException(n, "Cannot test for text() on the attribute axis"); 
							test= new TypeTest(Type.TEXT); 
						
				break;
			}
			case LITERAL_element:
			{
				AST __t116 = _t;
				org.exist.xquery.parser.XQueryAST tmp64_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,LITERAL_element);
				_t = _t.getFirstChild();
				
								if (axis == Constants.ATTRIBUTE_AXIS)
									throw new XPathException(n, "Cannot test for element() on the attribute axis"); 
								test= new TypeTest(Type.ELEMENT); 
							
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case QNAME:
				{
					qn2 = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,QNAME);
					_t = _t.getNextSibling();
					
										QName qname= QName.parse(context, qn2.getText());
										test= new NameTest(Type.ELEMENT, qname);
									
					break;
				}
				case WILDCARD:
				{
					org.exist.xquery.parser.XQueryAST tmp65_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,WILDCARD);
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				_t = __t116;
				_t = _t.getNextSibling();
				break;
			}
			case ATTRIBUTE_TEST:
			{
				AST __t118 = _t;
				org.exist.xquery.parser.XQueryAST tmp66_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,ATTRIBUTE_TEST);
				_t = _t.getFirstChild();
				test= new TypeTest(Type.ATTRIBUTE);
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case QNAME:
				{
					qn3 = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,QNAME);
					_t = _t.getNextSibling();
					
										QName qname= QName.parse(context, qn3.getText());
										test= new NameTest(Type.ATTRIBUTE, qname);
										axis= Constants.ATTRIBUTE_AXIS;
									
					break;
				}
				case WILDCARD:
				{
					org.exist.xquery.parser.XQueryAST tmp67_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,WILDCARD);
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				_t = __t118;
				_t = _t.getNextSibling();
				break;
			}
			case LITERAL_comment:
			{
				org.exist.xquery.parser.XQueryAST tmp68_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,LITERAL_comment);
				_t = _t.getNextSibling();
				
							if (axis == Constants.ATTRIBUTE_AXIS)
								throw new XPathException(n, "Cannot test for comment() on the attribute axis");
							test= new TypeTest(Type.COMMENT); 
						
				break;
			}
			case 136:
			{
				org.exist.xquery.parser.XQueryAST tmp69_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,136);
				_t = _t.getNextSibling();
				test= new TypeTest(Type.DOCUMENT);
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			
					step= new LocationStep(context, axis, test);
					path.add(step);
				
			{
			_loop121:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==PREDICATE)) {
					predicate(_t,(LocationStep) step);
					_t = _retTree;
				}
				else {
					break _loop121;
				}
				
			} while (true);
			}
			break;
		}
		case AT:
		{
			org.exist.xquery.parser.XQueryAST tmp70_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,AT);
			_t = _t.getNextSibling();
			QName qname= null;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			{
				attr = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,QNAME);
				_t = _t.getNextSibling();
				qname= QName.parse(context, attr.getText(), null);
				break;
			}
			case WILDCARD:
			{
				org.exist.xquery.parser.XQueryAST tmp71_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,WILDCARD);
				_t = _t.getNextSibling();
				break;
			}
			case PREFIX_WILDCARD:
			{
				AST __t123 = _t;
				org.exist.xquery.parser.XQueryAST tmp72_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,PREFIX_WILDCARD);
				_t = _t.getFirstChild();
				nc2 = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,NCNAME);
				_t = _t.getNextSibling();
				_t = __t123;
				_t = _t.getNextSibling();
				qname= new QName(nc2.getText(), null, null);
				break;
			}
			case NCNAME:
			{
				AST __t124 = _t;
				nc3 = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
				match(_t,NCNAME);
				_t = _t.getFirstChild();
				org.exist.xquery.parser.XQueryAST tmp73_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,WILDCARD);
				_t = _t.getNextSibling();
				_t = __t124;
				_t = _t.getNextSibling();
				
							String namespaceURI= context.getURIForPrefix(nc3.getText());
							if (namespaceURI == null)
								throw new EXistException("No namespace defined for prefix " + nc3.getText());
							qname= new QName(null, namespaceURI, null);
						
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			
					NodeTest test= qname == null ? new TypeTest(Type.ATTRIBUTE) : new NameTest(Type.ATTRIBUTE, qname);
					step= new LocationStep(context, Constants.ATTRIBUTE_AXIS, test);
					path.add(step);
				
			{
			_loop126:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==PREDICATE)) {
					predicate(_t,(LocationStep) step);
					_t = _retTree;
				}
				else {
					break _loop126;
				}
				
			} while (true);
			}
			break;
		}
		case SELF:
		{
			org.exist.xquery.parser.XQueryAST tmp74_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,SELF);
			_t = _t.getNextSibling();
			
					step= new LocationStep(context, Constants.SELF_AXIS, new TypeTest(Type.NODE));
					path.add(step);
				
			{
			_loop128:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==PREDICATE)) {
					predicate(_t,(LocationStep) step);
					_t = _retTree;
				}
				else {
					break _loop128;
				}
				
			} while (true);
			}
			break;
		}
		case PARENT:
		{
			org.exist.xquery.parser.XQueryAST tmp75_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,PARENT);
			_t = _t.getNextSibling();
			
					step= new LocationStep(context, Constants.PARENT_AXIS, new TypeTest(Type.NODE));
					path.add(step);
				
			{
			_loop130:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==PREDICATE)) {
					predicate(_t,(LocationStep) step);
					_t = _retTree;
				}
				else {
					break _loop130;
				}
				
			} while (true);
			}
			break;
		}
		case SLASH:
		{
			AST __t131 = _t;
			org.exist.xquery.parser.XQueryAST tmp76_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,SLASH);
			_t = _t.getFirstChild();
			step=expr(_t,path);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			case PARENTHESIZED:
			case ABSOLUTE_SLASH:
			case ABSOLUTE_DSLASH:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case UNARY_MINUS:
			case UNARY_PLUS:
			case VARIABLE_REF:
			case ELEMENT:
			case TEXT:
			case BEFORE:
			case AFTER:
			case ATTRIBUTE_TEST:
			case COMP_ELEM_CONSTRUCTOR:
			case COMP_ATTR_CONSTRUCTOR:
			case COMP_TEXT_CONSTRUCTOR:
			case COMP_COMMENT_CONSTRUCTOR:
			case COMP_PI_CONSTRUCTOR:
			case COMP_DOC_CONSTRUCTOR:
			case NCNAME:
			case EQ:
			case STRING_LITERAL:
			case LITERAL_element:
			case LCURLY:
			case COMMA:
			case STAR:
			case PLUS:
			case LITERAL_some:
			case LITERAL_every:
			case LITERAL_if:
			case LITERAL_return:
			case LITERAL_or:
			case LITERAL_and:
			case LITERAL_instance:
			case LITERAL_castable:
			case LITERAL_cast:
			case LT:
			case GT:
			case LITERAL_eq:
			case LITERAL_ne:
			case LITERAL_lt:
			case LITERAL_le:
			case LITERAL_gt:
			case LITERAL_ge:
			case NEQ:
			case GTEQ:
			case LTEQ:
			case LITERAL_is:
			case LITERAL_isnot:
			case ANDEQ:
			case OREQ:
			case LITERAL_to:
			case MINUS:
			case LITERAL_div:
			case LITERAL_idiv:
			case LITERAL_mod:
			case UNION:
			case LITERAL_intersect:
			case LITERAL_except:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case LITERAL_attribute:
			case LITERAL_comment:
			case 136:
			case SELF:
			case XML_COMMENT:
			case XML_PI:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_descendant:
			case 148:
			case 149:
			case LITERAL_following:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 153:
			case 154:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			case XML_CDATA:
			case LITERAL_preceding:
			{
				rightStep=expr(_t,path);
				_t = _retTree;
				
								if (rightStep instanceof LocationStep) {
									if(((LocationStep) rightStep).getAxis() == -1)
										((LocationStep) rightStep).setAxis(Constants.CHILD_AXIS);
								} else {
									rightStep.setPrimaryAxis(Constants.CHILD_AXIS);
									if(rightStep instanceof VariableReference) {
										rightStep = new SimpleStep(context, Constants.CHILD_AXIS, rightStep);
										path.replaceLastExpression(rightStep);
									}
								}
							
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t131;
			_t = _t.getNextSibling();
			
					if (step instanceof LocationStep && ((LocationStep) step).getAxis() == -1)
						 ((LocationStep) step).setAxis(Constants.CHILD_AXIS);
				
			break;
		}
		case DSLASH:
		{
			AST __t133 = _t;
			org.exist.xquery.parser.XQueryAST tmp77_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,DSLASH);
			_t = _t.getFirstChild();
			step=expr(_t,path);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			case PARENTHESIZED:
			case ABSOLUTE_SLASH:
			case ABSOLUTE_DSLASH:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case UNARY_MINUS:
			case UNARY_PLUS:
			case VARIABLE_REF:
			case ELEMENT:
			case TEXT:
			case BEFORE:
			case AFTER:
			case ATTRIBUTE_TEST:
			case COMP_ELEM_CONSTRUCTOR:
			case COMP_ATTR_CONSTRUCTOR:
			case COMP_TEXT_CONSTRUCTOR:
			case COMP_COMMENT_CONSTRUCTOR:
			case COMP_PI_CONSTRUCTOR:
			case COMP_DOC_CONSTRUCTOR:
			case NCNAME:
			case EQ:
			case STRING_LITERAL:
			case LITERAL_element:
			case LCURLY:
			case COMMA:
			case STAR:
			case PLUS:
			case LITERAL_some:
			case LITERAL_every:
			case LITERAL_if:
			case LITERAL_return:
			case LITERAL_or:
			case LITERAL_and:
			case LITERAL_instance:
			case LITERAL_castable:
			case LITERAL_cast:
			case LT:
			case GT:
			case LITERAL_eq:
			case LITERAL_ne:
			case LITERAL_lt:
			case LITERAL_le:
			case LITERAL_gt:
			case LITERAL_ge:
			case NEQ:
			case GTEQ:
			case LTEQ:
			case LITERAL_is:
			case LITERAL_isnot:
			case ANDEQ:
			case OREQ:
			case LITERAL_to:
			case MINUS:
			case LITERAL_div:
			case LITERAL_idiv:
			case LITERAL_mod:
			case UNION:
			case LITERAL_intersect:
			case LITERAL_except:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case LITERAL_attribute:
			case LITERAL_comment:
			case 136:
			case SELF:
			case XML_COMMENT:
			case XML_PI:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_descendant:
			case 148:
			case 149:
			case LITERAL_following:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 153:
			case 154:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			case XML_CDATA:
			case LITERAL_preceding:
			{
				rightStep=expr(_t,path);
				_t = _retTree;
				
								if (rightStep instanceof LocationStep) {
									LocationStep rs= (LocationStep) rightStep;
									if (rs.getAxis() == Constants.ATTRIBUTE_AXIS)
										rs.setAxis(Constants.DESCENDANT_ATTRIBUTE_AXIS);
									else
										rs.setAxis(Constants.DESCENDANT_SELF_AXIS);
								} else {
									rightStep.setPrimaryAxis(Constants.DESCENDANT_SELF_AXIS);
									if(rightStep instanceof VariableReference) {
										rightStep = new SimpleStep(context, Constants.DESCENDANT_SELF_AXIS, rightStep);
										path.replaceLastExpression(rightStep);
									}
								}
							
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t133;
			_t = _t.getNextSibling();
			
					if (step instanceof LocationStep && ((LocationStep) step).getAxis() == -1)
						 ((LocationStep) step).setAxis(Constants.DESCENDANT_SELF_AXIS);
				
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  numericExpr(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		org.exist.xquery.parser.XQueryAST numericExpr_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST plus = null;
		org.exist.xquery.parser.XQueryAST minus = null;
		org.exist.xquery.parser.XQueryAST uminus = null;
		org.exist.xquery.parser.XQueryAST uplus = null;
		org.exist.xquery.parser.XQueryAST div = null;
		org.exist.xquery.parser.XQueryAST idiv = null;
		org.exist.xquery.parser.XQueryAST mod = null;
		org.exist.xquery.parser.XQueryAST mult = null;
		
			step= null;
			PathExpr left= new PathExpr(context);
			PathExpr right= new PathExpr(context);
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case PLUS:
		{
			AST __t138 = _t;
			plus = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,PLUS);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			_t = __t138;
			_t = _t.getNextSibling();
			
					OpNumeric op= new OpNumeric(context, left, right, Constants.PLUS);
			op.setASTNode(plus);
					path.addPath(op);
					step= op;
				
			break;
		}
		case MINUS:
		{
			AST __t139 = _t;
			minus = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,MINUS);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			_t = __t139;
			_t = _t.getNextSibling();
			
					OpNumeric op= new OpNumeric(context, left, right, Constants.MINUS);
			op.setASTNode(minus);
					path.addPath(op);
					step= op;
				
			break;
		}
		case UNARY_MINUS:
		{
			AST __t140 = _t;
			uminus = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,UNARY_MINUS);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			_t = __t140;
			_t = _t.getNextSibling();
			
					UnaryExpr unary= new UnaryExpr(context, Constants.MINUS);
			unary.setASTNode(uminus);
					unary.add(left);
					path.addPath(unary);
					step= unary;
				
			break;
		}
		case UNARY_PLUS:
		{
			AST __t141 = _t;
			uplus = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,UNARY_PLUS);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			_t = __t141;
			_t = _t.getNextSibling();
			
					UnaryExpr unary= new UnaryExpr(context, Constants.PLUS);
			unary.setASTNode(uplus);
					unary.add(left);
					path.addPath(unary);
					step= unary;
				
			break;
		}
		case LITERAL_div:
		{
			AST __t142 = _t;
			div = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_div);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			_t = __t142;
			_t = _t.getNextSibling();
			
					OpNumeric op= new OpNumeric(context, left, right, Constants.DIV);
			op.setASTNode(div);
					path.addPath(op);
					step= op;
				
			break;
		}
		case LITERAL_idiv:
		{
			AST __t143 = _t;
			idiv = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_idiv);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			_t = __t143;
			_t = _t.getNextSibling();
			
					OpNumeric op= new OpNumeric(context, left, right, Constants.IDIV);
			op.setASTNode(idiv);
					path.addPath(op);
					step= op;
				
			break;
		}
		case LITERAL_mod:
		{
			AST __t144 = _t;
			mod = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_mod);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			_t = __t144;
			_t = _t.getNextSibling();
			
					OpNumeric op= new OpNumeric(context, left, right, Constants.MOD);
			op.setASTNode(mod);
					path.addPath(op);
					step= op;
				
			break;
		}
		case STAR:
		{
			AST __t145 = _t;
			mult = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,STAR);
			_t = _t.getFirstChild();
			step=expr(_t,left);
			_t = _retTree;
			step=expr(_t,right);
			_t = _retTree;
			_t = __t145;
			_t = _t.getNextSibling();
			
					OpNumeric op= new OpNumeric(context, left, right, Constants.MULT);
			op.setASTNode(mult);
					path.addPath(op);
					step= op;
				
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  constructor(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		org.exist.xquery.parser.XQueryAST constructor_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST qn = null;
		org.exist.xquery.parser.XQueryAST prefix = null;
		org.exist.xquery.parser.XQueryAST uri = null;
		org.exist.xquery.parser.XQueryAST attr = null;
		org.exist.xquery.parser.XQueryAST pid = null;
		org.exist.xquery.parser.XQueryAST e = null;
		org.exist.xquery.parser.XQueryAST attrName = null;
		org.exist.xquery.parser.XQueryAST attrVal = null;
		org.exist.xquery.parser.XQueryAST pcdata = null;
		org.exist.xquery.parser.XQueryAST t = null;
		org.exist.xquery.parser.XQueryAST tc = null;
		org.exist.xquery.parser.XQueryAST d = null;
		org.exist.xquery.parser.XQueryAST cdata = null;
		org.exist.xquery.parser.XQueryAST p = null;
		org.exist.xquery.parser.XQueryAST cdataSect = null;
		org.exist.xquery.parser.XQueryAST l = null;
		
			step= null;
			PathExpr elementContent= null;
			Expression contentExpr= null;
			Expression qnameExpr = null;
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case COMP_ELEM_CONSTRUCTOR:
		{
			AST __t180 = _t;
			qn = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,COMP_ELEM_CONSTRUCTOR);
			_t = _t.getFirstChild();
			
						ElementConstructor c= new ElementConstructor(context);
						c.setASTNode(qn);
						step= c;
						SequenceConstructor construct = new SequenceConstructor(context);
						EnclosedExpr enclosed = new EnclosedExpr(context);
						enclosed.addPath(construct);
						c.setContent(enclosed);
						PathExpr qnamePathExpr = new PathExpr(context);
						c.setNameExpr(qnamePathExpr);
					
			qnameExpr=expr(_t,qnamePathExpr);
			_t = _retTree;
			{
			_loop183:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case COMP_NS_CONSTRUCTOR:
				{
					AST __t182 = _t;
					prefix = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
					match(_t,COMP_NS_CONSTRUCTOR);
					_t = _t.getFirstChild();
					uri = (org.exist.xquery.parser.XQueryAST)_t;
					match(_t,STRING_LITERAL);
					_t = _t.getNextSibling();
					_t = __t182;
					_t = _t.getNextSibling();
					
									c.addNamespaceDecl(prefix.getText(), uri.getText());
								
					break;
				}
				case QNAME:
				case PARENTHESIZED:
				case ABSOLUTE_SLASH:
				case ABSOLUTE_DSLASH:
				case WILDCARD:
				case PREFIX_WILDCARD:
				case FUNCTION:
				case UNARY_MINUS:
				case UNARY_PLUS:
				case VARIABLE_REF:
				case ELEMENT:
				case TEXT:
				case BEFORE:
				case AFTER:
				case ATTRIBUTE_TEST:
				case COMP_ELEM_CONSTRUCTOR:
				case COMP_ATTR_CONSTRUCTOR:
				case COMP_TEXT_CONSTRUCTOR:
				case COMP_COMMENT_CONSTRUCTOR:
				case COMP_PI_CONSTRUCTOR:
				case COMP_DOC_CONSTRUCTOR:
				case NCNAME:
				case EQ:
				case STRING_LITERAL:
				case LITERAL_element:
				case LCURLY:
				case COMMA:
				case STAR:
				case PLUS:
				case LITERAL_some:
				case LITERAL_every:
				case LITERAL_if:
				case LITERAL_return:
				case LITERAL_or:
				case LITERAL_and:
				case LITERAL_instance:
				case LITERAL_castable:
				case LITERAL_cast:
				case LT:
				case GT:
				case LITERAL_eq:
				case LITERAL_ne:
				case LITERAL_lt:
				case LITERAL_le:
				case LITERAL_gt:
				case LITERAL_ge:
				case NEQ:
				case GTEQ:
				case LTEQ:
				case LITERAL_is:
				case LITERAL_isnot:
				case ANDEQ:
				case OREQ:
				case LITERAL_to:
				case MINUS:
				case LITERAL_div:
				case LITERAL_idiv:
				case LITERAL_mod:
				case UNION:
				case LITERAL_intersect:
				case LITERAL_except:
				case SLASH:
				case DSLASH:
				case LITERAL_text:
				case LITERAL_node:
				case LITERAL_attribute:
				case LITERAL_comment:
				case 136:
				case SELF:
				case XML_COMMENT:
				case XML_PI:
				case AT:
				case PARENT:
				case LITERAL_child:
				case LITERAL_self:
				case LITERAL_descendant:
				case 148:
				case 149:
				case LITERAL_following:
				case LITERAL_parent:
				case LITERAL_ancestor:
				case 153:
				case 154:
				case DOUBLE_LITERAL:
				case DECIMAL_LITERAL:
				case INTEGER_LITERAL:
				case XML_CDATA:
				case LITERAL_preceding:
				{
					elementContent = new PathExpr(context);
					contentExpr=expr(_t,elementContent);
					_t = _retTree;
					construct.addPath(elementContent);
					break;
				}
				default:
				{
					break _loop183;
				}
				}
			} while (true);
			}
			_t = __t180;
			_t = _t.getNextSibling();
			break;
		}
		case COMP_ATTR_CONSTRUCTOR:
		{
			AST __t184 = _t;
			attr = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,COMP_ATTR_CONSTRUCTOR);
			_t = _t.getFirstChild();
			
						DynamicAttributeConstructor a= new DynamicAttributeConstructor(context);
			a.setASTNode(attr);
			step = a;
			PathExpr qnamePathExpr = new PathExpr(context);
			a.setNameExpr(qnamePathExpr);
			elementContent = new PathExpr(context);
			a.setContentExpr(elementContent);
					
			qnameExpr=expr(_t,qnamePathExpr);
			_t = _retTree;
			contentExpr=expr(_t,elementContent);
			_t = _retTree;
			_t = __t184;
			_t = _t.getNextSibling();
			break;
		}
		case COMP_PI_CONSTRUCTOR:
		{
			AST __t185 = _t;
			pid = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,COMP_PI_CONSTRUCTOR);
			_t = _t.getFirstChild();
			
						DynamicPIConstructor pd= new DynamicPIConstructor(context);
			pd.setASTNode(pid);
			step = pd;
			PathExpr qnamePathExpr = new PathExpr(context);
			pd.setNameExpr(qnamePathExpr);
			elementContent = new PathExpr(context);
			pd.setContentExpr(elementContent);
					
			qnameExpr=expr(_t,qnamePathExpr);
			_t = _retTree;
			contentExpr=expr(_t,elementContent);
			_t = _retTree;
			_t = __t185;
			_t = _t.getNextSibling();
			break;
		}
		case ELEMENT:
		{
			AST __t186 = _t;
			e = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,ELEMENT);
			_t = _t.getFirstChild();
			
						ElementConstructor c= new ElementConstructor(context, e.getText());
						c.setASTNode(e);
						step= c;
					
			{
			_loop192:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==ATTRIBUTE)) {
					AST __t188 = _t;
					attrName = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
					match(_t,ATTRIBUTE);
					_t = _t.getFirstChild();
					
										AttributeConstructor attrib= new AttributeConstructor(context, attrName.getText());
					attrib.setASTNode(attrName);
									
					{
					int _cnt191=0;
					_loop191:
					do {
						if (_t==null) _t=ASTNULL;
						switch ( _t.getType()) {
						case ATTRIBUTE_CONTENT:
						{
							attrVal = (org.exist.xquery.parser.XQueryAST)_t;
							match(_t,ATTRIBUTE_CONTENT);
							_t = _t.getNextSibling();
							attrib.addValue(attrVal.getText());
							break;
						}
						case LCURLY:
						{
							AST __t190 = _t;
							org.exist.xquery.parser.XQueryAST tmp78_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
							match(_t,LCURLY);
							_t = _t.getFirstChild();
							PathExpr enclosed= new PathExpr(context);
							expr(_t,enclosed);
							_t = _retTree;
							attrib.addEnclosedExpr(enclosed);
							_t = __t190;
							_t = _t.getNextSibling();
							break;
						}
						default:
						{
							if ( _cnt191>=1 ) { break _loop191; } else {throw new NoViableAltException(_t);}
						}
						}
						_cnt191++;
					} while (true);
					}
					c.addAttribute(attrib);
					_t = __t188;
					_t = _t.getNextSibling();
				}
				else {
					break _loop192;
				}
				
			} while (true);
			}
			{
			_loop194:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_1.member(_t.getType()))) {
					
									if (elementContent == null) {
										elementContent= new PathExpr(context);
										c.setContent(elementContent);
									}
								
					contentExpr=constructor(_t,elementContent);
					_t = _retTree;
					elementContent.add(contentExpr);
				}
				else {
					break _loop194;
				}
				
			} while (true);
			}
			_t = __t186;
			_t = _t.getNextSibling();
			break;
		}
		case TEXT:
		{
			AST __t195 = _t;
			pcdata = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,TEXT);
			_t = _t.getFirstChild();
			
						TextConstructor text= new TextConstructor(context, pcdata.getText());
			text.setASTNode(pcdata);
						step= text;
					
			_t = __t195;
			_t = _t.getNextSibling();
			break;
		}
		case COMP_TEXT_CONSTRUCTOR:
		{
			AST __t196 = _t;
			t = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,COMP_TEXT_CONSTRUCTOR);
			_t = _t.getFirstChild();
			
						elementContent = new PathExpr(context);
						DynamicTextConstructor text = new DynamicTextConstructor(context, elementContent);
						text.setASTNode(t);
						step= text;
					
			contentExpr=expr(_t,elementContent);
			_t = _retTree;
			_t = __t196;
			_t = _t.getNextSibling();
			break;
		}
		case COMP_COMMENT_CONSTRUCTOR:
		{
			AST __t197 = _t;
			tc = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,COMP_COMMENT_CONSTRUCTOR);
			_t = _t.getFirstChild();
			
						elementContent = new PathExpr(context);
						DynamicCommentConstructor comment = new DynamicCommentConstructor(context, elementContent);
						comment.setASTNode(t);
						step= comment;
					
			contentExpr=expr(_t,elementContent);
			_t = _retTree;
			_t = __t197;
			_t = _t.getNextSibling();
			break;
		}
		case COMP_DOC_CONSTRUCTOR:
		{
			AST __t198 = _t;
			d = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,COMP_DOC_CONSTRUCTOR);
			_t = _t.getFirstChild();
			
						elementContent = new PathExpr(context);
						DocumentConstructor doc = new DocumentConstructor(context, elementContent);
						doc.setASTNode(d);
						step= doc;
					
			contentExpr=expr(_t,elementContent);
			_t = _retTree;
			_t = __t198;
			_t = _t.getNextSibling();
			break;
		}
		case XML_COMMENT:
		{
			AST __t199 = _t;
			cdata = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,XML_COMMENT);
			_t = _t.getFirstChild();
			
						CommentConstructor comment= new CommentConstructor(context, cdata.getText());
			comment.setASTNode(cdata);
						step= comment;
					
			_t = __t199;
			_t = _t.getNextSibling();
			break;
		}
		case XML_PI:
		{
			AST __t200 = _t;
			p = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,XML_PI);
			_t = _t.getFirstChild();
			
						PIConstructor pi= new PIConstructor(context, p.getText());
			pi.setASTNode(p);
						step= pi;
					
			_t = __t200;
			_t = _t.getNextSibling();
			break;
		}
		case XML_CDATA:
		{
			AST __t201 = _t;
			cdataSect = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,XML_CDATA);
			_t = _t.getFirstChild();
			
						CDATAConstructor cd = new CDATAConstructor(context, cdataSect.getText());
						cd.setASTNode(cdataSect);
						step= cd;
					
			_t = __t201;
			_t = _t.getNextSibling();
			break;
		}
		case LCURLY:
		{
			AST __t202 = _t;
			l = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LCURLY);
			_t = _t.getFirstChild();
			
			EnclosedExpr subexpr= new EnclosedExpr(context); 
			subexpr.setASTNode(l);
			
			step=expr(_t,subexpr);
			_t = _retTree;
			step= subexpr;
			_t = __t202;
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  predicates(AST _t,
		Expression expression
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		org.exist.xquery.parser.XQueryAST predicates_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		
			FilteredExpression filter= null;
			step= expression;
		
		
		{
		_loop149:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==PREDICATE)) {
				AST __t148 = _t;
				org.exist.xquery.parser.XQueryAST tmp79_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,PREDICATE);
				_t = _t.getFirstChild();
				
								if (filter == null) {
									filter= new FilteredExpression(context, step);
									step= filter;
								}
								Predicate predicateExpr= new Predicate(context);
							
				expr(_t,predicateExpr);
				_t = _retTree;
				
								filter.addPredicate(predicateExpr);
							
				_t = __t148;
				_t = _t.getNextSibling();
			}
			else {
				break _loop149;
			}
			
		} while (true);
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  literalExpr(AST _t,
		PathExpr path
	) throws RecognitionException, XPathException {
		Expression step;
		
		org.exist.xquery.parser.XQueryAST literalExpr_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST c = null;
		org.exist.xquery.parser.XQueryAST i = null;
		org.exist.xquery.parser.XQueryAST dec = null;
		org.exist.xquery.parser.XQueryAST dbl = null;
		step= null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case STRING_LITERAL:
		{
			c = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,STRING_LITERAL);
			_t = _t.getNextSibling();
			
					StringValue val = new StringValue(c.getText());
					val.expand();
			step= new LiteralValue(context, val);
			step.setASTNode(c);
			
			break;
		}
		case INTEGER_LITERAL:
		{
			i = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,INTEGER_LITERAL);
			_t = _t.getNextSibling();
			
			step= new LiteralValue(context, new IntegerValue(i.getText()));
			step.setASTNode(i);
			
			break;
		}
		case DOUBLE_LITERAL:
		case DECIMAL_LITERAL:
		{
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DECIMAL_LITERAL:
			{
				dec = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,DECIMAL_LITERAL);
				_t = _t.getNextSibling();
				
				step= new LiteralValue(context, new DecimalValue(dec.getText()));
				step.setASTNode(dec);
				
				break;
			}
			case DOUBLE_LITERAL:
			{
				dbl = (org.exist.xquery.parser.XQueryAST)_t;
				match(_t,DOUBLE_LITERAL);
				_t = _t.getNextSibling();
				
				step= new LiteralValue(context, 
				new DoubleValue(Double.parseDouble(dbl.getText())));
				step.setASTNode(dbl);
				
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  functionCall(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		org.exist.xquery.parser.XQueryAST functionCall_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		org.exist.xquery.parser.XQueryAST fn = null;
		
			PathExpr pathExpr;
			step= null;
		
		
		AST __t153 = _t;
		fn = _t==ASTNULL ? null :(org.exist.xquery.parser.XQueryAST)_t;
		match(_t,FUNCTION);
		_t = _t.getFirstChild();
		List params= new ArrayList(2);
		{
		_loop155:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_0.member(_t.getType()))) {
				pathExpr= new PathExpr(context);
				expr(_t,pathExpr);
				_t = _retTree;
				params.add(pathExpr);
			}
			else {
				break _loop155;
			}
			
		} while (true);
		}
		_t = __t153;
		_t = _t.getNextSibling();
		step= FunctionFactory.createFunction(context, fn, path, params);
		_retTree = _t;
		return step;
	}
	
	public final int  forwardAxis(AST _t) throws RecognitionException, PermissionDeniedException,EXistException {
		int axis;
		
		org.exist.xquery.parser.XQueryAST forwardAxis_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		axis= -1;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_child:
		{
			org.exist.xquery.parser.XQueryAST tmp80_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_child);
			_t = _t.getNextSibling();
			axis= Constants.CHILD_AXIS;
			break;
		}
		case LITERAL_attribute:
		{
			org.exist.xquery.parser.XQueryAST tmp81_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_attribute);
			_t = _t.getNextSibling();
			axis= Constants.ATTRIBUTE_AXIS;
			break;
		}
		case LITERAL_self:
		{
			org.exist.xquery.parser.XQueryAST tmp82_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_self);
			_t = _t.getNextSibling();
			axis= Constants.SELF_AXIS;
			break;
		}
		case LITERAL_parent:
		{
			org.exist.xquery.parser.XQueryAST tmp83_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_parent);
			_t = _t.getNextSibling();
			axis= Constants.PARENT_AXIS;
			break;
		}
		case LITERAL_descendant:
		{
			org.exist.xquery.parser.XQueryAST tmp84_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_descendant);
			_t = _t.getNextSibling();
			axis= Constants.DESCENDANT_AXIS;
			break;
		}
		case 148:
		{
			org.exist.xquery.parser.XQueryAST tmp85_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,148);
			_t = _t.getNextSibling();
			axis= Constants.DESCENDANT_SELF_AXIS;
			break;
		}
		case 149:
		{
			org.exist.xquery.parser.XQueryAST tmp86_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,149);
			_t = _t.getNextSibling();
			axis= Constants.FOLLOWING_SIBLING_AXIS;
			break;
		}
		case LITERAL_following:
		{
			org.exist.xquery.parser.XQueryAST tmp87_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_following);
			_t = _t.getNextSibling();
			axis= Constants.FOLLOWING_AXIS;
			break;
		}
		case 154:
		{
			org.exist.xquery.parser.XQueryAST tmp88_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,154);
			_t = _t.getNextSibling();
			axis= Constants.PRECEDING_SIBLING_AXIS;
			break;
		}
		case LITERAL_preceding:
		{
			org.exist.xquery.parser.XQueryAST tmp89_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_preceding);
			_t = _t.getNextSibling();
			axis= Constants.PRECEDING_AXIS;
			break;
		}
		case LITERAL_ancestor:
		{
			org.exist.xquery.parser.XQueryAST tmp90_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,LITERAL_ancestor);
			_t = _t.getNextSibling();
			axis= Constants.ANCESTOR_AXIS;
			break;
		}
		case 153:
		{
			org.exist.xquery.parser.XQueryAST tmp91_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
			match(_t,153);
			_t = _t.getNextSibling();
			axis= Constants.ANCESTOR_SELF_AXIS;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return axis;
	}
	
	public final void predicate(AST _t,
		LocationStep step
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		
		org.exist.xquery.parser.XQueryAST predicate_AST_in = (_t == ASTNULL) ? null : (org.exist.xquery.parser.XQueryAST)_t;
		
		AST __t151 = _t;
		org.exist.xquery.parser.XQueryAST tmp92_AST_in = (org.exist.xquery.parser.XQueryAST)_t;
		match(_t,PREDICATE);
		_t = _t.getFirstChild();
		Predicate predicateExpr= new Predicate(context);
		expr(_t,predicateExpr);
		_t = _retTree;
		step.addPredicate(predicateExpr);
		_t = __t151;
		_t = _t.getNextSibling();
		_retTree = _t;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"QNAME",
		"PREDICATE",
		"FLWOR",
		"PARENTHESIZED",
		"ABSOLUTE_SLASH",
		"ABSOLUTE_DSLASH",
		"WILDCARD",
		"PREFIX_WILDCARD",
		"FUNCTION",
		"UNARY_MINUS",
		"UNARY_PLUS",
		"XPOINTER",
		"XPOINTER_ID",
		"VARIABLE_REF",
		"VARIABLE_BINDING",
		"ELEMENT",
		"ATTRIBUTE",
		"TEXT",
		"VERSION_DECL",
		"NAMESPACE_DECL",
		"DEF_NAMESPACE_DECL",
		"DEF_COLLATION_DECL",
		"DEF_FUNCTION_NS_DECL",
		"GLOBAL_VAR",
		"FUNCTION_DECL",
		"PROLOG",
		"ATOMIC_TYPE",
		"MODULE",
		"ORDER_BY",
		"POSITIONAL_VAR",
		"BEFORE",
		"AFTER",
		"MODULE_DECL",
		"ATTRIBUTE_TEST",
		"COMP_ELEM_CONSTRUCTOR",
		"COMP_ATTR_CONSTRUCTOR",
		"COMP_TEXT_CONSTRUCTOR",
		"COMP_COMMENT_CONSTRUCTOR",
		"COMP_PI_CONSTRUCTOR",
		"COMP_NS_CONSTRUCTOR",
		"COMP_DOC_CONSTRUCTOR",
		"\"xpointer\"",
		"'('",
		"')'",
		"NCNAME",
		"\"xquery\"",
		"\"version\"",
		"SEMICOLON",
		"\"module\"",
		"\"namespace\"",
		"EQ",
		"STRING_LITERAL",
		"\"import\"",
		"\"declare\"",
		"\"default\"",
		"\"xmlspace\"",
		"\"ordering\"",
		"\"construction\"",
		"\"function\"",
		"\"variable\"",
		"\"collation\"",
		"\"element\"",
		"\"preserve\"",
		"\"strip\"",
		"\"ordered\"",
		"\"unordered\"",
		"DOLLAR",
		"LCURLY",
		"RCURLY",
		"\"at\"",
		"\"as\"",
		"COMMA",
		"\"empty\"",
		"QUESTION",
		"STAR",
		"PLUS",
		"\"item\"",
		"\"for\"",
		"\"let\"",
		"\"some\"",
		"\"every\"",
		"\"if\"",
		"\"where\"",
		"\"return\"",
		"\"in\"",
		"COLON",
		"\"order\"",
		"\"by\"",
		"\"ascending\"",
		"\"descending\"",
		"\"greatest\"",
		"\"least\"",
		"\"satisfies\"",
		"\"then\"",
		"\"else\"",
		"\"or\"",
		"\"and\"",
		"\"instance\"",
		"\"of\"",
		"\"castable\"",
		"\"cast\"",
		"LT",
		"GT",
		"\"eq\"",
		"\"ne\"",
		"\"lt\"",
		"\"le\"",
		"\"gt\"",
		"\"ge\"",
		"NEQ",
		"GTEQ",
		"LTEQ",
		"\"is\"",
		"\"isnot\"",
		"ANDEQ",
		"OREQ",
		"\"to\"",
		"MINUS",
		"\"div\"",
		"\"idiv\"",
		"\"mod\"",
		"\"union\"",
		"UNION",
		"\"intersect\"",
		"\"except\"",
		"SLASH",
		"DSLASH",
		"\"text\"",
		"\"node\"",
		"\"attribute\"",
		"\"comment\"",
		"\"processing-instruction\"",
		"\"document-node\"",
		"\"document\"",
		"SELF",
		"XML_COMMENT",
		"XML_PI",
		"LPPAREN",
		"RPPAREN",
		"AT",
		"PARENT",
		"\"child\"",
		"\"self\"",
		"\"descendant\"",
		"\"descendant-or-self\"",
		"\"following-sibling\"",
		"\"following\"",
		"\"parent\"",
		"\"ancestor\"",
		"\"ancestor-or-self\"",
		"\"preceding-sibling\"",
		"DOUBLE_LITERAL",
		"DECIMAL_LITERAL",
		"INTEGER_LITERAL",
		"END_TAG_START",
		"QUOT",
		"APOS",
		"ATTRIBUTE_CONTENT",
		"ELEMENT_CONTENT",
		"XML_COMMENT_END",
		"XML_PI_END",
		"CDATA",
		"\"collection\"",
		"\"preceding\"",
		"XML_PI_START",
		"XML_CDATA_START",
		"XML_CDATA_END",
		"LETTER",
		"DIGITS",
		"HEX_DIGITS",
		"NMSTART",
		"NMCHAR",
		"WS",
		"EXPR_COMMENT",
		"PRAGMA",
		"PRAGMA_CONTENT",
		"PRAGMA_QNAME",
		"PREDEFINED_ENTITY_REF",
		"CHAR_REF",
		"NEXT_TOKEN",
		"CHAR",
		"BASECHAR",
		"IDEOGRAPHIC",
		"COMBINING_CHAR",
		"DIGIT",
		"EXTENDER"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 54350972887662480L, -2305843318439229310L, 688268483967L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 26113403781120L, 128L, 137438959616L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	}
	
