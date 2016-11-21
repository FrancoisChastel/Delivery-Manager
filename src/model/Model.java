package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import controller.Controller; 

public class Model extends Observable implements IModel {

	private Graph<MapNode, Section>  graph = new Graph<MapNode, Section>();
	private XmlParser xmlParser;
	private DeliveryOrder deliveryOrder = new DeliveryOrder(0, null, null, null);
	
	
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
		//Parser le xml livraisons
	}
	
	public void generateTour()
	{
		LowerCosts.generateCosts(graph, deliveryOrder);
	}

	@Override
	public List<MapNode> getNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
