package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;
import TraceRoute.TraceRoute;
import controller.Controller;
import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryManager;
import model.engine.LowerCosts;
import model.engine.Pair;
import model.engine.TSP2;
import model.engine.TSP1;
import model.graph.GraphDeliveryManager;
import model.graph.MapNode;
import model.graph.Section;
import model.parser.XmlParser; 

public class Model extends Observable implements IModel {

	private Controller controller;
	private GraphDeliveryManager graphDelMan;
	private Tour tour;
	private XmlParser xmlParser;
	private DeliveryManager deliveryManager;
	private TSP2 tsp;
	private LowerCosts lowCosts;
	

	/**
	 * Normal constructor of the model
	 * @param controller
	 */
	public Model(Controller controller) {
		this.controller=controller;
		xmlParser 	= new XmlParser(this);
		graphDelMan = new GraphDeliveryManager(this);
		deliveryManager = new DeliveryManager();
	}

	/**
	 * This method call the parsing process of the parser
	 * @param currentFile
	 */
	public void parseMapFile(File currentFile) {
		try{
			xmlParser.xmlMapParser(currentFile);
			controller.getLogger().write(currentFile.getName() + " - Map loaded");
			setChanged();
			notifyObservers("UPDATE_MAP");
		}
		catch(Exception e)
		{
			controller.error("Parser : " + e.getMessage()+"\n"+e.getClass().getName()+" @ line "+e.getStackTrace()[0].getLineNumber()); 
		}
	}

	// Accesseur
	public List<MapNode> getMapNodes() {	return graphDelMan.getNodeList();	}	
	public List<Section> getSections() {	return graphDelMan.getSectionList();}
	public GraphDeliveryManager getGraphDeliveryManager() { return graphDelMan; }
	public DeliveryManager getDeliveryManager() { return deliveryManager; }
	public LowerCosts getLowerCosts() 	{ return lowCosts; }

	/**
	 * Step 1 of the engine. Calculates shortest way between all DeliveryPoint.
	 */
	public void dijkstra()
	{
		if(lowCosts == null)
			lowCosts = new LowerCosts(this);
		lowCosts.generateCosts();
	}
	
	/**
	 * Step 2 of the engine. Call TSP
	 */	
	public void TSP()
	{
		if(tsp==null)
			tsp = new TSP2();
		
		// Adapte the TSP Object
		TSPObject tspObject = AdapterModelTSP(this);
		
		// Call the TSP module
		tsp.chercheSolution(tspObject.departureDate,10000, tspObject.cout.length, tspObject.cout, tspObject.duree,tspObject.window);
		tspObject.bestSolution = tsp.getMeilleureSolution();

		// Print TSP Result
		String TSP = "TSP: ";
		try{
			for(int i = 0; i< tspObject.bestSolution.length;i++)
			{
				TSP+=tspObject.mappingId.get(tspObject.bestSolution[i]).getidNode()+" ";
			}
			
		
		System.out.println(TSP);
		
		// Constructing a Tour
		AdapterTSPModel(this, tspObject);
		}
		catch (Exception e) {
			controller.error("Impossible de calculer la tournée en respectant les conditions");
			return;
		}
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
		MapNode nodei, nodej;
		Delivery delivery;
		
		//get the time for each delivery
		out.duree = model.deliveryManager.getDeliveryOrder().getTimes();
		out.departureDate = model.deliveryManager.getDeliveryOrder().getStartingTime();
		
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
			// Adding the delivery windows for each
			for(int i = 0; i<model.getDeliveryManager().getDeliveryOrder().getDeliveryList().size();i++)
			{
				if(model.deliveryManager.getDeliveryOrder().getDeliveryList().get(i).getAdress().equals(nodei))
				{
					delivery = model.deliveryManager.getDeliveryOrder().getDeliveryList().get(i);
					out.window.add(new Pair<Date, Date>(delivery.getBeginning(), delivery.getEnd()));
				}
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
					for(int j = 0; j<list.size()-1;j++)
					{
						Section s = model.graphDelMan.getSection(list.get(j),list.get(j+1));
						sections.add(s);	
					}
				}
			}
		}
		
		// Link between the last and the first element
		
		MapNode o = tspObject.mappingId.get(tspObject.bestSolution[i]);
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
					Section s = model.graphDelMan.getSection(list.get(j),list.get(j+1));
					sections.add(s);	
				}
			}
			
		}
		
		// Building IdDeliveryList
		Integer [] listIds = new Integer[tspObject.bestSolution.length];
		for(int in =0; in<tspObject.bestSolution.length;in++)
			listIds[in]= tspObject.mappingId.get(tspObject.bestSolution[in]).getidNode();

		Tour tour = new Tour(sections,listIds,model.getDeliveryManager().getDeliveryOrder().getStoreAdress().getidNode());	
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
		
		try
		{
			// Step1 : parsing delivery file
			xmlParser.xmlDeliveriesParser(currentFile);
			controller.getLogger().write(currentFile.getName()+ " : Deliveries loaded");
			// Step2 : Call the engine to create a tour
			// Step2.1 : call dijkstra
			dijkstra();
			controller.getLogger().write(currentFile.getName()+ " : Dijkstra computed");
			// step2.2 : call TSP
			TSP();
			controller.getLogger().write(currentFile.getName()+ " : TSP done");
			// step2.3 : call RoadMap
			TraceRoute.generateInstructions(tour, this.getGraphDeliveryManager().getGraph());
			controller.getLogger().write(currentFile.getName()+ " : RoadMap done");
			setChanged();
			notifyObservers("UPDATE_DELIVERY");
		}
		catch(Exception e)
		{
			controller.error("Parser : " + e.getMessage()+"\n"+e.getClass().getName()+" @ line "+e.getStackTrace()[0].getLineNumber()); 
		}
	}

	public Controller getController() {
		return controller;
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
	public ArrayList<Pair<Date,Date>> window;
	Date departureDate;
		
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
		window = new ArrayList<Pair<Date,Date>>();
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