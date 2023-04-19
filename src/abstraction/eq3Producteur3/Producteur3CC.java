package abstraction.eq3Producteur3;

import java.awt.Color;
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
    
    /**
     * @author Corentin Caugant
     */
    public Producteur3CC() {
        super();
        this.contracts = new LinkedList<ExemplaireContratCadre>();
        
    }

    public void initialiser() {
        super.initialiser();
        this.superviseur = (SuperviseurVentesContratCadre)Filiere.LA_FILIERE.getActeur("Sup.CCadre");
    }

    /**
     * @author Corentin Caugant
     */
    public double getPrixTonne() {
        return Math.max(this.CoutTonne, 1000.0);
    }

    /**
     * @author Corentin Caugant
     */
    public double propositionPrix(ExemplaireContratCadre contrat) {
        if ((Feve)contrat.getProduit() == Feve.F_MQ_BE) {
            // We return a price being CoutTonne multiplied by a random factor between 1.1 and 1.2
            return this.getPrixTonne() * 1.3;
        } else {
            return this.getPrixTonne() * 1.6;
        }
    }

    /**
     * @author Corentin Caugant
     */
    public boolean vend(IProduit produit) {
        // We check if the product is in the list of products we can sell (i.e if it is a bean we have a stock of)
        for (Feve f : Feve.values()) {
            if (produit.getType().equals(f.getType()) && Stock.getQuantite((Feve)produit) > 0) {
                return true;
            }
        }
        return false;
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
            // We return a price weing CoutTonne multiplied by a random factor between 1.1 and 1.2
            if (contrat.getPrix() >= this.getPrixTonne() * 1.1) {
                return contrat.getPrix();
            } else {
                return this.getPrixTonne() * (1.1 + Math.random() * 0.1);
            }
        } else {
            if (contrat.getPrix() >= this.getPrixTonne() * 1.2) {
                return contrat.getPrix();
            } else {
                return this.getPrixTonne() * (1.2 + Math.random() * 0.3);
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
            echeancier.ajouter(Stock.getQuantite((Feve)contrat.getProduit()));
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
        List<IAcheteurContratCadre> acheteurs = superviseur.getAcheteurs(produit);
        IAcheteurContratCadre acheteur = acheteurs.get((int)(Math.random() * acheteurs.size()));

        // Now making the contract
        this.getJVente().ajouter(Color.LIGHT_GRAY, Color.BLACK, "Tentative de négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit + "...");
        int length = ((int) Math.round(Math.random() * 10)) + 1;
        ExemplaireContratCadre cc = superviseur.demandeVendeur(acheteur, this, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, length, (int) Math.round(SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER+this.getStock().getQuantite(produit)/length)), cryptogramme,false);
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
        this.getContractForProduct(Feve.F_HQ_BE);
        this.getContractForProduct(Feve.F_MQ_BE);
    }
}
