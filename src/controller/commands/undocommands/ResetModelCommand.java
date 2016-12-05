package controller.commands.undocommands;

import controller.commands.IUndoableCommand;
import model.Model;

public class ResetModelCommand implements IUndoableCommand {

	private Model model;
	
	public ResetModelCommand(Model model)
	{
		this.model=model;
		//get des attributs
	}
	
	@Override
	public void execute() throws Throwable {
		// TODO Auto-generated method stub
		model.resetModel();
	}

	@Override
	public void undo() throws Throwable {
		// TODO Auto-generated method stub
		//remise des attributs
	}

}
