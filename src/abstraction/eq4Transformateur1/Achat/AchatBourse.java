package abstraction.eq4Transformateur1.Achat;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class AchatBourse implements IAcheteurBourse{
	private double achatMaxParStep;

	@Override
	public double demande(Feve f, double cours) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notificationAchat(Lot l, double coursEnEuroParT) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notificationBlackList(int dureeEnStep) {
		// TODO Auto-generated method stub
		
	}

}
