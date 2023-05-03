package presentation;

import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.junit.Test;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;

public class FenetrePrincipaleTest {

	@Test
	public void test() {
		Filiere.LA_FILIERE = null;
		FenetrePrincipale fp = new FenetrePrincipale(new String[0]);
		for (int i=0; i<100; i++)
			((JButton) ((JPanel) fp.getRootPane().getContentPane().getComponent(2)).getComponent(0)).doClick();
		
		for (int i=0; i<10; i++)
			((JButton) ((JPanel) fp.getRootPane().getContentPane().getComponent(2)).getComponent(1)).doClick();
		
		((JButton) ((JPanel) fp.getRootPane().getContentPane().getComponent(2)).getComponent(2)).doClick();
		
		System.out.print("tempsEquipes={");
		System.out.print("\"eq1Producteur1\":\""+ Filiere.LA_FILIERE.tempsEquipes.get("EQ1") +"\",");
		System.out.print("\"eq2Producteur2\":\""+ Filiere.LA_FILIERE.tempsEquipes.get("EQ2") +"\",");
		System.out.print("\"eq3Producteur3\":\""+ Filiere.LA_FILIERE.tempsEquipes.get("EQ3") +"\",");
		System.out.print("\"eq4Transformateur1\":\""+ Filiere.LA_FILIERE.tempsEquipes.get("EQ4") +"\",");
		System.out.print("\"eq5Transformateur2\":\""+ Filiere.LA_FILIERE.tempsEquipes.get("EQ5") +"\",");
		System.out.print("\"eq6Transformateur3\":\""+ Filiere.LA_FILIERE.tempsEquipes.get("EQ6") +"\",");
		System.out.print("\"eq7Distributeur1\":\""+ Filiere.LA_FILIERE.tempsEquipes.get("EQ7") +"\",");
		System.out.print("\"eq8Distributeur2\":\""+ Filiere.LA_FILIERE.tempsEquipes.get("EQ8") +"\",");
		System.out.print("\"eq9Distributeur3\":\""+ Filiere.LA_FILIERE.tempsEquipes.get("EQ9") +"\"}");
		System.out.println();
		
	}

}
