package abstraction.eq8Distributeur2;

import java.awt.Color;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class ContratCadre implements IAcheteurContratCadre {

	@Override
	public void initialiser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getNom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Variable> getIndicateurs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Variable> getParametres() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Journal> getJournaux() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCryptogramme(Integer crypto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notificationFaillite(IActeur acteur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notificationOperationBancaire(double montant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getNomsFilieresProposees() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Filiere getFiliere(String nom) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean achete(IProduit produit) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return null;
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
