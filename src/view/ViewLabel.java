package view;

import java.awt.Color;
import java.awt.Graphics;

public class ViewLabel implements IShape {
	
	int x;
	int y;
	String information;
	public Color color = Color.RED;
	
	public ViewLabel(int x, int y, String information){
		this.x = x;
		this.y = y;
		this.information = information;
	}
	
	public boolean contains(int x, int y){
		return false;
	}
	
	public void drawShape(Graphics g, int width, int height) {
		
		g.setColor(color);
		g.drawString(information, x, y);
	}

}
