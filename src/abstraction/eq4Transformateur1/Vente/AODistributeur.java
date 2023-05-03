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

/**
 * @author alexian, verification François
 *
 */

public class AODistributeur extends CC_distributeur implements IVendeurAO {
	protected SuperviseurVentesAO superviseur;
	
	@Override
	public PropositionAchatAO choisir(List<PropositionAchatAO> propositions) {
		double prix=1000;
		PropositionAchatAO propRetenue=null;
		for (PropositionAchatAO p : propositions) {
			if (p.getPrixT()>prix) {
				propRetenue=p;
			}
		}
		return propRetenue;
	}
	
	public void initialiser() {
		super.initialiser();
		this.superviseur = (SuperviseurVentesAO)(Filiere.LA_FILIERE.getActeur("Sup.AO"));
	}

	public void next() {
		super.next();
		for (ChocolatDeMarque c : stockChocoMarque.keySet()) {
			if (this.stockChocoMarque.get(c)>2000) {
				PropositionAchatAO retenue = superviseur.vendreParAO(this, cryptogramme, c, 2000.0, false);
				if (retenue!=null) {
					this.totalStocksChocoMarque.ajouter(this,-retenue.getOffre().getQuantiteT() ,this.cryptogramme);
					this.stockChocoMarque.put(c, this.stockChocoMarque.get(c)-retenue.getOffre().getQuantiteT());
					journal.ajouter("vente de "+retenue.getOffre().getQuantiteT()+" T a "+retenue.getAcheteur().getNom());
				} else {
					journal.ajouter("pas d'offre retenue");
				}
			}
		}
	}


}