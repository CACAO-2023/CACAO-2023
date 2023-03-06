package controle;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JTextField;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;

class Utilisateur implements IActeur {
	public String getNom() {
		return "utilisateur";
	}
	public String getDescription() {
		return "L'humain qui utilise la simulation";
	}
	public Color getColor() {
		return new Color(255, 255, 255);
	}
	public void next() {
	}
	public void initialiser() {
	}
	public void setCryptogramme(Integer crypto) {
	}
	public List<String> getNomsFilieresProposees() {
		return new ArrayList<String>();
	}
	public Filiere getFiliere(String nom) {
		return null;
	}
	public List<Variable> getIndicateurs() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		return res;
	}
	public void notificationFaillite(IActeur acteur) {
	}
	public void notificationOperationBancaire(double montant) {
	}
}
public class CtrlJTextField  implements ActionListener, PropertyChangeListener {

    private JTextField field;
    private Variable ind;
    private Color defaultColor;
    
    public CtrlJTextField(JTextField field, Variable ind) {
    	this.field = field;
    	this.ind = ind;
    	this.defaultColor = this.field.getBackground();
    }
    
	public void actionPerformed(ActionEvent e) {
		NumberFormat dc = NumberFormat.getInstance(Locale.FRANCE);
		dc.setMaximumFractionDigits(4);
		dc.setGroupingUsed(true);
		double saisie =0;
		try {
			saisie = dc.parse(this.field.getText()).doubleValue();
			if (this.ind.getValeur()!=saisie) {
				this.ind.setValeur(new Utilisateur(), saisie, 0 );
			}
		} catch (NumberFormatException | ParseException e1) {
			e1.printStackTrace();
		}
//		if (this.ind.getValeur()<this.ind.getMin()) {
//			field.setBackground(Color.RED);
//			System.out.println("val="+this.ind.getValeur()+" min="+this.ind.getMin()+" minval="+Double.MIN_VALUE+" maxval="+Double.MAX_VALUE);
//		}
//		if (this.ind.getValeur()>this.ind.getMax()) {
//			field.setBackground(Color.PINK);
//		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		NumberFormat dc = NumberFormat.getInstance(Locale.FRANCE);
		dc.setMaximumFractionDigits(4);
		dc.setMinimumFractionDigits(4);
		dc.setGroupingUsed(true);
		String formattedText = dc.format(ind.getValeur());
		this.field.setText(formattedText);
		field.setBackground(this.defaultColor);
		if (this.ind.getValeur()<this.ind.getMin()) {
			field.setBackground(Color.RED);
		}
		if (this.ind.getValeur()>this.ind.getMax()) {
			field.setBackground(Color.CYAN);
		}
		if (ind.getMin()!=-Double.MAX_VALUE || ind.getMax()!=Double.MAX_VALUE) {
			field.setToolTipText("["+dc.format(ind.getMin())+", "+dc.format(ind.getMax())+"]");
		} else {
			field.setToolTipText("");
		}

	}

}