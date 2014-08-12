package org.dreamer.expression.calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

public class TokenParser {
	public TokenParser() {
		_operatorsAndFunctions = new HashMap<String, ParsedToken.Type>();
		_operatorsAndFunctions.put("(", ParsedToken.Type.OPEN_BRACKET);
		_operatorsAndFunctions.put(")", ParsedToken.Type.CLOSE_BRACKET);
		_operatorsAndFunctions.put("+", ParsedToken.Type.OP_ADD);
		_operatorsAndFunctions.put("-", ParsedToken.Type.OP_SUB);
		_operatorsAndFunctions.put("*", ParsedToken.Type.OP_MUL);
		_operatorsAndFunctions.put("/", ParsedToken.Type.OP_DIV);
		_operatorsAndFunctions.put("E", ParsedToken.Type.CONST_E);
		_operatorsAndFunctions.put("PI", ParsedToken.Type.CONST_PI);
		_operatorsAndFunctions.put("sin", ParsedToken.Type.FUNC_SIN);
		_operatorsAndFunctions.put("cos", ParsedToken.Type.FUNC_COS);
		_operatorsAndFunctions.put("exp", ParsedToken.Type.FUNC_EXP);
	}

	public ArrayList<ParsedToken> parse(Collection<Lexem> lexems) {
		ArrayList<ParsedToken> tokens = new ArrayList<ParsedToken>();
		for (Lexem lexem : lexems)
			tokens.add(parseLexem(lexem));
		return tokens;
	}

	private ParsedToken parseLexem(Lexem lexem) {
		ParsedToken.Type type = parseLexemType(lexem.getValue());
		return new ParsedToken(lexem, type);
	}

	private ParsedToken.Type parseLexemType(String lexem) {
		if (_operatorsAndFunctions.containsKey(lexem))
			return _operatorsAndFunctions.get(lexem);

		try {
			Double.parseDouble(lexem);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid value!");
		}
		return ParsedToken.Type.VALUE;
	}

	private HashMap<String, ParsedToken.Type> _operatorsAndFunctions;
}