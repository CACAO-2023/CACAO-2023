package abstraction.eqXRomu;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class RomuATVBABVAOAAOACCVendeurCC extends RomuATVBABVAOAAOAcheteurCC implements IVendeurContratCadre {

	public void next() {
		super.next();

		// === Lancement si possible d'un contrat cadre
		if (this.superviseurVentesCC!=null) {
			List<IProduit> produits = new LinkedList<IProduit>();
			produits.addAll(Filiere.LA_FILIERE.getChocolatsProduits());
			for (Feve f : Feve.values()) {
				produits.add(f);
			}
			for (Chocolat c : Chocolat.values()) {
				produits.add(c);
			}
			this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, " CCA : Tentative de lancer un contrat cadre");
			this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, " CCA : Liste de tous les produits "+produits);
			List<IProduit> produitsVendus = new LinkedList<IProduit>();
			List<IProduit> produits2Vendeurs = new LinkedList<IProduit>();
			for (IProduit prod : produits) {
				if (superviseurVentesCC.getVendeurs(prod).size()>0) {
					produitsVendus.add(prod);
					if (superviseurVentesCC.getVendeurs(prod).size()>1) {
						produits2Vendeurs.add(prod);
					}
				}
			}
			this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, " CCA : Liste de tous les produits pour lesquels il existe au moins 1 vendeur  "+produitsVendus);
			this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, " CCA : Liste de tous les produits pour lesquels il existe au moins 2 vendeurs "+produits2Vendeurs);
			if (produitsVendus.size()>0) {
				IProduit produit = produitsVendus.get((int)(Math.random()*produitsVendus.size()));
				this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, " CCA : Produit tire au sort = "+produit);
				List<IVendeurContratCadre> vendeurs = superviseurVentesCC.getVendeurs(produit);
				this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, " CCA : Les vendeurs de "+produit+" sont : "+vendeurs);
				if (vendeurs.size()>0) {
					IVendeurContratCadre vendeur = vendeurs.get((int)(Math.random()*vendeurs.size()));
					if (vendeur!=this) { // on ne peut pas passer de contrat avec soi meme
						this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, " CCA : Vendeur tire au sort = "+vendeur);
						Echeancier echeancier = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, 100);
						ExemplaireContratCadre contrat = superviseurVentesCC.demandeAcheteur(this, vendeur, produit, echeancier, this.cryptogramme, false);
						if (contrat!=null) {
							this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, " CCA : contrat signe = "+contrat);
						}
					}
				}
			}
		}
	}

	//========================================================
	//                  IVendeurContratCadre
	//========================================================

	public boolean vend(IProduit produit) {
		boolean res=false;
		if (produit instanceof ChocolatDeMarque) {
			this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : vend("+produit+") --> "+(this.stockChocoMarque.keySet().contains(produit)?" dans keySet "+this.stockChocoMarque.get(produit):"pas dans keySet"));
			res=this.stockChocoMarque.keySet().contains(produit) && this.stockChocoMarque.get(produit)>1000;
		} else if (produit instanceof Chocolat) {
			this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : vend("+produit+") --> "+(this.stockChoco.keySet().contains(produit)?" dans keySet "+this.stockChoco.get(produit):"pas dans keySet"));
			res=this.stockChoco.keySet().contains(produit) && this.stockChoco.get(produit)>1000;
		} else if (produit instanceof Feve) {
			this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : vend("+produit+") --> "+(this.stockFeves.keySet().contains(produit)?" dans keySet "+this.stockFeves.get(produit):"pas dans keySet"));
			res=this.stockFeves.keySet().contains(produit) && this.stockFeves.get(produit)>1000;
		} 
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : vend("+produit+") --> "+res);
		return res;
	}

	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getTeteGondole()) {
			return null;
		}
		Object produit = contrat.getProduit();
		double qtok=0;
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend(prod="+produit+"  ech="+contrat.getEcheancier());

		if (produit instanceof ChocolatDeMarque) {
			if (this.stockChocoMarque.keySet().contains(produit)) {
				qtok= this.stockChocoMarque.get(produit);
			}
		} else if (produit instanceof Chocolat) {
			if (this.stockChoco.keySet().contains(produit)) {
				qtok= this.stockChoco.get(produit);
			}
		} else if (produit instanceof Feve) {
			if (this.stockFeves.keySet().contains(produit)) {
				qtok= this.stockFeves.get(produit);
			}
		} 
		if (qtok<1000.0) {
			qtok=0.0;
		} else {
			if (contrat.getEcheancier().getQuantiteTotale()<qtok) {
				this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend --> meme echeancier");
				return contrat.getEcheancier();
			} else {
				this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 10, qtok/10.0));
				return new Echeancier(contrat.getEcheancier().getStepDebut(), 10, qtok/10.0);
			}
		}
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend --> return null");
		return null;
	}


	public double propositionPrix(ExemplaireContratCadre contrat) {
		double prix=0.0;
		Object produit = contrat.getProduit();
		if (produit instanceof ChocolatDeMarque) {
			produit = ((ChocolatDeMarque)produit).getChocolat();
		}
		if (produit instanceof Chocolat) {
			switch ((Chocolat)produit) {
			case C_HQ_BE   : prix= 11.0;break;
			case C_MQ_BE   : prix=  7.0;break;
			case C_MQ      : prix=  6.0;break;
			case C_BQ      : prix=  5.0;break;
			}
		} else if (produit instanceof Feve) {
			switch ((Feve)produit) {
			case F_HQ_BE : prix= 3.5;break;
			case F_MQ_BE    : prix= 2.7;break;
			case F_MQ      : prix= 2.5;break;
			case F_BQ : prix= 1.5;break;
			}
		}
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propose prix de "+prix+" pour "+produit);
		return prix;
	}

	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		double prixInit=contrat.getListePrix().get(0);
		double prix = contrat.getPrix();
		if (prix>0.0 && (prixInit-prix)/prixInit<=0.05) {
			return prix;
		} else {
			return prixInit;
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
				if (livre>0) {
					this.stockChocoMarque.put((ChocolatDeMarque)produit, this.stockChocoMarque.get(produit)-livre);
				}
				lot=new Lot((ChocolatDeMarque)produit);
			}
		} else if (produit instanceof Chocolat) {
			if (this.stockChoco.keySet().contains(produit)) {
				stock= this.stockChoco.get(produit);
				livre = Math.min(stock, quantite);
				if (livre>0) {
					this.stockChoco.put((Chocolat)produit, this.stockChoco.get(produit)-livre);
				}
				lot=new Lot((Chocolat)produit);
			}
		} else if (produit instanceof Feve) {
			if (this.stockFeves.keySet().contains(produit)) {
				stock= this.stockFeves.get(produit);
				livre = Math.min(stock, quantite);
				if (livre>0) {
					this.stockFeves.put((Feve)produit, this.stockFeves.get(produit)-livre);
				}
				lot=new Lot((Feve)produit);
			}
		} 
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : doit livrer "+quantite+" de "+produit+" --> livre "+livre);
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre);
		return lot;
	}

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : nouveau cc conclu "+contrat);
	}



}
