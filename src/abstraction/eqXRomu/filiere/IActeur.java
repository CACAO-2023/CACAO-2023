package abstraction.eqXRomu.filiere;

import java.awt.Color;
import java.util.List;

import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;

/**
 * Tout acteur doit implementer cette interface
 * 
 * Vous aurez donc a creer une/des implementation(s) de cette classe
 * 
 * @author Romuald Debruyne
 *
 */
public interface IActeur {

	/**
	 * Methode de l'acteur invoquee apres la creation des acteurs et avant le premier next.
	 * Cette methode peut notamment (a vous de decider de ses specifications) permettre de 
	 * determiner les acteurs commerciaux de la filiere Filiere.LA_FILIERE
	 * avec lesquels il faudra interagir. 
	 */
	public void initialiser();

	/**
	 * @return Le nom de l'acteur. Deux acteurs ne peuvent pas avoir le meme nom.
	 */
	public String getNom();

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////
	/**
	 * @return Retourne la couleur de l'acteur dans l'interface
	 */
	public Color getColor();

	/**
	 * @return Retourne une courte description de l'acteur. Cette description est visible lorsque le curseur
	 * demeure sur le nom de l'acteur dans l'interface
	 */
	public String getDescription();

	/**
	 * Methode de l'acteur invoquee suite a l'appui sur le bouton NEXT de la fenetre principale
	 */
	public void next();

	/**
	 * @return Retourne une liste composee des indicateurs de l'acteur
	 */
	public List<Variable> getIndicateurs();

	/**
	 * @return Retourne une liste composee des parametres de l'acteur
	 */
	public List<Variable> getParametres();

	/**
	 * @return Retourne une liste composee des journaux de l'acteur
	 */
	public List<Journal> getJournaux();

	
	////////////////////////////////////////////////////////
	//               En lien avec la Banque               //
	////////////////////////////////////////////////////////

	/**
	 * Methode appelee par la banque apres la creation du compte bancaire de l'acteur
	 * afin de lui communiquer le cryptogramme qui lui sera necessaire pour les operations
	 * bancaires
	 * @param crypto
	 */
	public void setCryptogramme(Integer crypto);

	/**
	 * Methode appelee par la Filiere afin d'indiquer que l'acteur fourni en parametre vient de faire faillite
	 * @param acteur
	 */
	public void notificationFaillite(IActeur acteur);

	/**
	 * Methode appelee par la Banque apres chaque operation bancaire sur un compte afin de prevenir son proprietaire.
	 * @param montant, le montant de l'operation, positif si il s'agit d'un depot, negatif s'il s'agit d'un retrait
	 */
	public void notificationOperationBancaire(double montant);

	
	////////////////////////////////////////////////////////
	//        Pour la creation de filieres de test        //
	////////////////////////////////////////////////////////
	/**
	 * En vue de tester certaines fonctionnalites et certains comportements, il peut etre
	 * interessant d'utiliser une filiere particuliere (impliquant certains des acteurs 
	 * avec un parametrage particulier). Chaque acteur est susceptible de proposer des 
	 * filieres.
	 * @return Retourne la liste des noms des filieres que l'acteur propose
	 */
	public List<String> getNomsFilieresProposees();

	/**
	 * @param nom, un nom de la liste nomsDesFilieresProposees()
	 * @return Retourne une instance de Filiere correspondant au nom nom.
	 */
	public Filiere getFiliere(String nom);

}
