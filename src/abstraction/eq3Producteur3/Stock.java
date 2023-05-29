package abstraction.eq3Producteur3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	 * @author Naveros Marine
	 */
	public Stock(Feve f, Lot l) {
		this.stock = new HashMap<Feve, Lot>();
		this.stock.put(f,l);
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
	 * This method will set the lot of beans of the type feve in the stock
	 * @param feve Type of bean
	 * @param lot Lot of beans of the type feve to set in the stock
	 * @author Corentin Caugant
	 */
	public void setLot(Feve feve, Lot lot) {
		this.stock.put(feve, lot);
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
		if (this.getQuantite(feve) >= quantite && quantite > 0) {
			this.stock.get(feve).retirer(quantite);
			return true;
		}
		return false;
	}
	
	/**
	 * Reprise de Lot.retirer pour retirer les feves d'un lot en commençant par les plus vielles
	 * @author BOCQUET Gabriel
	 */
	public Lot retirerVielleFeve(Feve f, double quantite) {
		if (quantite<=0.0 || quantite>this.getQuantite(f)+0.001) {
			throw new IllegalArgumentException("Essaie de retirer ("+quantite+") de " + f.toString() + " alors que les stocks sont de " + this.getQuantite(f));
		} else {
			Lot res=new Lot(f);
			List<Integer> vides = new LinkedList<Integer>();
			Set<Integer> s = this.getStock().get(f).getQuantites().keySet();
			List<Integer> keyList = new ArrayList<Integer>(s);
			Collections.sort(keyList);
			Collections.reverse(keyList);
			double reste = quantite;
			for (Integer i : keyList) {
				if (reste > 0.0) {
					if (this.getStock().get(f).getQuantites().get(i)>reste) {
						res.ajouter(i,reste);
						this.getStock().get(f).getQuantites().put(i,this.getStock().get(f).getQuantites().get(i)-reste);
						reste=0;
					} else {
						res.ajouter(i,this.getStock().get(f).getQuantites().get(i));
						reste = reste - this.getStock().get(f).getQuantites().get(i);
						vides.add(i);
					}
				}
			}
			for (Integer step : vides) {
				this.getStock().get(f).getQuantites().remove(step);
			}
			return res;
		}
	}
	/** 
	 * This method will at each next handles the beans that are too old
	 * @param stock The stock to update
	 * @return The updated stock
	 * @author Corentin Caugant
	 */
	public Stock miseAJourStock() {
		Stock stock = this;
		Stock newStock = new Stock(1);

		// First we begin by creating the new lots that will be added to the stock
		HashMap<Feve, Lot> newLots = new HashMap<Feve, Lot>();
		for (Feve f : Feve.values()) {
			newLots.put(f, new Lot(f));
		}

		// Then we loop through each bean type and we will change the quantities
		for (Map.Entry<Feve , Lot> entry : stock.getStock().entrySet()) {
			// We get the type of bean and the lot of beans of this type
			Feve f = (Feve)entry.getKey();
			Lot lot = (Lot)entry.getValue();
			HashMap<Integer, Double> quantite = lot.getQuantites();

			Lot newLot = newLots.get(f); // We will add to this lot the beans that are not too old
			for (Integer creationStep : quantite.keySet()) {
				if (quantite.get(creationStep) > 0) {;
					Integer age = Filiere.LA_FILIERE.getEtape() - creationStep;

					// We update the stock according to the age of these beans
					switch (age) {
						case 18: // If the beans are 18 steps old (9 months), we won't add them to the new lot
							break;
						case 12: // If the beans are 12 steps old (6 months), we lower their quality by one level
							// What we do will depend of the quality of the beans :
							switch (f) {
								case F_BQ: // If the beans are of the lowest quality, we remove them completely
									break;
								case F_MQ: // If the beans are of the medium quality, we lower their quality to the lowest quality
									newLots.get(Feve.F_BQ).ajouter(creationStep, quantite.get(creationStep)); // We add the beans to the lower quality stock
									break;
								case F_MQ_BE: // If the beans are of the medium quality, we lower their quality to the lowest quality
									newLots.get(Feve.F_BQ).ajouter(creationStep, quantite.get(creationStep)); // We add the beans to the lower quality stock
									break;
								case F_HQ_BE: // If the beans are of the highest quality, we lower their quality to the medium quality
									newLots.get(Feve.F_MQ_BE).ajouter(creationStep, quantite.get(creationStep)); // We add the beans to the lower quality stock
									break;
							}
							break;
						default: // If the beans are less than 6 months old, we add them to the new lot
							newLot.ajouter(creationStep, quantite.get(creationStep));
							break;
					}
				}
				
			}
		}
		
		// Finally we add the new lots to the stock
		for (Feve f : Feve.values()) {
			newStock.setLot(f, newLots.get(f));
		}
		return newStock;
	}
	
	/**
	 * This method will return the age of the beans of a given type
	 * @param feve Type of bean
	 * @return The age of the beans of the given type
	 * @author Caugant Corentin
	 */
	public int getAge(Feve feve) {
		if (this.stock.get(feve).getQuantites().keySet().size() > 0) {
			return this.stock.get(feve).getQuantites().keySet().iterator().next();
		} else {
			return Filiere.LA_FILIERE.getEtape();
		}
			
	}
}