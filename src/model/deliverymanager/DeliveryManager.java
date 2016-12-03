package model.deliverymanager;

import java.util.HashMap;

public class DeliveryManager {
	private HashMap<Integer,DeliveryOrder> delOrders;
	private static int factoryId = 0;
	private int id;

	public DeliveryManager()
	{
		delOrders = new HashMap<>();
	}
	
	public void addDeliveryOrder(DeliveryOrder deliveryOrder) {
		this.delOrders.put(factoryId,deliveryOrder);
		deliveryOrder.setIdOrder(factoryId);
		id=factoryId++;
	}

	public HashMap<Integer,DeliveryOrder> getDeliveryOrders() {	return delOrders;	}
	public int getId() { return id; }
}
