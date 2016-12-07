package model.deliverymanager;

import java.util.Date;

/**
 * 
 * @author FACANT
 *
 */
public class DeliveryPoint {
	private Delivery delivery;
	private Date arrivingDate;

	/**
	 * Create a delivery point in the sense of a tour with a delivery associated and an arriving date
	 * @param delivery used in the delivery point
	 * @param arriving moment to the delivery point
	 */
	public DeliveryPoint(Delivery delivery, Date arriving) {
		super();
		this.delivery = delivery;
		this.arrivingDate = arriving;
	}

	// Getters
	public Delivery getDelivery() {
		return delivery;
	}
	
	public Date getArrivingDate() {
		return arrivingDate;
	}
	
	public Date getLeavingDate(){
		Long timeLeaving = arrivingDate.getTime()+(delivery.getDuration()*1000);
		
		//System.out.println("dep"+arrivingDate+"  "+new Date(timeLeaving));
		return new Date(timeLeaving);
	}

	public int getMapNodeId() {
		return this.delivery.getAdress().getidNode();
	}
	
	// Setters
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}
	public void setArrivingDate(Date arriving) {
		this.arrivingDate = arriving;
	}
	
}
