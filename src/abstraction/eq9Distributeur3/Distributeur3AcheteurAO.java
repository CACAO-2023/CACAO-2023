package abstraction.eq9Distributeur3;

import java.util.HashMap;

import abstraction.eqXRomu.appelsOffres.ExempleAbsAcheteurAO;
import abstraction.eqXRomu.appelsOffres.IAcheteurAO;
import abstraction.eqXRomu.appelsOffres.IVendeurAO;
import abstraction.eqXRomu.appelsOffres.OffreVente;
import abstraction.eqXRomu.appelsOffres.PropositionAchatAO;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.offresAchat.PropositionVenteOA;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;
public class Distributeur3AcheteurAO extends ExempleAbsAcheteurAO implements IAcheteurAO {
	
	//////////////////////
	//////  william //////
	//////////////////////
	
	/////////////////////////////////////////////
	///// largement inspiré du code exemple /////
	/////////////////////////////////////////////
	
	private Distributeur3Acteur a;
	private Distributeur3AcheteurCC b;

	protected HashMap<IVendeurAO, Double> prix;

	
	public Distributeur3AcheteurAO(double prixInit,Distributeur3Acteur a,Distributeur3AcheteurCC b) {
		super(prixInit);
		this.prix=new HashMap<IVendeurAO, Double>();
		this.a = a;
		this.b = b;
	}
	

	@SuppressWarnings("unlikely-arg-type")
	public double proposerPrix(OffreVente offre) {
		a.journal_AO.ajouter("ProposerPrix("+offre+"):");
			double px = this.prixInit;
			if (this.prix.keySet().contains(offre.getVendeur())) {
				px = this.prix.get(offre.getVendeur());
			}
			
			else {
				// le prix d'achat initial est le prix moyen entre le prix du marche et le prix propose si le prix proposse est superieur au prix moyen
				if(px < b.prixMax.get(offre.getChocolat()))
				{
					px = this.prix.get(offre.getVendeur());

				}
			}
			
			a.journal_AO.ajouter("   je propose "+px);
			return px;
	}

	public void notifierAchatAO(PropositionAchatAO propositionRetenue) {
		double stock = (this.stock.keySet().contains(propositionRetenue.getOffre().getChocolat())) ?this.stock.get(propositionRetenue.getOffre().getChocolat()) : 0.0;
		this.stock.put(propositionRetenue.getOffre().getChocolat(), stock+ propositionRetenue.getOffre().getQuantiteT());
		this.prix.put(propositionRetenue.getOffre().getVendeur(), propositionRetenue.getPrixT()-1000.0);
		a.journal_AO.ajouter("   mon prix a ete accepte. Mon prix pour "+propositionRetenue.getOffre().getVendeur()+" passe a "+(propositionRetenue.getPrixT()-1000.0));
	
		adapter_prix_vente(propositionRetenue);
	}

	public void notifierPropositionNonRetenueAO(PropositionAchatAO propositionNonRetenue) {
		this.prix.put(propositionNonRetenue.getOffre().getVendeur(), propositionNonRetenue.getPrixT()+100.);
		a.journal_AO.ajouter("   mon prix a ete refuse. Mon prix pour "+propositionNonRetenue.getOffre().getVendeur()+" passe a "+(propositionNonRetenue.getPrixT()+100.));
	}
	
	
public void adapter_prix_vente(PropositionAchatAO proposition) {
		
		
		double prix_proposition = proposition.getPrixT() /*/contrat.getQuantiteTotale() deja à la tonne */;
		b.journal_ventes.ajouter("achat du chocolat" + proposition.getOffre().getChocolat()+"au prix à la tonne de" + prix + "par offre d'achat");
		ChocolatDeMarque choco = (ChocolatDeMarque)proposition.getOffre().getChocolat();

		// on calcule le prix de vente du chocolat dus contract en fonction de la gamme
		double prix_tonne_de_vente_contrat = 0.0;

		// marge de 80% sur HQ_BE
		if(choco.getGamme() == Gamme.HQ)  {
			prix_tonne_de_vente_contrat = prix_proposition*5;
		}
		// marge de 67% sur MQ_BE
		if(((ChocolatDeMarque)proposition.getOffre().getChocolat()).getGamme() == Gamme.MQ && ((ChocolatDeMarque)proposition.getOffre().getChocolat()).isBioEquitable()){
			prix_tonne_de_vente_contrat = prix_proposition*3;
		}
		// marge de 50% sur MQ
		if(((ChocolatDeMarque)proposition.getOffre().getChocolat()).getGamme() == Gamme.MQ  && !((ChocolatDeMarque)proposition.getOffre().getChocolat()).isBioEquitable()) {
			prix_tonne_de_vente_contrat = prix_proposition*2;
		}

		double prix_tonne_de_vente_apres_achat = 0.0;

		// si il existe deja un stock de ce chocolat, on fait la moyenne des prix pondérés par la quantite acheté et la quantite deja stockee
		// si il y a du stock
		if(b.stock.getStock(choco) != 0) {
			double qtte_actuelle = b.stock.getStock(choco);
			double qtte_apres_achat = qtte_actuelle + proposition.getOffre().getQuantiteT();
			// proportion de nouveau chocolat
			double proportion_contrat = proposition.getOffre().getQuantiteT()/qtte_apres_achat;
			// ponderation
			prix_tonne_de_vente_apres_achat = prix_tonne_de_vente_contrat*proportion_contrat +b.prix_tonne_vente.get(choco)*(1-proportion_contrat) ;
		}
		// il n'y a pas de stock
		else {
			prix_tonne_de_vente_apres_achat = prix_tonne_de_vente_contrat;
		}
		b.journal_prix_vente.ajouter("Etape "+ Filiere.LA_FILIERE.getEtape()+ " : " +"ancien prix tonne de " + proposition.getOffre().getChocolat()+" est de " + b.prix_tonne_vente.get(choco) + "€");
		b.journal_prix_vente.ajouter("Etape "+ Filiere.LA_FILIERE.getEtape()+ " : " +"nouveau prix tonne de " + proposition.getOffre().getChocolat()+" est de " + prix_tonne_de_vente_apres_achat + "€");

		b.prix_tonne_vente.put((ChocolatDeMarque)proposition.getOffre().getChocolat(), prix_tonne_de_vente_apres_achat);
		
	}
}