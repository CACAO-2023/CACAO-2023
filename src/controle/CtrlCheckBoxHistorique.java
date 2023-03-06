package controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;

import abstraction.eqXRomu.general.Historique;
import presentation.secondaire.FenetreHistorique;

public class CtrlCheckBoxHistorique  extends WindowAdapter implements ActionListener, PropertyChangeListener {

	private JCheckBox checkBox;
	private FenetreHistorique fenetreHistorique;
	private Historique historique;

	public CtrlCheckBoxHistorique(Historique historique, JCheckBox checkBox, FenetreHistorique fenetreHistorique) {
		this.checkBox = checkBox;
		this.fenetreHistorique = fenetreHistorique;
		this.historique = historique;
	}

	public void actionPerformed(ActionEvent e) {
		this.fenetreHistorique.setVisible(this.checkBox.isSelected());
	}


	public void windowClosing(WindowEvent arg0) {
		this.fenetreHistorique.setVisible(false);
		this.checkBox.setSelected(false);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (this.fenetreHistorique.isVisible()) {
			this.fenetreHistorique.setLabel(historique.toHtml());
		}
	}
}