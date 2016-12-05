package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import controller.commands.CommandContext;
import model.Tour;
import model.deliverymanager.DeliveryPoint;
import model.graph.MapNode;
import model.graph.Section;
import view.jtree.JTreeRenderer;
import view.jtree.TreeDefaultIconNode;
import view.jtree.TreeListener;
import view.jtree.TreeTour;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;

import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.BoxLayout;
import javax.swing.JTree;

import java.awt.GridLayout;

/**
 * 
 * @author antoine
 *
 */
public class MainFrame extends JFrame implements ActionListener {

	private View hamecon;
	private JPanel contentPane;
	private Map map;
	private Adapter adapter;
	private JMenuItem mntmLoadDeliveryfile;
	private JMenuItem mntmNewMap;
	private JMenuItem mntmUndo;
	private JMenuItem mntmRedo;
	private JMenuItem mntmReset;
	private JTree tourTree;
	private TreeDefaultIconNode root;
	private JPanel rightSidePanel;
	
	/**
	 * Normal Construcor
	 * @param view
	 */
	public MainFrame(View view) {
		setTitle("Delivery Manager");
		this.hamecon = view;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmNewMap = new JMenuItem("Load New Map");
		mnFile.add(mntmNewMap);
		mntmNewMap.addActionListener(this);
		
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mntmUndo = new JMenuItem("Undo");
		mnEdit.add(mntmUndo);
		mntmUndo.addActionListener(this);
		
		mntmRedo = new JMenuItem("Redo");
		mnEdit.add(mntmRedo);
		mntmRedo.setEnabled(false);
		mntmRedo.addActionListener(this);
		
		
		JMenu mnDelivery = new JMenu("Delivery");
		menuBar.add(mnDelivery);
		
		mntmLoadDeliveryfile = new JMenuItem("Load delivery file");
		mnDelivery.add(mntmLoadDeliveryfile);
		mntmLoadDeliveryfile.addActionListener(this);
		
		mntmReset = new JMenuItem("Reset");
		mnDelivery.add(mntmReset);
		mntmReset.addActionListener(this);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		// Instanciate Map
		map = new Map(this);
		contentPane.add(map, BorderLayout.CENTER);
		
		adapter = new Adapter(map);
		
		// RightSizePanel
		JLabel lblNewLabel = new JLabel("Calculed Tour");
		rightSidePanel = new JPanel();		
		rightSidePanel.add(lblNewLabel);
		rightSidePanel.setLayout(new BoxLayout(rightSidePanel, BoxLayout.PAGE_AXIS));
		contentPane.add(rightSidePanel, BorderLayout.EAST);
		
		// Initialization of the JTree -----------
		//create the root node
        root = new TreeDefaultIconNode("Deliveries",null);        
		tourTree = new JTree(root);	
		tourTree.setCellRenderer(new JTreeRenderer());

		// Manage listener
		TreeListener treeListener = new TreeListener(this);
		tourTree.addTreeSelectionListener(treeListener);
		tourTree.addMouseListener(treeListener);
		
		// Add to right pane
        JScrollPane treeView = new JScrollPane(tourTree);     
		rightSidePanel.add(treeView);
		
		repaint();
	}
	
	/**
	 * Draw the frame with adapted from model objects. Called by update
	 * @param nodes
	 * @param troncons
	 */
	public void adapte(List<MapNode> nodes, List<Section> troncons)
	{
		adapter.drawModel(nodes,troncons);		
	}
	
	/** 
	 * This method display a tour on the main frame.
	 */
	public void displayTour(Tour tour)
	{
		map.displayTour(tour);
		addTourTree(tour);		
	}
	
	/**
	 * This method add a tour into the Tree.
	 * @param tour
	 */	
	public void addTourTree(Tour tour)
	{		

		TreeTour tourInTree = adapter.getTreeTour(tour);
		System.out.println("Displaying t"+tourInTree.getId());
		
		for(int i =0; i<tour.getDeliveryPoints().size()-1;i++)
		{
			// Add the DeliveryPointNode
			tourInTree.add(adapter.getTreeNode(tour.getDeliveryPoints().get(i)));
			// Add the waiting Time node
			tourInTree.add(adapter.getTreeWaitingTime(tour.getDeliveryPoints().get(i), tour.getDeliveryPoints().get(i+1), tour));
		}
		
		// Add the last deliveryPoint
		tourInTree.add(adapter.getTreeNode(tour.getDeliveryPoints().get(tour.getDeliveryPoints().size()-1)));
		
		root.add(tourInTree);	
		this.tourTree.setSelectionRow(root.getChildCount()-1);
		DefaultTreeModel model = (DefaultTreeModel) tourTree.getModel();
		model.reload();
	}

	/**
	 * Action listener of the frame
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		// Load Livraison
		if(arg0.getSource()==mntmNewMap)
		{
			JFileChooser fc = new JFileChooser();	
            int returnVal = fc.showOpenDialog(MainFrame.this);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	File currentFile = fc.getSelectedFile();
            	hamecon.getController().reset();
            	hamecon.getController().parseMapFile(currentFile);
            	root.removeAllChildren();
            }
            mntmRedo.setEnabled(false);
		}
		else if(arg0.getSource()==mntmReset)
		{

			root.removeAllChildren();
			
			// Initialization of the JTree -----------
			//create the root node
	        root = new TreeDefaultIconNode("Deliveries",null);        
			tourTree = new JTree(root);	
			

			// Manage listener
			TreeListener treeListener = new TreeListener(this);
			tourTree.addTreeSelectionListener(treeListener);
			tourTree.addMouseListener(treeListener);

			try {
				hamecon.getController().resetDeliveries();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//refresh tree (empty)
			//repaint empty map
			mntmRedo.setEnabled(false);

		}
		else if(arg0.getSource()==mntmLoadDeliveryfile)
		{
			JFileChooser fc = new JFileChooser();	
            int returnVal = fc.showOpenDialog(MainFrame.this);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	File currentFile = fc.getSelectedFile();            	
            	hamecon.getController().loadDeliveryFile(currentFile);
            }
            mntmRedo.setEnabled(false);
		}
		else if(arg0.getSource()==mntmUndo)
		{
			hamecon.getController().undoCommand(CommandContext.MAIN, 1);
			mntmRedo.setEnabled(true);
		}
		else if(arg0.getSource()==mntmRedo)
		{
			hamecon.getController().redoCommand(CommandContext.MAIN, 1);
		}
		
	}

	public JTree getJtree() { return tourTree; }
	
	/**
	 * 
	 * @return
	 */
	public View getView() { return hamecon; }

	public Map getMap() {	return map;	}

}
