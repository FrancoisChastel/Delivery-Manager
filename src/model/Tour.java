package model;

import java.util.ArrayList;
import java.util.Iterator;

import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryOrder;
import model.graph.Section;

public class Tour {
	private ArrayList<Section> sections;
	private Integer[] deliveryPointsId;
	private int id;
	private static int factoryId = 0;
	private int entrepotId;
	
	/**
	 * Normal constructor
	 * @param sections
	 */
	public Tour(ArrayList<Section> sections, Integer[] deliveryPointsId, int entrepotId)
	{
		this.entrepotId=entrepotId;
		this.sections = sections;
		this.deliveryPointsId = deliveryPointsId;
		this.id=factoryId;
		factoryId++;
	}
	
	//Getters
	public ArrayList<Section> getSections() { 	return sections;}
	public int getId(){ 						return id; }
	public Integer[] getDeliveryPoints() {		return deliveryPointsId;}
	public int getEntrepotId() {				return entrepotId;}

	/**
	 * Get total duration of the whole tour
	 * @return duration of the whole tour
	 */
	public int getTotalDuration()
	{
		return 10;
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
		return 50;
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
		for(int i = 0; i<deliveryPointsId.length;i++)
		{
			if(deliveryPointsId[i]==idPoint)
				return true;
		}
		return false;
	}
	
}
