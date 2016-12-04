package view;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public interface ITreeItem {
	/**
	 * This enum is used to manage RightClic PopupMenu. 
	 */
	public static enum Menu {TourMenu, NodeMenu};
	
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
}
