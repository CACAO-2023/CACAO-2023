package abstraction.eqXRomu.filiere;

import java.util.HashMap;

public interface IAssermente {
// Interface implementee par les acteurs officiels assermentes (la banque et les superviseurs).
// Aucun autre acteur ne doit implementer cette interface.	
	
	
	/**
	 * Methode appelee durant la phase d'initialisation afin de communiquer les cryptogrammes
	 * des acteurs a l'acteur  assermente. Ce dernier pourra se servir de ces  cryptogrammes
	 * afin d'acceder a des operations sensibles.
	 */
	public void setCryptos(HashMap<IActeur, Integer> cryptos);

}
