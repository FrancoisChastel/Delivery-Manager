package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SettingFrame extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField textField;
	private JButton fileChooserButton;
	private JFileChooser fc;
	private File currentFile = null;
	private JButton validateButton;
	private View view;
	/**
	 * Create the frame.
	 */
	public SettingFrame(View view) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 452, 197);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		this.view = view;
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Choose the Map file (XML)");
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
		
		
		fc = new JFileChooser();		
		
		//file chooser JButton
		fileChooserButton = new JButton("Choose");		
		panel.add(fileChooserButton);
		fileChooserButton.addActionListener(this);
		
		// validate JButton
		validateButton = new JButton("Validate");
		validateButton.addActionListener(this);
		contentPane.add(validateButton, BorderLayout.SOUTH);
	}
	
	/**
	 * Action callback for fileChooserButton and for 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Click on file Chooser Button
		if (e.getSource() == fileChooserButton) {
            int returnVal = fc.showOpenDialog(SettingFrame.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	currentFile = fc.getSelectedFile();
                textField.setText(currentFile.getName());
            }
		}
		// Click on validate
		else if (e.getSource() == validateButton) {
			view.getController().parseMapFile(currentFile);
		}
	}
}
