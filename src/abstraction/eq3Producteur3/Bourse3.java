package abstraction.eq3Producteur3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

/**
 * @author Gabriel
 */
public class Bourse3 extends Producteur3CC implements IVendeurBourse {
	protected HashMap<Integer, Double> quantiteVenduBourseB;
	protected HashMap<Integer, Double> quantiteVenduBourseM;
	
	/** 
	 * @param s = quality of the feve
	 * @author BOCQUET Gabriel
	 */
	public double getQuantiteVenduBourse(String s, int step) {
		if (step > -1 ) {
		if(s=="B" && this.quantiteVenduBourseB.get(step) != null){
			return this.quantiteVenduBourseB.get(step);
		}
		else if (s=="M" && this.quantiteVenduBourseM.get(step) != null) {
			return this.quantiteVenduBourseM.get(step);
		}
		return 0.0;
		}
			return 0.0;
	}
	
	/**
	 * @author BOCQUET Gabriel
	 */
	public double offre(Feve f, double cours) {
		//On veut vendre que les fèves de moyenne quantite en bourse
		int quantite =0;
		if (f == Feve.F_MQ) {
		Lot lotM = super.getStock().getStock().get(Feve.F_MQ_BE);
		Set<Integer> KeyListM = lotM.getQuantites().keySet();
			for(Integer key : KeyListM) {
				//Les feves M ont depasse les 6 mois donc il faut les vendre avant qu'elles perissent
				if(Filiere.LA_FILIERE.getEtape() - key > this.getDateLimM().getValeur()) {
					quantite += lotM.getQuantites().get(key);
				}
			}			
		}
		else if (f == Feve.F_BQ) {
			Lot lotB = super.getStock().getStock().get(Feve.F_BQ);
			Set<Integer> KeyListB = lotB.getQuantites().keySet();
				for(Integer key : KeyListB) {
						quantite += lotB.getQuantites().get(key);
				}
		}
		return quantite;
	}

	/** On va mettre en vente que   des MQ et la bourse ne prend pas en compte le label
	 * @author BOCQUET Gabriel, Corentin Caugant
	 */
	public Lot notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		Lot l = new Lot(f);
		l.ajouter(Filiere.LA_FILIERE.getEtape(), quantiteEnT);
		Stock s = super.getStock();
		if (f == Feve.F_MQ) {
			this.quantiteVenduBourseM.put(Filiere.LA_FILIERE.getEtape(), quantiteEnT);
		s.retirerVielleFeve(Feve.F_MQ_BE, quantiteEnT);
		}
		else if ( f==Feve.F_BQ) {
			       this.quantiteVenduBourseB.put(Filiere.LA_FILIERE.getEtape(), quantiteEnT);
			s.retirerVielleFeve(Feve.F_BQ, quantiteEnT);
		}

		// Ajout de la quantité vendu à notre listeMG qui garde une trace des quantités vendus
		super.addVenteQuantite(quantiteEnT, f);

		super.getJVente().ajouter("La quantite " + quantiteEnT + " en tonnes de " + f.toString() + " va etre vendu "+coursEnEuroParT + "par tonne le step " + (Filiere.LA_FILIERE.getEtape()+1) + " en Bourse");
		return l;
	}

	/**
	 * @author BOCQUET Gabriel
	 */
	public void notificationBlackList(int dureeEnStep) {
		super.getJGeneral().ajouter("Livraison n'a pas pu etre honore");
		
	}

}
