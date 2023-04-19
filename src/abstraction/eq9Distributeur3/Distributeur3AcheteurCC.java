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
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;


public class Distributeur3AcheteurCC extends Distributeur3Acteur implements IAcheteurContratCadre{
	public static Color COLOR_LLGRAY = new Color(238,238,238);
	
	protected Journal journal;
	
	//faire une méthode qui connait le prix d'achat moyen d'un chocolat
	
	public Distributeur3AcheteurCC() {//ChocolatDeMarque[] chocos, double[] stocks) {
		
		
	}
//Mathilde
	public void next() {
		super.next();
		System.out.print("nexxxxxxt");
		SuperviseurVentesContratCadre supCCadre = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
		if (chocolats.size()>0) {
			System.out.print(">>>>0");
			
			List<IVendeurContratCadre> vendeursChocolat = supCCadre.getVendeurs(chocolats.get(0));
			//creation échéancier
			List<Double>  quantites = new ArrayList();
			quantites.add(1.);
			Echeancier echeancier = new Echeancier (Filiere.LA_FILIERE.getEtape(),quantites);
			if (vendeursChocolat.size()>0) {
				for (int i=0; i< vendeursChocolat.size();i++) {
					supCCadre.demandeAcheteur(this , vendeursChocolat.get(i), chocolats.get(0),echeancier , this.cryptogramme, initialise);
				}
			
			}
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
		
		// william 
		
		// marge de 80% sur HQ_BE
		if(((ChocolatDeMarque)contrat.getProduit()).getGamme() == Gamme.HQ)  {
			var prix_tonne_de_vente = prix*5;
			this.prix_tonne_vente.put((ChocolatDeMarque)contrat.getProduit(), prix_tonne_de_vente);
		}
		
		// marge de 67% sur MQ_BE
		if(((ChocolatDeMarque)contrat.getProduit()).getGamme() == Gamme.MQ && ((ChocolatDeMarque)contrat.getProduit()).isBioEquitable()){
			var prix_tonne_de_vente = prix*3;
			this.prix_tonne_vente.put((ChocolatDeMarque)contrat.getProduit(), prix_tonne_de_vente);
		}
		// marge de 50% sur MQ
		if(((ChocolatDeMarque)contrat.getProduit()).getGamme() == Gamme.MQ  && !((ChocolatDeMarque)contrat.getProduit()).isBioEquitable()) {
			var prix_tonne_de_vente = prix*2;
			this.prix_tonne_vente.put((ChocolatDeMarque)contrat.getProduit(), prix_tonne_de_vente);
		}
		
		
		
		
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
