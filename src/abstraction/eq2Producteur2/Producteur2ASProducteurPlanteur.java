// Code écrit par Florian Desplanque

package abstraction.eq2Producteur2;

import java.util.LinkedList;
import java.util.HashMap;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;

import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;



public class Producteur2ASProducteurPlanteur extends Producteur2AStockeur{
	public HashMap<Feve, Integer> employes;
	public HashMap<Feve, Double> salaires;
	public HashMap<Feve, Integer> surface_plantation;
	public HashMap<Feve, Double> prix;
	public HashMap<Feve,HashMap<Integer, Integer>> age_hectares; 
	// Pour age_hectares nous avons un Hashmap dans un autre, ici la première clef fait reference à la Feve car 
	//Les employes auront differents salaires selon la gamme sur laquelle ils travaillent
	//La deuxiemes clef est un entirer qui correspond à l'age (en step) des hectares
	//Ainsi pour chaque type de feve et chaque age on connait le nombre d'hectares que l'on possède

	public Producteur2ASProducteurPlanteur() {
		super();
		this.employes = new HashMap<Feve, Integer>();
		this.salaires = new HashMap<Feve, Double>();
		this.surface_plantation = new HashMap<Feve, Integer>();
		this.prix = new HashMap<Feve, Double>();
		this.age_hectares =  new HashMap<Feve,HashMap<Integer, Integer>>();
		for (Feve f: this.lesFeves)
			this.age_hectares.put(f, new HashMap<Integer, Integer>());
		setEmploye(300000, 300000, 250000, 50000);
		setSalaires(50,50,200,300);
		setSurface(300000, 300000, 250000, 50000);
		setPrix(4000, 5500, 6000, 8000);
		setAge(Feve.F_BQ, 24*3, 300000);
		setAge(Feve.F_MQ, 24*3, 300000);
		setAge(Feve.F_MQ_BE, 24*3, 250000);
		setAge(Feve.F_HQ_BE, 24*3, 50000);
	}
	// Mise en place des differents setters
	
	public HashMap<Feve, Integer> getEmployes(){
		return this.employes;
	}
	public HashMap<Feve, Double> getSalaires(){
		return this.salaires;
	}
	public HashMap<Feve, Integer> getSurface(){
		return this.surface_plantation;
	}

	public HashMap<Feve, Double> getPrix(){
		return this.prix;
	}
	
	public HashMap<Feve,HashMap<Integer, Integer>> get_Age_Hectares(){
		return this.age_hectares;
	}
	// Mise en place des differents setters qui pour la version 1 ne sont pas encore tous utilies car seuls les 
	//plantations et le nombre d'employes evoluent 
	//
	public void setEmploye(int employes_BQ, int employes_MQ, int employes_MQ_BE, int employes_HQ_BE) {
		this.employes.put(Feve.F_BQ, employes_BQ);
		this.employes.put(Feve.F_MQ, employes_MQ);
		this.employes.put(Feve.F_MQ_BE, employes_MQ_BE);
		this.employes.put(Feve.F_HQ_BE, employes_HQ_BE);
	}
	
	/**
	 * @author Florian
	 * @param salaire_employes_BQ
	 * @param salaire_employes_MQ
	 * @param salaire_employes_MQ_BE
	 * @param salaire_employes_HQ_BE
	 */
	public void setSalaires(double salaire_employes_BQ, double salaire_employes_MQ, double salaire_employes_MQ_BE, double salaire_employes_HQ_BE) {
		this.salaires.put(Feve.F_BQ, salaire_employes_BQ);
		this.salaires.put(Feve.F_MQ,salaire_employes_MQ);
		this.salaires.put(Feve.F_MQ_BE, salaire_employes_MQ_BE);
		this.salaires.put(Feve.F_HQ_BE, salaire_employes_HQ_BE);
		
	}
	public void setPrix(double prix_BQ, double prix_MQ, double prix_MQ_BE, double prix_HQ_BE) {
		this.prix.put(Feve.F_BQ, prix_BQ);
		this.prix.put(Feve.F_MQ,prix_MQ);
		this.prix.put(Feve.F_MQ_BE, prix_MQ_BE);
		this.prix.put(Feve.F_HQ_BE, prix_HQ_BE);
		
	}
	public void setSurface(int surface_plantation_BQ, int surface_plantation_MQ, int surface_plantation_MQ_BE, int surface_plantation_HQ_BE) {
		this.surface_plantation.put(Feve.F_BQ, surface_plantation_BQ);
		this.surface_plantation.put(Feve.F_MQ, surface_plantation_MQ);
		this.surface_plantation.put(Feve.F_MQ_BE, surface_plantation_MQ_BE);
		this.surface_plantation.put(Feve.F_HQ_BE, surface_plantation_HQ_BE);
	}
	
	public void setAge(Feve f, int age, int qte) {
		this.age_hectares.get(f).put(age, qte);
	}
	
	
	public void Planter(Feve f, int i) {
		this.age_hectares.get(f).put(Filiere.LA_FILIERE.getEtape(), i);
	}
		

	// Méthodes à utiliser dans chaque next //

		
		
	//}
	
	//La fonction ci_dessous prevois les quantities de cacao que l'on sera apte a produire à une step donnée
	//Avec nos terres actuelles. En effet dans notre cas un hectare met 3 ans pour que les cacaoyers dessus
	//puissent produire des feves, ils produisent des feves de maniere constante jusqu'a 40 ans
	
	public HashMap<Feve, Double> Prevision_Production(int step){
		HashMap<Feve, Double> prevision_production = new HashMap<Feve, Double>();
		for (Feve f : this.age_hectares.keySet()) {
			double qte = 0;
			for (int i : this.age_hectares.get(f).keySet()){
				if (step-i<3*24) {
					qte =+ 0;
				}
				if (step-i>=3*24 && Filiere.LA_FILIERE.getEtape()-i<40*24) {
					qte =+ prodHec.getValeur()*this.age_hectares.get(f).get(i);
				}
				if (step-i>=40*24) {
					this.age_hectares.get(f).remove(i);
				}
			}
			prevision_production.put(f, qte);
		}
		return prevision_production;	
	}
	
	// Cette fonction calcul les côut de production de chaque feve, on prend uniquement la masse salariale
	// dediee a chaque feve
	
	public HashMap<Feve, Double> CoutProd(){
		HashMap<Feve, Double> coûts_totaux = new HashMap<Feve, Double>();
		for (Feve f : this.salaires.keySet()) {
			coûts_totaux.put(f, this.salaires.get(f)*this.employes.get(f)); }
		return coûts_totaux;
	}
	
	//Pour prevoir les ventes on se base sur la somme des contrats cadre et la quantite vendue en bourse
	
	public HashMap<Feve, Double> Prevision_Vente(){
		 HashMap<Feve, Double> prevision_vente = new  HashMap<Feve, Double>();
		 prevision_vente.put(Feve.F_BQ, BQquantiteVendueBourse.getValeur());
		 prevision_vente.put(Feve.F_MQ, aLivrerStep(Filiere.LA_FILIERE.getEtape(), Feve.F_MQ)+MQquantiteVendueBourse.getValeur() );
		 prevision_vente.put(Feve.F_MQ_BE, aLivrerStep(Filiere.LA_FILIERE.getEtape(), Feve.F_MQ_BE));
		 prevision_vente.put(Feve.F_HQ_BE, aLivrerStep(Filiere.LA_FILIERE.getEtape(), Feve.F_HQ_BE));
		return prevision_vente;	
	}
	
	//Cette premiere fonction de rentrabilite permet de savoir pour une feve donne sur la prix choisi permet
	//une rentabilite superieure a 10%
	
	public boolean Rentabilites(Feve f, Double prix){
		double rentabilite = prix*Prevision_Production(Filiere.LA_FILIERE.getEtape()).get(f)/CoutProd().get(f);
		if (rentabilite>=1.1) {
			return true;
		}
		return false;	
	}
	
	//Cette fonction est legerement differente, ici on ne renvoit pas un bouleen mais selon la feve la fonction
	//nous renvoit le prix minimum de rentabilite de celle-ci
	
	public double prix_rentable(Feve f) {
		return 1.1*CoutProd().get(f)/Prevision_Production(Filiere.LA_FILIERE.getEtape()).get(f);
	}
	
	//
	public void ajustement_plantation() {
		for (Feve f : this.salaires.keySet()) {
			int nb_a_planter = 0;
			for (int i : this.age_hectares.get(f).keySet()){
				if (Filiere.LA_FILIERE.getEtape()-i>= 37*24) {
					nb_a_planter =+ 1;
				}
			if (Filiere.LA_FILIERE.getEtape()%24==0) {
				nb_a_planter =(int) + Math.round(Filiere.LA_FILIERE.getEtape()*0.03);
			}
			}
			Planter(f, nb_a_planter);	
		}
	}
	// Une fois les plantation ajustees, sachant qu'un employe s'occupe d'un hectare on adapte le nombre
	//d'employes aux nombres d'hectares
	public void ajustement_employes () {
		HashMap<Feve,Integer> B = new HashMap<Feve,Integer>();
		for (Feve f : this.age_hectares.keySet()){
			int A=0;
			for (int i=3*24 ; i<age_hectares.get(f).size(); i++) {
				A=+age_hectares.get(f).get(i);
			}
			if (f == Feve.F_MQ_BE ||f == Feve.F_HQ_BE ) {
				if (A<0.95*this.employes.get(f)) {
					A = (int) Math.ceil(0.95*this.employes.get(f));
				}
			}
			B.put(f, A);	
		}
		setEmploye(B.get(Feve.F_BQ), B.get(Feve.F_MQ), B.get(Feve.F_MQ_BE), B.get(Feve.F_HQ_BE));	
	}
	
	public void next() {
		super.next();
		this.CoutProd();
		this.Prevision_Vente();
		this.ajustement_plantation();
		this.Prevision_Production(Filiere.LA_FILIERE.getEtape());
		for (Feve f : this.age_hectares.keySet()) {
			if (Prevision_Production(Filiere.LA_FILIERE.getEtape()).get(f)!=0){
			this.ajouterStock(f, Filiere.LA_FILIERE.getEtape(), Prevision_Production(Filiere.LA_FILIERE.getEtape()).get(f));
		}
		this.ajustement_employes();
		}
	}
	
	
	
	// Classe calculant la production et les coûts de production, et gère les stocks de notre acteur

	
	
	// associer un employé par hectare

	
	

}
