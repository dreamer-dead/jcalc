package org.dreamer.expression.calc;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Stack;
import java.util.ArrayList;

public class PolishNotationTokensEvaluatorTest {
	public PolishNotationTokensEvaluatorTest() {}

	@Test
	public void evaluateValueTest() {
		final ParsedToken tokenVal = new ParsedToken(new Lexem("1.02", 0), ParsedToken.Type.VALUE);
		final ParsedToken tokenE = new ParsedToken(new Lexem("E", 0), ParsedToken.Type.CONST);
		final ParsedToken tokenPI = new ParsedToken(new Lexem("PI", 0), ParsedToken.Type.CONST);
		final Stack<Expression> stack = new Stack<Expression>();
		final Expression v = PolishNotationTokensEvaluator.evaluateFromToken(tokenVal, stack);
		assertEquals(1.02, v.eval(), 0.01);

		final Expression e = PolishNotationTokensEvaluator.evaluateFromToken(tokenE, stack);
		assertEquals(Math.E, e.eval(), 0.01);

		final Expression pi = PolishNotationTokensEvaluator.evaluateFromToken(tokenPI, stack);
		assertEquals(Math.PI, pi.eval(), 0.01);
	}

	@Test
	public void evaluateOpsTest() {
		Expression val = new ValueExpression(1.02);
		final Expression valE = new ValueExpression(Math.E);
		ParsedToken tokenOperator = new ParsedToken(new Lexem("+", 0), ParsedToken.Type.OP_ADD);
		final Stack<Expression> stack = new Stack<Expression>();
		stack.push(val);
		stack.push(valE);
		Expression v = PolishNotationTokensEvaluator.evaluateFromToken(tokenOperator, stack);
		assertEquals(val.eval() + valE.eval(), v.eval(), 0.01);
		assertTrue(stack.empty());

		stack.push(valE);
		stack.push(val);
		tokenOperator = new ParsedToken(new Lexem("-", 0), ParsedToken.Type.OP_SUB);
		v = PolishNotationTokensEvaluator.evaluateFromToken(tokenOperator, stack);
		assertEquals(valE.eval() - val.eval(), v.eval(), 0.01);
		assertTrue(stack.empty());

		val = new ValueExpression(3.42);
		stack.push(valE);
		stack.push(val);
		tokenOperator = new ParsedToken(new Lexem("*", 0), ParsedToken.Type.OP_MUL);
		v = PolishNotationTokensEvaluator.evaluateFromToken(tokenOperator, stack);
		assertEquals(valE.eval() * val.eval(), v.eval(), 0.01);
		assertTrue(stack.empty());

		val = new ValueExpression(123.42);
		stack.push(val);
		stack.push(valE);
		tokenOperator = new ParsedToken(new Lexem("/", 0), ParsedToken.Type.OP_DIV);
		v = PolishNotationTokensEvaluator.evaluateFromToken(tokenOperator, stack);
		assertEquals(val.eval() / valE.eval(), v.eval(), 0.01);
		assertTrue(stack.empty());
	}

	@Test
	public void evaluateFuncsTest() {
		final Expression valPI = new ValueExpression(Math.PI);
		ParsedToken tokenFunc = new ParsedToken(new Lexem("sin", 0), ParsedToken.Type.FUNC);
		final Stack<Expression> stack = new Stack<Expression>();
		stack.push(valPI);
		Expression v = PolishNotationTokensEvaluator.evaluateFromToken(tokenFunc, stack);
		assertEquals(Math.sin(valPI.eval()), v.eval(), 0.01);
		assertTrue(stack.empty());

		stack.push(valPI);
		tokenFunc = new ParsedToken(new Lexem("cos", 0), ParsedToken.Type.FUNC);
		v = PolishNotationTokensEvaluator.evaluateFromToken(tokenFunc, stack);
		assertEquals(Math.cos(valPI.eval()), v.eval(), 0.01);
		assertTrue(stack.empty());

		stack.push(valPI);
		tokenFunc = new ParsedToken(new Lexem("exp", 0), ParsedToken.Type.FUNC);
		v = PolishNotationTokensEvaluator.evaluateFromToken(tokenFunc, stack);
		assertEquals(Math.exp(valPI.eval()), v.eval(), 0.01);
		assertTrue(stack.empty());
	}

	private static Expression evaluateExpression(String expression) {
		final PolishNotationTokensEvaluator evaluator = new PolishNotationTokensEvaluator();
		final TokenParser tokenParser = new TokenParser();
		final LexemParser lexemParser = new LexemParser();
		final InfixToRPNConverter converter = new InfixToRPNConverter();
		final ArrayList<Lexem> lexems = lexemParser.parse(expression);
		final ArrayList<ParsedToken> tokens = tokenParser.parse(lexems);
		final ArrayList<ParsedToken> tokensInRPN = converter.convert(tokens);
		return evaluator.evaluate(tokensInRPN);
	}

	@Test
	public void evaluateExpressionTest() {
		final Expression e = evaluateExpression("1 + 2");
		assertEquals(3.0, e.eval(), 0.0);
	}

	@Test
	public void evaluateValueExpressionTest() {
		final Expression e = evaluateExpression("1+4/2-3*4");
		assertEquals(1.0+4/2-3*4, e.eval(), 0.0);
	}

	@Test
	public void evaluateFuncExpressionTest() {
		Expression e = evaluateExpression("sin(PI / 2.0) + cos(0) + E");
		assertEquals(Math.sin(Math.PI / 2.0) + Math.cos(0) + Math.E, e.eval(), 0.0);

		e = evaluateExpression("sin(PI / 4.0) * exp(100) / PI");
		assertEquals(Math.sin(Math.PI / 4.0) * Math.exp(100.0) / Math.PI, e.eval(), 0.0);
	}
}	