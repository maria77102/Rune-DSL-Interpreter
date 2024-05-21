package com.regnosys.rosetta.interpreternew;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterBooleanValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterIntegerValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterNumberValue;
import com.regnosys.rosetta.rosetta.expression.ArithmeticOperation;
import com.regnosys.rosetta.rosetta.expression.ExpressionFactory;
import com.regnosys.rosetta.rosetta.expression.LogicalOperation;
import com.regnosys.rosetta.rosetta.expression.RosettaBooleanLiteral;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaIntLiteral;
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


}
