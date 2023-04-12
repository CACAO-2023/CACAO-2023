package abstraction.eq2Producteur2;

//Code écrit par Nathan

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;

import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Producteur2AStockeur extends Producteur2Acteur {
	
	private HashMap<Feve, Lot> stocks; // Représente les stocks réels par type de fève,
									   // tout en gardant en mémoire l'étape de production
									   // de chaque fève stocké
	private HashMap<Feve, Variable> stocksTot;// Est composé des indicateurs de stock,
											  // que l'on tiens à jour à chaque modification
											  // des stocks
	
	public Producteur2AStockeur() {
		super();
	}
	
	public void initialiser() {
		super.initialiser();
		
		this.stocks = new HashMap<Feve, Lot>();
		
		this.stocks.put(Feve.F_BQ, new Lot(Feve.F_BQ));
		this.stocks.put(Feve.F_MQ, new Lot(Feve.F_MQ));
		this.stocks.put(Feve.F_MQ_BE, new Lot(Feve.F_MQ_BE));
		this.stocks.put(Feve.F_HQ_BE, new Lot(Feve.F_HQ_BE));
		
		this.stocks.get(Feve.F_BQ).ajouter(0, 10000);
		this.stocks.get(Feve.F_MQ).ajouter(0, 10000);
		this.stocks.get(Feve.F_MQ_BE).ajouter(0, 10000);
		this.stocks.get(Feve.F_HQ_BE).ajouter(0, 10000);
		
		this.stocksTot = new HashMap<Feve, Variable>();
		
		this.stocksTot.put(Feve.F_BQ, stockTotBasse);
		this.stocksTot.put(Feve.F_MQ, stockTotMoy);
		this.stocksTot.put(Feve.F_MQ_BE, stockTotMoyBE);
		this.stocksTot.put(Feve.F_HQ_BE, stockTotHauteBE);
		
		this.majTot();
	}
	
	// mets à jour le stock total de feve de type f
	private void majTot(Feve f) {
		this.stocksTot.get(f).setValeur(this, this.stocks.get(f).getQuantiteTotale(), this.cryptogramme);
	}
	
	// mets à jour les stocks totaux
	private void majTot() {
		for (Feve f : this.lesFeves) {
			this.majTot(f);
		}
	}
	
	public void next() {
		super.next();
		// À chaque étape, on mets à jour les stocks pour déclasser les fèves trop vieilles
		// et supprimer les fèves périmées
		int etapeDegrad = Filiere.LA_FILIERE.getEtape() - (int)this.tempsDegradationFeve.getValeur();
		int etapePerim = etapeDegrad - (int)this.tempsPerimationFeve.getValeur();
		//System.out.println(this.stocksString());
		for (Feve f : this.lesFeves) {
			HashMap<Integer, Double> stock = this.stocks.get(f).getQuantites();
			if (stock.containsKey(etapeDegrad)) {
				if (f == Feve.F_MQ || f == Feve.F_MQ_BE) {
					this.stocks.get(Feve.F_BQ).ajouter(etapeDegrad, stock.get(etapeDegrad));
				}
				if (f == Feve.F_HQ_BE) {
					this.stocks.get(Feve.F_MQ_BE).ajouter(etapeDegrad, stock.get(etapeDegrad));
				}
				stock.remove(etapeDegrad);
			}
			Set<Integer> key = new HashSet<>(stock.keySet());
			for (int i: key) 
				if (i <= etapePerim)
					stock.remove(i);
		}
		this.majTot();
		//System.out.println(this.stocks.get(Feve.F_BQ));
		/*this.ajouterStock(Feve.F_BQ, Filiere.LA_FILIERE.getEtape(), 1000);
		this.retirerStock(Feve.F_BQ, 500);
		Lot lotHQ_BE = new Lot(Feve.F_HQ_BE);
		lotHQ_BE.ajouter(0, 1000);
		this.ajouterStock(lotHQ_BE);
		//this.retirerStock(Feve.F_MQ, 500);
		System.out.println(this.stocksString());
		System.out.println(this.stocksTotString());
		System.out.println(this.getStockTotTime(Feve.F_BQ, 2));
		System.out.println(this.getStockTotStep(Feve.F_BQ, 2));*/
	}
	
	/**
	 * Renvoie la variable représentant la quantité totale de fèves stockée du type f
	 * @return la variable de la quantité totale de fèves stockée du type f
	 * @param f le type de fève
	 */
	protected Variable getStockTot(Feve f) {
		return this.stocksTot.get(f);
	}
	
	
	/**
	 * Calcule la quantité totale de fève stockées, tout type confondu
	 * @return la quantité totale de fève stockées
	 */
	protected double getStockTotTot() {
		double tot = 0;
		for (Feve f: this.stocksTot.keySet()) 
			tot += this.stocksTot.get(f).getValeur();
		return tot;
	}
	
	/**
	 * Calcule la quantité de fèves stocké du type f produites avant l'étape etape (inclus)
	 * @return la quantité de fève
	 * @param f Le type de fève
	 * @param etape L'étape maximal de production
	 */
	protected double getStockTotStep(Feve f, int etape) {
		HashMap<Integer, Double> stockFeve = this.stocks.get(f).getQuantites();
		double quantiteTot = 0.;
		for(int i: stockFeve.keySet()) 
			if (i <= etape) {
				quantiteTot += stockFeve.get(i);
			}
		return quantiteTot;
	}
	
	/**
	 * Calcule la quantité de fèves stocké du type f, qui sont produite depuis plus de nbStepStocke étapes (nbStepStocke étant inclus)
	 * @return la quantité de fève
	 * @param f le type de fève 
	 * @param nbStepProduite le temps minimal depuis la production
	 */
	protected double getStockTotTime(Feve f, int nbStepProduite) {
		return this.getStockTotStep(f, Filiere.LA_FILIERE.getEtape() - nbStepProduite);
	}
	
	/**
	 * Calcule le coût de stockage du stock actuel pour une étape
	 * @return le coût du stockage
	 */
	protected double coutStockage() {
		return this.coutMoyenStock.getValeur() * this.getStockTotTot();
	}
	
	/**
	 * Ajoute le lot au stock
	 * @param lot le lot à ajouter au stock
	 */
	protected void ajouterStock(Lot lot) {
		stocks.get((Feve)lot.getProduit()).ajouter(lot);
		this.majTot((Feve)lot.getProduit());
	}
	
	/**
	 * Ajoute la quantité quantite de fève de type f produites à l'étape etapeProd au stock
	 * @param f le type de fève
	 * @param etapeProd l'étape de production des fèves
	 * @param quantite la quantité de fève
	 */
	protected void ajouterStock(Feve f, int etapeProd, double quantite) {
		stocks.get(f).ajouter(etapeProd, quantite);
		this.majTot(f);
	}
	
	/**
	 * Retire une quantité quantite de fève de type f du stock
	 * @param f le type de fève
	 * @param quantite la quantité de fève
	 * @return un lot contenant la quantité voulu de fève de type f
	 */
	protected Lot retirerStock(Feve f, double quantite) {
		Lot res = this.stocks.get(f).retirer(quantite);
		this.majTot(f);
		return res;
	}
	
	/**
	 * Renvoie une chaîne de caractères décrivant le stock actuel en détails
	 * @return la description du stock actuel
	 */
	protected String stocksString() {
		return "Stock : \nBQ : " + this.stocks.get(Feve.F_BQ)
				+ "\nMQ : " + this.stocks.get(Feve.F_MQ)
				+ "\nMQ_BE : " + this.stocks.get(Feve.F_MQ_BE)
				+ "\nHQ_BE : " + this.stocks.get(Feve.F_HQ_BE);
	}
	
	/**
	 * Renvoie une chaîne de caractères décrivant le stock total de chaque type de fève
	 * @return la description des quantités stockées de chaque type de fève
	 */
	protected String stocksTotString() {
		return "Stock : \nBQ : " + this.stocksTot.get(Feve.F_BQ).getValeur()
		+ "\nMQ : " + this.stocksTot.get(Feve.F_MQ).getValeur()
		+ "\nMQ_BE : " + this.stocksTot.get(Feve.F_MQ_BE).getValeur()
		+ "\nHQ_BE : " + this.stocksTot.get(Feve.F_HQ_BE).getValeur();
	}
}
