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

public class Transformateur2AcheteurCC extends Transformateur2 implements IAcheteurContratCadre {

	public static Color COLOR_LLGRAY = new Color(238,238,238);
	protected SuperviseurVentesContratCadre superviseurVentesCC;
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

	public void next() {
		super.next();
	
	//this.journal.ajouter(COLOR_LLGRAY, COLOR_LPURPLE, "Qt de fèves achetées "+Journal.texteSurUneLargeurDe(f+"", 15;
	}}
