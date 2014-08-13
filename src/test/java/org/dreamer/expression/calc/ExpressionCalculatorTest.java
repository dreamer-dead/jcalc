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

	@Test
	public void calculationSiteTest() {
		assertEquals(
			11+(Math.exp(2.010635+Math.sin(Math.PI/2)*3)+50)/2,
			_calculator.evaluate("11+(exp(2.010635+sin(PI/2)*3)+50)/2"), 
			0.001);
	}

	private ExpressionCalculator _calculator;
}	