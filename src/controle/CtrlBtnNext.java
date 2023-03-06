package controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import abstraction.eqXRomu.filiere.Filiere;

public class CtrlBtnNext implements ActionListener {

	private Filiere filiere;
	private int nb;

	public CtrlBtnNext(Filiere monde, int nb) {
		this.filiere = monde;
		this.nb=nb;
	}
	public void actionPerformed(ActionEvent arg0) {
		for (int i=1; i<=this.nb; i++) { 
			this.filiere.next();
		}
	}
}
