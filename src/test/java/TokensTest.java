package org.dreamer.expression.calc;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokensTest {
	public TokensTest() {}

	@Test
	public void operandsTest() {
		OperandToken token = new OperandToken(10.0);
		assertEquals(Token.Type.OPERAND, token.getType());
		assertEquals(10.0, token.getValue(), 0.0);

		OperandToken tokenNegative = new OperandToken(-666.0);
		assertEquals(Token.Type.OPERAND, tokenNegative.getType());
		assertEquals(-666.0, tokenNegative.getValue(), 0.0);
	}

	@Test
	public void operatorsCreationTest() {
		OperatorToken token = OperatorToken.add();
		assertEquals(Token.Type.OPERATOR, token.getType());
		assertEquals(OperatorToken.OperatorType.ADD, token.getOperatorType());

		token = OperatorToken.sub();
		assertEquals(Token.Type.OPERATOR, token.getType());
		assertEquals(OperatorToken.OperatorType.SUB, token.getOperatorType());

		token = OperatorToken.mul();
		assertEquals(Token.Type.OPERATOR, token.getType());
		assertEquals(OperatorToken.OperatorType.MUL, token.getOperatorType());

		token = OperatorToken.div();
		assertEquals(Token.Type.OPERATOR, token.getType());
		assertEquals(OperatorToken.OperatorType.DIV, token.getOperatorType());
	}

	@Test
	public void operatorsApplyingTest() {
		OperatorToken operator = OperatorToken.add();
		OperandToken t1 = new OperandToken(10.0);
		OperandToken t2 = new OperandToken(5.0);
		assertEquals(t1.getValue() + t2.getValue(), operator.apply(t1, t2), 0.001);
	}
}