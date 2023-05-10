package abstraction.eq3Producteur3;

import java.util.TimerTask;

import javax.swing.JFrame;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;

public class ControlTimeGif extends TimerTask{
	private JFrame popup;
	private Producteur3Acteur producteur3Acteur;
	public ControlTimeGif(JFrame popup) {
        this.popup = popup;
        popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.producteur3Acteur = ((Producteur3Acteur)Filiere.LA_FILIERE.getActeur("EQ3"));
    }
	
	public void run() {
		this.popup.dispose();
		this.producteur3Acteur.setNbrpopup( this.producteur3Acteur.getNbrpopup() -1);
		
		
	}

}
