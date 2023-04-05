package abstraction.eq2Producteur2;

import java.util.LinkedList;

import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;

public class Producteur2ASProducteurPlanteur extends Producteur2AStockeur{
	public Feve feve;
	public Journal journal;
	public LinkedList<Integer> employes;
	public LinkedList<Double> salaires;
	public int surface_plantation;
	public LinkedList<Double> prix;
	
	
	public Producteur2ASProducteurPlanteur( Feve feve, Journal journal, LinkedList<Integer> employes, LinkedList<Double> salaires, int surface_plantation, LinkedList<Double> prix) {
		this.feve = feve;
		this.journal = journal;
		this.employes = employes;
		this.salaires = salaires;
		this.surface_plantation = surface_plantation;
	}
	
	public Producteur2ASProducteurPlanteur() {
		
	}
	
	public Feve getFeve() {
		return this.feve;
	}
	public LinkedList<Integer> getEmployes(){
		return this.employes;
	}
	public LinkedList<Double> getSalaires(){
		return this.salaires;
	}
	public int getSurface(){
		return this.surface_plantation;
	}

	public Journal getJournal() {
		return this.journal;
	}
	public LinkedList<Double> getPrix(){
		return this.getPrix();
	}
	
	public void setEmploye(int employes_BQ, int employes_MQ, int employes_MQ_BE, int employes_HQ_BE) {
		this.employes = new LinkedList <Integer>();
		employes.add(employes_BQ);
		employes.add(employes_MQ);
		employes.add(employes_MQ_BE);
		employes.add(employes_HQ_BE);
	}
	public void setSalaires(double salaire_employes_BQ, double salaire_employes_MQ, double salaire_employes_MQ_BE, double salaire_employes_HQ_BE) {
		this.salaires = new LinkedList<Double>();
		salaires.add(salaire_employes_BQ);
		salaires.add(salaire_employes_MQ);
		salaires.add(salaire_employes_MQ_BE);
		salaires.add(salaire_employes_HQ_BE);
	}
	public void setPrix(double prix_BQ, double prix_MQ, double prix_MQ_BE, double prix_HQ_BE) {
		this.salaires = new LinkedList<Double>();
		salaires.add(prix_BQ);
		salaires.add(prix_MQ);
		salaires.add(prix_MQ_BE);
		salaires.add(prix_HQ_BE);
	}
	public void setSurface(int surface_plantation) {
		this.surface_plantation = surface_plantation;
		
	}
	// Méthodes à utiliser dans chaque next //
	//public void adapter_salaires() {
		//for (int i; i<4; i++) {
		//	if ()
		//}
		
		
	//}
	public LinkedList<Double> Couts_Prod() {
		LinkedList<Double> coûts_totaux = new LinkedList<Double>();
		for (int i=0; i<4; i++) {
			coûts_totaux.add(this.salaires.get(i)*this.employes.get(i)); }
		return coûts_totaux;
	}
	public LinkedList<Double> Prevision_Vente(){
		LinkedList<Double> prevision_vente = new LinkedList<Double>();
		for (int i=0; i<4; i++) {
			prevision_vente.add(null); }//a voir comment determiner les ventes en fonction des contrats cadre et evolution des ventes precedentes
		return prevision_vente;	
	}
	public LinkedList<Double> Rentabilites(){
		LinkedList<Double> rentabilites = new LinkedList<Double>();
		for (int i=0; i<4; i++) {
			rentabilites.add(this.prix.get(i)*this.Prevision_Vente().get(i)/this.Couts_Prod().get(i));}
		return rentabilites;
		
	}
	
	
	// Classe calculant la production et les coûts de production, et gère les stocks de notre acteur

	
	
	// associer un employé par hectare

	
	

}
