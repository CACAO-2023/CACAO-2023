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
			for (int i=0; i<chocolats.size();i++) {
				List<IVendeurContratCadre> vendeursChocolat = supCCadre.getVendeurs(chocolats.get(i));
				Echeancier echeancier = new Echeancier (Filiere.LA_FILIERE.getEtape(),4, 1.0);
				System.out.println(""+vendeursChocolat.size()+" v");
				if (vendeursChocolat.size()>0 && stock.getQteStock().get(chocolats.get(i))<10.0 ) {
					boolean pasAchete=true;
					for (int j=0; j< vendeursChocolat.size()&&pasAchete;j++) {
						System.out.println(""+chocolats.get(i)+" demande");
						ExemplaireContratCadre cc =supCCadre.demandeAcheteur(this , vendeursChocolat.get(j), chocolats.get(i), echeancier , this.cryptogramme, initialise);
						pasAchete = false;
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
		ChocolatDeMarque choco = (ChocolatDeMarque)contrat.getProduit();
		// william 
		
		// on calcule le prix de vente du chocolat du contract en fonction de la gamme
		
		
		double prix_tonne_de_vente_contrat = 0.0;
		
		
		// marge de 80% sur HQ_BE
		if(choco.getGamme() == Gamme.HQ)  {
			prix_tonne_de_vente_contrat = prix*5;
		}
		// marge de 67% sur MQ_BE
		if(((ChocolatDeMarque)contrat.getProduit()).getGamme() == Gamme.MQ && ((ChocolatDeMarque)contrat.getProduit()).isBioEquitable()){
			prix_tonne_de_vente_contrat = prix*3;
		}
		// marge de 50% sur MQ
		if(((ChocolatDeMarque)contrat.getProduit()).getGamme() == Gamme.MQ  && !((ChocolatDeMarque)contrat.getProduit()).isBioEquitable()) {
			prix_tonne_de_vente_contrat = prix*2;
		}
		
		double prix_tonne_de_vente_apres_achat = 0.0;
		
		
		// si il existe deja un stock de ce chocolat, on fait la moyenne des prix pondérés par la quantite acheté et la quantite deja stockee
		// si il y a du stock
		if(stock.getStock(choco) != 0) {
			var qtte_actuelle = stock.getStock(choco);
			var qtte_apres_achat = qtte_actuelle + contrat.getQuantiteTotale();
			// proportion de nouveau chocolat
			var proportion_contrat = contrat.getQuantiteTotale()/qtte_apres_achat;
			// ponderation
			prix_tonne_de_vente_apres_achat = prix_tonne_de_vente_contrat*proportion_contrat +prix_tonne_vente.get(choco)*(1-proportion_contrat) ;
		}
		// il n'y a pas de stock
		else {
			prix_tonne_de_vente_apres_achat = prix_tonne_de_vente_contrat;
		}
		
		this.prix_tonne_vente.put((ChocolatDeMarque)contrat.getProduit(), prix_tonne_de_vente_apres_achat);
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
