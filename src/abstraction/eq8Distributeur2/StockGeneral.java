package abstraction.eq8Distributeur2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
//auteur : AZZI Maxime

public class StockGeneral {
	protected HashMap<ChocolatDeMarque, Stock> stocks;

	// auteur : AZZI Maxime
	public StockGeneral() {
		stocks = new HashMap<ChocolatDeMarque, Stock>();
		
	}

	// auteur : AZZI Maxime
	public double getStock(ChocolatDeMarque chocolat) {
		Stock stock = stocks.get(chocolat);
		if (stock != null) {
			return stock.getQuantite();
		}
		else {
			return 0.0;
		}
	}

	//auteur: MOUDACHIROU Kayodé
	public double getStockGlobal() {
		double s=0;
		for (Map.Entry mapentry : stocks.entrySet()) {
			s+=this.getStock((ChocolatDeMarque)mapentry.getKey());
		}
		return s;
	}

	// Autres méthodes pour gérer le stock général (ajouter, retirer, etc.)
	// auteur : AZZI Maxime
	public void ajouterAuStock(ChocolatDeMarque produit, double quantiteAjoutee) {
		double stock = getStock(produit)+ quantiteAjoutee;
		Stock S = new Stock(stock);
		stocks.put(produit,S);
	}

	// auteur : AZZI Maxime
	public void retirerDuStock(ChocolatDeMarque produit, double quantiteRetiree) {
		double stock = getStock(produit)- quantiteRetiree;
		Stock S = new Stock(stock);
		stocks.put(produit,S);
	}
	
	
}
