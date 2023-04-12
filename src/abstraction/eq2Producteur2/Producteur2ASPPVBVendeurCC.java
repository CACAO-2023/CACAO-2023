package abstraction.eq2Producteur2;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Producteur2ASPPVBVendeurCC extends Producteur2ASPPVendeurBourse implements IVendeurContratCadre{

	public Producteur2ASPPVBVendeurCC() {
		super();
	}

	public boolean peutVendre(IProduit produit) {
		return produit instanceof Feve && produit == Feve.F_BQ && produit == Feve.F_MQ && produit == Feve.F_MQ_BE && produit == Feve.F_HQ_BE; //Est-ce qu'on vend vraiment de tout ?
	}
	
	public boolean vend(IProduit produit) {
		boolean testQuantite = true;  /*Implementer dans le IF une condition sur la quantite minimale produite chaque step*/
		if(produit instanceof Feve && ((Feve) produit).isBioEquitable() && testQuantite) {
			return true;			
		}
		return false;
	}

	//On renvoie toujours  un Echeancier constant dans le temps dans la limite de nos capacités de production
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		Echeancier echeancierAch = contrat.getEcheancier();
		if(echeancierAch.getStepDebut() > Filiere.LA_FILIERE.getEtape()) {
			double testQuantite = 0.0;
			for(int i = echeancierAch.getStepDebut(); i<=echeancierAch.getStepFin(); i++) {
				if(contrat.getProduit() instanceof Feve && ((Feve) contrat.getProduit()).getGamme() == Gamme.HQ && ((Feve) contrat.getProduit()).isBioEquitable()) {
					testQuantite = testQuantite + this.getNbHecHauteBE().getValeur()*this.getProdHec().getValeur() - echeancierAch.getQuantite(i);
				}
				if(contrat.getProduit() instanceof Feve && ((Feve) contrat.getProduit()).getGamme() == Gamme.MQ && ((Feve) contrat.getProduit()).isBioEquitable()) {
					testQuantite = testQuantite + this.getNbHecHauteBE().getValeur()*this.getProdHec().getValeur() - echeancierAch.getQuantite(i);
				}
			}
			if(testQuantite < 0.0) {
				return new Echeancier(echeancierAch.getStepDebut(), echeancierAch.getStepFin(), (echeancierAch.getQuantiteTotale()+testQuantite)/echeancierAch.getNbEcheances());
			} 
			return echeancierAch;
		}
		return null;
	}

	public double propositionPrix(ExemplaireContratCadre contrat) {
		if(contrat.getProduit() == Feve.F_HQ_BE) {
			return contrat.getEcheancier().getQuantiteTotale()*this.getPrixHQ();
		}
		if(contrat.getProduit() == Feve.F_MQ_BE) {
			return contrat.getEcheancier().getQuantiteTotale()*this.getPrixMQBE();
		}
		return 0.0;		
	}

	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		if(contrat.getPrix() >= 1) {
			return contrat.getPrix();
		}
		if(contrat.getPrix() >= 0.9) {
			return contrat.getPrix()*0.25+contrat.getEcheancier().getQuantiteTotale()*this.getPrixHQ()*0.75; /*Négociation 1/4||3/4 pour tenter de tirer un prix convenable*/
		}
		return -2;
	}

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.getContrats().add(contrat);
	}

	public Lot livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		System.out.println(Math.min(this.getStockTot((Feve) produit).getValeur(), quantite));
		if(Math.min(this.getStockTot((Feve) produit).getValeur(), quantite) > 0.001) {
			return retirerStock((Feve) produit, Math.min(this.getStockTot((Feve) produit).getValeur(), quantite));
		} else {
			return new Lot(produit);
		}
	}
}
