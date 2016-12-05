package view;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class is the View in the JTree of a MapNode.
 * @author antoine
 */
public class TreeMapNode extends DefaultMutableTreeNode implements ITreeItem {
	private int id;
	
	/**
	 * Normal Constructor.
	 * @param id
	 */
	public TreeMapNode(Object child, int id)
	{
		super(child);
		this.id=id;
	}

	public int getId() { return id; }
	
	@Override
	public Menu getMenuType() {	return Menu.NodeMenu;}
}
