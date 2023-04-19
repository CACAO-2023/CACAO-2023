package abstraction.eqXRomu.bourseCacao;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.Banque;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.filiere.IAssermente;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariableReadOnly;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class BourseCacao implements IActeur, IAssermente {
	private HashMap<IActeur, Integer> cryptos;
	private Integer crypto;
	private HashMap<Feve, Journal> journal;
	private List<IVendeurBourse> vendeurs;
	private List<IAcheteurBourse> acheteurs;
	private HashMap<Feve, Variable> cours;
	private HashMap<IAcheteurBourse, Integer> blackListA; //nombre de steps pendant lequel l'acheteur ne peut plus acheter (car a fait une demande qu'il n'a pas pu honorer)
	private HashMap<IVendeurBourse, Integer> blackListV; //nombre de steps pendant lequel l'acheteur ne peut plus acheter (car a fait une demande qu'il n'a pas pu honorer)

	public BourseCacao() {
		this.journal = new HashMap<Feve, Journal>();
		this.cours=new HashMap<Feve, Variable>();
		// 1472 euros = 1600 dollars prix min du cours du cacao par tonne (1830 sur les 20 dernieres annees)
		// 2417 euros = 2627 dollars prix moy du cours du cacao par tonne de l'annee passee
		// 3221 euros = 3500 dollars prix max du cours du cacao par tonne (3321 sur les 20 dernieres annees)

		this.cours.put(Feve.F_MQ, new VariableReadOnly(getNom()+" cours M", "<html>le cours actuel<br>de FEVE_MOYENNE</html>", this,1472.0, 3221.0, 2417.0)); 
		this.cours.put(Feve.F_BQ, new VariableReadOnly(getNom()+" cours B", "<html>le cours actuel<br>de FEVE_BASSE</html>", this,1272.0, 3021.0, 2217.0));
		// PAS d'equitable en bourse : l'echange de produits equitables s'effectue via
		// des contrats cadre.
		// Un producteur qui aurait un stock excessif de F_MQ_BE pourrait eventuellement
		// les mettre en vente en bourse mais en tant que F_MQ  (perte du caractere
		//this.cours.put(Feve.F_HQ_BE, new VariableReadOnly(getNom()+" cours HBE", "<html>le cours actuel<br>de FEVE_HAUTE_BIO_EQUITABLE</html>", this, 1772.0, 3521.0, 2717.0));  
		//this.cours.put(Feve.F_MQ_BE, new VariableReadOnly(getNom()+" cours MBE", "<html>le cours actuel<br>de FEVE_MOYENNE_BIO_EQUITABLE</html>", this,1572.0, 3321.0, 2517.0));  
		this.blackListA=new HashMap<IAcheteurBourse, Integer>();
		this.blackListV=new HashMap<IVendeurBourse, Integer>();
		for (Feve f : Feve.values()) {
			if (!f.isBioEquitable()) {
				this.journal.put(f, new Journal("Journal "+this.getNom()+" "+f, this));
			}
		}
	}

	public Variable getCours(Feve f) {
		if (f==null || !this.cours.keySet().contains(f)) {
			throw new IllegalArgumentException("Appel de getCours(f) de BourseCacao avec un parametre invalide ("+f+")");
		} else {
			return this.cours.get(f);
		}
	}
	public String getNom() {
		return "BourseCacao";
	}

	public String getDescription() {
		return this.getNom();
	}

	public Color getColor() {
		return new Color(215, 215, 215);
	}

	public List<String> getNomsFilieresProposees() {
		return new LinkedList<String>();
	}

	public Filiere getFiliere(String nom) {
		return null;
	}

	public List<Variable> getIndicateurs() {
		List<Variable> res = new LinkedList<Variable>();
		for (Feve f : Feve.values()) {
			if (!f.isBioEquitable()) {
				res.add(this.cours.get(f));
			}
		}
		return res;
	}

	public List<Variable> getParametres() {
		return new LinkedList<Variable>();
	}

	public List<Journal> getJournaux() {
		List<Journal> res = new LinkedList<Journal>();
		for (Feve f : Feve.values()) {
			if (!f.isBioEquitable()) {
				res.add(this.journal.get(f));
			}
		}
		return res;
	}

	public void setCryptogramme(Integer crypto) {
		this.crypto = crypto;
	}

	public void notificationFaillite(IActeur acteur) {
		if (acteur instanceof IVendeurBourse) {
			this.vendeurs.remove((IVendeurBourse)acteur);
		}
		if (acteur instanceof IAcheteurBourse) {
			this.acheteurs.remove((IAcheteurBourse)acteur);
		}
	}

	public void notificationOperationBancaire(double montant) {
	}

	public void setCryptos(HashMap<IActeur, Integer> cryptos) {
		if (this.cryptos==null) { // Les cryptogrammes ne sont indique qu'une fois par la banque : si la methode est appelee une seconde fois c'est que l'auteur de l'appel n'est pas la banque et qu'on cherche a "pirater" l'acteur
			this.cryptos= cryptos;
		}
	}

	public void initialiser() {
		this.vendeurs = new LinkedList<IVendeurBourse>();
		this.acheteurs = new LinkedList<IAcheteurBourse>();
		List<IActeur> acteurs = Filiere.LA_FILIERE.getActeurs();
		for (IActeur acteur : acteurs) {
			System.out.println("acteur ="+acteur);
			if (acteur instanceof IVendeurBourse) {
				System.out.println("vendeur");
				this.vendeurs.add(((IVendeurBourse)acteur));
				this.blackListV.put((IVendeurBourse)acteur, 0);
			}
			if (acteur instanceof IAcheteurBourse) {
				System.out.println("acheteur");
				this.acheteurs.add(((IAcheteurBourse)acteur));
				this.blackListA.put((IAcheteurBourse)acteur, 0);
			}
		}
	}

	public void next() {
		Banque banque = Filiere.LA_FILIERE.getBanque();
		for (Feve f : Feve.values()) {
			if (!f.isBioEquitable()) {
				HashMap<IAcheteurBourse, Double> demandes=new HashMap<IAcheteurBourse, Double>();
				double totalDemandes=0;
				double cours = this.cours.get(f).getValeur();
				for (IAcheteurBourse acheteur : this.acheteurs) {
					if (blackListA.get(acheteur)==0) {
						double demande = acheteur.demande(f, cours);
						journal.get(f).ajouter(Journal.texteColore((IActeur)acheteur, ((IActeur)acheteur).getNom()+" souhaite acheter "+Journal.doubleSur(demande, 2)+" T de "+f));
						if (demande>0) {
							if (banque.verifierCapacitePaiement((IActeur)acheteur, cryptos.get((IActeur)acheteur), cours*demande)) {
								demandes.put(acheteur, demande);
								totalDemandes+=demande;
							} else {
								acheteur.notificationBlackList(6);
								blackListA.put(acheteur,6);
							}
						}
					} else {
						blackListA.put(acheteur,blackListA.get(acheteur)-1);
					}
				}
				HashMap<IVendeurBourse, Double> offres=new HashMap<IVendeurBourse, Double>();
				double totalOffres=0;
				for (IVendeurBourse vendeur : this.vendeurs) {
					if (blackListV.get(vendeur)==0) {
						double offre = vendeur.offre(f, cours);
						journal.get(f).ajouter(Journal.texteColore((IActeur)vendeur, ((IActeur)vendeur).getNom()+" souhaite vendre  "+Journal.doubleSur(offre, 2)+" T de "+f));
						if (offre>0) {
							offres.put(vendeur, offre);
							totalOffres+=offre;
						}
					}else {
						blackListV.put(vendeur,blackListV.get(vendeur)-1);
					}
				}

				if (totalOffres>=totalDemandes && totalDemandes>0.0) { // Les acheteurs vont obtenir la quantite souhaitee et vendeurs vendre au prorata de l'offre qu'ils ont faite
					Lot lotGlobal = new Lot(f);
					journal.get(f).ajouter("l'offre ("+Journal.doubleSur(totalOffres, 2)+") est superieure a la demande ("+Journal.doubleSur(totalDemandes, 2)+")");
					for (IVendeurBourse v : offres.keySet()){
						// La quantite vendue est au prorata de la quantite mis en vente
						double quantite = (totalDemandes*offres.get(v))/totalOffres; 
						Lot lot = v.notificationVente(f, quantite,cours);
						if (lot!=null && lot.getProduit().equals(f) && lot.getQuantiteTotale()>=quantite) {
							banque.virer(this, crypto, (IActeur)v,cours*quantite);
							lotGlobal.ajouter(lot);
							journal.get(f).ajouter(Journal.texteColore((IActeur)v, ((IActeur)v).getNom()+" vend "+Journal.doubleSur(quantite, 2)+" et est paye "+Journal.doubleSur(cours*quantite, 2)));
						} else {
							v.notificationBlackList(6);
							blackListV.put(v,6);
						}
					}
					totalOffres = lotGlobal.getQuantiteTotale(); // certains ont pu ne pas parvenir a tout livrer
					for (IAcheteurBourse a : demandes.keySet()){
						// S'il demeure plus d'offres que de demande la demande est honoree, sinon c'est au prorata
						double quantite = totalOffres>=totalDemandes ? demandes.get(a) : (totalOffres*demandes.get(a))/totalDemandes;
						boolean virementOk = banque.virer((IActeur)a, cryptos.get((IActeur)a), this,cours*quantite);
						if (virementOk) {
							Lot lot = lotGlobal.retirer(quantite);
							a.notificationAchat(lot, cours);
							journal.get(f).ajouter(Journal.texteColore((IActeur)a, ((IActeur)a).getNom()+" obtient "+Journal.doubleSur(lot.getQuantiteTotale(),2)+" et paye "+Journal.doubleSur(cours*lot.getQuantiteTotale(), 2)));
						} else { // Normalement la transaction peut avoir lieu vu qu'on a verifie au prealable la capacite du vendeur a payer
							a.notificationBlackList(6);
							blackListA.put(a,6);
						}
					}
				} else if (totalOffres<=totalDemandes && totalOffres>0.0){ // offre<demande : Les vendeurs vont vendre tout ce qu'ils ont mis en vente et les acheteurs auront des feves au prorata de leur proposition d'achat
					journal.get(f).ajouter("la demande ("+Journal.doubleSur(totalDemandes, 2)+") est superieure a l'offre ("+Journal.doubleSur(totalOffres, 2)+")");
					Lot lotGlobal = new Lot(f);
					for (IVendeurBourse v : offres.keySet()){
						// Chaque vendeur vend tout ce qu'il a annonce vouloir vendre
						double quantite = offres.get(v); 
						Lot lot = v.notificationVente(f, quantite,cours);
						if (lot!=null && lot.getProduit().equals(f) && lot.getQuantiteTotale()>=quantite) {
							banque.virer(this, crypto, (IActeur)v,cours*quantite);
							lotGlobal.ajouter(lot);
							journal.get(f).ajouter(Journal.texteColore((IActeur)v, ((IActeur)v).getNom()+" vend "+Journal.doubleSur(quantite, 2)+" et est paye "+Journal.doubleSur(cours*quantite, 2)));
						} else {
							v.notificationBlackList(6);
							blackListV.put(v,6);
						}
					}
					totalOffres = lotGlobal.getQuantiteTotale(); // certains ont pu ne pas parvenir a tout livrer
					for (IAcheteurBourse a : demandes.keySet()){
						// La quantite achetee est au prorata de la quantite demandee
						double quantite = (totalOffres*demandes.get(a))/totalDemandes; 
						if (cours*quantite>0) {
							boolean virementOk = banque.virer((IActeur)a, cryptos.get((IActeur)a), this,cours*quantite);
							if (virementOk) { // normalement c'est le cas vu qu'on a verifie auparavant
								Lot lot = lotGlobal.retirer(quantite);
								a.notificationAchat(lot, cours);
								journal.get(f).ajouter(Journal.texteColore((IActeur)a, ((IActeur)a).getNom()+" obtient "+Journal.doubleSur(quantite,2)+" et paye "+Journal.doubleSur(cours*quantite, 2)));
							} else {
								a.notificationBlackList(6);
								blackListA.put(a,6);
							}
						}
					}
				}
				// Mise a jour du cours.
				if ( totalDemandes==0.0 && totalOffres==0) {
					// il ne se passe rien
				} else if (totalDemandes==0.0 && totalOffres>0.0) {
					double diminution = Math.random()*2.0;  // ca va diminuer entre 0 et 2%
					double min = this.cours.get(f).getMin();
					if (cours * (1.0-(diminution/100.0))<min) {
						diminution=Math.min(diminution, Math.random()*0.1); // Si ca fait aller en dessous du Min connu alors on diminue au plus de 1 pour mille
					}
					cours = cours * (1.0- (diminution/100.0));
					this.cours.get(f).setValeur(this, cours, crypto);
				} else if (totalDemandes>0.0 && totalOffres==0.0) {
					double augmentation = Math.random()*2.0;  // ca va augmenter entre 0 et 2%
					double max = this.cours.get(f).getMax();
					if (cours * (1.0+ (augmentation/100.0))>max) {
						augmentation=Math.min(augmentation, Math.random()*0.1); // Si ca fait aller au dela du Max connu alors on augmente au plus de 1 pour 1000
					}
					cours = cours * (1.0+ (augmentation/100.0));
					this.cours.get(f).setValeur(this, cours, crypto);
				} else if (totalDemandes>totalOffres) {// Le cours va monter
					double augmentation = Math.max(Math.random()*1.5, Math.min(12.5,  (totalDemandes-totalOffres)/totalOffres)); // plus l'ecart entre demande et offre est eleve, plus l'agumentation sera forte, mais pas plus de 12.5% d'augmentation, pas moins qu'un nombre au hasard tire entre 0 et 1.5.
					double max = this.cours.get(f).getMax();
					if (cours * (1.0+ (augmentation/100.0))>max) {
						augmentation=Math.min(augmentation, Math.random()*0.1); // Si ca fait aller au dela du Max connu alors on augmente au plus de 1 pour 1000
					}
					cours = cours * (1.0+ (augmentation/100.0));
					this.cours.get(f).setValeur(this, cours, crypto);
				} else { // le cours va baisser
					double diminution = Math.max(Math.random()*1.5, Math.min(12.5,  (totalOffres-totalDemandes)/totalDemandes)); // plus l'ecart entre demande et offre est eleve, plus la diminution sera forte, mais pas plus de 12.5% de diminution, pas moins qu'un nombre au hasard tire entre 0 et 1.5.
					double min = this.cours.get(f).getMin();
					if (cours * (1.0-(diminution/100.0))<min) {
						diminution=Math.min(diminution, Math.random()*0.1); // Si ca fait aller en dessous du Min connu alors on diminue au plus de 1 pour mille
					}
					cours = cours * (1.0- (diminution/100.0));
					this.cours.get(f).setValeur(this, cours, crypto);
				}
				journal.get(f).ajouter("--> Le cours de la feve "+f+" passe a :"+Journal.doubleSur(cours, 4));
			}
		}
	}
}
