package abstraction.eq8Distributeur2;

import java.util.List;


import abstraction.eqXRomu.offresAchat.IAcheteurOA;
import abstraction.eqXRomu.offresAchat.PropositionVenteOA;

public class Distributeur2AcheteurOA extends ContratCadre implements IAcheteurOA{

	
	
	//Karim Ben Messaoud: retourne la prop avec le meilleur rapport qualité prixs
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

}
