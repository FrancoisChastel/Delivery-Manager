package model;

import java.util.ArrayList;
import java.util.Iterator;

import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryOrder;
import model.deliverymanager.DeliveryPoint;
import model.engine.LowerCosts;
import model.engine.Pair;
import model.graph.GraphDeliveryManager;
import model.graph.MapNode;
import model.graph.Section;

import java.util.List;

public class Tour {
	private ArrayList<Section> sections;
	private ArrayList<DeliveryPoint> deliveryPoints;
	private int id;
	private static int factoryId = 0;
	private int entrepotId;
	
	/**
	 * Normal constructor
	 * @param sections
	 */
	public Tour(ArrayList<Section> sections, ArrayList<DeliveryPoint> deliveryPoints, int entrepotId)
	{
		this.entrepotId=entrepotId;
		this.sections = sections;
		this.deliveryPoints = deliveryPoints;
		this.id=factoryId;
		factoryId++;
	}
	
	//Getters
	public ArrayList<Section> getSections() { 	return sections;}
	public int getId(){ 						return id; }
	public int getEntrepotId() {				return entrepotId;}
	public ArrayList<DeliveryPoint> getDeliveryPoints(){ return this.deliveryPoints;}
	/**
	 * Get total duration of the whole tour
	 * @return duration of the whole tour
	 */
	public int getTotalDuration()
	{
		int totalDuration = 0;
		
		for (Section section : this.sections)
			totalDuration += section.getLength()/section.getSpeed();
		
		
		for (DeliveryPoint deliveryPoint : this.deliveryPoints)
			totalDuration += deliveryPoint.getDelivery().getDuration();
		
		return totalDuration;
	}
	
	/**
	 * Obtain the duration of the path to reach the id delivery point
	 * @param id to stop the computation of the duration
	 * @return duration of the path
	 */
	public int getDuration(int id)
	{
		return 2;
	}
	
	/**
	 * OBtain the total length of the tour
	 * @return length of the tour
	 */
	public int getTotalLength()
	{
		int totalLength = 0;
		
		for (Section section : this.sections)
			totalLength += section.getLength();
		
		return totalLength;
	}
	
	/**
	 * Obtain the length of the path to reach the id delivery point
	 * @param id
	 * @return
	 */
	public int getLength(int id)
	{
		return 1;
	}
	
	/**
	 * Check if a Point is a delivery point
	 * @param idPoint
	 * @return
	 */
	public boolean isDeliveryPoint(int idPoint)
	{	
		for (DeliveryPoint deliveryPoint : this.deliveryPoints)
			if (deliveryPoint.getMapNodeId() == idPoint)
				return true;
		
		return false;
	}
	
	/**
	 * Calculate the travel time between two points
	 * @param d1
	 * @param d2
	 * @return
	 */
	public long travelTimeBetweenTwoPoints(DeliveryPoint d1, DeliveryPoint d2)
	{
		long duration = 0;
		boolean beginCount=false;
		
		for(Section s : sections)
		{
			if(s.getIdOrigin() == d1.getMapNodeId()) // we begin the sum when we have found the origin d1
				beginCount = true;
			
			if(beginCount) // if the sum has begun
				duration+= s.getLength()/s.getSpeed();
			
			if(s.getIdDestination()==d2.getMapNodeId()) // we stop when we have found the second point
				break;
		}
		
		return duration;
	}
	
	/**
	 * 
	 * @param p1
	 * @param p
	 * @return
	 */
	public long getAvailableTimeBeweenTwoPoints(DeliveryPoint p1, DeliveryPoint p2)
	{
		// Get the time travel between p1 and p2
		long travelTimeSec = travelTimeBetweenTwoPoints(p1,p2);
		
		// Get the effective time between p1 and p2
		long effectiveTime = (p2.getArrivingDate().getTime()/1000) - (p1.getLeavingDate().getTime()/1000);
		
		long waitingTime = effectiveTime-travelTimeSec;
			
		return waitingTime;
	}
	/**
	 * Delete a specific delivery point based on his id
	 * @param deliveryPointsId that will be deleted
	 */
	public void deleteDeliveryPoint(int deliveryPointId, GraphDeliveryManager graphManager) throws Throwable{		
		for (int cursor=0; cursor<this.getSections().size(); cursor++)
			if (this.getSections().get(cursor).getIdOrigin() == deliveryPointId)
			{
				Pair<Integer,Integer> association = getNearestDeliveryPointId(cursor);
				deletePath(association.getFirst(), association.getSecond());
				//this.updateSection(cursor, association.getFirst(), graphManager);	
				System.out.println(this.getSections().toString());
				
				cursor-=association.getSecond()-association.getFirst();
			}
	}
	
	/**
	 * Delete a specific delivery point based on his id
	 * @param deliveryPointsId that will be deleted
	 */
	public void addDeliveryPoint(int index, DeliveryPoint deliveryPoint){	
		this.deliveryPoints.add(index, deliveryPoint);
	}
	
	private Pair<Integer,Integer> getNearestDeliveryPointId(int deliveryPointId){
		int beginning=0;
		int ending=0;
		
		for (int cursor=deliveryPointId; cursor>=-1; cursor--)
			if (this.isDeliveryPoint(this.getSections().get(cursor).getIdOrigin()) || cursor ==-1){
				beginning = cursor;
				break;
			}
		
		for (int cursor=deliveryPointId; cursor<this.getSections().size(); cursor++)
			if (this.isDeliveryPoint(this.getSections().get(cursor).getIdDestination())){
				ending = cursor;
				break;
			}
		
		return new Pair<>(beginning,ending);	
	}
	
	private void deletePath(int beginningId, int endingId)
	{
		for (int cursor=beginningId; cursor<=endingId; cursor++)
			this.getSections().remove(beginningId);
	}
	
	private void updateSection(int sectionIndex, int originDelivery, GraphDeliveryManager graphManager)
	{
		ArrayList<MapNode> solution = LowerCosts.dijkstra(graphManager, 
				this.deliveryPoints.get(originDelivery).getDelivery().getAdress(),
				this.deliveryPoints.get(originDelivery+2).getDelivery().getAdress()).getFirst();
		
		for(int cursor = 0; cursor<solution.size()-1; cursor++)
		{
			this.sections.add(sectionIndex++, graphManager.getSection(solution.get(cursor), solution.get(cursor+1)));
		}
	}
}
