package com.regnosys.rosetta.interpreternew.visitors;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterNumberValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterValueEnvironmentTuple;
import com.regnosys.rosetta.rosetta.expression.RosettaNumberLiteral;

public class RosettaInterpreterRosettaNumberLiteralInterpreter 
	extends RosettaInterpreterConcreteInterpreter {
	
	/**
	 * Interpreter for the basic number literal.
	 *
	 * @param exp The number literal
	 * @param env The environment
	 * @return RosettaInterpreterValueEnvironmentTuple containing the wrapped number value and the environment
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaNumberLiteral exp, 
			RosettaInterpreterEnvironment env) {
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterNumberValue(exp.getValue()), env);
	}

}
