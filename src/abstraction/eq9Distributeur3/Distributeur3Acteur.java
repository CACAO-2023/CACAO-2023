package abstraction.eq9Distributeur3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.filiere.IMarqueChocolat;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;

public class Distributeur3Acteur implements IActeur{
	protected Integer cryptogramme;
	protected Stock stock;
	protected Journal journal_ventes;
	protected Journal journal_achats;
	protected Journal journal_operationsbancaires;
	protected Journal journal_stock;
	protected Journal journal_activitegenerale;
	protected Journal journal_coefs;
	protected Journal journal_OA;
	protected Journal journal_AO;
	protected Journal journal_STOP;

	protected Journal journal_prix_vente;
	
	
	public List<Double> CA;
	public Double CA_step;


	protected List<ChocolatDeMarque> chocolats;

	protected HashMap<ChocolatDeMarque, Double[]> prixMoyen;
	protected boolean initialise = true;
	protected double prix;
	private List<ChocolatDeMarque>chocosProduits;
	protected HashMap<ChocolatDeMarque, Double> prix_tonne_vente;
	protected Variable variable_stock_tot;
	protected Variable variable_stock_HQ_BE;
	protected Variable variable_stock_MQ_BE;
	protected Variable variable_stock_MQ;
	protected Variable variable_qtte_vendue_HQ_BE;
	protected Variable variable_qtte_vendue_MQ_BE;
	protected Variable variable_qtte_vendue_MQ;
	protected Variable variable_qtte_vendue_TOT;
	protected Variable variable_perc_vendue;
	


	protected Variable quanitite_cible_totale_OA;
	protected Variable variable_CA;
	protected Distributeur3AcheteurOA d;
	
	public Double qte_cible_OA_TOT;
	public HashMap<Double, Double> coef_prix_vente;
	
	
	
	public Distributeur3Acteur() {
		
		

		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
		this.chocolats = new LinkedList<ChocolatDeMarque>();
		
		// william : pour pouvoir acheter le chocolat qui nous intéresse (HQ BE, MQ BE, MQ)
	

		//this.chocolats.add(c1);
		//this.stock.ajoutQte(c1, 1000);
		
		this.journal_ventes = new Journal(this.getNom()+" ventes", this);
		this.journal_achats = new Journal(this.getNom()+" achats", this);
		this.journal_operationsbancaires = new Journal(this.getNom()+" operations", this);
		this.journal_activitegenerale = new Journal(this.getNom()+" activites", this);
		this.journal_coefs = new Journal(this.getNom()+" coefs ", this);
		this.journal_OA = new Journal(this.getNom()+" OA", this);
		this.journal_AO = new Journal(this.getNom()+" AO", this);

		this.journal_prix_vente = new Journal(this.getNom() + " prix vente ",this);


		this.journal_stock = new Journal(this.getNom()+" stock", this);
		this.journal_STOP = new Journal(this.getNom()+" STOP", this);

		this.prixMoyen = new HashMap<ChocolatDeMarque, Double[]>();
		
		this.prix_tonne_vente = new HashMap<ChocolatDeMarque, Double> ();
		
		this.stock = new Stock(this);
		
		
		qte_cible_OA_TOT = 0.0;
		coef_prix_vente = new HashMap<Double, Double>();
		
		quanitite_cible_totale_OA  = new VariablePrivee("Eq9QteCibleOA", "<html>Quantite ciblée (à atteindre) via les OA</html>",this, 0.0, 1000000.0, 0.0);
		variable_CA = new VariablePrivee("Eq9_Chiffre_Affaire_(Mrd€)","<html>Chiffre d'Affaire</html>",this,0.0,10000000,0.0);
		variable_stock_tot = new VariablePrivee("Eq9_Stock_Total", "<html>Quantite totale de tablettes en stock</html>",this, 0.0, 1000000.0, 0.0);
		variable_stock_HQ_BE = new VariablePrivee("Eq9_Stock_HQ_BE", "<html>Quantite totale de tablettes en stock</html>",this, 0.0, 1000000.0, 0.0);
		variable_stock_MQ_BE = new VariablePrivee("Eq9_Stock_MQ_BE", "<html>Quantite totale de tablettes en stock</html>",this, 0.0, 1000000.0, 0.0);
		variable_stock_MQ = new VariablePrivee("Eq9_Stock_MQ", "<html>Quantite totale de tablettes en stock</html>",this, 0.0, 1000000.0, 0.0);
		
		variable_qtte_vendue_HQ_BE = new VariablePrivee("Eq9_Qtte_Vendue_HQ_BE", "<html>Quantite vendue à ce step de HQ BE</html>",this, 0.0, 1000000.0, 0.0);
		variable_qtte_vendue_MQ_BE = new VariablePrivee("Eq9_Qtte_Vendue_MQ_BE", "<html>Quantite vendue à ce step de MQ BE</html>",this, 0.0, 1000000.0, 0.0);
		variable_qtte_vendue_MQ= new VariablePrivee("Eq9_Qtte_Vendue_MQ", "<html>Quantite vendue à ce step de MQ</html>",this, 0.0, 1000000.0, 0.0);

		variable_qtte_vendue_TOT = new VariablePrivee("Eq9_Qtte_Vendue_TOT", "<html>Quantite vendue à ce step</html>",this, 0.0, 1000000.0, 0.0);
	
		variable_perc_vendue = new VariablePrivee("Eq9_%_Vendue_TOT", "<html>Pourcentage de stock vendu à ce step</html>",this, 0.0, 1000000.0, 0.0);

	}
	
	public void initialiser() {
		
		coef_prix_vente.put(0.0, 5.0);
		coef_prix_vente.put(1.0, 3.0);
		coef_prix_vente.put(2.0, 2.0);
		journal_STOP.ajouter("ON");
		
		// william désormais on n'utilise plus une liste de String avec les chocolats qui nous intéressent, on sélectionne seulement à la gamme
		
		CA_step = 0.0;
		this.CA = new LinkedList<Double>();

		List<ChocolatDeMarque> chocolats_filiere = new LinkedList<ChocolatDeMarque>();
		chocolats_filiere = Filiere.LA_FILIERE.getChocolatsProduits();
		
		
		for (ChocolatDeMarque c :  Filiere.LA_FILIERE.getChocolatsProduits()) {
			double prixGamme =0;
			Gamme g;
			switch (c.getChocolat().getGamme()) {
			case HQ : prixGamme=50000;break;
			case MQ : prixGamme=30000;break;
			case BQ : prixGamme=15000;break;

			}
			this.prix_tonne_vente.put(c, prixGamme+(c.isBioEquitable()? 5000 : 0));
		}
		
		
		for (int i=0; i<chocolats_filiere.size(); i++) {
			
			if(chocolats_filiere.get(i).getGamme() == Gamme.HQ) {
				chocolats.add(chocolats_filiere.get(i));
				stock.QteStock.put(chocolats_filiere.get(i),0.0);
			}
			else if (chocolats_filiere.get(i).getGamme() == Gamme.MQ) {
				chocolats.add(chocolats_filiere.get(i));
				stock.QteStock.put(chocolats_filiere.get(i),0.0);
			}
		
		}
	
		// stock initial de 1000 tonnes du premier chocolat de la filiere
		for (int j=0; j <chocolats.size(); j++) {
			stock.ajoutQte(chocolats.get(j), 12000);
		}
		
		variable_stock_tot.setValeur(this, stock.qteStockTOT(), this.cryptogramme);
		variable_stock_HQ_BE.setValeur(this, stock.qteStock_HQ_BE(), this.cryptogramme);
		variable_stock_MQ_BE.setValeur(this, stock.qteStock_MQ_BE(), this.cryptogramme);
		variable_stock_MQ.setValeur(this, stock.qteStock_MQ(), this.cryptogramme);
		
		
		
		
	}
	
	public String toString() {
		return this.getNom();
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ9";
	}
	

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() {
		
		CA.add(CA_step);
		
		cout_stockage();
		
		journal_activitegenerale.ajouter("Etape="+Filiere.LA_FILIERE.getEtape());
		journal_activitegenerale.ajouter("Solde="+getSolde()+"€");
		journal_stock.ajouter("Etape "+ Filiere.LA_FILIERE.getEtape()+ " : " + "Etat du stock Total : "+stock.qteStockTOT()); 
		
		etat_ventes();
		
		
		quanitite_cible_totale_OA.setValeur(this,qte_cible_OA_TOT, this.cryptogramme);
		variable_CA.setValeur(this,CA_step/1000000000, this.cryptogramme);
	
		

	}
	
	public void cout_stockage() {

		//baptiste
		//cout du stockage
		prix = this.stock.coutDeStock();
		if(prix > 0.0) {
			Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ9"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), prix);

			notificationOperationBancaire(-1*prix);
			

			journal_activitegenerale.ajouter("Paiement stockage : " + -1*prix);
		//	notificationOperationBancaire(-1*prix);

		}

		
	}
	
	

	
	public void etat_ventes(){
		//william
		journal_ventes.ajouter("Etat des ventes : "+"\n");
		if (Filiere.LA_FILIERE.getEtape()>=1) {
			for (int i=0; i<this.chocolats.size(); i++) {
				journal_activitegenerale.ajouter("Le prix moyen du chocolat \""+chocolats.get(i).getNom()+"\" a l'etape precedente etait de "+Filiere.LA_FILIERE.prixMoyen(chocolats.get(i), Filiere.LA_FILIERE.getEtape()-1));
				journal_activitegenerale.ajouter("Les ventes de chocolat \""+chocolats.get(i)+" a l'etape precedente etaient de "+Filiere.LA_FILIERE.getVentes(chocolats.get(i), Filiere.LA_FILIERE.getEtape()-1));

			}
		}
	}


	

	public void calcul_prix_de_vente() {
		// pour chaque gamme, renvoie une hashmap <marque, prix>       
		// (prendre en compte la rentabilité, le positionnement des autres marques)

	}
	
	
	
	
	public ChocolatDeMarque get_chocolat_with_name(String name) {
		for(int i =0; i< chocolats.size();i++) {
			if( (chocolats.get(i)).toString().equals(name)) {
				return chocolats.get(i);
			}
		}
		return null;
	}
	


	public Color getColor() {// NE PAS MODIFIER
		return new Color(245, 155, 185); 
	}

	public String getDescription() {
		return "Des ingrédients d'exception pour un chocolat unique";
	}

	
	public List<Variable> getIndicateurs() {
		List<Variable> res=new ArrayList<Variable>();
		
		res.add(variable_CA);
		res.add(variable_stock_tot);
		res.add(variable_stock_HQ_BE);
		res.add(variable_stock_MQ_BE);
		res.add(variable_stock_MQ);
		
		res.add(variable_qtte_vendue_HQ_BE);
		res.add(variable_qtte_vendue_MQ_BE);
		res.add(variable_qtte_vendue_MQ);
		res.add(variable_qtte_vendue_TOT);

		res.add(quanitite_cible_totale_OA);
		res.add(variable_perc_vendue);
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
		res.add(journal_ventes);
		res.add(journal_achats);
		res.add(journal_operationsbancaires);
		res.add(journal_activitegenerale);
		res.add(journal_stock);
		res.add(journal_coefs);
		res.add(journal_AO);
		res.add(journal_STOP);

		res.add(journal_OA);
		res.add(journal_prix_vente);

		
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
		
		//- vous pouvez exploiter la methode notificationOperationBancaire de votre acteur pour afficher dans un journal 
		//vos entree/sorties d'argent : ça levera le doute sur les prix que vous estimez minimalistes.
		
		journal_operationsbancaires.ajouter("Operation de " + montant + " €" );

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
	
	public double getStock(ChocolatDeMarque c) {
		return this.stock.getStock(c);
	}

	
}
