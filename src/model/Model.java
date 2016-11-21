package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import controller.Controller;

public class Model extends Observable implements IModel {

	public Model(Controller controller) {
	
	}

	public void parseMapFile(File currentFile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Node> getNodes() {
		// TODO Auto-generated method stub
		ArrayList<Node> liste = new ArrayList<Node>();
		liste.add(new Node(0, 226, 240));
		liste.add(new Node(1, 423, 127));
		liste.add(new Node(2, 32, 164));
		liste.add(new Node(3, 26, 210));
		liste.add(new Node(4, 493, 15));
		liste.add(new Node(5, 28, 69));
		liste.add(new Node(6, 420, 410));
		liste.add(new Node(7, 10, 237));
		liste.add(new Node(8, 26, 48));
		liste.add(new Node(9, 14, 320));
		liste.add(new Node(10, 366, 240));
		liste.add(new Node(11, 366, 366));
		liste.add(new Node(12, 210, 25));
		liste.add(new Node(13, 460, 500));
		liste.add(new Node(14, 460, 450));
		liste.add(new Node(15, 500, 490));
		
		
		return liste;
	}	
}
