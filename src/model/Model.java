package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import controller.Controller; 

public class Model extends Observable implements IModel {

	Graph<MapNode, Section>  graph = new Graph<MapNode, Section>();
	XmlParser xmlParser;
	DeliveryOrder deliveryOrder;
	TSP1 tsp;
	//private Graph<Section, MapNode> map;
	
	public Model(Controller controller) {
		
	}

	public void parseMapFile(File currentFile) {
		// TODO Auto-generated method stub
		xmlParser = new XmlParser(currentFile);
		graph = xmlParser.getGraph();
		File deliveries = new File("XML/livraisons5x5-4.xml");
		xmlParser.xmlDeliveriesParser(deliveries);
		tsp = new TSP1();
		generateTour();
		setChanged();
		notifyObservers();
	}

	public List<MapNode> getMapNodes() {
		if(xmlParser.getNodeList() != null)
		{
			return xmlParser.getNodeList();
		}
		return null;
		
	}	
	
	@Override
	public List<Section> getSections() {
		if(xmlParser.getSectionList() != null)
		{
			return xmlParser.getSectionList();
		}
		return null;
	}
	
	public void parseDeliveriesFile(File currentFile)
	{
		
	}
	
	public void generateTour()
	{
		
		int[] reducedPath = new int[xmlParser.getDelOrder().getDeliveryList().size()];
		//LinkedList<MapNode> completePath;
				
		// get the order of the delivery	
		tsp.chercheSolution(2500, xmlParser.getDelOrder().getDeliveryList().size(), LowerCosts.generateCosts(graph, xmlParser.getDelOrder()), xmlParser.getDelOrder().getTimes());
		for( int i= 0 ; i < xmlParser.getDelOrder().getDeliveryList().size();i++)
		{		
			reducedPath[i] = tsp.getMeilleureSolution(i);
		}
			
		//adding the intermediates nodes
		addIntermediatePoints(reducedPath, xmlParser.getDelOrder());
		
	}

	public ArrayList<MapNode> addIntermediatePoints(int[] reducedGraph,DeliveryOrder deliveryOrder)
	{
		ArrayList<MapNode>  path = null;
		
		for(int i=0;i< reducedGraph.length;i++)
		{
			path.add(deliveryOrder.getDeliveryList().get(i).getAdress());
			// still need to add the intermediates nodes
		}
		
		
		
		return path;
	}

}


