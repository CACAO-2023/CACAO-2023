package abstraction.eq2Producteur2;

//Code écrit par Nathan

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.ContratCadre;
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
	
	protected Variable nbHecBasse = new VariablePrivee("nbHecBasse", "Le nombre d'hectare de fèves de basse qualité", this, 100);
	protected Variable nbHecMoy = new VariablePrivee("nbHecMoy", "Le nombre d'hectare de fèves de moyenne qualité", this, 100);
	protected Variable nbHecMoyBE = new VariablePrivee("nbHecMoyBE", "Le nombre d'hectare de fèves de moyenne qualité bio-équitable", this, 100);
	protected Variable nbHecHauteBE = new VariablePrivee("nbHecHaute", "Le nombre d'hectare de fèves de basse qualité", this, 100);
	protected Variable prodHec = new VariablePrivee("prodHec", "La production moyenne de feve en tonne par hectare par récolte", this, 0.56);
	protected Variable stockTotBasse = new VariablePrivee("stockTotBasse", "Stock total de fèves de basse qualité", this, 0);
	protected Variable stockTotMoy = new VariablePrivee("stockTotMoy", "Stock total de fèves de moyenne qualité", this, 0);
	protected Variable stockTotMoyBE = new VariablePrivee("stockTotMoyBE", "stock Total de fèves de moyenne qualité bio-équitable", this, 0);
	protected Variable stockTotHauteBE = new VariablePrivee("stockTotHauteBE", "stock Total de fèves de haute qualité bio-équitable", this, 0);
	protected Variable tempsDegradationFeve = new VariablePrivee("tempsDegradationFeve", "Temps (en nombre d'étapes) avant qu'une Feve ne perdent de la qualité", this, 12);
	protected Variable tempsPerimationFeve = new VariablePrivee("tempsPerimationFeve", "Temps (en nombre d'étapes) avant qu'une Feve ne se périme totalement  après avoir perdu une gamme", this, 6);
	protected Variable coutMoyenStock = new VariablePrivee("cout moyen stockage", "Cout moyen du stockage d'une tonne de fève pour un step", this, 1.5);
	
	//Prix provisoires
	public double prixMinBQ = 0.0;
	public double prixMinMQ = 0.0;
	public double prixMinMQBE = 0.0;
	public double prixMinHQ = 0.0;
	public HashMap<Feve, Double> prixMinCC;
	public double prixBQ = 1.0;
	public double prixMQ = 2.0;
	public double prixMQBE = 3.0;
	public double prixHQ = 4.0;
	public HashMap<Feve, Double> prixCC;
	
	protected LinkedList<ExemplaireContratCadre> contrats;
	
	
	protected Feve[] lesFeves = {Feve.F_BQ, Feve.F_MQ, Feve.F_MQ_BE, Feve.F_HQ_BE};

	public Producteur2Acteur() {
		this.journal = new Journal("Journal " + this.getNom(), this);
	}
	
	public void initialiser() {
		
		this.prixCC = new HashMap<Feve, Double>();
		this.prixMinCC = new HashMap<Feve, Double>();
		this.getPrixCC().put(Feve.F_BQ, prixBQ);
		this.getPrixCC().put(Feve.F_MQ, prixMQ);
		this.getPrixCC().put(Feve.F_MQ_BE, prixMQBE);
		this.getPrixCC().put(Feve.F_HQ_BE, prixHQ);
		this.getPrixMinCC().put(Feve.F_BQ, prixMinBQ);
		this.getPrixMinCC().put(Feve.F_MQ, prixMinMQ);
		this.getPrixMinCC().put(Feve.F_MQ_BE, prixMinMQBE);
		this.getPrixMinCC().put(Feve.F_HQ_BE, prixMinHQ);
		
		this.contrats = new LinkedList<ExemplaireContratCadre>();
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ2";
	}
	
	////////////////////////////////////////////////////////
	//               Getters et setters                   //
	////////////////////////////////////////////////////////

	public Journal getJournal() {
		return this.journal;
	}
	protected Variable getNbHecBasse() {
		return this.nbHecBasse;
	}
	protected Variable getNbHecMoy() {
		return this.nbHecMoy;
	}
	protected Variable getNbHecMoyBE() {
		return this.nbHecMoyBE;
	}
	protected Variable getNbHecHauteBE() {
		return this.nbHecHauteBE;
	}
	protected Variable getProdHec() {
		return this.prodHec;
	}
	public HashMap<Feve, Double> getPrixMinCC(){
		return this.prixMinCC;
	}
	public HashMap<Feve, Double> getPrixCC(){
		return this.prixCC;
	}
	public double getPrixMinCC(Feve f){
		return this.prixMinCC.get(f);
	}
	public double getPrixCC(Feve f){
		return this.prixCC.get(f);
	}
	public LinkedList<ExemplaireContratCadre> getContrats(){
		return this.contrats;
	}
	
	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() {
		this.getJournal().ajouter("Bonjour, nous sommes à l'étape " + Filiere.LA_FILIERE.getEtape() + "et nous n'avons pas encore fait faillite (enfin j'espère).");
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
		res.add(this.stockTotBasse);
		res.add(this.stockTotMoy);
		res.add(this.stockTotMoyBE);
		res.add(this.stockTotHauteBE);
		return res;
	}

	// Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		res.add(this.tempsDegradationFeve);
		res.add(this.tempsPerimationFeve);
		res.add(this.coutMoyenStock);
		return res;
	}

	// Renvoie les journaux
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(this.getJournal());
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
	
	/*Quantité à livrer au step i pour les ventes par contrat cadre pour la Feve feve*/
	public Double aLivrerStep(int step, Feve feve) {
		return aLivrer(feve).getQuantite(step);
	}
	/*Quantité à livrer aux différents steps pour les ventes par contrat cadre pour la Feve feve*/
	public Echeancier aLivrer(Feve feve) {
		Echeancier ech = new Echeancier(); /*Il faut peut-etre m'etre un 0 dans la paranthese de newEchancier() en fonction des tests futurs*/
		for(ExemplaireContratCadre conEx : this.contrats) {
			Echeancier ech2 = conEx.getEcheancier();
			if(conEx.getProduit() == feve) {
				for(int i = Filiere.LA_FILIERE.getEtape(); i<ech2.getStepFin(); i++) {
					ech.set(i, ech.getQuantite(i) + ech2.getQuantite(i));	
				}
			}
		}
		return ech;
	}
}
