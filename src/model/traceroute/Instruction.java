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

	/**
	 * Use by AssertEqual JUnit
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instruction other = (Instruction) obj;
		if (destinationIsDeliveryPoint != other.destinationIsDeliveryPoint)
			return false;
		if (direction != other.direction)
			return false;
		if (idDestination == null) {
			if (other.idDestination != null)
				return false;
		} else if (!idDestination.equals(other.idDestination))
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		if (length == null) {
			if (other.length != null)
				return false;
		} else if (!length.equals(other.length))
			return false;
		if (road == null) {
			if (other.road != null)
				return false;
		} else if (!road.equals(other.road))
			return false;
		if (uniqueOutgoingDestination != other.uniqueOutgoingDestination)
			return false;
		if (uniqueOutgoingDestinationInItsArea != other.uniqueOutgoingDestinationInItsArea)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Instruction [direction=" + direction + ", index=" + index + ", length=" + length + ", idDestination="
				+ idDestination + ", road=" + road + ", destinationIsDeliveryPoint=" + destinationIsDeliveryPoint
				+ ", uniqueOutgoingDestination=" + uniqueOutgoingDestination + ", uniqueOutgoingDestinationInItsArea="
				+ uniqueOutgoingDestinationInItsArea + "]";
	}


}
