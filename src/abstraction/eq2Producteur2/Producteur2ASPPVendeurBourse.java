package abstraction.eq2Producteur2;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Producteur2ASPPVendeurBourse extends Producteur2ASProducteurPlanteur implements IVendeurBourse{

	public Producteur2ASPPVendeurBourse() {
	}

	@Override
	public double offre(Feve f, double cours) {
			if (this.getFeve().equals(f)) { // a rajouter la condition cout de prod <=1.1*cours 
				BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
				double pourcentage = (bourse.getCours(getFeve()).getValeur()-bourse.getCours(getFeve()).getMin())/(bourse.getCours(getFeve()).getMax()-bourse.getCours(getFeve()).getMin());
				return this.stockFeve.getValeur()*pourcentage;
			} else {
				return 0.0;
			}
	}

	@Override
	public Lot notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		Lot L = new Lot(f);
		L.ajouter(Filiere.LA_FILIERE.getEtape(), quantiteEnT); // cet exemple ne gere pas ses culture et fait comme si les feves venaient d'etre produites
		this.stockFeve.setValeur(this, this.stockFeve.getValeur()-quantiteEnT);
		return L;
	}

	@Override
	public void notificationBlackList(int dureeEnStep) {
		this.journal.ajouter("Aie... blackliste pendant 6 steps");
	}

}
