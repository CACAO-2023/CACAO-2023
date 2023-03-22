package abstraction.eq2Producteur2;

import java.util.HashMap;

import abstraction.eqXRomu.produits.Feve;

public class Producteur2AStockeur extends Producteur2Acteur {
	
	protected HashMap<Feve, Double> stockFevesBasse;
	protected HashMap<Feve, Double> stockFevesMoy;
	protected HashMap<Feve, Double> stockFevesMoyEq;
	protected HashMap<Feve, Double> stockFevesHaute;

}
