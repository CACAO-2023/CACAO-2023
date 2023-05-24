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
