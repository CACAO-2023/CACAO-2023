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
	public int nbFeve() {
		return this.getStock().size();
	}
	public void suppFeve(Feve f) {
		this.getStock().remove(f);
	}
	public void suppFeve(int i) {
		this.getStock().remove(i);
	}
}