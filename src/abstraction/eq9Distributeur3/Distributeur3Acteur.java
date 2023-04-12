package abstraction.eq9Distributeur3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;

public class Distributeur3Acteur implements IActeur {
	private static int NB_INSTANCES = 0; // Afin d'attribuer un nom different a toutes les instances
	protected int numero;
	protected Integer cryptogramme;
	protected Stock stock;
	protected Journal journal_ventes;
	protected Journal journal_achats;
	protected Journal journal_operationsbancaires;
	protected Journal journal_activitegenerale;
	protected Journal journal_stock;
	protected List<ChocolatDeMarque> chocolats;

	public Distributeur3Acteur() {
		/*if (chocos==null || chocos.length<1 || stocks==null || stocks.length!=chocos.length) {
			throw new IllegalArgumentException("creation d'une instance de ExempleAbsDistributeurChocolatMarqe avec des arguments non valides");
		}		
		NB_INSTANCES++;
		this.numero=NB_INSTANCES;*/
		
		
		// Ici pour tester on se créé un stock de chocolat à partir de rien (william)
		// ChocolatDeMarque(Chocolat chocolat, String marque, int pourcentageCacao, int pourcentageRSE)
		ChocolatDeMarque c1 = new ChocolatDeMarque(Chocolat.C_HQ_BE, "marque", 50, 20);
		Stock stock = new Stock();
		this.stock = stock;
		
		this.chocolats = new LinkedList<ChocolatDeMarque>();
		this.chocolats.add(c1);
		this.stock.ajoutQte(c1, 1000);
		
		this.journal_ventes = new Journal(this.getNom()+" ventes", this);
		this.journal_achats = new Journal(this.getNom()+" achats", this);
		this.journal_operationsbancaires = new Journal(this.getNom()+" operations", this);
		this.journal_activitegenerale = new Journal(this.getNom()+" activites", this);
		this.journal_stock = new Journal(this.getNom()+" stocks", this);

		
	}
	
	public void initialiser() {
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


		// lancer un contrat seuil et repondre 

		
		
		// il va falloir faire la comparaison de contrats cadres par rapport à un seuil puis choisir le plus interessant


		journal_activitegenerale.ajouter("Etape="+Filiere.LA_FILIERE.getEtape());
		if (Filiere.LA_FILIERE.getEtape()>=1) {
			for (int i=0; i<this.chocolats.size(); i++) {
		//	journal.ajouter("Le prix moyen du chocolat \""+chocolats.get(i).getNom()+"\" a l'etape precedente etait de "+Filiere.LA_FILIERE.prixMoyen(chocolats.get(i), Filiere.LA_FILIERE.getEtape()-1));
			//journal.ajouter("Les ventes de chocolat \""+chocolats.get(i)+" il y a un an etaient de "+Filiere.LA_FILIERE.getVentes(chocolats.get(i), Filiere.LA_FILIERE.getEtape()-24));
			
			
			}
		}

	}

	
	
	public void etat_ventes(){
		/*
		Ils peuvent par contre connaître les volumes de ventes passés. En effet, pour tout chocolat de
		marque choco, Filiere.LA_FILIERE.getVentes(choco, etape) retourne la quantité totale (tous
		distributeurs cumulés) des ventes de choco à l’étape etape (avec etape dans [-24,
		Filire.LA_FILIERE.getEtape()
		
		regarder les stocks de chaque gamme (moyen, moyen BE, haut), 
		regarder nos ventes et les ventes du marché pour savoir ce 
		qu'il faut acheter par ordre de priorité

		William
		*/
	}
	public void achat_stock(){

		/* 
		 en fonction de lookat_results(), l�acteur devra réaliser des contrats
		 
		cadres ou des appels d'offres ou accepter des offres pour certaines 
		gammes bas� sur leur priorité.
		
		William
		
		*/


	}
	public void contrat_cadre(){}
	public void appels_offres(){}
	public void offres(){}
	public void calcul_prix_de_vente() {
		// pour chaque gamme, renvoie une hashmap <marque, prix>       
		// (prendre en compte la rentabilité, le positionnement des autres marques)

	}
	public void repartition_tete_gondole() {
		//renvoie une hashmap <marque, quatité>
	}
	public void cout_stock() {
				//, calcul le coût de stockage.

	}
	public void quantite_rayon() {

				//déterminer quel part du stock est mise en rayon

	}
	public void cout_masse_salariale() {}

	


	public Color getColor() {// NE PAS MODIFIER
		return new Color(245, 155, 185); 
	}

	public String getDescription() {
		
		return "Des ingrédients d'exception pour un chocolat unique";
	}


	
	
	public List<Variable> getIndicateurs() {
		
		ChocolatDeMarque c1 = new ChocolatDeMarque(Chocolat.C_HQ_BE, "marque", 50, 20);

		
		Variable res= new Variable("Stock",this,stock.getStock(c1));
		
		List<Variable> d = null;
		d.add(res);
		
		return d;
		
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
	
	public double getStock(ChocolatDeMarque c) {
		return this.stock.getStock(c);
	}

	

}
