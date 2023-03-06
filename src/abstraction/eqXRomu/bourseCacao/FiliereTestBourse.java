package abstraction.eqXRomu.bourseCacao;

import java.util.HashMap;

import abstraction.eqXRomu.Romu;
import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.Feve;

public class FiliereTestBourse  extends Filiere {
	private static final double DISTRIBUTIONS_ANNUELLES[][] = {
			//Jan1 Jan2 Fev1 Fev2 Mar1 Mar2 Avr1 Avr2 Mai1 Mai2 Jui1 Jui2 Jul1 Jul2 Aou1 Aou2 Sep1 Sep2 Oct1 Oct2 Nov1 Nov2 Dec1 Dec2
			{ 4.5, 4.5, 4.5, 4.5, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.5, 4.5, 4.5, 4.5, },			
			{ 5.5, 5.5, 5.0, 5.0, 4.5, 4.0, 4.0, 4.0, 4.0, 3.5, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.5, 4.0, 4.0, 4.5, 5.0, 5.0, 5.5, 5.5, },			
			{ 3.5, 3.5, 6.0, 3.5, 3.5, 3.5, 3.5, 3.5, 9.0, 3.5, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.5, 3.5, 3.5, 3.5, 3.5, 3.5, 9.0, 9.0, },			
			{ 3.0, 3.0, 6.0, 3.0, 3.0, 3.0, 3.0, 3.0, 9.0, 3.0, 3.0, 2.0, 2.0, 2.0, 2.0, 2.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0,15.0,15.0, },			
			{ 3.0, 3.0, 7.0, 3.0, 3.0, 3.0, 3.0, 3.0,10.0, 3.0, 3.0, 2.0, 2.0, 2.0, 2.0, 2.0, 3.0, 3.0, 3.0,10.0, 3.0, 3.0,11.0,10.0, },			
			{ 3.0, 3.0,10.0, 3.0, 3.0, 3.0, 3.0, 3.0,12.0, 3.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 3.0, 3.0, 3.0, 3.0, 3.0,15.0,15.0, },			
			{ 3.0, 3.0,11.0, 3.0, 3.0, 3.0, 3.0, 3.0,13.0, 3.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 3.0, 3.0,10.0, 3.0, 3.0,11.0,10.0, },			
	};

	private BourseCacao bourse;

	public FiliereTestBourse() {
		super();
		HashMap<Chocolat, Double> repartitionInitiale = new HashMap<Chocolat, Double>();
		repartitionInitiale.put(Chocolat.C_HQ_BE, 30.0); // Haute Qualite   Bio-Equitable  
		repartitionInitiale.put(Chocolat.C_MQ_BE,  5.0); // Moyenne Qualite Bio-Equitable 
		repartitionInitiale.put(Chocolat.C_MQ,    25.0); // Moyenne Qualite pas Bio-Equitable 
		repartitionInitiale.put(Chocolat.C_BQ,    40.0); // Basse Qualite   pas Bio-Equitable 

		ClientFinal  cf = new ClientFinal(7200000.0 , repartitionInitiale, DISTRIBUTIONS_ANNUELLES);

		this.ajouterActeur(cf);
		this.ajouterActeur(new ExempleVendeurBourseCacao(Feve.F_MQ, 100));
		this.ajouterActeur(new ExempleVendeurBourseCacao(Feve.F_MQ, 300));
		this.ajouterActeur(new ExempleVendeurBourseCacao(Feve.F_BQ, 200));
		this.ajouterActeur(new ExempleVendeurBourseCacao(Feve.F_BQ, 400));
		this.ajouterActeur(new ExempleAcheteurBourseCacao(Feve.F_MQ, 0, 10));
		this.ajouterActeur(new ExempleAcheteurBourseCacao(Feve.F_MQ, 0, 5));
		this.ajouterActeur(new ExempleAcheteurBourseCacao(Feve.F_BQ, 0, 25));
		this.ajouterActeur(new ExempleAcheteurBourseCacao(Feve.F_BQ, 0, 17));
		this.ajouterActeur(new Romu());
		this.bourse=new BourseCacao();
		this.ajouterActeur(this.bourse);

	}
}