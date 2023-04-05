package abstraction.eq8Distributeur2;

import java.util.HashMap;

import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class StockGeneral {
    protected HashMap<ChocolatDeMarque, Stock> stocks;

    public StockGeneral() {
        stocks = new HashMap<ChocolatDeMarque, Stock>();
    }

    public Stock getStock(ChocolatDeMarque produit) {
        Stock stock = stocks.get(produit);
        if (stock == null) {
            stock = new Stock(0); // On crée un nouveau stock avec une quantité initiale de 0
            stocks.put(produit, stock);
        }
        return stock;
    }

    // Autres méthodes pour gérer le stock général (ajouter, retirer, etc.)
    public void ajouterAuStock(ChocolatDeMarque produit, double quantiteAjoutee) {
        Stock stock = getStock(produit);
        stock.ajouter(quantiteAjoutee);
    }

    public boolean retirerDuStock(ChocolatDeMarque produit, double quantiteRetiree) {
        Stock stock = getStock(produit);
        return stock.retirer(quantiteRetiree);
    }
}