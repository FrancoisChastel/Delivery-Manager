package view;

import java.util.Iterator;
import java.util.List;

import model.DeliveryPoint;
import model.Node;

public class Adapter {
	
	private int minX, minY, maxX, maxY;
	private int etendueX, etendueY;
	private Map map;
	
	/**
	 * This method get the extremums of deliveryPoints in order to calibrate the size of the map.  
	 */
	public void calibration(List<Node> node)
	{
		minX = Integer.MAX_VALUE;
		maxX = 0;
		minY = Integer.MAX_VALUE;
		maxY = 0;
		
		// Iterate over the list to get extremum
		Iterator<Node> i = node.iterator();		
		while(i.hasNext())
		{
			Node curr = i.next();
			
			int currX =curr.getX();
			int currY =curr.getY();
			
			if(currX < minX)
				minX=currX;
			if(currX > maxX)
				maxX=currX;
			if(currY < minY)
				minY=currY;
			if(currY > maxY)
				maxY=currY;
		}
		
		etendueX = maxX-minX;
		etendueY = maxY-minY;
		System.out.println("MX"+maxX+"mX"+minX+"MY"+maxY+"mY"+minY);
	}
	
	/**
	 * This method calibrate and create all ViewPoint from a liste of model.Node.
	 * @param node
	 */
	public void drawModel(List<Node> node)
	{
		calibration(node);
		
		Iterator<Node> i = node.iterator();
		
		while(i.hasNext())
		{
			Node curr = i.next();
			map.addPoint(getView(curr), curr.getidNode());
		}
		map.repaint();
	}
	
	/**
	 * This methode create a ViewPoint from a Node. It calculate the x and y in frame percentage.
	 * @param p
	 * @return The created ViewPoint
	 */
	public ViewPoint getView(Node p)
	{		
		double x = ((double)(p.getX()-minX))/etendueX;
		double y = ((double)(p.getY()-minY))/etendueY;
		System.out.println("X:"+x+"Y:"+y);
		return new ViewPoint(x,y,p.getidNode());		
	}
	
	public Adapter(Map map)
	{
		this.map = map;
	}
}
