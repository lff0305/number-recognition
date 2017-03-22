package convolutional.gui;


import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import convolutional.*;



import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class TestPanel extends JPanel {

	private GridPanel panel;
	
	private JLabel lblResult;
	private JTextPane  txtResult;

	private Convolutional cn = new Convolutional();

	/**
	 * Create the panel.
	 */
	public TestPanel() {
		setLayout(null);

		panel = new GridPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 10, 242, 242);
		add(panel);

		JButton btnReg = new JButton("Recgnize");
		btnReg.setBounds(20, 262, 93, 23);
		add(btnReg);
		btnReg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reg();
			}
		});

		lblResult = new JLabel("<NA>");
		lblResult.setBounds(265, 10, 175, 15);
		add(lblResult);

		txtResult = new JTextPane();
		txtResult.setBounds(262, 46, 178, 206);
		add(txtResult);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.clear();
			}
		});
		btnClear.setBounds(144, 262, 93, 23);
		add(btnClear);
		
		cn.getNN().load("60000_9.nn");
	}

	public void init() {

	}
	
	private void reg() {
		double[] input = new double[29 * 29];
		for (int i=0; i<29; i++) {
			for (int j=0; j<29; j++) {
				System.out.print(panel.getGrid()[j][i] == true ? "*" : " ");
				// one is white, -one is black
				input[i*29+j] = (panel.getGrid()[j][i] == true ? 1 : -1);
			}
			System.out.println();
		}
		
		for (int i=0; i<29; i++) {
			for (int j=0; j<29; j++) {
				System.out.print(input[i*29 + j] + " ");
			}
			System.out.println();
		}
		
		double[] actualOutputVector = cn.calculate(input);
		int r = Utility.getMax(actualOutputVector);
		System.out.println(Arrays.toString(actualOutputVector));
		System.out.println("Get : " + r);
		lblResult.setText("Get : " + String.valueOf(r));
		txtResult.setText("");
		output(actualOutputVector, r);
	}

	private void output(double[] actualOutputVector, int result) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<actualOutputVector.length; i++) {
			sb.append(i);
			sb.append("=");
			sb.append(actualOutputVector[i]);
			sb.append("\n");
			if (i == result) {
				addResult(sb.toString(), Color.red);
			} else {
				addResult(sb.toString(), Color.blue);
			}
			sb = new StringBuilder();
		}
	}

	private void addResult(String txt, Color color) {
		SimpleAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setForeground(set, color);//设置文字颜色
		StyleConstants.setFontSize(set, 12);//设置字体大小
		Document doc = txtResult.getStyledDocument();
		try  {
		   doc.insertString(doc.getLength(), txt, set);//插入文字
		}
		catch (BadLocationException e) 	 {
		}		
	}
}
