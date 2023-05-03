package abstraction.eq5Transformateur2;

import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

public class Transformateur2Stocks extends Transformateur2Acteur {
	
	protected HashMap<Feve, Double> stockFeves; // Feves disponible (en stock)
	protected HashMap<Chocolat, Double> stockChoco; // Chocolat disponible
	protected HashMap<ChocolatDeMarque, Double> stockChocoMarque; // Chocolat de marque disponible 
	
	public Transformateur2Stocks() {
		super();
	}
	
	public void initialiser() {
		super.initialiser();
		this.stockFeves=new HashMap<Feve,Double>();
		for (Feve f : Feve.values()) { // on ajoute par défaut un certain stock de feves (10000 de chaque type)
			this.stockFeves.put(f, 10000.0);
			this.totalStocksFeves.ajouter(this, 10000.0, this.cryptogramme);
			this.journal.ajouter("ajout de 10000 de "+f+" au stock de feves --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
		}
		
		this.stockChoco=new HashMap<Chocolat,Double>(); // de meme avec les differents chocolats
		for (Chocolat c : Chocolat.values()) {
			this.stockChoco.put(c, 1000.0);
			this.totalStocksChoco.ajouter(this, 1000.0, this.cryptogramme);
			this.journal.ajouter("ajout de 1000 de "+c+" au stock de chocolat --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
		}
	}
	
	public void next() {
		
		/*
		 * @author adam
		 * coûts de stockage
		 */
		
		for(Feve f : Feve.values()) {
			double stockfeve = this.stockFeves.get(f);
			// dans le CdCf il a été convenu que le cout de stockage pour une feve était 4x celui des producteurs
			double cout = stockfeve*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*4;
			
		}
		
	}
	
	
	
	
	
}
