package abstraction.eq7Distributeur1;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class Distributeur1 extends Distributeur1Acteur implements IDistributeurChocolatDeMarque {
	
	public Distributeur1() {
		super();
	}
	
	public double prix(ChocolatDeMarque choco) {
		return 10;
	}
	
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		return 10;
	}
	
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		return 10;
	}
	
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
	
	}
	
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		
	}
}	
	
