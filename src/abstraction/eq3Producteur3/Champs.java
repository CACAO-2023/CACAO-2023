package abstraction.eq3Producteur3;

import java.util.HashMap;

public class Champs {
	private HashMap<String, HashMap<Integer, Integer>> Champs; /*String H ou M (clé) et Hashmap des champs. Champs contient tous les champs*/
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public Champs() {
		HashMap<String, HashMap<Integer, Integer>> Champs = new HashMap<String, HashMap<Integer, Integer>>();
		Integer NombreHectaresM = 22500;
		Integer NombreHectaresH = 1250;
		Integer Age = 0;
		for (int i=1; i<40; i++) {
			HashMap<Integer, Integer> ChampH = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> ChampM = new HashMap<Integer, Integer>();
			ChampH.put(Age, NombreHectaresH);
			ChampM.put(Age, NombreHectaresM);
			Champs.put("M", ChampM);
			Champs.put("H", ChampH);
			Age += 1;
		}
		
	}
	
	
}
