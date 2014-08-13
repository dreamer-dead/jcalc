package org.dreamer.expression.calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class InfixToRPNConverter {
	public InfixToRPNConverter() {
	}

	public static void p(Iterable<ParsedToken> ts) {
		for (ParsedToken t : ts) {
			System.out.print(t.getLexem().getValue());
			System.out.print(", ");
		}
		System.out.println("!");
	}

	public ArrayList<ParsedToken> convert(Iterable<ParsedToken> tokens) {
		final ArrayList<ParsedToken> result = new ArrayList<ParsedToken>();
		final Stack<ParsedToken> stack = new Stack<ParsedToken>();
		for (ParsedToken token : tokens) {
			if (token.isValue()) {
				result.add(token);
			} else if (token.isFunction()) {
				stack.push(token);
			} else if (token.isOperator()) {
				while (!stack.empty() && 0 >= token.comparePriority(stack.peek())) {
					result.add(stack.pop());
				}
				stack.push(token);
			} else if (token.getType() == ParsedToken.Type.OPEN_BRACKET
					|| token.getType() == ParsedToken.Type.CLOSE_BRACKET) {
				if (stack.empty() || token.getType() == ParsedToken.Type.OPEN_BRACKET) {
					stack.push(token);
				} else {
					ParsedToken tmp = stack.pop();
					while (tmp.getType() != ParsedToken.Type.OPEN_BRACKET) {
						result.add(tmp);
						if (stack.empty())
							throw new IllegalArgumentException("() Syntax error!");
						tmp = stack.pop();
					}
					if (!stack.empty() && stack.peek().isFunction())
						result.add(stack.pop());
				}
			}
		}
		while (!stack.empty())
			result.add(stack.pop());
		return result;
	}
}