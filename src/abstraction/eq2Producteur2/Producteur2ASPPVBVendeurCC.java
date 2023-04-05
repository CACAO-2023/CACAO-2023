package abstraction.eq2Producteur2;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
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

	@Override
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
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
