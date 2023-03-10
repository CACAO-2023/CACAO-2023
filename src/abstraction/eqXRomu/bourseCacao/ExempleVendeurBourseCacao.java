package abstraction.eqXRomu.bourseCacao;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class ExempleVendeurBourseCacao extends ExempleAbsVendeurBourseCacao implements IVendeurBourse{

	public ExempleVendeurBourseCacao(Feve feve, double stock) {
		super(feve, stock);
	}

	public double offre(Feve f, double cours) {
		if (this.getFeve().equals(f)) {
			BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
			double pourcentage = (bourse.getCours(getFeve()).getValeur()-bourse.getCours(getFeve()).getMin())/(bourse.getCours(getFeve()).getMax()-bourse.getCours(getFeve()).getMin());
			return this.stockFeve.getValeur()*pourcentage;
		} else {
			return 0.0;
		}
	}

	public Lot notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		Lot l = new Lot(f);
		l.ajouter(Filiere.LA_FILIERE.getEtape(), quantiteEnT); // cet exemple ne gere pas ses culture et fait comme si les feves venaient d'etre produites
		this.stockFeve.setValeur(this, this.stockFeve.getValeur()-quantiteEnT);
		return l;
	}


	public void notificationBlackList(int dureeEnStep) {
		this.journal.ajouter("Aie... blackliste pendant 6 steps");
	}
}
