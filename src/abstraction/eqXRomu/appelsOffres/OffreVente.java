package abstraction.eqXRomu.appelsOffres;

import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;


public class OffreVente {
	public static final double AO_QUANTITE_MIN = 2.0; // pas moins de 2 tonnes 
	
	private IVendeurAO vendeur;
	private ChocolatDeMarque choco;
	private double quantiteT;
	private boolean enTG; // vrai si la quantite de chocolat devra obligatoirement etre vendue en tete de gondole.
	                      // false s'il n'est pas obligatoire de vendre ce chocolat en tete de gondole.
	
	/**
	 * Constructeur initialisant l'offre de vente avec les informations fournies en parametres 
	 * (leve une IllegalArgumentException si les conditions sur les parametres ne sont
	 * pas respectees). 
	 * @param feve!=null
	 * @param quantite>=AO_FEVES_QUANTITE_MIN
	 */
	public OffreVente(IVendeurAO vendeur, ChocolatDeMarque choco, double quantiteT, boolean enTG) {
		if (vendeur==null) {
			throw new IllegalArgumentException("Appel du constructeur de OffreVente avec null pour vendeur");
		}
		if (choco==null) {
			throw new IllegalArgumentException("Appel du constructeur de OffreVente avec null pour type de chocolat de marque");
		}
		if (quantiteT<AO_QUANTITE_MIN) {
			throw new IllegalArgumentException("Appel du constructeur de OffreVente avec une quantite de "+quantiteT+" (min="+AO_QUANTITE_MIN+")");
		}
		this.vendeur = vendeur;
		this.choco = choco;
		this.quantiteT = quantiteT;
		this.enTG = enTG;
	}
	
	public IVendeurAO getVendeur() {
		return this.vendeur;
	}
	
	public ChocolatDeMarque getChocolat() {
		return this.choco;
	}
	
	public double getQuantiteT() {
		return this.quantiteT;
	}
	
	public boolean enTG() {
		return this.enTG;
	}
	
	public boolean equals(Object o) {
		return (o instanceof OffreVente) 
				&& this.getVendeur().equals(((OffreVente)o).getVendeur())
				&& this.getChocolat().equals(((OffreVente)o).getChocolat())
				&& this.getQuantiteT()==((OffreVente)o).getQuantiteT()
				&& this.enTG()==((OffreVente)o).enTG();
	}
	
	public String toString() {
		return "("+Journal.doubleSur(this.getQuantiteT(), 3)+" de "+this.getChocolat()+")";
	}
}
