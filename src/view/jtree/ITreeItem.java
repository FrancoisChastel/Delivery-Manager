package view.jtree;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * 
 * @author antoine
 *
 */
public interface ITreeItem {
	/**
	 * This enum is used to manage RightClic PopupMenu. 
	 */
	public static enum Menu {TourMenu, NodeMenu, WaitingTimeMenu};
	
	/**
	 * Items should return their id;
	 * @return
	 */
	public int getId();
	
	/**
	 * Every ItemTree should return which kind of menu it displays on right clic.
	 * @return
	 */
	public Menu getMenuType();
	
	/**
	 * This method return an ImageIcone for display in the Tree. It could Be null.
	 * @return
	 */
	public ImageIcon getIcone();
}
