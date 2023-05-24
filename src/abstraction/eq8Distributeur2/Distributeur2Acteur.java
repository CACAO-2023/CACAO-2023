
package abstraction.eq8Distributeur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.clients.ExempleDistributeurChocolatMarque;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.filiere.IMarqueChocolat;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Distributeur2Acteur implements IActeur,IDistributeurChocolatDeMarque, IMarqueChocolat {

	protected int cryptogramme;
	protected String nom;
	protected ArrayList<ChocolatDeMarque> chocolats;
	protected LinkedList<String> nos_chocolats;
	protected HashMap<ChocolatDeMarque, Double> prixDeVente;
	protected StockGeneral stocks;
	protected HashMap<Gamme, Double> pourcentagesGamme;
	private double stock_total;
	
	protected Journal journal_operationsbancaires;
	protected Journal journal_ventes;
	protected Journal journal_achats;
	protected Journal journal_stocks;
	protected Journal journal_ContratCadre;
	protected Journal journal_activitegenerale;
	


	public Distributeur2Acteur() {
		cryptogramme = 0; // valeur par défaut à modifier
		nom = "équipe 8";
		chocolats =  new ArrayList<ChocolatDeMarque>();
		prixDeVente = new HashMap<>();
		stocks = new StockGeneral();
		stock_total = 1000.0;
		pourcentagesGamme = new HashMap<>();

		journal_operationsbancaires = new Journal("Journal des Opérations bancaires de l'" + nom, this);
		journal_ventes = new Journal("Journal des Ventes de l'" + nom, this);
		journal_achats = new Journal("Journal des Achats de l'" + nom, this);
		journal_ContratCadre= new Journal("Journal des Contrats Cadre de l'" + nom, this);
		journal_activitegenerale = new Journal("Journal général de l'" + nom, this);
		journal_stocks = new Journal("Journal des stocks " + nom, this);
		initialiserGamme();

		this.nos_chocolats = new LinkedList<String>();
		
		//nos marques de chocolats
		this.nos_chocolats.add("C_HQ_BE_Vccotioi");
		this.nos_chocolats.add("C_MQ_ChocoPop");
		this.nos_chocolats.add("C_MQ_chokchoco");
		this.nos_chocolats.add("C_MQ_BE_Villors");

		
	//C_HQ_BE_Vccotioi
	//C_MQ_ChocoPop
	//C_HQ_BE_Maison Doutre
	//C_BQ_eco+ choco
	//C_MQ_chokchoco
	//C_MQ_BE_chokchoco bio
	//C_HQ_BE_Choc
	//C_HQ_BE_Villors
	//C_MQ_BE_Villors
	//C_MQ_Villors
	//C_BQ_Villors	
		
	}

	private void initialiserGamme() {
		pourcentagesGamme.put(Gamme.BQ, 0.55);
		pourcentagesGamme.put(Gamme.MQ, 0.40);
		pourcentagesGamme.put(Gamme.HQ, 0.05);
	}

	public void initialiser() {

		List<ChocolatDeMarque> chocolats_filiere = new LinkedList<ChocolatDeMarque>();
		chocolats_filiere = Filiere.LA_FILIERE.getChocolatsProduits();
		for (int i=0; i<chocolats_filiere.size(); i++) {
			if(nos_chocolats.contains((chocolats_filiere.get(i)).toString())){
				chocolats.add(chocolats_filiere.get(i));
			}
		}
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ8";
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() {
		
		List<ChocolatDeMarque> chocolats_filiere = new LinkedList<ChocolatDeMarque>();
		chocolats_filiere = Filiere.LA_FILIERE.getChocolatsProduits();
		for (int i=0; i<chocolats_filiere.size(); i++) {
			if(nos_chocolats.contains((chocolats_filiere.get(i)).toString())){
				chocolats.add(chocolats_filiere.get(i));
			}
		}
		
		if (Filiere.LA_FILIERE.getEtape()==1) {
			for (ChocolatDeMarque marque : chocolats) {
				stocks.ajouterAuStock(marque, 1.0);
				journal_stocks.ajouter("Stock de "+marque+" : "+stocks.getStock(marque)+" T");
			}	}

		//Mise à jour du stock total
		for (ChocolatDeMarque marque : chocolats) {
			stock_total += stocks.getStock(marque);
		}
		journal_stocks.ajouter("Stock total "+ stock_total+"T");

		//Répertoire de l'activité générale
		journal_activitegenerale.ajouter("Etape="+Filiere.LA_FILIERE.getEtape());
		if (Filiere.LA_FILIERE.getEtape()>=1) {
			for (int i=0; i<this.chocolats.size(); i++) {
				journal_activitegenerale.ajouter("Le prix moyen du chocolat \""+chocolats.get(i).getNom()+"\" a l'etape precedente etait de "+Filiere.LA_FILIERE.prixMoyen(chocolats.get(i), Filiere.LA_FILIERE.getEtape()-1));
				journal_activitegenerale.ajouter("Les ventes de chocolat \""+chocolats.get(i)+" il y a un an etaient de "+Filiere.LA_FILIERE.getVentes(chocolats.get(i), Filiere.LA_FILIERE.getEtape()-24));
			}
		}
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(209, 179, 221);
	}

	//Auteur : Ben Messaoud Karim
	public String getDescription() {
		return "Royal Roast, un distributeur de chocolat de qualité.";
	}

	//Auteur : Ben Messaoud Karim
	//Renvoie les indicateurs
	public List<Variable> getIndicateurs() {

		List<Variable> res = new ArrayList<Variable>();
		Variable s= new Variable("stock",this,stock_total);
		res.add(s);
		return res;
	}

	//Auteur : Marzougui Mariem
	//Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	//Auteur : Marzougui Mariem
	//Renvoie les journaux
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(journal_operationsbancaires);
		res.add(journal_achats);
		res.add(journal_ventes);
		res.add(journal_ContratCadre);
		res.add(journal_activitegenerale);
		res.add(journal_stocks);
		return res;
	}

	////////////////////////////////////////////////////////
	//               En lien avec la Banque               //
	////////////////////////////////////////////////////////

	// Appelee en debut de simulation pour vous communiquer 
	// votre cryptogramme personnel, indispensable pour les
	// transactions.
	//Auteur : Ben Messaoud Karim
	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}

	// Appelee lorsqu'un acteur fait faillite (potentiellement vous)
	// afin de vous en informer.
	//Auteur : Ben Messaoud Karim
	public void notificationFaillite(IActeur acteur) {
		if (this==acteur) {
			System.out.println("oups on est mort");
		}
		else {
			System.out.println("try again");
		}


	}
	//Auteur : Marzougui Mariem
	// Apres chaque operation sur votre compte bancaire, cette
	// operation est appelee pour vous en informer
	public void notificationOperationBancaire(double montant) {
		if (montant<0) {
			double m=montant*(-1);
			String ch="retrait de "+m+"€";
			this.journal_operationsbancaires.ajouter(ch);
		}
		if (montant>0) {
			String ch= "dépot de "+montant+"€";
			this.journal_operationsbancaires.ajouter(ch);
		}
	}

	// Renvoie le solde actuel de l'acteur
	//Auteur : Ben Messaoud Karim
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

	//Auteur : Ben Messaoud Karim
	public  Stock getStock(ChocolatDeMarque choco) {
		int pos = (((List<Variable>) choco).indexOf(choco));
		if (pos < 0) {
			return null;

		} else {
			return this.getStock(choco);
		}
	}

	public List<String> getMarquesChocolat() {
		return new ArrayList<String>();
	}
	//-----------------------------------------------
	//La fonction prix() permet de connaître le prix actuel 
	//d'un kg de chocolat de marque choco. Elle recherche dans 
	//la HashMap prixDeVente si le chocolat de marque choco est vendu par le distributeur.
	//Si c'est le cas, la fonction renvoie le prix correspondant à ce 
	//chocolat de marque. Sinon, la fonction renvoie 0. 

	//Auteur : Ben Messaoud Karim
	public double prix(ChocolatDeMarque choco) {
		if(prixDeVente.containsKey(choco)) {
			return prixDeVente.get(choco);
		}else {
			return 100000000;
		}
	}
	/*tous les next on ajoute dans le journal la quantité en stock*/
	/*mettre en indicateur que l'on est en rupture de stock */

	//Auteur : Ben Messaoud Karim et Maxime Azzi
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		if (choco == null || this.stocks.getStock(choco) == 0.0) {
			return 0.0;
		} else {
			double stockGamme;
			double stockChoco = this.stocks.getStock(choco);
			stockGamme = this.stocks.getStock(choco);

			return Math.min(stockGamme, stockChoco);
		}
	}

	//Auteur : Ben Messaoud Karim et Maxime Azzi
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		int pos = chocolats.indexOf(choco);
		if (pos < 0) {
			return 0.0;
		} else {
			if (choco.getGamme() == Gamme.BQ) {
				double n = (this.getStock(choco).getQuantite());
				return n / 10.0;
			} else {
				return 0.0;
			}}
	}

	//Auteur : Ben Messaoud Karim et Maxime Azzi
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		int pos = chocolats.indexOf(choco);
		if (pos >= 0) {
			this.stocks.retirerDuStock(choco, quantite);
			stock_total-=quantite;
			journal_stocks.ajouter("retrait d'une quantité de"+ quantite+"T");System.out.println("gggggg");
			journal_ventes.ajouter("La quantité " + quantite + " a été vendue à" + montant);
		}
	}

	//Auteur : Ben Messaoud Karim
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		// Ajouter un message dans le journal pour indiquer que le rayon est vide
		journal_activitegenerale.ajouter("Le rayon du chocolat " + choco.getNom() + " est vide.");
	}
	public String toString() {
        return this.getNom();
    }

}
