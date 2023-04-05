package abstraction.eq3Producteur3;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Lot;
import abstraction.eqXRomu.produits.Feve;

public class Stock {
	private HashMap<Feve , Lot> stock;
	
	/**
	 * Constructor of the class Stock, will create a stock with 1000 beans of each type at the beginning of the game
	 * @author BOCQUET Gabriel, NAVEROS Marine, Corentin Caugant
	*/
	public Stock() {
		// Calling the constructor with the number of beans of the beginning
		this(1000);
	}

	/**
	 * @param fevesDeDepart Number of beans at the beginning of the game (1000 by default) for each type of bean
	 * @author Corentin Caugant
	 */
	public Stock(Integer fevesDeDepart) {
		this.stock = new HashMap<Feve, Lot>();
		for (Feve feve : Feve.values()) {
			Lot lot = new Lot(feve);
			lot.ajouter(0, fevesDeDepart);
			this.stock.put(feve, lot);
		}
	}

	/**
	 * @author BOCQUET Gabriel, NAVEROS Marine, Corentin Caugant
	 */
	public HashMap<Feve , Lot> getStock() {
		return this.stock;
	}

	/**
	 * This method will return the number of beans of the type feve in the stock
	 * @param feve Type of bean
	 * @return The number of beans of the type feve in the stock
	 * @author BOCQUET Gabriel, NAVEROS Marine, Corentin Caugant
	 */
	public double getQuantite(Feve feve) {
		return this.stock.get(feve).getQuantiteTotale();
	}

	/**
	 * This method will return the total number of beans in the stock
	 * @return Total number of beans in the stock
	 * @author Corentin CAUGANT
	 */
	public double getQuantite() {
		double quantite = 0;
		for (Feve feve : Feve.values()) {
			quantite += this.getQuantite(feve);
		}
		return quantite;
	}

	/**
	 * This method will add beans to the stock
	 * @param feve Type of bean
	 * @param quantite Quantity of beans to add to the stock
	 * @author Corentin Caugant
	 */
	public void ajouter(Feve feve, double quantite) {
		this.stock.get(feve).ajouter(Filiere.LA_FILIERE.getEtape(), quantite);
	}

	/**
	 * This method will add beans to the stock, without specifying the type of bean. It will consider that the beans are of the lowest quality
	 * @param quantite Quantity of beans to add to the stock
	 * @author Corentin Caugant
	 */
	public void ajouter(double quantite) {
		this.ajouter(Feve.F_BQ, quantite);
	}

	/**
	 * This method will tell if the stock is empty or not, for a given type of bean
	 * @param feve Type of bean
	 * @return True if the stock is empty, false otherwise
	 * @author Corentin Caugant
	 */
	public boolean estVide(Feve feve) {
		return this.getQuantite(feve) == 0;
	}

	/**
	 * This method will tell if the stock is empty or not, for all types of beans
	 * @return True if the stock is empty, false otherwise
	 * @author Corentin Caugant
	 */
	public boolean estVide() {
		return this.getQuantite() == 0;
	}

	/**
	 * This method will remove beans of a given type from the stock
	 * @param feve Type of bean
	 * @param quantite Quantity of beans to remove from the stock
	 * @return True if the operation was successful, false otherwise (not enough beans in the stock)
	 * @author Corentin Caugant
	 */
	public boolean retirer(Feve feve, double quantite) {
		if (this.getQuantite(feve) >= quantite) {
			this.stock.get(feve).retirer(quantite);
			return true;
		}
		return false;
	}
	
	/** 
	 * This method will at each next handles the beans that are too old
	 * @param stock The stock to update
	 * @return The updated stock
	 */
	public static Stock miseAJourStock(Stock stock) {
		// We begin by looping through each bean type
		Stock newStock = new Stock(0);
		return newStock;
		/**
		 HashMap<Feve, HashMap<Integer, Double>> quantites = new HashMap<Feve, HashMap<Integer, Double>>();
		for (Feve f : Feve.values()) {
			// We get the lot of beans of the type f
			Lot lot = stock.getStock().get(f);

			// We get the map of the quantities of beans of the type f
			HashMap<Integer, Double> quantite = lot.getQuantites();
			quantites.put(f, quantite); // We add the map to the list of maps, to be able to loop through it later
		}

		// We loop through the list of maps
		for (var entry : quantites.entrySet()) {
			// We get the type of bean and the map of quantities
			Feve f = entry.getKey();
			HashMap<Integer, Double> quantite = entry.getValue();
			// We loop through the map of quantities
			for (Integer creationStep : quantite.keySet()) {
				Integer age = Filiere.LA_FILIERE.getEtape() - creationStep;

				// We update the stock according to the age of these beans
				switch (age) {
					case 18: // If the beans are 18 steps old (9 months), we remove them completely
						quantite.remove(creationStep);
						break;
					case 12: // If the beans are 12 steps old (6 months), we lower their quality by one level
						// What we do will depend of the quality of the beans :
						switch (f) {
							case F_BQ: // If the beans are of the lowest quality, we remove them completely
								quantite.remove(creationStep);
								break;
							case F_MQ: // If the beans are of the medium quality, we lower their quality to the lowest quality
								quantite.remove(creationStep); // We remove the beans from the stock
								Double newQuantity1 = quantite.get(creationStep) + quantites.get(Feve.F_BQ).get(creationStep); // We get the quantity of beans of the lower quality
								quantites.get(Feve.F_BQ).put(creationStep, newQuantity1); // We add the beans to the lower quality stock
								break;
							case F_MQ_BE: // If the beans are of the medium quality, we lower their quality to the lowest quality
								quantite.remove(creationStep);
								Double newQuantity2 = quantite.get(creationStep) + quantites.get(Feve.F_BQ).get(creationStep);
								quantites.get(Feve.F_BQ).put(creationStep, newQuantity2);
								break;
							case F_HQ_BE: // If the beans are of the highest quality, we lower their quality to the medium quality
								quantite.remove(creationStep);
								Double newQuantity3 = quantite.get(creationStep) + quantites.get(Feve.F_MQ_BE).get(creationStep);
								quantites.get(Feve.F_MQ_BE).put(creationStep, newQuantity3);
								break;
						}
			}
			// We rebuild the correspond Lot object
			Lot newLot = new Lot(f);
			
		}

		// We update the stock


		return newStock;
	}
		 */
		
	}
}