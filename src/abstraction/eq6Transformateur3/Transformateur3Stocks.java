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
	private double coutStockage ;
	private double niveauStockage ;  
	public Transformateur3Stocks() {
		super();		
		this.ListeProduits = new LinkedList<ChocolatDeMarque>();
	}

	public List<ChocolatDeMarque> getListeProduits() {
		return ListeProduits;
	}
}