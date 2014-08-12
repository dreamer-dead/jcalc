package org.dreamer.expression.calc;

public class ParsedToken {
	public enum Type {
		VALUE, OPEN_BRACKET, CLOSE_BRACKET,
		OP_ADD, OP_SUB, OP_MUL, OP_DIV,
		FUNC_SIN, FUNC_COS, FUNC_EXP,
		CONST_E, CONST_PI
	}

	public ParsedToken(Lexem lexem, Type type) {
		_type = type;
		_lexem = lexem;
	}

	public Type getType() {
		return _type;
	}

	public Lexem getLexem() {
		return _lexem;
	}

	public boolean isOperator() {
		return _type == Type.OP_ADD
			|| _type == Type.OP_SUB
			|| _type == Type.OP_MUL
			|| _type == Type.OP_DIV;
	}

	public int comparePriority(ParsedToken other) {
		if (isOperator() && other.isOperator()) {
			//return compareOperatorPriority(_type, other.getType());
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
		return _type == Type.FUNC_SIN
			|| _type == Type.FUNC_COS
			|| _type == Type.FUNC_EXP;
	}

	public boolean isValue() {
		return _type == Type.VALUE
			|| _type == Type.CONST_E
			|| _type == Type.CONST_PI;
	}

	private Type _type;
	Lexem _lexem;
}