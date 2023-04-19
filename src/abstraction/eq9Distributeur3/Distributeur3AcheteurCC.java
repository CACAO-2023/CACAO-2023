package abstraction.eq9Distributeur3;


import abstraction.eqXRomu.contratsCadres.ContratCadre;
import java.awt.Color;
import java.util.List;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;

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
	
	protected Journal journal;
	private List<ExemplaireContratCadre> contratEnCours;
	
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
			for (int i=0; i<chocolats.size();i++) {
				List<IVendeurContratCadre> vendeursChocolat = supCCadre.getVendeurs(chocolats.get(i));
				Echeancier echeancier = new Echeancier (Filiere.LA_FILIERE.getEtape(),24, 25000.0);
				System.out.println(""+vendeursChocolat.size()+" v");
				List<ExemplaireContratCadre> contratAvecChocolat = new ArrayList<ExemplaireContratCadre> ();
				if (contratEnCours != null) {
				for (int k = 0;k<contratEnCours.size();k++) {
					if (chocolats.get(i).equals((ChocolatDeMarque)((contratEnCours.get(k).getProduit())))) {
						contratAvecChocolat.add(contratEnCours.get(k));
					}
				}
				}
				if (vendeursChocolat.size()>0  ) {
					boolean pasAchete=true;
					if (contratAvecChocolat.size()==0) {
					for (int j=0; j< vendeursChocolat.size()&&pasAchete;j++) {
						System.out.println(""+chocolats.get(i)+" demande");
						
						//Echeancier echeancier = new Echeancier (contratEnCours.get(i).getEcheancier().getStepFin(),24, 25000.0);
						ExemplaireContratCadre cc =supCCadre.demandeAcheteur(this , vendeursChocolat.get(j), chocolats.get(i), echeancier , this.cryptogramme, initialise);
						pasAchete = false;
					}
					for (int k = 0;k<contratAvecChocolat.size();k++) {
						for (int j=0; j< vendeursChocolat.size()&&pasAchete;j++) {
							System.out.println(""+chocolats.get(i)+" demande");
							
							Echeancier echeancier2 = new Echeancier (contratAvecChocolat.get(k).getEcheancier().getStepFin(),24, 25000.0);
							ExemplaireContratCadre cc =supCCadre.demandeAcheteur(this , vendeursChocolat.get(j), chocolats.get(i), echeancier2 , this.cryptogramme, initialise);
							pasAchete = false;
					}
					}
				
				}
				
			}
			/*List<IVendeurContratCadre> vendeursChocolat = supCCadre.getVendeurs(chocolats.get(0));
			//creation échéancier
			List<Double>  quantites = new ArrayList();
			quantites.add(1.);
			quantites.add(1.);
			Echeancier echeancier = new Echeancier (Filiere.LA_FILIERE.getEtape(),4, 1.0);
			System.out.println(""+vendeursChocolat.size()+" v");
			if (vendeursChocolat.size()>0) {
				
				for (int i=0; i< vendeursChocolat.size();i++) {
					System.out.println(""+chocolats.get(0)+" demande");
					supCCadre.demandeAcheteur(this , vendeursChocolat.get(i), chocolats.get(0),echeancier , this.cryptogramme, initialise);
				}
			
			}*/
			}
		}
		

		
	}
	
// Mathilde : on accepte les chocolats qu'on vend
	public boolean achete(IProduit produit) {
		if (!(produit instanceof ChocolatDeMarque)) {
			
			return false;
		}
		for (ChocolatDeMarque chocolat: chocolats) {
			if (((ChocolatDeMarque)produit).equals(chocolat) && stock.getQteStock().get(chocolat)<10.0){
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
		journal_ventes.ajouter("achat du chocolat" + contrat.getProduit()+"au prix à la tonne de" + prix);
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
		this.contratEnCours.add(contrat);
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
