package tests.unitaires.model;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;

import model.Model;
import model.Tour;
import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryPoint;
import model.graph.Graph;
import model.graph.GraphDeliveryManager;
import model.graph.MapNode;
import model.graph.Section;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModelTest1 {
	private Tour tour;
	private GraphDeliveryManager graph1;
	
	@Test
	public void deleteDeliveryPointTest()
	{
		System.out.println("Avant delete");
		ArrayList<Section> a = tour.getSections();
		for(int i = 0 ; i < a.size() ; i++)
		{
			System.out.println("id du troncon = "+a.get(i).getId());
		}
		try {
			tour.deleteDeliveryPoint(4, graph1);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Apres delete");
		ArrayList<Section> sect = tour.getSections();
		for(int i = 0 ; i < a.size() ; i++)
		{
			int id = sect.get(i).getId();
			System.out.println("id du troncon = "+id);
		}
	}
	
	
	@Before
	public void setUp() {
		MapNode node0 = new MapNode(0,0,0);
		MapNode node1 = new MapNode(1,0,-1);
		MapNode node2 = new MapNode(2,1,-1);
		MapNode node3 = new MapNode(3,2,-1);
		MapNode node4 = new MapNode(4,1,0);
		ArrayList<MapNode> nodes = new ArrayList<MapNode>();
		nodes.add(node0);
		nodes.add(node1);
		nodes.add(node2);
		nodes.add(node3);
		nodes.add(node4);
		Graph<MapNode, Section> graph = new Graph<MapNode, Section>();
		graph.add(node0);
		graph.add(node1);
		graph.add(node2);
		graph.add(node3);
		graph.add(node4);
		Section section01 = new Section(0,1,"01",10,1);
		Section section12 = new Section(1,2,"12",10,1);
		Section section23 = new Section(2,3,"23",10,1);
		Section section32 = new Section(3,2,"23",10,1);
		Section section24 = new Section(2,4,"24",10,1);
		Section section20 = new Section(2,0,"20",10,1);
		Section section40 = new Section(4,0,"40",10,1);
		graph.addDestination(node0, section01, node1);
		graph.addDestination(node1, section12, node2);
		graph.addDestination(node2, section23, node3);
		graph.addDestination(node3, section32, node2);
		graph.addDestination(node2, section24, node4);
		graph.addDestination(node2, section20, node0);
		graph.addDestination(node4, section40, node0);
		
		ArrayList<DeliveryPoint> deliveryPoints = new ArrayList<DeliveryPoint>();
		Delivery deliveryNode3 = new Delivery(0,node3,1,new Date(),new Date());
		DeliveryPoint deliveryPointNode3 = new DeliveryPoint(deliveryNode3,new Date());
		Delivery deliveryNode4 = new Delivery(4,node4,1,new Date(),new Date());
		DeliveryPoint deliveryPointNode4 = new DeliveryPoint(deliveryNode4,new Date());
		deliveryPoints.add(deliveryPointNode3);
		deliveryPoints.add(deliveryPointNode4);
		
		ArrayList<Section> sections = new ArrayList<Section>();
		sections.add(section01);
		sections.add(section12);
		sections.add(section23);
		sections.add(section32);
		sections.add(section24);
		sections.add(section40);
		//Set Up Tour
		
		
		
		Tour tour = new Tour(sections, deliveryPoints, 0);
		
		this.tour = tour;
		this.graph1 = new GraphDeliveryManager(new Model(null));
		ArrayList<Section> s =this.graph1.getSectionList();
		s = sections;
		
		Graph<MapNode, Section> g =this.graph1.getGraph();
		g = graph;
		
		ArrayList<MapNode> m = this.graph1.getNodeList();
		m = nodes;
		
	}
}
	