package abstraction.eq7Distributeur1;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;

public class Distributeur1Acteur  implements IActeur {
	////////////////////////////////////////////////
	//declaration des variables
	public static Color COLOR_LLGRAY = new Color(238,238,238);
	public static Color COLOR_BROWN  = new Color(141,100,  7);
	public static Color COLOR_PURPLE = new Color(100, 10,115);
	public static Color COLOR_LPURPLE= new Color(155, 89,182);
	public static Color COLOR_GREEN  = new Color(  6,162, 37);
	public static Color COLOR_LGREEN = new Color(  6,255, 37);
	public static Color COLOR_LBLUE  = new Color(  6,130,230);
	
	protected Journal journal;
	protected Journal journal_achat;
	protected Journal journal_stock;

	//On est oblige de mettre les variables ici sinon la creation de la filiere est dans un tel ordre que nous n'y avons pas acces assez tot
	protected Variable totalStocks = new VariablePrivee("Eq7TotalStocks", "<html>Quantite totale de chocolat (de marque) en stock</html>",this, 0.0, 1000000.0, 0.0);
	//La quantité totale de stock de chocolat 
	protected Variable stock_BQ = new Variable("Eq7stock_BQ", "Stock total de chocolat de basse qualité", this, 0);
	protected Variable stock_MQ = new Variable("Eq7stock_MQ", "Stock total de chocolat de moyenne qualité", this, 0);
	protected Variable stock_MQ_BE = new Variable("Eq7stock_MQ_BE", "stock Total de chocolat de moyenne qualité bio-équitable", this, 0);
	protected Variable stock_HQ_BE = new Variable("Eq7stock_HQ_BE", "stock Total de chocolat de haute qualité bio-équitable", this, 0);

	protected double cout_BQ; //Cout d'1t de chocolat basse gamme
	
	protected double cout_MQ_BE; //Cout d'1t de chocolat moyenne gamme labellise

	protected double cout_MQ; //Cout d'1t de chocolat moyenne gamme non labellise

	protected double cout_HQ_BE; //Cout d'1t de chocolat haute gamme labellise
	
	/**
	 * donne les quantités mini pour un contrat cadre
	 * @author ghaly
	 */
	double quantite_min_cc = SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER;
	
	/**
	 * previsions de ventes de la filiere globale pour chaque etape_normalisee
	 * prevision etape -> marque -> valeur
	 */
	protected HashMap<Integer,HashMap<ChocolatDeMarque,Double>> previsions; 
	
	/**
	 * previsions de vente de l'equipe 7
	 * on suppose qu'on vend à chaque étape
	 * prevision etape -> marque -> valeur
	 */
	protected HashMap<Integer,HashMap<ChocolatDeMarque,Double>> previsionsperso; 
	
	/**
	 * couts: couts d'achat à travers les contrats cadres
	 */
	protected HashMap<ChocolatDeMarque,Double> couts = new HashMap<ChocolatDeMarque,Double>(); 
	
	/**
	 * nombre d'achat en contrat cadre, ça servira à calculer la moyenne des couts
	 */
	protected HashMap<ChocolatDeMarque,Double> nombre_achats = new HashMap<ChocolatDeMarque,Double>();; 

	protected Variable cout_stockage_distributeur = new Variable("cout moyen stockage distributeur", this);
	
	protected int cryptogramme;

	public Distributeur1Acteur() {
		this.cout_BQ = 3;
		this.cout_HQ_BE = 3;
		this.cout_MQ_BE = 3;
		this.cout_MQ = 3;
		this.journal = new Journal("Journal "+this.getNom(), this);
	    this.journal_achat=new Journal("Journal des Achats de l'" + this.getNom(),this);
	    this.journal_stock = new Journal("Journal des Stocks del'" + this.getNom(),this);
	}
	

	
	////////////////////////////////////////////////////////
	//         Methodes principales				          //
	////////////////////////////////////////////////////////
	
	/**
	 * @author Theo
	 * Renvoie les previsions de vente de la filiere globale, actualisees à chaque tour
	 */
	protected double getPrevisions(ChocolatDeMarque marque, Integer etape) {
		return previsions.get(etape).get(marque);
	}
	/**
	 * @author Theo
	 * Renvoie les previsions de vente de notre quipe, actualisees à chaque tour
	 */
	protected double getPrevisionsperso(ChocolatDeMarque marque, Integer etape) {
		return previsionsperso.get(etape).get(marque);
	}
	
	/**
	 * @author ghaly
	 * actualise la moyenne des couts d'un chocolat de marque a une etape donnée
	 */
	protected void actualise_cout(Double nv_cout) {
		Double n;
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
				n= nombre_achats.get(marque);
				couts.replace(marque,(couts.get(marque)*(n-1)+nv_cout)/n);}
	}

	/**
	 * @author ghaly
	 * renvois le cout moyen de la gamme
	 */	
	protected double getCout_gamme(Chocolat gamme) {
		int n = 0;
		double s = 0;
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (marque.getChocolat()==gamme) {
				n++;
				s+= couts.get(marque);
			}
		}
		return s/n;
	}

	/**
	 * @author Ghaly
	 * @return le prix de la marque 
	 */
	protected double getCout(ChocolatDeMarque produit) {
		return couts.get(produit);
	}
	
	/**
	 * @author Theo
	 * @return le prix de la gamme associée à marque (par tonne)
	 */
	protected double getCout_gamme(ChocolatDeMarque marque) {
		Chocolat gamme = marque.getChocolat();
		if (gamme == Chocolat.C_BQ) {
			return cout_BQ;
		}
		if (gamme == Chocolat.C_MQ) {
			return cout_MQ;
		}
		if (gamme == Chocolat.C_MQ_BE) {
			return cout_MQ_BE;
		}
		if (gamme == Chocolat.C_HQ_BE) {
			return cout_HQ_BE;
		}
		return cout_BQ;
	}
	
	/**
	 * @author Theo-ghaly
	 * Actualise les couts (par tonne)
	 */
	protected void actualise_indic_couts(ChocolatDeMarque marque) {
		Chocolat gamme = marque.getChocolat();
		double nv_prix = getCout_gamme(gamme);
		if (gamme== Chocolat.C_BQ ) {
			cout_BQ = nv_prix;
		}
		if (gamme ==  Chocolat.C_MQ_BE) {
			cout_MQ_BE = nv_prix;
		}
		if (gamme ==  Chocolat.C_MQ) {
			cout_MQ = nv_prix;
		}
		if (gamme ==  Chocolat.C_HQ_BE) {
			cout_HQ_BE = nv_prix;
		}
	}

	/**
	 * 	Actualisation des previsions de vente pour l'étape normalisée
	 * @author Theo,Ghaly
	 */
	public void actualiser_prevision(ChocolatDeMarque marque, int etape) {

		int etapepreced = etape-1;
		int etapeannee = (etapepreced/24)+1; //+1 car les etapes -1 a -24 constituent bien une annee prise en compte
		int etapenormalisee = (etapepreced+24)%24;
		HashMap<ChocolatDeMarque,Double> prevetap = previsions.get(etapenormalisee);
		//On remplace par la moyenne actualisee
		prevetap.replace(marque, (prevetap.get(marque)*etapeannee+Filiere.LA_FILIERE.getVentes(marque, etapepreced))/(etapeannee+1));
		previsions.replace(etapenormalisee, prevetap);
	}
	
	/**
	 * Actualisation des previsions persos
	 * @author Theo, Ghaly
	 */
	public void actualiser_prevision_perso(ChocolatDeMarque choco,  double quantite) {
		int etape_annee = Filiere.LA_FILIERE.getEtape()/24+1;
		int etapenormalisee = Filiere.LA_FILIERE.getEtape()%24;
		HashMap<ChocolatDeMarque,Double> prevetapeperso = previsionsperso.get(etapenormalisee);
		prevetapeperso.replace(choco, (prevetapeperso.get(choco)*etape_annee+quantite)/(etape_annee+1));
		previsionsperso.replace(etapenormalisee, prevetapeperso);
	}
	
	/**
	 * @author Theo and Ghaly
	 */
	public void initialiser() {
		
		cout_stockage_distributeur.setValeur(this, Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*16);
		
		//Initialisation des previsions
		this.previsions = new HashMap<Integer,HashMap<ChocolatDeMarque,Double>>();
		this.previsionsperso = new HashMap<Integer,HashMap<ChocolatDeMarque,Double>>(); 
		
		for (int i=0;i<24;i++) {
			HashMap<ChocolatDeMarque,Double> prevtour = new HashMap<ChocolatDeMarque,Double>();
			HashMap<ChocolatDeMarque,Double> prevtourperso = new HashMap<ChocolatDeMarque,Double>();
			for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
				prevtour.put(marque, Filiere.LA_FILIERE.getVentes(marque, -(i+1)));
				prevtourperso.put(marque, Filiere.LA_FILIERE.getVentes(marque, -(i+1))*0.5);
				//Pour l'initialisation, on estime vendre 50% des ventes totales (choix arbitraire pour démarrer
			}
			previsions.put(24-(i+1), prevtour);
			previsionsperso.put(24-(i+1), prevtourperso);
		}
	}

	public String getNom() {
		return "EQ7";
	}
	
	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////
	public String toString() {
		return this.getNom();
		}
	

	/**
	 * @author Romain,Ghaly et Theo
	 */
	public void next() {
		
		int etape = Filiere.LA_FILIERE.getEtape();
		
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			actualiser_prevision( marque,  etape);
		}
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(162, 207, 238); 
	}

	public String getDescription() {
		return "Bla bla bla";
	}


	/**
	 * Renvoie les indicateurs
	 * @author Ghaly 
	 */
	public List<Variable> getIndicateurs() {
		
		List<Variable> res = new ArrayList<Variable>();
		res.add(totalStocks);
		res.add(stock_HQ_BE);
		res.add(stock_MQ_BE);
		res.add(stock_BQ);
		res.add(stock_MQ);
		
		return res;
	
	}

	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		res.add(cout_stockage_distributeur);
		return res;
	}

	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(this.journal);
		res.add(this.journal_achat);
		res.add(this.journal_stock);
		return res;
	}

	////////////////////////////////////////////////////////
	//               En lien avec la Banque               //
	////////////////////////////////////////////////////////

/**
 * Methode appelee par la banque apres la creation du compte bancaire de l'acteur afin de lui communiquer le cryptogramme
 *  qui lui sera necessaire pour les operations bancaires
 */
	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;

	}
	
	/**
	 * Appelee lorsqu'un acteur fait faillite (potentiellement vous afin de vous en informer.
	 * @author Ghaly 
	 */
	public void notificationFaillite(IActeur acteur) {
		if (this==acteur) {
			System.out.println("They killed Chocorama... ");
		} else {
			System.out.println("try again "+acteur.getNom()+"... We ("+this.getNom()+") will not miss you.");
		}
	}

	/**
	 * Apres chaque operation sur votre compte bancaire, cette operation est appelee pour vous en informer
	 */
	public void notificationOperationBancaire(double montant) {
	}
	
	/**
	 *  Renvoie le solde actuel de l'acteur
	 */
	public double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme);
	}

	////////////////////////////////////////////////////////
	//        Pour la creation de filieres de test        //
	////////////////////////////////////////////////////////

	/**
	 *  Renvoie la liste des filieres proposees par l'acteur
	 */
	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		return(filieres);
	}

	/**
	 *  Renvoie une instance d'une filiere d'apres son nom
	 */
	public Filiere getFiliere(String nom) {
		return Filiere.LA_FILIERE;
	}

}
