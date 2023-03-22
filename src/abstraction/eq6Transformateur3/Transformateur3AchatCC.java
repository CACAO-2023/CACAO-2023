package abstraction.eq6Transformateur3;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;
import java.util.LinkedList;
import java.util.List;

/** Nathan Claeys*/

public class Transformateur3AchatCC extends Transformateur3Vente implements IAcheteurContratCadre {
	
	private List<ExemplaireContratCadre> ListeContratEnCours;
	
	public Transformateur3AchatCC () {
		this.ListeContratEnCours = new LinkedList<ExemplaireContratCadre>();
		
	}
	/**
	 * Methode appelee par le superviseur afin de savoir si l'acheteur est pret a
	 * faire un contrat cadre sur le produit indique.
	 * 
	 * @param produit
	 * @return Retourne false si l'acheteur ne souhaite pas etablir de contrat a
	 *         cette etape pour ce type de produit (retourne true si il est pret a
	 *         negocier un contrat cadre pour ce type de produit).
	 * La réponse va dépendre de la valeur de la valeur du stock du produit et de si il y a un contrat sur ce produit
	 */
	public boolean achete(IProduit produit) {
		// TODO Auto-generated method stub
		return false;
	}

    /**
     * Appelee suite au demarrage des negociations par le vendeur d'un contrat de feves labelisee 
     * afin de connaitre le pourcentage de rse qui sera effectif sur le chocolat de marque produit
     * a partir des feves de ce contrat cadre.
     * @param acheteur
     * @param vendeur
     * @param produit
     * @param echeancier
     * @param cryptogramme
     * @param tg
     * @return Le pourcentage de rse qui sera effectif sur le chocolat de marque produit
     * a partir des feves de ce contrat cadre.
     * => Il faut ajouter uns stratégié de modification 
     */
	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		// TODO Auto-generated method stub
		return (int) (100*super.pourcentageRSE.getValeur());
	}

	/**
	 * Methode appelee par le SuperviseurVentesContratCadre lors des negociations
	 * sur l'echeancier afin de connaitre la contreproposition de l'acheteur. Les
	 * precedentes propositions d'echeancier peuvent etre consultees via un appel a
	 * la methode getEcheanciers() sur le contrat passe en parametre.
	 * 
	 * @param contrat. Notamment, getEcheancier() appelee sur le contrat retourne
	 *                 l'echeancier que le vendeur vient de proposer.
	 * @return Retoune null si l'acheteur souhaite mettre fin aux negociation (et
	 *         abandonner du coup ce contrat). Retourne le meme echeancier que celui
	 *         du contrat (contrat.getEcheancier()) si l'acheteur est d'accord pour
	 *         un tel echeancier. Sinon, retourne un nouvel echeancier que le
	 *         superviseur soumettra au vendeur.
	 */
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Methode appelee par le SuperviseurVentesContratCadre lors des negociations
	 * sur le prix au Kg afin de connaitre la contreproposition de l'acheteur.
	 * L'acheteur peut consulter les precedentes propositions via un appel a la
	 * methode getListePrix() sur le contrat. En particulier la methode getPrix()
	 * appelee sur contrat indique la derniere proposition faite par le vendeur.
	 * 
	 * @param contrat
	 * @return Retourne un prix negatif ou nul si l'acheteur souhaite mettre fin aux
	 *         negociations (en renoncant a ce contrat). Retourne le prix actuel
	 *         (contrat.getPrix()) si il est d'accord avec ce prix. Sinon, retourne
	 *         un autre prix correspondant a sa contreproposition.
	 */
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Methode appelee par le SuperviseurVentesContratCadre afin de notifier le
	 * l'acheteur de la reussite des negociations sur le contrat precise en
	 * parametre qui a ete initie par le vendeur. Le superviseur veillera a
	 * l'application de ce contrat (des appels a livrer(...) seront effectues
	 * lorsque le vendeur devra livrer afin d'honorer le contrat, et des transferts
	 * d'argent auront lieur lorsque l'acheteur paiera les echeances prevues)..
	 * 
	 * @param contrat
	 */
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Methode appelee par le SuperviseurVentesContratCadre afin de notifier
	 * l'acheteur de la livraison du lot de produit precise en parametre
	 * (dans le cadre du contrat contrat). Il se peut que la quantitee livree
	 * soit inferieure a la quantite prevue par le contrat si le vendeur est dans 
	 * l'incapacite de la fournir. Dans ce cas, le vendeur aura une penalite 
	 * (un pourcentage de produit a livrer en plus). L'acheteur doit a minima 
	 * mettre ce produit dans son stock.
	 * 
	 * @param lot
	 * @param contrat
	 */
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}
	public List<ExemplaireContratCadre> getListeContratEnCours() {
		return ListeContratEnCours;
	}

}
