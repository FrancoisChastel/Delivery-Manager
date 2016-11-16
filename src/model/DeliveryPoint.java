package model;

import com.sun.jmx.snmp.Timestamp;

public class DeliveryPoint extends Node{

	
	public DeliveryPoint(long idNode, Long x, long y) {
		super(idNode, x, y);
		// TODO Auto-generated constructor stub
	}
	private Timestamp inTime;
	private Timestamp outTime;
	
	public DeliveryPoint(long idNode, Long x, long y, Timestamp inTime, Timestamp outTime) {
		super(idNode, x, y);
		this.inTime = inTime;
		this.outTime = outTime;
	}
	
	// Getters and Setters

	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}

	public Timestamp getOutTime() {
		return outTime;
	}

	public void setOutTime(Timestamp outTime) {
		this.outTime = outTime;
	}
	
	
	
}
