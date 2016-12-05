package view.jtree;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class is the View in the JTree of a MapNode.
 * @author antoine
 */
public class TreeMapNode extends DefaultMutableTreeNode implements ITreeItem {
	private int id;
	private ImageIcon icon;
	/**
	 * Normal Constructor.
	 * @param id
	 */
	public TreeMapNode(Object child, int id, ImageIcon icon)
	{
		super(child);
		this.id=id;
		this.icon = icon;
	}

	public int getId() { return id; }
	
	@Override
	public Menu getMenuType() {	return Menu.NodeMenu;}

	@Override
	public ImageIcon getIcone() {
		// TODO Auto-generated method stub
		return icon;
	}
}
