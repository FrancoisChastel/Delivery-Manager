package view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.Controller;

public class View implements Observer, IView {	
	
	public static enum Page {Setting, Main};
	private HashMap<Page,JFrame> pages;
	private Controller controller;
	
	
	/**
	 * This methode display an alert using JOptionPane
	 * @param message
	 * @param typeIcone use JOptionPane Options. Example : JOptionPane.ERROR_MESSAGE
	 */
	public static void displayMessage(String message, String title, int typeIcone, JFrame f)
	{
			
		JOptionPane.showMessageDialog(f,
				message,
				title,
				typeIcone);
	}
	
	public static void displayMessage(String message, String title, JFrame f)
	{
			
		JOptionPane.showMessageDialog(f,
				message,
				title,
				JOptionPane.INFORMATION_MESSAGE);
	} 
	
	public static String  getDate(Date d)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		return formatter.format(d);
	}
	
	/**
	 * Normal Contructor of View class. It instanciate all Frame pages and put it in its list of pages.
	 * @param controller
	 */
	public View(Controller controller) {
			this.controller = controller;
			pages = new HashMap<Page,JFrame>();
			pages.put(Page.Setting, new SettingFrame(this));
			pages.put(Page.Main, new MainFrame(this));	
		}
	
	/**
	 * Display the frame passed in parameter (setVisible). If the boolean only is true, all other pages will be set unvisible)
	 * @param p
	 * @param only
	 */	
	public void displayFrame(Page p, boolean only)
	{		
		if(only)
		{
		// Hiding all pages
			Iterator<JFrame> i = pages.values().iterator();
			while(i.hasNext())
			{
				i.next().setVisible(false);
			}
		}
		
		// Display the page
		pages.get(p).setVisible(true);
	}

	public Controller getController() {
		return controller;
	}

	/**
	 * This method format a string "mm min, ss sec" from a time in seconds
	 * @param totalSeconds
	 * @return
	 */
	public static String formatDateFromSecond(long totalSeconds)
	{
		// Formating
		int seconds 	 = (int) (totalSeconds % 60);
	    int totalMinutes = (int) (totalSeconds / 60);
	    
	    
		String formatedWaitingTime = String.format("%02d min, %02d sec", 
				totalMinutes,
				seconds
			);
		return formatedWaitingTime;
	}
	/** 
	 * Method of Observer/Observable pattern
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		HashMap<String,Object> params = (HashMap<String,Object>) arg1;
		
		String type = (String) params.get("type");
		
		if(type == "UPDATE_MAP")
		{
			((MainFrame) pages.get(Page.Main)).adapte(controller.getModel().getMapNodes(),controller.getModel().getSections());
			displayFrame(Page.Main,true);
		}
		else if(type == "UPDATE_DELIVERY")
		{
			Integer idTour = (Integer) params.get("tour");
			((MainFrame) pages.get(Page.Main)).displayTour(controller.getModel().getTourById(idTour));
		}
	}
	
	public JFrame getPage(Page p) { return pages.get(p); }
}
