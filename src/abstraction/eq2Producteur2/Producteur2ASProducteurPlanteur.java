package abstraction.eq2Producteur2;

import java.util.LinkedList;

import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;

public class Producteur2ASProducteurPlanteur extends Producteur2AStockeur{
	public Variable stockFeve;
	public Feve feve;
	public Journal journal;
	public LinkedList<Integer> employes;
	public LinkedList<Double> salaires;
	public int surface_plantation;
	
	public Producteur2ASProducteurPlanteur(Variable stockFeve, Feve feve, Journal journal, LinkedList<Integer> employes, LinkedList<Double> salaires, int surface_plantation) {
		super();
		this.stockFeve = stockFeve;
		this.feve = feve;
		this.journal = journal;
		this.employes = employes;
		this.salaires = salaires;
		this.surface_plantation = surface_plantation;
	}
	
	public Producteur2ASProducteurPlanteur() {
		super();
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
	public Variable getStock() {
		return this.stockFeve;
	}
	public Journal getJournal() {
		return this.journal;
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
	}
	
	// Classe calculant la production et les coûts de production, et gère les stocks de notre acteur
	private Variable mainOeuvreNonEq;
	private Variable mainOeuvreEq;
	
	public Variable getMainOeuvreNonEq() {
		return mainOeuvreNonEq;
	}
	public void setMainOeuvreNonEq(Variable mainOeuvreNonEq) {
		this.mainOeuvreNonEq = mainOeuvreNonEq;
	}
	public Variable getMainOeuvreEq() {
		return mainOeuvreEq;
	}
	public void setMainOeuvreEq(Variable mainOeuvreEq) {
		this.mainOeuvreEq = mainOeuvreEq;
	}
	
	
	// associer un employé par hectare

	
	

}
