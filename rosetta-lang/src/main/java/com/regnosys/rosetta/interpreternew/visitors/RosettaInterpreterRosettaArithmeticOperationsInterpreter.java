package com.regnosys.rosetta.interpreternew.visitors;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterIntegerValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterNumberValue;
import com.regnosys.rosetta.rosetta.expression.ArithmeticOperation;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaInterpreterValue;
import com.rosetta.model.lib.RosettaNumber;

public class RosettaInterpreterRosettaArithmeticOperationsInterpreter extends RosettaInterpreterConcreteInterpreter{

	public RosettaInterpreterValue interp(ArithmeticOperation expr) {

		RosettaNumber leftNumber = new RosettaNumber("0");
		RosettaNumber rightNumber = new RosettaNumber("0");
		
		RosettaExpression left = expr.getLeft();
		RosettaExpression right = expr.getRight();
		RosettaInterpreterValue leftInterpreted = left.accept(visitor);
		RosettaInterpreterValue rightInterpreted = right.accept(visitor);   
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
}

