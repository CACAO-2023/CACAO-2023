package abstraction.eq3Producteur3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

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
	}
	public Producteur3(HashMap<String,HashMap> m) {
		this.Champs=m;
	}
	
	public HashMap<String, LinkedList<Integer>> HarvestKeys(int CurrentStep, HashMap<String,HashMap> Fields) {
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
			/*DateActuelle-DatePlantaison=DureeGestation. Si DureeGestation est divisible par 14(nombre de semaines
			*dans 6 mois + 2 semaines de fermentation + 2 semaines de sechage), alors le champ M doit être recolte
			**/
			if((CurrentStep - key)%14==0) {
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
			*dans 7 mois+ 2 semaines de fermentation + 2 semaines de sechage), alors le champ H doit etre recolte
			**/
			if((CurrentStep - key)%16==0) {
				HarvestKeysH.add(key);
			}
		}
		HashMap<String, LinkedList<Integer>> KeysList = new HashMap<String,LinkedList<Integer>>();
		KeysList.put("H",HarvestKeysH);
		KeysList.put("M",HarvestKeysM);
		return KeysList;
	}
	public LinkedList<Integer> Harvest(int CurrentStep, HashMap<String, LinkedList<Integer>> Keys, HashMap<String,HashMap> FieldList){
		//On recupere la liste des champs de moyenne gamme
		HashMap<Integer,String> FieldM = FieldList.get("M");
		//On recupere la liste des clef M
		LinkedList<Integer> HarvestKeysM = Keys.get("M");
		int quantiteM=0;
		//Ce taux permet de prendre en compte l'aspect aleatoire d'une recolte
		double HarvestRateM = (Math.random() * (1.1- 0.8)) + 0.8;
		for (Integer key : HarvestKeysM) {
			//Le champ M a entre 0 et 3 ans
			if ((CurrentStep-key) <72 && (CurrentStep-key)>=0) {
				//quantiteM += 0 //Champ pas assez vieux
			}
			//Le champ M a entre 3 et 7 ans
			else if((CurrentStep-key) <168 && (CurrentStep-key)>=72) {
				//
				//quantiteM += FieldM.get(key).getFieldRange()*0.5*HarvestRateM //Champ jeune
			}
			//Le champ M a entre 7 et 35 ans
			else if((CurrentStep-key) <840 && (CurrentStep-key)>=168) {
				//
				//quantiteM += FieldM.get(key).getFieldRange()*HarvestRateM //Champ convenable
			}
			//Le champ a entre 35 et 40 ans
			else if((CurrentStep-key) <840 && (CurrentStep-key)>=960) {
				//
				//quantiteM += FieldM.get(key).getFieldRange()*0.5*HarvestRateM //Champ vieux
			}
			//L'arbre est trop vieux
			else {
				quantiteM +=0;
			}
		}
			
			HashMap<Integer,String> FieldH = FieldList.get("H");
			//On recupere la liste des clef M
			LinkedList<Integer> HarvestKeysH = Keys.get("H");
			int quantiteH=0;
			//Ce taux permet de prendre en compte l'aspect aleatoire d'une recolte
			double HarvestRateH = (Math.random() * (1.1- 0.8)) + 0.8;
			for (Integer keyH : HarvestKeysH) {
				//Le champ M a entre 0 et 3 ans
				if ((CurrentStep-keyH) <72 && (CurrentStep-keyH)>=0) {
					//quantiteH += 0 //Champ pas assez vieux
				}
				//Le champ M a entre 3 et 7 ans
				else if((CurrentStep-keyH) <168 && (CurrentStep-keyH)>=72) {
					//
					//quantiteH += FieldH.get(keyH).getFieldRange()*0.5*HarvestRateH //Champ jeune
				}
				//Le champ M a entre 7 et 35 ans
				else if((CurrentStep-keyH) <840 && (CurrentStep-keyH)>=168) {
					//
					//quantiteH += FieldH.get(keyH).getFieldRange()*HarvestRateH //Champ convenable
				}
				//Le champ a entre 35 et 40 ans
				else if((CurrentStep-keyH) <840 && (CurrentStep-keyH)>=960) {
					//
					//quantiteH += FieldH.get(keyH).getFieldRange()*0.5*HarvestRateH //Champ vieux
				}
				//L'arbre est trop vieux
				else {
					quantiteH +=0;
				}
		}
			LinkedList<Integer> l = new LinkedList();
			l.add(quantiteH);
			l.add(quantiteM);
			return l;
	}
}
