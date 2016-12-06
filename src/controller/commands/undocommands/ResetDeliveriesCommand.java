package controller.commands.undocommands;

import java.util.HashMap;

import controller.commands.IUndoableCommand;
import model.Model;
import model.Tour;
import model.deliverymanager.DeliveryManager;
import model.deliverymanager.DeliveryOrder;
import java.util.Observable;

public class ResetDeliveriesCommand extends Observable implements IUndoableCommand {

	private Model model;
	private HashMap<Integer,Integer> indexDelOrdersTours;
	private HashMap<Integer,Tour> tours;
	private DeliveryOrder selected;
	private DeliveryManager deliveryManager;

	public ResetDeliveriesCommand(Model model)
	{
		this.model=model;
		indexDelOrdersTours = model.getIndexDelOrdersTours();
		tours = model.getTours();
		selected = model.getSelected();
		deliveryManager = model.getDeliveryManager();
	}
	
	@Override
	public void execute() throws Throwable {
		model.resetDeliveries();
		model.updateResetDeliveries();
	}

	@Override
	public void undo() throws Throwable {
		// TODO Auto-generated method stub
		model.setDeliveryManager(deliveryManager);
		model.setIndexDelOrdersTours(indexDelOrdersTours);
		model.setTours(tours);
		model.setSelected(selected);
		model.updateMap();
		model.updateDeliveries();
	}

}
