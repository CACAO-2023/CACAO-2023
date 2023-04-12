package abstraction.eq4Transformateur1.Vente;

import java.awt.Color;

import abstraction.eq4Transformateur1.Stock;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.offresAchat.PropositionVenteOA;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class CC_distributeur extends Stock implements IVendeurContratCadre {

	public boolean vend(IProduit produit) {
		boolean res=false;
		if (produit instanceof ChocolatDeMarque) {
			this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : vend("+produit+") --> "+(this.stockChocoMarque.keySet().contains(produit)?" dans keySet "+this.stockChocoMarque.get(produit):"pas dans keySet"));
			res=this.stockChocoMarque.keySet().contains(produit) && this.stockChocoMarque.get(produit)>1000;
		} else if (produit instanceof Chocolat) {
			this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : vend("+produit+") --> "+(this.stockChoco.keySet().contains(produit)?" dans keySet "+this.stockChoco.get(produit):"pas dans keySet"));
			res=this.stockChoco.keySet().contains(produit) && this.stockChoco.get(produit)>1000;
		}
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : vend("+produit+") --> "+res);
		return res;
	}
	
	public Echeancier propositionDuVendeur(ExemplaireContratCadre contrat){
		
		Object produit = contrat.getProduit();
		double qtok=0;
		if (produit instanceof ChocolatDeMarque) {
			if (this.stockChocoMarque.keySet().contains(produit)) {
				qtok= this.stockChocoMarque.get(produit);
				this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propovend --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, qtok/15.0));
				return new Echeancier(contrat.getEcheancier().getStepDebut(), 15, qtok/15.0);
	}
			}
		else if (produit instanceof Chocolat) {
			if (this.stockChoco.keySet().contains(produit)) {
				qtok= this.stockChoco.get(produit);
				this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propovend --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, qtok/15.0));
				return new Echeancier(contrat.getEcheancier().getStepDebut(), 15, qtok/15.0);

	}
		}
		return null;
	}

	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getTeteGondole()) {
			return null;
		}
		Object produit = contrat.getProduit();
		double qtok=0;
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend(prod="+produit+"  ech="+contrat.getEcheancier());

		if (produit instanceof ChocolatDeMarque) {
			switch ((Chocolat)produit) {
			case C_BQ   : return null;
			case C_MQ  : return null;
			case C_MQ_BE :return null;
			case C_HQ_BE :
			if (this.stockChocoMarque.keySet().contains(produit)) {
				qtok= this.stockChocoMarque.get(produit);
				if (qtok>+0.0) {
					
					if (contrat.getEcheancier().getQuantiteTotale()<qtok ){
						this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend --> meme echeancier");
						return contrat.getEcheancier();
					} else {
						this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, (qtok*0.8)/15.0));
						return new Echeancier(contrat.getEcheancier().getStepDebut(), 15, qtok*0.8/15.0);
					}
			}
			}
			}
		} else if (produit instanceof Chocolat) {
				switch ((Chocolat)produit) {
				case C_HQ_BE   : return null;
				case C_MQ  : return null;
				case C_MQ_BE :return null;
				case C_BQ :
			if (this.stockChoco.keySet().contains(produit)) {
				qtok= this.stockChoco.get(produit);
				if (qtok>0.0) {
					if (contrat.getEcheancier().getQuantiteTotale()<qtok) {
						this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend --> meme echeancier");
						return contrat.getEcheancier();
					} else {
						this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : contrepropovend --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 15, qtok*0.8/15.0));
						return new Echeancier(contrat.getEcheancier().getStepDebut(), 15, qtok*0.8/15.0);
					}
			}
		}
				
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
			case C_HQ_BE   : prix= 11.55;break;
			case C_BQ      : prix=  5.25;break;
			}
		}
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : propose prix de "+prix+" pour "+produit);
		return prix;
	}

	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		double prixInit=contrat.getListePrix().get(0);
		double prix = contrat.getPrix();
		if (prix>0.0 && (prixInit-prix)/prixInit<=0.49) {
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
		} 
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : doit livrer "+quantite+" de "+produit+" --> livre "+livre);
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre);
		return lot;
	}

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : nouveau cc conclu "+contrat);
	}
}
