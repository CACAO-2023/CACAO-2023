// Code écrit par Florian Desplanque

package abstraction.eq2Producteur2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;

import abstraction.eqXRomu.produits.Feve;


// Classe calculant la production et les coûts de production

public class Producteur2ASProducteurPlanteur extends Producteur2AStockeur{
	private HashMap<Feve, Integer> employes;
	private HashMap<Feve, Double> salaires;
	private HashMap<Feve, Variable> surface_plantation;
	private HashMap<Feve, Double> prix;
	private HashMap<Feve,HashMap<Integer, Integer>> age_hectares;
	private HashMap<Feve, Double> cout_parcelle;
	//private double borne_min_BQ;
	//private double borne_max_HQ;
	//private double borne_min_BQ;
	//private double borne_min_BQ;
	//private double borne_min_BQ;
	
	// Pour age_hectares nous avons un Hashmap dans un autre, ici la première clef fait reference à la Feve car 
	//Les employes auront differents salaires selon la gamme sur laquelle ils travaillent
	//La deuxiemes clef est un entirer qui correspond à l'age (en step) des hectares
	//Ainsi pour chaque type de feve et chaque age on connait le nombre d'hectares que l'on possède

	public Producteur2ASProducteurPlanteur() {
		super();
		this.employes = new HashMap<Feve, Integer>();
		this.salaires = new HashMap<Feve, Double>();
		this.surface_plantation = new HashMap<Feve, Variable>();
		this.prix = new HashMap<Feve, Double>();
		this.age_hectares =  new HashMap<Feve,HashMap<Integer, Integer>>();
		for (Feve f: this.lesFeves)
			this.age_hectares.put(f, new HashMap<Integer, Integer>());
		this.cout_parcelle = new HashMap<Feve, Double>();
		this.surface_plantation.put(Feve.F_BQ, this.nbHecBasse);
		this.surface_plantation.put(Feve.F_MQ, this.nbHecMoy);
		this.surface_plantation.put(Feve.F_MQ_BE, this.nbHecMoyBE);
		this.surface_plantation.put(Feve.F_HQ_BE, this.nbHecHauteBE);
		setEmploye(30000, 30000, 25000, 5000);
		setSalaires(50,50,200,300);
		setPrix(3000, 3500, 4000, 4500);
		for (int i = -24 * 39; i <= 0; i += 24)
			setAge(Feve.F_BQ, i, 3750);
		for (int i = -24 * 39; i <= 0; i += 24)
			setAge(Feve.F_MQ, i, 3750);
		for (int i = -24 * 39; i <= 0; i += 24)
			setAge(Feve.F_MQ_BE, i, 3125);
		for (int i = -24 * 39; i <= 0; i += 24)
			setAge(Feve.F_HQ_BE, i, 625);
		setCout_Parcelle(1000, 2000, 3000, 5000);
	}
	
	
	public void initialiser() {
		super.initialiser();
		this.majSurfaceTot();
	}
	// Mise en place des differents setters
	
	
	
	protected HashMap<Feve, Integer> getEmployes(){
		return this.employes;
	}
	protected HashMap<Feve, Double> getSalaires(){
		return this.salaires;
	}

	protected HashMap<Feve, Double> getPrix(){
		return this.prix;
	}
	
	protected HashMap<Feve,HashMap<Integer, Integer>> get_Age_Hectares(){
		return this.age_hectares;
	}
	
	protected HashMap<Feve,Double> get_Cout_Parcelle(){
		return this.cout_parcelle;
	}
	
	private void majSurface(Feve f) {
		double surface = 0.;
		for (int etapePlantee : this.age_hectares.get(f).keySet())
			surface += this.age_hectares.get(f).get(etapePlantee);
		this.surface_plantation.get(f).setValeur(this, surface, this.cryptogramme);
	}
	
	private void majSurfaceTot() {
		for (Feve f : this.age_hectares.keySet()) {
			majSurface(f);
		}
	}
	
	// Mise en place des differents setters qui pour la version 1 ne sont pas encore tous utilies car seuls les 
	//plantations et le nombre d'employes evoluent 
	//
	private void setEmploye(int employes_BQ, int employes_MQ, int employes_MQ_BE, int employes_HQ_BE) {
		this.employes.put(Feve.F_BQ, employes_BQ);
		this.employes.put(Feve.F_MQ, employes_MQ);
		this.employes.put(Feve.F_MQ_BE, employes_MQ_BE);
		this.employes.put(Feve.F_HQ_BE, employes_HQ_BE);
	}
	
	/**
	 * @author Florian
	 * @param salaire_employes_BQ
	 * @param salaire_employes_MQ
	 * @param salaire_employes_MQ_BE
	 * @param salaire_employes_HQ_BE
	 */
	private void setSalaires(double salaire_employes_BQ, double salaire_employes_MQ, double salaire_employes_MQ_BE, double salaire_employes_HQ_BE) {
		this.salaires.put(Feve.F_BQ, salaire_employes_BQ);
		this.salaires.put(Feve.F_MQ,salaire_employes_MQ);
		this.salaires.put(Feve.F_MQ_BE, salaire_employes_MQ_BE);
		this.salaires.put(Feve.F_HQ_BE, salaire_employes_HQ_BE);
		
	}
	private void setPrix(double prix_BQ, double prix_MQ, double prix_MQ_BE, double prix_HQ_BE) {
		this.prix.put(Feve.F_BQ, prix_BQ);
		this.prix.put(Feve.F_MQ,prix_MQ);
		this.prix.put(Feve.F_MQ_BE, prix_MQ_BE);
		this.prix.put(Feve.F_HQ_BE, prix_HQ_BE);
		
	}
	
	private void setAge(Feve f, int age, int qte) {
		this.age_hectares.get(f).put(age, qte);
		this.majSurface(f);
	}
	
	private void setCout_Parcelle(double cout_parcelle_BQ, double cout_parcelle_MQ, double cout_parcelle_MQ_BE, double cout_parcelle_HQ_BE) {
		this.cout_parcelle.put(Feve.F_BQ, cout_parcelle_BQ);
		this.cout_parcelle.put(Feve.F_MQ, cout_parcelle_MQ);
		this.cout_parcelle.put(Feve.F_MQ_BE, cout_parcelle_MQ_BE);
		this.cout_parcelle.put(Feve.F_HQ_BE, cout_parcelle_HQ_BE);
	}
	
	
	private void Planter(Feve f, int i) {
		this.age_hectares.get(f).put(Filiere.LA_FILIERE.getEtape(), i);
		this.majSurface(f);
	}
		

	// Méthodes à utiliser dans chaque next //

	
	
	//La fonction suivante mets à jour les parcelles en supprimant les plants trop anciens
	
	private void majPlantation() {
		for (Feve f : this.age_hectares.keySet()) {
			Set<Integer> key = new HashSet<>(this.age_hectares.get(f).keySet());
			for (int etapePlantee : key)
				if (Filiere.LA_FILIERE.getEtape() - etapePlantee >= 40*24) 
					this.age_hectares.get(f).remove(etapePlantee);
		}
		this.majSurfaceTot();
	}
	
	//Entre 3 ans et 40 ans la productivité de nos hectare suivra cette gaussienne
	protected double Productivite_1_hectare(int step) {
		double P = 110/(720*Math.sqrt(2*Math.PI))*Math.exp(-1/2*((step-480)/720)^2);
		return P;
	}
	
	//La fonction ci_dessous prevois les quantities de cacao que l'on sera apte a produire à une step donnée
	//Avec nos terres actuelles. En effet dans notre cas un hectare met 3 ans pour que les cacaoyers dessus
	//puissent produire des feves, ils produisent des feves de maniere constante jusqu'a 40 ans
	
	protected HashMap<Feve, Double> Prevision_Production(int step){
		HashMap<Feve, Double> prevision_production = new HashMap<Feve, Double>();
		for (Feve f : this.age_hectares.keySet()) {
			double qte = 0;
			for (int i : this.age_hectares.get(f).keySet()){
				if (step-i<3*24) {
					qte =+ 0;
				}
				if (step-i>=3*24 && step-i<40*24) {
					qte =+ Productivite_1_hectare(i)*this.age_hectares.get(f).get(i);
				}
				
			}
			prevision_production.put(f, qte);
		}
		return prevision_production;	
	}
	
	// Cette fonction calcul les côut de production de chaque feve, on prend uniquement la masse salariale
	// dediee a chaque feve
	
	protected HashMap<Feve, Double> CoutProd(){
		HashMap<Feve, Double> coûts_totaux = new HashMap<Feve, Double>();
		for (Feve f : this.salaires.keySet()) {
			coûts_totaux.put(f, this.salaires.get(f)*this.employes.get(f)); }
		return coûts_totaux;
	}
	
	// Cette fonction calcul le coût total de la masse salariale pour un étape.
	
	protected double CoutSalaire() {
		double coût_total = 0.;
		for (Feve f : this.salaires.keySet())
			coût_total += this.salaires.get(f) * this.employes.get(f);
		return coût_total;
	}
	
	//Pour prevoir les ventes on se base sur la somme des contrats cadre et la quantite vendue en bourse
	
	protected HashMap<Feve, Double> Prevision_Vente(){
		 HashMap<Feve, Double> prevision_vente = new  HashMap<Feve, Double>();
		 prevision_vente.put(Feve.F_BQ, BQquantiteVendueBourse.getValeur());
		 prevision_vente.put(Feve.F_MQ, aLivrerStep(Filiere.LA_FILIERE.getEtape(), Feve.F_MQ)+MQquantiteVendueBourse.getValeur() );
		 prevision_vente.put(Feve.F_MQ_BE, aLivrerStep(Filiere.LA_FILIERE.getEtape(), Feve.F_MQ_BE));
		 prevision_vente.put(Feve.F_HQ_BE, aLivrerStep(Filiere.LA_FILIERE.getEtape(), Feve.F_HQ_BE));
		return prevision_vente;	
	}
	
	//Cette premiere fonction de rentrabilite permet de savoir pour une feve donne sur la prix choisi permet
	//une rentabilite superieure a 10%
	
	protected boolean Rentabilites(Feve f, Double prix){
		double rentabilite = prix * Prevision_Production(Filiere.LA_FILIERE.getEtape()).get(f)/this.salaires.get(f);
		if (rentabilite>=1.1) {
			return true;
		}
		return false;	
	}
	
	//Cette fonction est legerement differente, ici on ne renvoit pas un bouleen mais selon la feve la fonction
	//nous renvoit le prix minimum de rentabilite de celle-ci
	
	protected double prix_rentable(Feve f) {
		return 1.1*CoutProd().get(f)/Prevision_Production(Filiere.LA_FILIERE.getEtape()).get(f);
	}
	
	//
	private void ajustement_plantation() {
		for (Feve f : this.salaires.keySet()) {
			int nb_a_planter = 0;
			if (this.age_hectares.get(f).containsKey(Filiere.LA_FILIERE.getEtape() - 37*24)) {
				nb_a_planter = this.age_hectares.get(f).get(Filiere.LA_FILIERE.getEtape() - 37*24);
			}
			if (f == Feve.F_MQ_BE) {
				if (Filiere.LA_FILIERE.getEtape()%24==0) {
					nb_a_planter +=(int) Math.round(this.surface_plantation.get(f).getValeur()*0.08);
				}
			}
			if (nb_a_planter>0) {
				Planter(f, nb_a_planter);
				Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), this.cout_parcelle.get(f)*nb_a_planter);
			}
		}
	}
	// Une fois les plantation ajustees, sachant qu'un employe s'occupe d'un hectare on adapte le nombre
	//d'employes aux nombres d'hectares
	private void ajustement_employes () {
		for (Feve f : this.age_hectares.keySet()){
			double nbEmplNecessaire = this.surface_plantation.get(f).getValeur();
			if (f == Feve.F_MQ_BE ||f == Feve.F_HQ_BE ) {
				if (nbEmplNecessaire<0.95*this.employes.get(f)) {
					nbEmplNecessaire = (int) Math.ceil(0.95*this.employes.get(f));
				}
			}
			this.employes.put(f, (int) nbEmplNecessaire);	
		}
	}
	
	// On renvoie un nombre aléatoire entre une valeur min (incluse)
	// et une valeur max (exclue)
	protected double getRandomArbitrary(double min , double max) {
	  return Math.random() * (max - min) + min;
	}

	private HashMap<Feve, Double> Production_avec_intemperies() {
		HashMap<Feve, Double> prevision = Prevision_Production(Filiere.LA_FILIERE.getEtape());
		for (Feve f : Prevision_Production(Filiere.LA_FILIERE.getEtape()).keySet()) {
			if (f == Feve.F_BQ) {
				prevision.put(f, prevision.get(f)*getRandomArbitrary(0.9, 1.15));
			}
			if (f == Feve.F_MQ) {
				prevision.put(f, prevision.get(f)*getRandomArbitrary(0.9, 1.1));
			}
			if (f == Feve.F_MQ_BE) {
				prevision.put(f, prevision.get(f)*getRandomArbitrary(0.8, 1.1));
			}
			if (f == Feve.F_HQ_BE) {
				prevision.put(f, prevision.get(f)*getRandomArbitrary(0.75, 1.05));
			}
	}
		return prevision;
	}
	
	private HashMap<Feve, Double> Prevision_Production_minimale(int step) {
		HashMap<Feve, Double> prevision = Prevision_Production(step);
		for (Feve f : Prevision_Production(Filiere.LA_FILIERE.getEtape()).keySet()) {
			if (f == Feve.F_BQ) {
				prevision.put(f, prevision.get(f)*0.9);
			}
			if (f == Feve.F_MQ) {
				prevision.put(f, prevision.get(f)*0.9);
			}
			if (f == Feve.F_MQ_BE) {
				prevision.put(f, prevision.get(f)*0.8);
			}
			if (f == Feve.F_HQ_BE) {
				prevision.put(f, prevision.get(f)*0.75);
			}
	}
		return prevision;
	}
	
	public void next() {
		this.journalProd.ajouter("Etat plantation : " + this.age_hectares);
		this.journalProd.ajouter("Etat Masse Salariale : " + this.employes);
		super.next();
		this.majPlantation();
		for (Feve f : this.age_hectares.keySet()) {
			if (Production_avec_intemperies().get(f)>0){
				this.ajouterStock(f, Filiere.LA_FILIERE.getEtape(), Production_avec_intemperies().get(f));
			}
		}
		this.ajustement_plantation();
		this.ajustement_employes();
		
	}
	
	
	
}
