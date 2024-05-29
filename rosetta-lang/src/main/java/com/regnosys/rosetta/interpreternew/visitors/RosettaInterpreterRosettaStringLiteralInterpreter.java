package com.regnosys.rosetta.interpreternew.visitors;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterStringValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterValueEnvironmentTuple;
import com.regnosys.rosetta.rosetta.expression.RosettaStringLiteral;

public class RosettaInterpreterRosettaStringLiteralInterpreter 
	extends RosettaInterpreterConcreteInterpreter {

	public RosettaInterpreterValueEnvironmentTuple interp(RosettaStringLiteral exp, 
			RosettaInterpreterEnvironment env) {
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterStringValue(exp.getValue()), env);
	}

}
