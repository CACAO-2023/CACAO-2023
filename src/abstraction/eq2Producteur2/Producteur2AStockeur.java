package abstraction.eq2Producteur2;

import java.util.HashMap;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;

//code écrit par Nathan

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
		
		majTot();
	}
	
	private void majTot() {
		/*
		 * mets à jour les stocks totaux
		 */
		for (Feve f : this.lesFeves) {
			this.stocksTot.get(f).setValeur(this, this.stocks.get(f).getQuantiteTotale(), this.cryptogramme);
		}
	}
	
	public void next() {
		super.next();
		int etapeDegrad = Filiere.LA_FILIERE.getEtape() - (int)this.tempsDegradationFeve.getValeur();
		int etapePerim = etapeDegrad - (int)this.tempsPerimationFeve.getValeur();
		for (Feve f : this.lesFeves) {
			HashMap<Integer, Double> stock = this.stocks.get(f).getQuantites();
			if (stock.containsKey(etapeDegrad) && stock.get(etapeDegrad) != 0) {
				if (f == Feve.F_MQ || f == Feve.F_MQ_BE) {
					this.stocks.get(Feve.F_BQ).ajouter(etapeDegrad, stock.get(etapeDegrad));
				}
				if (f == Feve.F_HQ_BE) {
					this.stocks.get(Feve.F_MQ_BE).ajouter(etapeDegrad, stock.get(etapeDegrad));
				}
				this.stocks.get(f).retirer(stock.get(etapeDegrad));
			}
			if (stock.containsKey(etapePerim) && stock.get(etapePerim) != 0){
				this.stocks.get(f).retirer(stock.get(etapePerim));
			}
		}
		majTot();
	}
	
	protected Variable getStockTot(Feve f) {
		return this.stocksTot.get(f);
	}
	
}
