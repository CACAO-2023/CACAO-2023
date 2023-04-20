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
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;


public class Transformateur2VendeurCC extends Transformateur2AcheteurCC implements IVendeurContratCadre {
	public static Color COLOR_LLGRAY = new Color(238,238,238);
	protected SuperviseurVentesContratCadre superviseurVentesCC;
	protected LinkedList<ExemplaireContratCadre> contrats;
	private IProduit produit;
	
	//fait par yassine
	public void initialiser() {
        super.initialiser();
        this.superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
        //this.stockChoco.put(Chocolat.C_MQ,2000.);
    }
	
	
	public Transformateur2VendeurCC() {
		super();  
		this.contrats=new LinkedList<ExemplaireContratCadre>();
	}
	 
	//fait par wiem
	public boolean peutVendre(IProduit produit) {
		return ((produit.getType().equals("Chocolat"))||(produit.getType().equals("ChocolatDeMarque")));} 
	
	//fait par wiem
	public boolean vend(IProduit produit) {
		/*if ((produit.getType().equals("ChocolatDeMarque"))&&((((ChocolatDeMarque)produit).getGamme()== Gamme.MQ) ||((((ChocolatDeMarque)produit).getGamme()== Gamme.HQ)&&(((ChocolatDeMarque)produit).isBioEquitable())))){
			if (this.stockChocoMarque.get(produit)>1000) { 
				this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nous vendons du " + produit.getType() + " " + produit);
				return true;}}
				else*/ if ((produit.getType().equals("Chocolat"))&&((((Chocolat)produit).getGamme()== Gamme.MQ) ||((((Chocolat)produit).getGamme()== Gamme.HQ)&&(((Chocolat)produit).isBioEquitable())))){
		if (this.stockChoco.get(produit)>1000) { 
			this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nous vendons du " + produit.getType() + " " + produit);
			return true;
			}}
		else {this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nous ne vendons pas de " + produit.getType() + " " + produit );
		}return false;}
		
		/*if (produit instanceof Feve) {
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nous ne vendons pas de " + produit.getType() + " " + produit); }
		else if (produit instanceof ChocolatDeMarque) {
			if ((((ChocolatDeMarque)produit).getGamme()== Gamme.MQ) || ((((ChocolatDeMarque)produit).getGamme()==Gamme.HQ)&& ((ChocolatDeMarque)produit).isBioEquitable())) {
				this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nous vendons du " + produit.getType() + " " + produit);
			return true; }
			else if ((produit.getType().equals("Chocolat"))&&((((Chocolat)produit).getGamme()== Gamme.MQ) ||((((Chocolat)produit).getGamme()== Gamme.HQ)&&(((Chocolat)produit).isBioEquitable())))){
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nous vendons du " + produit.getType() + " " + produit );
		return true;}
			else { this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nous ne vendons pas de " + produit.getType() + " " + produit);}}	
		return false;}
		*/
	
		// On vend du chocolat seulement si le stock est supérieur à 1000

	
	/*public void InitialisationProposition(ExemplaireContratCadre contrat){
		Object produit = contrat.getProduit();
		double qt=0;
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : propovend(prod="+produit+"  ech="+contrat.getEcheancier());

		return ;//lorsque quelqu'un propose de nous acheter, on recoit l'offre
	}*/ 


	//fait par yassine
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : j'accepte l'echeancier "+contrat.getEcheancier());
		return contrat.getEcheancier(); } //pas de négociations


	//fait par wiem
	public double propositionPrix(ExemplaireContratCadre contrat) {
		double prix = 0;
		if ((Chocolat)contrat.getProduit() == Chocolat.C_MQ ) {
			prix = 7.0; }
		if ((Chocolat)contrat.getProduit() == Chocolat.C_HQ_BE ) {
		prix = 11.0 ; }
		return prix; }
		
		/*double prix=0.0;
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
	} */

	//fait par yassine 
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		return contrat.getPrix(); //pas de négociations
	}
	
	

	//fait par yassine
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
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre);
		return lot;
	}// renvoie la quantite livrée 
	//la pénalité n'est pas prise en compte 
	
	
	//fait par yassine 
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCV : nouveau cc conclu "+contrat);
	}


	//fait par wiem 
	public ExemplaireContratCadre getContrat(Chocolat produit) {
    	this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Recherche acheteur pour " + produit);
    	List<IAcheteurContratCadre> acheteurs = superviseurVentesCC.getAcheteurs(produit);
    	IAcheteurContratCadre acheteur = acheteurs.get((int)(Math.random() * acheteurs.size())); //on cherche un acheteur
    	
    	this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Tentative de négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit);
        ExemplaireContratCadre cc = superviseurVentesCC.demandeVendeur(acheteur, this, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, (SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER+10.0)/10), cryptogramme,false);
        if (cc != null) {   
        		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Contrat cadre passé avec " + acheteur.getNom() + " pour " + produit + "CC : " + cc);
        	} else {
        		this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Echec de la négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit);
        	}
        	return cc; //on établit le contrat
    	}
    
   //fait par wiem
	public void next() {
	super.next();
	this.getContrat(Chocolat.C_MQ);
	this.getContrat(Chocolat.C_HQ_BE);

    }
	/*
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
	
	public ExemplaireContratCadre getContrat(IProduit produit) {
		if (produit instanceof Feve) {
    	this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Recherche vendeur pour " + produit);
    	List<IVendeurContratCadre> vendeurs = superviseurVentesCC.getVendeurs(produit);
    	IVendeurContratCadre vendeur = vendeurs.get((int)(Math.random() * vendeurs.size())); //on cherche un vendeur
    	
    	this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Tentative de négociation de contrat cadre avec " + vendeur.getNom() + " pour " + produit);
        ExemplaireContratCadre cc = superviseurVentesCC.demandeAcheteur(this, vendeur, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, (SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER+10.0)/10), cryptogramme,false);
       if (cc != null) { 
        		this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Contrat cadre passé avec " + vendeur.getNom() + " pour " + produit + "CC : " + cc);
        	} else {
        		this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Echec de la négociation de contrat cadre avec " + vendeur.getNom() + " pour " + produit);
        	}
        	return cc; } 
		//on établit le contrat
    	 else if (produit instanceof Chocolat) {
        	this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Recherche acheteur pour " + produit);
        	List<IAcheteurContratCadre> acheteurs = superviseurVentesCC.getAcheteurs(produit);
        	IAcheteurContratCadre acheteur = acheteurs.get((int)(Math.random() * acheteurs.size())); //on cherche un vendeur
        	
        	this.journalVentes.ajouter(COLOR_LLGRAY, Color.BLUE, "Tentative de négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit);
            ExemplaireContratCadre cc = superviseurVentesCC.demandeAcheteur(acheteur, this, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, (SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER+10.0)/10), cryptogramme,false);
            if (cc != null) {   
           
            		this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Contrat cadre passé avec " + acheteur.getNom() + " pour " + produit + "CC : " + cc);
            } else {
            		this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Echec de la négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit);
            	
            	return cc;}
		return null;}
		return null;}
        	
    
    public void next() {
	super.next();
	this.getContrat(Feve.F_MQ);
	this.getContrat(Feve.F_HQ_BE);
	this.getContrat(Chocolat.C_MQ);
	this.getContrat(Chocolat.C_HQ_BE);
	}*/

}
