package com.regnosys.rosetta.interpreternew.values;

import java.util.Objects;

import com.regnosys.rosetta.rosetta.impl.RosettaInterpreterTupleImpl;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;

public class RosettaInterpreterValueEnvironmentTuple
				extends RosettaInterpreterTupleImpl {
	
	private RosettaInterpreterValue value;
	private RosettaInterpreterEnvironment env;
	
	/**
	 * Constructor for the return type of all interpreter classes.
	 *
	 * @param value The interpreted value
	 * @param env The environment, possibly modified by the interpreter logic
	 */
	public RosettaInterpreterValueEnvironmentTuple(RosettaInterpreterValue value,
			RosettaInterpreterEnvironment env) {
		super();
		this.value = value;
		this.env = env;
	}
	
	public RosettaInterpreterValue getValue() { return value; }
	
	public RosettaInterpreterEnvironment getEnvironment() { return env; }

	@Override
	public int hashCode() {
		return Objects.hash(value, env);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RosettaInterpreterValueEnvironmentTuple other = (RosettaInterpreterValueEnvironmentTuple) obj;
		return value == other.value && env == other.env;
	}
	
	@Override
	public String toString() {
		return "RosettaInterpreterValueEnvironmentTuple [value=" + value + ", environment=" + env + "]";
	}
}
