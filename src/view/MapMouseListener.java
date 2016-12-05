package view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import model.Tour;

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
			View.displayMessage("Click on "+((ViewPoint)point).getId(), "Debug", JOptionPane.INFORMATION_MESSAGE,null);
			map.repaint();
		}
					
		if(troncon != null)
		{
			String ids = "";
			Iterator<Integer> i = troncon.getIds().iterator();
			
			while (i.hasNext())
			{
				ids+=i.next()+" ";
			}
			View.displayMessage("Clic on "+ids, "Debug", JOptionPane.INFORMATION_MESSAGE,null);
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
			
			map.getMainFrame().getMap().removeAllAlreadyPassedEdges();
			int idTour = map.getSelectedTour();
			if(idTour != -1)
			{
				Tour ti = map.getMainFrame().getView().getController().getModel().getTourById(idTour);
				map.getMainFrame().getMap().displayEdgesAlreadyPassed(ti, point.getId());
			}	
			
			map.repaint();
		}
		else
		{
			if(!map.labelsIsEmpty())
			{
				map.removeAllLabels();
				map.removeAllAlreadyPassedEdges();
				map.repaint();
			}
		}
		
		
		
		//map.getMainFrame().getMap().setTourSelected(idTour);
		map.repaint();
	}

}
