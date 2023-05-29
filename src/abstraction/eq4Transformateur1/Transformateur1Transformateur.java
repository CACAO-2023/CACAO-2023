package abstraction.eq4Transformateur1;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eq4Transformateur1.Achat.AchatBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

// Francois, Alexian, Amine, Fouad

public class Transformateur1Transformateur extends Stock implements IFabricantChocolatDeMarque  {
	
	protected List<ChocolatDeMarque>chocosProduits;
	protected double qteTransfo;
	
	public Transformateur1Transformateur() {
		super();
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
	}
	
	//========================================================
	//               FabricantChocolatDeMarque
	//========================================================

	public List<ChocolatDeMarque> getChocolatsProduits() {
		if (this.chocosProduits.size()==0) {
			for (ChocolatDeMarque c : Filiere.LA_FILIERE.getChocolatsProduits()) {
				if(c.getMarque().equals("Vccotioi") || c.getMarque().equals("Yocttotoa") ) {
					chocosProduits.add(c);
				}
			}
		}
		return this.chocosProduits ;
	}

	public void initialiser() {
		super.initialiser();
	}

	/**
	 * @author fouad/amine
	 *
	 */

	
	public void next() {
		super.next();
		Feve fb = Feve.F_BQ;
		Chocolat cb = Chocolat.C_BQ;
		int transfo = (int) (Math.min(this.stockFeves.get(fb), Math.random()*1000));
		double conversionb = 1.58;
		if (transfo>0) {
			this.retirer(fb,transfo);
			int pourcentageCacao =  42;
			ChocolatDeMarque cm= new ChocolatDeMarque(cb, "Yocttotoa", pourcentageCacao, 0);
			this.ajouter(cm, transfo*conversionb);
			this.journal.ajouter(COLOR_LLGRAY, Color.PINK, "Transfo de "+(transfo)+" T de "+fb+" en "+transfo*conversionb+" T de "+cb);
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN," stock("+fb+")->"+this.stockFeves.get(fb));
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN," stock("+cm+")->"+this.stockChocoMarque.get(cm));
			this.qteTransfo=transfo;
		}
		Feve fh = Feve.F_HQ_BE;
		Chocolat ch = Chocolat.C_HQ_BE;
		int transfoh = (int) (Math.min(this.stockFeves.get(fh), Math.random()*1000));
		double conversion = 1.06;
		if (transfoh>0) {
			this.retirer(fh,transfoh);
			// Tous les chocolats sont directement étiquetés "Vccotioi"
			int pourcentageCacao =  94;
			ChocolatDeMarque cm= new ChocolatDeMarque(ch, "Vccotioi", pourcentageCacao, 10);
			this.ajouter(cm, transfoh*conversion);
			this.journal.ajouter(COLOR_LLGRAY, Color.PINK, "Transfo de "+(transfoh)+" T de "+fh+" en "+transfoh*conversion+" T de "+ch);
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN," stock("+fh+")->"+this.stockFeves.get(fh));
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN," stock("+cm+")->"+this.stockChocoMarque.get(cm));
			this.qteTransfo+=transfoh;
		}
	}
}
	
