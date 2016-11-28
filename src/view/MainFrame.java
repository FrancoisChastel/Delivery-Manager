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

import model.Tour;
import model.graph.MapNode;
import model.graph.Section;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JList;
import javax.swing.BoxLayout;
import javax.swing.JTree;

public class MainFrame extends JFrame implements ActionListener {

	private View hamecon;
	private JPanel contentPane;
	private Map map;
	private Adapter adapter;
	private JMenuItem mntmLoadDeliveryfile;
	private JTree tourTree;
	private DefaultMutableTreeNode root;
	private JPanel rightSidePanel;
	
	/**
	 * Normal Construcor
	 * @param view
	 */
	public MainFrame(View view) {
		this.hamecon = view;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnDelivery = new JMenu("Delivery");
		menuBar.add(mnDelivery);
		
		mntmLoadDeliveryfile = new JMenuItem("Load delivery file");
		mnDelivery.add(mntmLoadDeliveryfile);
		mntmLoadDeliveryfile.addActionListener(this);
		
		JMenuItem mntmNewDelivery = new JMenuItem("New delivery");
		mnDelivery.add(mntmNewDelivery);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Instanciate Map
		map = new Map(this);
		contentPane.add(map);
		
		adapter = new Adapter(map);
		
		rightSidePanel = new JPanel();
		contentPane.add(rightSidePanel, BorderLayout.EAST);
		rightSidePanel.setLayout(new BoxLayout(rightSidePanel, BoxLayout.PAGE_AXIS));
		
		
		JLabel lblNewLabel = new JLabel("Calculed Tour");
		rightSidePanel.add(lblNewLabel);
		
		// Initialization of the JTree -----------
		//create the root node
        root = new DefaultMutableTreeNode("Deliveries");        
		tourTree = new JTree(root);		
		
		tourTree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				JTree curr = (JTree) e.getSource();
				map.setAllPointHoved(false);
				if (curr.getLastSelectedPathComponent().getClass().getName() == "view.TreeMapNode")
				{
					int selected = ((TreeMapNode) curr.getLastSelectedPathComponent()).getId();
					map.getPoint(selected).setHoved(true);
					map.repaint();
				}
			}
		});
        JScrollPane treeView = new JScrollPane(tourTree);     
		rightSidePanel.add(treeView);
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
		DefaultMutableTreeNode tourTree = new DefaultMutableTreeNode("Tour "+tour.getId());
		for(Integer dp : tour.getDeliveryPoints())
		{
			tourTree.add(adapter.getTreeNode(dp));
		}
		
		root.add(tourTree);
	}

	/**
	 * Action listener of the frame
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		// Load Livraison
		if(arg0.getSource()==mntmLoadDeliveryfile)
		{
			JFileChooser fc = new JFileChooser();	
            int returnVal = fc.showOpenDialog(MainFrame.this);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	File currentFile = fc.getSelectedFile();            	
            	hamecon.getController().loadDeliveryFile(currentFile);
            }
		}			
	}
	
	public JTree getJtree() { return tourTree; }
	/**
	 * 
	 * @return
	 */
	public View getView() { return hamecon; }

	public void majPrefSize() {
		// TODO Auto-generated method stub
		
		rightSidePanel.setPreferredSize(new Dimension(this.getSize().height/4,rightSidePanel.getHeight()));
	}
}
