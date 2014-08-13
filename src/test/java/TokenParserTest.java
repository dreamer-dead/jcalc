package org.dreamer.expression.calc;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;	

public class TokenParserTest {
	public TokenParserTest() {}

	@Test
	public void priorityTokensTest() {
		final ParsedToken addToken = new ParsedToken(new Lexem("+", 0), ParsedToken.Type.OP_ADD);
		final ParsedToken subToken = new ParsedToken(new Lexem("-", 0), ParsedToken.Type.OP_SUB);
		final ParsedToken mulToken = new ParsedToken(new Lexem("*", 0), ParsedToken.Type.OP_MUL);
		final ParsedToken divToken = new ParsedToken(new Lexem("/", 0), ParsedToken.Type.OP_DIV);
		assertEquals(-1, addToken.comparePriority(divToken));
		assertEquals(1, divToken.comparePriority(addToken));
		assertEquals(1, mulToken.comparePriority(addToken));
		assertEquals(-1, addToken.comparePriority(mulToken));
	}

	@Test
	public void singleTokensTest() {
		final ParsedToken [] expected = new ParsedToken [] {
			new ParsedToken(new Lexem("1.02", 0), ParsedToken.Type.VALUE),
			new ParsedToken(new Lexem("+", 0), ParsedToken.Type.OP_ADD),
			new ParsedToken(new Lexem("-", 0), ParsedToken.Type.OP_SUB),
			new ParsedToken(new Lexem("*", 0), ParsedToken.Type.OP_MUL),
			new ParsedToken(new Lexem("/", 0), ParsedToken.Type.OP_DIV),
			new ParsedToken(new Lexem("E", 0), ParsedToken.Type.CONST),
			new ParsedToken(new Lexem("sin", 0), ParsedToken.Type.FUNC),
			new ParsedToken(new Lexem("cos", 0), ParsedToken.Type.FUNC),
			new ParsedToken(new Lexem("exp", 0), ParsedToken.Type.FUNC),
			new ParsedToken(new Lexem("(", 0), ParsedToken.Type.OPEN_BRACKET),
			new ParsedToken(new Lexem(")", 0), ParsedToken.Type.CLOSE_BRACKET)
		};
		final TokenParser tokenParser = new TokenParser();
		for (ParsedToken t : expected) {
			final ParsedToken.Type type = tokenParser.parseLexemType(t.getLexem());
			assertEquals(t.getType(), type);
		}
	}

	@Test
	public void expressionTokensTest() {
		final TokenParser tokenParser = new TokenParser();
		final LexemParser lexemParser = new LexemParser();
		final ArrayList<Lexem> lexems = lexemParser.parse("1 + 2");
		assertEquals(3, lexems.size());

		final ArrayList<ParsedToken> tokens = tokenParser.parse(lexems);
		assertEquals(3, tokens.size());
		assertEquals(ParsedToken.Type.VALUE, tokens.get(0).getType());
		assertEquals(ParsedToken.Type.OP_ADD, tokens.get(1).getType());
		assertEquals(ParsedToken.Type.VALUE, tokens.get(2).getType());
	}
}	