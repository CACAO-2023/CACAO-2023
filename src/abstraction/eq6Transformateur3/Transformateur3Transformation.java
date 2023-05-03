package abstraction.eq6Transformateur3;

import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;

public class Transformateur3Transformation extends Transformateur3Vente {

	/** écrit par Maxime Bedu*/
	
/** processus de transformation : 
	           unit� de temps de transformation : diff�rent selon les f�ves (in progress) 
	           prends dans le stock et remets dans le stock post-transfo, (OK)
	  pareil diff�rents types de stocks initiaux et finaux en fonction du type de f�ve (OK)
	           implementer IChocolatdemarque ou autre truc de Romu (� voir)
	           Type de produit � r�aliser (dans v1 seulement plaque) (donc useless)
	           Quantit� de f�ves � transformer dans les fonctions  (ok)
	           
	           pour info temps de transfo : A d�terminer, pour faire liste par produit de step avant qu'ils
	           ne soient pr�ts (in progress)
	           
	           Fonction besoin, en utilisant la fonction demande pour voir si il y a un manque ici, et pour 
	           pouvoir informer qu'il faut augmenter les stocks pour r�pondre � la demande
	           -peut aussi permettre de jouer sur "qte", la quantit� de transformation qu'on veut faire
	           � chaque step
	           
	           
	*/
	
	private double MQStep1;
	private double MQBEStep1;
	private double HQBEStep1;
	private double HQBEStep2;
	
	/** écrit par Maxime Bedu*/
	
	public Transformateur3Transformation() {
		super();
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

	/** écrit par Maxime Bedu*/
	
		public void transformationChoco(Feve f, double qte) {
		if (f == Feve.F_BQ) {
			double a=CoutMatPremiere(Feve.F_BQ,qte);
			if (a>Filiere.LA_FILIERE.getBanque().getSolde(this, super.cryptogramme)) {
				while (a>Filiere.LA_FILIERE.getBanque().getSolde(this, super.cryptogramme) && qte>1) {
					qte=qte-1;
					a=CoutMatPremiere(Feve.F_BQ,qte);
				}
			}
			double pourcentageTransfo =((double) this.getPourcentageCacaoBG())/100;
			stockFeveBG.retirer(pourcentageTransfo*qte);
			super.journalTransformation.ajouter("on retire du BG :"+pourcentageTransfo*qte);
			stockChocolatBG.ajouter(Filiere.LA_FILIERE.getEtape(),qte);
			} else {
				if (f == Feve.F_MQ) {
					double a=CoutMatPremiere(Feve.F_MQ,qte);
					if (a>Filiere.LA_FILIERE.getBanque().getSolde(this, super.cryptogramme) && qte>1) {
						while (a>Filiere.LA_FILIERE.getBanque().getSolde(this, super.cryptogramme)) {
							qte=qte-1;
							a=CoutMatPremiere(Feve.F_MQ,qte);
						}
					}
					double pourcentageTransfo = ((double)this.getPourcentageCacaoMG())/100;
					double c=getMQStep1();
					setMQStep1(qte);
					stockFeveMG.retirer(pourcentageTransfo*qte);
					if (c!=0) {
					stockChocolatMG.ajouter(Filiere.LA_FILIERE.getEtape(),c);
					}
					} else {
						if (f ==Feve.F_MQ_BE) {
							double a=CoutMatPremiere(Feve.F_MQ_BE,qte);
							if (a>Filiere.LA_FILIERE.getBanque().getSolde(this, super.cryptogramme) && qte>1) {
								while (a>Filiere.LA_FILIERE.getBanque().getSolde(this, super.cryptogramme) && qte>1) {
									qte=qte-1;
									a=CoutMatPremiere(Feve.F_MQ_BE,qte);
								}
							}
							double pourcentageTransfo = ((double)this.getPourcentageCacaoMGL())/100;
							double c=getMQBEStep1();
							setMQBEStep1(qte);
							stockFeveMGL.retirer(pourcentageTransfo*qte);
							if (c!=0) {
							stockChocolatMGL.ajouter(Filiere.LA_FILIERE.getEtape(), c);
							}
							} else {
								if (f == Feve.F_HQ_BE) {
									double a=CoutMatPremiere(Feve.F_HQ_BE,qte);
									if (a>Filiere.LA_FILIERE.getBanque().getSolde(this, super.cryptogramme) && qte>1) {
										while (a>Filiere.LA_FILIERE.getBanque().getSolde(this, super.cryptogramme)) {
											qte=qte-1;
											a=CoutMatPremiere(Feve.F_HQ_BE,qte);
										}
									}
									double pourcentageTransfo = ((double)this.getPourcentageCacaoHG())/100;
									double c=getHQBEStep1();
									setHQBEStep1(qte);
									double d = getHQBEStep2();
									setHQBEStep2(c);
									stockFeveHGL.retirer(pourcentageTransfo*qte);
									if (d!=0) {
									stockChocolatHGL.ajouter(Filiere.LA_FILIERE.getEtape(), d);
									}
							}
	
}
					}
			}
	}


		/** écrit par Maxime Bedu*/
		
protected double BesoinStep(int Step, Feve f) {
	int Stepi=Filiere.LA_FILIERE.getEtape();
	if (f == Feve.F_BQ) {
		double a=super.demandeTotStep(Stepi,Feve.F_BQ)-stockFeveBG.getQuantiteTotale();
		for (int i=0;i<(Step-Stepi);i++) {
		a=a+super.demandeTotStep(Stepi+i+1,Feve.F_BQ);
		}
		if (a>0) {
			return a;
		} else {
			return 0;
		}
	}
	if (f == Feve.F_MQ) {
		if ((Step-Stepi)>1) {
		double a=super.demandeTotStep(Stepi,Feve.F_MQ)-stockFeveMG.getQuantiteTotale()-getMQStep1();
		for (int i=0;i<(Step-Stepi);i++) {
		a=a+super.demandeTotStep(Stepi+i+1,Feve.F_MQ);
		}
		if (a>0) {
			return a;
		} else {
			return 0;
		}
	} else {
		if ((Step-Stepi)==1) {
			double a=super.demandeTotStep(Stepi,Feve.F_MQ)-getMQStep1();
			if (a>0) {
				return a;
			}else {
				return 0;
			}
			}
		}
			
		}
	if (f == Feve.F_MQ_BE) {
		if ((Step-Stepi)>1) {
		double a=super.demandeTotStep(Stepi,Feve.F_MQ_BE)-stockFeveMGL.getQuantiteTotale()-getMQBEStep1();
		for (int i=0;i<(Step-Stepi);i++) {
		a=a+super.demandeTotStep(Stepi+i+1,Feve.F_MQ_BE);
		}
		if (a>0) {
			return a;
		} else {
			return 0;
		}
	} else {
		if ((Step-Stepi)==1) {
			double a=super.demandeTotStep(Stepi,Feve.F_MQ_BE)-getMQBEStep1();
			if (a>0) {
				return a;
			}else {
				return 0;
			}
			}
		}
			
		}
	if (f == Feve.F_HQ_BE) {
		if ((Step-Stepi)>2) {
		double a=super.demandeTotStep(Stepi,Feve.F_HQ_BE)-stockFeveHGL.getQuantiteTotale()-getHQBEStep1()-getHQBEStep2();
		for (int i=0;i<(Step-Stepi);i++) {
		a=a+super.demandeTotStep(Stepi+i+1,Feve.F_HQ_BE);
		}
		if (a>0) {
			return a;
		} else {
			return 0;
		}
	} else {
		if ((Step-Stepi)==2) {
			double a=super.demandeTotStep(Stepi,Feve.F_HQ_BE)-getHQBEStep1()-getHQBEStep2();
			if (a>0) {
				return a;
			}else {
				return 0;
			}
			} else {
			if ((Step-Stepi)==1) {
				double a=super.demandeTotStep(Stepi,Feve.F_HQ_BE)-getHQBEStep2();
				if (a>0) {
					return a;
				}else {
					return 0;
				}
				}
			}
			
		}
	
} 
	return 100;
}

/** écrit par Maxime Bedu*/

protected double CoutMatPremiere(Feve f, double qte) {
	if (f == Feve.F_BQ) { 
		double pourcentageTransfo = ((double)this.getPourcentageCacaoBG())/100;
		return qte*(1-pourcentageTransfo)*400;
	} else {
		if (f == Feve.F_MQ) { 
			double pourcentageTransfo = ((double)this.getPourcentageCacaoMG())/100;
			return qte*(1-pourcentageTransfo)*400;
	} else {
		if (f == Feve.F_MQ_BE) { 
			double pourcentageTransfo = ((double)this.getPourcentageCacaoMGL())/100;
			return qte*(1-pourcentageTransfo)*400;
	} else {
		if (f == Feve.F_HQ_BE) { 
			double pourcentageTransfo = ((double)this.getPourcentageCacaoHG())/100;
			return qte*(1-pourcentageTransfo)*400;
	}
	}
	}}
	return 0;
}


/**ecrit par Nathan Claeys
 * pour pouvoir rendre les variables qui peuvent aider à la prise de decision
 */
public List<Variable> getIndicateurs() {
		List<Variable> res = super.getIndicateurs();
		return res;}

public void initialiser() {
	super.initialiser();
}

/**ecrit par Maxime Bedu*/

	public void next() {
		super.next();
		if (stockFeveBG.getQuantiteTotale()>0) {
			super.journalTransformation.ajouter("on veut transformer"+stockFeveBG.getQuantiteTotale()+"de Feves BG");
		this.transformationChoco(Feve.F_BQ, stockFeveBG.getQuantiteTotale());
		}
		if (stockFeveMG.getQuantiteTotale()>0) {
			super.journalTransformation.ajouter("on veut transformer"+stockFeveMG.getQuantiteTotale()+"de Feves MG");
		this.transformationChoco(Feve.F_MQ, stockFeveMG.getQuantiteTotale());
		}
		if (stockFeveMGL.getQuantiteTotale()>0) {
			super.journalTransformation.ajouter("on veut transformer"+stockFeveMGL.getQuantiteTotale()+"de Feves MGL");
		this.transformationChoco(Feve.F_MQ_BE, stockFeveMGL.getQuantiteTotale());
		}
		if (stockFeveHGL.getQuantiteTotale()>0) {
			super.journalTransformation.ajouter("on veut transformer"+stockFeveHGL.getQuantiteTotale()+"de Feves HG");
		this.transformationChoco(Feve.F_HQ_BE, stockFeveHGL.getQuantiteTotale());
		}
	
} 
}



