package abstraction.eqXRomu.filiere;

import java.util.HashMap;

import abstraction.eq1Producteur1.Producteur1;
import abstraction.eq2Producteur2.Producteur2;
import abstraction.eq3Producteur3.Producteur3;
import abstraction.eq4Transformateur1.Transformateur1;
import abstraction.eq5Transformateur2.Transformateur2;
import abstraction.eq6Transformateur3.Transformateur3;
import abstraction.eq7Distributeur1.Distributeur1;
import abstraction.eq8Distributeur2.Distributeur2;
import abstraction.eq9Distributeur3.Distributeur3;
import abstraction.eqXRomu.Romu;
import abstraction.eqXRomu.appelsOffres.SuperviseurVentesAO;
import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.offresAchat.SuperviseurVentesOA;
import abstraction.eqXRomu.produits.Chocolat;


public class FiliereParDefaut extends Filiere {
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
	private ClientFinal cf ;
	private SuperviseurVentesContratCadre superviseurCC;
	private BourseCacao bourse;
	private SuperviseurVentesAO superviseurAO;
	private SuperviseurVentesOA superviseurOA;


	public FiliereParDefaut() {
		super();
		HashMap<Chocolat, Double> repartitionInitiale = new HashMap<Chocolat, Double>();
		// Tirage au sort de la repartition
		int hasard = (int)(Math.random()*3); // tirage au hasard d'un nombre dans {0, 1, 2}
		this.journalFiliere.ajouter("Repartition initiale des  achats des clients finaux : "+hasard);

		switch (hasard) {
		case 0 :
			repartitionInitiale.put(Chocolat.C_HQ_BE, 45.0); // Haute Qualite   Bio-Equitable 
			repartitionInitiale.put(Chocolat.C_MQ_BE, 10.0); // Moyenne Qualite Bio-Equitable 
			repartitionInitiale.put(Chocolat.C_MQ,    15.0); // Moyenne Qualite pas Bio-Equitable 
			repartitionInitiale.put(Chocolat.C_BQ,    30.0); // Basse Qualite   pas Bio-Equitable 
			break;
		case 1 : 
			repartitionInitiale.put(Chocolat.C_HQ_BE, 30.0); // Haute Qualite   Bio-Equitable 
			repartitionInitiale.put(Chocolat.C_MQ_BE,  5.0); // Moyenne Qualite Bio-Equitable 
			repartitionInitiale.put(Chocolat.C_MQ,    25.0); // Moyenne Qualite pas Bio-Equitable 
			repartitionInitiale.put(Chocolat.C_BQ,    40.0); // Basse Qualite   pas Bio-Equitable 
			break;
		default : 
			repartitionInitiale.put(Chocolat.C_HQ_BE,  16.0); // Haute Qualite   Bio-Equitable   
			repartitionInitiale.put(Chocolat.C_MQ_BE,   4.0); // Moyenne Qualite Bio-Equitable 
			repartitionInitiale.put(Chocolat.C_MQ,     25.0); // Moyenne Qualite pas Bio-Equitable 
			repartitionInitiale.put(Chocolat.C_BQ,     55.0); // Basse Qualite   pas Bio-Equitable 
		}
		this.cf = new ClientFinal(7200000.0 , repartitionInitiale, DISTRIBUTIONS_ANNUELLES);
		this.ajouterActeur(cf);
		this.ajouterActeur(new Producteur1());
		this.ajouterActeur(new Producteur2());
		this.ajouterActeur(new Producteur3());
		this.ajouterActeur(new Transformateur1());
		this.ajouterActeur(new Transformateur2());
		this.ajouterActeur(new Transformateur3());
		this.ajouterActeur(new Distributeur1());
		this.ajouterActeur(new Distributeur2());
		this.ajouterActeur(new Distributeur3());
		this.ajouterActeur(new Romu());
		this.superviseurCC=new SuperviseurVentesContratCadre();
		this.ajouterActeur(this.superviseurCC);
		this.bourse=new BourseCacao();
		this.ajouterActeur(this.bourse);
		this.superviseurAO=new SuperviseurVentesAO();
		this.ajouterActeur(this.superviseurAO);
		this.superviseurOA=new SuperviseurVentesOA();
		this.ajouterActeur(this.superviseurOA);

	}
	/**
	 * Redefinition afin d'interdire l'acces direct a certains superviseurs/acteurs.
	 * Sans cela, il serait possible de contourner l'autentification
	 */
	public IActeur getActeur(String nom) {
		if (!nom.startsWith("C.F.")) {
			return super.getActeur(nom); 
		} else {
			return null;
		}
	}

	public void initialiser() {
		super.initialiser();
	}
}
