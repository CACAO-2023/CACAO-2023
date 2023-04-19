package abstraction.eq3Producteur3;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;

public class Producteur3 extends Bourse3  {

<<<<<<< HEAD
	
	
=======
	/**
	 * @author Dubus-Chanson Victor, Bocquet Gabriel
	 */
>>>>>>> branch 'Desov2suisUnBoulet' of https://github.com/Dahan13/CACAO-2023
	private HashMap<String,HashMap> Champs;
	


	private Champs fields;
	private Integer HectaresLibres; /*Repertorie le nombre d'hectares libres que l'on possede*/
	private Integer HectaresUtilises; /*Repertorie le nombre d'hectares que l'on utilise*/
	private LinkedList<Double> ListeCout; /*Les couts des 18 steps precedents, y compris celui-la*/
<<<<<<< HEAD
	
	/*
	 * Je n'ai pas trouve le type du champs donc j'ai choisit String. A CHANGER
	 * Il faudra aussi penser a se mettre d'accord sur les tailles des champs initiaux.
	 */
=======
	private Double CoutTonne; /*Le cout par tonne de cacao, calcule sur 18 step (destruction de la feve apres 9 mois), le meme pour toute gamme*/

>>>>>>> branch 'Desov2suisUnBoulet' of https://github.com/Dahan13/CACAO-2023
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public Producteur3() {
		super();
		this.fields = new Champs();
		

		this.CoutStep = 0.0;
		this.CoutTonne = 0.;
		this.HectaresLibres= 0;
		this.HectaresUtilises=950000;
		this.ListeCout = new LinkedList<Double>();
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
		this.ListeCout.add(0.);
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void updateListeCout() {
		this.ListeCout.addLast(this.CoutStep);
		this.ListeCout.removeFirst();
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void updateCoutTonne() {
		Double CoutTotal = 0.;
		for (Integer i = 0 ; i < this.ListeCout.size() ; i += 1) {
			CoutTotal += this.ListeCout.get(i);
		}
		Stock Stock = this.getStock();
		this.CoutTonne = CoutTotal / Stock.getQuantite();
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void initialiser() {
		super.initialiser();
		new Producteur3();		
	}
	
<<<<<<< HEAD
	/**
	 * @author Dubus-Chanson Victor
	 */
	public Champs getFields() {
=======
	protected Champs getFields() {
>>>>>>> branch 'Desov2suisUnBoulet' of https://github.com/Dahan13/CACAO-2023
		return this.fields;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	protected Stock getStock() {
		// TODO Auto-generated method stub
		return this.Stock;
	}
	protected Integer getHectaresUt() {
		return this.HectaresUtilises;
	}

	/**
	 * @author BOCQUET Gabriel, Dubus-Chanson Victor, Caugant Corentin
	 */
	public void next() {
		super.next();
		HarvestToStock(Filiere.LA_FILIERE.getEtape());
		this.Stock = Stock.miseAJourStock();

		// Now adding to the step cost the storage costs
		this.CoutStep += Stock.getQuantite()*50;
		updateHectaresLibres(Filiere.LA_FILIERE.getEtape());
		if (Filiere.LA_FILIERE.getEtape() % 12 == 0) {
			changeHectaresAndCoutsLies(variationBesoinHectares(Filiere.LA_FILIERE.getEtape()));
		}

		this.updateListeCout();
		this.updateCoutTonne();
		this.getJAchats().ajouter(Color.yellow, Color.BLACK, "Coût du step : " + this.CoutStep);
		this.getJGeneral().ajouter(Color.cyan, Color.BLACK, 
				"Step Actuelle : " + Filiere.LA_FILIERE.getEtape()+", Taille total des Champs utilisés : "+ this.HectaresUtilises+", Taille des champs libres" + this.HectaresLibres + ", Nombre d'employe : Pas encore calculé"+ this.HectaresUtilises);
		
		Filiere.LA_FILIERE.getBanque().virer(this, super.getCryptogramme(), Filiere.LA_FILIERE.getBanque(), CoutStep);
		this.getJOperation().ajouter(Color.cyan, Color.BLACK, "On a paye "+ this.CoutStep + "euros de frais divers");
		this.CoutStep = 0.0;

	}
	/*


	
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
		if(quantite.get(1) > 0) {
		Stock.ajouter(Feve.F_MQ_BE, quantite.get(1));
		}
		this.getJStock().ajouter(Color.GREEN, Color.BLACK,"On a ajoute "+ quantite.get(1) +" tonnes au stock de Moyenne Gamme le step n°"  +Filiere.LA_FILIERE.getEtape());
		this.getJStock().ajouter(Color.GREEN, Color.BLACK,"A l'étape "  +Filiere.LA_FILIERE.getEtape() + " les stocks de Moyenne Gamme sont de " + this.getStock().getQuantite(Feve.F_MQ_BE));
		this.getJStock().ajouter(Color.LIGHT_GRAY, Color.BLACK,"On a ajoute "+ quantite.get(0) +"tonnes au stock de Haute Gamme le step n°"  +Filiere.LA_FILIERE.getEtape());
		this.getJStock().ajouter(Color.LIGHT_GRAY, Color.BLACK,"A l'étape "  +Filiere.LA_FILIERE.getEtape() + " les stocks de Haute Gamme sont de " + this.getStock().getQuantite(Feve.F_HQ_BE));
	}


	/**
	 * @author Dubus-Chanson Victor
	 */
	
	/*Calcule le nombre d'Hectares (uniquement positif ou nul) que l'on a besoin de rajouter a la partie cultivee (seulement tous les 6 mois)*/
	/*A modifier, a besoin des quantites de feves echangees (via stock)*/
	
	public Integer variationBesoinHectares(Integer CurrentStep) {
		Integer BesoinHQ = 0;
		Integer BesoinMQ = 0;
		Stock Stock = this.getStock();
		Double Quantite_HQ_BE= Stock.getQuantite(Feve.F_HQ_BE);
		Double Quantite_MQ_BE= Stock.getQuantite(Feve.F_MQ_BE);
		if (Quantite_HQ_BE < 50000) {
			BesoinHQ += 1000; /*560 tonnes de plus par an à partir de 5ans*/
			HashMap<Integer, Integer> ChampsH = this.fields.getChamps().get("H");
			ChampsH.put(CurrentStep, BesoinHQ);
			this.fields.getChamps().put("H", ChampsH);
		}
		if (Quantite_MQ_BE < 500000) {
			BesoinMQ += 1000; /*560 tonnes de plus par an à partir de 5ans*/
			HashMap<Integer, Integer> ChampsM = this.fields.getChamps().get("M");
			ChampsM.put(CurrentStep, BesoinMQ);
			this.fields.getChamps().put("M", ChampsM);
		}
		/*LinkedList<Integer> Besoin = new LinkedList<Integer>();
		Besoin.add(BesoinMQ);
		Besoin.add(BesoinHQ);
		return Besoin;*/
		return BesoinHQ + BesoinMQ;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void achatHectares(Integer HectaresAAcheter) {
		Integer coutAchatHectares = HectaresAAcheter * 3250;
		this.CoutStep = this.CoutStep + coutAchatHectares;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	/*A faire a chaque step et tous les 6mois avant changeHectaresAndCoutsLies*/
	public void updateHectaresLibres(Integer CurrentStep) {
		Champs Champs = this.getFields();
		Integer HectaresLiberes = Champs.destructionVieuxHectares(CurrentStep);
		this.HectaresLibres += HectaresLiberes;
		this.HectaresUtilises -= HectaresLiberes;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	/*Modifie les variables de couts et d'hectares en fonction des resultats de variationBesoinHectares*/
	public void changeHectaresAndCoutsLies(Integer ajoutHectares) {
		this.HectaresUtilises = this.HectaresUtilises + ajoutHectares;
		Integer HectaresAAcheter = ajoutHectares - this.HectaresLibres;
		if (HectaresAAcheter > 0) {
			this.achatHectares(HectaresAAcheter);
		}
		this.HectaresLibres = this.HectaresLibres - ajoutHectares;
		if (this.HectaresLibres < 0) {
			this.HectaresLibres = 0;
		}
	}
}
