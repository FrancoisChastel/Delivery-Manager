package model.graph;


public class MapNode {
	
	public static enum ColorNode {Gris, Blanc, Noir};
	private int idNode;
	private int x;
	private int y;

	
	
	public MapNode(int idNode, int x, int y) {
		this.idNode = idNode;
		this.x = x;
		this.y = y;
	}
	
	// Getters and Setters	
	
	public int getidNode() {
		return idNode;
	}

	public void setidNode(int idNode) {
		this.idNode = idNode;
	}



	public int getX() {
		return x;
	}



	public void setX(int x) {
		this.x = x;
	}



	public int getY() {
		return y;
	}



	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return this.idNode;
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

	public String toString()
	{
		return "idNode = " + idNode + " X = " + x + " Y = " + y;
	}

	
	
	
}
