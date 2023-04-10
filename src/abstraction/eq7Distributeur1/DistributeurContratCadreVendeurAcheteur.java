package abstraction.eq7Distributeur1;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.*;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class DistributeurContratCadreVendeurAcheteur extends DistributeurContratCadre implements IAcheteurContratCadre{
	protected List<ExemplaireContratCadre> mesContratEnTantQuAcheteur;
	private Echeancier echeancier_type;
	
	public DistributeurContratCadreVendeurAcheteur(IProduit produit) {
		super(produit);
		this.mesContratEnTantQuAcheteur=new LinkedList<ExemplaireContratCadre>();
	}

	public Echeancier echeancier_strat(int stepDebut, int quantite, int nb_step) {
		Echeancier e = new Echeancier(stepDebut, nb_step, quantite/nb_step);
		return e;
		
	}
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (Math.random()<0.1) {
			return contrat.getEcheancier(); // on ne cherche pas a negocier sur le previsionnel de livraison
		} else {//dans 90% des cas on fait une contreproposition pour l'echeancier
			Echeancier e = contrat.getEcheancier();
			e.set(e.getStepDebut(), e.getQuantite(e.getStepDebut())*2.5);// on souhaite livrer 2.5 fois plus lors de la 1ere livraison... un choix arbitraire, juste pour l'exemple...
			return e;
		}
	}

	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		if (Math.random()<0.1) {
			return contrat.getPrix(); // on ne cherche pas a negocier dans 10% des cas
		} else {//dans 90% des cas on fait une contreproposition differente
			return contrat.getPrix()*0.95;// 5% de moins.
		}
	}
	public void next() {
		super.next();
		// On enleve les contrats obsolete (nous pourrions vouloir les conserver pour "archive"...)
		List<ExemplaireContratCadre> contratsObsoletes=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.mesContratEnTantQuAcheteur) {
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				contratsObsoletes.add(contrat);
			}
		}
		this.mesContratEnTantQuAcheteur.removeAll(contratsObsoletes);
		
		
		// Proposition d'un nouveau contrat a tous les vendeurs possibles
		/*
		for (IActeur acteur : Filiere.LA_FILIERE.getActeurs()) {
			if (acteur!=this && acteur instanceof IVendeurContratCadre && ((IVendeurContratCadre)acteur).vend(produit)) {
				Filiere.LA_FILIERE.getSuperviseurContratCadre().demande((IAcheteurContratCadre)this, ((IVendeurContratCadre)acteur), produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, 5.0), cryptogramme);
			}
		}*/
		
		
		// OU proposition d'un contrat a un des vendeurs choisi aleatoirement
		journal.ajouter("Recherche d'un vendeur aupres de qui acheter");
		List<IVendeurContratCadre> vendeurs = supCCadre.getVendeurs(produit);
		if (vendeurs.contains(this)) {
			vendeurs.remove(this);
		}
		IVendeurContratCadre vendeur = null;
		if (vendeurs.size()==1) {
			vendeur=vendeurs.get(0);
		} else if (vendeurs.size()>1) {
			vendeur = vendeurs.get((int)( Math.random()*vendeurs.size()));
		}
		if (vendeur!=null) {
			journal.ajouter("Demande au superviseur de debuter les negociations pour un contrat cadre de "+produit+" avec le vendeur "+vendeur);
			ExemplaireContratCadre cc = supCCadre.demandeAcheteur((IAcheteurContratCadre)this, vendeur, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, (SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER+10.0)/10), cryptogramme,false);
			journal.ajouter("-->aboutit au contrat "+cc);
		}
		
	}

	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		stock.ajouter(this, lot.getQuantiteTotale()); // Cet exemple ne gere pas la peremption : il n'utilise pas la mention du step de production du produit
	}

	public boolean achete(IProduit produit) {
		return true;
	}
	public String toString() {
		return this.getNom();
	}

	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		return 5;
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}



	



}
