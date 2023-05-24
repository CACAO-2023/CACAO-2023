package abstraction.eq7Distributeur1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

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
	private TreeMap<Double,ChocolatDeMarque> dict_ratio; //On utilise une TreeMap pour ranger les elements dans l'ordre
	
	public Distributeur1() {
		super();
	}
	
	/**
	 * @author Theo
	 */
	public void initialiser() {
		this.dict_ratio = new TreeMap<Double,ChocolatDeMarque>();
		super.initialiser();
	}
	
	public void next() {
		super.next();
		int etape = Filiere.LA_FILIERE.getEtape();
		journal.ajouter("============================== étape "+etape+" ==============================");
		journal_achat.ajouter("============================== étape "+etape+" ==============================");
		journal_stock.ajouter("============================== étape "+etape+" ==============================");
		journal_vente.ajouter("============================== étape "+etape+" ==============================");

		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			mettre_a_jour(Var_Cout_Choco, marque, getCoutTotal(marque));
			mettre_a_jour(Var_Marge_Choco, marque, prix(marque)-get_valeur(Var_Cout_Choco,marque));

		}
		
		//Prise en compte des couts de main doeuvre
		qte_totale_en_vente = 0.;
		for (ChocolatDeMarque choco : Filiere.LA_FILIERE.getChocolatsProduits()) {
			qte_totale_en_vente += quantiteEnVente(choco,cryptogramme);
		}
		Double cout_total_mise_en_rayon = qte_totale_en_vente * Filiere.LA_FILIERE.getParametre("cout mise en rayon").getValeur();
		if (cout_total_mise_en_rayon > 0) {
			Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme,Filiere.LA_FILIERE.getBanque(),cout_total_mise_en_rayon );	
			journal_vente.ajouter("Cout de main d'oeuvre : "+cout_total_mise_en_rayon);
				}
		
		
	}
	

	private double getTotalVentes() {
		double total = 0.;
		for (ChocolatDeMarque choco : Filiere.LA_FILIERE.getChocolatsProduits()) {
			total += quantiteEnVente(choco,cryptogramme);
		}
		return total;
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
	 */
	protected void updateRatioTG(){
		
		int etape = Filiere.LA_FILIERE.getEtape()%24;
		TreeMap<Double,ChocolatDeMarque> map = new TreeMap<Double,ChocolatDeMarque>();
		for (ChocolatDeMarque choco : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (get_valeur(Var_Stock_choco, choco) != 0) { //Inutile d'ajouter qqch qu'on ne vendra pas car pas de stock
				map.put(previsionsperso.get(etape).get(choco)/get_valeur(Var_Stock_choco, choco), choco);
			}
		}
		this.dict_ratio = map;
	}
	
	/**
	 * @author Theo
	 * @return la liste des chocolats de marque a mettre en tete de gondole, avec les quantites associees
	 */
	protected HashMap<ChocolatDeMarque,Double> stratTG() {
		updateRatioTG();
		List<Double> liste = new ArrayList<Double>(dict_ratio.keySet());
		List<ChocolatDeMarque> listemarque = new ArrayList<ChocolatDeMarque>();
		HashMap<ChocolatDeMarque,Double> listefinale = new HashMap<ChocolatDeMarque,Double>();
		for (Double ratio : liste) {
			listemarque.add(dict_ratio.get(ratio)); //ne contient que des marques pour lesquelles on a du stock
		}
		double totalqtevente = getTotalVentes();
		double seuil = Filiere.SEUIL_EN_TETE_DE_GONDOLE_POUR_IMPACT;
		double qtemaxtg = totalqtevente/10; //total TG = 10% du total ventes max
		int j = 0;
		while (j<listemarque.size()) {
			double qteTG = qtemaxtg*quantiteEnVente(listemarque.get(j),cryptogramme)/totalqtevente; //proportionnel a la part du produit dans nos rayons
			if (qteTG>=seuil) {
				listefinale.put(listemarque.get(j), qteTG);
			}
			j+=1;
		}
		return listefinale;
	}
	
	/**
	 * @author Theo, Ahmed
	 * @param choco, choco!=null
	 * @return Le prix de vente actuel d'une tonne de chocolat choco
	 * IMPORTANT : durant une meme etape, la fonction doit toujours retourner la meme valeur pour un chocolat donne.
	 */
	public double prix(ChocolatDeMarque choco) {
		double qualite = choco.qualitePercue();

		if (qualite <1) {
			qualite = 1.0; }
//		double coef = 1-(((10/3)*qualite)/100)+0.1;
		double promo = prixPromotion(choco);
		double cout = getCoutTotal(choco);
		double prix = cout*promo/(1-0.1*qualite);
		double prix_moyen = 0.;
		if (Filiere.LA_FILIERE.getEtape() >= 24) {
			prix_moyen = Filiere.LA_FILIERE.prixMoyen(choco,Filiere.LA_FILIERE.getEtape()-24);
		}
		if (((prix < prix_moyen*0.9) || (prix > prix_moyen*1.1)) && (prix_moyen != 0.)) {
			prix = prix - (prix-prix_moyen)*0.5;	
			}
		if (prix < cout/0.9) {
			return cout/0.9;
		}
		return prix;
	}
	
	/**
	 * @author Theo	 
	 * @return Le coeff de promo : une fois toutes les 3 etapes, on applique une promotion
	 */
	public double prixPromotion(ChocolatDeMarque choco) { 
		if (((Filiere.LA_FILIERE.getEtape()%3)==0)&&(choco.getChocolat()!=Chocolat.C_BQ)) {
			return 0.95;
		}
		else {
			return 1;
		}
	}
	
	/**
	 * @author Theo, Ahmed
	 * @param choco
	 * @return le cout de revient d'1t de chocolat de marque, calcule grace au type de chocolat
	 */
	public double getCoutTotal(ChocolatDeMarque choco) {
		Double cout_i = get_valeur(Var_Cout_Choco, choco);
		Double cout_s = cout_stockage_distributeur.getValeur();
		Double cout_m = Filiere.LA_FILIERE.getParametre("cout mise en rayon").getValeur(); //Cout de mise en rayon d'1T de chocolat
		return (cout_i+cout_s+cout_m);
	}
	
	/**
	 * @author Theo
	 * @param choco, choco!=null
	 * @return Retourne la quantite totale (rayon+tete de gondole) en tonne de chocolat de type choco 
	 * actuellement disponible a la vente (pour un achat immediat --> le distributeur a 
	 * au moins cette quantite en stock)
	 */
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		int etape = Filiere.LA_FILIERE.getEtape()%24;
		Double previsions = previsionsperso.get(etape).get(choco);
		double stock_choco = this.get_valeur(Var_Stock_choco, choco);
		if (stock_choco < previsions*1.5) {
			return stock_choco;
		}
		if (stock_choco >= previsions*1.5) {
			return previsions*1.5;
		}
		return stock_choco;
		
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
	 */
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {

		
		HashMap<ChocolatDeMarque,Double> listeTG = stratTG();
		if (listeTG.containsKey(choco)){
			return listeTG.get(choco);
		}
		return 0.;
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
		mettre_a_jour(Var_Stock_choco, choco, get_valeur(Var_Stock_choco, choco)-quantite);
		totalStocks.setValeur(this, totalStocks.getValeur(cryptogramme)-quantite, cryptogramme);
		
		this.journal_vente.ajouter(COLOR_GREEN, COLOR_BROWN, "ca nous coute "+ get_valeur(Var_Cout_Choco, choco));
		this.journal_vente.ajouter("Eq7 a vendu "+ (int)Math.floor(quantite)+" T de "+choco+ " aux clients finaux pour un prix à la tonne de "+ (int)Math.floor(montant/quantite));
		//Actualisation des previsions persos
		actualiser_prevision_perso( choco,   quantite);
		
		mettre_a_jour(Var_Vente_Choco, choco, montant);
		mettre_a_jour(Var_nbr_Vente_Choco, choco, quantite);
		

		vente_step+=montant;
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
	
