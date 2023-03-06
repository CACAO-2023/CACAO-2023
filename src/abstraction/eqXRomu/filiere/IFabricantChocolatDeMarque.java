package abstraction.eqXRomu.filiere;

import java.util.List;

import abstraction.eqXRomu.produits.ChocolatDeMarque;

/**
 * Interface implementee par tous les acteurs produisant des chocolats de marque
 * @author Romuald DEBRUYNE
 *
 */
public interface IFabricantChocolatDeMarque extends IActeur {
	/**
	 * @return Retourne la liste de toutes les sortes de ChocolatDeMarque que l'acteur produit 
	 * et peut vendre.
	 */
	public List<ChocolatDeMarque> getChocolatsProduits();

}
