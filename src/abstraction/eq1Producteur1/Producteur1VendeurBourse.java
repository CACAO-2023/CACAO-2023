// Charles



package abstraction.eq1Producteur1;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Producteur1VendeurBourse extends Producteur1Plantation implements IVendeurBourse {
	
	public double offre(Feve f, double cours) {
			
		if (f==Feve.F_BQ) {
			if (getStockBas().getQuantiteTotale()!=0.0) {
				return getStockBas().getQuantiteTotale()/10;
			}
		}
		if (f==Feve.F_MQ) {
			if (getStockMoy().getQuantiteTotale()!=0.0) {
				return getStockMoy().getQuantiteTotale()/10;
			}
		}
		return 0;
	}
	
	public Lot notificationVente(Feve f, double quantite, double cours) {
		Lot l = new Lot(f);
		l.ajouter(Filiere.LA_FILIERE.getEtape(), quantite); // cet exemple ne gere pas la production : tout le stock est considere comme venant d'etre produit
		if (f==Feve.F_BQ) {
			this.stockFeveBas.retirer(quantite);
			this.journal_stocks.ajouter("BOURSEV: vente de "+quantite+" T de "+f+" en bourse. Stock -> "+getStockBas().getQuantiteTotale());
			this.journal_ventes.ajouter("BOURSEV: vente de "+quantite+" T de "+f+" en bourse. Stock -> "+ quantite*cours);
		}
		if (f==Feve.F_MQ) {
			this.stockFeveMoy.retirer(quantite);
			this.journal_stocks.ajouter("BOURSEV: vente de "+quantite+" T de "+f+" en bourse. Stock -> "+getStockMoy().getQuantiteTotale());
			this.journal_ventes.ajouter("BOURSEV: vente de "+quantite+" T de "+f+" en bourse. Stock -> "+ quantite*cours);
		}
		return l;
	}

	
	public void notificationBlackList(int dureeEnStep) {
		this.journal.ajouter("Aie... blackliste pendant 6 steps");
	}
	
}
