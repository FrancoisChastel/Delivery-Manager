package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.JPanel;

import model.Tour;
import model.deliverymanager.DeliveryPoint;
import model.graph.Section;

public class Map extends JPanel {

	private ArrayList<ViewPoint> points;
	// the key of this map is the ID of the section
	private HashMap<Integer,ViewEdge> edges;
	private ArrayList<ViewLabel> labels;
	private ArrayList<ViewEdge> alreadyPassedEdges;

	private HashMap<Integer,ViewTour> tours;
	private MainFrame mainFrame;
	
	/**
	 * Create the panel.
	 */
	public Map(MainFrame mainFrame) {
		this.mainFrame=mainFrame;
		points = new ArrayList<ViewPoint>();
		edges  = new HashMap<Integer,ViewEdge>();
		labels = new ArrayList<ViewLabel>();
		tours  = new HashMap<>();
		alreadyPassedEdges = new ArrayList<ViewEdge>();
		this.setBackground(Color.DARK_GRAY);
		// Action Listener
		MapMouseListener mouseListener = new MapMouseListener(this);
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);
	}
	
	/**
	 	 * Change weight of edges already passed
	 	 * @param tour
	 	 * @param targetPoint
	 	 */
	 	public void displayEdgesAlreadyPassed(Tour tour, int idPoint)
	 	{
	 		Iterator<Section> sectionIterator = tour.getSections().iterator();
	 		boolean foundedPoint = false;
	 		ViewEdge edge;
	 		while(sectionIterator.hasNext())
	 		{
	 			edge = edges.get(((Section)sectionIterator.next()).getId());
	 			if(edge.getTarget().getId() == idPoint || edge.getOrigin().getId() == idPoint)
	 			{
	 				foundedPoint = true;
	 			}
	 		}
	 		if(foundedPoint)
	 		{
	 			sectionIterator = tour.getSections().iterator();
	 			int countRound= 0;
	 			while(sectionIterator.hasNext())
	 			{
	 				edge = edges.get(((Section)sectionIterator.next()).getId());
	 				if(((edge.getTarget().getId() == idPoint || edge.getOrigin().getId() == idPoint) && countRound > 0) || (countRound == 0 && edge.getTarget().getId() == idPoint))
	 				{
	 					edge.setAlreadyPassed(true);
	 					addAlreadyPassedEdge(edge);
	 					break;
	 				}
	 				else
	 				{
	 					edge.setAlreadyPassed(true);
	 					addAlreadyPassedEdge(edge);
	 				}
	 				countRound++;
	 			}
	 		}
	 	}
	
 	/**
 	 * this method removes all on the map.
 	 */
	public void resetMap()
	{
		points.clear();
		edges.clear();
		labels.clear();
		tours.clear();
	}
	
	public void addPoint(ViewPoint s,int id)
	{
		points.add(id,s);
	}
	
	public void addEdge(ViewEdge s, int id){
		edges.put(id,s);
	}
	
	public void addLabel(ViewLabel s){
		labels.add(s);
	}
	 
	
	/**
	 * This method display a tour on the map. If the tour already exists and you just want to modify it, it undraw previous tour and redraw the up-to-date one.
	 * @param tour
	 */
	public void displayTour(Tour tour)
	{		
		if(tours.containsKey(tour.getId())) // IF TOUR ALREADY EXIST THEN WE UNDRAW IT
		{ System.out.println("Updating an existing tour");
			tours.get(tour.getId()).undrawTour();
			tours.remove(tour.getId());
		}
		
		// Getting a list of concerned points that will be used for the used tour
		ArrayList<ViewPoint> concernedPoints = new ArrayList<>();
		
		for(DeliveryPoint dp: tour.getDeliveryPoints())
		{
			concernedPoints.add(points.get(dp.getMapNodeId()));
		}
		
		// Getting a map of concerned edges
		LinkedHashMap<Integer, ViewEdge> concernedEdge = new LinkedHashMap<>();
		
		// Iterate over the sections
		Iterator<Section> sectionIterator = tour.getSections().iterator();
		Section currSection;
		ViewEdge edge;
		
		while(sectionIterator.hasNext())
		{			
			currSection = sectionIterator.next();
			edge = edges.get(currSection.getId());
			concernedEdge.put(currSection.getId(), edge);
		}
		
		// Creating the tour object
		ViewTour vTour = new ViewTour(tour.getId(), concernedEdge, concernedPoints, points.get(tour.getEntrepotId()));
		
		tours.put(vTour.getId(), vTour);
		
		// if there is only one tour, then we set it selected
		setTourSelected(vTour.getId());
		
		repaint();
	}
	
	

	public void toursColoring()
	{
		ViewTour selected=null;
		for(Entry<Integer,ViewTour> tour : tours.entrySet())
		{
			ViewTour vT = tour.getValue();
			if(vT.isSelected())
				selected = vT;
			else
				vT.colorTourComponentsOnMap();
			
		}
		if(selected != null)
			selected.colorTourComponentsOnMap();
			
	}
	/**
	 * Set selected a tour. All other tours are unselected
	 * @param idTour
	 */
	public void setTourSelected(int idTour)
	{
		for(ViewTour t : tours.values())
		{
			t.setSelected(false);
		}
		tours.get(idTour).setSelected(true);
		toursColoring();
	}
	

	/**
	 * This is the draw method of the map. It iterate over its list of shapes, and draw all shapes.
	 */
	public void paintComponent(Graphics g) { 	
		super.paintComponent(g);
		
		/*
		RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	    ((Graphics2D) g).setRenderingHints(rh);
			*/	
		g.setFont(new Font("Calibri", Font.PLAIN, 20)); 
		
		Iterator<ViewPoint> i = points.iterator();
		Iterator<ViewEdge> j = edges.values().iterator();
		Iterator<ViewLabel> k = labels.iterator();
		
		
		while(i.hasNext())
			i.next().drawShape(g, getWidth(), getHeight());

		while(j.hasNext())
		{
			ViewEdge curr =j.next();	
			curr.drawShape(g, getWidth(), getHeight());
		}
		
		i = points.iterator();
		while(i.hasNext())
			i.next().drawShape(g, getWidth(), getHeight());
		
		while(k.hasNext()){
			ViewLabel curr = k.next();
			curr.drawShape(g, getWidth(), getHeight());
		}
		g.dispose();
		
	}
	

	/**
	 * This method return the shape (if exists) that contains the (x,y) point passed in parameter.
	 */
	public ViewPoint containsPoint(int x, int y)
	{
		Iterator<ViewPoint> i = points.iterator();
		while(i.hasNext())
		{
			ViewPoint cur = i.next();
			
			if(cur.contains(x, y))
				return cur;
		}
		
		return null;
	}

	public ViewPoint getPoint(int id)
	{
		return (ViewPoint) points.get(id);
	}
	
	private void addAlreadyPassedEdge(ViewEdge edge)
 	{
 		alreadyPassedEdges.add(edge);
 	}
	 	
 	public void removeAllAlreadyPassedEdges(){
 		Iterator<ViewEdge> i = alreadyPassedEdges.iterator();
 		while(i.hasNext())
 		{
 			ViewEdge e = i.next();
 			e.setAlreadyPassed(false);
 		}
 		alreadyPassedEdges.clear();
 	}
	
	/**
	 * Return the first matching Section.
	 * @param x
	 * @param y
	 * @return
	 */
	public ViewEdge containsEdge(int x, int y)
	{
		Iterator<ViewEdge> i = edges.values().iterator();
		while(i.hasNext())
		{
			ViewEdge current = i.next();
			if(current.contains(x, y))
			{
				return current;
			}
		}
		
		return null;
	}
	
	public boolean labelsIsEmpty (){
		return labels.isEmpty();
	}
	
	public void setAllPointHoved(boolean hooved)
	{
		Iterator<ViewPoint> i = this.points.iterator();
		while(i.hasNext())
		{
			i.next().setHoved(hooved);
		}
	}
	
	public void removeAllLabels(){
		labels.clear();
	}

	public HashMap<Integer, ViewEdge> getEdges() {
		// TODO Auto-generated method stub
		return edges;
	}
	
	public int getSelectedTour()
	{
		for(Entry<Integer,ViewTour> tour : tours.entrySet())
		{
			ViewTour vT = tour.getValue();
			if(vT.isSelected())
			{
				return vT.getId();
			}
				
		}
		return -1;
	}
	
	/**
	 * this method reset all the color and weights of element on the map.
	 */
	private void clearDisplaySectionAndPoints()
	{
		// Iterate over all section
		for(Entry<Integer, ViewEdge> entry : edges.entrySet())
		{
			entry.getValue().clearSection(entry.getKey());
		}
		
		// Iterate over all 
		for(ViewPoint point : points)
		{
			point.clearPoint();
		}
	}
	
	/** 
	 * This method reset all deliveries on the map. It clears the tours map and it display default aspects of all the points.
	 */
	public void resetDeliveriesOnMap() {
		tours.clear();
		clearDisplaySectionAndPoints();
		repaint();
	}
	
	public ViewTour getViewTour(int id) { return tours.get(id); }
	public MainFrame getMainFrame() { return mainFrame; }
}
