package model;

import java.io.File;
import java.util.ArrayList;
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
		//LowerCosts.generateCosts(graph, deliveryOrder);

				//#DeliveryOrder est ‡ remplacer par l'objet DeliveryOrder correspondant ‡ la demande de livraison
				//int[][] couts = new int[#DeliveryOrder.getmaxIdNode()+1][#DeliveryOrder.getmaxIdNode()+1];
				
				// Etape 1
				// InsÈrer Dijkstra ici
				
				// Etape 2
				// TSP.chercheSolution(2500, #DeliveryOrder.getDeliveryList().size(), couts, #DeliveryOrder.getTimes())
				// TSP.getMeilleureSolution(int i) ‡ chaque Ètape i
				
				// Etape 3
				// On insËre les points intermÈdiaires (appel ‡ Dijkstra)
				// 
	}

}


