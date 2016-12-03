package controller.commands;

import java.util.List;

/**
 * 
 * @author FACANT
 *
 */
public interface ICommander {

	/**
	 * obtain a list of the last top command of the context
	 * @param context of execution that need to be returned
	 * @param number of commands that will be returned
	 * @return last commands
	 */
	public ICommand[] historic(CommandContext context, int size);
	
	/**
	 * obtain a list of the last undo top command of the context
	 * @param context of execution that need to be returned
	 * @param number of commands that will be returned
	 * @return last commands
	 */
	public ICommand[] undoHistoric(CommandContext context, int size);
	
	/**
	 * execute the command
	 * @param context of execution of the command
	 * @param command that will be executed
	 * @throws Throwable 
	 */
	public void execute(CommandContext context, ICommand command) throws Throwable;
	
	/**
	 * execute the command
	 * @param context of execution of the command
	 * @param undoable command that will be executed
	 */
	public void execute(CommandContext context, IUndoableCommand command) throws Throwable;

	/**
	 * undo the last command of the context
	 * @param context targeted by the undo
	 * @throws Throwable
	 */
	public void undo(CommandContext context) throws Throwable;
	
	/**
	 * redo the last command of the context
	 * @param context targeted by the redo
	 * @throws Throwable
	 */
	public void redo(CommandContext context) throws Throwable;
	
}
