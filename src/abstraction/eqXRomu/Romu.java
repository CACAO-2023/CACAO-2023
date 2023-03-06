package abstraction.eqXRomu;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class Romu extends RomuATVBABVAOAAOACCVCCDistributeur//RomuATVBABVAOAAOACCVendeurCC 
implements  IAcheteurContratCadre  {

	public void next() {
		super.next();
		// Afficher les ventes
		for (ChocolatDeMarque c : Filiere.LA_FILIERE.getChocolatsProduits()) {
			String s =c+" -> ";
			for (int etape=0; etape<=Filiere.LA_FILIERE.getEtape(); etape++) {
				s+=Filiere.LA_FILIERE.getVentes(c, etape)+" ";
			}
			this.journal.ajouter(s);
		}
	}
}
