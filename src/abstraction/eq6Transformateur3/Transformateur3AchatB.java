package abstraction.eq6Transformateur3;

import java.util.List;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
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
		super();
		this.coursmaxBG = new Variable ("cours maximal BG","cours maximal que l'acteur va accepter pour les feves bas de gamme",this,0.0,4000,4000);
		this.coursmaxMG = new Variable ("cours maximal MG","cours maximal que l'acteur va accepter pour les feves moyenne gamme",this,0.0,5000,5000);
		this.coursmaxMGL = new Variable ("cours maximal MGL","cours maximal que l'acteur va accepter pour les feves moyenne gamme labelisees",this,0.0,5500,5500);
		this.coursmaxHGL = new Variable ("cours maximal HGL","cours maximal que l'acteur va accepter pour les feves haut de gamme",this,0.0,6000,6000);
		//Filiere.LA_FILIERE.getActeur("BourseCacao").getCoursFeve().getValeur();
		//peut être remplacer par une constante à modifier qui sera init par le cours actuel.
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
		super.journalAchatB.ajouter("on nous demande si on veut des"+f.getType()+"avec le cours : "+cours);
		double res = 0;
		if (privilegieCC(f,cours)) {
		if (f.getGamme()==Gamme.BQ) {if(cours<=this.getCoursmaxBG().getValeur()) {
													res =(10);}
		}
		else {res = 0.0;}
		if (f.getGamme()==Gamme.MQ && f.isBioEquitable()) {if(cours<=this.getCoursmaxMGL().getValeur()) {
			res = 10;}}
		else {res = 0.0;}
		if (f.getGamme()==Gamme.MQ) {if(cours<=this.getCoursmaxMG().getValeur()) {
			res = (10);}}
		else {res = 0.0;}
		if (f.getGamme()==Gamme.BQ && f.isBioEquitable()) {if(cours<=this.getCoursmaxHGL().getValeur()) {
			res = (10);}}
		else {res =0.0;}

		super.journalAchatB.ajouter("on dit qu'on en veut :"+res);
		return res;}
		else {
			if (f.getGamme()==Gamme.BQ) {
				double quant = Math.min(super.quantBQMax-super.stockFeveBG.getQuantiteTotale()-super.quantiteEnAttente-1000,4000 - super.totalStocksChoco.getValeur());
				res= Math.max(0,quant);
			}
			if (f.getGamme()==Gamme.MQ) {
				double quant = Math.min(super.quantMQMax-super.stockFeveMG.getQuantiteTotale()-super.quantiteEnAttente-1000,40000 -super.totalStocksChoco.getValeur());
				res= Math.max(0,quant);
			}
			if (f.getGamme()==Gamme.MQ && f.isBioEquitable()) {
				double quant = Math.min(super.quantMQLMax-super.stockFeveMGL.getQuantiteTotale()-super.quantiteEnAttente-1000,40000 - super.totalStocksChoco.getValeur());
				res= Math.max(0,quant);
			}
			if (f.getGamme()==Gamme.HQ) {
				double quant = Math.min(super.quantHQMax-super.stockFeveHGL.getQuantiteTotale()-super.quantiteEnAttente-1000,40000 -super.totalStocksChoco.getValeur());
				res= Math.max(0,quant);
			}
		return res;
		}
		}
	
	


	private double max(double d, double e) {
		// TODO Auto-generated method stub
		if (d>e) {return d;}
		else {return e;}
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
		super.ajouterFeve(l);
		super.journalAchatB.ajouter("Stock de "+l.getQuantiteTotale()+""+"tonnes de feves"+((Feve)l.getProduit()).toString()+" acheté en bourse de qualité:"+l.getProduit().toString());
		
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
		super.journalAchatB.ajouter("on est dans le rouge ça va mal finir");
		super.journal.ajouter("on est dans le rouge ça va mal finir");
		
	}
	/** ecrit par Nathan Claeys
	 * il faut ecrire une methode qui regarde le cours du marche de chaque feve et met à jour les valeurs 
	 * de coursmax afin que les achats se passent bien**/
	private void MaJCours () {
		
	}
	/**ecrit par Nathan Claeys
	   * pour pouvoir rendre les variables qui peuvent aider à la prise de decision
	   */
	  public List<Variable> getIndicateurs() {
			List<Variable> res = super.getIndicateurs();
			res.add(coursmaxBG);
			res.add(coursmaxHGL);
			res.add(coursmaxMG);
			res.add(coursmaxMGL);
			return res;}
	
	/**ecrit par Nathan Claeys
	 */
	public void initialiser() {
		super.initialiser();
	}
	public void next() {
		super.next();
		this.MaJCours();
	}
	

}
