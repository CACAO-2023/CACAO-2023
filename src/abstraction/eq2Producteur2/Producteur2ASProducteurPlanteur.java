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

	public Producteur2ASProducteurPlanteur() {
		super();
		this.employes = new HashMap<Feve, Integer>();
		this.salaires = new HashMap<Feve, Double>();
		this.surface_plantation = new HashMap<Feve, Integer>();
	}
		
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
	
	public void setEmploye(int employes_BQ, int employes_MQ, int employes_MQ_BE, int employes_HQ_BE) {
		this.employes.put(Feve.F_BQ, employes_BQ);
		this.employes.put(Feve.F_MQ, employes_MQ);
		this.employes.put(Feve.F_MQ_BE, employes_MQ_BE);
		this.employes.put(Feve.F_HQ_BE, employes_HQ_BE);
	}
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
		this.employes.put(Feve.F_MQ, surface_plantation_MQ);
		this.employes.put(Feve.F_MQ_BE, surface_plantation_MQ_BE);
		this.employes.put(Feve.F_HQ_BE, surface_plantation_HQ_BE);
	}
	public void Planter(Feve f, int i) {
		this.age_hectares.get(f).put(Filiere.LA_FILIERE.getEtape(), i);
	}
		

	// Méthodes à utiliser dans chaque next //

		
		
	//}
	public void next() {
		super.next();
		this.CoutProd();
		this.Prevision_Vente();
		this.ajustement();
		this.Prevision_Production(Filiere.LA_FILIERE.getEtape());
		for (Feve f : this.age_hectares.keySet()) {
			this.ajouterStock(f, Filiere.LA_FILIERE.getEtape(), Prevision_Production(Filiere.LA_FILIERE.getEtape()).get(f));	
		}
		
		
	}
	public HashMap<Feve, Double> Prevision_Production(int step){
		HashMap<Feve, Double> prevision_production = new HashMap<Feve, Double>();
		for (Feve f : this.age_hectares.keySet()) {
			double qte = 0;
			for (int i : this.age_hectares.get(f).keySet()){
				if (step-i<3*24) {
					qte =+ 0;
				}
				if (step-i>=3*24 && Filiere.LA_FILIERE.getEtape()-i<40*24) {
					qte =+ prodHec.getValeur();
				}
				if (step-i>=40*24) {
					this.age_hectares.get(f).remove(i);
				}
			}
			prevision_production.put(f, qte);
		}
		return prevision_production;	
	}
	
	public HashMap<Feve, Double> CoutProd(){
		HashMap<Feve, Double> coûts_totaux = new HashMap<Feve, Double>();
		for (Feve f : this.salaires.keySet()) {
			coûts_totaux.put(f, this.salaires.get(f)*this.employes.get(f)); }
		return coûts_totaux;
	}
	public HashMap<Feve, Double> Prevision_Vente(){
		 HashMap<Feve, Double> prevision_vente = new  HashMap<Feve, Double>();
		 prevision_vente.put(Feve.F_BQ, BQquantiteVendueBourse.getValeur());
		 prevision_vente.put(Feve.F_MQ, aLivrerStep(Filiere.LA_FILIERE.getEtape(), Feve.F_MQ)+MQquantiteVendueBourse.getValeur() );
		 prevision_vente.put(Feve.F_MQ_BE, aLivrerStep(Filiere.LA_FILIERE.getEtape(), Feve.F_MQ_BE));
		 prevision_vente.put(Feve.F_HQ_BE, aLivrerStep(Filiere.LA_FILIERE.getEtape(), Feve.F_HQ_BE));
		return prevision_vente;	
	}
	public boolean Rentabilites(Feve f, Double prix){
		double rentabilite = prix*Prevision_Production(Filiere.LA_FILIERE.getEtape()).get(f)/CoutProd().get(f);
		if (rentabilite>=1.1) {
			return true;
		}
		return false;	
	}
	public double prix_rentable(Feve f) {
		return 1.1*CoutProd().get(f)/Prevision_Production(Filiere.LA_FILIERE.getEtape()).get(f);
	}
	public void ajustement() {
		for (Feve f : this.salaires.keySet()) {
			int nb_a_planter = 0;
			for (int i : this.age_hectares.get(f).keySet()){
				if (Filiere.LA_FILIERE.getEtape()-i>= 37*24) {
					nb_a_planter =+ 1;
				}
			}
			Planter(f, nb_a_planter);	
		}
	}
	
	
	// Classe calculant la production et les coûts de production, et gère les stocks de notre acteur

	
	
	// associer un employé par hectare

	
	

}
