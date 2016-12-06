package tests.unitaires.engine;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Model;
import model.engine.LowerCosts;
import model.engine.Pair;
import model.graph.Graph;
import model.graph.GraphDeliveryManager;
import model.graph.MapNode;
import model.graph.Section;

public class DijkstraTest {

	private Graph <MapNode, Section> graph;
	private GraphDeliveryManager graphDelMan;
	
	@Before
	public void setUp() {
		Model model = new Model(null);
		graphDelMan = model.getGraphDeliveryManager();
		graph = graphDelMan.getGraph();
		MapNode n1 = new MapNode(1,300,500);
		MapNode n2 = new MapNode(2,300,400);
		MapNode n3 = new MapNode(3,200,500);
		MapNode n4 = new MapNode(4,100,400);
		Section s12 = new Section(1,2,"s12",200,50);
		Section s13 = new Section(1,3,"s13",300,30);
		Section s14 = new Section(1,4,"s14",400,10);
		Section s23 = new Section(2,3,"s23",100,50);
		Section s24 = new Section(2,4,"s24",400,10);
		Section s34 = new Section(3,4,"s34",100,20);
		graphDelMan.getNodeList().add(n1);
		graph.add(n1);
		graphDelMan.getNodeList().add(n2);
		graph.add(n2);
		graphDelMan.getNodeList().add(n3);
		graph.add(n3);
		graphDelMan.getNodeList().add(n4);
		graph.add(n4);
		graph.addDestination(n1, s12, n2);
		graph.addDestination(n1, s13, n3);
		graph.addDestination(n1, s14, n4);
		graph.addDestination(n2, s23, n3);
		graph.addDestination(n2, s24, n4);
		graph.addDestination(n3, s34, n4);
		
		
	}
	
	@After
	public void tearDown() {
		graph.emptyGraph();
	}
	
	
	/**
	 * Test dijkstra between two nodes
	 */
	@Test
	public void dijkstraTest1() {
		MapNode origin = new MapNode(1,300,500);
		MapNode target = new MapNode(4,100,400);
		
		MapNode n2 = new MapNode(2,300,400);
		MapNode n3 = new MapNode(3,200,500);
		
		Pair<ArrayList<MapNode>,Integer> dijkstra = LowerCosts.dijkstra(graphDelMan, origin, target);
		
		ArrayList<MapNode> nodesList = new ArrayList<>();
		nodesList.add(origin);
		nodesList.add(n2);
		nodesList.add(n3);
		nodesList.add(target);
		
		Pair<ArrayList<MapNode>,Integer> results = new Pair<ArrayList<MapNode>,Integer>(nodesList,11);
		
		assertEquals(dijkstra,results);
	}
	
	/**
	 * Test dijkstra between two new nodes
	 */
	@Test
	public void dijkstraTest2() {
		MapNode origin = new MapNode(1,300,500);
		MapNode target = new MapNode(4,100,400);
		
		MapNode n2 = new MapNode(2,300,400);
		MapNode n3 = new MapNode(3,200,500);
		
		//Path very long
		graph.getEdge(n2, n3).setLength(30000);;
		
		
		Pair<ArrayList<MapNode>,Integer> dijkstra = LowerCosts.dijkstra(graphDelMan, origin, target);
		
		ArrayList<MapNode> nodesList = new ArrayList<>();
		nodesList.add(origin);
		nodesList.add(n3);
		nodesList.add(target);
		
		Pair<ArrayList<MapNode>,Integer> results = new Pair<ArrayList<MapNode>,Integer>(nodesList,15);
		
		assertEquals(dijkstra,results);
	}
	
	

}