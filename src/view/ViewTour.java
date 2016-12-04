package view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class ViewTour {
	private int id;
	private boolean selected = false;
	private LinkedHashMap<Integer,ViewEdge> concernedEdge;
	private ArrayList<ViewPoint> concernedPoints;
	private Color gradient;
	private ViewPoint entrepot;
	
	/**
	 * Normal Constructor
	 */
	public ViewTour(int id, LinkedHashMap<Integer,ViewEdge> concernedEdge, ArrayList<ViewPoint> concernedPoints,ViewPoint entrepot)
	{
		this.id = id;
		this.concernedEdge 	 = concernedEdge;
		this.concernedPoints = concernedPoints;
		this.entrepot 		 = entrepot;
	}
	
	/**
	 * This method set the color of the sections and the points on the Map.
	 * It has 2 possible coloration ways : selected one (with gradient) and unselected one (with grey weighter line)
	 */
	public void colorTourComponentsOnMap()
	{
		// Points management
		for(ViewPoint p : concernedPoints)
		{
			p.color = Color.ORANGE;
		}
		
		entrepot.color = Color.RED;
		
		// Edges management
		if(selected)
			colorSelected();
		else
			colorUnselected();
	}
	
	/**
	 * This is the behaviour of a selected tour. It colorates edges of all lines following the gradient system. And it colorates points in orange.
	 */
	private void colorSelected()
	{

		
		// Edge management
		gradient =  new Color(Color.HSBtoRGB(0, 1, 1));
		Color increment = gradient;
		for(Entry<Integer, ViewEdge> line : concernedEdge.entrySet())
		{
			line.getValue().setColorId(increment, line.getKey());
			ajustColor();
			increment = gradient;
		}
	}
	
	/**
	 * This is the behaviour of an uselected tour. It does not colorate lines it only change the weight
	 */
	private void colorUnselected()
	{
		Color increment = gradient;
		
		for(Entry<Integer, ViewEdge> line : concernedEdge.entrySet())
		{
			ViewEdge current = line.getValue();
			
			// Coloring with default color
			current.clearSection(line.getKey());
			current.setBigWeight(true);
		}
	}
	
	/**
	 * This method calculates the new color of the gradient.
	 */
	private void ajustColor()
	{
			float[] hsbVals = new float[3];
			Color.RGBtoHSB(gradient.getRed(), gradient.getGreen(), gradient.getBlue(), hsbVals);
			if(hsbVals[0] <=1.0f)
			{
				gradient = new Color(Color.HSBtoRGB(hsbVals[0]+0.01f, hsbVals[1], hsbVals[2]));
			}
	}
	
	// Accessors
	public void setSelected(boolean value) { selected = value ; }
	public int getId() { return id; }
}
