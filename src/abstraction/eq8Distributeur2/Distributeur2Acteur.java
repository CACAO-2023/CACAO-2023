package abstraction.eq8Distributeur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.filiere.IMarqueChocolat;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Distributeur2Acteur implements IActeur,IDistributeurChocolatDeMarque, IMarqueChocolat {
	
	protected int cryptogramme;
	protected HashMap<ChocolatDeMarque, Double> prixDeVente;
    protected HashMap<ChocolatDeMarque, Double> stocks;
    protected HashMap<Gamme, Double> pourcentagesGamme;
    protected Journal journal;

	public Distributeur2Acteur() {
		prixDeVente = new HashMap<>();
        stocks = new HashMap<>();
        pourcentagesGamme = new HashMap<>();
        journal=new Journal("Journal équipe 8",this);
        initialiserGamme();
	}
	
	private void initialiserGamme() {
		 pourcentagesGamme.put(Gamme.BQ, 0.55);
	     pourcentagesGamme.put(Gamme.MQ, 0.40);
	     pourcentagesGamme.put(Gamme.HQ, 0.05);
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
		return "Bla bla bla";
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

	@Override
	public List<String> getMarquesChocolat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double prix(ChocolatDeMarque choco) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
		
	}

}
