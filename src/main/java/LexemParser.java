package org.dreamer.expression.calc;

import java.util.HashSet;
import java.util.ArrayList;

public class LexemParser {
	public LexemParser() {
		_validCharacters = "()+-*/.0123456789EPsinexp";
		_singleCharLexems = "()+-*/EP";
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
		for (int i = 0; i < expressionLength; ++i) {
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
				
				result.add(new Lexem(String.valueOf(c), i));
			} else {
				if (currentLexemValue.length() == 0)
					lexemPosition = i;
				if (Character.isDigit(c) || c == '.') {
					currentLexemValue.append(c);
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

	private String _validCharacters;
	private String _singleCharLexems;
}