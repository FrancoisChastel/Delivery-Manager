package model.deliverymanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import model.graph.MapNode;

public class DeliveryOrder {

	private int idOrder;
	private MapNode storeAdress;
	private Date startingTime;
	private ArrayList<Delivery> deliveryList;
	private int maxIdNode;
	private int[] times; 
	
	
	
	public DeliveryOrder(int idOrder, MapNode storeAdress, Date startingTime, ArrayList<Delivery> deliveryList) {
		super();
		this.idOrder = idOrder;
		this.storeAdress = storeAdress;
		this.startingTime = startingTime;
		this.deliveryList = deliveryList;
		for (int iter = 0; iter<deliveryList.size(); iter++){
			int idNode = (int) deliveryList.get(iter).getAdress().getidNode();
			if(idNode > this.maxIdNode){this.maxIdNode = idNode;}
		}
		this.times = new int[deliveryList.size()+1];
		for (int iter = 0; iter<deliveryList.size(); iter++){
			times[iter+1] = (int) deliveryList.get(iter).getLength();
		}
	}
	
	/**
	 * Return a Delivery from an Id
	 * @param mapNodeId
	 * @return
	 */
	public Delivery getDeliveryById(int mapNodeId)
	{
		for(Delivery d : deliveryList)
		{
			if(d.getAdress().getidNode() == mapNodeId)
				return d;
		}
		
		return null;
	}


	public int getIdOrder() {
		return idOrder;
	}


	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}


	public MapNode getStoreAdress() {
		return storeAdress;
	}


	public void setStoreAdress(MapNode storeAdress) {
		this.storeAdress = storeAdress;
	}


	public Date getStartingTime() {
		return startingTime;
	}


	public void setStartingTime(Date startingTime) {
		this.startingTime = startingTime;
	}


	public ArrayList<Delivery> getDeliveryList() {
		return deliveryList;
	}


	public void setDeliveryList(ArrayList<Delivery> deliveryList) {
		this.deliveryList = deliveryList;
	} 
	
	public int[] getTimes() {
		return times;
	}
	
	public int getmaxIdNode() {
		return maxIdNode;
		
	}
	
}
