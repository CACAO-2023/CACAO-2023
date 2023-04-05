/**
 * 
 */
package abstraction.eq4Transformateur1.Achat;

import java.awt.Color;
import java.util.List;

import abstraction.eq4Transformateur1.Transformateur1Acteur;
import abstraction.eq4Transformateur1.Stock;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

/**
 * @author franc
 *
 */
public class CC_producteur extends Stock implements IAcheteurContratCadre{
	public boolean achete(IProduit produit) {
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'affirme acheter le produit "+produit);
		return true;
	}

	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (contrat.getTeteGondole()) {
			return null;
		}
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
		if (produit instanceof ChocolatDeMarque) {
			if (this.stockChocoMarque.keySet().contains(produit)) {
				this.stockChocoMarque.put((ChocolatDeMarque)produit, this.stockChocoMarque.get(produit)+quantite);
			} else {
				this.stockChocoMarque.put((ChocolatDeMarque)produit, quantite);
			}
			this.totalStocksChocoMarque.ajouter(this, quantite, this.cryptogramme);
			this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : reception "+quantite+" T de C.M. "+produit+". Stock->  "+this.stockChocoMarque.get(produit));
		} else if (produit instanceof Chocolat) {
			if (this.stockChoco.keySet().contains(produit)) {
				this.stockChoco.put((Chocolat)produit, this.stockChoco.get(produit)+quantite);
			} else {
				this.stockChoco.put((Chocolat)produit, quantite);
			}
			this.totalStocksChoco.ajouter(this, quantite, this.cryptogramme);
			this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : reception "+quantite+" T de Choco "+produit+". Stock->  "+this.stockChoco.get(produit));
		} else if (produit instanceof Feve) {
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
		return 15; // --> j'afficherai un taux de RSE de 0% sur mes chocolats de marque produits
	}
	
}
