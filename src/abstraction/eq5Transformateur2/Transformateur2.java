/*/ Page redigee par Adam FERHOUT /*/

package abstraction.eq5Transformateur2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.awt.Color;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

/**
 * @author FERHOUT Adam
 */

public class Transformateur2 extends Transformateur2VendeurBourseCacao implements IFabricantChocolatDeMarque{
	
	private List<ChocolatDeMarque>chocosProduits; // Liste des chocolats de marque produits 
	protected HashMap<Feve, Double> stockFeves; // Feves disponible (en stock)
	protected HashMap<Chocolat, Double> stockChoco; // Chocolat disponible
	protected HashMap<ChocolatDeMarque, Double> stockChocoMarque; // Chocolat de marque disponible 
	protected HashMap<Feve, HashMap<Chocolat, Double>> pourcentageTransfo; // pour les differentes feves, le chocolat qu'elle peuvent contribuer a produire avec le pourcentage de chocolat associé

	public Transformateur2() { // constructeur 
		super();
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
	}
	

	public List<ChocolatDeMarque> getChocolatsProduits() { // nous produisons deux chocolats, chocopop et maison doutre
		if (this.chocosProduits.size()==0) {
				Chocolat c1 = Chocolat.C_MQ;
				Chocolat c2 = Chocolat.C_HQ_BE;
				this.chocosProduits.add(new ChocolatDeMarque(c1, "ChocoPop", 70, 0));
				this.chocosProduits.add(new ChocolatDeMarque(c2, "Maison Doutre", 90, 10));
		}
		return this.chocosProduits;
	}
	
}
