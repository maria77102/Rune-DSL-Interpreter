package com.regnosys.rosetta.interpreternew.visitors;

import java.util.List;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterBaseValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterBooleanValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterError;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterErrorValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterValueEnvironmentTuple;
import com.regnosys.rosetta.rosetta.RosettaInterpreterBaseEnvironment;
import com.regnosys.rosetta.rosetta.expression.RosettaConditionalExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;

public class RosettaInterpreterRosettaConditionalExpressionInterpreter 
extends RosettaInterpreterConcreteInterpreter {
	
	/**
	 * Interpreter method for Conditional Expressions.
	 *
	 * @param expr RosettaConditionalExpression to be interpreted
	 * @return The interpreted value
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaConditionalExpression expr,
			RosettaInterpreterEnvironment env) {
		boolean ifResult = false;
		
		RosettaExpression ifExpression = expr.getIf();
		RosettaExpression ifThen = expr.getIfthen();
		
		RosettaInterpreterValue ifValue = ((RosettaInterpreterValueEnvironmentTuple)ifExpression
				.accept(visitor, env)).getValue(); 
		RosettaInterpreterValue ifThenValue = ((RosettaInterpreterValueEnvironmentTuple)ifThen
				.accept(visitor, env)).getValue();
		
		RosettaExpression elseThen = null;
		RosettaInterpreterValue elseThenValue = null;
		
		if (ifValue instanceof RosettaInterpreterBooleanValue) {
			ifResult = ((RosettaInterpreterBooleanValue) ifValue).getValue();
		} else if (RosettaInterpreterErrorValue.errorsExist(ifValue)) {
			return new RosettaInterpreterValueEnvironmentTuple(createErrors(ifValue, 
					"Conditional expression: condition is an error value."), env);
		} else {
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterErrorValue(
							new RosettaInterpreterError(
					"Conditional expression: condition "
					+ "is not a boolean value.")), env);
		}
		
		if (expr.isFull()) {
			elseThen = expr.getElsethen();
			elseThenValue = ((RosettaInterpreterValueEnvironmentTuple)elseThen
					.accept(visitor, env)).getValue();
			
			RosettaInterpreterBaseValue ifInstance = 
					((RosettaInterpreterBaseValue) ifThenValue);
			
			RosettaInterpreterBaseValue elseInstance = 
			((RosettaInterpreterBaseValue) elseThenValue);
			
			if (!ifInstance.getClass().equals(elseInstance.getClass()) 
					&& !(ifInstance 
						instanceof RosettaInterpreterErrorValue) 
					&& !(elseInstance 
						instanceof RosettaInterpreterErrorValue)) {
				return new RosettaInterpreterValueEnvironmentTuple(
						new RosettaInterpreterErrorValue(
						new RosettaInterpreterError(
							"Conditional expression: "
							+ "then and else "
							+ "need to have the same type.")), env);
			}
		}
		
		if (ifResult) {
			if (RosettaInterpreterErrorValue.errorsExist(ifThenValue)) {
				return new RosettaInterpreterValueEnvironmentTuple(
						createErrors(ifThenValue, 
						"Conditional expression: then is an error value."), env);
			}
			
			return new RosettaInterpreterValueEnvironmentTuple(
					((RosettaInterpreterBaseValue) ifThenValue), env);
			
		} else if (expr.isFull()) {
			if (RosettaInterpreterErrorValue.errorsExist(elseThenValue)) {
				return new RosettaInterpreterValueEnvironmentTuple(
						createErrors(elseThenValue, 
						"Conditional expression: else is an error value."), env);
			}
			
			return new RosettaInterpreterValueEnvironmentTuple(
					((RosettaInterpreterBaseValue) elseThenValue), env);
		}
		
		return null;
	}
	
	private RosettaInterpreterBaseValue 
	createErrors(RosettaInterpreterValue exp, String message) {
		RosettaInterpreterErrorValue expError = (RosettaInterpreterErrorValue) exp;
		RosettaInterpreterErrorValue newExpError = 
				new RosettaInterpreterErrorValue(
						new RosettaInterpreterError(message));
		
		return RosettaInterpreterErrorValue.merge(List.of(newExpError, expError));
	}
}
