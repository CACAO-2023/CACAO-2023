                                                                                                                                                                                                                                                package abstraction.eq6Transformateur3;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Transformateur3Vente extends Transformateur3Stocks  implements IVendeurContratCadre{
/**Nathan Salbego*/
	
	protected LinkedList<ExemplaireContratCadre> listeCC ;

	
	public Transformateur3Vente() {
		super();
		listeCC=new LinkedList<ExemplaireContratCadre>();
	}
	/**Cette fontion doit rendre la quantite de chocolat d'un type que nous devons avoir pour le vendre au step step
	 * 
	 * @param step
	 * @param choco
	 * @return
	 */
	protected double demandeTotStep (int step,Object choco) {
		double tot=0;
		for (int i=0;i<listeCC.size();i++) {
			if (listeCC.get(i).getProduit().equals(choco)) 
			tot+=listeCC.get(i).getQuantiteALivrerAuStep();
		}
		if (tot == 0){return 100;}
		else{return tot;}
	}

	@Override
	public boolean vend(IProduit produit) {
		return true;
	}

	@Override
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		Echeancier e= contrat.getEcheancier();;
		double quantite=e.getQuantiteTotale()/e.getNbEcheances();
		if (vend((IProduit)contrat.getProduit())) {
			e.ajouter(quantite);
			e.ajouter(quantite);
			return e;
			}
		
		return null;
	}

	@Override
	public double propositionPrix(ExemplaireContratCadre contrat) {
		if (contrat.getProduit() instanceof ChocolatDeMarque) {
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("eco+ choco")) {
				return 2000;
			}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco")) {
				return 2100;
			}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco bio")) {
				return 2300;
			}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("Choc")) {
				return 2500;
			}
		}
		return 2000;
	}

	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getProduit() instanceof ChocolatDeMarque) {
			double i=(10+Math.random()/10);
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("eco+ choco")) {
				if(contrat.getPrix()<1800) {
					return 2000*(1-i);
				}
				else {return Math.min(contrat.getPrix()*(1+i), 2000);
			}}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco")) {
				if(contrat.getPrix()<1890) {
					return 2100*(1-i);
				}
				else {return Math.min(contrat.getPrix()*(1+i), 2100);
			}}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco bio")) {
				if(contrat.getPrix()<2070) {
					return 2300*(1-i);
				}
				else {return Math.min(contrat.getPrix()*(1+i), 2300);
			}}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("Choc")) {
				if(contrat.getPrix()<2250) {
					return 2500*(1-i);
				}
				else {return Math.min(contrat.getPrix()*(1+i), 2500);
			}}
		}
		return 2000;
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		super.journal.ajouter("Nouveau contrat cadre : Prix = "+ contrat.getPrix()+", Quantite totale = "+contrat.getQuantiteTotale()+", Nb de steps : "+contrat.getEcheancier().getNbEcheances());
		this.listeCC.add(contrat);
		
	}
	
	
	public void next() {
		super.next();
		List<ExemplaireContratCadre> contratsObsoletes=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.listeCC) {
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				contratsObsoletes.add(contrat);
			}
		}
		this.listeCC.removeAll(contratsObsoletes);
	}

	@Override
	public Lot livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		if (super.getLotChocolat(produit)!=null) {
		double livre = Math.min(super.getLotChocolat(produit).getQuantiteTotale(), quantite);
		if (livre>0.0) {
			super.retirerChocolat((ChocolatDeMarque)produit, livre);
		}
		Lot lot = new Lot(produit);
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre); 
		return lot;}
		else {
			Lot l=new Lot(produit);
			return l; 
		}
	}
}
