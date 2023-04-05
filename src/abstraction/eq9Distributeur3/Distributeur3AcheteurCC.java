package abstraction.eq9Distributeur3;


import abstraction.eqXRomu.contratsCadres.ContratCadre;

import java.awt.Color;


import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;

import abstraction.eqXRomu.general.Journal;

import abstraction.eqXRomu.produits.ChocolatDeMarque;

import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;


public class Distributeur3AcheteurCC extends Distributeur3Acteur implements IAcheteurContratCadre{
	public static Color COLOR_LLGRAY = new Color(238,238,238);
	protected Integer cryptogramme;
	protected Journal journal;
	
	//faire une méthode qui connait le prix d'achat moyen d'un chocolat
	
	public Distributeur3AcheteurCC(ChocolatDeMarque[] chocos, double[] stocks) {
		super(chocos, stocks);
		
	}

	public void next() {
		super.next();
		
	}
	

	public boolean achete(IProduit produit) {
		if (!(produit instanceof ChocolatDeMarque)) {
			return false;
		}
		return true;

	}

	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return contrat.getEcheancier();
	}

	@Override
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return contrat.getPrix();
	}

	

	@Override
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}
	
	

}
