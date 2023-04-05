package abstraction.eq7Distributeur1;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;

public class Distributeur1 extends Distributeur1Acteur implements IDistributeurChocolatDeMarque {
	
	public Distributeur1() {
		super();
	}
	/**
	 * @param choco, choco!=null
	 * @return Le prix actuel d'un Kg de chocolat choco
	 * IMPORTANT : durant une meme etape, la fonction doit toujours retourner la meme valeur pour un chocolat donne.
	 */
	public double prix(ChocolatDeMarque choco) {
		double qualite = choco.qualitePercue();
		double coef = 1-(((10/3)*qualite)/100)+0.1;
		if (choco.getChocolat()==Chocolat.C_BQ) {
			return coutCB/coef;
		}
		else if (choco.getChocolat()==Chocolat.C_MQ) {
			return coutCMNL/coef;
		}
		else if (choco.getChocolat()==Chocolat.C_MQ_BE) {
			return coutCML/coef;
		}
		else if (choco.getChocolat()==Chocolat.C_HQ_BE) {
			return coutCH/0.8;
		}
		return 2;
	}
	
	public double prixPromotion(ChocolatDeMarque choco) {
		double p = prix(choco);
		if (((temps%4)==0)&&(choco.getChocolat()!=Chocolat.C_BQ)) {
			return p*0.9;
		}
		else {
			return p;
		}
	}
	
	/**
	 * @param choco, choco!=null
	 * @return Retourne la quantite totale (rayon+tete de gondole) en Kg de chocolat de type choco 
	 * actuellement disponible a la vente (pour un achat immediat --> le distributeur a 
	 * au moins cette quantite en stock)
	 */
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		// recopie de l'exemple de romu
//		if (stockChocoMarque7.keySet().contains(choco)) {
//			double qStock = stockChocoMarque7.get(choco);
//			return qStock/2.0;
//		} else {
		return 0.0;
//		}
	}
	
	/**
	 * @param choco, choco!=null
	 * @return Retourne la quantite en Kg de chocolat de type choco 
	 * actuellement disponible a la vente EN TETE DE GONDOLE (pour un
	 * achat immediat --> le distributeur a au moins cette quantite en stock)
	 * Remarque : La somme des quantites en vente en tete de gondole ne peut 
	 * pas depasser 10% de la somme totale des quantites mises en vente. 
	 */
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		//recopie de l'exemple de romu
//		if (stockChocoMarque7.keySet().contains(choco)) {
//			double qStock = stockChocoMarque7.get(choco);
//			return qStock/20.0;
//		} else {
		return 0.0;
//		}
	}
	
	/**
	 * Met a jour les donnees du distributeur (dont son stock de chocolat choco) suite
	 * a la vente d'une quantite quantite de chocolat choco.
	 * Lorsque le client appelle cette methode il a deja verse la somme montant correspondant a l'achat
	 * sur le compte du distributeur : le distributeur a recu une notification de la banque ce qui lui permet 
	 * de verifier que la transaction est bien effective.
	 * @param client, le client qui achete 
	 * @param choco, choco!=null
	 * @param quantite, quantite>0.0 et quantite<=quantiteEnVente(choco)
	 * @param montant, le montant correspondant a la transaction que le client a deja verse sur le compte du distributeur
	 */
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
//		stockChocoMarque7.put(choco, stockChocoMarque7.get(choco)-quantite);
//		totalStocks.setValeur(this, totalStocks.getValeur(cryptogramme)-quantite, cryptogramme);
	}
	
	/**
	 * Methode appelee par le client final lorsque la quantite en rayon de chocolat choco 
	 * est inferieure a la quantite desiree
	 * @param choco, le chocolat de marque dont la quantite en rayon a ete integralement achetee
	 */
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		
	}
}	
	
