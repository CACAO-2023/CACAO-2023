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
		 // on ajoute par défaut un certain stock de feves (200 de chaque type)
		this.stockFeves.put(Feve.F_MQ, 200.0);
		this.totalStocksFeves.ajouter(this, 200.0, this.cryptogramme);
		this.journal.ajouter("ajout de 200 de "+Feve.F_MQ+" au stock de feves --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
		
		this.stockFeves.put(Feve.F_HQ_BE, 200.0);
		this.totalStocksFeves.ajouter(this, 200.0, this.cryptogramme);
		this.journal.ajouter("ajout de 200 de "+Feve.F_HQ_BE+" au stock de feves --> total="+this.totalStocksFeves.getValeur(this.cryptogramme));
			
		this.stockChoco=new HashMap<Chocolat,Double>(); // de meme avec les differents chocolats
		
		this.stockChoco.put(Chocolat.C_MQ, 0.0);
		this.totalStocksChoco.ajouter(this, 0.0, this.cryptogramme);
		this.stockChoco.put(Chocolat.C_HQ_BE, 0.0);
		this.totalStocksChoco.ajouter(this, 0.0, this.cryptogramme);

		
		this.stockChocoMarque=new HashMap<ChocolatDeMarque,Double>();
		ChocolatDeMarque Chocopop = new ChocolatDeMarque(Chocolat.C_MQ, "ChocoPop", 75, 0);
		this.stockChocoMarque.put(Chocopop, 0.0);
		ChocolatDeMarque MaisonDou = new ChocolatDeMarque(Chocolat.C_HQ_BE, "Maison Doutre", 90, 10);
		this.stockChocoMarque.put(MaisonDou, 0.0);
	}
	
	public void next() {
		super.next();
		
		/**
		 * @author adam
		 * coûts de stockage
		 */
		
		for(Feve f : Feve.values()) {
			if((stockFeves.containsKey(f))&&(stockFeves.get(f)!=0)) {
				double stockfeve = this.stockFeves.get(f);
				// dans le CdCf il a été convenu que le cout de stockage pour une feve était 4x celui des producteurs
				double cout = stockfeve*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*4;
				Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), cout);
				this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "Couts de stockage de la feve "+f+" ! On perd "+cout+" euros");
		}}
		
		for(Chocolat c : Chocolat.values()) {
			if((stockChoco.containsKey(c))&&(stockChoco.get(c)!=0)) {
				double stockchoc = this.stockChoco.get(c);
				// dans le CdCf il a été convenu que le cout de stockage pour le chocolat feve était 4x celui des producteurs
				double cout = stockchoc*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*4;
				Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), cout);
				this.journal.ajouter("Couts de stockage du chocolat "+c+" ! On perd "+cout+" euros");
		}}
		
	}
	
	
	
	
	
}
