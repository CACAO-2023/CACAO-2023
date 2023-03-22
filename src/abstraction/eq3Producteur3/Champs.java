package abstraction.eq3Producteur3;

import java.util.HashMap;

public class Champs {
	private HashMap<String, HashMap<Integer, Integer>> Champs; /* String H ou M (key) et Hashmap des champs. Champs contient tous les champs */
	
	/**
	 * @author Dubus-Chanson Victor, Corentin Caugant
	 */
	public Champs() {
		Integer NombreHectaresM = 22500;
		Integer NombreHectaresH = 1250;

		HashMap<Integer, Integer> ChampH = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> ChampM = new HashMap<Integer, Integer>();

		for (int Age = 0; Age > -960; Age = Age - 24) {
			ChampH.put(Age, NombreHectaresH);
			ChampM.put(Age, NombreHectaresM);
		}

		Champs = new HashMap<String, HashMap<Integer, Integer>>();
		Champs.put("M", ChampM);
		Champs.put("H", ChampH);
		
	}

	public HashMap<String, HashMap<Integer, Integer>> getChamps() {
		return Champs;
	}
	
	public void destructionVieuxHectares(int CurrentStep) {
		HashMap<Integer, Integer> ChampsH = Champs.get("H");
		HashMap<Integer, Integer> ChampsM = Champs.get("M");
		for (Integer i : ChampsH.keySet()) {
			if (CurrentStep - i == 960) {
				ChampsH.remove(i);
				break;
			}
		}
		for (Integer i : ChampsM.keySet()) {
			if (CurrentStep - i == 960) {
				ChampsM.remove(i);
				break;
			}
		}
	}
}
