package abstraction.eq1Producteur1;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

import java.util.LinkedList;
import java.util.List;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
// AYOUB
public class ProducteurVendeurCC extends Producteur1Plantation implements IVendeurContratCadre{
    private List<ExemplaireContratCadre> mescontrats;
    
	
	
	
	public ProducteurVendeurCC() {
		super();
		this.mescontrats= new LinkedList<ExemplaireContratCadre>();
	}
	
	public boolean peutVendre(IProduit produit) {
		return (produit instanceof Feve) ;
	}

	
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		if (this.peutVendre((IProduit) contrat.getProduit())) {
		  switch ((Feve)contrat.getProduit()) {
		  case F_BQ:
			if (contrat.getEcheancier().getQuantiteTotale()<super.getStockBas().getQuantiteTotale()) {
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
		  case F_MQ:
			  if (contrat.getEcheancier().getQuantiteTotale()<super.getStockMoy().getQuantiteTotale()) {
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
		  case F_HQ_BE : return null;
		  case F_MQ_BE : return null;
			  
		  }
		} else {
			return null;// on ne vend pas de ce produit
		}
		return null;
	}

	@Override
	public double propositionPrix(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.mescontrats.add(contrat);
		
	}

	@Override
	public Lot livrer(IProduit produi, double quantite, ExemplaireContratCadre contrat) {
	 switch ((Feve)produi) {
	 
	 case F_BQ:
		double livre = Math.min(super.getStockBas().getQuantiteTotale(), quantite);
		if (livre>0.0) {
			super.getStockBas().retirer(livre);
		}
		Lot lot = new Lot(produi);
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre); // cet exemple ne gere pas la peremption : la marchandise est consideree comme produite au step courant
		return lot;
	 case F_MQ:
		 double livr = Math.min(super.getStockMoy().getQuantiteTotale(), quantite);
			if (livr>0.0) {
				super.getStockMoy().retirer(livr);
			}
			Lot lot2 = new Lot(produi);
			lot2.ajouter(Filiere.LA_FILIERE.getEtape(), livr); // cet exemple ne gere pas la peremption : la marchandise est consideree comme produite au step courant
			return lot2;
	 	
	 }
	}

	@Override
	public boolean vend(IProduit produit) {
		// TODO Auto-generated method stub
		return false;
	}

}
