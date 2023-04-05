package abstraction.eq2Producteur2;

import abstraction.eqXRomu.general.Journal;
import java.util.LinkedList;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;

public class Producteur2AProducteurPlanteur extends Producteur2Acteur{
	public Variable stockFeve;
	public Feve feve;
	public Journal journal;
	public LinkedList<Integer> employes;
	public LinkedList<Double> salaires;
	public int surface_plantation;
	
	public Producteur2AProducteurPlanteur(Variable stockFeve, Feve feve, Journal journal, LinkedList<Integer> employes, LinkedList<Double> salaires, int surface_plantation) {
		this.stockFeve = stockFeve;
		this.feve = feve;
		this.journal = journal;
		this.employes = employes;
		this.salaires = salaires;
		this.surface_plantation = surface_plantation;
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
}
