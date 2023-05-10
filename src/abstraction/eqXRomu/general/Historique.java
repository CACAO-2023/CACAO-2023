package abstraction.eqXRomu.general;

import java.util.List;

import abstraction.eqXRomu.filiere.IActeur;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Classe modelisant un historique, vu comme un ensemble 
 * d'entrees (modification de la valeur d'un indicateur 
 * par un acteur a une etape donnee)
 * 
 * Vous n'aurez pas, a priori, a utiliser directement cette classe
 *
 * @author Romuald Debruyne
 */
public class Historique {
	public static final int MAX = 50; // on ne conserve que les MAX dernieres modifications.
	private HashMap<Integer, Entree> valeurAuStep; // Pour chaque step auquel une modification a eu lieue, conserve l'entree correspondant a la derniere modification (donc la derniere valeur qu'a eu la variable a ce step) 
	private LinkedList<Entree>dernieresModifs;
	private PropertyChangeSupport pcs;        // Pour notifier les observers (les graphiques notamment) des changements 

	public Historique() {
		this.valeurAuStep=new HashMap<Integer, Entree>();
		this.dernieresModifs=new LinkedList<Entree>();
		this.pcs = new  PropertyChangeSupport(this);
	}
	public void cloner(Historique h) {
		this.dernieresModifs=h.dernieresModifs;
		this.valeurAuStep=h.valeurAuStep;
	}
	public void ajouter(IActeur auteur, int etape, double valeur) {
		double old = this.getValeur();
		Entree e = new Entree( auteur,  etape,  valeur);
		this.valeurAuStep.put(etape, e);
		this.dernieresModifs.add(e);
		if (this.dernieresModifs.size()>MAX) {
			this.dernieresModifs.remove(0);
		}
		pcs.firePropertyChange("Value",old,valeur);
	}
	/**
	 * Retourne la valeur actuelle (donc la derniere)
	 * @return la valeur actuelle (donc la derniere)
	 */
	
	/**
	 *  Retourne la valeur qu'avait la variable au step i
	 * @param i i>=0 
	 * @return Retourne la valeur qu'avait la variable au step i
	 */
	public double getValeur(int i) {
		if (this.dernieresModifs.size()<1) {
			return 0.0;// par convention
		} else if (i>this.dernieresModifs.get(this.dernieresModifs.size()-1).getEtape()) {
				return this.dernieresModifs.get(this.dernieresModifs.size()-1).getValeur();
		} else if (i>=this.dernieresModifs.get(0).getEtape()) { // on cherche dans les dernieres modifs
			int pos=this.dernieresModifs.size()-1;
			while (pos>=0 && this.dernieresModifs.get(pos).getEtape()>i) {
				pos--;
			}
			return this.dernieresModifs.get(pos).getValeur();
		} else { // on cherche dans la map
			int pos=i;
			while (pos>=0 && !this.valeurAuStep.keySet().contains(pos)) {
				pos--;
			}
			return pos>=0 ? this.valeurAuStep.get(pos).getValeur() : 0.0 ;
		}
	}

	/**
	 * Retourne la valeur actuelle (donc la derniere)
	 * @return la valeur actuelle (donc la derniere)
	 */
	public double getValeur() {
		if (this.dernieresModifs.size()<1) {
			return 0.0;// par convention
		} else {
			return this.dernieresModifs.get(this.dernieresModifs.size()-1).getValeur();
		}
	}
	public String toString() {
		Collection<Entree> v = this.valeurAuStep.values();
		List<Entree> l = new LinkedList<Entree>();
		l.addAll(v);
		Collections.sort(l);
		String s="";
		if (dernieresModifs.size()>0) {
			for (int i=0; i<l.size() && l.get(i).getEtape()<dernieresModifs.get(0).getEtape(); i++) {
				s+=l.get(i).toString()+"<br>";
			}
			for (int i=0; i<this.dernieresModifs.size(); i++) {
				s+=this.dernieresModifs.get(i).toString()+"\n";
			}
		}
		return s;
	}
	public String toHtml() {
		Collection<Entree> v = this.valeurAuStep.values();
		List<Entree> l = new LinkedList<Entree>();
		l.addAll(v);
		Collections.sort(l);
		String s="<html>";
		if (dernieresModifs.size()>0) {
			for (int i=0; i<l.size() && l.get(i).getEtape()<dernieresModifs.get(0).getEtape(); i++) {
				s+=l.get(i).toString()+"<br>";
			}
			for (int i=0; i<this.dernieresModifs.size(); i++) {
				s+=this.dernieresModifs.get(i).toString()+"<br>";
			}
		}
		return s+"</html>";
	}
	public void addObserver(PropertyChangeListener obs) {
		pcs.addPropertyChangeListener(obs);
	}
}
