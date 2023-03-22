package abstraction.eq4Transformateur1.Achat;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class AchatBourse implements IAcheteurBourse{
	private Feve f;
	private double achatMaxParStep;
	private double stockFeve;

	@Override
	public double demande(Feve f, double cours) {
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
