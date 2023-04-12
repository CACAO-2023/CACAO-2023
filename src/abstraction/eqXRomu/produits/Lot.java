package abstraction.eqXRomu.produits;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.general.Journal;
/**
 * Modelise une quantite d'un meme produit qui n'a pas forcement ete
 * produite integralement a la meme etape. 
 *  
 * @author rdebruyn
 *
 */
public class Lot {
	protected IProduit produit;
	protected HashMap<Integer, Double> quantites; // table des associations <stepRecolte, quantiteEnT>
	// chaque association <S,Q> correspondant a une quantite de Q Tonnes de feves recoltees au step S. 

	/**
	 * Constructeur initialisant this comme etant un Lot de 0 tonnes de produit
	 * @param produit, produit!=null, le produit dont est constitue le lot
	 */
	public Lot(IProduit produit) {
		if (produit==null) {
			throw new IllegalArgumentException("Appel du constructeur de Lot avec null pour produit");
		}
		this.produit = produit;
		this.quantites = new HashMap<Integer, Double> ();
	}

	public IProduit getProduit() {
		return this.produit;
	}

	/**
	 * @return Retourne une Map dont le keySet() correspond a l'ensemble des
	 * etapes auxquelles une partie du lot a ete produite.
	 * Pour chaque etape eta de getQuantites().keySet(), 
	 * getQuantites().get(eta) indique la quantite du lot produite a l'etape eta.
	 */
	public HashMap<Integer, Double> getQuantites() {
		return this.quantites;
	}

	/**
	 * Ajoute au lot une quantite de produit produite a l'etape step
	 * @param step, step>=0
	 * @param quantite, quantite>0
	 */
	public void ajouter(int step, double quantite) {
		if (quantite<=0.0) {
			throw new IllegalArgumentException("Appel de ajouter de Lot avec une quantite de "+quantite);
		}
		if (step<0) {
			throw new IllegalArgumentException("Appel de ajouter de Lot avec un step de "+step);
		}
		double total=quantite;
		if (this.quantites.keySet().contains(step)) {
			total = total + this.quantites.get(step);
		}
		this.quantites.put(step, total);
	}

	/**
	 * Ajoute lot2 a this 
	 * @param lot2, lot2!=null et lot2 est du meme produit que this
	 */
	public void ajouter(Lot lot2) {
		if (lot2==null) {
			throw new IllegalArgumentException("Appel de add(Lot lot2) de Lot avec lot2==null");
		} else if (!lot2.getProduit().equals(this.produit)) {
			throw new IllegalArgumentException("Appel de add(Lot lot2) de Lot avec des produits qui ne sont pas identiques");
		} else {
			for (Integer i : lot2.quantites.keySet()) {
				this.ajouter(i, lot2.quantites.get(i));
			}
		}
	}
	
	/**
	 * @return Retourne le nombre de tonnes de produit 
	 * composant le lot
	 */
	public double getQuantiteTotale() {
		double q=0;
		for (Integer i : this.quantites.keySet()) {
			q = q + this.quantites.get(i);
		}
		return q;
	}

	/**
	 * @return Retourne une partie du lot correspondant a quantite tonnes
	 * et supprime de this cette partie.
	 * @param quantite, quantite>0 et quantite<=this.getQuantiteTotale()
	 */
	public Lot retirer(double quantite) {
		if (quantite<=0 || quantite>this.getQuantiteTotale()+0.001) {
			throw new IllegalArgumentException("Appel de retirer("+quantite+") sur un Lot de feves dont la quantite totale est "+this.getQuantiteTotale());
		} else {
			Lot res=new Lot(produit);
			List<Integer> vides = new LinkedList<Integer>();
			double reste = quantite;
			for (Integer i : this.quantites.keySet()) {
				if (reste>0) {
					if (this.quantites.get(i)>reste) {
						res.ajouter(i,reste);
						this.quantites.put(i,this.quantites.get(i)-reste);
						reste=0;
					} else {
						res.ajouter(i,this.quantites.get(i));
						reste = reste - this.quantites.get(i);
						vides.add(i);
					}
				}
			}
			for (Integer step : vides) {
				this.quantites.remove(step);
			}
			return res;
		}
	}

	public String toString() {
		String res="["+this.produit+" "+Journal.doubleSur(this.getQuantiteTotale(), 2)+"T:";
		for (Integer i : this.quantites.keySet()) {
			res = res + "<"+i+"->"+this.quantites.get(i)+">";
		}
		return res+"]";
	}
	
	public static void main(String[] args) {
		Lot lot = new Lot(Feve.F_MQ);
		lot.ajouter(2, 51.0);
		lot.ajouter(4, 12.0);
		System.out.println(lot);
		Lot lot2 = lot.retirer(60);
		System.out.println(lot2);
		System.out.println(lot);
		lot.ajouter(lot2);
		System.out.println(lot);
	}
}
