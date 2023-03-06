package presentation.secondaire;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import abstraction.eqXRomu.general.Journal;

/**
 * Classe modelisant une fenetre presentant graphiquement un Journal.
 * Cette classe est utilisee pour l'affichage des journaux.
 * 
 * Vous n'aurez pas, a priori, a utiliser directement cette classe
 * 
 * @author Romuald Debruyne
 */
public class FenetreJournal extends JFrame {

	private static final long serialVersionUID = 1L;

	private Journal journal;
	private JLabel label;
	private JCheckBox checkBox;

	public FenetreJournal(Journal j) {
		super(""+j.getNom());
		this.journal = j;

		this.setLayout(new BorderLayout());
		this.label = new JLabel();
		this.label.setText(this.journal.toHtml());
		this.label.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				//	journal.augmenterLignesAffichables();
				//	repaint();
			}
			public void mousePressed(MouseEvent e) {
				if (e.getButton()==MouseEvent.BUTTON1) {
					journal.augmenterLignesAffichables();
					repaint();
				} else {
					File f = new File("");
					JFileChooser select=new JFileChooser( f.getAbsolutePath() );

					select.setDialogTitle("Specifiez le nom du fichier texte a creer");
					select.setFileFilter(new FichiersTXT());

					int reponse=select.showSaveDialog(FenetreJournal.this);
					if (reponse==JFileChooser.APPROVE_OPTION) {
						File fichier =select.getSelectedFile();
						if (!FichiersTXT.accepts(fichier)) {
							fichier=new File(fichier.getAbsolutePath()+".txt");
						}
						enregistrer(fichier.getAbsolutePath());
					}
				}
			}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {} 

		});
		JScrollPane sp = new JScrollPane(label);
		this.add(sp, BorderLayout.CENTER);
		this.setSize(new Dimension(800,600));
		this.addWindowListener(new WindowAdapter() {public void windowClosing(WindowEvent e) {
			FenetreJournal gg = (FenetreJournal)(e.getSource());
			gg.setVisible(false);

			if (gg.checkBox!=null) {gg.checkBox.setSelected(false);} 
		}});

	}

	public void enregistrer(String nomFichier) {
		try {
			PrintWriter aEcrire= new PrintWriter(new BufferedWriter(new FileWriter(nomFichier)));
			aEcrire.println( journal.toStringSansBalise());
			aEcrire.close();
		}catch (IOException e) {
			System.out.println("Une operation sur les fichiers a leve l’exception "+e) ;
		}
	}
	@Override
	public void paint(Graphics g) {
		this.label.setText(journal.toHtml());
		super.paint(g);
	}

	public void setCheckBox(JCheckBox checkBox) {
		this.checkBox = checkBox;
	}
	public void setLabel(String s) {
		this.label.setText(s);
		if (this.isVisible()) {
			this.repaint();
		}
	}
}
class FichiersTXT extends FileFilter {
	// Classe servant a n'afficher que les fichiers PNG lors de la sauvegarde de l'image	
	public static boolean accepts(File f) {
		String n=f.getName().toLowerCase();
		int l=n.length();
		if (l<4) {
			return false;
		}
		char ex0=n.charAt(l-4);
		char ex1=n.charAt(l-3);
		char ex2=n.charAt(l-2);
		char ex3=n.charAt(l-1);
		return (ex0=='.' && ex1=='t' && ex2=='x' && ex3=='t');
	}
	public boolean 	accept(File f) {
		if (f.isDirectory()) {
			return true; 
		}
		String n=f.getName().toLowerCase();
		int l=n.length();
		if (l<4) {
			return false;
		}
		char ex0=n.charAt(l-4);
		char ex1=n.charAt(l-3);
		char ex2=n.charAt(l-2);
		char ex3=n.charAt(l-1);
		return (ex0=='.' && ex1=='t' && ex2=='x' && ex3=='t');
	}
	public String 	getDescription() {
		return "fichiers TXT";
	}
}
