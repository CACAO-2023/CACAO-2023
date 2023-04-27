package abstraction.eq7Distributeur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class DistributeurContratCadreAcheteur extends Distributeur1Stock implements IAcheteurContratCadre{
	protected List<ExemplaireContratCadre> mesContratEnTantQuAcheteur;
	protected List<ExemplaireContratCadre> historique_de_mes_contrats;
	protected SuperviseurVentesContratCadre superviseurVentesCC;
	private List<Object> negociations = new ArrayList<>();
	private double minNego=5;
	protected List<Integer> durees_CC = new ArrayList<>(); 
	
	
	public void initialiser() {
		super.initialiser();

		this.superviseurVentesCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}
	
	public void set_duree(){
		durees_CC.add(24); //12 mois = 1an
		durees_CC.add(18); //9 mois
		durees_CC.add(12); //6 mois
		durees_CC.add(6); //3 mois
	}
	
	public DistributeurContratCadreAcheteur() {
		super();
		this.mesContratEnTantQuAcheteur=new LinkedList<ExemplaireContratCadre>();
	}

	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (Math.random()<0.3) {
			return contrat.getEcheancier(); // on ne cherche pas a negocier sur le previsionnel de livraison
		} else {//dans 70% des cas on fait une contreproposition pour l'echeancier
			Echeancier e = contrat.getEcheancier();
			int stepdebut = e.getStepDebut();
			for (int step = stepdebut; step < e.getStepFin()+1; step++) {
				e.set(step, e.getQuantite(step)*0.9);
			}
			return e;
		}
	}

	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		ChocolatDeMarque marque = (ChocolatDeMarque) contrat.getProduit();
		if (nombre_achats.get(marque)==0) {
			return contrat.getPrix();
		}
		else {
			if (contrat.getPrix()<0.5*getCout(marque)) {
				return 0.;
			}
			else {
				
		if (Math.random()<0.3) {
			return contrat.getPrix(); // on ne cherche pas a negocier dans 30% des cas
		} else {//dans 70% des cas on fait une contreproposition differente
			return contrat.getPrix()*0.95;// 5% de moins.
		}}}
	}
	
	
    /**
     * 	enleve les contrats obsolete (nous pourrions vouloir les conserver pour "archive"...)
     * @author Ghaly sentissi
     */

	public void enleve_contrats_obsolete() {
		List<ExemplaireContratCadre> contratsObsoletes=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.mesContratEnTantQuAcheteur) {
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				contratsObsoletes.add(contrat);
			}
		}
		this.mesContratEnTantQuAcheteur.removeAll(contratsObsoletes);
	}

	/**   
	 * proposition d'un contrat a un des vendeurs choisi aleatoirement
	 * @param produit le produit qu'on veut vendre
	 * @return le contrat s'il existe, sinon null
     * @author Ghaly sentissi
     */
	public ExemplaireContratCadre getContrat(IProduit produit,Echeancier e) {
		this.journal_achat.ajouter("Recherche de vendeur CC pour le produit : " + produit + "...");
		List<IVendeurContratCadre> vendeurs = superviseurVentesCC.getVendeurs(produit);
		ExemplaireContratCadre cc = null;
		
		//On parcourt tous les vendeurs aleatoirement
		while (!vendeurs.isEmpty() && cc == null) {
			IVendeurContratCadre vendeur = null;
			if (vendeurs.size()==1) {
				vendeur=vendeurs.get(0);
			} 
			else if (vendeurs.size()>1) {
				vendeur = vendeurs.get((int)( Math.random()*vendeurs.size()));
			}
			vendeurs.remove(vendeur);
			
			if (vendeur!=null) {
				this.journal_achat.ajouter("Tentative de négociation de contrat cadre avec "+vendeur.getNom()+" pour "+produit);
				cc = superviseurVentesCC.demandeAcheteur((IAcheteurContratCadre)this, (IVendeurContratCadre) vendeur, produit, e, cryptogramme,false);
				
				if (cc != null) { //si le contrat est signé 
			        this.journal_achat.ajouter(Color.GREEN, Color.BLACK,"Contrat cadre passé avec "+vendeur.getNom()+" pour "+produit+"\nDétails : "+cc+"!");     
			        mesContratEnTantQuAcheteur.add(cc);
			    } 
				else { //si le contrat est un echec
			        this.journal_achat.ajouter(Color.RED, Color.BLACK,"Echec de la négociation de contrat cadre avec "+vendeur.getNom()+" pour "+produit+"...");
			    }
			}
		if (cc ==null) {
			journal.ajouter("On a cherché à établir un contrat cadre de durée "+e.getNbEcheances()+ " mais on a pas trouvé de vendeur");
		}
	}
		return cc;

		}

	/**
	 * @author Theo
	 * @return la qte d'un produit devant se faire livrer dans l'annee prochaine (en supposant que la duree d'un CC <= 1 an
	 */
	public double getLivraison(IProduit produit) {
		double somme = 0;
		for (ExemplaireContratCadre contrat : mesContratEnTantQuAcheteur) {
			if (contrat.getProduit() == produit) {
				somme += contrat.getQuantiteRestantALivrer();
			}
		}
		return somme;
	}
	
	/**
	 * @return la qte totale livree à ce tour
	 */
	public double getLivraisonEtape(IProduit produit) {
		double somme = 0;
		for (ExemplaireContratCadre contrat : mesContratEnTantQuAcheteur) {
			if (contrat.getProduit() == produit) {
				somme += contrat.getQuantiteALivrerAuStep();
			}
		}
		return somme;
	}

	/**
	 * est appelée pour savoir si on a besoin d'un contrat cadre sur la durée d
	 * On lance un CC seulement si notre stock n'est pas suffisant sur la durée qui suit
	 * il faut faire attention à ce que un CC > 100T
	 * @param d : nombre d'étapes 
	 * @author Ghaly & Theo
	 */
	public boolean besoin_de_CC (int d,ChocolatDeMarque marque) {  
			double previsionannee = 0;
			int step= Filiere.LA_FILIERE.getEtape();
			for (int numetape = step+1; numetape < step+d ; numetape++ ) {
				previsionannee += previsionsperso.get(numetape%24).get(marque);
				}
			return (previsionannee > stockChocoMarque.get(marque)+getLivraison(marque)+ quantite_min_cc);
	};
	/**
	 * est appelée pour savoir si de combien on a besoin sur la durée d
	 * @param d : nombre d'étapes 
	 * @author Ghaly
	 */
	public double quantite_besoin_cc (int d,ChocolatDeMarque marque) {  
			double previsionannee = 0;
			int etape = Filiere.LA_FILIERE.getEtape();
			for (int numetape = etape+1; numetape < etape+d ; numetape++ ) {
				previsionannee += previsionsperso.get(numetape%24).get(marque);
				}
			return previsionannee - stockChocoMarque.get(marque)-getLivraison(marque);
	};

	/**
	 * @author Theo
	 * @param stepDebut : debut de livraison
	 * @param d : nbr_etape
	 * @return echeancier sur d etapes, base sur les previsions de ventes
	 */
	//A COMPLETER POUR PRENDRE EN COMPTE VRAIES PREVISIONS PERSO
	public Echeancier echeancier_strat(int stepDebut, int d, ChocolatDeMarque marque) {
		Echeancier e = new Echeancier(stepDebut);
		for (int etape = stepDebut+1; etape<stepDebut+d; etape++) {
			int etapemod = etape%24;
			e.ajouter(previsionsperso.get(etapemod).get(marque)*1.5);
		}
		return e;
	}
	
	/**
	 * @author Ghaly & Theo
	 */
	public void next() {

		super.next();
		enleve_contrats_obsolete();

		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
//			for (Integer d : durees_CC) {
			int d =24;

			if(besoin_de_CC ( d,marque)) {	//On va regarder si on a besoin d'un nouveau contrat cadre pour chaque marque
							
				//Echeancier echeancier = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, d, quantite_besoin_cc(d, marque)/d);
				Echeancier echeancier = echeancier_strat(Filiere.LA_FILIERE.getEtape()+1,d,marque);
				ExemplaireContratCadre cc = getContrat(marque,echeancier);
				if (cc!=null) {
					nombre_achats.put(marque, nombre_achats.get(marque)+1);
					actualise_cout (cc.getPrix()/cc.getQuantiteTotale());  
					break;

				}
				}
			};  		
		}
		
	
	@Override
	/**
	 * @author Theo
	 */
	// A COMPLETER SI ASSEZ DE STOCK (appele si cc initie par vendeur)
	public boolean achete(IProduit produit) {
		if ((produit instanceof ChocolatDeMarque) && (besoin_de_CC (24,(ChocolatDeMarque)produit))) {
			return true;
		}
		return false;
	}
    /**
     * retourne l'étape de négociation en cours
     * @param contrat     
     * @author Ghaly sentissi
     */
	public Integer step_nego (ExemplaireContratCadre contrat) {
		return contrat.getListePrix().size()/2;
	}
	
//	public double contrePropositionPrixAcheteur_negociations(ExemplaireContratCadre contrat) {
//		int step_nego = step_nego ( contrat);
//		if (step_nego<minNego) {
//			return contrat.getPrix()*0.95
//		}
			
			
    /**
  
     * @author Ghaly sentissi
     */
	public void best_prix(IProduit produit, ExemplaireContratCadre contrat) {
		
		List<Object> list = new ArrayList<>();
		double prix_au_min_nego = contrat.getListePrix().get(2*(int)(minNego)-1);
		if (list.isEmpty()) {
			list.add(contrat.getVendeur());
			list.add(prix_au_min_nego);
			list.add(minNego);			
		}
		else {
			
//			if (Double.compare((double)(list.get(1)), prix_au_min_nego)) {
//				list.removeAll(list);
//				list.add(contrat.getVendeur());
//				list.add(prix_au_min_nego);
//				list.add(step_nego(contrat));		
//			}
	
		}
	}
//	public String meilleur_prix(Echeancier e,IProduit produit) {
//		HashMap<IActeur, Double> res= new HashMap<>();
//		int minNego=5;
//		
//		for (IActeur acteur : Filiere.LA_FILIERE.getActeurs()) {
//			if (acteur!=this && acteur instanceof IVendeurContratCadre && ((IVendeurContratCadre)acteur).vend(produit)) {
//				ExemplaireContratCadre cc = superviseurVentesCC.demandeAcheteur((IAcheteurContratCadre)this, (IVendeurContratCadre) acteur, produit,e, cryptogramme,false);
//			}}
//		
//		return "ok";
//	}
	
//	public static Integer obtenirValeurMinimale(HashMap<String, Integer> hashMap) {
//        Integer valeurMinimale = null;
//        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
//            Integer valeur = entry.getValue();
//            if (valeurMinimale == null || valeur < valeurMinimale) {
//                valeurMinimale = valeur;
//            }
//        }
//        return valeurMinimale;	}
	
	
	@Override
	/**
	 * @author Theo
	 * Actions necessaires pour actualiser/annoncer
	 */
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		IProduit produit= lot.getProduit();
		double quantite = lot.getQuantiteTotale();
		if (produit instanceof ChocolatDeMarque) {
			if (this.stockChocoMarque.keySet().contains(produit)) {
				this.stockChocoMarque.put((ChocolatDeMarque)produit, this.stockChocoMarque.get(produit)+quantite);
			} else {
				this.stockChocoMarque.put((ChocolatDeMarque)produit, quantite);
			}
			this.totalStocks.ajouter(this, quantite, this.cryptogramme);
			this.journal.ajouter("Reception de "+quantite+" T de "+produit+". Stock->  "+this.stockChocoMarque.get(produit));
		}
	}

	public String toString() {
		return this.getNom();
	}

	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		return 5;
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		if (nombre_achats.get((ChocolatDeMarque)(contrat.getProduit()))==0) {
			journal.ajouter(Color.GREEN, Color.BLACK,"Les negociations avec "+ contrat.getVendeur().getNom()+" ont abouti à un contrat cadre de "+contrat.getProduit().toString()+" à un prix de "+ contrat.getPrix());
			journal_achat.ajouter(Color.GREEN, Color.BLACK,"Les negociations avec "+ contrat.getVendeur().getNom()+" ont abouti à un contrat cadre de "+contrat.getProduit().toString()+" à un prix de "+ contrat.getPrix());

		}
		else {
			journal.ajouter(Color.RED, Color.BLACK,"Les negociations avec "+ contrat.getVendeur().getNom()+" ont abouti à un contrat cadre de "+contrat.getProduit().toString()+" à un prix de "+ contrat.getPrix()*100 /couts.get(contrat.getProduit())+ "%");
			journal_achat.ajouter(Color.RED, Color.BLACK,"Les negociations avec "+ contrat.getVendeur().getNom()+" ont abouti à un contrat cadre de "+contrat.getProduit().toString()+" à un prix de "+ contrat.getPrix());

		}
		
	}






}
