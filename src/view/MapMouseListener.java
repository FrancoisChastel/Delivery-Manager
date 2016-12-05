package view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;

import javax.swing.JOptionPane;

public class MapMouseListener implements MouseListener, MouseMotionListener {

	private Map map;
	private int shiftInformationFromMouse = 2;
	
	public MapMouseListener(Map map)
	{
		this.map = map;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		ViewPoint point = map.containsPoint(e.getX(), e.getY());
		ViewEdge troncon = map.containsEdge(e.getX(), e.getY());
		
		if(point != null)
		{
			map.getMainFrame().setPickedPointAddDelivery(point);
		}
					
		if(troncon != null)
		{

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

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		ViewPoint point = map.containsPoint(e.getX(), e.getY());
		if(point != null)
		{
			String information = ((Integer)point.getId()).toString();
			ViewLabel label = new ViewLabel(point.getCalculedX()+shiftInformationFromMouse,point.getCalculedY()-shiftInformationFromMouse,information);
			map.removeAllLabels();
			map.addLabel(label);
			map.repaint();
		}
		else
		{
			if(!map.labelsIsEmpty())
			{
				map.removeAllLabels();
				map.repaint();
			}
		}
	}

}
