package controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;

public class CtrlBtnFaillite implements ActionListener {
	private JButton boutonFaillite;
	private IActeur acteur;

	public CtrlBtnFaillite(JButton boutonFaillite, IActeur acteur) {
		this.boutonFaillite = boutonFaillite;
		this.acteur = acteur;
	}
	public void actionPerformed(ActionEvent e) {
		String[] options = {"Oui, l'acteur "+acteur.getNom()+" fait faillite", "Non, annulez"}; 
		int choix = JOptionPane.showOptionDialog(null,
				"Etes-vous sur de vouloir entrainer la faillite de l'acteur "+acteur.getNom(),
				"Vraiment sur ?",				
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		if (choix==JOptionPane.YES_OPTION) {
			Filiere.LA_FILIERE.getBanque().faireFaillite(this.acteur);
			boutonFaillite.setEnabled(false);
		}
	}
}
