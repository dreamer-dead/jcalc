package org.dreamer.expression.calc;

import org.junit.rules.ExpectedException;
import org.junit.Test;
import org.junit.Rule;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class LexemParserTest {
	@Rule
  	public ExpectedException exception = ExpectedException.none();

	public LexemParserTest() {
	}

	@Test
	public void parseSimpleTest() {
		final LexemParser parser = new LexemParser();
		final ArrayList<Lexem> lexems = parser.parse("1 + 1");
		assertEquals(3, lexems.size());
		assertEquals("1", lexems.get(0).getValue());
		assertEquals("+", lexems.get(1).getValue());
		assertEquals("1", lexems.get(2).getValue());
	}

	@Test
	public void parseParensTest() {
		final LexemParser parser = new LexemParser();
		final ArrayList<Lexem> lexems = parser.parse("1 +(3-2)");
		String [] expected = new String[] {
			"1", "+", "(", "3", "-", "2", ")"
		};
		assertEquals(expected.length, lexems.size());
		for (int i = 0; i < expected.length; ++i)
			assertEquals(expected[i], lexems.get(i).getValue());
	}

	@Test
	public void parseDotInDoubleTest() {
		final LexemParser parser = new LexemParser();
		final ArrayList<Lexem> lexems = parser.parse("1.44 + (5.0 - 2)");
		String [] expected = new String[] {
			"1.44", "+", "(", "5.0", "-", "2", ")"
		};
		assertEquals(expected.length, lexems.size());
		for (int i = 0; i < expected.length; ++i)
			assertEquals(expected[i], lexems.get(i).getValue());
	}

	@Test
	public void parseEmptyStringTest() {
		exception.expect(IllegalArgumentException.class);
		new LexemParser().parse("   ");
	}

	// @Test
	// public void parseInvalidSyntaxTest() {
	// 	exception.expect(IllegalArgumentException.class);
	// 	new LexemParser().parse("1a");
	// }

	private static void p(Iterable<Lexem> ls) {
		for (Lexem l : ls) {
			System.out.print(l.getValue());
			System.out.print(", ");
		}
		System.out.println("!");
	}

	@Test
	public void parseFuncTest() {
		final LexemParser parser = new LexemParser();
		final ArrayList<Lexem> lexems = parser.parse("sin(1)");
		assertEquals(4, lexems.size());
		assertEquals("sin", lexems.get(0).getValue());
		assertEquals("(", lexems.get(1).getValue());
		assertEquals("1", lexems.get(2).getValue());
		assertEquals(")", lexems.get(3).getValue());
	}

	@Test
	public void parseConstTest() {
		final LexemParser parser = new LexemParser();
		final ArrayList<Lexem> lexems = parser.parse("E + PI");
		assertEquals(3, lexems.size());
		assertEquals("E", lexems.get(0).getValue());
		assertEquals("+", lexems.get(1).getValue());
		assertEquals("PI", lexems.get(2).getValue());
	}

	@Test
	public void parseFuncAndConstTest() {
		final LexemParser parser = new LexemParser();
		final ArrayList<Lexem> lexems = parser.parse("cos(2 * PI)");
		assertEquals(6, lexems.size());
		assertEquals("cos", lexems.get(0).getValue());
		assertEquals("(", lexems.get(1).getValue());
		assertEquals("2", lexems.get(2).getValue());
		assertEquals("*", lexems.get(3).getValue());
		assertEquals("PI", lexems.get(4).getValue());
		assertEquals(")", lexems.get(5).getValue());
	}

	@Test
	public void parseUnknownLiteralsTest() {
		final LexemParser parser = new LexemParser();
		final ArrayList<Lexem> lexems = parser.parse("cso + cccc - IPI");
		assertEquals(5, lexems.size());
		assertEquals("cso", lexems.get(0).getValue());
		assertEquals("+", lexems.get(1).getValue());
		assertEquals("cccc", lexems.get(2).getValue());
		assertEquals("-", lexems.get(3).getValue());
		assertEquals("IPI", lexems.get(4).getValue());
	}
}