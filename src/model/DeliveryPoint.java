package model;

import java.util.Date;


public class DeliveryPoint extends Node{

	
	public DeliveryPoint(long idNode, Long x, long y) {
		super(idNode, x, y);
		// TODO Auto-generated constructor stub
	}
	private Date inTime;
	private Date outTime;
	
	public DeliveryPoint(long idNode, Long x, long y, Date inTime, Date outTime) {
		super(idNode, x, y);
		this.inTime = inTime;
		this.outTime = outTime;
	}
	
	// Getters and Setters

	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}
	
	
	
}
