package model;

import java.sql.Time;
import java.util.LinkedList;

public class DeliveryOrder {

	private long idOrder;
	private MapNode storeAdress;
	private Time startingTime;
	private LinkedList<Delivery> deliveryList;
	
	
	public DeliveryOrder(long idOrder, MapNode storeAdress, Time startingTime, LinkedList<Delivery> deliveryList) {
		super();
		this.idOrder = idOrder;
		this.storeAdress = storeAdress;
		this.startingTime = startingTime;
		this.deliveryList = deliveryList;
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


	public LinkedList<Delivery> getDeliveryList() {
		return deliveryList;
	}


	public void setDeliveryList(LinkedList<Delivery> deliveryList) {
		this.deliveryList = deliveryList;
	} 
	
	
	
}
