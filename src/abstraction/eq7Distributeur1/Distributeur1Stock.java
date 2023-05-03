package abstraction.eq7Distributeur1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class Distributeur1Stock extends Distributeur1Acteur{
	
	protected HashMap<Chocolat, Double> stockChoco;

	protected HashMap<ChocolatDeMarque,Double> stockChocoMarque; //Stock de chaque marque en tonne
	
	//	protected int totalStocksCB;  // La quantité totale de stock de chocolat bas de gamme 
	//	protected int totalStocksCML;  // La quantité totale de stock de chocolat moyenne gamme labellise
	//	protected int totalStocksCMNL;  // La quantité totale de stock de chocolat moyenne gamme non labellise
	//	protected int totalStocksCH;  // La quantité totale de stock de chocolat haute gamme
	
		/**
		 * initialise les indicateurs de stock
	 * @author ghaly
	 */
	protected void initialise_indic_stock() {
		stock_BQ.setValeur(this, 0);
		stock_HQ_BE.setValeur(this, 0);
		stock_MQ.setValeur(this, 0);
		stock_MQ_BE.setValeur(this, 0);
	}
	
	/**
	 * actualise les indicateurs de stock pour chaque gamme
	 * @author Ghaly
	 */
	protected void actualise_indic_stock() {
		initialise_indic_stock();
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			Chocolat gamme = marque.getChocolat();
    		if (gamme == Chocolat.C_BQ) {
    			stock_BQ.ajouter(this, stockChocoMarque.get(marque));
    		}
    		if (gamme == Chocolat.C_MQ) {
    			stock_MQ.ajouter(this, stockChocoMarque.get(marque));
    		}
    		if (gamme == Chocolat.C_MQ_BE) {
    			stock_MQ_BE.ajouter(this, stockChocoMarque.get(marque));
    		}
    		if (gamme == Chocolat.C_HQ_BE) {
    			stock_HQ_BE.ajouter(this, stockChocoMarque.get(marque));    		
    		}
		}
	}
	
	/**
	 * @author Ghaly
	 * renvoit le cout total de stockage actuel de la marque à l'instant t
	 */
	protected double get_cout_stockage(ChocolatDeMarque marque) {
		return cout_stockage_distributeur.getValeur() * stockChocoMarque.get(marque);
	}
	
	/**
	 * @author Theo and Ghaly
	 */
	public void initialiser() {
		
		//Initialisation des stocks
		this.stockChocoMarque = new HashMap<ChocolatDeMarque,Double>();
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			stockChocoMarque.put(marque,10000.);
			nombre_achats.put(marque, 0);
			journal_stock.ajouter("Stock de "+marque+" : "+stockChocoMarque.get(marque)+" T");
		}
		super.initialiser();

		//initialisation des indicateurs de stock
		actualise_indic_stock();
	}
	
	
	/**
	 * @return 
	 * 
	 */
	public void next() {
		super.next();
		
		//initialisation des indicateurs de stocks
		actualise_indic_stock();
		
		//Actualisation du stock total
		double newstock = 0.;
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			newstock += stockChocoMarque.get(marque);
		}
		totalStocks.setValeur(this, newstock, this.cryptogramme);
		
		
		//Affichage des stocks dans les Journaux
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			journal_stock.ajouter("Stock de "+marque+" : "+stockChocoMarque.get(marque)+" T");
		}
		
		//System.out.print(totalStocks.getValeur()==(stock_BQ.getValeur()+stock_HQ_BE.getValeur()+stock_MQ.getValeur()+stock_MQ_BE.getValeur()));
		//Prise en compte des couts de stockage
		if (totalStocks.getValeur()*cout_stockage_distributeur.getValeur() > 0) {
			double cout_STOCK =  totalStocks.getValeur()*cout_stockage_distributeur.getValeur();
			Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme,Filiere.LA_FILIERE.getBanque(),cout_STOCK );	
			journal_stock.ajouter("Cout de stockage : "+cout_STOCK);
			}
	}
	
}
