package tests.unitaires.graph;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.graph.Graph;
import model.graph.MapNode;
import model.graph.Section;

public class GraphTest {

	private Graph <MapNode, Section> graph;
	
	@Before
	public void setUp() {
		graph = new Graph<MapNode, Section>();
	}
	
	@After
	public void tearDown() {
		graph.emptyGraph();
	}
	
	
	/**
	 * Test Add Method and getNodeById
	 */
	@Test
	public void addTest1() {
		MapNode startNodeToAdd = new MapNode(1,0,0);
		graph.add(startNodeToAdd);
		assertEquals(startNodeToAdd,graph.getNodeById(1));
	}

	/**
	 * Test AddDestination Method and getEdge
	 */
	@Test
	public void addDestinationTest1() {
		MapNode startNodeToAdd = new MapNode(1,0,0);
		graph.add(startNodeToAdd);
		startNodeToAdd = graph.getNodeById(1);
		MapNode destinationNodeToAdd = new MapNode(2,10,10);
		Section sectionToAdd = new Section(1,2,"RoadName",1000,1000);
		graph.addDestination(startNodeToAdd, sectionToAdd, destinationNodeToAdd);
		assertEquals(sectionToAdd,graph.getEdge(startNodeToAdd, destinationNodeToAdd));
	}
	
	@Test
	public void getEdge1() {
		MapNode firstNode = new MapNode(1,0,0);
		MapNode secondNode = new MapNode (2,0,0);
		assertEquals(null,graph.getEdge(firstNode, secondNode));
	}
	
	@Test
	public void getDestination1() {
		MapNode firstNode = new MapNode(1,0,0);
		assertEquals(null,graph.getDestinations(firstNode));
	}
	
	
	
	

}
