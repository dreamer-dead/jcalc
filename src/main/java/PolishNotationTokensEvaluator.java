package org.dreamer.expression.calc;

import java.util.Stack;

public class PolishNotationTokensEvaluator {
	public PolishNotationTokensEvaluator() {}

	public Expression evaluate(Iterable<ParsedToken> tokens) {
		Stack<Expression> stack = new Stack<Expression>();
		for (ParsedToken t : tokens) {
			final Expression exprFromToken = evaluateFromToken(t, stack);
		}

		return null;
	}

	public static Expression evaluateFromToken(ParsedToken token, Stack<Expression> stack) {
		if (token.isValue()) {
			if (token.getType() == ParsedToken.Type.CONST)
				return ValueExpression.constant(token.getLexem().getValue());
			else
				return new ValueExpression(Double.parseDouble(token.getLexem().getValue()));
		} else if (token.isOperator()) {
			if (stack.size() < 2)
				throw new IllegalArgumentException("Invalid stack size!");
			final Expression p2 = stack.pop();
			final Expression p1 = stack.pop();

			switch(token.getType()) {
			case OP_ADD: return BinaryExpression.add(p1, p2);
			case OP_SUB: return BinaryExpression.sub(p1, p2);
			case OP_MUL: return BinaryExpression.mul(p1, p2);
			case OP_DIV: return BinaryExpression.div(p1, p2);
			}
		} else if (token.isFunction()) {
			if (stack.empty())
				throw new IllegalArgumentException("Invalid stack size!");
			final String functionName = token.getLexem().getValue();
			if (functionName.equals("sin"))
				return FunctionExpression.sin(stack.pop());
			else if (functionName.equals("cos"))
				return FunctionExpression.cos(stack.pop());
			else if (functionName.equals("exp"))
				return FunctionExpression.exp(stack.pop());

			throw new IllegalArgumentException("Unresolved function!");
		}

		throw new IllegalArgumentException("Unresolved token!");
	}
}