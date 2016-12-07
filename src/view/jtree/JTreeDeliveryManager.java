package view.jtree;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import model.Tour;
import view.Adapter;
import view.MainFrame;

/**
 * 
 * @author antoine
 *
 */
public class JTreeDeliveryManager extends JTree {
	
	private MainFrame mainFrame;
	private TreeDefaultIconNode root;
	
	/**
	 * Normal Constructor
	 */
	public JTreeDeliveryManager(TreeDefaultIconNode root, MainFrame mainFrame)
	{
		super(root);
		this.mainFrame = mainFrame;
		this.root = root;
		setCellRenderer(new JTreeRenderer());

		// Manage listener
		TreeListener treeListener = new TreeListener(mainFrame);
		addTreeSelectionListener(treeListener);
		addMouseListener(treeListener);
		JTree myTree = this;
		this.addTreeExpansionListener(new TreeExpansionListener() {
			
			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				// TODO Auto-generated method stub
				myTree.invalidate();
				myTree.validate();
				mainFrame.resize(mainFrame.getWidth(), mainFrame.getHeight()+1);
				mainFrame.repaint();
			}
			
			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				// TODO Auto-generated method stub
				myTree.invalidate();
				myTree.validate();
				mainFrame.resize(mainFrame.getWidth(), mainFrame.getHeight()+1);
				mainFrame.repaint();
			}
		});
	}
	
	/**
	 * This method add a tour into the Tree.
	 * @param tour
	 */	
	public void displayTourInTree(Tour tour)
	{		
		Adapter adapter = mainFrame.getAdapter();		
		TreeTour tourInTree = adapter.getTreeTour(tour);
		
		for(int i =0; i<tour.getDeliveryPoints().size()-1;i++)
		{
			// Add the DeliveryPointNode
			tourInTree.add(adapter.getTreeNode(tour.getDeliveryPoints().get(i)));
			// Add the waiting Time node
			tourInTree.add(adapter.getTreeWaitingTime(tour.getDeliveryPoints().get(i), tour.getDeliveryPoints().get(i+1), tour,i));
		}
		
		// Add the last deliveryPoint
		tourInTree.add(adapter.getTreeNode(tour.getDeliveryPoints().get(tour.getDeliveryPoints().size()-1)));
				
		//Add the warehouse
		tourInTree.add(adapter.getWarehouseView(tour.getEntrepotId(), tour.getBackToWareHouseDate()) );
		
		// If the tour already exists we add it here
		TreeTour existingTreeTour = getTourIfAlreadyExists(tour.getId());
		
		if(existingTreeTour !=null) // if there is an existing tree tour we modify it
		{
			System.out.println("Tour exists in tree");
			// get back the index of existing tour
			Integer indexOfExisting = existingTreeTour.getParent().getIndex(existingTreeTour);
			root.remove(indexOfExisting);
			root.insert(tourInTree, indexOfExisting);
		}
		else // If there is no existing treetour, we add it
		{		
			root.add(tourInTree);	
			setSelectionRow(root.getChildCount()-1);
		}
		refreshTree();
	}
	
	/**
	 * This method return a TreeTour from an id if already exists in tree. Else it returns null
	 * @return
	 */
	private TreeTour getTourIfAlreadyExists(int id)
	{
		Enumeration<DefaultMutableTreeNode> children = root.children();
			
		while (children.hasMoreElements())
		{
			TreeTour curTour = (TreeTour) children.nextElement();
			// If we find the corresponding id, we return it
			if(curTour.getId()==id)
				return curTour;
		}
		return null;		
	}

	/**
	 * This method refresh the tree display with the good content.
	 */
	public void refreshTree()
	{
		DefaultTreeModel model = (DefaultTreeModel) getModel();
		model.reload();
	}
	
	/** 
	 * This method clears the tree.
	 */
	public void resetDeliveriesOnTree() {
		// TODO Auto-generated method stub
		root.removeAllChildren();
		
		//refresh tree (empty)
		refreshTree();
	}
}
