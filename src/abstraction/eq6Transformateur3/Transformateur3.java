package abstraction.eq6Transformateur3;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class Transformateur3 extends Transformateur3AchatB implements IFabricantChocolatDeMarque{

	/**ecrit par Nathan Claeys
	 */

		public Transformateur3 () {
			super();
		}
/** ecrit par Nathan Claeys
 * @return
 */
	@Override
	public List<ChocolatDeMarque> getChocolatsProduits() {
		// TODO Auto-generated method stub
		return super.chocosProduits;
	}
	public List<String >getMarquesChocolat() {
		return super.getMarquesChocolat();} 
	public void next() {
		super.next();
	}
}
