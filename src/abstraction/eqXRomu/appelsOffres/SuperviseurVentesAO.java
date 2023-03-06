package abstraction.eqXRomu.appelsOffres;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.Banque;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.filiere.IAssermente;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class SuperviseurVentesAO implements IActeur, IAssermente {
	private Journal journal;
	private HashMap<IActeur, Integer> cryptos;
	private Banque laBanque;
	private List<IAcheteurAO> acheteurs; 

	public SuperviseurVentesAO() {
		this.journal = new Journal("Journal "+this.getNom(), this);
		this.acheteurs = new LinkedList<IAcheteurAO>();
	}
	public String getNom() {
		return "Sup.AO";
	}

	public void initialiser() {
		List<IActeur> acteurs = Filiere.LA_FILIERE.getActeurs();
		for (IActeur a : acteurs) {
			if (a instanceof IAcheteurAO) {
				this.acheteurs.add((IAcheteurAO)a);
			}
		}
		this.laBanque = Filiere.LA_FILIERE.getBanque();
	}

/**
 * 
 * @param vendeur
 * @param cryptogrammeVendeur
 * @param choco le chocolat de marque de la proposition
 * @param quantiteT doit etre superieur a OffreVente.AO_QUANTITE_MIN
 * @param enTG true si l'acheteur devra obligatoirement mettre le chocolat achete en tete de gondole (false si il n'y a pas cette obligation)
 * @return
 */
	public PropositionAchatAO vendreParAO(IVendeurAO vendeur, int cryptogrammeVendeur, ChocolatDeMarque choco, double quantiteT, boolean enTG) {
		if (vendeur==null) {
			throw new IllegalArgumentException(" appel de vendre(...) de SuperviseurVentesAppelOffre avec null pour vendeur");
		}
		if (choco==null) {
			throw new IllegalArgumentException(" appel de vendre(...) de SuperviseurVentesAppelOffre avec null pour chocolat de marque");
		}
		if (!Filiere.LA_FILIERE.getBanque().verifier(vendeur, cryptogrammeVendeur)) {
			throw new IllegalArgumentException(" appel de vendre(...) de SuperviseurVentesAppelOffre par le vendeur "+vendeur.getNom()+" avec un cryptogramme qui n'est pas le sien");
		}
		if (quantiteT<OffreVente.AO_QUANTITE_MIN) {
			throw new IllegalArgumentException(" appel de vendre(...) de SuperviseurVentesAppelOffre avec "+quantiteT+" pour quantite (min="+OffreVente.AO_QUANTITE_MIN+"))");
		}
		OffreVente offre = new OffreVente(vendeur, choco, quantiteT, enTG);
		journal.ajouter(Journal.texteColore(vendeur, vendeur.getNom())+" veut vendre "+Journal.doubleSur(quantiteT, 2)+ " de "+choco);

		List<PropositionAchatAO> propositions = new LinkedList<PropositionAchatAO>();
		if (acheteurs.size()>0) {
			for (IAcheteurAO a : acheteurs) {
				double prix = a.proposerPrix(offre);
				if (prix<=0.0) {
					journal.ajouter( Journal.texteColore(a, a.getNom()+" n'est pas interesse par l'offre "+offre));
				} else {
					if (this.laBanque.verifierCapacitePaiement((IActeur)a, cryptos.get((IActeur)a), prix*quantiteT)) {
						journal.ajouter( Journal.texteColore(a, a.getNom()+" propose un prix de "+prix));
						propositions.add( new PropositionAchatAO(offre, a, prix));
					} else { // a ne peut pas payer
						journal.ajouter( Journal.texteColore(a, a.getNom()+" a propose un prix de "+prix+" mais n'est pas en mesure de payer un tel prix"));
					}
				}
			}
			if (propositions.size()>0) {
				Collections.sort(propositions);
				Collections.reverse(propositions);
				journal.ajouter(" propositions : "+propositions);
				PropositionAchatAO retenue = vendeur.choisir(propositions);
				if (retenue != null) {
					journal.ajouter( Journal.texteColore(vendeur, vendeur.getNom()+" choisit la proposition "+retenue));
					// le virement peut se faire car on a verifie au prealable la solvabilite de l'acheteur
					this.laBanque.virer(retenue.getAcheteur(), this.cryptos.get(retenue.getAcheteur()), vendeur, retenue.getPrixT()*retenue.getOffre().getQuantiteT());
					// On notifie l'acheteur que sa proposition a ete retenue afin qu'il mette a jour ses stocks. 
					retenue.getAcheteur().notifierAchatAO(retenue);
					// on avertit les autres acheteurs que leur proposition n'a pas ete retenue
					for (PropositionAchatAO p : propositions) {
						if (!p.equals(retenue)) {
							p.getAcheteur().notifierPropositionNonRetenueAO(p);
						}
					}
				} else {
					// Aucune proposition retenue --> on avertit tous les acheteurs qui ont fait une proposition
					for (PropositionAchatAO p : propositions) {
						p.getAcheteur().notifierPropositionNonRetenueAO(p);
					}
				}
				return retenue;
			} else {
				journal.ajouter( Journal.texteColore(vendeur, vendeur.getNom()+" ne retient aucune proposition "));
				return null;
			}
		} else {
			journal.ajouter(" il n'existe aucun acheteur");
			return null;
		}
	}

	public void next() {
	}

	public String getDescription() {
		return this.getNom();
	}

	public Color getColor() {
		return new Color(240, 240, 240);
	}

	public List<String> getNomsFilieresProposees() {
		return new LinkedList<String>();
	}

	public Filiere getFiliere(String nom) {
		return null;
	}

	public List<Variable> getIndicateurs() {
		return new LinkedList<Variable>();
	}

	public List<Variable> getParametres() {
		return new LinkedList<Variable>();
	}

	public List<Journal> getJournaux() {
		List<Journal> res = new LinkedList<Journal>();
		res.add(this.journal);
		return res;
	}

	public void setCryptogramme(Integer crypto) {
	}

	public void notificationFaillite(IActeur acteur) {
		if (acheteurs.contains(acteur)) {
			acheteurs.remove(acteur);
			this.journal.ajouter(" l'acheteur "+acteur.getNom()+" fait faillite et est retire de la liste des achateurs par appels d'offres");
		}
	}

	public void notificationOperationBancaire(double montant) {
	}

	public void setCryptos(HashMap<IActeur, Integer> cryptos) {
		if (this.cryptos==null) { // Les cryptogrammes ne sont indique qu'une fois par la banque : si la methode est appelee une seconde fois c'est que l'auteur de l'appel n'est pas la banque et qu'on cherche a "pirater" l'acteur
			this.cryptos= cryptos;
		}
	}
}
