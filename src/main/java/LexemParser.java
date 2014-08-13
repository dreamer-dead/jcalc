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
		// TODO: Set initial capacity size.
		final StringBuilder currentLexemValue = new StringBuilder();
		int lexemPosition = 0;
		for (int i = 0; i < expressionLength;) {
			final char c = expression.charAt(i);
			if (_validCharacters.indexOf(c) < 0)
				throw new IllegalArgumentException(
					"Invalid expression syntax! (char '" + String.valueOf(c)
					+ "' at position " + String.valueOf(i) + ")");
			if (_singleCharLexems.indexOf(c) >= 0) {
				if (currentLexemValue.length() > 0) {
					result.add(new Lexem(currentLexemValue.toString(), lexemPosition));
					// Reset the buffer.
					currentLexemValue.setLength(0);
				}
				
				result.add(new Lexem(String.valueOf(c), i++));
			} else {
				if (Character.isDigit(c) || c == '.') {
					int nextPos = parseDigit(expression, i + 1);
					result.add(new Lexem(expression.substring(i, nextPos), i));
					i = nextPos;
				} else if (c == 's' || c == 'c' || c == 'e') {
					int nextPos = parseFunction(expression, i);
					result.add(new Lexem(expression.substring(i, nextPos), i));
					i = nextPos;
				} else if (c == 'P') {
					if (expression.length() <= i + 1)
						throw new IllegalArgumentException("Invalid syntax111!");

					if (expression.charAt(i + 1) != 'I')
						throw new IllegalArgumentException("Invalid syntax222!");
					result.add(new Lexem("PI", i));
					i += 2;
				}
			}
		}

		if (currentLexemValue.length() > 0) {
			result.add(new Lexem(currentLexemValue.toString(), lexemPosition));
			// Reset the buffer.
			currentLexemValue.setLength(0);
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

	private int parseFunction(String expression, int offset) {
		// Max length of function name is 3 now.
		final int nameEnd = offset + 3;
		if (expression.length() >= nameEnd) {
			final String name = expression.substring(offset, nameEnd);
			if (name.equals("sin") || name.equals("cos") || name.equals("exp"))
				return nameEnd;
		}

		throw new IllegalArgumentException("Invalid function name!");
	}

	private String _validCharacters;
	private String _singleCharLexems;
}