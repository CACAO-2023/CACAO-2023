package abstraction.eq1Producteur1;

//classe écrite par Elouan

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.produits.Lot;

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
	
	public void supprimer(Double quantite) {
		if (quantite<=0 || quantite>this.getNbHectare()+0.001) {
			throw new IllegalArgumentException("Appel de retirer("+quantite+") sur un nb d'hectare dont la quantite totale est "+this.getNbHectare());
		} else {
			HashMap<Integer, Double> res=new HashMap<Integer, Double>();
			List<Integer> vides = new LinkedList<Integer>();
			double reste = quantite;
			for (Integer i : this.quantite.keySet()) {
				if (reste>0) {
					if (this.quantite.get(i)>reste) {
						res.put(i,reste);
						this.quantite.put(i,this.quantite.get(i)-reste);
						reste=0;
					} else {
						res.put(i,this.quantite.get(i));
						reste = reste - this.quantite.get(i);
						vides.add(i);
					}
				}
			}
			for (Integer step : vides) {
				this.quantite.remove(step);
			}
		}
	}
	
}
