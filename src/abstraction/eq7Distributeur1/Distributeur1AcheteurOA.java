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
			if (p.getPrixT()*p.getPrixT()/p.getChocolatDeMarque().qualitePercue() < critere) { //Critere
				critere = p.getPrixT()*p.getPrixT()/p.getChocolatDeMarque().qualitePercue();
				best = p;
			}
		}
		return best;
	}
	
	private Boolean besoin() { //Besoin ou non d'un appel d'offre
		int etapesuiv = (Filiere.LA_FILIERE.getEtape()+1)%24;
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (get_valeur(Var_Stock_choco, marque) + getLivraisonEtape(marque,etapesuiv) < getPrevisionsperso(marque,etapesuiv)) { //On achete seulement si on prevoit de vendre plus que ce qu'on a
				return true;
			}
		}
		return false;
	}
	
	
	private HashMap<ChocolatDeMarque,Double> besoinQte() { //Quelle qte a-t-on besoin
		int etapesuiv = (Filiere.LA_FILIERE.getEtape()+1)%24;
		HashMap<ChocolatDeMarque,Double> qte = new HashMap<ChocolatDeMarque,Double>();
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			double test = getPrevisionsperso(marque,etapesuiv) - (get_valeur(Var_Stock_choco, marque))+getLivraisonEtape(marque,etapesuiv);
			if ((test > 0) && (1.5*test < 15000)) {
				qte.put(marque,1.5*test);
			}
			else {
				qte.put(marque, 15000.);
			}
		}
		return qte;
	}
	
	private List<ChocolatDeMarque> besoinMarque() { //De quelle marque avons-nous besoin
		int etapesuiv = (Filiere.LA_FILIERE.getEtape()+1)%24;
		List<ChocolatDeMarque> liste = new ArrayList<ChocolatDeMarque>();
		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (get_valeur(Var_Stock_choco, marque)+getLivraisonEtape(marque,Filiere.LA_FILIERE.getEtape()) < getPrevisionsperso(marque,etapesuiv)) {
				liste.add(marque);
			}
		}
		return liste;
	}
	
	public void next() {
		super.next();
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
						if (Var_Stock_choco.keySet().contains(pRetenue.getChocolatDeMarque())) {
							nouveauStock+=get_valeur(Var_Stock_choco, (pRetenue.getChocolatDeMarque()));
						}
						mettre_a_jour(Var_Stock_choco, pRetenue.getChocolatDeMarque(), nouveauStock);
						journal_achat.ajouter("Achat par offre d'achat de "+pRetenue+" --> quantite en stock = "+nouveauStock);
						//je ne sais pas si cest pertinent d'enregistrer le cout d'une OA vu qu'elle coute cher de base
					}
				}
				
			}
			
		}
		
		
	}
}
