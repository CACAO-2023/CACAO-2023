package abstraction.eq7Distributeur1;

import java.util.ArrayList;
import java.util.HashMap;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Distributeur1 extends Distributeur1AcheteurOA implements IDistributeurChocolatDeMarque {
	
	public Distributeur1() {
		super();
	}
	
	public void initialiser() {
		super.initialiser();
	}
	
	public void next() {
		super.next();
		int etape = Filiere.LA_FILIERE.getEtape();
		journal.ajouter("============================== étape "+etape+" ==============================");
		journal_achat.ajouter("============================== étape "+etape+" ==============================");
		journal_stock.ajouter("============================== étape "+etape+" ==============================");
	}
	
	private void strategie() {
	}
	
	/**
	 * @author Theo
	 * @return notre meilleure vente à ce tour
	 */
	protected ChocolatDeMarque topvente() {
		int etape = Filiere.LA_FILIERE.getEtape();
		int etapenormalisee = (etape+24)%24;
		ChocolatDeMarque topmarque = Filiere.LA_FILIERE.getChocolatsProduits().get(0);
		double topvente = previsionsperso.get(etapenormalisee).get(topmarque);
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (previsionsperso.get(etapenormalisee).get(marque) > topvente) {
				topmarque = marque;
			}
		}
		return topmarque;
	}
	
	/**
	 * @author Theo
	 * @param choco, choco!=null
	 * @return Le prix de vente actuel d'une tonne de chocolat choco
	 * IMPORTANT : durant une meme etape, la fonction doit toujours retourner la meme valeur pour un chocolat donne.
	 */
	public double prix(ChocolatDeMarque choco) {
		double qualite = choco.qualitePercue();
//		double coef = 1-(((10/3)*qualite)/100)+0.1;
		double promo = prixPromotion(choco);
		double cout = getCout(choco);
//		return (cout/1000)*promo/coef;
		double prix = (cout)*promo*1.2;
		return prix;
	}
	
	/**
	 * @author Theo	 
	 * @return Le coeff de promo : une fois toutes les 3 etapes, on applique une promotion
	 */
	public double prixPromotion(ChocolatDeMarque choco) { 
		if (((Filiere.LA_FILIERE.getEtape()%3)==0)&&(choco.getChocolat()!=Chocolat.C_BQ)) {
			return 0.9;
		}
		else {
			return 1;
		}
	}
	
	/**
	 * @author Theo
	 * @param choco, choco!=null
	 * @return Retourne la quantite totale (rayon+tete de gondole) en tonne de chocolat de type choco 
	 * actuellement disponible a la vente (pour un achat immediat --> le distributeur a 
	 * au moins cette quantite en stock)
	 */
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		// recopie de l'exemple de romu
//		if (stockChocoMarque7.keySet().contains(choco)) {
//			double qStock = stockChocoMarque7.get(choco);
//			return qStock/2.0;
//		} else {
		return stockChocoMarque.get(choco);
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
	
	/**
	 * @author Theo
	 * Pour la V1, on mettra seulement en tête de gondole le produit le plus vendu, pour booster ses ventes
	 * La mise en place d'une contrepartie avec le transformateur sera mise en place lors de la V2
	 */
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {

		ChocolatDeMarque topmarque = topvente();
		double seuil = Filiere.SEUIL_EN_TETE_DE_GONDOLE_POUR_IMPACT;
		if ((choco==topmarque)&&(stockChocoMarque.get(topmarque)>0)) {
			return stockChocoMarque.get(topmarque)/10;
		}
		return 0.0;
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
	 * @author Theo
	 */
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		stockChocoMarque.put(choco, stockChocoMarque.get(choco)-quantite);
		totalStocks.setValeur(this, totalStocks.getValeur(cryptogramme)-quantite, cryptogramme);
		this.journal.ajouter("Eq7 a vendu "+ (int)Math.floor(quantite)+" T de "+choco+ " aux clients finaux pour un total de " + (int)Math.floor(montant));
		
		//Actualisation des previsions persos
		actualiser_prevision_perso( choco,   quantite);

	}

	
	/**
	 * Methode appelee par le client final lorsque la quantite en rayon de chocolat choco 
	 * est inferieure a la quantite desiree
	 * @param choco, le chocolat de marque dont la quantite en rayon a ete integralement achetee
	 */
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		journal_stock.ajouter("Rayon vide pour le chocolat :"+choco);
	}
}	
	
