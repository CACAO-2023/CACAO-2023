package abstraction.eq2Producteur2;

//Code écrit par Nathan Rabier

import abstraction.eqXRomu.filiere.Filiere;

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
			Filiere.LA_FILIERE.getBanque().virer(thisP, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), this.CoutSalaire());
		this.coutStockage.setValeur(this, this.coutStockage(), cryptogramme);
		this.coutSalaire.setValeur(this, this.CoutSalaire(), cryptogramme);
		
		//System.out.println(this.coutStockage());
		//System.out.println(this.CoutSalaire());
	}
}
