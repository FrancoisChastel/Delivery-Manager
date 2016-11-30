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
	protected boolean checkWindow(ArrayList<Pair<Date,Date>> window,ArrayList<Integer> nonVus,int sommetCrt,int[] duree){
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
		int coutMin = Integer.MAX_VALUE;
		Iterator<Integer> iter = nonVus.iterator();
		Iterator<Integer> iter2 = nonVus.iterator();
		
		while (iter.hasNext()) {
			Integer iter_val = iter.next();
			int coutMinSommet = Integer.MAX_VALUE;
			
			while (iter2.hasNext()) {
				Integer iter2_val = iter2.next();
				if (cout[iter_val][iter2_val]<coutMin){coutMin=cout[iter_val][iter2_val];}
			}
			coutMin+= coutMinSommet + duree[iter_val];
		}	
		return coutMin;
	}
}