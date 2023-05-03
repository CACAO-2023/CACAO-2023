package abstraction.eq9Distributeur3;


import java.util.HashMap;
import java.util.Map.Entry;




import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class DistributeurChocolatDeMarque extends Distributeur3AcheteurCC implements IDistributeurChocolatDeMarque {
	
	private double capaciteDeVente = Double.MAX_VALUE;
	

	
	
	

	public DistributeurChocolatDeMarque() {
		
	}
	
	//william
	@Override
	public double prix(ChocolatDeMarque choco) {
		if(prix_tonne_vente.get(choco) != null) {
			return prix_tonne_vente.get(choco) +1.0;
			
		}
		// TODO Auto-generated method stub
		return 10.0;
	}
	
	//baptiste
	public HashMap<ChocolatDeMarque, Double> quantiteTotale() {
		HashMap<ChocolatDeMarque, Double> qtVente = new HashMap<ChocolatDeMarque, Double> ();
		journal_ventes.ajouter("Test" + qtVente.toString() );
		HashMap<ChocolatDeMarque, Double> Stock = this.stock.getQteStock();
		journal_ventes.ajouter("Voici l'état du stock : " + Stock.toString());
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
		journal_ventes.ajouter("On met en vente " + qtVente.toString() + "tonnes de chocolat");
		return qtVente;

	}

	//baptiste
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {

		if (crypto != this.cryptogramme) {
			journal_activitegenerale.ajouter("On essaie de me pirater (RayonVide)");
			return 0;
		} else {
			HashMap<ChocolatDeMarque, Double> qtVente = this.quantiteTotale();
			journal_ventes.ajouter("On vend " + qtVente.get(choco) +" "+ choco.getNom());
			if (qtVente.containsKey(choco)) {
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
			if (qtVente.containsKey(choco)) {
				return qtVente.get(choco)/10.0;
			} else {
				return 0;
			}

		}
	}

	@Override
	//william
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {

		if (crypto != this.cryptogramme) {
			journal_activitegenerale.ajouter("On essaie de me pirater (RayonVide)");
		} else {
			String qtte_string = "" + montant/this.prix(choco);
			String montant_string = "" + montant;
			journal_ventes.ajouter("Vente de " + qtte_string + "tonnes de " +  choco.getNom() + " pour " + montant_string + "€");
			
			if( montant/this.prix(choco) >= this.stock.getStock(choco)) { // on vérifie qu'on ai le stock
				this.stock.ajoutQte(choco, -(montant/this.prix(choco)));
				//notificationOperationBancaire(montant); deja notifié

			}
			else {
				// si on a pas le stock
				journal_ventes.ajouter("Vente annulée de " + choco.getNom());

			}
			
		}
		
		

	}

	@Override
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {

		journal_ventes.ajouter(" Aie... j'aurais du mettre davantage de "+choco.getNom()+" en vente");


		if (crypto != this.cryptogramme) {
			journal_activitegenerale.ajouter("On essaie de me pirater (RayonVide)");
		} else {
			journal_ventes.ajouter(" Aie... j'aurais du mettre davantage de "+choco.getNom()+" en vente");
		}
			


	}
	

	
	

}
