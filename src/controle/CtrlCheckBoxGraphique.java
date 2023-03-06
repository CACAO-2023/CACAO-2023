package controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;

import presentation.secondaire.FenetreGraphique;

public class CtrlCheckBoxGraphique extends WindowAdapter implements ActionListener, PropertyChangeListener{

	private FenetreGraphique graphique;
	private JCheckBox checkBox;

	public CtrlCheckBoxGraphique( FenetreGraphique graphique, JCheckBox checkBox) {
		this.graphique = graphique;
		this.checkBox = checkBox;
	}

	public void windowClosing(WindowEvent arg0) {
		this.graphique.setVisible(true);
		this.checkBox.setSelected(false);
	}

	public void actionPerformed(ActionEvent e) {
		this.graphique.setVisible(this.checkBox.isSelected());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (this.graphique.isVisible()) {
			this.graphique.repaint();
		}
	}
}
