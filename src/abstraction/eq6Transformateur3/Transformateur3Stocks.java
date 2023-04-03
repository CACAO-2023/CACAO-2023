package abstraction.eq6Transformateur3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur3Stocks extends Transformateur3Acteur  {

/** Nathan Claeys*/ 
	private HashMap<Feve, Double> fevesStocks;
    private HashMap<Chocolat, Double> chocolatStocks;
    private List<ChocolatDeMarque> produitsStockes;
	/** Mouhamed Sow*/ 
    public Transformateur3Stocks() {
        fevesStocks = new HashMap<Feve, Double>();
        chocolatStocks = new HashMap<Chocolat, Double>();
        produitsStockes = new ArrayList<ChocolatDeMarque>();
    }
    
    public void ajouter(Feve feve, double quantite) {
        if (fevesStocks.containsKey(feve)) {
            fevesStocks.put(feve, fevesStocks.get(feve) + quantite);
        } else {
            fevesStocks.put(feve, quantite);
        }
    }
    
    public void ajouter(Chocolat chocolat, double quantite) {
        if (chocolatStocks.containsKey(chocolat)) {
            chocolatStocks.put(chocolat, chocolatStocks.get(chocolat) + quantite);
        } else {
            chocolatStocks.put(chocolat, quantite);
        }
    }
    
    public void ajouter(ChocolatDeMarque chocolat, double quantite) {
        produitsStockes.add(chocolat);
    }
    
    
   

}