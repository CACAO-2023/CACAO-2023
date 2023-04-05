package abstraction.eq9Distributeur3;

import java.util.HashMap;
import java.util.Map.Entry;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class DistributeurChocolatDeMarque extends Distributeur3Acteur implements IDistributeurChocolatDeMarque {
	
	private double capaciteDeVente;
	private double[] prix;
	private String[] marques;
	
	

	public DistributeurChocolatDeMarque(ChocolatDeMarque[] chocos, double[] stocks, double capaciteDeVente, double[] prix, String[] marques) {
		super(chocos, stocks);
		this.capaciteDeVente = capaciteDeVente;
		this.prix = prix;
		this.marques = marques;
	}

	@Override
	public double prix(ChocolatDeMarque choco) {
		return 0;
	}
	
	public HashMap<ChocolatDeMarque, Double> quantiteTotale() {
		HashMap<ChocolatDeMarque, Double> qtVente = new HashMap<ChocolatDeMarque, Double> ();
		HashMap<ChocolatDeMarque, Double> Stock = stock.getQteStock();
		
		for (Entry<ChocolatDeMarque, Double> chocolat : Stock.entrySet()) {
			qtVente.put(chocolat.getKey(), (double) 0);
		}
		double quantiteEnVente = 0;
		while (quantiteEnVente < this.capaciteDeVente) {
			for (Entry<ChocolatDeMarque, Double> chocolat : Stock.entrySet()) {
				qtVente.replace(chocolat.getKey(), Math.min(Math.min(this.capaciteDeVente/3, chocolat.getValue()), this.capaciteDeVente - quantiteEnVente));
			}
		}
		return qtVente;

	}
	@Override
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		if (crypto != this.cryptogramme) {
			journal.ajouter("On essaie de me pirater (RayonVide)");
			return 0;
		} else {
			HashMap<ChocolatDeMarque, Double> qtVente = this.quantiteTotale();
			return qtVente.get(choco);
		}
	}

	@Override
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		if (crypto != this.cryptogramme) {
			journal.ajouter("On essaie de me pirater (RayonVide)");
			return 0;
		} else {
			return 0;
		}
	}

	@Override
	//baptiste
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		if (crypto != this.cryptogramme) {
			journal.ajouter("On essaie de me pirater (RayonVide)");
		} else {
			journal.ajouter("On a plus de " + choco.getNom());
			this.stock.ajoutQte(choco, -(montant/this.prix(choco)));
		}
		
		
	}

	@Override
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		if (crypto != this.cryptogramme) {
			journal.ajouter("On essaie de me pirater (RayonVide)");
		} else {
			journal.ajouter("On a plus de " + choco.getNom());
		}
		
	}

}
