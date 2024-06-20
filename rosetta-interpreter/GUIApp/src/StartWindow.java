import com.google.inject.Injector;
import com.regnosys.rosetta.tests.util.ExpressionParser;
import com.regnosys.rosetta.tests.util.ModelHelper;
import com.regnosys.rosetta.RosettaStandaloneSetup;
import com.regnosys.rosetta.interpreternew.RosettaInterpreterNew;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWindow {

	Injector injector = new RosettaStandaloneSetup().createInjectorAndDoEMFRegistration();
	ExpressionParser parser = injector.getInstance(ExpressionParser.class);
	ModelHelper modelHelper = injector.getInstance(ModelHelper.class);
	RosettaInterpreterNew interpreter = injector.getInstance(RosettaInterpreterNew.class); 
	
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
        frame.setBounds(100, 100, 1180, 653);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Scroll pane for code text area
        JScrollPane codeScrollPane = new JScrollPane();
        codeScrollPane.setBounds(10, 21, 427, 564);
        frame.getContentPane().add(codeScrollPane);

        // Text area for code input
        JTextArea codeTextArea = new JTextArea();
        codeTextArea.setRows(31);
        codeScrollPane.setColumnHeaderView(codeTextArea);

        // Button to interpret code
        JButton interpretButton = new JButton("Interpret");
        interpretButton.setBounds(469, 273, 172, 51);
        frame.getContentPane().add(interpretButton);

        // Scroll pane for result text area
        JScrollPane resultScrollPane = new JScrollPane();
        resultScrollPane.setBounds(670, 240, 406, 114);
        frame.getContentPane().add(resultScrollPane);

        // Text area for displaying results
        JTextArea resultTextArea = new JTextArea();
        resultTextArea.setRows(6);
        resultTextArea.setColumns(50);
        resultTextArea.setEditable(false); // Make it read-only
        resultScrollPane.setColumnHeaderView(resultTextArea);

        // Action listener for interpret button
        interpretButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the code from the codeTextArea
                String code = codeTextArea.getText();

                // Placeholder for interpreter logic
                // You need to implement the actual interpreter and replace this code
                RosettaExpression expr = parser.parseExpression(code);

                RosettaInterpreterValue result = interpreter.interp(expr);

                System.out.println(result);
                // Display the result in the resultTextArea
                resultTextArea.setText(result.toString());
            }
        });
    }
}
