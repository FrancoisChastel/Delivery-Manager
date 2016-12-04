package model.traceroute;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import model.Tour;
import model.graph.Graph;
import model.graph.MapNode;
import model.graph.Section;
import model.vector.MathVector;

/**
 * 
 * @author C.APARICIO && F.CHASTEL
 *
 */
public abstract class TraceRoute {
	/**
	 * Step 3 of the engine. Create RoadMap
	 * This method create a Road Map with a Tour present in Model
	 * @param model
	 */
	
	public static List<Instruction> generateInstructions(Tour tour, Graph<MapNode, Section> map,List<Instruction> instructions)
	{
		
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
			List <Integer> deleveryPoint = Arrays.asList(tour.getDeliveryPoints());
			
			//Compute New Vector
			destination.redifineVector(oldX, oldY, nextNode.getX(),  nextNode.getY());
					
			instructions.add(instruction(origin, destination, 
								section.getName(),section.getLength(),section.getIdOrigin(), section.getIdDestination(), 
								contains(tour.getDeliveryPoints(), section.getIdDestination()),
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
	
	private static boolean contains(Integer[] integers, Integer target)
	{
		for (Integer integer : integers)
			if (integer.equals(target)) return true;
		return false;
	}
	
	private static Instruction instruction(MathVector origin, MathVector destination, 
			String roadName, Integer length,
			int idOrigin, int idDestination,
			boolean isDeliveryPoint, Set<MapNode> nodesDestination)
	{
		double angle = origin.getAngle(destination);
		int counter = 1;
		if(Math.abs(angle) < 5)
		{
			return new Instruction(Direction.STRAIGHT, counter,length,idDestination,isDeliveryPoint, roadName);
		}
		else if(Math.abs(angle) == 180)
		{
			return new Instruction(Direction.TURNAROUND, counter,length,idDestination,isDeliveryPoint,roadName);
		}
		else if(angle < 0)
		{
			MathVector tmp = new MathVector();
			for(MapNode node : nodesDestination)
			{
				tmp.redifineVector(destination.getX1(), destination.getY1(), node.getX(), node.getY());
				double nodeAngle = origin.getAngle(tmp);
				if( nodeAngle < angle && !(tmp.getX2() == origin.getX1() && tmp.getY2() == origin.getY1()))
				{
					counter++;
				}
			}
			return new Instruction(Direction.LEFT, counter,length,idDestination,isDeliveryPoint, roadName);
		}
		else
		{
			MathVector tmp = new MathVector();
			for(MapNode node : nodesDestination)
			{
				tmp.redifineVector(destination.getX1(), destination.getY1(), node.getX(), node.getY());
				double nodeAngle = origin.getAngle(tmp);
				if( nodeAngle > angle && !(tmp.getX2() == origin.getX1() && tmp.getY2() == origin.getY1()))
				{
					counter++;
				}
			}
			return new Instruction(Direction.RIGHT, counter,length,idDestination,isDeliveryPoint, roadName);
		}		
	}
}