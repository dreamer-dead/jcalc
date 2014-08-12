package org.dreamer.expression.calc;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;	

public class TokenParserTest {
	public TokenParserTest() {}

	@Test
	public void singleTokensTest() {
		final ParsedToken [] expected = new ParsedToken [] {
			new ParsedToken(new Lexem("1.02", 0), ParsedToken.Type.VALUE),
			new ParsedToken(new Lexem("+", 0), ParsedToken.Type.OP_ADD),
			new ParsedToken(new Lexem("-", 0), ParsedToken.Type.OP_SUB),
			new ParsedToken(new Lexem("*", 0), ParsedToken.Type.OP_MUL),
			new ParsedToken(new Lexem("/", 0), ParsedToken.Type.OP_DIV),
			new ParsedToken(new Lexem("E", 0), ParsedToken.Type.CONST_E),
			new ParsedToken(new Lexem("sin", 0), ParsedToken.Type.FUNC_SIN),
			new ParsedToken(new Lexem("cos", 0), ParsedToken.Type.FUNC_COS),
			new ParsedToken(new Lexem("exp", 0), ParsedToken.Type.FUNC_EXP),
			new ParsedToken(new Lexem("(", 0), ParsedToken.Type.OPEN_BRACKET),
			new ParsedToken(new Lexem(")", 0), ParsedToken.Type.CLOSE_BRACKET)
		};
		final TokenParser tokenParser = new TokenParser();
		for (ParsedToken t : expected) {
			ArrayList<Lexem> tmp = new ArrayList<Lexem>();
			tmp.add(t.getLexem());
			ArrayList<ParsedToken> tokens = tokenParser.parse(tmp);
			assertEquals(1, tokens.size());
			assertEquals(t.getType(), tokens.get(0).getType());
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