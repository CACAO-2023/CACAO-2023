package abstraction.eqXRomu.contratsCadres;

import java.util.List;


// Version en simple consultation (non modifiable) d'un contrat cadre
public final class ExemplaireContratCadre {
	private ContratCadre contratCadre;
	
	public ExemplaireContratCadre(ContratCadre contratCadre) {
		this.contratCadre = contratCadre;
	}
	
	/**
	 * @return Retourne le numero unique designant le contrat
	 */
	public long getNumero() {
		return this.contratCadre.getNumero();
	}

	/**
	 * @return Retourne l'acheteur precise sur le contrat cadre
	 */
	public IAcheteurContratCadre getAcheteur() {
		return this.contratCadre.getAcheteur();
	}

	/**
	 * @return Retourne le vendeur indique sur le contrat cadre
	 */
	public IVendeurContratCadre getVendeur() {
		return this.contratCadre.getVendeur();
	}

	/**
	 * @return Retourne le produit indique sur le contrat cadre
	 */
	public Object getProduit() {
		return this.contratCadre.getProduit();
	}
	
	/**
	 * @return Retourne true si le produit est du chocolat de marque que le vendeur s'engage a vendre en tete de gondole
	 */
	public boolean getTeteGondole() {
		return this.contratCadre.getTeteGondole();
	}

	/**
	 * @return Retourne la quantite totale (la somme des quantites prevues a l'echeancier)
	 */
	public Double getQuantiteTotale() {
		return this.contratCadre.getQuantiteTotale();
	}

	/**
	 * @return Retourne la somme qu'il reste a regler par l'acheteur avant la fin du contrat
	 */
	public double getMontantRestantARegler() {
		return this.contratCadre.getMontantRestantARegler();
	}

	/**
	 * @return Retourne la quantite totale que le vendeur doit encore livrer avant la fin du contrat
	 */
	public double getQuantiteRestantALivrer() {
		return this.contratCadre.getQuantiteRestantALivrer();
	}

	/**
	 * @return Retourne la liste des echeanciers. Le premier (get(0)) correspond a l'echeancier
	 * propose intialiement par l'acheteur, le second la contreproposition faites par le vendeur, 
	 * ...
	 */
	public List<Echeancier> getEcheanciers() {
		return this.contratCadre.getEcheanciers();
	}

	/**
	 * @return Retourne le dernier echeancier (c'est sur lui que porte le contrat, les precedents
	 * ne sont que des etapes de negociations).
	 */
	public Echeancier getEcheancier() {
		return this.contratCadre.getEcheancier();
	}

	/**
	 * @return Retourne un echeancier forme des livraisons effectuees par le vendeur
	 * dans le cadre de ce contrat
	 */
	public Echeancier getQuantiteLivree() {
		return this.contratCadre.getQuantiteLivree();
	}

	/**
	 * @return Retourne un echeancier forme des paiements effectues par l'acheteur dans 
	 * le cadre de ce contrat
	 */
	public Echeancier getPaiementsEffectues() {
		return this.contratCadre.getPaiementsEffectues();
	}

	/**
	 * @return Retourne la quantite que le vendeur doit livrer a l'etape courante
	 */
	public double getQuantiteALivrerAuStep() {
		return this.contratCadre.getQuantiteALivrerAuStep();
	}

	/**
	 * @return Retourne le montant que l'acheteur doit regler a l'etape courant dans 
	 * le cadre de ce contrat
	 */
	public double getPaiementAEffectuerAuStep() {
		return this.contratCadre.getPaiementAEffectuerAuStep();
	}

	/**
	 * @return Retourne la liste des prix. Le premier element est la proposition 
	 * initiale faite par le vendeur, le second la contreproposition faite par l'acheteur, ...
	 */
	public List<Double> getListePrix() {
		return this.contratCadre.getListePrix();
	}

	/**
	 * @return Retourne le prix (la derniere valeur de la liste getListePrix, les 
	 * premiere valeurs de cette liste n'etant que les etapes de negociations)
	 */
	public Double getPrix() {
		return this.contratCadre.getPrix();
	}

	public String toString() {
		return this.contratCadre.toString();
	}

	public String toHtml() {
		return this.contratCadre.toHtml();
	}

	public String oneLineHtml() {
		return this.contratCadre.oneLineHtml();
	}
}
