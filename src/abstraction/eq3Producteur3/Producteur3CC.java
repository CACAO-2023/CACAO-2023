package abstraction.eq3Producteur3;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

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
    public double propositionPrix(ExemplaireContratCadre contrat) {
        return this.getPrixMin();
    }

    /**
     * @author Corentin Caugant
     */
    private double getPrixMin() {
        // return this.lot.getPrixMin();
        return 1.0;
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
        return this.getPrixMin();
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

        // We stop negociations if it lasts too long
        if (echeancier.getNbEcheances() > 12) {
            return null;
        } else {
            return echeancier;
        }
    }

    /** 
     * @author Corentin Caugant
     */
    public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
        this.getContracts().add(contrat);
        
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
        ExemplaireContratCadre cc = superviseur.demandeVendeur(acheteur, this, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, (SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER+10.0)/10), cryptogramme,false);
        if (cc != null) {
            this.getJVente().ajouter(Color.LIGHT_GRAY, Color.BLACK, "Contrat cadre passé avec " + acheteur.getNom() + " pour " + produit + "\nDétails : " + cc + "!");
        } else {
            this.getJVente().ajouter(Color.LIGHT_GRAY, Color.BLACK, "Echec de la négociation de contrat cadre avec " + acheteur.getNom() + " pour " + produit + "...");
        }
        return cc;
    }

    public void next() {
        super.next();
        this.getContractForProduct(Feve.F_HQ_BE);
        this.getContractForProduct(Feve.F_MQ_BE);
    }
}
