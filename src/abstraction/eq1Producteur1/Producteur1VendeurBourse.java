// Charles



package abstraction.eq1Producteur1;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Producteur1VendeurBourse extends ProducteurVendeurCC implements IVendeurBourse {
	
	// On repère les fêves qui vont périmées pour pouvoir les vendre.
	
	public int feveBQPeri() {
		
		int FeveBasPeri = 0;
		for (int i=0 ; i <= step ; i++) {
			FeveBasPeri += this.stockFeveBas.getQuantites().get(step);
		}
		return FeveBasPeri;
		
	}
	
	public int feveMQPeri () {
		
		int FeveMoyPeri = 0;
		for (int i=0 ; i <= step ; i++) {
			FeveMoyPeri += this.stockFeveMoy.getQuantites().get(step);
		}
		return FeveMoyPeri;
		
	}
	
	
	
	// On vend nos fêves qui vont périmées ainsi que les fêves en surplus (si les coûts de stockage deviennent trop importants --> coûts de stockage >= bénéfices / 2).
	
	public double offre(Feve f, double cours) {
		
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
			
		if (f==Feve.F_BQ) {
			int FeveBQPeri = this.feveBQPeri();
			if (this.getStockBas().getQuantiteTotale()!=0.0 && 
			   ((this.stockFeveBas.getQuantiteTotale()+this.stockFeveMoy.getQuantiteTotale())*50) >= (this.stockFeveBas.getQuantiteTotale()*bourse.getCours(Feve.F_BQ).getValeur()+this.stockFeveMoy.getQuantiteTotale()*bourse.getCours(Feve.F_MQ).getValeur())/2 ) {
				System.out.print(this.getStockBas().getQuantiteTotale()/10);
				return this.getStockBas().getQuantiteTotale()/10+FeveBQPeri;
			}
			return FeveBQPeri;
		}
		
		if (f==Feve.F_MQ) {
			int FeveMQPeri = this.feveMQPeri();
			if (this.getStockMoy().getQuantiteTotale()!=0.0 && 
			   ((this.stockFeveBas.getQuantiteTotale()+this.stockFeveMoy.getQuantiteTotale())*50) >= (this.stockFeveBas.getQuantiteTotale()*bourse.getCours(Feve.F_BQ).getValeur()+this.stockFeveMoy.getQuantiteTotale()*bourse.getCours(Feve.F_MQ).getValeur())/2  ) {
			return this.getStockMoy().getQuantiteTotale()/10+ FeveMQPeri;
			}

			return FeveMQPeri;
		}
		return 0;
	}
	
	public Lot notificationVente(Feve f, double quantite, double cours) {
		Lot l = new Lot(f);
		l.ajouter(Filiere.LA_FILIERE.getEtape(), quantite); // cet exemple ne gere pas la production : tout le stock est considere comme venant d'etre produit
		if (f==Feve.F_BQ) {
			this.stockFeveBas.retirer(quantite);
			this.journal_stocks.ajouter("BOURSEV: vente de "+quantite+" T de "+f+" en bourse. Stock -> "+this.getStockBas().getQuantiteTotale());
			this.journal_ventes.ajouter("BOURSEV: vente de "+quantite+" T de "+f+" en bourse. Stock -> "+ quantite*cours);
		}
		if (f==Feve.F_MQ) {
			this.stockFeveMoy.retirer(quantite);
			this.journal_stocks.ajouter("BOURSEV: vente de "+quantite+" T de "+f+" en bourse. Stock -> "+this.getStockMoy().getQuantiteTotale());
			this.journal_ventes.ajouter("BOURSEV: vente de "+quantite+" T de "+f+" en bourse. Stock -> "+ quantite*cours);
		}
		return l;
	}

	
	public void notificationBlackList(int dureeEnStep) {
		this.journal.ajouter("Aie... blackliste pendant 6 steps");
	}
	
}