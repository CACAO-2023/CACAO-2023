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
			for (Chocolat c : Chocolat.values()) {
				int pourcentageCacao =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+c.getGamme()).getValeur());
				this.chocosProduits.add(new ChocolatDeMarque(c, "Vccotioi", pourcentageCacao, 0));
			}
		}
		return this.chocosProduits;
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
			this.stockFeves.put(fb, this.stockFeves.get(fb)-transfo);
			this.totalStocksFeves.setValeur(this, this.totalStocksFeves.getValeur()-transfo,this.cryptogramme);
			this.stockChoco.put(cb, this.stockChoco.get(cb)+(transfo)*conversionb);
			int pourcentageCacao =  42;
			this.totalStocksChoco.ajouter(this, ((transfo)*conversionb), this.cryptogramme);
			this.journal.ajouter(COLOR_LLGRAY, Color.PINK, "Transfo de "+(transfo)+" T de "+fb+" en "+transfo*conversionb+" T de "+cb);
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN," stock("+fb+")->"+this.stockFeves.get(fb));
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN," stock("+cb+")->"+this.stockChoco.get(cb));
			this.qteTransfo=transfo;
		}
		Feve fh = Feve.F_HQ_BE;
		Chocolat ch = Chocolat.C_HQ_BE;
		int transfoh = (int) (Math.min(this.stockFeves.get(fh), Math.random()*1000));
		double conversion = 1.06;
		if (transfoh>0) {
			this.stockFeves.put(fh, this.stockFeves.get(fh)-transfoh);
			this.totalStocksFeves.setValeur(this, this.totalStocksFeves.getValeur()-transfoh,this.cryptogramme);
			// Tous les chocolats sont directement étiquetés "Vccotioi"
			int pourcentageCacao =  94;
			ChocolatDeMarque cm= new ChocolatDeMarque(ch, "Vccotioi", pourcentageCacao, 15);
			double scm = this.stockChocoMarque.keySet().contains(cm) ?this.stockChocoMarque.get(cm) : 0.0;
			this.stockChocoMarque.put(cm, scm+((transfoh)*conversion));
			this.totalStocksChocoMarque.ajouter(this, ((transfoh)*conversion), this.cryptogramme);
			this.journal.ajouter(COLOR_LLGRAY, Color.PINK, "Transfo de "+(transfoh)+" T de "+fh+" en "+transfoh*conversion+" T de "+ch);
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN," stock("+fh+")->"+this.stockFeves.get(fh));
			this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN," stock("+cm+")->"+this.stockChocoMarque.get(cm));
			this.qteTransfo+=transfoh;
		}
	}
}
	
