package abstraction.eq1Producteur1;

//classe écrite par Elouan

import java.util.HashMap;

public class champ {
	private HashMap<Integer, Double> quantite; //integer : nb de step (key), double : nb d'hectar (value)
	
	public champ() { 
		this.quantite = new HashMap<Integer, Double>();
	}
	public HashMap<Integer, Double> getQuantite(){
		return this.quantite;
	}
	public void ajouter(int i, Double d) {
		Double q = d;
		if (this.quantite.containsKey(i)) {
			q = q+this.quantite.get(i);}
		this.quantite.put(i, q);
	}
	public double getNbHectare() {
		double q=0;
		for (Integer i : this.quantite.keySet()) {
			q = q + this.quantite.get(i);
		}
		return q;
	}
	public void supprimer(int i) {
		this.getQuantite().remove(i);
	}
	public String toString() {
		String s = "qualite : ";
		for (Integer i : this.getQuantite().keySet()) {
			s=s+"On a "+this.getQuantite().get(i)+" hectares qui datent de "+i+" steps ("+(i*15)+"jours).";
		}
		return s;
	}
	
}
