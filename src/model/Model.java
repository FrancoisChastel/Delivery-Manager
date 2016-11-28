package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;

import controller.Controller;
import model.deliverymanager.DeliveryManager;
import model.deliverymanager.DeliveryOrder;
import model.engine.LowerCosts;
import model.engine.Pair;
import model.engine.TSP1;
import model.graph.Graph;
import model.graph.GraphDeliveryManager;
import model.graph.MapNode;
import model.graph.Section;
import model.parser.XmlParser; 

public class Model extends Observable implements IModel {

	private GraphDeliveryManager graphDelMan;
	private Tour tour;
	private XmlParser xmlParser;
	private DeliveryManager deliveryManager;
	private TSP1 tsp;
	private LowerCosts lowCosts;

	/**
	 * Normal constructor of the model
	 * @param controller
	 */
	public Model(Controller controller) {
		xmlParser 	= new XmlParser(this);
		graphDelMan = new GraphDeliveryManager(this);
		deliveryManager = new DeliveryManager();
	}

	/**
	 * This method call the parsing process of the parser
	 * @param currentFile
	 */
	public void parseMapFile(File currentFile) {
		xmlParser.xmlMapParser(currentFile);
		
		setChanged();
		notifyObservers("UPDATE_MAP");
	}

	// Accesseur
	public List<MapNode> getMapNodes() {	return graphDelMan.getNodeList();	}	
	public List<Section> getSections() {	return graphDelMan.getSectionList();}
	public GraphDeliveryManager getGraphDeliveryManager() { return graphDelMan; }
	public DeliveryManager getDeliveryManager() { return deliveryManager; }
	public LowerCosts getLowerCosts() 	{ return lowCosts; }

	/*
	public void generateTour()
	{
		lowCosts = new LowerCosts(graph, xmlParser.getDelOrder());
		int[] reducedPath = new int[xmlParser.getDelOrder().getDeliveryList().size()];
			
		// get the order of the delivery	
		tsp.chercheSolution(2500, xmlParser.getDelOrder().getDeliveryList().size(), lowCosts.getCostsMatrix(), xmlParser.getDelOrder().getTimes());
		
		for( int i= 0 ; i < xmlParser.getDelOrder().getDeliveryList().size();i++)
		{		
			reducedPath[i] = tsp.getMeilleureSolution(i);
		}
		
		//adding the intermediates nodes
		addIntermediatePoints(reducedPath, xmlParser.getDelOrder());
		
	}
*/
	/*
	public ArrayList<MapNode> addIntermediatePoints(int[] reducedGraph,DeliveryOrder deliveryOrder)
	{
		HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Double>>> pathFromPoint = lowCosts.getPaths();
		ArrayList<MapNode> path = new ArrayList<MapNode>();
		ArrayList<MapNode> pathToNode = new ArrayList<MapNode>();
		
		for(int i=0;i< reducedGraph.length-1;i++)
		{
			path.add(deliveryOrder.getDeliveryList().get(reducedGraph[i]).getAdress());
			for (Pair<ArrayList<MapNode>,Double> temp : (    pathFromPoint.get(    deliveryOrder.getDeliveryList().get(   reducedGraph[i]   ).getAdress()   )))
			{
				if( (temp.getFirst().get(temp.getFirst().size() -1 ).equals(deliveryOrder.getDeliveryList().get(reducedGraph[i+1]).getAdress())))
				{
					path.addAll(temp.getFirst());
					path.remove(path.size()-1);
				}
			
			}
		}
		// path to go back to stock node
		path.add(deliveryOrder.getDeliveryList().get(reducedGraph[reducedGraph.length-1]).getAdress());
		for (Pair<ArrayList<MapNode>,Double> temp : (    pathFromPoint.get(    deliveryOrder.getDeliveryList().get(   reducedGraph[reducedGraph.length-1]   ).getAdress()   )))
		{
			if( (temp.getFirst().get(temp.getFirst().size() -1 ).equals(deliveryOrder.getDeliveryList().get(reducedGraph[0]).getAdress())))
			{
				path.addAll(temp.getFirst());
			}
		
		}
		path.add(deliveryOrder.getDeliveryList().get(reducedGraph[0]).getAdress());
		for(int i=0;i<path.size()-1;i++)
		{
			sections.add((graph.getDestinations(path.get(i))).get(path.get(i+1)));
		}
		
		// create the tour instance
		tour = new Tour(sections, xmlParser.getDelOrder());
		
		
		return path;
	}

	public void loadDeliveryFile(File currentFile) {
		// TODO Auto-generated method stub
		xmlParser.xmlDeliveriesParser(currentFile);
		tsp = new TSP1();
		generateTour();
	}
*/
	/**
	 * Step 1 of the engine. Calculates shortest way between all DeliveryPoint.
	 */
	public void dijkstra()
	{
		if(lowCosts == null)
			lowCosts = new LowerCosts(this);
		lowCosts.generateCosts();
		lowCosts.printCost();
	//	lowCosts.printCost();
	}
	
	/**
	 * Step 2 of the engine. Call TSP
	 */	
	public void TSP()
	{
		if(tsp==null)
			tsp = new TSP1();
		
		// Adapte the TSP Object
		TSPObject tspObject = AdapterModelTSP(this);
		
		// Call the TSP module
		tsp.chercheSolution(10000, tspObject.cout.length, tspObject.cout, tspObject.duree);
		tspObject.bestSolution = tsp.getMeilleureSolution();

		// Print TSP Result
		String TSP = "TSP: ";
		for(int i = 0; i< tspObject.bestSolution.length;i++)
		{
			TSP+=tspObject.mappingId.get(tspObject.bestSolution[i]).getidNode()+" ";
		}
		System.out.println(TSP);
		
		// Constructing a Tour
		AdapterTSPModel(this, tspObject);
	}
	
	/**
	 * This method is a static Adapter which create a TSPObject (used to do a TSP Call) from the Model.
	 * @param paths
	 * @return
	 */
	public static TSPObject AdapterModelTSP(Model model)
	{		
		HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> paths = model.getLowerCosts().getPaths();
		int nbSommets = paths.size();
		
		TSPObject out = new TSPObject(nbSommets);
				
		// Loop which fill the out cout tab
		int index = 0;
		MapNode nodei, nodej;
		
		// For each nodes
		for(Entry<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> entry : paths.entrySet())
		{			
			nodei = entry.getKey();
			
			for(Pair<ArrayList<MapNode>,Integer> pair : entry.getValue())
			{
				ArrayList<MapNode> list = pair.getFirst();
				
				nodej = list.get(list.size()-1);
				
				// get the cost of the pair
				int cost =  (pair.getSecond()).intValue();
				
				//Adding the cost in the out object of i,j with
				out.cout[out.getIByMapNode(nodei)][out.getIByMapNode(nodej)] = cost;
			}				
		}
		return out;		
	}

	/**
	 * This method construct a Tour from a TSPObject
	 * @param model 
	 * @param tspObject
	 */
	public static void AdapterTSPModel(Model model, TSPObject tspObject)
	{
		HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> paths = model.getLowerCosts().getPaths();
		ArrayList<Section> sections= new ArrayList<>();
		int i;
		
		// Iterating over TSP result
		for(i=0; i<tspObject.bestSolution.length-1;i++)
		{
			MapNode o = tspObject.mappingId.get(tspObject.bestSolution[i]);
			MapNode d = tspObject.mappingId.get(tspObject.bestSolution[i+1]);
			
			// Getting paths between TSP results o and d
			for(Pair<ArrayList<MapNode>,Integer> pair : paths.get(o))
			{
				
				ArrayList<MapNode> list = pair.getFirst();
				
				// retrieve the good path
				if(list.get(list.size()-1).equals(d))
				{
					// Adding the path corresponding to O and D
					int j;
					for(j = 0; j<list.size()-1;j++)
					{
						System.out.println("Searching : "+list.get(j).getidNode()+", "+list.get(j+1).getidNode());
						Section s = model.graphDelMan.getSection(list.get(j),list.get(j+1));
						System.out.println(s.toString());
						sections.add(s);	
					}
				}
				
			}
			
		}
		
		// Link between the last and the first element
		
		MapNode o = tspObject.mappingId.get(tspObject.bestSolution[i]);
		System.out.println("Last Node : " +  o.getidNode());
		MapNode d = tspObject.mappingId.get(tspObject.bestSolution[0]);
		for(Pair<ArrayList<MapNode>,Integer> pair : paths.get(o))
		{
			
			ArrayList<MapNode> list = pair.getFirst();
			
			// retrieve the good path
			if(list.get(list.size()-1).equals(d))
			{
				// Adding the path corresponding to O and D
				int j;
				for(j = 0; j<list.size()-1;j++)
				{
					System.out.println("Searching : "+list.get(j).getidNode()+", "+list.get(j+1).getidNode());
					Section s = model.graphDelMan.getSection(list.get(j),list.get(j+1));
					System.out.println(s.toString());
					sections.add(s);	
				}
			}
			
		}
		
		
		
		// Building IdDeliveryList
		Integer [] listIds = new Integer[tspObject.bestSolution.length];
		for(int in =0; in<tspObject.bestSolution.length;in++)
			listIds[in]= tspObject.mappingId.get(tspObject.bestSolution[in]).getidNode();

		Tour tour = new Tour(sections,listIds);	
		model.setTour(tour);
	}
	
	public void setTour(Tour tour) { this.tour=tour;}
	public Tour getTour() { return tour; }
	
	/**
	 * This method load a delivery file and call the corresponding process in the model.
	 * 
	 */
	@Override
	public void loadDeliveryFile(File currentFile) {
		
		// Step1 : parsing delivery file
		xmlParser.xmlDeliveriesParser(currentFile);
		
		// Step2 : Call the engine to create a tour
		// Step2.1 : call dijkstra
		dijkstra();
		// step2.2 : call TSP
		TSP();
		
		setChanged();
		notifyObservers("UPDATE_DELIVERY");
	}
}

/**
 *  This class encapsulate the objects that the TSP needs to work.
 * @author antoine
 */
class TSPObject
{
	// in parameters of TSP
	public int[][] cout;
	public int[] duree;
		
	// TSP result
	public Integer[] bestSolution;
	
	// Mapping between NodeId and index in matrix
	public ArrayList<MapNode> mappingId;
	
	public TSPObject(int nbSommets)
	{
		System.out.println("NbSommets"+nbSommets);
		cout 		= new int[nbSommets][nbSommets];
		mappingId 	= new ArrayList<MapNode>();
		duree 		= new int[nbSommets];
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public int getIByMapNode(MapNode node)
	{
		int i = 0;
		for(i=0;i<mappingId.size();i++)
		{
			if(mappingId.get(i).getidNode()==node.getidNode())
				return i;
		}
	
		mappingId.add(node);

		return mappingId.size()-1;
	}
}