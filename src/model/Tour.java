package model;

import java.util.ArrayList;
import java.util.Iterator;

import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryOrder;
import model.deliverymanager.DeliveryPoint;
import model.graph.Section;

import java.util.List;

public class Tour {
	private ArrayList<Section> sections;
	private ArrayList<DeliveryPoint> deliveryPoints;
	private int id;
	private static int factoryId = 0;
	private int entrepotId;
	
	/**
	 * Normal constructor
	 * @param sections
	 */
	public Tour(ArrayList<Section> sections, ArrayList<DeliveryPoint> deliveryPoints, int entrepotId)
	{
		this.entrepotId=entrepotId;
		this.sections = sections;
		this.deliveryPoints = deliveryPoints;
		this.id=factoryId;
		factoryId++;
	}
	
	//Getters
	public ArrayList<Section> getSections() { 	return sections;}
	public int getId(){ 						return id; }
	public int getEntrepotId() {				return entrepotId;}
	public ArrayList<DeliveryPoint> getDeliveryPoints(){ return this.deliveryPoints;}
	/**
	 * Get total duration of the whole tour
	 * @return duration of the whole tour
	 */
	public int getTotalDuration()
	{
		int totalDuration = 0;
		
		for (Section section : this.sections)
			totalDuration += section.getLength()/section.getSpeed();
		
		
		for (DeliveryPoint deliveryPoint : this.deliveryPoints)
			totalDuration += deliveryPoint.getDelivery().getDuration();
		
		return totalDuration;
	}
	
	/**
	 * Obtain the duration of the path to reach the id delivery point
	 * @param id to stop the computation of the duration
	 * @return duration of the path
	 */
	public int getDuration(int id)
	{
		return 2;
	}
	
	/**
	 * OBtain the total length of the tour
	 * @return length of the tour
	 */
	public int getTotalLength()
	{
		int totalLength = 0;
		
		for (Section section : this.sections)
			totalLength += section.getLength();
		
		return totalLength;
	}
	
	/**
	 * Obtain the length of the path to reach the id delivery point
	 * @param id
	 * @return
	 */
	public int getLength(int id)
	{
		return 1;
	}
	
	/**
	 * Check if a Point is a delivery point
	 * @param idPoint
	 * @return
	 */
	public boolean isDeliveryPoint(int idPoint)
	{	
		for (DeliveryPoint deliveryPoint : this.deliveryPoints)
			if (deliveryPoint.getMapNodeId() == idPoint)
				return true;
		
		return false;
	}
	
	/**
	 * Delete a specific delivery point based on his id
	 * @param deliveryPointsId that will be deleted
	 */
	public int deleteDeliveryPoint(int deliveryPointId) throws Throwable{		
		for (int cursor=0; cursor<this.deliveryPoints.size(); cursor++)
			if (id == this.deliveryPoints.get(cursor).getMapNodeId())
			{
				this.deliveryPoints.remove(cursor);
				return id;
			}
		throw new Throwable("Error, there is no delivery point with the id "+deliveryPointId+", cannot delete a non-existing delivery point in a tour");
	}
	
	/**
	 * Delete a specific delivery point based on his id
	 * @param deliveryPointsId that will be deleted
	 */
	public void addDeliveryPoint(int index, DeliveryPoint deliveryPoint){	
		this.deliveryPoints.add(index, deliveryPoint);
	}
}
