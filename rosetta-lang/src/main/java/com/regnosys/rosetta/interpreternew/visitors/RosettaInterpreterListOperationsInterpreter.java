package com.regnosys.rosetta.interpreternew.visitors;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterBaseValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterBooleanValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterError;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterErrorValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterStringValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterValueEnvironmentTuple;
import com.regnosys.rosetta.rosetta.expression.JoinOperation;
import com.regnosys.rosetta.rosetta.expression.RosettaContainsExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaDisjointExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;

public class RosettaInterpreterListOperationsInterpreter
	extends RosettaInterpreterConcreteInterpreter {

	/**
	 * Interprets a rosetta contains expression.
	 * Checks if the right element, which may be a single element or a list,
	 * is contained within the left list
	 *
	 * @param exp - expression to evaluate
	 * @return value of contains expression
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaContainsExpression exp,
			RosettaInterpreterEnvironment env) {
		RosettaExpression leftExp = exp.getLeft();
		RosettaExpression rightExp = exp.getRight();
		
		RosettaInterpreterValue leftVal = ((RosettaInterpreterValueEnvironmentTuple)leftExp
				.accept(visitor, env)).getValue();
		RosettaInterpreterValue rightVal = ((RosettaInterpreterValueEnvironmentTuple)rightExp
				.accept(visitor, env)).getValue(); 
		
		if (RosettaInterpreterErrorValue.errorsExist(leftVal, rightVal)) {
			return new RosettaInterpreterValueEnvironmentTuple(
					RosettaInterpreterErrorValue.merge(leftVal, rightVal), env);
		}
		
		if (RosettaInterpreterBaseValue.valueStream(rightVal).count() < 1L
				|| RosettaInterpreterBaseValue.valueStream(leftVal).count() < 1L) {
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterBooleanValue(false), env);
		}
		
		HashSet<RosettaInterpreterValue> leftValueSet = new HashSet<>(
				RosettaInterpreterBaseValue.valueStream(leftVal)
				.collect(Collectors.toList()));
		
		boolean contains = RosettaInterpreterBaseValue.valueStream(rightVal)
				.allMatch(x -> leftValueSet.contains(x));
		
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterBooleanValue(contains), env);
	}
	
	/**
	 * Interprets a rosetta disjoint expression.
	 * Checks if the right element, which may be a single element or a list,
	 * is not contained within the left list
	 *
	 * @param exp - expression to evaluate
	 * @return value of contains expression
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaDisjointExpression exp,
			RosettaInterpreterEnvironment env) {
		RosettaExpression leftExp = exp.getLeft();
		RosettaExpression rightExp = exp.getRight();
		
		RosettaInterpreterValue leftVal = ((RosettaInterpreterValueEnvironmentTuple)leftExp
				.accept(visitor, env)).getValue();
		RosettaInterpreterValue rightVal = ((RosettaInterpreterValueEnvironmentTuple)rightExp
				.accept(visitor, env)).getValue(); 
		
		if (RosettaInterpreterErrorValue.errorsExist(leftVal, rightVal)) {
			return new RosettaInterpreterValueEnvironmentTuple(
					RosettaInterpreterErrorValue.merge(leftVal, rightVal), env);
		}
		
		if (RosettaInterpreterBaseValue.valueStream(rightVal).count() < 1L 
				|| RosettaInterpreterBaseValue.valueStream(leftVal).count() < 1L) {
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterBooleanValue(true), env);
		}
		
		HashSet<RosettaInterpreterValue> rightValueSet = new HashSet<>(
				RosettaInterpreterBaseValue.valueStream(rightVal)
				.collect(Collectors.toList()));
		
		boolean notContains = RosettaInterpreterBaseValue.valueStream(leftVal)
				.allMatch(x -> !rightValueSet.contains(x));
		
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterBooleanValue(notContains), env);
	}

	/**
	 * Interprets a join operation.
	 * Join takes as input a list of strings and a delimiter
	 * Then returns a single string of the strings concatenated with the join operator
	 *
	 * @param exp - join operation to interpret
	 * @return concatenated string
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(JoinOperation exp,
			RosettaInterpreterEnvironment env) {
		RosettaExpression stringsExp = exp.getLeft();
		RosettaExpression delimExp = exp.getRight();
		
		RosettaInterpreterValue stringsVal = ((RosettaInterpreterValueEnvironmentTuple)stringsExp
				.accept(visitor, env)).getValue();
		RosettaInterpreterValue delimVal = ((RosettaInterpreterValueEnvironmentTuple)delimExp
				.accept(visitor, env)).getValue();
		
		if (RosettaInterpreterErrorValue.errorsExist(stringsVal, delimVal)) {
			return new RosettaInterpreterValueEnvironmentTuple(
					RosettaInterpreterErrorValue.merge(stringsVal, delimVal), env);
		}
		
		boolean allStrings = RosettaInterpreterBaseValue.valueStream(stringsVal)
				.allMatch(x -> x instanceof RosettaInterpreterStringValue);
		
		if (!allStrings) {
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterErrorValue(
					new RosettaInterpreterError("The list of values for a join "
							+ "operation must be a list of strings")), env);
		}
		if (!(delimVal instanceof RosettaInterpreterStringValue)) {
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterErrorValue(
					new RosettaInterpreterError("The delimiter for a join"
							+ " operation must be a string")), env);
		}
		
		if (RosettaInterpreterBaseValue.valueStream(stringsVal)
				.count() < 1L) {
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterStringValue(""), env);
		}
		
		String delimString = ((RosettaInterpreterStringValue)delimVal).getValue();
		List<String> texts = RosettaInterpreterBaseValue.valueStream(stringsVal)
				.map(x -> ((RosettaInterpreterStringValue)x).getValue())
				.collect(Collectors.toList());
		
		String result = String.join(delimString, texts);
		
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterStringValue(result), env);
	}
		
}
