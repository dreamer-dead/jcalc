package org.dreamer.expression.calc;

public abstract class BinaryExpression implements Expression {
	public static Expression add(final Expression op1, final Expression op2) {
		return new BinaryExpression(op1, op2) {
			public double eval() {
				return _operand1.eval() + _operand2.eval();
			}
		};
	}

	public static Expression sub(final Expression op1, final Expression op2) {
		return new BinaryExpression(op1, op2) {
			public double eval() {
				return _operand1.eval() - _operand2.eval();
			}
		};
	}

	public static Expression mul(final Expression op1, final Expression op2) {
		return new BinaryExpression(op1, op2) {
			public double eval() {
				return _operand1.eval() * _operand2.eval();
			}
		};
	}

	public static Expression div(final Expression op1, final Expression op2) {
		return new BinaryExpression(op1, op2) {
			public double eval() {
				return _operand1.eval() / _operand2.eval();
			}
		};
	}

	public BinaryExpression(final Expression op1, final Expression op2) {
		_operand1 = op1;
		_operand2 = op2;
	}

	protected Expression _operand1;
	protected Expression _operand2;
}