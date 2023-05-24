package abstraction.eq2Producteur2;

import java.util.HashMap;

//Code écrit par Nathan Rabier

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;

public class Producteur2 extends Producteur2ASPPVBVendeurCC  {
	
	public Producteur2() {
		super();
		this.thisP = this; //Permet à Producteur2AStockeur d'accéder à la fonction de prévision de la production, qui est dans sa classe fille.
	}
	
	public void next() {
		super.next();
		if (this.coutStockage() > 0)
			Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), this.coutStockage());
		if (this.CoutSalaire() > 0)
			Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), this.CoutSalaire());
		this.coutStockage.setValeur(this, this.coutStockage(), cryptogramme);
		this.coutSalaire.setValeur(this, this.CoutSalaire(), cryptogramme);
		
		HashMap<Feve, Double> coutProdSalaire = this.CoutProd();
		for (Feve f : this.lesFeves) {
			this.coutProdFeve.get(f).setValeur(this, coutProdSalaire.get(f) + this.coutStockage(f), this.cryptogramme);
		}
	}
}
