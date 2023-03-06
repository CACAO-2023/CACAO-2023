package abstraction.eqXRomu.general;
import java.awt.*;
import java.util.ArrayList;

/**
 * Classe modelisant une courbe sous la forme d'un ensemble de points
 * 
 * Vous n'aurez pas, a priori, a utiliser directement cette classe
 * 
 * @author Romuald Debruyne
 */
public class Courbe {
    public static final double TRANSPARENCE_PAR_DEFAUT = 0.2;
    public static final int AUCUNE = -1;

    // VARIABLES D'INSTANCES
    /**
     * l'abscisse, l'ordonnee, et la marge d'erreur sur l'ordonnee pour chacun des points de la courbe
     */
    private ArrayList<Double> abscisses, ordonnees, margesErreurs; 
    /**
     * La couleur de trace de la courbe
     */
    private Color couleur; 
    /**
     * Le taux de transparence avec lequel sera affiche la marge d'erreur a l'ecran
     */
    private double transparence;
    /**
     * La marque symbolisant la courbe dans le graphique
     */
    private int marque;
    /**
     * Le titre de la courbe qui sera utilise dans la legende
     */
    private String titre;

    /**
     * Constructeur initialisant la courbe avec le nom s, aucun point, aucune couleur de specifiee, aucune marque et une transparence fixee a TRANSPARENCE_PAR_DEFAUT
     * @param s String, le nom de la courbe sur le graphique 
     */
    public Courbe(String s) {
	this(s,AUCUNE);
    }
    /**
     * Constructeur initialisant la courbe avec le nom s, aucun point, aucune couleur de specifiee, la marque d'index m et une transparence fixee a TRANSPARENCE_PAR_DEFAUT
     * @param s String, le nom de la courbe sur le graphique
     * @param m int, l'index de la marque de la courbe ( m doit valoir AUCUNE ou appartenir a [0, Graphique.NB_MARQUES[ ). 
     */
    public Courbe(String s, int m) {
	this.abscisses = new ArrayList<Double>();
	this.ordonnees = new ArrayList<Double>();
	this.margesErreurs = new ArrayList<Double>();
	this.couleur=null;
	this.transparence = TRANSPARENCE_PAR_DEFAUT;
	this.titre=s;
	this.marque=m;
    }
    /**
     * Accesseur permettant d'obtenir l'index de la marque de la courbe.
     * @return int, l'index de la marque de la courbe (la valeur retournee est soit AUCUNE, soit un entier de [0, Graphique.NB-MARQUES[ )
     */
    public int getMarque() {
	return this.marque;
    }
    /**
     * Accesseur permettant de modifier la marque de la courbe.
     * @param m int, l'index de la marque de la courbe ( m doit valoir AUCUNE ou appartenir a [0, Graphique.NB_MARQUES[ ). 
     */
    public void setMarque(int m) {
	this.marque=m;
    }
    /**
     * Accesseur permettant d'obtenir le titre de la courbe.
     * @return String, le titre de la courbe
     */
    public String getTitre() {
	return this.titre;
    }
    /**
     * Accesseur permettant de modifier le titre de la courbe.
     */
    public void setTitre(String titre) {
	this.titre = titre;
    }
    /**
     * Accesseur permettant d'obtenir le nombre de points que comporte la courbe
     * @return int, le nombre de points que comporte la courbe
     */
    public int getNbPoints() {
	return this.abscisses.size();// les trois arrayList ont la meme taille.
    }
    /**
     * Accesseur permettant d'obtenir l'abscisse du point d'index index de la courbe.
     * @return double, 0.0 si index<0 ou index>=this.getNbPoints(), et retourne l'abscisse du point d'index index sinon.
     */
    public double getX(int index) {
	if (index>=0 && index<this.getNbPoints()) {
	    return this.abscisses.get(index);
	}
	else {
	    return 0.0;
	}
    }
    /**
     * Accesseur permettant d'obtenir l'ordonnee du point d'index index de la courbe.
     * @return double, 0.0 si index<0 ou index>=this.getNbPoints(), et retourne l'ordonnee du point d'index index sinon.
     */
    public double getY(int index) {
	if (index>=0 && index<this.getNbPoints()) {
	    return this.ordonnees.get(index);
	}
	else {
	    return 0.0;
	}
    }
    /**
     * Accesseur permettant d'obtenir la marge d'erreur sur l'ordonnee du point d'index index de la courbe.
     * @return double, 0.0 si index<0 ou index>=this.getNbPoints(), et retourne la marge d'erreur sur l'ordonnee du point d'index index sinon.
     */
    public double getErr(int index) {
	if (index>=0 && index<this.getNbPoints()) {
	    return this.margesErreurs.get(index);
	}
	else {
	    return 0.0;
	}
    }
    /**
     * Accesseur permettant d'obtenir la couleur de la courbe
     * @return Color, la couleur de la courbe (null si la courbe n'a pas de couleur definie)
     */
    public Color getCouleur() {
	return this.couleur;
    }
    /**
     * Accesseur permettant d'obtenir le taux de transparence pour le tracer de la marge d'erreur de la courbe.
     * @return double, le taux de transparence pour le tracer de la marge d'erreur de la courbe.
     */

    public double getTransparence() {
	return this.transparence;
    } 
    /**
     * Accesseur permettant de modifier la couleur de la courbe.
     * @param c Color, la couleur desiree pour la courbe 
     */

    public void setCouleur(Color c) {
	this.couleur=c;
    }
    /**
     * Accesseur permettant de modifier le taux de transparence de la zone d'erreur 
     * @param tr double, le taux ( dans [0, 1[) de transparence desire pour l'affichage de la marge d'erreur. 
     */
    public void setTransparence( double tr) {
	if (tr>=0.0 && tr<1.0) {
	    this.transparence=tr;
	}
    }
    /**
     * Permet d'ajouter un point a la courbe. Attention toutefois au fait qu'il ne peut pas y avoir deux ordonnees differentes pour une meme abscisse 
     * Si la courbe possede deja un point ayant l'abscisse precisee en parametre, ce point est remplace par le point a ajouter.
     * @param x dounle, l'abscisse du point a ajouter
     * @param y double, l'ordonnee du point a ajouter
     * @param err double, la marge d'erreur sur l'ordonnee du point a ajouter. 
     */

    public void ajouter( double x, double y, double err) {
	// Sortie : c'est ajoute de sorte que les abscisses soient croissantes et une seule ordonnee par abscisse.
	int pos=0; 
	while (pos<this.getNbPoints() && this.abscisses.get(pos).doubleValue()<x) {
	    pos++;
	}
	if (pos<this.getNbPoints() && this.abscisses.get(pos).doubleValue()==x) { // Modif
	    this.ordonnees.remove(pos);
	    this.ordonnees.add(pos,Double.valueOf(y));
	    this.margesErreurs.remove(pos);
	    this.margesErreurs.add(pos,Double.valueOf(err));
	}
	else { // ajout
	    this.abscisses.add(pos,Double.valueOf(x));
	    this.ordonnees.add(pos,Double.valueOf(y));
	    this.margesErreurs.add(pos,Double.valueOf(err));
	}
    }

    /**
     * Permet d'ajouter un point a la courbe avec une marge d'erreur null sur l'ordonnee. 
     * Si la courbe possede deja un point ayant l'abscisse precisee en parametre, ce point est remplace par le point a ajouter.
     * @param x dounle, l'abscisse du point a ajouter
     * @param y double, l'ordonnee du point a ajouter
     */
    public void ajouter(double x, double y) {
	// Par defaut, il n'y a pas de marge d'erreur.
	this.ajouter(x, y, 0.0);
    }


    /**
     * Permet d'obtenir une chaine de caracteres correspondant a la valeur actuelle de la courbe 
     * Si la courbe possede deja un point ayant l'abscisse precisee en parametre, ce point est remplace par le point a ajouter.
     * @return String, une chaine de caracteres correspondant a la valeur de la courbe (son titre et l'ensemble des coordonnees de ses points)
     */
    public String toString(){
	String ar=this.getTitre();
	for (int i=0; i<this.getNbPoints(); i++) {
	    ar+="["+this.abscisses.get(i).doubleValue()+","+this.ordonnees.get(i).doubleValue()+","+this.margesErreurs.get(i).doubleValue()+"]";
	}
	return ar;
    }


    /**
     * Calcule la distance entre cette courbe et une autre courbe c.
     *
     * @Precondition les abscisses de c doivent coincider avec celles de cette courbe.
     * En particulier, la condition c.getNbPoints()==getNbPoints() doit �tre vraie.
     *
     */
    public double distance(Courbe c) {
	double dist=0;
	for (int i=1; i<getNbPoints(); i++) {
	    if (c.getX(i)!=getX(i)) {
		throw new Error("Impossible de calculer la distance entre ces deux courbes");
	    }
	    dist+=Math.abs((c.getY(i)-getY(i))*(c.getX(i)-c.getX(i-1)));
	}
	return dist;       	
    }


    public static void main(String[] args) {
	Courbe c = new Courbe("test");
	c.ajouter(1.0, 2.0, 0.0035);
	c.ajouter(2.0, 2.5, 0.00035);
	c.ajouter(2.5, 3.0, 0.000035);
	c.ajouter(1.5, 4.0, 0.000035);
	c.ajouter(0.5, 3.0, 0.035);
	c.ajouter(2.0, 10, 0.0);
	System.out.println(c);
    }
}
