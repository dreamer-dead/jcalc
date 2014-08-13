package org.dreamer.expression.calc;

public abstract class FunctionExpression implements Expression {
	public static Expression sin(final Expression p) {
		return new FunctionExpression(p) {
			public double eval() {
				return Math.sin(_parameter.eval());
			}
		};
	}

	public static Expression cos(final Expression p) {
		return new FunctionExpression(p) {
			public double eval() {
				return Math.cos(_parameter.eval());
			}
		};
	}

	public static Expression exp(final Expression p) {
		return new FunctionExpression(p) {
			public double eval() {
				return Math.exp(_parameter.eval());
			}
		};
	}

	public FunctionExpression(final Expression p) {
		_parameter = p;
	}

	protected Expression _parameter;
}