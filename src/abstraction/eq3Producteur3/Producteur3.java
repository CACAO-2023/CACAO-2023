package abstraction.eq3Producteur3;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;

public class Producteur3 extends Bourse3  {
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
	private Double SeuilHG;
	private Double SeuilMG;


	private Champs fields;
	private Integer HectaresLibres; /*Repertorie le nombre d'hectares libres que l'on possede*/
	private Integer HectaresUtilises; /*Repertorie le nombre d'hectares que l'on utilise*/
	private LinkedList<Double> ListeCout; /*Les couts des 18 steps precedents, y compris celui-la*/
	
	/*
	 * Je n'ai pas trouve le type du champs donc j'ai choisit String. A CHANGER
	 * Il faudra aussi penser a se mettre d'accord sur les tailles des champs initiaux
	 */
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public Producteur3() {
		super();
		this.fields = new Champs();
		this.SeuilHG = 0.;
		this.SeuilMG = 0.;

		this.CoutStep = 0.0;
		this.CoutTonne = 0.;
		this.HectaresLibres= 0;
		this.HectaresUtilises=950000;
		this.ListeCout = new LinkedList<Double>();
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void updateListeCout() {
		this.ListeCout.add(this.CoutStep);
		if (ListeCout.size() >= 8) {
			this.ListeCout.removeFirst();
		}
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void updateCoutTonne() {
		Double CoutTotal = 0.;

		if (this.ListeCout.size() == 0) {
			this.CoutTonne = 0.;
			return;
		}

		for (Integer i = 0 ; i < this.ListeCout.size() ; i += 1) {
			CoutTotal += this.ListeCout.get(i);
		}
		CoutTotal = CoutTotal / this.ListeCout.size();


		Stock Stock = this.getStock();
		this.CoutTonne = CoutTotal / Math.max(Stock.getQuantite(), 1);
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void initialiser() {
		super.initialiser();

		this.CoutStep += Stock.getQuantite()*50;
		this.addCoutHectaresUtilises();
		this.updateListeCout();
		this.updateCoutTonne();
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public Champs getFields() {
		return this.fields;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	protected Stock getStock() {
		// TODO Auto-generated method stub
		return this.Stock;
	}
  

	/**
	 * @author BOCQUET Gabriel, Dubus-Chanson Victor, Caugant Corentin
	 */
	public void next() {
		super.next();
		HarvestToStock(Filiere.LA_FILIERE.getEtape());
		this.Stock = Stock.miseAJourStock();

		// Now adding to the step cost the storage costs
		
		updateHectaresLibres(Filiere.LA_FILIERE.getEtape());
		if (Filiere.LA_FILIERE.getEtape() % 12 == 0) {
			if (Filiere.LA_FILIERE.getEtape() != 0) {
				if (Filiere.LA_FILIERE.getEtape() == 12) {
					changeHectaresAndCoutsLies(variationBesoinHectares(Filiere.LA_FILIERE.getEtape()));
				}
			}
		}

		// We only add the costs to CoutStep if we are not at step one :
		if (Filiere.LA_FILIERE.getEtape() > 0) {
			this.CoutStep += Stock.getQuantite()*50;
			this.addCoutHectaresUtilises();
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
	/*Initialise le seuil de HG des 6 premiers mois*/
	public void setSeuilHG(LinkedList<Double> Liste12DernieresVentesHG) {
		Double M = 0.;
		for (Double i : Liste12DernieresVentesHG) {
			M += i;
		}
		this.SeuilHG = M/12;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	/*Initialise le seuil de MG des 6 premiers mois*/
	public void setSeuilMG(LinkedList<Double> Liste12DernieresVentesMG) {
		Double M = 0.;
		for (Double i : Liste12DernieresVentesMG) {
			M += i;
		}
		this.SeuilMG = M/12;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 * @param CurrentStep
	 * @param Liste12DernieresVentes
	 * @param Seuil
	 * @return
	 */
	public Integer besoinHectares(Integer CurrentStep, LinkedList<Double> Liste12DernieresVentes, Double Seuil) {
		Double M12 = 0.;
		Double M4 = 0.;
		Integer besoin = 0;
		Double prix = 0.;
		for (Double i : Liste12DernieresVentes) {
			M12 += i;
		}
		M4 += Liste12DernieresVentes.get(11);
		M4 += Liste12DernieresVentes.get(10);
		M4 += Liste12DernieresVentes.get(9);
		M4 += Liste12DernieresVentes.get(8);
		if (M4 < (Seuil + 5000) && M4 > (Seuil - 10000)) {
			if (M12 > (Seuil + 5000)) {
				prix = M12 - Seuil;
				besoin = (int)(prix / 2500.); //2500euros etant ce que l'on considere comme ce qu'un hectare peut nous rapporter par recolte.
			}
		}
		
		if (M4 > (Seuil + 5000)) {
			if (M12 > (Seuil - 10000)) {
				prix = M12 - Seuil;
				besoin = (int)(prix / 2500.);
			}
		}
		return besoin;
	}
	
	
	/**
	 * @author Dubus-Chanson Victor
	 * @param CurrentStep
	 * @param Liste12DernieresVentesMG
	 * @param Liste12DernieresVentesHG
	 * @return
	 */
	public Integer variationBesoinHectaresv2(Integer CurrentStep, LinkedList<Double> Liste12DernieresVentesMG, LinkedList<Double> Liste12DernieresVentesHG) {
		Integer besoinHG = besoinHectares(CurrentStep, Liste12DernieresVentesHG, this.SeuilHG);
		Integer besoinMG = besoinHectares(CurrentStep, Liste12DernieresVentesHG, this.SeuilMG);
		
		if (besoinHG != 0) {
			this.SeuilHG += besoinHG * 2500.;
			HashMap<Integer, Integer> ChampsH = this.fields.getChamps().get("H");
			ChampsH.put(CurrentStep, besoinHG);
			this.fields.getChamps().put("H", ChampsH);
		}
		
		if (besoinMG != 0) {
			this.SeuilHG += besoinHG * 2500.;
			HashMap<Integer, Integer> ChampsM = this.fields.getChamps().get("M");
			ChampsM.put(CurrentStep, besoinMG);
			this.fields.getChamps().put("M", ChampsM);
		}
		
		return besoinHG + besoinMG;
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
