package controle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;

import abstraction.eqXRomu.filiere.Filiere;

public class CtrlLabelEtape implements PropertyChangeListener {

	private JLabel labelEtape;
	
	public CtrlLabelEtape(JLabel labelStep) {
		this.labelEtape = labelStep;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		this.labelEtape.setText("Etape : "+evt.getNewValue()+" = "+(Filiere.LA_FILIERE==null ? "" : Filiere.LA_FILIERE.getDate()));
	}
}
