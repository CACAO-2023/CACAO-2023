package abstraction.eq8Distributeur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;

public class Distributeur2Acteur implements IActeur {
	
	protected int cryptogramme;
	protected HashMap<String, Double> prixDeVente;
    protected HashMap<String, Double> stocks;
    protected HashMap<String, Double> prixDAchat;
    protected HashMap<String, Double> pourcentagesGamme;

	public Distributeur2Acteur() {
		prixDeVente = new HashMap<>();
        stocks = new HashMap<>();
        prixDAchat = new HashMap<>();
        pourcentagesGamme = new HashMap<>();
        initialiserGamme();
	}
	
	private void initialiserGamme() {
		 pourcentagesGamme.put("bas", 0.55);
	     pourcentagesGamme.put("moyen", 0.40);
	     pourcentagesGamme.put("haut", 0.05);
	}

	public void initialiser() {
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ8";
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() {
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(209, 179, 221); 
	}

	public String getDescription() {
		return "Notre acteur, Royal Roast, est un distributeur de chocolat de toutes les gammes qui s'engage à prendre en compte les enjeux de la filière du cacao pour distribuer un produit final qui respecte les normes et répond aux besoins des clients.";
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
		List<Journal> res = getJournaux();
		
		if (montant<0) {
			double m=montant*(-1);
			String ch="retrait de "+m;
			res.get(1).ajouter(ch);
		}
		if (montant>0) {
			String ch= "dépot de "+montant;
			res.get(1).ajouter(ch);
		}
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
