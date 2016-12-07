package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
	private int distance;
	private Date backToWarehouse;
	private Date departFromWarehouse;
	
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
		this.distance = distance;
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
	public int deleteDeliveryPoint(int deliveryPointId, GraphDeliveryManager graphManager) throws Throwable{		
		int currentDeliveryPoint = 0;
		Pair<Integer,Integer> association = null;
		
		for (int sectionIndex=0; sectionIndex < this.getSections().size(); sectionIndex++){
			Section section = this.getSections().get(sectionIndex);
			if (this.isDeliveryPoint(section.getIdOrigin()) && this.getDeliveryPoints().get(currentDeliveryPoint).getMapNodeId() == section.getIdOrigin()) 
			{	
				if (section.getIdOrigin() == deliveryPointId)
				{
					association = getNearestDeliveryPointId(sectionIndex);
					this.deletePath(association.getFirst(), association.getSecond());
					this.updateSection(association.getFirst(), currentDeliveryPoint-1, currentDeliveryPoint+1, graphManager);
					break;
				}
				currentDeliveryPoint++;
			}
		}
		this.getDeliveryPoints().remove(currentDeliveryPoint);
		return association.getFirst()+1; 
	}
	
	public void add2DeliveryPoint(int index, DeliveryPoint deliveryPoint, GraphDeliveryManager gdm) throws Exception{	
		DeliveryPoint previous = deliveryPoints.get(index);
		DeliveryPoint next = deliveryPoints.get(index+1);
		
		// previous to new
		Pair<ArrayList<MapNode>, Integer> prevToNew = LowerCosts.dijkstra(gdm, previous.getDelivery().getAdress(), deliveryPoint.getDelivery().getAdress());

		// previous to new
		Pair<ArrayList<MapNode>, Integer> newToNext = LowerCosts.dijkstra(gdm, deliveryPoint.getDelivery().getAdress(), next.getDelivery().getAdress());

		
		// Creating the new ArrayList of sections -----------------------------------------------------
		ArrayList<Section> newSections = new ArrayList<>();
		int deliveryPointIndex = 0; // current visiting point Index in the loop
		int previousPointFirstSectionIndex =-1;
		int nextPointFirstSectionIndex =-1;
		int currIndex = 0;
		
		// get sectionIndex of the previous, and the last
		for(Section section : sections)
		{
			int currOrigin = section.getIdOrigin();
			
			// when we reach a deliveryPoint, then we increment index.
			if(deliveryPointIndex<deliveryPoints.size() && currOrigin == deliveryPoints.get(deliveryPointIndex).getMapNodeId())
			{
				// check if this point is our previous point
				if(currOrigin == previous.getMapNodeId())
					previousPointFirstSectionIndex = currIndex;
				
				// check if this point is our next point
				if(currOrigin == next.getMapNodeId())
				{
					nextPointFirstSectionIndex = currIndex;
					break;
				}
				
				deliveryPointIndex++;
			}	
			currIndex++;			
		}
		
		if(previousPointFirstSectionIndex == -1 || nextPointFirstSectionIndex ==-1)
			throw new Exception("Tour could not find the previous and next point.");
		
		// Creating the new section arrayList --------------
		// sections from begining to previous
		for(int i =0; i<previousPointFirstSectionIndex; i++)
			newSections.add(sections.get(i));

		// sections from previous to new
		for(int j = 0; j<prevToNew.getFirst().size()-1; j++)
			newSections.add(gdm.getSection(prevToNew.getFirst().get(j),prevToNew.getFirst().get(j+1)));
		//newSections.add(gdm.getSection( prevToNew.getFirst().get(prevToNew.getFirst().size()-1), deliveryPoint.getDelivery().getAdress() ) );
		
		
		// sections from new to next
		for(int j = 0; j<newToNext.getFirst().size()-1; j++)
			newSections.add(gdm.getSection(newToNext.getFirst().get(j),newToNext.getFirst().get(j+1)));
		//newSections.add(gdm.getSection( newToNext.getFirst().get(newToNext.getFirst().size()-1), next.getDelivery().getAdress() ) );

		// adding the following of the list
		for(int j = nextPointFirstSectionIndex; j<sections.size();j++)
			newSections.add(sections.get(j));
		
		// Overwriting sections
		sections = newSections;
		//------------------------------------------------------
		
		// Creating the new points -----------------------------
		ArrayList<DeliveryPoint> newDelPoint = new ArrayList<>();
		newDelPoint.addAll(deliveryPoints.subList(0, index+1));
		newDelPoint.add(deliveryPoint);
		newDelPoint.addAll(deliveryPoints.subList(index+1, deliveryPoints.size()));
		// Overwrite del points		
		deliveryPoints = newDelPoint;		
		//------------------------------------------------------
		
		// set the correct date of a point
		System.out.println(prevToNew.getSecond());
		
		long arrivingTimeCurr = previous.getLeavingDate().getTime() + (prevToNew.getSecond()*1000);		
		deliveryPoint.setArrivingDate(new Date(arrivingTimeCurr));
	}
	
	public DeliveryPoint getDeliveryPointById(int nodeId){
		for(int i=0;i<this.getDeliveryPoints().size();i++)
		{
			int nodeAdress = this.getDeliveryPoints().get(i).getDelivery().getAdress().getidNode();
			if(nodeAdress==nodeId)
			{
				return this.getDeliveryPoints().get(i);
			}
		}
		return null;
	}
	
	public Pair<Integer,Integer> getNearestDeliveryPointId(int sectionIndex) throws Throwable{		
		int precedent = 0;
		int next = -1;
		
		for (int cursor=sectionIndex-1; cursor>=0; cursor--){
			if (this.isDeliveryPoint(this.getSections().get(cursor).getIdOrigin()))
			{
				precedent = cursor;
				break;
			}
		}	
		
	
		
		
		for (int cursor=sectionIndex; cursor<this.getSections().size(); cursor++){
			if (this.isDeliveryPoint(this.getSections().get(cursor).getIdDestination()))
			{
				next = cursor;
				break;
			}
			
		}
		return new Pair<Integer, Integer>(precedent, next);
	}
	
	private void deletePath(int beginningId, int endingId)
	{
		for (int cursor=beginningId; cursor<=endingId; cursor++)
			this.getSections().remove(beginningId);		
	}
	
	private void updateSection(int sectionIndex, int deliveryPointA, int deliveryPointB, GraphDeliveryManager graphManager)
	{
		ArrayList<MapNode> solution = LowerCosts.dijkstra(graphManager, 
				this.deliveryPoints.get(deliveryPointA).getDelivery().getAdress(),
				this.deliveryPoints.get(deliveryPointB).getDelivery().getAdress()).getFirst();
		
		for(int cursor = 0; cursor<solution.size()-1; cursor++)
		{
			this.sections.add(sectionIndex+cursor, graphManager.getSection(solution.get(cursor), solution.get(cursor+1)));
		}
	}
	
	public static void resetFactory() { factoryId=0; }
	public Date getBackToWareHouseDate() {return backToWarehouse; }
	public void setDateBackToWarehouse(Date backToWarehouse) {this.backToWarehouse= backToWarehouse; }
	public Date getDepartFromWarehouse() {return departFromWarehouse; }
	public void setDateDepartFromWarehouse(Date startingTime) {
		departFromWarehouse=startingTime;
		
	}
}
