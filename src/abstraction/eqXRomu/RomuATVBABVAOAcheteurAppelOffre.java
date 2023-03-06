package abstraction.eqXRomu;

import abstraction.eqXRomu.appelsOffres.IAcheteurAO;
import abstraction.eqXRomu.appelsOffres.OffreVente;
import abstraction.eqXRomu.appelsOffres.PropositionAchatAO;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class RomuATVBABVAOAcheteurAppelOffre extends RomuATVBABVendeurAppelOffre implements IAcheteurAO {

	//========================================================
	//                        IAcheteurAO
	//========================================================
	public double proposerPrix(OffreVente offre) {
		if (offre.getVendeur()==this) {
			return 0.0;
		}
		Chocolat c = offre.getChocolat().getChocolat();
		double prix=0.0;
		switch (c) {
		case C_HQ_BE   : prix= 13000.0;break;
		case C_MQ_BE   : prix=  9000.0;break;
		case C_MQ      : prix=  8000.0;break;
		case C_BQ      : prix=  7000.0;break;
		}
		double solde = Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme);
		double res =  prix>0 && solde> offre.getQuantiteT()*prix ? prix : 0;
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LGREEN, "   AOA : propose prix de "+res+" pour chocolat "+c+ " (solde = "+solde+")");
		return res;
	}

	public void notifierAchatAO(PropositionAchatAO propositionRetenue) {
		ChocolatDeMarque cm = propositionRetenue.getOffre().getChocolat();
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LGREEN, "   AOA : offre retenue de "+propositionRetenue.getOffre().getQuantiteT()+"T de "+cm);
		if (this.stockChocoMarque.keySet().contains(cm)) {
			this.stockChocoMarque.put(cm, this.stockChocoMarque.get(cm)+propositionRetenue.getOffre().getQuantiteT());
		} else {
			this.stockChocoMarque.put(cm, propositionRetenue.getOffre().getQuantiteT());
		}
		this.totalStocksChocoMarque.ajouter(this, propositionRetenue.getOffre().getQuantiteT(), this.cryptogramme);
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LGREEN, "   AOA : -> stock("+cm+") = "+this.stockChocoMarque.get(cm));
	}

	public void notifierPropositionNonRetenueAO(PropositionAchatAO propositionNonRetenue) {
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LGREEN, "   AOA : offre non retenue ");
	}

}
