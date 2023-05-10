package abstraction.eq9Distributeur3;


import java.util.HashMap;
import java.util.Map.Entry;




import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class DistributeurChocolatDeMarque extends Distributeur3AcheteurCC implements IDistributeurChocolatDeMarque {
	
	private double capaciteDeVente = Double.MAX_VALUE;
	private double prixRayon = 1.0;
	

	
	
	

	public DistributeurChocolatDeMarque() {
		
	}
	
	//william
	@Override
	public double prix(ChocolatDeMarque choco) {
		if(prix_tonne_vente.get(choco) != null) {
			return prix_tonne_vente.get(choco) +40000.0;
			
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
			HashMap<ChocolatDeMarque, Double> qtVente = this.quantiteTotale();
			
			
			
			if (qtVente.containsKey(choco)) {
				journal_ventes.ajouter("On vend " + qtVente.get(choco) +" "+ choco.getNom());
				if (qtVente.get(choco) != 0) {
				Filiere.LA_FILIERE.getBanque().virer(this, crypto, Filiere.LA_FILIERE.getActeur("Banque"), qtVente.get(choco)*prixRayon);
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
			HashMap<ChocolatDeMarque, Double> qtVente = this.quantiteTotale();
			
			double sum = 0.0;
			for (double d : qtVente.values()) {
				sum += d;
			}
			
			if (choco.equals(chocolats.get(5))) {
				double qtTg = Math.min(qtVente.get(choco), sum*0.1);
				journal_ventes.ajouter("On met "+ qtTg + "de" + choco.getNom() + "en tête de gondole.");
				return qtTg;
			} else {
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
			notificationOperationBancaire(montant);

			
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
