package TraceRoute;

public class Instruction
{
	private Direction direction;
	private Integer index;
	private Integer length;
	private Integer idDestination;
	private String road;
	private boolean destinationIsDeliveryPoint;
	
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

	public Instruction(Direction direction, Integer index,Integer length,Integer idDestination,boolean destinationIsDeliveryPoint, String road) {
		this.direction = direction;
		this.index = index;
		this.length = length/10; // Warning length is in decimeter 
		this.idDestination = idDestination;
		this.destinationIsDeliveryPoint = destinationIsDeliveryPoint;
		this.road = road;
	}

	@Override
	public String toString() {
		return "Instruction [direction=" + direction + ", index=" + index + ", length=" + length + ", idDestination="
				+ idDestination + ", road=" + road + ", destinationIsDeliveryPoint=" + destinationIsDeliveryPoint + "]";
	}

	


	
}
