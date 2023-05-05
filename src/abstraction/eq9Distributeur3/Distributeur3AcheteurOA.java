package abstraction.eq9Distributeur3;

import java.awt.Color;



import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.RomuATVBABVAOAAOACCVCCDistributeur;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.offresAchat.IAcheteurOA;
import abstraction.eqXRomu.offresAchat.PropositionVenteOA;
import abstraction.eqXRomu.offresAchat.SuperviseurVentesOA;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;



public class Distributeur3AcheteurOA extends Distributeur3AcheteurCC implements IAcheteurOA {
	
	//////////////////////
	//////  william //////
	//////////////////////
	
	
	/////////////////////////////////////////////
	///// largement inspiré du code exemple /////
	/////////////////////////////////////////////
	
	public Distributeur3AcheteurOA() {
		super();
		qte_cible = new HashMap<ChocolatDeMarque, Double>();
	}
	
	
	
	HashMap<ChocolatDeMarque, Double> qte_cible;
	
	private SuperviseurVentesOA supOA;
	
	public void determination_qte_cible() {
		// on regarde la quantite que l'on peut vendre (qui est achetée par les clients) et on retire ce que 
		// l'on a en stock pour savoir ce qu'il faut ajouter en plus pour ne pas avoit de repture de stock
		// et cela pour chaque chocolat que l'on vend
		// on fait l'hypothèse que on peut vendre 3000 de chaque chocolat (très peu réaliste)
		
		for(int i =0; i <  chocolats.size();i++) {
			if(stock.getStock(chocolats.get(i)) < 3000) {
				double cible = 3000 - stock.getStock(chocolats.get(i));
				qte_cible.put(chocolats.get(i), cible);
			}
			else {
				qte_cible.put(chocolats.get(i), 0.0);
			}
		}
	}

	
	// choisir la meilleure proposition parmi celles proposées
	@SuppressWarnings("unlikely-arg-type")
	public PropositionVenteOA choisirPV(List<PropositionVenteOA> propositions) {
		
		/*
		// qMin est la qualité minimale proposée, je ne vois pas l'intérêt de normaliser par qMin
		double qMin=Double.MAX_VALUE; 
		for (PropositionVenteOA p : propositions) {
			qMin=Math.min(qMin, p.getChocolatDeMarque().qualitePercue()); 
		}
		
		*/
		double best=Double.MAX_VALUE;
		int iBest=0;
		for (int i=0; i<propositions.size(); i++) {
			
			// rapport qualité prix (prixT est le prix à la tonne ???) (hypothèse)

			double rapport_qualite_prix = (propositions.get(i).getChocolatDeMarque().qualitePercue()/propositions.get(i).getPrixT());

			// si le rapport est plus petit que le meilleur, il devient le meilleur
			if (propositions.get(i).getVendeur()!=this && rapport_qualite_prix > best ) {
				best = rapport_qualite_prix;
				iBest=i;
			}
		}
		// on s'assure que le prix proposé n'est pas ssupérieur aux prix max que l'on s'est fixés
		ChocolatDeMarque c = propositions.get(iBest).getChocolatDeMarque();
		double prix_palier =40000;
		if(c != null) { 
		//	prix_palier = prixMax.get(propositions.get(iBest).getChocolatDeMarque());
			prix_palier = 20000;
		}
		
		if(propositions.get(iBest).getVendeur()==this || propositions.get(iBest).getPrixT()> prix_palier) {
			journal_OA.ajouter("OA de " + propositions.get(iBest).getVendeur().getNom() + " refusée du fait du prix trop élevé ou nous sommes le vendeur & l'acheteur");
			return null;
		}
		else {
			journal_OA.ajouter("OA de "+ propositions.get(iBest).getVendeur().getNom()+" acceptée");
			return propositions.get(iBest);

		} 
		
		
	}
	
	public void next() {
		super.next();
		
		determination_qte_cible();
		
		
		if (supOA==null) {
			this.journal_OA.ajouter("Superviseur Offre d'Achat trouvé");
			supOA =(SuperviseurVentesOA)(Filiere.LA_FILIERE.getActeur("Sup.OA"));
		}
		if (supOA!=null && Math.random()<0.1) { // 1 fois sur 10 en moyenne
			
			
			this.journal_OA.ajouter("Offre d'Achat initiée");
			
			for(int i =0; i < chocolats.size();i++) {
				
				if( qte_cible.get(chocolats.get(i)) > 2.0) {
					
				
					PropositionVenteOA pRetenue = supOA.acheterParAO(this, this.cryptogramme, chocolats.get(i).getChocolat(), chocolats.get(i).getMarque(), qte_cible.get(chocolats.get(i)), false);

			
					if (pRetenue!=null) {
						double nouveauStock = pRetenue.getOffre().getQuantiteT();
						if (this.stock.QteStock.keySet().contains(pRetenue.getChocolatDeMarque())) {
							nouveauStock+=this.stock.QteStock.get(pRetenue.getChocolatDeMarque());
						}
						this.stock.QteStock.put(pRetenue.getChocolatDeMarque(), nouveauStock);
						this.journal_OA.ajouter("   Achat par offre d'achat de "+pRetenue+" --> quantite en stock = "+nouveauStock);
				}
				}
			}
		}
		
	}

}