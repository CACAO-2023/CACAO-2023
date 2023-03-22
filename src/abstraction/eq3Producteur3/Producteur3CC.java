package abstraction.eq3Producteur3;

import java.util.LinkedList;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Producteur3CC extends Producteur3Acteur implements IVendeurContratCadre {
    protected LinkedList<ExemplaireContratCadre> contracts;
	protected Stock stock;
    
    /**
     * @author Corentin Caugant
     */
    public Producteur3CC(Stock stock) {
        super();
        this.stock = stock;
        this.contracts = new LinkedList<ExemplaireContratCadre>();
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
        return 100.0;
    }

    /**
     * @author Corentin Caugant
     */
    public boolean vend(IProduit produit) {
        LinkedList<String> produits = this.getStock().getProduits();
        for (int i = 0; i < produits.size(); i++) {
            if (produits.get(i).equals(produit.getType())) {
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

        int oldestStep = this.getStock().getOldestStep();
        double currentQuantite;
        if (this.getStock().getQuantite() >= quantite) {
            currentQuantite = quantite;
        } else {
            currentQuantite = this.getStock().getQuantite(); // ! Prepare for trouble, and make it double !
        }

        lot.ajouter(oldestStep, currentQuantite);
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
        if (echeancier.getQuantiteAPartirDe(contrat.getEcheancier().getStepDebut()) > this.getStock().getQuantite()) {
            echeancier.ajouter(this.getStock().getQuantite()*echeancier.getNbEcheances());
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

    private Stock getStock() {
        return this.stock;
    }
}
