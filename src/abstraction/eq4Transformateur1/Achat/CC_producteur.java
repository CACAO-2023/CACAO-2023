/**
 * 
 */
package abstraction.eq4Transformateur1.Achat;

import java.awt.Color;
import java.util.LinkedList;

import abstraction.eq4Transformateur1.Transformateur1Transformateur;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

/**
 * @author Francois / Alexian / fouad 
 *
 */



public class CC_producteur extends Transformateur1Transformateur implements IAcheteurContratCadre{
	protected SuperviseurVentesContratCadre superviseurVentesCC;
	
	public void initialiser() {
		super.initialiser();
		this.superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}
	
	public boolean achete(IProduit produit) {
		if (produit instanceof Feve) {
		if (((Feve) produit).getGamme().equals(Gamme.BQ) || (((Feve) produit).getGamme().equals(Gamme.HQ)))  {
			this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'affirme acheter le produit "+produit);
			return true;
		}
		return false;
	}
		return false;
}


	public Echeancier propositionDeLAcheteur(ExemplaireContratCadre contrat) {
		Object produit = contrat.getProduit();
		double qfeve=0;
		int ventetotH = 0;
		int ventetotB = 0;
		for (abstraction.eqXRomu.produits.ChocolatDeMarque c : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (c.getGamme().equals(Gamme.HQ)){
				ventetotH += Filiere.LA_FILIERE.getVentes(c, Filiere.LA_FILIERE.getEtape() );
			}
			if (c.getGamme().equals(Gamme.BQ)){
				ventetotB += Filiere.LA_FILIERE.getVentes(c, Filiere.LA_FILIERE.getEtape() );
			} 
		}
		if (produit instanceof Feve) {
			switch ((Feve)produit) {
			case F_MQ  : return null;
			case F_MQ_BE :return null;
			
			case F_BQ : 
				
				if (this.stockFeves.keySet().contains(produit)) {
					qfeve= this.stockFeves.get(produit);
					if ((qfeve >= ventetotB/30)){ // si quantité >= vente totale basse qualité / (15 steps * 2) en se disant que nous allons prendre 50% du marché
						return null;
					}
					else if (ventetotB/30 > 100){
						this.journal_CC_PROD.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propAchat --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, ventetotB/30));
						return new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 15, ventetotB/30);
					}else {
						this.journal_CC_PROD.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propAchat --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, 101));

							return new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 15, 101);
						}
					}
				
			
			case F_HQ_BE :
				
				
			if (this.stockFeves.keySet().contains(produit)) {
				
				qfeve= this.stockFeves.get(produit);
				if ((qfeve >= ventetotH/30)){
					return null;
				}

				else if (ventetotH/30 > 100){
					this.journal_CC_PROD.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propAchat --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, ventetotH/30));
					return new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 15, ventetotH/30);
				}else {
					this.journal_CC_PROD.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propAchat --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, 101));

						return new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 15, 101);
				}
	 
			}}
		}
		return null;
		}
	
	
	// François Glavatkii et Alexian 
	
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		
		Echeancier echeancier = contrat.getEcheancier();
		int duree = echeancier.getNbEcheances();
		double quantitetot = echeancier.getQuantiteTotale();
		
		int ventetotH = 0;
		int ventetotB = 0;
		for (abstraction.eqXRomu.produits.ChocolatDeMarque c : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (c.getGamme().equals(Gamme.HQ)){
				ventetotH += Filiere.LA_FILIERE.getVentes(c, Filiere.LA_FILIERE.getEtape() );
			}
			if (c.getGamme().equals(Gamme.BQ)){
				ventetotB += Filiere.LA_FILIERE.getVentes(c, Filiere.LA_FILIERE.getEtape() );
			} 
		}
		if (( duree >= 15) && ( quantitetot <= ventetotH) && ( quantitetot >= 10000)) {
			
			this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'accepte l'echeancier "+contrat.getEcheancier());
			return contrat.getEcheancier();
		}
		if (( duree >= 15) && ( quantitetot <= ventetotB) && ( quantitetot >= 10000)) {
			
			this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'accepte l'echeancier "+contrat.getEcheancier());
			return contrat.getEcheancier();
		}
		Feve f = (Feve) contrat.getProduit();
		if (f.getGamme().equals(Gamme.MQ)) {
			return null;
		}
		
		if (f.getGamme().equals(Gamme.HQ)) {
			
			 if (ventetotH/30 > 100){
				this.journal_CC_PROD.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propAchat --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, ventetotH/30));
				return new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 15, ventetotH/30);
			}else {
				this.journal_CC_PROD.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propAchat --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, 101));

					return new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 15, 101);
		}}
		
		if (f.getGamme().equals(Gamme.BQ)) {
			if (ventetotB/30 > 100){
				this.journal_CC_PROD.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propAchat --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, ventetotB/30));
				return new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 15, ventetotB/30);
			}else {
				this.journal_CC_PROD.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propAchat --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, 101));

					return new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 15, 101);
				}
		}
		return null;
		
	}
	
	
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		double prix=1500.0;
		double solde = Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme);
		Object produit = contrat.getProduit();
		if (produit instanceof Feve) {
			switch ((Feve)produit) {
			case F_HQ_BE : prix= 3125;break;
			case F_BQ : prix= 1106;break;
			}
		}
		int nbPas=0;
		while (nbPas<30 && prix*contrat.getQuantiteTotale()>(solde/10.0)) {
			prix = 0.75*prix;
			nbPas++;
		};
		if (nbPas==30) {
			return 0.0;
		}
		prix = Math.min(prix, contrat.getPrix());
		this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : on me propose le prix "+contrat.getPrix()+" -> ma proposition ="+prix);
		return prix;
	}

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		String[] nomA = new String[3];
		nomA[0] = "Eq1";
		nomA[1] = "Eq2";
		nomA[2] = "Eq3";
		for(int i =0; i<=2;i++) {
		if ( contrat.getAcheteur().getNom().equals(nomA[i])) {
		this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : nouveau cc_producteur conclu "+contrat);
		}else {
			this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : nouveau cc_distributeur conclu "+contrat);
			}
		}
	}
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		IProduit produit= lot.getProduit();
		double quantite = lot.getQuantiteTotale();
		if (produit instanceof Feve) {
			if (this.stockFeves.keySet().contains(produit)) {
				this.stockFeves.put((Feve)produit, this.stockFeves.get(produit)+quantite);
			} else {
				this.stockFeves.put((Feve)produit, quantite);
			}
			this.totalStocksFeves.ajouter(this, quantite, this.cryptogramme);
			this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : reception "+quantite+" T de feves "+produit+". Stock->  "+this.stockFeves.get(produit));
		} else {
			this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : reception d'un produit de type surprenant... "+produit);
		}
	}

	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		return 10; // --> j'afficherai un taux de RSE de 10% sur mes chocolats de marque produits
	}

	 public void next() {
			super.next();
		}
}
