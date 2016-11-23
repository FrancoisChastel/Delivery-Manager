package model;

import java.util.ArrayList;
import java.util.Iterator;

public class Tour {
	private ArrayList<Section> sections;
	private DeliveryOrder deliveryOrder;
	private int length = 10;
	private int totalDuration = 50;
	
	/**
	 * Normal constructor
	 * @param sections
	 */
	public Tour(ArrayList<Section> sections, DeliveryOrder deliveryOrder)
	{
		this.sections = sections;
		this.deliveryOrder = deliveryOrder;
	}
	
	/**
	 * Check if a Point is a delivery point
	 * @param idPoint
	 * @return
	 */
	public boolean isDeliveryPoint(int idPoint)
	{
		Iterator<Delivery> i =  deliveryOrder.getDeliveryList().iterator();
		
		while(i.hasNext())
		{
			Delivery delivery = i.next();
			
			if(delivery.getAdress().getidNode() == idPoint)
				return true;
		}		
		return false;
	}
	
	public ArrayList<Section> getSections() { return sections;}
}
