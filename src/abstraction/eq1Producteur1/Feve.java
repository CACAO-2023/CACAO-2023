package abstraction.eq1Producteur1;

//gab

public class Feve {
	private int nb_steps_depuis_récolte ; //compte le nb de steps depuis la récolte
	private boolean séché ; //booléen qui indique si la fève a séché
	private String qualité ; // indique la qualité de la fève
	
	public Feve() {
		this.nb_steps_depuis_récolte = 0;
		this.séché = false ;
		this.qualité = "Bas" ; //à changer pour prendre en compte la qualité associée au champ d'où la fève vient
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
	
	public String getQualite() {
		return this.qualité ;
	}

}
