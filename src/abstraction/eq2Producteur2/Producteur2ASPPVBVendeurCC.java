package abstraction.eq2Producteur2;

//Code ecrit par Nino

import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Producteur2ASPPVBVendeurCC extends Producteur2ASPPVendeurBourse implements IVendeurContratCadre{

	public Producteur2ASPPVBVendeurCC() {
		super();
	}
	
	/**
	 * Methode appelee par le superviseur afin d'efefctuer les actions de l'acteur
	 * a chaque etape. Ici on decide si on propose aux acheteurs un contrat cadre.
	 */
	public void next() {
		SuperviseurVentesContratCadre sup = ((SuperviseurVentesContratCadre) Filiere.LA_FILIERE.getActeur("Sup."+"CCadre"));
		List<IAcheteurContratCadre> acheteurs = sup.getAcheteurs(Feve.F_MQ_BE);
		for(IAcheteurContratCadre ach : acheteurs) {
			if(this.getStockTot(Feve.F_MQ_BE).getValeur() >= 10) {
				double prod = 1; //production previsionnelle moyenne des 12 steps à venir
				Echeancier ech = new Echeancier(Filiere.LA_FILIERE.getEtape(), 12, this.getStockTot(Feve.F_MQ_BE).getValeur()/12 + prod);
				ExemplaireContratCadre ex = sup.demandeVendeur(ach, this, Feve.F_MQ_BE, ech, this.cryptogramme, false);
				if(ex != null) {
					this.getContrats().add(ex);
				}
			}
			if(this.getStockTot(Feve.F_HQ_BE).getValeur() >= 10) {
				double prod = 1; //production previsionnelle moyenne des 12 steps à venir
				Echeancier ech = new Echeancier(Filiere.LA_FILIERE.getEtape(), 12, this.getStockTot(Feve.F_HQ_BE).getValeur()/12 + prod);
				ExemplaireContratCadre ex = sup.demandeVendeur(ach, this, Feve.F_MQ_BE, ech, this.cryptogramme, false);
				if(ex != null) {
					this.getContrats().add(ex);
				}
			}
			if(this.getStockTot(Feve.F_MQ).getValeur() >= 10) {
				double prod = 1; //production previsionnelle moyenne des 12 steps à venir
				Echeancier ech = new Echeancier(Filiere.LA_FILIERE.getEtape(), 12, this.getStockTot(Feve.F_MQ).getValeur()/12 + prod);
				ExemplaireContratCadre ex = sup.demandeVendeur(ach, this, Feve.F_MQ, ech, this.cryptogramme, false);
				if(ex != null) {
					this.getContrats().add(ex);
				}
			}
			
		}
		
		super.next();
	}
	
	/**
	 * @param produit, produit!=nul
	 * @return Retourne true si l'acteur peut durant la simulation (pas necessairement a cette etape)
	 * vendre le type de produit precise en parametre (retourne false si il s'agit d'un produit que
	 * l'acteur ne vendra pas durant la simulation.
	 */
	public boolean peutVendre(IProduit produit) {
		return produit instanceof Feve && produit == Feve.F_MQ && produit == Feve.F_MQ_BE && produit == Feve.F_HQ_BE; //Est-ce qu'on vend vraiment de tout ?
	}
	
	/**
	 * Methode appelee par le superviseur afin de savoir si l'acheteur
	 * est pret a faire un contrat cadre sur le produit indique.
	 * @param produit
	 * @return Retourne false si le vendeur ne souhaite pas etablir de contrat 
	 * a cette etape pour ce type de produit (retourne true si il est pret a
	 * negocier un contrat cadre pour ce type de produit).
	 */
	public boolean vend(IProduit produit) {
		boolean testQuantite = true;  /*Implementer dans le IF une condition sur la quantite minimale produite chaque step*/
		if(produit instanceof Feve && ((Feve) produit).isBioEquitable() && testQuantite) {
			return true;			
		}
		return false;
	}
	
	/**
	 * Methode appelee par le SuperviseurVentesContratCadre lors de la phase de negociation
	 * sur l'echeancier afin de connaitre la contreproposition du vendeur. Le vendeur
	 * peut connaitre les precedentes propositions d'echeanciers via un appel a la methode
	 * getEcheanciers() sur le contrat. Un appel a getEcheancier() sur le contrat retourne 
	 * le dernier echeancier que l'acheteur a propose.
	 * On renvoie toujours  un Echeancier constant dans le temps dans la limite de nos capacités de production.
	 * @param contrat
	 * @return Retourne null si le vendeur souhaite mettre fin aux negociations en renoncant a
	 * ce contrat. Retourne l'echeancier courant du contrat (contrat.getEcheancier()) si il est
	 * d'accord avec cet echeancier. Sinon, retourne un autre echeancier qui est une contreproposition.
	 */
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		Echeancier echeancierAch = contrat.getEcheancier();
		if(echeancierAch.getStepDebut() > Filiere.LA_FILIERE.getEtape()) {
			double testQuantite = 0.0;
			for(int i = echeancierAch.getStepDebut(); i<=echeancierAch.getStepFin(); i++) {
				if(contrat.getProduit() instanceof Feve && ((Feve) contrat.getProduit()).getGamme() == Gamme.HQ && ((Feve) contrat.getProduit()).isBioEquitable()) {
					testQuantite = testQuantite + this.getNbHecHauteBE().getValeur()*this.getProdHec().getValeur() - echeancierAch.getQuantite(i);
				}
				if(contrat.getProduit() instanceof Feve && ((Feve) contrat.getProduit()).getGamme() == Gamme.MQ && ((Feve) contrat.getProduit()).isBioEquitable()) {
					testQuantite = testQuantite + this.getNbHecHauteBE().getValeur()*this.getProdHec().getValeur() - echeancierAch.getQuantite(i);
				}
			}
			if(testQuantite < 0.0) {
				return new Echeancier(echeancierAch.getStepDebut(), echeancierAch.getStepFin(), (echeancierAch.getQuantiteTotale()+testQuantite)/echeancierAch.getNbEcheances());
			} 
			return echeancierAch;
		}
		return null;
	}
	
	/**
	 * Methode appele par le SuperviseurVentesContratCadre apres une negociation reussie
	 * sur l'echeancier afin de connaitre le prix a la tonne que le vendeur propose.
	 * @param contrat
	 * @return La proposition initale du prix a la tonne.
	 */
	public double propositionPrix(ExemplaireContratCadre contrat) {
		if(contrat.getProduit() == Feve.F_HQ_BE) {
			return contrat.getEcheancier().getQuantiteTotale()*this.getPrixHQ();
		}
		if(contrat.getProduit() == Feve.F_MQ_BE) {
			return contrat.getEcheancier().getQuantiteTotale()*this.getPrixMQBE();
		}
		return 0.0;		
	}
	
	/**
	 * Methode appelee par le SuperviseurVentesContratCadre apres une contreproposition
	 * de prix different de la part de l'acheteur, afin de connaitre la contreproposition
	 * de prix du vendeur.
	 * @param contrat
	 * @return Retourne un nombre inferieur ou egal a 0.0 si le vendeur souhaite mettre fin
	 * aux negociation en renoncant a ce contrat. Retourne le prix actuel a la tonne du 
	 * contrat (contrat.getPrix()) si le vendeur est d'accord avec ce prix.
	 * Sinon, retourne une contreproposition de prix.
	 */
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		if(contrat.getPrix() >= 1) {
			return contrat.getPrix();
		}
		if(contrat.getPrix() >= 0.9) {
			return contrat.getPrix()*0.25+contrat.getEcheancier().getQuantiteTotale()*this.getPrixHQ()*0.75; /*Négociation 1/4||3/4 pour tenter de tirer un prix convenable*/
		}
		return -2;
	}
	
	/**
	 * Methode appelee par le SuperviseurVentesContratCadre afin de notifier le
	 * vendeur de la reussite des negociations sur le contrat precise en parametre
	 * qui a ete initie par l'acheteur.
	 * Le superviseur veillera a l'application de ce contrat (des appels a livrer(...) 
	 * seront effectues lorsque le vendeur devra livrer afin d'honorer le contrat, et
	 * des transferts d'argent auront lieur lorsque l'acheteur paiera les echeances prevues)..
	 * @param contrat
	 */
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.getContrats().add(contrat);
	}
	
	/**
	 * Methode appelee par le SuperviseurVentesContratCadre lorsque le vendeur doit livrer 
	 * quantite tonnes de produit afin d'honorer le contrat precise en parametre. 
	 * @param produit
	 * @param quantite
	 * @param contrat
	 * @return Retourne un lot de quantite Tonnes du produit precise en parametre 
	 * Une penalite est prevue si la quantite du lot retourne est inferieure a la quantite
	 */
	public Lot livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		System.out.println(Math.min(this.getStockTot((Feve) produit).getValeur(), quantite));
		if(Math.min(this.getStockTot((Feve) produit).getValeur(), quantite) > 0.001) {
			return retirerStock((Feve) produit, Math.min(this.getStockTot((Feve) produit).getValeur(), quantite));
		} else {
			return new Lot(produit);
		}
	}
}
