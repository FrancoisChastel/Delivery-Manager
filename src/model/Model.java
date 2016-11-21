package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import controller.Controller; 

public class Model extends Observable implements IModel {

	//private Graph<Section, MapNode> map;
	
	public Model(Controller controller) {
		
	}

	public void parseMapFile(File currentFile) {
		// TODO Auto-generated method stub
		
	}

	public List<MapNode> getMapNodes() {
		return null;
		// TODO Auto-generated method stub
		/*ArrayList<MapNode> liste = new ArrayList<MapNode>();
		liste.add(new MapNode(0, 226, 240));
		liste.add(new MapNode(1, 423, 127));
		liste.add(new MapNode(2, 32, 164));
		liste.add(new MapNode(3, 26, 210));
		liste.add(new MapNode(4, 493, 15));
		liste.add(new MapNode(5, 28, 69));
		liste.add(new MapNode(6, 420, 410));
		liste.add(new MapNode(7, 10, 237));
		liste.add(new MapNode(8, 26, 48));
		liste.add(new MapNode(9, 14, 320));
		liste.add(new MapNode(10, 366, 240));
		liste.add(new MapNode(11, 366, 366));
		liste.add(new MapNode(12, 210, 25));
		liste.add(new MapNode(13, 460, 500));
		liste.add(new MapNode(14, 460, 450));
		liste.add(new MapNode(15, 500, 490));
		
		
		return liste;
		*/
	}	
	
	public void parseDeliveriesFile(File currentFile)
	{
		
	}
	
	public void generateTour()
	{
		
	}

	@Override
	public List<MapNode> getNodes() {
		// TODO Auto-generated method stub
		return null;
	}
}
