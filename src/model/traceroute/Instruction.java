package model.traceroute;

public class Instruction
{
	private Direction direction;
	private Integer index;
	private Integer length;
	private Integer idDestination;
	private String road;
	private boolean destinationIsDeliveryPoint;
	// If Unique : Continues on the road
	// Wrong : Take the first street to left
	private boolean uniqueOutgoingDestination; 
	// If Unique : Turn to left
	// Wrong : Take the first street to left
	private boolean uniqueOutgoingDestinationInItsArea;
	
	public Direction getDirection() {
		return direction;
	}
	
	public Integer getIndex() {
		return index;
	}
	
	public Integer getLength() {
		return length;
	}
	
	public String getRoad() {
		return road;
	}
	
	public Integer getIdDestination() {
		return idDestination;
	}
	
	public boolean isDestinationIsDeliveryPoint() {
		return destinationIsDeliveryPoint;
	}

	public boolean isUniqueOutgoingDestination() {
		return uniqueOutgoingDestination;
	}

	public boolean isUniqueOutgoingDestinationInItsArea() {
		return uniqueOutgoingDestinationInItsArea;
	}

	public Instruction(Direction direction, Integer index,Integer length,Integer idDestination,boolean destinationIsDeliveryPoint,
			String road,boolean uniqueOutgoingDestination,boolean uniqueOutgoingDestinationInItsArea) {
		this.direction = direction;
		this.index = index;
		this.length = length/10; // Warning length is in decimeter 
		this.idDestination = idDestination;
		this.destinationIsDeliveryPoint = destinationIsDeliveryPoint;
		this.road = road;
		this.uniqueOutgoingDestination = uniqueOutgoingDestination;
		this.uniqueOutgoingDestinationInItsArea = uniqueOutgoingDestinationInItsArea;
	}

	@Override
	public String toString() {
		return "Instruction [direction=" + direction + ", index=" + index + ", length=" + length + ", idDestination="
				+ idDestination + ", road=" + road + ", destinationIsDeliveryPoint=" + destinationIsDeliveryPoint
				+ ", uniqueOutgoingDestination=" + uniqueOutgoingDestination + ", uniqueOutgoingDestinationInItsArea="
				+ uniqueOutgoingDestinationInItsArea + "]";
	}


}
