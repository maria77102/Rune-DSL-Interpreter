package com.regnosys.rosetta.interpreternew.visitors;

import java.util.ArrayList;
import java.util.List;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterBaseValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterListValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterValueEnvironmentTuple;
import com.regnosys.rosetta.rosetta.expression.ListLiteral;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;

public class RosettaInterpreterListLiteralInterpreter
	extends RosettaInterpreterConcreteInterpreter {

	public RosettaInterpreterListLiteralInterpreter() {
		super();
	}
	
	/**
	 * Interprets a list literal, evaluating it to a list value.
	 *
	 * @param exp the expression to be interpreted
	 * @return the list value it represents
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(ListLiteral exp, 
			RosettaInterpreterEnvironment env) {
		List<RosettaExpression> expressions = exp.getElements();
		List<RosettaInterpreterValue> interpretedExpressions = new ArrayList<>();
		
		for (RosettaExpression e : expressions) {
			RosettaInterpreterValue val = ((RosettaInterpreterValueEnvironmentTuple)e
					.accept(visitor, env)).getValue();
			
			if (val instanceof RosettaInterpreterListValue) {
				interpretedExpressions.addAll(
						((RosettaInterpreterListValue)val)
						.getExpressions());
			} else {
				interpretedExpressions.add(val);
			}
		}
		
		
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterListValue(interpretedExpressions), env);
	}

}
