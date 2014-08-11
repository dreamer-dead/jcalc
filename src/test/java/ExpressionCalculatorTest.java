package org.dreamer.expression.calc;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionCalculatorTest {
	public ExpressionCalculatorTest() {}

	@Test
	public void calculationTest() {
		ExpressionCalculator calculator = new ExpressionCalculator();
		double result = calculator.evaluate("1 + 1");
		assertEquals(1.0, result);
	}
}