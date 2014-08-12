package org.dreamer.expression.calc;

import org.junit.rules.ExpectedException;
import org.junit.Test;
import org.junit.Rule;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class InfixToRPNConverterTest {
	public InfixToRPNConverterTest() {
	}

	@Test
	public void parseSimpleTest() {
		final InfixToRPNConverter converter = new InfixToRPNConverter();
		final ArrayList<ParsedToken> tokens = new ArrayList<ParsedToken>();
		tokens.add(new ParsedToken(new Lexem("1", 0), ParsedToken.Type.VALUE));
		tokens.add(new ParsedToken(new Lexem("+", 0), ParsedToken.Type.OP_ADD));
		tokens.add(new ParsedToken(new Lexem("2", 0), ParsedToken.Type.VALUE));
		final ArrayList<ParsedToken> tokensInRPN = converter.convert(tokens);

		assertEquals(3, tokensInRPN.size());
		assertEquals(ParsedToken.Type.VALUE, tokensInRPN.get(0).getType());
		assertEquals(ParsedToken.Type.VALUE, tokensInRPN.get(1).getType());
		assertEquals(ParsedToken.Type.OP_ADD, tokensInRPN.get(2).getType());
	}

	@Test
	public void parseExpression1Test() {
		final TokenParser tokenParser = new TokenParser();
		final LexemParser lexemParser = new LexemParser();
		final InfixToRPNConverter converter = new InfixToRPNConverter();
		final ArrayList<Lexem> lexems = lexemParser.parse("1 + 4 / 2");
		final ArrayList<ParsedToken> tokens = tokenParser.parse(lexems);
		assertEquals(5, tokens.size());

		final ArrayList<ParsedToken> tokensInRPN = converter.convert(tokens);
		final ParsedToken.Type [] expected = new ParsedToken.Type [] {
			ParsedToken.Type.VALUE,	ParsedToken.Type.VALUE, ParsedToken.Type.VALUE,
			ParsedToken.Type.OP_DIV, ParsedToken.Type.OP_ADD
		};

		assertEquals(expected.length, tokensInRPN.size());
		for (int i = 0; i < expected.length; ++i)
			assertEquals(expected[i], tokensInRPN.get(i).getType());
	}

	@Test
	public void parseExpression2Test() {
		final TokenParser tokenParser = new TokenParser();
		final LexemParser lexemParser = new LexemParser();
		final InfixToRPNConverter converter = new InfixToRPNConverter();
		final ArrayList<Lexem> lexems = lexemParser.parse("(1 + 4) / 2");
		final ArrayList<ParsedToken> tokens = tokenParser.parse(lexems);
		assertEquals(7, tokens.size());

		final ArrayList<ParsedToken> tokensInRPN = converter.convert(tokens);
		final ParsedToken.Type [] expected = new ParsedToken.Type [] {
			ParsedToken.Type.VALUE,	ParsedToken.Type.VALUE, ParsedToken.Type.OP_ADD,
			ParsedToken.Type.VALUE, ParsedToken.Type.OP_DIV
		};

		assertEquals(expected.length, tokensInRPN.size());
		for (int i = 0; i < expected.length; ++i)
			assertEquals(expected[i], tokensInRPN.get(i).getType());
	}
}