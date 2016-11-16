package model;

public class Section {

	
	private long idSection;
	private Node origin;
	private Node destination;
	private String name;
	private long length;
	private long speed;
	
	
	public Section(long idSection, Node origin, Node destination, String name, long length, long speed) {
		this.idSection = idSection;
		this.origin = origin;
		this.destination = destination;
		this.name = name;
		this.length = length;
		this.speed = speed;
	}


	
	
	// Getters and Setters
	
	public long getIdSection() {
		return idSection;
	}


	public void setIdSection(long idSection) {
		this.idSection = idSection;
	}


	public Node getOrigin() {
		return origin;
	}


	public void setOrigin(Node origin) {
		this.origin = origin;
	}


	public Node getDestination() {
		return destination;
	}


	public void setDestination(Node destination) {
		this.destination = destination;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public long getLength() {
		return length;
	}


	public void setLength(long length) {
		this.length = length;
	}


	public long getSpeed() {
		return speed;
	}


	public void setSpeed(long speed) {
		this.speed = speed;
	}
	
	
}
