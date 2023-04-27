package abstraction.eqXRomu.clients;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.filiere.IAssermente;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class ClientFinal implements IActeur, IAssermente, PropertyChangeListener {

	public static final double POURCENTAGE_MAX_EN_TG = 0.1; // La quantite mise en tete de gondole ne doit pas depasser 10% de la quantite totale mise en vente
	//	protected double pourcentageConsommationBasseQualite;
	//	protected Map<IDistributeurChocolatDeMarque, List<ChocolatDeMarque>> catalogues;
	//	protected Map<ChocolatDeMarque, List<IDistributeurChocolatDeMarque>> distributeursParChocolat;
	protected List<String> marques;
	//	protected List<ChocolatDeMarque> chocolatsDeMarquesDistribues;
	private Map<IDistributeurChocolatDeMarque, Map<ChocolatDeMarque, Double>> quantiteEnVente; // Memorise pour chaque distributeur la quantite de chaque chocolat de marque en vente au step courant. Cela permet d'eviter les incoherences qui pourraient etre engendrees par un distributeur ne retournant pas toujours la meme valeur durant le meme step (on ne fait appel qu'une fois et on memorise le resultat)
	private Map<IDistributeurChocolatDeMarque, Map<ChocolatDeMarque, Double>> quantiteEnTG;// Memorise pour chaque distributeur la quantite de chaque chocolat de marque en vente en tete de gondole au step courant. Cela permet d'eviter les incoherences qui pourraient etre engendrees par un distributeur ne retournant pas toujours la meme valeur durant le meme step (on ne fait appel qu'une fois et on memorise le resultat)
	private Map<IDistributeurChocolatDeMarque, Map<ChocolatDeMarque, Variable>> prix;// Memorise pour chaque distributeur et chaque chocolat de marque une variable gardant trace du prix a l'etape.
	//    private List<ChocolatDeMarque>chocolatsDeMarquesProduits; //  == Filiere.LA_FILIERE.getChocolatsProduits();


	private double distributionsAnnuelles[][] ;
	// Exemple : 
	//	{
	//    Jan1 Jan2 Fev1 Fev2 Mar1 Mar2 Avr1 Avr2 Mai1 Mai2 Jui1 Jui2 Jul1 Jul2 Aou1 Aou2 Sep1 Sep2 Oct1 Oct2 Nov1 Nov2 Dec1 Dec2
	//	{ 4.5, 4.5, 4.5, 4.5, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.5, 4.5, 4.5, 4.5, },			
	//	{ 5.5, 5.5, 5.0, 5.0, 4.5, 4.0, 4.0, 4.0, 4.0, 3.5, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.5, 4.0, 4.0, 4.5, 5.0, 5.0, 5.5, 5.5, },			
	//	{ 3.5, 3.5, 6.0, 3.5, 3.5, 3.5, 3.5, 3.5, 9.0, 3.5, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 9.0, 9.0, },			
	//	{ 3.0, 3.0, 6.0, 3.0, 3.0, 3.0, 3.0, 3.0, 9.0, 3.0, 3.0, 2.0, 2.0, 2.0, 2.0, 2.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0,15.0,15.0, },			
	//	{ 3.0, 3.0, 7.0, 3.0, 3.0, 3.0, 3.0, 3.0,10.0, 3.0, 3.0, 2.0, 2.0, 2.0, 2.0, 2.0, 3.0, 3.0, 3.0,10.0, 3.0, 3.0,11.0,10.0, },			
	//	{ 3.0, 3.0,10.0, 3.0, 3.0, 3.0, 3.0, 3.0,12.0, 3.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 3.0, 3.0, 3.0, 3.0, 3.0,15.0,15.0, },			
	//	{ 3.0, 3.0,11.0, 3.0, 3.0, 3.0, 3.0, 3.0,13.0, 3.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 3.0, 3.0,10.0, 3.0, 3.0,11.0,10.0, },			
	//	};

	private Variable volumeVente ; // le volume de vente du chocolat de marque selectionne
	private Variable cmSelectionnee; // l'index du chocolat selectionne
	private HashMap<ChocolatDeMarque, Variable> venteCM; // le volume de vente de chaque chocolat de marque
	
	private Map<ChocolatDeMarque, Double> attractiviteChocolat;// Plus un chocolat a une forte attractivite (compare aux autres chocolats), plus ce chocolat aura une place importante dans la consommation globale de chocolat
	private Map<ChocolatDeMarque, Map<IDistributeurChocolatDeMarque, Double>> attractiviteDistributeur;// Pour un chocolat donne, plus l'attractivite d'un distributeur est grande (comparee a celle des autres distributeurs) plus sa part de marche sur ce chocolat sera grande
	private Variable deltaConsoMin, deltaConsoMax, conso, surcoutMemeQualite, surcoutQualitesDifferentes, gainAttractiviteMemeQualite, gainAttractiviteQualiteDifferente;
	protected HashMap<Chocolat, Double> repartitionInitiale; 
	protected HashMap<ChocolatDeMarque, Double> repartitionIntentionsAchat; // Associe a chaque chocolat de marque son pourcentage vis a vis des intentions globales d'achat de chocolat 
	protected Integer cryptogramme;
	protected Journal JournalDistribution, journalAttractivites, journalPrix;

	// Evolution de la distribution temporelle de la consommation 
	private Variable dureeMinTransitionDistribution ;// passer d'une distribution de la consommation a une autre prend au moins ce nombre d'etapes
	private Variable dureeMaxTransitionDistribution;// passer d'une distribution de la consommation a une autre prend au plus ce nombre d'etapes
	private int distributionOrigine; // On part de DISTRIBUTIONS_ANNUELLES[ distributionOrigine ]
	private int distributionArrivee; // On evolue vers DISTRIBUTIONS_ANNUELLES[ distributionArrivee ]
	private int etapesEvolutionDistribution; // C'est au bout de etapesEvolutionDistribution qu'on atteindra la distribution DISTRIBUTIONS_ANNUELLES[ distributionArrivee ] 
	private int etapeEvolutionDistribution; // On a effectue etapeEvolutionDistribution etape sur les etapesEvolutionDistribution etapes necessaires pour atteindre la distribution d'arrivee
	private Map<Integer, Map<ChocolatDeMarque, Double>> historiqueVentes;
	private HashMap<IActeur, Integer> cryptos;
	private List<ChocolatDeMarque>chocolatsDeMarquesProduits; // init dans initialiser
	private Map<Chocolat, List<ChocolatDeMarque>> chocolatsDeMarqueProduitsParChocolat; // init dans initialiser
	/**
	 * 
	 * @param categorie
	 * @param consommationAnnuelle
	 * @param pourcentageConsommationBasseQualite, dans [1.0, 94.0], le pourcentage de la basse qualite dans la consommation de 
	 *    cette categorie de chocolat (le bio_equitable represente 3.0 (bio=3%), le haute_equitable 1.0, 
	 *    le moyenne_equitable 1.0 (equitable=5%), le moyenne qualite 95.0-pourcentageConsommationBasseQualite)
	 */
	public ClientFinal(double consommationAnnuelle, HashMap<Chocolat, Double> repartitionInitiale, double[][]distributionsAnnuelles) {
		this.distributionsAnnuelles = distributionsAnnuelles;
		this.conso=new Variable(getNom()+" consommation annuelle", null, this, consommationAnnuelle);
		this.deltaConsoMin=new Variable(getNom()+" delta annuel min conso", null, this, 0.0);
		this.deltaConsoMax=new Variable(getNom()+" delta annuel max conso", null, this, 0.0);
		this.dureeMinTransitionDistribution = new Variable(getNom()+" duree min evolution distribution", null, this, 24);
		this.dureeMaxTransitionDistribution = new Variable(getNom()+" duree max evolution distribution", null, this, 48);
		this.quantiteEnVente=new HashMap<IDistributeurChocolatDeMarque, Map<ChocolatDeMarque, Double>>();
		this.quantiteEnTG=new HashMap<IDistributeurChocolatDeMarque, Map<ChocolatDeMarque, Double>>();
		this.prix=new HashMap<IDistributeurChocolatDeMarque, Map<ChocolatDeMarque, Variable>>();

		this.JournalDistribution= new Journal(this.getNom()+" distribution", this);
		this.journalAttractivites = new Journal(this.getNom()+" attractivites", this);
		this.journalPrix = new Journal(this.getNom()+" prix", this);
		this.volumeVente = new Variable(getNom()+" volume vente CM", null, this, 0.0);
		this.cmSelectionnee = new Variable(getNom()+" choc. marque select.", "indiquez l'index du chocolat de marque", this, 0.0);
		this.surcoutMemeQualite = new Variable(getNom()+" surcout meme qualite", null, this, 0.25);
		this.surcoutQualitesDifferentes = new Variable(getNom()+" surcout qualites differentes", null, this, 1.25);
		this.gainAttractiviteMemeQualite = new Variable(getNom()+" gain attractivite meme qualite", null, this, 0.005); 
		this.gainAttractiviteQualiteDifferente = new Variable(getNom()+" gain attractivite qualite differentes", null, this, 0.05);
		this.repartitionInitiale = repartitionInitiale;

	}
	//--- IACTEUR ---
	public String getNom() {
		return "C.F.";
	}
	public String getDescription() {
		return "Client final ";
	}
	public Color getColor() {
		return new Color(140,140,140);
	}
	public void setCryptos(HashMap<IActeur, Integer> cryptos) {
		if (this.cryptos==null) { // Les cryptogrammes ne sont indique qu'une fois par la banque : si la methode est appelee une seconde fois c'est que l'auteur de l'appel n'est pas la banque et qu'on cherche a "pirater" l'acteur
			this.cryptos = cryptos;
		}
	}

	public void initialiser() {
		this.JournalDistribution.ajouter("=== initialisation du client "+this.getNom()+" ===");
		this.marques = Filiere.LA_FILIERE.getMarquesChocolat();
		this.initEvolutionDistributionAnnuelleDesVentes();

		this.chocolatsDeMarquesProduits = Filiere.LA_FILIERE.getChocolatsProduits();

		this.venteCM = new HashMap<ChocolatDeMarque, Variable> ();
		for (ChocolatDeMarque cm : chocolatsDeMarquesProduits) {
			this.venteCM.put(cm, new Variable(getNom()+" V.V."+cm, "V.V."+cm, this, 0.0));
		}
		this.volumeVente.cloner(this.venteCM.get(chocolatsDeMarquesProduits.get(0))); // initialement c'est le premier chocolat de marque quidont le volume de vente est affiche
		this.cmSelectionnee.addObserver(this);
		
		
		List<IDistributeurChocolatDeMarque> distributeurs = Filiere.LA_FILIERE.getDistributeurs();
		for (IDistributeurChocolatDeMarque d : distributeurs) {
			this.quantiteEnVente.put(d, new HashMap<ChocolatDeMarque, Double>());
			this.quantiteEnTG.put(d, new HashMap<ChocolatDeMarque, Double>());
			this.prix.put(d, new HashMap<ChocolatDeMarque, Variable>());
			for (ChocolatDeMarque choco : chocolatsDeMarquesProduits) {
				this.prix.get(d).put(choco, new Variable("prix"+d.getNom()+choco, null, this,  0.0, 100.0, -1.0));
			}
		}
		// Initialisation de l'attractivite de tous les distributeurs sur tous les types de chocolat
		// L'attractivite globale des distributeurs est la meme mais elle se reparti sur l'ensemble des
		// chocolats propose : un distributeur plus specialise aura une attractivite nulle sur les produits
		// qu'il ne vend pas mais davantage d'attractivite sur les produits qu'il vend qu'un distributeur
		// commercialisant tous les chocolats possibles.

		// Initialement tous a 1.0
		this.attractiviteDistributeur=new HashMap<ChocolatDeMarque, Map<IDistributeurChocolatDeMarque, Double>>();
		for (ChocolatDeMarque choco : chocolatsDeMarquesProduits) {
			HashMap<IDistributeurChocolatDeMarque, Double> attract = new HashMap<IDistributeurChocolatDeMarque,Double>();
			for (IDistributeurChocolatDeMarque distri : distributeurs) {
				attract.put(distri, 1.0);
				journalAttractivites.ajouter("Attractivite initiale du chocolat "+choco+" chez "+distri+"="+Journal.doubleSur(attract.get(distri),4 ));
			}
			this.attractiviteDistributeur.put(choco, attract);
		}

		// Initialisation de l'attractivite des chocolats de cette categorie.
		// L'attractivite initiale correspond au pourcentage de consommation du choco
		// divise par le nombre de chocolats de marque aux caracteristiques identiques.

		this.chocolatsDeMarqueProduitsParChocolat  = new HashMap<Chocolat, List<ChocolatDeMarque>>();
		for (Chocolat choco : Chocolat.values()) {
			this.chocolatsDeMarqueProduitsParChocolat.put(choco, new LinkedList<ChocolatDeMarque>());
		}
		for (ChocolatDeMarque chocoMarque : chocolatsDeMarquesProduits) {
			this.chocolatsDeMarqueProduitsParChocolat.get(chocoMarque.getChocolat()).add(chocoMarque);
		}

		// La division sert a repartir l'attractivite d'un type de chocolat entre les marques aux catalogues de ce type de chocolat
		this.attractiviteChocolat = new HashMap<ChocolatDeMarque, Double>();

		for (ChocolatDeMarque choco : chocolatsDeMarquesProduits) {
			attractiviteChocolat.put(choco, repartitionInitiale.get(choco.getChocolat())/chocolatsDeMarqueProduitsParChocolat.get(choco.getChocolat()).size());

			journalAttractivites.ajouter("Attractivite initiale du chocolat "+choco+" = "+attractiviteChocolat.get(choco));
		}

		// Initialisation de l'historique des volumes de ventes
		this.historiqueVentes=new HashMap<Integer, Map<ChocolatDeMarque, Double>>();
		double totalAttractiviteChocolatsDeMarque = this.getTotalAttractiviteChocolatsDeMarque();
		for (int etape=0; etape<24; etape++) {
			Map<ChocolatDeMarque, Double> ventesEtape = new HashMap<ChocolatDeMarque, Double>();
			double consoStep = conso.getValeur()*ratioStep(etape);
			// Repartition des besoins clients en ventes
			for (ChocolatDeMarque choco : chocolatsDeMarquesProduits) {
				ventesEtape.put(choco,consoStep*attractiviteChocolat.get(choco)/totalAttractiviteChocolatsDeMarque);
			}
			historiqueVentes.put(etape-24, ventesEtape);
		}
	}
	private double getTotalAttractiviteChocolatsDeMarque() { // Serait a 100 sans les problemes d'arrondis/representation des double
		double totalAttractiviteChocolats = 0.0;
		for (ChocolatDeMarque choco : chocolatsDeMarquesProduits) {
			totalAttractiviteChocolats+=attractiviteChocolat.get(choco);
		}
		return totalAttractiviteChocolats;
	}

	public void next() {
		// Evolution de la distribution annuelle des ventes
		this.evolutionDistributionAnnuelleDesVentes();
		this.JournalDistribution.ajouter("=== Etape "+Filiere.LA_FILIERE.getEtape()+" ======================");
		this.JournalDistribution.ajouter(" origine="+this.distributionOrigine+" arrive="+this.distributionArrivee+" etape="+this.etapeEvolutionDistribution+"/"+this.etapesEvolutionDistribution);

		// Verification des volumes mis en vente
		journalPrix.ajouter("=== Etape "+Filiere.LA_FILIERE.getEtape()+" ===============================");
		HashMap<IDistributeurChocolatDeMarque,Double> quantiteTotaleMiseEnVente = new HashMap<IDistributeurChocolatDeMarque, Double>();
		for (IDistributeurChocolatDeMarque distri : Filiere.LA_FILIERE.getDistributeurs()) {
			double quantiteTotale = 0.0;
			double quantiteTG = 0.0;
			double pri=-1.0;
			for (ChocolatDeMarque choco :chocolatsDeMarquesProduits) {
				double qv = distri.quantiteEnVente(choco, cryptos.get(distri));
				if (qv<0.0) {
					throw new IllegalStateException("Le distributeur "+distri.getNom()+" met en vente un volume de "+qv+" de "+choco);
				}
				this.quantiteEnVente.get(distri).put(choco, qv);
				quantiteTotale+=qv;

				double qtg = distri.quantiteEnVenteTG(choco, cryptos.get(distri));
				if (qtg<0.0) {
					throw new IllegalStateException("Le distributeur "+distri.getNom()+" met en vente en tete de gondole un volume de "+qtg+" de "+choco);
				}
				this.quantiteEnTG.get(distri).put(choco, qtg);
				// memorisation des prix de vente
				pri = qv>0.0 ? distri.prix(choco) : -1.0; // On ne tient pas compte des prix si le distributeur ne met pas de ce produit en vente
				this.prix.get(distri).get(choco).setValeur(this, pri); 
				this.journalPrix.ajouter(" "+distri.getNom()+" vend "+choco.getNom()+" au prix de "+Journal.doubleSur(distri.prix(choco), 4));
				quantiteTG+=qtg;
			}
			quantiteTotaleMiseEnVente.put(distri, quantiteTotale);
			if (quantiteTG>(quantiteTotale*POURCENTAGE_MAX_EN_TG)+0.001) {
				throw new IllegalStateException("Le distributeur "+distri.getNom()+" met en vente en tete de gondole un volume de "+quantiteTG+" superieur a "+(POURCENTAGE_MAX_EN_TG*100.0)+"% du volume total mis en vente ("+quantiteTotale+")");
			} else {
				this.JournalDistribution.ajouter("---"+Journal.texteColore(distri, distri.getNom())+" met en TG "+Journal.doubleSur(quantiteTG, 2)+" T / "+Journal.doubleSur(quantiteTotale, 2)+" T au prix de "+pri);
			}
		}

		journalAttractivites.ajouter("=== Etape "+Filiere.LA_FILIERE.getEtape()+" ===============================");

		// Evolution du volume de la consommation annuelle
		if (deltaConsoMin.getValeur()>deltaConsoMax.getValeur()) {
			double max = deltaConsoMin.getValeur();
			deltaConsoMin.setValeur(this,  deltaConsoMax.getValeur());
			deltaConsoMax.setValeur(this,  max);
		}
		double deltaConso = deltaConsoMin.getValeur() + (Math.random()*(deltaConsoMax.getValeur() - deltaConsoMin.getValeur()));
		conso.setValeur(this, conso.getValeur()*(1+(deltaConso/24.0)));

		JournalDistribution.ajouter("Evolution conso globale : delta conso = "+Journal.doubleSur(deltaConso, 4)+" ==> conso = "+Journal.doubleSur(conso.getValeur(),2));

		// Calcul du volume de la consommation a cette etape 
		double consoStep = conso.getValeur()*ratioStep();
		JournalDistribution.ajouter("Conso a cette etape = "+Journal.doubleSur(consoStep, 4)+" (ratio distribution="+Journal.doubleSur(ratioStep(), 4)+")");

		// Repartition des besoins clients en ventes
		JournalDistribution.ajouter("= Repartition des besoins clients en ventes ===");
		double totalAttractiviteChocolats = this.getTotalAttractiviteChocolatsDeMarque();

		JournalDistribution.ajouter("&nbsp;&nbsp;Somme des attractivites des chocolats = "+Journal.doubleSur(totalAttractiviteChocolats,4));
		Map<ChocolatDeMarque, Double> ventesEtape = new HashMap<ChocolatDeMarque, Double>();

		for (ChocolatDeMarque choco : chocolatsDeMarquesProduits) {
			double consoStepChoco = consoStep*attractiviteChocolat.get(choco)/totalAttractiviteChocolats;
			JournalDistribution.ajouter(Color.cyan,Color.black,"&nbsp;&nbsp;-- Choco : "+choco.getNom()+" attractivite="+Journal.doubleSur(attractiviteChocolat.get(choco), 4)+" --> conso etape = "+Journal.doubleSur(consoStepChoco, 2));
			// Cette consommation consoStepChoco est a repartir entre les distributeurs au prorata de leur attractivite sur ce produit
			List<IDistributeurChocolatDeMarque> distributeursDeChoco = Filiere.LA_FILIERE.getDistributeurs();
			double totalAttractiviteDistris = 0.0;
			double totalVentes=0.0;// pour memoriser dans l'historique
			for (IDistributeurChocolatDeMarque dist : distributeursDeChoco) {
				totalAttractiviteDistris+=this.attractiviteDistributeur.get(choco).get(dist).doubleValue();
			}
			double prixMoyen=prixMoyen(choco);
			JournalDistribution.ajouter("Distributeurs : "+distributeursDeChoco);
			for (IDistributeurChocolatDeMarque dist : distributeursDeChoco) {
				double quantiteDesiree = consoStepChoco*this.attractiviteDistributeur.get(choco).get(dist)/totalAttractiviteDistris;
				// La non disponibilite, les tetes de gondole et le prix vont pouvoir impacter l'attractivite MAIS cela 
				// influe doucement/a long terme. Or, une augmentation subite du prix peut freiner considerablement les
				// achats car personne n'achete un chocolat hors de prix/beaucoup plus cher que chez le concurrent.
				// Au contraire, un prix bien plus bas que la concurrence peut booster les ventes (==promo).
				// La moitie des ventes n'est pas impactee (achat sans tenir compte du prix pour 50% des achats)
				double pri = this.prix.get(dist).get(choco).getValeur();
				if (quantiteDesiree>0 && pri>0.0 && prixMoyen>0.0) { // Si le prixMoyen est negatif c'est qu'aucun distributeur n'est en mesure de fournir ce chocolat
					quantiteDesiree*= (0.5 + (0.5*prixMoyen/pri));
				}
				double enVente = this.quantiteEnVente.get(dist).get(choco);
				double quantiteAchetee = Math.max(0.0, Math.min(quantiteDesiree, enVente));
				quantiteAchetee = quantiteAchetee<0.05 ? 0.0 : quantiteAchetee; // En dessous de 50kg la quantite demandee devient 0.0 
				JournalDistribution.ajouter("&nbsp;&nbsp;&nbsp;&nbsp;pour "+dist.getNom()+" d'attractivite "+Journal.doubleSur(this.attractiviteDistributeur.get(choco).get(dist), 4)+" la quantite desiree est "+Journal.doubleSur(quantiteDesiree,4)+" et quantite en vente ="+Journal.doubleSur(enVente, 4)+" -> quantitee achetee "+Journal.doubleSur(quantiteAchetee, 4));
				if (quantiteAchetee>0.0) {
					totalVentes+=quantiteAchetee;
					Filiere.LA_FILIERE.getBanque().virer(this, cryptogramme, dist, quantiteAchetee*dist.prix(choco));
					dist.vendre(this, choco, quantiteAchetee, quantiteAchetee*pri, this.cryptos.get(dist));
				} 
				if (quantiteDesiree>enVente) {
					dist.notificationRayonVide(choco, this.cryptos.get(dist));
				}
				double impactRupture = (quantiteDesiree>enVente ? -0.03 : 0.01); // si le client n'a pas trouve tout ce qu'il souhaite la penalite est de -3%, sinon l'attractivite augmente de 1%
				double impactPrix = ((prixMoyen-pri)/prixMoyen)/10.0; // 10% du pourcentage d'ecart de prix avec la moyenne.
				double impactTG = quantiteTotaleMiseEnVente.get(dist)==0 ? 0.0 : (this.quantiteEnTG.get(dist).get(choco)/quantiteTotaleMiseEnVente.get(dist))/5.0; // 2% au plus repartis sur les differents chocolats mis en tete de gondole
				attractiviteDistributeur.get(choco).put(dist,attractiviteDistributeur.get(choco).get(dist)*(1.0+impactRupture+impactPrix+impactTG));
				if (impactTG!=0.0) {
					attractiviteChocolat.put(choco, attractiviteChocolat.get(choco)*(1.0+impactTG));
				}
				journalAttractivites.ajouter("  Attractivite de "+Journal.texteColore(dist, dist.getNom())+" pour "+choco.getNom()+" = "+Journal.doubleSur(attractiviteDistributeur.get(choco).get(dist), 4)+ " (impact rupture="+Journal.doubleSur(impactRupture,  4)+"  impact prix="+Journal.doubleSur(impactPrix,  4)+"  impact TG="+Journal.doubleSur(impactTG,  4)+")");
			}
			this.venteCM.get(choco).setValeur(this, totalVentes);
			ventesEtape.put(choco,totalVentes);
			this.historiqueVentes.put(Filiere.LA_FILIERE.getEtape(), ventesEtape);
		}

		journalAttractivites.ajouter("= Transfert des attractivites entre les chocolats en fonction des rapports qualite/prix");
		// Transfert d'attractivite entre les chocolats en fonction des rapports qualite/prix
		for (ChocolatDeMarque choco1 : chocolatsDeMarquesProduits) {
			for (ChocolatDeMarque choco2 : chocolatsDeMarquesProduits) {
				if (prixMoyen(choco1)>0.0 && prixMoyen(choco2)>0.0 && choco1!=choco2) {
					ChocolatDeMarque moinsCher=choco1;
					ChocolatDeMarque plusCher=choco2;
					if (prixMoyen(choco2)<prixMoyen(choco1)) {
						moinsCher=choco2;
						plusCher=choco1;
					}
					if (moinsCher.qualitePercue()==plusCher.qualitePercue()) {
						if ((prixMoyen(plusCher)-prixMoyen(moinsCher))/prixMoyen(moinsCher)>this.surcoutMemeQualite.getValeur()) {// a qualite identique un ecart de prix de plus de 25% modifie l'attractivite
							attractiviteChocolat.put(moinsCher, attractiviteChocolat.get(moinsCher)*(1+this.gainAttractiviteMemeQualite.getValeur()));//1.005);// +0.5%						
							attractiviteChocolat.put(plusCher, attractiviteChocolat.get(plusCher)*(1-+this.gainAttractiviteMemeQualite.getValeur()));//// -0.5%						
							this.journalAttractivites.ajouter("&nbsp;&nbsp;prixMoyen("+moinsCher.getNom()+")="+Journal.doubleSur(prixMoyen(moinsCher), 4)+" et prixMoyen("+plusCher.getNom()+")="+Journal.doubleSur(prixMoyen(plusCher), 4)+" --> attractivite +"+this.gainAttractiviteMemeQualite.getValeur()+" pour le moins cher");
						}
					} else if (moinsCher.qualitePercue()>plusCher.qualitePercue()) {
						attractiviteChocolat.put(moinsCher, attractiviteChocolat.get(moinsCher)*(1+this.gainAttractiviteQualiteDifferente.getValeur()));//// +5%						
						attractiviteChocolat.put(plusCher, attractiviteChocolat.get(plusCher)*(1-this.gainAttractiviteQualiteDifferente.getValeur()));//// -5%						
						this.journalAttractivites.ajouter("&nbsp;&nbsp;prixMoyen("+moinsCher.getNom()+")="+Journal.doubleSur(prixMoyen(moinsCher), 4)+" et prixMoyen("+plusCher.getNom()+")="+Journal.doubleSur(prixMoyen(plusCher), 4)+" --> attractivite +"+this.gainAttractiviteQualiteDifferente.getValeur()+" pour le moins cher");
					} else {
						if ((prixMoyen(plusCher)-prixMoyen(moinsCher))/prixMoyen(moinsCher)>this.surcoutQualitesDifferentes.getValeur()*(plusCher.qualitePercue()-moinsCher.qualitePercue())) {
							attractiviteChocolat.put(moinsCher, attractiviteChocolat.get(moinsCher)*(1+this.gainAttractiviteQualiteDifferente.getValeur()));						
							attractiviteChocolat.put(plusCher, attractiviteChocolat.get(plusCher)*(1-this.gainAttractiviteQualiteDifferente.getValeur()));			
							this.journalAttractivites.ajouter("&nbsp;&nbsp;prixMoyen("+moinsCher.getNom()+")="+Journal.doubleSur(prixMoyen(moinsCher), 4)+" et prixMoyen("+plusCher.getNom()+")="+Journal.doubleSur(prixMoyen(plusCher), 4)+" --> attractivite +"+this.gainAttractiviteQualiteDifferente.getValeur()+" pour le moins cher");
						}
					}
				}
			}
		}
		for (ChocolatDeMarque choco1 : chocolatsDeMarquesProduits) {
			this.journalAttractivites.ajouter("attractivite du "+choco1.getNom()+" == "+Journal.doubleSur(attractiviteChocolat.get(choco1), 4));
		}
	}
	public List<String> getNomsFilieresProposees() {
		List<String> noms = new ArrayList<String>();

		return noms;
	}
	public Filiere getFiliere(String nom) {
		return null;
	}
	public List<Variable> getIndicateurs() {
		List<Variable> res=new ArrayList<Variable>();
		res.add(this.conso);
		res.add(deltaConsoMin);
		res.add(deltaConsoMax);
		res.add(this.volumeVente);
		res.add(this.cmSelectionnee);
		return res;
	}
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		res.add(this.dureeMinTransitionDistribution);
		res.add(this.dureeMaxTransitionDistribution);
		res.add(this.surcoutMemeQualite);
		res.add(this.surcoutQualitesDifferentes);
		res.add(this.gainAttractiviteMemeQualite);
		res.add(this.gainAttractiviteQualiteDifferente);
		return res;
	}
	public List<Journal> getJournaux() {
		List<Journal> j= new ArrayList<Journal>();
		j.add(this.journalAttractivites);
		j.add(this.journalPrix);
		j.add(this.JournalDistribution);
		return j;
	}
	public void notificationFaillite(IActeur acteur) {
	}
	public void notificationOperationBancaire(double montant) {
	}
	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}
	//--- FIN IACTEUR---

	//--- Evolution de la consommation annuelle
	private void initEvolutionDistributionAnnuelleDesVentes() {
		this.distributionOrigine = (int) (Math.random()*distributionsAnnuelles.length);
		this.distributionArrivee = (int) (Math.random()*distributionsAnnuelles.length);// potentiellement distributionArrivee==distributionOrigine ==> dans ce cas on est dans une periode ou les pics de consommation ne changent pas.
		this.etapesEvolutionDistribution =(int) ( dureeMinTransitionDistribution.getValeur() + (int)(Math.random()*(dureeMaxTransitionDistribution.getValeur()-dureeMinTransitionDistribution.getValeur()))); // Nombre d'etapes pour atteindre la prochaine distribution
		this.etapeEvolutionDistribution=0;
	}
	private void evolutionDistributionAnnuelleDesVentes() {
		this.etapeEvolutionDistribution++;
		if (this.etapeEvolutionDistribution>this.etapesEvolutionDistribution) {
			this.etapeEvolutionDistribution=0;
			this.distributionOrigine=this.distributionArrivee;
			this.distributionArrivee = (int) (Math.random()*distributionsAnnuelles.length);// potentiellement distributionArrivee==distributionOrigine ==> dans ce cas on est dans une periode ou les pics de consommation ne changent pas.
			this.etapesEvolutionDistribution =(int) ( dureeMinTransitionDistribution.getValeur() + (int)(Math.random()*(dureeMaxTransitionDistribution.getValeur()-dureeMinTransitionDistribution.getValeur()))); // Nombre d'etapes pour atteindre la prochaine distribution
		}
	}
	private double ratioStep(int step) {
		double origine = distributionsAnnuelles[distributionOrigine][step%24];
		double arrivee = distributionsAnnuelles[distributionArrivee][step%24];
		return (origine+((arrivee-origine)*etapeEvolutionDistribution)/etapesEvolutionDistribution)/100.0;
	}

	private double ratioStep() {
		return ratioStep(Filiere.LA_FILIERE.getEtape());
	}
	public double getVentes(int etape, ChocolatDeMarque choco) {
		if (this.historiqueVentes.keySet().contains(etape)) {
			if (this.historiqueVentes.get(etape).containsKey(choco)) {
				return this.historiqueVentes.get(etape).get(choco);
			} else {
				return 0.0;
			}
		} else {
			throw new IllegalArgumentException(" Appel de ClientFinal.getVentes avec etape=="+etape+" alors que les etapes valides sont "+this.historiqueVentes.keySet());
		}
	}

	public double getVentes(int etape, Chocolat choco) {
		if (this.historiqueVentes.keySet().contains(etape)) {
			Map<ChocolatDeMarque, Double> ventes = this.historiqueVentes.get(etape);
			double totalVentes=0.0;
			for (ChocolatDeMarque cdm : ventes.keySet()) {
				if (cdm.getChocolat().equals(choco)) {
					totalVentes=totalVentes+ventes.get(cdm);
				}
			}
			return totalVentes;
		} else {
			return 0;
		}
	}

	public void initAttractiviteChoco(ChocolatDeMarque choco, double val) {
		if (Filiere.LA_FILIERE==null || Filiere.LA_FILIERE.getEtape()<1) {
			if (val<0.1) {
				throw new IllegalArgumentException("la methode initAttractiviteChoco de ClientFinal n'accepte pas une valeur inferieure a 0.1");
			} else {
				attractiviteChocolat.put(choco, val);
			}
		} else {
			throw new IllegalArgumentException("la methode initAttractiviteChoco de ClientFinal ne peut etre appelee qu'avant le premier step");
		}
	}

//	private double prix(IDistributeurChocolatDeMarque d, ChocolatDeMarque choco) {
//		return prix.get(d).get(choco).getValeur(Filiere.LA_FILIERE.getEtape());
//	}
//
//	private double prix(IDistributeurChocolatDeMarque d, ChocolatDeMarque choco,  int etape) {
//		return prix.get(d).get(choco).getValeur(etape);
//	}

	private double prixMoyen(ChocolatDeMarque choco) {
		return prixMoyenPrive(choco, Filiere.LA_FILIERE.getEtape());
	}
	public double prixMoyen(ChocolatDeMarque choco, int etape) {
		if (etape<0 || etape>=Filiere.LA_FILIERE.getEtape()) {
			throw new IllegalArgumentException("Appel de prixMoyen("+choco+", "+etape+") de ClientFinal avec des arguments non autorises");
		}
		return prixMoyenPrive(choco, etape);
	}
	private double prixMoyenPrive(ChocolatDeMarque choco, int etape) {
		List<IDistributeurChocolatDeMarque> distris = Filiere.LA_FILIERE.getDistributeurs();
		int nbVendeurs = 0;
		double sommePrix = 0.0;
		for (IDistributeurChocolatDeMarque d : distris) {
			double p = prix.get(d).get(choco).getValeur(etape);
			if (p!=-1.0) {
				nbVendeurs++;
				sommePrix+=p;
			}
		}
		if (nbVendeurs==0) {
			return -1.0;
		} else {
			return sommePrix/nbVendeurs;
		}	
	}


	//-----------------------------------------------------------------------
	// OPERATIONS "SENSIBLES" ACCESSIBLES UNIQUEMENT AUX ACTEURS ASSERMENTES
	//         (verification via le cryptogramme passe en parametre)       
	//-----------------------------------------------------------------------
	public double getQuantiteEnVente(IDistributeurChocolatDeMarque distri, ChocolatDeMarque choco, int crypto) {
		if (crypto==this.cryptogramme) {
			return this.quantiteEnVente.get(distri).get(choco);
		} else {
			throw new IllegalArgumentException("Appel de getQuantiteEnVente de ClientFinal avec un cryptogramme incorrect");
		}
	}
	public double getQuantiteEnVenteTG(IDistributeurChocolatDeMarque distri, ChocolatDeMarque choco, int crypto) {
		if (crypto==this.cryptogramme) {
			return this.quantiteEnTG.get(distri).get(choco);
		} else {
			throw new IllegalArgumentException("Appel de getQuantiteEnVenteTG de ClientFinal avec un cryptogramme incorrect");
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		int index = (int)(this.cmSelectionnee.getValeur());
		if (index<0 || index>=this.chocolatsDeMarquesProduits.size()) {
			index=0;
			this.cmSelectionnee.setValeur(this, index);
		}
		this.volumeVente.cloner(this.venteCM.get(this.chocolatsDeMarquesProduits.get(index)));
		System.out.println("Chocolat de marque selectionne :"+this.chocolatsDeMarquesProduits.get(index));
	}
}
