package model.deliverymanager;

public class DeliveryManager {
	private DeliveryOrder delOrder;


	public void addDeliveryOrder(DeliveryOrder deliveryOrder) {
		this.delOrder = deliveryOrder;
	}

	public DeliveryOrder getDeliveryOrder() {	return delOrder;	}
}
