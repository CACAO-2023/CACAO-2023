package abstraction.eqXRomu;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class RomuATVendeurBourse extends RomuATransformateur implements IVendeurBourse {

	//========================================================
	//                      VENTE EN BOURSE
	//========================================================

	public double offre(Feve f, double cours) {
		double offre =  Math.min(this.stockFeves.get(f), Math.random()*50.0);
		this.journal.ajouter(COLOR_LLGRAY, COLOR_PURPLE,"   BOURSEV: offre "+offre+" T de "+f+" en bourse ");
		return offre;
	}

	public Lot notificationVente(Feve f, double quantite, double cours) {
		Lot l = new Lot(f);
		l.ajouter(Filiere.LA_FILIERE.getEtape(), quantite); // cet exemple ne gere pas la production : tout le stock est considere comme venant d'etre produit
		this.stockFeves.put(f,  this.stockFeves.get(f) - quantite);
		this.totalStocksFeves.retirer(this, quantite, this.cryptogramme);
		this.journal.ajouter(COLOR_LLGRAY, COLOR_PURPLE,"   BOURSEV: vente de "+quantite+" T de "+f+" en bourse. Stock -> "+this.stockFeves.get(f));
		return l;
	}


	public void notificationBlackList(int dureeEnStep) {
		this.journal.ajouter(COLOR_LLGRAY, COLOR_PURPLE,"   BOURSEV: blackliste pendant 6 steps ");
		
	}
}
