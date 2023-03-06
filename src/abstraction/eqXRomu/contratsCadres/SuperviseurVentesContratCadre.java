package abstraction.eqXRomu.contratsCadres;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.*;
import abstraction.eqXRomu.general.*;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class SuperviseurVentesContratCadre implements IActeur, IAssermente {
	public static final int MAX_PRIX_NEGO = 14 ; // Les negociations sur le prix s'arretent apres au plus MAX_PRIX_NEGO propositions de prix
	public static final double QUANTITE_MIN_ECHEANCIER = 100.0; // Il ne peut pas etre propose de contrat avec un echeancier de moins de QUANTITE_MIN_ECHEANCIER
	public static int NB_SUPRVISEURS_CONTRAT_CADRE = 0;
	private int numero;
	private Journal journal;
	private List<ContratCadre> contratsEnCours;
	private List<ContratCadre> contratsTermines;
	private HashMap<IActeur, Integer> cryptos;
	//	private long cryptogramme;


	public static final int MAX_MEME_VENDEUR_PAR_STEP = 15; 
	// Au cours d'un meme step un acheteur peut negocier au plus MAX_MEME_VENDEUR_PAR_STEP
	// fois avec le meme vendeur. Si l'acheteur demande a negocier avec le vendeur v alors qu'il a 
	// deja negocie MAX_MEME_VENDEUR_PAR_STEP fois avec v durant le step courant alors l'acheteur
	// ne pourra plus negocier durant le step.

	public SuperviseurVentesContratCadre() {
		NB_SUPRVISEURS_CONTRAT_CADRE++;
		this.numero = NB_SUPRVISEURS_CONTRAT_CADRE;
		this.journal = new Journal("Journal "+this.getNom(), this);
		this.contratsEnCours= new ArrayList<ContratCadre>();
		this.contratsTermines= new ArrayList<ContratCadre>();
	}
	public String getNom() {
		return "Sup."+(this.numero>1?this.numero+"":"")+"CCadre";
	}

	public void initialiser() {
	}

	public List<IVendeurContratCadre> getVendeurs(IProduit produit) {
		List<IVendeurContratCadre> vendeurs = new LinkedList<IVendeurContratCadre>();
		List<IActeur> acteurs = Filiere.LA_FILIERE.getActeursSolvables();
		for (IActeur acteur : acteurs) {
			if (acteur instanceof IVendeurContratCadre && ((IVendeurContratCadre)acteur).vend(produit)) {
				vendeurs.add(((IVendeurContratCadre)acteur));
			}
		}
		return vendeurs;
	}
	public List<IAcheteurContratCadre> getAcheteurs(IProduit produit) {
		List<IAcheteurContratCadre> acheteurs = new LinkedList<IAcheteurContratCadre>();
		List<IActeur> acteurs = Filiere.LA_FILIERE.getActeursSolvables();
		for (IActeur acteur : acteurs) {
			if (acteur instanceof IAcheteurContratCadre && ((IAcheteurContratCadre)acteur).achete(produit)) {
				acheteurs.add(((IAcheteurContratCadre)acteur));
			}
		}
		return acheteurs;
	}

	/**
	 * 
	 * @param acheteur l'acheteur appelant la methode
	 * @param vendeur le vendeur avec qui l'acheteur souhaite etablir un contrat cadre
	 * @param produit le produit vendu via ce contrat cadre
	 * @param echeancier un Echeancier precisant les quantites a livre a chaque step
	 * @param cryptogramme le cryptogramme de l'acheteur afin de verifier l'identite de l'acteur appelant la methode
	 * @param tg true si l'acheteur est un distributeur et que le produit devra 
	 *           obligatoirement etre vendu en tete de gondole. False dans tous les autres
	 *           cas (l'acheteur n'est pas un distributeur et/ou le produit pourra ne 
	 *           pas etre commercialise en tete de gondole)
	 * @return L'exemplaire de contrat cadre conclu en cas d'accord (null si un accord n'a pas pu etre trouve)
	 */
	public ExemplaireContratCadre demandeAcheteur(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit, Echeancier echeancier, int cryptogramme, boolean tg, int pourcentageRSE) {
		if (acheteur==null) {
			throw new IllegalArgumentException(" appel de demandeAcheteur(...) de SuperViseurVentesContratCadre avec null pour acheteur");
		}
		if (vendeur==null) {
			throw new IllegalArgumentException(" appel de demandeAcheteur(...) de SuperViseurVentesContratCadre avec null pour vendeur");
		}
		if (produit==null) {
			throw new IllegalArgumentException(" appel de demandeAcheteur(...) de SuperViseurVentesContratCadre avec null pour produit");
		}
		if (echeancier==null) {
			throw new IllegalArgumentException(" appel de demandeAcheteur(...) de SuperViseurVentesContratCadre avec null pour echeancier");
		}

		if (acheteur==vendeur) {
			throw new IllegalArgumentException(" appel de demandeAcheteur(...) de SuperViseurVentesContratCadre avec vendeur==acheteur. On ne peut pas faire un contrat cadre avec soi meme");
		}
		if (!Filiere.LA_FILIERE.getBanque().verifier(acheteur, cryptogramme)) {
			throw new IllegalArgumentException(" appel de demandeAcheteur(...) de SuperViseurVentesContratCadre par l'acheteur "+acheteur.getNom()+" avec un cryptogramme qui n'est pas le sien");
		}
		if (!(acheteur instanceof IDistributeurChocolatDeMarque) && tg) {
			throw new IllegalArgumentException(" appel de demandeAcheteur(...) de SuperViseurVentesContratCadre par l'acheteur "+acheteur.getNom()+" avec tg==true alors que l'acheteur n'est pas un distributeur (seuls les distributeurs peuvent s'engager a vendre en tete de gondole)");
		}
		if (echeancier.getQuantiteTotale()<QUANTITE_MIN_ECHEANCIER) {
			if (Filiere.LA_FILIERE.getActeursSolvables().contains(acheteur)) {
				System.out.println("!!! "+acheteur.getNom()+" appel de demandeAcheteur(...) de SuperViseurVentesContratCadre avec un echeancier d'un volume total de moins de "+QUANTITE_MIN_ECHEANCIER+" T");
				Filiere.LA_FILIERE.getBanque().faireFaillite(acheteur);
			}
			return null;
		}
		if (echeancier.getStepDebut()<=Filiere.LA_FILIERE.getEtape()) {
			if (Filiere.LA_FILIERE.getActeursSolvables().contains(acheteur)) {
				System.out.println("!!! "+acheteur.getNom()+" appel de demandeAcheteur(...) de SuperViseurVentesContratCadre avec un echeancier commencant a l'etape "+echeancier.getStepDebut()+" a l'etape "+Filiere.LA_FILIERE.getEtape());
				Filiere.LA_FILIERE.getBanque().faireFaillite(acheteur);
			}
			return null;			
		}
		ContratCadre contrat = new ContratCadre(acheteur, vendeur, produit, echeancier, cryptogramme, tg, pourcentageRSE);
		return negociations(acheteur, vendeur, produit, echeancier, cryptogramme, tg, contrat,acheteur);
	}
	// si on ne precise pas le taux de RSE il est (par defaut) de 0
	public ExemplaireContratCadre demandeAcheteur(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit, Echeancier echeancier, int cryptogramme, boolean tg) {
		return demandeAcheteur(acheteur, vendeur, produit, echeancier, cryptogramme, tg, 0);
	}
	/**
	 * 
	 * @param acheteur l'acheteur avec qui l'acheteur souhaite etablir un contrat cadre
	 * @param vendeur le vendeur appelant la methode
	 * @param produit le produit vendu via ce contrat cadre
	 * @param echeancier un Echeancier precisant les quantites a livre a chaque step
	 * @param cryptogramme le cryptogramme du vendeur afin de verifier l'identite de l'acteur appelant la methode
	 * @param tg true si l'acheteur est un distributeur et que le produit devra 
	 *           obligatoirement etre vendu en tete de gondole. False dans tous les autres
	 *           cas (l'acheteur n'est pas un distributeur et/ou le produit pourra ne 
	 *           pas etre commercialise en tete de gondole)
	 * @return L'exemplaire de contrat cadre conclu en cas d'accord (null si un accord n'a pas pu etre trouve)
	 */
	public ExemplaireContratCadre demandeVendeur(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit, Echeancier echeancier, int cryptogramme, boolean tg) {
		if (acheteur==null) {
			throw new IllegalArgumentException(" appel de demandeVendeur(...) de SuperViseurVentesContratCadre avec null pour acheteur");
		}
		if (vendeur==null) {
			throw new IllegalArgumentException(" appel de demandeVendeur(...) de SuperViseurVentesContratCadre avec null pour vendeur");
		}
		if (produit==null) {
			throw new IllegalArgumentException(" appel de demandeVendeur(...) de SuperViseurVentesContratCadre avec null pour produit");
		}
		if (echeancier==null) {
			throw new IllegalArgumentException(" appel de demandeVendeur(...) de SuperViseurVentesContratCadre avec null pour echeancier");
		}
		if (echeancier.getQuantiteTotale()<QUANTITE_MIN_ECHEANCIER) {
			throw new IllegalArgumentException(" appel de demandeVendeur(...) de SuperViseurVentesContratCadre avec un echeancier d'un volume total de moins de "+QUANTITE_MIN_ECHEANCIER+" T");
		}
		if (echeancier.getStepDebut()<=Filiere.LA_FILIERE.getEtape()) {
			if (Filiere.LA_FILIERE.getActeursSolvables().contains(vendeur)) {
				System.out.println("!!! "+acheteur.getNom()+" appel de demandeVendeur(...) de SuperViseurVentesContratCadre avec un echeancier commencant a l'etape "+echeancier.getStepDebut()+" a l'etape "+Filiere.LA_FILIERE.getEtape());
				Filiere.LA_FILIERE.getBanque().faireFaillite(vendeur);
			}
			return null;			
		}		
		if (acheteur==vendeur) {
			throw new IllegalArgumentException(" appel de demandeVendeur(...) de SuperViseurVentesContratCadre avec vendeur==acheteur. On ne peut pas faire un contrat cadre avec soi meme");
		}
		if (!Filiere.LA_FILIERE.getBanque().verifier(vendeur, cryptogramme)) {
			throw new IllegalArgumentException(" appel de demandeVendeur(...) de SuperViseurVentesContratCadre par l'acheteur "+acheteur.getNom()+" avec un cryptogramme qui n'est pas le sien");
		}
		if (!(acheteur instanceof IDistributeurChocolatDeMarque) && tg) {
			throw new IllegalArgumentException(" appel de demandeVendeur(...) de SuperViseurVentesContratCadre par l'acheteur "+acheteur.getNom()+" avec tg==true alors que l'acheteur n'est pas un distributeur (seuls les distributeurs peuvent s'engager a vendre en tete de gondole)");
		}
		ContratCadre contrat = null;
		if (produit.getType().equals("Feve") && ((Feve)produit).isBioEquitable()) {
			int pourcentageRSE = acheteur.fixerPourcentageRSE(acheteur, vendeur, produit, echeancier, cryptogramme, tg);
			contrat = new ContratCadre(acheteur, vendeur, produit, echeancier, cryptogramme, tg, pourcentageRSE);
		} else {
			contrat = new ContratCadre(acheteur, vendeur, produit, echeancier, cryptogramme, tg);
		}
		Echeancier contrePropositionA;
		journal.ajouter(Journal.texteColore(vendeur, vendeur.getNom())+" lance le contrat #"+contrat.getNumero()+" de "+contrat.getQuantiteTotale()+" T de "+contrat.getProduit()+" a "+Journal.texteColore(acheteur, acheteur.getNom()));
		contrePropositionA=acheteur.contrePropositionDeLAcheteur(new ExemplaireContratCadre(contrat));
		if (contrePropositionA==null) {
			journal.ajouter("   "+Journal.texteColore(acheteur, acheteur.getNom()+" retourne null pour echeancier : arret des negociations"));
			journal.ajouter("contrat #"+contrat.getNumero()+Journal.texteColore(Color.RED,  Color.white, " ANNULE "));
			return null;// arret des negociations
		}
		contrat.ajouterEcheancier(contrePropositionA);
		if (!contrat.accordSurEcheancier()) {
			journal.ajouter("   "+Journal.texteColore(acheteur, acheteur.getNom())+" propose un echeancier different de "+Journal.doubleSur(contrat.getQuantiteTotale(),4)+" T "+contrat.getEcheancier());
		}		
		return negociations(acheteur, vendeur, produit, echeancier, cryptogramme, tg, contrat,vendeur);
	}
	private ExemplaireContratCadre negociations(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, Object produit, Echeancier echeancier, int cryptogramme, boolean tg, ContratCadre contrat, IActeur initiateur) {
		int maxNego = 5 + (int)(Math.random()*11); // Le nombre maximum de contrepropositions est compris dans [5, 15]

		// NEGOCIATIONS SUR L'ECHEANCIER
		Echeancier contrePropositionV, contrePropositionA;
		journal.ajouter(Journal.texteColore(acheteur, acheteur.getNom())+" lance le contrat #"+contrat.getNumero()+" de "+contrat.getQuantiteTotale()+" T de "+contrat.getProduit()+" a "+Journal.texteColore(vendeur, vendeur.getNom()));
		int numNego=0;
		do { 
			numNego++;
			contrePropositionV= vendeur.contrePropositionDuVendeur(new ExemplaireContratCadre(contrat));
			if (contrePropositionV==null) {
				journal.ajouter("   "+Journal.texteColore(vendeur, vendeur.getNom()+" retourne null pour echeancier : arret des negociations"));
				journal.ajouter("contrat #"+contrat.getNumero()+Journal.texteColore(Color.RED,  Color.white, " ANNULE "));
				return null;// arret des negociations
			} 
			contrat.ajouterEcheancier(contrePropositionV);
			if (!contrat.accordSurEcheancier()) {
				journal.ajouter("   "+Journal.texteColore(vendeur, vendeur.getNom())+" propose un echeancier different de "+Journal.doubleSur(contrat.getQuantiteTotale(),4)+" T "+contrat.getEcheancier());
				contrePropositionA=acheteur.contrePropositionDeLAcheteur(new ExemplaireContratCadre(contrat));
				if (contrePropositionA==null) {
					journal.ajouter("   "+Journal.texteColore(acheteur, acheteur.getNom()+" retourne null pour echeancier : arret des negociations"));
					journal.ajouter("contrat #"+contrat.getNumero()+Journal.texteColore(Color.RED,  Color.white, " ANNULE "));
					return null;// arret des negociations
				}
				contrat.ajouterEcheancier(contrePropositionA);
				if (!contrat.accordSurEcheancier()) {
					journal.ajouter("   "+Journal.texteColore(acheteur, acheteur.getNom())+" propose un echeancier different de "+Journal.doubleSur(contrat.getQuantiteTotale(),4)+" T "+contrat.getEcheancier());
				}
			}
		} while (numNego<maxNego && !contrat.accordSurEcheancier());
		if (!contrat.accordSurEcheancier()) {
			journal.ajouter("   aucun accord sur l'echeancier n'a pu etre trouve en moins de "+maxNego+" etapes : arret des negociations");
			journal.ajouter("contrat #"+contrat.getNumero()+Journal.texteColore(Color.RED,  Color.white, " ANNULE "));
			return null;
		} else {
			journal.ajouter("   accord sur l'echeancier : "+contrat.getEcheancier());
		}
		// NEGOCIATIONS SUR LE PRIX
		double propositionV = vendeur.propositionPrix(new ExemplaireContratCadre(contrat));
		journal.ajouter("   "+Journal.texteColore(vendeur, vendeur.getNom())+" propose un prix de "+Journal.doubleSur(propositionV,4));
		if (propositionV<=0.0) {
			journal.ajouter("   arret des negociations");
			journal.ajouter("contrat #"+contrat.getNumero()+Journal.texteColore(Color.RED,  Color.white, " ANNULE "));
			return null;// arret des negociations
		}
		contrat.ajouterPrix(propositionV);
		double propositionA;
		numNego=0;
		do {
			numNego++;
			propositionA = acheteur.contrePropositionPrixAcheteur(new ExemplaireContratCadre(contrat));
			journal.ajouter("   "+Journal.texteColore(acheteur, acheteur.getNom())+" propose un prix de "+Journal.doubleSur(propositionA,4));
			if (propositionA<=0.0) {
				journal.ajouter("   arret des negociations");
				journal.ajouter("contrat #"+contrat.getNumero()+Journal.texteColore(Color.RED,  Color.white, " ANNULE "));
				return null;// arret des negociations
			}
			contrat.ajouterPrix(propositionA);
			if (!contrat.accordSurPrix()) {
				propositionV = vendeur.contrePropositionPrixVendeur(new ExemplaireContratCadre(contrat));
				journal.ajouter("   "+Journal.texteColore(vendeur, vendeur.getNom())+" propose un prix de "+Journal.doubleSur(propositionV,4));
				if (propositionV<=0.0) {
					journal.ajouter("   arret des negociations");
					journal.ajouter("contrat #"+contrat.getNumero()+Journal.texteColore(Color.RED,  Color.white, " ANNULE "));
					return null;// arret des negociations
				}
				contrat.ajouterPrix(propositionV);
			}
		} while (numNego<maxNego && !contrat.accordSurPrix());

		if (!contrat.accordSurPrix()) {
			journal.ajouter("   aucun accord sur le prix n'a pu etre trouve en moins de "+maxNego+" etapes : arret des negociations");
			journal.ajouter("contrat #"+contrat.getNumero()+Journal.texteColore(Color.RED,  Color.white, " ANNULE "));
			return null;
		}
		contrat.signer();// accord : on realise les previsionnels de livraison et paiement
		// On notifie l'acteur qui n'est pas a l'origine de l'appel qu'un contrat vient d'etre signe
		if (initiateur==acheteur) {
			vendeur.notificationNouveauContratCadre(new ExemplaireContratCadre(contrat));
		} else {
			acheteur.notificationNouveauContratCadre(new ExemplaireContratCadre(contrat));
		}
		this.contratsEnCours.add(contrat);
		journal.ajouter(Journal.texteColore(Color.GREEN, Color.BLACK,"   contrat #"+contrat.getNumero()+" entre ")+Journal.texteColore(vendeur, vendeur.getNom())+" et "+Journal.texteColore(acheteur, acheteur.getNom())+" sur "+Journal.doubleSur(contrat.getQuantiteTotale(),4)+" de "+contrat.getProduit()+" etales sur "+contrat.getEcheancier());
		return new ExemplaireContratCadre(contrat);
	}

	public void recapitulerContratsEnCours() {
		this.journal.ajouter("Step "+Filiere.LA_FILIERE.getEtape()+" Contrats en cours : ");
		for (ContratCadre cc : this.contratsEnCours) {
			this.journal.ajouter(cc.oneLineHtml());
		}		
	}

	public void gererLesEcheancesDesContratsEnCours() {
		this.journal.ajouter("Step "+Filiere.LA_FILIERE.getEtape()+" GESTION DES ECHEANCES DES CONTRATS EN COURS ========");
		for (ContratCadre cc : this.contratsEnCours) {
			this.journal.ajouter("- contrat :"+cc.oneLineHtml());
			double aLivrer = cc.getQuantiteALivrerAuStep();
			if (aLivrer>0.0) {
				IVendeurContratCadre vendeur = cc.getVendeur();
			//	double effectivementLivre;
				Lot lotLivre = vendeur.livrer(cc.getProduit(), aLivrer, new ExemplaireContratCadre(cc));
                if (lotLivre==null) {
                	throw new Error(" le IVendeurContratCadre "+vendeur.getNom()+" a sa methode livrer("+cc.getProduit()+", "+aLivrer+", ...) qui retourne null");
                }
                if (!lotLivre.getProduit().equals(cc.getProduit())) {
                	throw new Error(" le IVendeurContratCadre "+vendeur.getNom()+" a sa methode livrer("+cc.getProduit()+", "+aLivrer+", ...) qui retourne un lot dont le produit est "+lotLivre.getProduit()+ "au lieu de "+cc.getProduit());
                }
				this.journal.ajouter("  a livrer="+String.format("%.3f",aLivrer)+"  livre="+String.format("%.3f",lotLivre.getQuantiteTotale()));
				if (lotLivre.getQuantiteTotale()>0.0) {
					IAcheteurContratCadre acheteur = cc.getAcheteur();
					acheteur.receptionner(lotLivre, new ExemplaireContratCadre(cc));
					cc.livrer(lotLivre.getQuantiteTotale());
				} else if (lotLivre.getQuantiteTotale()<0.0) {
					throw new Error(" La methode livrer() du vendeur "+vendeur.getNom()+" retourne un negatif");
				}
			} else {
				this.journal.ajouter("- rien a livrer a cette etape");
			}
			double aPayer = cc.getPaiementAEffectuerAuStep();
			if (aPayer>0.0) {
				IAcheteurContratCadre acheteur = cc.getAcheteur();
				Banque banque = Filiere.LA_FILIERE.getBanque();
				boolean virementOk = banque.virer(acheteur, cryptos.get(acheteur), cc.getVendeur(),aPayer);
				double effectivementPaye = virementOk ? aPayer : 0.0; 
				this.journal.ajouter("  a payer="+String.format("%.3f",aPayer)+"  paye="+String.format("%.3f",effectivementPaye));
				if (effectivementPaye>0.0) {
					cc.payer(effectivementPaye);
				}
			} else {
				this.journal.ajouter("- rien a payer a cette etape");
			}
			cc.penaliteLivraison();
			cc.penalitePaiement();
		}		
	}

	public void archiverContrats() {
		Banque banque = Filiere.LA_FILIERE.getBanque();
		List<ContratCadre> aArchiver = new ArrayList<ContratCadre>();
		for (ContratCadre cc : this.contratsEnCours) {
			if (banque.aFaitFaillite(cc.getVendeur()) || banque.aFaitFaillite(cc.getAcheteur()) || (cc.getMontantRestantARegler()==0.0 && cc.getQuantiteRestantALivrer()==0.0)) {
				aArchiver.add(cc);
			}
		}
		for (ContratCadre cc : aArchiver) {
			this.journal.ajouter("Archivage du contrat :<br>"+cc.toHtml());
			this.contratsEnCours.remove(cc);
			this.contratsTermines.add(cc);
		}
	}

	public void next() {
		recapitulerContratsEnCours();
		gererLesEcheancesDesContratsEnCours();
		archiverContrats();
	}

	public String getDescription() {
		return this.getNom();
	}

	public Color getColor() {
		return new Color(190, 190, 190);
	}

	public List<String> getNomsFilieresProposees() {
		return new LinkedList<String>();
	}

	public Filiere getFiliere(String nom) {
		return null;
	}

	public List<Variable> getIndicateurs() {
		return new LinkedList<Variable>();
	}

	public List<Variable> getParametres() {
		return new LinkedList<Variable>();
	}

	public List<Journal> getJournaux() {
		List<Journal> res = new LinkedList<Journal>();
		res.add(this.journal);
		return res;
	}

	public void setCryptogramme(Integer crypto) {
	}

	public void notificationFaillite(IActeur acteur) {
	}

	public void notificationOperationBancaire(double montant) {
	}

	public void setCryptos(HashMap<IActeur, Integer> cryptos) {
		if (this.cryptos==null) { // Les cryptogrammes ne sont indique qu'une fois par la banque : si la methode est appelee une seconde fois c'est que l'auteur de l'appel n'est pas la banque et qu'on cherche a "pirater" l'acteur
			this.cryptos= cryptos;
		}
	}
}
