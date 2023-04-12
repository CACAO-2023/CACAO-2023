package abstraction.eq6Transformateur3;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.Lot;

public class Transformateur3AchatB extends Transformateur3AchatCC implements IAcheteurBourse{
/**ecrit par Nathan Claeys
 */
	protected Variable coursmaxBG;
	protected Variable coursmaxMG;
	protected Variable coursmaxMGL;
	protected Variable coursmaxHGL;
	
	public Transformateur3AchatB () {
		this.coursmaxBG = new Variable ("cours maximal BG","cours maximal que l'acteur va accepter pour les feves bas de gamme",this,0.0,100,1);
		this.coursmaxMG = new Variable ("cours maximal MG","cours maximal que l'acteur va accepter pour les feves moyenne gamme",this,0.0,100,1);
		this.coursmaxMGL = new Variable ("cours maximal MGL","cours maximal que l'acteur va accepter pour les feves moyenne gamme labelisees",this,0.0,100,1);
		this.coursmaxHGL = new Variable ("cours maximal HGL","cours maximal que l'acteur va accepter pour les feves haut de gamme",this,0.0,100,1);
	}
	/**
	 * ecrit par Nathan Claeys
	 * Retourne la quantite en tonnes de feves de type f desiree par l'acheteur 
	 * sachant que le cours actuel de la feve f est cours
	 * @param f le type de feve
	 * @param cours le cours actuel des feves de type f
	 * @return la quantite en tonnes de feves de type f desiree 
	 * 
	 */
	public double demande(Feve f, double cours) {
		if (f.getGamme().compareTo(Gamme.BQ)==0) {if(cours<=this.getCoursmaxBG().getValeur()) {
													return (super.BesoinStep(Filiere.LA_FILIERE.getEtape()+1,f)-super.getArrivageCCStep(Filiere.LA_FILIERE.getEtape()+1,f));}
		}else {
		if (f.getGamme().compareTo(Gamme.MQ)==0 && f.isBioEquitable()) {if(cours<=this.getCoursmaxMGL().getValeur()) {
			return (super.BesoinStep(Filiere.LA_FILIERE.getEtape()+1,f)-super.getArrivageCCStep(Filiere.LA_FILIERE.getEtape()+1,f));}}
			else {
		if (f.getGamme().compareTo(Gamme.MQ)==0) {if(cours<=this.getCoursmaxMG().getValeur()) {
			return (super.BesoinStep(Filiere.LA_FILIERE.getEtape()+1,f)-super.getArrivageCCStep(Filiere.LA_FILIERE.getEtape()+1,f));}}
		else {
		if (f.getGamme().compareTo(Gamme.BQ)==0 && f.isBioEquitable()) {if(cours<=this.getCoursmaxHGL().getValeur()) {
			return (super.BesoinStep(Filiere.LA_FILIERE.getEtape()+1,f)-super.getArrivageCCStep(Filiere.LA_FILIERE.getEtape()+1,f));}}
		else {return 0;};};};};
		return 0;
	}

	/**
	 * @return the coursmaxBG
	 */
	public Variable getCoursmaxBG() {
		return coursmaxBG;
	}
	/**
	 * @return the coursmaxMG
	 */
	public Variable getCoursmaxMG() {
		return coursmaxMG;
	}
	/**
	 * @return the coursmaxMGL
	 */
	public Variable getCoursmaxMGL() {
		return coursmaxMGL;
	}
	/**
	 * @return the coursmaxHGL
	 */
	public Variable getCoursmaxHGL() {
		return coursmaxHGL;
	}
	/**
	 * ecrit par Nathan Claeys
	 * Methode appelee par la bourse pour avertir l'acheteur qu'il vient d'acheter
	 * quantiteEnT tonnes de feve f au prix de  coursEnEuroParT euros par tonne.
	 * L'acteur this doit augmenter son stock de feves de type f de la 
	 * quantite quantiteEnT.
	 * Lorsque cette methode est appelee la transaction bancaire a eu lieu (l'acheteur
	 * n'a pas a s'occuper du paiement qui a deja ete effectue)
	 */
	public void notificationAchat(Lot l, double coursEnEuroParT) {
		super.ajouterFeve((Feve)l.getProduit(), l.getQuantites().get(Filiere.LA_FILIERE.getEtape()),Filiere.LA_FILIERE.getEtape());
		super.journal.ajouter("Stock de "+l.getQuantiteTotale()+""+"tonnes de feves"+((Feve)l.getProduit()).toString()+" acheté en bourse");
		
	}

	/**
	 * ecrit par Nathan Claeys
	 * Methode appelee par la bourse pour avertir l'acheteur qu'il vient 
	 * d'etre ajoute a la black list : l'acteur a passe une commande en bourse
	 * qu'il n'a pas pu honorer du fait d'un compte en banque trop faible. 
	 * this ne pourra pas acheter en bourse pendant la duree precisee en 
	 * parametre (toute commande sera ignoree prendant les prochaines duree etapes)
	 */
	public void notificationBlackList(int dureeEnStep) {
		// TODO Auto-generated method stub
		
	}
	/** ecrit par Nathan Claeys
	 * il faut ecrire une methode qui regarde le cours du marche de chaque feve et met à jour les valeurs 
	 * de coursmax afin que les achats se passent bien**/
	private void MaJCours () {
		
	}
	
	/**ecrit par Nathan Claeys
	 */
	public void next() {
		super.next();
		this.MaJCours();
	}
	

}
