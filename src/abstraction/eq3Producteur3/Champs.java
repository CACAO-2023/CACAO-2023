package abstraction.eq3Producteur3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class Champs {
	private HashMap<String, HashMap<Integer, Integer>> Champs; /* String H ou M (key) et Hashmap des champs. Champs contient tous les champs */
	
	/**
	 * @author Dubus-Chanson Victor, Corentin Caugant
	 */
	public Champs() {
		Integer NombreHectaresM = 22500;
		Integer NombreHectaresH = 1250;

		HashMap<Integer, Integer> ChampH = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> ChampM = new HashMap<Integer, Integer>();

		for (int Age = 0; Age > -960; Age = Age - 24) {
			ChampH.put(Age, NombreHectaresH);
			ChampM.put(Age, NombreHectaresM);
		}

		Champs = new HashMap<String, HashMap<Integer, Integer>>();
		Champs.put("M", ChampM);
		Champs.put("H", ChampH);
		
	}

	public HashMap<String, HashMap<Integer, Integer>> getChamps() {
		return Champs;
	}

	/**
	 * @author BOCQUET Gabriel
	 */	
	public HashMap<String, LinkedList<Integer>> HarvestKeys(int CurrentStep) {
		HashMap<String, HashMap<Integer, Integer>> Fields =this.getChamps();
		if(Fields==null) {
			throw new IllegalArgumentException("HashMap<String,HashMap> Fields ==null --> Pb");
		}
		/*
		 * Recolte des feves moyennes gammes
		 */
		HashMap<Integer,Integer> FieldM = Fields.get("M");
		//On recupere la liste des clefs de FieldM
		Set<Integer> KeysM = FieldM.keySet();
		//On cree une liste qui contient les clefs de tous les champs M qui doivent etre recoltes
		LinkedList<Integer> HarvestKeysM = new LinkedList<Integer>();
		//On regarde si un des champs de FieldM doit etre recolte sachant qu'un champ M doit etre recolte tous les 6 mois
		for(Integer key : KeysM) {

			/*DateActuelle-DatePlantaison=DureeGestation. Si DureeGestation est divisible par 12(nombre de semaines
			*dans 6 mois), alors le champ M doit tre recolte
			**/
			if((CurrentStep - key)%14==0) {
				HarvestKeysM.add(key);
			}
		}
		//Lot lotH = HarvestM(HarvestKeysM,FieldM);
		/*
		 * Recolte des feves hautes gammes
		 */
		HashMap<Integer,Integer> FieldH = Fields.get("H");
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
	
	/**
	 * @author BOCQUET Gabriel
	 */	
	public LinkedList<Integer> HarvestQuantity(int CurrentStep, HashMap<String, LinkedList<Integer>> Keys){
		HashMap<String, HashMap<Integer, Integer>> FieldList =this.getChamps();
		//On recupere la liste des champs de moyenne gamme
		HashMap<Integer,Integer> FieldM = FieldList.get("M");
		//On recupere la liste des clef M
		LinkedList<Integer> HarvestKeysM = Keys.get("M");
		int quantiteM=0;
		//Ce taux permet de prendre en compte l'aspect aleatoire d'une recolte
		double HarvestRateM = (Math.random() * (1.1- 0.9)) + 0.9;
		for (Integer key : HarvestKeysM) {
			//Le champ M a entre 0 et 3 ans
			if ((CurrentStep-key) <72 && (CurrentStep-key)>=0) {
				quantiteM += 0; //Champ pas assez vieux
			}
			//Le champ M a entre 3 et 7 ans
			else if((CurrentStep-key) <168 && (CurrentStep-key)>=72) {
				
				quantiteM += FieldM.get(key)*0.56*0.5*HarvestRateM; //Champ jeune
				
			}
			//Le champ M a entre 7 et 35 ans
			else if((CurrentStep-key) <840 && (CurrentStep-key)>=168) {
				
				quantiteM += FieldM.get(key)*0.56*HarvestRateM; //Champ convenable
				
			}
			//Le champ a entre 35 et 40 ans
			else if((CurrentStep-key) <840 && (CurrentStep-key)>=960) {
				
				quantiteM += FieldM.get(key)*0.56*0.5*HarvestRateM; //Champ vieux
			}
			//L'arbre est trop vieux
			else {
				quantiteM +=0;
			}
		}
			
			HashMap<Integer,Integer> FieldH = FieldList.get("H");
			//On recupere la liste des clef H
			LinkedList<Integer> HarvestKeysH = Keys.get("H");
			int quantiteH=0;
			//Ce taux permet de prendre en compte l'aspect aleatoire d'une recolte
			double HarvestRateH = (Math.random() * (1.1- 0.85)) + 0.85;
			for (Integer keyH : HarvestKeysH) {
				//Le champ M a entre 0 et 3 ans
				if ((CurrentStep-keyH) <72 && (CurrentStep-keyH)>=0) {
					quantiteH += 0; //Champ pas assez vieux
				}
				//Le champ M a entre 3 et 7 ans
				else if((CurrentStep-keyH) <168 && (CurrentStep-keyH)>=72) {
					
					quantiteH += FieldH.get(keyH)*0.56*0.5*HarvestRateH; //Champ jeune
				}
				//Le champ M a entre 7 et 35 ans
				else if((CurrentStep-keyH) <840 && (CurrentStep-keyH)>=168) {
					
					quantiteH += FieldH.get(keyH)*0.56*HarvestRateH; //Champ convenable
				}
				//Le champ a entre 35 et 40 ans
				else if((CurrentStep-keyH) <840 && (CurrentStep-keyH)>=960) {
					
					quantiteH += FieldH.get(keyH)*0.56*0.5*HarvestRateH; //Champ vieux
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
	/**
	 * @author BOCQUET Gabriel
	 */	

	//Cette fonction nous donne les quantitites produites lors de CurrentStep
	public LinkedList<Integer> HarvestHM(int CurrentStep){
		HashMap<String, LinkedList<Integer>> Keys = this.HarvestKeys(CurrentStep);
		LinkedList<Integer> q = this.HarvestQuantity(CurrentStep, Keys);
		return q;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	/*Ajouter HectaresLiberes a HectaresLibres de Producteur3 a chaque step*/
	/*Utilisee dans Producteur3 dans methode updateHectaresLiberes()*/
  	public Integer destructionVieuxHectares(int CurrentStep) {
		HashMap<Integer, Integer> ChampsH = Champs.get("H");
		HashMap<Integer, Integer> ChampsM = Champs.get("M");
		Integer HectaresLiberes = 0;
		for (Integer i : ChampsH.keySet()) {
			if (CurrentStep - i == 960) {
				HectaresLiberes += ChampsH.get(i);
				ChampsH.remove(i);
				break;
			}
		}
		for (Integer i : ChampsM.keySet()) {
			if (CurrentStep - i == 960) {
				HectaresLiberes += ChampsM.get(i);
				ChampsM.remove(i);
				break;
			}
		}

		return HectaresLiberes;

	}
  	
  	/**
  	 * 
  	 * @param CurrentStep
  	 * @param Keys
  	 * @param NbrGreviste
  	 * @author BOCQUET Gabriel
  	 */
  	//Quantitée théorique produite par les gréviste
  	public LinkedList<Integer> HarvestQuantityG(int CurrentStep, HashMap<String, LinkedList<Integer>> Keys,int NbrGreviste){
		HashMap<String, HashMap<Integer, Integer>> FieldList =this.getChamps();
		//On recupere la liste des champs de moyenne gamme
		HashMap<Integer,Integer> FieldM = FieldList.get("M");
		//On recupere la liste des clef M
		LinkedList<Integer> HarvestKeysM = Keys.get("M");
		int quantiteM=0;
		HashMap<Integer,Integer> FieldH = FieldList.get("H");
		//On recupere la liste des clefs H
		LinkedList<Integer> HarvestKeysH = Keys.get("H");
		int quantiteH=0;
		
		int quantitePerdu = 0;
		while (quantitePerdu < NbrGreviste) {
			//On regarde si on choisit un champs Haut de Gamme ou Moyenne Gamme
			double MouH = Math.random() ;
			if(MouH <= 0.5) {
				//Ce taux permet de prendre en compte l'aspect aleatoire d'une recolte
				double HarvestRateM = (Math.random() * (1.1- 0.9)) + 0.9;
				//On choisit la clef du champs où il va y avoir des grevistes.
				Integer ChampGreve = (int)(Math.random() * (HarvestKeysM.size()-1));
				Integer key = HarvestKeysM.get(ChampGreve);
				if ((CurrentStep-key) <72 && (CurrentStep-key)>=0) {
					quantiteM += 0; //Champ pas assez vieux
					quantitePerdu +=FieldM.get(key);
				}
				//Le champ M a entre 3 et 7 ans
				else if((CurrentStep-key) <168 && (CurrentStep-key)>=72) {
					
					quantiteM += FieldM.get(key)*0.56*0.5*HarvestRateM; //Champ jeune
					quantitePerdu +=FieldM.get(key);
					
				}
				//Le champ M a entre 7 et 35 ans
				else if((CurrentStep-key) <840 && (CurrentStep-key)>=168) {
					
					quantiteM += FieldM.get(key)*0.56*HarvestRateM; //Champ convenable
					quantitePerdu +=FieldM.get(key);
					
				}
				//Le champ a entre 35 et 40 ans
				else if((CurrentStep-key) <840 && (CurrentStep-key)>=960) {
					
					quantiteM += FieldM.get(key)*0.56*0.5*HarvestRateM; //Champ vieux
					quantitePerdu +=FieldM.get(key);
				}
				//L'arbre est trop vieux
				else {
					quantiteM +=0;
					quantitePerdu +=FieldM.get(key);
				}
			}
			else {
			//Ce taux permet de prendre en compte l'aspect aleatoire d'une recolte
			double HarvestRateH = (Math.random() * (1.1- 0.85)) + 0.85;
			//On choisit la clef du champs où il va y avoir des grevistes.
			Integer ChampGreve = (int)(Math.random() * (HarvestKeysM.size()-1));
			Integer keyH = HarvestKeysH.get(ChampGreve);

				//Le champ M a entre 0 et 3 ans
				if ((CurrentStep-keyH) <72 && (CurrentStep-keyH)>=0) {
					quantiteH += 0; //Champ pas assez vieux
					quantitePerdu +=FieldH.get(keyH);
				}
				//Le champ M a entre 3 et 7 ans
				else if((CurrentStep-keyH) <168 && (CurrentStep-keyH)>=72) {
					
					quantiteH += FieldH.get(keyH)*0.56*0.5*HarvestRateH; //Champ jeune
					quantitePerdu +=FieldH.get(keyH);
				}
				//Le champ M a entre 7 et 35 ans
				else if((CurrentStep-keyH) <840 && (CurrentStep-keyH)>=168) {
					
					quantiteH += FieldH.get(keyH)*0.56*HarvestRateH; //Champ convenable
					quantitePerdu +=FieldH.get(keyH);
				}
				//Le champ a entre 35 et 40 ans
				else if((CurrentStep-keyH) <840 && (CurrentStep-keyH)>=960) {
					
					quantiteH += FieldH.get(keyH)*0.56*0.5*HarvestRateH; //Champ vieux
					quantitePerdu +=FieldH.get(keyH);
				}
				//L'arbre est trop vieux
				else {
					quantiteH +=0;
					quantitePerdu +=FieldH.get(keyH);
				}
			}
		}
			LinkedList<Integer> l = new LinkedList();
			l.add(quantiteH);
			l.add(quantiteM);
			return l;

	
		}
  	
	
}
