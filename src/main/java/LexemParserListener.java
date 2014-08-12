package org.dreamer.expression.calc;

public interface LexemParserListener {
	public void onSubExpressionStart();
	public void onSubExpressionEnd();
	public void onLexemParsed(Lexem lexem);
}