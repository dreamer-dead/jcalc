package org.dreamer.expression.calc;

public class TypedToken {
	public enum Type {
		VALUE,
		OPEN_BRACKET, CLOSE_BRACKET,
		OP_ADD, OP_SUB, OP_MUL, OP_DIV,
		FUNC,
		CONST
	}

	public TypedToken(Token token, Type type) {
		_type = type;
		_token = token;
	}

	public Type getType() {
		return _type;
	}

	public String getValue() {
		return _token.getValue();
	}

	public int getPosition() {
		return _token.getPosition();
	}

	public Token getTokenValue() {
		return _token;
	}

	public boolean isOperator() {
		return _type == Type.OP_ADD
			|| _type == Type.OP_SUB
			|| _type == Type.OP_MUL
			|| _type == Type.OP_DIV;
	}

	public int comparePriority(TypedToken other) {
		if (isOperator() && other.isOperator()) {
			return getPriority(_type) - getPriority(other.getType());
		}
		return 1;
	}

	private int getPriority(Type type) {
		if (type == Type.OP_MUL || type == Type.OP_DIV)
			return 2;

		return 1;
	}

	public boolean isFunction() {
		return _type == Type.FUNC;
	}

	public boolean isValue() {
		return _type == Type.VALUE
			|| _type == Type.CONST;
	}

	private Type _type;
	Token _token;
}