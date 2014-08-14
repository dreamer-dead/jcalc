package org.dreamer.expression.calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.text.ParseException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;

/**
* Class that detects token type from token value.
*/
public class TypedTokenParser {
	// Internal state of the parser.
	private enum ParserState {
		EMPTY,
		OPEN_BRACKET,
		CLOSE_BRACKET,
		OPERATOR,
		VALUE,
		FUNC
	};

	public TypedTokenParser() {
		// Define all known tokens like operators and constants.
		_operatorsAndConstants = new HashMap<String, TypedToken.Type>();
		_operatorsAndConstants.put("(", TypedToken.Type.OPEN_BRACKET);
		_operatorsAndConstants.put(")", TypedToken.Type.CLOSE_BRACKET);
		_operatorsAndConstants.put("+", TypedToken.Type.OP_ADD);
		_operatorsAndConstants.put("-", TypedToken.Type.OP_SUB);
		_operatorsAndConstants.put("*", TypedToken.Type.OP_MUL);
		_operatorsAndConstants.put("/", TypedToken.Type.OP_DIV);
		_operatorsAndConstants.put("E", TypedToken.Type.CONST);
		_operatorsAndConstants.put("PI", TypedToken.Type.CONST);

		// Starting with empty state.
		_lastState = ParserState.EMPTY;
	}

	public ArrayList<TypedToken> parse(Iterable<Token> tokens) throws ParserException {
		ArrayList<TypedToken> result = new ArrayList<TypedToken>();
		// Process all tokens.
		for (Token token : tokens)
			result.add(parseToken(token));

		// Check our state at the end.
		finishParsing();
		return result;
	}

	private TypedToken parseToken(Token token) throws ParserException {
		final TypedToken.Type type = parseTokenType(token);
		final TypedToken result = new TypedToken(token, type);
		// Check expression syntax.
		promoteParserState(result);
		return result;
	}

	public TypedToken.Type parseTokenType(Token token) throws ParserException {
		final String tokenValue = token.getValue();
		if (_operatorsAndConstants.containsKey(tokenValue))
			return _operatorsAndConstants.get(tokenValue);

		if (checkOnlyLiterals(tokenValue))
			return TypedToken.Type.FUNC;

		// It can be only a number, so check that.
		try {
			if (null == parseNumber(tokenValue))
				throw ExceptionsHelper.parseNumberError(token);
		} catch (ParseException e) {
			throw ExceptionsHelper.parseNumberError(token);
		}
		return TypedToken.Type.VALUE;
	}

	private static Number parseNumber(String input) throws ParseException {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		ParsePosition parsePosition = new ParsePosition(0);
		final Number result = df.parse(input, parsePosition);

		if (parsePosition.getIndex() != input.length()) {
			throw new ParseException("Invalid input", parsePosition.getIndex());
		}

		return result;
	}

	private static boolean checkOnlyLiterals(String token) {
		for (int i = 0; i < token.length(); ++i)
			if (!Character.isLetter(token.charAt(i)))
				return false;

		return true;
	}

	// Here we check the order of tokens.
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
			// First token right after start or after operator should be
			// '(', number/constant or function name.
			if (newState != ParserState.OPEN_BRACKET &&
				newState != ParserState.VALUE &&
				newState != ParserState.FUNC)
				throw ExceptionsHelper.invalidSyntax(_lastState.toString(), newState.toString(), token);
			break;
		case OPEN_BRACKET:
			// After '(' there should be a number, one more '(' or function name.
			if (newState != ParserState.VALUE &&
				newState != ParserState.OPEN_BRACKET &&
				newState != ParserState.FUNC)
				throw ExceptionsHelper.invalidSyntax(_lastState.toString(), newState.toString(), token);
			break;
		case CLOSE_BRACKET:
		case VALUE:
			// After number or constant there should be operator or ')'.
			if (newState != ParserState.OPERATOR &&
				newState != ParserState.CLOSE_BRACKET)
				throw ExceptionsHelper.invalidSyntax(_lastState.toString(), newState.toString(), token);
			break;
		case FUNC:
			// And only '(' should follow after function name.
			if (newState != ParserState.OPEN_BRACKET)
				throw ExceptionsHelper.invalidSyntax(_lastState.toString(), newState.toString(), token);
			break;
		}
		_lastState = newState;
	}

	private void finishParsing() throws ParserException {
		// Check that expression ends with ')' or number/constant.
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