package abstraction.eq1Producteur1;

import java.util.LinkedList;

//classe écrite par Elouan

public class champ {
	private LinkedList<hectar> hectares;
	
	public LinkedList<hectar> getHectares(){
		return this.hectares;
	}
	public void add(hectar h) {
		this.getHectares().add(h);
	}


}
