package abstraction.eq3Producteur3;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
 
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;
import abstraction.eqXRomu.produits.Feve;

public class Producteur3Stock {
	private HashMap<String,Lot> Stock;
	
	/**
	 * @author BOCQUET Gabriel, NAVEROS Marine
	 
	public Producteur3Stock() {
		//IProduit FM = new Feve(Gamme.MQ, true); //Je ne sais pas comment utiliser les feves
		//IProduit HM = new Feve(Gamme.HQ, true); //Je ne sais pas comment utiliser les feves
		Lot LotInitialM = new Lot(FM);
		Lot LotInitialH = new Lot(HM);
		LotInitialM.ajouter(0,1000000); //J'ai mis une quantite au pif a changer
		LotInitialH.ajouter(0,1000000); //J'ai mis une quantite au pif a changer
		HashMap<String,Lot> s = new HashMap<String,Lot>();
		s.put("H", LotInitialH);
		s.put("M", LotInitialM);
		this.Stock= s;
	}
	*/
	
	/**
	 * @author BOCQUET Gabriel, NAVEROS Marine
	 */
	public HashMap<String,Lot> getStock(){
		return this.Stock;
	}
	
	/**
	 * @author BOCQUET Gabriel, NAVEROS Marine
	 */
	public void ajouterH(int step, double quantite) {
		HashMap<String,Lot> Stock = this.getStock();
		Stock.get("H").ajouter(step, quantite);
	}
	
	/**
	 * @author BOCQUET Gabriel, NAVEROS Marine
	 */
	public void ajouterM(int step, double quantite) {
		HashMap<String,Lot> Stock = this.getStock();
		Stock.get("M").ajouter(step, quantite);
	}
	

	public double getQuantiteTotM() {
		return this.getStock().get("M").getQuantiteTotale();
	}
	
	public double getQuantiteTotH() {
		return this.getStock().get("H").getQuantiteTotale();
	}
	
	public double CoutStockageM() {
		return this.getQuantiteTotM()*50; //Chiffre a changer pour le prix
	}
	
	public double CoutStockageH() {
		return this.getQuantiteTotH()*50; //Chiffre a changer pour le prix
	}
	}
