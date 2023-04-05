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

public class Producteur2Acteur implements IActeur {
	
	protected int cryptogramme;
	protected Journal journal;
	
	protected Variable nbHecBasse;
	protected Variable nbHecMoy;
	protected Variable nbHecMoyBE;
	protected Variable nbHecHauteBE;
	protected Variable prodHec;
	protected Variable stockTotBasse;
	protected Variable stockTotMoy;
	protected Variable stockTotMoyBE;
	protected Variable stockTotHauteBE;

	public Producteur2Acteur() {
	}
	
	public void initialiser() {
		this.journal = new Journal("Journal " + this.getNom(), this);
		
		this.nbHecBasse = new VariablePrivee("nbHecBasse", "Le nombre d'hectare de fèves de basse qualité", this, 100);
		this.nbHecMoy = new VariablePrivee("nbHecMoy", "Le nombre d'hectare de fèves de moyenne qualité", this, 100);
		this.nbHecMoyBE = new VariablePrivee("nbHecMoyBE", "Le nombre d'hectare de fèves de moyenne qualité bio-équitable", this, 100);
		this.nbHecHauteBE = new VariablePrivee("nbHecHaute", "Le nombre d'hectare de fèves de basse qualité", this, 100);
		
		this.prodHec = new Variable("prodHec", "La production moyenne de feve en tonne par hectare par récolte", this, 0.56);
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ2";
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() {
		this.journal.ajouter("Bonjour, nous sommes à l'étape " + Filiere.LA_FILIERE.getEtape() + "et nous n'avons pas encore fait faillite (enfin j'espère).");
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
