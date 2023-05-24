package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Transformateur2AcheteurBourseCacao extends Transformateur2VendeurCC implements IAcheteurBourse {

	/**
	 * @author FERHOUT Adam
	 */
	
	private double coursMaxMQ;
	private double coursMinMQ;
	
	private double coursMaxHQ;
	private double coursMinHQ;
	
	public Transformateur2AcheteurBourseCacao() { // on utilisera ces variables plus tard dans la V2.
		this.coursMinMQ = 10;
		this.coursMaxMQ = 20;

	}

	/**
	 * @author FERHOUT Adam
	 */
	
	public double demande(Feve f, double cours) {
		// on achete en quantités aléatoires si notre solde le permet
		System.out.println(this.totalStocksFeves.getValeur());
		if((f.getGamme()==Feve.F_MQ.getGamme())
				&&(this.totalStocksFeves.getValeur() < 20000)) // on achète plus de fèves a partir de 20000 tonnes
		{
			double solde = Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme);
			double demande = Math.max(0, Math.min( Math.random()*500, solde));
			this.journalAchats.ajouter(COLOR_LLGRAY, COLOR_PURPLE,"   BOURSEA: demande en bourse de "+demande+" de "+f);
			return demande;
		}
		if(this.totalStocksFeves.getValeur()>20000) {
			this.journalAchats.ajouter(COLOR_LLGRAY, COLOR_PURPLE,"   Stock trop elevé ! Pas d'achat. ");
			return 0.0;	
		}
		return 0.0;
	}


	
	/**
	 * @author FERHOUT Adam
	 */
	
	public void notificationAchat(Lot l, double coursEnEuroParT) {
		Feve feve_concernee = ((Feve) l.getProduit());
		double quantite = l.getQuantiteTotale();
		//.stockFeves.get(feve_conernee).setValeur(this, this.stockFeves.get(feve_concernee)+l.getQuantiteTotale());
		this.stockFeves.put(feve_concernee, this.stockFeves.get(feve_concernee)+quantite); // on ajoute nos feves a notre stock
		this.journalAchats.ajouter(COLOR_LLGRAY, COLOR_GREEN,"Achat de "+feve_concernee.getGamme()+" En quantité "+ quantite);
	}
	
	@Override
	public void notificationBlackList(int dureeEnStep) {
		this.journalAchats.ajouter("Aie... je suis blackliste... j'aurais du verifier que j'avais assez d'argent avant de passer une trop grosse commande en bourse...");
	
		
	}
	


	
}
