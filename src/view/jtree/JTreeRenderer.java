package view.jtree;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;

/**
 * 
 * @author antoine
 *
 */
public class JTreeRenderer extends DefaultTreeCellRenderer   {
		
	/**
	 * This method renderer the nodes in the tree. Icons are managed in this.
	 * @param tree
	 * @param value
	 * @param selected
	 * @param expanded
	 * @param leaf
	 * @param row
	 * @param hasFocus
	 * @return
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value,selected, expanded, leaf, row, hasFocus);
		ITreeItem node = (ITreeItem) value;
		
		// Set the icone of the leaf
		if(node.getIcone() != null)
			setIcon(node.getIcone());
		return this;
	}
}
