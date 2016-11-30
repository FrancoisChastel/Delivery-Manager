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

public class LowerCosts {
	private Model model;	
	DeliveryOrder delOrder;
	ArrayList<MapNode> deliveryNodes= new ArrayList<MapNode>();
	Graph<MapNode, Section> graph;
	HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> paths;
	int costsMatrix[][];
	
	/**
	 * Normal Constructor.
	 * @param model
	 */
	public LowerCosts(Model model)
	{
		this.model = model;
		this.graph = model.getGraphDeliveryManager().getGraph();
		delOrder = model.getDeliveryManager().getDeliveryOrder();
		paths = new HashMap<>();
		
		int numberOfDeliveries = delOrder.getDeliveryList().size()+1;
		costsMatrix = new int[numberOfDeliveries][numberOfDeliveries];
		deliveryNodes.add(delOrder.getStoreAdress());
		
		for(int i=0;i<delOrder.getDeliveryList().size();i++)
		{
			deliveryNodes.add(delOrder.getDeliveryList().get(i).getAdress());
		}
	}
	
	/**
	 * Generate square Matrix with the delivery points as indexes, diagonal empty
	 */
	public void generateCosts()
	{
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
				Pair<ArrayList<MapNode>,Integer> dijkstra = this.dijkstra(beginning,deliveryNodes.get(0));
				paths.get(beginning).add(dijkstra);
				deliveryNodes.remove(0);
			}
		}
	}
	
	/**
	 * Dijkstra
	 * @param origin
	 * @param target
	 * @return
	 */
	public Pair<ArrayList<MapNode>,Double> dijkstra2 (MapNode origin, MapNode target)
	{
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
	
	public Pair<ArrayList<MapNode>, Integer> dijkstra (MapNode origin, MapNode target)
	{
		GraphDeliveryManager graphe = model.getGraphDeliveryManager();

		
		HashMap<MapNode,Integer> couts = new HashMap<>();
		HashMap<MapNode,MapNode> pi = new HashMap<>();
		
		ArrayList<MapNode> blackNodes = new ArrayList<>();
		ArrayList<MapNode> greyNodes = new ArrayList<>();
		ArrayList<MapNode> whiteNodes = new ArrayList<>();
		
		// On initialise les couts et la liste de noeuds blancs
		for(MapNode m : graphe.getNodeList())
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
					relacher(si,sj,couts,pi);
					
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
	public MapNode getSommetMinimal(ArrayList<MapNode> Sommets, HashMap<MapNode,Integer> couts)
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
	public void relacher(MapNode si, MapNode sj, HashMap<MapNode,Integer> couts, HashMap<MapNode,MapNode> pi)
	{
		Section secIJ = graph.getEdge(si, sj);
		int coutIJ = (secIJ.getLength()/secIJ.getSpeed());
		
		if(couts.get(sj)>couts.get(si)+coutIJ)
		{
			couts.put(sj, couts.get(si)+coutIJ);
			pi.put(sj, si);
		}
	}
	
	
	public int[][] getCostsMatrix() {
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
	
	public void printCost(){
		int IDi, IDj;		
		for(Entry<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> entry : paths.entrySet())
		{			
			IDi = entry.getKey().getidNode();
			
			for(Pair<ArrayList<MapNode>,Integer> pair : entry.getValue())
			{
				ArrayList<MapNode> list = pair.getFirst();				
				IDj = list.get(list.size()-1).getidNode();
				
				// get the cost of the pair
				int cost =  (pair.getSecond()).intValue();
				
				System.out.print("<"+IDi+","+IDj+"> = " +cost + " : ");
				for(MapNode map : list)
				{
					System.out.print(" - " + map.getidNode()  + " - ");
				}
				System.out.println("");
			}				
		}		
	}
	
	public void refresh()
	{
		this.graph = model.getGraphDeliveryManager().getGraph();
		delOrder = model.getDeliveryManager().getDeliveryOrder();
		paths = new HashMap<>();
		
		int numberOfDeliveries = delOrder.getDeliveryList().size()+1;
		costsMatrix = new int[numberOfDeliveries][numberOfDeliveries];
		deliveryNodes.add(delOrder.getStoreAdress());
		
		for(int i=0;i<delOrder.getDeliveryList().size();i++)
		{
			deliveryNodes.add(delOrder.getDeliveryList().get(i).getAdress());
		}
	}
	
	public void setCostsMatrix(int[][] costsMatrix) {
		this.costsMatrix = costsMatrix;
	}
	public HashMap<MapNode, ArrayList<Pair<ArrayList<MapNode>, Integer>>> getPaths() {
		return paths;
	}
	public void setPaths(HashMap<MapNode, ArrayList<Pair<ArrayList<MapNode>, Integer>>> paths) {
		this.paths = paths;
	}	
	
}
