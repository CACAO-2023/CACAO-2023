package abstraction.eq5Transformateur2;

import java.awt.Color;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Transformateur2AcheteurCC extends Transformateur2Acteur implements IAcheteurContratCadre {

	public static Color COLOR_LLGRAY = new Color(238,238,238);
	protected SuperviseurVentesContratCadre superviseurVentesCC;
	public void initialiser() {
		super.initialiser();
		this.superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}

	@Override
	public boolean achete(IProduit produit) {
		// TODO Auto-generated method stub
		if (produit instanceof ChocolatDeMarque) {
			this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'affirme vouloir acheter le produit "+produit);
		return true;}
		else {
			this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'affirme ne pas vouloir acheter le produit "+produit);
		return false;}
		
		
		
	}

	@Override
	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		if ((( ((ChocolatDeMarque) produit).getMarque())) == "Maison Doutre") {
			return 10; }
		else { 
			return 0; 
			}
	}
		// TODO Auto-generated method stub


	@Override
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (contrat.getTeteGondole()) {
			return null;
		}
		this.journal.ajouter(COLOR_LLGRAY, Color.BLUE, "  CCA : j'accepte l'echeancier "+contrat.getEcheancier());
		return contrat.getEcheancier();
	}
		

	@Override
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}

}

