package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Transformateur2AcheteurBourseCacao extends Transformateur2AcheteurCC implements IAcheteurBourse {

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
		/*if (this.getFeve().equals(f)) {
			BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
			double pourcentage = (bourse.getCours(getFeve()).getMax()-bourse.getCours(getFeve()).getValeur())/(bourse.getCours(getFeve()).getMax()-bourse.getCours(getFeve()).getMin()); // difference de prix avec le max / amplitude totale
			this.journalAchats.ajouter(COLOR_LLGRAY, COLOR_PURPLE,"   BOURSEA: demande en bourse de "+achatMaxParStep*pourcentage+" de "+f);
			return achatMaxParStep*pourcentage;*/
			double solde = Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme);
			double demande = Math.max(0, Math.min( Math.random()*50, solde));
			this.journalAchats.ajouter(COLOR_LLGRAY, COLOR_PURPLE,"   BOURSEA: demande en bourse de "+demande+" de "+f);
			return demande;
		} /*else {
			return 0.0;
		}}*/


	@Override
	public void notificationAchat(Lot l, double coursEnEuroParT) {
		Feve feve_concernee = ((Feve) l.getProduit());
		double quantite = l.getQuantiteTotale();
		System.out.println("stock"+stockFeves.get(feve_concernee));
		this.stockFeves.put(feve_concernee, this.stockFeves.get(feve_concernee)+quantite);
	}
	
	@Override
	public void notificationBlackList(int dureeEnStep) {
		this.journal.ajouter("Aie... je suis blackliste... j'aurais du verifier que j'avais assez d'argent avant de passer une trop grosse commande en bourse...");
	
		
	}
	


	
}
