package abstraction.eq4Transformateur1.Vente;

import abstraction.eqXRomu.offresAchat.IVendeurOA;
import abstraction.eqXRomu.offresAchat.OffreAchat;
import abstraction.eqXRomu.offresAchat.PropositionVenteOA;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class AODistributeur extends CC_distributeur implements IVendeurOA {

	public PropositionVenteOA proposerVente(OffreAchat offre) {
		this.journal.ajouter("proposerVente(offre = "+offre+")");
		for (ChocolatDeMarque c : this.stockChocoMarque.keySet()) {
			this.journal.ajouter("      "+c+" "+(offre.getChocolat().equals(c.getChocolat())?"convient":"ne convient pas")+" "+(offre.getMarque()==null || offre.getMarque().equals(c.getMarque())?" marque ok":" marque pas ok"));
			if (offre.getChocolat().equals(c.getChocolat()) && (offre.getMarque()==null || offre.getMarque().equals(c.getMarque()))) { // type recherche
				if (this.stockChocoMarque.get(c)>=offre.getQuantiteT()) {
					this.journal.ajouter(" "+this.stockChocoMarque.get(c)+" T en stock -> quantite suffisante");
					return new PropositionVenteOA(offre, this, c, 16.0);
				} else {
					this.journal.ajouter(" "+this.stockChocoMarque.get(c)+" T en stock -> quantite insuffisante");
				}
			}
		}
		return null;
	}

	public void notifierAchatOA(PropositionVenteOA propositionRetenue) {
		double nouveauStock = Math.max(0.0, this.stockChocoMarque.get(propositionRetenue.getChocolatDeMarque())-propositionRetenue.getOffre().getQuantiteT());
		this.journal.ajouter(" le stock de "+propositionRetenue.getChocolatDeMarque()+" passe a "+nouveauStock+" suite a la vente "+propositionRetenue);
		this.stockChocoMarque.put(propositionRetenue.getChocolatDeMarque(), nouveauStock);
	}

	public void notifierPropositionNonRetenueOA(PropositionVenteOA propositionRefusee) {
	}

}