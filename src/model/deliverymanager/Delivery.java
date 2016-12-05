package model.deliverymanager;

import java.util.Date;

import model.graph.MapNode;

public class Delivery {
	private int idDelivery;
	private MapNode adress;
	private int duration;
	private Date beginning;
	private Date end;
	
	// Constructor for a delivery with time slot
	public Delivery(int idDelivery, MapNode adress, int duration, Date beginning, Date end) {
		this.idDelivery = idDelivery;
		this.adress = adress;
		this.duration = duration;
		this.beginning = beginning;
		this.end = end;
	}

	// Constructor for a delivery WITHOUT time slot
	public Delivery(int idDelivery, MapNode adress, int duration) {
		this.idDelivery = idDelivery;
		this.adress = adress;
		this.duration = duration;
		this.beginning= null;
		this.end = null;
	}
	
	// Getters and Setters
	public long getIdDelivery() {
		return idDelivery;
	}

	public void setIdDelivery(int idDelivery) {
		this.idDelivery = idDelivery;
	}

	public MapNode getAdress() {
		return adress;
	}

	public void setAdress(MapNode adress) {
		this.adress = adress;
	}

	public long getDuration() {
		return duration;
	}

	public void setLength(int length) {
		this.duration = length;
	}

	public Date getBeginning() {
		return beginning;
	}

	public void setBeginning(Date beginning) {
		this.beginning = beginning;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}
