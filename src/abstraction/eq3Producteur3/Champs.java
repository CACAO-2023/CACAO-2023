package abstraction.eq3Producteur3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import abstraction.eqXRomu.filiere.Filiere;

public class Champs {
	private HashMap<String, HashMap<Integer, Integer>> Champs; /* String H ou M (key) et Hashmap des champs. Champs contient tous les champs */
	
	/**
	 * @author Dubus-Chanson Victor, Corentin Caugant
	 */
	public Champs() {
		Integer NombreHectaresM = 22500;
		Integer NombreHectaresH =1250;
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

	/**
	 * @author BOCQUET Gabriel
	 */	
	public HashMap<String, HashMap<Integer, Integer>> getChamps() {
		return Champs;
	}

	public void setChampM(HashMap<Integer,Integer> f) {
		this.Champs.put("M", f);
	}
	
	public void setChampH(HashMap<Integer,Integer> f) {
		this.Champs.put("H", f);
	}
	
	/**
	 * @author BOCQUET Gabriel
	 * Cette fonction nous donne le taux de recolte en fonction de l'age des arbres. Les valeurs ont été trouvé par Florian de l'equipe 2
	 */
	public double gaussienne(double x) {
		Random r = new Random();
		double sigma = Filiere.LA_FILIERE.getParametre("Ecart-type gaussienne pour production").getValeur();
		double esperance = Filiere.LA_FILIERE.getParametre("Esperance gaussienne pour production").getValeur();
		return (600/(Math.sqrt(2*Math.PI)*sigma))*Math.exp(-(x-esperance)*(x-esperance)/(2*sigma*sigma));
	}
	/**
	 * @author BOCQUET Gabriel
	 */	
	public HashMap<String, LinkedList<Integer>> HarvestKeys(int CurrentStep) {
		String[] Gamme = {"M","H"};
		HashMap<String, HashMap<Integer, Integer>> Fields =this.getChamps();
		HashMap<String, LinkedList<Integer>> KeysList = new HashMap<String,LinkedList<Integer>>();
		if(Fields==null) {
			throw new IllegalArgumentException("HashMap<String,HashMap> Fields ==null --> Pb");
		}
		
		/*
		 * Recolte des feves moyennes gammes
		 */
		for(String s : Gamme) {
		HashMap<Integer,Integer> Field= Fields.get(s);
		//On recupere la liste des clefs de Field
		Set<Integer> Keys = Field.keySet();
		//On cree une liste qui contient les clefs de tous les champs qui doivent etre recoltes
		LinkedList<Integer> HarvestKeys = new LinkedList<Integer>();
		//On regarde si un des champs de Field doit etre recolte sachant qu'un champ doit etre recolte tous les 6 ou 7mois
		for(Integer key : Keys) {

			/*DateActuelle-DatePlantaison=DureeGestation. Si DureeGestation est divisible par 12(nombre de semaines
			*dans 6 mois), alors le champ M doit tre recolte
			**/
			if(s == "M") {
			if((CurrentStep - key)%14==0) {
				HarvestKeys.add(key);
			}
			}
			
			/*DateActuelle-DatePlantaison=DureeGestation. Si DureeGestation est divisible par 14(nombre de semaines
			*dans 7 mois), alors le champ M doit etre recolte
			**/
			if(s == "H") {
				if((CurrentStep - key)%16==0) {
					HarvestKeys.add(key);
				}
				}
		}
		
		
		KeysList.put(s,HarvestKeys);
		}
		return KeysList;
	}
	
	/**
	 * @author BOCQUET Gabriel
	 * Gaussienne ?
	 */	
	public LinkedList<Integer> HarvestQuantity(int CurrentStep, HashMap<String, LinkedList<Integer>> Keys){
		String[] Gamme = {"H","M"};
		HashMap<String, HashMap<Integer, Integer>> FieldList =this.getChamps();
		LinkedList<Integer> l = new LinkedList();
		//On recupere la liste des champs de moyenne gamme
		for(String s : Gamme) {
		HashMap<Integer,Integer> Field = FieldList.get(s);
		//On recupere la liste des clef 
		LinkedList<Integer> HarvestKeys = Keys.get(s);
		int quantite=0;
		//Ce taux permet de prendre en compte l'aspect aleatoire d'une recolte
		for (Integer key : HarvestKeys) {
			double HarvestRate = this.gaussienne(CurrentStep-key);
			quantite +=Field.get(key)*HarvestRate;
		}
			
			l.add(quantite);
		}
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
		Set<Integer> s1= (FieldM.keySet());
		LinkedList<Integer> allKeyM = new LinkedList<>(s1);
		Set<Integer> s2= (FieldH.keySet());
		LinkedList<Integer> allKeyH = new LinkedList<>(s2);
		int quantitePerdu = 0;
		while (quantitePerdu < NbrGreviste && HarvestKeysM.size()!=0 ) {
			//On regarde si les grevistes travaillaient sur les champs H ou M
			double MouH = Math.random() ;
			if(MouH <= 0.5) {
				double HarvestRateM =  0.9;
				//On choisit un nombre qui nous donnera la clef du champs ou il va y avoir des grevistes et ils ne travaillent pas forcement sur un champ qui a besoin d'etre recolte.
				Integer ChampGreve = (int)(Math.random() * (FieldM.size()-1));
				//on regarde si le champ ou les ouvriers font greve doit etre recolte
				if(!HarvestKeysM.contains(allKeyM.get(ChampGreve))) {
					quantitePerdu += FieldM.get(allKeyM.get(ChampGreve));
				}
				else {
				Integer key = allKeyM.get(ChampGreve);
				HarvestKeysM.remove(HarvestKeysM.indexOf(key)); 
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
				
			}
			else {
			//Ce taux permet de prendre en compte l'aspect aleatoire d'une recolte
			double HarvestRateH =  0.85;
			//On choisit la clef du champs où il va y avoir des grevistes.
			Integer ChampGreve = (int)(Math.random() * (FieldH.size()-1));
			if(!HarvestKeysH.contains(allKeyH.get(ChampGreve))) {
				quantitePerdu += FieldH.get(allKeyH.get(ChampGreve));
			}
			else {
			Integer keyH = allKeyH.get(ChampGreve);
			HarvestKeysH.remove(HarvestKeysH.indexOf(keyH));
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

		}
			LinkedList<Integer> l = new LinkedList();
			l.add(quantiteH);
			l.add(quantiteM);
			if(l.get(0) >0 && l.get(1) > 0) {
			}
			return l;

	
		}
  	/**
  	 * 
  	 * @param String s
  	 * @return taille
  	 * @author BOCQUET Gabriel
  	 */
  	//Renvoie la taille du champs de gamme H
	protected int getTaille(String s) {	
		HashMap<String,HashMap<Integer,Integer>> field = this.Champs;
		int taille = 0;
		if(s=="H") {
			HashMap<Integer,Integer> fieldH = field.get("H");
			for(Integer i : fieldH.keySet()) {
				taille += fieldH.get(i);
			}
		}
		if(s=="M") {
			HashMap<Integer,Integer> fieldM = field.get("M");
			for(Integer i : fieldM.keySet()) {
				taille += fieldM.get(i);
			}
	}
		return taille;
	}
}
