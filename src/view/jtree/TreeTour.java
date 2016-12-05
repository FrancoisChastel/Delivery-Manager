package view.jtree;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeTour extends DefaultMutableTreeNode implements ITreeItem{

	private int id;
	private ImageIcon icon;
	
	public TreeTour(Object child, int id, ImageIcon icon)
	{
		super(child);
		this.id=id;
		this.icon = icon;
	}

	@Override
	public int getId() {return id;}

	@Override
	public Menu getMenuType() {	return Menu.TourMenu;}

	@Override
	public ImageIcon getIcone() {
		// TODO Auto-generated method stub
		return icon;
	}

}
