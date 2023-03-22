package abstraction.eq9Distributeur3;

import java.util.HashMap;


import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class Stock {
	private HashMap<ChocolatDeMarque,Double> QteStock;
	
	public Stock() {
		QteStock = new HashMap<ChocolatDeMarque, Double> ();
	}
	public double QteStockTOT () {
		double tot = 0.0;
		for (Double qte : QteStock.values()) {
	           tot += qte;
			}
		return tot;
		}
	public double getStock(ChocolatDeMarque c) {
		double res = this.QteStock.get(c);
		if (res == 0.0) {
			return 0.0;
		}
		return res;
		
	}
}
