package org.dreamer.expression.calc;

import java.util.Stack;

/**
* Class for building an expression tree from RPN sequence of tokens.
*/
public class RPNTokenCompiler {
	public RPNTokenCompiler() {}

	public Expression compile(Iterable<TypedToken> tokens) {
		Stack<Expression> stack = new Stack<Expression>();
		for (TypedToken t : tokens) {
			// compileToken can take expressions from the stack
			// to build a new expression.
			final Expression exprFromToken = compileToken(t, stack);
			stack.push(exprFromToken);
		}

		// There should be only one expression at the end.
		if (stack.size() != 1)
			throw ExceptionsHelper.stackSizeError(stack.size(), 1);
		return stack.pop();
	}

	public static Expression compileToken(TypedToken token, Stack<Expression> stack) {
		// Create a single value or constant expression.
		// Stack isn't used.
		if (token.isValue()) {
			if (token.getType() == TypedToken.Type.CONST)
				return ValueExpression.constant(token.getValue());
			else
				return new ValueExpression(Double.parseDouble(token.getValue()));
		} else if (token.isOperator()) {
			// We need two operands from stack.
			if (stack.size() < 2)
				throw ExceptionsHelper.stackSizeError(stack.size(), 2);

			// Get it from stack in reverse order.
			final Expression p2 = stack.pop();
			final Expression p1 = stack.pop();

			// Create a binary operation expression.
			switch(token.getType()) {
			case OP_ADD: return BinaryExpression.add(p1, p2);
			case OP_SUB: return BinaryExpression.sub(p1, p2);
			case OP_MUL: return BinaryExpression.mul(p1, p2);
			case OP_DIV: return BinaryExpression.div(p1, p2);
			}
		} else if (token.isFunction()) {
			// We need at least one value in stack for function.
			if (stack.empty())
				throw ExceptionsHelper.stackSizeError(stack.size(), 1);

			// Get a function expression.
			final String functionName = token.getValue();
			if (functionName.equals("sin"))
				return FunctionExpression.sin(stack.pop());
			else if (functionName.equals("cos"))
				return FunctionExpression.cos(stack.pop());
			else if (functionName.equals("exp"))
				return FunctionExpression.exp(stack.pop());

			throw ExceptionsHelper.unresolvedFunction(token);
		}

		throw ExceptionsHelper.unresolvedToken(token);
	}
}