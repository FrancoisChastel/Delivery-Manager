package view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JPanel;

public class Map extends JPanel {

	private ArrayList<ViewPoint> points;
	public HashMap<Integer,ViewEdge> edges;
	
	/**
	 * Create the panel.
	 */
	public Map() {
				
		points = new ArrayList<ViewPoint>();
		edges = new HashMap<Integer,ViewEdge>();

		// Action Listener
		this.addMouseListener(new MapMouseListener(this));
	}
	
	public void addPoint(ViewPoint s,int id)
	{
		points.add(id,s);
	}
	
	public void addEdge(ViewEdge s, int id){
		edges.put(id,s);
	}
	
	public void setTourne(ArrayList<Integer> ids)
	{
		Iterator<Integer> i = ids.iterator();
		
		while(i.hasNext())
		{
			int currId = i.next();
			ViewEdge v =edges.get(currId);
			v.setColorId(Color.green,currId);
		}
		
		repaint();
	}
	
	/**
	 * This is the draw method of the map. It iterate over its list of shapes, and draw all shapes.
	 */
	public void paintComponent(Graphics g) { 	
		super.paintComponent(g);
				
		Iterator<ViewPoint> i = points.iterator();
		Iterator<ViewEdge> j = edges.values().iterator();
		
		while(i.hasNext())
			i.next().drawShape(g, getWidth(), getHeight());

		while(j.hasNext())
		{
			ViewEdge curr =j.next();
			
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
}
