package abstraction.eq4Transformateur1;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class Transformateur1 extends Transformateur1Acteur implements IFabricantChocolatDeMarque{
	
	private List<ChocolatDeMarque>chocosProduits;

	public Transformateur1() {
		super();
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
	}

	public List<ChocolatDeMarque> getChocolatsProduits() {
		if (this.chocosProduits.size()==0) {
		}
		return this.chocosProduits;
	}
}
