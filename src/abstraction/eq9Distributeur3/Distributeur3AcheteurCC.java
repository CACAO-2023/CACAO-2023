package abstraction.eq9Distributeur3;


import abstraction.eqXRomu.contratsCadres.ContratCadre;

import java.awt.Color;
import java.util.Map.Entry;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
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
		
		
	}

	public void next() {
		super.next();
		
	}
	
// Sami : version simpliste ou on accepte tous les chocolats
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
//Mathilde 
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
		IProduit nt;
		stock.ajoutQte(((ChocolatDeMarque)(contrat.getProduit())), lot.getQuantiteTotale());
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		this.journal.ajouter("Etape "+ Filiere.LA_FILIERE.getEtape()+ " : " + "je viens de passer le contrat "+contrat);
	}
	
	// mettre à jour dans notification et next
	public void prixMoyen (ChocolatDeMarque choc) {
		double prixMoy = 0.0;
		for (Entry<ChocolatDeMarque, Double[]> chocolat : prixMoyen.entrySet()) {
			if (chocolat.equals(choc) && achete(choc)) {
				prixMoyen.replace(choc, prixMoyen.get(choc), prixMoyen.get(choc) );
				// sur la quantité
			}
		}
		
	}
	

}
