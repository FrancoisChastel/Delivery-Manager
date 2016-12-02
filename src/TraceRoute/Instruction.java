package TraceRoute;

public class Instruction
{
	private Direction direction;
	private Integer index;
	private String road;
	
	public Direction getDirection() {
		return direction;
	}
	
	public Integer getIndex() {
		return index;
	}
	
	public String getRoad() {
		return road;
	}
	
	public Instruction(Direction direction, Integer index, String road) {
		this.direction = direction;
		this.index = index;
		this.road = road;
	}
	
	@Override
	public String toString() {
		return "Instruction [direction=" + direction + ", index=" + index + ", road=" + road + "]";
	}
	
}
