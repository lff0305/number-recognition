package convolutional.gui;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import convolutional.Convolutional;
import convolutional.Utility;

public class VerifyPanel extends JPanel {

	private GridPanel panel;
	
	private JLabel lblResult;
	private JTextPane  txtResult;

	private Convolutional cn = new Convolutional();
	private JTextField edtIndex;
	private RandomAccessFile imageFile = null;
	private RandomAccessFile labelFile = null;
	
	
	private static final String db = "60000_9.nn";
	
	private boolean fileLoaded = false;
	private int count;
	private JLabel lblNewLabel;
	/**
	 * Create the panel.
	 */
	public VerifyPanel() {
		setLayout(null);

		panel = new GridPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 46, 242, 206);
		add(panel);

		JButton btnLeft = new JButton("<<");
		btnLeft.setBounds(33, 262, 51, 23);
		add(btnLeft);
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				move(-1);
				verify();
			}
		});

		lblResult = new JLabel("<NA>");
		lblResult.setBounds(265, 10, 175, 15);
		add(lblResult);

		txtResult = new JTextPane();
		txtResult.setBounds(262, 46, 178, 206);
		add(txtResult);

		JButton btnRight = new JButton(">>");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				move(1);
				verify();
			}
		});
		btnRight.setBounds(178, 262, 51, 23);
		add(btnRight);
		
		edtIndex = new JTextField();
		edtIndex.setHorizontalAlignment(SwingConstants.CENTER);
		edtIndex.setText("0");
		edtIndex.setBounds(94, 262, 66, 21);
		add(edtIndex);
		edtIndex.setColumns(10);
		
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
		
		fileLoaded = loadFiles();
		if (fileLoaded) {
			verify();
		}
	}
	
	private void move(int delta) {
		try {
			int idx = Integer.valueOf(edtIndex.getText());
			idx += delta;
			if (idx >= count) {
				idx = 0;
			}
			if (idx < 0) {
				idx = count - 1;
			}
			edtIndex.setText(String.valueOf(idx));
		} catch (RuntimeException e) {
			JOptionPane.showMessageDialog(this, "Parse integer failed : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}		
	}

	private void verify() {
		if (!fileLoaded) {
			JOptionPane.showMessageDialog(this, "Verify Files are not loaded.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			int idx = Integer.valueOf(edtIndex.getText());
			int pos = 16 + idx * 28 * 28; //28*28
			imageFile.seek(pos);
			boolean[][] g = new boolean[29][29];
			for (int i=0; i<29; i++) {
				for (int j=0; j<29; j++) {
					g[i][j] = false;
				}
			}
			for (int i=0; i<28; i++) {
				for (int j=0; j<28; j++) {
					int grey = imageFile.readByte() & 0xff;
					if (grey > 200) {
						g[j + 1][i + 1] = true;
					} else {
						g[j + 1][i + 1] = false;
					}
				}
			}
			panel.draw(g);
			
			labelFile.seek(8 + idx);
			int expected = labelFile.readByte() & 0xff;
			reg(cn, expected);
			
		} catch (RuntimeException e) {
			JOptionPane.showMessageDialog(this, "Parse integer failed : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "File read error : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	private void reg(Convolutional cn, int expected) {
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
		lblResult.setText("Get : " + r + " expected = " + String.valueOf(expected));
		txtResult.setText("");
		output(actualOutputVector, r);
		
	}

	private boolean loadFiles() {
		try {
			imageFile = new RandomAccessFile("t10k-images.idx3-ubyte", "r");
		}  catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "File t10k-images.idx3-ubyte does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		try {
			labelFile = new RandomAccessFile("t10k-labels.idx1-ubyte", "r");
		}  catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "File train-labels.idx1-ubyte does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		try {
			imageFile.seek(0);
			int magic = imageFile.readInt(); //ignores magic
			count = imageFile.readInt();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "File t10k-images.idx3-ubyte read error:" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		
		return true;
	}

	public void init() {

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
