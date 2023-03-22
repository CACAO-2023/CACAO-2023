package abstraction.eq1Producteur1;

import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
//gab et charles

public class Feve1 {
	private Feve feve;
	private int nb_steps_depuis_récolte ; //compte le nb de steps depuis la récolte
	private boolean séché ; //booléen qui indique si la fève a séché
	
	public Feve1() {
		this.nb_steps_depuis_récolte = 0;
		this.séché = false ;
	}
	
	public void setNbStepsDepuisRecolte(int nb_steps) {
		this.nb_steps_depuis_récolte = nb_steps ;
	}
	
	public int getNbStepsDepuisRecolte() {
		return this.nb_steps_depuis_récolte ;
	}
	
	public boolean getSeche() {
		return this.séché ;
	}
	
	public Gamme getQualite() {
		return this.feve.getGamme();
	}

}
