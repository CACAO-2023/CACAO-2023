package abstraction.eqXRomu.appelsOffres;

import abstraction.eqXRomu.filiere.IActeur;

public interface IAcheteurAO extends IActeur {
	
	/**
	 * @param offre offre!=null
	 * @return Retourne une proposition de prix a la tonne pour l'offre indiquee en parametre
	 * (retourne 0.0 si l'acheteur n'est pas interesse par cette offre).
	 */
	public double proposerPrix(OffreVente offre);
	
	/**
	 * Methode appelee lorsque la proposition de prix de l'acheteur a ete retenue 
	 * par le vendeur. La transaction d'argent a deja ete effectue mais il reste 
	 * a l'acheteur a mettre a jour ses stock pour tenir compte de l'achat qu'il
	 * vient de faire ( propositionRetenue.getOffre() ).
	 * ATTENTION : si le booleen enTG vaut true alors l'acheteur est oblige 
	 *   de vendre ce chocolat en tete de gondole.
	 * @param propositionRetenue la proposition qu'a fait l'acheteur this et qui
	 *  vient d'etre retenue par le vendeur.
	 */
	public void notifierAchatAO(PropositionAchatAO propositionRetenue);

	/**
	 * Methode appelee lorsque la proposition de prix de l'acheteur n'a pas ete retenue.
	 * @param propositionNonRetenue Une proposition de prix faite par l'acheteur this
	 * qui n'a pas ete retenue par le vendeur.
	 */
	public void notifierPropositionNonRetenueAO(PropositionAchatAO propositionNonRetenue);

}
