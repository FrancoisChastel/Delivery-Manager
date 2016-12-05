package view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import model.Tour;
import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryPoint;
import model.graph.MapNode;
import model.graph.Section;
import view.jtree.TreeDefaultIconNode;
import view.jtree.TreeMapNode;
import view.jtree.TreeTour;
import view.jtree.TreeWaitingTime;

/**
 * 
 * @author antoine
 *
 */
public class Adapter {
	
	private int minX, minY, maxX, maxY;
	private int etendueX, etendueY;
	private Map map;
	
	// this enum is used to use JTree Icon -----------------------------
	public static enum Icons {DELIVERY_ARRIVING,DELIVERY_LEAVING, DELIVERY, TOUR, ADDRESS, SCHEDULES, WAITING};
	// you can use icon through this map.
	private HashMap<Icons,ImageIcon> icons;
	// -----------------------------------------------------------------
	
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
		map.resetMap();
		
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
			ViewEdge edge = getIfalreadyExist(map.getEdges(), currSection);
			
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
	
	/**
	 * Create a TreeMapNode from a deliveryPoint
	 * @param dp
	 * @return
	 */
	public TreeMapNode getTreeNode(DeliveryPoint dp)
	{		
		TreeMapNode node = new TreeMapNode("Point "+dp.getMapNodeId(), dp.getMapNodeId(), icons.get(Icons.DELIVERY));
		
		// Get back the corresponding deliveryNode
		Delivery delivery = dp.getDelivery();
		
		// Address
		node.add(new TreeDefaultIconNode( "Address : "+ delivery.getAdress().getidNode(),icons.get(Icons.ADDRESS)) );
		
		// Plage
		TreeDefaultIconNode plage = new TreeDefaultIconNode("Schedules",icons.get(Icons.SCHEDULES));
		
		// Schedules
		plage.add(new TreeDefaultIconNode(View.getDate(dp.getArrivingDate()), icons.get(Icons.DELIVERY_ARRIVING)));
		plage.add(new TreeDefaultIconNode(View.getDate(dp.getLeavingDate()), icons.get(Icons.DELIVERY_LEAVING)));
		
		node.add(plage);
		
		return node;
	}
	
	/**
	 * Return a TreeWaitingTime.
	 * @param p1
	 * @param p2
	 * @return
	 */
	public TreeWaitingTime getTreeWaitingTime(DeliveryPoint p1, DeliveryPoint p2, Tour t, int positionInTour)
	{
		long waitingTime = t.getAvailableTimeBeweenTwoPoints(p1, p2);	
		
		return new TreeWaitingTime("waiting : "+View.formatDateFromSecond(waitingTime), icons.get(Icons.WAITING), p1.getMapNodeId(),positionInTour,waitingTime);
	}
	
	/**
	 * Create a TreeTour from tour.
	 * @param t
	 * @return
	 */
	public TreeTour getTreeTour(Tour t)
	{
		TreeTour res = new TreeTour("Tour "+t.getId(), t.getId(), icons.get(Icons.TOUR));
		// TODO add meta data of tour
		
		return res;
	}
	
	/**
	 * Create de ViewEdge from a Section.
	 * @param section
	 * @return
	 */
	public ViewEdge getView(Section section)
	{		
		return new ViewEdge(map.getPoint(section.getIdOrigin()), map.getPoint(section.getIdDestination()), section.getId());		
	}
	
	
	/**
	 * Constructor
	 * @param map
	 */
	public Adapter(Map map)
	{
		this.map = map;
		loadIcons();
	}
	
	/**
	 * This method manages all Image Icon creations.
	 */
	private void loadIcons()
	{
		icons = new HashMap<>();
		icons.put(Icons.DELIVERY_ARRIVING, new ImageIcon("icone/next.png"));
		icons.put(Icons.DELIVERY_LEAVING, new ImageIcon("icone/back.png"));
		icons.put(Icons.DELIVERY, new ImageIcon("icone/box.png"));
		icons.put(Icons.TOUR, new ImageIcon("icone/trucking.png"));
		icons.put(Icons.ADDRESS, new ImageIcon("icone/street-map.png"));
		icons.put(Icons.SCHEDULES, new ImageIcon("icone/stopwatch.png"));
		icons.put(Icons.WAITING, new ImageIcon("icone/sand-clock.png"));
	}
}
