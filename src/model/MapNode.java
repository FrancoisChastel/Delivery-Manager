package model;


public class MapNode {
	
	private long idNode;
	private Long x;
	private long y;
	
	
	
	public MapNode(long idNode, Long x, long y) {
		this.idNode = idNode;
		this.x = x;
		this.y = y;
	}



	
	// Getters and Setters
	
	
	public long getidNode() {
		return idNode;
	}

	public void setidNode(long idNode) {
		this.idNode = idNode;
	}



	public Long getX() {
		return x;
	}



	public void setX(Long x) {
		this.x = x;
	}



	public long getY() {
		return y;
	}



	public void setY(long y) {
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (idNode ^ (idNode >>> 32));
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
		MapNode other = (MapNode) obj;
		if (idNode != other.idNode)
			return false;
		return true;
	}


	
	
	
}
