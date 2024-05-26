package com.regnosys.rosetta.interpreternew.visitors;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.regnosys.rosetta.interpreternew.RosettaInterpreterNew;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterErrorValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterIntegerValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterListValue;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterNumberValue;
import com.regnosys.rosetta.rosetta.expression.ExpressionFactory;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.expression.impl.ExpressionFactoryImpl;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;
import com.regnosys.rosetta.tests.RosettaInjectorProvider;
import com.regnosys.rosetta.tests.util.ExpressionParser;
import com.regnosys.rosetta.tests.util.ExpressionValidationHelper;

@ExtendWith(InjectionExtension.class)
@InjectWith(RosettaInjectorProvider.class)
class RosettaInterpreterListOperatorInterpreterTest {
	@Inject
	private ExpressionParser parser;
	@Inject
	RosettaInterpreterNew interpreter;
	@Inject
	private ExpressionValidationHelper validation;
	@SuppressWarnings("unused")
	private ExpressionFactory exFactory;
	
	@BeforeEach
	public void setup() {
		exFactory = ExpressionFactoryImpl.init();
	}

	@Test
	void testInterpDistinctOperationUseless() {
		String expressionText = "[1 ,2] distinct";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		RosettaInterpreterListValue exp = new RosettaInterpreterListValue(
				List.of(new RosettaInterpreterIntegerValue(1), 
						new RosettaInterpreterIntegerValue(2)));
		
		assertEquals(exp, val);
	}
	
	@Test
	void testInterpDistinctOperationSimple() {
		String expressionText = "[1 ,2, 2] distinct";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		RosettaInterpreterListValue exp = new RosettaInterpreterListValue(
				List.of(new RosettaInterpreterIntegerValue(1), 
						new RosettaInterpreterIntegerValue(2)));
		
		assertEquals(exp, val);
	}
	
	@Test
	void testInterpDistinctOperationMany() {
		String expressionText = "[2, 2, 2, 2, 2, 2] distinct";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		RosettaInterpreterListValue exp = new RosettaInterpreterListValue(
				List.of(new RosettaInterpreterIntegerValue(2)));
		
		assertEquals(exp, val);
	}
	
	@Test
	void testInterpDistinctOperationEmpty() {
		String expressionText = "[] distinct";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		RosettaInterpreterListValue exp = new RosettaInterpreterListValue(
				List.of());
		
		assertEquals(exp, val);
	}
	
	@Test
	void testInterpDistinctOperationNonList() {
		String expressionText = "1 distinct";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		RosettaInterpreterListValue exp = new RosettaInterpreterListValue(
				List.of(new RosettaInterpreterIntegerValue(1)));
		
		assertEquals(exp, val);
	}
	
	@Test
	void testInterpDistinctOperationError() {
		String expressionText = "[(1 and False), 2] distinct";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		
		assertTrue(val instanceof RosettaInterpreterErrorValue);
	}

	@Test
	void testInterpReverseOperationSimple() {
		String expressionText = "[1 ,2] reverse";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		RosettaInterpreterListValue exp = new RosettaInterpreterListValue(
				List.of(new RosettaInterpreterIntegerValue(2), 
						new RosettaInterpreterIntegerValue(1)));
		
		assertEquals(exp, val);
	}
	
	@Test
	void testInterpReverseOperationSingle() {
		String expressionText = "1 reverse";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		RosettaInterpreterListValue exp = new RosettaInterpreterListValue(
				List.of(new RosettaInterpreterIntegerValue(1)));
		
		assertEquals(exp, val);
	}
	
	@Test
	void testInterpReverseOperationEmpty() {
		String expressionText = "[] reverse";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		RosettaInterpreterListValue exp = new RosettaInterpreterListValue(
				List.of());
		
		assertEquals(exp, val);
	}
	
	@Test
	void testInterpReverseOperationBig() {
		String expressionText = "[1 ,2, 3, 4, 5, 6] reverse";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		RosettaInterpreterListValue exp = new RosettaInterpreterListValue(
				List.of(new RosettaInterpreterIntegerValue(6),
						new RosettaInterpreterIntegerValue(5),
						new RosettaInterpreterIntegerValue(4),
						new RosettaInterpreterIntegerValue(3),
						new RosettaInterpreterIntegerValue(2), 
						new RosettaInterpreterIntegerValue(1)));
		
		assertEquals(exp, val);
	}
	
	@Test
	void testInterpReverseOperationError() {
		String expressionText = "[(1 and False), 2] reverse";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		
		assertTrue(val instanceof RosettaInterpreterErrorValue);
	}

	@Test
	void testInterpSumOperationSimple() {
		String expressionText = "[1 ,2] sum";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		RosettaInterpreterNumberValue exp =
				new RosettaInterpreterNumberValue(BigDecimal.valueOf(3));
		
		assertEquals(exp, val);
	}
	
	@Test
	void testInterpSumOperationSingle() {
		String expressionText = "2 sum";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		RosettaInterpreterNumberValue exp =
				new RosettaInterpreterNumberValue(BigDecimal.valueOf(2));
		
		assertEquals(exp, val);
	}
	
	@Test
	void testInterpSumOperationMixed() {
		String expressionText = "[2, 3.5, 0.1] sum";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		RosettaInterpreterNumberValue exp =
				new RosettaInterpreterNumberValue(BigDecimal.valueOf(5.6));
		
		assertEquals(exp, val);
	}
	
	@Test
	void testInterpSumOperationEmpty() {
		String expressionText = "[] sum";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		
		assertTrue(val instanceof RosettaInterpreterErrorValue);
	}
	
	@Test
	void testInterpSumOperationWrongType() {
		String expressionText = "[1, True, 3] sum";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		
		assertTrue(val instanceof RosettaInterpreterErrorValue);
	}
	
	@Test
	void testInterpSumOperationErrorInside() {
		String expressionText = "[1, (1 and False), 3] sum";
		RosettaExpression expr = parser.parseExpression(expressionText);
		validation.assertNoIssues(expr);
		RosettaInterpreterValue val = interpreter.interp(expr);
		
		assertTrue(val instanceof RosettaInterpreterErrorValue);
	}

}
