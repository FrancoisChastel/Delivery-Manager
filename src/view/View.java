package view;

import java.util.HashMap;
import javax.swing.JFrame;
import controller.Controller;

public class View {	
	
	public static enum Pages {Accueil, Principale};
	private HashMap<Pages,JFrame> pages;
	private Controller controller;
	
	public View(Controller controller) {
		// TODO Auto-generated constructor stub
		this.controller = controller;
	}
}
