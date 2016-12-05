package model.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;



public abstract class TemplateTSP implements TSP {
	
	private Integer[] meilleureSolution;
	private Date meilleureSolutionDate;
	private Boolean tempsLimiteAtteint;
	private Date departureDate;
	private Date[] datesLivraisons;
	
	public Boolean getTempsLimiteAtteint(){
		return tempsLimiteAtteint;
	}
	
	public Date[] getDatesLivraisons(){
		return datesLivraisons;
	}
 	
	public void chercheSolution(Date departureDate,int tpsLimite, int nbSommets, int[][] cout, int[] duree,ArrayList<Pair<Date,Date>> window){
		tempsLimiteAtteint = false;
		meilleureSolutionDate = new Date(Integer.MAX_VALUE);
		meilleureSolution = new Integer[nbSommets];
		datesLivraisons = new Date[nbSommets];
		this.departureDate= departureDate;
		ArrayList<Integer> nonVus = new ArrayList<Integer>();
		for (int i=1; i<nbSommets; i++) nonVus.add(i);
		ArrayList<Integer> vus = new ArrayList<Integer>(nbSommets);
		vus.add(0); // le premier sommet visite est 0
		branchAndBound(0, nonVus, vus,cout, duree,window, System.currentTimeMillis(), tpsLimite,departureDate);
		
		
	}
	
	public Integer getMeilleureSolution(int i){
		if ((meilleureSolution == null) || (i<0) || (i>=meilleureSolution.length))
			return null;
		return meilleureSolution[i];
	}
	
	public Integer[] getMeilleureSolution(){
		return meilleureSolution;
	}
	
	public Date getDateMeilleureSolution(){
		return meilleureSolutionDate;
	}
	
	/**
	 * Methode devant etre redefinie par les sous-classes de TemplateTSP
	 * @param sommetCourant
	 * @param nonVus : tableau des sommets restant a visiter
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
	 * @param duree : duree[i] = duree pour visiter le sommet i, avec 0 <= i < nbSommets
	 * @return une borne inferieure du cout des permutations commencant par sommetCourant, 
	 * contenant chaque sommet de nonVus exactement une fois et terminant par le sommet 0
	 */
	protected abstract int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree);
	
	protected abstract Date updateDate(ArrayList<Pair<Date,Date>> window,int sommmetCrt,int sommetProchain,int[][] cout,int[] duree,Date actualDate);
	
	protected abstract boolean checkWindow(ArrayList<Pair<Date,Date>> window,ArrayList<Integer> nonVus,int SommetCrt);
	
	/**
	 * Methode devant etre redefinie par les sous-classes de TemplateTSP
	 * @param sommetCrt
	 * @param nonVus : tableau des sommets restant a visiter
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
	 * @param duree : duree[i] = duree pour visiter le sommet i, avec 0 <= i < nbSommets
	 * @return un iterateur permettant d'iterer sur tous les sommets de nonVus
	 */
	protected abstract Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree);
	
	
	//protected abstract boolean checkDeliveryWindow(ArrayList<Integer> nonVus);
	/**
	 * Methode definissant le patron (template) d'une resolution par separation et evaluation (branch and bound) du TSP
	 * @param sommetCrt le dernier sommet visite
	 * @param nonVus la liste des sommets qui n'ont pas encore ete visites
	 * @param vus la liste des sommets visites (y compris sommetCrt)
	 * @param coutVus la somme des couts des arcs du chemin passant par tous les sommets de vus + la somme des duree des sommets de vus
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
	 * @param duree : duree[i] = duree pour visiter le sommet i, avec 0 <= i < nbSommets
	 * @param tpsDebut : moment ou la resolution a commence
	 * @param tpsLimite : limite de temps pour la resolution
	 */	
	 void branchAndBound(int sommetCrt, ArrayList<Integer> nonVus, ArrayList<Integer> vus, int[][] cout, int[] duree,ArrayList<Pair<Date,Date>> window, long tpsDebut, int tpsLimite,Date actualDate){
		 Date nextDate;
		 /*
		 if (System.currentTimeMillis() - tpsDebut > tpsLimite)
		 {
			 tempsLimiteAtteint = true;
			 System.out.println("out of time");
			 return;
		 }
		 */
		 if (nonVus.size() == 0){ // tous les sommets ont ete visites
	    	actualDate = new Date(actualDate.getTime() + cout[sommetCrt][0]);
	    	datesLivraisons[0] = actualDate;
	    	if (actualDate.before(meilleureSolutionDate)){ // on a trouve une solution meilleure que meilleureSolution
	    		vus.toArray(meilleureSolution);
	    		meilleureSolutionDate = actualDate;
	    		datesLivraisons[sommetCrt]= actualDate;
/*	    		for(int i=0;i<datesLivraisons.length;i++)
	    		{
	    			System.out.println("@ Node "+ i + " " + datesLivraisons[i]);
	    		}*/
	    	}
	    	
	    	
		 } else if (vus.get(0) == 0 && checkWindow(window,nonVus,sommetCrt) && (actualDate.getTime()/1000 + bound(sommetCrt, nonVus, cout, duree) < meilleureSolutionDate.getTime()/1000)){
	        Iterator<Integer> it = iterator(sommetCrt, nonVus, cout, duree);
	        while (it.hasNext()){
	        	Integer prochainSommet = it.next();
	        	nextDate = updateDate(window, sommetCrt, prochainSommet, cout, duree, actualDate);
	        	if(nextDate != null)
	        	{
	        		vus.add(prochainSommet);
	        		nonVus.remove(prochainSommet);
	        		datesLivraisons[sommetCrt] = actualDate;
	        		branchAndBound(prochainSommet, nonVus, vus, cout, duree,window, tpsDebut, tpsLimite,nextDate);
	        		vus.remove(prochainSommet);
	        		nonVus.add(prochainSommet);
	        	}
	        }	    
	    }
	}
}

