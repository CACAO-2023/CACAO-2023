package abstraction.eqXRomu.contratsCadres;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class ExempleTransformateurContratCadreVendeur extends ExempleTransformateurContratCadre implements IVendeurContratCadre {

	protected List<ExemplaireContratCadre> mesContratEnTantQueVendeur;
	
	public ExempleTransformateurContratCadreVendeur(IProduit produit) {
		super(produit);
		this.mesContratEnTantQueVendeur=new LinkedList<ExemplaireContratCadre>();
	}

	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getProduit().equals(produit)) {
			if (contrat.getEcheancier().getQuantiteTotale()<stock.getValeur()) {
				if (Math.random()<0.1) {
				return contrat.getEcheancier(); // on ne cherche pas a negocier sur le previsionnel de livraison
				} else {//dans 90% des cas on fait une contreproposition pour l'echeancier
					Echeancier e = contrat.getEcheancier();
					e.set(e.getStepDebut(), e.getQuantite(e.getStepDebut())/2.0);// on souhaite livrer deux fois moins lors de la 1ere livraison... un choix arbitraire, juste pour l'exemple...
					return e;
				}
			} else {
				return null; // on est frileux : on ne s'engage dans un contrat cadre que si on a toute la quantite en stock (on pourrait accepter meme si nous n'avons pas tout car nous pouvons produire/acheter pour tenir les engagements) 
			}
		} else {
			return null;// on ne vend pas de ce produit
		}
	}

	public double propositionPrix(ExemplaireContratCadre contrat) {
		return 0.5 + (5000.0-contrat.getQuantiteTotale());// plus la quantite est elevee, plus le prix est interessant
	}

	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		if (Math.random()<0.1) {
			return contrat.getPrix(); // on ne cherche pas a negocier dans 10% des cas
		} else {//dans 90% des cas on fait une contreproposition differente
			return 0.5 + (5000.0-(contrat.getQuantiteTotale()*Math.random()));
		}
	}

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.mesContratEnTantQueVendeur.add(contrat);
	}
	
	public void next() {
		List<ExemplaireContratCadre> contratsObsoletes=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.mesContratEnTantQueVendeur) {
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				contratsObsoletes.add(contrat);
			}
		}
		this.mesContratEnTantQueVendeur.removeAll(contratsObsoletes);
	}

	public boolean vend(IProduit produi) {
		return produit==produi;
	}

	public Lot livrer(IProduit produi, double quantite, ExemplaireContratCadre contrat) {
		double livre = Math.min(stock.getValeur(), quantite);
		if (livre>0.0) {
			stock.retirer(this,  livre);
		}
		Lot lot = new Lot(produi);
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre); // cet exemple ne gere pas la peremption : la marchandise est consideree comme produite au step courant
		return lot;
	}

	public boolean peutVendre(IProduit produit) {
		return super.produit.equals(produit);
	}
	public String toString() {
		return this.getNom();
	}

}
