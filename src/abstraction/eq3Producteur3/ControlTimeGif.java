package abstraction.eq3Producteur3;

import java.util.TimerTask;

import javax.swing.JFrame;

public class ControlTimeGif extends TimerTask{
	private JFrame popup;
	
	public ControlTimeGif(JFrame popup) {
        this.popup = popup;
        popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	
	public void run() {
		this.popup.dispose();
		
		
	}

}
