package abstraction.eq5Transformateur2; ///code écrit par WIEM LABBAOUI

import java.awt.Color;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Transformateur2AcheteurCC extends Transformateur2Transfo implements IAcheteurContratCadre {

	public static Color COLOR_LLGRAY = new Color(238,238,238);
	protected SuperviseurVentesContratCadre superviseurVentesCC;
	protected LinkedList<ExemplaireContratCadre> ContratsAcheteurMQ;
	protected LinkedList<ExemplaireContratCadre> ContratsAcheteurHQBE;


	public void initialiser() {
		super.initialiser();
		this.superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
		this.ContratsAcheteurMQ = new LinkedList<>();
		this.ContratsAcheteurHQBE = new LinkedList<>();
	}

	/**
	 * @author LABBAOUI Wiem
	 * On achète des fèves MQ et HQ_BE
	 */

	public boolean achete(IProduit produit) {
		boolean somme = false;
		if ((produit.getType().equals("Feve")
				&& ((((Feve)produit).getGamme()== Gamme.MQ)&&(!((Feve)produit).isBioEquitable())
						|| ((((Feve)produit).getGamme()== Gamme.HQ)&&(((Feve)produit).isBioEquitable()))))) {
			somme = true;} 
		return somme ; 


	}


	/**
	 * @author LABBAOUI Wiem
	 * Pour la marque Maison Doutre : RSE de 10%
	 * Pour la marque ChocoPop : pas de RSE
	 */

	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		if ((((Feve)produit).getGamme()== Gamme.HQ)&&(((Feve)produit).isBioEquitable())) {
			return 10; } 
		else { 
			return 0; }
	}


	/**
	 * @author MISBAH Yassine
	 * On refuse l'échéancier si on ne peut pas livrer la quantité demandée
	 */

	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (contrat.getQuantiteTotale()>stockFeves.get(contrat.getProduit())) {

			return null;}
		else { return contrat.getEcheancier(); }
	}



	/**
	 * @author LABBAOUI Wiem
	 * Si l'acheteur propose d'acheter plus de 600T de ChocoPop,
	 * on augmente le prix et on achète à 5% plus cher
	 * Sinon, augmentation de 3%
	 * Si l'acheteur propose d'acheter plus de 200T de Maison Doutre,
	 * on augmente le prix et on achète avec 7% plus cher
	 * Sinon, augmentation de 3%
	 */

	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		double prix = contrat.getPrix();
		IProduit produit= (IProduit)contrat.getProduit();
		if ((((Feve)produit).getGamme() == Gamme.MQ)) {
			if ((contrat.getEcheancier().getQuantiteTotale()>600)) {
				prix = (2800+1500)*1.25;}
			else { prix = (2800+1500)*1.23;} }

		else if ((((Feve)produit).getGamme() == Gamme.HQ)&& (((Feve)produit).isBioEquitable())) {
			if ((contrat.getEcheancier().getQuantiteTotale()>200)) {
				prix = (2800+11800)*1.27;}
			else {prix = (2800+11800)*1.23;}}

		return prix;}


	/**
	 * @author MISBAH Yassine
	 * On stock dans deux listes différentes les CCA pour nos deux marques
	 */

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method
		IProduit produit= (IProduit)contrat.getProduit();
		this.journalAchats.ajouter(COLOR_LLGRAY, Color.MAGENTA, " CCA : Nouveau CC conclu : PRODUIT ET QT TOTALE = "+
				+ contrat.getQuantiteTotale()+" " +contrat.getProduit()+
				", VENDEUR = "+contrat.getVendeur()+ ", PRIX = "+contrat.getPrix());
		if (((Feve)produit).getGamme() == Gamme.MQ){
			ContratsAcheteurMQ.add(contrat);
		}
		else if ((((Feve)produit).getGamme() == Gamme.HQ)&& (((Feve)produit).isBioEquitable())) {
			ContratsAcheteurHQBE.add(contrat);}

	}

	/**
	 * @author MISBAH Yassine
	 * Reception des fèves, mise à jour des stocks
	 */

	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		IProduit produit= lot.getProduit();
		double quantite = lot.getQuantiteTotale();
		if (contrat == null) {

		}
		else if (produit instanceof Feve) {
			if (this.stockFeves.keySet().contains(produit)) {
				this.stockFeves.put((Feve)produit, this.stockFeves.get(produit)+quantite);
			} else {
				this.stockFeves.put((Feve)produit, quantite);
			}
			this.totalStocksFeves.ajouter(this, quantite, this.cryptogramme);
			this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : reception "+quantite+" T de feves "+produit+". Stock de " + produit + "->  "+this.stockFeves.get(produit));
		}
	}


	/**
	 * @author LABBAOUI Wiem
	 * On cherche dans un premier temps un vendeur
	 * Puis on établit le contrat
	 * On la quantité 100+A selon la gamme de la feve
	 */

	public ExemplaireContratCadre getContrat(Feve produit) {
		double somme = (this.stockFeves.get(Feve.F_MQ)+this.stockFeves.get(Feve.F_HQ_BE));
		if ((produit instanceof Feve) && (somme < 2000.0)) {

			this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Recherche vendeur pour " + produit);
			List<IVendeurContratCadre> vendeurs = superviseurVentesCC.getVendeurs(produit);
			// code ajoute par romu pour pallier a l'erreur juste apres d'acces a l'element 0 dans une liste vide
			if (vendeurs.size()==0) {
				return null;
			}
			// fin de code ajoute par romu 
			IVendeurContratCadre vendeur = vendeurs.get((int)(Math.random() * vendeurs.size())); //on cherche un vendeur
			double A = 0;
			if (produit.getGamme() == Gamme.MQ) {
				A = 500; }
			else if ((produit.getGamme() == Gamme.HQ)&&(produit.isBioEquitable())){
				A = 100; }
			ExemplaireContratCadre cc = superviseurVentesCC.demandeAcheteur(this, vendeur, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10,(SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER+A)/10), cryptogramme,false);
			if (cc != null) {   
				this.journalAchats.ajouter(COLOR_LLGRAY, Color.GREEN, "NOUVEAU CCA : VENDEUR = " + vendeur.getNom() + ", PRODUIT = " + produit + ", PRIX = "+cc.getPrix()); } 
			else {
				this.journalAchats.ajouter(COLOR_LLGRAY, Color.RED, "Echec de la négociation de contrat cadre avec " + vendeur.getNom() + " pour " + produit);
			}
			return cc;} //on établit le contrat
		else {this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Stock trop elevé ! Pas d'achat en CC.");
		return null ;}
	}

	public void next() {
		super.next();
		this.getContrat(Feve.F_MQ);
		this.getContrat(Feve.F_HQ_BE);


	}
}
