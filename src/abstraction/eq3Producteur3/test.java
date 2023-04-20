package abstraction.eq3Producteur3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class test {

	public static void main(String[] args) {
		Lot l = new Lot(Feve.F_MQ_BE);
		l.ajouter(0,10);
		l.ajouter(6,20);
		l.ajouter(13,110);
		l.ajouter(20,1000);
		System.out.println(l.toString());
		Stock s= new Stock(Feve.F_MQ_BE,l);
		s.retirerVielleFeve(Feve.F_MQ_BE,1100);
		System.out.println(s.getStock().get(Feve.F_MQ_BE).toString());
		
	}

}
