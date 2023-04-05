package abstraction.eq1Producteur1;

public class hectar {

//Elouan a fait cette classe
	
		private int nb_step; //compte le nb de step entre 2 récoltes
		private int nb_recolte; //utile pour voir la durée de vie
		private String qualite; //les feves recoltees dans cet hectare seront de cette qualite : B ou M
		
		public int getNombreSep() {
			return this.nb_step;
		}
		public int getNombreRecolte() {
			return this.nb_recolte;
		}
		public String getQualite() {
			return this.qualite;
		}
		public hectar(String qualite) {
			this.nb_step = 0;
			this.nb_recolte = 0;
			this.qualite = qualite;
		}
		public void setNb_step(int n) {
			this.nb_step = n;
		}
		public void setNb_recolte(int n) {
			this.nb_recolte = n;
		}
		
}

