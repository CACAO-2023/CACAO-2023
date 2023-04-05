package abstraction.eq2Producteur2;

import java.util.HashMap;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;

//code écrit par Nathan

import abstraction.eqXRomu.general.VariablePrivee;
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
		
		this.tempsDegradationFeve = new Variable("tempsDegradationFeve", "Temps (en nombre d'étapes) avant qu'une Feve ne perdent de la qualité", this, 12);
		this.tempsPerimationFeve = new Variable("tempsPerimationFeve", "Temps (en nombre d'étapes) avant qu'une Feve ne se périme totalement  après avoir perdu une gamme", this, 6);
		
		this.stocks = new HashMap<Feve, Lot>();
		
		this.stocks.put(Feve.F_BQ, new Lot(Feve.F_BQ));
		this.stocks.put(Feve.F_MQ, new Lot(Feve.F_MQ));
		this.stocks.put(Feve.F_MQ_BE, new Lot(Feve.F_MQ_BE));
		this.stocks.put(Feve.F_HQ_BE, new Lot(Feve.F_HQ_BE));
		
		this.stocks.get(Feve.F_BQ).ajouter(0, 10000);
		this.stocks.get(Feve.F_MQ).ajouter(0, 10000);
		this.stocks.get(Feve.F_MQ_BE).ajouter(0, 10000);
		this.stocks.get(Feve.F_HQ_BE).ajouter(0, 10000);
		
		this.stockTotBasse = new VariablePrivee("stockTotBasse", "Stock total de fèves de basse qualité", this, this.stocks.get(Feve.F_BQ).getQuantiteTotale());
		this.stockTotMoy = new VariablePrivee("stockTotMoy", "Stock total de fèves de moyenne qualité", this, this.stocks.get(Feve.F_MQ).getQuantiteTotale());
		this.stockTotMoyBE = new VariablePrivee("stockTotMoyBE", "stock Total de fèves de moyenne qualité bio-équitable", this, this.stocks.get(Feve.F_MQ_BE).getQuantiteTotale());
		this.stockTotHauteBE = new VariablePrivee("stockTotHauteBE", "stock Total de fèves de haute qualité bio-équitable", this, this.stocks.get(Feve.F_HQ_BE).getQuantiteTotale());
		
		this.stocksTot.put(Feve.F_BQ, stockTotBasse);
		this.stocksTot.put(Feve.F_MQ, stockTotMoy);
		this.stocksTot.put(Feve.F_MQ_BE, stockTotMoyBE);
		this.stocksTot.put(Feve.F_HQ_BE, stockTotHauteBE);
	}
	
	public void next() {
		super.next();
		int etapeDegrad = Filiere.LA_FILIERE.getEtape() - (int)this.tempsDegradationFeve.getValeur();
		int etapePerim = Filiere.LA_FILIERE.getEtape() - (int)this.tempsPerimationFeve.getValeur();
		for (Feve f : this.lesFeves) {
			HashMap<Integer, Double> stock = this.stocks.get(f).getQuantites();
			if (stock.containsKey(etapeDegrad)){
				this.stocks.get(f).retirer(stock.get(etapeDegrad));
				if (f == Feve.F_MQ || f == Feve.F_MQ_BE) {
					this.stocks.get(Feve.F_BQ).ajouter(etapeDegrad, stock.get(etapeDegrad));
				}
				if (f == Feve.F_HQ_BE) {
					this.stocks.get(Feve.F_HQ_BE).ajouter(etapeDegrad, stock.get(etapeDegrad));
				}
			}
			if (stock.containsKey(etapePerim)){
				this.stocks.get(f).retirer(stock.get(etapePerim));
			}
		}
	}
	
	
}
