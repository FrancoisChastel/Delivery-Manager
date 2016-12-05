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
	 * @return
	 */
	public static Commander getInstance() {
		if (null == instance) { 
			instance = new Commander();
		}
		return instance;	
	}
	
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
	public void execute(CommandContext context, IUndoableCommand command) throws Throwable {
		command.execute();

		historics.get(context).add(command);
	}

	@Override
	public void undo(CommandContext context, int numberOfUndo) throws Throwable {
		for (int counter=0; counter<numberOfUndo; counter++)
		{
			this.historics.get(context).get(0).undo();
			this.undoHistorics.get(context).add(historics.get(context).get(0));
			this.historics.get(context).remove(0);
		}
	}
	
	@Override
	public void redo(CommandContext context,	int numberOfUndo) throws Throwable {
		for (int counter=0; counter<numberOfUndo; counter++)
		{
			this.undoHistorics.get(context).get(0).execute();
			this.historics.get(context).add(undoHistorics.get(context).get(0));
			this.undoHistorics.get(context).remove(0);
		}
	}


	@Override
	public ICommand[] historic(CommandContext context, int size) {
		ICommand[] historic = new ICommand[size];
		
		for (int counter=0; counter>size; counter++)
		{
			historic[counter] = this.historics.get(context).get(counter);
		}
		return historic;
	}
	
	@Override
	public ICommand[] undoHistoric(CommandContext context, int size) {
		ICommand[] undoHistoric = new ICommand[size];
		
		for (int counter=0; counter>size; counter++)
		{
			undoHistoric[counter] = this.undoHistorics.get(context).get(counter);
		}
		return undoHistoric;
	}
	
    private static Commander instance;
}
