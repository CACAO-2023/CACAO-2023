package abstraction.eq3Producteur3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import abstraction.eqXRomu.produits.Feve;
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
	private Stock Stock;
	/*
	 * Je n'ai pas trouve le type du champs donc j'ai choisit String. A CHANGER
	 * Il faudra aussi penser a se mettre d'accord sur les tailles des champs initiaux.
	 */
	public Producteur3() {
		super();
		this.fields = new Champs();
		this.Stock = new Stock();
		Integer HectaresLibres = 0;
		Integer HectaresUtilises = 950000;
		Integer CoutStep = 0;
	}


	
	public Champs getFields() {
		return this.fields;
	}
	private Stock getStock() {
		// TODO Auto-generated method stub
		return this.Stock;
	}
  
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void addCoutHectaresUtilises() {
		Integer coutEmployes = this.HectaresUtilises * 220;
		this.CoutStep = this.CoutStep + coutEmployes;
	}
	
	public String toString() {
		return this.getNom();
	}
	

	/**
	 * @author BOCQUET Gabriel
	 */
	//Cette fonction ajoute  a chaque step les feves recoltees
	
	public void HarvestToStock(int step) {
		LinkedList<Integer> quantite = this.getFields().HarvestHM(step);
		Stock Stock = this.getStock();
		if(quantite.get(0) > 0) {
		Stock.ajouter(Feve.F_HQ_BE, quantite.get(0));
		}
		else if(quantite.get(1) > 0) {
		Stock.ajouter(Feve.F_MQ_BE, quantite.get(1));
		}
	}


	/**
	 * @author Dubus-Chanson Victor
	 */
	
	/*Calcule le nombre d'Hectares (uniquement positif ou nul) que l'on a besoin de rajouter a la partie cultivee (seulement tous les 6 mois)*/
	/*A modifier, a besoin des quantites de feves echangees (via stock)*/
	public LinkedList<Integer> variationBesoinHectares() {
		Integer BesoinHQ = 0;
		Integer BesoinMQ = 0;
		Stock Stock = this.getStock();
		Double Quantite_HQ_BE= Stock.getQuantite(Feve.F_HQ_BE);
		Double Quantite_MQ_BE= Stock.getQuantite(Feve.F_MQ_BE);
		if (Quantite_HQ_BE < 100) {
			BesoinHQ += 100; /*56 tonnes de plus par an à partir de 5ans*/
		}
		if (Quantite_MQ_BE < 100) {
			BesoinMQ += 100; /*56 tonnes de plus par an à partir de 5ans*/
		}
		LinkedList<Integer> Besoin = new LinkedList<Integer>();
		Besoin.add(BesoinMQ);
		Besoin.add(BesoinHQ);
		return Besoin;
	}
	
	public void achatHectares(Integer HectaresAAcheter) {
		Integer coutAchatHectares = HectaresAAcheter * 3250;
		this.CoutStep = this.CoutStep + coutAchatHectares;
	}
	
	/*Modifie les variables de couts et d'hectares en fonction des resultats de variationBesoinHectares*/
	/*HectaresLiberes vient de la methode destructionVieuxHectares(int CurrentStep) de la classe Champs*/
	public void changeHectaresAndCoutsLies(Integer ajoutHectares, Integer HectaresLiberes) {
		this.HectaresUtilises = this.HectaresUtilises + ajoutHectares;
		this.HectaresLibres = this.HectaresLibres + HectaresLiberes;
		Integer HectaresAAcheter = ajoutHectares - this.HectaresLibres;
		if (HectaresAAcheter > 0) {
			this.achatHectares(HectaresAAcheter);
		}
		this.HectaresLibres = ajoutHectares - this.HectaresLibres;
		if (this.HectaresLibres < 0) {
			this.HectaresLibres = 0;
		}
	}
	

}
