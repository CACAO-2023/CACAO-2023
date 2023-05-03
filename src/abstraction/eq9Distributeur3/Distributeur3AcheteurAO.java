package abstraction.eq9Distributeur3;

import java.util.HashMap;

import abstraction.eqXRomu.appelsOffres.ExempleAbsAcheteurAO;
import abstraction.eqXRomu.appelsOffres.IAcheteurAO;
import abstraction.eqXRomu.appelsOffres.IVendeurAO;
import abstraction.eqXRomu.appelsOffres.OffreVente;
import abstraction.eqXRomu.appelsOffres.PropositionAchatAO;
import abstraction.eqXRomu.produits.Gamme;
public class Distributeur3AcheteurAO extends ExempleAbsAcheteurAO implements IAcheteurAO {
	
	//////////////////////
	//////  william //////
	//////////////////////
	
	/////////////////////////////////////////////
	///// largement inspiré du code exemple /////
	/////////////////////////////////////////////
	
	private Distributeur3Acteur a;
	private Distributeur3AcheteurCC b;

	protected HashMap<IVendeurAO, Double> prix;

	
	public Distributeur3AcheteurAO(double prixInit,Distributeur3Acteur a,Distributeur3AcheteurCC b) {
		super(prixInit);
		this.prix=new HashMap<IVendeurAO, Double>();
		this.a = a;
		this.b = b;
	}
	

	@SuppressWarnings("unlikely-arg-type")
	public double proposerPrix(OffreVente offre) {
		a.journal_AO.ajouter("ProposerPrix("+offre+"):");
			double px = this.prixInit;
			if (this.prix.keySet().contains(offre.getVendeur())) {
				px = this.prix.get(offre.getVendeur());
			}
			
			else {
				// le prix d'achat initial est le prix moyen entre le prix du marche et le prix propose si le prix proposse est superieur au prix moyen
				if(px < b.prixMax.get(offre.getChocolat()))
				{
					px = this.prix.get(offre.getVendeur());

				}
			}
			
			a.journal_AO.ajouter("   je propose "+px);
			return px;
	}

	public void notifierAchatAO(PropositionAchatAO propositionRetenue) {
		double stock = (this.stock.keySet().contains(propositionRetenue.getOffre().getChocolat())) ?this.stock.get(propositionRetenue.getOffre().getChocolat()) : 0.0;
		this.stock.put(propositionRetenue.getOffre().getChocolat(), stock+ propositionRetenue.getOffre().getQuantiteT());
		this.prix.put(propositionRetenue.getOffre().getVendeur(), propositionRetenue.getPrixT()-1000.0);
		a.journal_AO.ajouter("   mon prix a ete accepte. Mon prix pour "+propositionRetenue.getOffre().getVendeur()+" passe a "+(propositionRetenue.getPrixT()-1000.0));
	}

	public void notifierPropositionNonRetenueAO(PropositionAchatAO propositionNonRetenue) {
		this.prix.put(propositionNonRetenue.getOffre().getVendeur(), propositionNonRetenue.getPrixT()+100.);
		a.journal_AO.ajouter("   mon prix a ete refuse. Mon prix pour "+propositionNonRetenue.getOffre().getVendeur()+" passe a "+(propositionNonRetenue.getPrixT()+100.));
	}
}