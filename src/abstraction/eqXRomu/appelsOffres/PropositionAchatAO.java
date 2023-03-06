package abstraction.eqXRomu.appelsOffres;

public class PropositionAchatAO implements Comparable<PropositionAchatAO>{
	private OffreVente offre;
	private IAcheteurAO acheteur;
	private double prixT;
	
	public PropositionAchatAO(OffreVente offre, IAcheteurAO acheteur, double prixT) {
		this.offre = offre;
		this.acheteur = acheteur;
		this.prixT = prixT;
	}

	public OffreVente getOffre() {
		return offre;
	}

	public IAcheteurAO getAcheteur() {
		return acheteur;
	}

	public double getPrixT() {
		return prixT;
	}

	public String toString() {
		return "["+offre+" a="+acheteur+" px="+prixT+"]";
	}
	
	public int compareTo(PropositionAchatAO o) {
		return this.getPrixT()<o.getPrixT()? -1 : (this.getPrixT()==o.getPrixT()? 0 : 1);
	}
}
