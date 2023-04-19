package abstraction.eq3Producteur3;

import java.util.HashMap;
import java.util.Set;

public class Catastrophe extends Producteur3{
	
	public void fire(Champs fields) {
		
	}
	/**
	 * @author NAVEROS Marine
	 */	
	public void Cyclone(String s) {
		Champs fields = this.getFields();
		HashMap<Integer,Integer> FieldH = fields.getChamps().get("H");
		HashMap<Integer, Integer> FieldM = fields.getChamps().get("M");
		Set<Integer> KeysH = FieldH.keySet();
		Set<Integer> KeysM = FieldM.keySet();
		Journal j = this.getJCatastrophe();
		if (s.equals("H")) {
			
			
		}
		
		
		
		
		
	}
	
	
	
	
	
	
	
}
