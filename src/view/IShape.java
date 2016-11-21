package view;

import java.awt.Graphics;

public interface IShape {
	
	public boolean contains(int x, int y);
	public void drawShape(Graphics g, int width, int height);
}
