package com.regnosys.rosetta.interpreternew;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import javax.inject.Inject;

import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterErrorValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterNumberValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterStringValue;
import com.regnosys.rosetta.rosetta.expression.ExpressionFactory;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;
import com.regnosys.rosetta.rosetta.expression.impl.ExpressionFactoryImpl;
import com.regnosys.rosetta.tests.RosettaInjectorProvider;
import com.regnosys.rosetta.tests.util.ExpressionParser;
import com.rosetta.model.lib.RosettaNumber;

@ExtendWith(InjectionExtension.class)
@InjectWith(RosettaInjectorProvider.class)
public class RosettaInterpreterArithmeticOperationsTest {
	@Inject
	private ExpressionParser parser;
	@Inject
	RosettaInterpreterNew interpreter;

	@SuppressWarnings("unused")
	private ExpressionFactory eFactory;
	
	@BeforeEach
	public void setup() {
		eFactory = ExpressionFactoryImpl.init();
	}
	
	@Test
	public void PlusTest() {
		RosettaExpression expr = parser.parseExpression("1+2");
		RosettaInterpreterValue val = interpreter.interp(expr);
		assertEquals(RosettaNumber.valueOf(BigDecimal.valueOf(3)), ((RosettaInterpreterNumberValue)val).getValue());
	}
	
	@Test
	public void CompositePlusTest() {
		RosettaExpression expr = parser.parseExpression("1+2+3");
		RosettaInterpreterValue val = interpreter.interp(expr);
		assertEquals(RosettaNumber.valueOf(BigDecimal.valueOf(6)), ((RosettaInterpreterNumberValue)val).getValue());
	}
	
	@Test
	public void DecimalPlusTest() {
		RosettaExpression expr = parser.parseExpression("1.2 + 2.7");
		RosettaInterpreterValue val = interpreter.interp(expr);
		assertEquals(RosettaNumber.valueOf(BigDecimal.valueOf(3.9)), ((RosettaInterpreterNumberValue)val).getValue());
	}

	@Test
	public void MinusTest() {
		RosettaExpression expr = parser.parseExpression("1-2");
		RosettaInterpreterValue val = interpreter.interp(expr);
		assertEquals(RosettaNumber.valueOf(BigDecimal.valueOf(-1)), ((RosettaInterpreterNumberValue)val).getValue());
	}
	
	@Test
	public void MultiplyTest() {
		RosettaExpression expr = parser.parseExpression("5*2");
		RosettaInterpreterValue val = interpreter.interp(expr);
		assertEquals(RosettaNumber.valueOf(BigDecimal.valueOf(10)), ((RosettaInterpreterNumberValue)val).getValue());
	}
	
	@Test
	public void DivideTest() {
		RosettaExpression expr = parser.parseExpression("6/2");
		RosettaInterpreterValue val = interpreter.interp(expr);
		assertEquals(RosettaNumber.valueOf(BigDecimal.valueOf(3)), ((RosettaInterpreterNumberValue)val).getValue());
	}

	@Test
	public void StringConcatenationTest() {
		RosettaExpression expr = parser.parseExpression("\"Hello \" + \"World\"");
		RosettaInterpreterValue val = interpreter.interp(expr);
		assertEquals("Hello World", ((RosettaInterpreterStringValue)val).getValue());
	}
	
	@Test
	public void StringConcatenationErrorTest() {
		RosettaExpression expr = parser.parseExpression("\"Hello \" - \"World\"");
		RosettaInterpreterValue val = interpreter.interp(expr);
		assertEquals("The terms are strings but the operation is not concatenation: not implemented", 
				((RosettaInterpreterErrorValue)val).getErrors().get(0).getMessage());
	}
	
	@Test
	public void WrongTypeLeftTest() {
		RosettaExpression expr = parser.parseExpression("True - \"World\"");
		RosettaInterpreterValue val = interpreter.interp(expr);
		assertEquals("Arithmetic Operation: Leftside is not of type Number/String", 
				((RosettaInterpreterErrorValue)val).getErrors().get(0).getMessage());
	}
	
	@Test
	public void WrongTypeRightTest() {
		RosettaExpression expr = parser.parseExpression("\"Hello \" + True");
		RosettaInterpreterValue val = interpreter.interp(expr);
		assertEquals("Arithmetic Operation: RightSide is not of type Number/String", 
				((RosettaInterpreterErrorValue)val).getErrors().get(0).getMessage());
	}
	
	@Test
	public void LeftsideErrorTest() {
		RosettaExpression expr = parser.parseExpression("\"Hello \" - \"World\" + \"World\"");
		RosettaInterpreterValue val = interpreter.interp(expr);
		assertEquals("The terms are strings but the operation is not concatenation: not implemented", 
				((RosettaInterpreterErrorValue)val).getErrors().get(0).getMessage());
	}
}
