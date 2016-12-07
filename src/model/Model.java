package model;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


import controller.Controller;
import controller.commands.Commander;
import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryManager;
import model.deliverymanager.DeliveryOrder;
import model.deliverymanager.DeliveryPoint;
import model.engine.LowerCosts;
import model.engine.LowerDistance;
import model.engine.Pair;
import model.engine.TSP2;
import model.graph.GraphDeliveryManager;
import model.graph.MapNode;
import model.graph.Section;
import model.parser.XmlParser; 
import model.traceroute.HtmlGenerator;
import model.traceroute.TraceRoute;
import view.View;

public class Model extends IModel {

	private Controller controller;
	private GraphDeliveryManager graphDelMan;
	private HashMap<Integer,Integer> indexDelOrdersTours;
	private HashMap<Integer,Tour> tours;
	private DeliveryOrder selected;
	private DeliveryManager deliveryManager;
	private TSP2 tsp;
	private Commander commander;

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
		commander = new Commander();
	}

	// Getters 
	public Controller getController() { 					return controller; }
	public GraphDeliveryManager getGraphDeliveryManager() { return graphDelMan; }
	public DeliveryManager getDeliveryManager() { 			return deliveryManager; }
	public GraphDeliveryManager getGraphDelMan() {			return graphDelMan;}
	public HashMap<Integer, Integer> getIndexDelOrdersTours() {	return indexDelOrdersTours; }
	public HashMap<Integer, Tour> getTours() {				return tours; }

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
	public void setSelected(DeliveryOrder selected) 
	{		
		this.selected = selected;
	}
	
	public void setTour(Tour tour) 
	{
		tours.put(tour.getId(), tour);
		indexDelOrdersTours.put(selected.getIdOrder(),tour.getId()); 
	}
	
	public void setGraphDelMan(GraphDeliveryManager graphDelMan) {
		this.graphDelMan = graphDelMan;
	}

	public void setIndexDelOrdersTours(HashMap<Integer, Integer> indexDelOrdersTours) {
		this.indexDelOrdersTours = indexDelOrdersTours;
	}

	public void setTours(HashMap<Integer, Tour> tours) {
		this.tours = tours;
	}

	public void setDeliveryManager(DeliveryManager deliveryManager) {
		this.deliveryManager = deliveryManager;
	}

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
	
	public void updateResetDeliveries()
	{
		setChanged();
		HashMap<String,Object> map = new HashMap<>();
		map.put("type", "RESET_DELIVERIES");
		notifyObservers(map);
	}
	
	public void updateMap()
	{
		setChanged();
		HashMap<String,Object> map = new HashMap<>();
		map.put("type", "UPDATE_MAP");
		notifyObservers(map);
	}
	
	public void updateDeliveries()
	{
		setChanged();
		HashMap<String,Object> map = new HashMap<>();
		map.put("type", "UPDATE_DELIVERY");
		if(this.selected==null)
		{
			map.put("tour", null);
		}
		else
		{
			map.put("tour", indexDelOrdersTours.get(this.selected.getIdOrder()));
		}
		notifyObservers(map);
	}

	@Override
	public void loadMapFile(File currentFile) {
		try{
			Date dateBeforeParser = new Date();
			this.getController().getLogger().write("Parsing map file : "+currentFile.getName());
			XmlParser.xmlMapParser(this.getGraphDeliveryManager().getGraph(),this.getGraphDeliveryManager().getNodeList(),
			this.getGraphDeliveryManager().getSectionList(),currentFile);
			Date dateAfterParser = new Date();
			int duration = dateAfterParser.compareTo(dateBeforeParser);
			this.getController().getLogger().write("Map parsed in "+duration+" ms");
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
			// Step1 : parsing delivery file((MainFrame) p;
			Date dateBeforeParser = new Date();
			DeliveryOrder newOrder = XmlParser.xmlDeliveriesParser(this.getGraphDeliveryManager().getGraph(),currentFile);
			this.getDeliveryManager().addDeliveryOrder(newOrder);
			this.setSelected(newOrder);	
			Date dateAfterParser = new Date();
			int duration = dateAfterParser.compareTo(dateBeforeParser);
			this.getController().getLogger().write("Deliveries parsed in "+duration+" ms");
			controller.getLogger().write(currentFile.getName()+ " : Deliveries loaded");
			// Step2 : Call the engine to create a tour
			// Step2.1 : call dijkstra for times
			HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> paths = dijkstra();
			controller.getLogger().write(currentFile.getName()+ " : Dijkstra on times computed");
			// Step2.2 : call dijkstra for distance
			HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> pathsDistance = dijkstraDistance();
			controller.getLogger().write(currentFile.getName()+ " : Dijkstra on distance computed");
			// step2.3 : call TSP
			TSP(paths,pathsDistance);
			controller.getLogger().write(currentFile.getName()+ " : TSP done");
			
			ArrayList<Integer> toursArray = getToursArray();
			setChanged();
			HashMap<String,Object> map = new HashMap<>();
			map.put("type", "UPDATE_DELIVERY");
			map.put("tour", toursArray.get(0));
			
			
			notifyObservers(map);
			updateDeliveries();
			map = new HashMap<>();
			
			map.put("type", "UPDATE_DELIVERY");
			map.put("tour", toursArray.get(1));
			
			notifyObservers(map);
		}
		catch(Exception e)
		{
			controller.error("Parser : " + e.getMessage()+"\n"+e.getClass().getName()+" @ line "+e.getStackTrace()[0].getLineNumber()); 
		}
	}
	
	public ArrayList<Integer> getToursArray()
	{
		ArrayList<Integer> toursArray = new ArrayList<>();
		for(Entry<Integer,Tour> entry : tours.entrySet())
		{
			toursArray.add(entry.getKey());
		}
		return toursArray;
	}
	
	public void unloadDeliveriesFile()
	{
		deliveryManager.getDeliveryOrders().remove(selected);
		tours.remove(indexDelOrdersTours.get(selected.getIdOrder()));
		indexDelOrdersTours.remove(selected.getIdOrder());
		selected = null;
	}
	
	@Override
	public void generateTraceRoute(int tourid)
	{	
		File htmlFile=null;
		try{
			htmlFile = new File("roadMap/index.html");
			HtmlGenerator.generateHtml(this.getGraphDeliveryManager().getGraph().getNodeById(tours.get(tourid).getEntrepotId()),this.tours.get(tourid),TraceRoute.generateInstructions(tours.get(tourid), this.getGraphDeliveryManager().getGraph()),htmlFile);
			controller.getLogger().write("Tour "+ tours.get(tourid)+ " : Instructions in HTML done");
		}
		catch(IOException e)
		{
			controller.error("Parser : " + e.getMessage()+"\n"+e.getClass().getName()+" @ line "+e.getStackTrace()[0].getLineNumber()); 
		}
		try {
			Desktop.getDesktop().browse(htmlFile.toURI());
			controller.getLogger().write("HTML print");
		} catch (IOException e) {
			controller.error("Print HTML : " + e.getMessage()+"\n"+e.getClass().getName()+" @ line "+e.getStackTrace()[0].getLineNumber()); 		
		}
		
	}

	@Override
	public int deleteDeliveryPoint(int tourID, int deliveryPointId) {
		int index = -1;
		try {
			this.controller.getLogger().write("Deleting in tour "+tourID+" the delivery point "+deliveryPointId+" there is "+this.tours.get(tourID).getTotalLength());
			index = this.tours.get(tourID).deleteDeliveryPoint(deliveryPointId, this.getGraphDeliveryManager());
			this.controller.getLogger().write("Deleted in tour "+tourID+" the delivery point "+deliveryPointId+" there is "+this.tours.get(tourID).getTotalLength());
		} catch (Throwable e) {
			this.controller.getLogger().write("Stopping deletion, error in model : "+e.getMessage()+"");
			e.printStackTrace();
		}
		
		System.out.println("Deleting a deliveryPoint t"+tourID+" d"+deliveryPointId);
		setChanged();
		HashMap<String,Object> map = new HashMap<>();
		map.put("type", "UPDATE_DELIVERY");
		map.put("tour", tourID);

		this.notifyObservers(map);
		return index;
	}

	@Override
	public void addDeliveryPoint(int tourId,  int index, int nodeId, int duration,
			Date availabilityBeginning, Date availabilityEnd) {
		//this.tours.get(tourId).addDeliveryPoint(index, new DeliveryPoint(new Delivery(), new Date()), this.getGraphDeliveryManager());
		// Create new Delivery;
		MapNode address = this.getGraphDeliveryManager().getGraph().getNodeById(nodeId);
		int idNewDel = selected.getDeliveryList().size();
		Delivery del = new Delivery(idNewDel,address, duration, availabilityBeginning, availabilityEnd);
		selected.getDeliveryList().add(del);
		
		try {
			tours.get(tourId).add2DeliveryPoint(index, new DeliveryPoint(del, new Date()), graphDelMan);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			View.displayMessage("Error while adding a point :"+e.getMessage(), "Error", null);
		}
		
		System.out.println("Adding del point"+nodeId);
		
		setChanged();
		HashMap<String,Object> map = new HashMap<>();
		map.put("type", "UPDATE_DELIVERY");
		map.put("tour", tourId);

		this.notifyObservers(map);
	}
		
	@Override	
	public DeliveryPoint getDeliveryPointById(int tourId, int index){
		return this.getTourById(tourId).getDeliveryPointById(index);
	}
	/**
	 * Step 1 of the engine. Calculates shortest way between all DeliveryPoint.
	 */
	private HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> dijkstra()
	{
		return LowerCosts.generateCosts(graphDelMan,selected);
	}
	
	private HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> dijkstraDistance()
	{
		return LowerDistance.generateCosts(graphDelMan,selected);
	}
	
	/**
	 * Step 2 of the engine. Call TSP
	 */	
	private void TSP(HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> paths,HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> pathsDistance)
	{
		tsp = new TSP2();
		
		// Adapte the TSP Object
		TSPObject tspObject = AdapterModelTSP(this, paths,pathsDistance);
		
		// Call the TSP module
		tsp.chercheSolution(tspObject.departureDate,10000, tspObject.cout.length, tspObject.cout,tspObject.distances, tspObject.duree,tspObject.window);
		tspObject.bestSolutionTime = tsp.getMeilleureSolutionTime();
		tspObject.bestSolutionDistance = tsp.getMeilleureSolutionDistance();
		tspObject.datesLivraisonsTime = tsp.getDatesLivraisonsTime();
		tspObject.datesLivraisonsDistance = tsp.getDatesLivraisonsDistance();
		tspObject.bestDistanceDistance = tsp.getDistanceDistance();
		tspObject.bestDistanceTime = tsp.getDistanceTime();

		// Print TSP Result
	/*	String TSP = "TSP: ";
			for(int i = 0; i< tspObject.bestSolution.length;i++)
			{
				TSP+=tspObject.mappingId.get(tspObject.bestSolution[i]).getidNode()+" ";
			}
					
		System.out.println(TSP);*/
		
		// Constructing a Tour
		try {
		AdapterTSPModel(this, tspObject, paths,tspObject.bestSolutionTime,tspObject.datesLivraisonsTime,tspObject.bestDistanceTime);
		AdapterTSPModel(this, tspObject, paths,tspObject.bestSolutionDistance,tspObject.datesLivraisonsDistance,tspObject.bestDistanceDistance);
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
	public static TSPObject AdapterModelTSP(Model model, HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> paths,HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> pathsDistance)
	{		
		int nbSommets = paths.size();
		
		TSPObject out = new TSPObject(nbSommets);
				
		// Loop which fill the out cout tab
		MapNode nodei, nodej;
		Delivery delivery;
		
		//get the time for each delivery
		out.duree = model.selected.getTimes();
		out.departureDate = model.selected.getStartingTime();
		
		// adding delivery node first
		out.getIByMapNode(model.selected.getStoreAdress());
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
		// parsing the distance map
		for(Entry<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> entry : pathsDistance.entrySet())
		{			
			nodei = entry.getKey();
			
			for(Pair<ArrayList<MapNode>,Integer> pair : entry.getValue())
			{
				ArrayList<MapNode> list = pair.getFirst();
				
				nodej = list.get(list.size()-1);
				
				// get the cost of the pair
				int cost =  (pair.getSecond()).intValue();
				
				//Adding the cost in the out object of i,j with
				out.distances[out.getIByMapNode(nodei)][out.getIByMapNode(nodej)] = cost;
			}
		}
		return out;		
	}

	/**
	 * This method construct a Tour from a TSPObject
	 * @param model 
	 * @param tspObject
	 */
	public static void AdapterTSPModel(Model model, TSPObject tspObject,HashMap<MapNode,ArrayList<Pair<ArrayList<MapNode>,Integer>>> paths,Integer[] bestSolution,Date[] datesLivraisons,int bestDistance)
	{
		ArrayList<Section> sections= new ArrayList<>();
		int i;
		int storeId =  tspObject.mappingId.get(bestSolution[0]).getidNode();
				
		// Iterating over TSP result
		for(i=0; i<bestSolution.length-1;i++)
		{
			MapNode o = tspObject.mappingId.get(bestSolution[i]);
			MapNode d = tspObject.mappingId.get(bestSolution[i+1]);
			
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
		
		MapNode o = tspObject.mappingId.get(bestSolution[i]);
		MapNode d = tspObject.mappingId.get(bestSolution[0]);
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
		ArrayList<DeliveryPoint> deliveryPoints = new ArrayList<DeliveryPoint>();
		
		for(int in =1; in<bestSolution.length;in++)
		{
			// Get the current delivery Node Id
			int idNode = tspObject.mappingId.get(bestSolution[in]).getidNode();
			// get the corresponding delivery in model
			Delivery delivery = model.selected.getDeliveryById(idNode);
			// Create the right deliveryPoint
			DeliveryPoint dp  = new DeliveryPoint(delivery, datesLivraisons[bestSolution[in]]);
			
			deliveryPoints.add(dp);
		}

		if(model.selected.getStoreAdress().getidNode() !=storeId)
			View.displayMessage("Error", "The entrepot out of the TSP is not the same as the selected tour", null);
		Tour tour = new Tour(sections,deliveryPoints,storeId,bestDistance);	
		tour.setDateBackToWarehouse(datesLivraisons[0]);		
		tour.setDateDepartFromWarehouse(model.selected.getStartingTime());
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
	public int[][] distances;
	public int[] duree;
	public ArrayList<Pair<Date,Date>> window;
	Date departureDate;
		
	// TSP result
	public Integer[] bestSolutionTime;
	public Date [] datesLivraisonsTime;
	public Integer[] bestSolutionDistance;
	public Date [] datesLivraisonsDistance;
	public int bestDistanceTime;
	public int bestDistanceDistance;
	
	 
	// Mapping between NodeId and index in matrix
	public ArrayList<MapNode> mappingId;
	
	public TSPObject(int nbSommets)
	{
		cout 		= new int[nbSommets][nbSommets];
		distances 		= new int[nbSommets][nbSommets];
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