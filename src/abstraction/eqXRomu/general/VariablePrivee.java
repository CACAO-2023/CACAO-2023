package abstraction.eqXRomu.general;

import abstraction.eqXRomu.filiere.Banque;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
/**
 * Variable qui ne peut etre consultee/modifiee que par son createur
 * @author R. DEBRUYNE
 */
public class VariablePrivee extends VariableReadOnly {
	public VariablePrivee(String nom, String infobulle, IActeur createur,  double min, double max, double valInit) {
		super(nom, infobulle, createur,  min, max, valInit);
	}
	public VariablePrivee(String nom, IActeur createur,  double min, double max, double valInit) {
		super(nom, createur, min, max, valInit);
	}
	public VariablePrivee(String nom, String infobulle, IActeur createur, double valInit) {
		super(nom, infobulle, createur, valInit);
	}
	public VariablePrivee(String nom, IActeur createur, double valInit) {
		super(nom, createur, valInit);
	}
	public VariablePrivee(String nom, String infobulle, IActeur createur) {
		super(nom, infobulle, createur);
	}
	public VariablePrivee(String nom, IActeur createur) {
		super(nom, createur);
	}
	
	public Historique getHistorique(Integer crypto) {
		if (Filiere.LA_FILIERE!=null) {
			Banque b = Filiere.LA_FILIERE.getBanque();
			if (b!=null) {
				if (b.verifier(this.getCreateur(), crypto)) {
					return super.getHistorique();
				}
			}
		}
		return new Historique();
	}

	public Courbe getCourbe(Integer crypto) {
		if (Filiere.LA_FILIERE!=null) {
			Banque b = Filiere.LA_FILIERE.getBanque();
			if (b!=null) {
				if (b.verifier(this.getCreateur(), crypto)) {
					return super.getCourbe();
				}
			}
		}
		return new Courbe("Bidon");
	}

	public double getValeur(Integer crypto) {
		if (Filiere.LA_FILIERE!=null) {
			Banque b = Filiere.LA_FILIERE.getBanque();
			if (b!=null) {
				if (b.verifier(this.getCreateur(), crypto)) {
					return super.getValeur();
				}
			}
		}		
		return 0.0;
	}

	public double getValeur(int step,Integer crypto) {
		if (Filiere.LA_FILIERE!=null) {
			Banque b = Filiere.LA_FILIERE.getBanque();
			if (b!=null) {
				if (b.verifier(this.getCreateur(), crypto)) {
					return super.getValeur(step);
				}
			}
		}	
		return 0.0;
	}

	public double getMin(Integer crypto) {
		if (Filiere.LA_FILIERE!=null) {
			Banque b = Filiere.LA_FILIERE.getBanque();
			if (b!=null) {
				if (b.verifier(this.getCreateur(), crypto)) {
					return super.getMin();
				}
			}
		}	
		return Double.MIN_VALUE;
	}

	public double getMax(Integer crypto) {
		if (Filiere.LA_FILIERE!=null) {
			Banque b = Filiere.LA_FILIERE.getBanque();
			if (b!=null) {
				if (b.verifier(this.getCreateur(), crypto)) {
					return super.getMax();
				}
			}
		}	
		return Double.MAX_VALUE;
	}
}
