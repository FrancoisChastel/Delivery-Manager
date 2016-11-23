package controller;

import java.io.File;

import model.Model;
import view.View;
import view.View.Page;

public class Controller implements IController{

	private Model model;
	private View view;
	
	/**
	 * Default constructor of the controller. It instanciate model and view, and set up observer/observable pattern
	 */
	public Controller()
	{
		model = new Model(this);
		view  = new View(this);
		model.addObserver(view);
		
		view.displayFrame(Page.Setting, false);

	}

	/**
	 * This method just call the parseMapFile method of the model. It called by the view when a click on 
	 * Validate(SettingFrame) is caught.
	 * @param currentFile
	 */
	public void parseMapFile(File currentFile) {
		model.parseMapFile(currentFile);
	}
	
	/**
	 * This method just call the parseDeliveriesFile method of the model. It called by the view when a click on 
	 * Validate(SettingFrame) is caught.
	 * @param currentFile
	 */
	public void parseDeliveriesFile(File currentFile)
	{
		model.parseDeliveriesFile(currentFile);
	}
	
	public Model getModel() { return model; }

	/**
	 * Load a delivery file XML. It can be called by the View.Mainframe
	 * @param currentFile
	 */
	public void loadDeliveryFile(File currentFile) {
		
		model.loadDeliveryFile(currentFile);
	}
}
