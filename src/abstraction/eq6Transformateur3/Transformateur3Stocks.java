package abstraction.eq6Transformateur3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur3Stocks extends Transformateur3Acteur  {

/** Nathan Claeys*/ 
	private List<ChocolatDeMarque> ListeProduits;
	protected HashMap<Feve, Double> stockFeves;
	protected HashMap<Chocolat, Double> stockChoco;
	/** Mouhamed Sow*/ 
	private double coutStockage ;
	private double niveauStockage ; 
	
	public void ajoutStockFeve(Feve feve, Double nbreStock) {
		/** ajouter au stock */
		Double stockInstantT=this.stockFeves.get(feve) ;
		this.stockFeves.put(feve, stockInstantT+nbreStock) ;
	}
	public void ajoutStockChocolat(Chocolat choco, Double nbreChoco) {
		
	}
	
	public Transformateur3Stocks() {
		super();		
		this.ListeProduits = new LinkedList<ChocolatDeMarque>();
	}
	
	public List<ChocolatDeMarque> getListeProduits() {
		return ListeProduits;
	}


}