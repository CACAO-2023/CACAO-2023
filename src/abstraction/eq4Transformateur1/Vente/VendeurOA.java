package abstraction.eq4Transformateur1.Vente;

import java.awt.Color;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.offresAchat.IVendeurOA;
import abstraction.eqXRomu.offresAchat.OffreAchat;
import abstraction.eqXRomu.offresAchat.PropositionVenteOA;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

/**
 * @author Fouad 
 *
 */

public class VendeurOA extends CC_distributeur implements IVendeurOA {
	
	public PropositionVenteOA proposerVente(OffreAchat offre) {
		this.journal_appel.ajouter("proposerVente(offre = "+offre+")");
		for (ChocolatDeMarque c: Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (c.getMarque().equals("Vccotioi")) {
				this.journal_appel.ajouter("      "+c+" "+(offre.getChocolat().equals(c.getChocolat())?"convient":"ne convient pas")+" "+(offre.getMarque()==null || offre.getMarque().equals(c.getMarque())?" marque ok":" marque pas ok"));
				if (offre.getChocolat().equals(c.getChocolat()) && (offre.getMarque()==null || offre.getMarque().equals(c.getMarque()))) { // type recherche
					if (this.stockChocoMarque.get(c)>=offre.getQuantiteT()) {
						this.journal_appel.ajouter(" "+this.stockChocoMarque.get(c)+" T en stock -> quantite suffisante");
						return new PropositionVenteOA(offre, this, c, 200);
					} else {
						this.journal_appel.ajouter(" "+this.stockChocoMarque.get(c)+" T en stock -> quantite insuffisante");
				}
			}
		}
		}
		return null;
	}

	public void notifierAchatOA(PropositionVenteOA propositionRetenue) {
		double nouveauStock = Math.max(0.0, this.stockChocoMarque.get(propositionRetenue.getChocolatDeMarque())-propositionRetenue.getOffre().getQuantiteT());
		this.journal_appel.ajouter(" le stock de "+propositionRetenue.getChocolatDeMarque()+" passe a "+nouveauStock+" suite a la vente "+propositionRetenue);
		this.stockChocoMarque.put(propositionRetenue.getChocolatDeMarque(), nouveauStock);
	}

	public void notifierPropositionNonRetenueOA(PropositionVenteOA propositionRefusee) {
		this.journal_appel.ajouter(COLOR_LLGRAY, Color.BLUE, "  Proposition Refusée : "+propositionRefusee);
	}

}
