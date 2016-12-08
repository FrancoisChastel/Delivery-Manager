package controller.commands.undocommands;

import java.util.HashMap;

import controller.commands.IUndoableCommand;
import model.Model;
import model.Tour;
import model.deliverymanager.DeliveryManager;
import model.deliverymanager.DeliveryOrder;
import model.graph.GraphDeliveryManager;

public class ResetModelCommand implements IUndoableCommand {

	private Model model;
	private GraphDeliveryManager graphDelMan;
	private HashMap<Integer,Integer> indexDelOrdersTours;
	private HashMap<Integer,Tour> tours;
	private DeliveryOrder selected;
	private DeliveryManager deliveryManager;
	
	public ResetModelCommand(Model model)
	{
		this.model=model;
		graphDelMan = model.getGraphDeliveryManager();
		indexDelOrdersTours = model.getIndexDelOrdersTours();
		tours = model.getTours();
		selected = model.getSelected();
		deliveryManager = model.getDeliveryManager();
		//get des attributs
	}
	
	@Override
	public void execute() throws Throwable {
		// TODO Auto-generated method stub
		model.resetModel();
		model.resetDeliveries();
		model.updateMap();
		model.updateDeliveries();
		model.updateResetDeliveries();
	}

	@Override
	public void undo() throws Throwable {
		// TODO Auto-generated method stub
		model.setDeliveryManager(deliveryManager);
		model.setGraphDelMan(graphDelMan);
		model.setIndexDelOrdersTours(indexDelOrdersTours);
		model.setTours(tours);
		model.setSelected(selected);
		model.updateMap();
		model.updateDeliveries();
	}

}
