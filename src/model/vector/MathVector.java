package model.vector;

/**
 * 
 * @author C.APARICIO && F.CHASTEL
 */
public class MathVector {
	private int x1;
	private int x2;
	private int y1;
	private int y2;
	
	private Integer[] coordinates;
	
	/**
	 * 
	 */
	public MathVector() {
		this.x1 = 0;
		this.x2 = 0;
		this.y1 = 0;
		this.y2 = -1;
		
		this.coordinates = new Integer[2];
		this.coordinates[0] = 0;
		this.coordinates[1] = -1;
	}
	
	/**
	 * 
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 */
	public MathVector(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		
		this.coordinates = new Integer[2];
		this.coordinates[0] = x2-x1;
		this.coordinates[1] = y2-y1;
	}

	public int getX1() {
		return x1;
	}
	public int getX2() {
		return x2;
	}
	public int getY1() {
		return y1;
	}
	public int getY2() {
		return y2;
	}
	
	public void redifineVector(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		
		this.coordinates[0] = x2-x1;
		this.coordinates[1] = y2-y1;
	}
	
	public Integer getCoordinateX() {
		return this.coordinates[0];
	}
	
	public Integer getCoordinateY() {
		return this.coordinates[1];
	}

	/**
	 * 
	 * @param other
	 * @return
	 */
	public double getAngle(MathVector other)
	{ 
		int dotProduct = (this.getCoordinateX()*other.getCoordinateX())+(this.getCoordinateY()*other.getCoordinateY());
		double Norm1 = Math.sqrt(Math.pow(this.getCoordinateX(), 2)+Math.pow(this.getCoordinateY(), 2));
		double Norm2 = Math.sqrt(Math.pow(other.getCoordinateX(), 2)+Math.pow(other.getCoordinateY(), 2));
		
		//CrossProduct Horaire ou Anti-Horaire
		if(this.getCoordinateX()*other.getCoordinateY() - this.getCoordinateY()*other.getCoordinateX() < 0)
			return -Math.toDegrees(Math.acos(dotProduct/(Norm1*Norm2)));
		
		return Math.toDegrees(Math.acos(dotProduct/(Norm1*Norm2)));
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x1;
		result = prime * result + x2;
		result = prime * result + y1;
		result = prime * result + y2;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MathVector other = (MathVector) obj;
		if (x1 != other.x1)
			return false;
		if (x2 != other.x2)
			return false;
		if (y1 != other.y1)
			return false;
		if (y2 != other.y2)
			return false;
		return true;
	}
	
}
