package org.dreamer.expression.calc;

/**
* Simple class to represent text token.
* For example, '+', '-', 'sin' is a tokens.
*/
public class Token {
	public Token(String value, int position) {
		_value = value;
		_position = position;
	}

	public String getValue() {
		return _value;
	}

	public int getPosition() {
		return _position;
	}

	protected String _value;
	protected int _position;
}