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
import org.junit.Test;


public class ModelTest1 {
	private Tour tour;
	private GraphDeliveryManager graph1;
	
	@Test
	public void getNearestDeliveryPointIdTest1() throws Throwable
	{
		model.engine.Pair<Integer, Integer> nearestDeliveryPointId = null;
		nearestDeliveryPointId = this.tour.getNearestDeliveryPointId(0);		
		if (nearestDeliveryPointId.getFirst()!=0){
			throw new Throwable("The nearest element before the delivery point id is "+nearestDeliveryPointId.getFirst()+", it should be 0.");
		}
		if (nearestDeliveryPointId.getSecond()!=1){
			throw new Throwable("The nearest element after the delivery point id is "+nearestDeliveryPointId.getSecond()+", it should be 31");
		}
	}
	
	@Test
	public void getNearestDeliveryPointIdTest2() throws Throwable
	{
		model.engine.Pair<Integer, Integer> nearestDeliveryPointId = null;
		nearestDeliveryPointId = this.tour.getNearestDeliveryPointId(2);		
		if (nearestDeliveryPointId.getFirst()!=0){
			throw new Throwable("The nearest element before the delivery point id is "+nearestDeliveryPointId.getFirst()+", it should be 0.");
		}
		if (nearestDeliveryPointId.getSecond()!=3){
			throw new Throwable("The nearest element after the delivery point id is "+nearestDeliveryPointId.getSecond()+", it should be 3.");
		}
	}
	
	@Test
	public void getNearestDeliveryPointIdTest3() throws Throwable
	{
		model.engine.Pair<Integer, Integer> nearestDeliveryPointId = null;
		nearestDeliveryPointId = this.tour.getNearestDeliveryPointId(4);		
		if (nearestDeliveryPointId.getFirst()!=2){
			throw new Throwable("The nearest element before the delivery point id is "+nearestDeliveryPointId.getFirst()+", it should be 2.");
		}
		if (nearestDeliveryPointId.getSecond()!=5){
			throw new Throwable("The nearest element after the delivery point id is "+nearestDeliveryPointId.getSecond()+", it should be 5.");
		}
	}
	
	@Test
	public void getNearestDeliveryPointIdTest4() throws Throwable
	{
		model.engine.Pair<Integer, Integer> nearestDeliveryPointId = null;
		nearestDeliveryPointId = this.tour.getNearestDeliveryPointId(6);		
		if (nearestDeliveryPointId.getFirst()!=4){
			throw new Throwable("The nearest element before the delivery point id is "+nearestDeliveryPointId.getFirst()+", it should be 4.");
		}
		if (nearestDeliveryPointId.getSecond()!=6){
			throw new Throwable("The nearest element after the delivery point id is "+nearestDeliveryPointId.getSecond()+", it should be 6.");
		}
	}
	
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
			tour.deleteDeliveryPoint(1, graph1);
		} catch (Throwable e) {
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
		MapNode node1 = new MapNode(1,0,-1);
		MapNode node2 = new MapNode(2,1,-1);
		MapNode node3 = new MapNode(3,2,-1);
		MapNode node4 = new MapNode(4,1,2);
		MapNode node5 = new MapNode(5,1,3);
		MapNode node6 = new MapNode(6,1,4);
		MapNode node7 = new MapNode(7,1,5);

		ArrayList<MapNode> nodes = new ArrayList<MapNode>();
		nodes.add(node1);
		nodes.add(node2);
		nodes.add(node3);
		nodes.add(node4);
		nodes.add(node5);
		nodes.add(node6);
		nodes.add(node7);

		Graph<MapNode, Section> graph = new Graph<MapNode, Section>();
		graph.add(node1);
		graph.add(node2);
		graph.add(node3);
		graph.add(node4);
		graph.add(node5);
		graph.add(node6);
		graph.add(node7);
		Section section12 = new Section(1,2,"12",10,1);
		Section section23 = new Section(2,3,"23",10,1);
		Section section34 = new Section(3,4,"34",10,1);
		Section section45 = new Section(4,5,"45",10,1);
		Section section56 = new Section(5,6,"56",10,1);
		Section section67 = new Section(6,7,"67",10,1);
		Section section71 = new Section(7,1,"71",10,1);
		Section section61 = new Section(6,1,"61",10,1);
		
		graph.addDestination(node1, section12, node2);
		graph.addDestination(node2, section23, node3);
		graph.addDestination(node3, section34, node4);
		graph.addDestination(node4, section45, node5);
		graph.addDestination(node5, section56, node6);
		graph.addDestination(node6, section67, node7);
		graph.addDestination(node7, section71, node1);
		graph.addDestination(node6, section61, node1);

		ArrayList<DeliveryPoint> deliveryPoints = new ArrayList<DeliveryPoint>();
		Delivery deliveryNode1 = new Delivery(1,node1,1,new Date(),new Date());
		Delivery deliveryNode3 = new Delivery(3,node3,1,new Date(),new Date());
		Delivery deliveryNode5 = new Delivery(5,node5,1,new Date(),new Date());
		Delivery deliveryNode7 = new Delivery(7,node7,1,new Date(),new Date());

		DeliveryPoint deliveryPointNode1 = new DeliveryPoint(deliveryNode1,new Date());
		DeliveryPoint deliveryPointNode3 = new DeliveryPoint(deliveryNode3,new Date());
		DeliveryPoint deliveryPointNode5 = new DeliveryPoint(deliveryNode5,new Date());
		DeliveryPoint deliveryPointNode7 = new DeliveryPoint(deliveryNode7,new Date());

		deliveryPoints.add(deliveryPointNode1);
		deliveryPoints.add(deliveryPointNode3);
		deliveryPoints.add(deliveryPointNode5);
		deliveryPoints.add(deliveryPointNode7);

		ArrayList<Section> sections = new ArrayList<Section>();
		sections.add(section12);
		sections.add(section23);
		sections.add(section34);
		sections.add(section45);
		sections.add(section56);
		sections.add(section67);
		sections.add(section71);

<<<<<<< HEAD
		//Set Up Tour		
		Tour tour = new Tour(sections, deliveryPoints, 1);
=======
		//Set Up Tour
		
		Tour tour = new Tour(sections, deliveryPoints, 1,1);
>>>>>>> branch 'master' of https://github.com/FrancoisChastel/Delivery-Manager.git
		
		this.tour = tour;
		this.graph1 = new GraphDeliveryManager(new Model(null));
		this.graph1.setGraph(graph);
		this.graph1.setNodes(nodes);
		this.graph1.setSections(sections);
		
	}
}
	