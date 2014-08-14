package org.dreamer.expression.calc;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Stack;
import java.util.ArrayList;

public class RPNTokenCompilerTest {
	public RPNTokenCompilerTest() {}

	@Test
	public void evaluateValueTest() {
		final TypedToken tokenVal = new TypedToken(new Token("1.02", 0), TypedToken.Type.VALUE);
		final TypedToken tokenE = new TypedToken(new Token("E", 0), TypedToken.Type.CONST);
		final TypedToken tokenPI = new TypedToken(new Token("PI", 0), TypedToken.Type.CONST);
		final Stack<Expression> stack = new Stack<Expression>();
		final Expression v = RPNTokenCompiler.compileToken(tokenVal, stack);
		assertEquals(1.02, v.eval(), 0.01);

		final Expression e = RPNTokenCompiler.compileToken(tokenE, stack);
		assertEquals(Math.E, e.eval(), 0.01);

		final Expression pi = RPNTokenCompiler.compileToken(tokenPI, stack);
		assertEquals(Math.PI, pi.eval(), 0.01);
	}

	@Test
	public void evaluateOpsTest() {
		Expression val = new ValueExpression(1.02);
		final Expression valE = new ValueExpression(Math.E);
		TypedToken tokenOperator = new TypedToken(new Token("+", 0), TypedToken.Type.OP_ADD);
		final Stack<Expression> stack = new Stack<Expression>();
		stack.push(val);
		stack.push(valE);
		Expression v = RPNTokenCompiler.compileToken(tokenOperator, stack);
		assertEquals(val.eval() + valE.eval(), v.eval(), 0.01);
		assertTrue(stack.empty());

		stack.push(valE);
		stack.push(val);
		tokenOperator = new TypedToken(new Token("-", 0), TypedToken.Type.OP_SUB);
		v = RPNTokenCompiler.compileToken(tokenOperator, stack);
		assertEquals(valE.eval() - val.eval(), v.eval(), 0.01);
		assertTrue(stack.empty());

		val = new ValueExpression(3.42);
		stack.push(valE);
		stack.push(val);
		tokenOperator = new TypedToken(new Token("*", 0), TypedToken.Type.OP_MUL);
		v = RPNTokenCompiler.compileToken(tokenOperator, stack);
		assertEquals(valE.eval() * val.eval(), v.eval(), 0.01);
		assertTrue(stack.empty());

		val = new ValueExpression(123.42);
		stack.push(val);
		stack.push(valE);
		tokenOperator = new TypedToken(new Token("/", 0), TypedToken.Type.OP_DIV);
		v = RPNTokenCompiler.compileToken(tokenOperator, stack);
		assertEquals(val.eval() / valE.eval(), v.eval(), 0.01);
		assertTrue(stack.empty());
	}

	@Test
	public void evaluateFuncsTest() {
		final Expression valPI = new ValueExpression(Math.PI);
		TypedToken tokenFunc = new TypedToken(new Token("sin", 0), TypedToken.Type.FUNC);
		final Stack<Expression> stack = new Stack<Expression>();
		stack.push(valPI);
		Expression v = RPNTokenCompiler.compileToken(tokenFunc, stack);
		assertEquals(Math.sin(valPI.eval()), v.eval(), 0.01);
		assertTrue(stack.empty());

		stack.push(valPI);
		tokenFunc = new TypedToken(new Token("cos", 0), TypedToken.Type.FUNC);
		v = RPNTokenCompiler.compileToken(tokenFunc, stack);
		assertEquals(Math.cos(valPI.eval()), v.eval(), 0.01);
		assertTrue(stack.empty());

		stack.push(valPI);
		tokenFunc = new TypedToken(new Token("exp", 0), TypedToken.Type.FUNC);
		v = RPNTokenCompiler.compileToken(tokenFunc, stack);
		assertEquals(Math.exp(valPI.eval()), v.eval(), 0.01);
		assertTrue(stack.empty());
	}

	private static Expression compileExpression(String expression) throws ParserException {
		final RPNTokenCompiler compiler = new RPNTokenCompiler();
		final TypedTokenParser typedTokenParser = new TypedTokenParser();
		final TokenParser tokenParser = new TokenParser();
		final InfixToRPNConverter converter = new InfixToRPNConverter();
		final ArrayList<Token> tokens = tokenParser.parse(expression);
		final ArrayList<TypedToken> typedTokens = typedTokenParser.parse(tokens);
		final ArrayList<TypedToken> tokensInRPN = converter.convert(typedTokens);
		return compiler.compile(tokensInRPN);
	}

	@Test
	public void evaluateExpressionTest() throws ParserException {
		final Expression e = compileExpression("1 + 2");
		assertEquals(3.0, e.eval(), 0.0);
	}

	@Test
	public void evaluateValueExpressionTest() throws ParserException {
		final Expression e = compileExpression("1+4/2-3*4");
		assertEquals(1.0+4/2-3*4, e.eval(), 0.0);
	}

	@Test
	public void evaluateFuncExpressionTest() throws ParserException {
		Expression e = compileExpression("sin(PI / 2.0) + cos(0) + E");
		assertEquals(Math.sin(Math.PI / 2.0) + Math.cos(0) + Math.E, e.eval(), 0.0);

		e = compileExpression("sin(PI / 4.0) * exp(100) / PI");
		assertEquals(Math.sin(Math.PI / 4.0) * Math.exp(100.0) / Math.PI, e.eval(), 0.0);
	}
}	