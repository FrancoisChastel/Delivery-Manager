package model;

import java.util.ArrayList;
import java.util.Iterator;

import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryOrder;
import model.graph.Section;

public class Tour {
	private ArrayList<Section> sections;
	private Integer[] deliveryPointsId;
	private int length = 10;
	private int totalDuration = 50;
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
	
	public ArrayList<Section> getSections() { return sections;}
	public int getId(){ return id; }
	public Integer[] getDeliveryPoints() {return deliveryPointsId;}

	public int getEntrepotId() {return entrepotId;}
}
