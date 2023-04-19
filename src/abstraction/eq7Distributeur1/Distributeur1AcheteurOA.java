package abstraction.eq7Distributeur1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.offresAchat.IAcheteurOA;
import abstraction.eqXRomu.offresAchat.PropositionVenteOA;
import abstraction.eqXRomu.offresAchat.SuperviseurVentesOA;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;

/**
 * Classe permettant d'acheter par appel d'offre
 * @author Theo
 *
 */

public class Distributeur1AcheteurOA extends DistributeurContratCadreAcheteur implements IAcheteurOA {

	private SuperviseurVentesOA supOA;

	public Distributeur1AcheteurOA() {
		super();
	}
	
	public void initialiser() {
		super.initialiser();
	}

//	@Override
	public PropositionVenteOA choisirPV(List<PropositionVenteOA> propositions) { //Choisit la meilleure proposition
		PropositionVenteOA best = propositions.get(0);
		double critere = propositions.get(0).getPrixT()*propositions.get(0).getPrixT()/propositions.get(0).getChocolatDeMarque().qualitePercue();
		for (PropositionVenteOA p : propositions) {
			if (p.getPrixT()*p.getPrixT()/p.getChocolatDeMarque().qualitePercue() < critere) { //Critere ameliorable
				best = p;
			}
		}
		return best;
	}
	
	private Boolean besoin() { //Besoin ou non d'un appel d'offre
		int etapesuiv = (Filiere.LA_FILIERE.getEtape()+1)%24;
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (stockChocoMarque.get(marque) < previsionsperso(marque,etapesuiv)) { //On achete seulement si on prevoit de vendre plus que ce qu'on a
				return true;
			}
		}
		return false;
	}
	
	
	private HashMap<ChocolatDeMarque,Double> besoinQte() { //Quelle qte a-t-on besoin
		int etapesuiv = (Filiere.LA_FILIERE.getEtape()+1)%24;
		HashMap<ChocolatDeMarque,Double> qte = new HashMap<ChocolatDeMarque,Double>();
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (stockChocoMarque.get(marque)+getLivraisonEtape(marque) < previsionsperso(marque,etapesuiv)) {
				qte.put(marque,2*(previsionsperso(marque,etapesuiv)-(stockChocoMarque.get(marque))+getLivraisonEtape(marque)));
			}
		}
		return qte;
	}
	
	private List<ChocolatDeMarque> besoinMarque() { //De quelle marque avons-nous besoin
		int etapesuiv = (Filiere.LA_FILIERE.getEtape()+1)%24;
		List<ChocolatDeMarque> liste = new ArrayList<ChocolatDeMarque>();
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (stockChocoMarque.get(marque)+getLivraisonEtape(marque) < previsionsperso(marque,etapesuiv)) {
				liste.add(marque);
			}
		}
		return liste;
	}
	
	public void next() {
		super.next();
		journal.ajouter("AcheteurOA");
		if (supOA==null) {
			supOA =(SuperviseurVentesOA)(Filiere.LA_FILIERE.getActeur("Sup.OA"));
		}
		if (besoin()==true) { //Si on manque à l'instant t de stock, on lance cette méthode d'achat car l'effet est immediat
			List<ChocolatDeMarque> marque = besoinMarque();
			HashMap<ChocolatDeMarque,Double> qte = besoinQte();
			for (ChocolatDeMarque m : marque) {
				if (qte.get(m) > 2.0) { //On ne peut pas lancer d'appel d'offre de moins de 2 tonnes
					PropositionVenteOA pRetenue = supOA.acheterParAO(this, cryptogramme,m.getChocolat(), m.getMarque(), qte.get(m), false); //acteur,crypto,choco,marque,qté,TG
					if (pRetenue!=null) { //Update des paramètres etc
						double nouveauStock = pRetenue.getOffre().getQuantiteT();
						if (this.stockChocoMarque.keySet().contains(pRetenue.getChocolatDeMarque())) {
							nouveauStock+=this.stockChocoMarque.get(pRetenue.getChocolatDeMarque());
						}
						stockChocoMarque.replace(pRetenue.getChocolatDeMarque(), nouveauStock);
						journal.ajouter("   Achat par offre d'achat de "+pRetenue+" --> quantite en stock = "+nouveauStock);

		////////////////////////////////////////////////				
						//						couts(m,pRetenue.getPrixT()/pRetenue.getOffre().getQuantiteT());
		//je ne sais pas si cest pertinent d'enregistrer le cout d'une OA vu qu'elle coute cher de base
						////////////////////////////////////////////////////////////////////
					}
				}
				
			}
			
		}
		
		
	}
}
