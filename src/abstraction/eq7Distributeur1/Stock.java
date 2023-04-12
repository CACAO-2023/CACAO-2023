package abstraction.eq7Distributeur1;

import java.util.HashMap;

import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.Gamme;

public class Stock {
	HashMap<Chocolat, Integer> stockChoco;
	
	public Stock (HashMap<Chocolat, Integer> stockChoco) {
		this.stockChoco= stockChoco;
	}
	
	
	public void initialiser(HashMap<Chocolat, Integer> stockChoco) {
		stockChoco.put( Chocolat.C_HQ_BE,0); // Haute Qualite   Bio-Equitable   
		stockChoco.put( Chocolat.C_MQ_BE,0); // Moyenne Qualite Bio-Equitable   
		stockChoco.put( Chocolat.C_MQ,0); // Moyenne Qualite pas Bio-Equitable   
		stockChoco.put( Chocolat.C_BQ,0); // Basse Qualite pas Bio-Equitable   
	}
	public int get(Chocolat c) {
		return stockChoco.get(c);
	}
	public void ajouter(Chocolat c,Integer q) {
		stockChoco.put(c,stockChoco.get(c)+q);
	}
	public boolean peut_retirer(Chocolat c, Integer q) {
		return stockChoco.get(c)>=q;
	}
	public void retirer (Chocolat c, Integer q) {
		stockChoco.put(c,stockChoco.get(c)-q);

	}
	

}
