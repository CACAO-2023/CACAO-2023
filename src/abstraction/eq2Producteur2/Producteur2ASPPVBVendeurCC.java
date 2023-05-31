package abstraction.eq2Producteur2;

import java.util.HashMap;
import java.util.LinkedList;

//Code ecrit par Nino

import java.util.List;

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

public class Producteur2ASPPVBVendeurCC extends Producteur2ASPPVendeurBourse implements IVendeurContratCadre{
	protected HashMap<Feve, Integer> nbEchecVentePrix = new HashMap<Feve, Integer>(); //Permet de connaitre le nombre de ventes ayant echoue à la suite 
	protected HashMap<Feve, Boolean> tentativeVente = new HashMap<Feve, Boolean>();  //Permet de savoir si la derniere vente a reussi pour chaque produit
	protected int nbIterationVentePrix; //Compte le nombre d'appel à contrePropositionPrix pour faire évoluer le prix
	protected double facteurPrixInit = 1.2;
	protected double venteMin = SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER;
	protected int nbStepFidelité = 12;
	protected int nbStepSuperFidelité = 40;
	protected int nbStepProposition = 8;
	protected double facteurTolerance = 0.95; //Facteur de tolérance pour l'acceptation des ventes
	protected HashMap<IActeur, HashMap<Integer, Boolean>> historiqueFidelite = new HashMap<IActeur, HashMap<Integer, Boolean>>();
	
	
	public Producteur2ASPPVBVendeurCC() {
		super();
	}
	
	
	public void initialiser() {
		super.initialiser();
		
		this.tentativeVente.put(Feve.F_MQ_BE, false);
		this.tentativeVente.put(Feve.F_HQ_BE, false);
		this.tentativeVente.put(Feve.F_MQ, false);
		this.nbEchecVentePrix.put(Feve.F_MQ_BE, 0);
		this.nbEchecVentePrix.put(Feve.F_HQ_BE, 0);
		this.nbEchecVentePrix.put(Feve.F_MQ, 0);
		for(IAcheteurContratCadre ach : ((SuperviseurVentesContratCadre) Filiere.LA_FILIERE.getActeur("Sup."+"CCadre")).getAcheteurs(Feve.F_HQ_BE)) {
			this.historiqueFidelite.put(ach, new HashMap<Integer, Boolean>());
		}
	}
	
	/**
	 * Methode appelee par le superviseur afin d'effectuer les actions de l'acteur
	 * a chaque etape. Ici on decide si on propose aux acheteurs un contrat cadre.
	 */
	public void next() {
		super.next();
		SuperviseurVentesContratCadre sup = ((SuperviseurVentesContratCadre) Filiere.LA_FILIERE.getActeur("Sup."+"CCadre"));
		List<IAcheteurContratCadre> acheteursMQ = sup.getAcheteurs(Feve.F_MQ_BE);
		List<IAcheteurContratCadre> acheteursHQ = sup.getAcheteurs(Feve.F_HQ_BE);
		Echeancier echMaxMQ = this.getEcheancierMax(Filiere.LA_FILIERE.getEtape() + nbStepProposition + 1).get(Feve.F_MQ_BE);
		Echeancier echMaxHQ = this.getEcheancierMax(Filiere.LA_FILIERE.getEtape() + nbStepProposition + 1).get(Feve.F_HQ_BE);
		if(echMaxMQ.getQuantiteTotale() > venteMin) {
			int nbAcheteursMQ = acheteursMQ.size();
			for(int i = 0; i<nbAcheteursMQ; i++) {
				IAcheteurContratCadre ach = acheteursMQ.get((int) Math.random()*acheteursMQ.size());
				acheteursMQ.remove(ach);
				ExemplaireContratCadre ex = sup.demandeVendeur(ach, this, Feve.F_MQ_BE, echMaxMQ, this.cryptogramme, false);
				if(ex != null) {
					this.notificationNouveauContratCadre(ex);
				}
			}
		}
		if(echMaxHQ.getQuantiteTotale() > venteMin) {
			int nbAcheteursHQ = acheteursHQ.size();
			for(int i = 0; i<nbAcheteursHQ; i++) {
				IAcheteurContratCadre ach = acheteursHQ.get((int) Math.random()*acheteursHQ.size());
				acheteursHQ.remove(ach);
				ExemplaireContratCadre ex = sup.demandeVendeur(ach, this, Feve.F_HQ_BE, echMaxHQ, this.cryptogramme, false);
				if(ex != null) {
					this.notificationNouveauContratCadre(ex);
				}
			}
		}
		this.majListeCC();
		this.journalCC.ajouter("Contrats Cadre en cours : " + this.contrats);
	}
	
	/**
	 * @param produit, produit!=nul
	 * @return Retourne true si l'acteur peut durant la simulation (pas necessairement a cette etape)
	 * vendre le type de produit precise en parametre (retourne false si il s'agit d'un produit que
	 * l'acteur ne vendra pas durant la simulation.
	 */
	public boolean peutVendre(IProduit produit) {
		return produit instanceof Feve && (produit == Feve.F_MQ_BE || produit == Feve.F_HQ_BE);
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
		if (!peutVendre(produit)) {
			return false;
		} else {
			return this.getEcheancierMax(Filiere.LA_FILIERE.getEtape() + this.nbStepProposition + 1).get(produit).getQuantiteTotale() >= venteMin;
		}
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
		Echeancier echeancierAch = contrat.getEcheancier(); //Echeancier proposé par l'acheteur
		Echeancier echMax = this.getEcheancierMax(echeancierAch.getStepFin()).get(contrat.getProduit()); //Echeancier correspondant à la vente de tout notre stock
		Echeancier ech = new Echeancier(echeancierAch.getStepDebut()); //Echeancier renvoyé
		if(echeancierAch.getStepDebut() > Filiere.LA_FILIERE.getEtape()) {
			for(int i = contrat.getEcheancier().getStepDebut(); i<echeancierAch.getStepFin()+1; i++){
				ech.ajouter(Math.min(echMax.getQuantite(i) + echMax.getQuantiteJusquA(i-1) - ech.getQuantiteJusquA(i-1), echeancierAch.getQuantite(i))); //On accepte la proposition de l'acheteur en vérifiant que l'on pourra y répondre
			}
			if(ech.getQuantiteTotale()>venteMin) {
				return ech;
			}
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
		if(this.tentativeVente.get(contrat.getProduit())) {
			this.nbEchecVentePrix.put((Feve) contrat.getProduit(), this.nbEchecVentePrix.get(contrat.getProduit()) + 1);
		}
		if(this.nbEchecVentePrix.get(contrat.getProduit()) == 3) { //Si un produit voit trois ventes annulés de suite, on baisse son prix
			this.nbIterationVentePrix = 0;
			this.nbEchecVentePrix.put((Feve) contrat.getProduit(), 0);
			this.getPrixCC().get((Feve) contrat.getProduit()).setValeur(this, this.getPrixCC((Feve) contrat.getProduit())*0.9);
		}
		this.tentativeVente.put((Feve) contrat.getProduit(), true);
		this.nbIterationVentePrix = 0;
		return Math.max(this.prix_rentable((Feve) contrat.getProduit()), getPrixSouhaitéCC(contrat));
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
		double prixPrec = contrat.getListePrix().get(contrat.getListePrix().size() - 2); //Le dernier prix que l'on a proposé
		if(contrat.getPrix() >= prixPrec) { //Si le prix est supérieur au prix que l'on proposait précédement, on l'accepte dans notre grande gentillesse
			return contrat.getPrix();
		}
		if(contrat.getPrix() >= this.prix_rentable((Feve) contrat.getProduit())) {
			if(contrat.getPrix() >= prixPrec*facteurTolerance) {
				return contrat.getPrix(); //Si le prix proposé est roche du prix souhaité précédement, on accepte afin d'assurer que la vente ai lieu
			} else {
				return getPrixSouhaitéCC(contrat)*3/4 + contrat.getPrix()/4; /*Négociation 1/4||3/4 pour tenter de tirer un prix convenable*/
			}
		}
		return this.prix_rentable((Feve) contrat.getProduit());
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
		this.tentativeVente.put((Feve) contrat.getProduit(), false);
		this.nbEchecVentePrix.put((Feve) contrat.getProduit(), 0);
		this.getPrixCC().get((Feve) contrat.getProduit()).setValeur(this, this.getPrixCC((Feve) contrat.getProduit())*0.9 + contrat.getPrix()*0.1); //On essaye d'adapter nos prix
		this.getContrats().add(contrat);
		Echeancier ech = contrat.getEcheancier();
		IActeur ach = contrat.getAcheteur();
		for(int i = ech.getStepDebut(); i<=ech.getStepFin(); i++) {
			this.historiqueFidelite.get(ach).put(i, true);
		}
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
		double quantiteLivre = Math.min(this.getStockTot((Feve) produit).getValeur(), quantite);
		if(quantiteLivre > 0.001) {
			this.argentVente.get((Feve) produit).ajouter(this, quantiteLivre*contrat.getPrix(), this.cryptogramme);
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
	private double facteurPrix() {
		return (this.nbIterationVentePrix-15)*(0.8-this.facteurPrixInit)/15 + 0.8;
	}
	
	/**
	 * Methode calculant les avantages du client liés à sa fidélité
	 * La fidélité se calcule par seuil
	 * @param act
	 * @return Retourne un double correspondant au facteur diminuant le prix proposé à cet acteur
	 */
	private double facteurFidelite(IActeur act) {
		int decompte = 0;
		for(int i = Math.max(0, Filiere.LA_FILIERE.getEtape()-50); i<Filiere.LA_FILIERE.getEtape(); i++) {
			if(this.historiqueFidelite.get(act).get(i) == null) {
				this.historiqueFidelite.get(act).put(i, false);
			} else if(this.historiqueFidelite.get(act).get(i)) {
				decompte++;
			}
		}
		if(decompte >= this.nbStepSuperFidelité) {
			return 0.85;
		} 
		if(decompte >= this.nbStepFidelité) {
			return 0.95;
		}
		return 1.0;
	}
	
	/**
	 * Méthode calculant le prix souhaité pour ce contrat i.e. en prenant en compte le produit et l'acheteur. Cette fonction ne tient pas compte de la rentabilité
	 * @param contrat
	 * @return Retourne un double correspondant au prix souhaité
	 */
	private double getPrixSouhaitéCC(ExemplaireContratCadre contrat) {
		return this.getPrixCC((Feve) contrat.getProduit())*this.facteurFidelite(contrat.getAcheteur())*this.facteurPrix();
	}
	
	private void majListeCC() {
		LinkedList<ExemplaireContratCadre> supp = new LinkedList<ExemplaireContratCadre>();
		for(ExemplaireContratCadre cc : this.contrats) {
			if(cc.getEcheancier().getStepFin() < Filiere.LA_FILIERE.getEtape()) {
				supp.add(cc);
			}
		}
		for(ExemplaireContratCadre cc : supp) {
			this.contrats.remove(cc);
		}
	}
}
