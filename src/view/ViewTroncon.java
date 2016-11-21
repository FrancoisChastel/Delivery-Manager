package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ViewTroncon implements IShape {
	
	private static int strokeLine = 3;
	private static int epsilon = 8;
	private ViewPoint pointOrigin;
	private ViewPoint pointTarget;
	private int id;
	private String name;
	private Color c;
	private static int idFactory = 0;
	
	public ViewTroncon(ViewPoint pointOrigin, ViewPoint pointTarget, int id, String name){
		this.pointOrigin = pointOrigin;
		this.pointTarget = pointTarget;
		this.id=idFactory;
		idFactory++;
		this.name = name;
		c = Color.DARK_GRAY;
	}
	

	@Override
	public boolean contains(int x, int y) {
		// TODO Auto-generated method stub
		float coeff;
		if( pointTarget.getCalculedX() != pointOrigin.getCalculedX())
		{
			coeff = (pointTarget.getCalculedY() - pointOrigin.getCalculedY())/ (pointTarget.getCalculedX() - pointOrigin.getCalculedX());
		}
		else
		{
			coeff = 1;
		}
		float ordonnee = pointOrigin.getCalculedY() - coeff* pointOrigin.getCalculedX();
		float exactValSearched =  x*coeff+ordonnee;
		
		if( y < exactValSearched + epsilon && y > exactValSearched - epsilon && x > Integer.min(pointOrigin.getCalculedX(),pointTarget.getCalculedX()) && x < Integer.max(pointOrigin.getCalculedX(),pointTarget.getCalculedX())){
			return true;
		}
		else{
			return false;
		}

	}
	
	public void setColor(Color c) { this.c=c;}

	public String getName()
	{
		return name;
	}
	@Override
	public void drawShape(Graphics g, int width, int height) {
		//Graphics2D g2d = (Graphics2D) g;
		System.out.println("Painting in"+c);
		g.setColor(c);
		//g2d.setStroke(new BasicStroke(strokeLine));
		g.drawLine(pointOrigin.getCalculedX(), pointOrigin.getCalculedY(), pointTarget.getCalculedX(), pointTarget.getCalculedY());
		
	}


	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}


	public Color getColor() {
		// TODO Auto-generated method stub
		return c;
	}
	

}
