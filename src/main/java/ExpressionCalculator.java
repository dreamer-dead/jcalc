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

	// Entry point for expression calculator
	public static void main(String [] args) {
		System.out.println("Hello!");
	}

	PolishNotationTokensEvaluator _evaluator;
	TokenParser _tokenParser;
	LexemParser _lexemParser;
	InfixToRPNConverter _converter;
}