package abstraction.eqXRomu.contratsCadres;

import java.util.ArrayList;
import java.util.List;

import abstraction.eqXRomu.filiere.*;
import abstraction.eqXRomu.produits.IProduit;


/**
 * @author Romuald Debruyne
 * Seul le superviseurVentesContratCadre manipule des ContratCadre. Les acheteurs et
 * les vendeurs manipulent quant a eux des ExemplaireContratCadre qui sont des 
 * "copies non modifiables de contrats cadres"
 */
public class ContratCadre {
	public static long NB_CONTRATS = 0;
	public static final Double EPSILON = 0.1;
	public static final Double PENALITE_LIVRAISON = 0.03;
	public static final Double PENALITE_PAIEMENT = 0.03;

	private long numero; // numero unique identifiant le contrat
	private IAcheteurContratCadre acheteur;
	private IVendeurContratCadre vendeur;
	private IProduit produit;
	private int cryptoAcheteur;
	private boolean tg; //true si le produit est du ChocolatDeMarque et que l'acheteur s'engage a le placer en tete de gondole
	private int pourcentageRSE;   // si le produit est une feve bio/equitable, pourcentage du prix de vente du chocolat de marque qui sera produit a partir de ces feves qui seront reverses pour la RSE au producteur, de [0, 100] (0 notamment si le produit n'est pas une feve labelisee) 

	private List<Echeancier> echeanciers ; // la liste des echeanciers 
	// les echeanciers sont alternativement proposes par l'acheteur et le vendeur. Formellement : 
	// -- Pour tout k de [0, (echeanciers.size()-1)/2], echeanciers.get(2k) est un echeancier propose par l'acheteur. 
	// -- Pour tout k de [0, (echeanciers.size()-1)/2], echeanciers.get(2k+1) est un echeancier propose par le vendeur. 

	private List<Double> prix; // la liste des propositions de prix

	private Echeancier previsionnelPaiements; // Un previsionnel des paiements a effectuer par 
	// l'acheteur, etabli a la signature du contrat. 

	private Echeancier quantitesLivrees; // La quantite effectivement livree. 
	// Seul le superviseur pourra completer cet echeancier au fur et a mesure des livraisons. 


	private Echeancier paiements; // Les paiments effectues par l'acheteur. 
	// Seul le superviseur pourra completer cet echeancier au fur et a mesure des paiements. 

	private double montantRestantARegler;
	private double quantiteRestantALivrer;


	public ContratCadre(IAcheteurContratCadre acheteur,
			IVendeurContratCadre vendeur, 
			IProduit produit, 
			Echeancier livraisons, 
			int cryptogramme, 
			boolean tg) {
		if (acheteur==null) {
			throw new IllegalArgumentException("Appel du constructeur de ContratCadre avec acheteur==null");
		}
		if (vendeur==null) {
			throw new IllegalArgumentException("Appel du constructeur de ContratCadre avec vendeur==null");
		}
		if (produit==null) {
			throw new IllegalArgumentException("Appel du constructeur de ContratCadre avec produit==null");
		}
		if (livraisons==null) {
			throw new IllegalArgumentException("Appel du constructeur de ContratCadre avec livraisons==null");
		}
		if (livraisons.getQuantiteTotale()<SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER) {
			throw new IllegalArgumentException("Appel du constructeur de ContratCadre avec un echeancier ayant une quantite totale inferieure a "+SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER);
		}
		NB_CONTRATS++;
		this.numero = NB_CONTRATS;
		this.acheteur = acheteur;
		this.vendeur = vendeur; 
		this.produit = produit;
		this.echeanciers = new ArrayList<Echeancier>();
		this.echeanciers.add(new Echeancier(livraisons));
		this.prix = new ArrayList<Double>();
		this.quantitesLivrees=null;// ce n'est qu'une fois le contrat signe qu'un echeancier memorisant les livraisons sera cree.
		this.paiements = null;
		this.previsionnelPaiements = null;
		this.quantiteRestantALivrer = 0.0;
		this.montantRestantARegler = 0.0;
		this.cryptoAcheteur=cryptogramme;
		this.tg = tg;
		this.pourcentageRSE=0;
	}

	public ContratCadre(IAcheteurContratCadre acheteur,
			IVendeurContratCadre vendeur, 
			IProduit produit, 
			Echeancier livraisons, 
			int cryptogramme, 
			boolean tg, 
			int pourcentageRSE) {
		this(acheteur, vendeur, produit, livraisons, cryptogramme, tg);
		this.pourcentageRSE=pourcentageRSE;
	}

	public long getNumero() {
		return this.numero;
	}

	public IAcheteurContratCadre getAcheteur() {
		return this.acheteur;
	}

	public IVendeurContratCadre getVendeur() {
		return this.vendeur;
	}

	public IProduit getProduit() {
		return this.produit;
	}

	public Double getQuantiteTotale() {
		return this.echeanciers.get(this.echeanciers.size()-1).getQuantiteTotale();
	}

	public int getCryptogramme() {
		return this.cryptoAcheteur;
	}
	
	public boolean getTeteGondole() {
		return this.tg;
	}
	
	public int getPourcentageRSE() {
		return this.pourcentageRSE;
	}
	
	public void signer() {
		this.quantitesLivrees = new Echeancier(this.getEcheancier().getStepDebut()); // les livraisons debuteront en debut de contrat.
		this.paiements = new Echeancier(this.getEcheancier().getStepDebut()); 
		this.previsionnelPaiements = new Echeancier(this.getEcheancier().getStepDebut()); 
		Echeancier e = this.getEcheancier();
		this.montantRestantARegler = 0.0;
		for (int step = e.getStepDebut(); step<=e.getStepFin(); step++) {
			if (e.getQuantite(step)>0.0) {
				double quantiteStep =  e.getQuantite(step);
				double montantStep = quantiteStep*this.getPrix();
				this.previsionnelPaiements.set(step, montantStep);
				this.montantRestantARegler += montantStep;
				this.quantiteRestantALivrer += quantiteStep;
			}
		}
	}

	public double getMontantRestantARegler() {
		return this.montantRestantARegler<EPSILON ? 0.0 : this.montantRestantARegler;
	}

	public double getQuantiteRestantALivrer() {
		return this.quantiteRestantALivrer<EPSILON ? 0.0 : this.quantiteRestantALivrer;
	}

	public void ajouterEcheancier(Echeancier e) {
		if (e==null) {
			throw new IllegalArgumentException("Appel de la methode ajouterEcheancier(e) avec e==null");
		}
		if (e.getQuantiteTotale()<SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER) {
			throw new IllegalArgumentException("Appel de la methode ajouterEcheancier(e) avec e.getQuantiteTotale()<"+SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER);
		}
		this.echeanciers.add(e);
	}

	public void ajouterPrix(Double p) {
		if (!Double.isNaN(p)) {
			this.prix.add(p);
		}
	}

	public List<Echeancier> getEcheanciers() {
		List<Echeancier> res = new ArrayList<Echeancier>();
		for (Echeancier e : this.echeanciers) {
			res.add(new Echeancier(e));
		}
		return res;
	}

	public Echeancier getEcheancier() {
		if (this.echeanciers==null || this.echeanciers.size()<1) {
			return null;
		} else {
			return new Echeancier(this.echeanciers.get(this.echeanciers.size()-1));
		}
	}

	public boolean accordSurEcheancier() {
		return this.echeanciers!=null && this.echeanciers.size()>=2 && this.echeanciers.get(this.echeanciers.size()-1).equals(this.echeanciers.get(this.echeanciers.size()-2));
	}

	public Echeancier getQuantiteLivree() {
		return new Echeancier(this.quantitesLivrees);
	}

	public Echeancier getPaiementsEffectues() {
		return new Echeancier(this.paiements);
	}

	public double getQuantiteALivrerAuStep() {
		if (Filiere.LA_FILIERE==null) {
			return 0.0;
		}
		int step = Filiere.LA_FILIERE.getEtape();
		if (step<=this.getEcheancier().getStepFin()) {
			return this.getEcheancier().getQuantiteJusquA(step)- (this.getQuantiteLivree()==null ? 0.0 : this.getQuantiteLivree().getQuantiteJusquA(step));
		} else {
			return this.quantiteRestantALivrer;
		}
	}

	public double getPaiementAEffectuerAuStep() {
		if (Filiere.LA_FILIERE==null) {
			return 0.0;
		}
		int step = Filiere.LA_FILIERE.getEtape();
		double prixDePasLivre = getQuantiteALivrerAuStep()*this.getPrix();
		if (step<=this.getEcheancier().getStepFin()) {
			return this.previsionnelPaiements.getQuantiteJusquA(step) -(this.paiements==null ? 0.0 : this.paiements.getQuantiteJusquA(step))-prixDePasLivre;
		} else {
			return this.montantRestantARegler-prixDePasLivre; 
		}
	}


	public List<Double> getListePrix() {
		List<Double> res = new ArrayList<Double>();
		for (Double p : this.prix) {
			res.add(p);
		}
		return res;
	}

	public Double getPrix() {
		return this.prix.size()>0 ? this.prix.get(this.prix.size()-1) : Double.NaN;
	}

	public boolean accordSurPrix() {
		return this.prix!=null && this.prix.size()>=2 && this.prix.get(this.prix.size()-1).equals(this.prix.get(this.prix.size()-2));
	}

	public String toString() {
		String res = "Contrat #"+this.getNumero()+"\nAcheteur:"+(this.getAcheteur().getNom())+"\n"
				+"Vendeur:"+(this.getVendeur().getNom())+"\n"
				+"Produit:"+this.getProduit()+"\n"
				+"Quantite:"+String.format("%.4f",this.getQuantiteTotale())+"\n"
				+"Echeanciers:\n";
		for (Echeancier e : this.echeanciers) {
			res = res+e.toString()+"\n";
		}
		res=res+"Prix :\n";
		for (Double p : this.prix) {
			res=res+String.format("%.4f",p)+"\n";
		}
		return res;
	}

	public String toHtml() {
		return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+this.toString().replaceAll("\n",  "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	}

	public String oneLineHtml() {
		return "#"+this.getNumero()
		+" Ac="+((IActeur)(this.getAcheteur())).getNom()
		+" Ve="+((IActeur)(this.getVendeur())).getNom()
		+" Pr="+this.getProduit()
		+" Qu="+String.format("%.4f",this.getQuantiteTotale())
		+" ["+this.getEcheancier().getStepDebut()+"->"+this.getEcheancier().getStepFin()+"]"
		+" Px="+String.format("%.4f",this.getPrix())
		+" RL="+String.format("%.4f",this.getQuantiteRestantALivrer())
		+" RP="+String.format("%.4f",this.getMontantRestantARegler())		     ;
	}

	public void livrer(Double quantite) {
		if (quantite<0.0) {
			throw new IllegalArgumentException("Appel de la methode livrer(quantite) de ContratCadre avec quantite<0.0 ( "+quantite+" )");
		}
		this.quantitesLivrees.set(Filiere.LA_FILIERE.getEtape(), quantite);
		this.quantiteRestantALivrer-=quantite;
	}

	public void payer(Double montant) {
		if (montant<0.0) {
			throw new IllegalArgumentException("Appel de la methode payer(montant) de ContratCadre avec montant<0.0 ( "+montant+" )");
		}
		this.paiements.set(Filiere.LA_FILIERE.getEtape(), montant);
		this.montantRestantARegler-=montant;
	}

	public void penaliteLivraison() {
		int step = Filiere.LA_FILIERE.getEtape();
		if (step<=this.getEcheancier().getStepFin()) {
			double nonLivre = this.getEcheancier().getQuantiteJusquA(step)- (this.getQuantiteLivree()==null ? 0.0 : this.getQuantiteLivree().getQuantiteJusquA(step));
			this.quantiteRestantALivrer += nonLivre*PENALITE_LIVRAISON;
		} else {
			this.quantiteRestantALivrer *= (1.0+PENALITE_LIVRAISON);
		}
	}

	public void penalitePaiement() {
		int step = Filiere.LA_FILIERE.getEtape();
		if (step<=this.getEcheancier().getStepFin()) {
			double prixDePasLivre = getQuantiteALivrerAuStep()*this.getPrix();
			double nonPaye = this.previsionnelPaiements.getQuantiteJusquA(step)- (this.paiements==null ? 0.0 : this.paiements.getQuantiteJusquA(step))-prixDePasLivre;
			this.montantRestantARegler += nonPaye*PENALITE_PAIEMENT;
		} else {
			this.montantRestantARegler *= (1.0+PENALITE_PAIEMENT);
		}
	}
}