package abstraction.eq3Producteur3;

import java.awt.Color;
import java.util.HashMap;
import java.util.Set;

import abstraction.eqXRomu.general.Journal;

public class Catastrophe extends Producteur3{
	
	/**
	 * @author BOCQUET Gabriel
	 * @param s
	 */
	public void Fire(String s) {
			Champs fields = this.getFields();
			HashMap<Integer,Integer> FieldsH = fields.getChamps().get("H");
			HashMap<Integer,Integer> FieldsM = fields.getChamps().get("M");
			double hectarMburnt = 0;
			double hectarHburnt = 0;
			Set<Integer> KeyM = FieldsM.keySet();
			Set<Integer> KeyH = FieldsH.keySet();
			Journal j = this.getJCatastrophe();
			if(s.equals("Big")) {
				for(Integer key : KeyM) {
					hectarMburnt += FieldsM.get(key)*0.5;
					FieldsM.put(key,(int) (FieldsM.get(key)*0.5));
				}
				j.ajouter(Color.gray, Color.black, hectarMburnt + " d'hectares de Moyenne Gamme d'arbres ont brulé");
				for(Integer key : KeyH) {
					hectarHburnt = FieldsH.get(key)*0.5;
					FieldsH.put(key,(int) (FieldsH.get(key)*0.5));
				}
				j.ajouter(Color.yellow, Color.black, hectarHburnt + " d'hectares de Haute Gamme d'arbres ont brulé");
			}
			else if(s.equals("Med")) {
				for(Integer key : KeyM) {
					hectarMburnt += FieldsM.get(key)*0.2;
					FieldsM.put(key,(int) (FieldsM.get(key)*0.8));
				}
				j.ajouter(Color.gray, Color.black, hectarMburnt + " d'hectares de Moyenne Gamme d'arbres ont brulé");
				for(Integer key : KeyH) {
					hectarHburnt = FieldsH.get(key)*0.2;
					FieldsH.put(key,(int) (FieldsH.get(key)*0.8));
				}
				j.ajouter(Color.yellow, Color.black, hectarHburnt + " d'hectares de Haute Gamme d'arbres ont brulé");
			}
			else if(s.equals("Lit")) {
				for(Integer key : KeyM) {
					hectarMburnt += FieldsM.get(key)*0.1;
					FieldsM.put(key,(int) (FieldsM.get(key)*0.9));
				}
				j.ajouter(Color.gray, Color.black, hectarMburnt + " d'hectares de Moyenne Gamme d'arbres ont brulé");
				for(Integer key : KeyH) {
					hectarHburnt = FieldsH.get(key)*0.1;
					FieldsH.put(key,(int) (FieldsH.get(key)*0.9));
				}
				j.ajouter(Color.yellow, Color.black, hectarHburnt + " d'hectares de Haute Gamme d'arbres ont brulé");
			}
		
	}
	//Pour modéliser la grève générale, on va considérer les champs qui ne sont pas récoltés seront une perte de fève
	protected void GreveGeneral() {
		//On a autant d'employé que d'hectare Utilisé
		Integer nbrgreviste = ((Integer)super.getHectaresUt()*0.8);
		
	}
	
	
	
	
	
	
}
