package abstraction;

import org.junit.Test;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.FiliereParDefaut;;

public class FiliereParDefaultTest {

	@Test
	public void testNext() {
		Filiere.LA_FILIERE = null;
		Filiere.LA_FILIERE = new FiliereParDefaut();
		Filiere.LA_FILIERE.initialiser();
		for (int i=0; i<300; i++)
			Filiere.LA_FILIERE.next();
	}

}
