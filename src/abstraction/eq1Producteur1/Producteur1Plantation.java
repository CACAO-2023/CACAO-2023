package abstraction.eq1Producteur1;


import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
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

	//===== Elouan =====
	//méthode qui s'occupe des catastrophes naturelles qui peuvent apparaitre. elle prendra aussi en compte les grèves
	//on utilise les parametres de l'equipe 3 pour les chances d'apparition comme convenue
	public void next() {
		super.next();
		this.journal_champs.ajouter("===== step : "+step+" =====");
		champ champm = this.getChampMoy();
		champ champb = this.getChampBas();
		this.journal_evenements.ajouter("===== step : "+step+" =====");
		boolean b = true;
		boolean g = false;
		double greve = Math.random();
		if (greve<Filiere.LA_FILIERE.getParametre("Equipe3 Proba Greve").getValeur()) { //les greves peuvent apparaitre assez frequement 
			b = false;
			g = true;
			this.journal_evenements.ajouter(Filiere.LA_FILIERE.getParametre("Equipe3 Pourcentage Greviste").getValeur()*100+"% de notre main d'oeuvre fait grève.");
			this.journal_champs.ajouter(Color.WHITE, Color.RED,"ATTENTION : une grève a lieu"); //Equipe3 Pourcentage Perte Greviste au dessus 
			this.journal_stocks.ajouter(Color.WHITE, Color.RED,"ATTENTION : une grève a lieu");
		}
		double incendieL = Math.random();
		if (incendieL<Filiere.LA_FILIERE.getParametre("Equipe3 Proba Incendi L").getValeur()) { 
			b = false;
			this.journal_champs.ajouter(Color.WHITE, Color.RED,"ATTENTION : un incendie a eu lieu");
			this.journal_evenements.ajouter("Petit incendie : "+Filiere.LA_FILIERE.getParametre("Equipe3 Proportion Champs Brules Incendie L").getValeur()*100+"% des champs sont détruits");
			champm.supprimer(champm.getNbHectare()*Filiere.LA_FILIERE.getParametre("Equipe3 Proportion Champs Brules Incendie L").getValeur());
			champb.supprimer(champb.getNbHectare()*Filiere.LA_FILIERE.getParametre("Equipe3 Proportion Champs Brules Incendie L").getValeur());
		}
		double incendieM = Math.random();
		if (incendieM<Filiere.LA_FILIERE.getParametre("Equipe3 Proba Incendi M").getValeur()) { 
			b = false;
			this.journal_champs.ajouter(Color.WHITE, Color.RED,"ATTENTION : un incendie a eu lieu");
			this.journal_evenements.ajouter("Moyen incendie :"+Filiere.LA_FILIERE.getParametre("Equipe3 Proportion Champs Brules Incendie M").getValeur()*100+"% des champs sont détruits");
			champm.supprimer(champm.getNbHectare()*Filiere.LA_FILIERE.getParametre("Equipe3 Proportion Champs Brules Incendie M").getValeur());
			champb.supprimer(champb.getNbHectare()*Filiere.LA_FILIERE.getParametre("Equipe3 Proportion Champs Brules Incendie M").getValeur());
		}
		double incendieH = Math.random();
		if (incendieH<Filiere.LA_FILIERE.getParametre("Equipe3 Proba Incendi H").getValeur()) { 
			this.journal_champs.ajouter(Color.WHITE, Color.RED,"ATTENTION : un incendie a eu lieu");
			b = false;
			this.journal_evenements.ajouter("Fort incendie :"+Filiere.LA_FILIERE.getParametre("Equipe3 Proportion Champs Brules Incendie H").getValeur()*100+"% des champs sont détruits");
			champm.supprimer(champm.getNbHectare()*Filiere.LA_FILIERE.getParametre("Equipe3 Proportion Champs Brules Incendie H").getValeur());
			champb.supprimer(champb.getNbHectare()*Filiere.LA_FILIERE.getParametre("Equipe3 Proportion Champs Brules Incendie H").getValeur());
		}
		double cyclone = Math.random();
		if (cyclone<Filiere.LA_FILIERE.getParametre("Equipe3 Proba Cyclone").getValeur()) {
			b = false;
			this.journal_champs.ajouter(Color.WHITE, Color.RED,"ATTENTION : un cyclone est passé par nos champs");
			double random = ThreadLocalRandom.current().nextDouble(0.0, Filiere.LA_FILIERE.getParametre("Equipe3 Proportion Champs Detruits Cyclone Max").getValeur());
			this.journal_evenements.ajouter("Cyclone : "+random*100+"% des champs sont détruits");
			champm.supprimer(champm.getNbHectare()*random);
			champb.supprimer(champb.getNbHectare()*random);
		}
		if(b) {
			this.journal_evenements.ajouter("Rien à signaler");
		}
		recolte(g);

	}

	public void recolte(boolean greve) {
		//===== Elouan =====
		//cette methode est notre next V1, mais n'est pas toujours appele (en cas de greve par exemple)
		this.journal_champs.ajouter("---> Qualite : Moy");
		Lot lot_moy = this.getStockMoy();
		Lot lot_moy_sec = this.getVraiStockM();
		HashMap<Integer, Double> quantiteFeveM = lot_moy.getQuantites();
		champ cm = this.getChampMoy();
		if (cm!=null) {
			HashMap<Integer,Double> aretirerM = new HashMap<Integer,Double>();
			Double a = 0.; //utile en cas de grève
			Double max = (1-Filiere.LA_FILIERE.getParametre("Equipe3 Pourcentage Greviste").getValeur())*cm.getNbHectare();
			for (Integer i : cm.getQuantite().keySet()) {
				double q = cm.getQuantite().get(i);
				a = a+q;
				Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), this.coutmaindoeuvre.getValeur()*q);
				if (step-i==2080) { //supprime l'hectar quand il produit plus, au bout de 40 ans pour la v1
					aretirerM.put(i,q); //on les stock dans une liste pour les retirer plus tard, sinon on a une erreur
					Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), this.coutreplantation.getValeur()*q);
					this.journal_champs.ajouter("Un champ de "+q+" hectares a été planté");
				}
				else if ((step-i)%12==0 && step-i>0 && !greve || (greve&&(a<=max))) {
					if (this.Stock.getValeur()*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()<2000000) {
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
					} else {
						this.journal_stocks.ajouter("On ne récolte pas pour ne pas faire exploser notre cout de stockage");
					}
				}
			}

			for (Integer i : aretirerM.keySet()) {
				cm.supprimer(i);
				cm.ajouter(i, aretirerM.get(i));}
		}
		this.journal_champs.ajouter(this.champMoy.toString());
		this.journal_champs.ajouter("Cela fait en tout "+this.champMoy.getNbHectare()+" hectares");
		this.journal_champs.ajouter("---> Qualite : Bas");
		//on retire les feves perimes
		int nb_step_perime = step-12;
		Double fevemoyabas = null;
		if (quantiteFeveM!=null) {
			fevemoyabas = quantiteFeveM.get(nb_step_perime); //quantite de feve qui baisse de gamme, qu'on va rajouter dans le stock de bas
			quantiteFeveM.remove(nb_step_perime);}
		// on s'occupe des feves sechées
		if (quantiteFeveM.get(step-1)!=null) {
			Double nb_feve_sec = quantiteFeveM.get(step-1);
			lot_moy_sec.ajouter(step, nb_feve_sec);
			quantiteFeveM.remove(step-1);}
		//on refait pareil pour les feves de basses qualités
		Lot lot_bas = this.getStockBas();
		HashMap<Integer, Double> quantiteFeveB = lot_bas.getQuantites();
		Lot lot_bas_sec = this.getVraiStockB();
		champ cb = this.getChampBas();
		if (cb!=null) {
			HashMap<Integer,Double> aretirerB = new HashMap<Integer,Double>();
			Double a = 0.; //utile en cas de grève
			Double max = (1-Filiere.LA_FILIERE.getParametre("Equipe3 Pourcentage Greviste").getValeur())*cb.getNbHectare();
			for (Integer i : cb.getQuantite().keySet()) {
				double q = cb.getQuantite().get(i);
				a = a+q;
				Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), q*this.coutmaindoeuvre.getValeur());
				if (step-i%2080==0) { //supprime l'hectar quand il produit plus, au bout de 40 ans pour la v1
					aretirerB.put(i,q); //on les stock dans une liste pour les retirer plus tard, sinon on a une erreur
					Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), q*this.coutreplantation.getValeur());
					this.journal_champs.ajouter("Un champ de "+q+" hectares a été planté");
				}
				else if ((step-i)%10==0 && step-i>0 && !greve || (greve&&(a<=max))) 
				{if (this.Stock.getValeur()*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()<2000000) {
					double nb_tonnes = q*0.56 ; //ajouter facteur random
					double random = ThreadLocalRandom.current().nextDouble(0.9, 1.15);
					nb_tonnes = nb_tonnes * random ;
					stockFeveBas.ajouter(step, nb_tonnes); //recolte

					if (quantiteFeveB.containsKey(step)) {
						quantiteFeveB.replace(step, quantiteFeveB.get(step)+nb_tonnes);
					} else {
						quantiteFeveB.put(step, nb_tonnes);
					}
				}else {
					this.journal_stocks.ajouter("On ne récolte pas pour ne pas faire exploser notre cout de stockage");
				}
				}
			}
			for (Integer i : aretirerB.keySet()) {
				cb.supprimer(i);
				cb.ajouter(i, aretirerB.get(i));}
		}
		this.journal_champs.ajouter(this.champBas.toString());
		this.journal_champs.ajouter("Cela fait en tout "+this.champBas.getNbHectare()+" hectares");
		//on retire les feves perimes
		if (fevemoyabas!=null) {
			quantiteFeveB.put(step-6, fevemoyabas); //on rajoute les feves qui ont baisse de qualite pour qu'elles aient une duree de vie de 3 mois
		}
		quantiteFeveB.remove(nb_step_perime);
		// on s'occupe des feves sechées
		Double nb_feve_sec2 = quantiteFeveB.get(step-1);
		if (nb_feve_sec2!=null) {
			lot_bas_sec.ajouter(step, nb_feve_sec2);}
		quantiteFeveB.remove(step-1);
		// cout de stockage
		Double q1 = 0.0;
		Double q2 = 0.0;
		if (lot_moy_sec!=null) 
		{q1=lot_moy_sec.getQuantiteTotale();}
		if (lot_bas_sec!=null) 
		{q2=lot_bas_sec.getQuantiteTotale();}
		Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), (q1+q2)*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		this.journal_stocks.ajouter("Cout de stockage bas de gamme : "+q2*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		this.journal_stocks.ajouter("Cout de stockage moyenne gamme : "+q1*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		//on replante les champs détruits pour toujours rester autour de 250kHa pour notre production de masse
		if (cm.getNbHectare()<50000) {
			cm.ajouter(step, 50000-cm.getNbHectare());
		}
		if (cb.getNbHectare()<200000) {
			cb.ajouter(step, 200000-cb.getNbHectare());
		}
	}

	//====== gab : calcul du coût de revient pour une qtté donnée d'un produit 

	//méthode qui renvoie le coût de revient d'un stock de fève avant de le vendre
	//feve chgt qualité?
	public double calculCoutRevient(IProduit produit, double quantite, int step_recolte) {
		double cout_revient ;

		//prend en compte la plantation, la main d'oeuvre, le stockage

		if (produit==Feve.F_BQ) {
			double nb_hectares_BQ = quantite/(1.025*0.56) ; //moyenne du facteur aléatoire (pas possible de connaître exactement le nombre d'hectares)
			cout_revient = nb_hectares_BQ*Filiere.LA_FILIERE.getParametre("Cout de replantation").getValeur()/(2080*10) //cout replantation/(durée replantation*nb step pousse)
					+ nb_hectares_BQ * 30 * 10 //hectare*cout main doeuvre*nb step pousse
					+ quantite*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()* (step-step_recolte) ;
		} else {
			double nb_hectares_MQ = quantite/(0.95*0.56); //moyenne du facteur aléatoire (pas possible de connaître exactement le nombre d'hectares)
			cout_revient = nb_hectares_MQ*Filiere.LA_FILIERE.getParametre("Cout de replantation").getValeur()/(2080*12) //cout replantation/(durée replantation*nb step pousse)
					+ nb_hectares_MQ * 30 * 12 //hectare*cout main doeuvre*nb step pousse
					+ quantite*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()* (step-step_recolte) ;
		}	
		return cout_revient ;

	}

	//méthode qui permet d'obtenir le stock dont on veut calculer le coût de revient
	public Lot fevesConsiderees(IProduit produit, double quantite) {
		Lot stockFeve ;
		if (produit==Feve.F_BQ) {
			stockFeve = stockFeveBas ;
		} else {
			stockFeve=stockFeveMoy ;
		}


		Lot lotVente=new Lot(produit);
		if (quantite<=0) { 
			throw new IllegalArgumentException("Appel de retirer une qtté <=0");
		} if (quantite>stockFeve.getQuantiteTotale()+0.001){ //si on a pas assez de stock, on considère que l'on crée tout ce qui manque au step d'après pour prendre en compte ces coûts
			double quantite_a_produire = quantite-stockFeve.getQuantiteTotale() ;
			lotVente = fevesConsiderees(produit, stockFeve.getQuantiteTotale()) ;
			// même si tt est pas prêt au step d'après on livre au fur et à mesure donc pas d'incidence sur les coûts de stockage
			lotVente.ajouter(step, quantite_a_produire) ;

		} else {
			double reste = quantite;
			for (Integer i : stockFeve.getQuantites().keySet()) {
				if (reste>0) {
					if (stockFeve.getQuantites().get(i)>reste) {
						lotVente.ajouter(i,reste);
						reste=0;
					} else {
						lotVente.ajouter(i,stockFeve.getQuantites().get(i));
						reste = reste - stockFeve.getQuantites().get(i);
					}
				}
			}
		}
		return lotVente ;

	}

	//méthode qui renvoie le coût de revient de la production d'une certaine quantité pour un produit donné 
	//(on souhaite vendre les fèves les plus vieilles)
	public double coutRevientQuantite(IProduit produit, double quantite) {
		Lot lotVente = fevesConsiderees(produit, quantite) ;
		double cout_revient_total = 0;

		for (Integer j : lotVente.getQuantites().keySet()) {
			cout_revient_total += calculCoutRevient(produit, lotVente.getQuantites().get(j), j) ;
		}

		return cout_revient_total ;

	}

	// calcul du coût de revient de la production d'une certaine qqté pour un produit donné avec une marge de 10% pour la vente
	public double prixMinAvecMarge(IProduit produit, double quantite) {
		return coutRevientQuantite(produit, quantite)*1.10 ;
	}





	//===== fin gab

}