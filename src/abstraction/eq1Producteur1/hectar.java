package abstraction.eq1Producteur1;

public class hectar {

//Elouan a fait cette classe
	
		private int nb_step; //compte le nb de step entre 2 récoltes
		private int nb_recolte; //utile pour voir la durée de vie
		
		public int getNombreSep() {
			return this.nb_step;
		}
		public int getNombreRecolte() {
			return this.nb_recolte;
		}
		public hectar() {
			this.nb_step = 0;
			this.nb_recolte = 0;
		}
}

