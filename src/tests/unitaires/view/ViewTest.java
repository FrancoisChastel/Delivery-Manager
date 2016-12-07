package tests.unitaires.view;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import model.Model;
import model.Tour;
import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryPoint;
import model.graph.Graph;
import model.graph.GraphDeliveryManager;
import model.graph.MapNode;
import model.graph.Section;
import model.traceroute.Direction;
import model.traceroute.Instruction;
import model.traceroute.TraceRoute;
import view.MainFrame;
import view.Map;
import view.ViewTour;


public class ViewTest {

	
	@Test
	public void MapTourAddSingleTour() {

		// ------Building context -----
		Tour.resetFactory();
		// Points
		ArrayList<MapNode> points = new ArrayList<>();
		points.add(new MapNode(0,300,500));
		points.add(new MapNode(1,300,500));
		points.add(new MapNode(2,300,400));
		points.add(new MapNode(3,200,500));

		
		// Sections
		ArrayList<Section> sections = new ArrayList<>();
		sections.add(new Section(3,0,"s30",200,50));
		sections.add(new Section(0,2,"s02",300,30));
		sections.add(new Section(2,3,"s23",100,20));
		sections.add(new Section(0,1,"s01",100,50));
		sections.add(new Section(2,1,"s21",400,10));
		
			
		// Deliveries
		HashMap<Integer, Delivery> deliveries = new HashMap<Integer, Delivery>();
		deliveries.put(0, new Delivery(0,points.get(1),300));
		deliveries.put(1, new Delivery(1,points.get(2),300));
		
		// SectionsTour
		ArrayList<Section> sectionsTour = new ArrayList<>();
		sectionsTour.add(sections.get(0));
		sectionsTour.add(sections.get(1));
		sectionsTour.add(sections.get(2));
		sectionsTour.add(sections.get(0));
		
		// Delivery Points
		ArrayList<DeliveryPoint> dps = new ArrayList<>();
		dps.add(new DeliveryPoint(deliveries.get(0), new Date()));
		dps.add(new DeliveryPoint(deliveries.get(1), new Date()));
		
		// Create the tour
		Tour tour1 = new Tour(sectionsTour, dps, 3);
		
		// ------End Context ----------
		
		MainFrame main = new MainFrame(null);			
		main.adapte(points, sections);
		
		main.displayTour(tour1);
		
		ViewTour vt1 = main.getMap().getViewTour(tour1.getId());
		
		assertEquals(vt1.toString(),"tour:{id:0, store:3, concernedPoints:[{id:1, color:colored}, {id:2, color:colored}]");
	}
	
	
	@Test
	public void MapTourDeleteAPoint() {

		Model model = new Model(null);
		GraphDeliveryManager graphDelMan = model.getGraphDeliveryManager();
		
		// ------Building context -----
		Tour.resetFactory();
		// Points
		ArrayList<MapNode> points = new ArrayList<>();
		points.add(new MapNode(0,300,500));
		points.add(new MapNode(1,300,500));
		points.add(new MapNode(2,300,400));
		points.add(new MapNode(3,200,500));

		
		// Sections
		ArrayList<Section> sections = new ArrayList<>();
		sections.add(new Section(3,0,"s30",200,50));
		sections.add(new Section(0,2,"s02",300,30));
		sections.add(new Section(2,3,"s23",100,20));
		sections.add(new Section(0,1,"s01",100,50));
		sections.add(new Section(2,1,"s21",400,10));
		
			
		// Add indo graph del man

		// SectionsTour
		ArrayList<Section> sectionsTour = new ArrayList<>();
		sectionsTour.add(sections.get(0));
		sectionsTour.add(sections.get(1));
		sectionsTour.add(sections.get(2));
		sectionsTour.add(sections.get(0));
		
		// Delivery Points
		ArrayList<DeliveryPoint> dps = new ArrayList<>();
		dps.add(new DeliveryPoint( new Delivery(0,points.get(1),300), new Date()));
		dps.add(new DeliveryPoint( new Delivery(1,points.get(2),300), new Date()));
		
		// Create the tour
		Tour tour1 = new Tour(sectionsTour, dps, 3);
		
		// ------End Context ----------
		int deliveryPointCount = tour1.getDeliveryPoints().size();
		
		MainFrame main = new MainFrame(null);			
		main.adapte(points, sections);
		
		main.displayTour(tour1);
		
		ViewTour vt1 = main.getMap().getViewTour(tour1.getId());
		
		assertEquals("1 2 ", getIdStringFromTour(tour1));
		assertEquals("tour:{id:0, store:3, concernedPoints:[{id:1, color:colored}, {id:2, color:colored}]",vt1.toString());
		assertEquals(2,deliveryPointCount);
		
		try {
			tour1.deleteDeliveryPoint(1, graphDelMan);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		main.displayTour(tour1);
		vt1 = main.getMap().getViewTour(tour1.getId());
		
		assertEquals("2 ", getIdStringFromTour(tour1));
		assertEquals(deliveryPointCount-1,tour1.getDeliveryPoints().size());
		assertEquals("tour:{id:0, store:3, concernedPoints:[{id:2, color:colored}]",vt1.toString());

	}
	
	public static String getIdStringFromTour(Tour tour)
	{
		//Checking id of tour
		String ids="";
		for(DeliveryPoint dp : tour.getDeliveryPoints())
			ids += dp.getMapNodeId()+" ";
		return ids;
	}
	
	@Test
	public void MapTourAddTwoTour() {

		// ------Building context -----
		Tour.resetFactory();
		// Points
		ArrayList<MapNode> points = new ArrayList<>();
		points.add(new MapNode(0,300,500));
		points.add(new MapNode(1,300,500));
		points.add(new MapNode(2,300,400));
		points.add(new MapNode(3,200,500));

		
		// Sections
		ArrayList<Section> sections = new ArrayList<>();
		sections.add(new Section(3,0,"s30",200,50));
		sections.add(new Section(0,2,"s02",300,30));
		sections.add(new Section(2,3,"s23",100,20));
		sections.add(new Section(0,1,"s01",100,50));
		sections.add(new Section(2,1,"s21",400,10));
		sections.add(new Section(1,2,"s12",400,10));
		
		// TOUR 1
		// Deliveries
		HashMap<Integer, Delivery> deliveries = new HashMap<Integer, Delivery>();
		deliveries.put(0, new Delivery(0,points.get(1),300));
		deliveries.put(1, new Delivery(1,points.get(2),300));
		
		// SectionsTour
		ArrayList<Section> sectionsTour = new ArrayList<>();
		sectionsTour.add(sections.get(0));
		sectionsTour.add(sections.get(1));
		sectionsTour.add(sections.get(2));
		sectionsTour.add(sections.get(0));
		
		// Delivery Points
		ArrayList<DeliveryPoint> dps = new ArrayList<>();
		dps.add(new DeliveryPoint(deliveries.get(0), new Date()));
		dps.add(new DeliveryPoint(deliveries.get(1), new Date()));
		
		// Create the tour
		Tour tour1 = new Tour(sectionsTour, dps, 3);
		
		// TOUR 2
		// Deliveries
		HashMap<Integer, Delivery> deliveries2 = new HashMap<Integer, Delivery>();
		deliveries2.put(0, new Delivery(0,points.get(0),300));
		deliveries2.put(1, new Delivery(1,points.get(1),300));
		
		// SectionsTour
		ArrayList<Section> sectionsTour2 = new ArrayList<>();
		sectionsTour2.add(sections.get(0));
		sectionsTour2.add(sections.get(3));
		sectionsTour2.add(sections.get(5));
		sectionsTour2.add(sections.get(2));
		
		// Delivery Points
		ArrayList<DeliveryPoint> dps2 = new ArrayList<>();
		dps2.add(new DeliveryPoint(deliveries2.get(0), new Date()));
		dps2.add(new DeliveryPoint(deliveries2.get(1), new Date()));
		
		// Create the tour
		Tour tour2 = new Tour(sectionsTour2, dps2, 3);
		// ------End Context ----------
		
		MainFrame main = new MainFrame(null);			
		main.adapte(points, sections);
		
		main.displayTour(tour1);
		main.displayTour(tour2);
		ViewTour vt1 = main.getMap().getViewTour(tour1.getId());
		ViewTour vt2 = main.getMap().getViewTour(tour2.getId());

		assertEquals("tour:{id:0, store:3, concernedPoints:[{id:1, color:colored}, {id:2, color:colored}]",vt1.toString());
		assertEquals("tour:{id:1, store:3, concernedPoints:[{id:0, color:colored}, {id:1, color:colored}]",vt2.toString());
	}
	
}
