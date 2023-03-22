package abstraction.eq4Transformateur1;

import java.util.HashMap;

import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

public class Stock extends Transformateur1Acteur{
	protected HashMap<Feve, Double> stockFeves;
	protected HashMap<Chocolat, Double> stockChoco;
	protected HashMap<ChocolatDeMarque, Double> stockChocoMarque;
	
	public Stock() {
		super();
	}
	
	public void initialiser() {
		super.initialiser();
	}
}
