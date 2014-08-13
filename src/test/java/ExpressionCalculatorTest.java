package org.dreamer.expression.calc;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionCalculatorTest {
	public ExpressionCalculatorTest() {
		_calculator = new ExpressionCalculator();
	}

	@Test
	public void calculationTest() {
		assertEquals(1.0 + 1.0, _calculator.evaluate("1 + 1"), 0.0);
	}

	private ExpressionCalculator _calculator;
}	