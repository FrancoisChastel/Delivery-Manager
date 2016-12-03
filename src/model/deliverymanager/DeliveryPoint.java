package model.deliverymanager;

import java.util.Date;

import model.graph.MapNode;


public class DeliveryPoint extends MapNode{
	private Date inTime;
	private Date outTime;
	
	public DeliveryPoint(int idNode, int x, int y) {
		super(idNode, x, y);

	}
	
	public DeliveryPoint(int idNode, int x, int y, Date inTime, Date outTime) {
		super(idNode, x, y);
		this.inTime = inTime;
		this.outTime = outTime;
	}
	
	// Getters and Setters
	public Date getInTime() {	return inTime;	}
	public void setInTime(Date inTime) {	this.inTime = inTime;	}
	public Date getOutTime() {	return outTime;	}
	public void setOutTime(Date outTime) {	this.outTime = outTime;	}	
}
