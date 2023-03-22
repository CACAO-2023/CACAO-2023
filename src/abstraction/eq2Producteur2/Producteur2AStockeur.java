package abstraction.eq2Producteur2;

import java.util.HashMap;

import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Producteur2AStockeur extends Producteur2Acteur {
	
	protected Lot stockFevesBasse;
	protected Lot stockFevesMoy;
	protected Lot stockFevesMoyBE;
	protected Lot stockFevesHaute;
	
	public Producteur2AStockeur() {
		super();
	}
	
	public void initialiser() {
		super.initialiser();
		this.stockFevesBasse = new Lot(Feve.F_BQ);
		stockFevesBasse.ajouter(0, 10000);
		stockFevesMoy.ajouter(0, 10000);
		stockFevesMoyBE.ajouter(0, 10000);
		stockFevesHaute.ajouter(0, 10000);
	}
}
