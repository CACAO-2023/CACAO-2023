package abstraction.eq4Transformateur1;

// @author Francois / Alexian / fouad 

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.filiere.IMarqueChocolat;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.general.VariableReadOnly;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur1Acteur implements IActeur, IMarqueChocolat {
	public static Color COLOR_LLGRAY = new Color(238,238,238);
	public static Color COLOR_BROWN  = new Color(141,100,  7);
	public static Color COLOR_PURPLE = new Color(100, 10,115);
	public static Color COLOR_LPURPLE= new Color(155, 89,182);
	public static Color COLOR_GREEN  = new Color(  6,162, 37);
	public static Color COLOR_LGREEN = new Color(  6,255, 37);
	public static Color COLOR_LBLUE = new Color(  6,130,230);
	protected Journal journal;
	protected Journal journal_BOURSE;
	protected Journal journal_CC_PROD;
	protected Journal journal_CC_DISTRI;
	protected Journal journal_appel;
	protected int step; // tour du jeu 
	private Variable qualiteHaute;  // La qualite d'un chocolat de gamme haute 
	private Variable qualiteMoyenne;// La qualite d'un chocolat de gamme moyenne  
	private Variable qualiteBasse;  // La qualite d'un chocolat de gamme basse
	private Variable gainQualiteBioEquitable;// Le gain en qualite des chocolats bio equitables
	private Variable gainQualiteOriginal;// Le gain en qualite des chocolats originaux
	private Variable partMarqueQualitePercue;// Le gain en qualite des chocolats originaux
	private Variable pourcentageMinCacaoBQ; //Le pourcentage minimal de cacao dans un chocolat de basse qualite
	private Variable pourcentageMinCacaoMQ; //Le pourcentage minimal de cacao dans un chocolat de moyenne qualite
	private Variable pourcentageMinCacaoHQ; //Le pourcentage minimal de cacao dans un chocolat de haute qualite
	private Variable partCacaoQualitePercue ;//L'impact d'un % de cacao plus eleve dans la qualite percue du chocolat
	private Variable pourcentageRSEmax;//Le pourcentage de reversion RSE pour un impact max sur la qualite percue
	private Variable partRSEQualitePercue;//L'impact de pourcentageRSEmax% du prix consacres aux RSE dans la qualite percue du chocolat
	private Variable coutStockageProducteur;//Le cout moyen du stockage d'une Tonne a chaque step chez un producteur de feves

	protected Variable totalStocksFeves;
	protected Variable totalStocksChocoMarque;  // La quantite totale de stock de chocolat de marque 
	protected List<Feve> lesFeves;
	protected Integer cryptogramme;
	protected ArrayList<ExemplaireContratCadre> ContratEnCours_F_BQ;
	protected ArrayList<ExemplaireContratCadre> ContratEnCours_F_HQ;
	protected ArrayList<ExemplaireContratCadre> ContratEnCours_C_BQ;
	protected ArrayList<ExemplaireContratCadre> ContratEnCours_C_HQ;
	protected Variable NbContratEnCours;
	protected Variable PartDeMarcheHQ;
	protected Variable PartDeMarcheBQ;
	protected Variable PrixMoyF_HQ;
	protected Variable PrixMoyF_BQ;


	
	public Transformateur1Acteur() {
		this.qualiteHaute   = new VariableReadOnly("qualite haute", "<html>Qualite du chocolat<br>de gamme haute</html>",this, 80.0, 95.0, 94.0); //(min, max, valeur init) 
		this.qualiteMoyenne = new VariableReadOnly("qualite moyenne", "<html>Qualite du chocolat<br>de gamme moyenne</html>",this, 0.0, 10.0, 2.0);
		this.qualiteBasse   = new VariableReadOnly("qualite basse", "<html>Qualite du chocolat<br>de gamme basse</html>",this, 40.0, 60.0, 42.0);
		this.gainQualiteBioEquitable  = new VariableReadOnly("gain qualite bioequitable", "<html>Gain en qualite des<br>chocolats bio equitables</html>",this, 0.0, 5.0, 3.5);
		this.gainQualiteOriginal  = new VariableReadOnly("gain qualite original", "<html>Gain en qualite des<br>chocolats originaux</html>",this, 0.0, 5.0, 0.5);
		this.partMarqueQualitePercue  = new VariableReadOnly("impact marque qualite percue", "<html>% de la qualite percue de la marque dans la qualite percue du chocolat</html>",this, 0.0, 0.5, 0.3);

		this.pourcentageMinCacaoBQ  = new VariableReadOnly("pourcentage min cacao BQ", "<html>Le pourcentage minimal de cacao dans un chocolat de basse qualite</html>",this, 30.0, 50.0, 40.0);
		this.pourcentageMinCacaoMQ  = new VariableReadOnly("pourcentage min cacao MQ", "<html>Le pourcentage minimal de cacao dans un chocolat de moyenne qualite</html>",this, 50.0, 65.0, 65.0);
		this.pourcentageMinCacaoHQ  = new VariableReadOnly("pourcentage min cacao HQ", "<html>Le pourcentage minimal de cacao dans un chocolat de haute qualite</html>",this, 65.0, 90.0, 80.0);
		this.partCacaoQualitePercue = new VariableReadOnly("impact cacao qualite percue", "<html>L'impact d'un % de cacao plus eleve dans la qualite percue du chocolat</html>",this, 0.0, 0.5, 0.3);
		
		this.pourcentageRSEmax    = new VariableReadOnly("pourcentage rse max", "<html>Le pourcentage de reversion RSE pour un impact max sur la qualite percue</html>",this, 5.0, 15.0, 10.0);
		this.partRSEQualitePercue = new VariableReadOnly("impact rse qualite percue", "<html>L'impact de 25% du prix consacres aux RSE dans la qualite percue du chocolat</html>",this, 0.0, 0.5, 0.3);

		this.coutStockageProducteur = new VariableReadOnly("cout moyen stockage producteur", "<html>Le cout moyen du stockage d'une Tomme de produit chez un producteur</html>",this, 150.0, 240.0, 200.0); //stockage 4* producteur 
		this.journal = new Journal("Journal "+this.getNom(), this);
		this.journal_BOURSE = new Journal("Journal "+this.getNom() + " achat bourse", this);
		this.journal_CC_PROD = new Journal("Journal "+this.getNom() + " achat CC producteur", this);
		this.journal_CC_DISTRI = new Journal("Journal "+this.getNom() + " vente CC distributeur", this);
		this.journal_appel = new Journal("Journal "+this.getNom() + " vente appel d'offre", this);
		this.totalStocksFeves = new VariablePrivee("Eq4StockFeves", "<html>Quantite totale de feves en stock</html>",this, 0.0, 1000000.0, 0.0);this.totalStocksFeves= new VariablePrivee("Eq4StockFeves", "<html>Quantite totale de feves en stock</html>",this, 0.0, 1000000.0, 0.0);
//		this.totalStocksChoco = new VariablePrivee("Eq4StockChoco", "<html>Quantite totale de chocolat en stock</html>",this, 0.0, 1000000.0, 0.0); 
		this.totalStocksChocoMarque = new VariablePrivee("Eq4StockChocoMarque", "<html>Quantite totale de chocolat de marque en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.NbContratEnCours = new VariablePrivee("Eq4NbContratEnCours","<html>Nombre de contrat cadre actuellement en cours</html>",this,0.0,200.0,0.0);
		this.PartDeMarcheBQ = new VariablePrivee("Eq4PartDeMarcheBQ","<html>Part de marché sur les ventes de chocolat basse qualité</html>",this,0.0,1.1,0.0);
		this.PartDeMarcheHQ = new VariablePrivee("Eq4PartDeMarcheHQ","<html>Part de marché sur les ventes de chocolat haute qualité</html>",this,0.0,1.1,0.0);
		this.PrixMoyF_BQ = new VariablePrivee("Eq4PrixMoyF_BQ","<html>Prix moyen de nos contrat cadre de fève basse qualité</html>",this,0.0,100000.0,0.0);
		this.PrixMoyF_HQ = new VariablePrivee("Eq4PrixMoyF_HQ","<html>Prix moyen de nos contrat cadre de fève haute qualité</html>",this,0.0,100000.0,0.0);
	}
	
	//François Glavatkii
	
	public void initialiser() {
		this.lesFeves = new LinkedList<Feve>();
		this.journal.ajouter("Les Feves sont :");
		for (Feve f : Feve.values()) {
			this.lesFeves.add(f);
			this.journal.ajouter("   - "+f);
		}
		
		this.step = 0;
		this.ContratEnCours_C_BQ = new ArrayList<ExemplaireContratCadre> ();
		this.ContratEnCours_C_HQ = new ArrayList<ExemplaireContratCadre> ();
		this.ContratEnCours_F_BQ = new ArrayList<ExemplaireContratCadre> ();
		this.ContratEnCours_F_HQ = new ArrayList<ExemplaireContratCadre> ();	
	}
	

	public String getNom() {// NE PAS MODIFIER
		return "EQ4";
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	//François Glavatkii
	
	public void next() {
		this.step += 1;
		this.journal.ajouter("step:" + this.step);
	
	}
		

	public Color getColor() {// NE PAS MODIFIER
		return new Color(229, 243, 157); 
	}
	public String toString() {
		return this.getNom();
	}

	public String getDescription() {
		return "Pour les produits bas de gamme vendus sous marque distributeur, notre objectif est de maximiser les ventes en proposant des prix comp�titifs. Nous comparons les prix propos�s par tous les producteurs et la bourse afin d'acheter le cacao au meilleur prix. Les producteurs sont s�lectionnes par contrat cadre ainsi que les distributeurs. Nous envisageons �galement des principes de solde et de promotion pour �couler les stocks plus rapidement, � choisir entre la p�riode des f�tes ou non.\r\n"
				+ "Pour les produits haut de gamme, changement, nous allons opter pour vendre en CC aux distrbuteurs (qui donne le meilleur prix) sous notre marque avec une très grande qualité. Nous signons des contrats cadres avec les producteurs s�lectionn�s pour garantir la qualit� de notre cacao. Nous prenons �galement en compte l'avis des clients pour d�cider si nous optons pour une approche haut de gamme ou moyenne gamme en mati�re de RSE. En cas de surplus de stock, nous pouvons envisager de vendre en appel d'offre. Notre objectif n'est pas de faire de grosses marges, mais de vendre en quantit� suffisante pour maintenir notre entreprise durable et responsable.";
	}

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		res.add(this.totalStocksFeves);
		res.add(this.totalStocksChocoMarque);
		res.add(this.NbContratEnCours);
		res.add(this.PartDeMarcheBQ);
		res.add(this.PartDeMarcheHQ);
		res.add(this.PrixMoyF_BQ);
		res.add(this.PrixMoyF_HQ);
		return res;
	}

	// Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	// Renvoie les journaux
	public List<Journal> getJournaux() {
		List<Journal> res = new LinkedList<Journal>();
		res.add(this.journal);
		res.add(this.journal_CC_PROD);
		res.add(this.journal_BOURSE);
		res.add(this.journal_CC_DISTRI);
		res.add(this.journal_appel);
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

	public List<String> getMarquesChocolat() {
		LinkedList<String> marques = new LinkedList<String>();
		marques.add("Vccotioi");
		marques.add("Yocttotoa");
		return marques;
	}
}
