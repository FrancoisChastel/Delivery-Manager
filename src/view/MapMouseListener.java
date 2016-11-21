package view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

public class MapMouseListener implements MouseListener {

	private Map map;
	
	public MapMouseListener(Map map)
	{
		this.map = map;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		IShape point = map.containsPoint(e.getX(), e.getY());
		IShape troncon = map.containsTroncon(e.getX(), e.getY());
		
		if(point != null)
			View.displayMessage("Click on "+((ViewPoint)point).getId(), "Debug", JOptionPane.INFORMATION_MESSAGE);
		

		
		if(troncon != null)
		{
			View.displayMessage("Click on "+((ViewTroncon)troncon).getName()+" - "+((ViewTroncon)troncon).getId(), "Debug", JOptionPane.INFORMATION_MESSAGE);
			((ViewTroncon)troncon).setColor(Color.red);
			map.repaint();
		}
				
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
