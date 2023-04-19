package abstraction.eq1Producteur1;

import abstraction.eqXRomu.contratsCadres.ContratCadre;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
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
			if (contrat.getEcheancier().getQuantiteTotale()<super.getVraiStockB().getQuantiteTotale()) {
				Echeancier e = contrat.getEcheancier();
				if (e.getQuantite(e.getStepDebut())>super.getVraiStockB().getQuantiteTotale()/5) { //Si la quantité demandé au premier step est inferieur au cinquieme de notre stock on negocie
				    return contrat.getEcheancier(); // on ne cherche pas a negocier sur le previsionnel de livraison
				} else {
					e.set(e.getStepDebut(), e.getQuantite(e.getStepDebut())*2);// on livre plus lors de la 1ere livraison... 
					return e;
				}
			}else {
				return null; // On s'engage pas si on a pas la quantité demandé
			}
		  case F_MQ:
			  if (contrat.getEcheancier().getQuantiteTotale()<super.getVraiStockM().getQuantiteTotale()) {
				  Echeancier e = contrat.getEcheancier();
					if (e.getQuantite(e.getStepDebut())>super.getVraiStockM().getQuantiteTotale()/10) { //Si la quantité demandé au premier step est inferieur au dixieme de notre stock on negocie
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
		if((Feve)c.getProduit()==Feve.F_BQ) {		
			p= 1.1*c.getQuantiteTotale();
		if((Feve)c.getProduit()==Feve.F_MQ)
			p= 1.3*c.getQuantiteTotale();
		}
		return p;
		
		
	}

	
	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre c) {
		
		
		double p= (c.getPrix()+ propositionPrix(c))/2;		
		if (c.getPrix()>=propositionPrix(c)) {
			return c.getPrix();
		}
		else {
		
		if (p>0.75*propositionPrix(c)) {
		return p;
		}else {
			return propositionPrix(c)*Math.random()+ propositionPrix(c);
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
		double livraisonBQ = Math.min(super.getVraiStockB().getQuantiteTotale(), quantite);		
		Lot lot = new Lot(produi);
		lot.ajouter(Filiere.LA_FILIERE.getEtape(), livraisonBQ); 
		this.journal_ventes.ajouter("Livraison de "+ livraisonBQ +" bas de gamme pour "+ contrat.getAcheteur());
		return lot;
	 case F_MQ:
		 double livraisonMQ = Math.min(super.getVraiStockM().getQuantiteTotale(), quantite);
		 Lot lot2 = new Lot(produi);
		 lot2.ajouter(Filiere.LA_FILIERE.getEtape(), livraisonMQ); 
		 this.journal_ventes.ajouter("Livraison de "+ livraisonMQ +" moyen de gamme pour "+ contrat.getAcheteur());
		 return lot2;
	 case F_HQ_BE : return null;
	 case F_MQ_BE : return null;	
	 }
	return null;
	}
	public void next() {
		super.next();
		List<ExemplaireContratCadre> contratstermine=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.mescontrats) {
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				contratstermine.add(contrat);
			}
		}
		this.mescontrats.removeAll(contratstermine);
		this.PropositionVendeur(Feve.F_BQ);
		this.PropositionVendeur(Feve.F_MQ);
		this.journal_ventes.ajouter("Nos contrats à l'étape "+ Filiere.LA_FILIERE.getEtape()+"sont "+ this.mescontrats);
	}

	@Override
	public boolean vend(IProduit produit) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public ExemplaireContratCadre PropositionVendeur(IProduit produit){
		
		IAcheteurContratCadre client=supCCadre.getAcheteurs(produit).get((int)supCCadre.getAcheteurs(produit).size()-1);
		Echeancier e= new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, 1000);
		this.journal_ventes.ajouter("Client potentiel contrat cadre "+ this.getNom());
		this.journal_ventes.ajouter("Négociation avec "+client.getNom()+ " pour " + produit);
		ExemplaireContratCadre c=supCCadre.demandeVendeur(client, this, produit, e, cryptogramme, false);
		if (c != null) {
			this.journal_ventes.ajouter("Début contrat cadre avec "+client +"pour" + produit +c);
		}
		return c;
		
	}
	

}
