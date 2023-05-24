package abstraction.eq9Distributeur3;

import java.util.HashMap;
import java.util.Map.Entry;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;


public class Stock  {
	HashMap<ChocolatDeMarque,Double> QteStock;
	HashMap<Integer,Double> qteStockHQ;
	HashMap<Integer,Double> qteStockMQ;
	HashMap<Integer,Double> qteStockMQBE;
	private Distributeur3Acteur a;
	

	
	public Stock(Distributeur3Acteur a) {
		QteStock = new HashMap<ChocolatDeMarque, Double> ();
		this.a = a;
		qteStockHQ = new HashMap<Integer, Double>();
		qteStockMQ = new HashMap<Integer, Double>();
		qteStockMQBE = new HashMap<Integer, Double>();
		
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
	
	public double qteStock_HQ_BE () {
		double tot = 0.0;
		for (ChocolatDeMarque c : a.chocolats) {
			if(c.getChocolat().getGamme() == Gamme.HQ) {
				 tot += getStock(c);
			}
		}
		return tot;
		}
	
	public double qteStock_MQ_BE () {
		double tot = 0.0;
		for (ChocolatDeMarque c : a.chocolats) {
			if(c.getChocolat().getGamme() == Gamme.MQ &&c.getChocolat().isBioEquitable() ) {
				 tot += getStock(c);
			}  
		}
		return tot;
		}
	
	public double qteStock_MQ () {
		double tot = 0.0;
		for (ChocolatDeMarque c : a.chocolats) {
			if(c.getChocolat().getGamme() == Gamme.MQ && !c.getChocolat().isBioEquitable()) {
				 tot += getStock(c);
			}
	          
		}
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
	
	public void liquider() {
		for (ChocolatDeMarque c : a.chocolats) {
			QteStock.put(c,0.0);
			}

	}
	
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
		double cout = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*this.qteStockTOT()*16;
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
