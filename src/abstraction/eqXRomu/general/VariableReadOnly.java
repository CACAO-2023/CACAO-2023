package abstraction.eqXRomu.general;
import abstraction.eqXRomu.filiere.Banque;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;

/**
 * Variable qui ne peut etre modifiee que par son createur
 * mais que tout le monde peut consulter
 * @author R. DEBRUYNE
 */
public class VariableReadOnly extends Variable {

	public VariableReadOnly(String nom, String infobulle, IActeur createur,  double min, double max, double valInit) {
		super(nom, infobulle, createur,  min, max, valInit);
	}
	public VariableReadOnly(String nom, IActeur createur,  double min, double max, double valInit) {
		super(nom, createur, min, max, valInit);
	}
	public VariableReadOnly(String nom, String infobulle, IActeur createur, double valInit) {
		super(nom, infobulle, createur, valInit);
	}
	public VariableReadOnly(String nom, IActeur createur, double valInit) {
		super(nom, createur, valInit);
	}
	public VariableReadOnly(String nom, String infobulle, IActeur createur) {
		super(nom, infobulle, createur);
	}
	public VariableReadOnly(String nom, IActeur createur) {
		super(nom, createur);
	}
	public void setMax(double max) {
		// Pas de modification
	}
	public void setValeur(IActeur auteur, double valeur) {
		// Pas de modification
	}
	public void setMin(double min) {
		// Pas de modification
	}
	public void ajouter(IActeur auteur, double delta) {
		// Pas de modification
	}
	public void retirer(IActeur auteur, double delta) {
		// Pas de modification
	}
	public void setMax(double max, Integer crypto) {
		if (Filiere.LA_FILIERE!=null) {
			Banque b = Filiere.LA_FILIERE.getBanque();
			if (b!=null) {
				if (b.verifier(this.getCreateur(), crypto)) {
					super.setMax(max);
				}
			}
		}
	}
	public void setValeur(IActeur auteur, double valeur, Integer crypto) {
		if (Filiere.LA_FILIERE!=null) {
			Banque b = Filiere.LA_FILIERE.getBanque();
			if (b!=null) {
				if (b.verifier(this.getCreateur(), crypto)||auteur.getNom().equals("utilisateur")) {
					super.setValeur(auteur, valeur);
				}
			}
		}
	}
	public void setMin(double min, Integer crypto) {
		if (Filiere.LA_FILIERE!=null) {
			Banque b = Filiere.LA_FILIERE.getBanque();
			if (b!=null) {
				if (b.verifier(this.getCreateur(), crypto)) {
					super.setMin(min);
				}
			}
		}
	}
	public void ajouter(IActeur auteur, double delta, Integer crypto) {
		if (Filiere.LA_FILIERE!=null) {
			Banque b = Filiere.LA_FILIERE.getBanque();
			if (b!=null) {
				if (b.verifier(this.getCreateur(), crypto)||auteur.getNom().equals("utilisateur")) {
					super.ajouter(auteur, delta, crypto);
				}
			}
		}
	}
	public void retirer(IActeur auteur, double delta, Integer crypto) {
		if (Filiere.LA_FILIERE!=null) {
			Banque b = Filiere.LA_FILIERE.getBanque();
			if (b!=null) {
				if (b.verifier(this.getCreateur(), crypto)||auteur.getNom().equals("utilisateur")) {
					super.retirer(auteur, delta);
				}
			}
		}
	}

}
