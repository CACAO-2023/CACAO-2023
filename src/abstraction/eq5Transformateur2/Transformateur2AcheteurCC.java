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
/*
- dans acheteurCC vous declarez une liste de contrats protected LinkedList<ExemplaireContratCadre> contrats; mais qui n'est jamais créée/initialisée/remplie/utilisée
- dans vendeurCC idem, vous declarez la meme liste de contrats, avec le meme nom --> elle masque la liste de sa fille. Mais bon, ce n'est pas visible comme problème vu qu'elle n'est pas plus créée/.../utilisée que celle de acheteurCC
-     public boolean peutVendre(IProduit produit) {
        return ((produit.getType().equals("Chocolat"))||(produit.getType().equals("ChocolatDeMarque")));} 
---> Vous declarez pouvoir vendre n'importe quel chocolat / chocolatDeMarque, alors que c'est faux
- dans vend vous ajoutez a votre journal this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nous vendons du " + produit.getType() + " " + produit);   C'est un message qui induit en erreur car on croit que vous vendez alors qu'à ce stade vous ne faites que déclarer que vous etes en mesure de vendre.  --> plutot mentionner "nous declarons pouvoir vendre du ...*/

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
	@Override
	public boolean achete(IProduit produit) {
		// TODO Auto-generated method stub
		boolean somme = false;
		if ((produit.getType().equals("Feve")
				&& ((((Feve)produit).getGamme()== Gamme.MQ)&&(!((Feve)produit).isBioEquitable())
						|| ((((Feve)produit).getGamme()== Gamme.HQ)&&(((Feve)produit).isBioEquitable()))))) {
			//this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'affirme vouloir acheter le produit "+produit);
			somme = true;} //on achète seulement les fèves haute gamme bio équitable et les fèves moyenne gamme
		return somme ; 


	}

	@Override
	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		if ((((Feve)produit).getGamme()== Gamme.HQ)&&(((Feve)produit).isBioEquitable())) {
			return 10; } //1O% de RSE pour la marque "Maison Doutre"
		else { 
			return 0; }
	}// 0% pour la marque "ChocoPop"


	@Override
	//Mathis DOUTRE
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
				if (contrat.getQuantiteTotale()>stockFeves.get(contrat.getProduit())) {
				return null;}
				else { return contrat.getEcheancier(); }
			}
	
		/*double somme = (this.stockFeves.get(Feve.F_MQ)+this.stockFeves.get(Feve.F_HQ_BE));
		if (somme < 2000.0) {
			double prixMax = 0;// Prix maximum acceptable
			if (contrat.getProduit() == Feve.F_MQ ) {
				prixMax = 2000 ; 
			}
			else if (contrat.getProduit() == Feve.F_HQ_BE) {
				prixMax = 12300 ;
			}
			double soldeDisponible = super.getSolde(); 
			Echeancier echeancierPropose = contrat.getEcheancier();
			Echeancier nouvelEcheancier = new Echeancier(echeancierPropose);

			if (contrat.getPrix() > prixMax) {
				// Si le prix proposé est supérieur au prix maximum, annuler les négociations
				nouvelEcheancier.vider();
				return nouvelEcheancier;
			}

			double coutTotal = contrat.getEcheancier().getQuantiteTotale() * contrat.getPrix();

			if (coutTotal > soldeDisponible) {
				// Si le coût total est supérieur au solde disponible, ajuster les quantités de l'échéancier
				double facteurAjustement = soldeDisponible / coutTotal;
				for (int step = nouvelEcheancier.getStepDebut(); step <= nouvelEcheancier.getStepFin(); step++) {
					double quantiteProposee = nouvelEcheancier.getQuantite(step);
					double nouvelleQuantite = quantiteProposee * facteurAjustement;
					nouvelEcheancier.set(step, nouvelleQuantite);
				}
			} else {
				// Si le coût total est inférieur ou égal au solde disponible, ajuster les quantités de l'échéancier en les multipliant par un facteur
				for (int step = nouvelEcheancier.getStepDebut(); step <= nouvelEcheancier.getStepFin(); step++) {
					double quantiteProposee = nouvelEcheancier.getQuantite(step);
					double nouvelleQuantite = quantiteProposee * 0.9; // Multiplier par un facteur 0.9
					nouvelEcheancier.set(step, nouvelleQuantite);
				}
			}

			return nouvelEcheancier.getQuantiteTotale()>100.0 ? nouvelEcheancier : null;}
		else { return null; }} */



	//Par Mathis DOUTRE

	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		double somme = (this.stockFeves.get(Feve.F_MQ)+this.stockFeves.get(Feve.F_HQ_BE));
		//double stock = stockFeves.get(contrat.getProduit());
		double prix = contrat.getPrix();
		if (somme < 2000.0) {
		
		if ((contrat.getProduit() == Gamme.MQ)&&(contrat.getEcheancier().getQuantiteTotale()>600)) {
			prix = (2800+1500)*1.1*this.stockFeves.get(contrat.getProduit()); }
		else if ((contrat.getProduit() == "Maison Doutre")&&(contrat.getEcheancier().getQuantiteTotale()>200)) {
			prix = (2800+11800)*1.1*this.stockFeves.get(contrat.getProduit());
		}
		//double prix = contrat.getPrix();

		//if(prix >= nvprix) {
			//return prix;
		//} 
		//else {
			return prix;}
		return prix;}
		/*double dernierPrix = contrat.getPrix();
		double soldeDisponible = super.getSolde(); 
		double proposition = 0;


		// Si c'est la première offre, propose un prix inférieur de 15%
		if (contrat.getListePrix().size() == 1) {
			proposition = dernierPrix * 0.85;
		} 
		// Sinon, calcule la proposition en fonction des deux derniers prix
		else {
			double avantDernierPrix = contrat.getListePrix().get(contrat.getListePrix().size() - 2);

			// Si le dernier prix est inférieur ou égal à 15% plus élevé que le précédent, accepte le prix
			if (dernierPrix <= avantDernierPrix * 1.15) {
				proposition = dernierPrix;
			} 
			// Sinon, propose un prix entre les deux derniers prix avec une réduction de 50%
			else {
				proposition = avantDernierPrix + (dernierPrix - avantDernierPrix) * 0.50;
			}
		}

		// Si la proposition est supérieure au solde disponible, propose le maximum possible
		if (proposition > soldeDisponible) {
			proposition = soldeDisponible;
		}

		// Retourne la proposition de prix
		return proposition;
*/
		



	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method
		this.journalAchats.ajouter(COLOR_LLGRAY, Color.MAGENTA, " CCA : Nouveau CC conclu : PRODUIT ET QT TOTALE = "+
				+ contrat.getQuantiteTotale()+" " +contrat.getProduit()+
				", VENDEUR = "+contrat.getVendeur()+ ", PRIX = "+contrat.getPrix());
		if (contrat.getProduit() == Feve.F_MQ) {
			ContratsAcheteurMQ.add(contrat);
		}
		else if (contrat.getProduit() == Feve.F_HQ_BE ) {
			ContratsAcheteurHQBE.add(contrat);}

	}
	//réussite des négociations sur le contrat précisé en paramètre dans tous les cas 

	@Override
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		IProduit produit= lot.getProduit();
		double quantite = lot.getQuantiteTotale();
		if (contrat == null ) { this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "TROP DE FEVES")
			;}
		else if (produit instanceof Feve) {
			if (this.stockFeves.keySet().contains(produit)) {
				this.stockFeves.put((Feve)produit, this.stockFeves.get(produit)+quantite);
			} else {
				this.stockFeves.put((Feve)produit, quantite);
			}
			this.totalStocksFeves.ajouter(this, quantite, this.cryptogramme);
			this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : reception "+quantite+" T de feves "+produit+". Stock de " + produit + "->  "+this.stockFeves.get(produit));
		}}//mise à jour du stock de fèves après reception d'une livraison



	public ExemplaireContratCadre getContrat(Feve produit) {
		double somme = (this.stockFeves.get(Feve.F_MQ)+this.stockFeves.get(Feve.F_HQ_BE));
		if (somme < 2000.0) {

			this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Recherche vendeur pour " + produit);
			List<IVendeurContratCadre> vendeurs = superviseurVentesCC.getVendeurs(produit);
			// code ajoute par romu pour pallier a l'erreur juste apres d'acces a l'element 0 dans une liste vide
			if (vendeurs.size()==0) {
				return null;
			}
			// fin de code ajoute par romu 
			//100 MQ 500 HQBE
			IVendeurContratCadre vendeur = vendeurs.get((int)(Math.random() * vendeurs.size())); //on cherche un vendeur
			//this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Tentative de négociation de contrat cadre avec " + vendeur.getNom() + " pour " + produit);
			double A = 0;
			if (produit.getGamme() == Gamme.MQ) {
				A = 500; }
			else if ((produit.getGamme() == Gamme.HQ)&&(produit.isBioEquitable())){
				A = 100; }
			ExemplaireContratCadre cc = superviseurVentesCC.demandeAcheteur(this, vendeur, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10,(SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER+A)), cryptogramme,false);
			if (cc != null) {   
				this.journalAchats.ajouter(COLOR_LLGRAY, Color.GREEN, "Contrat cadre passé avec " + vendeur.getNom() + " pour " + produit + "CC : " + cc);
			} 
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
