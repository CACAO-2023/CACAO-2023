package abstraction.eqXRomu.appelsOffres;

import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;

public class ExempleVendeurAO extends ExempleAbsVendeurAO implements IVendeurAO {
	protected SuperviseurVentesAO superviseur;

	public ExempleVendeurAO(Chocolat choco, String marque, double stock, double prixMin) {
		super(choco, marque, stock, prixMin);
	}
	
	public void initialiser() {
		this.superviseur = (SuperviseurVentesAO)(Filiere.LA_FILIERE.getActeur("Sup.AO"));
		journal.ajouter("PrixMin=="+this.prixMin);

	}
	
	public void next() {
		journal.ajouter("Etape="+Filiere.LA_FILIERE.getEtape());
		if (Filiere.LA_FILIERE.getEtape()>=1) {
			if (this.stock.getValeur()>200) {
				PropositionAchatAO retenue = superviseur.vendreParAO(this, cryptogramme, getChocolatDeMarque(), 200.0, false);
				if (retenue!=null) {
					this.stock.setValeur(this, this.stock.getValeur()-retenue.getOffre().getQuantiteT());
					journal.ajouter("vente de "+retenue.getOffre().getQuantiteT()+" T a "+retenue.getAcheteur().getNom());
				} else {
					journal.ajouter("pas d'offre retenue");
				}
			}
		}
	}

	public PropositionAchatAO choisir(List<PropositionAchatAO> propositions) {
		this.journal.ajouter("propositions : "+propositions);
		if (propositions==null) {
			return null;
		} else {
			PropositionAchatAO retenue = propositions.get(0);
			if (retenue.getPrixT()>this.prixMin) {
				this.journal.ajouter("  --> je choisis "+retenue);
				return retenue;
			} else {
				this.journal.ajouter("  --> je ne retiens rien");
				return null;
			}
		}
	}

}
