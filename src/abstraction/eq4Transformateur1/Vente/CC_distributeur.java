package abstraction.eq4Transformateur1.Vente;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eq4Transformateur1.Stock;
import abstraction.eq4Transformateur1.Achat.AchatBourse;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.offresAchat.PropositionVenteOA;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

/**

 * @author Fouad LBAKALI & Amine RAHIM & verification François Glavatkii

/**

 *
 */

public class CC_distributeur extends AchatBourse implements IVendeurContratCadre {


	protected SuperviseurVentesContratCadre superviseurVentesCC;
	
	public void initialiser() {
		super.initialiser();
		this.superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}
	
	/**
	 * @author fouad
	 *
	 */

	
	

	//next de Fouad :
	public void next() {
		super.next();

		// === Lancement si possible d'un contrat cadre
		if (this.superviseurVentesCC!=null) {
			// Tentative de lancer un contrat avec tous les acheteurs
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
				List<ChocolatDeMarque> produits = new LinkedList<ChocolatDeMarque>();
				for (ChocolatDeMarque c: Filiere.LA_FILIERE.getChocolatsProduits()) {
					if (c.getMarque().equals("Vccotioi") || c.getMarque().equals("Yocttotoa")) {
						produits.add(c);
					}
				}
				for (ChocolatDeMarque cm : produits) {
					List<IAcheteurContratCadre> acheteurs = superviseurVentesCC.getAcheteurs(cm);
					this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, Color.BLACK, " CCV : tentative de vente de "+cm+" aupres de "+acheteurs);
					for (IAcheteurContratCadre acheteur : acheteurs) {
						if (!acheteur.equals(this)) {
							if (cm.getGamme().equals(Gamme.BQ)){
								Echeancier echeancier = new Echeancier(Filiere.LA_FILIERE.getEtape()+1,15, ventetotB/2);
								this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, Color.BLUE, " CCV_BQ : tentative de vente aupres de "+acheteurs);
								ExemplaireContratCadre contrat1 = superviseurVentesCC.demandeVendeur(acheteur, this, cm, echeancier, this.cryptogramme, false);
								if (contrat1!=null) {
									this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, Color.BLUE, " CCV_BQ : contrat signe = "+contrat1);
							}
							}
							if (cm.getGamme().equals(Gamme.HQ)){
								Echeancier echeancierB = new Echeancier(Filiere.LA_FILIERE.getEtape()+1,15, ventetotH/2);
								this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, Color.BLUE, " CCV_HQ : tentative d'achat aupres de "+acheteurs);
								ExemplaireContratCadre contrat2 = superviseurVentesCC.demandeVendeur(acheteur, this, cm, echeancierB, this.cryptogramme, false);
								if (contrat2!=null) {
									this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, Color.BLUE, " CCV_HQ : contrat signe = "+contrat2);
							}
							}
						}
					}
						}
				}
			}
	
	public boolean vend(IProduit produit) {
		boolean res=false;
		if (produit instanceof ChocolatDeMarque) {
			//this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : vend("+produit+") --> "+(this.stockChocoMarque.keySet().contains(produit)?" dans keySet "+this.stockChocoMarque.get(produit):"pas dans keySet"));
			res=this.stockChocoMarque.keySet().contains(produit) && this.stockChocoMarque.get(produit)>200;
//		} else if (produit instanceof Chocolat) {
			//this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : vend("+produit+") --> "+(this.stockChoco.keySet().contains(produit)?" dans keySet "+this.stockChoco.get(produit):"pas dans keySet"));
//			res=this.stockChoco.keySet().contains(produit) && this.stockChoco.get(produit)>200;
		}
		//this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : vend("+produit+") --> "+res);
		return res;
	}
	
	
	/**
	 @author amine
	 */
	
	public Echeancier propositionDuVendeur(IProduit produit){
		double qtok=0;
		this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  Salut c moi tchoupi");
		if (produit instanceof ChocolatDeMarque) {
			if (this.stockChocoMarque.keySet().contains(produit)) {
				qtok= this.stockChocoMarque.get(produit);
				if (qtok>200) {
				this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propovend --> nouvel echeancier="+new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 15, qtok/15.0));
				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 15, qtok/15.0);
	}
			}
			}
//		else if (produit instanceof Chocolat) {
//			if (this.stockChoco.keySet().contains(produit)) {
//				qtok= this.stockChoco.get(produit);
//				if (qtok>200) {
//				this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propovend --> nouvel echeancier="+new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 15, qtok/15.0));
//				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 15, qtok/15.0);
//				}
//	}
//		}
		return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 15, 101);
	}
	

	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getTeteGondole()) {
			return null;
		}
		Object produit = contrat.getProduit();
		double qtok=0;
		if (produit instanceof ChocolatDeMarque) {
			if ((((ChocolatDeMarque) produit).getMarque().equals("Vccotioi") || ((ChocolatDeMarque) produit).getMarque().equals("Yocttotoa")) && this.stockChocoMarque.keySet().contains(produit)) {
				qtok= this.stockChocoMarque.get(produit);
				if (qtok>200) {
					
					if (contrat.getEcheancier().getQuantiteTotale()<qtok && contrat.getEcheancier().getQuantiteTotale()>100 ){
						this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend --> meme echeancier");
						return contrat.getEcheancier();
					} else {
						this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, (qtok*0.8)/15.0));
						return new Echeancier(contrat.getEcheancier().getStepDebut(), 15, qtok*0.8/15.0);
					}
			}
			}
			}
//		 else if (produit instanceof Chocolat) {
//				switch ((Chocolat)produit) {
//				case C_HQ_BE   : return null;
//				case C_MQ  : return null;
//				case C_MQ_BE :return null;
//				case C_BQ :
//			if (this.stockChoco.keySet().contains(produit)) {
//				qtok= this.stockChoco.get(produit);
//				if (qtok>200) {
//					if (contrat.getEcheancier().getQuantiteTotale()<qtok && contrat.getEcheancier().getQuantiteTotale()>100) {
//						this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend --> meme echeancier");
//						return contrat.getEcheancier();
//					} else {
//						this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, (qtok*0.8)/15.0));
//						return new Echeancier(contrat.getEcheancier().getStepDebut(), 15, qtok*0.8/15.0);
//					}
//			}
//		}
//				
//		}
//		
//		
//}
		this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend --> return null");
		return null;
	}


	
	/**
	 * @author fouad
	 *
	 */
	
	public double propositionPrix(ExemplaireContratCadre contrat) {
		double prix=0.0;
		Object produit = contrat.getProduit();
		if (produit instanceof ChocolatDeMarque) {
			produit = ((ChocolatDeMarque)produit).getChocolat();
		}
		if (produit instanceof Chocolat) {
			switch ((Chocolat)produit) {
			case C_HQ_BE   : prix= 50000;break;
			case C_BQ      : prix= 15000;break;
			}
		}
		this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propose prix de "+prix+" pour "+produit);
		return prix;
	}

	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : "+contrat.getListePrix());
		double prixInit=contrat.getListePrix().get(contrat.getListePrix().size()-2);
		double prix = contrat.getPrix();
		if (prix>0.0 && (prixInit-prix)/prixInit<=0.049) {
			return prix;
		} else {
			return prixInit*(1-0.049);
		}
	}

	public Lot livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		double stock=0.0;
		double livre=0.0;
		Lot lot = null;
		if (produit instanceof ChocolatDeMarque) {
			if (this.stockChocoMarque.keySet().contains(produit)) {
				stock= this.stockChocoMarque.get(produit);
				livre = Math.min(stock, quantite);
				if (quantite>stock) {
					this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  Stock insuffisant pour satisfaire toute la demande");
				}
				if (livre>0) {
					this.stockChocoMarque.put((ChocolatDeMarque)produit, this.stockChocoMarque.get(produit)-livre);
				}
				lot=new Lot((ChocolatDeMarque)produit);
			}
		} 
//		else if (produit instanceof Chocolat) {
//			if (this.stockChoco.keySet().contains(produit)) {
//				stock= this.stockChoco.get(produit);
//				livre = Math.min(stock, quantite);
//				if (quantite>stock) {
//					this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  Stock insuffisant pour satisfaire toute la demande");
//
//				}
//				if (livre>0) {
//					this.stockChoco.put((Chocolat)produit, this.stockChoco.get(produit)-livre);
//				}
//				lot=new Lot((Chocolat)produit);
//			}
//		} 
		this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : doit livrer "+quantite+" de "+produit+" --> livre "+livre);
		if (livre>0) {
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre);}
		return lot;
	}
	

	/*public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.journal_CC_DISTRI.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nouveau cc conclu "+contrat);
	}*/
}

