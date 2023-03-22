package abstraction.eq3Producteur3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class Producteur3 extends Producteur3Acteur  {
	

	
	private Integer HectaresLibres; /*Repertorie le nombre d'hectares que l'on possede*/
	
	private Integer HectaresUtilises; /*Repertorie le nombre d'hectares que l'on utilise*/
	
	private Integer CoutStep; /* Tout nos couts du step, reinitialises a zero au debut de chaque step et payes a la fin du step*/
	
	/*
	 * Je n'ai pas trouve le type du champs donc j'ai choisit String. A CHANGER
	 * Il faudra aussi penser a se mettre d'accord sur les tailles des champs initiaux.
	 */
	public Producteur3() {
		super();
		Integer HectaresLibres = 0;
		Integer HectaresUtilises = 950000;
		Integer CoutStep = 0;
	}
		
	/*
	 * @author Dubus-Chanson Victor
	 */
	public void addCoutHectaresUtilises() {
		Integer coutEmployes = HectaresUtilises * 220;
		this.CoutStep = this.CoutStep + coutEmployes;
	}
	
}
