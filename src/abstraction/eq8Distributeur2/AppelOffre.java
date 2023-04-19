package abstraction.eq8Distributeur2;

import java.util.List;

import abstraction.eqXRomu.offresAchat.IAcheteurOA;
import abstraction.eqXRomu.offresAchat.PropositionVenteOA;

public class AppelOffre extends Distributeur2 implements IAcheteurOA{

	
	//Auteur : Marzougui Mariem
	public PropositionVenteOA choisirPV(List<PropositionVenteOA> propositions) {
		double meilleure_proposition = propositions.get(0).getPrixT()*propositions.get(0).getPrixT()/propositions.get(0).getChocolatDeMarque().qualitePercue();
		PropositionVenteOA proposition=propositions.get(0);
		for (int i=0; i<propositions.size(); i++) {
			if (propositions.get(i).getPrixT()/propositions.get(i).getChocolatDeMarque().qualitePercue()<meilleure_proposition) {
				meilleure_proposition=propositions.get(i).getPrixT()/propositions.get(i).getChocolatDeMarque().qualitePercue();
				proposition=propositions.get(i);
			}	
		}
		return proposition ;
	}

}
