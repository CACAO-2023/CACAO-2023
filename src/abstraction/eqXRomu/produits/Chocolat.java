 package abstraction.eqXRomu.produits;

import abstraction.eqXRomu.filiere.Filiere;

public enum Chocolat implements IProduit {
	C_HQ_BE(Gamme.HQ, true), // Haute Qualite   Bio-Equitable   
	C_MQ_BE(Gamme.MQ, true), // Moyenne Qualite Bio-Equitable   
	C_MQ   (Gamme.MQ, false),// Moyenne Qualite pas Bio-Equitable 
	C_BQ   (Gamme.BQ, false);// Basse Qualite   pas Bio-Equitable 
	
	private Gamme gamme;
	private boolean bioEquitable;
	
	Chocolat(Gamme gamme, boolean bioEquitable) {
		this.gamme = gamme;
		this.bioEquitable = bioEquitable;
	}
	
	public String getType() {
		return "Chocolat";
	}
	
	public Gamme getGamme() {
		return this.gamme;
	}
	
	public boolean isBioEquitable() {
		return this.bioEquitable;
	}

	public static Chocolat get(Gamme gamme, boolean bioEquitable) {
		for (Chocolat c : Chocolat.values()) {
			if (c.gamme==gamme && c.isBioEquitable()==bioEquitable) {
				return c;
			}
		}
		return null;
	}
	
	public double qualite() {
		double qualite;
		switch (getGamme()) {
		case BQ : qualite=Filiere.LA_FILIERE.getParametre("qualite basse").getValeur();
		case MQ : qualite=Filiere.LA_FILIERE.getParametre("qualite moyenne").getValeur();
		default : qualite=Filiere.LA_FILIERE.getParametre("qualite haute").getValeur(); //HAUTE
		}
		if (isBioEquitable()) {
			qualite=qualite+Filiere.LA_FILIERE.getParametre("gain qualite bioequitable").getValeur();
		}
		return qualite;
	}
	public static void main(String[] args) {
		System.out.println("== Les differentes chocolats ==");
		for (Chocolat c : Chocolat.values()) {
			System.out.println(c);
		}
		
		Chocolat c = Chocolat.C_MQ_BE;
		System.out.println("\n"+c+" ->getGamme()="+c.getGamme()+"\n        ->IsBioEquitable()="+c.isBioEquitable()+"\n        ->getType()="+c.getType());

		System.out.println("\n Chocolat.get(Gamme.HQ, true) -> "+Chocolat.get(Gamme.HQ, true));
	}
}
