package org.dreamer.expression.calc;

import java.util.ArrayList;

class ExpressionCalculator {
	public ExpressionCalculator() {
		_compiler = new RPNTokenCompiler();
		_typedTokenParser = new TypedTokenParser();
		_tokenParser = new TokenParser();
		_converter = new InfixToRPNConverter();
	}

	public double evaluate(String expression) throws ParserException {
		final ArrayList<Token> tokens = _tokenParser.parse(expression);
		final ArrayList<TypedToken> typedTokens = _typedTokenParser.parse(tokens);
		final ArrayList<TypedToken> tokensInRPN = _converter.convert(typedTokens);
		return _compiler.compile(tokensInRPN).eval();
	}

	private static void printHelp() {
		System.out.println("Usage: java -jar calc.jar \"1 + 2\"");
	}

	// Entry point for expression calculator
	public static void main(String [] args) {
		if (args.length != 1) {
			printHelp();
			return;
		}

		try {
			double result = new ExpressionCalculator().evaluate(args[0]);
			System.out.println(String.format("Result is %1$.5f", result));
		} catch(Exception e) {
			System.out.println("Failed to evaluate expression!");
			System.out.println(e.getMessage());
		}
	}

	RPNTokenCompiler _compiler;
	TypedTokenParser _typedTokenParser;
	TokenParser _tokenParser;
	InfixToRPNConverter _converter;
}