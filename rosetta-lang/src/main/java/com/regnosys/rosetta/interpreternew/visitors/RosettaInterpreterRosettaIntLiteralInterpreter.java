package com.regnosys.rosetta.interpreternew.visitors;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterIntegerValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterValueEnvironmentTuple;
import com.regnosys.rosetta.rosetta.expression.RosettaIntLiteral;

public class RosettaInterpreterRosettaIntLiteralInterpreter 
	extends RosettaInterpreterConcreteInterpreter {
	
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaIntLiteral expr, 
			RosettaInterpreterEnvironment env) {
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterIntegerValue(expr.getValue()), env);
	}
}
