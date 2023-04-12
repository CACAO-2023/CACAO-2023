package abstraction.eq4Transformateur1.Vente;

import abstraction.eqXRomu.offresAchat.IVendeurOA;

import java.util.List;

import abstraction.eqXRomu.appelsOffres.IVendeurAO;
import abstraction.eqXRomu.appelsOffres.PropositionAchatAO;
import abstraction.eqXRomu.appelsOffres.SuperviseurVentesAO;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.offresAchat.OffreAchat;
import abstraction.eqXRomu.offresAchat.PropositionVenteOA;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class AODistributeur extends CC_distributeur implements IVendeurAO {
	protected SuperviseurVentesAO superviseur;
	
	@Override
	public PropositionAchatAO choisir(List<PropositionAchatAO> propositions) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void initialiser() {
		this.superviseur = (SuperviseurVentesAO)(Filiere.LA_FILIERE.getActeur("Sup.AO"));
	}

//	public void next() {
//		super.next();
//		if (Filiere.LA_FILIERE.getEtape()>=1) {
//			if (this.stock.getValeur()>200) {
//				PropositionAchatAO retenue = superviseur.vendreParAO(this, cryptogramme, this.stockChocoMarque.(), 200.0, false);
//				if (retenue!=null) {
//					this.stock.setValeur(this, this.stock.getValeur()-retenue.getOffre().getQuantiteT());
//					journal.ajouter("vente de "+retenue.getOffre().getQuantiteT()+" T a "+retenue.getAcheteur().getNom());
//				} else {
//					journal.ajouter("pas d'offre retenue");
//				}
//			}
//		}
//	}


}