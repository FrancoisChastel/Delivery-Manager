package model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedList;

public class DeliveryOrder {

	private long idOrder;
	private MapNode storeAdress;
	private Time startingTime;
	private ArrayList<Delivery> deliveryList;
	private int maxIdNode;
	private int[] times; 
	
	
	
	public DeliveryOrder(long idOrder, MapNode storeAdress, Time startingTime, ArrayList<Delivery> deliveryList) {
		super();
		this.idOrder = idOrder;
		this.storeAdress = storeAdress;
		this.startingTime = startingTime;
		this.deliveryList = deliveryList;
		for (int iter = 0; iter<deliveryList.size(); iter++){
			int idNode = (int) deliveryList.get(iter).getAdress().getidNode();
			if(idNode > this.maxIdNode){this.maxIdNode = idNode;}
		}
		this.times = new int[maxIdNode+1];
		for (int iter = 0; iter<deliveryList.size(); iter++){
			times[(int)deliveryList.get(iter).getAdress().getidNode()] = (int) deliveryList.get(iter).getLength();
		}
	}


	public long getIdOrder() {
		return idOrder;
	}


	public void setIdOrder(long idOrder) {
		this.idOrder = idOrder;
	}


	public MapNode getStoreAdress() {
		return storeAdress;
	}


	public void setStoreAdress(MapNode storeAdress) {
		this.storeAdress = storeAdress;
	}


	public Time getStartingTime() {
		return startingTime;
	}


	public void setStartingTime(Time startingTime) {
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
