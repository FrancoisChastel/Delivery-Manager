package view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import model.DeliveryPoint;
import model.MapNode;
import model.Section;


public class Adapter {
	
	private int minX, minY, maxX, maxY;
	private int etendueX, etendueY;
	private Map map;
	
	/**
	 * This method get the extremums of deliveryPoints in order to calibrate the size of the map.  
	 */
	public void calibration(List<MapNode> node)
	{
		minX = Integer.MAX_VALUE;
		maxX = 0;
		minY = Integer.MAX_VALUE;
		maxY = 0;
		
		// Iterate over the list to get extremum
		Iterator<MapNode> i = node.iterator();		
		while(i.hasNext())
		{
			MapNode curr = i.next();
			
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
	}
	
	/**
	 * This method calibrate and create all ViewPoint from a liste of model.Node.
	 * @param node
	 */
	public void drawModel(List<MapNode> node, List<Section> sections)
	{
		calibration(node);
		
		Iterator<MapNode> i = node.iterator();
		
		// Building the ViewPoint List
		while(i.hasNext())
		{
			MapNode curr = i.next();
			map.addPoint(getView(curr), curr.getidNode());
		}
		
		Iterator<Section> j = sections.iterator();
		
		// Building the Viewedge map
		while(j.hasNext())
		{
			Section currSection = j.next();
			
			// Try to get the edge corresponding to the section
			ViewEdge edge = getIfalreadyExist(map.edges, currSection);
			
			// If it does not exists, then we create it.
			if(edge == null)
				edge = getView(currSection);
			else // If it exists, we just append the current Section ID to it
			{
				edge.addSection(currSection.getId());
			}
			// Add it to the edge map associated with the sectionId
			map.addEdge(edge, currSection.getId());
		}
		
		map.repaint();
	}
	
	/**
	 * This method return a Viewedge if the section passed in parameter 
	 * corresponds to and existing Viewedge.
	 * @param t
	 * @return
	 */
	public ViewEdge getIfalreadyExist(HashMap<Integer,ViewEdge> edges, Section t)
	{
		Iterator<ViewEdge> j = edges.values().iterator();
		
		while(j.hasNext())
		{
			ViewEdge curr = j.next();
			if(curr.getOrigin().getId()==t.getIdDestination() && t.getIdOrigin()==curr.getTarget().getId() )
				return curr;			
		}
		return null;
	}
	
	/**
	 * This method create a ViewPoint from a Node. It calculate the x and y in frame percentage.
	 * @param p
	 * @return The created ViewPoint
	 */
	public ViewPoint getView(MapNode p)
	{		
		double x = ((double)(p.getX()-minX))/etendueX;
		double y = ((double)(p.getY()-minY))/etendueY;
		return new ViewPoint(x,y,p.getidNode());		
	}
	
	public ViewEdge getView(Section section)
	{		
		return new ViewEdge(map.getPoint(section.getIdOrigin()), map.getPoint(section.getIdDestination()), section.getId());		
	}
	
	
	public Adapter(Map map)
	{
		this.map = map;
	}
}
