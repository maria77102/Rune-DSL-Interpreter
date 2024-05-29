package com.regnosys.rosetta.interpreternew.visitors;

import java.util.List;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterError;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterErrorValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterIntegerValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterNumberValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterStringValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterValueEnvironmentTuple;
import com.regnosys.rosetta.rosetta.RosettaInterpreterBaseEnvironment;
import com.regnosys.rosetta.rosetta.expression.ArithmeticOperation;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;
import com.rosetta.model.lib.RosettaNumber;

public class RosettaInterpreterRosettaArithmeticOperationsInterpreter 
					extends RosettaInterpreterConcreteInterpreter {
	
	
	/**
	 * Interprets an arithmetic operation, evaluating the operation between the two terms.
	 *
	 * @param expr The ArithmeticOperation expression to interpret
	 * @return If no errors are encountered, a RosettaInterpreterNumberValue or
	 * 		   RosettaInterpreterStringValue representing
	 * 		   the result of the arithmetic/concatenation operation, 
	 * 		   alongside the current environment, contained in RosettaInterpreterValueEnvironmentTuple
	 * 		   If errors are encountered, a RosettaInterpreterErrorValue representing
     *         the error.
	 */
	public RosettaInterpreterValueEnvironmentTuple interp(ArithmeticOperation expr,
			RosettaInterpreterEnvironment env) {
		
		String leftString = null;
		String rightString = null;
		
		RosettaExpression left = expr.getLeft();
		RosettaExpression right = expr.getRight();
		RosettaInterpreterValue leftInterpreted = ((RosettaInterpreterValueEnvironmentTuple)left
				.accept(visitor, env)).getValue();
		RosettaInterpreterValue rightInterpreted = ((RosettaInterpreterValueEnvironmentTuple)right
				.accept(visitor, env)).getValue(); 
		
		if (!(leftInterpreted instanceof RosettaInterpreterNumberValue 
				|| leftInterpreted instanceof RosettaInterpreterStringValue 
				|| leftInterpreted instanceof RosettaInterpreterIntegerValue) 
				|| !(rightInterpreted instanceof RosettaInterpreterNumberValue 
				|| rightInterpreted instanceof RosettaInterpreterStringValue 
				|| rightInterpreted instanceof RosettaInterpreterIntegerValue)) {
			
			// Check for errors in the left or right side of the binary operation
			RosettaInterpreterErrorValue leftErrors = 
					checkForErrors(leftInterpreted, "Leftside");
			RosettaInterpreterErrorValue rightErrors = 
					checkForErrors(rightInterpreted, "Rightside");
			return new RosettaInterpreterValueEnvironmentTuple(
				RosettaInterpreterErrorValue.merge(List.of(leftErrors, rightErrors)), env);
		}
		
		boolean sameType = 
				(leftInterpreted instanceof RosettaInterpreterStringValue
				&& rightInterpreted instanceof RosettaInterpreterStringValue) 
				|| (!(leftInterpreted instanceof RosettaInterpreterStringValue)
				&& !(rightInterpreted instanceof RosettaInterpreterStringValue)); 
		if (!sameType) {
			return new RosettaInterpreterValueEnvironmentTuple(new RosettaInterpreterErrorValue(
					new RosettaInterpreterError(
				"The terms of the operation "
				+ "are neither both strings nor both numbers")), env);
			}
			
		if (leftInterpreted instanceof RosettaInterpreterStringValue) {
			leftString = ((RosettaInterpreterStringValue) leftInterpreted)
					.getValue();
			rightString = ((RosettaInterpreterStringValue) rightInterpreted)
					.getValue();
			if (expr.getOperator().equals("+")) {
				return new RosettaInterpreterValueEnvironmentTuple(
						new RosettaInterpreterStringValue(leftString + rightString), env);
				}
			else {
				return new RosettaInterpreterValueEnvironmentTuple(new RosettaInterpreterErrorValue(
					new RosettaInterpreterError(
				"The terms are strings but the operation "
				+ "is not concatenation: not implemented")), env);
			}
		}
		
		RosettaNumber leftNumber;
		RosettaNumber rightNumber;
			
		if (leftInterpreted instanceof RosettaInterpreterNumberValue) {
			leftNumber = ((RosettaInterpreterNumberValue) leftInterpreted).getValue();
		}
			else {
				leftNumber = RosettaNumber
					.valueOf(((RosettaInterpreterIntegerValue) leftInterpreted)
					.getValue());
				}
		if (rightInterpreted instanceof RosettaInterpreterNumberValue) {
			rightNumber = ((RosettaInterpreterNumberValue) rightInterpreted).getValue();
		} else {
				rightNumber = RosettaNumber
					.valueOf(((RosettaInterpreterIntegerValue) rightInterpreted)
					.getValue());
		}
		if (expr.getOperator().equals("+")) {
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterNumberValue((leftNumber
					.add(rightNumber)).bigDecimalValue()), env);
		} else if (expr.getOperator().equals("-")) {
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterNumberValue((leftNumber
					.subtract(rightNumber)).bigDecimalValue()), env);
		} else if (expr.getOperator().equals("*")) {
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterNumberValue((leftNumber
					.multiply(rightNumber)).bigDecimalValue()), env);
		} else {
			return new RosettaInterpreterValueEnvironmentTuple(
					new RosettaInterpreterNumberValue((leftNumber
					.divide(rightNumber)).bigDecimalValue()), env);
		}
	}
	
	
	/**
	 * Helper method that takes an interpretedValue and a string,
	 * and returns the correct error which
	 * interpretedValue causes, if any.
	 *
	 * @param interpretedValue The interpreted value which we check for errors
	 * @param side String containing either "Leftside" or "Rightside", 
	 *        purely for clearer error messages
	 * @return The correct RosettaInterpreterErrorValue, or "null" 
	 *         if the interpretedValue does not cause an error
	 */
	private RosettaInterpreterErrorValue checkForErrors(
			RosettaInterpreterValue interpretedValue, String side) {
		if  (interpretedValue instanceof RosettaInterpreterNumberValue 
				|| interpretedValue instanceof RosettaInterpreterStringValue 
				|| interpretedValue instanceof RosettaInterpreterIntegerValue) {
			// If the value satisfies the type conditions, we return an empty 
			// error value so that the merger has two error values to merge
			return new RosettaInterpreterErrorValue();
		}
		
		else if (RosettaInterpreterErrorValue.errorsExist(interpretedValue)) {
			// The interpreted value was an error so we propagate it
			return (RosettaInterpreterErrorValue) interpretedValue;
		} else {
			// The interpreted value was not an error,
			// but something other than a string or number
			return new RosettaInterpreterErrorValue(
					new RosettaInterpreterError(
							"Arithmetic Operation: " + side 
							+ " is not of type Number/String"));
		}
	}
}

