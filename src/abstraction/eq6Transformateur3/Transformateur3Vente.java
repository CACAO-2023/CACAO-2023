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
		int t=6;
		Echeancier e=contrat.getEcheancier();
		for (int i=contrat.getEcheancier().getStepDebut();i<contrat.getEcheancier().getStepFin()+1;i++) {
			if(demandeTotStep(i,contrat.getProduit())>super.capTransMax/t) {
				super.journalVentes.ajouter("Proposition rejetée pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return null;
			}
			else {
			if (demandeTotStep(i,contrat.getProduit())+contrat.getEcheancier().getQuantite(i)>super.capTransMax/t) {
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
		int a=1800;
		int b=3300;
		int c=3600;
		int d=4000;
		if (contrat.getProduit() instanceof ChocolatDeMarque) {
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("eco+ choco")) {
				super.journalVentes.ajouter("Proposition de prix: "+a+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return a;
			}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco")) {
				super.journalVentes.ajouter("Proposition de prix: "+b+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return b;
			}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco bio")) {
				super.journalVentes.ajouter("Proposition de prix: "+c+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return c;
			}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("Choc")) {
				super.journalVentes.ajouter("Proposition de prix: "+d+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
				return d;
			}
		}
		super.journalVentes.ajouter("Proposition de prix: "+0+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
		return 0;
	}

	@Override
	/**Nathan Salbego*/
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		int a=1800;
		int b=3300;
		int c=3600;
		int d=4000;
		if (contrat.getProduit() instanceof ChocolatDeMarque) {
			double i=(Math.random()/10);
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("eco+ choco")) {
				if(contrat.getPrix()<(a*0.9)) {
					super.journalVentes.ajouter("Contre proposition de prix: "+a*(1-i)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return a*(1-i);
				}
				else {super.journalVentes.ajouter("Contre proposition de prix: "+Math.min(contrat.getPrix()*(1+i), a)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return Math.min(contrat.getPrix()*(1+i), a);
			}}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco")) {
				if(contrat.getPrix()<(b*0.9)) {
					super.journalVentes.ajouter("Contre proposition de prix: "+b*(1-i)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return b*(1-i);
				}
				else {super.journalVentes.ajouter("Contre proposition de prix: "+Math.min(contrat.getPrix()*(1+i), b)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return Math.min(contrat.getPrix()*(1+i), b);
			}}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("chokchoco bio")) {
				if(contrat.getPrix()<(c*0.9)) {
					super.journalVentes.ajouter("Contre proposition de prix: "+c*(1-i)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return c*(1-i);
				}
				else {super.journalVentes.ajouter("Contre proposition de prix: "+Math.min(contrat.getPrix()*(1+i), c)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return Math.min(contrat.getPrix()*(1+i), c);
			}}
			if (((ChocolatDeMarque)contrat.getProduit()).getMarque().equals("Choc")) {
				if(contrat.getPrix()<(d*0.9)) {
					super.journalVentes.ajouter("Contre proposition de prix: "+d*(1-i)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return d*(1-i);
				}
				else {super.journalVentes.ajouter("Contre proposition de prix: "+Math.min(contrat.getPrix()*(1+i), d)+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
					return Math.min(contrat.getPrix()*(1+i), d);
			}}
		}
		super.journalVentes.ajouter("Contre proposition de prix: "+2500+" pour :"+((ChocolatDeMarque)contrat.getProduit()).getMarque());
		return 2500;
	}

	@Override
	/**Nathan Salbego*/
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		if (contrat.getVendeur()==this) {
		this.listeCC.add(contrat);
		super.journalVentes.ajouter("Nouveau contrat de vente passé :"+contrat.toString());}
		
	}
	 
	/**Nathan Salbego*/
	public void initialiser() {
		super.initialiser();
		this.superviseur = (SuperviseurVentesContratCadre)Filiere.LA_FILIERE.getActeur("Sup.CCadre");
	}
	/**Nathan Salbego*/
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
		if (livre==super.getLotChocolat(produit).getQuantiteTotale()) {
			super.journalVentes.ajouter("On livre seulement : "+livre+"de : "+((ChocolatDeMarque)produit).getMarque());
		}
		else {super.journalVentes.ajouter("On livre : "+livre+"de : "+((ChocolatDeMarque)produit).getMarque());
		}
		if (livre>0.0) {
			double res = super.retirerChocolat((ChocolatDeMarque)produit, livre);//Attention il faut que cela soit possible; verifier la quantité
		
		if (res>0) {
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), res); }
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
