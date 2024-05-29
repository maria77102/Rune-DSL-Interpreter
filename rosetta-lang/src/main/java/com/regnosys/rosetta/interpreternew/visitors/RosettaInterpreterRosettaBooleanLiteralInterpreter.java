package com.regnosys.rosetta.interpreternew.visitors;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterBooleanValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterValueEnvironmentTuple;
import com.regnosys.rosetta.rosetta.expression.RosettaBooleanLiteral;

public class RosettaInterpreterRosettaBooleanLiteralInterpreter
	extends RosettaInterpreterConcreteInterpreter {

	public RosettaInterpreterValueEnvironmentTuple interp(RosettaBooleanLiteral expr, 
			RosettaInterpreterEnvironment env) {
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterBooleanValue(expr.isValue()), env);
	}
}
