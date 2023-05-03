                                                                                                                                                                                                                                                package abstraction.eq6Transformateur3;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;
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
		return tot;
	}

	@Override
	public boolean vend(IProduit produit) {
		if (produit instanceof ChocolatDeMarque) {
			return (((ChocolatDeMarque)produit).getMarque().equals("eco+ choco"))||((ChocolatDeMarque)produit).getMarque().equals("chokchoco")||((ChocolatDeMarque)produit).getMarque().equals("chokchoco bio")||((ChocolatDeMarque)produit).getMarque().equals("Choc");
		}
		return false;
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
				super.journalVentes.ajouter("Proposition de prix: "+2+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return 2;
			}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco")) {
				super.journalVentes.ajouter("Proposition de prix: "+2+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return 2;
			}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco bio")) {
				super.journalVentes.ajouter("Proposition de prix: "+2+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return 2;
			}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("Choc")) {
				super.journalVentes.ajouter("Proposition de prix: "+2+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return 2;
			}
		}
		super.journalVentes.ajouter("Proposition de prix: "+0+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
		return 0;
	}

	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getProduit() instanceof ChocolatDeMarque) {
			double i=(Math.random()/10);
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("eco+ choco")) {
				if(contrat.getPrix()<1800) {
					super.journalVentes.ajouter("Contre proposition de prix: "+2000*(1-i)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return 2000*(1-i);
				}
				else {super.journalVentes.ajouter("Contre proposition de prix: "+Math.min(contrat.getPrix()*(1+i), 2000)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return Math.min(contrat.getPrix()*(1+i), 2000);
			}}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco")) {
				if(contrat.getPrix()<1890) {
					super.journalVentes.ajouter("Contre proposition de prix: "+2100*(1-i)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return 2100*(1-i);
				}
				else {super.journalVentes.ajouter("Contre proposition de prix: "+Math.min(contrat.getPrix()*(1+i), 2100)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return Math.min(contrat.getPrix()*(1+i), 2100);
			}}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco bio")) {
				if(contrat.getPrix()<2070) {
					super.journalVentes.ajouter("Contre proposition de prix: "+2300*(1-i)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return 2300*(1-i);
				}
				else {super.journalVentes.ajouter("Contre proposition de prix: "+Math.min(contrat.getPrix()*(1+i), 2300)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return Math.min(contrat.getPrix()*(1+i), 2300);
			}}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("Choc")) {
				if(contrat.getPrix()<2250) {
					super.journalVentes.ajouter("Contre proposition de prix: "+2500*(1-i)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return 2500*(1-i);
				}
				else {super.journalVentes.ajouter("Contre proposition de prix: "+Math.min(contrat.getPrix()*(1+i), 2500)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return Math.min(contrat.getPrix()*(1+i), 2500);
			}}
		}
		super.journalVentes.ajouter("Contre proposition de prix: "+2000+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
		return 2000;
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.listeCC.add(contrat);
		super.journalVentes.ajouter("Nouveau contrat de vente passé :"+contrat.toString());
		
	}
	
	public void initialiser() {
		super.initialiser();
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
		super.journalVentes.ajouter("On livre : "+livre+"de : "+produit.getType());
		if (livre>0.0) {
			super.retirerChocolat((ChocolatDeMarque)produit, livre);//Attention il faut que cela soit possible; verifier la quantité
		}
		Lot lot = new Lot(produit);
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre); 
		return lot;}
		else {
			Lot l=new Lot(produit);
			return l; 
		}

	}
	/**ecrit par Nathan Claeys
	   * pour pouvoir rendre les variables qui peuvent aider à la prise de decision
	   */
	  public List<Variable> getIndicateurs() {
			List<Variable> res = super.getIndicateurs();
			return res;}
}
