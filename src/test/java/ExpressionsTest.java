package org.dreamer.expression.calc;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionsTest {
	public ExpressionsTest() {}

	@Test
	public void constExpressionsTest() {
		assertEquals(Math.E, ValueExpression.constant("E").eval(), 0.0);
		assertEquals(Math.PI, ValueExpression.constant("PI").eval(), 0.0);
	}

	@Test
	public void binaryExpressionsTest() {
		final ValueExpression v1 = new ValueExpression(10.0);
		final ValueExpression v2 = new ValueExpression(5.0);
		assertEquals(v1.eval() + v2.eval(), BinaryExpression.add(v1, v2).eval(), 0.0);
		assertEquals(v1.eval() - v2.eval(), BinaryExpression.sub(v1, v2).eval(), 0.0);
		assertEquals(v1.eval() * v2.eval(), BinaryExpression.mul(v1, v2).eval(), 0.0);
		assertEquals(v1.eval() / v2.eval(), BinaryExpression.div(v1, v2).eval(), 0.0);
	}
}	