package model.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;




public class TSP1 extends TemplateTSP {

	@Override
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
		return new IteratorSeq(nonVus, sommetCrt);
	}

	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
		return 0;
	}

	@Override
	protected boolean checkWindow(ArrayList<Pair<Date,Date>> window,ArrayList<Integer> nonVus,int sommetCrt){
		return true;
	}

	@Override
	protected Date updateDate(ArrayList<Pair<Date, Date>> window, int sommmetCrt, int sommetProchain, int[][] cout,
			int[] duree, Date actualDate) {
		// TODO Auto-generated method stub
		return actualDate;
	}
	
	
}
