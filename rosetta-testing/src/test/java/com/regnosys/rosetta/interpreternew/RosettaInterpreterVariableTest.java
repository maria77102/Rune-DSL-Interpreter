package com.regnosys.rosetta.interpreternew;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterBooleanValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterError;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterErrorValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterIntegerValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterListValue;
import com.regnosys.rosetta.rosetta.expression.ExpressionFactory;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.expression.impl.ExpressionFactoryImpl;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterBaseError;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;
import com.regnosys.rosetta.tests.RosettaInjectorProvider;
import com.regnosys.rosetta.tests.util.ExpressionParser;


@ExtendWith(InjectionExtension.class)
@InjectWith(RosettaInjectorProvider.class)
public class RosettaInterpreterVariableTest {
	
	@Inject
	private ExpressionParser parser;
	@Inject
	RosettaInterpreterNew interpreter;
	
	@SuppressWarnings("unused")
	private ExpressionFactory exFactory;
	
	@BeforeEach
	public void setup() {
		exFactory = ExpressionFactoryImpl.init();
	}

	
//	@Test
//	public void variableGoodComparisonTest() {
//		//create the environment and add variable 'a' to it
//		RosettaInterpreterEnvironment env = new RosettaInterpreterEnvironment();
//		RosettaInterpreterIntegerValue intValue = 
//				new RosettaInterpreterIntegerValue(BigInteger.valueOf(5));
//		env.addValue("a", intValue);
//
//		//give the same environment to the parser
//		RosettaExpression expr = parser.parseExpression("a >= 2", 
//				List.of("a int (1..1)"));
//		
//		RosettaInterpreterValue val = interpreter.interp(expr,env);
//		
//		assertTrue(((RosettaInterpreterBooleanValue)val).getValue());
//	}
//	
//	@Test
//	public void variableLeftErrorComparisonTest() {
//		//create the environment and add variable 'a' to it
//		RosettaInterpreterEnvironment env = new RosettaInterpreterEnvironment();
//		RosettaInterpreterIntegerValue intValue = 
//				new RosettaInterpreterIntegerValue(BigInteger.valueOf(5));
//		env.addValue("a", intValue);
//		
//		RosettaInterpreterErrorValue expectedError = new RosettaInterpreterErrorValue(
//				new RosettaInterpreterError(
//						"b does not exist in the environment"));
//
//		//give a different environment to the parser
//		RosettaExpression expr = parser.parseExpression("b >= 2", 
//				List.of("b int (1..1)"));
//		
//		RosettaInterpreterValue val = interpreter.interp(expr,env);
//		
//		assertEquals(expectedError.getErrors().get(0).getMessage(),
//				((RosettaInterpreterErrorValue)val)
//					.getErrors().get(0).getMessage());
//	}
//	
//	@Test
//	public void variableRightErrorComparisonTest() {
//		//create empty environment
//		RosettaInterpreterEnvironment env = new RosettaInterpreterEnvironment();
//		
//		RosettaInterpreterErrorValue expectedError = new RosettaInterpreterErrorValue(
//				new RosettaInterpreterError(
//						"b does not exist in the environment"));
//
//		//give a different environment to the parser
//		RosettaExpression expr = parser.parseExpression("1 = b", 
//				List.of("b int (1..1)"));
//		
//		RosettaInterpreterValue val = interpreter.interp(expr,env);
//		
//		assertEquals(expectedError.getErrors().get(0).getMessage(),
//				((RosettaInterpreterErrorValue)val)
//					.getErrors().get(0).getMessage());
//	}
//	
//	@Test
//	public void variableBothErrorTest() {
//		//create empty environment
//		RosettaInterpreterEnvironment env = new RosettaInterpreterEnvironment();
//		
//		List<RosettaInterpreterError> expected = new ArrayList<RosettaInterpreterError>();
//		expected.add(new RosettaInterpreterError(
//						"a does not exist in the environment"));
//		expected.add(new RosettaInterpreterError(
//						"b does not exist in the environment"));
//
//		//give a different environment to the parser
//		RosettaExpression expr = parser.parseExpression("a <= b", 
//				List.of("a int (1..1)", "b int (1..1)"));
//		
//		RosettaInterpreterValue val = interpreter.interp(expr,env);
//		
//		EList<RosettaInterpreterBaseError> errors = 
//				((RosettaInterpreterErrorValue) val).getErrors();
//		
//		assertEquals(expected.size(), errors.size());
//		for (int i = 0; i < expected.size(); i++) {
//			RosettaInterpreterError newError = (RosettaInterpreterError) errors.get(i);
//			assertEquals(expected.get(i).getError(), newError.getError());
//		}
//	}
	
	
	@Test
    public void demoTest() {
        RosettaExpression listExpr = parser.parseExpression("[1,2,3]");
        RosettaInterpreterListValue list = (RosettaInterpreterListValue) interpreter.interp(listExpr);
        RosettaInterpreterEnvironment env = new RosettaInterpreterEnvironment();
        env.addValue("list", list);

        RosettaExpression expr = parser.parseExpression(
                "if (list contains 1) then (list last) else (list first)",
                List.of("list number (1..*)"));

        RosettaInterpreterValue result = interpreter.interp(expr, env);

        assertEquals(BigInteger.valueOf(3), 
                ((RosettaInterpreterIntegerValue) result).getValue());
    }
	
	@Test
    public void nbInt() {
        RosettaExpression expr = parser.parseExpression("42 = 42.0");

        RosettaInterpreterValue result = interpreter.interp(expr);

        assertEquals(BigInteger.valueOf(3), 
                ((RosettaInterpreterBooleanValue) result).getValue());
    }

}
