package com.regnosys.rosetta.interpreternew;

import com.regnosys.rosetta.rosetta.expression.ArithmeticOperation;
import com.regnosys.rosetta.rosetta.expression.LogicalOperation;
import com.regnosys.rosetta.rosetta.RosettaInterpreterBaseEnvironment;
import com.regnosys.rosetta.rosetta.expression.ReverseOperation;
import com.regnosys.rosetta.rosetta.expression.ComparisonOperation;
import com.regnosys.rosetta.rosetta.expression.DistinctOperation;
import com.regnosys.rosetta.rosetta.expression.EqualityOperation;
import com.regnosys.rosetta.rosetta.expression.FirstOperation;
import com.regnosys.rosetta.rosetta.expression.JoinOperation;
import com.regnosys.rosetta.rosetta.expression.LastOperation;
import com.regnosys.rosetta.rosetta.expression.ListLiteral;
import com.regnosys.rosetta.rosetta.expression.RosettaAbsentExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaBooleanLiteral;
import com.regnosys.rosetta.rosetta.expression.RosettaContainsExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaCountOperation;
import com.regnosys.rosetta.rosetta.expression.RosettaDisjointExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaExistsExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaConditionalExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaIntLiteral;
import com.regnosys.rosetta.rosetta.expression.RosettaNumberLiteral;
import com.regnosys.rosetta.rosetta.expression.RosettaOnlyElement;
import com.regnosys.rosetta.rosetta.expression.RosettaPatternLiteral;
import com.regnosys.rosetta.rosetta.expression.RosettaStringLiteral;
import com.regnosys.rosetta.rosetta.expression.RosettaSymbolReference;
import com.regnosys.rosetta.rosetta.expression.SumOperation;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.interpreternew.visitors.RosettaInterpreterLogicalOperationInterpreter;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterError;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterErrorValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterValueEnvironmentTuple;
import com.regnosys.rosetta.interpreternew.visitors.RosettaInterpreterComparisonOperationInterpreter;
import com.regnosys.rosetta.interpreternew.visitors.RosettaInterpreterListLiteralInterpreter;
import com.regnosys.rosetta.interpreternew.visitors.RosettaInterpreterRosettaArithmeticOperationsInterpreter;
import com.regnosys.rosetta.interpreternew.visitors.RosettaInterpreterListOperationsInterpreter;
import com.regnosys.rosetta.interpreternew.visitors.RosettaInterpreterListOperatorInterpreter;
import com.regnosys.rosetta.interpreternew.visitors.RosettaInterpreterRosettaBooleanLiteralInterpreter;
import com.regnosys.rosetta.interpreternew.visitors.RosettaInterpreterRosettaConditionalExpressionInterpreter;
import com.regnosys.rosetta.interpreternew.visitors.RosettaInterpreterRosettaIntLiteralInterpreter;
import com.regnosys.rosetta.interpreternew.visitors.RosettaInterpreterRosettaNumberLiteralInterpreter;
import com.regnosys.rosetta.interpreternew.visitors.RosettaInterpreterRosettaStringLiteralInterpreter;
import com.regnosys.rosetta.interpreternew.visitors.RosettaInterpreterVariableInterpreter;

public class RosettaInterpreterVisitor extends RosettaInterpreterVisitorBase {
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaBooleanLiteral exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterRosettaBooleanLiteralInterpreter().interp(exp, 
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaStringLiteral exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterRosettaStringLiteralInterpreter().interp(exp, 
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaNumberLiteral exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterRosettaNumberLiteralInterpreter().interp(exp, 
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaIntLiteral exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterRosettaIntLiteralInterpreter().interp(exp, 
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaPatternLiteral exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterValueEnvironmentTuple(new RosettaInterpreterErrorValue(
				new RosettaInterpreterError("Pattern literals are not supported")), 
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(ListLiteral exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterListLiteralInterpreter().interp(exp, 
				(RosettaInterpreterEnvironment) env);
	}
	
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaConditionalExpression exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterRosettaConditionalExpressionInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(LogicalOperation exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterLogicalOperationInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(EqualityOperation exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterComparisonOperationInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(ComparisonOperation exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterComparisonOperationInterpreter().interp(exp, 
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(ArithmeticOperation exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterRosettaArithmeticOperationsInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}

	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaSymbolReference exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterVariableInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}	

	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaContainsExpression exp,
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterListOperationsInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}

	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaDisjointExpression exp,
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterListOperationsInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}

	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(JoinOperation exp,
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterListOperationsInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaExistsExpression exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterListOperatorInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaAbsentExpression exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterListOperatorInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaOnlyElement exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterListOperatorInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(LastOperation exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterListOperatorInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}

	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(FirstOperation exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterListOperatorInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}
	
	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(RosettaCountOperation exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterListOperatorInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}

	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(DistinctOperation exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterListOperatorInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}

	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(ReverseOperation exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterListOperatorInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}

	@Override
	public RosettaInterpreterValueEnvironmentTuple interp(SumOperation exp, 
			RosettaInterpreterBaseEnvironment env) {
		return new RosettaInterpreterListOperatorInterpreter().interp(exp,
				(RosettaInterpreterEnvironment) env);
	}
}
