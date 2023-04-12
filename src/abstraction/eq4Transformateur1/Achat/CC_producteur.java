/**
 * 
 */
package abstraction.eq4Transformateur1.Achat;

import java.awt.Color;
import java.util.List;

import abstraction.eq4Transformateur1.Transformateur1Acteur;
import abstraction.eq4Transformateur1.Transformateur1Transformateur;
import abstraction.eq4Transformateur1.Stock;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

/**
 * @author franc
 *
 */
public class CC_producteur extends Transformateur1Transformateur implements IAcheteurContratCadre{
	public boolean achete(IProduit produit) {
		if (produit instanceof Feve) {
		if (((Feve) produit).getGamme().equals(Gamme.BQ) || (((Feve) produit).getGamme().equals(Gamme.HQ)))  {
			this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'affirme acheter le produit "+produit);
			return true;
		}
		return false;
	}
		return false;
}
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {

		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'accepte l'echeancier "+contrat.getEcheancier());
		return contrat.getEcheancier();
		
	}

	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		double prix=0.0;
		double solde = Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme);
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
		int nbPas=0;
		while (nbPas<30 && prix*contrat.getQuantiteTotale()>(solde/10.0)) {
			prix = 0.75*prix;
			nbPas++;
		};
		if (nbPas==30) {
			return 0.0;
		}
		prix = Math.min(prix, contrat.getPrix());
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : on me propose le prix "+contrat.getPrix()+" -> ma proposition ="+prix);
		return prix;
	}

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : nouveau cc conclu "+contrat);
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
			this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : reception "+quantite+" T de feves "+produit+". Stock->  "+this.stockFeves.get(produit));
		} else {
			this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : reception d'un produit de type surprenant... "+produit);
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
