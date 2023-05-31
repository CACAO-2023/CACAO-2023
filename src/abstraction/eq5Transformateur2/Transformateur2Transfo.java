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

public class Transformateur2Transfo extends Transformateur2Stocks implements IFabricantChocolatDeMarque {
	
	private List<ChocolatDeMarque>chocosProduits; // Liste des chocolats de marque produits 
	protected HashMap<Feve, HashMap<Chocolat, Double>> pourcentageTransfo; // pour les differentes feves, le chocolat qu'elle peuvent contribuer a produire avec le pourcentage de chocolat associé
	private double MQStep;
	private double MQteFeve;
	private double HQStep;
	private double HQteFeve;
	
	

	private double getMQteFeve() {
		return MQteFeve;
	}

	private void setMQteFeve(double mQteFeve) {
		this.MQteFeve = mQteFeve;
	}


	private double getMQStep() {
		return MQStep;
	}

	private double getHQStep() {
		return HQStep;
	}

	private double getHQteFeve() {
		return HQteFeve;
	}

	private void setMQStep(double mQStep) {
		this.MQStep = mQStep;
	}

	private void setHQStep(double HQStep) {
		this.HQStep = HQStep;
	}

	private void setHQteFeve(double HQteFeve) {
		this.HQteFeve = HQteFeve;
	}

	/**
	 * @author FERHOUT Adam
	 */
	
	public List<ChocolatDeMarque> getChocolatsProduits() { // nous produisons deux chocolats, chocopop et maison doutre
		if (this.chocosProduits.size()==0) {
				Chocolat c1 = Chocolat.C_MQ;
				Chocolat c2 = Chocolat.C_HQ_BE;
				this.chocosProduits.add(new ChocolatDeMarque(c1, "ChocoPop", 70, 0));
				this.chocosProduits.add(new ChocolatDeMarque(c2, "Maison Doutre", 90, 10));
		}
		
		return this.chocosProduits;
	}
	
	/**
	 * @author FERHOUT Adam
	 */

	public void initialiser() {
		super.initialiser();
		
		// on initialise les variables servant a creer les délais de transformation
		this.setHQStep(0);
		this.setMQStep(0);
		
		// on initialise les coefficients donnant le poids de chocolat otbenu avec 1 tonne de feve.
		this.pourcentageTransfo = new HashMap<Feve, HashMap<Chocolat, Double>>();
		this.pourcentageTransfo.put(Feve.F_HQ_BE, new HashMap<Chocolat, Double>());
		double conversion = 1.1;
		
		this.pourcentageTransfo.get(Feve.F_HQ_BE).put(Chocolat.C_HQ_BE, conversion);// la masse de chocolat obtenue est plus importante que la masse de feve vue l'ajout d'autres ingredients
		conversion = 1.3;
		this.pourcentageTransfo.put(Feve.F_MQ, new HashMap<Chocolat, Double>());
		this.pourcentageTransfo.get(Feve.F_MQ).put(Chocolat.C_MQ, conversion);
	}
	
	/**
	 * @author FERHOUT Adam
	 */
	
	public void ajoutStock(double qtefeve, double proportion_marque, Feve f, Chocolat c, String Marque, int cacao) {
		
		// On cree le chocolat sans marque si il y en a
		this.stockChoco.put(c, this.stockChoco.get(c)+((qtefeve*(1-proportion_marque))*this.pourcentageTransfo.get(f).get(c))); // on ajoute la quantité de chocolat obtenue
		this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN,"Transformation de "+qtefeve*(1-proportion_marque)+" feves "+Journal.texteSurUneLargeurDe(f.getGamme()+"", 15)+" en "+(qtefeve*(1-proportion_marque)*this.pourcentageTransfo.get(f).get(c)+" chocolat "+Journal.texteSurUneLargeurDe(c.getGamme()+"", 15)));

		double cout = 0;
		// On paie les coûts de transformation
		cout = qtefeve*1500; // le coût de transformation est fixé a 1500€ par tonne de feve.
		if (cout>0) {
			// on est débité des frais
			Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), cout);
			this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE,"Paiement de "+cout+" pour la transformation de "+qtefeve+" tonnes de feve en chocolat ");
			
			// on cree le chocolat et on l'ajoute dans nos stocks
			ChocolatDeMarque cm = new ChocolatDeMarque(c, Marque, cacao, 0); // On cree le chocolat de marque
			double scm = this.stockChocoMarque.keySet().contains(cm) ?this.stockChocoMarque.get(cm) : 0.0;	
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN,"Transformation de "+qtefeve*proportion_marque+" feves "+Journal.texteSurUneLargeurDe(f.getGamme()+"", 15)+" en "+((qtefeve*proportion_marque)*this.pourcentageTransfo.get(f).get(c))+Journal.texteSurUneLargeurDe(cm.getMarque(), 15));
			this.stockChocoMarque.put(cm, scm+((qtefeve*proportion_marque)*this.pourcentageTransfo.get(f).get(c)));					
			this.totalStocksChocoMarque.ajouter(this, ((qtefeve*proportion_marque)*this.pourcentageTransfo.get(f).get(c)), this.cryptogramme);
			this.totalStocksChoco.ajouter(this, ((qtefeve*(1-proportion_marque))*this.pourcentageTransfo.get(f).get(c)), this.cryptogramme);
		}}
	/**
	 * @author FERHOUT Adaam
	 */
	
	public void next() {
		super.next();
		double somme = this.stockFeves.get(Feve.F_MQ)+this.stockFeves.get(Feve.F_HQ_BE);
		
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
				
				double qtefeve = 0.0; 
				double proportion_marque = 0;
				String Marque = "";
				int cacao = 0;
				double cout = 0;
				
				if ((f.getGamme()==Gamme.MQ)&&(this.getMQStep()==0)){ // on ne consomme des fèves que si on a fini de transformer le lot précédent (MQStep = 0)
					qtefeve = this.stockFeves.get(f);
					this.setMQteFeve(qtefeve); // on garde en memoire la quantité de feves retirées pour les transformer en chocolat dans un step
					proportion_marque = 1; // Aucun distributeur n'achete de chocolat sans marque
					Marque = "ChocoPop";
					cacao = 70;
					this.stockFeves.put(f, (double) 0); // on les retire a nos stocks
					this.totalStocksFeves.retirer(this, qtefeve, this.cryptogramme); // et aux stocks totaux
				}
				
				if(((c.getGamme()==Gamme.HQ) && (c.isBioEquitable()))&&(this.getHQStep()==0)) { // pareil avec HQ step
					qtefeve = this.stockFeves.get(f);
					this.setHQteFeve(this.stockFeves.get(f)); 
					proportion_marque = 1;
					Marque = "Maison Doutre";
					cacao = 90;
					this.stockFeves.put(f, (double) 0); // on les retire a nos stocks
					this.totalStocksFeves.retirer(this, qtefeve, this.cryptogramme); // et aux stocks totaux
				}
				
							
				// Si on a bien attendu un step, on ajoute la qté MQteFeve transformée en chocolat feves au stock
				
				if((this.getMQStep()==1)&&(f.getGamme()==Gamme.MQ)) {
					this.ajoutStock(this.getMQteFeve(), 0.8, f, c, "ChocoPop", 70);
					this.setMQStep(0);
				}
				else {
					if(f.getGamme()==Gamme.MQ) {
						this.setMQStep(this.getMQStep()+1);
				}}
				
				// Si on a bien attendu deux steps, on ajoute la qté HQtéFeve transformée en chocolat feves au stock
				
				if((this.getHQStep()==2)&&(c.getGamme()==Gamme.HQ)) {
					this.ajoutStock(this.getHQteFeve(), 1, f, c, "Maison Doutre", 90);
					this.setHQStep(0);
				}
				else {
					if(c.getGamme()==Gamme.HQ) {
						this.setHQStep(this.getHQStep()+1);
				}}
				
							
			}
		}
	}
	
}
