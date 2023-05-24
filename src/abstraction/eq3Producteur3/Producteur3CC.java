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
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Producteur3CC extends Producteur3Acteur implements IVendeurContratCadre {
    protected LinkedList<ExemplaireContratCadre> contracts;
    protected SuperviseurVentesContratCadre superviseur;

    // On va garder une trace de la fiabilité de nos acheteurs
    protected HashMap<IAcheteurContratCadre, Integer> acheteursMQfiabilité;
    protected HashMap<IAcheteurContratCadre, Integer> acheteursHQfiabilité;

    // On va aussi conserver le prix de la dernière transaction avec chaque acheteur
    protected HashMap<IAcheteurContratCadre, Double> acheteursMQprix;
    protected HashMap<IAcheteurContratCadre, Double> acheteursHQprix;
    
    /**
     * @author Corentin Caugant
     */
    public Producteur3CC() {
        super();
        this.contracts = new LinkedList<ExemplaireContratCadre>();
        this.acheteursMQfiabilité = new HashMap<IAcheteurContratCadre, Integer>();
        this.acheteursHQfiabilité = new HashMap<IAcheteurContratCadre, Integer>();
        this.acheteursMQprix = new HashMap<IAcheteurContratCadre, Double>();
        this.acheteursHQprix = new HashMap<IAcheteurContratCadre, Double>();
    }

    /**
     * @author Corentin Caugant
     */
    public void initialiser() {
        super.initialiser();
        this.superviseur = (SuperviseurVentesContratCadre)Filiere.LA_FILIERE.getActeur("Sup.CCadre");

        // Initialisation des HashMaps. Au début tous nos acheteurs ont la même fiabilité.
        Double PRIX_DEPART_MQ = 50000.0;
        Double PRIX_DEPART_HQ = 100000.0;
        List<IAcheteurContratCadre> acheteursMQ = superviseur.getAcheteurs(Feve.F_MQ_BE);
        for (int i = 0; i < acheteursMQ.size(); i++) {
            this.acheteursMQfiabilité.put(acheteursMQ.get(i), 1);
            this.acheteursMQprix.put(acheteursMQ.get(i), PRIX_DEPART_MQ);
        }

        List<IAcheteurContratCadre> acheteursHQ = superviseur.getAcheteurs(Feve.F_HQ_BE);
        for (int i = 0; i < acheteursHQ.size(); i++) {
            this.acheteursHQfiabilité.put(acheteursHQ.get(i), 1);
            this.acheteursHQprix.put(acheteursHQ.get(i), PRIX_DEPART_HQ);
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
            int randomInt = (int)(Math.random() * reliabilitySum);

            // We iterate through the list of buyers until we find the one corresponding to the random number
            int currentSum = 0;
            int previousSum = 0;
            for (IAcheteurContratCadre acheteur : this.acheteursMQfiabilité.keySet()) {
                currentSum += this.acheteursMQfiabilité.get(acheteur);
                if (currentSum > randomInt && randomInt <= previousSum) {
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
     * @param acheteur The buyer we want to sell to
     * @param feve The type of beans we want to sell
     * @return The price we propose to the buyer, based on the type of beans and the buyer's last agreed price
     * @author Corentin Caugant
     */
    public double propositionPrixIntial(IAcheteurContratCadre acheteur, Feve feve) {
        if (feve == Feve.F_MQ_BE) {
            return Math.max(this.getPrixTonne() * 1.2, this.acheteursMQprix.get(acheteur) * 1.1);
        } else {
            return Math.max(this.getPrixTonne() * 1.4, this.acheteursHQprix.get(acheteur) * 1.3);
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
        Lot lot = new Lot(produit);

        int oldestStep = Stock.getAge((Feve)produit);
        double currentQuantite;
        if (Stock.retirer((Feve)produit, quantite)) {
            currentQuantite = quantite;
        } else {
            currentQuantite = Stock.getQuantite((Feve)produit); // ! Prepare for trouble, and make it double !
            journal_ventes.ajouter(Color.RED, Color.WHITE, "Attention, rupture de stock de " + contrat.getProduit() + ". Quantité livrée/quantité demandée : " + currentQuantite + "/" + quantite + ".");
            Stock.retirer((Feve)produit, currentQuantite);
        }

        if (currentQuantite > 0) {
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

        // We rework the echeancier to fit the stock
        if (echeancier.getQuantiteAPartirDe(contrat.getEcheancier().getStepDebut()) > Stock.getQuantite((Feve)contrat.getProduit())) {
            echeancier.ajouter(Math.max(SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER, Stock.getQuantite((Feve)contrat.getProduit())));
        }

        return echeancier;
    }

    /** 
     * @author Corentin Caugant
     */
    public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
        this.getContracts().add(contrat);
        // Ajout de la quantite vendu dans la liste gardant une trace des dernières quantités vendus
        super.addVenteQuantite(contrat.getQuantiteTotale(), (Feve)contrat.getProduit());

        // Mise à jour de la fiabilité du client
        if ((Feve)contrat.getProduit() == Feve.F_MQ_BE) {
            this.acheteursMQfiabilité.put((IAcheteurContratCadre)contrat.getAcheteur(), this.acheteursMQfiabilité.get((IAcheteurContratCadre)contrat.getAcheteur()) + 1);
            this.acheteursMQprix.put((IAcheteurContratCadre)contrat.getAcheteur(), contrat.getPrix());
        } else if ((Feve)contrat.getProduit() == Feve.F_HQ_BE) {
            this.acheteursHQfiabilité.put((IAcheteurContratCadre)contrat.getAcheteur(), this.acheteursHQfiabilité.get((IAcheteurContratCadre)contrat.getAcheteur()) + 1);
            this.acheteursHQprix.put((IAcheteurContratCadre)contrat.getAcheteur(), contrat.getPrix());
        }
        
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
        IAcheteurContratCadre acheteur = this.choisirClient(produit);

        // Now making the contract
        this.getJVente().ajouter(Color.LIGHT_GRAY, Color.BLACK, "Tentative de négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit + "...");
        int length = ((int) Math.round(Math.random() * 10)) + 1;
        ExemplaireContratCadre cc = superviseur.demandeVendeur(acheteur, this, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, length, (int) Math.round(this.getStock().getQuantite(produit)/length)), cryptogramme,false);
        if (cc != null) {
            this.getJVente().ajouter(Color.LIGHT_GRAY, Color.BLACK, "Contrat cadre passé avec " + acheteur.getNom() + " pour " + produit + "\nDétails : " + cc + "!");
        } else {
            this.getJVente().ajouter(Color.LIGHT_GRAY, Color.BLACK, "Echec de la négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit + "...");
        }
        return cc;
    }

    /** 
     * @author Corentin Caugant
     */
    public void next() {
        super.next();
        if (this.getStock().getQuantite(Feve.F_HQ_BE) > 100) {
            this.getContractForProduct(Feve.F_HQ_BE);
        }
        if (this.getStock().getQuantite(Feve.F_MQ_BE) > 100) {
            this.getContractForProduct(Feve.F_MQ_BE);
        }
    }
}
