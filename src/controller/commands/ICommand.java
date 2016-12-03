/*************************************************************************
                        IComamnd 
 -------------------
 Begin                : 03/12/2016
 Copyright            : (C) 2016 by FACANT
 *************************************************************************/
package controller.commands;

/**
<<<<<<< 29fd2f6a18b2017c769c4def39a77583c7f1ced5
 * Command interface that is undoable 
=======
 * Command interface that is not undoable 
>>>>>>> Modify - Refactor name of package
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
