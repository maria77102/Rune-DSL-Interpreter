import com.regnosys.rosetta.tests.RosettaInjectorProvider;
import com.regnosys.rosetta.tests.util.ExpressionParser;
import com.regnosys.rosetta.tests.util.ModelHelper;
import com.regnosys.rosetta.rosetta.RosettaModel;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.interpreter.RosettaInterpreterValue;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class StartWindow {

	private ExpressionParser parser = new ExpressionParser();
	
//	private RosettaInterpreterNew interpreter = new RosettaInterpreterNew();
	
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartWindow window = new StartWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
                
//                RosettaInterpreterValue result = interpreter.interp(expr);
//                
                System.out.println(expr);
                // Display the result in the resultTextArea
//                resultTextArea.setText(result.toString());
            }
        });
	}

}
