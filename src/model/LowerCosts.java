package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class LowerCosts {
	
	/*
	 * Generate square Matrix with the delivery points as indexes, diagonal empty
	 */
	public static int[][] generateCosts(Graph<MapNode, Section> graph, DeliveryOrder tour)
	{
		int numberOfDeliveries = tour.getDeliveryList().size();
		//int costs[][] = new int[numberOfDeliveries][numberOfDeliveries];
		HashMap<MapNode,HashMap<MapNode,Double>> costs = new HashMap<>();
		//Init HashMap with nearly infinites
		
		
		
		//Dijkstra for each node
		for(int i=0;i<tour.getDeliveryList().size();i++)
		{
			MapNode beginning = tour.getDeliveryList().get(i).getAdress();
			
			ArrayList<MapNode> nodesThrough = new ArrayList<>();
			
			HashMap<MapNode, Double> nodesCost = new HashMap<>();
			nodesCost.put(beginning, (double) 0);
			//costs.put(beginning, nodesCost);
			
			ArrayList<MapNode> nodesList = new ArrayList<>();
			nodesList.add(beginning);
			
			while(!nodesList.isEmpty())
			{
				MapNode origin = nodesList.get(0);
				HashMap<MapNode,Section> destinations = graph.getDestinations(origin);
				nodesList.remove(0);
				for(Entry<MapNode, Section> entry : destinations.entrySet())
				{
					if(!nodesThrough.contains(entry.getKey()))
					{
						MapNode target = entry.getKey();
						double distNode;
						if(nodesCost.containsKey(target))
						{
							 distNode = nodesCost.get(target).intValue();
						}
						else
						{
							distNode = 100000000;
						}
						double distOrigin = nodesCost.get(origin).intValue();
						double distPath = entry.getValue().getLength()/entry.getValue().getSpeed();
						if((distPath+distOrigin)<distNode)
						{
				            nodesCost.put(target, (distPath+distOrigin));
				            nodesList.add(target);
						}
					}
				}
				nodesThrough.add(origin);
			}
			costs.put(beginning, nodesCost);
			
		}
		
		for(Entry<MapNode, HashMap<MapNode,Double>> entry : costs.entrySet())
		{
			System.out.println("idNode : " + entry.getKey().getidNode());
			for(Entry<MapNode, Double> entryNode : entry.getValue().entrySet())
			{
				System.out.println("Durée node "+entryNode.getKey().getidNode() + " : " + entryNode.getValue().toString() + " s");
			}
			
		}
		
		return null;
	}
	
	
	
}
