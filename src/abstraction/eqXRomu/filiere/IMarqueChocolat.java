package abstraction.eqXRomu.filiere;

import java.util.List;

/**
 * Interface que doit implementer tout acteur 
 * detenteur d'une ou plusieurs marques de chocolat.
 * 
 * @author Romuald DEBRUYNE
 *
 */
public interface IMarqueChocolat {

/**
 * Methode appelee en debut de simulation afin de connaitre les marques
 * de chocolat dont l'acteur est le detenteur
 * Remarques : 
 * - un distributeur a au plus une marque.
 * - un acteur peut produire des chocolats de n'importe quelle
 *   marque mais il ne pourra vendre un chocolat de la marque m
 *   que si m est une de ses marque ou une marque de l'acheteur
 * - une marque n'est detenue que par un acteur (deux acteurs ne 
 *   peuvent pas detenir la meme marque)
 * @return
 */
	public List<String> getMarquesChocolat() ;
}
