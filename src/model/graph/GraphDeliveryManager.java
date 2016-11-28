package model.graph;

import java.util.ArrayList;
import java.util.List;

import model.Model;

/**
 * This class encapsulates all services of the DeliveryManagerGraph. It uses the generic "Graph" class.
 * @author antoine
 *
 */
public class GraphDeliveryManager {
	// Attributes
	private Graph<MapNode, Section>  graph;
	private ArrayList<Section> sections;
	private ArrayList <MapNode> nodes;
	private Model model;
	
	/**
	 * Normal Constructor.
	 * @param model
	 */
	public GraphDeliveryManager(Model model)
	{
		this.model = model;
		graph 	= new Graph<MapNode, Section> ();
		sections = new ArrayList<Section>();
		nodes = new ArrayList<MapNode>();
	}
	
	public Section getSection(MapNode o, MapNode d)
	{
		return graph.getEdge(o, d);
	}
	// Accessors
	public ArrayList<MapNode> getNodeList() 		{	return nodes;	}
	public ArrayList<Section> getSectionList() 	{	return sections;	}
	public Graph<MapNode, Section> getGraph() {return graph;}
}
