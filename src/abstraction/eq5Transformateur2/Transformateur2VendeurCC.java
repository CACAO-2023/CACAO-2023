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

	//fait par yassine
	public void initialiser() {
		super.initialiser();
		this.superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
		this.ContratsVendeurHQBE = new LinkedList<>();
		this.ContratsVendeurMQ = new LinkedList<>();

	}


	//fait par wiem : nous vendons du chocolat sans et avec marque
	public boolean peutVendre(IProduit produit) {
		if ((produit.getType().equals("ChocolatDeMarque"))) {
			if (((ChocolatDeMarque)produit).getNom()=="ChocoPop" ||(((ChocolatDeMarque)produit).getNom()== "MaisonDoutre")) {
				return true;}
			return false;}
		return false; }



	//fait par wiem : nous vendons du chocolat de moyenne gamme et haute gamme bioéquitable. La vente est possible ssi le stock est supérieur à 100T
	public boolean vend(IProduit produit) {
		if ((stockChocoMarque.containsKey(produit))&&(produit.getType().equals("ChocolatDeMarque"))&&((((ChocolatDeMarque)produit).getGamme()== Gamme.MQ) ||((((ChocolatDeMarque)produit).getGamme()== Gamme.HQ)&&(((ChocolatDeMarque)produit).isBioEquitable())))){
			if (this.stockChocoMarque.get(produit)>100) { 
				//this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nous déclarons pouvoir vendre du " + produit.getType() + " " + produit);
				return true;}
			else {
				return false;}}
		else {
			return false;}}


	//fait par wiem : prix 
	public double propositionPrix(ExemplaireContratCadre contrat) {
		double prix = 0.0;
		IProduit produit = (IProduit)contrat.getProduit();
		if ((produit instanceof ChocolatDeMarque)&&(stockChocoMarque.get(produit)!= null) ){ 
			if (((ChocolatDeMarque)produit).getMarque().equals("ChocoPop")) {
				prix = (2800+1500)*1.2; }
			else if (((ChocolatDeMarque)produit).getMarque().equals("Maison Doutre")) {
				prix = (2800+11800)*1.2;	
			}
		}
		return prix;
	}

	//fait par yassine : négociations
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
				prix = (2800+11800)*1.1; }
			else { prix = (2800+1500)*1.15;}
		}
		return prix;}

	//s


	//fait par yassine : on négocie en fonction des stocks
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getQuantiteTotale()>stockChocoMarque.get(contrat.getProduit())) {
			return null;}
		else { return contrat.getEcheancier(); }
	}

	//fait par yassine : renvoie la quantité livrée, met à jour les stocks.
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


	//fait par yassine  : ajout au journal des propositions de contrats cadres
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


	//fait par wiem  : on cherche un acheteur potentiel et on établit un contrat avec 
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
		System.out.println("stocks : "+stockChocoMarque);
		System.out.println("stock produit "+produit+ " -> "+stock);
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





	//fait par wiem 
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
