package model;

import java.io.File;
import java.util.List;
import java.util.Observable;

import controller.Controller;

public class Model extends Observable implements IModel {

	//private Graph<Section, Node> map;
	
	public Model(Controller controller) {
		
	}

	public void parseMapFile(File currentFile) {
		// TODO Auto-generated method stub
		
	}
	
	public void parseDeliveriesFile(File currentFile)
	{
		
	}
	
	public void generateTour()
	{
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

	@Override
	public List<MapNode> getMapNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Section> getSections() {
		// TODO Auto-generated method stub
		return null;
	}
}
