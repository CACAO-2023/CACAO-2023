package abstraction.eqXRomu.general;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;


/**
 * Classe modelisant un indicateur. 
 * Un indicateur est une variable numerique dont la valeur 
 * pourra etre historisee et visualisee.
 * 
 * Vous aurez a creer des instances de cette classe (potentiellement 
 * partagees entre plusieurs acteurs) et a faire evoluer leurs valeurs.
 *
 * @author Romuald Debruyne
 */
public class Variable implements Comparable<Variable>{
	private String nom;           // nom designant l'indicateur
	private String infobulle;     // description courte aparaissant lorsque la souris figure sur le nom de la variable 
	private IActeur createur;     // acteur a l'origine de la creation de l'indicateur
	private Courbe courbe;        // ensemble des couples (etape, valeur). Exploite par la classe Graphique
	// min et max sont des seuils d'alerte, mais ne sont pas bloquants : la valeur peut depasser ces seuils
	private double min;           // si la valeur est inferieure a min alors elle sera en rouge dans l'interface
	private double max;           // si la valeur est superieure a max alors elle sera en rose dans l'interface
	private Historique historique;// memorise l'historique des changements de valeur depuis la creation (premier element)
	// a la valeur actuelle (dernier element), en precisant pour chaque changement
	// le nom de l'acteur a l'origine de la modification, l'etape a laquelle le 
	// changement intervient et bien sur la nouvelle valeur.
	private PropertyChangeSupport pcs; // Pour notifier les observers (les graphiques notamment) des changements 

	/**
	 * 
	 * @param nom identifiant designant l'indicateur
	 * @param createur l'acteur qui est a l'origine de la creation de l'indicateur
	 * @param valInit la valeur initiale de l'indicateur
	 */
	public Variable(String nom, String infobulle, IActeur createur,  double min, double max, double valInit) {
		this.nom = nom;
		this.infobulle = infobulle;
		this.createur = createur;
		this.min = min;
		this.max = max;
		this.historique = new Historique();
		this.historique.ajouter(createur, Filiere.LA_FILIERE==null ? 0 : Filiere.LA_FILIERE.getEtape(), valInit);
		this.courbe = new Courbe(this.nom);
		this.courbe.ajouter(Filiere.LA_FILIERE==null ? 0 : Filiere.LA_FILIERE.getEtape(), valInit);
		this.pcs = new PropertyChangeSupport(this);
	}
	public Variable(String nom, IActeur createur,  double min, double max, double valInit) {
		this(nom, null, createur, min, max, valInit);
	}
	public Variable(String nom, String infobulle, IActeur createur, double valInit) {
		this(nom, infobulle, createur, -Double.MAX_VALUE, Double.MAX_VALUE, valInit);
	}
	public Variable(String nom, IActeur createur, double valInit) {
		this(nom, null, createur, -Double.MAX_VALUE, Double.MAX_VALUE, valInit);
	}
	/**
	 * Par defaut, la valeur initiale est fixee a 0.0
	 */
	public Variable(String nom, String infobulle, IActeur createur) {
		this(nom, infobulle, createur, 0.0);
	}

	/**
	 * Par defaut, il n'y a pas d'infobulle
	 */
	public Variable(String nom, IActeur createur) {
		this(nom, null, createur, 0.0);
	}
	/**
	 * @return Retourne le nom de l'indicateur
	 */
	public String getNom() {
		return this.nom;
	}
	/**
	 * @return Retourne l'infobulle de l'indicateur
	 */
	public String getInfobulle() {
		return this.infobulle;
	}
	/**
	 * @return Retourne l'acteur a l'origine de la creation de l'indicateur
	 */
	public IActeur getCreateur() {
		return this.createur;
	}
	/**
	 * @return Retourne l'historique des changements de valeur
	 */
	public Historique getHistorique() {
		return this.historique;
	}
	/**
	 * Version ciblee pour les variables a acces limite.
	 * Dans le cas des Variable l'acces est ouvert a la fois
	 * en lecture et en ecriture et on ne verifie donc
	 * pas le cryptogramme
	 */
	public Historique getHistorique(Integer crypto) {
		return this.getHistorique();
	}
	
	
	/**
	 * @return Retourne la Courbe (utilise principalement 
	 * pour l'affichage du graphique correspondant)
	 */
	public Courbe getCourbe() {
		return this.courbe;
	}
	/**
	 * Version ciblee pour les variables a acces limite.
	 * Dans le cas des Variable l'acces est ouvert a la fois
	 * en lecture et en ecriture et on ne verifie donc
	 * pas le cryptogramme
	 */
	public Courbe getCourbe(Integer crypto) {
		return this.getCourbe();
	}
	
	/**
	 * @return Retourne la valeur actuelle de la variable
	 * (donc la derniere valeur indiquee dans l'historique)
	 */
	public double getValeur() {
		return this.historique.getValeur();
	}
	/**
	 * Version ciblee pour les variables a acces limite.
	 * Dans le cas des Variable l'acces est ouvert a la fois
	 * en lecture et en ecriture et on ne verifie donc
	 * pas le cryptogramme
	 */
	public double getValeur(Integer crypto) {
		return this.getValeur();
	}
	
	/**
	 * @param step
	 * @return Retourne la valeur de la variable au step
	 * indique en parametre
	 */
	public double getValeur(int step) {
		return this.historique.getValeur(step);
	}
	/**
	 * Version ciblee pour les variables a acces limite.
	 * Dans le cas des Variable l'acces est ouvert a la fois
	 * en lecture et en ecriture et on ne verifie donc
	 * pas le cryptogramme
	 */
	public double getValeur(int step, Integer crypto) {
		return this.getValeur(step);
	}
	
	
	/**
	 * Affecte la valeur valeur a la variable en precisant
	 * que c'est auteur qui est a l'origine de ce changement.
	 * @param auteur Acteur a l'origine de la modification de valeur
	 * @param valeur La nouvelle valeur
	 */
	public void setValeur(IActeur auteur, double valeur) {
		double old = getValeur();
		int etape = Filiere.LA_FILIERE==null ? 0 : Filiere.LA_FILIERE.getEtape();
		this.historique.ajouter(auteur, etape, valeur);
		this.courbe.ajouter(etape, this.getValeur());
		pcs.firePropertyChange("Value",old,valeur);
	}
	
	/**
	 * Version ciblee pour les variables a acces limite.
	 * Dans le cas des Variable l'acces est ouvert a la fois
	 * en lecture et en ecriture et on ne verifie donc
	 * pas le cryptogramme
	 */
	public void setValeur(IActeur auteur, double valeur, Integer crypto) {
		this.setValeur(auteur, valeur);
	}
	
	public double getMin() {
		return this.min;
	}
	/**
	 * Version ciblee pour les variables a acces limite.
	 * Dans le cas des Variable l'acces est ouvert a la fois
	 * en lecture et en ecriture et on ne verifie donc
	 * pas le cryptogramme
	 */
	public double getMin(Integer crypto) {
		return this.getMin();
	}	
	
	public void setMin(double min) {
		double oldMin=this.min;
		this.min = min;
		pcs.firePropertyChange("min",oldMin,min);
	}
	/**
	 * Version ciblee pour les variables a acces limite.
	 * Dans le cas des Variable l'acces est ouvert a la fois
	 * en lecture et en ecriture et on ne verifie donc
	 * pas le cryptogramme
	 */
	public void setMin(double min, Integer crypto) {
		this.setMin(min);
	}
	
	public double getMax() {
		return this.max;
	}
	/**
	 * Version ciblee pour les variables a acces limite.
	 * Dans le cas des Variable l'acces est ouvert a la fois
	 * en lecture et en ecriture et on ne verifie donc
	 * pas le cryptogramme
	 */
	public double getMax(Integer crypto) {
		return this.getMax();
	}
	
	public void setMax(double max) {
		double oldMax=this.max;
		this.max = max;
		pcs.firePropertyChange("max",oldMax,max);
	}
	/**
	 * Version ciblee pour les variables a acces limite.
	 * Dans le cas des Variable l'acces est ouvert a la fois
	 * en lecture et en ecriture et on ne verifie donc
	 * pas le cryptogramme
	 */
	public void setMax(double max, Integer crypto) {
		this.setMax(max);
	}
	public boolean equals(Object o) {
		return (o instanceof Variable) && (this.getNom().equals(((Variable)o).getNom()));
	}
	/**
	 * Ajoute montant a la valeur de l'indicateur
	 * @param auteur
	 * @param delta>0
	 */
	public void ajouter(IActeur auteur, double delta) {
		this.setValeur(auteur, this.getValeur()+delta);
	}
	/**
	 * Version ciblee pour les variables a acces limite.
	 * Dans le cas des Variable l'acces est ouvert a la fois
	 * en lecture et en ecriture et on ne verifie donc
	 * pas le cryptogramme
	 */
	public void ajouter(IActeur auteur, double delta, Integer crypto) {
		this.setValeur(auteur, this.getValeur(crypto)+delta, crypto);
	}
	/**
	 * Retire montant a la valeur de l'indicateur
	 * @param auteur
	 * @param delta>0
	 */
	public void retirer(IActeur auteur, double delta) {
		this.setValeur(auteur, this.getValeur()-delta);
	}
	/**
	 * Version ciblee pour les variables a acces limite.
	 * Dans le cas des Variable l'acces est ouvert a la fois
	 * en lecture et en ecriture et on ne verifie donc
	 * pas le cryptogramme
	 */
	public void retirer(IActeur auteur, double delta, Integer crypto) {
		this.retirer(auteur, delta);
	}

	public void addObserver(PropertyChangeListener obs) {
		pcs.addPropertyChangeListener(obs);
	}
	public int compareTo(Variable o) {
		return this.getNom().compareTo(o.getNom());
	}
}
