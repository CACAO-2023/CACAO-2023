package abstraction.eq3Producteur3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
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
	protected Double CoutStep; /*
								 * Tout nos couts du step, reinitialises a zero au debut de chaque step et payes
								 * a la fin du step
								 */
	protected Double CoutTonne; /*
								 * Le cout par tonne de cacao, calcule sur 18 step (destruction de la feve apres
								 * 9 mois), le meme pour toute gamme
								 */

	public Variable StockFeveH;
	public Variable StockFeveM;
	public Variable StockFeveB;
	public Variable tailleH;
	public Variable tailleM;

	public Variable coutMoyen;
	public Variable coutEmployeStep;
	public Variable coutSalaireTot;
	public Variable BeneficeH;
	public Variable BeneficeM;
	public Variable BeneficeB;
	public Variable dateLimiteVenteM;
	public Variable probaIncendiL;
	public Variable probaIncendiM;
	public Variable probaIncendiH;
	public Variable probaGreve;
	public Variable probaCyclone;
	public Variable quantiteBruleL;
	public Variable quantiteBruleM;
	public Variable quantiteBruleH;
	public Variable quantiteDetruiteCyclone;
	public Variable pourcentageGrevise;
	public Variable EsperanceGaussienneProduction;
	public Variable EcartTypeGaussienneProduction;
	public Variable margeStockage;

	
	protected LinkedList<Double> VentesMG; /*Les 12 quantités des dernières ventes de moyens de gammes*/
	protected LinkedList<Double> VentesHG; /*Les 12 quantités des dernières ventes de hauts de gammes*/

	// On va garder une trace de la fiabilité de nos acheteurs
    protected HashMap<IAcheteurContratCadre, Integer> acheteursMQfiabilité;
    protected HashMap<IAcheteurContratCadre, Integer> acheteursHQfiabilité;

    // On va aussi conserver le prix de la dernière transaction avec chaque acheteur
    protected HashMap<IAcheteurContratCadre, Double> acheteursMQprix;
    protected HashMap<IAcheteurContratCadre, Double> acheteursHQprix;
	

	protected Champs fields;

	/**
	 * @author BOCQUET Gabriel, Corentin Caugant
	 */
	public Producteur3Acteur() {
	String nom = "Equipe 3";
	this.fields = new Champs();
	
	this.acheteursMQfiabilité = new HashMap<IAcheteurContratCadre, Integer>();
	this.acheteursHQfiabilité = new HashMap<IAcheteurContratCadre, Integer>();
	this.acheteursMQprix = new HashMap<IAcheteurContratCadre, Double>();
	this.acheteursHQprix = new HashMap<IAcheteurContratCadre, Double>();
	
	journal_operationsbancaires=new Journal("Journal des Opérations bancaires de l'"+nom,this);
    journal_ventes=new Journal("Journal des Ventes de l'"+nom,this);
    journal_achats=new Journal("Journal des Achats de l'"+nom,this);
    journal_activitegenerale=new Journal("Journal général de l'"+nom,this);
    journal_Stock = new Journal("Journal des Stocks de l'"+nom,this);
    journal_catastrophe = new Journal("Journal des Catastrophes de l'"+nom,this);
    journal_plantation = new Journal("Journal des Plantations de l'"+nom,this);
    journal_plantation.ajouter(Color.GRAY,Color.BLACK,this.printField("H"));
    journal_plantation.ajouter(Color.GREEN,Color.BLACK,this.printField("M"));
	this.Stock = new Stock();
	this.CoutTonne = 0.0;
	this.VentesMG = new LinkedList<Double>();
	this.VentesHG = new LinkedList<Double>();
	this.StockFeveH = new Variable("Equipe3 Stock Haut de gamme", "Represente la quantite de Haut de Gamme en Stock",this,this.Stock.getQuantite(Feve.F_HQ_BE));
	this.StockFeveM = new Variable("Equipe3 Stock Moyen de gamme", "Represente la quantite de Moyen de Gamme en Stock",this,this.Stock.getQuantite(Feve.F_MQ_BE));
	this.StockFeveB = new Variable("Equipe3 Stock Bas de gamme", "Represente la quantite de Bas de Gamme en Stock",this,this.Stock.getQuantite(Feve.F_BQ));
	this.tailleH = new Variable("Equipe3 Taille Champs H", "Represente la taille de nos champs Hauts de Gamme en Stock",this,this.fields.getTaille("H"));
	this.tailleM = new Variable("Equipe3 Taille Champs M", "Represente la taille de nos champs Moyens de Gamme en Stock",this,this.fields.getTaille("M"));
	this.coutMoyen = new Variable("Equipe3 Cout Moyen par tonne", "Correspond au cout depense par step par tonne de cacao ",this,this.CoutTonne);
	this.coutEmployeStep = new Variable("Equipe3 Cout par Employe", "Correspond au salaire d'un employe par step ",this,220);
	this.coutSalaireTot = new Variable("Equipe3 Cout Salaire", "Correspond au total des salaires que nous devons payer ",this,(this.fields.getTaille("M")+this.fields.getTaille("H"))*this.coutEmployeStep.getValeur());
	this.BeneficeH =  new Variable("Equipe3 Benefice Feves Hautes Gamme", "Correspond au benefice fait sur les feves Hautes Gamme ",this,0);
	this.BeneficeM = new Variable("Equipe3 Benefice Feves Moyennes  Gamme", "Correspond au benefice fait sur les feves Moyennes Gamme ",this,0);
	this.BeneficeB = new Variable("Equipe3 Benefice Feves Bas de Gamme", "Correspond au benefice fait sur les feves Bas de Gamme ",this,0);
	this.dateLimiteVenteM = new Variable("Equipe3 Date limite vente Bouse Feve M", "Fixe la date limite de vente des feves M avant de les vendre en bouse ",this,10);
	this.probaIncendiL = new Variable("Equipe3 Proba Incendi L", "Fixe la probabilite qu'un incendie de taille L arrive ",this,0.1);
	this.probaIncendiM = new Variable("Equipe3 Proba Incendi M", "Fixe la probabilite qu'un incendie de taille M arrive ",this,0.05);
	this.probaIncendiH = new Variable("Equipe3 Proba Incendi H", "Fixe la probabilite qu'un incendie de taille H arrive ",this,0.02);
	this.probaCyclone = new Variable("Equipe3 Proba Cyclone", "Fixe la probabilite qu'un Cyclone arrive ",this,0);
	this.probaGreve = new Variable("Equipe3 Proba Greve", "Fixe la probabilite qu'une Greve arrive ",this,0.02);
	this.pourcentageGrevise = new Variable("Equipe3 Pourcentage Greviste", "Fixe la proportion d'ouvrier en Greve ",this,0.2);
	this.quantiteBruleH = new Variable("Equipe3 Proportion Champs Brules Incendie H", "Fixe le pourcentage d'arbre brules suite a un incendie H ",this,0.5);
	this.quantiteBruleM = new Variable("Equipe3 Proportion Champs Brules Incendie M", "Fixe le pourcentage d'arbre brules suite a un incendie M ",this,0.2);

	this.quantiteBruleL = new Variable("Equipe3 Proportion Champs Brules Incendie L", "Fixe le pourcentage d'arbre brules suite a un incendie L ",this,0.1);
	this.quantiteDetruiteCyclone = new Variable("Equipe3 Proportion Champs Detruit Cyclone", "Fixe le pourcentage d'arbre detruit suite a un Cyclone ",this,0.05);
	this.EsperanceGaussienneProduction = new Variable("Esperance gaussienne pour production", "Fixe l'esperance de la gaussienne permettant d'avoir le pourcentage de feves recoltes",this,480);
	this.EcartTypeGaussienneProduction = new Variable("Ecart-type gaussienne pour production", "Fixe l'ecart type de la gaussienne permettant d'avoir le pourcentage de feves recoltes",this,720);
	this.margeStockage = new Variable("Equipe3 Marge de stockage", "Fixe la marge de stockage de nos feves pour parer aux imprévus ",this,0.1);


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
	// En lien avec l'interface graphique //
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
	protected Variable getDateLimM() {
		return this.dateLimiteVenteM;
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
	
	protected Journal getJPlantation() {
		return this.journal_plantation;
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
		// Removing the first element for both lists and adding a new 0 at the end of
		// both
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
		res.add(this.coutMoyen);
		res.add(this.coutSalaireTot);
		res.add(this.BeneficeB);
		res.add(this.BeneficeM);
		res.add(this.BeneficeH);
		res.add(this.margeStockage);
		return res;
	}

	// Renvoie les parametres
	public List<Variable> getParametres() {

		List<Variable> res=new ArrayList<Variable>();
		res.add(this.coutEmployeStep);
		res.add(this.dateLimiteVenteM);
		res.add(this.probaIncendiH);
		res.add(this.probaIncendiM);
		res.add(this.probaIncendiL);
		res.add(this.probaCyclone);
		res.add(this.probaGreve);
		res.add(quantiteBruleH);
		res.add(quantiteBruleM);
		res.add(quantiteBruleL);
		res.add(quantiteDetruiteCyclone);
		res.add(pourcentageGrevise);

		res.add(EsperanceGaussienneProduction);
		res.add(EcartTypeGaussienneProduction);

		return res;
	}

	// Renvoie les journaux
	public List<Journal> getJournaux() {
		List<Journal> res = new ArrayList<Journal>();
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
	// En lien avec la Banque //
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
		if (this == acteur) {
			JFrame popup = new JFrame("L'equipe 3 a fait faillite...Triste");
			popup.setLocation(300, 100);
			double proba = Math.random();
			ImageIcon icon;
			if (proba < 0.5) {
				icon = new ImageIcon("./src/abstraction/eq3Producteur3/Gif/Fallite1.gif");
			} else {
				icon = new ImageIcon("./src/abstraction/eq3Producteur3/Gif/faillite2.gif");
			}
			JLabel label = new JLabel(icon);
			popup.getContentPane().add(label);
			popup.pack();
			popup.setVisible(true);
			Timer timer = new Timer();
			ControlTimeGif monTimerTask = new ControlTimeGif(popup);
			timer.schedule(monTimerTask, 2500);
			popup.setVisible(true);
		} else {
			if (this.acheteursMQfiabilité.containsKey((IAcheteurContratCadre)acteur)) {
				this.acheteursMQfiabilité.remove(acteur);
			}
			if (this.acheteursHQfiabilité.containsKey((IAcheteurContratCadre)acteur)) {
				this.acheteursHQfiabilité.remove(acteur);
			}
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
	// Pour la creation de filieres de test //
	////////////////////////////////////////////////////////

	// Renvoie la liste des filieres proposees par l'acteur
	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		return (filieres);
	}

	// Renvoie une instance d'une filiere d'apres son nom
	public Filiere getFiliere(String nom) {
		return Filiere.LA_FILIERE;
	}

	////////////////////////////////////////////////////////
	// Quelques fonction utilitaires diverses //
	////////////////////////////////////////////////////////

	/**
	 * @author Corentin Caugant
	 */
	public void addVenteQuantite(double quantite, Feve feve) {
		if (feve == Feve.F_MQ_BE || feve == Feve.F_MQ) {
			double newValue = quantite + this.VentesMG.getLast();
			this.VentesMG.set(11, newValue);
		} else if (feve == Feve.F_HQ_BE) {
			double newValue = quantite + this.VentesHG.getLast();
			this.VentesHG.set(11, newValue);
		}
	}
	
	public LinkedList<Double> VentesMH(String s){
		if(s=="H") {
			return this.VentesHG;
		}
		else if(s=="M"){
			return this.VentesMG;
		}
		return null;
	}
	
	public String printField(String s) {
		HashMap<Integer,Integer> Field = this.getFields().getChamps().get(s);
		String st = "{ ";
		for(Integer i : Field.keySet()) {
			st+= i + ": " + Field.get(i) +" ,";
		}
		return "Etat des champs "+s+": " +st + "}";
	}
}
