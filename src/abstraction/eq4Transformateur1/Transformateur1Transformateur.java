package abstraction.eq4Transformateur1;
import abstraction.eq4Transformateur1.Transformateur1Acteur;


import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eq4Transformateur1.Achat.AchatBourse;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

// Francois, Alexian, Amine, Fouad

public class Transformateur1Transformateur extends Stock implements IFabricantChocolatDeMarque  {
	
	protected List<ChocolatDeMarque>chocosProduits;
	protected double qteTransfo;
	protected double aVendreHQ;
	protected double aVendreBQ;
	private int multiplicateur;
	
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
		multiplicateur=0;
	}

	/**
	 * @author fouad/amine
	 *
	 */

	
	public void next() {
		super.next();
		Feve fb = Feve.F_BQ;
		Chocolat cb = Chocolat.C_BQ;
		aVendreBQ=0;
		for (ExemplaireContratCadre c : ContratEnCours_C_BQ ) {
			aVendreBQ+=c.getQuantiteALivrerAuStep();
		}
		double transfo = (int) (Math.min(this.stockFeves.get(fb), Math.random()*1000+(aVendreBQ)/1.58));
		for (ChocolatDeMarque c : this.stockChocoMarque.keySet()) {
			if (c.getGamme().equals(Gamme.BQ)) {
				if (this.stockChocoMarque.get(c)<500) {
					multiplicateur+=1;
					transfo=500*multiplicateur;
				}
				if(this.stockChocoMarque.get(c)>20000) {
					transfo=0;
				}
			}
		}
		
		double conversionb = 1.58;
		if (transfo>0.0) {
			this.retirer(fb,transfo);
			int pourcentageCacao =  42;
			for (ChocolatDeMarque c : Filiere.LA_FILIERE.getChocolatsProduits()) {
				if (c.getMarque().equals("Yocttotoa")) {
					ChocolatDeMarque cm =c;
					this.ajouter(cm, transfo*conversionb);
					this.journal.ajouter(COLOR_LLGRAY, Color.PINK, "Transfo de "+(transfo)+" T de "+fb+" en "+transfo*conversionb+" T de "+cb);
					this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN," stock("+fb+")->"+this.stockFeves.get(fb));
					this.journal.ajouter(COLOR_LLGRAY, COLOR_BROWN," stock("+cm+")->"+this.stockChocoMarque.get(cm));
					this.qteTransfo=transfo;
				}
			}
			
		}
		Feve fh = Feve.F_HQ_BE;
		Chocolat ch = Chocolat.C_HQ_BE;
		aVendreHQ=0;
		for (ExemplaireContratCadre c : ContratEnCours_C_HQ ) {
			aVendreHQ+=c.getQuantiteALivrerAuStep();
		}
		
		double transfoh = (int) (Math.min(this.stockFeves.get(fh), Math.random()*1000+(aVendreHQ)/1.06));
		
		for (ChocolatDeMarque c : this.stockChocoMarque.keySet()) {
			if (c.getGamme().equals(Gamme.HQ)) {
				if(this.stockChocoMarque.get(c)>20000) {
					transfoh=0;
				}
			}
		}
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
	
