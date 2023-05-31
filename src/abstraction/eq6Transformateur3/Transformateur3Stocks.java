package abstraction.eq6Transformateur3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eqXRomu.produits.Lot;

public class Transformateur3Stocks extends Transformateur3Acteur  {

/**Mouhamed SOW*/
	protected Lot stockFeveBG; // feve bas gamme
    protected Lot stockFeveMG; // feve moyenne gamme
    protected Lot stockFeveMGL;// feve moyenne gamme labelisée
    protected Lot stockFeveHGL;// feve haute gamme labelisée
    protected Lot stockChocolatBG; // Chocolat bas gamme
    protected Lot stockChocolatMG; // Chocolat moyenne gamme
    protected Lot stockChocolatMGL; // Chocolat moyenne gamme labelisée
    protected Lot stockChocolatHGL; // Chocolat haute gamme labélisée
    protected List<ChocolatDeMarque> stockProduit;
    private int dureePeremption = 6; 

    /**Mouhamed SOW*/  

 /**Il faut ecrire la fonction constructeur sans param c'est celle là que Romu appelle *
  * dans le code de la filière il faut donc construire les feves et les lots et ensuite 
  * utiliser les constructeur avec le constructeur déjà ecrit
  */
  /**Mouhamed SOW*/
  public Transformateur3Stocks() {
      this.stockFeveBG = new Lot(Feve.F_BQ);
      this.stockFeveMG = new Lot(Feve.F_MQ);
      this.stockFeveMGL = new Lot(Feve.F_MQ_BE);
      this.stockFeveHGL = new Lot(Feve.F_HQ_BE);
      this.stockChocolatBG = new Lot(new ChocolatDeMarque(Chocolat.C_BQ,"eco+ choco",super.pourcentageCacaoBG,super.pourcentageRSE));
      this.stockChocolatMG = new Lot(new ChocolatDeMarque(Chocolat.C_MQ,"chokchoco",super.pourcentageCacaoMG,super.pourcentageRSE));
      this.stockChocolatMGL = new Lot(new ChocolatDeMarque(Chocolat.C_MQ_BE,"chokchoco bio",super.pourcentageCacaoMGL,super.pourcentageRSE));
      this.stockChocolatHGL = new Lot(new ChocolatDeMarque(Chocolat.C_HQ_BE,"Choc",super.pourcentageCacaoHG,super.pourcentageRSE));
      this.stockProduit = new ArrayList<ChocolatDeMarque>();
  }
  /**Mouhamed SOW*/
  public void ajouterFeve(Lot l) {
	    if (l.getQuantiteTotale() ==0) {;}
	    else {
	    switch(((Feve)l.getProduit()).getGamme()) {
	        case BQ:
	            stockFeveBG.ajouter(l);
	            super.totalStocksFeves.setValeur(this, super.totalStocksFeves.getValeur()+l.getQuantiteTotale());
	            break;
	        case MQ:
	        	if(((Feve) l.getProduit()).isBioEquitable()) {
	        		stockFeveMGL.ajouter(l);
	        		super.totalStocksFeves.setValeur(this, super.totalStocksFeves.getValeur()+l.getQuantiteTotale());

	        	}else {
	        		stockFeveMG.ajouter(l);
	        		super.totalStocksFeves.setValeur(this, super.totalStocksFeves.getValeur()+l.getQuantiteTotale());

	        	}
	        	break;
	        case HQ:
	        	stockFeveHGL.ajouter(l);
	        	super.totalStocksFeves.setValeur(this, super.totalStocksFeves.getValeur()+l.getQuantiteTotale());
        		break ;
	        default:
	            throw new IllegalArgumentException("Type de fève invalide");
	    }
	    }
	    
	}
  /**Mouhamed SOW*/
  /**methode pour savoir ou il faut ajouter la feve*/
  public Lot getLotFeve(IProduit produit) {
	    if (produit instanceof Feve) {
	        Feve feve = (Feve) produit;
	        switch (feve.getGamme()) {
	            case BQ:
	                return stockFeveBG;
	            case MQ:
	                if (feve.isBioEquitable()) {
	                    return stockFeveMGL;
	                } else {
	                    return stockFeveMG;
	                }
	            case HQ:
	                return stockFeveHGL;
	            default:
	                return null;
	        }
	    } else {
	        return null;
	    }
	}
  /**Mouhamed SOW*/
  /**methode pour savoir ou il faut ajouter le chocolat*/
  public Lot getLotChocolat(IProduit produit) {
	  if(produit instanceof ChocolatDeMarque) {
		  ChocolatDeMarque chocolat=(ChocolatDeMarque) produit ;
		  switch(chocolat.getGamme()) {
		  case MQ :
			  return this.stockChocolatBG ;
		  case BQ :
			  if(chocolat.isBioEquitable()) {
				  return this.stockChocolatMGL ;
			  }else {
				  return this.stockChocolatMG ;
			  }
		  case HQ :
			  return this.stockChocolatHGL ;
			  default:
				  return null ;
		  }
	  }else {
		  return null ;
	  }
  }

  /**Mouhamed SOW*/
  public void retirerFeve(Feve feve, double quantite) {
	   if(quantite<=0.0) {;}
	   else {
	  switch(feve.getGamme()) {
        case BQ:
            if(stockFeveBG.getQuantiteTotale()>=quantite) {
        	stockFeveBG.retirer(quantite); 
        	super.totalStocksFeves.setValeur(this, super.totalStocksFeves.getValeur()-quantite);

        	}
            else {stockFeveBG.retirer(stockFeveBG.getQuantiteTotale());
            super.totalStocksFeves.setValeur(this, super.totalStocksFeves.getValeur()-stockFeveBG.getQuantiteTotale());
            super.journalStock.ajouter(" La quantité de feve BG est nulle" );

            }
            break;
        case MQ:
        	if(feve.isBioEquitable()) {
        		if(stockFeveMGL.getQuantiteTotale()>=quantite) {
        		stockFeveMGL.retirer(quantite);
        		super.totalStocksFeves.setValeur(this, super.totalStocksFeves.getValeur()-quantite);

        		}
        		else {stockFeveMGL.retirer(stockFeveMGL.getQuantiteTotale());
        		super.totalStocksFeves.setValeur(this, super.totalStocksFeves.getValeur()-stockFeveMGL.getQuantiteTotale());
        		super.journalStock.ajouter(" La quantité de feve MGL est nulle" );

        		}
        	}else {
        		if(stockFeveMG.getQuantiteTotale()>=quantite) {
            		stockFeveMG.retirer(quantite);
            		super.totalStocksFeves.setValeur(this, super.totalStocksFeves.getValeur()-quantite);

            		}
            		else {stockFeveMG.retirer(stockFeveMG.getQuantiteTotale());
            		super.totalStocksFeves.setValeur(this, super.totalStocksFeves.getValeur()-stockFeveMG.getQuantiteTotale());
            		super.journalStock.ajouter(" La quantité de feve MG est nulle" );

            		}
        	}
        	break;
        case HQ:
        	if(stockFeveHGL.getQuantiteTotale()>=quantite) {
        		stockFeveHGL.retirer(quantite);
        		super.totalStocksFeves.setValeur(this, super.totalStocksFeves.getValeur()-quantite);
        		}
        		else {stockFeveHGL.retirer(stockFeveHGL.getQuantiteTotale());
        		super.totalStocksFeves.setValeur(this, super.totalStocksFeves.getValeur()-stockFeveHGL.getQuantiteTotale());
        		super.journalStock.ajouter(" La quantité de feve HGL est nulle" );
        		}
        	break;
        default:
            throw new IllegalArgumentException("Type de fève invalide");
    }
	}   
	}


/**Mouhamed SOW*/
  public void ajouterChocolat(ChocolatDeMarque choco,Double quantite,int dateProduction) {
	  Lot lot ;
	  if (quantite <= 0) {;}
	  else {
	  switch(choco.getGamme()) {
	  	case BQ :
	  		this.stockChocolatBG.ajouter(dateProduction, quantite); 
	  		super.totalStocksChoco.setValeur(this, super.totalStocksChoco.getValeur()+quantite);
	  		break ;
	  	case MQ :
	  		if(choco.isBioEquitable()) {
	  			this.stockChocolatMGL.ajouter(dateProduction, quantite);
	  			super.totalStocksChoco.setValeur(this, super.totalStocksChoco.getValeur()+quantite);
	  		}else {
	  			this.stockChocolatMG.ajouter(dateProduction, quantite); 
	  			super.totalStocksChoco.setValeur(this, super.totalStocksChoco.getValeur()+quantite);
	  		}
	  		break;
	  	case HQ :
	  		this.stockChocolatHGL.ajouter(dateProduction, quantite); 
	  		super.totalStocksChoco.setValeur(this, super.totalStocksChoco.getValeur()+quantite);
	  		break ;
	  	default :
	  		throw new IllegalArgumentException("Type de Chocolat invalide");
	  }
	  }
  }
 /**Mouhamed SOW*/
  public double retirerChocolat(ChocolatDeMarque chocolat,Double quantite) {
	  double res = 0.0;
	  if(quantite<=0.0) {;}
	  else {
	    switch(chocolat.getGamme()) {
	  	case BQ :
	  		if(stockChocolatBG.getQuantiteTotale()>=quantite) {
	  			stockChocolatBG.retirer(quantite);
	  			super.totalStocksChoco.setValeur(this, super.totalStocksChoco.getValeur()-quantite);
	  			res = quantite;
	  		}
        		else {if(stockChocolatBG.getQuantiteTotale()>0.0){stockChocolatBG.retirer(stockChocolatBG.getQuantiteTotale());
        		super.totalStocksChoco.setValeur(this, super.totalStocksChoco.getValeur()-stockChocolatBG.getQuantiteTotale());
        		res = stockChocolatBG.getQuantiteTotale();}
        		}
        		break ;
        		
	  	case MQ :
	  		if(chocolat.isBioEquitable()) {
	  			if(stockChocolatMGL.getQuantiteTotale()>=quantite) {
		  			stockChocolatMGL.retirer(quantite);
		  			super.totalStocksChoco.setValeur(this, super.totalStocksChoco.getValeur()-quantite);
		  			res = quantite;
	  			}
	        		else {if(stockChocolatMGL.getQuantiteTotale()>0.0){
	        			stockChocolatMGL.retirer(stockChocolatMGL.getQuantiteTotale());
	        
	        		super.totalStocksChoco.setValeur(this, super.totalStocksChoco.getValeur()-stockChocolatMGL.getQuantiteTotale());
	        		res = stockChocolatMGL.getQuantiteTotale();}}
	  		}else {
	  			if(stockChocolatMG.getQuantiteTotale()>=quantite) {
		  			stockChocolatMG.retirer(quantite);
		  			super.totalStocksChoco.setValeur(this, super.totalStocksChoco.getValeur()-quantite);
		  			res = quantite;
	  			}
	        		else {if(stockChocolatMG.getQuantiteTotale()>0.0){
	        			stockChocolatMG.retirer(stockChocolatMG.getQuantiteTotale());
	        		super.totalStocksChoco.setValeur(this, super.totalStocksChoco.getValeur()-stockChocolatMG.getQuantiteTotale());
	        		
	        		res = stockChocolatMG.getQuantiteTotale();}}
	  		}
	  		break;
	  	case HQ :
	  		if(stockChocolatHGL.getQuantiteTotale()>=quantite) {
	  			stockChocolatHGL.retirer(quantite);
	  			super.totalStocksChoco.setValeur(this, super.totalStocksChoco.getValeur()-quantite);
	  			res = quantite;
	  		}
        		else {if(stockChocolatHGL.getQuantiteTotale()>0.0){
        			stockChocolatHGL.retirer(stockChocolatHGL.getQuantiteTotale());
        		super.totalStocksChoco.setValeur(this, super.totalStocksChoco.getValeur()-stockChocolatHGL.getQuantiteTotale());
           		res = stockChocolatHGL.getQuantiteTotale();
        		}}
	  		break ;
	  	default :
	  		//throw new IllegalArgumentException("Type de Chocolat invalide");
	  		res = 0.0;
	  }
	  }
  return res;  
  }
  /**ecrit par Nathan Claeys
   * pour pouvoir rendre les variables qui peuvent aider à la prise de decision
   */
  public List<Variable> getIndicateurs() {
		List<Variable> res = super.getIndicateurs();
		return res;}
  
  
  public boolean estUnDeNosChoco (ChocolatDeMarque chocolat) {
	  boolean res = false;
	  for (ChocolatDeMarque choco : super.chocosProduits) {
		  res = res || choco.equals(chocolat);
	  }
	  return res;
  }
  
  public void initialiser() {
	  super.initialiser();
	}
  	public void next() {
  		super.next();
  		int date = Filiere.LA_FILIERE.getEtape() ;
  		int perim = date - dureePeremption ;
  		Object quantiteRetiree = this.stockFeveBG.getQuantites().get(perim) ;
  		if(quantiteRetiree!=null && quantiteRetiree instanceof Double && ((Double)quantiteRetiree) >0) {
  			super.journalStock.ajouter("on retire les fèves périmées BG"+quantiteRetiree);
  		this.stockFeveBG.retirer(((Double)quantiteRetiree)) ;} // retire la feve perime
  		Object quantiteRetiree2 = this.stockFeveMG.getQuantites().get(perim) ;
  		if(quantiteRetiree2!=null && quantiteRetiree2 instanceof Double &&((Double)quantiteRetiree2)>0) {
  			super.journalStock.ajouter("on retire les fèves périmées MG"+quantiteRetiree2);
  		this.stockFeveMG.retirer(((Double)quantiteRetiree2)) ;}
  		Object quantiteRetiree3 = this.stockFeveMGL.getQuantites().get(perim) ;
  		if(quantiteRetiree3!=null && quantiteRetiree3 instanceof Double &&((Double)quantiteRetiree3)>0) {  		
  			super.journalStock.ajouter("on retire les fèves périmées MGL"+quantiteRetiree3);
  		this.stockFeveMGL.retirer(((Double)quantiteRetiree3)) ;}
  		Object quantiteRetiree4 = this.stockFeveHGL.getQuantites().get(perim) ;
  		if(quantiteRetiree4!=null && quantiteRetiree4 instanceof Double && ((Double)quantiteRetiree4)>0) { 
  			super.journalStock.ajouter("on retire les fèves périmées HGL"+quantiteRetiree4);
  		this.stockFeveHGL.retirer(((Double)quantiteRetiree4)) ;}
  		super.journalStock.ajouter(date+" ");
  		super.journalStock.ajouter(" La quantité de feve BG est :"+ this.stockFeveBG.getQuantiteTotale() );
  		super.journalStock.ajouter(" La quantité de feve MG est :"+ this.stockFeveMG.getQuantiteTotale() );
  		super.journalStock.ajouter(" La quantité de feve MGL est :"+ this.stockFeveMGL.getQuantiteTotale() );
  		super.journalStock.ajouter(" La quantité de feve HGL est :"+ this.stockFeveHGL.getQuantiteTotale() );
  		super.journalStock.ajouter(" La quantité de Chocolat BG est :"+ this.stockChocolatBG.getQuantiteTotale() );
  		super.journalStock.ajouter(" La quantité de Chocolat MG est :"+ this.stockChocolatMG.getQuantiteTotale() );
  		super.journalStock.ajouter(" La quantité de Chocolat MGL est :"+ this.stockChocolatMGL.getQuantiteTotale() );
  		super.journalStock.ajouter(" La quantité de Chocolat HGL est :"+ this.stockChocolatHGL.getQuantiteTotale() );
  		double coutFeve=super.totalStocksFeves.getValeur(date)*4*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur() ;
  		double coutChoco=super.totalStocksFeves.getValeur(date)*4*Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur() ;
  		if(coutFeve>0) {
  		Filiere.LA_FILIERE.getBanque().virer(this, super.cryptogramme, Filiere.LA_FILIERE.getBanque(), coutFeve) ;
  		super.journalStock.ajouter(" on a payé :"+ coutChoco+" pour le cout de stockage des feves" );
  		}
  		if(coutChoco>0) {
  		Filiere.LA_FILIERE.getBanque().virer(this, super.cryptogramme, Filiere.LA_FILIERE.getBanque(), coutChoco) ;
  		super.journalStock.ajouter(" on a payé :"+ coutChoco+" pour le cout de stockage des chocolats" );
  		}
  		
  		
  	}
}
    
    
   

