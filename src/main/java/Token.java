package org.dreamer.expression.calc;

public class Token {
	public enum Type { OPERAND, OPERATOR }

	public Token(Type type) {
		_type = type;
	}

	public Type getType() {
		return _type;
	}

	private Type _type;
}