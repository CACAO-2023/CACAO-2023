package abstraction.eq3Producteur3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class Producteur3 extends Producteur3Acteur  {
	

	private Champs fields;
	private Integer HectaresLibres; /*Repertorie le nombre d'hectares que l'on possede*/
	private Integer HectaresUtilises; /*Repertorie le nombre d'hectares que l'on utilise*/
	private Integer CoutStep; /* Tout nos couts du step, reinitialises a zero au debut de chaque step et payes a la fin du step*/
	private Producteur3Stock Stock;
	/*
	 * Je n'ai pas trouve le type du champs donc j'ai choisit String. A CHANGER
	 * Il faudra aussi penser a se mettre d'accord sur les tailles des champs initiaux.
	 */
	public Producteur3() {
		super();
		this.fields = new Champs();
		this.Stock = new Producteur3Stock();
		Integer HectaresLibres = 0;
		Integer HectaresUtilises = 950000;
		Integer CoutStep = 0;
	}
	
	public Champs getFields() {
		return this.fields;
	}
	private Producteur3Stock getStock() {
		// TODO Auto-generated method stub
		return this.Stock;
	}
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void addCoutHectaresUtilises() {
		Integer coutEmployes = HectaresUtilises * 220;
		this.CoutStep = this.CoutStep + coutEmployes;
	}
	
	/**
	 * @author BOCQUET Gabriel
	 */
	//Cette fonction ajoute  a chaque step les feves recoltees
	
	public void HarvestToStock(int step) {
		LinkedList<Integer> quantite = this.getFields().HarvestHM(step);
		Producteur3Stock Stock = this.getStock();
		if(quantite.get(0) > 0) {
		Stock.ajouterH(step, quantite.get(0));
		}
		else if(quantite.get(1) > 0) {
		Stock.ajouterM(step, quantite.get(1));
		}
	}


}
