package abstraction.eq9Distributeur3;

import java.util.HashMap;
import java.util.Map.Entry;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;


public class Stock  {
	HashMap<ChocolatDeMarque,Double> QteStock;
	private Distributeur3Acteur a;

	
	public Stock(Distributeur3Acteur a) {
		QteStock = new HashMap<ChocolatDeMarque, Double> ();
		this.a = a;
		
	}
	
	
	// Renvoie la qté de stock total 
	// Mathilde Soun 
	public double qteStockTOT () {
		double tot = 0.0;
		for (Double qte : QteStock.values()) {
	           tot += qte;
			}
		a.journal_stock.ajouter("Etape "+ Filiere.LA_FILIERE.getEtape()+ " : " + "Etat du stock Total : "+tot); 
		return tot;
		}
	
	public HashMap<ChocolatDeMarque, Double> getQteStock() {
		return QteStock;
	}


	// renvoie le stock du chocolat demandé 
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
		a.journal_stock.ajouter("On ajoute au stock de "+ c.getNom() + " une quantite de  " + ajout); 

		if(this.QteStock.get(c) == null) {
			this.QteStock.put(c, 0.0);
		}
		
		
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
	// met à jour le journal pour le stock de chaque chocolat 
	// Mathilde 
	public void maJ () {
		//liste de chocolat de stock ?? 
		for (Entry<ChocolatDeMarque, Double> chocolat : QteStock.entrySet()) {

			a.journal_stock.ajouter("Etat du stock du chocolat  : "+ chocolat.getKey()+ " "+this.QteStock.get(chocolat));

			a.journal_stock.ajouter("Etape "+ Filiere.LA_FILIERE.getEtape()+ " : " + "Etat du stock du chocolat  : "+ chocolat.getKey()+ " "+this.QteStock.get(chocolat));

		}
		
	}
}
