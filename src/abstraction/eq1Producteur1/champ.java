package abstraction.eq1Producteur1;

//classe écrite par Elouan

import java.util.HashMap;
import abstraction.eqXRomu.produits.Gamme;

public class champ {
	private Gamme qualite; //qualite : B ou M
	private HashMap<Integer, Double> quantite; //integer : nb de step (key), double : nb d'hectar (value)
	
	public champ(Gamme g) { 
		this.quantite = new HashMap<Integer, Double>();
		this.qualite = g;
	}
	
	public Gamme getQualite(){
		return this.qualite;
	}
	public HashMap<Integer, Double> getQuantite(){
		return this.quantite;
	}
	public void ajouter(int i, Double d) {
		this.quantite.put(i, d);
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
