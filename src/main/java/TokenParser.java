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
		_operatorsAndFunctions = new HashMap<String, ParsedToken.Type>();
		_operatorsAndFunctions.put("(", ParsedToken.Type.OPEN_BRACKET);
		_operatorsAndFunctions.put(")", ParsedToken.Type.CLOSE_BRACKET);
		_operatorsAndFunctions.put("+", ParsedToken.Type.OP_ADD);
		_operatorsAndFunctions.put("-", ParsedToken.Type.OP_SUB);
		_operatorsAndFunctions.put("*", ParsedToken.Type.OP_MUL);
		_operatorsAndFunctions.put("/", ParsedToken.Type.OP_DIV);
		_operatorsAndFunctions.put("E", ParsedToken.Type.CONST);
		_operatorsAndFunctions.put("PI", ParsedToken.Type.CONST);
		_operatorsAndFunctions.put("sin", ParsedToken.Type.FUNC);
		_operatorsAndFunctions.put("cos", ParsedToken.Type.FUNC);
		_operatorsAndFunctions.put("exp", ParsedToken.Type.FUNC);
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
		if (_operatorsAndFunctions.containsKey(lexem.getValue()))
			return _operatorsAndFunctions.get(lexem.getValue());

		try {
			Double.parseDouble(lexem.getValue());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid value!");
		}
		return ParsedToken.Type.VALUE;
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
		case OPERATOR:
			if (newState != ParserState.VALUE &&
				newState != ParserState.FUNC)
				throw new IllegalArgumentException("Invalid syntax!5");
			break;
		}
		_lastState = newState;
	}

	private HashMap<String, ParsedToken.Type> _operatorsAndFunctions;
	private ParserState _lastState;
}