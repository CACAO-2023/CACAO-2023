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


	protected Lot stockFeveBG; // feve bas gamme
    protected Lot stockFeveMG; // feve moyenne gamme
    protected Lot stockFeveMGL;// feve moyenne gamme lab�lis�e
    protected Lot stockFeveHGL;// feve haute gamme lab�lis�e
    protected Lot stockChocolatBG; // Chocolat bas gamme
    protected Lot stockChocolatMG; // Chocolat moyenne gamme
    protected Lot stockChocolatMGL; // Chocolat moyenne gamme lab�lis�e
    protected Lot stockChocolatHGL; // Chocolat haute gamme lab�lis�e
    protected List<ChocolatDeMarque> stockProduit;

    
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
public void ajouterFeve(Feve feve, Double quantite) {
	  
  }
  public void ajouterChocolat(Chocolat chocolat, Double quantite) {
	
  }
  public boolean estPerimeFeve(int datePeremption) {
	  return Filiere.LA_FILIERE.getEtape()>(datePeremption);
  }
  public void retirerFeve(Feve feve, double quantite)  {
	    
	}

	    
	    
	    
	    
	    
  
  public void retirerChocolat(Chocolat chocolat, Double quantite) {
	  

  }

}
    
    
   

