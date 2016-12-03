package controller.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 
 * @author FACANT
 *
 */
public class Commander implements ICommander{
	private Map<CommandContext,List<IUndoableCommand>> historics;
	private Map<CommandContext,List<IUndoableCommand>> undoHistorics;

	/**
	 * 
	 */
	public Commander() {
		super();
		this.historics = new HashMap<CommandContext, List<IUndoableCommand>>();
		this.undoHistorics = new HashMap<CommandContext, List<IUndoableCommand>>();

		for (CommandContext context : CommandContext.values())
		{
			historics.put(context, new Stack<IUndoableCommand>());
			undoHistorics.put(context, new Stack<IUndoableCommand>());
		}
	}

	
	@Override
	public void execute(CommandContext context, ICommand command) throws Throwable {
		command.execute();
	}

	@Override
	public void execute(CommandContext context, IUndoableCommand command) {
		
		
	}

	@Override
	public void undo(CommandContext context) throws Throwable {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void redo(CommandContext context) throws Throwable {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<ICommand> historic(CommandContext context, int size) {
		
	}
	
}
