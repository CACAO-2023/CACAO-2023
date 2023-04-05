package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Transformateur2AcheteurBourseCacao extends Transformateur2 implements IAcheteurBourse {

	private double achatMaxParStep;
	private Feve feve;
	private Variable stockFeve;
	
	public Transformateur2AcheteurBourseCacao(Feve f, Variable s, double a) {
		super();
		this.achatMaxParStep = a;
		this.feve = f;
		this.stockFeve = s;
	}
	
	public Feve getFeve() {
		return feve;
	}


	@Override
	public double demande(Feve f, double cours) {
		if (this.getFeve().equals(f)) {
			BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
			double pourcentage = (bourse.getCours(getFeve()).getMax()-bourse.getCours(getFeve()).getValeur())/(bourse.getCours(getFeve()).getMax()-bourse.getCours(getFeve()).getMin()); // difference de prix avec le max / amplitude totale
			return achatMaxParStep*pourcentage;
		} else {
			return 0.0;
		}
	}

	@Override
	public void notificationAchat(Lot l, double coursEnEuroParT) {
		this.stockFeve.setValeur(this, this.stockFeve.getValeur()+l.getQuantiteTotale());
	}
	
	@Override
	public void notificationBlackList(int dureeEnStep) {
		this.journal.ajouter("Aie... je suis blackliste... j'aurais du verifier que j'avais assez d'argent avant de passer une trop grosse commande en bourse...");
	
		
	}
	

}
