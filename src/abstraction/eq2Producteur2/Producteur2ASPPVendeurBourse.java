package abstraction.eq2Producteur2;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Producteur2ASPPVendeurBourse extends Producteur2ASProducteurPlanteur implements IVendeurBourse{

	@Override
	public double offre(Feve f, double cours) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Lot notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notificationBlackList(int dureeEnStep) {
		// TODO Auto-generated method stub
		
	}

}
