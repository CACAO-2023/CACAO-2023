package abstraction.eq4Transformateur1;

import java.util.LinkedList;

import java.util.List;

import abstraction.eq4Transformateur1.Vente.AODistributeur;
import abstraction.eq4Transformateur1.Vente.VendeurOA;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

/**
 * @author Francois, Alexian 
 *
 */

public class Transformateur1 extends VendeurOA implements IFabricantChocolatDeMarque{
	
	private List<ChocolatDeMarque>chocosProduits;

	public Transformateur1() {
		super();
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
	}

	public List<ChocolatDeMarque> getChocolatsProduits() {
		if (this.chocosProduits.size()==0) {
				Chocolat c = Chocolat.C_HQ_BE;
				this.chocosProduits.add(new ChocolatDeMarque(c, "Vccotioi", 90, 10));
				Chocolat b = Chocolat.C_BQ;
				this.chocosProduits.add(new ChocolatDeMarque(b, "Yocttotoa", 42, 0));
		}
	//System.out.println(	Filiere.LA_FILIERE.getParametre("pourcentage min cacao BQ").getValeur());
		
		return this.chocosProduits;
	}
	
	public void initialiser() {
		super.initialiser();
	}
	
	public void next() {
		super.next();
		double coutStock = 4*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*(this.totalStocksChocoMarque.getValeur()+this.totalStocksFeves.getValeur());
		double coutMainDOeuvre = 5*this.qteTransfo;
		Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), coutStock+coutMainDOeuvre);
		this.journal.ajouter(COLOR_LLGRAY, COLOR_LGREEN,"Le coût de stockage à ce step s'élève à : "+coutStock+" et le coût de la main d'oeuvre  à : "+coutMainDOeuvre);
		
		for (ExemplaireContratCadre c : ContratEnCours_C_BQ ) {
			if (c.getEcheancier().getStepFin() == Filiere.LA_FILIERE.getEtape()) {
				ContratEnCours_C_BQ.remove(c);
			}
		}
		for (ExemplaireContratCadre c : ContratEnCours_C_HQ ) {
			if (c.getEcheancier().getStepFin() == Filiere.LA_FILIERE.getEtape()) {
				ContratEnCours_C_HQ.remove(c);
			}	
		}
		for (ExemplaireContratCadre c : ContratEnCours_F_BQ ) {
			if (c.getEcheancier().getStepFin() == Filiere.LA_FILIERE.getEtape()) {
				ContratEnCours_F_BQ.remove(c);
			}
		}
		for (ExemplaireContratCadre c : ContratEnCours_F_HQ ) {
			if (c.getEcheancier().getStepFin() == Filiere.LA_FILIERE.getEtape()) {
				ContratEnCours_F_HQ.remove(c);
			}
		}	
	}

}
	
