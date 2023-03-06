package abstraction.eqXRomu.bourseCacao;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class ExempleAcheteurBourseCacao extends ExempleAbsAcheteurBourseCacao implements IAcheteurBourse {

	private double achatMaxParStep;

	public ExempleAcheteurBourseCacao(Feve feve, double stock, double achatMaxParStep) {
		super(feve, stock);
		this.achatMaxParStep=achatMaxParStep;
	}

	public double demande(Feve f, double cours) {
		if (this.getFeve().equals(f)) {
			BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
			double pourcentage = (bourse.getCours(getFeve()).getMax()-bourse.getCours(getFeve()).getValeur())/(bourse.getCours(getFeve()).getMax()-bourse.getCours(getFeve()).getMin());
			return achatMaxParStep*pourcentage;
		} else {
			return 0.0;
		}
	}

	public void notificationAchat(Lot l, double coursEnEuroParT) {
		this.stockFeve.setValeur(this, this.stockFeve.getValeur()+l.getQuantiteTotale());
	}

	public void notificationBlackList(int dureeEnStep) {
		this.journal.ajouter("Aie... je suis blackliste... j'aurais du verifier que j'avais assez d'argent avant de passer une trop grosse commande en bourse...");
	}


}
