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

	public ArrayList<TypedToken> parse(Collection<Token> tokens) throws ParserException {
		ArrayList<TypedToken> result = new ArrayList<TypedToken>();
		for (Token token : tokens)
			result.add(parseToken(token));

		finishParsing();
		return result;
	}

	private TypedToken parseToken(Token token) throws ParserException {
		TypedToken.Type type = parseTokenType(token);
		final TypedToken result = new TypedToken(token, type);
		promoteParserState(result);
		return result;
	}

	public TypedToken.Type parseTokenType(Token token) throws ParserException {
		final String tokenValue = token.getValue();
		if (_operatorsAndConstants.containsKey(tokenValue))
			return _operatorsAndConstants.get(tokenValue);

		if (checkOnlyLiterals(tokenValue))
			return TypedToken.Type.FUNC;

		try {
			Double.parseDouble(tokenValue);
		} catch (NumberFormatException e) {
			throw ExceptionsHelper.parseNumberError(token);
		}
		return TypedToken.Type.VALUE;
	}

	private static boolean checkOnlyLiterals(String token) {
		for (int i = 0; i < token.length(); ++i)
			if (!Character.isLetter(token.charAt(i)))
				return false;

		return true;
	}

	private void promoteParserState(TypedToken token) throws ParserException {
		ParserState newState = ParserState.EMPTY;
		switch (token.getType()) {
		case OPEN_BRACKET: newState = ParserState.OPEN_BRACKET; break;
		case CLOSE_BRACKET: newState = ParserState.CLOSE_BRACKET; break;
		case CONST:
		case VALUE: newState = ParserState.VALUE;  break;
		case OP_ADD:
		case OP_SUB:
		case OP_MUL:
		case OP_DIV: newState = ParserState.OPERATOR;  break;
		case FUNC: newState = ParserState.FUNC; break;
		}
		if (newState == ParserState.EMPTY)
			throw ExceptionsHelper.invalidToken(token);

		switch (_lastState) {
		case EMPTY:
		case OPERATOR:
			if (newState != ParserState.OPEN_BRACKET &&
				newState != ParserState.VALUE &&
				newState != ParserState.FUNC)
				throw ExceptionsHelper.invalidSyntax(_lastState.toString(), newState.toString(), token);
			break;
		case OPEN_BRACKET:
			if (newState != ParserState.VALUE &&
				newState != ParserState.OPEN_BRACKET &&
				newState != ParserState.FUNC)
				throw ExceptionsHelper.invalidSyntax(_lastState.toString(), newState.toString(), token);
			break;
		case CLOSE_BRACKET:
		case VALUE:
			if (newState != ParserState.OPERATOR &&
				newState != ParserState.CLOSE_BRACKET)
				throw ExceptionsHelper.invalidSyntax(_lastState.toString(), newState.toString(), token);
			break;
		case FUNC:
			if (newState != ParserState.OPEN_BRACKET)
				throw ExceptionsHelper.invalidSyntax(_lastState.toString(), newState.toString(), token);
			break;
		}
		_lastState = newState;
	}

	private void finishParsing() throws ParserException {
		switch (_lastState) {
		case CLOSE_BRACKET:
		case VALUE:
			_lastState = ParserState.EMPTY;
			return;
		}
		throw ExceptionsHelper.invalidSyntaxAtEnd(_lastState.toString());
	}

	private HashMap<String, TypedToken.Type> _operatorsAndConstants;
	private ParserState _lastState;
}