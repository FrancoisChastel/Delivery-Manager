package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JPanel;

import model.Tour;
import model.graph.Section;

public class Map extends JPanel {

	private ArrayList<ViewPoint> points;
	// the key of this map is the ID of the section
	private HashMap<Integer,ViewEdge> edges;
	private ArrayList<ViewLabel> labels;
	private HashMap<Integer,ViewTour> tours;
	/**
	 * Create the panel.
	 */
	public Map() {
				
		points = new ArrayList<ViewPoint>();
		edges  = new HashMap<Integer,ViewEdge>();
		labels = new ArrayList<ViewLabel>();
		tours  = new HashMap<>();
		
		this.setBackground(Color.DARK_GRAY);
		// Action Listener
		MapMouseListener mouseListener = new MapMouseListener(this);
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);
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
	 * This method display a tour on the map. 
	 * @param tour
	 */
	public void displayTour(Tour tour)
	{		
		// Getting a list of concerned points that will be used for the used tour
		ArrayList<ViewPoint> concernedPoints = new ArrayList<>();
		
		for(Integer id: tour.getDeliveryPoints())
			concernedPoints.add(points.get(id));
		
		// Getting a map of concerned edges
		LinkedHashMap<Integer, ViewEdge> concernedEdge = new LinkedHashMap<>();
		
		// Iterate over the sections
		Iterator<Section> sectionIterator = tour.getSections().iterator();
		Section currSection;
		ViewEdge edge;
		
		while(sectionIterator.hasNext())
		{			
			currSection = sectionIterator.next();
			concernedEdge.put(currSection.getId(), edges.get(currSection.getId()));
		}
		
		// Creating the tour object
		ViewTour vTour = new ViewTour(tour.getId(), concernedEdge, concernedPoints, points.get(tour.getEntrepotId()));
		
		tours.put(vTour.getId(), vTour);
		
		// if there is only one tour, then we set it selected
		if(tours.size()==1)
			setTourSelected(vTour.getId());
		repaint();
	}
	
	/**
	 * Set selected a tour. All other tours are unselected
	 * @param idTour
	 */
	private void setTourSelected(int idTour)
	{
		for(ViewTour t : tours.values())
		{
			t.setSelected(false);
		}
		tours.get(idTour).setSelected(true);
	}
	

	/**
	 * This is the draw method of the map. It iterate over its list of shapes, and draw all shapes.
	 */
	public void paintComponent(Graphics g) { 	
		super.paintComponent(g);
				
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
	
	public void removeAllLabels(){
		labels.clear();
	}



	public HashMap<Integer, ViewEdge> getEdges() {
		// TODO Auto-generated method stub
		return edges;
	}
}
