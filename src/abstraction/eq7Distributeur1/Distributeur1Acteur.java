package abstraction.eq7Distributeur1;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Courbe;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import presentation.secondaire.FenetreGraphique;

public class Distributeur1Acteur  implements IActeur, PropertyChangeListener {
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
	protected Journal Bilan_achat;
	protected Journal journal_stock;
	protected Journal journal_vente;
	
	
	//On est oblige de mettre les variables ici sinon la creation de la filiere est dans un tel ordre que nous n'y avons pas acces assez tot
	protected Variable totalStocks = new VariablePrivee("Eq7TotalStocks", "<html>Quantite totale de chocolat (de marque) en stock</html>",this, 0.0, 1000000.0, 0.0);
	//La quantité totale de stock de chocolat 
	protected Variable stock_BQ = new Variable("Eq7stock_BQ", "Stock total de chocolat de basse qualité", this, 0);
	protected Variable stock_MQ = new Variable("Eq7stock_MQ", "Stock total de chocolat de moyenne qualité", this, 0);
	protected Variable stock_MQ_BE = new Variable("Eq7stock_MQ_BE", "stock Total de chocolat de moyenne qualité bio-équitable", this, 0);
	protected Variable stock_HQ_BE = new Variable("Eq7stock_HQ_BE", "stock Total de chocolat de haute qualité bio-équitable", this, 0);
	protected Variable ventes = new Variable("Eq7ventes","ventes totales réalisées lors de ce tour",this,0);
	protected Variable depenses = new Variable("Eq7Depenses à l'étape courante", "Depenses totales de ce tour ",this, 0);
	protected Variable cmSelectionnee; // l'index du chocolat selectionne
	protected HashMap<ChocolatDeMarque, Variable> Var_Stock_choco; // le stock de chaque chocolat de marque
	protected HashMap<ChocolatDeMarque, Variable> Var_Cout_Choco; // le cout de chaque chocolat de marque
	protected HashMap<ChocolatDeMarque, Variable> Var_Marge_Choco; // la marge de chaque chocolat de marque
	protected HashMap<ChocolatDeMarque, Variable> Var_Vente_Choco; // la vente de chaque chocolat de marque
	protected HashMap<ChocolatDeMarque, Variable> Var_nbr_Vente_Choco; // la quantité vendue de chaque chocolat de marque
	protected FenetreGraphique graphique;
	protected Variable afficher_vente_depense_courrant; 
	

	protected double qte_totale_en_vente;
	protected double vente_step; //variable représentant la somme des vente au step courrant
	protected Variable marge_Choco_marque_selectionnee = new Variable("Eq7_marge_Choco_marque_selectionnee", "marge Total de la marque de chocolat sélectionnée grâce à cmselectionne", this, 0);
	protected Variable cout_Choco_marque_selectionnee = new Variable("Eq7_cout_Choco_marque_selectionnee", "cout Total de la marque de chocolat sélectionnée grâce à cmselectionne", this, 0);	
	protected Variable stock_Choco_marque_selectionnee = new Variable("Eq7_stock_Choco_marque_selectionnee", "stock Total de la marque de chocolat sélectionnée grâce à cmselectionne", this, 0);
	protected Variable Vente_Choco_marque_selectionnee = new Variable("Eq7_vente_Choco_marque_selectionnee", "vente Total de la marque de chocolat sélectionnée grâce à cmselectionne", this, 0);
	protected Variable Vente_nbr_Choco_marque_selectionnee = new Variable("Eq7_quantite_vendue_Choco_marque_selectionnee", "nombre d'articles vente Total de la marque de chocolat sélectionnée grâce à cmselectionne", this, 0);

	protected Variable solde_bancaire = new Variable("Eq7_solde_bancaire","solde_bancaire",this,0);
	
	protected HashMap<ChocolatDeMarque, Courbe> courbes; 

	protected Courbe cventes = new Courbe("ventes ");
	protected Courbe cdepense = new Courbe("depenses ");
	
	
	private List<ChocolatDeMarque>chocolatsDeMarquesProduits; // init dans initialiser

	/**
	 * donne les quantités mini pour un contrat cadre
	 * @author ghaly
	 */
	double quantite_min_cc = SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER;
	
	protected double valeur_stock_initial = 10000.;
	
	protected HashMap<IActeur, Integer> cc_sans_vendeur ;
	protected HashMap<IActeur, Integer> cc_vendus ;
	protected HashMap<IActeur, Integer> cc_non_aboutis ;


	
	/**
	 * previsions de vente de l'equipe 7
	 * on suppose qu'on vend à chaque étape
	 * prevision etape -> marque -> valeur
	 */
	protected HashMap<Integer,HashMap<ChocolatDeMarque,Double>> previsionsperso; 
	


	/**
	 * Cout en fonction du chocolat, pour 1t
	 */
	protected HashMap<Chocolat,Double> cout_chocolat = new HashMap<Chocolat,Double>();
	
	/**
	 * nombre d'achat en contrat cadre, ça servira à calculer la moyenne des couts
	 */
	protected HashMap<ChocolatDeMarque,Integer> nombre_achats = new HashMap<ChocolatDeMarque,Integer>();; 

	protected Variable cout_stockage_distributeur = new Variable("cout moyen stockage distributeur", this);
	protected Variable cout_main_doeuvre_distributeur = new Variable("cout de la main d'oeuvre pour les distributeur", this); //cout total par tour

	protected LinkedList<VariablePrivee> liste = new LinkedList<VariablePrivee>();
	protected int cryptogramme;
	

	public Distributeur1Acteur() {
		this.journal = new Journal("Journal "+this.getNom(), this);
	    this.journal_achat=new Journal("Journal des Achats de l'" + this.getNom(),this);
	    this.Bilan_achat=new Journal("bilan des Achats de l'" + this.getNom(),this);
	    this.journal_stock = new Journal("Journal des Stocks de l'" + this.getNom(),this);
	    this.journal_vente = new Journal("Journal des ventes de l'" + this.getNom(),this);
		this.cmSelectionnee = new Variable(getNom()+" chocolat de marque selectionné", "indiquez l'index du chocolat de marque", this, 0.0);
		this.afficher_vente_depense_courrant = new Variable(getNom()+" évolution des dépenses et ventes de la marque selectionnée", "indiquez l'index du chocolat de marque", this, 0.0);
		
	}
	

	
	////////////////////////////////////////////////////////
	//         Methodes principales				          //
	////////////////////////////////////////////////////////
	

	/**
	 * @author Theo
	 * Renvoie les previsions de vente de notre quipe, actualisees à chaque tour
	 */
	protected double getPrevisionsperso(ChocolatDeMarque marque, Integer etape) {
		return previsionsperso.get(etape).get(marque);
	}
	
	/**
	 * @author Theo
	 * @return le prix de la gamme associée à marque (par tonne)
	 */
	protected double getCout_gamme(ChocolatDeMarque marque) {
		Chocolat gamme = marque.getChocolat();
		return cout_chocolat.get(gamme);
	}
	/**
	 * @author ghaly
	 * met a jour la valeur de la variable associée à la marque de chocolat
	 * 
	 */
	public void mettre_a_jour(HashMap<ChocolatDeMarque, Variable> h_var, ChocolatDeMarque marque, double montant) {
		Variable var = h_var.get(marque);
		var.setValeur(this, montant);
		h_var.replace(marque, var);
	}
	
	/**
	 * retourne la valeur de la variable associée à la marque de chocolat
	 */
	public double get_valeur(HashMap<ChocolatDeMarque, Variable> h_var, ChocolatDeMarque marque) {
		Variable var = h_var.get(marque);
		return var.getValeur();
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
		
		
		cout_chocolat.put(Chocolat.C_HQ_BE, 25000.);
		cout_chocolat.put(Chocolat.C_MQ_BE, 20000.);
		cout_chocolat.put(Chocolat.C_MQ, 15000.);
		cout_chocolat.put(Chocolat.C_BQ, 10000.);

		
		this.cc_sans_vendeur= new  HashMap<IActeur, Integer>()  ;
		this.cc_non_aboutis= new  HashMap<IActeur, Integer> () ;
		this.cc_vendus= new  HashMap<IActeur, Integer> () ;
		vente_step=0;
		this.chocolatsDeMarquesProduits = Filiere.LA_FILIERE.getChocolatsProduits();

		this.Var_Stock_choco = new HashMap<ChocolatDeMarque, Variable> ();
		this.Var_Cout_Choco = new HashMap<ChocolatDeMarque, Variable> ();
		this.Var_Marge_Choco = new HashMap<ChocolatDeMarque, Variable> ();
		this.Var_Vente_Choco = new HashMap<ChocolatDeMarque, Variable> ();
		this.Var_nbr_Vente_Choco = new HashMap<ChocolatDeMarque, Variable> ();
	
		cventes.setCouleur(Color.green);
		cdepense.setCouleur(Color.red);
		
		for (ChocolatDeMarque cm : chocolatsDeMarquesProduits) {
			totalStocks.setValeur(this, totalStocks.getValeur()+valeur_stock_initial, this.cryptogramme);
			nombre_achats.put(cm, 0);
			journal_stock.ajouter("Stock de "+cm+" : "+ valeur_stock_initial +" T");
			this.Var_Stock_choco.put(cm, new Variable("le stock de la marque "+cm.getNom(), "le stock de la marque "+cm.getNom(), this, valeur_stock_initial));
			this.Var_Marge_Choco.put(cm, new Variable("la marge de la marque "+cm.getNom(), "la marge de la marque "+cm.getNom(), this, 0.0));
			this.Var_Cout_Choco.put(cm, new Variable("le cout de la marque "+cm.getNom(), "le cout de la marque "+cm.getNom(), this, 0.0));
			this.Var_Vente_Choco.put(cm, new Variable("le nombre de vente de la marque "+cm.getNom(), "le nombre de vente de la marque "+cm.getNom(), this, 0.0));
			this.Var_nbr_Vente_Choco.put(cm, new Variable("la quantite vendue de la marque "+cm.getNom(), "la quantite vendue de la marque "+cm.getNom(), this, 0.0));
			
		}
		this.cventes.ajouter(Filiere.LA_FILIERE.getEtape(), this.ventes.getValeur());
		this.cdepense.ajouter(Filiere.LA_FILIERE.getEtape(), this.depenses.getValeur());
		this.marge_Choco_marque_selectionnee.cloner(Var_Marge_Choco.get(chocolatsDeMarquesProduits.get(0)));
		this.cout_Choco_marque_selectionnee.cloner(Var_Cout_Choco.get(chocolatsDeMarquesProduits.get(0)));
		this.stock_Choco_marque_selectionnee.cloner(this.Var_Stock_choco.get(chocolatsDeMarquesProduits.get(0))); // initialement c'est le premier chocolat de marque qui dont le stock est affiche
		this.Vente_Choco_marque_selectionnee.cloner(this.Var_Vente_Choco.get(chocolatsDeMarquesProduits.get(0))); // initialement c'est le premier chocolat de marque qui dont le stock est affiche
		this.Vente_nbr_Choco_marque_selectionnee.cloner(this.Var_nbr_Vente_Choco.get(chocolatsDeMarquesProduits.get(0))); // initialement c'est le premier chocolat de marque qui dont le stock est affiche
		
		this.cmSelectionnee.addObserver(this);
		this.afficher_vente_depense_courrant.addObserver(this);
		
		/////////////////////////////////////
		//POTENTIELLEMENT à Changer
		cout_main_doeuvre_distributeur.setValeur(this, Filiere.LA_FILIERE.getParametre("cout mise en rayon").getValeur());
		///////////////////////////////////////
		
		//Initialisation des couts
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			mettre_a_jour(Var_Cout_Choco, marque,getCout_gamme(marque) );
		}
		//Initialisation des previsions
		this.previsionsperso = new HashMap<Integer,HashMap<ChocolatDeMarque,Double>>(); 
		
		for (int i=0;i<24;i++) {
			HashMap<ChocolatDeMarque,Double> prevtour = new HashMap<ChocolatDeMarque,Double>();
			HashMap<ChocolatDeMarque,Double> prevtourperso = new HashMap<ChocolatDeMarque,Double>();
			for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
				prevtour.put(marque, Filiere.LA_FILIERE.getVentes(marque, -(i+1)));
				prevtourperso.put(marque, Filiere.LA_FILIERE.getVentes(marque, -(i+1))*0.5);
				//Pour l'initialisation, on estime vendre 50% des ventes totales (choix arbitraire pour démarrer)
			}
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
			actualiser_prevision_perso( marque,  etape);

		}
		ventes.setValeur(this, vente_step);

		this.cventes.ajouter(Filiere.LA_FILIERE.getEtape(), this.ventes.getValeur());
		this.cdepense.ajouter(Filiere.LA_FILIERE.getEtape(), this.depenses.getValeur());
		vente_step = 0 ; //réinitialiser la variable pour la prochaine étape
		actualise_variable_selectionnee();
		

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
		res.add(cmSelectionnee);
		res.add(afficher_vente_depense_courrant);
		res.add(totalStocks);
		res.add(stock_HQ_BE);
		res.add(stock_MQ_BE);
		res.add(stock_BQ);
		res.add(stock_MQ);
		res.add(ventes);
		res.add(depenses);
		res.add(stock_Choco_marque_selectionnee);
		res.add(cout_Choco_marque_selectionnee);
		res.add(marge_Choco_marque_selectionnee);
		res.add(Vente_Choco_marque_selectionnee);
		res.add(Vente_nbr_Choco_marque_selectionnee);
		
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
		res.add(this.Bilan_achat);
		res.add(this.journal_stock);
		res.add(this.journal_vente);
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
			System.out.println("Try again "+acteur.getNom()+"... We ("+this.getNom()+") will not miss you.");
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
	
	/**
	 * @author ghaly
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		int index = (int)(this.cmSelectionnee.getValeur());
		if (index<0 || index>=Filiere.LA_FILIERE.getChocolatsProduits().size()) {
			index=0;
			this.cmSelectionnee.setValeur(this, index);
		}
		this.stock_Choco_marque_selectionnee.cloner(this.Var_Stock_choco.get(this.chocolatsDeMarquesProduits.get(index)));
		this.marge_Choco_marque_selectionnee.cloner(this.Var_Marge_Choco.get(this.chocolatsDeMarquesProduits.get(index)));
		this.cout_Choco_marque_selectionnee.cloner(this.Var_Cout_Choco.get(this.chocolatsDeMarquesProduits.get(index)));
		this.Vente_Choco_marque_selectionnee.cloner(this.Var_Vente_Choco.get(this.chocolatsDeMarquesProduits.get(index)));
		this.Vente_nbr_Choco_marque_selectionnee.cloner(this.Var_nbr_Vente_Choco.get(this.chocolatsDeMarquesProduits.get(index)));
		
		System.out.println("Chocolat de marque selectionne :"+this.chocolatsDeMarquesProduits.get(index));
		int index_2 = (int)(this.afficher_vente_depense_courrant.getValeur());
		if(index_2==1) {
		afficher_graphique();
		}

	}
	
	
	public void afficher_graphique() {
		this.graphique= new FenetreGraphique("ventes et dépenses pendant le tour", 500,400);
		
		this.graphique.ajouter(cventes);
		this.graphique.ajouter(cdepense);
		this.graphique.setVisible(true);
	}
	/**
	 * @author ghaly
	 * actualise les valeurs des variables séléctionnées
	 */
	public void actualise_variable_selectionnee() {
		
		ChocolatDeMarque choco= chocolatsDeMarquesProduits.get( (int)(cmSelectionnee.getValeur() ));
		stock_Choco_marque_selectionnee.setValeur(this,get_valeur(Var_Stock_choco, choco)  );
		cout_Choco_marque_selectionnee.setValeur(this, get_valeur(Var_Cout_Choco, choco));
		marge_Choco_marque_selectionnee.setValeur(this, get_valeur(Var_Marge_Choco, choco));
		Vente_Choco_marque_selectionnee.setValeur(this, get_valeur(Var_Vente_Choco, choco));
		Vente_nbr_Choco_marque_selectionnee.setValeur(this, get_valeur(Var_nbr_Vente_Choco, choco));
		
		
	}
}
