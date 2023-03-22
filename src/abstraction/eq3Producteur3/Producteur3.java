package abstraction.eq3Producteur3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import abstraction.eqXRomu.produits.Lot;

public class Producteur3 extends Producteur3Acteur  {
	/*
	 * ChampsH est un dictionnaire qui contient les champs Haut de Gamme
	 * On associe a un ensemble d'hectars un int qui correspond à leur step de plantaison 
	 *
	 *private HashMap<Integer,String> ChampsH;//UTILE ?
	 *
	 * ChampsM est un dictionnaire qui contient les champs Moyen de Gamme
	 * On associe a un ensemble d'hectars un int qui correspond à leur step de plantaison 
	 *
	 *private HashMap<Integer,String> ChampsM;//UTILE ?
	 *
	 * On cree un dictionnaire qui associe à la clef H ou M le dico ChampsM ou ChapmsH
	 */
	private HashMap<String,HashMap> Champs;
	
	
	private Integer HectaresLibres; /*Repertorie le nombre d'hectares que l'on possede*/
	
	private Integer HectaresUtilises; /*Repertorie le nombre d'hectares que l'on utilise*/
	
	private Integer CoutStep; /* Tout nos couts du step, reinitialises a zero au debut de chaque step et payes a la fin du step*/
	
	/*
	 * Je n'ai pas trouve le type du champs donc j'ai choisit String. A CHANGER
	 * Il faudra aussi penser a se mettre d'accord sur les tailles des champs initiaux.
	 */
	public Producteur3() {
		super();
		HashMap<Integer,String> ChampsInitialeH = new HashMap<Integer,String>();
		HashMap<Integer,String> ChampsInitialeM = new HashMap<Integer,String>();
		HashMap<String,HashMap> ChampsInitiale = new HashMap<String,HashMap>();
		ChampsInitiale.put( "H",ChampsInitialeH);
		ChampsInitiale.put( "M",ChampsInitialeM);
		this.Champs=ChampsInitiale;
		Integer HectaresLibres = 0;
		Integer CoutStep = 0;
	}
	public Producteur3(HashMap<String,HashMap> m) {
		this.Champs=m;
	}
	
	public LinkedList<Lot> Harvest(int CurrentStep, HashMap<String,HashMap> Fields) {
		if(Champs==null) {
			throw new IllegalArgumentException("HashMap<String,HashMap> Fields ==null --> Pb");
		}
		/*
		 * Recolte des feves moyennes gammes
		 */
		HashMap<Integer,String> FieldM = Fields.get("M");
		//On recupere la liste des clefs de FieldM
		Set<Integer> KeysM = FieldM.keySet();
		//On cree une liste qui contient les clefs de tous les champs M qui doivent etre recoltes
		LinkedList<Integer> HarvestKeysM = new LinkedList<Integer>();
		//On regarde si un des champs de FieldM doit etre recolte sachant qu'un champ M doit etre recolte tous les 6 mois
		for(Integer key : KeysM) {
			/*DateActuelle-DatePlantaison=DureeGestation. Si DureeGestation est divisible par 12(nombre de semaines
			*dans 6 mois), alors le champ M doit être recolte
			**/
			if((CurrentStep - key)%12==0) {
				HarvestKeysM.add(key);
			}
		}
		//Lot lotH = HarvestM(HarvestKeysM,FieldM);
		/*
		 * Recolte des feves hautes gammes
		 */
		HashMap<Integer,String> FieldH = Fields.get("H");
		//On recupere la liste des clefs de FieldH
		Set<Integer> KeysH = FieldH.keySet();
		//On cree une liste qui contient les clefs de tous les champs H qui doivent etre recoltes
		LinkedList<Integer> HarvestKeysH = new LinkedList<Integer>();
		//On regarde si un des champs de FieldH doit etre recolte sachant qu'un champs H doit etre recolte tous les 7 mois
		for(Integer key : KeysM) {
			/*DateActuelle-DatePlantaison=DureeGestation. Si DureeGestation est divisible par 14(nombre de semaines
			*dans 7 mois), alors le champ H doit etre recolte
			**/
			if((CurrentStep - key)%14==0) {
				HarvestKeysH.add(key);
			}
		}
		//PARTIE RÉCOLTE-->Lot
		return null;
	}
	
	
	
}
