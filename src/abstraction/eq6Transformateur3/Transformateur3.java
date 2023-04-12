package abstraction.eq6Transformateur3;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class Transformateur3 extends Transformateur3Acteur implements IFabricantChocolatDeMarque{

	/**ecrit par Nathan Claeys
	 */
	protected List<ChocolatDeMarque>chocosProduits;
		public Transformateur3 () {
			super();
			this.chocosProduits = new LinkedList<ChocolatDeMarque>();
		}
/** ecrit par Nathan Claeys
 * @return
 */
	@Override
	public List<ChocolatDeMarque> getChocolatsProduits() {
		// TODO Auto-generated method stub
		return this.chocosProduits;
	}
}
