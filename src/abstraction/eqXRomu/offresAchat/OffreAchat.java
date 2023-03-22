package abstraction.eqXRomu.offresAchat;

import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;


public class OffreAchat {
	public static final double OA_QUANTITE_MIN = 2.0; // pas moins de 2 tonnes 
	
	private IAcheteurOA acheteur;
	private Chocolat choco;
	private String marque;
	private double quantiteT;
	private boolean enTG; // vrai si le vendeur s'engage a mettre ce chocolat en tete de gondole
	                      // false si le vendeur ne s'y engage pas (il pourra le vendre en rayon)
	
	/**
	 * Constructeur initialisant l'offre d'achat avec les informations fournies en parametres 
	 * (leve une IllegalArgumentException si les conditions sur les parametres ne sont
	 * pas respectees). 
	 * @param feve!=null
	 * @param quantite>=AO_FEVES_QUANTITE_MIN
	 */
	public OffreAchat(IAcheteurOA acheteur, Chocolat choco, String marque, double quantiteT, boolean enTG) {
		if (acheteur==null) {
			throw new IllegalArgumentException("Appel du constructeur de OffreAchat avec null pour vendeur");
		}
		if (choco==null) {
			throw new IllegalArgumentException("Appel du constructeur de OffreAchat avec null pour type de chocolat de marque");
		}
		if (quantiteT<OA_QUANTITE_MIN) {
			throw new IllegalArgumentException("Appel du constructeur de OffreAchat avec une quantite de "+quantiteT+" (min="+OA_QUANTITE_MIN+")");
		}
		this.acheteur = acheteur;
		this.choco = choco;
		this.marque = marque;
		this.quantiteT = quantiteT;
		this.enTG = enTG;
	}
	
	public IAcheteurOA getAcheteur() {
		return this.acheteur;
	}
	
	public Chocolat getChocolat() {
		return this.choco;
	}
	
	public String getMarque() {
		return this.marque;
	}
	
	public double getQuantiteT() {
		return this.quantiteT;
	}
	
	public boolean enTG() {
		return this.enTG;
	}
	
	public boolean equals(Object o) {
		return (o instanceof OffreAchat) 
				&& this.getAcheteur().equals(((OffreAchat)o).getAcheteur())
				&& this.getChocolat().equals(((OffreAchat)o).getChocolat())
				&& this.getQuantiteT()==((OffreAchat)o).getQuantiteT()
				&& this.enTG()==((OffreAchat)o).enTG();
	}
	
	public String toString() {
		return "("+getAcheteur()+" veut acheter "+Journal.doubleSur(this.getQuantiteT(), 3)+" de "+this.getChocolat()+")";
	}
}
