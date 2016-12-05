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
		return new Date(this.arrivingDate.getTime()+(this.delivery.getDuration()*1000));
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
