                                                                                                                                                                                                                                                package abstraction.eq6Transformateur3;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Transformateur3Vente extends Transformateur3Stocks  implements IVendeurContratCadre{
/**Nathan Salbego*/
	
	protected LinkedList<ExemplaireContratCadre> listeCC ;
	private SuperviseurVentesContratCadre superviseur ;

	
	/**Nathan Salbego*/
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
	/**Nathan Salbego*/
	protected double demandeTotStep (int step,Object choco) {
		double tot=0;
		for (int i=0;i<listeCC.size();i++) {
			if (listeCC.get(i).getProduit().equals(choco)) 
			tot+=listeCC.get(i).getQuantiteALivrerAuStep();
		}
		return tot;
	}
	
	
	@Override
	/**Nathan Salbego*/
	public boolean vend(IProduit produit) {
		if (produit instanceof ChocolatDeMarque) {
			return (((ChocolatDeMarque)produit).getMarque().equals("eco+ choco"))||((ChocolatDeMarque)produit).getMarque().equals("chokchoco")||((ChocolatDeMarque)produit).getMarque().equals("chokchoco bio")||((ChocolatDeMarque)produit).getMarque().equals("Choc");
		}
		return false;
	}

	@Override
	/**Nathan Salbego*/
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		Echeancier e=contrat.getEcheancier();
		for (int i=contrat.getEcheancier().getStepDebut();i<contrat.getEcheancier().getStepFin()+1;i++) {
			if(demandeTotStep(i,contrat.getProduit())>super.capTransMax/4) {
				super.journalVentes.ajouter("Proposition rejetée pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return null;
			}
			else {
			if (demandeTotStep(i,contrat.getProduit())+contrat.getEcheancier().getQuantite(i)>super.capTransMax/4) {
				e.set(i,super.capTransMax/4-demandeTotStep(i,contrat.getProduit()));
				super.journalVentes.ajouter("Proposition négociée pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				}
			else{
				super.journalVentes.ajouter("Proposition acceptée pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());}
			}
	}
		if (e.getQuantiteTotale()<100) {
			super.journalVentes.ajouter("Proposition rejetée pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
			return null;
		}
		else {
		return e;}
		}

	@Override
	/**Nathan Salbego*/
	public double propositionPrix(ExemplaireContratCadre contrat) {
		if (contrat.getProduit() instanceof ChocolatDeMarque) {
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("eco+ choco")) {
				super.journalVentes.ajouter("Proposition de prix: "+20000+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return 20000;
			}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco")) {
				super.journalVentes.ajouter("Proposition de prix: "+21000+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return 21000;
			}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco bio")) {
				super.journalVentes.ajouter("Proposition de prix: "+23000+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return 23000;
			}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("Choc")) {
				super.journalVentes.ajouter("Proposition de prix: "+25000+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return 25000;
			}
		}
		super.journalVentes.ajouter("Proposition de prix: "+0+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
		return 0;
	}

	@Override
	/**Nathan Salbego*/
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getProduit() instanceof ChocolatDeMarque) {
			double i=(Math.random()/10);
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("eco+ choco")) {
				if(contrat.getPrix()<18000) {
					super.journalVentes.ajouter("Contre proposition de prix: "+2000*(1-i)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return 20000*(1-i);
				}
				else {super.journalVentes.ajouter("Contre proposition de prix: "+Math.min(contrat.getPrix()*(1+i), 20000)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return Math.min(contrat.getPrix()*(1+i), 20000);
			}}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco")) {
				if(contrat.getPrix()<18900) {
					super.journalVentes.ajouter("Contre proposition de prix: "+21000*(1-i)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return 21000*(1-i);
				}
				else {super.journalVentes.ajouter("Contre proposition de prix: "+Math.min(contrat.getPrix()*(1+i), 21000)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return Math.min(contrat.getPrix()*(1+i), 21000);
			}}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco bio")) {
				if(contrat.getPrix()<20700) {
					super.journalVentes.ajouter("Contre proposition de prix: "+23000*(1-i)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return 23000*(1-i);
				}
				else {super.journalVentes.ajouter("Contre proposition de prix: "+Math.min(contrat.getPrix()*(1+i), 20300)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return Math.min(contrat.getPrix()*(1+i), 23000);
			}}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("Choc")) {
				if(contrat.getPrix()<22500) {
					super.journalVentes.ajouter("Contre proposition de prix: "+25000*(1-i)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return 25000*(1-i);
				}
				else {super.journalVentes.ajouter("Contre proposition de prix: "+Math.min(contrat.getPrix()*(1+i), 25000)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return Math.min(contrat.getPrix()*(1+i), 25000);
			}}
		}
		super.journalVentes.ajouter("Contre proposition de prix: "+20000+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
		return 20000;
	}

	@Override
	/**Nathan Salbego*/
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		if (contrat.getVendeur()==this) {
		this.listeCC.add(contrat);
		super.journalVentes.ajouter("Nouveau contrat de vente passé :"+contrat.toString());}
		
	}
	 
<<<<<<< HEAD
	/**Nathan Salbego*/
=======
	
>>>>>>> branch 'main' of https://github.com/noikitu/CACAO-2023
	public void initialiser() {
		super.initialiser();
		this.superviseur = (SuperviseurVentesContratCadre)Filiere.LA_FILIERE.getActeur("Sup.CCadre");
	}
<<<<<<< HEAD
	/**Nathan Salbego*/
=======
>>>>>>> branch 'main' of https://github.com/noikitu/CACAO-2023
	public void rechercheContrat(IProduit produit) {
		if (superviseur != null) {
		List<IAcheteurContratCadre> acheteurs = superviseur.getAcheteurs(produit);
		if (acheteurs.size()!=0) {
			Collections.shuffle(acheteurs);
			for (IAcheteurContratCadre acheteur : acheteurs) {
				if (acheteur!=Filiere.LA_FILIERE.getActeur("ecochoco")) {
			super.journalVentes.ajouter("on essaie de demander un contrat à l'equipe :"+acheteur.getNom());
			ExemplaireContratCadre contrat = superviseur.demandeVendeur(acheteur, this, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1,Filiere.LA_FILIERE.getEtape()+9,100.0), super.cryptogramme, false);
			if (contrat != null) {super.journalVentes.ajouter("CC cherché et trouvé :"+contrat.toString());
									this.listeCC.add(contrat);
									}}}}}
		
	}
	/**Nathan Salbego*/
	public void next() {
		super.next();
		List<ExemplaireContratCadre> contratsObsoletes=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.listeCC) {
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				contratsObsoletes.add(contrat);
			}
		}
		this.listeCC.removeAll(contratsObsoletes);
		if ((this.stockChocolatBG.getQuantiteTotale()>5000 ))
		{this.rechercheContrat(super.chocosProduits.get(0));}
		if ((this.stockChocolatMG.getQuantiteTotale()>5000))
		{this.rechercheContrat(super.chocosProduits.get(1));}
		if ((this.stockChocolatMGL.getQuantiteTotale()>5000))
		{this.rechercheContrat(super.chocosProduits.get(2));}
		if ((this.stockChocolatHGL.getQuantiteTotale()>5000)) {
		{this.rechercheContrat(super.chocosProduits.get(3));}}
	}

	@Override
	/**Nathan Salbego*/
	public Lot livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		super.journalVentes.ajouter("Stock HGL="+this.stockChocolatHGL.getQuantiteTotale());
		super.journalVentes.ajouter("Stock MGL="+this.stockChocolatMGL.getQuantiteTotale());
		super.journalVentes.ajouter("Stock MG="+this.stockChocolatMG.getQuantiteTotale());
		super.journalVentes.ajouter("Stock BG="+this.stockChocolatBG.getQuantiteTotale());
		Lot lot = new Lot(produit);
		if (super.getLotChocolat(produit)!=null) {
		double livre = Math.min(super.getLotChocolat(produit).getQuantiteTotale(), quantite);
		super.journalVentes.ajouter("On livre : "+livre+"de : "+((ChocolatDeMarque)produit).getMarque());
		if (livre>0.0) {
<<<<<<< HEAD
			//if (super.estUnDeNosChoco((ChocolatDeMarque)produit)){
			double res = super.retirerChocolat((ChocolatDeMarque)produit, livre);//Attention il faut que cela soit possible; verifier la quantité
		
		if (res>0) {
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), res); }
=======
			super.retirerChocolat((ChocolatDeMarque)produit, livre);//Attention il faut que cela soit possible; verifier la quantité
		
		
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre); 
>>>>>>> branch 'main' of https://github.com/noikitu/CACAO-2023
		return lot;}
		else {
			super.journalVentes.ajouter("On ne livre pas");
		}}
		return lot;
	}
	/**ecrit par Nathan Claeys
	   * pour pouvoir rendre les variables qui peuvent aider à la prise de decision
	   */
	  public List<Variable> getIndicateurs() {
			List<Variable> res = super.getIndicateurs();
			return res;}
}
