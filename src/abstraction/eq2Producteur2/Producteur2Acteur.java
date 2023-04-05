package abstraction.eq2Producteur2;

//Code écrit par Nathan

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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
	
	protected Feve[] lesFeves = {Feve.F_BQ, Feve.F_MQ, Feve.F_MQ_BE, Feve.F_HQ_BE};

	public Producteur2Acteur() {
	}
	
	public void initialiser() {
		this.journal = new Journal("Journal " + this.getNom(), this);
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
	
	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() {
		this.getJournal().ajouter("Bonjour, nous sommes à l'étape " + Filiere.LA_FILIERE.getEtape() + "et nous n'avons pas encore fait faillite (enfin j'espère).");
		System.out.print(this.journal.toString());
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(244, 198, 156); 
	}

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
		res.add(this.tempsDegradationFeve);
		res.add(this.tempsPerimationFeve);
		return res;
	}

	// Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	// Renvoie les journaux
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
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
}
