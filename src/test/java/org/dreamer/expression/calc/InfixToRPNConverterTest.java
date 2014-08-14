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
	public void parseSimpleTest() throws ParserException {
		final InfixToRPNConverter converter = new InfixToRPNConverter();
		final ArrayList<TypedToken> tokens = new ArrayList<TypedToken>();
		tokens.add(new TypedToken(new Token("1", 0), TypedToken.Type.VALUE));
		tokens.add(new TypedToken(new Token("+", 0), TypedToken.Type.OP_ADD));
		tokens.add(new TypedToken(new Token("2", 0), TypedToken.Type.VALUE));
		final ArrayList<TypedToken> tokensInRPN = converter.convert(tokens);

		assertEquals(3, tokensInRPN.size());
		assertEquals(TypedToken.Type.VALUE, tokensInRPN.get(0).getType());
		assertEquals(TypedToken.Type.VALUE, tokensInRPN.get(1).getType());
		assertEquals(TypedToken.Type.OP_ADD, tokensInRPN.get(2).getType());
	}

	@Test
	public void parseExpression1Test() throws ParserException {
		final TypedTokenParser typedTokenParser = new TypedTokenParser();
		final TokenParser tokenParser = new TokenParser();
		final InfixToRPNConverter converter = new InfixToRPNConverter();
		final ArrayList<Token> tokens = tokenParser.parse("1 + 4 / 2");
		final ArrayList<TypedToken> typedTokens = typedTokenParser.parse(tokens);
		assertEquals(5, typedTokens.size());

		final ArrayList<TypedToken> tokensInRPN = converter.convert(typedTokens);
		final TypedToken.Type [] expected = new TypedToken.Type [] {
			TypedToken.Type.VALUE,	TypedToken.Type.VALUE, TypedToken.Type.VALUE,
			TypedToken.Type.OP_DIV, TypedToken.Type.OP_ADD
		};

		assertEquals(expected.length, tokensInRPN.size());
		for (int i = 0; i < expected.length; ++i)
			assertEquals(expected[i], tokensInRPN.get(i).getType());
	}

	@Test
	public void parseExpressionPriorityTest() throws ParserException {
		final TypedTokenParser typedTokenParser = new TypedTokenParser();
		final TokenParser tokenParser = new TokenParser();
		final InfixToRPNConverter converter = new InfixToRPNConverter();
		final ArrayList<Token> tokens = tokenParser.parse("(1 + 4) / 2");
		final ArrayList<TypedToken> typedTokens = typedTokenParser.parse(tokens);
		assertEquals(7, typedTokens.size());

		final ArrayList<TypedToken> tokensInRPN = converter.convert(typedTokens);
		final TypedToken.Type [] expected = new TypedToken.Type [] {
			TypedToken.Type.VALUE,	TypedToken.Type.VALUE, TypedToken.Type.OP_ADD,
			TypedToken.Type.VALUE, TypedToken.Type.OP_DIV
		};

		assertEquals(expected.length, tokensInRPN.size());
		for (int i = 0; i < expected.length; ++i)
			assertEquals(expected[i], tokensInRPN.get(i).getType());
	}

	@Test
	public void parseExpressionWithFuncTest() throws ParserException {
		final TypedTokenParser typedTokenParser = new TypedTokenParser();
		final TokenParser tokenParser = new TokenParser();
		final InfixToRPNConverter converter = new InfixToRPNConverter();
		final ArrayList<Token> tokens = tokenParser.parse("sin(0) * 2 + 0.7");
		final ArrayList<TypedToken> typedTokens = typedTokenParser.parse(tokens);
		assertEquals(8, typedTokens.size());

		final ArrayList<TypedToken> tokensInRPN = converter.convert(typedTokens);
		final TypedToken.Type [] expected = new TypedToken.Type [] {
			TypedToken.Type.VALUE,	TypedToken.Type.FUNC, TypedToken.Type.VALUE,
			TypedToken.Type.OP_MUL, TypedToken.Type.VALUE, TypedToken.Type.OP_ADD
		};

		assertEquals(expected.length, tokensInRPN.size());
		for (int i = 0; i < expected.length; ++i)
			assertEquals(expected[i], tokensInRPN.get(i).getType());
	}

	@Test
	public void parseExpressionWithFuncsTest() throws ParserException {
		final TypedTokenParser typedTokenParser = new TypedTokenParser();
		final TokenParser tokenParser = new TokenParser();
		final InfixToRPNConverter converter = new InfixToRPNConverter();
		final ArrayList<Token> tokens = tokenParser.parse("1 + sin(0)-cos(PI)");
		final ArrayList<TypedToken> typedTokens = typedTokenParser.parse(tokens);
		assertEquals(11, typedTokens.size());

		final ArrayList<TypedToken> tokensInRPN = converter.convert(typedTokens);
		// 1,0,sin,+,PI,cos,-
		final TypedToken.Type [] expected = new TypedToken.Type [] {
			TypedToken.Type.VALUE, TypedToken.Type.VALUE,
			TypedToken.Type.FUNC, TypedToken.Type.OP_ADD,
			TypedToken.Type.CONST, TypedToken.Type.FUNC,
			TypedToken.Type.OP_SUB, 
		};

		assertEquals(expected.length, tokensInRPN.size());
		for (int i = 0; i < expected.length; ++i)
			assertEquals(expected[i], tokensInRPN.get(i).getType());
	}
}