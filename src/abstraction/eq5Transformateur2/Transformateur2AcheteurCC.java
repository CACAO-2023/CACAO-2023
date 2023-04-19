package abstraction.eq5Transformateur2; ///WIEM

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
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;
import java.util.LinkedList;
import java.util.List;

public class Transformateur2AcheteurCC extends Transformateur2Transfo implements IAcheteurContratCadre {

	public static Color COLOR_LLGRAY = new Color(238,238,238);
	protected SuperviseurVentesContratCadre superviseurVentesCC;
	protected LinkedList<ExemplaireContratCadre> contrats;
	public void initialiser() {
		super.initialiser();
		this.superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}

	@Override
	public boolean achete(IProduit produit) {
		// TODO Auto-generated method stub
		if (produit.getType().equals("Feve")) {
			this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'affirme vouloir acheter le produit "+produit);
		return true;} //on achète tout type de fèves
		else {
			this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'affirme ne pas vouloir acheter le produit "+produit);
		return false;}
		
		
		
	}

	@Override
	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		if ((( ((ChocolatDeMarque) produit).getMarque())) == "Maison Doutre") {
			return 10; } //1O% de RSE pour la marque "Maison Doutre"
		else { 
			return 0; } // 0% pour la marque "ChocoPop"
			}

		// TODO Auto-generated method stub


	@Override
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'accepte l'echeancier "+contrat.getEcheancier());
		return contrat.getEcheancier(); //pas de négociations 
	}
		

	@Override
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
			return contrat.getPrix(); //pas de négociations
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : nouveau cc conclu "+contrat);
	} //réussite des négociations sur le contrat précisé en paramètre dans tous les cas 

	@Override
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		IProduit produit= lot.getProduit();
		double quantite = lot.getQuantiteTotale();
		if (produit instanceof Feve) {
			if (this.stockFeves.keySet().contains(produit)) {
				this.stockFeves.put((Feve)produit, this.stockFeves.get(produit)+quantite);
			} else {
				this.stockFeves.put((Feve)produit, quantite);
			}
				this.totalStocksFeves.ajouter(this, quantite, this.cryptogramme);
			this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : reception "+quantite+" T de feves "+produit+". Stock->  "+this.stockFeves.get(produit));
		} else {
			this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : reception d'un produit de type surprenant... "+produit);
		}}//mise à jour du stock de fèves après reception d'une livraison
		  //ne prend pas en compte la pénalité si la quantité livrée est inférieure à la quantité prévue

	       
	   
	    public ExemplaireContratCadre getContrat(Feve produit) {
	    	this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Recherche vendeur pour " + produit);
	    	List<IVendeurContratCadre> vendeurs = superviseurVentesCC.getVendeurs(produit);
	    	IVendeurContratCadre vendeur = vendeurs.get((int)(Math.random() * vendeurs.size())); //on cherche un vendeur
	    	
	    	this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Tentative de négociation de contrat cadre avec " + vendeur.getNom() + " pour " + produit);
	        ExemplaireContratCadre cc = superviseurVentesCC.demandeAcheteur(this, vendeur, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, (SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER+10.0)/10), cryptogramme,false);
	        	//demandeAcheteur(acheteur, vendeur, produit, echeancier, cryptogramme, tg, 0);
	        if (cc != null) {   
	        		this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Contrat cadre passé avec " + vendeur.getNom() + " pour " + produit + "CC : " + cc);
	        	} else {
	        		this.journalAchats.ajouter(COLOR_LLGRAY, Color.BLUE, "Echec de la négociation de contrat cadre avec " + vendeur.getNom() + " pour " + produit);
	        	}
	        	return cc; //on établit le contrat
	    	}
	    
	    public void next() {
		super.next();
		this.getContrat(Feve.F_MQ);
		this.getContrat(Feve.F_HQ_BE);
		

		}
}
	