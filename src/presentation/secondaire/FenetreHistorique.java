package presentation.secondaire;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import abstraction.eqXRomu.general.Historique;
import abstraction.eqXRomu.general.Variable;

/**
 * Classe modelisant une fenetre presentant graphiquement un Historique.
 * Cette classe est utilisee pour l'affichage des historiques.
 * 
 * Vous n'aurez pas, a priori, a utiliser directement cette classe
 * 
 * @author Romuald Debruyne
 */
public class FenetreHistorique extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private Historique historique;
	private JLabel label;
    
	public FenetreHistorique(Variable i, Integer crypto) {
		super("Historique "+i.getNom());
		this.historique = i.getHistorique(crypto);

		this.setLayout(new BorderLayout());
		this.label = new JLabel();
		this.label.setText(this.historique.toHtml());
		JScrollPane sp = new JScrollPane(label);
		this.add(sp, BorderLayout.CENTER);
		this.setSize(new Dimension(800,600));
	}
	
	@Override
	public void paint(Graphics g) {
		this.label.setText(historique.toHtml());
		super.paint(g);
	}
	
	public void setLabel(String s) {
		this.label.setText(s);
		if (this.isVisible()) {
			this.repaint();
		}
	}
}
