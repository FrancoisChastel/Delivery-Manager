package model;


public class Node {
	
	private long idNode;
	private Long x;
	private long y;
	
	
	
	public Node(long idNode, Long x, long y) {
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
	
	

	
	
	
}
