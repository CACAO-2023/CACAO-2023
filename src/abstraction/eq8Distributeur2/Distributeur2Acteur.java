package abstraction.eq8Distributeur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

public class Distributeur2Acteur implements IActeur,IDistributeurChocolatDeMarque, IMarqueChocolat,IAcheteurContratCadre {
	
	protected int cryptogramme;
	protected String nom;
	protected ArrayList<ChocolatDeMarque> chocolats;
	protected HashMap<ChocolatDeMarque, Double> prixDeVente;
    protected HashMap<ChocolatDeMarque, Variable> stocks;
    protected HashMap<Gamme, Double> pourcentagesGamme;
    private double[] prix;
    private String[] marques;
   
	private double stockBasDeGamme;
	private double stockMoyenDeGamme;
	private double stockHautDeGamme;
    protected Journal journal_operationsbancaires;
    protected Journal journal_ventes;
    protected Journal journal_achats;
    protected Journal journal_stocks;
    protected Journal journal_activitegenerale;
    

	public Distributeur2Acteur() {
		cryptogramme = 0; // valeur par défaut à modifier
	    nom = "équipe 8";
	    chocolats =  new ArrayList<ChocolatDeMarque>();
	    prixDeVente = new HashMap<>();
	    stocks = new HashMap<>();
	    pourcentagesGamme = new HashMap<>();
	    prix = new double[]{0, 0, 0}; // valeurs par défaut à modifier
	    marques = new String[]{"marque 1", "marque 2", "marque 3"}; // valeurs par défaut à modifier
	    stockBasDeGamme = 0.0; // valeur par défaut à modifier
	    stockMoyenDeGamme = 0.0; // valeur par défaut à modifier
	    stockHautDeGamme = 0.0; // valeur par défaut à modifier
	    journal_operationsbancaires = new Journal("Journal des Opérations bancaires de l'" + nom, this);
	    journal_ventes = new Journal("Journal des Ventes de l'" + nom, this);
	    journal_achats = new Journal("Journal des Achats de l'" + nom, this);
	    journal_activitegenerale = new Journal("Journal général de l'" + nom, this);
	    journal_stocks = new Journal("Journal des stocks" + nom, this);
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
		res.add(journal_operationsbancaires);
		res.add(journal_achats);
		res.add(journal_ventes);
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
		if (montant<0) {
			double m=montant*(-1);
			String ch="retrait de "+m;
			this.journal_operationsbancaires.ajouter(ch);
		}
		if (montant>0) {
			String ch= "dépot de "+montant;
			this.journal_operationsbancaires.ajouter(ch);
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

	public  Variable getStock(ChocolatDeMarque choco) {
	    int pos = (((List<Variable>) choco).indexOf(choco));
	    if (pos < 0) {
	        return null;
	    } else {
	        return stocks.get(pos);
	    }
	}

	public List<String> getMarquesChocolat() {
		return null;
	}
//-----------------------------------------------
	//La fonction prix() permet de connaître le prix actuel 
	//d'un kg de chocolat de marque choco. Elle recherche dans 
	//la HashMap prixDeVente si le chocolat de marque choco est vendu par le distributeur.
	//Si c'est le cas, la fonction renvoie le prix correspondant à ce 
	//chocolat de marque. Sinon, la fonction renvoie 0. 
	
	
	@Override
	public double prix(ChocolatDeMarque choco) {
		if(prixDeVente.containsKey(choco)) {
			return prixDeVente.get(choco);
		}else {
			return 0;
		}
	}

	@Override
    public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
        int pos = (chocolats.indexOf(choco));
        if (pos < 0) {
            return 0.0;
        } else {
            double stockGamme;
            if (choco.getGamme() == Gamme.BQ) {
                stockGamme = stockBasDeGamme;
            } else if (choco.getGamme() == Gamme.MQ) {
                stockGamme = stockMoyenDeGamme;
            } else {
                stockGamme = stockHautDeGamme;
            }
            return Math.min(stockGamme, this.getStock(choco).getValeur());
        }
    }

	@Override
    public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
        int pos = chocolats.indexOf(choco);
        if (pos < 0) {
            return 0.0;
        } else {
            if (choco.getGamme() == Gamme.BQ) {
                return Math.min(stockBasDeGamme, this.getStock(choco).getValeur()) / 10.0;
            } else {
                return 0.0;
            }}
        }

	 @Override
	    public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
	        int pos = chocolats.indexOf(choco);
	        if (pos >= 0) {
	            this.getStock(choco).retirer(this, quantite);
	        }
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
