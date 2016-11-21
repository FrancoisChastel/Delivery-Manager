package model;

import java.sql.Time;

public class Delivery {

	private int idDelivery;
	private MapNode adress;
	private int length;
	private Time beginning;
	private Time end;
	
	// Constructor for a delivery with time slot
	public Delivery(int idDelivery, MapNode adress, int length, Time beginning, Time end) {
		this.idDelivery = idDelivery;
		this.adress = adress;
		this.length = length;
		this.beginning = beginning;
		this.end = end;
	}

	// Constructor for a delivery WITHOUT time slot
	public Delivery(int idDelivery, MapNode adress, int length) {
		this.idDelivery = idDelivery;
		this.adress = adress;
		this.length = length;
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

	public long getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Time getBeginning() {
		return beginning;
	}

	public void setBeginning(Time beginning) {
		this.beginning = beginning;
	}

	public Time getEnd() {
		return end;
	}

	public void setEnd(Time end) {
		this.end = end;
	}
	

	
	
	
	
	
	
	
}
