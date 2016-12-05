package view.jtree;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeDefaultIconNode extends DefaultMutableTreeNode implements ITreeItem {

	private ImageIcon icon;
	
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Menu getMenuType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageIcon getIcone() {
		// TODO Auto-generated method stub
		return icon;
	}

	/**
	 * Normal Constructor
	 * @param o
	 * @param logo
	 */
	public TreeDefaultIconNode(Object o, ImageIcon icon)
	{
		super(o);
		this.icon = icon;
	}
}
