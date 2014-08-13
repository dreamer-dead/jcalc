package org.dreamer.expression.calc;

import java.util.ArrayList;

class ExpressionCalculator {
	public ExpressionCalculator() {
		_evaluator = new PolishNotationTokensEvaluator();
		_tokenParser = new TokenParser();
		_lexemParser = new LexemParser();
		_converter = new InfixToRPNConverter();
	}

	public double evaluate(String expression) {
		final ArrayList<Lexem> lexems = _lexemParser.parse(expression);
		final ArrayList<ParsedToken> tokens = _tokenParser.parse(lexems);
		final ArrayList<ParsedToken> tokensInRPN = _converter.convert(tokens);
		return _evaluator.evaluate(tokensInRPN).eval();
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

	PolishNotationTokensEvaluator _evaluator;
	TokenParser _tokenParser;
	LexemParser _lexemParser;
	InfixToRPNConverter _converter;
}