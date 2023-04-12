/*/ Page redigee par Adam FERHOUT /*/

package abstraction.eq5Transformateur2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.awt.Color;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur2 extends Transformateur2Acteur implements IFabricantChocolatDeMarque{
	
	private List<ChocolatDeMarque>chocosProduits; // Liste des chocolats de marque produits 
	protected HashMap<Feve, Double> stockFeves; // Feves disponible (en stock)
	protected HashMap<Chocolat, Double> stockChoco; // Chocolat disponible
	protected HashMap<ChocolatDeMarque, Double> stockChocoMarque; // Chocolat de marque disponible 
	protected HashMap<Feve, HashMap<Chocolat, Double>> pourcentageTransfo; // pour les differentes feves, le chocolat qu'elle peuvent contribuer a produire avec le pourcentage de chocolat associé

	public Transformateur2() { // constructeur 
		super();
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
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
		
		// on référence les differents pourcentages en cacao nécéssaires pour les chocolats produits
		
		this.pourcentageTransfo = new HashMap<Feve, HashMap<Chocolat, Double>>();
		this.pourcentageTransfo.put(Feve.F_HQ_BE, new HashMap<Chocolat, Double>());
		double conversion = 1.0 + (100.0 - 90.0)/100.0;
		this.pourcentageTransfo.get(Feve.F_HQ_BE).put(Chocolat.C_HQ_BE, conversion);// la masse de chocolat obtenue est plus importante que la masse de feve vue l'ajout d'autres ingredients
		conversion = 1.0 + (100.0 - 70.0)/100.0;
		this.pourcentageTransfo.put(Feve.F_MQ, new HashMap<Chocolat, Double>());
		this.pourcentageTransfo.get(Feve.F_MQ).put(Chocolat.C_MQ, conversion);
	}

	public List<ChocolatDeMarque> getChocolatsProduits() { // nous produisons deux chocolats, chocopop et maison doutre
		if (this.chocosProduits.size()==0) {
				Chocolat c1 = Chocolat.C_MQ;
				Chocolat c2 = Chocolat.C_HQ_BE;
				this.chocosProduits.add(new ChocolatDeMarque(c1, "ChocoPop", 70, 0));
				this.chocosProduits.add(new ChocolatDeMarque(c2, "Maison Doutre", 90, 10));
		}
		return this.chocosProduits;
	}
	
	public void next() {
		super.next();

		this.journal.ajouter("=== Step numéro "+ Filiere.LA_FILIERE.getEtape()+" ===");
		
		this.journal.ajouter("=== STOCK === ");
		
		for (Feve f : Feve.values()) {
			this.journal.ajouter(COLOR_LLGRAY, COLOR_LPURPLE, "Stock de feve "+Journal.texteSurUneLargeurDe(f+"", 15)+" = "+this.stockFeves.get(f));
		}
		
		for (Chocolat c : Chocolat.values()) {
			this.journal.ajouter(COLOR_LLGRAY, COLOR_LPURPLE, "Stock de chocolat "+Journal.texteSurUneLargeurDe(c+"", 15)+" = "+this.stockChoco.get(c));
		}
		
		if (this.stockChocoMarque.keySet().size()>0) {
			for (ChocolatDeMarque cm : this.stockChocoMarque.keySet()) {
				this.journal.ajouter(COLOR_LLGRAY, COLOR_LPURPLE,"Stock du cm "+Journal.texteSurUneLargeurDe(cm+"", 15)+" = "+this.stockChocoMarque.get(cm));
			}
		}
		
		for (Feve f : this.pourcentageTransfo.keySet()) {
			for (Chocolat c : this.pourcentageTransfo.get(f).keySet()) {
				
				double qtefeve = 0; 
				double proportion_marque = 0;
				String Marque = "";
				int cacao = 0;
				
				if (f.getGamme()==Gamme.MQ) {
					qtefeve = this.stockFeves.get(f)*0.15 ; //15% des feves moyenne gamme sont transformées en ChocoPop, 25% sans marque
					proportion_marque = 0.75;
					Marque = "ChocoPop";
					cacao = 70;
				}
				
				if((c.getGamme()==Gamme.HQ) && (c.isBioEquitable())) {
					qtefeve = this.stockFeves.get(f)*0.10 ; //15% des feves moyenne gamme sont transformées en ChocoPop, 25% sans marque
					proportion_marque = 1;
					Marque = "Maison Doutre";
					cacao = 90;
				}
					
				this.stockFeves.put(f, this.stockFeves.get(f)-qtefeve); // on les retire a nos stocks
				this.totalStocksFeves.retirer(this, qtefeve, this.cryptogramme); // et aux stocks totaux
				
				this.stockChoco.put(c, this.stockChoco.get(c)+((qtefeve*(1-proportion_marque))*this.pourcentageTransfo.get(f).get(c)));
				this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN,"Transformation de feves "+Journal.texteSurUneLargeurDe(f.getGamme()+"", 15)+" en chocolat "+Journal.texteSurUneLargeurDe(c.getGamme()+"", 15));
		
				ChocolatDeMarque cm = new ChocolatDeMarque(c, Marque, cacao, 0); // le chocolat ChocoPop est a 70% fait de cacao
				double scm = this.stockChocoMarque.keySet().contains(cm) ?this.stockChocoMarque.get(cm) : 0.0;	
				this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN,"Transformation de feves "+Journal.texteSurUneLargeurDe(f.getGamme()+"", 15)+" en "+Journal.texteSurUneLargeurDe(cm.getMarque(), 15));
				this.stockChocoMarque.put(cm, scm+((qtefeve*proportion_marque)*this.pourcentageTransfo.get(f).get(c)));					
				this.totalStocksChocoMarque.ajouter(this, ((qtefeve*proportion_marque)*this.pourcentageTransfo.get(f).get(c)), this.cryptogramme);
				this.totalStocksChoco.ajouter(this, ((qtefeve*(1-proportion_marque))*this.pourcentageTransfo.get(f).get(c)), this.cryptogramme);
					
			}
		}
		
		
		
		}
}
