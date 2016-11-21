package view;

import java.awt.Color;
import java.awt.Graphics;

public class ViewPoint implements IShape{

	private double x;
	private double y;
	private static int radius = 10;
	private int calculedX;
	private int calculedY;
	private int id;
	
	/**
	 * Normal Constructor of the view point. Coordinates are double because they are expressed in Frame Percentage
	 * @param x
	 * @param y
	 */
	public ViewPoint(double x, double y, int id)
	{
		this.x	= x;
		this.y	= y;
		this.id = id;
	}
	@Override
	public boolean contains(int x, int y) {
		return ( (calculedX-radius)< x && x < (calculedX+radius) )&& ( (calculedY-radius)< y && y < (calculedY+radius) );
	}

	@Override
	public void drawShape(Graphics g, int width, int height) {
		// TODO Auto-generated method stub
		g.setColor(Color.BLACK);
		calculedX = (int) (width*x);
		calculedY = (int) (height*y);
		
		int dx = calculedX-(radius/2);
		int dy = calculedY-(radius/2);
		g.fillOval(dx,dy,radius,radius);
	}
	
	public int getId() { return id;}

}
