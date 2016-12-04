package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class ViewEdge implements IShape {
	
	// Constantes ------
	private static int BigLineWeight = 5;
	private static int DefaultLineWeight = 1;
	private static int epsilon = 8;
	private static Color defaultColor = Color.WHITE;	
	private static int idFactory = 0;
	
	// Attributes ------
	private ViewPoint pointOrigin;
	private ViewPoint pointTarget;
	private boolean	bigWeight = false;
	
	// This map represents the sections on this Troncon. It could be sized 2 or 1. The key is the ID of the section and the value its color.
	private HashMap <Integer,Color> sections;

	/**
	 * Normal Constructor
	 * @param pointOrigin
	 * @param pointTarget
	 * @param id
	 */
	public ViewEdge(ViewPoint pointOrigin, ViewPoint pointTarget, int id){
		this.pointOrigin = pointOrigin;
		this.pointTarget = pointTarget;
		
		sections = new HashMap <Integer,Color>();
		sections.put(id, defaultColor);
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
	
	/**
	 * This method draw the Troncon. If it contains 2 sections, it will draw 2 sections.
	 * It iterate over the section map, draw the corresponding lines.
	 * @param g
	 * @param width
	 * @param height
	 * @param stroke
	 */
	public void drawShape(Graphics g, int width, int height) {
		Graphics2D g2d = (Graphics2D) g;
		
		Iterator<Color> i = sections.values().iterator();
		
		Color section1 = i.next();
		
		// if there is only one line, then draw it
		if(!i.hasNext())
		{
			// Prepare to draw the first line
			g2d.setColor(section1);
			
			if(section1 == defaultColor  && !bigWeight)
				g2d.setStroke(new BasicStroke(DefaultLineWeight));
			else if (section1 != defaultColor || bigWeight)
				g2d.setStroke(new BasicStroke(BigLineWeight));
			g2d.drawLine(pointOrigin.getCalculedX(), pointOrigin.getCalculedY(), pointTarget.getCalculedX(), pointTarget.getCalculedY());
		}
		else // If we have to draw the second line, we draw it stroke only if there is conflict
		{			
			Color section2 = i.next();
			// if both sections are default colored, then draw a single line
			if(section2 == defaultColor && section1 == defaultColor) 
			{
				g2d.setColor(section1);
				
				if(bigWeight)
					g2d.setStroke(new BasicStroke(BigLineWeight));
				else
					g2d.setStroke(new BasicStroke(DefaultLineWeight));
				
				g2d.drawLine(pointOrigin.getCalculedX(), pointOrigin.getCalculedY(), pointTarget.getCalculedX(), pointTarget.getCalculedY());
			}
			// if section1 is default and section2 is not , then we draw section2 color 
			else if(section1 == defaultColor && section2 != defaultColor)
			{
				g2d.setColor(section2);
				g2d.setStroke(new BasicStroke(BigLineWeight));
				g2d.drawLine(pointOrigin.getCalculedX(), pointOrigin.getCalculedY(), pointTarget.getCalculedX(), pointTarget.getCalculedY());
			}
			// if section2 is default and section1 is not , then we draw section1 color 
			else if(section1 != defaultColor && section2 == defaultColor)
			{
				g2d.setColor(section1);
				g2d.setStroke(new BasicStroke(BigLineWeight));
				g2d.drawLine(pointOrigin.getCalculedX(), pointOrigin.getCalculedY(), pointTarget.getCalculedX(), pointTarget.getCalculedY());
			}
			// if both are not default, then color with stroke line
			else if(section1 != defaultColor && section2 != defaultColor)
			{
				// we draw normal first line
				g2d.setColor(section1);				
				g2d.setStroke(new BasicStroke(BigLineWeight));
				g2d.drawLine(pointOrigin.getCalculedX(), pointOrigin.getCalculedY(), pointTarget.getCalculedX(), pointTarget.getCalculedY());
				
				// Then we draw stroke second line
				g2d.setColor(section2);
				g2d.setStroke(new BasicStroke(BigLineWeight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, new float[] { 10.0f, 10.0f }, 0.0f));
				g2d.drawLine(pointOrigin.getCalculedX(), pointOrigin.getCalculedY(), pointTarget.getCalculedX(), pointTarget.getCalculedY());
			}
		}
	}

	public Set<Integer> getIds() {return sections.keySet();	}
	public Color getColorId(int id) { return sections.get(id);	}
	public void addSection(int id) { sections.put(id, defaultColor); }
	public void setColorId(Color c, int id) { sections.put(id, c);}
	public ViewPoint getOrigin() {return pointOrigin;}
	public ViewPoint getTarget() {return pointTarget;}
	public void setBigWeight(boolean value) {bigWeight=value; }
	public void clearSection(int id) {sections.put(id, defaultColor); }
}
