package abstraction.eq3Producteur3;

import java.util.Set;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

/**
 * @author Gabriel
 */
public class Bourse3 extends Producteur3CC implements IVendeurBourse {
	
	/**
	 * @author BOCQUET Gabriel
	 */
	public double offre(Feve f, double cours) {
		//On veut vendre que les fèves de moyenne quantite en bourse
		int quantiteM =0;
		Lot lotM = super.getStock().getStock().get(Feve.F_MQ_BE);
		Set<Integer> KeyListM = lotM.getQuantites().keySet();
			for(Integer key : KeyListM) {
				//Les feves M ont depasse les 6 mois donc il faut les vendre avant qu'elles perissent
				if(Filiere.LA_FILIERE.getEtape() - key > 12) {
					quantiteM += lotM.getQuantites().get(key);
				}
			
			
		}
		return quantiteM;
	}

	/** On va mettre en vente que des MQ et la bourse ne prend pas en compte le label
	 * @author BOCQUET Gabriel
	 */
	public Lot notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		Lot l = new Lot(f);
		l.ajouter(Filiere.LA_FILIERE.getEtape(), quantiteEnT);
		Stock s = super.getStock();
		s.retirerVielleFeve(Feve.F_MQ_BE, quantiteEnT);
		super.getJVente().ajouter("La quantite " + quantiteEnT + " en tonnes de " + f.toString() + " a ete vendu "+coursEnEuroParT + "le step " + Filiere.LA_FILIERE.getEtape() + " en Bourse");
		return l;
	}

	/**
	 * @author BOCQUET Gabriel
	 */
	public void notificationBlackList(int dureeEnStep) {
		super.getJGeneral().ajouter("Livraison n'a pas pu etre honore");
		
	}

}
