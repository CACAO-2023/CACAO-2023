package abstraction.eq2Producteur2;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Producteur2ASPPVendeurBourse extends Producteur2ASProducteurPlanteur implements IVendeurBourse{
	
	// code écrit par Flavien
	
	public Producteur2ASPPVendeurBourse() {
		super();
	}

	/**
	 * Retourne la quantite en tonnes de feves de type f que le vendeur 
	 * souhaite vendre a cette etape sachant que le cours actuel de 
	 * la feve f est cours
	 * @param f le type de feve
	 * @param cours le cours actuel des feves de type f
	 * @return la quantite en tonnes de feves de type f que this souhaite vendre 
	 */
	
	public double stock_mis_en_bourse(Feve f) {
		if (f==Feve.F_BQ) {
			return this.stockTotBasse.getValeur();
		}
		if (f==Feve.F_MQ) {
			return this.stockTotMoy.getValeur();
		}
		if (f==Feve.F_MQ_BE) {
			return this.stockTotMoyBE.getValeur();
		}
		if (f==Feve.F_HQ_BE) {
			return this.stockTotHauteBE.getValeur();
		}
		return 0;
	}
		
	// Cette fonction en-dessous doit servir à déterminer la quantité de chaque fève mis en bourse en fonction du cours de la bourse et des quantités disponible,
	// attention à bien décrémenter le stock entre les étapes, notamment celui du contrat cadre.
	
	public double offre(Feve f, double cours_de_f) {
		if (f==Feve.F_BQ) {
			// regarder en focntion du cours de F et de la fève  un seuil à partir duquel il est acceptable de vendre, et en quelles quantités
			// celà passe par la création de 2 prix seuil, un à partir duquel on commence à vendre, et un autre à partir duquel on vend tout le stock
			// avec une continuité linéaire de la relation prix/proportion_vendue entre ces 2 points
			// prix_seuil_1=1, prix_seuil_2=10, ces prix sont pour l'instant arbitraires
			float prix_seuil_1=1;
			float prix_seuil_2=10;
			if (cours_de_f < prix_seuil_1) {
				return 0;
			}
			if (cours_de_f >= prix_seuil_1 && cours_de_f <= prix_seuil_2) {
				return stock_mis_en_bourse(f)*(cours_de_f - prix_seuil_1)/(prix_seuil_2 - prix_seuil_1);
			}
			if(cours_de_f >= prix_seuil_2) {
				return stock_mis_en_bourse(f);
			}
		}
		if (f==Feve.F_MQ) {
			float prix_seuil_1=10;
			float prix_seuil_2=100;
			if (cours_de_f < prix_seuil_1) {
				return 0;
			}
			if (cours_de_f >= prix_seuil_1 && cours_de_f <= prix_seuil_2) {
				return stock_mis_en_bourse(f)*(cours_de_f - prix_seuil_1)/(prix_seuil_2 - prix_seuil_1);
			}
			if(cours_de_f >= prix_seuil_2) {
				return stock_mis_en_bourse(f);
			}
		}
//		if (f==Feve.F_MQ_BE){            ON NE VEUT VENDRE EN BOURSE QUE DES FEVES BQ ET MQ
//			float prix_seuil_1=100;
//			float prix_seuil_2=1000;
//			if (cours_de_f < prix_seuil_1) {
//				return 0;
//			}
//			if (cours_de_f >= prix_seuil_1 && cours_de_f<=prix_seuil_2) {
//				return stock_mis_en_bourse(f)*(cours_de_f - prix_seuil_1)/(prix_seuil_2 - prix_seuil_1);
//			}
//			if(cours_de_f >= prix_seuil_2) {
//				return stock_mis_en_bourse(f);
//			}
//		}
//		if (f==Feve.F_HQ_BE) {
//			float prix_seuil_1=1000;
//			float prix_seuil_2=10000;
//			if (cours_de_f < prix_seuil_1) {
//				return 0;
//			}
//			if (cours_de_f >= prix_seuil_1 && cours_de_f <= prix_seuil_2) {
//				return stock_mis_en_bourse(f)*(cours_de_f - prix_seuil_1)/(prix_seuil_2 - prix_seuil_1);
//			}
//			if(cours_de_f >= prix_seuil_2) {
//				return stock_mis_en_bourse(f);
//			}
//		}
		return 0;
	}

	/**
	 * Methode appelee par la bourse pour avertir le vendeur qu'il est parvenu
	 * a vendre quantiteEnT tonnes de feve f au prix de coursEnEuroParT euros par tonne.
	 * L'acteur this doit retourner un Lot de feves F de quantite totale>=quantiteEnT et 
	 * retirer ces feves livrees de son stock de feves .
	 * Lorsque cette methode est appelee la transaction bancaire a eu lieu 
	 * (vendeurs et acheteurs n'ont pas a s'occuper du virement)
	 */
	public Lot notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		Lot l = new Lot(f);
		l.ajouter(Filiere.LA_FILIERE.getEtape(), quantiteEnT); 
		if (f == Feve.F_BQ) {
			this.BQquantiteVendueBourse.setValeur(null, quantiteEnT);
		
		}
		if (f == Feve.F_MQ) {
			this.MQquantiteVendueBourse.setValeur(null, quantiteEnT);
		}
		this.stockFeve.setValeur(this, this.stockFeve.getValeur()-quantiteEnT);
		return l;
	}

	/**
	 * Methode appelee par la bourse pour avertir le vendeur qu'il vient 
	 * d'etre ajoute a la black list : l'acteur a precise une quantite qu'il desirait mettre en vente
	 * qu'il n'a pas pu honorer (le lot qu'il a retourne n'etait pas du bon 
	 * type de feves ou de quantite insuffisante)
	 * this ne pourra pas vendre en bourse pendant la duree precisee en 
	 * parametre 
	 */
	public void notificationBlackList(int dureeEnStep) {
		this.journal.ajouter("Aie... blackliste pendant 6 steps");
	}

	
	

}
