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

public class Stock extends Transformateur1Acteur{
	protected HashMap<Feve, Double> stockFeves;
	protected HashMap<Chocolat, Double> stockChoco;
	protected HashMap<ChocolatDeMarque, Double> stockChocoMarque;
	
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

		this.stockChoco=new HashMap<Chocolat,Double>();
		this.stockChoco.put(Chocolat.C_BQ, 1000.0);
		this.totalStocksChoco.ajouter(this, 1000.0, this.cryptogramme);
		this.journal.ajouter("ajout de 1000 de "+Chocolat.C_BQ+" au stock de chocolat --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
			
		this.stockChocoMarque=new HashMap<ChocolatDeMarque,Double>();
		for (ChocolatDeMarque c: Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (c.getMarque().equals("Vccotioi")) {
					this.stockChocoMarque.put(c, 1000.0);
					this.totalStocksChocoMarque.ajouter(this, 1000.0, this.cryptogramme);
					this.journal.ajouter("ajout de 1000 de "+c+" au stock de chocolat de marque "+c.getMarque() +" +--> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
			}
		}
	}
	
	public void next() {
		super.next();
		this.journal.ajouter("on a stockFeve : "+stockFeves);
		this.journal.ajouter("=== STOCKS === ");
		for (Feve f : this.stockFeves.keySet()) {
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN,"Stock de "+Journal.texteSurUneLargeurDe(f+"", 15)+" = "+this.stockFeves.get(f));
		}
		for (Chocolat c : this.stockChoco.keySet()) {
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN,"Stock de "+Journal.texteSurUneLargeurDe(c+"", 15)+" = "+this.stockChoco.get(c));
		}
		for (ChocolatDeMarque cm : this.stockChocoMarque.keySet()) {
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN,"Stock de "+Journal.texteSurUneLargeurDe(cm+"", 15)+" = "+this.stockChocoMarque.get(cm));
			
		}
	}
}
