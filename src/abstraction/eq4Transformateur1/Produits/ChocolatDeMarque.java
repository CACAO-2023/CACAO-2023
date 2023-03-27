package abstraction.eq4Transformateur1.Produits;

import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.IProduit;

public class ChocolatDeMarque implements IProduit {
	private Chocolat chocolat;
	private String marque;
	private int pourcentageCacao; // Pourcentage de cacao, de [0, 100]
	private int pourcentageRSE;   // Pourcentage du prix de vente au distributeur reverse pour la RSE, de [0, 100]
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
