package abstraction.eq8Distributeur2;

import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.offresAchat.IAcheteurOA;
import abstraction.eqXRomu.offresAchat.PropositionVenteOA;
import abstraction.eqXRomu.offresAchat.SuperviseurVentesOA;
import abstraction.eqXRomu.produits.Chocolat;

public class Distributeur2AcheteurOA extends ContratCadre implements IAcheteurOA{

	private SuperviseurVentesOA supOA;
	
	
	public void initialiser() {	
		super.initialiser();
	}
	
	//Karim Ben Messaoud: retourne la prop avec le meilleur rapport qualité prix
	
	public PropositionVenteOA choisirPV(List<PropositionVenteOA> propositions) {
        PropositionVenteOA meilleureProposition = null;
        double meilleurRapport = 0;

        for (PropositionVenteOA proposition : propositions) {
            double rapport = (double) proposition.getChocolatDeMarque().qualitePercue() / proposition.getPrixT();

            if (rapport > meilleurRapport) {
                meilleureProposition = proposition;
                meilleurRapport = rapport;
                
            }
        }
        return meilleureProposition;
        
        
        
        
    }
  
	/**
	public void next() {
		super.next();
		if (supOA==null) {
			supOA =(SuperviseurVentesOA)(Filiere.LA_FILIERE.getActeur("Sup.OA"));
		}
		if (supOA!=null && Math.random()<0.1) { // 1 fois sur 10 en moyenne
			PropositionVenteOA pRetenue = supOA.acheterParAO(this, this.cryptogramme, Chocolat.C_MQ, null, 3, false);
			if (pRetenue!=null) {
				double nouveauStock = pRetenue.getOffre().getQuantiteT();
				if (this.stocks.getStock(chocolats).contains(pRetenue.getChocolatDeMarque())) {
					nouveauStock+=this.stockChocoMarque.get(pRetenue.getChocolatDeMarque());
				}
				this.stockChocoMarque.put(pRetenue.getChocolatDeMarque(), nouveauStock);
				this.journal_achats.ajouter("   Achat par offre d'achat de "+pRetenue+" --> quantite en stock = "+nouveauStock);
			}
		}
		
	}
	public PropositionVenteOA choisirPV(List<PropositionVenteOA> propositions) {
		double qMin=Double.MAX_VALUE;
		for (PropositionVenteOA p : propositions) {
			qMin=Math.min(qMin, p.getChocolatDeMarque().qualitePercue()); 
		}
		double best=Double.MAX_VALUE;
		int iBest=0;
		for (int i=0; i<propositions.size(); i++) {
			double rapport = (propositions.get(i).getPrixT()*propositions.get(i).getOffre().getQuantiteT())/(propositions.get(i).getChocolatDeMarque().qualitePercue()/qMin);
			if (propositions.get(i).getVendeur()!=this && rapport<best ) {
				best = rapport;
				iBest=i;
			}
		}
		return (propositions.get(iBest).getVendeur()==this || propositions.get(iBest).getPrixT()>18.0)?null:propositions.get(iBest);
	}
**/
	//karim
	public void next() {
		this.journal_OA.ajouter("next");
		
	    super.next();
	    if (supOA == null) {
	        supOA = (SuperviseurVentesOA) (Filiere.LA_FILIERE.getActeur("Sup.OA"));
	    }
	    if (supOA != null) {
            this.journal_OA.ajouter("SUPERVISEUR TROUVE");

	    	for (int i =0;this.chocolats.size()> i; i++) {
		        PropositionVenteOA pRetenue = supOA.acheterParAO(this, this.cryptogramme, this.chocolats.get(i).getChocolat(), this.chocolats.get(i).getMarque(), 3, true);

			

	        if (pRetenue != null) {
	            
	        	double nouveauStock = pRetenue.getOffre().getQuantiteT();
	        	stock_total += nouveauStock;
	            this.stocks.ajouterAuStock(pRetenue.getChocolatDeMarque(), nouveauStock);
	    		s.setValeur(this, stock_total, this.cryptogramme);
	            this.journal_OA.ajouter("Achat par offre d'achat de " + pRetenue + " --> quantite en stock = " + nouveauStock);
	        }}
	    }
	}

	
	
	
	
	

}
