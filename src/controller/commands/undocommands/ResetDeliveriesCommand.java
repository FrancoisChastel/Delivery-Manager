package controller.commands.undocommands;

import controller.commands.IUndoableCommand;
import model.Model;

public class ResetDeliveriesCommand implements IUndoableCommand {

	private Model model;
	
	public ResetDeliveriesCommand(Model model)
	{
		this.model=model;
	}
	
	@Override
	public void execute() throws Throwable {
		// TODO Auto-generated method stub
		model.resetDeliveries();
	}

	@Override
	public void undo() throws Throwable {
		// TODO Auto-generated method stub
		
	}

}
