package abstraction.eq2Producteur2;

//Code écrit par Nathan Rabier

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.Feve;

public class Producteur2Acteur implements IActeur {
	
	protected int cryptogramme;
	protected Journal journal;
	protected Journal journalCC;
	protected Journal journalStocks;
	protected Journal journalProd;
	protected Journal journalBourse;
	
	protected Variable nbHecBasse = new VariablePrivee("Eq2 nombre d'hectare BQ", "Le nombre d'hectare de fèves de basse qualité", this, 300000);
	protected Variable nbHecMoy = new VariablePrivee("Eq2 nombre d'hectare MQ", "Le nombre d'hectare de fèves de moyenne qualité", this, 300000);
	protected Variable nbHecMoyBE = new VariablePrivee("Eq2 nombre d'hectare MQ_BE", "Le nombre d'hectare de fèves de moyenne qualité bio-équitable", this, 250000);
	protected Variable nbHecHauteBE = new VariablePrivee("Eq2 nombre d'hectare HQ_BE", "Le nombre d'hectare de fèves de haute qualité bio_équitable", this, 50000);
	protected Variable stockTotBasse = new VariablePrivee("Eq2 stock tot BQ", "Stock total de fèves de basse qualité", this, 0);
	protected Variable stockTotMoy = new VariablePrivee("Eq2 stock total MQ", "Stock total de fèves de moyenne qualité", this, 0);
	protected Variable stockTotMoyBE = new VariablePrivee("Eq2 stock total MQ_BE", "stock Total de fèves de moyenne qualité bio-équitable", this, 0);
	protected Variable stockTotHauteBE = new VariablePrivee("Eq2 stock total HQ_BE", "stock Total de fèves de haute qualité bio-équitable", this, 0);
	protected Variable tempsDegradationFeve = new VariablePrivee("Eq2 temps degradation feve", "Temps (en nombre d'étapes) avant qu'une Feve ne perdent de la qualité", this, 12);
	protected Variable tempsPerimationFeve = new VariablePrivee("Eq2 temps perimation feve", "Temps (en nombre d'étapes) avant qu'une Feve ne se périme totalement  après avoir perdu une gamme", this, 6);
	protected Variable BQquantiteVendueBourse = new VariablePrivee("Eq2 BQ quantite vendue en bourse","quantite de fèves Vendue en Bourse en BQ par step", this, 0);
	protected Variable MQquantiteVendueBourse = new VariablePrivee("Eq2 MQ quantite vendue en bourse","quantite de fèves Vendue en Bourse en MQ par step", this, 0);
	protected Variable coutStockage = new VariablePrivee("EQ2 cout stockage", "coût du stockage à chaque étape", this, 0);
	protected Variable coutSalaire = new VariablePrivee("EQ2 cout salaire", "coût des salaires à chaque étape", this, 0);
	protected Variable stepsVecuesPourBourseMQ_BE = new VariablePrivee("EQ2 seuil age vente en bourse MQ_BE", "seuil d'ancienneté de vente de MQ_BE en bourse", this, 10);
	protected Variable stepsVecuesPourBourseMQ = new VariablePrivee("EQ2 seuil age vente en bourse MQ", "seuil d'ancienneté de vente obligatoire de MQ en bourse", this, 10);
	protected Variable stepsVecuesPourBourseBQ = new VariablePrivee("EQ2 seuil age vente en bourse BQ", "seuil d'ancienneté de vente obligatoire de BQ en bourse", this, 10);
	protected Variable argentVenteBQ = new VariablePrivee("EQ2 argent gagné par la vente de BQ", "montre l'argent gagné par la vente de BQ à chaque tour", this, 0);
	protected Variable argentVenteMQ = new VariablePrivee("EQ2 argent gagné par la vente de MQ", "montre l'argent gagné par la vente de MQ à chaque tour", this, 0);
	protected Variable argentVenteMQ_BE = new VariablePrivee("EQ2 argent gagné par la vente de MQ_BE", "montre l'argent gagné par la vente de MQ_BE à chaque tour", this, 0);
	protected Variable argentVenteHQ_BE = new VariablePrivee("EQ2 argent gagné par la vente de HQ_BE", "montre l'argent gagné par la vente de HQ_BE à chaque tour", this, 0);
	protected Variable coutProdBQ = new VariablePrivee("EQ2 coût de production et stockage de BQ", "coût total de production et de stockage de BQ à chaque step", this, 0);
	protected Variable coutProdMQ = new VariablePrivee("EQ2 coût de production et stockage de MQ", "coût total de production et de stockage de MQ à chaque step", this, 0);
	protected Variable coutProMQ_BE = new VariablePrivee("EQ2 coût de production et stockage de MQ_BE", "coût total de production et de stockage de MQ_BE à chaque step", this, 0);
	protected Variable coutProdHQ_BE = new VariablePrivee("EQ2 coût de production et stockage de HQ_BE", "coût total de production et de stockage de HQ_BE à chaque step", this, 0);
	protected Variable prodBQ = new VariablePrivee("EQ2 quantite de BQ produite", "quantite de fève de BQ produite à chaque tour", this, 0);
	protected Variable prodMQ = new VariablePrivee("EQ2 quantite de MQ produite", "quantite de fève de MQ produite à chaque tour", this, 0);
	protected Variable prodMQ_BE = new VariablePrivee("EQ2 quantite de MQ_BE produite", "quantite de fève de MQ_BE produite à chaque tour", this, 0);
	protected Variable prodHQ_BE = new VariablePrivee("EQ2 quantite de HQ_BE produite", "quantite de fève de HQ_BE produite à chaque tour", this, 0);
	protected HashMap<Feve, Variable> stepsVecuesPourBourse = new HashMap<Feve, Variable>();
	protected HashMap<Feve, Variable> argentVente = new HashMap<Feve, Variable>();
	protected HashMap<Feve, Variable> coutProdFeve = new HashMap<Feve, Variable>();
	protected HashMap<Feve, Variable> prodFeve = new HashMap<Feve, Variable>();
	protected Producteur2 thisP;

	//Prix pour les contrats cadres
	protected Variable prixBQ = new Variable("EQ2 prix de référence BQ CC", "prix de référence de BQ pour les CC", this, 2200.0*1.01);
	protected Variable prixMQ = new Variable("EQ2 prix de référence MQ CC", "prix de référence de MQ pour les CC", this,3500.0*1.05);
	protected Variable prixMQBE = new Variable("EQ2 prix de référence MQ_BE CC", "prix de référence de MQ_BE pour les CC", this,5000.0*0.99);
	protected Variable prixHQ = new Variable("EQ2 prix de référence HQ_BE CC", "prix de référence de HQ_BE pour les CC", this,10000.0*1.1);
	protected HashMap<Feve, Variable> prixCC;
	protected LinkedList<ExemplaireContratCadre> contrats;
	
	protected Feve[] lesFeves = {Feve.F_BQ, Feve.F_MQ, Feve.F_MQ_BE, Feve.F_HQ_BE};

	public Producteur2Acteur() {
		this.journal = new Journal("Journal " + this.getNom(), this);
		this.journalCC = new Journal("Journal Contrat Cadre " + this.getNom(), this);
		this.journalBourse = new Journal("Journal Bourse " + this.getNom(), this);
		this.journalProd = new Journal("Journal Production " + this.getNom(), this);
		this.journalStocks = new Journal("Journal Stocks " + this.getNom(), this);
		
		this.stepsVecuesPourBourse.put(Feve.F_BQ, this.stepsVecuesPourBourseBQ);
		this.stepsVecuesPourBourse.put(Feve.F_MQ, this.stepsVecuesPourBourseMQ);
		this.stepsVecuesPourBourse.put(Feve.F_MQ_BE, this.stepsVecuesPourBourseMQ_BE);
		
		this.argentVente.put(Feve.F_BQ, this.argentVenteBQ);
		this.argentVente.put(Feve.F_MQ, this.argentVenteMQ);
		this.argentVente.put(Feve.F_MQ_BE, this.argentVenteMQ_BE);
		this.argentVente.put(Feve.F_HQ_BE, this.argentVenteHQ_BE);
		
		this.coutProdFeve.put(Feve.F_BQ, this.coutProdBQ);
		this.coutProdFeve.put(Feve.F_MQ, this.coutProdMQ);
		this.coutProdFeve.put(Feve.F_MQ_BE, this.coutProMQ_BE);
		this.coutProdFeve.put(Feve.F_HQ_BE, this.coutProdHQ_BE);
		
		this.prodFeve.put(Feve.F_BQ, this.prodBQ);
		this.prodFeve.put(Feve.F_MQ, this.prodMQ);
		this.prodFeve.put(Feve.F_MQ_BE, this.prodMQ_BE);
		this.prodFeve.put(Feve.F_HQ_BE, this.prodHQ_BE);
	}
	
	public void initialiser() {
		
		this.prixCC = new HashMap<Feve, Variable>();
		this.getPrixCC().put(Feve.F_BQ, prixBQ);
		this.getPrixCC().put(Feve.F_MQ, prixMQ);
		this.getPrixCC().put(Feve.F_MQ_BE, prixMQBE);
		this.getPrixCC().put(Feve.F_HQ_BE, prixHQ);
		

		this.contrats = new LinkedList<ExemplaireContratCadre>();
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ2";
	}
	
	////////////////////////////////////////////////////////
	//               Getters et setters                   //
	////////////////////////////////////////////////////////

	
	public HashMap<Feve, Variable> getPrixCC(){
		return this.prixCC;
	}

	public double getPrixCC(Feve f){
		return this.prixCC.get(f).getValeur();

	}
	public LinkedList<ExemplaireContratCadre> getContrats(){
		return this.contrats;
	}
	
	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() {
		this.journal.ajouter("Bonjour, nous sommes à l'étape " + Filiere.LA_FILIERE.getEtape() + "et nous n'avons pas encore fait faillite.");
		this.argentVenteBQ.setValeur(this, 0, this.cryptogramme);
		this.argentVenteMQ.setValeur(this, 0, this.cryptogramme);
		this.argentVenteMQ_BE.setValeur(this, 0, this.cryptogramme);
		this.argentVenteHQ_BE.setValeur(this, 0, this.cryptogramme);
	}
	
	// Renvoie la couleur
	public Color getColor() {// NE PAS MODIFIER
		return new Color(244, 198, 156); 
	}
	
	// Renvoie la description
	public String getDescription() {
		return "La filiere CACAindO represente la beaute du savoir-faire indonesien et des richesses de la culture du cacao dans la region. Entre cacao a un prix abordable et feve d'origine volcanique, il y en a pour tous les gouts.";
	}

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		res.add(this.nbHecBasse);
		res.add(this.nbHecMoy);
		res.add(this.nbHecMoyBE);
		res.add(this.nbHecHauteBE);
		res.add(this.prodBQ);
		res.add(this.prodMQ);
		res.add(this.prodMQ_BE);
		res.add(this.prodHQ_BE);
		res.add(this.stockTotBasse);
		res.add(this.stockTotMoy);
		res.add(this.stockTotMoyBE);
		res.add(this.stockTotHauteBE);
		res.add(this.BQquantiteVendueBourse);
		res.add(this.MQquantiteVendueBourse);
		res.add(this.coutStockage);
		res.add(this.coutSalaire);
		res.add(this.argentVenteBQ);
		res.add(this.argentVenteMQ);
		res.add(this.argentVenteMQ_BE);
		res.add(this.argentVenteHQ_BE);
		res.add(this.prixBQ);
		res.add(this.prixMQ);
		res.add(this.prixMQBE);
		res.add(this.prixHQ);
		res.add(this.coutProdBQ);
		res.add(this.coutProdMQ);
		res.add(this.coutProMQ_BE);
		res.add(this.coutProdHQ_BE);
		return res;
	}

	// Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		res.add(this.tempsDegradationFeve);
		res.add(this.tempsPerimationFeve);
		res.add(this.stepsVecuesPourBourseMQ_BE);
		res.add(this.stepsVecuesPourBourseMQ);
		res.add(this.stepsVecuesPourBourseBQ);
		return res;
	}

	// Renvoie les journaux
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(this.journal);
		res.add(this.journalBourse);
		res.add(this.journalCC);
		res.add(this.journalProd);
		res.add(this.journalStocks);
		return res;
	}

	////////////////////////////////////////////////////////
	//               En lien avec la Banque               //
	////////////////////////////////////////////////////////

	// Appelee en debut de simulation pour vous communiquer 
	// votre cryptogramme personnel, indispensable pour les
	// transactions.
	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}

	// Appelee lorsqu'un acteur fait faillite (potentiellement vous)
	// afin de vous en informer.
	public void notificationFaillite(IActeur acteur) {
		if(acteur.getNom().equals("EQ2")) {
			this.journal.ajouter("Adieu monde cruel !");
		} else {
			this.journal.ajouter("RIP " + acteur.getNom() + ", nous ne t'oublierons pas.");
		}
	}

	// Apres chaque operation sur votre compte bancaire, cette
	// operation est appelee pour vous en informer
	public void notificationOperationBancaire(double montant) {
	}
	
	// Renvoie le solde actuel de l'acteur
	public double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme);
	}

	////////////////////////////////////////////////////////
	//        Pour la creation de filieres de test        //
	////////////////////////////////////////////////////////

	// Renvoie la liste des filieres proposees par l'acteur
	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		return(filieres);
	}

	// Renvoie une instance d'une filiere d'apres son nom
	public Filiere getFiliere(String nom) {
		return Filiere.LA_FILIERE;
	}
	
	public String toString() {
		return this.getNom();
	}
	
	////////////////////////////////////////////////////////
	//         Pour prévoir les ventes à venir            //
	////////////////////////////////////////////////////////
	//Code par Nino
	
	
	/*Quantité à livrer au step i pour les ventes par contrat cadre pour la Feve feve*/
	public Double aLivrerStep(int step, Feve feve) {
		return aLivrer(feve).getQuantite(step);
	}
	/*Quantité à livrer aux différents steps pour les ventes par contrat cadre pour la Feve feve*/
	public Echeancier aLivrer(Feve feve) {
		Echeancier ech = new Echeancier(); /*Il faut peut-etre mettre un 0 dans la paranthese de newEchancier() en fonction des tests futurs*/
		for(ExemplaireContratCadre conEx : this.contrats) {
			Echeancier ech2 = conEx.getEcheancier();
			if(conEx.getProduit() == feve) {
				for(int i = ech.getStepDebut(); i<ech2.getStepFin(); i++) {
					ech.set(i, ech.getQuantite(i) + ech2.getQuantite(i));	
				}
			}
		}
		return ech;
	}
}
