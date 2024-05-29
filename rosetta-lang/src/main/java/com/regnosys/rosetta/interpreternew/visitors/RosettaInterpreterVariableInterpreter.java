package com.regnosys.rosetta.interpreternew.visitors;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterBaseValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterValueEnvironmentTuple;
import com.regnosys.rosetta.rosetta.RosettaInterpreterBaseEnvironment;
import com.regnosys.rosetta.rosetta.expression.RosettaSymbolReference;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;

public class RosettaInterpreterVariableInterpreter {
		
	/**
	 * Interprets a variable, returns the value of it.
	 *
	 * @param exp The RosettaSymbolReference expression to interpret
	 * @param environment RosettaInterpreterBaseEnvironment that keeps track
	 *		   of the current state of the program
	 * @return If no errors are encountered, a RosettaInterpreterValue representing
	 * 		   the value of the variable.
	 * 		   If errors are encountered, a RosettaInterpreterErrorValue representing
     *         the error.
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaSymbolReference exp, 
			RosettaInterpreterEnvironment env) {
		
		//Search for variable in environment
		RosettaInterpreterBaseValue varValue = env.findValue(exp.getSymbol().getName());
		
		return new RosettaInterpreterValueEnvironmentTuple(varValue, env);
	}

}
