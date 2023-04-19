package abstraction.eq2Producteur2;

import java.util.ArrayList;
import java.util.HashMap;

//Code ecrit par Nino

import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Producteur2ASPPVBVendeurCC extends Producteur2ASPPVendeurBourse implements IVendeurContratCadre{
	private HashMap<Feve, Integer> nbEchecVentePrix = new HashMap<Feve, Integer>(); //Permet de connaitre le nombre de vente ayant echoue à la suite 
	private HashMap<Feve, Boolean> echecVentePrix = new HashMap<Feve, Boolean>();  //Permet de savoir si la derniere venet a reussi pour chaque produit
	private int nbIterationVentePrix; //Compte le nombre d'appel à contrePropositionPrix pour faire évoluer le prix
	double facteurPrixInit = 1.75;
	double venteMin = 500;
	
	
	public Producteur2ASPPVBVendeurCC() {
		super();
	}
	
	public void initialiser() {
		super.initialiser();
		
		this.echecVentePrix.put(Feve.F_MQ_BE, false);
		this.echecVentePrix.put(Feve.F_HQ_BE, false);
		this.echecVentePrix.put(Feve.F_MQ, false);
		this.nbEchecVentePrix.put(Feve.F_MQ_BE, 0);
		this.nbEchecVentePrix.put(Feve.F_HQ_BE, 0);
		this.nbEchecVentePrix.put(Feve.F_MQ, 0);
	}
	
	/**
	 * Methode appelee par le superviseur afin d'efefctuer les actions de l'acteur
	 * a chaque etape. Ici on decide si on propose aux acheteurs un contrat cadre.
	 */
	public void next() {
		super.next();
		
		SuperviseurVentesContratCadre sup = ((SuperviseurVentesContratCadre) Filiere.LA_FILIERE.getActeur("Sup."+"CCadre"));
		List<IAcheteurContratCadre> acheteurs = sup.getAcheteurs(Feve.F_MQ_BE);
		HashMap<Integer, Double> hs = getStocksTotTheo(Feve.F_MQ_BE, Filiere.LA_FILIERE.getEtape() + 9);
		HashMap<Integer, Double> hs2 = getStocksTotTheo(Feve.F_MQ_BE, Filiere.LA_FILIERE.getEtape() + 9);
		for(IAcheteurContratCadre ach : acheteurs) {
			boolean testQuantite = true;
			for(int i = Filiere.LA_FILIERE.getEtape() +1; i<Filiere.LA_FILIERE.getEtape()+9; i++){
				if(hs.get(i)-(i-Filiere.LA_FILIERE.getEtape() +1)*this.venteMin < this.venteMin) {
					testQuantite = false;
				}
			}if(testQuantite) {
				Echeancier ech = new Echeancier(Filiere.LA_FILIERE.getEtape(), 8, hs.get(Filiere.LA_FILIERE.getEtape()+9)/8);
				ExemplaireContratCadre ex = sup.demandeVendeur(ach, this, Feve.F_MQ_BE, ech, this.cryptogramme, false);
				if(ex != null) {
					this.getContrats().add(ex);
					this.echecVentePrix.put(Feve.F_MQ_BE, false);
					this.nbEchecVentePrix.put(Feve.F_MQ_BE, 0);
				}
			}
		List<IAcheteurContratCadre> acheteurs2 = sup.getAcheteurs(Feve.F_HQ_BE);
		for(IAcheteurContratCadre ach2 : acheteurs2) {
			boolean testQuantite2 = true;
			for(int i = Filiere.LA_FILIERE.getEtape() +1; i<Filiere.LA_FILIERE.getEtape()+9; i++){
				if(hs.get(i)-(i-Filiere.LA_FILIERE.getEtape() +1)*this.venteMin/2 < this.venteMin/2) {
					testQuantite2 = false;
				}
			}if(testQuantite2) {
				Echeancier ech = new Echeancier(Filiere.LA_FILIERE.getEtape(), 8, hs2.get(Filiere.LA_FILIERE.getEtape()+9)/8);
				ExemplaireContratCadre ex = sup.demandeVendeur(ach2, this, Feve.F_MQ_BE, ech, this.cryptogramme, false);
				if(ex != null) {
					this.getContrats().add(ex);
					this.echecVentePrix.put(Feve.F_MQ_BE, false);
					this.nbEchecVentePrix.put(Feve.F_MQ_BE, 0);
				}
			}
		}
			/* double prodMQ = this.aLivrer(Feve.F_MQ).getQuantiteJusquA(Filiere.LA_FILIERE.getEtape() + 12) - this.aLivrer(Feve.F_MQ).getQuantiteJusquA(Filiere.LA_FILIERE.getEtape()); //production previsionnelle moyenne des 12 steps à venir
			if(this.getStockTot(Feve.F_MQ).getValeur()/12 + prodMQ >= 10) {
				Echeancier ech = new Echeancier(Filiere.LA_FILIERE.getEtape(), 12, this.getStockTot(Feve.F_MQ).getValeur()/12 + prodMQ);
				ExemplaireContratCadre ex = sup.demandeVendeur(ach, this, Feve.F_MQ, ech, this.cryptogramme, false);
				if(ex != null) {
					this.getContrats().add(ex);
					this.echecVentePrix.put(Feve.F_MQ, false);
					this.nbEchecVentePrix.put(Feve.F_MQ, 0);
				}
			} */
			
		}
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
		boolean testQuantite = true;
		HashMap<Integer, Double> hs = getStocksTotTheo((Feve) produit, Filiere.LA_FILIERE.getEtape() + 9);
		for(int i = Filiere.LA_FILIERE.getEtape() +1; i<Filiere.LA_FILIERE.getEtape()+13; i++){
			if(hs.get(i) < this.venteMin) {
				testQuantite = false;
			}
		}
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
			boolean testQuantite = true;
			HashMap<Integer, Double> hs = getStocksTotTheo((Feve) contrat.getProduit(), Filiere.LA_FILIERE.getEtape() + contrat.getEcheancier().getNbEcheances() + 1);
			for(int i = Filiere.LA_FILIERE.getEtape() +1; i<Filiere.LA_FILIERE.getEtape()+contrat.getEcheancier().getNbEcheances()+1; i++){
				if(hs.get(i)-contrat.getEcheancier().getQuantiteJusquA(i) < 0.0) {
					testQuantite = false;
				}
			}
			if(testQuantite) {
				return new Echeancier(echeancierAch.getStepDebut(), echeancierAch.getStepFin(), (3*echeancierAch.getQuantiteTotale()+hs.get(contrat.getEcheancier().getStepFin()+1))/(echeancierAch.getNbEcheances()*4)); //On essaye de refiler plus de notre stock aux acheteurs
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
		if(this.echecVentePrix.get(contrat.getProduit())) {
			this.nbEchecVentePrix.put((Feve) contrat.getProduit(), this.nbEchecVentePrix.get(contrat.getProduit()) + 1);
		}
		if(this.nbEchecVentePrix.get(contrat.getProduit()) == 3) { //Si un produit voit trois ventes annulés de suite, on baisse son prix
			this.nbIterationVentePrix = 0;
			this.nbEchecVentePrix.put((Feve) contrat.getProduit(), 0);
			this.getPrixCC().put((Feve) contrat.getProduit(), this.getPrixCC((Feve) contrat.getProduit())*0.9);
		}
		this.echecVentePrix.put((Feve) contrat.getProduit(), true);
		this.nbIterationVentePrix = 0; 
		return contrat.getEcheancier().getQuantiteTotale()*Math.min(this.prix_rentable((Feve) contrat.getProduit()), this.getPrixCC((Feve) contrat.getProduit())*this.facteurPrix(nbIterationVentePrix));
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
		this.nbIterationVentePrix++;
		if(contrat.getPrix() >= this.getPrixCC((Feve) contrat.getProduit())) {
			return contrat.getPrix();
		}
		if(contrat.getPrix() >= this.prix_rentable((Feve) contrat.getProduit())) {
			return contrat.getEcheancier().getQuantiteTotale()*this.getPrixCC((Feve) contrat.getProduit())*this.facteurPrix(nbIterationVentePrix); /*Négociation 1/4||3/4 pour tenter de tirer un prix convenable*/
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
		this.echecVentePrix.put((Feve) contrat.getProduit(), false);
		this.nbEchecVentePrix.put((Feve) contrat.getProduit(), 0);
		this.getPrixCC().put((Feve) contrat.getProduit(), this.getPrixCC((Feve) contrat.getProduit())*0.9 + contrat.getPrix()*0.1);
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
		if(Math.min(this.getStockTot((Feve) produit).getValeur(), quantite) > 0.001) {
			return retirerStock((Feve) produit, Math.min(this.getStockTot((Feve) produit).getValeur(), quantite));
		} else {
			return new Lot(produit);
		}
	}
	
	/**
	 * Methode appelee pour connaitre le facteur de prix que l'on applqiue en focntion du nombre d'appels précédents
	 * @param iteration
	 * @return Retourne un double correspondant au facteur
	 */
	public double facteurPrix(int iteration) {
		return (iteration-10)*0.8 + iteration*this.facteurPrixInit;
	}
}
