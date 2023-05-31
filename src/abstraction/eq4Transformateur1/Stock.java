package abstraction.eq4Transformateur1;
import java.util.HashMap;
import abstraction.eqXRomu.filiere.Filiere;

/**
 * @author alexian 
 *
 */

import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Stock extends Transformateur1Acteur{
	protected HashMap<Feve, Double> stockFeves;
//	protected HashMap<Chocolat, Double> stockChoco;
	protected HashMap<ChocolatDeMarque, Double> stockChocoMarque;
	protected double stockHPrec;
	protected double stockBPrec;
	
	public Stock() {
		super();
	}
	
	public void initialiser() {
		super.initialiser();
		this.stockFeves=new HashMap<Feve,Double>();
		this.stockFeves.put(Feve.F_BQ, 10000.0);
		this.totalStocksFeves.ajouter(this, 10000.0, this.cryptogramme);
		this.journal.ajouter("ajout de 10000 de "+Feve.F_BQ+" au stock de feves --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));

		this.stockFeves.put(Feve.F_HQ_BE, 10000.0);
		this.totalStocksFeves.ajouter(this, 10000.0, this.cryptogramme);
		this.journal.ajouter("ajout de 10000 de "+Feve.F_HQ_BE+" au stock de feves --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));

//		this.stockChoco=new HashMap<Chocolat,Double>();
//		this.stockChoco.put(Chocolat.C_BQ, 1000.0);
//		this.totalStocksChoco.ajouter(this, 1000.0, this.cryptogramme);
//		this.journal.ajouter("ajout de 1000 de "+Chocolat.C_BQ+" au stock de chocolat --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
			
		this.stockChocoMarque=new HashMap<ChocolatDeMarque,Double>();
		for (ChocolatDeMarque c: Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (c.getMarque().equals("Vccotioi") || c.getMarque().equals("Yocttotoa")) {
					this.stockChocoMarque.put(c, 1000.0);
					this.totalStocksChocoMarque.ajouter(this, 1000.0, this.cryptogramme);
					this.journal.ajouter("ajout de 1000 de "+c+" au stock de chocolat de marque "+c.getMarque() +" +--> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
			}
		}
		this.stockHPrec=0;
		this.stockBPrec=0;
	}
	
	public void ajouter(IProduit produit, double quantite) {
		if (produit.getType().equals("Feve")) {
			if (this.stockFeves.keySet().contains(produit)) {
				this.stockFeves.put((Feve)produit,this.stockFeves.get((Feve)produit)+quantite);
				}
			else {
				this.stockFeves.put((Feve)produit,quantite);
				
			}
			this.totalStocksFeves.ajouter(this,quantite,this.cryptogramme);
			}
		else if(produit.getType().equals("ChocolatDeMarque")) {

			if (this.stockChocoMarque.keySet().contains(produit)) {
				this.stockChocoMarque.put((ChocolatDeMarque)produit,this.stockChocoMarque.get((ChocolatDeMarque)produit)+quantite);
				}
			else {
				this.stockChocoMarque.put((ChocolatDeMarque)produit,quantite);
			}
			this.totalStocksChocoMarque.ajouter(this,quantite,this.cryptogramme);
		}
	}
	
	public void retirer(IProduit produit, double quantite) {
		if (produit.getType().equals("Feve") && this.stockFeves.keySet().contains(produit)) {
			this.stockFeves.put((Feve)produit,this.stockFeves.get((Feve)produit)-quantite);
			this.totalStocksFeves.setValeur(this,this.totalStocksFeves.getValeur()-quantite,this.cryptogramme);
			}
		else if(produit.getType().equals("ChocolatDeMarque") && this.stockChocoMarque.keySet().contains(produit)) {
			this.stockChocoMarque.put((ChocolatDeMarque)produit,this.stockChocoMarque.get((ChocolatDeMarque)produit)-quantite);
			this.totalStocksChocoMarque.setValeur(this,this.totalStocksFeves.getValeur()-quantite,this.cryptogramme);
		}
	}
	
	public void next() {
		super.next();
		this.journal.ajouter("on a stockFeve : "+stockFeves);
		this.journal.ajouter("=== STOCKS === ");
		for (Feve f : this.stockFeves.keySet()) {
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN,"Stock de "+Journal.texteSurUneLargeurDe(f+"", 15)+" = "+this.stockFeves.get(f));
		}
		for (ChocolatDeMarque cm : this.stockChocoMarque.keySet()) {
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN,"Stock de "+Journal.texteSurUneLargeurDe(cm+"", 15)+" = "+this.stockChocoMarque.get(cm));
			if (cm.getGamme().equals(Gamme.BQ)) {
				this.stockBPrec=this.stockChocoMarque.get(cm);
			}
			if (cm.getGamme().equals(Gamme.HQ)) {
				this.stockHPrec=this.stockChocoMarque.get(cm);
			}
		}
		this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN,"StockFevesTotal="+this.totalStocksFeves.getValeur());
		this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN,"StockChocoDeMarqueTotal="+this.totalStocksChocoMarque.getValeur());
	}
}
