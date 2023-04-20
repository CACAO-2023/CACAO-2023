package abstraction.eq1Producteur1;


import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;
public class Producteur1Plantation extends Producteur1Acteur {
	private Lot stockBasPasSec;
	private Lot stockMoyPasSec;

//===== 4 premières methodes : elouan =====

	public champ getChampBas() {
		return this.champBas;
	}
	public champ getChampMoy() {
		return this.champMoy;
	}
	public Lot getStockMoy() {
		return this.stockMoyPasSec ;
	}
	public Lot getStockBas() {
		return this.stockBasPasSec ;
	}
	public Lot getVraiStockM() {
		return this.stockFeveMoy;
	}
	public Lot getVraiStockB() {
		return this.stockFeveBas;
	}
	
	public void initialiser () { // elouan
		//initialisation d'un faible nombre de feves qui sont en sechage
		super.initialiser();
		this.stockBasPasSec = new Lot(Feve.F_BQ);
		this.stockBasPasSec.ajouter(0,50);
		this.stockMoyPasSec = new Lot(Feve.F_MQ);
		this.stockMoyPasSec.ajouter(0,10);
	}

	
	public void next() {
		//===== début Elouan =====
		super.next();
		Lot lot_moy = this.getStockMoy();
		Lot lot_moy_sec = this.getVraiStockM();
		HashMap<Integer, Double> quantiteFeveM = lot_moy.getQuantites();
		champ cm = this.getChampMoy();
		for (Integer i : cm.getQuantite().keySet()) {
			double q = cm.getQuantite().get(i);
			Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), 30*q);
			if (step-i==2080) { //supprime l'hectar quand il produit plus, au bout de 40 ans pour la v1
				cm.supprimer(i);
				cm.ajouter(step, q); //on le replante quand il périme : v2 à améliorer
			}
				else if ((step-i)%12==0 && step-i>0) 
				// ===== elouan et début gab =====
				{
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
		int nb_step_perime = step-12;
		Double fevemoyabas = quantiteFeveM.get(nb_step_perime); //quantite de feve qui baisse de gamme, qu'on va rajouter dans le stock de bas
		quantiteFeveM.remove(nb_step_perime);
		// on s'occupe des feves sechées
		Double nb_feve_sec = quantiteFeveM.get(step-1);
		lot_moy_sec.ajouter(step, nb_feve_sec);
		quantiteFeveM.remove(step-1);
		Lot lot_bas = this.getStockBas();
		HashMap<Integer, Double> quantiteFeveB = lot_bas.getQuantites();
		Lot lot_bas_sec = this.getVraiStockB();
		champ cb = this.getChampBas();
		for (Integer i : cb.getQuantite().keySet()) {
			double q = cb.getQuantite().get(i);
			Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), 30*q);
			if (step-i==2080) { //supprime l'hectar quand il produit plus, au bout de 40 ans pour la v1
				cb.supprimer(i);
				cb.ajouter(step, q); //on le replante quand il périme : v2 à améliorer
			}
				else if ((step-i)%10==0 && step-i>0) 
				// ===== elouan et début gab =====
				{
				double nb_tonnes = q*0.56 ; //ajouter facteur random
				double random = ThreadLocalRandom.current().nextDouble(0.9, 1.15);
				nb_tonnes = nb_tonnes * random ;
				stockFeveBas.ajouter(step, nb_tonnes); //recolte
				
				if (quantiteFeveB.containsKey(step)) {
					quantiteFeveB.replace(step, quantiteFeveB.get(step)+nb_tonnes);
				} else {
					quantiteFeveB.put(step, nb_tonnes);
				}
				//ajouter lot moyen et cout replantation 
						}
			}
		//on retire les feves perimes
		if (fevemoyabas!=null) {
			quantiteFeveB.put(step-6, fevemoyabas); //on rajoute les feves qui ont baisse de qualite pour qu'elles aient une duree de vie de 3 mois
		}
		quantiteFeveB.remove(nb_step_perime);
		// on s'occupe des feves sechées
		Double nb_feve_sec2 = quantiteFeveB.get(step-1);
		lot_bas_sec.ajouter(step, nb_feve_sec2);
		quantiteFeveB.remove(step-1);
		Double q1 = 0.0;
		Double q2 = 0.0;
		if (lot_moy_sec!=null) 
			{q1=lot_moy_sec.getQuantiteTotale();}
		if (lot_bas_sec!=null) 
			{q2=lot_moy_sec.getQuantiteTotale();}
		Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), (q1+q2)*50);
		// cout de stockage
		}
		//===== fin elouan et gab =====	
	}