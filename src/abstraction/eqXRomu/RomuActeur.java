package abstraction.eqXRomu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.appelsOffres.FiliereTestAO;
import abstraction.eqXRomu.bourseCacao.FiliereTestBourse;
import abstraction.eqXRomu.clients.FiliereTestClientFinal;
import abstraction.eqXRomu.contratsCadres.FiliereTestContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.filiere.IMarqueChocolat;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.general.VariableReadOnly;
import abstraction.eqXRomu.produits.Feve;

public class RomuActeur implements IActeur, IMarqueChocolat {
	public static Color COLOR_LLGRAY = new Color(238,238,238);
	public static Color COLOR_BROWN  = new Color(141,100,  7);
	public static Color COLOR_PURPLE = new Color(100, 10,115);
	public static Color COLOR_LPURPLE= new Color(155, 89,182);
	public static Color COLOR_GREEN  = new Color(  6,162, 37);
	public static Color COLOR_LGREEN = new Color(  6,255, 37);
	public static Color COLOR_LBLUE = new Color(  6,130,230);

	
	protected Integer cryptogramme;
	protected Journal journal;

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

	protected Variable totalStocksFeves;  // La qualite totale de stock de feves 
	protected Variable totalStocksChoco;  // La qualite totale de stock de chocolat 
	protected Variable totalStocksChocoMarque;  // La qualite totale de stock de chocolat de marque 
	protected List<Feve> lesFeves;
	
	public RomuActeur() {
		this.qualiteHaute   = new VariableReadOnly("qualite haute", "<html>Qualite du chocolat<br>de gamme haute</html>",this, 0.0, 10.0, 3.0);
		this.qualiteMoyenne = new VariableReadOnly("qualite moyenne", "<html>Qualite du chocolat<br>de gamme moyenne</html>",this, 0.0, 10.0, 2.0);
		this.qualiteBasse   = new VariableReadOnly("qualite basse", "<html>Qualite du chocolat<br>de gamme basse</html>",this, 0.0, 10.0, 1.0);
		this.gainQualiteBioEquitable  = new VariableReadOnly("gain qualite bioequitable", "<html>Gain en qualite des<br>chocolats bio equitables</html>",this, 0.0, 5.0, 0.5);
		this.gainQualiteOriginal  = new VariableReadOnly("gain qualite original", "<html>Gain en qualite des<br>chocolats originaux</html>",this, 0.0, 5.0, 0.5);
		this.partMarqueQualitePercue  = new VariableReadOnly("impact marque qualite percue", "<html>% de la qualite percue de la marque dans la qualite percue du chocolat</html>",this, 0.0, 0.5, 0.3);

		this.pourcentageMinCacaoBQ  = new VariableReadOnly("pourcentage min cacao BQ", "<html>Le pourcentage minimal de cacao dans un chocolat de basse qualite</html>",this, 30.0, 45.0, 40.0);
		this.pourcentageMinCacaoMQ  = new VariableReadOnly("pourcentage min cacao MQ", "<html>Le pourcentage minimal de cacao dans un chocolat de moyenne qualite</html>",this, 45.0, 60.0, 60.0);
		this.pourcentageMinCacaoHQ  = new VariableReadOnly("pourcentage min cacao HQ", "<html>Le pourcentage minimal de cacao dans un chocolat de haute qualite</html>",this, 60.0, 90.0, 80.0);
		this.partCacaoQualitePercue = new VariableReadOnly("impact cacao qualite percue", "<html>L'impact d'un % de cacao plus eleve dans la qualite percue du chocolat</html>",this, 0.0, 0.5, 0.3);
		
		this.pourcentageRSEmax    = new VariableReadOnly("pourcentage rse max", "<html>Le pourcentage de reversion RSE pour un impact max sur la qualite percue</html>",this, 5.0, 30.0, 20.0);
		this.partRSEQualitePercue = new VariableReadOnly("impact rse qualite percue", "<html>L'impact de 25% du prix consacres aux RSE dans la qualite percue du chocolat</html>",this, 0.0, 0.5, 0.3);

		this.coutStockageProducteur = new VariableReadOnly("cout moyen stockage producteur", "<html>Le cout moyen du stockage d'une Tomme de produit chez un producteur</html>",this, 0.0, 3.0, 1.5);
		this.journal = new Journal("Journal "+this.getNom(), this);
		this.totalStocksFeves = new VariablePrivee("EqXStockFeves", "<html>Quantite totale de feves en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.totalStocksChoco = new VariablePrivee("EqXStockChoco", "<html>Quantite totale de chocolat en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.totalStocksChocoMarque = new VariablePrivee("EqXStockChocoMarque", "<html>Quantite totale de chocolat de marque en stock</html>",this, 0.0, 1000000.0, 0.0);
	}

	//========================================================
	//                         IActeur
	//========================================================


	public void initialiser() {
		this.lesFeves = new LinkedList<Feve>();
		this.journal.ajouter("Les Feves sont :");
		for (Feve f : Feve.values()) {
			this.lesFeves.add(f);
			this.journal.ajouter("   - "+f);
		}
	}
	
	public String getNom() {
		return "EQX";
	}

	public String getDescription() {
		return "Villors";
	}

	public Color getColor() {
		return new Color(165,165,165);
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
			
	}

	public List<Variable> getIndicateurs() {
		List<Variable> res =  new ArrayList<Variable>();
		res.add(this.totalStocksFeves);
		res.add(this.totalStocksChoco);
		res.add(this.totalStocksChocoMarque);
		return res;
	}
	
	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}

	public List<Variable> getParametres() {
		List<Variable> p= new ArrayList<Variable>();
		p.add(this.qualiteHaute);
		p.add(this.qualiteMoyenne);
		p.add(this.qualiteBasse);
		p.add(this.gainQualiteBioEquitable);
		p.add(this.gainQualiteOriginal); 
		p.add(this.partMarqueQualitePercue);
		p.add(this.pourcentageMinCacaoBQ);
		p.add(this.pourcentageMinCacaoMQ);
		p.add(this.pourcentageMinCacaoHQ);
		p.add(this.partCacaoQualitePercue);
		p.add(this.pourcentageRSEmax);
		p.add(this.partRSEQualitePercue);
		p.add(this.coutStockageProducteur);
		return p;
	}

	public List<Journal> getJournaux() {
		List<Journal> res = new LinkedList<Journal>();
		res.add(this.journal);
		return res;
	}

	public void notificationFaillite(IActeur acteur) {
		if (this==acteur) {
			System.out.println("They killed Romu... ");
		} else {
			System.out.println("Poor "+acteur.getNom()+"... We will miss you. "+this.getNom());
		}
	}

	public void notificationOperationBancaire(double montant) {
	}

	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		filieres.add("TESTCLIENT"); 
		filieres.add("TESTCC"); 
		filieres.add("TESTBOURSE"); 
		filieres.add("TESTAO"); 
		return filieres;
	}

	public Filiere getFiliere(String nom) {
		switch (nom) { 
		case "TESTCLIENT" : return new FiliereTestClientFinal();
		case "TESTCC" : return new FiliereTestContratCadre();
		case "TESTBOURSE" : return new FiliereTestBourse();
		case "TESTAO" : return new FiliereTestAO();
		default : return null;
		}
	}
	
	//========================================================
	//                    IMarqueChocolat
	//========================================================

	
	public List<String> getMarquesChocolat() {
		LinkedList<String> marques = new LinkedList<String>();
		marques.add("Villors");
		return marques;
	}

	public String toString() {
		return this.getNom();
	}
}
