package abstraction.eq8Distributeur2;

import java.util.HashMap;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
//auteur : AZZI Maxime

public class StockGeneral {
protected HashMap<ChocolatDeMarque, Stock> stocks;

// auteur : AZZI Maxime
public StockGeneral() {
stocks = new HashMap<ChocolatDeMarque, Stock>();
}

// auteur : AZZI Maxime
public double getStock(ChocolatDeMarque produit) {
Stock stock = stocks.get(produit);
if (stock == null) {
stock = new Stock(0); // On crée un nouveau stock avec une quantité initiale de 0
stocks.put(produit, stock);
}
return stock.getQuantite();
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
