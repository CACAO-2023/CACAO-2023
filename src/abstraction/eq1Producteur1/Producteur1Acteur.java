package abstraction.eq1Producteur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.Lot;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

public class Producteur1Acteur implements IActeur {
	
	protected int cryptogramme;
	protected Variable coutreplantation = new Variable("Cout de replantation", "Cout de replantation pour un hectare, peu importe sa gamme", this, 1000); //choisi arbitrairement
	protected Variable coutmaindoeuvre = new Variable("Cout de la main d'oeuvre", "Cout de la main d'oeuvre par hectare par step", this, 30);
	protected Journal journal;
	protected Journal journal_stocks;
	protected Journal journal_ventes;
	protected Journal journal_champs;
	protected int step;
	protected champ champBas;
	protected champ champMoy;
	protected Lot stockFeveBas;
	protected Lot stockFeveMoy;
	protected Variable Champ = new Variable("Champ totale en hectare ", "Le nombre d'hectare de champ produisant des fèves de moyenne et basse qualité", this, 250000);
	protected Variable Stock = new Variable("Stock totale de fèves en tonne", "La quantité en tonne de fèves séchées de moyenne et basse qualité présentes dans nos stocks", this, 2000);

	public Producteur1Acteur() {
		this.journal = new Journal("Journal "+this.getNom(), this);
		this.journal_stocks = new Journal("Journal : stocks"+this.getNom(), this);
		this.journal_ventes = new Journal("Journal : ventes"+this.getNom(), this);
		this.journal_champs = new Journal("Journal : champs"+this.getNom(), this);
	}
	
	public void initialiser() { //elouan et charles
		this.step = 0;
		this.champBas = new champ();//initialisation de nos champs avec un hectare pour compiler sans bug : à modifier
		for (int i=0; i<40; i++) {
			this.champBas.ajouter(-i*51, 5000.);
		}
		this.champMoy = new champ();//initialisation de nos champs avec un hectare pour compiler sans bug : à modifier
		for (int i=0; i<40; i++) {
			this.champMoy.ajouter(-i*51, 1250.);
		}
		this.stockFeveBas = new Lot(Feve.F_BQ);
		this.stockFeveBas.ajouter(0,1200);
		this.stockFeveMoy = new Lot(Feve.F_MQ);
		this.stockFeveMoy.ajouter(0,300);
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ1";
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() { //elouan
		this.step = this.step + 1;
		// maj des journaux
		this.journal.ajouter("step : "+step);
		this.journal_stocks.ajouter("===== step : "+step+" =====");
		this.journal_stocks.ajouter("Stock bas de gamme : "+this.stockFeveBas.getQuantiteTotale());
		this.journal_stocks.ajouter("Stock moyenne gamme : "+this.stockFeveMoy.getQuantiteTotale());
		this.journal_ventes.ajouter("===== step : "+step+" =====");
		// maj des indicateurs
		this.Stock.setValeur(this, this.stockFeveBas.getQuantiteTotale()+this.stockFeveMoy.getQuantiteTotale());
		this.Champ.setValeur(this, this.champBas.getNbHectare()+this.champMoy.getNbHectare());
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(243, 165, 175); 
	}

	public String getDescription() {
		return "Bla bla bla";
	}

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		res.add(this.Champ);
		res.add(this.Stock);
		return res;
	}

	// Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		res.add(this.coutmaindoeuvre);
		res.add(this.coutreplantation);
		return res;
	}

	// Renvoie les journaux
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(this.journal);
		res.add(this.journal_ventes);
		res.add(this.journal_stocks);
		res.add(this.journal_champs);
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
	public void notificationFaillite(IActeur acteur) { //elouan
		if (this==acteur) {
			System.out.println("bye :( ");
		} else {
			System.out.println("Force à vous "+acteur.getNom());
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
}
