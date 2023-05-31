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
		/*if (produit.getType().equals("Chocolat")) {
			if (((((Chocolat)produit).getGamme()== Gamme.MQ)
					||(((Chocolat)produit).getGamme()== Gamme.HQ)&&(((Chocolat)produit).isBioEquitable()))) {
				return true;}
			return false;}*/
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
		/*else if ((((stockChoco.containsKey(produit))&& (produit.getType().equals("Chocolat"))&&((((Chocolat)produit).getGamme()== Gamme.MQ) ||((((Chocolat)produit).getGamme()== Gamme.HQ)&&(((Chocolat)produit).isBioEquitable())))))){
			if (this.stockChoco.get(produit)>100) { 
				//this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nous déclarons pouvoir vendre du " + produit.getType() + " " + produit);
				return true;
			}
			else {
				return false;}}*/
		else {
			return false;}}


	//fait par wiem : prix 
	public double propositionPrix(ExemplaireContratCadre contrat) {
		double prix = 0.0;
		Object produit = contrat.getProduit();
		if ((produit instanceof ChocolatDeMarque)&&(stockChocoMarque.get(produit)!= null) ){ 
			double stock = stockChocoMarque.get(produit);
			if (((ChocolatDeMarque)produit).getMarque().equals("ChocoPop")) {
				prix = (2800+1500)*1.2*stock; }
			else if (((ChocolatDeMarque)produit).getMarque().equals("Maison Doutre")) {
				prix = (2800+11800)*1.2*stock;	
			}
		}
		return prix;
	}

	//fait par yassine : négociations
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		double stock = stockChocoMarque.get(contrat.getProduit());
		double prix = contrat.getPrix();
		if ((contrat.getProduit() == "ChocoPop")&&(contrat.getEcheancier().getQuantiteTotale()>0.35*stock)) {
			prix = (2800+1500)*1.1*stock; }
		else if ((contrat.getProduit() == "Maison Doutre")&&(contrat.getEcheancier().getQuantiteTotale()>0.15)) {
			prix = (2800+11800)*1.1*stock;	
		}
		//double prix = contrat.getPrix();

		//if(prix >= nvprix) {
		//return prix;
		//} 
		//else {
		return prix;
	}



	//fait par yassine : on négocie en fonction des stocks
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getQuantiteTotale()>stockChocoMarque.get(contrat.getProduit())) {
			return null;}
			else { return contrat.getEcheancier(); }
		}
		
		
		

	//double stock = stockChocoMarque.get(contrat.getProduit()) != null ? stockChocoMarque.get(contrat.getProduit()) : stockChoco.get(contrat.getProduit());
	/*if (stockChocoMarque.get(contrat.getProduit()) != null) {
			double stock = stockChocoMarque.get(contrat.getProduit());

			if (contrat.getEcheancier().getQuantiteTotale()<stock) {
				if ((contrat.getProduit() == "ChocoPop")) {
						if(contrat.getEcheancier().getQuantiteTotale()>600){
							double prix = contrat.getPrix();
							contrat.getPrix() = 0.95*prix;}
						else if (contrat.getEcheancier().getStepFin() > 15) {
					return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, (0.35*stock)+(SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER));
				}
				}
			}
				if ((contrat.getProduit() == "Maison Doutre")
						&& (contrat.getEcheancier().getQuantiteTotale()>200)) { 
			double coutTotal = contrat.getEcheancier().getQuantiteTotale() * contrat.getPrix();
			double prixDonne = contrat.getPrix();

			Echeancier echeancierPropose = contrat.getEcheancier();
			Echeancier nouvelEcheancier = new Echeancier(echeancierPropose);
			//this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : j'accepte l'echeancier "+contrat.getEcheancier());
			return nouvelEcheancier;} return null;}}
	 */

	/*if (echeancierPropose.getQuantiteAPartirDe(contrat.getEcheancier().getStepDebut()) > stock.getQuantite((Chocolat.C_MQ)contrat.getProduit())) {
			echeancierPropose.ajouter(Math.max(SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER, stock.getQuantite((Chocolat)contrat.getProduit())));
			return nouvelEcheancier;
		}}*/

	//double coutTotal = contrat.getEcheancier().getQuantiteTotale() * contrat.getPrix();
	//double prixDonne = contrat.getPrix();








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
		} /*else if (produit instanceof Chocolat) {
			if (this.stockChoco.keySet().contains(produit)) {
				stock= this.stockChoco.get(produit);
				livre = Math.min(stock, quantite);
				if (livre==0) {
					this.stockChoco.put((Chocolat)produit, this.stockChoco.get(produit)-livre);
				}
				lot=new Lot((Chocolat)produit);
			}}*/
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : doit livrer "+quantite+" de "+produit+" --> livre "+livre);
		if (livre != 0) {
			lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre);}
		return lot;
	}


	//fait par yassine  : ajout au journal des propositions de contrats cadres
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.MAGENTA, "  CCV : Nouveau CC conclu : PRODUIT ET QT TOTALE = "+ 

				//contrat.getQuantiteLivree()
				contrat.getQuantiteTotale()//.getEcheancier().getQuantiteTotale()
			//	contrat.getQuantiteALivrerAuStep()
		
				+" "+contrat.getProduit()+", ACHETEUR = "+contrat.getAcheteur()+ ", PRIX = "+contrat.getPrix());
		if (contrat.getProduit() == "ChocoPop") {
			this.ContratsVendeurMQ.add(contrat); 
		}
		else if (contrat.getProduit() == "Maison Doutre") {
			this.ContratsVendeurHQBE.add(contrat);
		}

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


		//this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Tentative de négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit);

		Double stock = stockChocoMarque.get(produit);
		if ((stock != null)&&(stock>100)) {
			double A = 0;
			if (produit.getNom() == "Maison Doutre") {
				A = 0.1; }
			else if (produit.getNom() == "ChocoPop") {
				A = 0.3; }
			ExemplaireContratCadre cc = superviseurVentesCC.demandeVendeur(acheteur, this, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, A*stock+SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER), cryptogramme,false);

			if (cc != null) {   
				this.journalVentes.ajouter(COLOR_LLGRAY, Color.GREEN, "Contrat cadre passé avec " + acheteur.getNom() + " pour " + produit + "CC : " + cc);
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
