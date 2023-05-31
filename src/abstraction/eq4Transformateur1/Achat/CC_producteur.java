/**
 * 
 */
package abstraction.eq4Transformateur1.Achat;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eq4Transformateur1.Transformateur1Transformateur;
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
		return null;
	
		}
	
	
	// François Glavatkii et Alexian 
	
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		/*
		Echeancier echeancier = contrat.getEcheancier();
		int duree = echeancier.getNbEcheances();
		double quantitetot = echeancier.getQuantiteTotale();
		
		int ventetotX = 0;
		for (abstraction.eqXRomu.produits.ChocolatDeMarque c : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if(c.getGamme().equals(((Feve)contrat.getProduit()).getGamme())) {
				ventetotX += Filiere.LA_FILIERE.getVentes(c, Filiere.LA_FILIERE.getEtape() );
			}
		}
//		if (( duree >= 15) && ( quantitetot <= ventetotX) && ( quantitetot >= 10000)) {
			
//			this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'accepte l'echeancier "+contrat.getEcheancier());
//			return contrat.getEcheancier();
//		}

		Feve f = (Feve) contrat.getProduit();
		if (f.getGamme().equals(Gamme.MQ)) {
			return null;
		}
		
		int aLivrer =0;
		if (f.getGamme().equals(Gamme.HQ)) {
			for (ExemplaireContratCadre c : ContratEnCours_F_HQ) {
				aLivrer += c.getQuantiteALivrerAuStep();
			}
		}
		else if (f.getGamme().equals(Gamme.BQ)) {
			for (ExemplaireContratCadre c : ContratEnCours_F_BQ) {
				aLivrer += c.getQuantiteALivrerAuStep();
			}
		}
		if (aLivrer>this.qteTransfo*2) {
			return null;
		}
			
<<<<<<< HEAD
//		if (f.getGamme().equals(Gamme.HQ)) {
			
//			 if (ventetotX/2 > 100){
//				this.journal_CC_PROD.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCA : propAchat --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, ventetotX/2));
//				return new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 15, ventetotX/2);
//			}else {
//				this.journal_CC_PROD.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCA : propAchat --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, ventetotX/2));
=======
			 if (ventetotX/2 > 500){
				this.journal_CC_PROD.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCA : propAchat avec "+contrat.getVendeur()+"--> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, ventetotX/2));
				return new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 15, ventetotX/2);
			}else {
				this.journal_CC_PROD.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCA : propAchat avec "+contrat.getVendeur()+"--> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, 1000));
>>>>>>> branch 'main' of https://github.com/AlexianBtrl/CACAO-2023-Eq4

//					return new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 15, ventetotX/2);
//		}  
//			 }
		return echeancier;
		}
		
		return null;*/
		return contrat.getEcheancier();
	}

	
	// François et Fouad  	
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		/*double prix=3000.0;
//		System.out.println(" type produit "+contrat.getProduit());
		double solde = Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme);
		Object produit = contrat.getProduit();
		if (produit instanceof Feve) {
			switch ((Feve)produit) {
			case F_HQ_BE : prix= 3500;break;
			case F_BQ : prix= 1506;break;
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
		this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : "+contrat.getVendeur()+" propose le prix "+contrat.getPrix()+" -> ma proposition ="+prix);
		return prix;*/
		
		
		// condition avec le prix du chocolat 
		
		this.journal_CC_PROD.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCA : "+contrat.getListePrix());
		double prixInit = contrat.getPrix();
		if (contrat.getListePrix().size()==7) {
			return prixInit;
		}else {
		if (contrat.getListePrix().size()==1) {
			return prixInit*0.9;
		} else {
			double prix=contrat.getListePrix().get(contrat.getListePrix().size()-2);
			return prix*1.015165;
		}
	}
}
		


// François Glavatkii
	
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		String[] nomA = new String[3];

        nomA[0] = "Eq1";
        nomA[1] = "Eq2";
        nomA[2] = "Eq3";
        for(int i =0; i<=2;i++) {
        if ( contrat.getAcheteur().getNom().equals(nomA[i])) {
        this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : nouveau cc_producteur conclu "+contrat);
        }else {
            this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nouveau cc_distributeur conclu "+contrat);
            }
        }
	}


	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		IProduit produit= lot.getProduit();
		double quantite = lot.getQuantiteTotale();
		if (produit instanceof Feve) {
			this.ajouter(produit, quantite);
			this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : reception "+quantite+" T de feves "+produit+". Stock->  "+this.stockFeves.get(produit));
		} else {
			this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : reception d'un produit de type surprenant... "+produit);
		}
	}

	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		return 10; // --> j'afficherai un taux de RSE de 10% sur mes chocolats de marque produits
	}

	// François et Fouad

	 public void next() {
			super.next();
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
			List<Feve> produits = new LinkedList<Feve>();
			Feve fb = Feve.F_BQ;	
			produits.add(fb);
			Feve fh = Feve.F_HQ_BE;	
			produits.add(fh);
			for (Feve cm : produits ) {
				List<IVendeurContratCadre> vendeurs = superviseurVentesCC.getVendeurs(cm);
				this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLACK, " CCA : tentative d'achat de "+cm+" aupres de "+vendeurs);
				for (IVendeurContratCadre vendeur : vendeurs) {
					if (!vendeur.equals(this)) {
						if (cm.getGamme().equals(Gamme.BQ)){
							double quantite = 0;
							if (ventetotB/2>100) {
								quantite = ventetotB/2;
							}
							else {
								quantite = 101;
							}
							Echeancier echeancier = new Echeancier(Filiere.LA_FILIERE.getEtape()+1,15, quantite);
							this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, " CCA : tentative d'achat aupres de "+vendeurs);
							ExemplaireContratCadre contrat1 = superviseurVentesCC.demandeAcheteur(this, vendeur, cm, echeancier, this.cryptogramme, false);
							if (contrat1!=null) {
								this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, " CCA_BQ: contrat signe = "+contrat1);
								this.ContratEnCours_F_BQ.add(contrat1);

						}
						}
						if (cm.getGamme().equals(Gamme.HQ)){
							double quantite2 = 0;
							if (ventetotH/2>100) {
								quantite2 = ventetotH/2;
							}
							else {
								quantite2 = 101;
							}
							Echeancier echeancierH = new Echeancier(Filiere.LA_FILIERE.getEtape()+1,15, quantite2);
							this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, " CCA : tentative d'achat aupres de "+vendeurs);
							ExemplaireContratCadre contrat2 = superviseurVentesCC.demandeAcheteur(this, vendeur, cm, echeancierH, this.cryptogramme, false);
							if (contrat2!=null) {
								this.journal_CC_PROD.ajouter(COLOR_LLGRAY, Color.BLUE, " CCA_HQ : contrat signe = "+contrat2);
								ContratEnCours_F_HQ.add(contrat2);
								
						}
					}
				}
			}
		}
	}
}
