package abstraction.eq2Producteur2;

//Code écrit par Nathan Rabier

import abstraction.eqXRomu.filiere.Filiere;

public class Producteur2 extends Producteur2ASPPVBVendeurCC  {
	
	public Producteur2() {
		super();
	}
	
	public void next() {
		super.next();
		if (this.coutStockage() > 0)
			Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), this.coutStockage());
		//System.out.println(this.coutStockage());
	}
}
