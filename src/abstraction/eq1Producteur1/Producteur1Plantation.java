package abstraction.eq1Producteur1;

public class Producteur1Plantation extends Producteur1Acteur {
	private champ champ;
	
	public champ getChamp() {
		return this.champ;
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
			}
			}
		//fin Elouan
		
	}

}
