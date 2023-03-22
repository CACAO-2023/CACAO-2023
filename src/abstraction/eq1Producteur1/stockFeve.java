package abstraction.eq1Producteur1;

import java.util.LinkedList;

//elouan
 
public class stockFeve {
	private LinkedList<Feve1> stock;

	public LinkedList<Feve1> getStock(){
		return this.stock;
	}
	public Feve1 getFeve(int i) {
		return this.getStock().get(i);
	}
	public void ajouterFeve(Feve1 f) {
		this.getStock().add(f);
	}
	public int nbFeve() {
		return this.getStock().size();
	}
	public void suppFeve(Feve1 f) {
		this.getStock().remove(f);
	}
	public void suppFeve(int i) {
		this.getStock().remove(i);
	}
	
}