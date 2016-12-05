package view.jtree;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import view.jtree.ITreeItem.Menu;

/**
 * 
 * @author antoine
 */
public class TreeWaitingTime extends TreeDefaultIconNode implements ITreeItem{

	private int idPreviousPoint;
	private int positionInTour;
	private long availableTime;
	
	/**
	 * Normal Constructor
	 * @param o
	 * @param icon
	 * @param idPreviousPoint
	 */
	public TreeWaitingTime(Object o, ImageIcon icon, int idPreviousPoint, int positionInTour, long availableTime) {
		super(o, icon);
		this.positionInTour  = positionInTour;
		this.availableTime   = availableTime;
		this.idPreviousPoint = idPreviousPoint;
	}
	
	@Override
	public int getId() { return this.idPreviousPoint; }
	
	@Override
	public Menu getMenuType() {
		// TODO Auto-generated method stub
		return Menu.WaitingTimeMenu;
	}
	
	public int getPositionInTour() {return this.positionInTour; }
	public long getAvailableTime() { return this.availableTime; }
}
