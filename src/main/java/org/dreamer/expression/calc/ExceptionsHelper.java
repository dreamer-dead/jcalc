package org.dreamer.expression.calc;

public class ExceptionsHelper {
	public static IllegalArgumentException emptyExpression() {
		return new IllegalArgumentException("Empty expression!");
	}

	public static IllegalArgumentException missingOpenBracket() {
		return new IllegalArgumentException("Missing open bracket!");
	}

	public static IllegalArgumentException stackSizeError(int actualSize, int expectedSize) {
		final String message = String.format(
			"Stack size is %d, but expected %d", actualSize, expectedSize
		);
		return new IllegalArgumentException(message);
	}

	public static IllegalArgumentException unresolvedFunction(TypedToken token) {
		final String message = String.format(
			"Unresolved function name: %s at %d!", token.getValue(), token.getPosition()
		);
		return new IllegalArgumentException(message);
	}

	public static IllegalArgumentException unresolvedToken(TypedToken token) {
		final String message = String.format(
			"Unresolved token: %s at %d!", token.getValue(), token.getPosition()
		);
		return new IllegalArgumentException(message);
	}

	public static IllegalArgumentException unresolvedConstant(String name) {
		final String message = String.format(
			"Unresolved constant: %s!", name
		);
		return new IllegalArgumentException(message);
	}

	public static IllegalArgumentException unsupportedCharacter(char c, int position) {
		final String message = String.format(
			"Invalid expression syntax! (char '%c' at position %d)", c, position
		);
		return new IllegalArgumentException(message);
	}

	// Parser errors.
	public static ParserException parseNumberError(Token token) {
		final String message = String.format(
			"Can't parse number from '%s' at %d!", token.getValue(), token.getPosition()
		);
		return new ParserException(message);
	}

	public static ParserException invalidToken(TypedToken token) {
		final String message = String.format(
			"Invalid token '%s' at %d", token.getValue(), token.getPosition()
		);
		return new ParserException(message);
	}

	public static ParserException invalidSyntax(String before, String after, TypedToken token) {
		final String message = String.format(
			"Invalid syntax, %s after %s (token '%s' at %d)", after, before, token.getValue(), token.getPosition()
		);
		return new ParserException(message);
	}

	public static ParserException invalidSyntaxAtEnd(String lastToken) {
		final String message = String.format(
			"Invalid syntax, %s as last token!", lastToken
		);
		return new ParserException(message);
	}
}