package view.jtree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.omg.PortableServer.POA;

import view.MainFrame;
import view.jtree.ITreeItem.Menu;

public class TreeListener implements TreeSelectionListener, ActionListener, MouseListener {

	// Attributes
	private MainFrame mainFrame;
	private HashMap<Menu,JPopupMenu> mapPopupMenu;
	
	/**
	 * Normal Constructor
	 * @param mainFrame
	 */
	public TreeListener(MainFrame mainFrame)
	{
		this.mainFrame = mainFrame;
		initialisedMenus();
	}
	
	/**
	 * Creates the popup menus
	 */
	private void initialisedMenus()
	{
		mapPopupMenu = new HashMap<>();
		
		// Tour menu --------------------------------------------
		JPopupMenu tourPopupMenu = new JPopupMenu();
		
		// Trace route
		JMenuItem generateTourItem = new JMenuItem("Generate trace route...");
		generateTourItem.addActionListener(this);
		tourPopupMenu.add(generateTourItem);
		
		mapPopupMenu.put(Menu.TourMenu, tourPopupMenu);
		
		// MapNode menu -----------------------------------------
		JPopupMenu nodeMenu = new JPopupMenu();
		// TDelete point
		JMenuItem deletePoint = new JMenuItem("Delete this point");
		deletePoint.addActionListener(this);
		tourPopupMenu.add(deletePoint);
		nodeMenu.add(deletePoint);
		
		mapPopupMenu.put(Menu.NodeMenu, nodeMenu);
	}
	
	/**
	 * Node Selected CallBack.
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {

		JTree curr = (JTree) e.getSource();
		if(curr == null)
			return;
		
		mainFrame.getMap().setAllPointHoved(false);
		
		// Selected Tree Node
		if (curr.getLastSelectedPathComponent() != null && curr.getLastSelectedPathComponent().getClass().getName() == "view.TreeMapNode")
		{
			int selected = ((TreeMapNode) curr.getLastSelectedPathComponent()).getId();
			mainFrame.getMap().getPoint(selected).setHoved(true);
			mainFrame.getMap().repaint();
		}
		// Selected TourNode
		else if (curr.getLastSelectedPathComponent() != null && curr.getLastSelectedPathComponent().getClass().getName() == "view.TreeTour")
		{
			
			int selectedTour = ((TreeTour) curr.getLastSelectedPathComponent()).getId();
			mainFrame.getMap().setTourSelected(selectedTour);
			mainFrame.getMap().repaint();
		}
	}

	/**
	 * Clic on menu item listener.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JMenuItem item = (JMenuItem) arg0.getSource();
		
		// Get back the JTree
		JTree tree = mainFrame.getJtree();
		
		if(tree == null || tree.getLastSelectedPathComponent() == null )
			return;
		
		// Get back the selected item
		ITreeItem selected = ((ITreeItem) tree.getLastSelectedPathComponent());

		// Treat each menu item -----------------------------------
		if(item.getActionCommand() == "Generate trace route...") 
		{
			mainFrame.getView().getController().generateTraceRoute(selected.getId());
		}		
		else if(item.getActionCommand() == "Delete this point")
		{
			int pointId = selected.getId();
			// Get the tour ID
			int tourId  = ( (TreeTour) ((DefaultMutableTreeNode) selected).getParent()).getId();
			mainFrame.getView().getController().deletePoint(tourId, pointId);
		}
			
	}

	/**
	 * Right clic on menu Callback. On a right click, it displays the appropriated menu. Either MapNode menu or TourMenu.
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (SwingUtilities.isRightMouseButton(arg0)) {

	        int row = mainFrame.getJtree().getClosestRowForLocation(arg0.getX(), arg0.getY());
	        mainFrame.getJtree().setSelectionRow(row);
	        
	        // Try to get the selected item if exists
	        ITreeItem selected =null;
	        
	        try{
	        	selected = ((ITreeItem) mainFrame.getJtree().getLastSelectedPathComponent());
	        }
	        catch(Exception e) { return;}
	        
	        // Get the appropriated menu
	        JPopupMenu menu = mapPopupMenu.get(selected.getMenuType());
	        
	        // If the menu exists then we display it
	        if(menu != null)
	        	menu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
	    }
	}

	@Override
	public void mouseEntered(MouseEvent arg0)  {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0)   {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0)  {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
