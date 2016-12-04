package model;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import model.deliverymanager.DeliveryOrder;
import model.graph.MapNode;
import model.graph.Section;

public abstract class IModel extends Observable {
	
	/**
	 * Generate trace route in HTML based on a specific tour
	 * @param tourid of the tour that will be generated in HTML
	 */
	public abstract void generateTraceRoute(int tourid);
	
	/**
	 * Return all the nodes of the map in the model
	 * @return nodes of the map
	 */
	public abstract List<MapNode> getMapNodes();
	
	/**
	 * Return all the sections of the map in the model
	 * @return sections of the map
	 */
	public abstract List<Section> getSections();
	
	/**
	 * Get selected delivery order by the user
	 * @return delivery order
	 */
	//TODO : refactor using an ID to get a selected, stock in IHM the selected
	public abstract DeliveryOrder getSelected();
	
	/**
	 * Get tour based on his id
	 * @param id of the tour
	 * @return tour 
	 */
	public abstract Tour getTourById(int id);
	
	/**
	 * Load a specific delivery file in the model
	 * @param currentFile of the deliveries that will be parsed
	 */
	public abstract void loadDeliveriesFile(File currentFile);
	
	/**
	 * Load and parse map file in the model
	 * @param currentFile of the map that will be parsed
	 */
	public abstract void loadMapFile(File currentFile);
	
	/**
	 * Reset all the datas of the model
	 */
	public abstract void resetModel();
	
	/**
	 * Reset all the deliveries
	 */
	public abstract void resetDeliveries();
	
	/**
	 * delete a delivery point
	 * @param tourID of the delivery point
	 * @param deliveryPointId that will deleted
	 */
	public abstract void deleteDeliveryPoint(int tourID, int deliveryPointId);
	
	/**
	 * Create and add a delivery point to a specific tour.
	 * @param tourId which is concerned by the add of the delivery point
	 * @param nodeId where the delivery point will be set
	 * @param duration of delivery 
	 * @param availabilityBeginning of the delivery  
	 * @param availabilityEnd of the delivery
	 */
	public abstract void addDeliveryPoint(int tourId,int nodeId, int duration, Date availabilityBeginning, Date availabilityEnd);
}
