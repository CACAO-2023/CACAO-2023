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
		
		for (Entry<IActeur, Long> entry : Filiere.LA_FILIERE.tempsEquipes.entrySet()) {
			System.out.println(entry.getKey().getNom() + " : " + entry.getValue());
		}
	}

}
