package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import controller.Controller; 

public class Model extends Observable implements IModel {

	Graph<MapNode, Section>  graph = new Graph<MapNode, Section>();
	XmlParser xmlParser;
	DeliveryOrder deliveryOrder;
	TSP1 tsp;
	LowerCosts lowCosts;
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
		
		int[] reducedPath = new int[xmlParser.getDelOrder().getDeliveryList().size()+1];
		//LinkedList<MapNode> completePath;
				
		// get the order of the delivery	
		lowCosts = new LowerCosts(graph,xmlParser.getDelOrder());
		tsp.chercheSolution(2500, xmlParser.getDelOrder().getDeliveryList().size()+1, lowCosts.getCostsMatrix(), xmlParser.getDelOrder().getTimes());
		for( int i= 0 ; i < xmlParser.getDelOrder().getDeliveryList().size()+1;i++)
		{		
			reducedPath[i] = tsp.getMeilleureSolution(i);
			System.out.println(tsp.getMeilleureSolution(i));
		}
		
		//adding the intermediates nodes
		addIntermediatePoints(reducedPath, xmlParser.getDelOrder());
		
	}

	public ArrayList<MapNode> addIntermediatePoints(int[] reducedGraph,DeliveryOrder deliveryOrder)
	{
		HashMap<MapNode,HashMap<MapNode,ArrayList<MapNode>>> pathFromPoint = lowCosts.getPaths();
		ArrayList<MapNode>  path = new ArrayList<MapNode>();
		ArrayList<MapNode> pathToNode = new ArrayList<MapNode>();
		
		for(int i=0;i< reducedGraph.length;i++)
		{
			path.add(deliveryOrder.getDeliveryList().get(reducedGraph[i]).getAdress());
			HashMap<MapNode,ArrayList<MapNode>> fromPoint = pathFromPoint.get(deliveryOrder.getDeliveryList().get(reducedGraph[i]).getAdress());
			pathToNode = fromPoint.get(deliveryOrder.getDeliveryList().get(reducedGraph[i+1]).getAdress());
			
			path.addAll(pathToNode);
		}
		
		for(int i=0;i<path.size();i++)
		{
			System.out.println(path.get(i).getidNode());
		}
		
		
		
		return path;
	}

	public void loadDeliveryFile(File currentFile) {
		// TODO Auto-generated method stub
		
	}

}


