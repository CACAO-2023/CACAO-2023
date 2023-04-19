package abstraction.eq7Distributeur1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.*;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class DistributeurContratCadreVendeurAcheteur extends Distributeur1Acteur implements IAcheteurContratCadre{
	protected List<ExemplaireContratCadre> mesContratEnTantQuAcheteur;
	private Echeancier echeancier_type;
	private IProduit produit;
	
	public DistributeurContratCadreVendeurAcheteur(IProduit produit) {
		super();
		this.produit=produit;
		this.mesContratEnTantQuAcheteur=new LinkedList<ExemplaireContratCadre>();
	}

	public Echeancier echeancier_strat(int stepDebut, int quantite, int nb_step) {
		Echeancier e = new Echeancier(stepDebut, nb_step, quantite/nb_step);
		return e;
		
	}
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		return contrat.getEcheancier();
	}

	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		if (contrat.getPrix()/contrat.getQuantiteTotale() > 1.2) {
			return contrat.getPrix();
		} else {
			return contrat.getPrix()*0.95;
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
		
		journal.ajouter("Recherche d'un vendeur aupres de qui acheter");
		for (IActeur acteur : Filiere.LA_FILIERE.getActeurs()) {
			if (acteur!=this && acteur instanceof IVendeurContratCadre && ((IVendeurContratCadre)acteur).vend(produit)) {
				SuperviseurVentesContratCadre superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
				superviseurVentesCC.demandeAcheteur((IAcheteurContratCadre)this, ((IVendeurContratCadre)acteur), produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, 5.0), cryptogramme,false);
				journal.ajouter("Demande au superviseur de debuter les negociations pour un contrat cadre de "+produit+" avec le vendeur "+acteur);

			}
		}
		
		System.out.println("okkkkk");
		
		// OU proposition d'un contrat a un des vendeurs choisi aleatoirement
//		journal.ajouter("Recherche d'un vendeur aupres de qui acheter");
//		List<IVendeurContratCadre> vendeurs = supCCadre.getVendeurs(produit);
//		if (vendeurs.contains(this)) {
//			vendeurs.remove(this);
//		}
//		IVendeurContratCadre vendeur = null;
//		if (vendeurs.size()==1) {
//			vendeur=vendeurs.get(0);
//		} else if (vendeurs.size()>1) {
//			vendeur = vendeurs.get((int)( Math.random()*vendeurs.size()));
//		}
//		if (vendeur!=null) {
//			journal.ajouter("Demande au superviseur de debuter les negociations pour un contrat cadre de "+produit+" avec le vendeur "+vendeur);
//			ExemplaireContratCadre cc = supCCadre.demandeAcheteur((IAcheteurContratCadre)this, vendeur, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, (SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER+10.0)/10), cryptogramme,false);
//			journal.ajouter("-->aboutit au contrat "+cc);
//		}
		
	}
	
	
//	public String meilleur_prix(Echeancier e,IProduit produit) {
//		HashMap<IActeur, Double> res= new HashMap<>();
//		
//		for (IActeur acteur : Filiere.LA_FILIERE.getActeurs()) {
//			if (acteur!=this && acteur instanceof IVendeurContratCadre && ((IVendeurContratCadre)acteur).vend(produit)) {
//				ExemplaireContratCadre cc = supCCadre.demandeAcheteur((IAcheteurContratCadre)this, (IVendeurContratCadre) acteur, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, (SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER+10.0)/10), cryptogramme,false);
//			}}
//		
//		return "ok";
//	}
	
//	public static Integer obtenirValeurMinimale(HashMap<String, Integer> hashMap) {
//        Integer valeurMinimale = null;
//        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
//            Integer valeur = entry.getValue();
//            if (valeurMinimale == null || valeur < valeurMinimale) {
//                valeurMinimale = valeur;
//            }
//        }
//        return valeurMinimale;	}
	
	
	
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
//		stock.ajouter(this, lot.getQuantiteTotale()); // Cet exemple ne gere pas la peremption : il n'utilise pas la mention du step de production du produit
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
		journal.ajouter("Les negociations avec "+ contrat.getVendeur().getNom()+" ont abouti à un contrat cadre de "+contrat.getProduit().toString());
		
	}


}
