package abstraction.eq1Producteur1;


import java.util.HashMap;
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

	
	public void next() {
		//===== début Elouan =====
		super.next();
		this.journal_champs.ajouter("===== step : "+step+" =====");
		this.journal_champs.ajouter("---> Qualite : Moy");
		Lot lot_moy = this.getStockMoy();
		Lot lot_moy_sec = this.getVraiStockM();
		HashMap<Integer, Double> quantiteFeveM = lot_moy.getQuantites();
		champ cm = this.getChampMoy();
		if (cm!=null) {
			for (Integer i : cm.getQuantite().keySet()) {
				double q = cm.getQuantite().get(i);
				Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), this.coutmaindoeuvre.getValeur()*q);
				if (step-i%2080==0) { //supprime l'hectar quand il produit plus, au bout de 40 ans pour la v1
					cm.supprimer(i);
					cm.ajouter(step, q); //on le replante quand il périme : v2 à améliorer
					Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), this.coutreplantation.getValeur()*q);
					this.journal_champs.ajouter("Un champ de "+q+" hectares a été planté");
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
		Lot lot_bas = this.getStockBas();
		HashMap<Integer, Double> quantiteFeveB = lot_bas.getQuantites();
		Lot lot_bas_sec = this.getVraiStockB();
		champ cb = this.getChampBas();
		if (cb!=null) {
			for (Integer i : cb.getQuantite().keySet()) {
				double q = cb.getQuantite().get(i);
				Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), q*this.coutmaindoeuvre.getValeur());
				if (step-i%2080==0) { //supprime l'hectar quand il produit plus, au bout de 40 ans pour la v1
					cb.supprimer(i);
					cb.ajouter(step, q); //on le replante quand il périme : v2 à améliorer
					Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), q*this.coutreplantation.getValeur());
					this.journal_champs.ajouter("Un champ de "+q+" hectares a été planté");
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
					}
				}
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
			{q2=lot_moy_sec.getQuantiteTotale();}

		Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ1"), cryptogramme, Filiere.LA_FILIERE.getActeur("Banque"), (q1+q2)*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		this.journal_stocks.ajouter("Cout de stockage bas de gamme : "+q2*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		this.journal_stocks.ajouter("Cout de stockage moyenne gamme : "+q1*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		}
		//===== fin elouan et gab =====	
	
	
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
					+ quantite*Filiere.LA_FILIERE.getParametre("cout moyen stockage").getValeur()* (step-step_recolte) ;
		} else {
				double nb_hectares_MQ = quantite/(0.95*0.56); //moyenne du facteur aléatoire (pas possible de connaître exactement le nombre d'hectares)
				cout_revient = nb_hectares_MQ*Filiere.LA_FILIERE.getParametre("Cout de replantation").getValeur()/(2080*12) //cout replantation/(durée replantation*nb step pousse)
						+ nb_hectares_MQ * 30 * 12 //hectare*cout main doeuvre*nb step pousse
						+ quantite*Filiere.LA_FILIERE.getParametre("cout moyen stockage").getValeur()* (step-step_recolte) ;
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