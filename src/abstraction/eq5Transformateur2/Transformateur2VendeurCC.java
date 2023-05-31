package abstraction.eq5Transformateur2;//Fait par Yassine et Wiem

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;


public class Transformateur2VendeurCC extends Transformateur2AcheteurCC implements IVendeurContratCadre {
	public static Color COLOR_LLGRAY = new Color(238,238,238);
	protected SuperviseurVentesContratCadre superviseurVentesCC;
	protected LinkedList<ExemplaireContratCadre> ContratsVendeurHQBE;
	protected LinkedList<ExemplaireContratCadre> ContratsVendeurMQ;


	public void initialiser() {
		super.initialiser();
		this.superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
		this.ContratsVendeurHQBE = new LinkedList<>();
		this.ContratsVendeurMQ = new LinkedList<>();

	}

	/**
	 * @author LABBAOUI Wiem
	 : On ne peut vend que du chocolat de marque MQ "ChocoPop" et HQ_BE "Maison Doutre"
	 */

	public boolean peutVendre(IProduit produit) {
		if ((produit.getType().equals("ChocolatDeMarque"))) {
			if (((ChocolatDeMarque)produit).getNom()=="ChocoPop" ||(((ChocolatDeMarque)produit).getNom()== "Maison Doutre")) {
				return true;}
			return false;}
		return false; }



	/**
	 * @author LABBAOUI Wiem
	 : On vend ssi le stock est supérieur à 100 T
	 */

	public boolean vend(IProduit produit) {
		if ((stockChocoMarque.containsKey(produit))&&(produit.getType().equals("ChocolatDeMarque"))&&((((ChocolatDeMarque)produit).getGamme()== Gamme.MQ) ||((((ChocolatDeMarque)produit).getGamme()== Gamme.HQ)&&(((ChocolatDeMarque)produit).isBioEquitable())))){
			if (this.stockChocoMarque.get(produit)>100) { 
				return true;}
			else {
				return false;}}
		else {
			return false;}}



	/**
	 * @author LABBAOUI Wiem
	 : On vend nos chocolats avec 20% de marge
	   2800 = prix de transfo et coût de stockage
	   1500 = prix d'achat d'une tonne de F_MQ
	   11800 = prix d'achat d'une tonne de F_HQ_BE
	 */ 

	public double propositionPrix(ExemplaireContratCadre contrat) {
		double prix = 0.0;
		IProduit produit = (IProduit)contrat.getProduit();
		if ((produit instanceof ChocolatDeMarque)&&(stockChocoMarque.get(produit)!= null) ){ 
			if (((ChocolatDeMarque)produit).getMarque().equals("ChocoPop")) {
				prix = (2800+1500)*1.2; }
			else if (((ChocolatDeMarque)produit).getMarque().equals("Maison Doutre")) {
				prix = (2800+11800)*1.3;	
			}
		}
		return prix;
	}


	/**
	 * @author LABBAOUI Wiem
	 * Si l'acheteur propose d'acheter plus de 35% de notre stock pour ChocoPop,
	 * on baisse le prix et on achète avec 10% de marge
	 * Sinon, marge de 15%
	 * Si l'acheteur propose d'acheter plus de 15% de notre stock pour Maison Doutre,
	 * on baisse le prix et on achète avec 20% de marge
	 * Sinon, marge de 25%
	 */

	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		double stock = stockChocoMarque.get(contrat.getProduit());
		IProduit produit = (IProduit)contrat.getProduit();
		double prix = contrat.getPrix();
		if (((ChocolatDeMarque)produit).getMarque().equals("ChocoPop")) {
			if(contrat.getEcheancier().getQuantiteTotale()>0.35*stock) {
				prix = (2800+1500)*1.1;}
			else { prix  = (2800+1500)*1.15; }}
		else if (((ChocolatDeMarque)produit).getMarque().equals("Maison Doutre")) { 
			if (contrat.getEcheancier().getQuantiteTotale()>0.15*stock) {
				prix = (2800+11800)*1.2; }
			else { prix = (2800+1500)*1.25;}
		}
		return prix;}

	//s



	/**
	 * @author MISBAH Yassine
	 * On refuse l'échéancier si on ne peut pas livrer la quantité demandée
	 */

	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getQuantiteTotale()>stockChocoMarque.get(contrat.getProduit())) {
			return null;}
		else { return contrat.getEcheancier(); }
	}


	/**
	 * @author MISBAH Yassine 
	 * On livre tout notre stock si la quantité demandée est trop importante, mais 
	 * ne devrait pas arriver à priori car l'échéancier vaudrait null
	 */
	public Lot livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		double stock=0.0;
		double livre=0.0;
		Lot lot = null;
		if (produit instanceof ChocolatDeMarque) {
			if (this.stockChocoMarque.keySet().contains(produit)) {
				stock= this.stockChocoMarque.get(produit);
				livre = Math.min(stock, quantite);
				if (livre==0) {
					this.stockChocoMarque.put((ChocolatDeMarque)produit, this.stockChocoMarque.get(produit)-livre);
				}
				lot=new Lot((ChocolatDeMarque)produit);
			}
		} 
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : doit livrer "+quantite+" de "+produit+" --> livre "+livre);
		if (livre != 0) {
			lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre);}
		return lot;
	}



	/**
	 * @author MISBAH Yassine
	 * On stock dans deux listes différentes les CCV pour nos deux marques
	 */
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		IProduit produit= (IProduit)contrat.getProduit();
		if (produit instanceof ChocolatDeMarque) {
			this.journalVentes.ajouter(COLOR_LLGRAY, Color.MAGENTA, "Nouveau CCV conclu : PRODUIT ET QT TOTALE = "+contrat.getQuantiteTotale() +" "+contrat.getProduit()+", ACHETEUR = "+contrat.getAcheteur()+ ", PRIX = "+contrat.getPrix());
			if (((ChocolatDeMarque)produit).getMarque() == "ChocoPop") {
				this.ContratsVendeurMQ.add(contrat); 
			}
			else if (((ChocolatDeMarque)produit).getMarque() ==  "Maison Doutre") {
				this.ContratsVendeurHQBE.add(contrat);
			}}

	}



	/**
	 * @author LABBAOUI Wiem
	 * On cherche dans un premier temps un acheteur
	 * Puis on établit le contrat
	 * On vend 100 + 10% du stock pour la marque Maison Doutre
	 * et 100 + 30% du stock pour la marque ChocoPop
	 */

	public ExemplaireContratCadre getContrat(ChocolatDeMarque produit) {
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Recherche acheteur pour " + produit);
		List<IAcheteurContratCadre> acheteurs = superviseurVentesCC.getAcheteurs(produit);
		// CODE AJOUTE PAR ROMU POUR EVITER ERREURS
		if (acheteurs.size()<1) {
			return null;
		}
		// FIN DE CODE AJOUTE PAR ROMU
		IAcheteurContratCadre acheteur = acheteurs.get((int)(Math.random() * acheteurs.size())); 
		Double stock = stockChocoMarque.get(produit);
		if ((stock != null)&&(stock>100)) {
			double A = 0;
			if (produit.getMarque() == "Maison Doutre") {
				A = 0.1; }
			else if (produit.getMarque() == "ChocoPop") {
				A = 0.3; }
			ExemplaireContratCadre cc = superviseurVentesCC.demandeVendeur(acheteur, this, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10,(A*stock+SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER)/10), cryptogramme,false);

			if (cc != null) {   
				this.journalVentes.ajouter(COLOR_LLGRAY, Color.GREEN, "NOUVEAU CCV : ACHETEUR = " + acheteur.getNom() + ", PRODUIT = " + produit + ", PRIX = "+cc.getPrix());
			} else {
				this.journalVentes.ajouter(COLOR_LLGRAY, Color.RED, "Echec de la négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit);
			}
			return cc;}
		return null;}




	/**
	 * @author LABBAOUI Wiem
	 */

	public void next() {
		super.next();
		for (ChocolatDeMarque c: Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (c.getMarque().equals("Maison Doutre")) {
				this.getContrat(c);

			}
			if (c.getMarque().equals("ChocoPop")) {
				this.getContrat(c);

			}


		}}}
