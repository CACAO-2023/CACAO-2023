package abstraction.eqXRomu;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class RomuATVBAcheteurBourse extends RomuATVendeurBourse implements IAcheteurBourse {
	//========================================================
	//                    ACHATS EN BOURSE
	//========================================================

	public double demande(Feve f, double cours) {
		double solde = Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme);
		double demande = Math.max(0, Math.min( Math.random()*50, solde));
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LPURPLE,"   BOURSEA: demande en bourse de "+demande+" de "+f);
		return demande;
	}

	public void notificationAchat(Lot l, double coursEnEuroParT) {
		Feve f = (Feve)(l.getProduit());
		this.stockFeves.put(f,  this.stockFeves.get(f) + l.getQuantiteTotale());
		this.totalStocksFeves.ajouter(this,  l.getQuantiteTotale(), this.cryptogramme);
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LPURPLE,"   BOURSEA: obtenu "+ l.getQuantiteTotale()+" T de "+f+" en bourse. Stock -> "+this.stockFeves.get(f));
	}

	public void notificationBlackList(int dureeEnStep) {
		this.journal.ajouter(" aie aie aie ... blackliste de la bourse pendant "+dureeEnStep+" tour");
	}



}
