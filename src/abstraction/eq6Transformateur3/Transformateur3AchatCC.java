package abstraction.eq6Transformateur3;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/** Nathan Claeys*/

public class Transformateur3AchatCC extends Transformateur3Transformation  implements IAcheteurContratCadre {
	
	private List<ExemplaireContratCadre> ListeContratEnCoursAchat;
	private SuperviseurVentesContratCadre superviseur ;
	private Variable prixmaxBG;
	private Variable prixmaxMG;
	private Variable prixmaxMGL;
	private Variable prixmaxHG;
	protected double quantiteEnAttente ;
	//Les var ci dessous indiquent la tranche dans laquelle on veut que les stocks restent
	protected double quantBQMin = 10000.0;
	protected double quantMQMin = 10000.0;
	protected double quantMQLMin = 10000.0;
	protected double quantHQMin = 10000.0;
	protected double quantBQMax = 250000.0;
	protected double quantMQMax = 250000.0;
	protected double quantMQLMax = 250000.0;
	protected double quantHQMax = 250000.0;
	protected double prixMoyBQ = 0.0;
	protected double prixMoyMQ = 0.0;
	protected double prixMoyMQL = 0.0;
	protected double prixMoyHQ = 0.0;
	protected boolean prioriteCCBG = true;
	protected boolean prioriteCCMG = true;
	protected boolean prioriteCCMGL =true;
	protected boolean prioriteCCHG =true;
	
	
	public Transformateur3AchatCC () {
		super();
		this.ListeContratEnCoursAchat = new LinkedList<ExemplaireContratCadre>();
		this.prixmaxBG = new Variable ("prix maximal BG","prix maximal que l'acteur va accepter pour les feves bas de gamme en CC",this,0.0,6000,4000);
		this.prixmaxMG = new Variable ("prix maximal MG","prix maximal que l'acteur va accepter pour les feves milieu gamme en CC",this,0.0,7000,5000);
		this.prixmaxMGL = new Variable ("prix maximal MGL","prix maximal que l'acteur va accepter pour les feves milieu gamme lab en CC",this,0.0,8000,6000);
		this.prixmaxHG = new Variable ("prix maximal HG","prix maximal que l'acteur va accepter pour les feves haut de gamme en CC",this,0.0,10000,8000);
		this.quantiteEnAttente = 0.0;
		
	}
	public void setprixMoyBQ(double p) {
		this.prixMoyBQ = p;
	}
	public void setprixMoyMQ(double p) {
		this.prixMoyMQ = p;
	}	
	public void setprixMoyMQL(double p) {
		this.prixMoyMQL = p;
	}
	public void setprixMoyHQ(double p) {
		this.prixMoyHQ = p;
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
	 * Dans cette fonction on va regarder pour chaque type de feves si leur stock est bien dans l'encadrement souhaité.
	 * Si il n'est pas à sa valeur max on va essayer de le compéter sinon on ne prend pas.
	 */
	public boolean achete(IProduit produit) {
		double stock = 0.0;
		boolean res =false;
		if (produit.getType() == "Feve") {
		switch(((Feve)produit).getGamme()) {
		case BQ:
			stock = super.stockChocolatBG.getQuantiteTotale();
			if (stock<quantBQMax-500) {res= true;}
		case MQ:
			if (((Feve)produit).isBioEquitable()) {
				stock = super.stockChocolatMGL.getQuantiteTotale();
				if (stock<quantMQLMax-500) {res= true;}
			}
			else {stock = super.stockChocolatMG.getQuantiteTotale();
			if (stock<quantMQMax-500) {res= true;}}
		case HQ:
			stock = super.stockChocolatHGL.getQuantiteTotale();
			if (stock<quantHQMax-500) {res= true;}
		}}
		return res;
	}
	
	public boolean acheteV1(IProduit produit) {
		int step = Filiere.LA_FILIERE.getEtape();
		if (produit instanceof Feve) {/**List<Double> besoin_prochain = new LinkedList<Double>();
									  for (int i=1;i<5;i++) {
										  besoin_prochain.add(super.BesoinStep(step + i,((Feve)produit))- this.getArrivageCCStep(step + i, ((Feve)produit)));}
									  int b = 0;
									  
									  for (int i=0;i<4;i++) {
										  if (((double)besoin_prochain.get(i))>0) {b= b+1;
										  											}	}
									  if (b>2)*/
									  if (super.totalStocksFeves.getValeur()+this.quantiteEnAttente<super.totalStocksFeves.getMax()-10000){return true;}
									  else {return true;}//pour le test
										  
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
		Echeancier vendeurecheancier = contrat.getEcheancier();
		Echeancier res = null;
		int stepdebut = vendeurecheancier.getStepDebut();
		int duree = vendeurecheancier.getNbEcheances();
		boolean cbon = true;
		double stock = 0.0;
		double stockMax = 0.0;
		int dureeMax = duree;
		switch (((Feve)contrat.getProduit()).getGamme()) {
		case BQ:
			stock = super.stockChocolatBG.getQuantiteTotale();
			stockMax = this.quantBQMax;
		case MQ:
			if (((Feve)contrat.getProduit()).isBioEquitable()) {
				stock = super.stockChocolatMGL.getQuantiteTotale();
				stockMax = this.quantMQLMax;
			}
			else {stock = super.stockChocolatMG.getQuantiteTotale();
				  stockMax = this.quantMQMax;}
		case HQ:
			stock = super.stockChocolatHGL.getQuantiteTotale();
			stockMax = this.quantHQMax;
		}
		for (int i = stepdebut;i<stepdebut+duree;i++) {
			stock = stock+this.getArrivageCCStep(i, ((Feve)contrat.getProduit()));
			if (stock>stockMax) {cbon=false;dureeMax = i-1-stepdebut;}
		}
		if (dureeMax<duree) {
			if (dureeMax>0) {
				LinkedList<Double> l = new LinkedList<Double>();
				for (int i=0;i<dureeMax;i++) {
					l.add(vendeurecheancier.getQuantite(stepdebut+i));
				}
				Echeancier ech = new Echeancier(stepdebut,l);
			}
			else {res = null;}}
		else {res = vendeurecheancier;}
		
		
		return res;
	}
	
	
	
	public Echeancier contrePropositionDeLAcheteurV1(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		Echeancier vendeurecheancier = contrat.getEcheancier();
		int stepdebut = vendeurecheancier.getStepDebut();
		int duree = vendeurecheancier.getNbEcheances();
		int compt = 0; /**si il y a 3 step à la suite sans besoin on reduit le contrat**/
		int notreduree = 0;
		for (int i=stepdebut;i<stepdebut+duree && compt<3;i++) {
			if (1>0) {compt = 0;notreduree=i;}//a refaire
			else {compt = compt+1;}			
		}
		if (notreduree == 0) {return null;}
		else {
		double c = max(101.0,this.BesoinMaxEntre(stepdebut,notreduree,((Feve)contrat.getProduit())));
		super.journalAchatCC.ajouter("besoin calculé pour l'eatape "+stepdebut+" à "+notreduree+ " est "+this.BesoinMaxEntre(stepdebut,notreduree,((Feve)contrat.getProduit())));
		double commandemin = Math.min(c, 1000.0);
		List<Double> res = new LinkedList<Double>();
		for (int i =stepdebut;i<=notreduree;i++) {
			if (vendeurecheancier.getQuantite(i)>=commandemin&&vendeurecheancier.getQuantite(i)<1000) {res.add(max(vendeurecheancier.getQuantite(i),100.0));}
			else {res.add(commandemin);}
		}
		Echeancier ech = new Echeancier(stepdebut,res);
		super.journalAchatCC.ajouter("valeur tot du stock après ce contrat : "+ech.getQuantiteTotale()+super.totalStocksFeves.getValeur()+this.quantiteEnAttente);
		if (ech.getQuantiteTotale()+super.totalStocksFeves.getValeur()+this.quantiteEnAttente>super.totalStocksFeves.getMax()) {return null;}
		else {return ech;}}
	}

	private double max(double a, double b) {
		// TODO Auto-generated method stub
		if (a<b) {return b;}else {
		return a;}
	}
	private double BesoinMaxEntre(int d, int f,Feve feve) {
		double max =0;
		for (int i= d;i<f;i++) {
			double besoin = 100;// a faire
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
		double res = 1;
		switch(((Feve)contrat.getProduit()).getGamme()) {//On ne veut pas accepter des prix trop haut
	  	case BQ :
	  		if (contrat.getPrix()>prixmaxBG.getValeur()) {res =0.0;};
        case MQ:
            if (((Feve)contrat.getProduit()).isBioEquitable()) {
            	if (contrat.getPrix()>prixmaxMGL.getValeur()) {res = 0.0;};
            } else {
            	if (contrat.getPrix()>prixmaxMG.getValeur()) {res = 0.0;};
            }
        case HQ:
        	if (contrat.getPrix()>prixmaxHG.getValeur()) {res = 0.0;}
        if (res==1) {
		double dernier_prix = contrat.getPrix();
		if (contrat.getListePrix().size()==1) {res = 0.9*dernier_prix;}
		else {double mon_dernier_prix = contrat.getListePrix().get(contrat.getListePrix().size()-2);
			  if (dernier_prix <= 1.1*mon_dernier_prix) {res = dernier_prix;}
			  else {double proposition =(mon_dernier_prix + (dernier_prix - mon_dernier_prix)/4);
			  		if (super.getSolde()<proposition) {res = super.getSolde();}
			  		else {res = proposition;}}}}}
		return res;
		
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
		if (contrat.getAcheteur()==this) {
			this.getListeContratEnCoursA().add(contrat);
			this.quantiteEnAttente = this.quantiteEnAttente+contrat.getQuantiteTotale();
			super.journalAchatCC.ajouter("nouvelle valeur en attente : "+quantiteEnAttente+" et tot stock : "+super.totalStocksFeves.getValeur());
			super.journalAchatCC.ajouter("Un nouveau contrat cadre a été passé : "+contrat.toString());}
		else {super.notificationNouveauContratCadre(contrat);}
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
		if (produit instanceof Feve && lot.getQuantiteTotale()>0) {
			super.ajouterFeve(lot);}
		this.quantiteEnAttente = this.quantiteEnAttente - lot.getQuantiteTotale();
		super.journalAchatCC.ajouter("nouvel arrivage d'un lot de contrat cadre de "+lot.getQuantiteTotale()+"");
		super.journalAchatCC.ajouter("nouvelle valeur en attente : "+quantiteEnAttente+" et tot stock : "+super.totalStocksFeves.getValeur());
	}
	public List<ExemplaireContratCadre> getListeContratEnCoursA() {
		return ListeContratEnCoursAchat;
	}
	/**Cette fonction donne la quantit� de feves qui doit arriver par contrat � un step suivant
	 * Elle doit permettre d'�valuer si un contrat cadre est n�cessaire
	 * @param step
	 * @return
	 */
	protected double getArrivageCCStep(int step,Feve f) {
		double res = 0;
		for (ExemplaireContratCadre contrat: this.getListeContratEnCoursA()) {
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
 * retourne un CC si on a trouvé un acheteur et que la négociation à réussi
 * return null sinon
 */

	public void chercheContrat(IProduit produit) {
		if (superviseur != null) {
		List<IVendeurContratCadre> vendeurs = superviseur.getVendeurs(produit);
		if (vendeurs.size()!=0) {
			Collections.shuffle(vendeurs);
			for (IVendeurContratCadre vendeur : vendeurs) {
			super.journalAchatCC.ajouter("on essaie de demander un contrat à l'equipe :"+vendeur.getNom());
			ExemplaireContratCadre contrat = superviseur.demandeAcheteur(this, vendeur, produit, new Echeancier(Filiere.LA_FILIERE.getEtape()+1,Filiere.LA_FILIERE.getEtape()+9,100.0), super.cryptogramme, false);
			if (contrat != null) {super.journalAchatCC.ajouter("CC cherché et trouvé :"+contrat.toString());
									this.ListeContratEnCoursAchat.add(contrat);
									this.quantiteEnAttente=this.quantiteEnAttente+contrat.getQuantiteTotale();
									super.journalAchatCC.ajouter("nouvelle valeur en attente : "+quantiteEnAttente+" et tot stock : "+super.totalStocksFeves.getValeur());}}}}
		
	}
	/**ecrit par Nathan Claeys
	   * pour pouvoir rendre les variables qui peuvent aider à la prise de decision
	   */
	  public List<Variable> getIndicateurs() {
			List<Variable> res = super.getIndicateurs();
			res.add(prixmaxBG);
			res.add(prixmaxHG);
			res.add(prixmaxMG);
			res.add(prixmaxMGL);
			return res;}

	/** ecrit par Nathan Claeys
	 */	
	
	public void initialiser() {
		super.initialiser();
		this.superviseur = (SuperviseurVentesContratCadre)Filiere.LA_FILIERE.getActeur("Sup.CCadre");
	}
	
	public void next() {
		super.next(); 
		this.actualisePrixMoyenCC();
		List<ExemplaireContratCadre> contratsObsoletes=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.getListeContratEnCoursA()) {
			super.journalAchatCC.ajouter(contrat.toString());
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				contratsObsoletes.add(contrat);
			}
			else {/**if (contrat.getPaiementAEffectuerAuStep()>0.0 && contrat.getVendeur()!=this) {Filiere.LA_FILIERE.getBanque().virer(this, super.cryptogramme, contrat.getVendeur(), contrat.getPaiementAEffectuerAuStep());
			super.journal.ajouter("virement de "+contrat.getPaiementAEffectuerAuStep()+"fait à l'équipe"+contrat.getVendeur().getNom());}*/}
		}  
		
		this.getListeContratEnCoursA().removeAll(contratsObsoletes);
		if ((super.stockFeveBG.getQuantiteTotale()+this.quantiteEnAttente+100000<this.quantBQMax 
				&& this.prioriteCCBG)
				|| super.stockFeveBG.getQuantiteTotale()+this.quantiteEnAttente<this.quantBQMin+10000) {
		this.chercheContrat(Feve.F_BQ);}
		if ((super.stockFeveMG.getQuantiteTotale()+this.quantiteEnAttente+100000<this.quantMQMax
				&& this.prioriteCCMG)
				|| super.stockFeveMG.getQuantiteTotale()+this.quantiteEnAttente<this.quantMQMin +10000) {
		this.chercheContrat(Feve.F_MQ);}
		if ((super.stockFeveMGL.getQuantiteTotale()+this.quantiteEnAttente+100000<this.quantMQLMax
				&& this.prioriteCCMGL)
				||super.stockFeveMGL.getQuantiteTotale()+this.quantiteEnAttente<this.quantMQLMin + 10000) {
		this.chercheContrat(Feve.F_MQ_BE);}
		if ((super.stockFeveHGL.getQuantiteTotale()+this.quantiteEnAttente+100000<this.quantHQMax
				&& this.prioriteCCHG)
				||super.stockFeveHGL.getQuantiteTotale()+this.quantiteEnAttente<this.quantHQMin + 10000) {
		this.chercheContrat(Feve.F_HQ_BE);}
		
		
		
	}  
	public void actualisePrixMoyenCC() {
		LinkedList<Double> listBQ = new LinkedList<Double>();
		LinkedList<Double> listMQ = new LinkedList<Double>();
		LinkedList<Double> listMQL = new LinkedList<Double>();
		LinkedList<Double> listHQ = new LinkedList<Double>();
		for (ExemplaireContratCadre contrat : this.ListeContratEnCoursAchat) {
			switch (((Feve)contrat.getProduit()).getGamme()) {
			case BQ:
				listBQ.add(contrat.getPrix());
			case MQ:
				if(((Feve)contrat.getProduit()).isBioEquitable()) {
					listMQL.add(contrat.getPrix());
				}else {
					listMQ.add(contrat.getPrix());
				}
			case HQ:
				listHQ.add(contrat.getPrix());
			}
		}
		this.prixMoyBQ = this.moyList(listBQ);
		this.prixMoyMQ = this.moyList(listMQ);
		this.prixMoyMQL = this.moyList(listMQL);
		this.prixMoyHQ = this.moyList(listHQ);
	}
	
	public double moyList(List<Double> l) {
		int n =  l.size();
		if (n == 0) {return 0.0;}
		else{
		double res = 0.0;
		for (int i =0;i<n;i++) {
			res = res + l.get(i);
		}
		return res/n;}
	}
	/** ecrit par Nathan Salbego
	 */	
	
	public boolean privilegieCC(Feve f, double coursBourse) {
		boolean res = false;
		switch (f.getGamme()) {
			case BQ:
		res = this.prixMoyBQ<coursBourse;
		this.prioriteCCBG = res;
			case MQ:
				if (f.isBioEquitable()) {
	  	res = this.prixMoyMQL<coursBourse;
	  	this.prioriteCCMGL = res;}
				else {
	  	res = this.prixMoyMQ<coursBourse;
	  	this.prioriteCCMG = res;}
			case HQ:
	  	res = this.prixMoyHQ <coursBourse;
	  	this.prioriteCCHG = res;
	  	
	}return res;}
	
}  
