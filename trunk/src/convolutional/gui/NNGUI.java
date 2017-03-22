package convolutional.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;



public class NNGUI extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private TestPanel testPanel;
	private VerifyPanel verifyPanel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NNGUI dialog = new NNGUI();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		testPanel.init();		
	}

	/**
	 * Create the dialog.
	 */
	public NNGUI() {
		setBounds(100, 100, 633, 462);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 617, 391);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 10, 597, 371);
		
		JLabel label1=new JLabel("abc");
		JPanel panel1=new JPanel();
		panel1.add(label1);
		     
		JLabel label2=new JLabel("Label 2",JLabel.CENTER);
		label2.setBackground(Color.pink);
		label2.setOpaque(true);
		JPanel panel2=new JPanel();
		panel2.add(label2);
		     
		JLabel label3=new JLabel("Label 3",JLabel.CENTER);
		label3.setBackground(Color.yellow);
		label3.setOpaque(true);
		JPanel panel3=new JPanel();
		panel3.add(label3);
		     
		tabbedPane.setTabPlacement(JTabbedPane.TOP);
		
		
		verifyPanel = new VerifyPanel();
		tabbedPane.addTab("Verify", null, verifyPanel);
		
		testPanel = new TestPanel();
		tabbedPane.addTab("Test",null, testPanel);
		// tabbedPane.addTab("Train",panel2);

		
//		tabbedPane.addTab("Test", null);
//		tabbedPane.addTab("Train", null);
		contentPanel.add(tabbedPane);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 391, 617, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	}
}
