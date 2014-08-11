package org.dreamer.expression.calc;

public class OperatorToken extends Token {
	public static OperatorToken add() {
		return new OperatorToken(OperatorType.ADD);
	}

	public static OperatorToken sub() {
		return new OperatorToken(OperatorType.SUB);
	}

	public static OperatorToken mul() {
		return new OperatorToken(OperatorType.MUL);
	}

	public static OperatorToken div() {
		return new OperatorToken(OperatorType.DIV);
	}

	public enum OperatorType { ADD, SUB, MUL, DIV }

	public OperatorToken(OperatorType operatorType) {
		super(Token.Type.OPERATOR);
		_operatorType = operatorType;
	}

	public double apply(OperandToken lvalue, OperandToken rvalue) {
		return 0.0;
	}

	public OperatorType getOperatorType() {
		return _operatorType;
	}

	private OperatorType _operatorType;

}