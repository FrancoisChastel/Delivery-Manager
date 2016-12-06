package tests.unitaires.traceroute;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import model.Tour;
import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryPoint;
import model.graph.Graph;
import model.graph.MapNode;
import model.graph.Section;
import model.traceroute.Direction;
import model.traceroute.Instruction;
import model.traceroute.TraceRoute;

public class TraceRouteTest {


	
	@Test
	public void generateInstructionsTest1() {
		
		MapNode node0 = new MapNode(0,0,0);
		MapNode node1 = new MapNode(1,0,-1);
		MapNode node2 = new MapNode(2,1,-1);
		MapNode node3 = new MapNode(3,2,-1);
		MapNode node4 = new MapNode(4,1,0);
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
		
		List<Instruction> instructions = TraceRoute.generateInstructions(tour,graph);
		Instruction instruction0 = new Instruction(Direction.STRAIGHT,1,10,1,false,"01",true,true);
		Instruction instruction1 = new Instruction(Direction.RIGHT,1,10,2,false,"12",true,true);
		Instruction instruction2 = new Instruction(Direction.STRAIGHT,1,10,3,true,"23",false,true);
		Instruction instruction3 = new Instruction(Direction.TURNAROUND,1,10,2,false,"23",true,true);
		Instruction instruction4 = new Instruction(Direction.LEFT,1,10,4,true,"24",false,false);
		Instruction instruction5 = new Instruction(Direction.RIGHT,1,10,0,false,"40",true,true);
		assertEquals(instructions.get(0),instruction0);
		assertEquals(instructions.get(1),instruction1);
		assertEquals(instructions.get(2),instruction2);
		assertEquals(instructions.get(3),instruction3);
		assertEquals(instructions.get(4),instruction4);
		assertEquals(instructions.get(5),instruction5);
	}
	
	@Test
	public void generateInstructionsTestWithoutSection1() {
		
		MapNode node0 = new MapNode(0,0,0);
		Graph<MapNode, Section> graph = new Graph<MapNode, Section>();
		graph.add(node0);
		ArrayList<Section> sections = new ArrayList<Section>();
		ArrayList<DeliveryPoint> deliveryPoints = new ArrayList<DeliveryPoint>();
		
		Tour tour = new Tour(sections, deliveryPoints, 0);
		assertEquals(TraceRoute.generateInstructions(tour,graph).isEmpty(),true);
		
	}

}
