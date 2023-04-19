package abstraction.eq5Transformateur2;//Fait par Yassine et Wiem

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;


public class Transformateur2VendeurCC extends Transformateur2AcheteurCC implements IVendeurContratCadre {
	public static Color COLOR_LLGRAY = new Color(238,238,238);
	private LinkedList<ExemplaireContratCadre> mesContratEnTantQueVendeur;
	private IProduit produit;
	
	public Transformateur2VendeurCC(IProduit produit) {
		super();  
		this.produit = produit;
		this.mesContratEnTantQueVendeur=new LinkedList<ExemplaireContratCadre>();
	}
	 
	public boolean vend(IProduit produit) {
		if ((produit instanceof ChocolatDeMarque)||(produit instanceof Chocolat)) {
			this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : vend " + produit);
		if (this.stockChocoMarque.get(produit)>1000) { 
			return true;
			}
		else {
			return false;
			}}
		else {return false;}
		}
		// On vend du chocolat seulement si le stock est supérieur à 1000
	
	
	
	/*
	public void InitialisationProposition(ExemplaireContratCadre contrat){
		Object produit = contrat.getProduit();
		double qt=0;
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : propovend(prod="+produit+"  ech="+contrat.getEcheancier());

		return ;//lorsque quelqu'un propose de nous acheter, on recoit l'offre
	}
	*/
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : j'accepte l'echeancier "+contrat.getEcheancier());
		return contrat.getEcheancier(); } //pas de négociations


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
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : propose prix de "+prix+" pour "+produit);
		return prix;
	}

	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		return contrat.getPrix(); //pas de négociations
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
			}}
		this.journalVentes.ajouter(COLOR_LLGRAY, COLOR_LBLUE, "  CCV : doit livrer "+quantite+" de "+produit+" --> livre "+livre);
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre);
		return lot;
	}// renvoie la quantite livrée 
	//la pénalité n'est pas prise en compte 
	
	
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nouveau cc conclu "+contrat);
	}
	
	public boolean peutVendre(IProduit produit) {
		if ((produit.getType().equals("Chocolat"))) {
		return true; }
		else { 
			return false;}
		}


	public ExemplaireContratCadre getContratV(IProduit produit) {
    	this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Recherche acheteur pour " + produit);
    	List<IAcheteurContratCadre> acheteurs = superviseurVentesCC.getAcheteurs(produit);
    	IAcheteurContratCadre acheteur = acheteurs.get((int)(Math.random() * acheteurs.size())); //on cherche un vendeur
    	
    	this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Tentative de négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit);
        ExemplaireContratCadre cc = superviseurVentesCC.demandeVendeur(acheteur, this, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, (SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER+10.0)/10), cryptogramme,false);
       	
        if (cc != null) {   
        		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Contrat cadre passé avec " + acheteur.getNom() + " pour " + produit + "CC : " + cc);
        	} else {
        		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Echec de la négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit);
        	}
        	return cc; //on établit le contrat
    	}
    
    public void next() {
	super.next();
	this.getContratV(Chocolat.C_MQ);
	this.getContratV(Chocolat.C_HQ_BE);

    }
}
