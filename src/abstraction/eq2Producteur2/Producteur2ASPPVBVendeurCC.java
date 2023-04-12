package abstraction.eq2Producteur2;

import java.util.LinkedList;
import java.util.List;

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
		return produit instanceof Feve && (((Feve) produit).getGamme() != Gamme.MQ || ((Feve) produit).isBioEquitable() != false);
	}
	
	public boolean vend(IProduit produit) {
		boolean testQuantite = true;  /*Implementer dans le IF une condition sur la quantite minimale produite chaque step*/
		if(produit instanceof Feve && ((Feve) produit).isBioEquitable() && testQuantite) {
			return true;			
		}
		return false;
	}

	//On renvoie toujours  un Echeancier constan tdans le temps dans la limite de nos cpaacités de production
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
		if(contrat.getProduit() instanceof Feve && ((Feve) contrat.getProduit()).getGamme() == Gamme.HQ) {
			return contrat.getEcheancier().getQuantiteTotale()*this.getPrixHQ();
		}
		if(contrat.getProduit() instanceof Feve && ((Feve) contrat.getProduit()).getGamme() == Gamme.MQ) {
			return contrat.getEcheancier().getQuantiteTotale()*this.getPrixMQBE();
		}
		return 0.0;		
	}

	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Lot livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return null;
	}

	/*Creer une fonction qui renvoie les quantités à livrer au prochain step/aux prochains steps (proposition)
	*/
}
