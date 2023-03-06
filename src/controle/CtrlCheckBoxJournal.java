package controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
//import java.util.Observable;
//import java.util.Observer;

import javax.swing.JCheckBox;

import abstraction.eqXRomu.general.Journal;
import presentation.secondaire.FenetreJournal;

public class CtrlCheckBoxJournal  implements ActionListener, PropertyChangeListener {

	private Journal j;
    private JCheckBox box;
    private FenetreJournal fJournal;
    
    public CtrlCheckBoxJournal(Journal j, JCheckBox box, FenetreJournal fJournal) {
    	this.box = box;
    	this.fJournal = fJournal;
    	this.j = j;
    }
    
	public void actionPerformed(ActionEvent e) {
			this.fJournal.setVisible(this.box.isSelected());
	}

//	public void update(Observable arg0, Object arg1) {
//		if (this.fJournal.isVisible()) {
//			Journal j = (Journal) arg0;
//			this.fJournal.setLabel(j.toHtml());
//		}
//	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (this.fJournal.isVisible()) {
			this.fJournal.setLabel(j.toHtml());
		}
	}

}