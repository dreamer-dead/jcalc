package org.dreamer.expression.calc;

import java.util.HashSet;
import java.util.ArrayList;

public class TokenParser {
	public TokenParser() {
		_validCharacters = "()+-*/.0123456789EPIsincosexp";
		_singleCharTokens = "()+-*/E";
	}

	public ArrayList<Token> parse(String expression) {
		String trimmedExpression = expression.trim();
		if (trimmedExpression.isEmpty())
			throw ExceptionsHelper.emptyExpression();

		return parseInternal(trimmedExpression);
	}

	private ArrayList<Token> parseInternal(String expression) {
		ArrayList<Token> result = new ArrayList<Token>();
		final int expressionLength = expression.length();
		for (int i = 0; i < expressionLength;) {
			final char c = expression.charAt(i);
			if (Character.isWhitespace(c)) {
				i = skipWhitespace(expression, i + 1);
				continue;
			}
			if (_validCharacters.indexOf(c) < 0)
				throw ExceptionsHelper.unsupportedCharacter(c, i);
			if (_singleCharTokens.indexOf(c) >= 0) {
				result.add(new Token(String.valueOf(c), i++));
			} else {
				int nextPos = i + 1;
				if (Character.isDigit(c) || c == '.') {
					nextPos = parseDigit(expression, nextPos);
				} else {
					nextPos = parseLiteral(expression, nextPos);
				}
				result.add(new Token(expression.substring(i, nextPos), i));
				i = nextPos;
			}
		}

		return result;
	}

	private int skipWhitespace(String expression, int offset) {
		for (int i = offset; i < expression.length(); ++i) {
			final char c = expression.charAt(i);
			if (!Character.isWhitespace(c))
				return i;
		}
		return expression.length();
	}

	private int parseDigit(String expression, int offset) {
		for (int i = offset; i < expression.length(); ++i) {
			final char c = expression.charAt(i);
			if (!Character.isDigit(c) && c != '.')
				return i;
		}
		return expression.length();
	}

	private int parseLiteral(String expression, int offset) {
		for (int i = offset; i < expression.length(); ++i) {
			final char c = expression.charAt(i);
			if (!Character.isAlphabetic(c))
				return i;
		}
		return expression.length();
	}

	private String _validCharacters;
	private String _singleCharTokens;
}