package abstraction.eq9Distributeur3;

import java.util.Arrays;
import java.util.List;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class DistributeurChocolatDeMarque extends Distributeur3Acteur implements IDistributeurChocolatDeMarque {
	
	private double capaciteDeVente;
	private double[] prix;
	private String[] marques;
	//william
	public DistributeurChocolatDeMarque(ChocolatDeMarque[] chocos, double[] stocks, double capaciteDeVente, double[] prix, String[]marques) {
		super(chocos, stocks);
		this.capaciteDeVente = capaciteDeVente;
		this.prix = prix;
		this.marques = marques;
	}
	//william
	@Override
	public double prix(ChocolatDeMarque choco) {
		int pos= (chocolats.indexOf(choco));
		if (pos<0) {
			return 0.0;
		} else {
			return prix[pos];
		}
	}
	
	
	
	//william
	@Override
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		if (crypto!=this.cryptogramme) {
			journal.ajouter("Quelqu'un essaye de me pirater !");
			return 0.0;
		} else {
			int pos= (chocolats.indexOf(choco));
			if (pos<0) {
				return 0.0;
			} else {
				return Math.min(capaciteDeVente, this.stock.getStock(choco));
			}
		}
	}
	//william
	// On met 10% de ce tout ce qu'on met en vente (on pourrait mettre l'accente sur
	// un produit a promouvoir mais il s'agit ici d'un exemple simpliste
	@Override
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		if (crypto!=this.cryptogramme) {
			journal.ajouter("Quelqu'un essaye de me pirater !");
			return 0.0;
		} else {
			int pos= (chocolats.indexOf(choco));
			if (pos<0) {
				return 0.0;
			} else {
				return Math.min(capaciteDeVente, this.stock.getStock(choco))/10.0;
			}
		}
	}

	@Override
	//william
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		int pos= (chocolats.indexOf(choco));
		if (pos>=0) {
			// QUANTITE NEGATIVE CAR ON RETIRE DU STOCK
			this.stock.ajoutQte(choco,-1*quantite);
		}
	}
	//william
	public List<String> getMarquesChocolat() {
		return Arrays.asList(this.marques);
	}

	
	public void notificationRayonVide(ChocolatDeMarque choco) {
		journal.ajouter(" Aie... j'aurais du mettre davantage de "+choco.getNom()+" en vente");
	}
	@Override
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		journal.ajouter(" Aie... j'aurais du mettre davantage de "+choco.getNom()+" en vente");
			
		
	}

}
