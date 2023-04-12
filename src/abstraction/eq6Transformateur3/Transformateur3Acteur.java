package abstraction.eq6Transformateur3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur3Acteur implements IActeur {
	
	protected int cryptogramme;
/** Nathan Claeys*/
	protected Journal journal;
	protected List<Journal> ListJournal;
	protected Variable pourcentageCacaoBG ;
	protected Variable pourcentageCacaoMG ;
	protected Variable pourcentageCacaoMGL ;
	protected Variable pourcentageCacaoHG ;
	protected Variable pourcentageRSE ;
	protected Variable totalStocksFeves;   
	protected Variable totalStocksChoco; 
	
	/**Nathan Claeys*/
	protected Transformateur3Acteur() {
		this.journal = new Journal("Journal"+this.getNom(),this);
		this.pourcentageCacaoBG = new Variable ("pourcentageCacaoBG","pourcentage de cacao dans le produit BG fini",this,0.4,0.60,0.5);
		this.pourcentageCacaoMG = new Variable ("pourcentageCacaoMG","pourcentage de cacao dans le produit MG non labelise fini",this,0.6,0.80,0.65);
		this.pourcentageCacaoMGL = new Variable ("pourcentageCacaoMGL","pourcentage de cacao dans le produit MG labelise fini",this,0.6,0.80,0.75);
		this.pourcentageCacaoHG = new Variable ("pourcentageCacaoHG","pourcentage de cacao dans le produit HG labelise fini",this,0.8,0.95,0.85);
		this.pourcentageRSE = new Variable ("pourcentageRSE", "defini le pourcentage RSE sur les recettes",this,0.05,0.15,0.05);
		this.totalStocksFeves = new Variable ("totalStocksFeves","defini l'etat total du stock de feves",this,0.0,1000000.0,0.0);
		this.totalStocksChoco = new Variable ("totalStocksChoco","defini l'etat total du stock de produit fini",this,0.0,1000000.0,0.0);
		this.ListJournal = new LinkedList<Journal>();
		ListJournal.add(this.journal);
	}
	
	/**
	 * @return the pourcentageCacaoBG
	 */
	public Variable getPourcentageCacaoBG() {
		return pourcentageCacaoBG;
	}

	/**
	 * @return the pourcentageCacaoMG
	 */
	public Variable getPourcentageCacaoMG() {
		return pourcentageCacaoMG;
	}

	/**
	 * @return the pourcentageCacaoMGL
	 */
	public Variable getPourcentageCacaoMGL() {
		return pourcentageCacaoMGL;
	}

	/**
	 * @return the pourcentageCacaoHG
	 */
	public Variable getPourcentageCacaoHG() {
		return pourcentageCacaoHG;
	}

	/**
	 * @return the pourcentageRSE
	 */
	public Variable getPourcentageRSE() {
		return pourcentageRSE;
	}

	public void initialiser() {
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ6";
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() {
		this.journal = new Journal(Filiere.LA_FILIERE.getEtape()+"",this);
		this.ListJournal.add(this.journal);
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(158, 242, 226); 
	}

	public String getDescription() {
		return "Eco Choco, le choco est un cadeau !";
	}

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		return res;
	}

	// Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	// Renvoie les journaux
	public List<Journal> getJournaux() {
		return this.ListJournal;
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
