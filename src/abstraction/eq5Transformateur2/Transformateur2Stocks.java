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
		
		this.stockChocoMarque=new HashMap<ChocolatDeMarque,Double>();
		ChocolatDeMarque Chocopop = new ChocolatDeMarque(Chocolat.C_MQ, "ChocoPop", 75, 0);
		this.stockChocoMarque.put(Chocopop, 0.0);
		ChocolatDeMarque MaisonDou = new ChocolatDeMarque(Chocolat.C_HQ_BE, "Maison Doutre", 90, 10);
		this.stockChocoMarque.put(MaisonDou, 0.0);
	}
	
	public void next() {
		super.next();
		
		System.out.println("ok");
		
		/**
		 * @author adam
		 * coûts de stockage
		 */
		
		for(Feve f : Feve.values()) {
			double stockfeve = this.stockFeves.get(f);
			// dans le CdCf il a été convenu que le cout de stockage pour une feve était 4x celui des producteurs
			double cout = stockfeve*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*4;
			Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), cout);
			this.journal.ajouter("Couts de stockage de la feve "+f+" ! On perd "+cout+" euros");
		}
		
		for(Chocolat c : Chocolat.values()) {
			double stockchoc = this.stockChoco.get(c);
			// dans le CdCf il a été convenu que le cout de stockage pour le chocolat feve était 4x celui des producteurs
			double cout = stockchoc*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*4;
			Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), cout);
			this.journal.ajouter("Couts de stockage du chocolat "+c+" ! On perd "+cout+" euros");
		}
		
	}
	
	
	
	
	
}
