package abstraction.eq8Distributeur2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import abstraction.eqXRomu.produits.ChocolatDeMarque;
//auteur : AZZI Maxime

public class StockGeneral {
protected HashMap<ChocolatDeMarque, Stock> stocks;

// auteur : AZZI Maxime
public StockGeneral() {
stocks = new HashMap<ChocolatDeMarque, Stock>();
}

// auteur : AZZI Maxime
public double getStock(ChocolatDeMarque chocolats) {
Stock stock = stocks.get(chocolats);
if (stock == null) {
stock = new Stock(0); // On crée un nouveau stock avec une quantité initiale de 0
stocks.put(chocolats, stock);
}
return stock.getQuantite();
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
double stock = getStock(produit);
stock+=quantiteAjoutee;
}

// auteur : AZZI Maxime
public void retirerDuStock(ChocolatDeMarque produit, double quantiteRetiree) {
double stock = getStock(produit);
stock-=quantiteRetiree;
}
}
