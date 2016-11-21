package view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

public class Map extends JPanel {

	private ArrayList<IShape> points;
	private ArrayList<IShape> troncons;
	
	/**
	 * Create the panel.
	 */
	public Map() {
				
		points = new ArrayList<IShape>();
		troncons = new ArrayList<IShape>();

		// Action Listener
		this.addMouseListener(new MapMouseListener(this));
	}
	
	public void addPoint(IShape s,int id)
	{
		points.add(id,s);
	}
	
	public void addTroncon(IShape s, int id){
		troncons.add(id,s);
	}
	
	/**
	 * This is the draw method of the map. It iterate over its list of shapes, and draw all shapes.
	 */
	public void paintComponent(Graphics g) { 	
		super.paintComponent(g);
		g.setColor(getForeground());
		Iterator<IShape> i = points.iterator();
		Iterator<IShape> j = troncons.iterator();
		
		while(i.hasNext())
			i.next().drawShape(g, getWidth(), getHeight());

		while(j.hasNext())
			j.next().drawShape(g, getWidth(), getHeight());
	
	}

	/**
	 * This method return the shape (if exists) that contains the (x,y) point passed in parameter.
	 */
	public IShape containsPoint(int x, int y)
	{
		Iterator<IShape> i = points.iterator();
		while(i.hasNext())
		{
			IShape cur = i.next();
			
			if(cur.contains(x, y))
				return cur;
		}
		
		return null;
	}

	public ViewPoint getPoint(int id)
	{
		return (ViewPoint) points.get(id);
	}
	
	public IShape containsTroncon(int x, int y)
	{
		Iterator<IShape> i = troncons.iterator();
		while(i.hasNext())
		{
			IShape current = i.next();
			if(current.contains(x, y))
			{
				return current;
			}
		}
		
		return null;
	}

	public void addTroncon(ViewTroncon view) {
		troncons.add(view);
	}
	
}
