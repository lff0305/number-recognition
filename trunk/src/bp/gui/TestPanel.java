package bp.gui;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import convolutional.*;



import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class TestPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4775331159341713203L;

	private GridPanel panel;
	
	private JLabel lblResult;
	private JTextPane  txtResult;

	private Convolutional cn = new Convolutional();
	private JLabel lblNewLabel;
	private static final String db = "60000_9.nn";
	
	/**
	 * Create the panel.
	 */
	public TestPanel() {
		setLayout(null);

		panel = new GridPanel(false);
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 34, 242, 242);
		add(panel);

		JButton btnReg = new JButton("Recgnize");
		btnReg.setBounds(20, 282, 93, 23);
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
		txtResult.setBounds(262, 34, 178, 242);
		add(txtResult);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.clear();
			}
		});
		btnClear.setBounds(144, 282, 93, 23);
		add(btnClear);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(10, 10, 242, 15);
		add(lblNewLabel);
		
		try {
			cn.getNN().load(db);
			lblNewLabel.setText(db + " is loaded.");
		} catch (FileNotFoundException e) {
			lblNewLabel.setText(db + " cannot found.");
		} catch (IOException e) {
			lblNewLabel.setText(db + " cannot read. Exception " + e.getMessage());
		}	
	}

	public void init() {

	}
	
	private void reg() {
		double[] input = new double[29 * 29];
		for (int i=0; i<29; i++) {
			for (int j=0; j<29; j++) {
				// one is white, -one is black
				input[i*29+j] = (panel.getGrid()[j][i] == true ? 1 : -1);
			}
		}
		
		double[] actualOutputVector = cn.calculate(input);
		int r = Utility.getMax(actualOutputVector);
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
