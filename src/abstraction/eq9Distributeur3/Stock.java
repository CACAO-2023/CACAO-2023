package abstraction.eq9Distributeur3;

import java.util.HashMap;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;


public class Stock {
	private HashMap<ChocolatDeMarque,Double> QteStock;
	
	
	public Stock() {
		QteStock = new HashMap<ChocolatDeMarque, Double> ();

	}
	
	
	// Renvoie la qté de stock total 
	// Mathilde Soun 
	public double qteStockTOT () {
		double tot = 0.0;
		for (Double qte : QteStock.values()) {
	           tot += qte;
			}
		return tot;
		}
	
	public HashMap<ChocolatDeMarque, Double> getQteStock() {
		return QteStock;
	}


	// renvoie le stock de chaque chocolat
	// Mathilde Soun 
	public double getStock(ChocolatDeMarque c) {
		double res = this.QteStock.get(c);
		if (res == 0.0) {
			return 0.0;
		}
		return res;
		
	}
	// ajout d'une qte de chocolat (ou soustraction de chocolat)
	// Mathilde Soun 
	
	public void ajoutQte(ChocolatDeMarque c, double ajout){
		double qte = this.QteStock.get(c);
		qte = qte + ajout;
		if (qte<0) {
			
			this.QteStock.put(c, null);
		}
		this.QteStock.put(c, qte);
		
	}
	
	
	
	
	// fonction coût du stock 
	// Mathilde Soun 
	public double coutDeStock () {
		double cout = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*this.qteStockTOT();
		return cout;
	}
}
