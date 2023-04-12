package abstraction.eq1Producteur1;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
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
    protected SuperviseurVentesContratCadre supCCadre;
	
	
	
	public ProducteurVendeurCC() {
		super();
		this.mescontrats= new LinkedList<ExemplaireContratCadre>();
	}
	public void initialiser() {
		super.initialiser();
		this.supCCadre = (SuperviseurVentesContratCadre) (Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}
	
	public boolean peutVendre(IProduit produit) {
		return (produit instanceof Feve) ;
	}
	

	//On doit revoir la stratégie de contreproposition(criteres, quantité max, quantité min ...)
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		if (this.peutVendre((IProduit) contrat.getProduit())) {
		  switch ((Feve)contrat.getProduit()) {
		  case F_BQ:
			if (contrat.getEcheancier().getQuantiteTotale()<super.getStockBas().getQuantiteTotale()) {
				Echeancier e = contrat.getEcheancier();
				if (e.getQuantite(e.getStepDebut())>super.getStockBas().getQuantiteTotale()/5) { //Si la quantité demandé au premier step est inferieur au cinquieme de notre stock on negocie
				    return contrat.getEcheancier(); // on ne cherche pas a negocier sur le previsionnel de livraison
				} else {
					e.set(e.getStepDebut(), e.getQuantite(e.getStepDebut())*2);// on livre plus lors de la 1ere livraison... 
					return e;
				}
			}else {
				return null; // On s'engage pas si on a pas la quantité demandé
			}
		  case F_MQ:
			  if (contrat.getEcheancier().getQuantiteTotale()<super.getStockMoy().getQuantiteTotale()) {
				  Echeancier e = contrat.getEcheancier();
					if (e.getQuantite(e.getStepDebut())>super.getStockBas().getQuantiteTotale()/10) { //Si la quantité demandé au premier step est inferieur au dixieme de notre stock on negocie
					    return contrat.getEcheancier(); 
					} else {
						e.set(e.getStepDebut(), e.getQuantite(e.getStepDebut())*2);
						return e;
					}
				} else {
					return null; // On s'engage pas si on a pas la quantité necessaire
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
	public double propositionPrix(ExemplaireContratCadre c) {
		double p=0;
		switch((Feve)c.getProduit()) {		
		case F_BQ:
			p= 1100*c.getQuantiteTotale();
		case F_MQ:
			p= 1300*c.getQuantiteTotale();
		case F_HQ_BE : p= 0;
		case F_MQ_BE : p= 0;
		}
		return p;
		
		
	}

	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre c) {
		if (c.getPrix()>propositionPrix(c)) {//s'ils sont genereux pourquoi pas :)
			return c.getPrix();
		}
		else {
		double p= (c.getPrix()+propositionPrix(c))/2;
		if (p>0.75*propositionPrix(c)) {
		return p;
		}else {
			return (p+propositionPrix(c))/2;
		}}
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
		Lot lot = new Lot(produi);
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), livre); // cet exemple ne gere pas la peremption : la marchandise est consideree comme produite au step courant
		return lot;
	 case F_MQ:
		 double livr = Math.min(super.getStockMoy().getQuantiteTotale(), quantite);
		 Lot lot2 = new Lot(produi);
		 lot2.ajouter(Filiere.LA_FILIERE.getEtape(), livr); // cet exemple ne gere pas la peremption : la marchandise est consideree comme produite au step courant
		 return lot2;
	 case F_HQ_BE : return null;
	 case F_MQ_BE : return null;	
	 }
	return null;
	}
	public void next() {
		List<ExemplaireContratCadre> contratstermine=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.mescontrats) {
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				contratstermine.add(contrat);
			}
		}
		this.mescontrats.removeAll(contratstermine);
	}

	@Override
	public boolean vend(IProduit produit) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
