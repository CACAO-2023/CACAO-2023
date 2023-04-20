package abstraction.eq7Distributeur1;

import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class brouillon_indicateurs {
//	protected HashMap<ChocolatDeMarque,VariablePrivee>  indicateurs = new HashMap<ChocolatDeMarque,VariablePrivee>() ;
//
//	/**
//	 * crée les indicateurs pour chaque chocolat de marque
//	 * @author Ghaly
//	 */
//	public void cree_indicateurs(){
//		
//	    List<ChocolatDeMarque> liste_marques = Filiere.LA_FILIERE.getChocolatsProduits();
//	    for (int i = 0; i < liste_marques.size(); i++) {
//	    	ChocolatDeMarque marque = liste_marques.get(i);
//	        VariablePrivee x= new VariablePrivee("Equ7_"+marque.getNom(), "Stock total de "+ marque.getChocolat(), this, 0); // appel de la méthode getNom() sur l'objet ChocolatDeMarque
//	        x.setValeur(this,stockChocoMarque.get(marque));
//	        indicateurs.put(marque,x);
//	    }
//	}
//	/**
//	 * @author ghaly
//	 * actualise les indicateurs de stocks de chaque chocolat de marque
//	 */
//	private void actualise_indic_chocolat_de_marque() {
//		for (ChocolatDeMarque marque : Filiere.LA_FILIERE.getChocolatsProduits()) {
//			VariablePrivee x = indicateurs.get(marque);
//			x.setValeur(this, stockChocoMarque.get(marque));
//			indicateurs.replace(marque, x);
//		}
//	}
//	cree_indicateurs();
//	Set<ChocolatDeMarque> keys = indicateurs.keySet();
//    for (ChocolatDeMarque marque : keys) {
//		res.add(indicateurs.get(marque));
//	}
}
