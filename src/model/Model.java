package model;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import controller.Controller; 

public class Model extends Observable implements IModel {

	Graph<MapNode, Section>  graph = new Graph<MapNode, Section>();
	XmlParser xmlParser;
	//private Graph<Section, MapNode> map;
	
	public Model(Controller controller) {
		
	}

	public void parseMapFile(File currentFile) {
		// TODO Auto-generated method stub
		XmlParser xmlParser = new XmlParser(currentFile);
		graph = xmlParser.getGraph();
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
		//int[] reducedPath;
		//LinkedList<MapNode> completePath;
		//LowerCosts.generateCosts(graph, deliveryOrder);

				//#DeliveryOrder est ‡ remplacer par l'objet DeliveryOrder correspondant ‡ la demande de livraison
				//int[][] couts = new int[#DeliveryOrder.getmaxIdNode()+1][#DeliveryOrder.getmaxIdNode()+1];
				
				// Etape 1
				// InsÈrer Dijkstra ici
				
				// Etape 2
				// TSP.chercheSolution(2500, #DeliveryOrder.getDeliveryList().size(), couts, #DeliveryOrder.getTimes())
				// for( int i= 0 ; i < #DeliveryOrder.getDeliveryList().size();i++)
				// {		
				// 		reducedPath[i} = TSP.getMeilleureSolution(i);
				// }
				
				// Etape 3
				// On insËre les points intermÈdiaires (appel ‡ Dijkstra)
				// completePath =addIntermediatePoints(reducedGraph,#DeliveryOrder)
	}
	
	/*public LinkedList<MapNode> addIntermediatePoints(int[] reducedGraph,DeliveryOrder deliveryOrder)
	{
		LinkedList<MapNode>  path;
		
		for(int i=0;i< reducedGraph[i].length();i++)
		{
			path.add(deliveryOrder.getDeliveryList().[i].getAdress();
			// still need to add the intermediates nodes
		}
		
		
		
		return path;
	}*/

}


