package abstraction.eq1Producteur1;

import java.util.LinkedList;

//elouan
 
public class stockFeve {
	private LinkedList<Feve> stock;

	public LinkedList<Feve> getStock(){
		return this.stock;
	}
	public Feve getFeve(int i) {
		return this.getStock().get(i);
	}
	public void ajouterFeve(Feve f) {
		this.getStock().add(f);
	}
	public double nbFeve() {
		int n = this.getStock().size();
		double s = 0.0; 
		for (int i=0; i<n; i++) {
			s=s+this.getFeve(i).getQuantite();
		}
		return s;
	}
	public void suppFeve(Feve f) {
		this.getStock().remove(f);
	}
	public void suppFeve(int i) {
		this.getStock().remove(i);
	}
	
}