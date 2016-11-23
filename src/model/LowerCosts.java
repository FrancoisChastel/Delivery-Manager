package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class LowerCosts {
	DeliveryOrder tour;
	ArrayList<MapNode> deliveryNodes= new ArrayList<MapNode>();
	Graph<MapNode, Section> graph;
	HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Double>>> paths;
	int costsMatrix[][];
	
	public LowerCosts(Graph<MapNode, Section> graph, DeliveryOrder tour)
	{
		this.tour=tour;
		this.graph=graph;
		paths = new HashMap<>();
		int numberOfDeliveries = tour.getDeliveryList().size()+1;
		costsMatrix = new int[numberOfDeliveries][numberOfDeliveries];
		deliveryNodes.add(tour.getStoreAdress());
		for(int i=0;i<tour.getDeliveryList().size();i++)
		{
			deliveryNodes.add(tour.getDeliveryList().get(i).getAdress());
		}
		generateCosts();
	}
	/*
	 * Generate square Matrix with the delivery points as indexes, diagonal empty
	 */
	public void generateCosts()
	{
		//Init HashMap with nearly infinites
		
		tour.getDeliveryList().add(0,new Delivery(0,tour.getStoreAdress(),0,null,null));
		
		//Dijkstra for each node
		for(int i=0;i<tour.getDeliveryList().size();i++)
		{
			MapNode beginning = tour.getDeliveryList().get(i).getAdress();
			

			ArrayList<MapNode> deliveryNodes = new ArrayList<>();

			for(int j=0;j<tour.getDeliveryList().size();j++)
			{
				if(!tour.getDeliveryList().get(j).getAdress().equals(beginning))
				{
					deliveryNodes.add(tour.getDeliveryList().get(j).getAdress());
				}
			}
			
			paths.put(beginning, new ArrayList<Pair<ArrayList<MapNode>,Double>>());
			while(!deliveryNodes.isEmpty())
			{
				//Appel dijkstra
				Pair<ArrayList<MapNode>,Double> dijkstra = this.dijkstra(beginning,deliveryNodes.get(0));
				paths.get(beginning).add(dijkstra);
				deliveryNodes.remove(0);
			}
		}
	}
	
	
	
	public Pair<ArrayList<MapNode>,Double> dijkstra (MapNode origin, MapNode target)
	{
		HashMap<MapNode,Pair<Double,MapNode>> weightTemp = new HashMap<>();
		HashMap<MapNode, Pair<ArrayList<MapNode>,Double>> dijkstraTemp = new HashMap<>(); 
		weightTemp.put(origin, new Pair<Double, MapNode>(0.0,origin));
		dijkstraTemp.put(origin,new Pair<ArrayList<MapNode>,Double>(new ArrayList<>(),0.0));
		Boolean bestPathTarget = false;
		HashMap<MapNode,Integer> nodesVisited = new HashMap<>();
		while(!bestPathTarget)
		{
			//Select shortest node path in weightTemp for the new origin
			double min = Double.MAX_VALUE;
			MapNode oldOrigin = origin;
			for(Entry<MapNode,Pair<Double,MapNode>> entry : weightTemp.entrySet())
			{
				if(entry.getValue().getFirst()<min)
				{
					origin = entry.getKey();
					min=entry.getValue().getFirst();
				}
			}
			//Update dijkstraTemp by adding the path
			ArrayList<MapNode> pathTemp = new ArrayList<>();
			pathTemp = dijkstraTemp.get(oldOrigin).getFirst();
			pathTemp.add(origin);
			dijkstraTemp.put(origin, new Pair<ArrayList<MapNode>,Double>(pathTemp,min));
			
			nodesVisited.put(oldOrigin, 0);
			
			weightTemp.remove(origin);
			
			//Stop condition
			if(dijkstraTemp.containsKey(target))
			{
				bestPathTarget=true;
			}
			
			//Update weightTemp with the new origin
			HashMap<MapNode,Section> destinations = graph.getDestinations(origin);
			for(Entry<MapNode, Section> entry : destinations.entrySet())
			{
				if(!nodesVisited.containsKey(entry.getKey()))
				{
					double weight = (entry.getValue().getLength()/entry.getValue().getSpeed()) + min;
					if(weightTemp.containsKey(entry.getKey()))
					{ 
						
						if(weightTemp.get(entry.getKey()).getFirst()<weight)
						{
							weightTemp.put(entry.getKey(), new Pair<Double,MapNode>(weight,origin));
						}
					}
					else
					{
						weightTemp.put(entry.getKey(), new Pair<Double,MapNode>(weight,origin));
					}
				}
			}
		}
		return dijkstraTemp.get(target);
	}
	
	public int[][] getCostsMatrix() {
		return costsMatrix;
	}
	public void setCostsMatrix(int[][] costsMatrix) {
		this.costsMatrix = costsMatrix;
	}
	public HashMap<MapNode, ArrayList<Pair<ArrayList<MapNode>, Double>>> getPaths() {
		return paths;
	}
	public void setPaths(HashMap<MapNode, ArrayList<Pair<ArrayList<MapNode>, Double>>> paths) {
		this.paths = paths;
	}
	
	
	
	
}
