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
		switch (_operatorType) {
		case ADD:
			return lvalue.getValue() + rvalue.getValue();
		case SUB:
			return lvalue.getValue() - rvalue.getValue();
		case MUL:
			return lvalue.getValue() * rvalue.getValue();
		case DIV:
			return lvalue.getValue() / rvalue.getValue();
		default:
			return 0.0;
		}
	}

	public OperatorType getOperatorType() {
		return _operatorType;
	}

	private OperatorType _operatorType;

}