package model;

public class Section implements Comparable<Section>{

	private int idOrigin;
	private int idDestination;
	private String name;
	private int length;
	private int speed;
	
	
	public Section(int idOrigin,int idDestination, String name, int length, int speed) {
		this.idOrigin = idOrigin;
		this.idDestination = idDestination;
		this.name = name;
		this.length = length;
		this.speed = speed;
	}


	
	
	// Getters and Setters

	public int getIdOrigin() {
		return idOrigin;
	}

	public void setIdOrigin(int idOrigin) {
		this.idOrigin = idOrigin;
	}




	public int getIdDestination() {
		return idDestination;
	}

	public void setIdDestination(int idDestination) {
		this.idDestination = idDestination;
	}


	public String getName() {
		return name;
	}
	

	public void setName(String name) {
		this.name = name;
	}


	public int getLength() {
		return length;
	}


	public void setLength(int length) {
		this.length = length;
	}


	public int getSpeed() {
		return speed;
	}


	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@Override
	public int compareTo(Section o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
