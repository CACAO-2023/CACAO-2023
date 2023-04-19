package abstraction.eq3Producteur3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Lot;

public class test {

	public static void main(String[] args) {
		LinkedList<Integer> l = new LinkedList<Integer>();
		l.add(20);
		l.add(50);
		l.add(70);
		l.add(200);
		System.out.println(l.toString());
		l.remove(200);
		System.out.println(Integer.valueOf(300));
	}

}
