package abstraction.eq5Transformateur2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur2 extends Transformateur2Acteur implements IFabricantChocolatDeMarque{
	
	private List<ChocolatDeMarque>chocosProduits;
	protected HashMap<Feve, Double> stockFeves;
	protected HashMap<Chocolat, Double> stockChoco;
	protected HashMap<ChocolatDeMarque, Double> stockChocoMarque;
	protected HashMap<Feve, HashMap<Chocolat, Double>> pourcentageTransfo; // pour les differentes feves, le chocolat qu'elle peuvent contribuer a produire avec le ratio


	public Transformateur2() {
		super();
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
	}

	public List<ChocolatDeMarque> getChocolatsProduits() {
		if (this.chocosProduits.size()==0) {
				Chocolat c1 = Chocolat.C_MQ;
				Chocolat c2 = Chocolat.C_HQ_BE;
				this.chocosProduits.add(new ChocolatDeMarque(c1, "ChocoPop", 70, 0));
				this.chocosProduits.add(new ChocolatDeMarque(c2, "Maison Doutre", 90, 10));
		}
		return this.chocosProduits;
	}
}
