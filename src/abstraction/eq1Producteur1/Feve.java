package abstraction.eq1Producteur1;

//gab

public class Feve {
	private int nb_steps_depuis_récolte ; //compte le nb de steps depuis la récolte
	private boolean séché ; //booléen qui indique si la fève a séché
	private String qualite ; // indique la qualité de la fève
	private double quantite; // quantite de la recolte en tonne
	
	public Feve(String qualite, double quantite) {
		this.nb_steps_depuis_récolte = 0;
		this.séché = false ;
		this.qualite = qualite ;
		this.quantite = quantite;
	}
	
	public void setNbStepsDepuisRecolte(int nb_steps) {
		this.nb_steps_depuis_récolte = nb_steps ;
	}
	
	public void setSeche(boolean bool) {
		this.séché = bool ;
	}
	
	public int getNbStepsDepuisRecolte() {
		return this.nb_steps_depuis_récolte ;
	}
	
	public boolean getSeche() {
		return this.séché ;
	}
	
	public String getQualite() {
		return this.qualite ;
	}
	public double getQuantite() {
		return this.quantite;
	}

}
