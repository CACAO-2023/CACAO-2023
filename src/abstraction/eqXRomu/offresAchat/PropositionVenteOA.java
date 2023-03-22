package abstraction.eqXRomu.offresAchat;

import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class PropositionVenteOA implements Comparable<PropositionVenteOA>{
	private OffreAchat offre;
	private IVendeurOA vendeur;
	private ChocolatDeMarque chocolatDeMarque;
	private double prixT;
	
	public PropositionVenteOA(OffreAchat offre, IVendeurOA vendeur, ChocolatDeMarque chocolatDeMarque, double prixT) {
		this.offre = offre;
		this.vendeur = vendeur;
		this.chocolatDeMarque = chocolatDeMarque;
		this.prixT = prixT;
	}

	public OffreAchat getOffre() {
		return offre;
	}

	public IVendeurOA getVendeur() {
		return vendeur;
	}
	
	public ChocolatDeMarque getChocolatDeMarque() {
		return this.chocolatDeMarque;
	}

	public double getPrixT() {
		return prixT;
	}

	public String toString() {
		return "["+offre+" v="+vendeur+" choco="+chocolatDeMarque+" px="+prixT+"]";
	}
	
	public int compareTo(PropositionVenteOA o) {
		return this.getPrixT()<o.getPrixT()? -1 : (this.getPrixT()==o.getPrixT()? 0 : 1);
	}
}
