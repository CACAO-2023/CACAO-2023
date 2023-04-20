package abstraction.eq7Distributeur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.ContratCadre;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Distributeur1ContratCadre extends Distributeur1Acteur implements IAcheteurContratCadre {
	protected SuperviseurVentesContratCadre superviseurVentesCC;
	protected List<ExemplaireContratCadre> mesContrats;
	private double minNego = 5;
	
	public void initialiser() {
		super.initialiser();
		this.superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
		mesContrats = new LinkedList<ExemplaireContratCadre>();
	}
	
	
	/**
	 * @author Theo
	 * @return echeancier sur 1 an, base sur les previsions de ventes
	 */
	//A COMPLETER POUR PRENDRE EN COMPTE VRAIES PREVISIONS PERSO
	public Echeancier echeancier_strat(int stepDebut, ChocolatDeMarque marque) {
		Echeancier e = new Echeancier(stepDebut);
		for (int etape = stepDebut+1; etape<stepDebut+25; etape++) {
			int etapemod = etape%24;
			e.ajouter(previsions.get(etapemod).get(marque));
		}
		return e;
	}
	
	/**
	 * 
	 * @author Theo
	 * @return la qte d'un produit devant se faire livrer dans l'annee prochaine (en supposant que la duree d'un CC <= 1 an
	 */
	public double getLivraison(IProduit produit) {
		double somme = 0;
		for (ExemplaireContratCadre contrat : mesContrats) {
			if (contrat.getProduit() == produit) {
				somme += contrat.getQuantiteRestantALivrer();
			}
		}
		return somme;
	}
	
	public double getLivraisonEtape(IProduit produit) {
		double somme = 0;
		for (ExemplaireContratCadre contrat : mesContrats) {
			if (contrat.getProduit() == produit) {
				somme += contrat.getQuantiteALivrerAuStep();
			}
		}
		return somme;
	}
	
	/**
     * 	enleve les contrats obsolete (nous pourrions vouloir les conserver pour "archive"...)
     * @author Ghaly sentissi
     */
	public void enleve_contrats_obsolete() {
		List<ExemplaireContratCadre> contratsObsoletes=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.mesContrats) {
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				contratsObsoletes.add(contrat);
			}
		}
		this.mesContrats.removeAll(contratsObsoletes);
	}

    /**   
     * @author Theo & Ghaly sentissi
     */
	public void next() {
		super.next();
		enleve_contrats_obsolete();
		
		//On va regarder si on a besoin d'un nouveau contrat cadre pour chaque marque
		if (this.superviseurVentesCC!=null) {
			int etape = Filiere.LA_FILIERE.getEtape();
			for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
				double previsionannee = 0;
				for (int numetape = etape+1; numetape < etape+25 ; numetape++ ) {
					previsionannee += previsions.get(numetape%24).get(marque);
				}
				if (previsionannee > stockChocoMarque.get(marque)+getLivraison(marque)) { //On lance un CC seulement si notre stock n'est pas suffisant sur l'annee qui suit
					Echeancier echeancier = echeancier_strat(etape+1,marque);
					for (IVendeurContratCadre vendeur : superviseurVentesCC.getVendeurs(marque)){
						ExemplaireContratCadre contrat = superviseurVentesCC.demandeAcheteur(this, vendeur, marque, echeancier, this.cryptogramme, false);
						if (contrat != null) {
							journal.ajouter("Nouveau contrat avec "+contrat.getVendeur()+" pour le produit "+contrat.getProduit());
							break;
						}
					}
				}
				
			}
		}
		
	}
	
	@Override
	/**
	 * @author Theo
	 */
	// A COMPLETER SI ASSEZ DE STOCK (appele si cc initie par vendeur)
	public boolean achete(IProduit produit) {
		if (produit instanceof ChocolatDeMarque) {
			return true;
		}
		return false;
	}

	@Override
	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		return 0;
	}
	
	/**   
	 * proposition d'un contrat a un des vendeurs choisi aleatoirement
     * @author Ghaly sentissi
     */
	public void proposition_achat_aleatoire(IProduit produit,Echeancier e) {
		SuperviseurVentesContratCadre superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre")); 

		journal_achat.ajouter("Recherche d'un vendeur aupres de qui acheter");
		List<IVendeurContratCadre> vendeurs = superviseurVentesCC.getVendeurs(produit);
		if (vendeurs.contains(this)) {
			vendeurs.remove(this);
		}
		IVendeurContratCadre vendeur = null;
		if (vendeurs.size()==1) {
			vendeur=vendeurs.get(0);
		} else if (vendeurs.size()>1) {
			vendeur = vendeurs.get((int)( Math.random()*vendeurs.size()));
		}
		if (vendeur!=null) {
			
			getContractForProduct(produit,e,vendeur);
	}}
    /**
     * Cette méthode va essayer de lancer un contrat cadre d'un produit avec un acteur donné
     * @param produit le produit qu'on veut vendre
     * @param acteur l'acteur à qui on essaye de vendre
     * @return le contrat s'il existe, sinon null
     * @author Ghaly sentissi
     */
    public ExemplaireContratCadre getContractForProduct(IProduit produit,Echeancier e,  IActeur acteur) {
        // First we need to select a buyer for the product
        this.journal_achat.ajouter(Color.LIGHT_GRAY, Color.BLACK, "Recherche acheteur pour " + produit + "...");

        // Now making the contract
        this.journal_achat.ajouter(Color.LIGHT_GRAY, Color.BLACK, "Tentative de négociation de contrat cadre avec " + acteur.getNom() + " pour " + produit + "...");
        int length = ((int) Math.round(Math.random() * 10)) + 1;
    	SuperviseurVentesContratCadre superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre")); 

		ExemplaireContratCadre cc = superviseurVentesCC.demandeAcheteur((IAcheteurContratCadre)this, ((IVendeurContratCadre)acteur), produit, e, cryptogramme,false);
        if (cc != null) {
            this.journal_achat.ajouter(Color.LIGHT_GRAY, Color.BLACK, "Contrat cadre passé avec " + acteur.getNom() + " pour " + produit + "\nDétails : " + cc + "!");
        } else {
            this.journal_achat.ajouter(Color.LIGHT_GRAY, Color.BLACK, "Echec de la négociation de contrat cadre avec " + acteur.getNom() + " pour " + produit + "...");
        }
        return cc;
    }
	
	@Override
	/**
	 * @author Ghaly & Theo
	 */
	// A COMPLETER
	// RAJOUTER CONDITION ANNULATION NEGO
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (Math.random()<0.3) {
			return contrat.getEcheancier(); // on ne cherche pas a negocier sur le previsionnel de livraison
		} else {//dans 70% des cas on fait une contreproposition pour l'echeancier
			Echeancier e = contrat.getEcheancier();
			int stepdebut = e.getStepDebut();
			for (int step = stepdebut; step < e.getStepFin()+1; step++) {
				e.set(step, e.getQuantite(step)*0.9);
			}
			return e;
		}
	}

	@Override
	/**
	 * @author Ghaly & Theo
	 */
	// A COMPLETER EN FONCTION DE LA METHODE COUT
	// RAJOUTER CONDITION ANNULATION NEGO
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		if (Math.random()<0.3) {
			return contrat.getPrix(); // on ne cherche pas a negocier dans 30% des cas
		} else {//dans 70% des cas on fait une contreproposition differente
			return contrat.getPrix()*0.95;// 5% de moins.
		}
	}

	@Override
	/**
	 * @author Ghaly
	 */
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		journal.ajouter("Les negociations avec "+ contrat.getVendeur().getNom()+" ont abouti à un contrat cadre de "+contrat.getProduit().toString());
	}

	@Override
	/**
	 * @author Theo
	 */
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		IProduit produit= lot.getProduit();
		double quantite = lot.getQuantiteTotale();
		if (produit instanceof ChocolatDeMarque) {
			if (this.stockChocoMarque.keySet().contains(produit)) {
				this.stockChocoMarque.put((ChocolatDeMarque)produit, this.stockChocoMarque.get(produit)+quantite);
			} else {
				this.stockChocoMarque.put((ChocolatDeMarque)produit, quantite);
			}
			this.totalStocks.ajouter(this, quantite, this.cryptogramme);
			this.journal.ajouter("Reception de "+quantite+" T de "+produit+". Stock->  "+this.stockChocoMarque.get(produit));
		}
	}
	
	/**
     * retourne l'étape de négociation en cours
     * @param contrat     
     * @author Ghaly sentissi
     */
	public int step_nego (ExemplaireContratCadre contrat) {
		return contrat.getListePrix().size()/2;
	}	

}
