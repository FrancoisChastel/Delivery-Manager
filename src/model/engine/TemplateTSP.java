package model.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;



public abstract class TemplateTSP implements TSP {
	
	// used for the time TSP
	private Integer[] meilleureSolutionTime;
	private Date meilleureDate;
	private Date[] datesLivraisonsTime;
	//used for the distance TSP
	private Integer[] meilleureSolutionDistance;
	private int meilleureDistance;
	private Date[] datesLivraisonsDistance;
	
	private Boolean tempsLimiteAtteint;
	private Date[] datesLivraisons;
	
	
	public Boolean getTempsLimiteAtteint(){
		return tempsLimiteAtteint;
	}

	
 	
	public Integer[] getMeilleureSolutionTime() {
		return meilleureSolutionTime;
	}



	public Date[] getDatesLivraisonsTime() {
		return datesLivraisonsTime;
	}



	public Integer[] getMeilleureSolutionDistance() {
		return meilleureSolutionDistance;
	}



	public Date[] getDatesLivraisonsDistance() {
		return datesLivraisonsDistance;
	}



	public void chercheSolution(Date departureDate,int tpsLimite, int nbSommets, int[][] cout,int[][] distances, int[] duree,ArrayList<Pair<Date,Date>> window){
		tempsLimiteAtteint = false;
		meilleureDate = new Date(Integer.MAX_VALUE);
		meilleureDistance = Integer.MAX_VALUE;
		meilleureSolutionTime = new Integer[nbSommets];
		meilleureSolutionDistance = new Integer[nbSommets];
		datesLivraisonsDistance = new Date[nbSommets];
		datesLivraisonsTime = new Date[nbSommets];
		datesLivraisons = new Date[nbSommets];
		ArrayList<Integer> nonVus = new ArrayList<Integer>();
		for (int i=1; i<nbSommets; i++) nonVus.add(i);
		ArrayList<Integer> vus = new ArrayList<Integer>(nbSommets);
		vus.add(0); // le premier sommet visite est 0
		branchAndBound(0, nonVus, vus,cout,distances, duree,window, System.currentTimeMillis(), tpsLimite,departureDate,0);
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
	protected abstract  int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree);
	
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
	 void branchAndBound(int sommetCrt, ArrayList<Integer> nonVus, ArrayList<Integer> vus, int[][] cout,int[][]distances, int[] duree,ArrayList<Pair<Date,Date>> window, long tpsDebut, int tpsLimite,Date actualDate,int actualDistance){
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
	    	datesLivraisons[0] = new Date(actualDate.getTime() + (cout[sommetCrt][0])*1000);
	    	if (actualDate.before(meilleureDate)){ // on a trouve une solution meilleure en temps
	    		vus.toArray(meilleureSolutionTime);
	    		meilleureDate = actualDate;
	    		datesLivraisons[sommetCrt]= actualDate;
	    		datesLivraisonsTime = datesLivraisons;
	    	}
	    	else if(actualDistance<meilleureDistance){ // on a trouve une solution meilleure en distance
	    		vus.toArray(meilleureSolutionDistance);
	    		meilleureDistance = actualDistance;
	    		datesLivraisons[sommetCrt]= actualDate;
	    		datesLivraisonsDistance = datesLivraisons;
	    	}
	    		
		 } else if (checkWindow(window,nonVus,sommetCrt) && ((actualDate.getTime()/1000 + bound(sommetCrt, nonVus, cout, duree) < meilleureDate.getTime()/1000) || ( (actualDistance + bound(sommetCrt, nonVus, distances, new int[duree.length])<meilleureDistance) ))){
	        Iterator<Integer> it = iterator(sommetCrt, nonVus, cout, duree);
	        while (it.hasNext()){
	        	Integer prochainSommet = it.next();
	        	nextDate = updateDate(window, sommetCrt, prochainSommet, cout, duree, actualDate);
	        	if(nextDate != null)
	        	{
	        		vus.add(prochainSommet);
	        		nonVus.remove(prochainSommet);
	        		datesLivraisons[prochainSommet] = nextDate;
	        		branchAndBound(prochainSommet, nonVus, vus, cout,distances, duree,window, tpsDebut, tpsLimite,nextDate,actualDistance + distances[sommetCrt][prochainSommet]);
	        		vus.remove(prochainSommet);
	        		nonVus.add(prochainSommet);
	        	}
	        	else
	        	{
	        		break;
	        	}
	        }
	    }
	}
}

