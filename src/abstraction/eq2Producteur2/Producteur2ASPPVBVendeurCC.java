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

	public Producteur2ASPPVBVendeurCC(Feve feve, double stock) {
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

	@Override
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		Echeancier echeancierAch = contrat.getEcheancier();
		if(echeancierAch.getStepDebut() > Filiere.LA_FILIERE.getEtape()) {
			double testQuantite = 0.0;
			double maxDeficit = 0.0;
			for(int i = echeancierAch.getStepDebut(); i<=echeancierAch.getStepFin(); i++) {
				if(contrat.getProduit() instanceof Feve && ((Feve) contrat.getProduit()).getGamme() == Gamme.HQ) {
					testQuantite = testQuantite + this.getNbHecHauteBE().getValeur()*this.getProdHec().getValeur() - echeancierAch.getQuantite(i);
				}
				if(contrat.getProduit() instanceof Feve && ((Feve) contrat.getProduit()).getGamme() == Gamme.MQ) {
					testQuantite = testQuantite + this.getNbHecHauteBE().getValeur()*this.getProdHec().getValeur() - echeancierAch.getQuantite(i);
				}
				if(testQuantite < 0.0) {
					maxDeficit = Math.min(maxDeficit, testQuantite/(i - echeancierAch.getStepDebut()));
				}
			}
			if(maxDeficit < 0) {
				List<Double> quantites = new LinkedList<Double>();
				for(int i = echeancierAch.getStepDebut(); i<=echeancierAch.getStepFin(); i++) {
					quantites.add(echeancierAch.getQuantite(i));
				}
			}
		}
		return null;
	}

	@Override
	public double propositionPrix(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
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
