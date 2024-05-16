package com.regnosys.rosetta.interpreternew.visitors;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.regnosys.rosetta.interpreternew.RosettaInterpreterNewException;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterIntegerValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterNumberValue;
import com.regnosys.rosetta.rosetta.expression.ArithmeticOperation;
import com.regnosys.rosetta.rosetta.expression.LogicalOperation;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaInterpreterValue;

public class RosettaInterpreterRosettaArithmeticOperationsInterpreter extends RosettaInterpreterConcreteInterpreter{

	public RosettaInterpreterIntegerValue interp(ArithmeticOperation expr) {

		BigInteger leftNumber = BigInteger.valueOf(0);
		BigInteger rightNumber = BigInteger.valueOf(0);
		
		
		RosettaExpression left = expr.getLeft();
		RosettaExpression right = expr.getRight();
		RosettaInterpreterValue leftInterpreted = left.accept(visitor);
		RosettaInterpreterValue rightInterpreted = right.accept(visitor);   
		if(leftInterpreted instanceof RosettaInterpreterIntegerValue 
				&& rightInterpreted instanceof RosettaInterpreterIntegerValue) {
			leftNumber = ((RosettaInterpreterIntegerValue) leftInterpreted).getValue();
			rightNumber = ((RosettaInterpreterIntegerValue) rightInterpreted).getValue();
		} else {
			//throw new RosettaInterpreterNewException("The terms of the operation are not numbers.");
			return null;
		}
		
		if(expr.getOperator().equals("+")) {
			return new RosettaInterpreterIntegerValue(leftNumber.add(rightNumber));
		} else if(expr.getOperator().equals("-")) {
			return new RosettaInterpreterIntegerValue(leftNumber.subtract(rightNumber));
		} else if(expr.getOperator().equals("*")) {
			return new RosettaInterpreterIntegerValue(leftNumber.multiply(rightNumber));
		} else if(expr.getOperator().equals("/")) {
			return new RosettaInterpreterIntegerValue(leftNumber.divide(rightNumber));
		} else {
			//throw new RosettaInterpreterNewException("The terms of the operation are not numbers.");
			return null;
		}
	}
}

