package abstraction.eqXRomu.offresAchat;

import abstraction.eqXRomu.filiere.IActeur;

public interface IVendeurOA extends IActeur {

	/**
	 * 
	 * @param offre
	 * @return Retourne une proposition de vente dont le chocolat de marque correspond au chocolat de offre,
	 *         vendeur correspond a this, et le prix est determine par le IVendeurOA this
	 *         (retourne null si le IVendeurOA ne peut pas ou ne veut pas vendre un tel lot)
	 */
	public PropositionVenteOA proposerVente(OffreAchat offre);
	
	/**
	 * Methode appelee lorsque la proposition de prix du vendeur a ete retenue 
	 * par l'acheteur. La transaction d'argent a deja ete effectuee mais il reste 
	 * au vendeur a mettre a jour ses stock pour tenir compte de l'achat qu'il
	 * vient de faire ( propositionRetenue.getOffre() ).
	 * @param propositionRetenue la proposition qu'a fait l'acheteur this et qui
	 *  vient d'etre retenue par le vendeur.
	 */
	public void notifierAchatOA(PropositionVenteOA propositionRetenue);

	/**
	 * Methode appelee pour avertir le vendeur que sa proposition de vente n'a pas ete retenue 
	 * @param propositionRefusee, la proposition qui avait ete faite mais qui n'a pas ete retenue
	 */
	public void notifierPropositionNonRetenueOA(PropositionVenteOA propositionRefusee);
}
