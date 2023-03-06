package abstraction.eqXRomu.general;

import abstraction.eqXRomu.filiere.IActeur;

/**
 * Element dans un historique memorisant la valeur de l'indicateur a un
 * une etape precise. L'auteur de la modification est memorise.
 * 
 * Vous n'aurez pas, a priori, a utiliser directement cette classe
 * 
 * @author Romuald DEBRUYNE
 */
 public class Entree implements Comparable<Entree> {
 
	private IActeur auteur; // l'auteur de la modification
	private double valeur; // la nouvelle valeur
	private int etape;     // l'etape a laquelle le changement a eu lieu

	public Entree(IActeur auteur, int etape, double valeur)  {
		this.auteur = auteur;
		this.etape = etape;
		this.valeur = valeur;
	}

	public IActeur getAuteur() {
		return this.auteur;
	}

	public double getValeur() {
		return this.valeur;
	}

	public int getEtape() {
		return this.etape;
	}

	public String toString() {
		return this.getAuteur().getNom()+" a l'etape "+this.getEtape()+" --> "+this.getValeur();
	}

	public int compareTo(Entree o) {
		return this.getEtape()-o.getEtape();
	}
}