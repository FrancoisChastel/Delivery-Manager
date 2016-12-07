package controller.commands.undocommands;

import java.util.Date;

import model.IModel;
import model.deliverymanager.DeliveryPoint;
import controller.commands.IUndoableCommand;

public class DeleteDeliveryPointCommand  implements IUndoableCommand{
	private int nodeId;
	private int tourId;
	private int positionInSection;
	private DeliveryPoint deliveryPoint ;
	private IModel model;
	
	public DeleteDeliveryPointCommand(int tourId, int nodeId, IModel model) {
		super();
		this.tourId = tourId;
		this.model = model;
		this.nodeId = nodeId;
		this.deliveryPoint = this.model.getDeliveryPointById(tourId, nodeId);
	}

	@Override
	public void execute() throws Throwable {
		this.positionInSection = this.model.deleteDeliveryPoint(this.tourId, this.nodeId);
	}

	@Override
	public void undo() throws Throwable {
		System.out.println(tourId);
		System.out.println(this.positionInSection);
		System.out.println(this.nodeId);
		System.out.println(this.deliveryPoint.getDelivery().getDuration());
		System.out.println(this.deliveryPoint.getDelivery().getBeginning());
		System.out.println(this.deliveryPoint.getDelivery().getEnd());
		this.model.addDeliveryPoint(this.tourId, this.positionInSection,this.nodeId,  (int) this.deliveryPoint.getDelivery().getDuration(), this.deliveryPoint.getDelivery().getBeginning(), this.deliveryPoint.getDelivery().getEnd());		
	}
	

}
