package abstraction.eq9Distributeur3;


import java.util.HashMap;
import java.util.Map.Entry;




import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;

public class DistributeurChocolatDeMarque extends Distributeur3AcheteurOA implements IDistributeurChocolatDeMarque {
	
	private double capaciteDeVente = Double.MAX_VALUE;
	private double prixRayon = 1.0;
	public double qteVendue_HQ_BE;
	public double qteVendue_MQ_BE;
	public double qteVendue_MQ;
	public double qteVendue_TOT;




	
	

	

	public DistributeurChocolatDeMarque() {
		
	}
	
	public void next() {
		
		super.next();
		this.variable_qtte_vendue_TOT.setValeur(this, qteVendue_TOT, this.cryptogramme);
		this.variable_qtte_vendue_HQ_BE.setValeur(this, qteVendue_HQ_BE, this.cryptogramme);
		this.variable_qtte_vendue_MQ_BE.setValeur(this, qteVendue_MQ_BE, this.cryptogramme);
		this.variable_qtte_vendue_MQ.setValeur(this, qteVendue_MQ, this.cryptogramme);
		
		this.journal_AO.ajouter("coef 1 = " + this.coef_prix_vente.get(0.0));
		this.journal_AO.ajouter("coef 2 = " + this.coef_prix_vente.get(1.0));
		this.journal_AO.ajouter("coef 3 = " + this.coef_prix_vente.get(2.0));

		
		if(100*qteVendue_HQ_BE/this.variable_stock_HQ_BE.getValeur() < 40 && this.coef_prix_vente.get(0.0)*0.8 > 1.0) {
			this.journal_ventes.ajouter("On vend seulement " + qteVendue_HQ_BE/this.variable_stock_HQ_BE.getValeur() + "% de notre stock de HQ_BE");
			this.coef_prix_vente.put(0.0,this.coef_prix_vente.get(0.0)*0.8);
		}
		if(100*qteVendue_MQ_BE/this.variable_stock_MQ_BE.getValeur() < 40 && this.coef_prix_vente.get(1.0)*0.8 > 1.0) {
			this.journal_ventes.ajouter("On vend seulement " + qteVendue_MQ_BE/this.variable_stock_MQ_BE.getValeur() + "% de notre stock de MQ_BE");
			this.coef_prix_vente.put(1.0,this.coef_prix_vente.get(1.0)*0.8);
		}
		if(100*qteVendue_MQ/this.variable_stock_MQ.getValeur() < 40 && this.coef_prix_vente.get(2.0)*0.8 > 1.0) {
			this.journal_ventes.ajouter("On vend seulement " + qteVendue_MQ/this.variable_stock_MQ.getValeur() + "% de notre stock de MQ");
			this.coef_prix_vente.put(2.0,this.coef_prix_vente.get(2.0)*0.8);
		}
		
		
		this.variable_perc_vendue.setValeur(this, 100*qteVendue_TOT/this.variable_stock_tot.getValeur(), this.cryptogramme);

		

		
		qteVendue_HQ_BE = 0.0;
		qteVendue_MQ_BE = 0.0;
		qteVendue_MQ = 0.0;
		qteVendue_TOT = 0.0;

		
	}
	
	
	//william
	@Override
	public double prix(ChocolatDeMarque choco) {
		if(prix_tonne_vente.get(choco) != null) {
			return prix_tonne_vente.get(choco);
			
		}
		// TODO Auto-generated method stub
		return 40000.0;
	}
	
	//baptiste
	public HashMap<ChocolatDeMarque, Double> quantiteTotale() {
		HashMap<ChocolatDeMarque, Double> qtVente = new HashMap<ChocolatDeMarque, Double> ();
		HashMap<ChocolatDeMarque, Double> Stock = this.stock.getQteStock();

		for (Entry<ChocolatDeMarque, Double> chocolat : Stock.entrySet()) {
			qtVente.put(chocolat.getKey(), (double) 0);
		}
		double quantiteEnVente = 0;
		double quantiteEnVente_0 = 0;
		boolean rupture = true;
		
		while (quantiteEnVente < this.capaciteDeVente && rupture) {
			for (Entry<ChocolatDeMarque, Double> chocolat : Stock.entrySet()) {
				qtVente.replace(chocolat.getKey(), Math.min(Math.min(this.capaciteDeVente/3, chocolat.getValue()), this.capaciteDeVente - quantiteEnVente));
			}
			
			if (quantiteEnVente == quantiteEnVente_0) {
				rupture = false;
			} else {
				quantiteEnVente_0 = quantiteEnVente;
			}
		}
		return qtVente;

	}

	//baptiste
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {

		if (crypto != this.cryptogramme) {
			journal_activitegenerale.ajouter("On essaie de me pirater (RayonVide)");
			return 0;
		} else {
			HashMap<ChocolatDeMarque, Double> qtVente = quantiteTotale();
			
			
			
			if (qtVente.containsKey(choco)) {
				journal_ventes.ajouter("On met en vente " + qtVente.get(choco) +" "+ choco.getNom());
				if (qtVente.get(choco) != 0) {
				Filiere.LA_FILIERE.getBanque().virer(this, crypto, Filiere.LA_FILIERE.getActeur("Banque"), qtVente.get(choco)*prixRayon);

				journal_operationsbancaires.ajouter("On paie "+ qtVente.get(choco)*prixRayon + " pour la mise en rayon de " + choco.getNom() );

				}
				return qtVente.get(choco);
			} else {
				return 0;
			}
			
			

		}
	}
	
	//baptiste 
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {

		if (crypto!=this.cryptogramme) {
			journal_activitegenerale.ajouter("Quelqu'un essaye de me pirater !");
			return 0.0;
		} else {
			HashMap<ChocolatDeMarque, Double> qtVente = quantiteTotale();
			
			double sum = 0.0;
			for (double d : qtVente.values()) {
				sum += d;
			}
			
			if (choco.equals(chocolats.get(5))) {
				double qtTg = Math.min(qtVente.get(choco), sum*0.1);
				journal_ventes.ajouter("On met "+ qtTg + " de " + choco.getNom() + " en tête de gondole.");
				return qtTg;
			} else {
				journal_ventes.ajouter("On ne met pas de " + choco.getNom() + " en tête de gondole.");
				return 0.0;
			}

		}
	}

	@Override
	//baptiste
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {

		if (crypto != this.cryptogramme) {
			journal_activitegenerale.ajouter("On essaie de me pirater (RayonVide)");
		} else {

			journal_ventes.ajouter("Vente de " + quantite + "tonnes de " +  choco.getNom() + " pour " + montant + "€");
			this.stock.ajoutQte(choco, -quantite);
			//e notificationOperationBancaire(montant);
			CA_step += montant;
			
			
			if(choco.getGamme() == Gamme.HQ) {
				qteVendue_HQ_BE += quantite;
			}
			else if(choco.getGamme() == Gamme.MQ && choco.isBioEquitable()) {
				qteVendue_MQ_BE += quantite;
			}
			else if(choco.getGamme() == Gamme.MQ && !choco.isBioEquitable()) {
				qteVendue_MQ += quantite;
			}
			else {
				this.journal_activitegenerale.ajouter("vente de chocolat basse qualité !!!");
			}
			
			
			qteVendue_TOT+= quantite;
			

			
		}
	}

	@Override
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {

		if (crypto != this.cryptogramme) {
			journal_activitegenerale.ajouter("On essaie de me pirater (RayonVide)");
		} else {
			journal_ventes.ajouter(" Aie... j'aurais du mettre davantage de "+choco.getNom()+" en vente");
		}
			


	}
	

	
	

}
