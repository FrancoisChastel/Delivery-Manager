package view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

public class Map extends JPanel {

	private ArrayList<IShape> points;
	
	/**
	 * Create the panel.
	 */
	public Map() {
		this.setBackground(Color.BLACK);		
		points = new ArrayList<IShape>();		
		addPoint(new ViewPoint(0.5,0.5,0),0);
		addPoint(new ViewPoint(0.55,0.55,1),1);
		// Action Listener
		this.addMouseListener(new MapMouseListener(this));
	}
	
	public void addPoint(IShape s,int id)
	{
		points.add(id,s);
	}
	
	/**
	 * This is the draw method of the map. It iterate over its list of shapes, and draw all shapes.
	 */
	public void paintComponent(Graphics g) { 	
		Iterator<IShape> i = points.iterator();
		
		while(i.hasNext())
			i.next().drawShape(g, getWidth(), getHeight());
	}

	/**
	 * This method return the shape (if exists) that contains the (x,y) point passed in parameter.
	 */
	public IShape containsShape(int x, int y)
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
}
