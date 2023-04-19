package abstraction.eq7Distributeur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;






public class Distributeur1Acteur implements IActeur {
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

//	private Variable qualiteHaute;  // La qualite d'un chocolat de gamme haute 
//	private Variable qualiteMoyenne;// La qualite d'un chocolat de gamme moyenne  
//	private Variable qualiteBasse;  // La qualite d'un chocolat de gamme basse
//	private Variable pourcentageRSEmax;//Le pourcentage de reversion RSE pour un impact max sur la qualite percue
//	private Variable partRSEQualitePercue;//L'impact de pourcentageRSEmax% du prix consacres aux RSE dans la qualite percue du chocolat
//	private Variable coutStockageProducteur;//Le cout moyen du stockage d'une Tonne a chaque step chez un producteur de feves
	
//	protected int totalStocksCB;  // La quantité totale de stock de chocolat bas de gamme 
//	protected int totalStocksCML;  // La quantité totale de stock de chocolat moyenne gamme labellise
//	protected int totalStocksCMNL;  // La quantité totale de stock de chocolat moyenne gamme non labellise
//	protected int totalStocksCH;  // La quantité totale de stock de chocolat haute gamme
	protected Variable totalStocks;  // La quantité totale de stock de chocolat
	
	protected double cout_BQ; //Cout d'1t de chocolat basse gamme
	protected double cout_MQ_BE; //Cout d'1t de chocolat moyenne gamme labellise
	protected double cout_MQ; //Cout d'1t de chocolat moyenne gamme non labellise
	protected double cout_HQ_BE; //Cout d'1t de chocolat haute gamme labellise
	

	////////////////////////////////////////
	protected HashMap<Chocolat, Double> stockChoco;

	protected HashMap<ChocolatDeMarque,Double> stockChocoMarque; //stock de chaque marque en tonne
	protected HashMap<Integer,HashMap<ChocolatDeMarque,Double>> previsions; //previsions de ventes de la filiere globale
	protected HashMap<Integer,HashMap<ChocolatDeMarque,Double>> previsionsperso; //previsions de vente perso
	protected HashMap<ChocolatDeMarque,List<Double>> couts; //couts
	protected Variable stock_BQ = new VariablePrivee("Eq7stock_BQ", "Stock total de chocolat de basse qualité", this, 0);
	protected Variable stock_MQ = new VariablePrivee("Eq7stock_MQ", "Stock total de chocolat de moyenne qualité", this, 0);
	protected Variable stock_MQ_BE = new VariablePrivee("Eq7stock_MQ_BE", "stock Total de chocolat de moyenne qualité bio-équitable", this, 0);
	protected Variable stock_HQ_BE = new VariablePrivee("Eq7stock_HQ_BE", "stock Total de chocolat de haute qualité bio-équitable", this, 0);
//	protected LinkedList<VariablePrivee> indicateurs = indicateurs();
	protected int cryptogramme;


	public Distributeur1Acteur() {
		this.cout_BQ = 3;
		this.cout_HQ_BE = 3;
		this.cout_MQ_BE = 3;
		this.cout_MQ = 3;
		this.totalStocks = new VariablePrivee("Eq7TotalStocks", "<html>Quantite totale de chocolat (de marque) en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.journal = new Journal("Journal "+this.getNom(), this);
	    this.journal_achat=new Journal("Journal des Achats de l'" + this.getNom(),this);
	    this.journal_stock = new Journal("Journal des Stocks del'" + this.getNom(),this);

	}
	
	
	public LinkedList<VariablePrivee> indicateurs(){
		
	    LinkedList<VariablePrivee> liste = new LinkedList<VariablePrivee>();
	    List<String> liste_marques = Filiere.LA_FILIERE.getMarquesChocolat();
	    for (int i = 0; i < liste_marques.size(); i++) {
	    	String marque = liste_marques.get(i);
	        liste.add(new VariablePrivee(marque, "Stock total de "+ marque, this, 0)); // appel de la méthode getNom() sur l'objet ChocolatDeMarque
	    }
	    return liste;}
	
	////////////////////////////////////////////////////////
	//         Methodes principales				          //
	////////////////////////////////////////////////////////
	
	/**
	 * @author Theo
	 * Renvoie les previsions, actualisees à chaque tour
	 */
	protected double previsions(ChocolatDeMarque marque, Integer etape) {
		return previsions.get(etape).get(marque);
	}
	
	protected double previsionsperso(ChocolatDeMarque marque, Integer etape) {
		return previsionsperso.get(etape).get(marque);
	}
	
	/**
	 * @author ghaly
	 * @return moyenne de la liste passée en commentaire
	 */
	public double moyenne(List<Double> l) {
		int n = 0;
		double s = 0;
		for (double d :l) {
			n++;
			s+=d;
			
		}
		return s/n;
	}
	/**
	 * @author ghaly
	 */
	protected double getCout_gamme(Gamme gamme) {
		int n = 0;
		double s = 0;
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (marque.getGamme()==gamme) {
				n++;
				s+= moyenne(couts.get(marque));
			}
		}
		return s/n;
		
	}
	/**
	 * @author Theo
	 * @return le prix de la gamme associée à marque
	 */
	protected double getCout(ChocolatDeMarque marque) {
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
	protected void couts(ChocolatDeMarque marque) {
		Gamme gamme = marque.getGamme();
		double nv_prix = getCout_gamme(gamme);
		if (gamme== Gamme.BQ ) {
			cout_BQ = nv_prix;
		}
		if (gamme ==  Gamme.MQ && marque.isBioEquitable()) {
			cout_MQ_BE = nv_prix;
		}
		if (gamme ==  Gamme.MQ && !marque.isBioEquitable()) {
			cout_MQ = nv_prix;
		}
		if (gamme ==  Gamme.HQ) {
			cout_HQ_BE = nv_prix;
		}
	}
	
	
	/**
	 * @author Theo and Ghaly
	 */
	public void initialiser() {

		
		//Initialisation des stocks
		this.stockChocoMarque = new HashMap<ChocolatDeMarque,Double>();
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			stockChocoMarque.put(marque,100000.);
			journal_stock.ajouter("Stock de "+marque+" : "+stockChocoMarque.get(marque)+" T");
		}
		
		//Initialisation des previsions
		//le probleme est ici que ces previsions concernent l'ensemble de la filiere et non pas juste notre acteur
		//il faut creer un autre fonction car notre part de vente depend de la marque et plus generalement de la gamme
		this.previsions = new HashMap<Integer,HashMap<ChocolatDeMarque,Double>>();
		this.previsionsperso = new HashMap<Integer,HashMap<ChocolatDeMarque,Double>>(); 
		for (int i=0;i<24;i++) {
			HashMap<ChocolatDeMarque,Double> prevtour = new HashMap<ChocolatDeMarque,Double>();
			HashMap<ChocolatDeMarque,Double> prevtourperso = new HashMap<ChocolatDeMarque,Double>();
			for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
				prevtour.put(marque, Filiere.LA_FILIERE.getVentes(marque, -(i+1)));
				prevtourperso.put(marque, Filiere.LA_FILIERE.getVentes(marque, -(i+1))*0.5);
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
		
		//Actualisation des previsions
		int etapepreced = Filiere.LA_FILIERE.getEtape()-1;
		int etapenormalisee = (etapepreced+24)%24;
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			HashMap<ChocolatDeMarque,Double> prevetap = previsions.get(etapenormalisee);
			prevetap.replace(marque, (prevetap.get(marque)+Filiere.LA_FILIERE.getVentes(marque, etapepreced))/2);
			previsions.replace(etapenormalisee, prevetap);
		}
		//Actualisation du stock total
		double newstock = 0.;
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			newstock += stockChocoMarque.get(marque);
		}
		totalStocks.setValeur(this, newstock, this.cryptogramme);

		//Prise en compte des couts de stockage
		if (totalStocks.getValeur()*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur() > 0) {
			Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), totalStocks.getValeur()*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());	
			journal.ajouter("Cout de stockage : "+totalStocks.getValeur()*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		}
		//Journaux
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			journal_stock.ajouter("Stock de "+marque+" : "+stockChocoMarque.get(marque)+" T");
		}
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(162, 207, 238); 
	}

	public String getDescription() {
		return "Bla bla bla";
	}

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
//		LinkedList<VariablePrivee> list = indicateurs();
//		for (VariablePrivee v : list) {
//			res.add(v);
//		}

		res.add(totalStocks);
		/**
		res.add(stock_HQ_BE);
		res.add(stock_MQ_BE);
		res.add(stock_BQ);
		res.add(stock_MQ);
		*/
		return res;
	}

	// Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	// Renvoie les journaux
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

	// Appelee en debut de simulation pour vous communiquer 
	// votre cryptogramme personnel, indispensable pour les
	// transactions.
	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;

	}

	// Appelee lorsqu'un acteur fait faillite (potentiellement vous)
	// afin de vous en informer.
	public void notificationFaillite(IActeur acteur) {
		if (this==acteur) {
			System.out.println("They killed Chocorama... ");
		} else {
			System.out.println("try again "+acteur.getNom()+"... We will not miss you. "+this.getNom());
		}
	}

	// Apres chaque operation sur votre compte bancaire, cette
	// operation est appelee pour vous en informer
	public void notificationOperationBancaire(double montant) {
	}
	
	// Renvoie le solde actuel de l'acteur
	public double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme);
	}

	////////////////////////////////////////////////////////
	//        Pour la creation de filieres de test        //
	////////////////////////////////////////////////////////

	// Renvoie la liste des filieres proposees par l'acteur
	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		return(filieres);
	}

	// Renvoie une instance d'une filiere d'apres son nom
	public Filiere getFiliere(String nom) {
		return Filiere.LA_FILIERE;
	}

}
