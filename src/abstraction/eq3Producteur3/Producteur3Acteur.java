package abstraction.eq3Producteur3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;

public class Producteur3Acteur implements IActeur {
	
	protected int cryptogramme;
	protected Journal journal_operationsbancaires;
    protected Journal journal_ventes;
    protected Journal journal_achats;
    protected Journal journal_activitegenerale;
    protected Journal journal_Stock;
    protected Journal journal_catastrophe;
    protected Journal journal_plantation;
    protected Stock Stock;
	protected Double CoutStep; /* Tout nos couts du step, reinitialises a zero au debut de chaque step et payes a la fin du step*/
	protected Double CoutTonne; /*Le cout par tonne de cacao, calcule sur 18 step (destruction de la feve apres 9 mois), le meme pour toute gamme*/
	
	public Variable StockFeveH ;
	public Variable StockFeveM ;
	public Variable StockFeveB ;
	public Variable tailleH;
	public Variable tailleM;
	
	protected LinkedList<Double> VentesMG; /*Les 12 quantités des dernières ventes de moyens de gammes*/
	protected LinkedList<Double> VentesHG; /*Les 12 quantités des dernières ventes de hauts de gammes*/
	
	protected Champs fields;

	/**
	 * @author BOCQUET Gabriel, Corentin Caugant
	 */
	public Producteur3Acteur() {

	String nom = "Equipe 3";
	this.fields = new Champs();
	journal_operationsbancaires=new Journal("Journal des Opérations bancaires de l'"+nom,this);
    journal_ventes=new Journal("Journal des Ventes de l'"+nom,this);
    journal_achats=new Journal("Journal des Achats de l'"+nom,this);
    journal_activitegenerale=new Journal("Journal général de l'"+nom,this);
    journal_Stock = new Journal("Journal des Stocks de l'"+nom,this);
    journal_catastrophe = new Journal("Journal des Catastrophes de l'"+nom,this);
    journal_plantation = new Journal("Journal des Plantations de l'"+nom,this);
	this.Stock = new Stock();
	this.CoutTonne = 0.0;
	this.VentesMG = new LinkedList<Double>();
	this.VentesHG = new LinkedList<Double>();
	this.StockFeveH = new Variable("Equipe3 Stock Haut de gamme", "Represente la quantite de Haut de Gamme en Stock",this,this.Stock.getQuantite(Feve.F_HQ_BE));
	this.StockFeveM = new Variable("Equipe3 Stock Moyen de gamme", "Represente la quantite de Moyen de Gamme en Stock",this,this.Stock.getQuantite(Feve.F_MQ_BE));
	this.StockFeveB = new Variable("Equipe3 Stock Bas de gamme", "Represente la quantite de Bas de Gamme en Stock",this,this.Stock.getQuantite(Feve.F_BQ));
	this.tailleH = new Variable("Equipe3 Taille Champs H", "Represente la taille de nos champs Hauts de Gamme en Stock",this,this.fields.getTaille("H"));
	this.tailleM = new Variable("Equipe3 Taille Champs M", "Represente la taille de nos champs Moyens de Gamme en Stock",this,this.fields.getTaille("M"));
	}
	
	/**
	 * @author BOCQUET Gabriel
	 */	
	public void initialiser() {
		for (int i = 0; i < 12; i++) {
			this.VentesMG.add(0.0);
			this.VentesHG.add(0.0);
		}
	}

	/**
	 * @author BOCQUET Gabriel
	 */	
	public String getNom() {// NE PAS MODIFIER
		return "EQ3";
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////
	
	/**
	 * @author BOCQUET Gabriel
	 */	
	protected Journal getJGeneral() {
		return this.journal_activitegenerale;
	}
	
	/**
	 * @author BOCQUET Gabriel
	 */	
	protected Journal getJStock() {
		return this.journal_Stock;
	}


	/**
	 * @author BOCQUET Gabriel
	 */	
	protected Journal getJCatastrophe() {
		return this.journal_catastrophe;
	}


	protected int getCryptogramme() {
		return this.cryptogramme;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	protected Champs getFields() {
		return this.fields;
	}
	
	/**
	 * @author BOCQUET Gabriel
	 */	
	protected Journal getJOperation() {
		return this.journal_operationsbancaires;
	}
	
	/**
	 * @author BOCQUET Gabriel
	 */	
	protected Journal getJVente() {
		return this.journal_ventes;
	}
	
	/**
	 * @author BOCQUET Gabriel
	 */	
	protected Journal getJAchats() {
		return this.journal_achats;
	}

	/**
	 * @author BOCQUET Gabriel
	 */	
	protected Stock getStock() {
		return this.Stock;
	}

	/**
	 * @author BOCQUET Gabriel
	 */	
	public void next() {
		// Removing the first element for both lists and adding a new 0 at the end of both
		this.VentesMG.removeFirst();
		this.VentesMG.add(0.0);
		this.VentesHG.removeFirst();
		this.VentesHG.add(0.0);

	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(249, 230, 151); 
	}

	
	public String getDescription() {
		return "Vendeurs ELITE de cacao, spécialistes de la faillite éclair, de la vente à perte et de la vente de produits de qualité médiocre. Nous sommes les meilleurs !";
	}

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		res.add(this.StockFeveH);
		res.add(this.StockFeveM);
		res.add(this.StockFeveB);
		res.add(this.tailleH);
		res.add(this.tailleM);
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
		res.add(journal_activitegenerale);
		res.add(journal_operationsbancaires);
		res.add(journal_ventes);
		res.add(journal_achats);
		res.add(journal_Stock);
		res.add(journal_catastrophe);
		res.add(journal_plantation);
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
		JFrame popup = new JFrame("L'equipe 3 a fait faillite...Triste");
		popup.setLocation(300, 100);
		double proba =Math.random();
		ImageIcon icon;
		if(proba<0.5) {
		icon = new ImageIcon("./src/abstraction/eq3Producteur3/Gif/faillite1.gif");
		}
		else {
			icon = new ImageIcon("./src/abstraction/eq3Producteur3/Gif/faillite2.gif");
		}
		JLabel label = new JLabel(icon);
		popup.getContentPane().add(label);
        popup.pack();
		popup.setVisible(true);
		//Timer timer = new Timer();
		//ControlTimeGif monTimerTask = new ControlTimeGif(popup);
		//timer.schedule(monTimerTask, 10000);
		popup.setVisible(true);
		
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

	////////////////////////////////////////////////////////
	//      Quelques fonction utilitaires diverses        //
	////////////////////////////////////////////////////////

	/**
	 * @author Corentin Caugant
	 */
	public void addVenteQuantite(double quantite, Feve feve) {
		if (feve == Feve.F_MQ_BE || feve == Feve.F_MQ) {
			double newValue = quantite + this.VentesMG.getLast();
			this.VentesMG.set(11, newValue);
		}
		else if (feve == Feve.F_HQ_BE) {
			double newValue = quantite + this.VentesHG.getLast();
			this.VentesHG.set(11, newValue);
		}
	}
}
