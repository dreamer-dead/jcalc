package org.dreamer.expression.calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
* Class that converts input tokens into a reverse polish notation.
* @see http://en.wikipedia.org/wiki/Reverse_Polish_notation
*/
public class InfixToRPNConverter {
	public InfixToRPNConverter() {
	}

	// Size of output sequence could be less than size of input sequence.
	// Tokens '(' and ')' will be removed.
	public ArrayList<TypedToken> convert(Iterable<TypedToken> tokens) {
		final ArrayList<TypedToken> result = new ArrayList<TypedToken>();
		final Stack<TypedToken> stack = new Stack<TypedToken>();
		for (TypedToken token : tokens) {
			if (token.isValue()) {
				// It's just a value, add it.
				result.add(token);
			} else if (token.isFunction()) {
				// Add function token to operations stack.
				stack.push(token);
			} else if (token.isOperator()) {
				// Copy operators with greater priority to result sequnce.
				// Functions have higher priority than operators.
				while (!stack.empty() && 0 >= token.comparePriority(stack.peek())) {
					result.add(stack.pop());
				}
				// Add new operator to the stack.
				stack.push(token);
			} else if (token.getType() == TypedToken.Type.OPEN_BRACKET
					|| token.getType() == TypedToken.Type.CLOSE_BRACKET) {
				if (stack.empty() || token.getType() == TypedToken.Type.OPEN_BRACKET) {
					stack.push(token);
				} else {
					// It's a CLOSE_BRACKET.
					TypedToken tmp = stack.pop();
					// Copy all tokens to output until we meet OPEN_BRACKET.
					// OPEN_BRACKET won't be copied.
					while (tmp.getType() != TypedToken.Type.OPEN_BRACKET) {
						result.add(tmp);
						if (stack.empty())
							throw ExceptionsHelper.missingOpenBracket();
						tmp = stack.pop();
					}
					// Copy function token to output.
					// So after input [sin, (, PI, )]
					// we'll have output [PI, sin]
					if (!stack.empty() && stack.peek().isFunction())
						result.add(stack.pop());
				}
			}
		}

		// Copy rest of tokens.
		while (!stack.empty())
			result.add(stack.pop());
		return result;
	}
}