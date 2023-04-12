package abstraction.eq1Producteur1;


import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import abstraction.eqXRomu.produits.Lot;
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
		//===== début Elouan =====
		HashMap<Integer, Double> stockFeve = lot_bas.getQuantites() ;
		super.next();
		champ c = this.getChamp();
		for (Integer i : c.getQuantite().keySet()) {
			double q = c.getQuantite().get(i);
			if (step-i==2080) { //supprime l'hectar quand il produit plus, au bout de 40 ans pour la v1
				champ.supprimer(i);
				champ.ajouter(step, q); //on le replante quand il périme : v2 à améliorer
			}
				else if ((step-i)%10==0 && step-i>0) 
				// ===== elouan et début gab =====
				{double nb_tonnes = q*0.56 ; //ajouter facteur random
				double random = ThreadLocalRandom.current().nextDouble(0.9, 1.15);
				nb_tonnes = nb_tonnes * random ;
				lot_bas.ajouter(step, nb_tonnes); //recolte
				
				if (stockFeve.containsKey(step)) {
					stockFeve.replace(step, stockFeve.get(step)+nb_tonnes) ;
				} else {
					stockFeve.put(step, nb_tonnes) ;
				}
				//ajouter lot moyen et cout replantation 
						}
			}
		//on retire les feves perimes
		int nb_step_perime = step-12;
		this.lot_bas.getQuantites().remove(nb_step_perime);
		
		//Elouan : tout ce qui suit sert a rien a mon avis si on utilise la classe Feve (on retrouve si la feve est seche avec la hashmap etc)
		
		//for (Integer i : this.lot_bas.getQuantites().keySet()) {
			//Feve feve = this.lot_bas.getProduit() ; 
			//if (lot_bas.) {
				// péremption fève au bout de 6mois
				//condition pour basse qualité, si moyenne à changer
				//stockFeve.suppFeve(i) ;
			//}
			
			//if (feve.getSeche()==true && feve.getNbStepsDepuisRecolte()>=1) {
				//mise à jour du séchage des fèves après 1 step
				//feve.setSeche(true);
			
			//enfait on prend en compte le séchage au moment de la vente pour éviter de rajouter un booléen aux lots
			//}
		
		
		}
		
		//===== fin elouan et gab =====
		
		
		
	}


