package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;

import controller.Controller;
import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryManager;
import model.deliverymanager.DeliveryOrder;
import model.engine.LowerCosts;
import model.engine.Pair;
import model.engine.TSP2;
import model.graph.GraphDeliveryManager;
import model.graph.MapNode;
import model.graph.Section;
import model.parser.XmlParser; 
import model.traceroute.HtmlGenerator;
import model.traceroute.TraceRoute;

public class Model extends IModel {

	private Controller controller;
	private GraphDeliveryManager graphDelMan;
	private HashMap<Integer,Integer> indexDelOrdersTours;
	private HashMap<Integer,Tour> tours;
	private DeliveryOrder selected;
	private DeliveryManager deliveryManager;
	private TSP2 tsp;
	private LowerCosts lowCosts;	

	/**
	 * Normal constructor of the model
	 * @param controller
	 */
	public Model(Controller controller) {
		this.controller=controller;
		graphDelMan = new GraphDeliveryManager(this);
		deliveryManager = new DeliveryManager();
		indexDelOrdersTours = new HashMap<>();
		tours = new HashMap<>();
	}

	// Getters 
	public Controller getController() { 					return controller; }
	public GraphDeliveryManager getGraphDeliveryManager() { return graphDelMan; }
	public DeliveryManager getDeliveryManager() { 			return deliveryManager; }
	public LowerCosts getLowerCosts() 	{ 					return lowCosts; }
	public Tour getTour(int id) { 							return tours.get(id); }
	@Override
	public List<MapNode> getMapNodes() {					return graphDelMan.getNodeList();	}
	@Override
	public List<Section> getSections() {					return graphDelMan.getSectionList();}
	@Override
	public Tour getTourById(int id) { 						return tours.get(id); }
	@Override
	public DeliveryOrder getDeliveryOrderById(int id) {		return this.getDeliveryManager().getDeliveryOrders().get(id); }
	@Override
	public DeliveryOrder getSelected() { 					return selected; }

	// Setters
	public void setSelected(DeliveryOrder selected) {		this.selected = selected; }
	public void setTour(Tour tour) {  						tours.put(tour.getId(), tour);
															indexDelOrdersTours.put(selected.getIdOrder(),tour.getId()); }

	@Override
	public void resetModel()
	{
		indexDelOrdersTours = new HashMap<>();
		tours=new HashMap<>();
		deliveryManager = new DeliveryManager();
		graphDelMan=new GraphDeliveryManager(this);
	}
	
	@Override
	public void resetDeliveries()
	{
		indexDelOrdersTours = new HashMap<>();
		tours = new HashMap<>();
		deliveryManager = new DeliveryManager();
	}

	@Override
	public void loadMapFile(File currentFile) {
		try{
			XmlParser.xmlMapParser(this,currentFile);
			controller.getLogger().write(currentFile.getName() + " - Map loaded");
			setChanged();
			HashMap<String,Object> map = new HashMap<>();
			map.put("type", "UPDATE_MAP");
			notifyObservers(map);
		}
		catch(Exception e)
		{
			controller.error("Parser : " + e.getMessage()+"\n"+e.getClass().getName()+" @ line "+e.getStackTrace()[0].getLineNumber()); 
		}
	}
	
	@Override
	public void loadDeliveriesFile(File currentFile) {
		try
		{
			// Step1 : parsing delivery file
			XmlParser.xmlDeliveriesParser(this,currentFile);
			controller.getLogger().write(currentFile.getName()+ " : Deliveries loaded");
			// Step2 : Call the engine to create a tour
			// Step2.1 : call dijkstra
			dijkstra();
			controller.getLogger().write(currentFile.getName()+ " : Dijkstra computed");
			// step2.2 : call TSP
			TSP();
			controller.getLogger().write(currentFile.getName()+ " : TSP done");
						
			setChanged();
			HashMap<String,Object> map = new HashMap<>();
			map.put("type", "UPDATE_DELIVERY");
			map.put("tour", indexDelOrdersTours.get(selected.getIdOrder()));
			
			notifyObservers(map);
		}
		catch(Exception e)
		{
			controller.error("Parser : " + e.getMessage()+"\n"+e.getClass().getName()+" @ line "+e.getStackTrace()[0].getLineNumber()); 
		}
	}
	
	@Override
	public void generateTraceRoute(int tourid)
	{
				
		File htmlFile = new File("roadMap/index.html");
		HtmlGenerator.generateHtml(TraceRoute.generateInstructions(tours.get(tourid), this.getGraphDeliveryManager().getGraph()),this.deliveryManager,htmlFile);
		controller.getLogger().write("Tour "+tours.get(tourid)+ " : Instructions in HTML done");
	}

	@Override
	public void deleteDeliveryPoint(int tourID, int deliveryPointId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDeliveryPoint(int tourId, int nodeId, int duration,
			Date availabilityBeginning, Date availabilityEnd) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Step 1 of the engine. Calculates shortest way between all DeliveryPoint.
	 */
	private void dijkstra()
	{
		if(lowCosts == null)
		{
			lowCosts = new LowerCosts(this);
		}
		else
		{
			lowCosts.refresh();
		}
		lowCosts.generateCosts();
	}
	
	/**
	 * Step 2 of the engine. Call TSP
	 */	
	private void TSP()
	{
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
		out.duree = model.selected.getTimes();
		out.departureDate = model.selected.getStartingTime();
		
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
			for(int i = 0; i<model.selected.getDeliveryList().size();i++)
			{
				if(model.selected.getDeliveryList().get(i).getAdress().equals(nodei))
				{
					delivery = model.selected.getDeliveryList().get(i);
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

		Tour tour = new Tour(sections,listIds,model.selected.getStoreAdress().getidNode());	
		model.setTour(tour);
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