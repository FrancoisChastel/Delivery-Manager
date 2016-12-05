package model.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import model.Model;
import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryOrder;
import model.graph.Graph;
import model.graph.GraphDeliveryManager;
import model.graph.MapNode;
import model.graph.Section;

public class LowerDistance {
	
	/**
	 * Generate square Matrix with the delivery points as indexes, diagonal empty
	 * @param GraphDeliveryManager graph
	 * @param DeliveryOrder delOrder
	 * @param HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> paths
	 */
	public static HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> generateCosts(GraphDeliveryManager graph, DeliveryOrder delOrder)
	{
		HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> paths = new HashMap<>();
		
		//Init HashMap with nearly infinites
		
		delOrder.getDeliveryList().add(0,new Delivery(0,delOrder.getStoreAdress(),0,null,null));
		
		//Dijkstra for each node
		for(int i=0;i<delOrder.getDeliveryList().size();i++)
		{
			MapNode beginning = delOrder.getDeliveryList().get(i).getAdress();
			

			ArrayList<MapNode> deliveryNodes = new ArrayList<>();

			for(int j=0;j<delOrder.getDeliveryList().size();j++)
			{
				if(!delOrder.getDeliveryList().get(j).getAdress().equals(beginning))
				{
					deliveryNodes.add(delOrder.getDeliveryList().get(j).getAdress());
				}
			}
			
			paths.put(beginning, new ArrayList<Pair<ArrayList<MapNode>,Integer>>());
			while(!deliveryNodes.isEmpty())
			{
				//Appel dijkstra
				Pair<ArrayList<MapNode>,Integer> dijkstra = dijkstra(graph,beginning,deliveryNodes.get(0));
				paths.get(beginning).add(dijkstra);
				deliveryNodes.remove(0);
			}
		}
		
		return paths;
	}
	
	/**
	 * Dijkstra
	 * @param GraphDeliveryManager graphManager
	 * @param MapNode origin
	 * @param MapNode target
	 * @return
	 */
	public static Pair<ArrayList<MapNode>,Double> dijkstra2 (GraphDeliveryManager graphManager, MapNode origin, MapNode target)
	{
		Graph<MapNode,Section> graph = graphManager.getGraph();
		
		HashMap<MapNode,Pair<Double,MapNode>> weightTemp = new HashMap<>();
		HashMap<MapNode, Pair<ArrayList<MapNode>,Double>> dijkstraTemp = new HashMap<>(); 
		weightTemp.put(origin, new Pair<Double, MapNode>(0.0,origin));
		dijkstraTemp.put(origin,new Pair<ArrayList<MapNode>,Double>(new ArrayList<>(),0.0));
		Boolean bestPathTarget = false;
		HashMap<MapNode,Integer> nodesVisited = new HashMap<>();
		ArrayList<MapNode> pathTemp;
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
					double weight = (entry.getValue().getLength() + min);
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
	
	/**
	 * 
	 * @param GraphDeliveryManager graphManager
	 * @param MapNode origin
	 * @param MapNode target
	 * @return
	 */
	public static Pair<ArrayList<MapNode>, Integer> dijkstra (GraphDeliveryManager graphManager, MapNode origin, MapNode target)
	{
		Graph<MapNode,Section> graph = graphManager.getGraph();
		
		HashMap<MapNode,Integer> couts = new HashMap<>();
		HashMap<MapNode,MapNode> pi = new HashMap<>();
		
		ArrayList<MapNode> blackNodes = new ArrayList<>();
		ArrayList<MapNode> greyNodes = new ArrayList<>();
		ArrayList<MapNode> whiteNodes = new ArrayList<>();
		
		// On initialise les couts et la liste de noeuds blancs
		for(MapNode m : graphManager.getNodeList())
		{
			couts.put(m, Integer.MAX_VALUE);
			whiteNodes.add(m);
		}
		
		// add s0 grey
		whiteNodes.remove(origin);
		greyNodes.add(origin);			
		couts.put(origin,0);
		
		// Boucle du parcous en profondeur
		while(!greyNodes.isEmpty())
		{
			
			// Recupération du sommet gris minimal
			MapNode si = getSommetMinimal(greyNodes,couts);
			
			// Pour chaque successeur de si
			HashMap<MapNode,Section> succSi = graph.getDestinations(si);

			for(Entry<MapNode,Section> entry: succSi.entrySet())
			{
				MapNode sj = entry.getKey();
				
				// Si le successeur est blanc
				if( whiteNodes.contains(sj) || greyNodes.contains(sj))
				{
					relacher(graph,si,sj,couts,pi);
					
					if(whiteNodes.contains(sj))
					{
						whiteNodes.remove(sj);
						greyNodes.add(sj);
					}
				}
			}
			greyNodes.remove(si);
			blackNodes.add(si);
		}
		
		ArrayList<MapNode> tmp = new ArrayList<MapNode>();
		MapNode predecesseur = target;
		tmp.add(predecesseur);
		
		while((predecesseur = pi.get(predecesseur))!=null)
		{
			tmp.add(predecesseur);
		}
		
		ArrayList<MapNode> path = new ArrayList<>();
		
		for(int i = tmp.size()-1;i>=0;i--)
		{
			path.add(tmp.get(i));
		}
		
		return new Pair<ArrayList<MapNode>,Integer>(path, couts.get(target));
	}
	
	/**
	 * Used for Djikstra
	 * @return
	 */
	public static MapNode getSommetMinimal(ArrayList<MapNode> Sommets, HashMap<MapNode,Integer> couts)
	{
		int min = couts.get(Sommets.get(0));
		MapNode minNode = Sommets.get(0);
		
		for(MapNode m : Sommets)
		{
			if(couts.get(m)<min)
			{
				min = couts.get(m);
				minNode = m;
			}
		}
		return minNode;		
	}
	
	/**
	 * Used for djikstra
	 * @param si
	 * @param sj
	 * @param couts
	 * @param pi
	 */
	public static void relacher(Graph<MapNode,Section> graph, MapNode si, MapNode sj, HashMap<MapNode,Integer> couts, HashMap<MapNode,MapNode> pi)
	{
		Section secIJ = graph.getEdge(si, sj);
		int coutIJ = (secIJ.getLength());
		
		if(couts.get(sj)>couts.get(si)+coutIJ)
		{
			couts.put(sj, couts.get(si)+coutIJ);
			pi.put(sj, si);
		}
	}
	
	
	public static int[][] getCostsMatrix(DeliveryOrder delOrder, HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> paths) {
		//delOrder = model.getDeliveryManager().getDeliveryOrders().get(0);
		
		//Add all delivery nodes to the arrayList
		ArrayList<MapNode> deliveryNodes = new ArrayList<>();
		deliveryNodes.add(delOrder.getStoreAdress());
		for(int i=0;i<delOrder.getDeliveryList().size();i++)
		{
			deliveryNodes.add(delOrder.getDeliveryList().get(i).getAdress());
		}
		
		int numberOfDeliveries = delOrder.getDeliveryList().size()+1;
		int costsMatrix[][] = new int[numberOfDeliveries][numberOfDeliveries];
		
		for(Entry<MapNode, ArrayList<Pair<ArrayList<MapNode>, Integer>>> origin : paths.entrySet())
		{
			if(deliveryNodes.indexOf(origin.getKey()) != -1)
			{
					for(Pair<ArrayList<MapNode>,Integer> temp : origin.getValue())
					{
						if(deliveryNodes.contains(  temp.getFirst().get(   temp.getFirst().size()-1  )  )  )
						{
							costsMatrix[deliveryNodes.indexOf(origin.getKey())][deliveryNodes.indexOf(temp.getFirst().get(   temp.getFirst().size()-1  ))] = (int) Math.floor(temp.getSecond());
						}
								
					}
			}
			
		}
		
		return costsMatrix;
	}
}
