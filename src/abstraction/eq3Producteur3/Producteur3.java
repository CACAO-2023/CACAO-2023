package abstraction.eq3Producteur3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import abstraction.eqXRomu.produits.Lot;

public class Producteur3 extends Producteur3Acteur  {
	/*
	 * ChampsH est un dictionnaire qui contient les champs Haut de Gamme
	 * On associe a un ensemble d'hectars un int qui correspond  leur step de plantaison 
	 *
	 *private HashMap<Integer,String> ChampsH;//UTILE ?
	 *
	 * ChampsM est un dictionnaire qui contient les champs Moyen de Gamme
	 * On associe a un ensemble d'hectars un int qui correspond  leur step de plantaison 
	 *
	 *private HashMap<Integer,String> ChampsM;//UTILE ?
	 *
	 * On cree un dictionnaire qui associe  la clef H ou M le dico ChampsM ou ChapmsH
	 */
	private HashMap<String,HashMap> Champs;
	

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
  
	public Producteur3(HashMap<String,HashMap> m) {
		this.Champs=m;
	}
	
	/*

	 * @author Dubus-Chanson Victor
	 */
	public void addCoutHectaresUtilises() {
		Integer coutEmployes = this.HectaresUtilises * 220;
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



	/*Calcule le nombre d'Hectares (uniquement positif ou nul) que l'on a besoin de rajouter a la partie cultivee (seulement tous les 6 mois)*/
	/*A modifier, a besoin des quantites de feves echangees (via stock)*/
	public Integer variationBesoinHectares() {
		Integer NbHectares = 0;
		return NbHectares;
	}
	
	public void achatHectares(Integer HectaresAAcheter) {
		Integer coutAchatHectares = HectaresAAcheter * 3250;
		this.CoutStep = this.CoutStep + coutAchatHectares;
	}
	
	/*Modifie les variables de couts et d'hectares en fonction des resultats de variationBesoinHectares*/
	public void changeHectaresAndCoutsLies(Integer ajoutHectares, Integer HectaresLiberes) {
		this.HectaresUtilises = this.HectaresUtilises + ajoutHectares;
		this.HectaresLibres = this.HectaresLibres + HectaresLiberes;
		Integer HectaresAAcheter = this.HectaresLibres - ajoutHectares;
		if (HectaresAAcheter > 0) {
			this.achatHectares(HectaresAAcheter);
		}
		this.HectaresUtilises = this.HectaresUtilises + HectaresAAcheter;
		this.HectaresLibres = this.HectaresLibres - ajoutHectares;
		if (this.HectaresLibres < 0) {
			this.HectaresLibres = 0;
		}
	}
	

}
