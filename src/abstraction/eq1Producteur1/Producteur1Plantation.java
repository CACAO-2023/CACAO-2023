package abstraction.eq1Producteur1;


import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import abstraction.eqXRomu.produits.Lot;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Feve;
public class Producteur1Plantation extends Producteur1Acteur {

	private champ champ;
	private Lot lot_bas ;
	private Lot lot_moyen ;

	public champ getChamp() {
		return this.champ;
	}
	
	public Lot getStockBas() {
		return this.lot_bas;
	}
	
	public Lot getStockMoy() {
		return this.lot_moyen ;
	}

	
	public void next() {
		HashMap<Integer, Double> stockFeve = lot_bas.getQuantites() ;
		//début Elouan

		int n = champ.nbhectare();

		super.next();
		champ c = this.getChamp();
		for (int i=0; i<n; i++) {
			hectar h = champ.getHectare(i);
			h.setNb_step(h.getNombreSep()+1);
			if (h.getNombreSep()==10) 
				{h.setNb_step(0);
				//rajt une ligne de code pour récolter les feves
				
				// debut gab
				// ramassage, ajout dans stock et replantage
				double nb_tonnes = 0.56 ; //ajouter facteur random
				double random = ThreadLocalRandom.current().nextDouble(0.9, 1.15);
				nb_tonnes = nb_tonnes * random ;
				lot_bas.ajouter(step, nb_tonnes);
				//if (stockFeve.containsKey(step)) {
					//stockFeve.replace(step, stockFeve.get(step)+nb_tonnes) ;
				//} else {
					//stockFeve.ajouter(step, nb_tonnes) ;
				//}
				h.setNb_recolte(h.getNombreRecolte()+1);
				//ajouter lot moyen et cout replantation 
						
						
				h.setNb_recolte(h.getNombreRecolte()+1);}
			if (h.getNombreRecolte()==96) { //supprime l'hectar quand il produit plus
				champ.supphectare(h);
				// supprime l'hectare ou replante direct en fct de la qualité qu'on veut + coût replantation
			}
			}
		//fin Elouan
		
		//debut gab
		
		
		int m = stockFeve.size() ; //faut changer avec le lot
		//calcul nb step du lot périmé
		int nb_step_perime = step-12 ;
		double qtte_perime = stockFeve.get(nb_step_perime) ;
		lot_bas.retirer(qtte_perime) ;
		
		for (int i=0; i<m; i++) {
			//Feve1 feve = stockFeve.getFeve(i) ; //faut changer avec le lot
			//feve.setNbStepsDepuisRecolte(feve.getNbStepsDepuisRecolte()+1) ;
			
			//if (lot_bas.) {
				// péremption fève au bout de 6mois
				//condition pour basse qualité, si moyenne à changer
//				stockFeve.suppFeve(i) ;
//			}
			
			//if (feve.getSeche()==true && feve.getNbStepsDepuisRecolte()>=1) {
				//mise à jour du séchage des fèves après 1 step
				//feve.setSeche(true);
			
			//enfait on prend en compte le séchage au moment de la vente pour éviter de rajouter un booléen aux lots
			}
		
		
		}
		
		//fin gab
		
		
		
	}


