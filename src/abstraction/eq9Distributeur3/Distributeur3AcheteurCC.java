package abstraction.eq9Distributeur3;


import abstraction.eqXRomu.contratsCadres.ContratCadre;
import java.awt.Color;
import java.util.List;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;


public class Distributeur3AcheteurCC extends Distributeur3Acteur implements IAcheteurContratCadre{
	public static Color COLOR_LLGRAY = new Color(238,238,238);

	protected Journal journal;
	private List<ExemplaireContratCadre> contratEnCours;
	private HashMap<Chocolat, Double> prixMax;
	private HashMap<Chocolat, Double>precedentPrix;
	//faire une méthode qui connait le prix d'achat moyen d'un chocolat

	public Distributeur3AcheteurCC() {//ChocolatDeMarque[] chocos, double[] stocks) {
		contratEnCours = new LinkedList<ExemplaireContratCadre>();
		this.precedentPrix = new HashMap<Chocolat, Double>();
		this.prixMax = new HashMap<Chocolat, Double>();

	}
	public void initialiser() {
		super.initialiser();
		for (Chocolat c :  Chocolat.values()) {
			double prixGamme =0;
			Gamme g;
			switch (c.getGamme()) {
			case HQ : prixGamme=50000;break;
			case MQ : prixGamme=30000;break;
			case BQ : prixGamme=10000;break;

			}
			this.prixMax.put(c, prixGamme+(c.isBioEquitable()? 5000 : 0));
			this.precedentPrix.put(c, 1000.0);
		}
	}


	//Mathilde
	public void next() {
		super.next();

		SuperviseurVentesContratCadre supCCadre = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
		if (chocolats.size()>0) {

			for (int i=0; i<chocolats.size();i++) {
				List<IVendeurContratCadre> vendeursChocolat = supCCadre.getVendeurs(chocolats.get(i));
				Echeancier echeancier = new Echeancier (Filiere.LA_FILIERE.getEtape()+1,24, 25000.0);
				//System.out.println(""+vendeursChocolat.size()+" v est "+vendeursChocolat.get(0));
				List<ExemplaireContratCadre> contratAvecChocolat = new ArrayList<ExemplaireContratCadre> ();
				if (contratEnCours != null) {
					for (int k = 0;k<contratEnCours.size();k++) {
						if (chocolats.get(i).equals((ChocolatDeMarque)((contratEnCours.get(k).getProduit())))) {
							contratAvecChocolat.add(contratEnCours.get(k));
						}
					}
				for (int k = 0;k<contratEnCours.size();k++) {
					if (chocolats.get(i).equals((ChocolatDeMarque)((contratEnCours.get(k).getProduit())))) {
						contratAvecChocolat.add(contratEnCours.get(k));
					}//ajoute les contrats avec le chocolat qui nous interesse 

				}
				if (vendeursChocolat.size()>0  ) {
					boolean pasAchete=true;

					if (contratAvecChocolat.size()==0) {
						for (int j=0; j< vendeursChocolat.size()&&pasAchete;j++) {


							//Echeancier echeancier = new Echeancier (contratEnCours.get(i).getEcheancier().getStepFin(),24, 25000.0);
							ExemplaireContratCadre cc =supCCadre.demandeAcheteur(this , vendeursChocolat.get(j), chocolats.get(i), echeancier , this.cryptogramme, initialise);
							pasAchete = false;
						}
					}

					for (int k = 0;k<contratAvecChocolat.size();k++) {
						for (int j=0; j< vendeursChocolat.size()&&pasAchete;j++) {


							//Echeancier echeancier2 = new Echeancier (contratAvecChocolat.get(k).getEcheancier().getStepFin(),24, 25000.0);

							
							List<Double> qteVoulue = new LinkedList();
							for (int f = 0; f<24; f++) {
								qteVoulue.add(f, 25000.0);
								//on regarde si l'on se trouve dans les périodes de fortes ventes 
								if ((contratAvecChocolat.get(k).getEcheancier().getStepFin()+f)%24==3 || (contratAvecChocolat.get(k).getEcheancier().getStepFin()+f)%24==6 || (contratAvecChocolat.get(k).getEcheancier().getStepFin()+f)%24==23) {
									qteVoulue.add(f, 35000.0);
								}
							}
							Echeancier echeancier2 = new Echeancier (contratAvecChocolat.get(k).getEcheancier().getStepFin(), qteVoulue);
							ExemplaireContratCadre cc =supCCadre.demandeAcheteur(this , vendeursChocolat.get(j), chocolats.get(i), echeancier2 , this.cryptogramme, initialise);
							pasAchete = false;}
					}}
			}}}}






	/*List<IVendeurContratCadre> vendeursChocolat = supCCadre.getVendeurs(chocolats.get(0));
			//creation échéancier
			List<Double>  quantites = new ArrayList();
			quantites.add(1.);
			quantites.add(1.);
			Echeancier echeancier = new Echeancier (Filiere.LA_FILIERE.getEtape(),4, 1.0);
			System.out.println(""+vendeursChocolat.size()+" v");
			if (vendeursChocolat.size()>0) {

				for (int i=0; i< vendeursChocolat.size();i++) {
					System.out.println(""+chocolats.get(0)+" demande");
					supCCadre.demandeAcheteur(this , vendeursChocolat.get(i), chocolats.get(0),echeancier , this.cryptogramme, initialise);
				}

			}*/






	// Mathilde : on accepte les chocolats qu'on vend
	public boolean achete(IProduit produit) {
		if (!(produit instanceof ChocolatDeMarque)) {

			return false;
		}
		for (ChocolatDeMarque chocolat: chocolats) {
			if (((ChocolatDeMarque)produit).equals(chocolat)){

				this.journal_achats.ajouter("j'affirme etre acheteur de " + produit.toString());
				return true;

			}
		}
		return false;

	}

	public int fixerPourcentageRSE(IAcheteurContratCadre acheteur, IVendeurContratCadre vendeur, IProduit produit,
			Echeancier echeancier, long cryptogramme, boolean tg) {
		// TODO Auto-generated method stub
		return 0;
	}

//Mathilde
	// Dans cette contreproposition j'essaye de renvoyer un échéancier sur 12 mois avec au total un peu plus de 25000 
	// tablettes de chocolats achetées. Je chercher à savoir à chaque fois si ma quantité de chocolat est assez grande 
	// à chaque step (25000/24 environ = 1050)(dernier if) 

	@Override
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		if (contrat.getProduit() instanceof ChocolatDeMarque) {
			Echeancier e = contrat.getEcheancier();
			//ChocolatDeMarque chocolat = (ChocolatDeMarque)contrat.getProduit();
			//double qte = this.stock.getStock(chocolat);
			//double qteProp = e.getQuantiteTotale();
			
				double qtechocstep = 0.0;
				for(int j=0; j< e.getNbEcheances(); j++){
				for(int i =0; i<contratEnCours.size();i++) {
					
					qtechocstep = qtechocstep +  contratEnCours.get(i).getEcheancier().getQuantite(j);}
					
				if ((qtechocstep + e.getQuantite(j)) < 25000.0) {
					if ((e.getStepDebut()+j)%24 ==3 || (e.getStepDebut()+j)%24 == 6 || (e.getStepDebut()+j)%24 == 23) {
						e.set(e.getStepDebut()+j, 35000.0-(qtechocstep+e.getQuantite(j)));
					}
					else {	e.set(e.getStepDebut()+j, 25000.0-(qtechocstep+e.getQuantite(j)));}
				}
				
			
		}
			return e;
			}
		
		return null; 
	}

	//Sami
	@Override
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		prix = contrat.getPrix();
		journal_ventes.ajouter("proposition d'achat du chocolat" + contrat.getProduit()+"au prix à la tonne de" + prix);
		ChocolatDeMarque choco = (ChocolatDeMarque)contrat.getProduit();
		Chocolat c = choco.getChocolat();
		double prix_max = prixMax.get(c);
		double prix_min=1000;
		double prec = precedentPrix.get(choco.getChocolat());
		journal_ventes.ajouter("ancien prix tonne de " + contrat.getProduit()+" est de " + prec + "€");

		/*On regarde d'abord la taille de la liste des prix proposés. Si la liste est de longueur 6 ou plus,
		on accepte le contrat s'il est dans la fourchette prixMin, prixMax car on risque de perdre le contrat*/
		if(contrat.getListePrix().size()>=6) {
			if(prix<prix_max && prix>prix_min) {
				prec=prix;
				journal_ventes.ajouter("nouveau prix tonne de " + contrat.getProduit()+" est de " + prix + "€");
				return prix;
			}
			else {

				return 0.0;
			}
		} else {
			/* 
			 On cherche à négocier le prix proposé entre le prix et prixMin 
			 */
			if(prix<prix_max && prix>prix_min) {
				//On pourra peut etre rajouter un comportement selon le comportement de l'autre partie (s'il fait pas d'ffort on s'adapte par exemple)
				prix=prix*0.8;
				prec=prix;
				journal_ventes.ajouter("nouveau prix tonne de " + contrat.getProduit()+" est de " + prix + "€");
				return prix;
			}
			else {
				return 0.0;
			}
		}
		
	}


@Override
public void receptionner(Lot lot, ExemplaireContratCadre contrat) {
	IProduit nt;
	this.journal_achats = new Journal("On receptionne du chocolat : " + contrat.getProduit() + " en quantite : " + lot.getQuantiteTotale(), this);
	stock.ajoutQte(((ChocolatDeMarque)(contrat.getProduit())), lot.getQuantiteTotale());
}

@Override
public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
	// TODO Auto-generated method stub
	this.journal_achats.ajouter("Etape "+ Filiere.LA_FILIERE.getEtape()+ " : " + "je viens de passer le contrat "+contrat + "et j'ai achete le chocolat " + contrat.getProduit());
	this.contratEnCours.add(contrat);

	notificationOperationBancaire(-1*contrat.getPrix()*contrat.getQuantiteTotale());

	// william 
	prix = contrat.getPrix() /*/contrat.getQuantiteTotale() deja à la tonne */;
	journal_ventes.ajouter("achat du chocolat" + contrat.getProduit()+"au prix à la tonne de" + prix);
	ChocolatDeMarque choco = (ChocolatDeMarque)contrat.getProduit();

	// on calcule le prix de vente du chocolat dus contract en fonction de la gamme
	double prix_tonne_de_vente_contrat = 0.0;

	// marge de 80% sur HQ_BE
	if(choco.getGamme() == Gamme.HQ)  {
		prix_tonne_de_vente_contrat = prix*5;
	}
	// marge de 67% sur MQ_BE
	if(((ChocolatDeMarque)contrat.getProduit()).getGamme() == Gamme.MQ && ((ChocolatDeMarque)contrat.getProduit()).isBioEquitable()){
		prix_tonne_de_vente_contrat = prix*3;
	}
	// marge de 50% sur MQ
	if(((ChocolatDeMarque)contrat.getProduit()).getGamme() == Gamme.MQ  && !((ChocolatDeMarque)contrat.getProduit()).isBioEquitable()) {
		prix_tonne_de_vente_contrat = prix*2;
	}

	double prix_tonne_de_vente_apres_achat = 0.0;

	// si il existe deja un stock de ce chocolat, on fait la moyenne des prix pondérés par la quantite acheté et la quantite deja stockee
	// si il y a du stock
	if(stock.getStock(choco) != 0) {
		double qtte_actuelle = stock.getStock(choco);
		double qtte_apres_achat = qtte_actuelle + contrat.getQuantiteTotale();
		// proportion de nouveau chocolat
		double proportion_contrat = contrat.getQuantiteTotale()/qtte_apres_achat;
		// ponderation
		prix_tonne_de_vente_apres_achat = prix_tonne_de_vente_contrat*proportion_contrat +prix_tonne_vente.get(choco)*(1-proportion_contrat) ;
	}
	// il n'y a pas de stock
	else {
		prix_tonne_de_vente_apres_achat = prix_tonne_de_vente_contrat;
	}
	journal_ventes.ajouter("ancien prix tonne de " + contrat.getProduit()+" est de " + prix_tonne_vente.get(choco) + "€");
	journal_ventes.ajouter("nouveau prix tonne de " + contrat.getProduit()+" est de " + prix_tonne_de_vente_apres_achat + "€");

	this.prix_tonne_vente.put((ChocolatDeMarque)contrat.getProduit(), prix_tonne_de_vente_apres_achat);

}

// mettre à jour dans notification et next
public void prixMoyen (ChocolatDeMarque choc) {
	double prixMoy = 0.0;
	for (Entry<ChocolatDeMarque, Double[]> chocolat : prixMoyen.entrySet()) {
		if (chocolat.equals(choc) && achete(choc)) {
			prixMoyen.replace(choc, prixMoyen.get(choc), prixMoyen.get(choc) );
			// sur la quantité
		}
	}

}


}
