package abstraction.eq8Distributeur2;

public class Stock {
    private double quantite;

    public Stock(double quantiteInitiale) {
        this.quantite = quantiteInitiale;
    }

    public double getQuantite() {
        return quantite;
    }

    public void ajouter(double quantiteAjoutee) {
        if (quantiteAjoutee > 0) {
            quantite += quantiteAjoutee;
        }
    }

    public boolean retirer(double quantiteRetiree) {
        if (quantiteRetiree > 0 && quantiteRetiree <= quantite) {
            quantite -= quantiteRetiree;
            return true;
        }
        return false;
    }
}
