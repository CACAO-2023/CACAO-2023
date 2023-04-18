package abstraction.eq5Transformateur2;//Fait par Yassine 

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;


public class Transformateur2VendeurCC extends Transformateur2 implements IVendeurContratCadre {
	public static Color COLOR_LLGRAY = new Color(238,238,238);
	private LinkedList<ExemplaireContratCadre> mesContratEnTantQueVendeur;
	private IProduit produit;
	
	public Transformateur2VendeurCC(IProduit produit) {
		super();
		this.produit = produit;
		this.mesContratEnTantQueVendeur=new LinkedList<ExemplaireContratCadre>();
	}
	
	public boolean vend(IProduit produit) {
		boolean res=false;
		if (produit instanceof ChocolatDeMarque) {
			this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : vend("+produit+") --> "+(this.stockChocoMarque.keySet().contains(produit)?" dans keySet "+this.stockChocoMarque.get(produit):"pas dans keySet"));
			res=this.stockChocoMarque.keySet().contains(produit) && this.stockChocoMarque.get(produit)>1000;
		} else if (produit instanceof Chocolat) {
			this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : vend("+produit+") --> "+(this.stockChoco.keySet().contains(produit)?" dans keySet "+this.stockChoco.get(produit):"pas dans keySet"));
			res=this.stockChoco.keySet().contains(produit) && this.stockChoco.get(produit)>1000;
		}
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : vend("+produit+") --> "+res);
		return res;//On initie la vente de produit en fonction des stocks
	}
	/*
	public void InitialisationProposition(ExemplaireContratCadre contrat){
		Object produit = contrat.getProduit();
		double qt=0;
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : propovend(prod="+produit+"  ech="+contrat.getEcheancier());

		return ;//lorsque quelqu'un propose de nous acheter, on recoit l'offre
	}
	*/
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		
		Object produit = contrat.getProduit();
		double qt=0;
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : contrepropovend(prod="+produit+"  ech="+contrat.getEcheancier());

		if (produit instanceof ChocolatDeMarque) {
			if (this.stockChocoMarque.keySet().contains(produit)) {
				qt= this.stockChocoMarque.get(produit);
				if (qt<1000.0) {
					qt=0.0;
				} 
				else {
					if (contrat.getEcheancier().getQuantiteTotale()<qt ){
						this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : contrepropovend --> meme echeancier");
						return contrat.getEcheancier();
					} else {
						this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : contrepropovend --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 10, qt/10.0));
						return new Echeancier(contrat.getEcheancier().getStepDebut(), 10, qt/10.0);
					}
			}
				
		} else if (produit instanceof Chocolat) {
			if (this.stockChoco.keySet().contains(produit)) {
				qt= this.stockChoco.get(produit);
				if (qt<1000.0) {
					qt=0.0;
				} 
				else {
					if (contrat.getEcheancier().getQuantiteTotale()<qt) {
						this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : contrepropovend --> meme echeancier");
						return contrat.getEcheancier();
					} else {
						this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : contrepropovend --> nouvel echeancier="+new Echeancier(contrat.getEcheancier().getStepDebut(), 10, qt/10.0));
						return new Echeancier(contrat.getEcheancier().getStepDebut(), 10, qt/10.0);
					}
			}
		}
		
		}
		}
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : contrepropovend --> return null");
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
			case C_MQ      : prix=  7.0;break;
			}
		}
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : propose prix de "+prix+" pour "+produit);
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
		} 
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : doit livrer "+quantite+" de "+produit+" --> livre "+livre);
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre);
		return lot;
	}

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : nouveau cc conclu "+contrat);
	}
	
	public boolean peutVendre(IProduit produit) {
		return produit.equals(produit);
	}
	
	
	public String toString() {
		return this.getNom();
	}
	/*
	public void next() {
		List<ExemplaireContratCadre> contratsObsoletes=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.mesContratEnTantQueVendeur) {
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				contratsObsoletes.add(contrat);
			}
		}
		this.mesContratEnTantQueVendeur.removeAll(contratsObsoletes);
		super.next();
	}
	*/
}
