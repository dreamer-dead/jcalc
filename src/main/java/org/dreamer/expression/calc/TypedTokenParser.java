package org.dreamer.expression.calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

public class TypedTokenParser {
	private enum ParserState {
		EMPTY,
		OPEN_BRACKET,
		CLOSE_BRACKET,
		OPERATOR,
		VALUE,
		FUNC
	};

	public TypedTokenParser() {
		_operatorsAndConstants = new HashMap<String, TypedToken.Type>();
		_operatorsAndConstants.put("(", TypedToken.Type.OPEN_BRACKET);
		_operatorsAndConstants.put(")", TypedToken.Type.CLOSE_BRACKET);
		_operatorsAndConstants.put("+", TypedToken.Type.OP_ADD);
		_operatorsAndConstants.put("-", TypedToken.Type.OP_SUB);
		_operatorsAndConstants.put("*", TypedToken.Type.OP_MUL);
		_operatorsAndConstants.put("/", TypedToken.Type.OP_DIV);
		_operatorsAndConstants.put("E", TypedToken.Type.CONST);
		_operatorsAndConstants.put("PI", TypedToken.Type.CONST);
		_lastState = ParserState.EMPTY;
	}

	public ArrayList<TypedToken> parse(Collection<Token> tokens) {
		ArrayList<TypedToken> result = new ArrayList<TypedToken>();
		for (Token token : tokens)
			result.add(parseToken(token));

		finishParsing();
		return result;
	}

	private TypedToken parseToken(Token token) {
		TypedToken.Type type = parseTokenType(token);
		promoteParserState(type);
		return new TypedToken(token, type);
	}

	public TypedToken.Type parseTokenType(Token token) {
		final String tokenValue = token.getValue();
		if (_operatorsAndConstants.containsKey(tokenValue))
			return _operatorsAndConstants.get(tokenValue);

		if (checkOnlyLiterals(tokenValue))
			return TypedToken.Type.FUNC;

		try {
			Double.parseDouble(tokenValue);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid value!");
		}
		return TypedToken.Type.VALUE;
	}

	private static boolean checkOnlyLiterals(String token) {
		for (int i = 0; i < token.length(); ++i)
			if (!Character.isLetter(token.charAt(i)))
				return false;

		return true;
	}

	private void promoteParserState(TypedToken.Type type) {
		ParserState newState = ParserState.EMPTY;
		switch (type) {
		case OPEN_BRACKET: newState = ParserState.OPEN_BRACKET; break;
		case CLOSE_BRACKET: newState = ParserState.CLOSE_BRACKET;  break;
		case CONST:
		case VALUE: newState = ParserState.VALUE;  break;
		case OP_ADD:
		case OP_SUB:
		case OP_MUL:
		case OP_DIV: newState = ParserState.OPERATOR;  break;
		case FUNC: newState = ParserState.FUNC; break;
		}
		if (newState == ParserState.EMPTY)
			throw new IllegalArgumentException("Invalid token type!0");

		switch (_lastState) {
		case EMPTY:
		case OPERATOR:
			if (newState != ParserState.OPEN_BRACKET &&
				newState != ParserState.VALUE &&
				newState != ParserState.FUNC)
				throw new IllegalArgumentException("Invalid syntax!1");
			break;
		case OPEN_BRACKET:
			if (newState != ParserState.VALUE &&
				newState != ParserState.OPEN_BRACKET &&
				newState != ParserState.FUNC)
				throw new IllegalArgumentException("Invalid syntax!2");
			break;
		case CLOSE_BRACKET:
		case VALUE:
			if (newState != ParserState.OPERATOR &&
				newState != ParserState.CLOSE_BRACKET)
				throw new IllegalArgumentException("Invalid syntax!3");
			break;
		case FUNC:
			if (newState != ParserState.OPEN_BRACKET)
				throw new IllegalArgumentException("Invalid syntax!4");
			break;
		}
		_lastState = newState;
	}

	private void finishParsing() {
		switch (_lastState) {
		case CLOSE_BRACKET:
		case VALUE:
			_lastState = ParserState.EMPTY;
			return;
		}
		throw new IllegalArgumentException("Invalid syntax!6");
	}

	private HashMap<String, TypedToken.Type> _operatorsAndConstants;
	private ParserState _lastState;
}