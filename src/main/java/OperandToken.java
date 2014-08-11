package org.dreamer.expression.calc;

public class OperandToken extends Token {
	public OperandToken(double value) {
		super(Token.Type.OPERAND);
		_value = value;
	}

	public double getValue() {
		return _value;
	}

	private double _value;
}