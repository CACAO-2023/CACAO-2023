package abstraction.eqXRomu.produits;

public enum Feve implements IProduit {

	F_HQ_BE(Gamme.HQ, true), // FEVE HAUTE QUALITE BIO EQUITABLE
	F_MQ_BE(Gamme.MQ, true), // FEVE MOYENNE QUALITE BIO EQUITABLE
	F_MQ(   Gamme.MQ, false),// FEVE MOYENNE QUALITE PAS BIO_EQUITABLE
	F_BQ(   Gamme.BQ, false);// FEVE BASSE QUALITE PAS BIO_EQUITABLE
	
	private Gamme gamme;
	private boolean bioequitable;
	
	Feve(Gamme gamme, boolean bioEquitable) {
		this.gamme = gamme;
		this.bioequitable = bioEquitable;
	}
	
	public String getType() {
		return "Feve";
	}

	public Gamme getGamme() {
		return this.gamme;
	}
	
	public boolean isBioEquitable() {
		return this.bioequitable;
	}
	
	public static Feve get(Gamme gamme, boolean bioEquitable) {
		for (Feve c : Feve.values()) {
			if (c.gamme==gamme && c.bioequitable==bioEquitable) {
				return c;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println("== Les differentes feves ==");
		for (Feve f : Feve.values()) {
			System.out.println(f);
		}
		
		Feve f = Feve.F_HQ_BE;
		System.out.println("\n"+f+" ->getGamme()="+f.getGamme()+"\n        ->IsBioEquitable()="+f.isBioEquitable()+"\n        ->getType()="+f.getType());
		
		System.out.println("\nFeve.get(Gamme.MQ, true)="+Feve.get(Gamme.MQ, true));
	}

}
