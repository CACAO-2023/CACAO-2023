// Charles



package abstraction.eq1Producteur1;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Producteur1VendeurBourse extends ProducteurVendeurCC implements IVendeurBourse {
	
	// On repère les fêves qui vont périmées pour pouvoir les vendre.
	
	public Double feveBQPeri() { //Elouan
		if (this.stockFeveBas.getQuantites().get(step-11)==null)
			{
			return 0.;
			}
		else {
			return this.stockFeveBas.getQuantites().get(step-11);
		}
	}

	/*public Double feveBQPeri() {
		int FeveBasPeri = 0;
		for (int i=0 ; i <= step ; i++) {
			FeveBasPeri += this.stockFeveBas.getQuantites().get(step);
		}
		return FeveBasPeri;
		}*/
	
	public Double feveMQPeri() { //Elouan
		if (this.stockFeveMoy.getQuantites().get(step-11)==null)
			{
			return 0.0;
			}
		else {
			return this.stockFeveMoy.getQuantites().get(step-11);
		}
	}
	
	/*public int feveMQPeri () {
		int FeveMoyPeri = 0;
		for (int i=0 ; i <= step ; i++) {
			FeveMoyPeri += this.stockFeveMoy.getQuantites().get(step);
		}
		return FeveMoyPeri;
	}*/
	
	
	
	// On vend nos fêves qui vont périmées ainsi que les fêves en surplus (si les coûts de stockage deviennent trop importants --> coûts de stockage >= bénéfices / 2).
	
	public double offre(Feve f, double cours) {
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		int quantite = 1;
		if (f==Feve.F_BQ) {
			Double FeveBQPeri = this.feveBQPeri();
			if ((this.getStockBas().getQuantiteTotale()!=0.0) && 
			   (bourse.getCours(f).getValeur() >= prixMinAvecMarge( f, quantite))) {
				double pourcentage = ((bourse.getCours(f).getValeur()/(prixMinAvecMarge( f, quantite)))/100.0);
					return (this.getStockBas().getQuantiteTotale()*(pourcentage*10))+FeveBQPeri;
				
			}
			return FeveBQPeri;
		}
		
		if (f==Feve.F_MQ) {
			Double FeveMQPeri = this.feveMQPeri();
			if ((this.getStockMoy().getQuantiteTotale()!=0.0 && 
			   (bourse.getCours(f).getValeur() >= prixMinAvecMarge( f, quantite)))) {
				int pourcentage = (int) (10*(bourse.getCours(f).getValeur()/(prixMinAvecMarge( f, quantite))));
				
					return (this.getStockMoy().getQuantiteTotale()*(pourcentage*10))+FeveMQPeri;
			}
			
	
			return FeveMQPeri;
		}
		return 0;
	}
	
	public Lot notificationVente(Feve f, double quantite, double cours) {
		Lot l = new Lot(f);
		
		if (f==Feve.F_BQ) {
			int q = (int)Math.min(quantite, super.getVraiStockB().getQuantiteTotale());
			if (q!=0) {
			l.ajouter(Filiere.LA_FILIERE.getEtape(), q);} // cet exemple ne gere pas la production : tout le stock est considere comme venant d'etre produit;}
			else {return null;}
			this.stockFeveBas.retirer(quantite);
			this.journal_stocks.ajouter("BOURSEV: vente de "+quantite+" T de "+f+" en bourse. Stock -> "+this.getStockBas().getQuantiteTotale());
			this.journal_ventes.ajouter("BOURSEV: vente de "+quantite+" T de "+f+" en bourse. Stock -> "+ quantite*cours);
		}
		if (f==Feve.F_MQ) {
			int q = (int)Math.min(quantite, super.getVraiStockM().getQuantiteTotale());
			if (q!=0) {
			l.ajouter(Filiere.LA_FILIERE.getEtape(), q); }// cet exemple ne gere pas la production : tout le stock est considere comme venant d'etre produit;
			else {return null;}
			this.stockFeveMoy.retirer(quantite);
			this.journal_stocks.ajouter("BOURSEV: vente de "+quantite+" T de "+f+" en bourse. Stock -> "+this.getStockMoy().getQuantiteTotale());
			this.journal_ventes.ajouter("BOURSEV: vente de "+quantite+" T de "+f+" en bourse. Stock -> "+ quantite*cours);
		}
		return l;
	}

	
	public void notificationBlackList(int dureeEnStep) {
		this.journal_evenements.ajouter("Aie... blackliste pendant 6 steps");
	}
	
}