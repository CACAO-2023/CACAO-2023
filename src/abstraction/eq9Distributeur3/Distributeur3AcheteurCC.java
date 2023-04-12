package abstraction.eq9Distributeur3;


import abstraction.eqXRomu.contratsCadres.ContratCadre;
import java.awt.Color;
import java.util.List;
import java.util.Map.Entry;
import java.util.ArrayList;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;


public class Distributeur3AcheteurCC extends Distributeur3Acteur implements IAcheteurContratCadre{
	public static Color COLOR_LLGRAY = new Color(238,238,238);
	protected Integer cryptogramme;
	protected Journal journal;
	
	//faire une méthode qui connait le prix d'achat moyen d'un chocolat
	
	public Distributeur3AcheteurCC(ChocolatDeMarque[] chocos, double[] stocks) {
		
		
	}
//Mathilde
	public void next() {
		super.next();
		SuperviseurVentesContratCadre supCCadre = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
		List<IVendeurContratCadre> vendeursChocolat = supCCadre.getVendeurs(chocolats.get(0));
		//creation échéancier
		List<Double>  quantites = new ArrayList();
		quantites.add(1.);
		Echeancier echeancier = new Echeancier (Filiere.LA_FILIERE.getEtape(),quantites);
		if (vendeursChocolat.size()>0) {
		supCCadre.demandeAcheteur(this , vendeursChocolat.get(0), chocolats.get(0),echeancier , this.cryptogramme, initialise);
		}

		
	}
	
// Mathilde : on accepte les chocolats qu'on vend
	public boolean achete(IProduit produit) {
		if (!(produit instanceof ChocolatDeMarque)) {
			
			return false;
		}
		for (ChocolatDeMarque chocolat: chocolats) {
			if (((ChocolatDeMarque)produit).equals(chocolat)){
				return true;
			}
		}
		return false;

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
		prix = contrat.getPrix()/contrat.getQuantiteTotale();
		return contrat.getPrix();
		
	}

	

	@Override
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		IProduit nt;
		this.journal_achats = new Journal("On receptionne du chocolat : " + contrat.getProduit() + " en quantite : " + lot.getQuantiteTotale(), this);
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
