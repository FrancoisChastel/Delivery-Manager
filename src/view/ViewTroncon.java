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
	
	public ViewTroncon(ViewPoint pointOrigin, ViewPoint pointTarget, int id){
		this.pointOrigin = pointOrigin;
		this.pointTarget = pointTarget;
		this.id = id;
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO Auto-generated method stub
		float coeff = (pointTarget.getCalculedY() - pointOrigin.getCalculedY())/ (pointTarget.getCalculedX() - pointOrigin.getCalculedX());
		float ordonnee = pointOrigin.getCalculedY() - coeff* pointOrigin.getCalculedX();
		float exactValSearched =  x*coeff+ordonnee;
		
		if( y < exactValSearched + epsilon && y > exactValSearched - epsilon && x > Integer.min(pointOrigin.getCalculedX(),pointTarget.getCalculedX()) && x < Integer.max(pointOrigin.getCalculedX(),pointTarget.getCalculedX())){
			return true;
		}
		else{
			return false;
		}

	}

	@Override
	public void drawShape(Graphics g, int width, int height) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(strokeLine));
		g2d.drawLine(pointOrigin.getCalculedX(), pointOrigin.getCalculedY(), pointTarget.getCalculedX(), pointTarget.getCalculedY());

	}

}
