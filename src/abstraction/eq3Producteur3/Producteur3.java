package abstraction.eq3Producteur3;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.util.Timer;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;

public class Producteur3 extends Bourse3  {


	
	

	/**
	 * @author Dubus-Chanson Victor, Bocquet Gabriel
	 */

	private HashMap<String,HashMap> Champs;
	private Champs fields;
	private Integer HectaresLibres; /*Repertorie le nombre d'hectares libres que l'on possede*/
	private Integer HectaresUtilises; /*Repertorie le nombre d'hectares que l'on utilise*/
	private LinkedList<Double> ListeCout; /*Les couts des 18 steps precedents, y compris celui-la*/

	private Double CoutTonne; /*Le cout par tonne de cacao, calcule sur 18 step (destruction de la feve apres 9 mois), le meme pour toute gamme*/


	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public Producteur3() {
		super();
		this.fields = new Champs();
		

		this.CoutStep = 0.0;
		this.CoutTonne = 0.;
		this.HectaresLibres= 0;
		this.HectaresUtilises=950000;
		this.ListeCout = new LinkedList<Double>();
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
	}
	
	/**
	 * @author Dubus-Chanson Victor
	 */
	public void initialiser() {
		super.initialiser();

		this.CoutStep += Stock.getQuantite()*50;
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
			changeHectaresAndCoutsLies(variationBesoinHectares(Filiere.LA_FILIERE.getEtape()));
		}

		// We only add the costs to CoutStep if we are not at step one :
		if (Filiere.LA_FILIERE.getEtape() > 0) {
			this.CoutStep += Stock.getQuantite()*50;
			this.addCoutHectaresUtilises();
		}

		this.updateListeCout();
		this.updateCoutTonne();
		// Incendie ?
				double probaIncendie =  Math.random();
				if(probaIncendie < 0.02) {
					this.Fire("Big");
				}
				else if(probaIncendie < 0.05) {
					this.Fire("Med");
				}
				else if(probaIncendie < 0.1) {	
					this.Fire("Lit");
				}
				//Cyclone ou tempete ?
				double probaCyclone =  Math.random();
				if(probaCyclone <0.05) {
					this.Cyclone();
			}
				//Greve ?
				double probaGreve = Math.random();
				if(probaGreve < 0.02) {
					this.GreveGeneral();
				}
		this.getJAchats().ajouter(Color.yellow, Color.BLACK, "Coût du step : " + this.CoutStep);
		this.getJGeneral().ajouter(Color.cyan, Color.BLACK, 
				"Step Actuelle : " + Filiere.LA_FILIERE.getEtape()+", Taille total des Champs utilisés : "+ this.HectaresUtilises+", Taille des champs libres" + this.HectaresLibres + ", Nombre d'employe : Pas encore calculé"+ this.HectaresUtilises);
		
		Filiere.LA_FILIERE.getBanque().virer(this, super.getCryptogramme(), Filiere.LA_FILIERE.getBanque(), CoutStep);
		this.getJOperation().ajouter(Color.cyan, Color.BLACK, "On a paye "+ this.CoutStep + "euros de frais divers");
		this.CoutStep = 0.0;

	}
	/*


	
	/**

	 * @author Dubus-Chanson Victor
	 */
	public void addCoutHectaresUtilises() {
		Integer coutEmployes = this.HectaresUtilises * 220;
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
	public void achatHectares(Integer HectaresAAcheter) {
		Integer coutAchatHectares = HectaresAAcheter * 3250;
		this.CoutStep = this.CoutStep + coutAchatHectares;
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
	 * @param s
	 */
	public void Fire(String s) {
			Champs fields = this.getFields();
			HashMap<Integer,Integer> FieldsH = fields.getChamps().get("H");
			HashMap<Integer,Integer> FieldsM = fields.getChamps().get("M");
			double hectarMburnt = 0;
			double hectarHburnt = 0;
			Set<Integer> KeyM = FieldsM.keySet();
			Set<Integer> KeyH = FieldsH.keySet();
			Journal j = this.getJCatastrophe();
			if(s.equals("Big")) {
				JFrame popup = new JFrame("Gros incendie !");		
				popup.setLocation(300, 100);
				ImageIcon icon = new ImageIcon("C:\\Users\\Gabriel\\AppData\\Roaming\\SPB_Data\\git\\CACAO-2023\\src\\abstraction\\eq3Producteur3\\Gif\\Gros_incendie.gif");
				JLabel label = new JLabel(icon);
		        popup.getContentPane().add(label);
		        popup.pack();
		        popup.setVisible(true);
				for(Integer key : KeyM) {
					hectarMburnt += FieldsM.get(key)*0.5;
					FieldsM.put(key,(int) (FieldsM.get(key)*0.5));
				}
				j.ajouter(Color.gray, Color.black, hectarMburnt + " d'hectares de Moyenne Gamme d'arbres ont brulé");
				for(Integer key : KeyH) {
					hectarHburnt = FieldsH.get(key)*0.5;
					FieldsH.put(key,(int) (FieldsH.get(key)*0.5));
				}
				j.ajouter(Color.yellow, Color.black, hectarHburnt + " d'hectares de Haute Gamme d'arbres ont brulé");
			}
			else if(s.equals("Med")) {
				JFrame popup = new JFrame("Incendie Moyen !");		
				popup.setLocation(300, 100);
				ImageIcon icon = new ImageIcon("C:\\Users\\Gabriel\\AppData\\Roaming\\SPB_Data\\git\\CACAO-2023\\src\\abstraction\\eq3Producteur3\\Gif\\Incendie_Moyen.gif");
				JLabel label = new JLabel(icon);
		        popup.getContentPane().add(label);
		        popup.pack();
		        popup.setVisible(true);
				for(Integer key : KeyM) {
					hectarMburnt += FieldsM.get(key)*0.2;
					FieldsM.put(key,(int) (FieldsM.get(key)*0.8));
				}
				j.ajouter(Color.gray, Color.black, hectarMburnt + " d'hectares de Moyenne Gamme d'arbres ont brulé");
				for(Integer key : KeyH) {
					hectarHburnt = FieldsH.get(key)*0.2;
					FieldsH.put(key,(int) (FieldsH.get(key)*0.8));
				}
				j.ajouter(Color.yellow, Color.black, hectarHburnt + " d'hectares de Haute Gamme d'arbres ont brulé");
			}
			else if(s.equals("Lit")) {
				JFrame popup = new JFrame("Petit Incendie !");		
				popup.setLocation(300, 100);
				ImageIcon icon = new ImageIcon("C:\\Users\\Gabriel\\AppData\\Roaming\\SPB_Data\\git\\CACAO-2023\\src\\abstraction\\eq3Producteur3\\Gif\\Petit_Incendie.gif");
				JLabel label = new JLabel(icon);
		        popup.getContentPane().add(label);
		        popup.pack();
		        popup.setVisible(true);
				for(Integer key : KeyM) {
					hectarMburnt += FieldsM.get(key)*0.1;
					FieldsM.put(key,(int) (FieldsM.get(key)*0.9));
				}
				j.ajouter(Color.gray, Color.black, hectarMburnt + " d'hectares de Moyenne Gamme d'arbres ont brulé");
				for(Integer key : KeyH) {
					hectarHburnt = FieldsH.get(key)*0.1;
					FieldsH.put(key,(int) (FieldsH.get(key)*0.9));
				}
				j.ajouter(Color.yellow, Color.black, hectarHburnt + " d'hectares de Haute Gamme d'arbres ont brulé");
			}
		
	}

	/**
	 * @author NAVEROS Marine
	 */	
	public void Cyclone() {
		JFrame popup = new JFrame("Cyclone !");		
		popup.setLocation(300, 100);
		ImageIcon icon = new ImageIcon("C:\\Users\\Gabriel\\AppData\\Roaming\\SPB_Data\\git\\CACAO-2023\\src\\abstraction\\eq3Producteur3\\Gif\\Cyclone.gif");
		JLabel label = new JLabel(icon);
        popup.getContentPane().add(label);
        popup.pack();
        popup.setVisible(true);
		Champs fields = this.getFields();
		HashMap<Integer,Integer> FieldH = fields.getChamps().get("H");
		HashMap<Integer, Integer> FieldM = fields.getChamps().get("M");
		double hectarDetruitH = 0;
		double hectarDetruitM=0;
		Set<Integer> KeysH = FieldH.keySet();
		Set<Integer> KeysM = FieldM.keySet();
		Journal j = this.getJCatastrophe();
		for(Integer key: KeysH) {
			hectarDetruitH += FieldH.get(key)*(0+ Math.random()*0.7);
			FieldH.put(key, (int)(FieldH.get(key)*(0+ Math.random()*0.7)));	
		}
		j.ajouter(Color.yellow, Color.black, hectarDetruitH + "d'hectares de Haute Gamme qui ont été détruits par un cyclone");
		for (Integer key: KeysM) {
			hectarDetruitM += FieldM.get(key)*(0+ Math.random()*0.7);
			FieldM.put(key, (int)(FieldM.get(key)*(0+ Math.random()*0.7)));
		}
		j.ajouter(Color.gray, Color.black, hectarDetruitM+"d'hectares de Moyenne Gamme qui ont été détruits par un cyclone");		
		}
			
		
	
	/**
	 * @author BOCQUET Gabriel
	 */
	//Pour modéliser la grève générale, on va considérer les champs qui ne sont pas récoltés seront une perte de fève
	protected void GreveGeneral() {
		JFrame popup = new JFrame("Grêve des Ouvriers !");
		
		popup.setLocation(300, 100);
		ImageIcon icon = new ImageIcon("C:\\Users\\Gabriel\\AppData\\Roaming\\SPB_Data\\git\\CACAO-2023\\src\\abstraction\\eq3Producteur3\\Gif\\Greve.gif");
		JLabel label = new JLabel(icon);
        popup.getContentPane().add(label);
        popup.pack();
		popup.setVisible(true);
		//On a autant d'employé que d'hectare Utilise
		Integer nbrgreviste = (int) Math.round(this.getHectaresUt()*0.2);
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


