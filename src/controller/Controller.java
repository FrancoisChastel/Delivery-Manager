package controller;

import model.Model;
import view.View;

public class Controller implements IController{

	private Model model;
	private View view;
	
	/**
	 * Default constructor of the controller.
	 */
	public Controller()
	{
		model = new Model(this);
		view  = new View(this);
	}
}
