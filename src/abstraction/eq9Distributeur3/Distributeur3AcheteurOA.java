package abstraction.eq9Distributeur3;

import java.awt.Color;
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

public class Distributeur3AcheteurOA extends Distributeur3Acteur implements IAcheteurOA {
	
	//////////////////////
	//////  william //////
	//////////////////////
	
	
	/////////////////////////////////////////////
	///// largement inspiré du code exemple /////
	/////////////////////////////////////////////
	
	private SuperviseurVentesOA supOA;

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
	
	public void next() {
		super.next();
		if (supOA==null) {
			this.journal_OA.ajouter("Superviseur Offre d'Achat trouvé");
			supOA =(SuperviseurVentesOA)(Filiere.LA_FILIERE.getActeur("Sup.OA"));
		}
		if (supOA!=null && Math.random()<0.1) { // 1 fois sur 10 en moyenne
			this.journal_OA.ajouter("Offre d'Achat initiée");
			PropositionVenteOA pRetenue = supOA.acheterParAO(this, this.cryptogramme, Chocolat.C_MQ, null, 3, false);
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