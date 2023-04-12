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
    protected HashMap<ChocolatDeMarque, Stock> stocks ; 
    protected HashMap<Gamme, Double> pourcentagesGamme;
    private double[] prix;
    private String[] marques;
    private IProduit produit;
    private HashMap<IProduit, Integer> produitsEnStock;
   
//	private Stock stockBasDeGamme;
//	private Stock stockMoyenDeGamme;
//	private Stock stockHautDeGamme;
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
	    stocks = new HashMap<ChocolatDeMarque, Stock>();
	    
	    pourcentagesGamme = new HashMap<>();
	    prix = new double[]{0, 0, 0}; // valeurs par défaut à modifier
	    marques = new String[]{"marque 1", "marque 2", "marque 3"}; // valeurs par défaut à modifier
//	    stockBasDeGamme = new Stock(0.0);//0.0; // valeur par défaut à modifier
//	    stockMoyenDeGamme =  new Stock(0.0);//0.0; // valeur par défaut à modifier
///	    stockHautDeGamme =  new Stock(0.0);//0.0; // valeur par défaut à modifier
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
//Auteur : Ben Messaoud Karim
	public String getDescription() {
		return "Notre acteur, Royal Roast, est un distributeur de chocolat de toutes les gammes qui s'engage à prendre en compte les enjeux de la filière du cacao pour distribuer un produit final qui respecte les normes et répond aux besoins des clients.";
	}
//Auteur : Ben Messaoud Karim
	
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
	//Auteur : Ben Messaoud Karim
	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}

	// Appelee lorsqu'un acteur fait faillite (potentiellement vous)
	// afin de vous en informer.
	//Auteur : Ben Messaoud Karim
	public void notificationFaillite(IActeur acteur) {
		
	}
	//Auteur : Marzougui Mariem
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
	
	//Auteur : Ben Messaoud Karim
	@Override
	public double prix(ChocolatDeMarque choco) {
		if(prixDeVente.containsKey(choco)) {
			return prixDeVente.get(choco);
		}else {
			return 0;
		}
	}
	//Auteur : Ben Messaoud Karim et Maxime Azzi
	@Override
    public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
        int pos = (chocolats.indexOf(choco));
        if (pos < 0) {
            return 0.0;
        } else {
            double stockGamme;
            if (choco.getGamme() == Gamme.BQ) {
                stockGamme = this.stockBasDeGamme.getQuantite();
            } else if (choco.getGamme() == Gamme.MQ) {
                stockGamme = stockMoyenDeGamme.getQuantite();
            } else {
                stockGamme = stockHautDeGamme.getQuantite();
            }
            return Math.min(stockGamme, this.getStock(choco).getValeur());
        }
    }
	//Auteur : Ben Messaoud Karim et Maxime Azzi
	@Override
    public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
        int pos = chocolats.indexOf(choco);
        if (pos < 0) {
            return 0.0;
        } else {
            if (choco.getGamme() == Gamme.BQ) {
                return Math.min(stockBasDeGamme.getQuantite(), this.getStock(choco).getValeur()) / 10.0;
            } else {
                return 0.0;
            }}
        }
	//Auteur : Ben Messaoud Karim et Maxime Azzi
	 @Override
	    public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
	        int pos = chocolats.indexOf(choco);
	        if (pos >= 0) {
	            this.getStock(choco).retirer(this, quantite);
	        }
	    }
	//Auteur : Ben Messaoud Karim
	@Override
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		// Ajouter un message dans le journal pour indiquer que le rayon est vide
	    journal_activitegenerale.ajouter("Le rayon du chocolat " + choco.getNom() + " est vide.");
	}
//-----------------------------------------Partie contrat cadre
	@Override
	public boolean achete(IProduit produit) {
		// TODO Auto-generated method stub
		return false;
	}
	//Auteur : Ben Messaoud Karim
	@Override
	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		
		return 10;
	}

	@Override
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (contrat.getProduit().equals(produit)) {
			if (contrat.getEcheancier().getQuantiteTotale()<this.stock().getValeur()) {
				if (Math.random()<0.1) {
				return contrat.getEcheancier(); // on ne cherche pas a negocier sur le previsionnel de livraison
				} else {//dans 90% des cas on fait une contreproposition pour l'echeancier
					Echeancier e = contrat.getEcheancier();
					e.set(e.getStepDebut(), e.getQuantite(e.getStepDebut())/2.0);// on souhaite livrer deux fois moins lors de la 1ere livraison... un choix arbitraire, juste pour l'exemple...
					return e;
				}
			} else {
				return null; // on est frileux : on ne s'engage dans un contrat cadre que si on a toute la quantite en stock (on pourrait accepter meme si nous n'avons pas tout car nous pouvons produire/acheter pour tenir les engagements) 
			}
		} else {
			return null;// on ne vend pas de ce produit
		}
		return null;
	}
	//Auteur : Marzougui Mariem
	@Override
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		return contrat.getPrix();
	}
	//Auteur : Marzougui Mariem
	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.journal_activitegenerale.ajouter(contrat.toString());
		
	}
	//Auteur : Marzougui Mariem
	@Override
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}
	//-----------------------------------------FIN Partie contrat cadre
}
