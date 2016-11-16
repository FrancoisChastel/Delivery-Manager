package view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;


import controller.Controller;

public class View implements Observer {	
	
	public static enum Page {Setting, Principale};
	private HashMap<Page,JFrame> pages;
	private Controller controller;
	
	/**
	 * Normal Contructor of View class. It instanciate all Frame pages and put it in its list of page.
	 * @param controller
	 */
	public View(Controller controller) {
		this.controller = controller;
		pages = new HashMap<Page,JFrame>();
		pages.put(Page.Setting, new SettingFrame(this));
		
		displayFrame(Page.Setting, false);
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
	 * Method of Observer/Observable pattern
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		
	}
}
