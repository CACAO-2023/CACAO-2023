package abstraction.eq6Transformateur3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Transformateur3Stocks extends Transformateur3Acteur  {

/**Mouhamed SOW*/
	protected Lot stockFeveBG; // feve bas gamme
    protected Lot stockFeveMG; // feve moyenne gamme
    protected Lot stockFeveMGL;// feve moyenne gamme labelisée
    protected Lot stockFeveHGL;// feve haute gamme labelisée
    protected Lot stockChocolatBG; // Chocolat bas gamme
    protected Lot stockChocolatMG; // Chocolat moyenne gamme
    protected Lot stockChocolatMGL; // Chocolat moyenne gamme labelisée
    protected Lot stockChocolatHGL; // Chocolat haute gamme labélisée
    protected List<ChocolatDeMarque> stockProduit;

    /**Mouhamed SOW*/  
  public Transformateur3Stocks(Lot stockFeveBG, Lot stockFeveMG, Lot stockFeveMGL, Lot stockFeveHGL,
			Lot stockChocolatBG, Lot stockChocolatMG, Lot stockChocolatMGL, Lot stockChocolatHGL,
			List<ChocolatDeMarque> stockProduit) {
		this.stockFeveBG = stockFeveBG;
		this.stockFeveMG = stockFeveMG;
		this.stockFeveMGL = stockFeveMGL;
		this.stockFeveHGL = stockFeveHGL;
		this.stockChocolatBG = stockChocolatBG;
		this.stockChocolatMG = stockChocolatMG;
		this.stockChocolatMGL = stockChocolatMGL;
		this.stockChocolatHGL = stockChocolatHGL;
		this.stockProduit = stockProduit;
	}
 /**Il faut ecrire la fonction constructeur sans param c'est celle là que Romu appelle *
  * dans le code de la filière il faut donc construire les feves et les lots et ensuite 
  * utiliser les constructeur avec le constructeur déjà ecrit
  */
  /**Mouhamed SOW*/
  public Transformateur3Stocks() {
      this.stockFeveBG = new Lot(Feve.F_BQ);
      this.stockFeveMG = new Lot(Feve.F_MQ);
      this.stockFeveMGL = new Lot(Feve.F_MQ_BE);
      this.stockFeveHGL = new Lot(Feve.F_HQ_BE);
      this.stockChocolatBG = new Lot(new ChocolatDeMarque(Chocolat.C_BQ,"eco+ choco",super.pourcentageCacaoBG,super.pourcentageRSE));
      this.stockChocolatMG = new Lot(new ChocolatDeMarque(Chocolat.C_MQ,"chokchoco",super.pourcentageCacaoMG,super.pourcentageRSE));
      this.stockChocolatMGL = new Lot(new ChocolatDeMarque(Chocolat.C_MQ_BE,"chokchoco bio",super.pourcentageCacaoMGL,super.pourcentageRSE));
      this.stockChocolatHGL = new Lot(new ChocolatDeMarque(Chocolat.C_HQ_BE,"Choc",super.pourcentageCacaoHG,super.pourcentageRSE));
      this.stockProduit = new ArrayList<ChocolatDeMarque>();
  }
  /**Mouhamed SOW*/
  public void ajouterFeve(Feve feve, Double quantite, int dateDeRecolte) {
	    Lot lot;
	    switch(feve.getGamme()) {
	        case BQ:
	            lot = stockFeveBG;
	            break;
	        case MQ:
	        	if(feve.isBioEquitable()) {
	        		lot = stockFeveMGL;
	        		break ;
	        	}else {
	        		lot = stockFeveMG;
	                break;
	        	}
	        case HQ:
	        	lot = stockFeveHGL;
        		break ;
	        default:
	            throw new IllegalArgumentException("Type de fève invalide");
	    }
	    lot.ajouter(dateDeRecolte, quantite);
	}
  /**Mouhamed SOW*/
  /**methode pour savoir ou il faut ajouter la feve*/
  private Lot getLotFeve(IProduit produit) {
	    if (produit instanceof Feve) {
	        Feve feve = (Feve) produit;
	        switch (feve.getGamme()) {
	            case BQ:
	                return stockFeveBG;
	            case MQ:
	                if (feve.isBioEquitable()) {
	                    return stockFeveMGL;
	                } else {
	                    return stockFeveMG;
	                }
	            case HQ:
	                return stockFeveHGL;
	            default:
	                return null;
	        }
	    } else {
	        return null;
	    }
	}
  /**Mouhamed SOW*/
  /**methode pour savoir ou il faut ajouter le chocolat*/
  private Lot getLotChocolat(IProduit produit) {
	  if(produit instanceof Chocolat) {
		  Chocolat chocolat=(Chocolat) produit ;
		  switch(chocolat.getGamme()) {
		  case MQ :
			  return this.stockChocolatBG ;
		  case BQ :
			  if(chocolat.isBioEquitable()) {
				  return this.stockChocolatMGL ;
			  }else {
				  return this.stockChocolatMG ;
			  }
		  case HQ :
			  return this.stockChocolatHGL ;
			  default:
				  return null ;
		  }
	  }else {
		  return null ;
	  }
  }

  /**Mouhamed SOW*/
  public void retirerFeve(Feve feve, double quantite) {
	    Lot lot = getLotFeve(feve);
	    HashMap<Integer, Double> quantites = lot.getQuantites();

	    // Tri des clés dans l'ordre décroissant (les plus vieilles feves en premier)
	    List<Integer> keys = new ArrayList<Integer>(quantites.keySet());
	    Collections.sort(keys, Collections.reverseOrder());

	    double quantiteRetiree = 0;
	    for (int key : keys) {
	        if (quantiteRetiree == quantite) {
	            break;
	        }
	        if (key >= 0 && key <= 6) { // On retire uniquement les feves périmées (plus de 6 mois)
	            double quantiteRestante = quantites.get(key);
	            if (quantiteRestante >= quantite - quantiteRetiree) {
	                quantites.put(key, quantiteRestante - (quantite - quantiteRetiree));
	                quantiteRetiree = quantite;
	            } else {
	                quantites.remove(key);
	                quantiteRetiree += quantiteRestante;
	            }
	        }
	    }
	}
  /**Mouhamed SOW*/
  public void ajouterChocolat(ChocolatDeMarque choco,Double quantite,int dateProduction) {
	  Lot lot ;
	  switch(choco.getGamme()) {
	  	case BQ :
	  		lot=this.stockChocolatBG ;
	  		break ;
	  	case MQ :
	  		if(choco.isBioEquitable()) {
	  			lot=this.stockChocolatMGL ;
	  			break ;
	  		}else {
	  			lot=this.stockChocolatMG ;
	  			break ;
	  		}
	  	case HQ :
	  		lot=this.stockChocolatHGL ;
	  		break ;
	  	default :
	  		throw new IllegalArgumentException("Type de Chocolat invalide");
	  }
	  lot.ajouter(dateProduction,quantite);
  }
 /**Mouhamed SOW*/
  public void retirerChcoclat(ChocolatDeMarque chocolat,Double quantite) {
	  Lot lot=this.getLotChocolat(chocolat) ;
	  HashMap<Integer, Double> quantites = lot.getQuantites();

	    // Tri des clés dans l'ordre décroissant (les plus vieux choco en premier)
	    List<Integer> keys = new ArrayList<Integer>(quantites.keySet());
	    Collections.sort(keys, Collections.reverseOrder());

	    double quantiteRetiree = 0;
	    for (int key : keys) {
	        if (quantiteRetiree == quantite) {
	            break;
	        }
	        if (key >= 0 && key <= 6) { // On retire uniquement les choco périmés (plus de 6 mois)
	            double quantiteRestante = quantites.get(key);
	            if (quantiteRestante >= quantite - quantiteRetiree) {
	                quantites.put(key, quantiteRestante - (quantite - quantiteRetiree));
	                quantiteRetiree = quantite;
	            } else {
	                quantites.remove(key);
	                quantiteRetiree += quantiteRestante;
	            }
	        }
	    }
  }
  
}
    
    
   

