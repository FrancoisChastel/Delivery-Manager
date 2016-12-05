package view.jtree;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 
 * @author antoine
 */
public class TreeWaitingTime extends TreeDefaultIconNode{

	private int idPreviousPoint;

	/**
	 * Normal Constructor
	 * @param o
	 * @param icon
	 * @param idPreviousPoint
	 */
	public TreeWaitingTime(Object o, ImageIcon icon, int idPreviousPoint) {
		super(o, icon);
		
		this.idPreviousPoint = idPreviousPoint;
	}
}
