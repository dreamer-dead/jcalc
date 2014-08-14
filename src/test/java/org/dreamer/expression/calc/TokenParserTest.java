package org.dreamer.expression.calc;

import org.junit.rules.ExpectedException;
import org.junit.Test;
import org.junit.Rule;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class TokenParserTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	public TokenParserTest() {
	}

	@Test
	public void parseSimpleTest() {
		final TokenParser parser = new TokenParser();
		final ArrayList<Token> tokens = parser.parse("1 + 1");
		assertEquals(3, tokens.size());
		assertEquals("1", tokens.get(0).getValue());
		assertEquals("+", tokens.get(1).getValue());
		assertEquals("1", tokens.get(2).getValue());
	}

	@Test
	public void parseParensTest() {
		final TokenParser parser = new TokenParser();
		final ArrayList<Token> tokens = parser.parse("1 +(3-2)");
		String [] expected = new String[] {
			"1", "+", "(", "3", "-", "2", ")"
		};
		assertEquals(expected.length, tokens.size());
		for (int i = 0; i < expected.length; ++i)
			assertEquals(expected[i], tokens.get(i).getValue());
	}

	@Test
	public void parseDotInDoubleTest() {
		final TokenParser parser = new TokenParser();
		final ArrayList<Token> tokens = parser.parse("1.44 + (5.0 - 2)");
		String [] expected = new String[] {
			"1.44", "+", "(", "5.0", "-", "2", ")"
		};
		assertEquals(expected.length, tokens.size());
		for (int i = 0; i < expected.length; ++i)
			assertEquals(expected[i], tokens.get(i).getValue());
	}

	@Test
	public void parseEmptyStringTest() {
		exception.expect(IllegalArgumentException.class);
		new TokenParser().parse("   ");
	}

	// @Test
	// public void parseInvalidSyntaxTest() {
	// 	exception.expect(IllegalArgumentException.class);
	// 	new TokenParser().parse("1a");
	// }

	private static void p(Iterable<Token> ls) {
		for (Token l : ls) {
			System.out.print(l.getValue());
			System.out.print(", ");
		}
		System.out.println("!");
	}

	@Test
	public void parseFuncTest() {
		final TokenParser parser = new TokenParser();
		final ArrayList<Token> tokens = parser.parse("sin(1)");
		assertEquals(4, tokens.size());
		assertEquals("sin", tokens.get(0).getValue());
		assertEquals("(", tokens.get(1).getValue());
		assertEquals("1", tokens.get(2).getValue());
		assertEquals(")", tokens.get(3).getValue());
	}

	@Test
	public void parseConstTest() {
		final TokenParser parser = new TokenParser();
		final ArrayList<Token> tokens = parser.parse("E + PI");
		assertEquals(3, tokens.size());
		assertEquals("E", tokens.get(0).getValue());
		assertEquals("+", tokens.get(1).getValue());
		assertEquals("PI", tokens.get(2).getValue());
	}

	@Test
	public void parseFuncAndConstTest() {
		final TokenParser parser = new TokenParser();
		final ArrayList<Token> tokens = parser.parse("cos(2 * PI)");
		assertEquals(6, tokens.size());
		assertEquals("cos", tokens.get(0).getValue());
		assertEquals("(", tokens.get(1).getValue());
		assertEquals("2", tokens.get(2).getValue());
		assertEquals("*", tokens.get(3).getValue());
		assertEquals("PI", tokens.get(4).getValue());
		assertEquals(")", tokens.get(5).getValue());
	}

	@Test
	public void parseUnknownLiteralsTest() {
		final TokenParser parser = new TokenParser();
		final ArrayList<Token> tokens = parser.parse("cso + cccc - IPI");
		assertEquals(5, tokens.size());
		assertEquals("cso", tokens.get(0).getValue());
		assertEquals("+", tokens.get(1).getValue());
		assertEquals("cccc", tokens.get(2).getValue());
		assertEquals("-", tokens.get(3).getValue());
		assertEquals("IPI", tokens.get(4).getValue());
	}
}