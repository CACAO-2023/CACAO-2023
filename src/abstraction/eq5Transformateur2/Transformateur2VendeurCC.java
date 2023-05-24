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
	//protected LinkedList<ExemplaireContratCadre> contrats;
	//protected HashMap<ExemplaireContratCadre, String> ContratsVendeur;

	//fait par yassine
	public void initialiser() {
		super.initialiser();
		this.superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
		//this.stockChoco.put(Chocolat.C_MQ,2000.);
	}


	/*public Transformateur2VendeurCC() {
		super();  
		this.ContratsVendeur =new HashMap<>();
	}*/

	//fait par wiem : nous vendons du chocolat sans et avec marque
	public boolean peutVendre(IProduit produit) {
		if (produit.getType().equals("Chocolat")) {
			if (((((Chocolat)produit).getGamme()== Gamme.MQ)
					||(((Chocolat)produit).getGamme()== Gamme.HQ)&&(((Chocolat)produit).isBioEquitable()))) {
				return true;}
			return false;}
		else if ((produit.getType().equals("ChocolatDeMarque"))) {
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
		else if ((((stockChoco.containsKey(produit))&& (produit.getType().equals("Chocolat"))&&((((Chocolat)produit).getGamme()== Gamme.MQ) ||((((Chocolat)produit).getGamme()== Gamme.HQ)&&(((Chocolat)produit).isBioEquitable())))))){
			if (this.stockChoco.get(produit)>100) { 
				//this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nous déclarons pouvoir vendre du " + produit.getType() + " " + produit);
				return true;
			}
			else {
				return false;}}
		else {
			return false;}}


	//fait par yassine : pas de négociations
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : j'accepte l'echeancier "+contrat.getEcheancier());
		return contrat.getEcheancier(); } 


	//fait par wiem
	public double propositionPrix(ExemplaireContratCadre contrat) {
		double prix = 0;
		Chocolat cp=null ;
		if ((contrat.getProduit() instanceof Chocolat)) {
			cp=(Chocolat)contrat.getProduit();
		} else {
			cp = ((ChocolatDeMarque)contrat.getProduit()).getChocolat();
		}
		if (cp == Chocolat.C_MQ ) {
			Double stock = stockChoco.get(cp);
			if (stock!=null) {
				//stock*(cout de stockage 1300 + prix de transfo 1500 + prix moyen d'une tonne de feves MQ) + marge de 10%
				prix = (2800*1500)*1.1*stock ;
				this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "stock = "+stock+ "prix ="+prix);
			}}
		if ( cp == Chocolat.C_HQ_BE) {
			Double stock = stockChocoMarque.get(cp);
			if (stock!=null) {
				//stock*(cout de stockage 1300 + prix de transfo 1500 + prix moyen d'une tonne de feves HQ_BE) + marge de 10%
				prix = (2800+11800)*1.1*stock;
				this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "stock = "+stock+ "prix ="+prix);}}
		return prix; 
		}

	


	//fait par yassine : pas de négociations
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		return contrat.getPrix(); 
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
		} else if (produit instanceof Chocolat) {
			if (this.stockChoco.keySet().contains(produit)) {
				stock= this.stockChoco.get(produit);
				livre = Math.min(stock, quantite);
				if (livre==0) {
					this.stockChoco.put((Chocolat)produit, this.stockChoco.get(produit)-livre);
				}
				lot=new Lot((Chocolat)produit);
			}}
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : doit livrer "+quantite+" de "+produit+" --> livre "+livre);
		if (livre != 0) {
			lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre);}
		return lot;
	}


	//fait par yassine  : ajout au journal des propositions de contrats cadres
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nouveau cc conclu "+contrat);
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

		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Tentative de négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit);

		//if ((produit.getNom() == "MaisonDoutre") || (produit.getNom() == "ChocoPop")) {
			Double stock = stockChocoMarque.get(produit);
			ExemplaireContratCadre cc = superviseurVentesCC.demandeVendeur(acheteur, this, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, 0.1*stock), cryptogramme,false);

		 if (cc != null) {   
				this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Contrat cadre passé avec " + acheteur.getNom() + " pour " + produit + "CC : " + cc);
				//this.ContratsVendeur.put(cc,  acheteur.getNom());
			} else {
				this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Echec de la négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit);
			}
			return cc; }
		

		//fait par wiem 
		public void next() {
			super.next();
			for (ChocolatDeMarque c: Filiere.LA_FILIERE.getChocolatsProduits()) {
				if (c.getMarque().equals("MaisonDoutre")) {
					this.getContrat(c);
					
				}
				if (c.getMarque().equals("ChocoPop")) {
					this.getContrat(c);
					
				}
			

		}}}
