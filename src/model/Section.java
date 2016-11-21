package model;

public class Section implements Comparable<Section>{

	private long idOrigin;
	private long idDestination;
	private String name;
	private long length;
	private long speed;
	
	
	public Section(long idOrigin,long idDestination, String name, long length, long speed) {
		this.idOrigin = idOrigin;
		this.idDestination = idDestination;
		this.name = name;
		this.length = length;
		this.speed = speed;
	}


	
	
	// Getters and Setters

	public long getIdOrigin() {
		return idOrigin;
	}

	public void setIdOrigin(long idOrigin) {
		this.idOrigin = idOrigin;
	}




	public long getIdDestination() {
		return idDestination;
	}

	public void setIdDestination(long idDestination) {
		this.idDestination = idDestination;
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




<<<<<<< HEAD


	@Override
	public int compareTo(Section o) {
=======
	@Override
	public int compareTo(Object o) {
>>>>>>> 0cd19d096f71149fe4593df29c390aa91589d92c
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
