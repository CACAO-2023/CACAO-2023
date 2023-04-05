package abstraction.eq6Transformateur3;

import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur3Transformation extends Transformateur3Stocks {

	/** Maxime Bedu*/
	
/** processus de transformation : 
	           unité de temps de transformation : différent selon les fèves (in progress) 
	           prends dans le stock et remets dans le stock post-transfo, (OK)
	  pareil différents types de stocks initiaux et finaux en fonction du type de fève (OK)
	           implementer IChocolatdemarque ou autre truc de Romu (à voir)
	           Type de produit à réaliser (dans v1 seulement plaque) (donc useless)
	           Quantité de fèves à transformer dans les fonctions  (ok)
	           
	           pour info temps de transfo : A déterminer, pour faire liste par produit de step avant qu'ils
	           ne soient prêts (in progress)
	           
	           Fonction besoin, en utilisant la fonction demande pour voir si il y a un manque ici, et pour 
	           pouvoir informer qu'il faut augmenter les stocks pour répondre à la demande
	           -peut aussi permettre de jouer sur "qte", la quantité de transformation qu'on veut faire
	           à chaque step
	           
	           
	*/
	
	private double MQStep1;
	private double MQBEStep1;
	private double HQBEStep1;
	private double HQBEStep2;
	
	public Transformateur3Transformation() {
		this.MQStep1=0;
		this.MQBEStep1=0;
		this.HQBEStep1=0;
		this.HQBEStep2=0;
	}
	
	public double getMQStep1() {
		return MQStep1;
	}
	
	public void setMQStep1(double a) {
		this.MQStep1 = a;
	}
	
	public double getMQBEStep1() {
		return MQBEStep1;
	}
	
	public void setMQBEStep1(double a) {
		MQBEStep1 = a;
	}
	
	public double getHQBEStep1() {
		return HQBEStep1;
	}
	
	public void setHQBEStep1(double a) {
		HQBEStep1 = a;
	}
	
	public double getHQBEStep2() {
		return HQBEStep2;
	}
	
	public void setHQBEStep2(double a) {
		HQBEStep2 = a;
	}

	
		public void transformationChoco(Feve f, double qte) {
		if (f instanceof F_BQ) {
			double pourcentageTransfo = this.getPourcentageCacaoBG().getValeur();
			double a=stockFeve.get(F_BQ);
			stockFeve.replace(F_BQ, a-(pourcentageTransfo*qte));
			double b=stockChocolat.get(C_BQ);
			stockChocolat.replace(C_BQ,b+qte);
			} else {
				if (f instanceof F_MQ) {
					double pourcentageTransfo = this.getPourcentageCacaoBG().getValeur();
					double a=stockFeve.get(F_MQ);
					stockFeve.replace(F_MQ, a-(pourcentageTransfo*qte));
					double b=stockChocolat.get(C_MQ);
					stockChocolat.replace(C_MQ,b+qte);
					} else {
						if (f instanceof F_MQ_BE) {
							double pourcentageTransfo = this.getPourcentageCacaoBG().getValeur();
							double a=stockFeve.get(F_MQ_BE);
							stockFeve.replace(F_MQ_BE, a-(pourcentageTransfo*qte));
							double b=stockChocolat.get(C_MQ_BE);
							stockChocolat.replace(C_MQ_BE,b+qte);
							} else {
								if (f instanceof F_HQ_BE) {
									double pourcentageTransfo = this.getPourcentageCacaoBG().getValeur();
									double a=stockFeve.get(F_HQ_BE);
									stockFeve.replace(F_HQ_BE, a-(pourcentageTransfo*qte));
									double b=stockChocolat.get(C_HQ_BE);
									stockChocolat.replace(C_HQ_BE,b+qte);
							}
	
}
					}
			}
	}



protected double BesoinStep(int Step, Feve f) {
