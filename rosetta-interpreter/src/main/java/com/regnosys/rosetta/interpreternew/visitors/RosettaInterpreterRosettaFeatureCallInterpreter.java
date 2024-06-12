package com.regnosys.rosetta.interpreternew.visitors;

import java.util.List;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterBaseValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterDateTimeValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterDateValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnumElementValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterError;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterErrorValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterZonedDateTimeValue;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterBaseEnvironment;
import com.regnosys.rosetta.rosetta.RosettaEnumValue;
import com.regnosys.rosetta.rosetta.RosettaEnumeration;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaFeatureCall;
import com.regnosys.rosetta.rosetta.expression.impl.RosettaSymbolReferenceImpl;
import com.regnosys.rosetta.rosetta.impl.RosettaEnumValueImpl;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;

public class RosettaInterpreterRosettaFeatureCallInterpreter extends RosettaInterpreterConcreteInterpreter {

	
	
	
	/**
	 * Based on what class the symbol reference belongs to, it chooses what method to send it to.
	 *
	 * @param expr The RosettaFeatureCall expression to redirect
	 * @param env The Environment
	 * @return If no errors are encountered, a RosettaInterpreterNumberValue or
	 * 		   RosettaInterpreterStringValue representing
	 * 		   the result of the arithmetic/concatenation operation.
	 * 		   If errors are encountered, a RosettaInterpreterErrorValue representing
     *         the error.
	 */
	public RosettaInterpreterValue interp(RosettaFeatureCall expr,
			RosettaInterpreterEnvironment env) {
			RosettaSymbolReferenceImpl ref = (RosettaSymbolReferenceImpl) expr.getReceiver();
			RosettaEnumValue enumVal;
			if(expr.getFeature() instanceof RosettaEnumValueImpl) {
				enumVal = (RosettaEnumValueImpl) expr.getFeature();
				RosettaEnumeration enumeration = (RosettaEnumeration) ref.getSymbol();
				return interpEnum(enumeration, enumVal, env);
			}
			else {
				return interpRecord(expr, env);
			}
			
	}
	
	/**
	 * Interprets an enum feature call.
	 *
	 * @param enumeration The RosettaEnumeration to interpret alongside
	 * @param val The enum value to interpret
	 * @param env The Environment
	 * @return If no errors are encountered, a RosettaInterpreterNumberValue or
	 * 		   RosettaInterpreterStringValue representing
	 * 		   the result of the arithmetic/concatenation operation.
	 * 		   If errors are encountered, a RosettaInterpreterErrorValue representing
     *         the error.
	 */
	public RosettaInterpreterValue interpEnum(RosettaEnumeration enumeration, RosettaEnumValue val,
			RosettaInterpreterEnvironment env) {
		
			return new RosettaInterpreterEnumElementValue(val.getEnumeration().getName(),
							val.getName());	
	}
	
	
	/**
	 * Interpreter method for Feature Calls.
	 *
	 * @param exp 		RosettaFeatureCall to be interpreted
	 * @param env		the environment used
	 * @return 			the interpreted value
	 */
	public RosettaInterpreterBaseValue interpRecord(RosettaFeatureCall exp, RosettaInterpreterBaseEnvironment env) {
		RosettaExpression receiver = exp.getReceiver();
		RosettaInterpreterValue receiverValue = receiver.accept(visitor, env);
		
		String feature = exp.getFeature().getGetNameOrDefault();
		
		if (receiverValue instanceof RosettaInterpreterDateValue) {
			if (feature.equals("day")) {
				return ((RosettaInterpreterDateValue) receiverValue).getDay();
			} else if (feature.equals("month")) {
				return ((RosettaInterpreterDateValue) receiverValue).getMonth();
			} else {
				return ((RosettaInterpreterDateValue) receiverValue).getYear();
			}
			
		} else if (receiverValue instanceof RosettaInterpreterDateTimeValue) {
			if (feature.equals("date")) {
				return ((RosettaInterpreterDateTimeValue) receiverValue).getDate();
			} else {
				return ((RosettaInterpreterDateTimeValue) receiverValue).getTime();
			}
			
		} else if (receiverValue instanceof RosettaInterpreterZonedDateTimeValue) {
			if (feature.equals("date")) {
				return ((RosettaInterpreterZonedDateTimeValue) receiverValue).getDate();
			} else if (feature.equals("time")) {
				return ((RosettaInterpreterZonedDateTimeValue) receiverValue).getTime();
			} else {
				return ((RosettaInterpreterZonedDateTimeValue) receiverValue).getTimeZone();
			}
			
		} else if (RosettaInterpreterErrorValue.errorsExist(receiverValue)) {
			RosettaInterpreterErrorValue expError = (RosettaInterpreterErrorValue) receiverValue;
			RosettaInterpreterErrorValue newExpError = 
					new RosettaInterpreterErrorValue(
							new RosettaInterpreterError("Feature calls: the "
									+ "receiver is an error value."));
			
			return RosettaInterpreterErrorValue.merge(List.of(newExpError, expError));
		}
		return new RosettaInterpreterErrorValue(new RosettaInterpreterError(
				"Feature calls: receiver doesn't exist."));
	}
}
