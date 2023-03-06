package abstraction.eqXRomu.contratsCadres;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.*;

public class Echeancier {
	private int stepDebut;
	private List<Double>  quantites;

	/**
	 * Initialise l'echeancier avec stepDebut pour date de premiere echeance 
	 * et une liste vide de quantites planifiees.
	 * @param stepDebut
	 */
	public Echeancier(int stepDebut) {
		this.stepDebut = stepDebut;
		this.quantites = new ArrayList<Double>();
	}

	/**
	 * Initialise l'echeancier avec pour premiere echeance la prochaine etape 
	 * et une liste vide de quantites planifiees.
	/**
	 * 
	 */
	public Echeancier() {
		this(Filiere.LA_FILIERE==null ? 1 : Filiere.LA_FILIERE.getEtape()+1);
	}

	/**
	 * Cree un echeancier regulier de nbStep echeances d'une quantite quantiteParStep
	 * qui commence au step stepdebut.
	 * @param stepDebut
	 * @param nbStep, nbStep>0
	 * @param quantiteParStep, quantiteParStep>=0.0
	 */
	public Echeancier(int stepDebut, int nbStep, double quantiteParStep) {
		this(stepDebut);
		if (nbStep<1) {
			throw new IllegalArgumentException("Le constructeur d'Echeancier est appele avec nbStep<=0");
		}
		if (quantiteParStep<0.0) {
			throw new IllegalArgumentException("Le constructeur d'Echeancier est appele avec quantiteParStep<0.0");
		}
		for (int i=0; i<nbStep; i++) {
			this.ajouter(quantiteParStep);
		}
	}

	/**
	 * Initialise l'echeance afin qu'il debute a stepdebut en ayant des echeances dont les
	 * quantites sont listees dans la liste quantites passee en parametre.
	 * @param stepDebut
	 * @param quantites
	 */
	public Echeancier(int stepDebut, List<Double> quantites) {
		this(stepDebut);
		for (Double d : quantites) {
			if (d<0.0) {
				throw new IllegalArgumentException("Le constructeur(Echeancier((stepDebut, quantites) est appele avec une liste comportant une/des valeurs negatives : "+quantites);
			}
		}
		this.quantites = new LinkedList<Double>(quantites);
	}

	/**
	 * Constructeur par recopie
	 * Initialise l'echeancier de telle sorte qu'il soit identique a 
	 * l'echeancier passe en parametre
	 * @param e
	 */
	public Echeancier(Echeancier e) {
		this(e.getStepDebut());
		for (int step=e.getStepDebut(); step<=e.getStepFin(); step++) {
			this.set(step, e.getQuantite(step));
		}
	}

	/**
	 * @return Retourne le nombre d'echeances
	 */
	public int getNbEcheances() {
		return this.quantites.size();
	}

	/**
	 * @return Retourne l'etape de la premiere echeance
	 */
	public int getStepDebut() {
		return this.stepDebut;
	}

	/**
	 * @return Retourne l'etape de la derniere echeance
	 */
	public int getStepFin() {
		return this.getStepDebut() + this.getNbEcheances() - 1;
	}

	/**
	 * @param step
	 * @return Retourne la quantite prevue a l'etape step (0.0 si aucune echeance
	 * n'est prevue a l'etape step)
	 */
	public double getQuantite(int step) {
		if (step<this.getStepDebut() || step>this.getStepFin()) {
			return 0.0;
		} else {
			return this.quantites.get(step-this.getStepDebut());
		}
	}

	/**
	 * @param step
	 * @return Retourne la somme des quantites prevues jusqu'a l'etape step incluse.
	 */
	public double getQuantiteJusquA(int step) {
		double res=0;
		for (int s= this.stepDebut; s<=step; s++) {
			res += this.getQuantite(s);
		}
		return res;
	}
	
	/**
	 * Retire toutes les echeances planifiees
	 */
	public void vider() {
		for (int step=this.getStepDebut() ; step<=this.getStepFin() ; step++) {
			this.set(step, 0.0);
		}
	}

	/**
	 * @return Retourne la quantite globale de l'echeancier, c'est-a-dire la somme des
	 * quantites prevues aux differentes echeances.
	 */
	public double getQuantiteTotale() {
		return getQuantiteAPartirDe(this.getStepDebut());
	}

	/**
	 * @param step
	 * @return Retourne la somme des quantites a partir du step step (les echeances avant step ne 
	 * sont pas consideree). Informellement, la quantite totale "qu'il reste" a partir de step.
	 */
	public double getQuantiteAPartirDe(int step) {
		double somme=0.0;
		for (int s = step; s<=this.getStepFin(); s++) {
			somme+=this.getQuantite(s);
		}
		return somme;
	}

	/**
	 * Si quantite<0.0, leve une IllegalArgumentException.
	 * Sinon, ajoute en fin d'echeancier une echeance supplementaire correspondant 
	 * a la quantite passee en parametre
	 * @param quantite, quantite>=0.0
	 */
	public void ajouter(double quantite) {
		if (quantite<0.0) {
			throw new IllegalArgumentException("La methode ajouter(quantite) d'Echeancier est appelee avec quantite<0.0");
		}
		this.quantites.add(quantite);
	}

	/**
	 * Si step<stepDebut ou quantite<0.0, leve une IllegalArgumentException.
	 * Sinon, fixe la quantite a quantite pour l'etape step (la methode peut etre amenee
	 * a ajouter des echeances de quantite 0.0 si l'echeancier s'achevait avant step).
	 * @param step, step>=stepDebut
	 * @param quantite, quantite>=0.0
	 */
	public void set(int step, double quantite) {
		if (quantite<0.0) {
			throw new IllegalArgumentException("La methode set(step,quantite) d'Echeancier est appelee avec quantite<0.0");
		}
		if (step<this.getStepDebut()) {
			throw new IllegalArgumentException("La methode set(step,quantite) d'Echeancier est appelee avec step="+step+" alors que l'echeancier debute au step "+this.getStepDebut());
		}
		if (step<=this.getStepFin()) {
			int index = step-this.getStepDebut();
			this.quantites.remove(index);
			this.quantites.add(index,quantite);
		} else {
			while (this.getStepFin()+1<step) {
				this.ajouter(0.0);
			}
			this.ajouter(quantite);
		}
	}

	/**
	 * @param e
	 * @return Retourne true si this "est euivalent" a e (c'est a dire si il debute et s'acheve 
	 * aux memes etapes que e et si la quantite globale est egale a celle de e (la quantite planifiee n'est
	 * pas forcement la meme a toutes les etapes).
	 */
	public boolean equivalent(Echeancier e) {
		return this.getStepDebut() == e.getStepDebut() 
				&& this.getStepFin()==e.getStepFin()
				&& this.getQuantiteTotale()==e.getQuantiteTotale();
	}

	public String toString() {
		String res="[";
		for (int step=this.getStepDebut(); step<this.getStepFin(); step++) {
			res=res+step+":"+String.format("%.3f",this.getQuantite(step))+", ";
		}
		if (this.getStepFin()>=this.getStepDebut()) {
			res=res+this.getStepFin()+":"+String.format("%.3f",this.getQuantite(this.getStepFin()));
		}
		return res+"]";
	}


	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((quantites == null) ? 0 : quantites.hashCode());
		result = prime * result + stepDebut;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (!(obj instanceof Echeancier)) {
			return false;
		} else {
			Echeancier other = (Echeancier) obj;
			if (quantites == null) {
				return (other.quantites == null);
			} else if (!quantites.equals(other.quantites)) {
				return false;
			} else if (stepDebut != other.stepDebut) {
				return false;
			} else {
				return true;
			}
		}
	}
}
