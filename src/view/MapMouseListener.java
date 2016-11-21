package view;

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
		IShape shape = map.containsShape(e.getX(), e.getY());
		
		if(shape != null)
			View.displayMessage("Click on "+((ViewPoint)shape).getId(), "Debug", JOptionPane.INFORMATION_MESSAGE);
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
