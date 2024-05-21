package com.regnosys.rosetta.interpreternew.visitors;

import java.util.List;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterBooleanValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterError;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterErrorValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterIntegerValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterNumberValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterStringValue;
import com.regnosys.rosetta.rosetta.expression.ArithmeticOperation;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;
import com.rosetta.model.lib.RosettaNumber;

public class RosettaInterpreterRosettaArithmeticOperationsInterpreter extends RosettaInterpreterConcreteInterpreter{

	public RosettaInterpreterValue interp(ArithmeticOperation expr) {

		RosettaNumber leftNumber = new RosettaNumber("0");
		RosettaNumber rightNumber = new RosettaNumber("0");
		
		String leftString = null;
		String rightString = null;
		
		RosettaExpression left = expr.getLeft();
		RosettaExpression right = expr.getRight();
		RosettaInterpreterValue leftInterpreted = left.accept(visitor);
		RosettaInterpreterValue rightInterpreted = right.accept(visitor); 
		
		if (!(leftInterpreted instanceof RosettaInterpreterNumberValue || 
				leftInterpreted instanceof RosettaInterpreterStringValue ||
				leftInterpreted instanceof RosettaInterpreterIntegerValue) ||
				!(rightInterpreted instanceof RosettaInterpreterNumberValue || 
						rightInterpreted instanceof RosettaInterpreterStringValue ||
						rightInterpreted instanceof RosettaInterpreterIntegerValue)) {
				// Check for errors in the left or right side of the binary operation
			RosettaInterpreterErrorValue leftErrors = 
					checkForErrors(leftInterpreted, "Leftside");
			RosettaInterpreterErrorValue rightErrors = 
					checkForErrors(rightInterpreted, "Rightside");
			if(RosettaInterpreterErrorValue.errorsExist(leftErrors, rightErrors))
				return RosettaInterpreterErrorValue.merge(List.of(leftErrors, rightErrors));
		}
		
		
		if (leftInterpreted instanceof RosettaInterpreterStringValue) {
			leftString = ((RosettaInterpreterStringValue) leftInterpreted).getValue();
			rightString = ((RosettaInterpreterStringValue) rightInterpreted).getValue();
			if(expr.getOperator().equals("+"))
				return new RosettaInterpreterStringValue(leftString + rightString);
			else return new RosettaInterpreterErrorValue(
					new RosettaInterpreterError(
							"The terms are strings but the operation is not concatenation: not implemented"));
		}
			
		if (leftInterpreted instanceof RosettaInterpreterNumberValue)
			leftNumber = ((RosettaInterpreterNumberValue) leftInterpreted).getValue();
			else if (leftInterpreted instanceof RosettaInterpreterIntegerValue)
				leftNumber = RosettaNumber.valueOf(((RosettaInterpreterIntegerValue) leftInterpreted).getValue());
			//else //error here after we implement it
		if (rightInterpreted instanceof RosettaInterpreterNumberValue)
			rightNumber = ((RosettaInterpreterNumberValue) rightInterpreted).getValue();
			else if (rightInterpreted instanceof RosettaInterpreterIntegerValue)
				rightNumber = RosettaNumber.valueOf(((RosettaInterpreterIntegerValue) rightInterpreted).getValue());
			//else //error here after we implement it
		if(expr.getOperator().equals("+")) {
			return new RosettaInterpreterNumberValue((leftNumber.add(rightNumber)).bigDecimalValue());
		} else if(expr.getOperator().equals("-")) {
			return new RosettaInterpreterNumberValue((leftNumber.subtract(rightNumber)).bigDecimalValue());
		} else if(expr.getOperator().equals("*")) {
			return new RosettaInterpreterNumberValue((leftNumber.multiply(rightNumber)).bigDecimalValue());
		} else if(expr.getOperator().equals("/")) {
			return new RosettaInterpreterNumberValue((leftNumber.divide(rightNumber)).bigDecimalValue());
		}
		return null; //propagated error when we have it
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
		if (interpretedValue instanceof RosettaInterpreterNumberValue || 
				interpretedValue instanceof RosettaInterpreterStringValue ||
				interpretedValue instanceof RosettaInterpreterIntegerValue) {
			// No errors found.
			// I return an error value without any errors in its list,
			// So that I can still use the merge method with 2 elements
			return new RosettaInterpreterErrorValue();
		} else if (RosettaInterpreterErrorValue.errorsExist(interpretedValue)) {
			// The interpreted value was an error so we propagate it
			return (RosettaInterpreterErrorValue) interpretedValue;
		} else {
			// The interpreted value was not an error,
			// but something other than a boolean
			return new RosettaInterpreterErrorValue(
					new RosettaInterpreterError(
							"Arithmetic Operation: " + side + 
							" is not of type Number/String"));
		}
	}
}

