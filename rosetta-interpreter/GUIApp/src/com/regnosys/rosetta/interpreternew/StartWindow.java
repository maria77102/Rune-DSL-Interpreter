package com.regnosys.rosetta.interpreternew;

import com.google.inject.Injector;
import com.regnosys.rosetta.tests.util.ExpressionParser;
import com.regnosys.rosetta.tests.util.ModelHelper;
import com.regnosys.rosetta.RosettaStandaloneSetup;
import com.regnosys.rosetta.interpreternew.values.RosettaInterpreterEnvironment;
import com.regnosys.rosetta.rosetta.RosettaModel;
import com.regnosys.rosetta.rosetta.expression.impl.RosettaSymbolReferenceImpl;
//import com.regnosys.rosetta.interpreternew.RosettaInterpreterNew;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;
import com.regnosys.rosetta.rosetta.simple.impl.FunctionImpl;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Color;

public class StartWindow {

	Injector injector = new RosettaStandaloneSetup().createInjectorAndDoEMFRegistration();
	ExpressionParser parser = injector.getInstance(ExpressionParser.class);
	ModelHelper modelHelper = injector.getInstance(ModelHelper.class);
	RosettaInterpreterNew interpreter = injector.getInstance(RosettaInterpreterNew.class); 
	
	RosettaInterpreterVisitor visitor = injector.getInstance(RosettaInterpreterVisitor.class);
	RosettaInterpreterEnvironment environment = injector.getInstance(RosettaInterpreterEnvironment.class);
	
	
	String example_1 = 
			  "func Add:\n"
			+ "  inputs:\n"
			+ "    a number (1..1)\n"
			+ "    b int (1..1)\n"
			+ "  output:\n"
			+ "    result number (1..10)\n\n"
			+ "  alias bee: b\n"
			+ "  set result:\n"
			+ "    a + bee\n\n"
			+ "func MyTest:\n"
			+ "  output:\n"
			+ "    result number (1..10)\n"
			+ "  set result:\n"
			+ "    Add(2.0, 1.0)\n";
	
    private JFrame frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        StartWindow window = new StartWindow();
        window.frame.setVisible(true);
    }

    /**
     * Create the application.
     */
    public StartWindow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setFont(new Font("Calibri", Font.PLAIN, 14));
        frame.setBounds(100, 100, 1363, 772);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Scroll pane for code text area
        JScrollPane codeScrollPane = new JScrollPane();
        codeScrollPane.setBounds(10, 131, 507, 548);
        frame.getContentPane().add(codeScrollPane);

        // Text area for code input
        JTextArea codeTextArea = new JTextArea();
        codeTextArea.setFont(new Font("Cascadia Code", Font.PLAIN, 13));
        codeTextArea.setRows(31);
        codeScrollPane.setViewportView(codeTextArea);

        // Button to interpret code
        JButton interpretButton = new JButton("Interpret");
        interpretButton.setBackground(new Color(240, 240, 240));
        interpretButton.setFont(new Font("Calibri", Font.PLAIN, 21));
        interpretButton.setBounds(540, 351, 172, 51);
        frame.getContentPane().add(interpretButton);

        // Scroll pane for result text area
        JScrollPane resultScrollPane = new JScrollPane();
        resultScrollPane.setBounds(742, 285, 580, 168);
        frame.getContentPane().add(resultScrollPane);

        // Text area for displaying results
        JTextArea resultTextArea = new JTextArea();
        resultTextArea.setRows(9);
        resultTextArea.setColumns(50);
        resultTextArea.setEditable(false); // Make it read-only
        resultScrollPane.setViewportView(resultTextArea);
        
        JComboBox<String> tests = new JComboBox<String>();
        tests.setFont(new Font("Calibri", Font.PLAIN, 14));
        tests.setEditable(true);
        tests.setToolTipText("");
        tests.setBounds(10, 91, 172, 28);
        frame.getContentPane().add(tests);
        
        tests.setSelectedItem("Select Rune Example");
        
        JButton exampleButton = new JButton("Get Code");
        exampleButton.setBackground(new Color(255, 255, 255));
        exampleButton.setFont(new Font("Calibri", Font.PLAIN, 14));
        exampleButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String chosenExample = tests.getSelectedItem().toString();
        		switch(chosenExample) {
        			case "Example 1":
        				codeTextArea.setText(example_1);
        			default:
        				break;
        		}	
        	}
        });
        exampleButton.setBounds(221, 93, 125, 26);
        frame.getContentPane().add(exampleButton);
        tests.addItem("Example 1");
        tests.addItem("Example 2");
        tests.addItem("Example 3");

        // Action listener for interpret button
        interpretButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the code from the codeTextArea
                String code = codeTextArea.getText();

//                RosettaExpression expr = parser.parseExpression(code);
                RosettaModel model = modelHelper.parseRosettaWithNoErrors(code);
                FunctionImpl function = (FunctionImpl) model.getElements().get(0);
            	RosettaSymbolReferenceImpl ref = (RosettaSymbolReferenceImpl) 
            			((FunctionImpl)model.getElements().get(1)).getOperations().get(0).getExpression();
            	RosettaInterpreterEnvironment env = 
            			(RosettaInterpreterEnvironment) interpreter.interp(function);
            	RosettaInterpreterValue result = interpreter.interp(ref, env);
//            	System.out.println(function.getClass().getName());
//                System.out.println(result);
//                 Display the result in the resultTextArea
                resultTextArea.setText(result.toString());
            }
        });
    }
}
