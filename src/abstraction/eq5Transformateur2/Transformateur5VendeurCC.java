package abstraction.eq5Transformateur2;

import java.util.HashMap;
import java.util.Map;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;
 
public class Transformateur5VendeurCC extends Transformateur2Acteur implements IVendeurContratCadre {
	
	private Map<IProduit, Double> stocks;
	
	public Transformateur5VendeurCC() {
		this.stocks = new HashMap<IProduit, Double>();
		//initialisation des stocks avec des valeurs.
		
	}
	
	 
	
	public void next() {
		super.next();
		
	}

	
	@Override
	public boolean vend(IProduit produit) {
		if (this.stocks.containsKey(produit) && this.stocks.get(produit) > 0) {
			// Le transformateur a suffisamment de stock pour vendre le produit demand
			return true;
		}
		// Sinon, le transformateur ne peut pas vendre le produit demand
		return false;
	
	
		
		
		
	
	}

	@Override
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		//if (contrat.getProduit().equals(produit)) {
			//if (contrat.getEcheancier().getQuantiteTotale()<stock.getValeur()) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double propositionPrix(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Lot livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return null;
	}

}
