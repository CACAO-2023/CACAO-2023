package abstraction.eqXRomu.filiere;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import presentation.FenetrePrincipale;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Classe modelisant une filiere vue comme un regroupement d'acteurs, 
 * d'indicateurs, de parametres et de journaux. 
 * 
 * Les acteurs/indicateurs/parametres/journaux que vous creerez devront etre
 * ajoutes a l'unique instance de cette classe designee par la 
 * variable LA_FILIERE. 
 *
 * @author Romuald Debruyne
 */
public class Filiere implements IAssermente {
	public static Filiere LA_FILIERE; // Filiere.LA_FILIERE reference l'unique instance de Filiere
	public static double SEUIL_EN_TETE_DE_GONDOLE_POUR_IMPACT = 0.05; // Si les produits d'une marque m representent au moins 5% de la quantite totale en tete de gondole alors il y a un impact positif sur l'attrait de la marque.


	private int etape;                   // Le numero d'etape en cours
	private Banque laBanque;             // L'unique banque. Tous les acteurs ont un compte a cette banque
	private List<IActeur> acteurs;       // La liste des acteurs
	private List<IActeur> acteursSolvables;// La liste des acteurs n'ayant pas fait faillite
	private List<ClientFinal> clientsFinaux;
	private HashMap<String, Variable> indicateurs;// Table associant a chaque nom d'indicateur la variable qui la represente
	private HashMap<IActeur, List<Variable>> indicateursParActeur; // Table associant a chaque acteur sa liste d'indicateurs
	private HashMap<String, Variable> parametres;// Table associant a chaque nom de parametre la variable qui la represente
	private HashMap<IActeur, List<Variable>> parametresParActeur; // Table associant a chaque acteur sa liste de parametres
	private HashMap<String, Journal> journaux;      // La liste des journaux
	private HashMap<IActeur, List<Journal>> journauxParActeur; // Table associant a chaque acteur sa liste de journaux
	private PropertyChangeSupport pcs;        // Pour notifier les observers des changements de step 
	protected Journal journalFiliere;
	private HashMap<String, IActeur> marquesDeposees; // Associe a chaque marque de chocolat deposee l'acteur qui la possede
	//private List<IFabricantChocolatDeMarque> fabricantsDeChocolatDeMarque; // Tous
	private List<ChocolatDeMarque> chocolatsProduits; // La liste de tous les types de chocolat de marque produits.
	private HashMap<ChocolatDeMarque, List<IFabricantChocolatDeMarque>> fabricantsChocolatDeMarque; // Associe a chaque chocolat de marque la liste des acteurs qui le produise 
	private HashMap<String, Double> qualiteMoyenneMarque; // Associe a une marque la qualite moyenne des produits portant cette marque

	// Influence des tetes de gondole sur la notoriete des marques
	private List<String> presenceEnTG; // A chaque step, si les produits d'une marque representent au moins 5% des quantites mis 
	//en vente en tete de gondole alors la marque est ajoutee a cette liste. Seuls les 100 derniers ajouts sont conserves.
	private HashMap<String, Integer> nbPresencesEnTg; // nombre d'occurrence de la marque dans la liste presenceEnTG;
	private HashMap<IActeur, Integer> cryptos;

	/**
	 * Initialise la filiere de sorte que le numero d'etape soit 0, 
	 * et qu'il n'y ait pour l'heure que la Banque pour unique acteur. 
	 * Les constructeurs des sous-classes de Filiere devront ajouter les autres acteurs
	 */
	public Filiere() {
		this.etape=0;
		this.acteurs=new ArrayList<IActeur>();
		this.acteursSolvables=new ArrayList<IActeur>();
		this.clientsFinaux=new ArrayList<ClientFinal>();
		this.indicateurs=new HashMap<String, Variable>();
		this.parametres=new HashMap<String, Variable>();
		this.indicateursParActeur=new HashMap<IActeur, List<Variable>>();
		this.parametresParActeur=new HashMap<IActeur, List<Variable>>();
		this.journaux=new HashMap<String, Journal>();
		this.journauxParActeur=new HashMap<IActeur, List<Journal>>();
		this.pcs = new  PropertyChangeSupport(this);
		this.laBanque = new Banque();
		this.journalFiliere = this.laBanque.getJournaux().get(0);
		this.marquesDeposees = new HashMap<String, IActeur>();
		this.chocolatsProduits = new ArrayList<ChocolatDeMarque>();
		this.fabricantsChocolatDeMarque = new HashMap<ChocolatDeMarque, List<IFabricantChocolatDeMarque>>();
		this.qualiteMoyenneMarque = new HashMap<String, Double>();
		this.presenceEnTG = new LinkedList<String>();
		this.nbPresencesEnTg = new HashMap<String, Integer>();
		this.ajouterActeur(this.laBanque);
	}

	public void initialiser() {
		// Depot des marques de chocolat
		for (IActeur a : this.acteurs) {
			if (a instanceof IMarqueChocolat) {
				List<String> marques = ((IMarqueChocolat)a).getMarquesChocolat();
				if (marques!=null && marques.size()>0) {
					if ((a instanceof IDistributeurChocolatDeMarque) && marques.size()>1) {
						throw new IllegalStateException("Le distributeur "+a.getNom()+" a une methode getMarquesChocolat qui retourne une liste de longueur "+marques.size()+" (un distribteur ne peut avoir qu'une marque de chocolat)");
					}
					for (String m : marques) {
						if (this.marquesDeposees.get(m)!=null) {
							throw new IllegalStateException("L'acteur "+a.getNom()+" tente de deposer la marque "+m+" qui est deja la propriete de "+marquesDeposees.get(m).getNom());
						} else {
							this.marquesDeposees.put(m, a);
						}
					}
				}
			}
		}
		this.journalFiliere.ajouter("Marques deposees : "+this.marquesDeposees);
		this.journalFiliere.ajouter("Marques distributeurs : "+this.getMarquesDistributeur());

		// Determination de la liste de tous les chocolats de marques produits

		List<IFabricantChocolatDeMarque> fabricants = new ArrayList<IFabricantChocolatDeMarque>();
		for (IActeur a : this.acteurs) {
			if ((a instanceof IFabricantChocolatDeMarque)) {
				fabricants.add((IFabricantChocolatDeMarque)a);
			}
		}
		for (IFabricantChocolatDeMarque f : fabricants) {
			List<ChocolatDeMarque> produits = f.getChocolatsProduits();
			for (ChocolatDeMarque c : produits) {
				if (!this.chocolatsProduits.contains(c)) {
					this.chocolatsProduits.add(c);
				}
			}
		}
		this.journalFiliere.ajouter("Chocolats de marque produits : "+this.chocolatsProduits);

		// Association a chaque chocolat de marque produit de la liste des acteurs qui le produisent
		for (ChocolatDeMarque c : this.chocolatsProduits) {
			this.fabricantsChocolatDeMarque.put(c,  new ArrayList<IFabricantChocolatDeMarque>());
		}
		for (IFabricantChocolatDeMarque f : fabricants) {
			List<ChocolatDeMarque> produits = f.getChocolatsProduits();
			for (ChocolatDeMarque c : produits) {
				this.fabricantsChocolatDeMarque.get(c).add(f);
			}
		}
		for (ChocolatDeMarque c : this.chocolatsProduits) {
			this.journalFiliere.ajouter("Producteurs de "+c+" : "+this.fabricantsChocolatDeMarque.get(c));
		}


		// Association a chaque marque de la qualite moyenne des produits portant cette marque
		for (String marque : getMarquesChocolat()) {
			int nbProduits=0;
			double totalQualites=0.0;
			for (ChocolatDeMarque choco : chocolatsProduits) {
				if (choco.getMarque().equals(marque)) {
					totalQualites+=choco.getChocolat().qualite();
					nbProduits++;
				}
			}
			if (nbProduits==0) {
				throw new IllegalStateException("la marque "+marque+" est deposee mais il n'y a aucune production d'un chocolat portant cette marque");
			}
			this.qualiteMoyenneMarque.put(marque, totalQualites/nbProduits);
			this.journalFiliere.ajouter("qualite moyenne de la marque "+marque+" = "+this.qualiteMoyenneMarque.get(marque));
		}

		// Initialisation de la presence en TG
		for (String marque : getMarquesChocolat()) {
			this.nbPresencesEnTg.put(marque, 0);
		}

		// Initialisation de chaque acteur
		this.journalFiliere.ajouter("Initialiser()");
		for (IActeur a : this.acteurs) {
			a.initialiser();
		}
	}

	/**
	 * @return La liste de toutes les marques de chocolat deposees
	 */
	public List<String> getMarquesChocolat() {
		return new ArrayList<String>(this.marquesDeposees.keySet());
	}

	/**
	 * 
	 * @return La liste des marques deposees par un distributeur
	 */
	public List<String> getMarquesDistributeur() {
		ArrayList<String> marquesDistri = new ArrayList<String>();
		List<String> marques = getMarquesChocolat();
		for (String marque :marques) {
			if (getProprietaireMarque(marque) instanceof IDistributeurChocolatDeMarque) {
				marquesDistri.add(marque);
			}
		}
		return marquesDistri;
	}

	/**
	 * @param marque
	 * @return Le proprietaire de la marque precisee en parametre si il s'agit d'une marque deposee (null si le parametre n'est pas une marque deposee)
	 */
	public IActeur getProprietaireMarque(String marque) {
		return this.marquesDeposees.get(marque);
	}

	/**
	 * @return Retourne l'unique instance de Banque de la filiere
	 */
	public Banque getBanque() {
		return this.laBanque;
	}


	/**
	 * @return Retourne le numero de l'etape en cours.
	 */
	public int getEtape() {
		return this.etape;
	}

	/** @param etape etape>=0
	 * @return Retourne l'annee de l'etape precisee en parametre */
	public int getAnnee(int etape) {
		return 2023+etape/24;
	}
	/** @return Retourne l'annee de l'etape courante */
	public int getAnnee() {
		return 2023+this.etape/24;
	}

	/** @return Le numero du mois de l'etape precisee en parametre
	 *  (1==Janvier, 2==Fevrier, ... 12==Decembre	 */
	public int getNumeroMois(int etape) {
		return 1+(etape%24)/2;
	}
	/** @return Le numero du mois courant (1==Janvier, 2==Fevrier, ... 12==Decembre	 */
	public int getNumeroMois() {
		return getNumeroMois(this.etape);
	}
	/** @return Retourne le nom du mois de l'etape precisee en parametre */
	public String getMois(int etape) {
		String[] mois= {"janvier", "fevrier", "mars", "avril", "mai", "juin", "juillet", "aout", "septembre", "octobre", "novembre", "decembre"};
		return mois[getNumeroMois(etape)-1];
	}
	/** @return Retourne le nom du mois de l'etape courante */
	public String getMois() {
		String[] mois= {"janvier", "fevrier", "mars", "avril", "mai", "juin", "juillet", "aout", "septembre", "octobre", "novembre", "decembre"};
		return mois[getNumeroMois()-1];
	}

	/** @return Retourne le numero du jour de l'etape precisee en parametre 
	 * (1==debut de mois, 15==milieu de mois) 	 */
	public int getJour(int etape) {
		return (etape%2==0 ? 1 : 15);
	}
	/** @return Retourne le numero du jour de l'etape courante 	 */
	public int getJour() {
		return getJour(this.etape);
	}

	/** @return Retourne une chaine de caracteres correspond a la date de l'etape precisee en parametre	 */
	public String getDate(int etape) {
		return this.getJour(etape)+" "+this.getMois(etape)+" "+this.getAnnee(etape);
	}
	/** @return Retourne une chaine de caracteres correspond a la date de l'etape courante	*/
	public String getDate() {
		return this.getJour()+" "+this.getMois()+" "+this.getAnnee();
	}

	/**
	 * Ajoute l'acteur ac a la filiere si il n'existe pas deja un acteur portant le meme nom
	 * Leve une erreur si le parametre est null ou si le nom d'ac est celui d'un acteur deja
	 * dans la filiere
	 * @param ac, l'acteur a ajouter
	 */
	public void ajouterActeur(IActeur ac) {
		this.journalFiliere.ajouter(Journal.texteColore(ac==null ? Color.white : ac.getColor(), Color.black,"ajouterActeur("+(ac==null?"null":ac.getNom())+")"));
		if (ac==null) {
			erreur("Appel a ajouterActeur de Filiere avec un parametre null");
		} else if (this.getActeur(ac.getNom())==null) {
			this.acteurs.add(ac);
			this.acteursSolvables.add(ac);
			if (ac instanceof ClientFinal) {
				this.clientsFinaux.add((ClientFinal)ac);
			}
		} else {
			erreur("Appel a ajouterActeur de Filiere avec pour parametre le nom d'un acteur deja present dans la filiere");
		}
		this.getBanque().creerCompte(ac);
		this.journalFiliere.ajouter(Journal.texteColore(ac, "- creation du compte bancaire de "+ac.getNom()));
		//		this.initIndicateurs(ac);
		this.indicateursParActeur.put(ac, new ArrayList<Variable>());
		List<Variable> indicateursAAjouter = ac.getIndicateurs();
		for (Variable v : indicateursAAjouter) {
			this.ajouterIndicateur(v);
			this.journalFiliere.ajouter(Journal.texteColore(ac,"- ajout de l'indicateur "+v.getNom()));
		}
		//		this.initParametres(ac);
		this.parametresParActeur.put(ac, new ArrayList<Variable>());
		List<Variable> parametresAAjouter = ac.getParametres();
		for (Variable v : parametresAAjouter) {
			this.ajouterParametre(v);
			this.journalFiliere.ajouter(Journal.texteColore(ac,"- ajout du parametre "+v.getNom()));
		}
		//		this.initJournaux(ac);
		this.journauxParActeur.put(ac, new ArrayList<Journal>());
		List<Journal> journauxAAjouter = ac.getJournaux();
		for (Journal j : journauxAAjouter) {
			this.ajouterJournal(j);
			this.journalFiliere.ajouter(Journal.texteColore(ac,"- ajout du journal "+j.getNom()));
		}
	}

	/**
	 * @return Retourne une copie de la liste des acteurs de la filiere
	 */
	public List<IActeur> getActeurs() {
		return new ArrayList<IActeur>(this.acteurs);
	}

	/**
	 * @return Retourne une copie de la liste des acteurs de la filiere n'ayant pas fait faillite
	 */
	public List<IActeur> getActeursSolvables() {
		return new ArrayList<IActeur>(this.acteursSolvables);
	}

	/**
	 * @param nom Le nom de l'acteur a retourner
	 * @return Si il existe dans la filiere un acteur de nom nom, retourne cet acteur.
	 * Sinon, returne null. 
	 */
	public IActeur getActeur(String nom) {
		int i=0; 
		while (i<this.acteurs.size() && !this.acteurs.get(i).getNom().equals(nom)) {
			i++;
		}
		if (i<this.acteurs.size()) {
			return this.acteurs.get(i);
		} else {
			return null;
		}
	}

	/** 
	 * @return Retourne une copie de la liste des indicateurs de l'acteur
	 */
	public List<Variable> getIndicateurs(IActeur acteur) {
		if (acteur==null) {
			erreur("Appel de getIndicateurs de Filiere avec null pour parametre");
		} else if (!this.indicateursParActeur.keySet().contains(acteur)){
			erreur("Appel de getIndicateurs de Filiere avec pour parametre un acteur non present ");
		} 
		return new ArrayList<Variable>(this.indicateursParActeur.get(acteur));
	}

	/** 
	 * @return Retourne une copie de la liste des parametres de l'acteur
	 */
	public List<Variable> getParametres(IActeur acteur) {
		if (acteur==null) {
			erreur("Appel de getParametres de Filiere avec null pour parametre");
		} else if (!this.parametresParActeur.keySet().contains(acteur)){
			erreur("Appel de getParametres de Filiere avec pour parametre un acteur non present ");
		} 
		return new ArrayList<Variable>(this.parametresParActeur.get(acteur));
	}

	/**
	 * @param nom le nom de l'indicateur a retourner
	 * @return Si il existe dans le Monde un indicateur de nom nom
	 * retourne cet indicateur. Sinon, affiche un message d'alerte 
	 * et retourne null.
	 */
	public Variable getIndicateur(String nomIndicateur) {
		if (nomIndicateur==null) {
			erreur("Appel de getIndicateur de Filiere avec null pour parametre");
		}
		Variable res = this.indicateurs.get(nomIndicateur);
		if (res==null) {
			System.out.println("  Aie... recherche d'un indicateur en utilisant un nom incorrect : \""+nomIndicateur+"\" n'est pas dans la liste :"+indicateurs.keySet());
			System.out.println("  la variable que vous recherchez est peut etre un parametre plutot qu'un indicateur ?");
		}
		return res;
	}

	/**
	 * @param nom le nom de l'indicateur a retourner
	 * @return Si il existe dans le Monde un indicateur de nom nom
	 * retourne cet indicateur. Sinon, affiche un message d'alerte 
	 * et retourne null.
	 */
	public Variable getParametre(String nomParametre) {
		if (nomParametre==null) {
			erreur("Appel de getParametre de Filiere avec null pour parametre");
		} 
		Variable res = this.parametres.get(nomParametre);
		if (res==null) {
			System.out.println("  Aie... recherche d'un parametre en utilisant un nom incorrect : \""+nomParametre+"\" n'est pas dans la liste :"+parametres.keySet());
			System.out.println("  la variable que vous recherchez est peut etre un indicateur plutot qu'un parametre ?");
		}
		return res;
	}

	/**
	 * @return Retourne la liste des journaux de l'acteur specifie
	 */
	public List<Journal> getJournaux(IActeur acteur) {
		if (acteur==null) {
			erreur("Appel de getJournaux de Filiere avec null pour parametre");
		} else if (!this.journauxParActeur.keySet().contains(acteur)){
			erreur("Appel de getJournaux de Filiere avec pour parametre un acteur non present ");
		} 
		return new ArrayList<Journal>(this.journauxParActeur.get(acteur));
	}

	public void erreur(String s) {
		this.journalFiliere.ajouter(Journal.texteColore(Color.RED, Color.WHITE,s));
		throw new Error(s);
	}

	/**
	 * Methode appelee lorsque l'utilisateur clique sur le bouton NEXT de l'interface graphique.
	 * Cette methode incremente le numero d'etape puis appelle la methode next() de chaque acteur du monde.
	 */
	public void next() {
		this.journalFiliere.ajouter("Next() : Passage a l'etape suivante====================== ");
		for (IActeur a : this.acteurs) {
			if (!this.laBanque.aFaitFaillite(a)) {
				this.journalFiliere.ajouter(Journal.texteColore(a, "- "+a.getNom()+".next()"));
				this.journalFiliere.notifyObservers();
				a.next();
				for (Journal j : journauxParActeur.get(a)) {
					j.notifyObservers();
				}
			}
		}
		// Mise a jour de l'impact de la presence en TG sur les marques
		HashMap<String, Double> quantiteEnTG=new HashMap<String,Double>(); // associe a chaque marque la quantite de produit en tete de gondole
		for (String marque : getMarquesChocolat()) {
			quantiteEnTG.put(marque, 0.0);
		}
		ClientFinal cf = this.clientsFinaux.get(0);
		double quantiteTotaleEnTG=0.0;
		for (ChocolatDeMarque choco : getChocolatsProduits()) {
			for (IDistributeurChocolatDeMarque distri : getDistributeurs()) {
				double qtg = cf.getQuantiteEnVenteTG(distri, choco, cryptos.get(cf));
				quantiteEnTG.put(choco.getMarque(), quantiteEnTG.get(choco.getMarque())+qtg);
				quantiteTotaleEnTG+=qtg;
			}
		}
		for (String marque : getMarquesChocolat()) {
			if (quantiteEnTG.get(marque)>=(SEUIL_EN_TETE_DE_GONDOLE_POUR_IMPACT*quantiteTotaleEnTG)) {
				presenceEnTG.add(marque);
				nbPresencesEnTg.put(marque, nbPresencesEnTg.get(marque)+1);
				if (presenceEnTG.size()>100) {
					String premier = presenceEnTG.get(0);
					nbPresencesEnTg.put(premier, nbPresencesEnTg.get(premier)-1);
					presenceEnTG.remove(0);
				}
			}
		}
		this.incEtape();
	}

	public void addObserver(PropertyChangeListener obs) {
		pcs.addPropertyChangeListener(obs);
	}

	public void notificationFaillite(IActeur acteur) {
		this.acteursSolvables.remove(acteur);
		this.journalFiliere.ajouter(Journal.texteColore(acteur,"Faillite de "+acteur.getNom()));
		this.journalFiliere.notifyObservers();
		if (FenetrePrincipale.LA_FENETRE_PRINCIPALE!=null) {
			FenetrePrincipale.LA_FENETRE_PRINCIPALE.notificationFaillite(acteur);
		}
	}

	public double qualitePercueMarque(String marque) {
		double bonusTG=0.0;
		if (!this.getMarquesChocolat().contains(marque)) {
			throw new IllegalArgumentException("Appel de qualitePercueMarque("+marque+") alors que les marques deposees sont "+this.getMarquesChocolat());
		}
		double nb = nbPresencesEnTg.get(marque);
		if (nb>0) {
			bonusTG = nb / presenceEnTG.size();
		}
		return this.qualiteMoyenneMarque.get(marque)+bonusTG;
	}

	public List<IDistributeurChocolatDeMarque> getDistributeurs() {
		List<IDistributeurChocolatDeMarque> res = new ArrayList<IDistributeurChocolatDeMarque>();
		for (IActeur a : this.acteurs) {
			if ((a instanceof IDistributeurChocolatDeMarque) && (!this.laBanque.aFaitFaillite(a))) {
				res.add((IDistributeurChocolatDeMarque)a);
			}
		}
		return res;
	}

	public List<IFabricantChocolatDeMarque> getFabricantsChocolatDeMarque(ChocolatDeMarque c) {
		return new ArrayList<IFabricantChocolatDeMarque>(this.fabricantsChocolatDeMarque.get(c));
	}
	/**
	 * @return Retourne la liste de tous les chocolats de marque produits par les differents acteurs de la filiere
	 * Remarque : vous ne pouvez pas appeler cette methode dans votre constructeur
	 * (vous obtiendriez une liste vide).
	 */
	public List<ChocolatDeMarque> getChocolatsProduits() {
		return new ArrayList<ChocolatDeMarque>(this.chocolatsProduits);
	}
	public double prixMoyen(ChocolatDeMarque choco, int etape) {
		return clientsFinaux.get(0).prixMoyen(choco, etape);
	}
	public double getVentes(ChocolatDeMarque choco, int etape) {
		return clientsFinaux.get(0).getVentes(etape, choco);
	}

	/***********************************************************************/
	/**                METHODES INTERNES (non accessibles)                **/
	/***********************************************************************/

	/**
	 * Ajoute l'indicateur i a la filiere
	 * @param i l'idicateur a ajouter
	 */
	private void ajouterIndicateur(Variable i) {
		if (i==null) {
			erreur("Appel a ajouterIndicateur de Filiere avec null pour parametre");
		} else if (this.indicateurs.get(i.getNom())!=null) {
			erreur("Appel a ajouterIndicateur(v) de Filiere alors qu'il existe deja dans la filiere un indicateur portant le meme nom que v (\""+i.getNom()+"\")");
		} else {
			this.indicateurs.put(i.getNom(), i);
			List<Variable> indicateursActuels = this.indicateursParActeur.get(i.getCreateur());
			if (indicateursActuels==null) {
				indicateursActuels=new ArrayList<Variable>();
			}
			indicateursActuels.add(i);
			this.indicateursParActeur.put(i.getCreateur(), indicateursActuels);
		}
	}

	/**
	 * Ajoute le parametre i a la filiere
	 * @param i le parametre a ajouter
	 */
	private void ajouterParametre(Variable i) {
		if (i==null) {
			erreur("Appel a ajouterParametre de Filiere avec null pour parametre");
		} else if (this.parametres.get(i.getNom())!=null) {
			erreur("Appel a ajouterParametre(v) de Filiere alors qu'il existe deja dans la filiere un parametre portant le meme nom que v (\""+i.getNom()+"\")");
		} else {
			this.parametres.put(i.getNom(), i);
			List<Variable> parametresActuels = this.parametresParActeur.get(i.getCreateur());
			if (parametresActuels==null) {
				parametresActuels=new ArrayList<Variable>();
			}
			parametresActuels.add(i);
			this.parametresParActeur.put(i.getCreateur(), parametresActuels);
		}
	}


	/**
	 * Ajoute le journal j au monde
	 * @param j le journal a ajouter
	 */
	private void ajouterJournal(Journal j) {
		if (j==null) {
			erreur("Appel a ajouterJournal de Filiere avec null pour parametre");
		} else if (this.journaux.get(j.getNom())!=null) {
			erreur("Appel a ajouterJournal(j) de Filiere alors qu'il existe deja dans la filiere un journal portant le meme nom que j (\""+j.getNom()+"\")");
		} else {
			this.journaux.put(j.getNom(), j);
			List<Journal> journauxActuels = this.journauxParActeur.get(j.getCreateur());
			if (journauxActuels==null) {
				journauxActuels=new ArrayList<Journal>();
			}
			journauxActuels.add(j);
			this.journauxParActeur.put(j.getCreateur(), journauxActuels);
		}
	}


	/**
	 * Methode interne (non accessible) permettant de passer a l'etape suivante
	 * en notifiant les observateurs
	 */
	private void incEtape() {
		int old = this.etape;
		this.etape++;
		pcs.firePropertyChange("Etape",old,this.etape);
	}

	public void setCryptos(HashMap<IActeur, Integer> cryptos) {
		this.cryptos = cryptos;
	}
}
