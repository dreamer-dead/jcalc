package org.dreamer.expression.calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

public class TokenParser {
	private enum ParserState {
		EMPTY,
		OPEN_BRACKET,
		CLOSE_BRACKET,
		OPERATOR,
		VALUE,
		FUNC
	};

	public TokenParser() {
		_operatorsAndConstants = new HashMap<String, ParsedToken.Type>();
		_operatorsAndConstants.put("(", ParsedToken.Type.OPEN_BRACKET);
		_operatorsAndConstants.put(")", ParsedToken.Type.CLOSE_BRACKET);
		_operatorsAndConstants.put("+", ParsedToken.Type.OP_ADD);
		_operatorsAndConstants.put("-", ParsedToken.Type.OP_SUB);
		_operatorsAndConstants.put("*", ParsedToken.Type.OP_MUL);
		_operatorsAndConstants.put("/", ParsedToken.Type.OP_DIV);
		_operatorsAndConstants.put("E", ParsedToken.Type.CONST);
		_operatorsAndConstants.put("PI", ParsedToken.Type.CONST);
		_lastState = ParserState.EMPTY;
	}

	public ArrayList<ParsedToken> parse(Collection<Lexem> lexems) {
		ArrayList<ParsedToken> tokens = new ArrayList<ParsedToken>();
		for (Lexem lexem : lexems)
			tokens.add(parseLexem(lexem));
		return tokens;
	}

	private ParsedToken parseLexem(Lexem lexem) {
		ParsedToken.Type type = parseLexemType(lexem);
		promoteParserState(type);
		return new ParsedToken(lexem, type);
	}

	public ParsedToken.Type parseLexemType(Lexem lexem) {
		final String lexemValue = lexem.getValue();
		if (_operatorsAndConstants.containsKey(lexemValue))
			return _operatorsAndConstants.get(lexemValue);

		if (checkOnlyLiterals(lexemValue))
			return ParsedToken.Type.FUNC;

		try {
			Double.parseDouble(lexemValue);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid value!");
		}
		return ParsedToken.Type.VALUE;
	}

	private static boolean checkOnlyLiterals(String lexem) {
		for (int i = 0; i < lexem.length(); ++i)
			if (!Character.isLetter(lexem.charAt(i)))
				return false;

		return true;
	}

	private void promoteParserState(ParsedToken.Type type) {
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

	private HashMap<String, ParsedToken.Type> _operatorsAndConstants;
	private ParserState _lastState;
}