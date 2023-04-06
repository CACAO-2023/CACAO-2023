package abstraction.eq2Producteur2;

//code écrit par Nathan

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;

import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Producteur2AStockeur extends Producteur2Acteur {
	
	private HashMap<Feve, Lot> stocks;
	private HashMap<Feve, Variable> stocksTot;
	
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
	
	private void majTot(Feve f) {
		/*
		 * mets à jour le stock total de feve de type f
		 */
		this.stocksTot.get(f).setValeur(this, this.stocks.get(f).getQuantiteTotale(), this.cryptogramme);
	}
	
	private void majTot() {
		/*
		 * mets à jour les stocks totaux
		 */
		for (Feve f : this.lesFeves) {
			this.majTot(f);
		}
	}
	
	public void next() {
		super.next();
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
		/*this.ajouterStock(Feve.F_BQ, Filiere.LA_FILIERE.getEtape(), 1000);
		System.out.println(this.stocks.get(Feve.F_BQ));
		this.retirerStock(Feve.F_BQ, 500);
		Lot lotHQ_BE = new Lot(Feve.F_HQ_BE);
		lotHQ_BE.ajouter(0, 1000);
		this.ajouterStock(lotHQ_BE);
		//this.retirerStock(Feve.F_MQ, 500);
		this.majTot();
		System.out.println(this.stocksString());
		System.out.println(this.stocksTotString());
		System.out.println(this.getStockTotTime(Feve.F_BQ, 2));
		System.out.println(this.getStockTotStep(Feve.F_BQ, 2));*/
	}
	
	protected Variable getStockTot(Feve f) {
		return this.stocksTot.get(f);
	}
	
	protected double getStockTotStep(Feve f, int etape) {
		/*
		 * @return la quantité de fèves stocké du type f produites avant l'étape etape (inclus)
		 * @param le type de feve, Feve f, et l'étape, int etape
		 */
		HashMap<Integer, Double> stockFeve = this.stocks.get(f).getQuantites();
		double quantiteTot = 0.;
		for(int i: stockFeve.keySet()) 
			if (i <= etape) {
				quantiteTot += stockFeve.get(i);
			}
		return quantiteTot;
	}
	
	protected double getStockTotTime(Feve f, int nbStepProduite) {
		/*
		 * @return la quantité de fèves stocké du type f, qui sont produite depuis plus de nbStepStocke étapes (nbStepStocke étant inclus)
		 * @param le type de Fève, Feve f, et le nombre d'étapes, int nbStepStocke
		 */
		return this.getStockTotStep(f, Filiere.LA_FILIERE.getEtape() - nbStepProduite);
	}
	
	protected void ajouterStock(Lot lot) {
		stocks.get((Feve)lot.getProduit()).ajouter(lot);
		this.majTot((Feve)lot.getProduit());
	}
	
	protected void ajouterStock(Feve f, int etapeProd, double quantite) {
		stocks.get(f).ajouter(etapeProd, quantite);
		this.majTot(f);
	}
	
	protected Lot retirerStock(Feve f, double quantite) {
		Lot res = this.stocks.get(f).retirer(quantite);
		this.majTot(f);
		return res;
	}
	
	protected String stocksString() {
		return "Stock : \nBQ : " + this.stocks.get(Feve.F_BQ)
				+ "\nMQ : " + this.stocks.get(Feve.F_MQ)
				+ "\nMQ_BE : " + this.stocks.get(Feve.F_MQ_BE)
				+ "\nHQ_BE : " + this.stocks.get(Feve.F_HQ_BE);
	}
	
	protected String stocksTotString() {
		return "Stock : \nBQ : " + this.stocksTot.get(Feve.F_BQ).getValeur()
		+ "\nMQ : " + this.stocksTot.get(Feve.F_MQ).getValeur()
		+ "\nMQ_BE : " + this.stocksTot.get(Feve.F_MQ_BE).getValeur()
		+ "\nHQ_BE : " + this.stocksTot.get(Feve.F_HQ_BE).getValeur();
	}
}
