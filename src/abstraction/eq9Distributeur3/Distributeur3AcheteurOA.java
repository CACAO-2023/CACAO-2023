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
import abstraction.eqXRomu.offresAchat.OffreAchat;
import abstraction.eqXRomu.offresAchat.PropositionVenteOA;
import abstraction.eqXRomu.offresAchat.SuperviseurVentesOA;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;



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
		journal_OA.ajouter("determination_qte_cible");
		for(int i =0; i <  this.chocolats.size();i++) {

			if(stock.getStock(this.chocolats.get(i)) < 20000) {
				double cible = 20000 - stock.getStock(this.chocolats.get(i));
				qte_cible.put(this.chocolats.get(i), cible);
				journal_OA.ajouter(this.chocolats.get(i) + " needs an OA of qte : "+ cible);
			}
			else {
				qte_cible.put(this.chocolats.get(i), 0.0);
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
		double best=Double.MIN_VALUE;
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
			System.out.print(prixMax);
			if(prixMax != null) {
				prix_palier = prixMax.get(c.getChocolat());

			}
			
		}
		journal_OA.ajouter("prix_palier de " +prix_palier );
		
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
		
		journal_OA.ajouter("Etape "+ Filiere.LA_FILIERE.getEtape()+ " \n");
		
		determination_qte_cible();
		
		
		if (supOA==null) {
			this.journal_OA.ajouter("Superviseur Offre d'Achat trouvé");
			supOA =(SuperviseurVentesOA)(Filiere.LA_FILIERE.getActeur("Sup.OA"));
		}
		if (supOA!=null) { // 1 fois sur 10 en moyenne
			
			
			this.journal_OA.ajouter("Offre d'Achat initiée");
			
			for(int i =0; i < this.chocolats.size();i++) {
				
				if( qte_cible.get(this.chocolats.get(i)) > 2.0) {
					
				
					PropositionVenteOA pRetenue = supOA.acheterParAO(this, this.cryptogramme, this.chocolats.get(i).getChocolat(), this.chocolats.get(i).getMarque(), qte_cible.get(chocolats.get(i)), false);

			
					if (pRetenue!=null) {
						double nouveauStock = pRetenue.getOffre().getQuantiteT();
						if (this.stock.QteStock.keySet().contains(pRetenue.getChocolatDeMarque())) {
							nouveauStock+=this.stock.QteStock.get(pRetenue.getChocolatDeMarque());
						}
						this.stock.QteStock.put(pRetenue.getChocolatDeMarque(), nouveauStock);
						this.journal_OA.ajouter("   Achat par offre d'achat de "+pRetenue+" --> quantite en stock = "+nouveauStock);
						this.journal_achats.ajouter("   Achat par offre d'achat de "+pRetenue+" --> quantite en stock = "+nouveauStock);

						this.journal_OA.ajouter("On adapte le prix de vente en fonction du prix d'achat de cette offre d'achat");
						this.adapter_prix_vente(pRetenue);
						
						

				}
				}
			}
		}
		journal_OA.ajouter("on actualise qte cible tot");
		qte_cible_tot();
		
	}
	
	public void qte_cible_tot() {
		double qte_cible_TOT = 0.0;
		for(int i =0; i < this.qte_cible.size();i++) {
			
			qte_cible_TOT += this.qte_cible.get(this.chocolats.get(i));
		}
		this.qte_cible_OA_TOT = qte_cible_TOT;
		journal_OA.ajouter("qte cible tot : " + qte_cible_TOT);
	}
	
	
	public void adapter_prix_vente(PropositionVenteOA proposition) {
		
		
		double prix_proposition = proposition.getPrixT() /*/contrat.getQuantiteTotale() deja à la tonne */;
		journal_OA.ajouter("achat du chocolat" + proposition.getChocolatDeMarque()+"au prix à la tonne de" + prix + "par offre d'achat");
		ChocolatDeMarque choco = (ChocolatDeMarque)proposition.getChocolatDeMarque();

		// on calcule le prix de vente du chocolat dus contract en fonction de la gamme
		double prix_tonne_de_vente_contrat = 0.0;

		// marge de 80% sur HQ_BE
		if(choco.getGamme() == Gamme.HQ)  {
			prix_tonne_de_vente_contrat = prix_proposition*5;
		}
		// marge de 67% sur MQ_BE
		if(((ChocolatDeMarque)proposition.getChocolatDeMarque()).getGamme() == Gamme.MQ && ((ChocolatDeMarque)proposition.getChocolatDeMarque()).isBioEquitable()){
			prix_tonne_de_vente_contrat = prix_proposition*3;
		}
		// marge de 50% sur MQ
		if(((ChocolatDeMarque)proposition.getChocolatDeMarque()).getGamme() == Gamme.MQ  && !((ChocolatDeMarque)proposition.getChocolatDeMarque()).isBioEquitable()) {
			prix_tonne_de_vente_contrat = prix*2;
		}
		
		

		double prix_tonne_de_vente_apres_achat = 0.0;

		// si il existe deja un stock de ce chocolat, on fait la moyenne des prix pondérés par la quantite acheté et la quantite deja stockee
		// si il y a du stock
		if(stock.getStock(choco) != 0) {
			double qtte_actuelle = stock.getStock(choco);
			double qtte_apres_achat = qtte_actuelle + proposition.getOffre().getQuantiteT();
			// proportion de nouveau chocolat
			double proportion_contrat = proposition.getOffre().getQuantiteT()/qtte_apres_achat;
			// ponderation
			prix_tonne_de_vente_apres_achat = prix_tonne_de_vente_contrat*proportion_contrat +this.prix_tonne_vente.get(choco)*(1-proportion_contrat) ;
		}
		// il n'y a pas de stock
		else {
			prix_tonne_de_vente_apres_achat = prix_tonne_de_vente_contrat;
		}
		this.journal_prix_vente.ajouter("Etape "+ Filiere.LA_FILIERE.getEtape()+ " : " +"ancien prix tonne de " + proposition.getChocolatDeMarque()+" est de " + prix_tonne_vente.get(choco) + "€");
		this.journal_prix_vente.ajouter("Etape "+ Filiere.LA_FILIERE.getEtape()+ " : " +"nouveau prix tonne de " + proposition.getChocolatDeMarque()+" est de " + prix_tonne_de_vente_apres_achat + "€");

		this.prix_tonne_vente.put((ChocolatDeMarque)proposition.getChocolatDeMarque(), prix_tonne_de_vente_apres_achat);
		
	}

}