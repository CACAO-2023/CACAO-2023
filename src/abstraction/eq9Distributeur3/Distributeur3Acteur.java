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
	protected List<ChocolatDeMarque> chocolats;
	protected List<String> chocolats_cible_noms;
	protected HashMap<ChocolatDeMarque, Double[]> prixMoyen;
	protected boolean initialise = true;
	protected double prix;
	private List<ChocolatDeMarque>chocosProduits;
	protected HashMap<ChocolatDeMarque, Double> prix_tonne_vente;
	protected Variable variable_stock;
	public Distributeur3Acteur() {

		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
		this.chocolats = new LinkedList<ChocolatDeMarque>();
		
		// william : pour pouvoir acheter le chocolat qui nous intéresse (HQ BE, MQ BE, MQ)
		this.chocolats_cible_noms = new LinkedList<String>();
		this.chocolats_cible_noms.add("C_HQ_BE_Vccotioi");
		this.chocolats_cible_noms.add("C_HQ_BE_Maison_Doutre");
		this.chocolats_cible_noms.add("C_HQ_BE_Choc");
		this.chocolats_cible_noms.add("C_MQ_BE chokchoco bio");
		this.chocolats_cible_noms.add("C_HQ_BE_Villors");
		this.chocolats_cible_noms.add("C_MQ_BE_Villors");
		this.chocolats_cible_noms.add("C_BQ_Villors");

		//this.chocolats.add(c1);
		//this.stock.ajoutQte(c1, 1000);
		
		this.journal_ventes = new Journal(this.getNom()+" ventes", this);
		this.journal_achats = new Journal(this.getNom()+" achats", this);
		this.journal_operationsbancaires = new Journal(this.getNom()+" operations", this);
		this.journal_activitegenerale = new Journal(this.getNom()+" activites", this);
		this.journal_stock = new Journal(this.getNom()+" stock", this);
		this.prixMoyen = new HashMap<ChocolatDeMarque, Double[]>();
		
		this.prix_tonne_vente = new HashMap<ChocolatDeMarque, Double> ();
		
		this.stock = new Stock(this);
		variable_stock = new VariablePrivee("Eq9StockTablettes", "<html>Quantite totale de tablettes en stock</html>",this, 0.0, 1000000.0, 0.0);

	}
	
	public void initialiser() {
		List<ChocolatDeMarque> chocolats_filiere = new LinkedList<ChocolatDeMarque>();
		chocolats_filiere = Filiere.LA_FILIERE.getChocolatsProduits();
		for (int i=0; i<chocolats_filiere.size(); i++) {

			if(chocolats_cible_noms.contains((chocolats_filiere.get(i)).toString())){
				chocolats.add(chocolats_filiere.get(i));
				stock.QteStock.put(chocolats_filiere.get(i),0.0);
			}
		}
		System.out.println(chocolats);
	//	for (int i = 0; i< this.chocolats.size(); i++) {
		//	this.stock.ajoutQte(chocolats.get(i), 100000000);
		//	this.prix_tonne_vente.put(chocolats.get(i), 10000.0);
		//}
		
		
		
		
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
		
		journal_activitegenerale.ajouter("Etape="+Filiere.LA_FILIERE.getEtape());
		journal_activitegenerale.ajouter("Solde="+getSolde()+"€");
		etat_ventes();
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


	
	public void repartition_tete_gondole() {
		HashMap<ChocolatDeMarque, Double> repartition = new HashMap<ChocolatDeMarque, Double>();
		repartition.put((get_chocolat_with_name("C_HQ_BE_Choc")),1.0);
		
		//renvoie une hashmap <marque, quatité>
	}

	
	
	public ChocolatDeMarque get_chocolat_with_name(String name) {
		for(int i =0; i< chocolats.size();i++) {
			if( (chocolats.get(i)).toString() == name) {
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

		res.add(variable_stock);
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
		journal_operationsbancaires.ajouter("Operation de " + montant + " €");

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
