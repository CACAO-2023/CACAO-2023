package abstraction.eq4Transformateur1;

import java.util.ArrayList;
import java.util.LinkedList;

import java.util.List;

import abstraction.eq4Transformateur1.Vente.AODistributeur;
import abstraction.eq4Transformateur1.Vente.VendeurOA;
import abstraction.eq8Distributeur2.ContratCadre;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
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
		List<ExemplaireContratCadre> aretirer=new ArrayList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre c : ContratEnCours_C_BQ ) {
			if (c.getEcheancier().getStepFin() == Filiere.LA_FILIERE.getEtape()) {
				aretirer.add(c);
			}
		}
		ContratEnCours_C_BQ.removeAll(aretirer);
		for (ExemplaireContratCadre c : ContratEnCours_C_HQ ) {
			if (c.getEcheancier().getStepFin() == Filiere.LA_FILIERE.getEtape()) {
				aretirer.add(c);
			}	
		}
		ContratEnCours_C_HQ.removeAll(aretirer);
		for (ExemplaireContratCadre c : ContratEnCours_F_BQ ) {
			if (c.getEcheancier().getStepFin() == Filiere.LA_FILIERE.getEtape()) {
				aretirer.add(c);
			}
		}
		ContratEnCours_F_BQ.removeAll(aretirer);
		for (ExemplaireContratCadre c : ContratEnCours_F_HQ ) {
			if (c.getEcheancier().getStepFin() == Filiere.LA_FILIERE.getEtape()) {
				aretirer.add(c);
			}
		}	
		ContratEnCours_F_HQ.removeAll(aretirer);
		this.NbContratEnCours.setValeur(this, ContratEnCours_F_HQ.size()+ContratEnCours_F_BQ.size()+ContratEnCours_C_HQ.size()+ContratEnCours_C_BQ.size(), this.cryptogramme);
		double venteTotB=0;
		double venteTotH=0;
		int etape = Filiere.LA_FILIERE.getEtape();
		for (ChocolatDeMarque c : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (c.getGamme().equals(Gamme.BQ)) {
				venteTotB+=Filiere.LA_FILIERE.getVentes(c, etape);
			}
			if (c.getGamme().equals(Gamme.HQ)) {
				venteTotH+=Filiere.LA_FILIERE.getVentes(c, etape);
			}
		}
		for (ChocolatDeMarque cm : this.stockChocoMarque.keySet()) {
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN,"Stock de "+Journal.texteSurUneLargeurDe(cm+"", 15)+" = "+this.stockChocoMarque.get(cm));
			if (cm.getGamme().equals(Gamme.BQ)) {
				this.PartDeMarcheBQ.setValeur(this, (Filiere.LA_FILIERE.getVentes(cm, etape))/venteTotB, this.cryptogramme);
			}
			if (cm.getGamme().equals(Gamme.HQ)) {
				this.PartDeMarcheHQ.setValeur(this, (Filiere.LA_FILIERE.getVentes(cm, etape))/venteTotH, this.cryptogramme);
			}
		}
	}

}
	
