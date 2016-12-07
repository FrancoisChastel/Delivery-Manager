package controller.commands.undocommands;

import java.io.File;
import java.util.HashMap;

import controller.commands.IUndoableCommand;
import model.Model;
import model.Tour;
import model.deliverymanager.DeliveryManager;
import model.deliverymanager.DeliveryOrder;
import model.graph.GraphDeliveryManager;

public class LoadDeliveryCommand implements IUndoableCommand {

	private Model model;
	private HashMap<Integer,Integer> indexDelOrdersTours;
	private HashMap<Integer,Tour> tours;
	private DeliveryOrder selected;
	private DeliveryManager deliveryManager;
	private File currentFile;
	
	public LoadDeliveryCommand(Model model, File currentFile)
	{
		this.model=model;
		indexDelOrdersTours = model.getIndexDelOrdersTours();
		tours = model.getTours();
		selected = model.getSelected();
		deliveryManager = model.getDeliveryManager();
		this.currentFile=currentFile;
		//get des attributs
	}
	
	@Override
	public void execute() throws Throwable {
		// TODO Auto-generated method stub
		model.loadDeliveriesFile(currentFile);
	}

	@Override
	public void undo() throws Throwable {
		// TODO Auto-generated method stub
		model.unloadDeliveriesFile();
		model.updateMap();
		model.updateResetDeliveries();
	}

}
