package model.traceroute;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import model.Tour;
import model.deliverymanager.DeliveryPoint;
import model.graph.Graph;
import model.graph.MapNode;
import model.graph.Section;
import model.vector.MathVector;

/**
 * @author C.APARICIO && F.CHASTEL
 * This class provides a service for generate a list of instruction according to a Tour and a Graph of MapNode and Section
 * All Instructions contain essential informations ready to be displayed for the deliverer.
 */
public abstract class TraceRoute {
	
	/**
	 * This method will translate a list of sections passed as parameter in list of instructions object including essential
	 * information.
	 * @param tour : To get all sections in a tour
	 * @param map : To get MapNodes for each IdOrigin and IdDestination in a section
	 * @return List of Instructions more easily understandable by a human and ready to be printed.
	 */
	public static List<Instruction> generateInstructions(Tour tour, Graph<MapNode, Section> map)
	{
		List<Instruction> instructions = new LinkedList<Instruction>();
		int oldX,oldY;
		MathVector origin = new MathVector();
		MathVector destination = new MathVector();

		MapNode nextNode;
		HashMap <MapNode,Section> allDestinations;
		
		oldX = map.getNodeById(tour.getEntrepotId()).getX();
		oldY = map.getNodeById(tour.getEntrepotId()).getY();
		for(Section section : tour.getSections())
		{
			nextNode        = map.getNodeById(section.getIdDestination());
			allDestinations = map.getDestinations(map.getNodeById(section.getIdOrigin()));
			boolean uniqueOutgoingDestination = false;
			if(allDestinations.size() == 2 || allDestinations.size() == 1)
			{
				uniqueOutgoingDestination = true;
			}
			//List <Integer> deleveryPoint = Arrays.asList(tour.getDeliveryPoints());

			//Compute New Vector
			destination.redifineVector(oldX, oldY, nextNode.getX(),  nextNode.getY());
					
			instructions.add(instruction(origin, destination, 
								section.getName(),section.getLength(), section.getIdDestination(), 
								contains(tour.getDeliveryPoints(), section.getIdDestination()),uniqueOutgoingDestination,
								allDestinations.keySet()));
			/*
			System.out.println(instruction(origin, destination, 
					section.getName(),section.getLength(),section.getIdOrigin(), section.getIdDestination(), 
					contains(tour.getDeliveryPoints(), section.getIdDestination()),
					allDestinations.keySet()).toString());
			*/
			// Change Vector and old point
			oldX = nextNode.getX();
			oldY = nextNode.getY();
			
			// Change Vector and old point
			origin.redifineVector(destination.getX1(), destination.getY1(), destination.getX2(), destination.getY2());
			
		}
		
		return instructions;
	}
	
	/**
	 * See if a IdNode is present in a list of delivery Points
	 * @param deliveryPoints : List of delivery Points where it's possible to know MapNodes that compose it
	 * @param target IdNode to see if it is present
	 * @return True if target is in deliveryPoints or false in the other case
	 */
	private static boolean contains(List<DeliveryPoint> deliveryPoints, Integer target)
	{
		for (DeliveryPoint deliveryPoint : deliveryPoints)
			if (deliveryPoint.getMapNodeId() == target) return true;
		return false;
	}
	
	/** Converts two sections symbolized by a vector each to a unique instruction including essential information
	 * @param origin : Vector Origin
	 * @param destination : Vector Destination
	 * @param roadName : To know the road name of the destination vector
	 * @param length : To know the length of the destination vector
	 * @param idDestination : To know the Id of MapNode of the destination vector
	 * @param isDeliveryPoint : To know if the Destination MapNode is a delivery Point.
	 * @param uniqueOutgoingDestination : To know if there is a unique vector destination a MapNode
	 * @param nodesDestination : Set of MapNode reachable to create temporary Vector
	 * @return Instruction object including essential information
	 */
	private static Instruction instruction(MathVector origin, MathVector destination, 
			String roadName, Integer length, int idDestination,
			boolean isDeliveryPoint,boolean uniqueOutgoingDestination, Set<MapNode> nodesDestination)
	{
		double angle = origin.getAngle(destination);
		int counter = 1; // If counter = 1, Turn at the first direction, counter = second, Turn at the second direction....
		int nodeInArea =0; // If at the end = 1, the vector is alone in its area
		boolean uniqueOutgoingDestinationInItsArea = false;
		// Go Straight
		if(Math.abs(angle) < 10)
		{
			return new Instruction(Direction.STRAIGHT, counter,length,idDestination,isDeliveryPoint, roadName,uniqueOutgoingDestination,true);
		}
		// Go Turnaroud
		else if(Math.abs(angle) == 180)
		{
			return new Instruction(Direction.TURNAROUND, counter,length,idDestination,isDeliveryPoint, roadName,uniqueOutgoingDestination,true);
		}
		// Turn Left
		else if(angle < 0)
		{
			MathVector tmp = new MathVector();
			for(MapNode node : nodesDestination)
			{
				tmp.redifineVector(destination.getX1(), destination.getY1(), node.getX(), node.getY());
				double nodeAngle = origin.getAngle(tmp);
				// To know if the left vector is alone in his area
				if(nodeAngle < -10)
				{
					nodeInArea++;
				}
				// To increment the number of vector present before him in relation to the normal
				if( nodeAngle < angle && !(tmp.getX2() == origin.getX1() && tmp.getY2() == origin.getY1()))
				{
					counter++;
				}
			}
			if(nodeInArea == 1)
			{
				uniqueOutgoingDestinationInItsArea = true;
			}
			return new Instruction(Direction.LEFT, counter,length,idDestination,isDeliveryPoint,roadName,uniqueOutgoingDestination,uniqueOutgoingDestinationInItsArea);
		}
		else
		{
			MathVector tmp = new MathVector();
			for(MapNode node : nodesDestination)
			{
				tmp.redifineVector(destination.getX1(), destination.getY1(), node.getX(), node.getY());
				double nodeAngle = origin.getAngle(tmp);
				// To know if the right vector is alone in his area
				if(nodeAngle > 10)
				{
					nodeInArea++;
				}
				// To increment the number of vector present before him in relation to the normal
				if( nodeAngle > angle && !(tmp.getX2() == origin.getX1() && tmp.getY2() == origin.getY1()))
				{
					counter++;
				}
			}
			if(nodeInArea == 1)
			{
				uniqueOutgoingDestinationInItsArea = true;
			}
			return new Instruction(Direction.RIGHT, counter,length,idDestination,isDeliveryPoint,roadName,uniqueOutgoingDestination,uniqueOutgoingDestinationInItsArea);
		}		
	}
}