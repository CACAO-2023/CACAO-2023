package abstraction.eqXRomu;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class RomuATVBABVAOAAOACCVCCDistributeur extends RomuATVBABVAOAAOACCVendeurCC implements IDistributeurChocolatDeMarque {

	public double prix(ChocolatDeMarque choco) {
		return 25000;
	}

	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		if (stockChocoMarque.keySet().contains(choco)) {
			double qStock = stockChocoMarque.get(choco);
			return qStock/2.0;
		} else {
			return 0.0;
		}
	}

	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		if (stockChocoMarque.keySet().contains(choco)) {
			double qStock = stockChocoMarque.get(choco);
			return qStock/20.0;
		} else {
			return 0.0;
		}
	}

	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		stockChocoMarque.put(choco, stockChocoMarque.get(choco)-quantite);
		totalStocksChocoMarque.setValeur(this, totalStocksChocoMarque.getValeur(cryptogramme)-quantite, cryptogramme);
		this.journal.ajouter("je vend "+quantite+" T de "+choco+ " aux clients finaux ");
	}

	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
	}
}
