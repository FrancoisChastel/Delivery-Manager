/*************************************************************************
                        IComamnd 
 -------------------
 Begin                : 03/12/2016
 Copyright            : (C) 2016 by FACANT
 *************************************************************************/
package controller.commands;
/**
 * Command interface that is undoable 
 * @author FACANT
 *
 */
public interface ICommand {

	/**
	 * Execute the command
	 * @throws Throwable
	 */
	public void execute() throws Throwable;
}
