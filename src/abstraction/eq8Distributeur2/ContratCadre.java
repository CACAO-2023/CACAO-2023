package abstraction.eq8Distributeur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class ContratCadre extends Distributeur2Acteur implements IAcheteurContratCadre {
	private List<ExemplaireContratCadre> contratEnCours;

	public void initialiser() {	
	}

	//Auteur : Marzougui Mariem
	public boolean achete(IProduit produit) {
		if (produit instanceof ChocolatDeMarque) {
			this.journal_achats.ajouter("achat du produit"+ produit.toString());
			return true;
		}
		return false;
	}
	
	//Auteur : Marzougui Mariem
	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		return 10;
	}

	//Auteur : Marzougui Mariem
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (contrat.getProduit() instanceof ChocolatDeMarque) {
			ChocolatDeMarque produit = (ChocolatDeMarque) contrat.getProduit();
			if (produit != null && this.stocks.getStock(produit) != 0.0 ) {
				double quantiteEnStock = this.stocks.getStock(produit);
				if (contrat.getEcheancier().getQuantiteTotale() < quantiteEnStock) {
					if (Math.random() < 0.1) {
						this.notificationNouveauContratCadre(contrat);
						this.journal_ContratCadre.ajouter("effectuation du contrat:"+contrat.toString());
						return contrat.getEcheancier(); // on ne cherche pas a negocier sur le previsionnel de livraison
						
					} else { //dans 90% des cas on fait une contreproposition pour l'echeancier
						Echeancier e = contrat.getEcheancier();
						e.set(e.getStepDebut(), e.getQuantite(e.getStepDebut()) / 2.0); // on souhaite livrer deux fois moins lors de la 1ere livraison
						this.notificationNouveauContratCadre(contrat);
						this.journal_ContratCadre.ajouter("effectuation du contrat:"+contrat.toString()+contrePropositionPrixAcheteur(contrat));
						return e;
					}
				} else {
					this.journal_ContratCadre.ajouter("rejet du contrat:"+contrat.toString()+"frileux"+contrat.getProduit()+"    "+quantiteEnStock);
					return null; // on est frileux : on ne s'engage dans un contrat cadre que si on a toute la quantite en stock (on pourrait accepter même si nous n'avons pas tout car nous pouvons produire/acheter pour tenir les engagements)
				}
			} else {
				this.journal_ContratCadre.ajouter("rejet du contrat:"+contrat.toString()+"on ne vend pas de ce produit"+contrat.getProduit());
				return null; // on ne vend pas de ce produit
			}}else {
				this.journal_ContratCadre.ajouter("le produit de ce contrat ne correspond pas à une marque de chocolat"+contrat.toString());
				return null;
			}
	}

	//Auteur : Marzougui Mariem
	//On retourne le prix sans négociation
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		return contrat.getPrix()*0.95;
	}

	//Auteur : Marzougui Mariem
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.journal_ContratCadre.ajouter("contrat effectué:"+contrat.toString()+contrePropositionPrixAcheteur(contrat));	
	}

	public void next() {
		super.next();
	}

	//Auteur : Marzougui Mariem
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		stocks.ajouterAuStock((ChocolatDeMarque)(contrat.getProduit()),lot.getQuantiteTotale() );
		stock_total+=lot.getQuantiteTotale();
		s.setValeur(this, stock_total, this.cryptogramme);
		this.journal_stocks.ajouter("ajout d'une quantité de"+lot.getQuantiteTotale()+"T livraison de CC "+contrat.getNumero());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

