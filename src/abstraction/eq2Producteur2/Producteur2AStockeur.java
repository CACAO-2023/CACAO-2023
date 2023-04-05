package abstraction.eq2Producteur2;

//code écrit par Nathan

import java.util.HashMap;

import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class Producteur2AStockeur extends Producteur2Acteur {
	
	protected Lot stockFevesBasse;
	protected Lot stockFevesMoy;
	protected Lot stockFevesMoyBE;
	protected Lot stockFevesHauteBE;
	
	public Producteur2AStockeur() {
		super();
	}
	
	public void initialiser() {
		super.initialiser();
		this.stockFevesBasse = new Lot(Feve.F_BQ);
		this.stockFevesMoy = new Lot(Feve.F_MQ);
		this.stockFevesMoyBE = new Lot(Feve.F_MQ_BE);
		this.stockFevesHauteBE = new Lot(Feve.F_HQ_BE);
		
		this.stockFevesBasse.ajouter(0, 10000);
		this.stockFevesMoy.ajouter(0, 10000);
		this.stockFevesMoyBE.ajouter(0, 10000);
		this.stockFevesHauteBE.ajouter(0, 10000);
		
		this.stockTotBasse = new VariablePrivee("stockTotBasse", "Stock total de fèves de basse qualité", this, this.stockFevesBasse.getQuantiteTotale());
		this.stockTotMoy = new VariablePrivee("stockTotMoy", "Stock total de fèves de moyenne qualité", this, this.stockFevesMoy.getQuantiteTotale());
		this.stockTotMoyBE = new VariablePrivee("stockTotMoyBE", "stock Total de fèves de moyenne qualité bio-équitable", this, this.stockFevesMoyBE.getQuantiteTotale());
		this.stockTotHauteBE = new VariablePrivee("stockTotHauteBE", "stock Total de fèves de haute qualité bio-équitable", this, this.stockFevesHauteBE.getQuantiteTotale());
	}
	
	public void next() {
		super.next();
	}
}
