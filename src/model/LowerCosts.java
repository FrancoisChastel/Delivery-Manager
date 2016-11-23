package model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class LowerCosts {
	HashMap<MapNode,HashMap<MapNode,Double>> costs;
	DeliveryOrder tour;
	Graph<MapNode, Section> graph;
	HashMap<MapNode,HashMap<MapNode,ArrayList<MapNode>>> paths;
	int costsMatrix[][];
	
	public LowerCosts(Graph<MapNode, Section> graph, DeliveryOrder tour)
	{
		this.tour=tour;
		this.graph=graph;
		costs = new HashMap<>();
		paths = new HashMap<>();
		int numberOfDeliveries = tour.getDeliveryList().size();
		costsMatrix = new int[numberOfDeliveries][numberOfDeliveries];
		generateCosts();
	}
	/*
	 * Generate square Matrix with the delivery points as indexes, diagonal empty
	 */
	public void generateCosts()
	{
		//Add starting point as a delivery 
		
		tour.getDeliveryList().add(new Delivery(0,tour.getStoreAdress(),0,null,null));
		
		//Dijkstra for each node
		for(int i=0;i<tour.getDeliveryList().size();i++)
		{
			//System.out.println(tour.getDeliveryList().get(i).getAdress().getidNode());
			MapNode beginning = tour.getDeliveryList().get(i).getAdress();
			
			HashMap<MapNode,MapNode> predecessor = new HashMap<>();
			
			ArrayList<MapNode> nodesThrough = new ArrayList<>();
			
			HashMap<MapNode, Double> nodesCost = new HashMap<>();
			nodesCost.put(beginning, (double) 0);
			
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
							predecessor.put(target, origin);
				            nodesCost.put(target, (distPath+distOrigin));
				            nodesList.add(target);
						}
					}
				}
				nodesThrough.add(origin);
			}
			costs.put(beginning, nodesCost);
			HashMap<MapNode, ArrayList<MapNode>> pathTargets = new HashMap<>();
			for(int j=0;j<tour.getDeliveryList().size();j++)
			{
				if(!beginning.equals(tour.getDeliveryList().get(j).getAdress()))
				{
					ArrayList<MapNode> path = getBestPath(predecessor,beginning, tour.getDeliveryList().get(j).getAdress(),new ArrayList<MapNode>());
					Collections.reverse(path);
					path.remove(0);
					path.remove(path.size()-1);
					pathTargets.put(tour.getDeliveryList().get(j).getAdress(),path);
					paths.put(tour.getDeliveryList().get(i).getAdress(), pathTargets);
				}
				
				/*for(int n=0;n<path.size();n++)
				{
					System.out.print(path.get(n).getidNode() + " ");
				}
				System.out.println("");*/
			}
			
		}
		
		for(Entry<MapNode, HashMap<MapNode,Double>> entry : costs.entrySet())
		{
			for(Entry<MapNode, Double> entryNode : entry.getValue().entrySet())
			{
				try{
				costsMatrix[tour.getDeliveryList().indexOf(entry)][tour.getDeliveryList().indexOf(entryNode)] = (int) Math.floor(entryNode.getValue());
				}
				catch (Exception e) {
					//System.out.println("The node is not a delivery Point");
				}
			}
			
		}
	}
	
	
	public ArrayList<MapNode> getBestPath(HashMap<MapNode,MapNode> predecessor, MapNode origin, MapNode target, ArrayList<MapNode> path)
	{
		path.add(target);
		if(target.equals(origin))
		{
			return path;
		}
		else
		{
			if(predecessor.containsKey(target))
			{
				getBestPath(predecessor, origin, predecessor.get(target),path);
			}
			else
			{
				return path;
			}
		}
		return path;
	}
	public int[][] getCostsMatrix() {
		return costsMatrix;
	}
	public void setCostsMatrix(int[][] costsMatrix) {
		this.costsMatrix = costsMatrix;
	}
	public HashMap<MapNode, HashMap<MapNode, ArrayList<MapNode>>> getPaths() {
		return paths;
	}
	public void setPaths(HashMap<MapNode, HashMap<MapNode, ArrayList<MapNode>>> paths) {
		this.paths = paths;
	}
	
	
	
}
