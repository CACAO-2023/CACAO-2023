package abstraction.eq6Transformateur3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Transformateur3Stocks extends Transformateur3Acteur  {

/** Nathan Claeys*/ 
	protected Lot stockFeveBG; // feve bas gamme
    protected Lot stockFeveMG; // feve moyenne gamme
    protected Lot stockFeveMGL;// feve moyenne gamme labélisée
    protected Lot stockFeveHGL;// feve haute gamme labélisée
    protected Lot stockChocolatBG; // Chocolat bas gamme
    protected Lot stockChocolatMG; // Chocolat moyenne gamme
    protected Lot stockChocolatMGL; // Chocolat moyenne gamme labélisée
    protected Lot stockChocolatHGL; // Chocolat haute gamme labélisée
    protected List<ChocolatDeMarque> stockProduit;
	/** Mouhamed Sow*/ 
    public Transformateur3Stocks() {
        stockFeve = new HashMap<Feve, Lot>();
        stockChocolat = new HashMap<Chocolat, Double>();
        stockProduit = new ArrayList<ChocolatDeMarque>();
        
    }
    
  public void ajouterFeve(Feve feve, Double quantité) {
	  
  }
  public void ajouterChocolat(Chocolat chocolat, Double quantité) {
	
  }
  public boolean estPerimeFeve(Date datePeremption) {
	  return Filiere.LA_FILIERE.getDate().isAfter(datePeremption);
  }
  public void retirerFeve(Feve feve, double quantite)  {
	    
	}
	    
	    
	    
	    
	    
  
  public void retirerChocolat(Chocolat chocolat, Double quantité) {
	  
  }
    
    
   

}