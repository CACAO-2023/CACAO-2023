package abstraction.eq8Distributeur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.clients.ClientFinal;
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

public class Distributeur2Acteur implements IActeur,IDistributeurChocolatDeMarque, IMarqueChocolat,IAcheteurContratCadre {
	
	protected int cryptogramme;
	protected String nom;
	protected HashMap<ChocolatDeMarque, Double> prixDeVente;
    protected HashMap<ChocolatDeMarque, Double> stocks;
    protected HashMap<Gamme, Double> pourcentagesGamme;
    protected Journal journal_operationsbancaires;
    protected Journal journal_ventes;
    protected Journal journal_achats;
    protected Journal journal_activitegenerale;
    

	public Distributeur2Acteur() {
		nom="équipe 8";
		prixDeVente = new HashMap<>();
        stocks = new HashMap<>();
        journal_operationsbancaires=new Journal("Journal des Opérations bancaires de l'"+nom,this);
        journal_ventes=new Journal("Journal des Ventes de l'"+nom,this);
        journal_achats=new Journal("Journal des Achats de l'"+nom,this);
        journal_activitegenerale=new Journal("Journal général de l'"+nom,this);
        pourcentagesGamme = new HashMap<>();
        
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
//-----------------------------------------Partie contrat cadre
	@Override
	public boolean achete(IProduit produit) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}
	//-----------------------------------------FIN Partie contrat cadre
}
