package abstraction.eq1Producteur1;

public class Producteur1Plantation extends Producteur1Acteur {
	private champ champ_moy;
	private champ champ_bas;
	private stockFeve stockFeve;
	
	public champ getChampM() {
		return this.champ_moy;
	}
	
	public champ getChampB() {
		return this.champ_bas;
	}
	
	public stockFeve getStock() {
		return this.stockFeve ;
	}
	
	public void next() {
		//début Elouan
		champ c = this.getChamp();
		int n = c.nbhectare();
		for (int i=0; i<n; i++) {
			hectar h = c.getHectare(i);
			h.setNb_step(h.getNombreSep()+1);
			if (h.getNombreSep()==10) 
				{h.setNb_step(0);
				//rajt une ligne de code pour récolter les feves
				h.setNb_recolte(h.getNombreRecolte()+1);}
			if (h.getNombreRecolte()==96) { //supprime l'hectar quand il produit plus
				c.supphectare(h);
				// supprime l'hectare ou replante direct en fct de la qualité qu'on veut + coût replantation
			}
			}
		//fin Elouan
		
		//debut gab
		
//		stockFeve stockFeve = this.getStock() ;
//		int m = stockFeve.nbFeve() ; // faut changer avec le lot
//		for (int i=0; i<n; i++) {
//			Feve feve = stockFeve.getFeve(i) ; // faut changer avec le lot
//			feve.setNbStepsDepuisRecolte(feve.getNbStepsDepuisRecolte()+1) ;
			
//			if (feve.getNbStepsDepuisRecolte() == 12) {
				// péremption fève au bout de 6mois
				//condition pour basse qualité, si moyenne à changer
//				stockFeve.suppFeve(i) ;
//			}
			
//			if (feve.getSeche()==true && feve.getNbStepsDepuisRecolte()>=1) {
				
//			}
//		}
		
		//fin gab
		
		
		
	}

}
