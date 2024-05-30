package com.regnosys.rosetta.interpreternew.visitors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterBaseValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterBooleanValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterError;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterErrorValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterIntegerValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterListValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterNumberValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterValueEnvironmentTuple;
import com.regnosys.rosetta.rosetta.expression.DistinctOperation;
import com.regnosys.rosetta.rosetta.expression.FirstOperation;
import com.regnosys.rosetta.rosetta.expression.LastOperation;
import com.regnosys.rosetta.rosetta.expression.ReverseOperation;
import com.regnosys.rosetta.rosetta.expression.RosettaAbsentExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaCountOperation;
import com.regnosys.rosetta.rosetta.expression.RosettaExistsExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaOnlyElement;
import com.regnosys.rosetta.rosetta.expression.SumOperation;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;
import com.rosetta.model.lib.RosettaNumber;

public class RosettaInterpreterListOperatorInterpreter
	extends RosettaInterpreterConcreteInterpreter {

	/**
	 * Interprets an exists operation.
	 * If the argument of the expression is an error, it returns it.
	 * Otherwise, it checks to see if the interpreted argument
	 * is of single or multiple cardinality.
	 *
	 * @param exp Exists operation to interpret
	 * @return Boolean indicating if the argument exists or not
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaExistsExpression exp, 
			RosettaInterpreterEnvironment env) {
		RosettaExpression argument = exp.getArgument();
		RosettaInterpreterValue interpretedArgument = ((RosettaInterpreterValueEnvironmentTuple)argument
				.accept(visitor, env)).getValue();
		
		if (RosettaInterpreterErrorValue.errorsExist(interpretedArgument)) {
			return new RosettaInterpreterValueEnvironmentTuple(interpretedArgument, env);
		}
		
		long count = RosettaInterpreterBaseValue.valueStream(interpretedArgument).count();
		
		boolean exists;
		switch (exp.getModifier()) {
			case SINGLE:
				exists = count == 1;
				break;
			case MULTIPLE:
				exists = count > 1;
				break;
			default:
				exists = count > 0;
		}
		
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterBooleanValue(exists), env);
	}

	/**
	 * Interprets an "is absent" expression.
	 * If the argument of the expression is of size 0, so:
	 * 	- either it is optional, (0..*), and it was not instantiated
	 *  - or it is a list with 0 elements []
	 *
	 * @param exp "Is absent" expression to intepret
	 * @return Boolean indicating if the interpreted argument is absent
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaAbsentExpression exp,
			RosettaInterpreterEnvironment env) {
		RosettaExpression argument = exp.getArgument();
		RosettaInterpreterValue interpretedArgument = ((RosettaInterpreterValueEnvironmentTuple)argument
				.accept(visitor, env)).getValue();
		
		if (RosettaInterpreterErrorValue.errorsExist(interpretedArgument)) {
			return new RosettaInterpreterValueEnvironmentTuple(interpretedArgument, env);
		}
		
		long count = RosettaInterpreterBaseValue.valueStream(interpretedArgument).count();
		boolean isAbsent = count == 0;
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterBooleanValue(isAbsent), env);
	}

	/**
	 * Interprets a count operation.
	 * Return the number of elements in a list
	 *
	 * @param exp Expression to perform 'count' on
	 * @return Integer indicating how many elements there are in the list
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaCountOperation exp, 
			RosettaInterpreterEnvironment env) {
		RosettaExpression argument = exp.getArgument();
		RosettaInterpreterValue interpretedArgument = ((RosettaInterpreterValueEnvironmentTuple)argument
				.accept(visitor, env)).getValue();
		
		if (RosettaInterpreterErrorValue.errorsExist(interpretedArgument)) {
			return new RosettaInterpreterValueEnvironmentTuple(
					interpretedArgument, env);
		}
		
		long count = RosettaInterpreterBaseValue.valueStream(interpretedArgument).count();
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterIntegerValue(BigInteger.valueOf(count)), env);
	}

	/**
	 * Interprets a first operation.
	 * If a list is not empty, it returns the first element.
	 * Otherwise, it returns an error.
	 *
	 * @param exp Expression on which to perform 'first' operation
	 * @return First element of the list
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(FirstOperation exp, 
			RosettaInterpreterEnvironment env) {
		RosettaExpression argument = exp.getArgument();
		RosettaInterpreterValue interpretedArgument = ((RosettaInterpreterValueEnvironmentTuple)argument
				.accept(visitor, env)).getValue();
		
		if (RosettaInterpreterErrorValue.errorsExist(interpretedArgument)) {
			return new RosettaInterpreterValueEnvironmentTuple(
					interpretedArgument, env);
		}
		
		long count = RosettaInterpreterBaseValue.valueStream(interpretedArgument).count();
		if (count == 0L) {
			// List is empty
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterErrorValue(
					new RosettaInterpreterError("List is empty")), env);
		} else {
			// List has at least one element
			return new RosettaInterpreterValueEnvironmentTuple(
					RosettaInterpreterBaseValue.valueStream(interpretedArgument)
					.collect(Collectors.toList()).get(0), env);
		}
	}
	
	/**
	 * Interprets a only-element operation.
	 * If a list has exactly one element, it returns it.
	 * Otherwise, it returns an error.
	 *
	 * @param exp Expression on which to perform 'only-element' operation
	 * @return The single element of the list
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaOnlyElement exp, 
			RosettaInterpreterEnvironment env) {
		RosettaExpression argument = exp.getArgument();
		RosettaInterpreterValue interpretedArgument = ((RosettaInterpreterValueEnvironmentTuple)argument
				.accept(visitor, env)).getValue();
		
		if (RosettaInterpreterErrorValue.errorsExist(interpretedArgument)) {
			return new RosettaInterpreterValueEnvironmentTuple(
					interpretedArgument, env);
		}
		
		long count = RosettaInterpreterBaseValue.valueStream(interpretedArgument).count();
		if (count == 0L) {
			// List is empty
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterErrorValue(
					new RosettaInterpreterError("List is empty")), env);
		} else if (count == 1L) { 
			// List has one element
			return new RosettaInterpreterValueEnvironmentTuple(
					RosettaInterpreterBaseValue.valueStream(interpretedArgument)
					.collect(Collectors.toList()).get(0), env);
		} else {
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterErrorValue(
					new RosettaInterpreterError("List contains more than one element")), env);
		}
	}

	
	/**
	 * Interprets a last operation.
	 * If a list is not empty, it returns the last element.
	 * Otherwise, it returns an error.
	 *
	 * @param exp Expression on which to perform 'last' operation
	 * @return Last element of the list
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(LastOperation exp, 
			RosettaInterpreterEnvironment env) {
		RosettaExpression argument = exp.getArgument();
		RosettaInterpreterValue interpretedArgument = ((RosettaInterpreterValueEnvironmentTuple)argument
				.accept(visitor, env)).getValue();
		
		if (RosettaInterpreterErrorValue.errorsExist(interpretedArgument)) {
			return new RosettaInterpreterValueEnvironmentTuple(interpretedArgument, env);
		}
		
		long count = RosettaInterpreterBaseValue.valueStream(interpretedArgument).count();
		if (count == 0L) {
			// List is empty
			return new RosettaInterpreterValueEnvironmentTuple(new RosettaInterpreterErrorValue(
					new RosettaInterpreterError("List is empty")), env);
		} else {
			// List has at least one element
			return new RosettaInterpreterValueEnvironmentTuple(
					RosettaInterpreterBaseValue.valueStream(interpretedArgument)
					.collect(Collectors.toList()).get((int)count - 1), env);
		}
	}
	
	/**
	 * Interprets a Distinct Operation.
	 * Returns a list where duplicate elements are removed
	 * such that only one copy of them exists in the resulting
	 * list
	 *
	 * @param exp - distinct operation to interpret
	 * @return - list Value of distinct values
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(DistinctOperation exp, 
			RosettaInterpreterEnvironment env) {
		RosettaExpression expression = exp.getArgument();
		RosettaInterpreterValue val = ((RosettaInterpreterValueEnvironmentTuple)expression
				.accept(visitor, env)).getValue();
		
		if (RosettaInterpreterErrorValue.errorsExist(val)) {
			return new RosettaInterpreterValueEnvironmentTuple(
					RosettaInterpreterErrorValue.merge(val), env);
		}
		
		List<RosettaInterpreterValue> distinct = 
				RosettaInterpreterBaseValue.valueStream(val)
				.distinct()
				.collect(Collectors.toList());
		
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterListValue(distinct), env);
	}

	/**
	 * Interprets a Reverse operation.
	 * Reverses the order of the elements in the list and returns it
	 *
	 * @param exp - Reverse operation to interpret
	 * @return - Reversed list
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(ReverseOperation exp,
			RosettaInterpreterEnvironment env) {
		RosettaExpression expression = exp.getArgument();
		RosettaInterpreterValue val = ((RosettaInterpreterValueEnvironmentTuple)expression
				.accept(visitor, env)).getValue();
		if (RosettaInterpreterErrorValue.errorsExist(val)) {
			return new RosettaInterpreterValueEnvironmentTuple(
					RosettaInterpreterErrorValue.merge(val), env);
		}
		
		List<RosettaInterpreterValue> values =
				RosettaInterpreterBaseValue.toValueList(val);
		Collections.reverse(values);
		
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterListValue(values), env);
	}

	/**
	 * Interprets the sum operation.
	 * Returns a sum of all the summable elements of the list
	 * If the elements are not summable returns an error
	 *
	 * @param exp - Sum operation to interpret
	 * @return sum of elements or error if elements are not summable
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(SumOperation exp, 
			RosettaInterpreterEnvironment env) {
		RosettaExpression expression = exp.getArgument();
		RosettaInterpreterValue val = ((RosettaInterpreterValueEnvironmentTuple)expression
				.accept(visitor, env)).getValue();
		
		if (RosettaInterpreterErrorValue.errorsExist(val)) {
			return new RosettaInterpreterValueEnvironmentTuple(
					RosettaInterpreterErrorValue.merge(val), env);
		}
		
		// In the compiler, this returns a null rather than an error
		// So I'm not exactly sure how to handle it
		if (RosettaInterpreterBaseValue.toValueList(val).size() < 1) {
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterErrorValue(
					new RosettaInterpreterError("Cannot take sum"
							+ " of empty list")), env);
		}
		
		List<RosettaInterpreterValue> values =
				RosettaInterpreterBaseValue.toValueList(val);
		
		// Check that all values are numbers, and convert ints
		// to numbers for further simplicity
		for (int i = 0; i < values.size(); i++) {
			RosettaInterpreterValue v = values.get(i);
			if (v instanceof RosettaInterpreterIntegerValue) {
				RosettaInterpreterIntegerValue valInt =
						(RosettaInterpreterIntegerValue)v;
				values.set(i, new RosettaInterpreterNumberValue(
						BigDecimal.valueOf(valInt.getValue().longValue())));
			}
			else if (!(v instanceof RosettaInterpreterNumberValue)) {
				return new RosettaInterpreterValueEnvironmentTuple(
						new RosettaInterpreterErrorValue(
						new RosettaInterpreterError("Cannot take sum"
								+ "of non-number value")), env);
			}
		}
		
		RosettaNumber result = values.stream()
				.map(x -> ((RosettaInterpreterNumberValue)x).getValue())
				.reduce(RosettaNumber.valueOf(BigDecimal.valueOf(0)), (x, y) -> x.add(y));
		
		return new RosettaInterpreterValueEnvironmentTuple(
				new RosettaInterpreterNumberValue(result), env);
	}
}
