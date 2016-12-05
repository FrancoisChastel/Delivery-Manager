package controller.commands.undocommands;

import java.util.HashMap;

import controller.commands.IUndoableCommand;
import model.Model;
import model.deliverymanager.DeliveryManager;

public class ResetDeliveriesCommand implements IUndoableCommand {

	private Model model;

	public ResetDeliveriesCommand(Model model)
	{
		this.model=model;
	}
	
	@Override
	public void execute() throws Throwable {
		model.resetDeliveries();
	}

	@Override
	public void undo() throws Throwable {
		// TODO Auto-generated method stub
		
	}

}
