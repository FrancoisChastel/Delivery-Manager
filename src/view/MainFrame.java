package view;

import java.awt.BorderLayout;
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
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import model.MapNode;
import model.Section;
import model.Tour;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
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
		map = new Map();
		contentPane.add(map);
		
		adapter = new Adapter(map);
		
		JPanel rightSidePanel = new JPanel();
		contentPane.add(rightSidePanel, BorderLayout.EAST);
		rightSidePanel.setLayout(new BoxLayout(rightSidePanel, BoxLayout.PAGE_AXIS));
		
		
		JLabel lblNewLabel = new JLabel("Calculed Tour");
		rightSidePanel.add(lblNewLabel);
		
		// Initialization of the JTree -----------
		//create the root node
        root = new DefaultMutableTreeNode("Root");
        
		tourTree = new JTree(root);
		rightSidePanel.add(tourTree);
		       
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
	public void displayTour()
	{
		//TEST DEBUGING
	//	Tour tour = null;

	//	map.displayTour(tour);
				
	}
	/**
	 * This method add a tour into the Tree.
	 * @param tour
	 */	
/*	public void addTourTree(Tour tour)
	{		
		root.add(new DefaultMutableTreeNode("Tournée 1"));
		// Adding the first Level of JTree		
	}
*/
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

}
