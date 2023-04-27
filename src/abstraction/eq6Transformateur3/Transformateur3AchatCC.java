package abstraction.eq6Transformateur3;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;
import java.util.LinkedList;
import java.util.List;

/** Nathan Claeys*/

public class Transformateur3AchatCC extends Transformateur3Transformation  implements IAcheteurContratCadre {
	
	private List<ExemplaireContratCadre> ListeContratEnCours;
	
	public Transformateur3AchatCC () {
		super();
		this.ListeContratEnCours = new LinkedList<ExemplaireContratCadre>();
		
	}
	/**
	 * ecrit par Nathan Claeys
	 * Methode appelee par le superviseur afin de savoir si l'acheteur est pret a
	 * faire un contrat cadre sur le produit indique.
	 * 
	 * @param produit
	 * @return Retourne false si l'acheteur ne souhaite pas etablir de contrat a
	 *         cette etape pour ce type de produit (retourne true si il est pret a
	 *         negocier un contrat cadre pour ce type de produit).
	 * La r�ponse va d�pendre de la valeur de la valeur du stock du produit et de si il y a un contrat sur ce produit
	 */
	public boolean achete(IProduit produit) {
		int step = Filiere.LA_FILIERE.getEtape();
		if (produit instanceof Feve) {List<Double> besoin_prochain = new LinkedList<Double>();
									  for (int i=1;i<5;i++) {
										  besoin_prochain.add(super.BesoinStep(step + i,((Feve)produit))- this.getArrivageCCStep(step + i, ((Feve)produit)));}
									  int b = 0;
									  /**double max = 0;**/
									  for (int i=0;i<4;i++) {
										  if (((double)besoin_prochain.get(i))>0) {b= b+1;
										  											/**if (((double)besoin_prochain.get(i))>0) {max = ((double)besoin_prochain.get(i));}**/}	}
									  if (b>2) {return true;}
									  else {return false;}
										  
									  }
		else {return false;}
									  }
  

    /**
     * ecrit par Nathan Claeys
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
     * => Il faut ajouter uns strat�gi� de modification 
     */
	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		// TODO Auto-generated method stub
		return (int) (super.pourcentageRSE);
	}

	/**
	 * ecrit par Nathan Claeys
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
	 * Il faut verifier que la duree nous convient et que chaque livraison proposée est sup à ce que l'on souhaite avoir
	 */
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		Echeancier vendeurecheancier = contrat.getEcheancier();
		int stepdebut = vendeurecheancier.getStepDebut();
		int duree = vendeurecheancier.getNbEcheances();
		int compt = 0; /**si il y a 3 step à la suite sans besoin on reduit le contrat**/
		int notreduree = 0;
		for (int i=stepdebut;i<stepdebut+duree && compt<3;i++) {
			if (super.BesoinStep(i,((Feve)contrat.getProduit()))>0) {compt = 0;notreduree=i;}
			else {compt = compt+1;}			
		}
		double commandemin = this.BesoinMaxEntre(stepdebut,notreduree,((Feve)contrat.getProduit()));
		List<Double> res = new LinkedList<Double>();
		for (int i =stepdebut;i<notreduree;i++) {
			if (vendeurecheancier.getQuantite(i)>=commandemin) {res.add(vendeurecheancier.getQuantite(i));}
			else {res.add(commandemin);}
		}
		return new Echeancier(stepdebut,res);
	}

	private double BesoinMaxEntre(int d, int f,Feve feve) {
		double max =0;
		for (int i= d;i<f;i++) {
			double besoin = super.BesoinStep(i, feve);
			if (besoin>max) {max = besoin;}
		}
		return max;
	}
	/**
	 * Ecrit par Nathan Claeys 
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
		double dernier_prix = contrat.getPrix();
		if (contrat.getListePrix().size()==1) {return 0.9*dernier_prix;}
		else {double mon_dernier_prix = contrat.getListePrix().get(contrat.getListePrix().size()-2);
			  if (dernier_prix <= 1.1*mon_dernier_prix) {return dernier_prix;}
			  else {double proposition =(mon_dernier_prix + (dernier_prix - mon_dernier_prix)/4);
			  		if (super.getSolde()<proposition) {return super.getSolde();}
			  		else {return proposition;}}}
	}

	/**
	 * ecrit par Nathan Claeys
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
			this.getListeContratEnCours().add(contrat);
			super.journal.ajouter("Un nouveau contrat cadre a été passé : "+contrat.toString());
	}

	/**
	 * Ecris par Nathan Claeys
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
	 * => ajouter ce produit arriv� dans le stock 
	 */
	public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
		Object produit = contrat.getProduit();
		if (produit instanceof Feve && lot.getQuantiteTotale()>0) {super.ajouterFeve(((Feve)produit), contrat.getQuantiteLivree().getQuantite(Filiere.LA_FILIERE.getEtape()),Filiere.LA_FILIERE.getEtape());}
		super.journal.ajouter("nouvel arrivage d'un lot de contrat cadre de "+lot.getQuantites()+"");
	}
	public List<ExemplaireContratCadre> getListeContratEnCours() {
		return ListeContratEnCours;
	}
	/**Cette fonction donne la quantit� de feves qui doit arriver par contrat � un step suivant
	 * Elle doit permettre d'�valuer si un contrat cadre est n�cessaire
	 * @param step
	 * @return
	 */
	protected double getArrivageCCStep(int step,Feve f) {
		double res = 0;
		for (ExemplaireContratCadre contrat: this.getListeContratEnCours()) {
			if (contrat.getProduit() instanceof Feve 
					&& ((Feve)contrat.getProduit()).getGamme().equals(f.getGamme()) 
					&& ((Feve)contrat.getProduit()).isBioEquitable()==(f.isBioEquitable())) {
			res = res + contrat.getEcheancier().getQuantite(step);}
		}
		return res;
	}
/**ecrit par Nathan Claeys
 */
	/**private void retirerCCFinis() {
		List<ExemplaireContratCadre> contrat_fini = new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.getListeContratEnCours()) {
			if (contrat.getQuantiteRestantALivrer()==0 && contrat.getMontantRestantARegler()==0) {contrat_fini.add(contrat);}
		for (ExemplaireContratCadre c : contrat_fini)
			{this.getListeContratEnCours().remove(c);}
		}
	}*/
	
/** ecrit par Nathan Claeys
 */

	/**public ExemplaireContratCadre getContrat(Feve produit) {
		Object l = SuperviseurVentesContratCadre.getVendeurs(produit);
		if (SuperviseurVentesContratCadre.getVendeurs(produit).size()!=0)
		List<IVendeurContratCadre> vendeurs = SuperviseurVentesContratCadre.getVendeurs(produit);
	}*/

	/** ecrit par Nathan Claeys
	 */	
	public void next() {
		super.next(); 
		List<ExemplaireContratCadre> contratsObsoletes=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.getListeContratEnCours()) {
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				contratsObsoletes.add(contrat);
			}
		}  
		this.getListeContratEnCours().removeAll(contratsObsoletes);		
	}  
}  
