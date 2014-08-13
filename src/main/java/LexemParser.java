package org.dreamer.expression.calc;

import java.util.HashSet;
import java.util.ArrayList;

public class LexemParser {
	public LexemParser() {
		_validCharacters = "()+-*/.0123456789EPIsincosexp";
		_singleCharLexems = "()+-*/E";
	}

	public ArrayList<Lexem> parse(String expression) {
		String trimmedExpression = expression.replaceAll("\\s", "");
		if (trimmedExpression.isEmpty())
			throw new IllegalArgumentException("Empty expression!");

		return parseInternal(trimmedExpression);
	}

	private ArrayList<Lexem> parseInternal(String expression) {
		ArrayList<Lexem> result = new ArrayList<Lexem>();
		final int expressionLength = expression.length();
		for (int i = 0; i < expressionLength;) {
			final char c = expression.charAt(i);
			if (_validCharacters.indexOf(c) < 0)
				throw new IllegalArgumentException(
					"Invalid expression syntax! (char '" + String.valueOf(c)
					+ "' at position " + String.valueOf(i) + ")");
			if (_singleCharLexems.indexOf(c) >= 0) {
				result.add(new Lexem(String.valueOf(c), i++));
			} else {
				int nextPos = i + 1;
				if (Character.isDigit(c) || c == '.') {
					nextPos = parseDigit(expression, nextPos);
				} else {
					nextPos = parseLiteral(expression, nextPos);
				}
				result.add(new Lexem(expression.substring(i, nextPos), i));
				i = nextPos;
			}
		}

		return result;
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
	private String _singleCharLexems;
}