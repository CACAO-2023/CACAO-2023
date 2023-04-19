package abstraction.eq3Producteur3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;

public class Producteur3Acteur implements IActeur {
	
	protected int cryptogramme;
	protected Journal journal_operationsbancaires;
    protected Journal journal_ventes;
    protected Journal journal_achats;
    protected Journal journal_activitegenerale;
    protected Journal journal_Stock;
    protected Stock Stock;
	protected Double CoutStep; /* Tout nos couts du step, reinitialises a zero au debut de chaque step et payes a la fin du step*/
	protected Double CoutTonne; /*Le cout par tonne de cacao, calcule sur 18 step (destruction de la feve apres 9 mois), le meme pour toute gamme*/

	protected LinkedList<Double> VentesMG; /*Les 12 quantités des dernières ventes de moyens de gammes*/
	protected LinkedList<Double> VentesHG; /*Les 12 quantités des dernières ventes de hauts de gammes*/

	public Producteur3Acteur() {
		String nom = "Equipe 3";
		journal_operationsbancaires=new Journal("Journal des Opérations bancaires de l'"+nom,this);
    	journal_ventes=new Journal("Journal des Ventes de l'"+nom,this);
    	journal_achats=new Journal("Journal des Achats de l'"+nom,this);
    	journal_activitegenerale=new Journal("Journal général de l'"+nom,this);
    	journal_Stock = new Journal("Journal des Stocks de l'"+nom,this);
		this.Stock = new Stock();

		this.VentesMG = new LinkedList<Double>();
		this.VentesHG = new LinkedList<Double>();
	}
	
	/**
	 * @author BOCQUET Gabriel
	 */	
	public void initialiser() {
		;
	}

	/**
	 * @author BOCQUET Gabriel
	 */	
	public String getNom() {// NE PAS MODIFIER
		return "EQ3";
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////
	
	/**
	 * @author BOCQUET Gabriel
	 */	
	protected Journal getJGeneral() {
		return this.journal_activitegenerale;
	}
	
	/**
	 * @author BOCQUET Gabriel
	 */	
	protected Journal getJStock() {
		return this.journal_Stock;
	}

	/**
	 * @author BOCQUET Gabriel
	 */	
	protected int getCryptogramme() {
		return this.cryptogramme;
	}
	
	/**
	 * @author BOCQUET Gabriel
	 */	
	protected Journal getJOperation() {
		return this.journal_operationsbancaires;
	}
	
	/**
	 * @author BOCQUET Gabriel
	 */	
	protected Journal getJVente() {
		return this.journal_ventes;
	}
	
	/**
	 * @author BOCQUET Gabriel
	 */	
	protected Journal getJAchats() {
		return this.journal_achats;
	}

	/**
	 * @author BOCQUET Gabriel
	 */	
	protected Stock getStock() {
		return this.Stock;
	}

	/**
	 * @author BOCQUET Gabriel
	 */	
	public void next() {
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(249, 230, 151); 
	}

	
	public String getDescription() {
		return "Vendeurs ELITE de cacao, spécialistes de la faillite éclair, de la vente à perte et de la vente de produits de qualité médiocre. Nous sommes les meilleurs !";
	}

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
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
		res.add(journal_activitegenerale);
		res.add(journal_operationsbancaires);
		res.add(journal_ventes);
		res.add(journal_achats);
		res.add(journal_Stock);
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

	////////////////////////////////////////////////////////
	//      Quelques fonction utilitaires diverses        //
	////////////////////////////////////////////////////////

	/**
	 * @author Corentin Caugant
	 */
	public void addVenteQuantite(double quantite, Feve feve) {
		if (feve == Feve.F_MQ_BE || feve == Feve.F_MQ) {
			if (this.VentesMG.size() == 12) {
				this.VentesMG.removeFirst();
			}
			this.VentesMG.addLast(quantite);
		}
		else if (feve == Feve.F_HQ_BE) {
			if (this.VentesHG.size() == 12) {
				this.VentesHG.removeFirst();
			}
			this.VentesHG.addLast(quantite);
		}
	}
}
