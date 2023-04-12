package abstraction.eq1Producteur1;


import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Lot;
public class Producteur1Plantation extends Producteur1Acteur {

	public champ getChampBas() {
		return this.champBas;
	}
	public champ getChampMoy() {
		return this.champMoy;
	}
	
	public Lot getStockBas() {
		return this.stockFeveBas;
	}
	
	public Lot getStockMoy() {
		return this.stockFeveMoy ;
	}

	
	public void next() {
		//===== début Elouan =====
		super.next();
		Lot lot_bas = this.getStockBas();
		HashMap<Integer, Double> quantiteFeveB = lot_bas.getQuantites() ;
		champ cb = this.getChampBas();
		for (Integer i : cb.getQuantite().keySet()) {
			double q = cb.getQuantite().get(i);
			if (step-i==2080) { //supprime l'hectar quand il produit plus, au bout de 40 ans pour la v1
				cb.supprimer(i);
				cb.ajouter(step, q); //on le replante quand il périme : v2 à améliorer
			}
				else if ((step-i)%10==0 && step-i>0) 
				// ===== elouan et début gab =====
				{Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), 30*q);
				double nb_tonnes = q*0.56 ; //ajouter facteur random
				double random = ThreadLocalRandom.current().nextDouble(0.9, 1.15);
				nb_tonnes = nb_tonnes * random ;
				lot_bas.ajouter(step, nb_tonnes); //recolte
				
				if (quantiteFeveB.containsKey(step)) {
					quantiteFeveB.replace(step, quantiteFeveB.get(step)+nb_tonnes);
				} else {
					quantiteFeveB.put(step, nb_tonnes);
				}
				//ajouter lot moyen et cout replantation 
						}
			}
		//on retire les feves perimes
		int nb_step_perime = step-12;
		quantiteFeveB.remove(nb_step_perime);
		Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), this.getStockBas().getQuantiteTotale()*50);
		
		Lot lot_moy = this.getStockMoy();
		HashMap<Integer, Double> quantiteFeveM = lot_moy.getQuantites() ;
		champ cm = this.getChampMoy();
		for (Integer i : cm.getQuantite().keySet()) {
			double q = cm.getQuantite().get(i);
			if (step-i==2080) { //supprime l'hectar quand il produit plus, au bout de 40 ans pour la v1
				cm.supprimer(i);
				cm.ajouter(step, q); //on le replante quand il périme : v2 à améliorer
			}
				else if ((step-i)%12==0 && step-i>0) 
				// ===== elouan et début gab =====
				{Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), 30*q);
				double nb_tonnes = q*0.56 ; //ajouter facteur random
				double random = ThreadLocalRandom.current().nextDouble(0.9, 1.1);
				nb_tonnes = nb_tonnes * random ;
				lot_moy.ajouter(step, nb_tonnes); //recolte
				
				if (quantiteFeveM.containsKey(step)) {
					quantiteFeveM.replace(step, quantiteFeveM.get(step)+nb_tonnes);
				} else {
					quantiteFeveM.put(step, nb_tonnes);
				}
				//ajouter lot moyen et cout replantation 
						}
			}
		//on retire les feves perimes
		quantiteFeveM.remove(nb_step_perime);
		Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), this.getStockBas().getQuantiteTotale()*50);
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


