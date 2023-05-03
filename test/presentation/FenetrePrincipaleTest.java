package presentation;

import java.util.Map.Entry;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
		
		try {
			FileWriter fileWriter = new FileWriter("tempsEquipes.json");
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.print("times={");
			printWriter.print("\"eq1Producteur1\":"+ Filiere.LA_FILIERE.tempsEquipes.get("EQ1") +",");
			printWriter.print("\"eq2Producteur2\":"+ Filiere.LA_FILIERE.tempsEquipes.get("EQ2") +",");
			printWriter.print("\"eq3Producteur3\":"+ Filiere.LA_FILIERE.tempsEquipes.get("EQ3") +",");
			printWriter.print("\"eq4Transformateur1\":"+ Filiere.LA_FILIERE.tempsEquipes.get("EQ4") +",");
			printWriter.print("\"eq5Transformateur2\":"+ Filiere.LA_FILIERE.tempsEquipes.get("EQ5") +",");
			printWriter.print("\"eq6Transformateur3\":"+ Filiere.LA_FILIERE.tempsEquipes.get("EQ6") +",");
			printWriter.print("\"eq7Distributeur1\":"+ Filiere.LA_FILIERE.tempsEquipes.get("EQ7") +",");
			printWriter.print("\"eq8Distributeur2\":"+ Filiere.LA_FILIERE.tempsEquipes.get("EQ8") +",");
			printWriter.print("\"eq9Distributeur3\":"+ Filiere.LA_FILIERE.tempsEquipes.get("EQ9") +"}");
			printWriter.println();
		    printWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}

}
