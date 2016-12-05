package view;

import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeTour extends DefaultMutableTreeNode implements ITreeItem{

	private int id;
	
	public TreeTour(Object child, int id)
	{
		super(child);
		this.id=id;
	}

	@Override
	public int getId() {return id;}

	@Override
	public Menu getMenuType() {	return Menu.TourMenu;}

}
