package org.dreamer.expression.calc;

public class ValueExpression implements Expression {
	public static Expression constant(String name) {
		if (name.equals("E"))
			return new ValueExpression(Math.E);
		else if (name.equals("PI"))
			return new ValueExpression(Math.PI);

		throw ExceptionsHelper.unresolvedConstant(name);
	}

	public ValueExpression(double value) {
		_value = value;
	}

	public double eval() {
		return _value;
	}

	private double _value;
}