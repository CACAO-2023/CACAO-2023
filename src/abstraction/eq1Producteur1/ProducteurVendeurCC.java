package abstraction.eq1Producteur1;

import abstraction.eqXRomu.contratsCadres.ContratCadre;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
// AYOUB
public class ProducteurVendeurCC extends Producteur1Plantation implements IVendeurContratCadre{
    private List<ExemplaireContratCadre> mescontrats;
    protected SuperviseurVentesContratCadre supCCadre;
	private HashMap<Feve, Integer> nego; 
	private HashMap<IActeur, Integer> systemefidelite;
	
	
	public ProducteurVendeurCC() {
		super();
		this.mescontrats= new LinkedList<ExemplaireContratCadre>();
		this.nego= new HashMap<Feve, Integer>();
		this.systemefidelite=new HashMap<IActeur, Integer>();
		
	}
	public void initialiser() {
		super.initialiser();
		this.supCCadre = (SuperviseurVentesContratCadre) (Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
		for (IActeur acteur : Filiere.LA_FILIERE.getActeurs()) {
			systemefidelite.put(acteur, 0);
		}
	}
	
	
	public boolean peutVendre(IProduit produit) {
		return produit instanceof Feve && (produit == Feve.F_BQ || produit == Feve.F_MQ);
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
	
	public double DeterminerFacteurPrix(int i, IActeur acteur) { //On tient compte du systeme de fidélité et de la difficulté des négociations
		double f=1;
		if (i>=3) {
			f=f-i/10;
		}
		int NbreDachat=this.systemefidelite.get(acteur);
		return f - NbreDachat/20;
	}

	@Override
	public double propositionPrix(ExemplaireContratCadre c) {
		double p= prixMinAvecMarge((Feve)c.getProduit(), 1)*1.3;
		if (this.systemefidelite.get(c.getAcheteur())>=10) {
			p=p*0.9;
		}
		return p;
	}
	

	
	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre c) {		
		if (!(this.nego.keySet().contains(c.getProduit()))) {
			this.nego.put((Feve)c.getProduit(), 0);}
		this.nego.put((Feve)c.getProduit(), this.nego.get(c.getProduit())+1);
        double fp=DeterminerFacteurPrix(this.nego.get(c.getProduit()), c.getAcheteur()); //Facteur prix
		double p= (c.getPrix()+ propositionPrix(c))/2;		
		if (fp*p<c.getPrix()){ 
			return c.getPrix();
		}
		
		else if (fp*p>super.prixMinAvecMarge((Feve)c.getProduit(), c.getQuantiteTotale())/c.getQuantiteTotale()) {
			return fp*p; 
		}
		else {
			return super.prixMinAvecMarge((Feve)c.getProduit(), c.getQuantiteTotale())/c.getQuantiteTotale();
		}
		
		
	}
	
	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.mescontrats.add(contrat);
		
	}
	
	
	@Override
	public Lot livrer(IProduit produi, double quantite, ExemplaireContratCadre contrat) {
	 switch ((Feve)produi) {
	 
	 case F_BQ:
		int oldeststepBQ = this.OldestStep(produi);
		int livraisonBQ = (int)Math.min(super.getVraiStockB().getQuantiteTotale(), quantite);		
		Lot lot = new Lot(produi);
		this.journal_ventes.ajouter("Livraison de "+ livraisonBQ +"tonnes de bas de gamme pour "+ contrat.getAcheteur());
		if (livraisonBQ>0) {
		super.getVraiStockB().retirer(livraisonBQ);
		lot.ajouter(livraisonBQ, oldeststepBQ);}
		return lot;
	 case F_MQ:
		 int oldeststepMQ = this.OldestStep(produi);
		 int livraisonMQ = (int)Math.min(super.getVraiStockM().getQuantiteTotale(), quantite);
		 Lot lot2 = new Lot(produi);
		 this.journal_ventes.ajouter("Livraison de "+ livraisonMQ +"tonnes de moyen de gamme pour "+ contrat.getAcheteur());
		 if (livraisonMQ>0) {
		 super.getVraiStockM().retirer(livraisonMQ);
		 lot2.ajouter(oldeststepMQ, livraisonMQ);}
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
		this.nego.clear();
		for (IActeur acteur : this.systemefidelite.keySet()){
			this.journal_fidelite.ajouter("L'acteur "+acteur.getNom()+ "a "+this.systemefidelite.get(acteur));
			
		}
	}

	@Override
	public boolean vend(IProduit produit) {
		return produit instanceof Feve && (produit == Feve.F_BQ || produit == Feve.F_MQ);
	}	
	
	public ExemplaireContratCadre PropositionVendeur(IProduit produit){
		int a=(int)(supCCadre.getAcheteurs(produit).size()*Math.random());		
		IAcheteurContratCadre client=supCCadre.getAcheteurs(produit).get(a);
		Echeancier e= new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, 1000);
		this.journal_ventes.ajouter("Client potentiel contrat cadre "+ client.getNom());
		this.journal_ventes.ajouter("Négociation avec "+client.getNom()+ " pour " + produit);
		ExemplaireContratCadre c=supCCadre.demandeVendeur(client, this, produit, e, cryptogramme, false);
		if (c != null) {
			this.journal_ventes.ajouter("Début contrat cadre avec "+client.getNom() +"pour" + produit +c);
			this.mescontrats.add(c);
			this.nego.put((Feve)c.getProduit(), 1);
			UpdateFidelite(c);
		}
		return c;
		
	}
	
	
	
	public void UpdateFidelite(ExemplaireContratCadre contrat) {
		IActeur acteur= contrat.getAcheteur();
		this.systemefidelite.put(acteur, this.systemefidelite.get(acteur)+1);
		
	}
	
	public int OldestStep(IProduit produit) {
	    double os = this.getVraiStockB().getQuantites().get(Filiere.LA_FILIERE.getEtape());
		if (produit==Feve.F_BQ) {
	    	for (int i : this.getVraiStockB().getQuantites().keySet()) {
	    		if (this.getVraiStockB().getQuantites().get(i)<os) {
	    			os=this.getVraiStockB().getQuantites().get(i);
	    		}
	    	}
	    }
		else if (produit==Feve.F_MQ) {
			for (int i: this.getVraiStockM().getQuantites().keySet()) {
				if (this.getVraiStockM().getQuantites().get(i)<os) {
					os=this.getVraiStockM().getQuantites().get(i);
				}
			}
		}
		return (int)os;
	}

}
