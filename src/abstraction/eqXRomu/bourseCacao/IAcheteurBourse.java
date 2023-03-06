package abstraction.eqXRomu.bourseCacao;

import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public interface IAcheteurBourse {
	
	/**
	 * Retourne la quantite en tonnes de feves de type f desiree par l'acheteur 
	 * sachant que le cours actuel de la feve f est cours
	 * @param f le type de feve
	 * @param cours le cours actuel des feves de type f
	 * @return la quantite en tonnes de feves de type f desiree 
	 */
	public double demande(Feve f, double cours);

	/**
	 * Methode appelee par la bourse pour avertir l'acheteur qu'il vient d'acheter
	 * quantiteEnT tonnes de feve f au prix de  coursEnEuroParT euros par tonne.
	 * L'acteur this doit augmenter son stock de feves de type f de la 
	 * quantite quantiteEnT.
	 * Lorsque cette methode est appelee la transaction bancaire a eu lieu (l'acheteur
	 * n'a pas a s'occuper du paiement qui a deja ete effectue)
	 */
	public void notificationAchat(Lot l, double coursEnEuroParT);

	/**
	 * Methode appelee par la bourse pour avertir l'acheteur qu'il vient 
	 * d'etre ajoute a la black list : l'acteur a passe une commande en bourse
	 * qu'il n'a pas pu honorer du fait d'un compte en banque trop faible. 
	 * this ne pourra pas acheter en bourse pendant la duree precisee en 
	 * parametre (toute commande sera ignoree prendant les prochaines duree etapes)
	 */
	public void notificationBlackList(int dureeEnStep);
}
