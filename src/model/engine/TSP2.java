package model.engine;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


public class TSP2 extends TemplateTSP {

	@Override
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
		return new IteratorSeq(nonVus, sommetCrt);
	}

	@Override
	protected Date updateDate(ArrayList<Pair<Date,Date>> window,int sommmetCrt,int sommetProchain,int[][] cout,int[] duree,Date actualDate)
	{
		//System.out.println("Node " + sommmetCrt + " to " + sommetProchain);
		Date limit;
		Date in;
		long deliveryTime = actualDate.getTime()+(cout[sommmetCrt][sommetProchain]+duree[sommmetCrt])*1000;
		Date delivery = new Date(deliveryTime);
		limit = window.get(sommetProchain).getSecond();
		
		if(limit != null)
		{
			
			in = window.get(sommetProchain).getFirst();
			if(!delivery.after(limit))
			{
				if(delivery.before(in))
				{
					//System.out.println("wait for " + (in.getTime()-delivery.getTime())/1000 + " @ node " + sommetProchain);
					return in;
				}
				return delivery;
			}

			return null;
		}
		return delivery;
	}
	@Override
	protected boolean checkWindow(ArrayList<Pair<Date,Date>> window,ArrayList<Integer> nonVus,int sommetCrt){
		if(window.get(sommetCrt).getFirst() != null)
		{	
			Date in = window.get(sommetCrt).getFirst();
			Date out = window.get(sommetCrt).getSecond();
			for(int i : nonVus)
			{
				if(window.get(i).getFirst() != null)
				{
					if(window.get(i).getFirst().before(in) && window.get(i).getSecond().before(out))
					{
						return false;
					}
				}
			}
			
			
		}
		return true;
	}
	
	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
		ArrayList<Integer> sommetRestants = new ArrayList<>(nonVus);
		int sommetCrt = sommetCourant; // sommet de départ
		int bestNext = 0; // eventuel sommet suivant
		int coutMin=duree[sommetCourant]; // cout mini total
		
		
		while(!sommetRestants.isEmpty())
		{
			int coutArc=Integer.MAX_VALUE; // cout pour chaque parcours le plus court
			for(Integer next : sommetRestants)
			{
				if(cout[sommetCrt][next]<coutArc)
				{
					bestNext = next;
					coutArc=cout[sommetCrt][next];
				}
			
			}
			coutMin = coutMin + coutArc + duree[bestNext];
			sommetRestants.remove((Object)new Integer(bestNext));
			sommetCrt = bestNext;
			
		}
		return coutMin+cout[bestNext][0];
	}
}