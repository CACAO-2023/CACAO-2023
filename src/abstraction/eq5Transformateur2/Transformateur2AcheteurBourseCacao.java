package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Transformateur2AcheteurBourseCacao extends Transformateur2 implements IAcheteurBourse {

	private double achatMaxParStep;
	
	public Transformateur2AcheteurBourseCacao() {
		super();
	}
	
	@Override
	public double demande(Feve f, double cours) {
		if (this.getFeve().equals(f)) {
			BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
			double pourcentage = (bourse.getCours(getFeve()).getMax()-bourse.getCours(getFeve()).getValeur())/(bourse.getCours(getFeve()).getMax()-bourse.getCours(getFeve()).getMin());
			return achatMaxParStep*pourcentage;
		} else {
			return 0.0;
		}
	}

	@Override
	public void notificationAchat(Lot l, double coursEnEuroParT) {
		this.stockFeve.setValeur(this, this.stockFeve.getValeur()+l.getQuantiteTotale());
	}
	
	@Override
	public void notificationBlackList(int dureeEnStep) {
		this.journal.ajouter("Aie... je suis blackliste... j'aurais du verifier que j'avais assez d'argent avant de passer une trop grosse commande en bourse...");
	
		
	}
	

}
