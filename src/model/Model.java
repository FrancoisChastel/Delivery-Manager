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
		xmlParser = new XmlParser(currentFile);
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
		
	}

}
