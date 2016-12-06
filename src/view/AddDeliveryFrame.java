package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.Font;
import javax.swing.JButton;

public class AddDeliveryFrame extends JFrame {

	private JPanel contentPane;
	private JTextField toTF;
	private JTextField fromTF;
	private JTextField durationTF;
	private JTextField addressTF;

	private MainFrame frame;
	private int pickedPointId;
	private int index;
	private long availableTime;
	private int tourId;
	private Date begining;
	private Date end;
	
	/**
	 * Create the frame.
	 */
	public AddDeliveryFrame(MainFrame frame) {
		this.frame=frame;
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JLabel lblNewLabel_1 = new JLabel("Availability");
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 14));
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("HH:MM");
		lblNewLabel_2.setFont(new Font("Dialog", Font.ITALIC, 12));
		contentPane.add(lblNewLabel_2);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblAvailableFrom = new JLabel("from  :");
		panel_1.add(lblAvailableFrom);
		
		fromTF = new JTextField();
		fromTF.setColumns(10);
		panel_1.add(fromTF);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblNewLabel = new JLabel(" to :");
		panel.add(lblNewLabel);
		
		toTF = new JTextField();
		panel.add(toTF);
		toTF.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		panel_2.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblDuration = new JLabel("duration :");
		panel_2.add(lblDuration);
		
		durationTF = new JTextField();
		durationTF.setColumns(10);
		panel_2.add(durationTF);
		
		JLabel lblNewLabel_3 = new JLabel("Address");
		lblNewLabel_3.setFont(new Font("Dialog", Font.BOLD, 14));
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_5 = new JLabel("Click on a point on the map");
		lblNewLabel_5.setFont(new Font("Dialog", Font.ITALIC, 12));
		contentPane.add(lblNewLabel_5);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3);
		
		JLabel lblNewLabel_4 = new JLabel("address :");
		panel_3.add(lblNewLabel_4);
		
		addressTF = new JTextField();
		addressTF.setText(" - ");
		panel_3.add(addressTF);
		addressTF.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Create point");
		btnNewButton_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validateFields())
					frame.getView().getController().addPoint(tourId, index, new Integer(addressTF.getText()), new Integer(durationTF.getText()), begining, end);
			}
		});
		contentPane.add(btnNewButton_1);
		
		pack();
	}
	
	
	/**
	 * This method checks if fields are corrects
	 * @return
	 */
	private boolean validateFields()
	{
		
		// Date managing
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
		try
		{
			this.begining = dateFormat.parse(fromTF.getText());
			this.end = dateFormat.parse(toTF.getText());
		}
		catch(Exception e)
		{
			View.displayMessage("Invalid fields : date availability", "Error", JOptionPane.ERROR_MESSAGE, null);
			return false;
		}
				
		// address
		try
		{
			new Integer(addressTF.getText());
		}
		catch(Exception e)
		{
			View.displayMessage("Invalid fields : address. Must be an integer", "Error", JOptionPane.ERROR_MESSAGE, null);
			return false;
		}
		
		// duration
		try
		{
			new Integer(durationTF.getText());
		}
		catch(Exception e)
		{
			View.displayMessage("Invalid duration : Must be an integer (seconds)", "Error", JOptionPane.ERROR_MESSAGE, null);
			return false;
		}
		
		// Check available time
		if(new Long(durationTF.getText()) > availableTime)
		{
			View.displayMessage("You don't have enough waiting time to do this delivery.","Error", JOptionPane.ERROR_MESSAGE, null);
			return false;
		}
		return true;
	}
	
	//Accessors
	public void setPickedPointId(int id) {
		this.pickedPointId=id; 
		addressTF.setText(""+pickedPointId);
	}
	
	public void setIndex(int i) {this.index=i;}
	public void setAvailableTime(long availableTime2) {	this.availableTime = availableTime2;}
	public void setTourId(int tourId) {	this.tourId = tourId;}
}
