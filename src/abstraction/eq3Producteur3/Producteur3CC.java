package abstraction.eq3Producteur3;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Producteur3CC extends Producteur3Acteur implements IVendeurContratCadre {
    protected LinkedList<ExemplaireContratCadre> contracts;
    protected SuperviseurVentesContratCadre superviseur;
    protected LinkedList<IAcheteurContratCadre> acheteursMQ;
    protected LinkedList<IAcheteurContratCadre> acheteursHQ;
    
    /**
     * @author Corentin Caugant
     */
    public Producteur3CC() {
        super();
        this.contracts = new LinkedList<ExemplaireContratCadre>();

        this.acheteursHQ = new LinkedList<IAcheteurContratCadre>();
        this.acheteursMQ = new LinkedList<IAcheteurContratCadre>();
    }

    /**
     * @author Corentin Caugant, Victor Dubus-Chanson
     */
    public void initialiser() {
        super.initialiser();
        this.superviseur = (SuperviseurVentesContratCadre)Filiere.LA_FILIERE.getActeur("Sup.CCadre");

        // Initialisation des HashMaps. Au début tous nos acheteurs ont la même fiabilité.
        Double PRIX_DEPART_MQ = 5000.0; //10000 avant, mais des équipes ne négocient pas
        Double PRIX_DEPART_HQ = 10000.0; //30000 avant, mais des équipes ne négocient pas

        List<IAcheteurContratCadre> acheteurs = new LinkedList<IAcheteurContratCadre>();
		List<IActeur> acteurs = Filiere.LA_FILIERE.getActeursSolvables();
		for (IActeur acteur : acteurs) {
			if (acteur instanceof IAcheteurContratCadre) {
				acheteurs.add(((IAcheteurContratCadre)acteur));
			}
		}
        
        List<IAcheteurContratCadre> acheteursMQ = superviseur.getAcheteurs(Feve.F_MQ_BE);
        for (int i = 0; i < acheteursMQ.size(); i++) {
            this.acheteursMQfiabilité.put(acheteursMQ.get(i), 1); 
        }

        List<IAcheteurContratCadre> acheteursHQ = superviseur.getAcheteurs(Feve.F_HQ_BE);
        for (int i = 0; i < acheteursHQ.size(); i++) {
            this.acheteursHQfiabilité.put(acheteursHQ.get(i), 1);
        }

        for (int i = 0; i < acheteurs.size(); i++) {
            this.acheteursMQprix.put(acheteurs.get(i), PRIX_DEPART_MQ);
            this.acheteursHQprix.put(acheteurs.get(i), PRIX_DEPART_HQ);
        }
    }


    
    /**
     * Chooses a client to sell our beans. We are more likely to choose the client with the highest reliability.
     * @param feve The type of beans we want to sell
     * @return The client we chose
     * @author Corentin Caugant
     */
    public IAcheteurContratCadre choisirClient(Feve feve) {
        if (feve == Feve.F_MQ_BE) {
            // We choose a random number between 0 and the sum of all the reliabilities
            int reliabilitySum = 0;
            for (IAcheteurContratCadre acheteur : this.acheteursMQfiabilité.keySet()) {
                reliabilitySum += this.acheteursMQfiabilité.get(acheteur);
            }
            double randomInt = Math.random() * reliabilitySum;
            // We iterate through the list of buyers until we find the one corresponding to the random number
            double currentSum = 0;
            double previousSum = 0;
            for (IAcheteurContratCadre acheteur : this.acheteursMQfiabilité.keySet()) {
                currentSum += this.acheteursMQfiabilité.get(acheteur);
                if (currentSum > randomInt && randomInt >= previousSum) {
                    return acheteur;
                }
                previousSum += this.acheteursMQfiabilité.get(acheteur);
            }
        } else {
            // We choose a random number between 0 and the sum of all the reliabilities
            int reliabilitySum = 0;
            for (IAcheteurContratCadre acheteur : this.acheteursHQfiabilité.keySet()) {
                reliabilitySum += this.acheteursHQfiabilité.get(acheteur);
            }
            int randomInt = (int)(Math.random() * reliabilitySum);

            // We iterate through the list of buyers until we find the one corresponding to the random number
            int currentSum = 0;
            int previousSum = 0;
            for (IAcheteurContratCadre acheteur : this.acheteursHQfiabilité.keySet()) {
                currentSum += this.acheteursHQfiabilité.get(acheteur);
                if (currentSum > randomInt && randomInt <= previousSum) {
                    return acheteur;
                }
                previousSum += this.acheteursHQfiabilité.get(acheteur);
            }
        }
        return superviseur.getAcheteurs(Feve.F_HQ_BE).get(0);
    }

    /**
     * @author Corentin Caugant
     */
    public double getPrixTonne() {
        return Math.max(this.CoutTonne, 1000.0);
    }

    /**
     * Returns the initial price we will propose to the buyer.
     * Does not increase the price because some teams just straight out do not negotiate and will keep buying at the increased price
     * @param acheteur The buyer we want to sell to
     * @param feve The type of beans we want to sell
     * @return The price we propose to the buyer, based on the type of beans and the buyer's last agreed price
     * @author Corentin Caugant, Victor Dubus-Chanson
     */
    public double propositionPrixIntial(IAcheteurContratCadre acheteur, Feve feve) {
        if (feve == Feve.F_MQ_BE) {
            double price = Math.max(this.getPrixTonne() /* * 1.2 */, this.acheteursMQprix.get(acheteur) /* * 1.1*/);
            System.out.println("Equipe 3 : acheteursMQprixget : " + this.acheteursMQprix.get(acheteur));
            this.acheteursMQprix.put(acheteur, price);
            return price;
        } else {
            double price = Math.max(this.getPrixTonne() /* * 1.4*/, this.acheteursHQprix.get(acheteur) /* * 1.3*/);
            System.out.println("Equipe 3 : acheteursHQprixget : " + this.acheteursHQprix.get(acheteur));
            this.acheteursHQprix.put(acheteur, price);
            return price;
        }
    }

    /**
     * @author Corentin Caugant
     */
    public double propositionPrix(ExemplaireContratCadre contrat) {
        return propositionPrixIntial(contrat.getAcheteur(), (Feve)contrat.getProduit());
    }

    /**
     * @author Corentin Caugant
     */
    public boolean vend(IProduit produit) {
        // We check if the product is in the list of products we can sell (i.e if it is a bean we have a stock of)
        if (produit.getType().equals("Feve") && ((Feve)produit == Feve.F_MQ_BE || (Feve)produit == Feve.F_HQ_BE)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author Corentin Caugant  
     */
    @Override
    public Lot livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {

        Lot lot =  new Lot(produit);

        int oldestStep = Stock.getAge((Feve)produit);
        double currentQuantite;
        if (Stock.retirer((Feve)produit, quantite)) {
            currentQuantite = quantite;
        } else {
            currentQuantite = Stock.getQuantite((Feve)produit); // ! Prepare for trouble, and make it double !
            journal_ventes.ajouter(Color.RED, Color.WHITE, "Attention, rupture de stock de " + contrat.getProduit() + ". Quantité livrée/quantité demandée : " + currentQuantite + "/" + quantite + ".");
            Stock.retirer((Feve)produit, currentQuantite);

            // On modifie également notre variable marge de Stockage, pour essayer d'éviter que cette rupture se reproduise
            double marge = this.margeStockage.getValeur();
            if (marge < 0.3) { // On ne veut pas que la marge soit trop grande
                this.margeStockage.setValeur(this, marge + 0.01, this.cryptogramme);
            }
        }

        if (currentQuantite > 0.0) {
            lot.ajouter(oldestStep, currentQuantite);
        }
        
        return lot;
    }

    /** 
     * @author Corentin Caugant
     */
    public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
        if ((Feve)contrat.getProduit() == Feve.F_MQ_BE) {
            // First we choose the price we agree to go down to
            Double prixAcceptable = Math.max(this.acheteursMQprix.get(contrat.getAcheteur()) - this.acheteursMQprix.get(contrat.getAcheteur()) * ((Math.random() * 0.05) + 0.05), this.getPrixTonne() * 1.1); // Between 5% and 10% less than the last agreed price
            if (contrat.getPrix() >= prixAcceptable) {
                return contrat.getPrix();
            } else {
                this.acheteursMQprix.put(contrat.getAcheteur(), prixAcceptable); // We update the price we are willing to sell at
                return prixAcceptable;
            }
        } else {
            // First we choose the price we agree to go down to
            Double prixAcceptable = Math.max(this.acheteursHQprix.get(contrat.getAcheteur()) - this.acheteursHQprix.get(contrat.getAcheteur()) * ((Math.random() * 0.05) + 0.025), this.getPrixTonne() * 1.2); // Between 2.5% and 7.5% less than the last agreed price
            if (contrat.getPrix() >= prixAcceptable) {
                return contrat.getPrix();
            } else {
                this.acheteursHQprix.put(contrat.getAcheteur(), prixAcceptable); // We update the price we are willing to sell at
                return prixAcceptable;
            }
        }
    }

    /** 
     * @author Corentin Caugant
     */
    @Override
    public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
        Echeancier echeancier = contrat.getEcheancier();
        if (this.getAvailableQuantity((Feve)contrat.getProduit()) <= 0) {
            return null;
        }

        // We rework the echeancier to fit the stock
        if (echeancier.getQuantiteTotale() > this.getAvailableQuantity((Feve)contrat.getProduit())) {
            echeancier.ajouter(this.getAvailableQuantity((Feve)contrat.getProduit())/(echeancier.getStepFin() - echeancier.getStepDebut()));
        }

        return echeancier;
    }

    /** 
     * @author Corentin Caugant
     */
    public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
        this.contracts.add(contrat);
        // Ajout de la quantite vendu dans la liste gardant une trace des dernières quantités vendus
        super.addVenteQuantite(contrat.getQuantiteTotale(), (Feve)contrat.getProduit());

        // Mise à jour de la fiabilité du client
        if ((Feve)contrat.getProduit() == Feve.F_MQ_BE) {
            this.acheteursMQfiabilité.put((IAcheteurContratCadre)contrat.getAcheteur(), this.acheteursMQfiabilité.get((IAcheteurContratCadre)contrat.getAcheteur()) + 1);
            this.acheteursMQprix.put((IAcheteurContratCadre)contrat.getAcheteur(), contrat.getPrix());
            this.acheteursMQ.add((IAcheteurContratCadre)contrat.getAcheteur());
        } else if ((Feve)contrat.getProduit() == Feve.F_HQ_BE) {
            this.acheteursHQfiabilité.put((IAcheteurContratCadre)contrat.getAcheteur(), this.acheteursHQfiabilité.get((IAcheteurContratCadre)contrat.getAcheteur()) + 1);
            this.acheteursHQprix.put((IAcheteurContratCadre)contrat.getAcheteur(), contrat.getPrix());
            this.acheteursHQ.add((IAcheteurContratCadre)contrat.getAcheteur());
        }
        this.getJVente().ajouter(Color.LIGHT_GRAY, Color.BLACK, "Contrat cadre passé avec " + contrat.getAcheteur().getNom() + " pour " + contrat.getProduit() + "\nDétails : " + contrat + "!");
    }

    private LinkedList<ExemplaireContratCadre> getContracts() {
        return this.contracts;
    }

    /**
     * This method will try finding a contract for a given product
     * @param produit The product we want to sell
     * @return The contract if found, null otherwise
     * @author Corentin Caugant
     */
    public ExemplaireContratCadre getContractForProduct(Feve produit) {
        // First we need to select a buyer for the product
        this.getJVente().ajouter(Color.LIGHT_GRAY, Color.BLACK, "Recherche acheteur pour " + produit + "...");
        IAcheteurContratCadre acheteur;
        do {
            acheteur = this.choisirClient(produit);
        } while ((produit == Feve.F_HQ_BE && (this.acheteursHQ.contains(acheteur) && this.acheteursHQ.size() < this.acheteursHQfiabilité.size())) || (produit == Feve.F_MQ_BE && this.acheteursMQ.contains(acheteur) && this.acheteursMQ.size() < this.acheteursMQfiabilité.size()));

        // Now making the contract
        this.getJVente().ajouter(Color.LIGHT_GRAY, Color.BLACK, "Tentative de négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit + "...");
        int length = ((int) Math.round(Math.random() * 10)) + 2;
        ExemplaireContratCadre cc = superviseur.demandeVendeur(acheteur, this, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, length,(((int) Math.round(this.getAvailableQuantity(produit)/length))) + 1), cryptogramme,false);
        if (cc != null) {

            this.contracts.add(cc);
            if ((Feve)cc.getProduit() == Feve.F_MQ_BE) {
                this.acheteursMQfiabilité.put((IAcheteurContratCadre)cc.getAcheteur(), this.acheteursMQfiabilité.get((IAcheteurContratCadre)cc.getAcheteur()) + 1);
                this.acheteursMQprix.put((IAcheteurContratCadre)cc.getAcheteur(), cc.getPrix());
                this.acheteursMQ.add((IAcheteurContratCadre)cc.getAcheteur());
            } else if ((Feve)cc.getProduit() == Feve.F_HQ_BE) {
                this.acheteursHQfiabilité.put((IAcheteurContratCadre)cc.getAcheteur(), this.acheteursHQfiabilité.get((IAcheteurContratCadre)cc.getAcheteur()) + 1);
                this.acheteursHQprix.put((IAcheteurContratCadre)cc.getAcheteur(), cc.getPrix());
                this.acheteursHQ.add((IAcheteurContratCadre)cc.getAcheteur());
            }

            this.getJVente().ajouter(Color.LIGHT_GRAY, Color.BLACK, "Contrat cadre passé avec " + acheteur.getNom() + " pour " + produit + "\nDétails : " + cc + "!");
        } else {
            if (produit == Feve.F_MQ_BE) {
                this.acheteursMQ.add(acheteur);
            } else if (produit == Feve.F_HQ_BE) {
                this.acheteursHQ.add(acheteur);
            }
            this.getJVente().ajouter(Color.LIGHT_GRAY, Color.BLACK, "Echec de la négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit + "...");
        }
        return cc;
    }

    /** 
     * @author Corentin Caugant
     */
    public void next() {
        super.next();
        //Pour la suite, on abesoin de savoir les contrats qui ont ete effectue au step precedent
        LinkedList<ExemplaireContratCadre> contraprecedent = new LinkedList<ExemplaireContratCadre>();
        contraprecedent.addAll(this.contracts);
        this.contractprecedent = contraprecedent;
        List<ExemplaireContratCadre> contratsObsoletes=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.contracts) {
			if (contrat.getQuantiteRestantALivrer()<=0.0 && contrat.getMontantRestantARegler()<=0.0) {
				contratsObsoletes.add(contrat);
            }
		}
		this.contracts.removeAll(contratsObsoletes);



        for (int i = 0; i < 5; i++) {
            if (this.getAvailableQuantity(Feve.F_HQ_BE) > 100) {
                this.getContractForProduct(Feve.F_HQ_BE);
            }
            if (this.getAvailableQuantity(Feve.F_MQ_BE) > 100) {
                this.getContractForProduct(Feve.F_MQ_BE);
            }
        }

        this.acheteursHQ = new LinkedList<IAcheteurContratCadre>();
        this.acheteursMQ = new LinkedList<IAcheteurContratCadre>();
    }

    /**
     * Returns the quantity of beans available for a given quality.
     * This method takes into account ongoing CCs and the stock to compute the quantity available for sale accurately.
     * @author Corentin Caugant
     */
    public double getAvailableQuantity(Feve qualite) {
        double available = this.getStock().getQuantite(qualite);
        for (ExemplaireContratCadre cc : this.getContracts()) {
            if ((Feve)cc.getProduit() == qualite) {
                if (cc.getQuantiteRestantALivrer() >= 0) {
                    available -= cc.getQuantiteRestantALivrer();
                }
            }
        }
        
        double SAFE_MARGIN = this.margeStockage.getValeur(); // Percentage of the stock we want to keep
        return Math.min(Math.max(available * (1 - SAFE_MARGIN), 0.0), 100000);
    }
}
