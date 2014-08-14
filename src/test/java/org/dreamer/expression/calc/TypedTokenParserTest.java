package org.dreamer.expression.calc;

import org.junit.Test;
import org.junit.Rule;
import static org.junit.Assert.*;
import org.junit.rules.ExpectedException;
import java.util.ArrayList;

public class TypedTokenParserTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	public TypedTokenParserTest() {}

	@Test
	public void priorityTokensTest() {
		final TypedToken addToken = new TypedToken(new Token("+", 0), TypedToken.Type.OP_ADD);
		final TypedToken subToken = new TypedToken(new Token("-", 0), TypedToken.Type.OP_SUB);
		final TypedToken mulToken = new TypedToken(new Token("*", 0), TypedToken.Type.OP_MUL);
		final TypedToken divToken = new TypedToken(new Token("/", 0), TypedToken.Type.OP_DIV);
		assertEquals(-1, addToken.comparePriority(divToken));
		assertEquals(1, divToken.comparePriority(addToken));
		assertEquals(1, mulToken.comparePriority(addToken));
		assertEquals(-1, addToken.comparePriority(mulToken));
	}

	@Test
	public void singleTokensTest() throws ParserException {
		final TypedToken [] expected = new TypedToken [] {
			new TypedToken(new Token("1.02", 0), TypedToken.Type.VALUE),
			new TypedToken(new Token("+", 0), TypedToken.Type.OP_ADD),
			new TypedToken(new Token("-", 0), TypedToken.Type.OP_SUB),
			new TypedToken(new Token("*", 0), TypedToken.Type.OP_MUL),
			new TypedToken(new Token("/", 0), TypedToken.Type.OP_DIV),
			new TypedToken(new Token("E", 0), TypedToken.Type.CONST),
			new TypedToken(new Token("sin", 0), TypedToken.Type.FUNC),
			new TypedToken(new Token("cos", 0), TypedToken.Type.FUNC),
			new TypedToken(new Token("exp", 0), TypedToken.Type.FUNC),
			new TypedToken(new Token("(", 0), TypedToken.Type.OPEN_BRACKET),
			new TypedToken(new Token(")", 0), TypedToken.Type.CLOSE_BRACKET)
		};
		final TypedTokenParser tokenParser = new TypedTokenParser();
		for (TypedToken t : expected) {
			final TypedToken.Type type = tokenParser.parseTokenType(t.getTokenValue());
			assertEquals(t.getType(), type);
		}
	}

	@Test
	public void expressionTokensTest() throws ParserException {
		final TypedTokenParser typedTokenParser = new TypedTokenParser();
		final TokenParser tokenParser = new TokenParser();
		final ArrayList<Token> tokens = tokenParser.parse("1 + 2");
		assertEquals(3, tokens.size());

		final ArrayList<TypedToken> typedTokens = typedTokenParser.parse(tokens);
		assertEquals(3, typedTokens.size());
		assertEquals(TypedToken.Type.VALUE, typedTokens.get(0).getType());
		assertEquals(TypedToken.Type.OP_ADD, typedTokens.get(1).getType());
		assertEquals(TypedToken.Type.VALUE, typedTokens.get(2).getType());
	}

	@Test
	public void parseNumberTest() throws ParserException {
		final TypedTokenParser tokenParser = new TypedTokenParser();
		final Token numToken = new Token("111.222", 0);
		assertEquals(TypedToken.Type.VALUE, tokenParser.parseTokenType(numToken));

		exception.expect(ParserException.class);
		final Token wrongToken = new Token("111,222", 0);
		tokenParser.parseTokenType(wrongToken);
	}
}	