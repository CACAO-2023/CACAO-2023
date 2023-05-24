package abstraction.eq3Producteur3;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.util.Timer;
import java.util.TimerTask;

import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Historique;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;


public class Producteur3 extends Bourse3  {



	private Double SeuilHG;
	private Double SeuilMG;

	private Integer HectaresLibres; /*Repertorie le nombre d'hectares libres que l'on possede*/
	private Integer HectaresUtilises; /*Repertorie le nombre d'hectares que l'on utilise*/
	private LinkedList<Double> ListeCout; /*Les couts des 18 steps precedents, y compris celui-la*/
	private HashMap<Integer,Integer> achatHectarCout;
	
	private Double CoutTonne; /*Le cout par tonne de cacao, calcule sur 18 step (destruction de la feve apres 9 mois), le meme pour toute gamme*/


	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public Producteur3() {
		super();
		this.fields = new Champs();
		this.SeuilHG = 0.;
		this.SeuilMG = 0.;
		this.quantiteVenduBourseB = new HashMap<Integer, Double>();
		this.quantiteVenduBourseM = new HashMap<Integer, Double>();
		this.CoutStep = 0.0;
		this.CoutTonne = 0.;
		this.HectaresLibres = 0;
		//this.HectaresUtilises = 950000; premiers essais
		this.HectaresUtilises = 728000; //optimises
		this.ListeCout = new LinkedList<Double>();
		this.achatHectarCout = new HashMap<Integer, Integer>();
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void updateListeCout() {
		this.ListeCout.add(this.CoutStep);
		if (ListeCout.size() >= 8) {
			this.ListeCout.removeFirst();
		}
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void updateCoutTonne() {
		Double CoutTotal = 0.;

		if (this.ListeCout.size() == 0) {
			this.CoutTonne = 0.;
			return;
		}

		for (Integer i = 0 ; i < this.ListeCout.size() ; i += 1) {
			CoutTotal += this.ListeCout.get(i);
		}
		CoutTotal = CoutTotal / this.ListeCout.size();


		Stock Stock = this.getStock();
		this.CoutTonne = CoutTotal / Math.max(Stock.getQuantite(), 1);
		
		if (this.CoutTonne > 10000) {
			this.CoutTonne = 10000.;
		}
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void initialiser() {
		super.initialiser();
		this.CoutStep += Stock.getQuantite()*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur();
		this.addCoutHectaresUtilises();
		this.updateListeCout();
		this.updateCoutTonne();
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	protected Champs getFields() {
		return this.fields;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	protected Stock getStock() {
		// TODO Auto-generated method stub
		return this.Stock;
	}
	protected Integer getHectaresUt() {
		return this.HectaresUtilises;
	}

	/**
	 * @author BOCQUET Gabriel, Dubus-Chanson Victor, Caugant Corentin
	 */
	public void next() {
		super.next();
		HarvestToStock(Filiere.LA_FILIERE.getEtape());
		this.Stock = Stock.miseAJourStock();

		// Now adding to the step cost the storage costs
		
		updateHectaresLibres(Filiere.LA_FILIERE.getEtape());
		if (Filiere.LA_FILIERE.getEtape() % 12 == 0) {
			if (Filiere.LA_FILIERE.getEtape() != 0) {
				if (Filiere.LA_FILIERE.getEtape() == 12) {
					changeHectaresAndCoutsLies(variationBesoinHectares(Filiere.LA_FILIERE.getEtape()));
				}
				else {
					changeHectaresAndCoutsLies(variationBesoinHectaresv2(Filiere.LA_FILIERE.getEtape(), VentesMG, VentesHG));
				}
			}
		}

		
		//Greve ?
		double probaGreve = Math.random();
		if(probaGreve < this.probaGreve.getValeur()){
					this.GreveGeneral();

		}
		// Incendie ?	
		double probaIncendie =  Math.random();
				if(probaIncendie < this.probaIncendiH.getValeur()) {
					this.Fire("Big");
				}
				else if(probaIncendie < this.probaIncendiM.getValeur()) {
					this.Fire("Med");
				}
				else if(probaIncendie < this.probaIncendiL.getValeur()) {	
					this.Fire("Lit");
				}
				//Cyclone ou tempete ?
				double probaCyclone =  Math.random();
				if(probaCyclone <this.probaCyclone.getValeur()) {
					this.Cyclone();
			}
			// We only add the costs to CoutStep if we are not at step zero :
			if (Filiere.LA_FILIERE.getEtape() > 0) {
				this.CoutStep += Stock.getQuantite()*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur();
				this.addCoutHectaresUtilises();
			}

			this.updateListeCout();
			this.updateCoutTonne();
		this.getJAchats().ajouter(Color.yellow, Color.BLACK, "Coût du step : " + this.CoutStep + ", Hectares Achetés : " + this.HectaresAchetes.getValeur() + ", Coût de la tonne : " + this.CoutTonne);

		this.getJGeneral().ajouter(Color.cyan, Color.BLACK, 
				"Step Actuelle : " + Filiere.LA_FILIERE.getEtape()+", Taille total des Champs utilisés : "+ this.HectaresUtilises+", Taille des champs libres" + this.HectaresLibres + ", Nombre d'employe : "+ (this.HectaresUtilises + this.HectaresLibres));
		
		Filiere.LA_FILIERE.getBanque().virer(this, super.getCryptogramme(), Filiere.LA_FILIERE.getBanque(), CoutStep);
		this.getJOperation().ajouter(Color.cyan, Color.BLACK, "On a paye "+ this.CoutStep + "euros de frais divers");
		this.CoutStep = 0.0;
		this.getJPlantation().ajouter(Color.GRAY,Color.BLACK,this.printField("H"));
		this.getJPlantation().ajouter(Color.GREEN,Color.BLACK,this.printField("M"));
		this.StockFeveH.setValeur(this, super.Stock.getQuantite(Feve.F_HQ_BE));
		this.StockFeveM.setValeur(this, super.Stock.getQuantite(Feve.F_MQ_BE));
		this.StockFeveB.setValeur(this, super.Stock.getQuantite(Feve.F_BQ));
		this.tailleH.setValeur(this, super.fields.getTaille("H"));
		this.tailleM.setValeur(this, super.fields.getTaille("M"));
		this.coutMoyen.setValeur(this, this.CoutTonne);
		this.coutSalaireTot.setValeur(this,(this.HectaresUtilises + this.HectaresLibres)*this.coutEmployeStep.getValeur());
		this.BeneficeB.setValeur(this, this.getBenefice("B"));
		this.BeneficeM.setValeur(this, this.getBenefice("M"));
		this.BeneficeH.setValeur(this, this.getBenefice("H"));
		this.HectaresAchetes.setValeur(this, 0);
	}
	
	

	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void addCoutHectaresUtilises() {
		Integer coutEmployes = (this.HectaresUtilises + this.HectaresLibres) * ((int)this.coutEmployeStep.getValeur());
		this.CoutStep = this.CoutStep + coutEmployes;
	}
	
	public String toString() {
		return this.getNom();
	}
	

	/**
	 * @author BOCQUET Gabriel
	 */
	//Cette fonction ajoute  a chaque step les feves recoltees
	
	public void HarvestToStock(int step) {
		LinkedList<Integer> quantite = this.getFields().HarvestHM(step);
		Stock Stock = this.getStock();
		if(quantite.get(0) > 0) {
		Stock.ajouter(Feve.F_HQ_BE, quantite.get(0));
		}
		if(quantite.get(1) > 0) {
		Stock.ajouter(Feve.F_MQ_BE, quantite.get(1));
		}
		this.getJStock().ajouter(Color.GREEN, Color.BLACK,"On a ajoute "+ quantite.get(1) +" tonnes au stock de Moyenne Gamme le step n°"  +Filiere.LA_FILIERE.getEtape());
		this.getJStock().ajouter(Color.GREEN, Color.BLACK,"A l'étape "  +Filiere.LA_FILIERE.getEtape() + " les stocks de Moyenne Gamme sont de " + this.getStock().getQuantite(Feve.F_MQ_BE));
		this.getJStock().ajouter(Color.LIGHT_GRAY, Color.BLACK,"On a ajoute "+ quantite.get(0) +"tonnes au stock de Haute Gamme le step n°"  +Filiere.LA_FILIERE.getEtape());
		this.getJStock().ajouter(Color.LIGHT_GRAY, Color.BLACK,"A l'étape "  +Filiere.LA_FILIERE.getEtape() + " les stocks de Haute Gamme sont de " + this.getStock().getQuantite(Feve.F_HQ_BE));
	}


	/**
	 * @author Dubus-Chanson Victor
	 */
	
	/*Calcule le nombre d'Hectares (uniquement positif ou nul) que l'on a besoin de rajouter a la partie cultivee (seulement tous les 6 mois)*/
	/*A modifier, a besoin des quantites de feves echangees (via stock)*/
	
	public Integer variationBesoinHectares(Integer CurrentStep) {
		Integer BesoinHQ = 0;
		Integer BesoinMQ = 0;
		Stock Stock = this.getStock();
		Double Quantite_HQ_BE= Stock.getQuantite(Feve.F_HQ_BE);
		Double Quantite_MQ_BE= Stock.getQuantite(Feve.F_MQ_BE);
		if (Quantite_HQ_BE < 50000) {
			BesoinHQ += 1000; /*560 tonnes de plus par an à partir de 5ans*/
			HashMap<Integer, Integer> ChampsH = this.fields.getChamps().get("H");
			ChampsH.put(CurrentStep, BesoinHQ);
			this.fields.getChamps().put("H", ChampsH);
		}
		if (Quantite_MQ_BE < 500000) {
			BesoinMQ += 1000; /*560 tonnes de plus par an à partir de 5ans*/
			HashMap<Integer, Integer> ChampsM = this.fields.getChamps().get("M");
			ChampsM.put(CurrentStep, BesoinMQ);
			this.fields.getChamps().put("M", ChampsM);
		}
		/*LinkedList<Integer> Besoin = new LinkedList<Integer>();
		Besoin.add(BesoinMQ);
		Besoin.add(BesoinHQ);
		return Besoin;*/
		return BesoinHQ + BesoinMQ;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	/*Initialise le seuil de HG des 6 premiers mois*/
	public void setSeuilHG(LinkedList<Double> Liste12DernieresVentesHG) {
		Double M = 0.;
		for (Double i : Liste12DernieresVentesHG) {
			M += i;
		}
		this.SeuilHG = M/12;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	/*Initialise le seuil de MG des 6 premiers mois*/
	public void setSeuilMG(LinkedList<Double> Liste12DernieresVentesMG) {
		Double M = 0.;
		for (Double i : Liste12DernieresVentesMG) {
			M += i;
		}
		this.SeuilMG = M/12;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 * @param CurrentStep
	 * @param Liste12DernieresVentes
	 * @param Seuil
	 * @return
	 */
	public Integer besoinHectares(Integer CurrentStep, LinkedList<Double> Liste12DernieresVentes, Double Seuil) {
		Double M12 = 0.;
		Double M4 = 0.;
		Integer besoin = 0;
		Double prix = 0.;
		for (Double i : Liste12DernieresVentes) {
			M12 += i;
		}
		M4 += Liste12DernieresVentes.get(11);
		M4 += Liste12DernieresVentes.get(10);
		M4 += Liste12DernieresVentes.get(9);
		M4 += Liste12DernieresVentes.get(8);
		if (M4 < (Seuil + 5000) && M4 > (Seuil - 10000)) {
			if (M12 > (Seuil + 5000)) {
				prix = M12 - Seuil;
				besoin = (int)(prix / 2500.); //2500euros etant ce que l'on considere comme ce qu'un hectare peut nous rapporter par recolte.
			}
		}
		
		if (M4 > (Seuil + 5000)) {
			if (M12 > (Seuil - 10000)) {
				prix = M12 - Seuil;
				besoin = (int)(prix / 2500.);
			}
		}
		return besoin;
	}
	
	
	/**
	 * @author Dubus-Chanson Victor
	 * @param CurrentStep
	 * @param Liste12DernieresVentesMG
	 * @param Liste12DernieresVentesHG
	 * @return
	 */
	public Integer variationBesoinHectaresv2(Integer CurrentStep, LinkedList<Double> Liste12DernieresVentesMG, LinkedList<Double> Liste12DernieresVentesHG) {
		Integer besoinHG = besoinHectares(CurrentStep, Liste12DernieresVentesHG, this.SeuilHG);
		Integer besoinMG = besoinHectares(CurrentStep, Liste12DernieresVentesHG, this.SeuilMG);
		
		if (besoinHG != 0) {
			this.SeuilHG += besoinHG * 2500.;
			HashMap<Integer, Integer> ChampsH = this.fields.getChamps().get("H");
			ChampsH.put(CurrentStep, besoinHG);
			this.fields.getChamps().put("H", ChampsH);
		}
		
		if (besoinMG != 0) {
			this.SeuilHG += besoinHG * 2500.;
			HashMap<Integer, Integer> ChampsM = this.fields.getChamps().get("M");
			ChampsM.put(CurrentStep, besoinMG);
			this.fields.getChamps().put("M", ChampsM);
		}
		
		return besoinHG + besoinMG;
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void achatHectares(Integer HectaresAAcheter) {
		Integer coutAchatHectares = HectaresAAcheter * 3250;
		this.HectaresAchetes.setValeur(this, HectaresAAcheter);
		this.achatHectarCout.put(Filiere.LA_FILIERE.getEtape(), coutAchatHectares);
		this.CoutStep = this.CoutStep + coutAchatHectares;
	}
	/**
	 * @author BOCQUET Gabriel
	 * @param  s
	 * Return the cost of the buying of a field at step s
	 */
	public Integer getCoutAchatChamp(Integer s) {
		if (s > -1) {
			if(this.achatHectarCout.get(s) != null) {
				return this.achatHectarCout.get(s);
			}
			
		}
		return 0;
	}
	/**
	 * @author Dubus-Chanson Victor
	 */
	/*A faire a chaque step et tous les 6mois avant changeHectaresAndCoutsLies*/
	public void updateHectaresLibres(Integer CurrentStep) {
		Champs Champs = this.getFields();
		Integer HectaresLiberes = Champs.destructionVieuxHectares(CurrentStep);
		this.HectaresLibres += HectaresLiberes;
		this.HectaresUtilises -= HectaresLiberes;
	}
	
	/** 
	 * @param s
	 * @author BOCQUET Gabriel
	 * @return argent gagne grace a la vente des feves de qualite s au précédent step
	 */
	protected double getRecetteCC(String s) {
		if(Filiere.LA_FILIERE.getEtape() > 0) {
		Feve f;
		if(s=="M") {f=Feve.F_MQ_BE;}
		else {f=Feve.F_HQ_BE;}
		
		LinkedList<ExemplaireContratCadre> contracts = this.contractprecedent;
		LinkedList<ExemplaireContratCadre> contractsGoods = new LinkedList<ExemplaireContratCadre>();
		//contractsGoods contient tous les contrats associes aux feves de qualite s
		for(ExemplaireContratCadre c : contracts) {
			if(f==((Feve)c.getProduit())){
				contractsGoods.add(c);
			}
		}
		double argentGagne = 0.0;
		int i =0;
		int step = Filiere.LA_FILIERE.getEtape() -1;
		this.journal_activitegenerale.ajouter("Au step "+ step + "pour " + s  );
		for(ExemplaireContratCadre c : contractsGoods) {
			i+=1;			
			this.journal_activitegenerale.ajouter("Argent gagne = " + c.getPaiementsEffectues().getQuantite(Filiere.LA_FILIERE.getEtape() -1) + " pour i = " + i);
			//Suppose que meme si on a pas assez de feve on renvoie ce que l'on a
			argentGagne +=c.getPaiementsEffectues().getQuantite(Filiere.LA_FILIERE.getEtape() -1);		
		}
		return argentGagne;
		}
		return 0.0;
	}
	
	/** 
	 * @param s
	 * @author BOCQUET Gabriel
	 * @return Benefice gagne suite a la vente des feves de qualite s
	 */
	protected double getBenefice(String s) {
		double coutCurrentStep;
		double recette;
		int step = Filiere.LA_FILIERE.getEtape() - 1;
		if(s.equals("H") || s.equals("M")) {
			Feve f;
			double tailleStock;
			double tailleChamp;
			double proportionChamps;
			if(s=="H") {
				f=Feve.F_HQ_BE;
				tailleStock = this.StockFeveH.getValeur(step);
				tailleChamp = this.getMaxPrec(tailleH);//this.getMaxPrec(this.tailleH);
				this.journal_plantation.ajouter("Nombre employé H: " + tailleChamp);
				proportionChamps = this.tailleH.getValeur(step)/(this.tailleH.getValeur(step) + this.tailleM.getValeur(step));
			}
			else {
				f=Feve.F_MQ_BE;
				tailleStock = this.StockFeveM.getValeur(step);
				tailleChamp =this.getMaxPrec(tailleM);//this.getMaxPrec(this.tailleM);
				this.journal_plantation.ajouter("Nombre employé M: " + tailleChamp);
				proportionChamps = tailleChamp/ (this.tailleH.getValeur(step) + this.tailleM.getValeur(step));
			}
			//CoutStep = CoutStockageFeve + CoutEntretientChamp + CoutAchatDesChamps
			coutCurrentStep = tailleStock*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur(step) + tailleChamp*this.coutEmployeStep.getValeur(step) + this.getCoutAchatChamp(step)*proportionChamps;
			 
			//si on a des Hautes Gammes, this.getQuantiteVenduBourse =0. De plus, on a deja ajoute la quantite vendue en Bourse dans VentesHG ou VentesMG
			recette = this.getQuantiteVenduBourse(s,step)*Filiere.LA_FILIERE.getIndicateur("BourseCacao cours M").getValeur(step) + this.getRecetteCC(s);
		}
		else {
			coutCurrentStep = this.StockFeveB.getValeur(step)*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur(step) ;
			recette = this.getQuantiteVenduBourse(s,step)*Filiere.LA_FILIERE.getIndicateur("BourseCacao cours B").getValeur(step);
		}
		this.journal_activitegenerale.ajouter("Cout au step" + step +" pour " + s + ":" + coutCurrentStep);
		this.journal_activitegenerale.ajouter("Recette pour " + s + "au step " + step + ":" + recette);
		return recette - coutCurrentStep;
	}
	/**
	 * 
	 * @param Variable v
	 * @return vClone.getMax()
	 * @author BOCQUET Gabriel  
	 * Retourne le max de v en ne prenant pas en compte le step actuelle*
	 * 
	 */
	public int getMaxPrec(Variable v) {
		if(Filiere.LA_FILIERE.getEtape() - 1 > -1) {
		Historique h = v.getHistorique();
		int max = 999999999;
		for(int i= 0 ;i < Filiere.LA_FILIERE.getEtape();i++) {
			int Valuei = (int)h.getValeur(i);
			System.out.println("Valuei = " + Valuei);
			if(Valuei < max) {
				max = Valuei;
			}
		}
		return max;
		}
		return 0;
	}
	/**
	 * @author Dubus-Chanson Victor
	 */
	/*Modifie les variables de couts et d'hectares en fonction des resultats de variationBesoinHectares*/
	public void changeHectaresAndCoutsLies(Integer ajoutHectares) {
		this.HectaresUtilises = this.HectaresUtilises + ajoutHectares;
		Integer HectaresAAcheter = ajoutHectares - this.HectaresLibres;
		if (HectaresAAcheter > 0) {
			this.achatHectares(HectaresAAcheter);
		}
		this.HectaresLibres = this.HectaresLibres - ajoutHectares;
		if (this.HectaresLibres < 0) {
			this.HectaresLibres = 0;
		}
	}
	
	//PARTIE CATASTROPHE
	/**
	 * @author BOCQUET Gabriel
	 * @author NAVEROS Marine
	 * @param s
	 */
	public void Fire(String s) {
			Champs fields = this.getFields();
			HashMap<Integer,Integer> FieldsH = fields.getChamps().get("H");
			HashMap<Integer,Integer> FieldsM = fields.getChamps().get("M");
			double hectarMburnt = 0;
			double hectarHburnt = 0;
			double Degat =0;
			String nom_incendie ="";
			String url ="";
			Set<Integer> KeyM = FieldsM.keySet();
			Set<Integer> KeyH = FieldsH.keySet();
			Journal j = this.getJCatastrophe();
			if(s.equals("Big")) {
				url = "./src/abstraction/eq3Producteur3/Gif/Gros_incendie.gif";
				nom_incendie = "Gros Incendie !";
				Degat=quantiteBruleH.getValeur();		
				
			}
			if(s.equals("Med")){
				url = "./src/abstraction/eq3Producteur3/Gif/Incendie_Moyen.gif";
				nom_incendie = "Incendie Moyen !";
				Degat=quantiteBruleM.getValeur();
			}
			if(s.equals("Lit")) {
				url = "./src/abstraction/eq3Producteur3/Gif/Petit_Incendie.gif";
				Degat=quantiteBruleL.getValeur();
				nom_incendie = "Petit Incendie !";
				
			}
			if (this.nbr_popup < 1) {
			this.nbr_popup +=1;
		//	JFrame popup = new JFrame(nom_incendie);		
			//popup.setLocation(300, 100);
			//ImageIcon icon = new ImageIcon(url);
			//JLabel label = new JLabel(icon);
	        //popup.getContentPane().add(label);
	        //popup.pack();
	        //popup.setVisible(true);
	      //  Timer timer = new Timer();
		//	ControlTimeGif monTimerTask = new ControlTimeGif(popup);
			//timer.schedule(monTimerTask, 3000);
			}
			for(Integer key : KeyM) {
				hectarMburnt += FieldsM.get(key)*Degat;
				FieldsM.put(key,(int) (FieldsM.get(key)*(1-Degat)));
			}
			j.ajouter(Color.gray, Color.black, hectarMburnt + " d'hectares de Moyenne Gamme d'arbres ont brulé");
			for(Integer key : KeyH) {
				hectarHburnt = FieldsH.get(key)*Degat;
				FieldsH.put(key,(int) (FieldsH.get(key)*(1-Degat)));
			}
			j.ajouter(Color.yellow, Color.black, hectarHburnt + " d'hectares de Haute Gamme d'arbres ont brulé");
			
			//On pense a mettre a jour les champs
			this.fields.setChampM(FieldsM);
			this.fields.setChampH(FieldsH);
	}

	/**
	 * @author NAVEROS Marine
	 */	
	public void Cyclone() {
		if(this.nbr_popup < 1) {
		this.nbr_popup +=1;
//		JFrame popup = new JFrame("Cyclone !");		
//		popup.setLocation(300, 100);
//		ImageIcon icon = new ImageIcon("./src/abstraction/eq3Producteur3/Gif/Cyclone.gif");
//		JLabel label = new JLabel(icon);
//        popup.getContentPane().add(label);
//        popup.pack();
//        popup.setVisible(true);
//        Timer timer = new Timer();
//		ControlTimeGif monTimerTask = new ControlTimeGif(popup);
//		timer.schedule(monTimerTask, 3000);	
		}
		Champs fields = this.getFields();
		HashMap<Integer,Integer> FieldH = fields.getChamps().get("H");
		HashMap<Integer, Integer> FieldM = fields.getChamps().get("M");
		double hectarDetruitH = 0;
		double hectarDetruitM=0.0;
		Set<Integer> KeysH = FieldH.keySet();
		Set<Integer> KeysM = FieldM.keySet();
		Journal j = this.getJCatastrophe();
		for(Integer key: KeysH) {
			hectarDetruitH += FieldH.get(key)*(0+ Math.random()*(1-this.quantiteDetruiteCyclone.getValeur()));
			FieldH.put(key, (int)(FieldH.get(key)*(0+ Math.random()*(1-this.quantiteDetruiteCyclone.getValeur()))));	
		}
		j.ajouter(Color.yellow, Color.black, hectarDetruitH + "d'hectares de Haute Gamme qui ont été détruits par un cyclone");
		for (Integer key: KeysM) {
			hectarDetruitM += FieldM.get(key)*(0+ Math.random()*(1-this.quantiteDetruiteCyclone.getValeur()));
			FieldM.put(key, (int)(FieldM.get(key)*(0+ Math.random()*(1-this.quantiteDetruiteCyclone.getValeur()))));
		}
		//mise à jour des champs après le cyclone
		this.fields.setChampM(FieldM);
		this.fields.setChampH(FieldH);
		j.ajouter(Color.gray, Color.black, hectarDetruitM+"d'hectares de Moyenne Gamme qui ont été détruits par un cyclone");		
		}
			
		   
	
	/**
	 * @author BOCQUET Gabriel
	 *
	 */
	//Pour modéliser    la grève générale, on va considérer les champs qui ne sont pas récoltés seront une perte de fève

	protected void GreveGeneral() {
		if (nbr_popup < 0) {
			this.nbr_popup +=1;
			JFrame popup = new JFrame("Grêve des Ouvriers !");		
			popup.setLocation(300, 100);
			ImageIcon icon = new ImageIcon("./src/abstraction/eq3Producteur3/Gif/Greve.gif"); //
			JLabel label = new JLabel(icon);
			popup.getContentPane().add(label);
	        popup.pack();
			popup.setVisible(true);
			Timer timer = new Timer();
			ControlTimeGif monTimerTask = new ControlTimeGif(popup);
			timer.schedule(monTimerTask, 3000);
		}
		
		
		//On a autant d'employé que d'hectare Utilise
		Integer nbrgreviste = (int) Math.round(this.getHectaresUt()*this.pourcentageGrevise.getValeur());
		//on calcule le ce qu'on aurait du produire avec ces employees
		Champs fields = this.getFields();
		HashMap<String, LinkedList<Integer>> Keys = fields.HarvestKeys(Filiere.LA_FILIERE.getEtape());
		LinkedList<Integer> quantitePerdues = fields.HarvestQuantityG(Filiere.LA_FILIERE.getEtape(),Keys, nbrgreviste);
		if(quantitePerdues.get(0) > 0) {
		super.getStock().retirerVielleFeve(Feve.F_HQ_BE,quantitePerdues.get(0));
		}
		if(quantitePerdues.get(1) > 0) {
		super.getStock().retirerVielleFeve(Feve.F_MQ_BE,quantitePerdues.get(1));
		}
		Journal j = super.getJCatastrophe();
		j.ajouter(Color.red, Color.black, "Il y a "+ nbrgreviste + " qui font grèves ");
		j.ajouter(Color.gray, Color.black, quantitePerdues.get(1) + " d'hectares de Feves Moyennes Gammes n'ont pas été récolté par les grévistes ");
		j.ajouter(Color.yellow, Color.black, quantitePerdues.get(0) + " d'hectares de Feves Hautes Gammes n'ont pas été récolté par les grévistes ");
		
		
	}
	
	
}


