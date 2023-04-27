package abstraction.eq6Transformateur3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.filiere.IMarqueChocolat;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur3Acteur implements IActeur, IMarqueChocolat  {
	
	private List<ChocolatDeMarque> ListeProduits;
	protected HashMap<Feve, Double> stockFeves;
	protected HashMap<Chocolat, Double> stockChoco;
	protected int cryptogramme;
/** Nathan Claeys*/
	protected Journal journal;
	protected Journal journalStock;
	protected List<Journal> ListJournal;
	protected int pourcentageCacaoBG ;
	protected int pourcentageCacaoMG ;
	protected int pourcentageCacaoMGL ;
	protected int pourcentageCacaoHG ;
	protected int pourcentageRSE ;
	protected Variable totalStocksFeves;   
	protected Variable totalStocksChoco; 
	protected List<ChocolatDeMarque>chocosProduits;
	
	/**Nathan Claeys*/
	protected Transformateur3Acteur() {
		this.journal = new Journal("Journal"+this.getNom(),this);
		this.journalStock = new Journal("Journal des stocks"+this.getNom(),this);
		this.pourcentageCacaoBG = 50;
		this.pourcentageCacaoMG = 65;
		this.pourcentageCacaoMGL = 75;
		this.pourcentageCacaoHG = 85;
		this.pourcentageRSE = 10;
		this.totalStocksFeves = new Variable ("totalStocksFeves","defini l'etat total du stock de feves",this,0.0,1000000.0,0.0);
		this.totalStocksChoco = new Variable ("totalStocksChoco","defini l'etat total du stock de produit fini",this,0.0,1000000.0,0.0);
		this.ListJournal = new LinkedList<Journal>();
		ListJournal.add(this.journal);
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
		this.chocosProduits.add(new ChocolatDeMarque (Chocolat.C_BQ,"eco+ choco",this.pourcentageCacaoBG,this.pourcentageRSE));
		this.chocosProduits.add(new ChocolatDeMarque (Chocolat.C_MQ,"chokchoco",this.pourcentageCacaoMG,this.pourcentageRSE));
		this.chocosProduits.add(new ChocolatDeMarque (Chocolat.C_MQ_BE,"chokchoco bio",this.pourcentageCacaoMGL,this.pourcentageRSE));
		this.chocosProduits.add(new ChocolatDeMarque (Chocolat.C_HQ_BE,"Choc",this.pourcentageCacaoHG,this.pourcentageRSE));
	}
	
	/**
	 * @return the pourcentageCacaoBG
	 */
	public int getPourcentageCacaoBG() {
		return pourcentageCacaoBG;
	}

	/**
	 * @return the pourcentageCacaoMG
	 */
	public int getPourcentageCacaoMG() {
		return pourcentageCacaoMG;
	}

	/**
	 * @return the pourcentageCacaoMGL
	 */
	public int getPourcentageCacaoMGL() {
		return pourcentageCacaoMGL;
	}

	/**
	 * @return the pourcentageCacaoHG
	 */
	public int getPourcentageCacaoHG() {
		return pourcentageCacaoHG;
	}

	/**
	 * @return the pourcentageRSE
	 */
	public int getPourcentageRSE() {
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
/**ecrit par Nathan Claeys
 */
	public void next() {
		this.journal.ajouter(Filiere.LA_FILIERE.getEtape()+"");
		this.ListJournal.add(this.journal);
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(158, 242, 226); 
	}
/** par Maxime Bedu
 */
	public String getDescription() {
		return "Eco Choco, le choco est un cadeau !";
	}

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		return res;
	}
/** ecrit par Nathan Claeys
 */
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
/** ecrit par Nathan Claeys
*/
	@Override
	public List<String> getMarquesChocolat() {
		// TODO Auto-generated method stub
		LinkedList<String> l= new LinkedList<String>();
		l.add("Choc");
		l.add("chokchoco bio");
		l.add("chokchoco");
		l.add("eco+ choco");
		return l;
	}

}
