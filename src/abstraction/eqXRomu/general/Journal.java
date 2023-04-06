package abstraction.eqXRomu.general;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
/**
 * Classe modelisant un journal. 
 * Un journal sert a garder trace de messages qu'on y depose.
 * Cela peut etre particulierement utilse en phase de debogage : 
 * plutot que d'afficher des messages sur la console pour tracer
 * le deroulement de l'execution, mieux vaut ajouter des chaines
 * dans un journal. On peut ainsi avoir la trace de differents
 * acteurs sans que les messages s'entrelacent sur la console.
 * 
 * Vous pourriez etre amenes a creer des instances de cette classe.
 *
 * @author Romuald Debruyne
 */
public class Journal {
	private String nom;            // le nom du journal
	private IActeur createur;
	private ArrayList<String>liste;// la liste des messages deposes sur le journal
	private int lignesAffichables;
	private PropertyChangeSupport pcs; // Pour notifier les observers (l'ecouteur qui actualise la fenetre d'affichage du journal) des changements
	// Ils ne sont pas notifies a chaque modifications mais uniquement en fin de next.
	/**
	 * Initialise le journal avec le nom nom et une liste de messages vide   
	 * @param nom le nom du journal
	 */
	public Journal(String nom, IActeur createur) {
		this.nom = nom;
		this.createur = createur;
		this.liste=new ArrayList<String>();
		this.lignesAffichables=200;
		this.pcs = new  PropertyChangeSupport(this);
	}
	/**
	 * @return Retourne le nom du journal
	 */
	public String getNom() {
		return this.nom;
	}

	public IActeur getCreateur() {
		return this.createur;
	}
	/**
	 * @return Retourne le nombre de messages sur le journal
	 */
	public int getTaille() {
		return this.liste.size();
	}

	public void augmenterLignesAffichables() {
		this.lignesAffichables*=2;
	}

	/**
	 * Ajoute le message s sur le journal
	 * @param s le message a ajouter sur le journal
	 */
	public void ajouter(String s) {
		this.liste.add( "Et"+entierSur6(Filiere.LA_FILIERE==null? 0 : Filiere.LA_FILIERE.getEtape())+" "+s);
	}

	/**
	 * Ajoute le message s sur le journal
	 * @param s le message a ajouter sur le journal
	 */
	public void ajouter(Color background, Color foreground, String s) {
		this.ajouter(texteColore(background, foreground,s));
	}

	public static String texteColore(Color background, Color foreground, String s) {
		return "<span style=\"font-family: Monospace; background-color:rgb("+background.getRed()+","+background.getGreen()+","+background.getBlue()+")\"><font color=\"rgb("+foreground.getRed()+","+foreground.getGreen()+","+foreground.getBlue()+")\">"+s+"<span style=\"font-family: Monospace; background-color:rgb(255,255,255)\"><font color=\"rgb(0,0,0)\">";
	}

	public static String texteColore(IActeur acteur, String s) {
		return texteColore(acteur.getColor(), Color.BLACK, s);
	}
	/**
	 * Si i>=0 et i<this.getTaille(), retourne le message d'index i.
	 * Sinon retourne null.
	 * @param i
	 * @return
	 */
	public String get(int i) {
		if (i>=0 && i<this.getTaille()) {
			return this.liste.get(i);
		} else {
			return null;
		}
	}
	/**
	 * Retourne une chaine de caracteres agregeant les messages 
	 * du journal separes par un retour a la ligne
	 */
	public String toString() {
		String s="";
		for (int i=0; i<this.getTaille(); i++) {
			s+=this.get(i).toString()+"\n";
		}
		return s;
	}
	/**
	 * @return Retourne une chaine de caracteres agregeant les messages
	 * du journal avec des balises html pour le passage a la ligne
	 * entre chaque message
	 * Cette methode est utilisee pour l'affichage
	 */
	public String toHtml() {
		String s="<html>...<br/>";
		for (int i=Math.max(0,this.getTaille()-this.lignesAffichables); i<this.getTaille(); i++) {
			s+=//"<span style=\"font-family: Monospace; background-color:rgb(200,200,255)\"><font color=\"rgb(128,0,255)\">"+sur6(i)+" "
					"<span style=\"font-family: Monospace; background-color:rgb(255,255,255)\"><font color=\"rgb(0,0,0)\">"+entierSur6(i)+" "
					+this.get(i).toString()+"<br/>";
		}
		return s+"</html>";
	}

	public static String sansBalise(String s) {
		StringBuffer sb = new StringBuffer();
		boolean dansBalise=false;
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '<' : dansBalise=true;break;
			case '>' : dansBalise=false;break;
			default : if (!dansBalise) {
				sb.append(c);
			}
			}
		}
		return sb.toString();
	}
	public String toStringSansBalise() {
		StringBuffer sb=new StringBuffer();
		for (int i=0; i<this.getTaille(); i++) {
			sb.append(sansBalise(this.get(i).toString())+"\n");
		}
		return sb.toString();
	}
	public String allToHtml() {
		String s="<html>...<br/>";
		for (int i=0; i<this.getTaille(); i++) {
			s+=//"<span style=\"font-family: Monospace; background-color:rgb(200,200,255)\"><font color=\"rgb(128,0,255)\">"+sur6(i)+" "
					"<span style=\"font-family: Monospace; background-color:rgb(255,255,255)\"><font color=\"rgb(0,0,0)\">"+entierSur6(i)+" "
					+this.get(i).toString()+"<br/>";
		}
		return s+"</html>";
	}

	public static String entierSur6(int i) {
		return ""+(i%1000000)/100000+""+(i%100000)/10000+""+(i%10000)/1000+""+(i%1000)/100+""+(i%100)/10+""+(i%10)+"";
	}

	public static  String texteSurUneLargeurDe(String s, int largeur) {
		if (s.length()>largeur) {
			return s.substring(0,largeur);
		} else {
			int espacesAAjouter = largeur-s.length();
			for (int i=0; i<espacesAAjouter; i++) {
				s="&nbsp;"+s;
			}
			return s;
		}
	}

	public static String doubleSur(double nombre, int caracteresApresLaVirgule) {
		String avantLaVirgule = "";
		String apresLaVirgule = "";
		if (nombre<0.0) {
			avantLaVirgule="-";
			nombre=-nombre;
		}
		long partieEntiere = (long)nombre;
		if (partieEntiere==0) {
			avantLaVirgule=avantLaVirgule+"0";
		} else {		
			while (partieEntiere>999) {
				String sur1000 = ""+(partieEntiere%1000);
				while (sur1000.length()<3) {
					sur1000="0"+sur1000;
				}
				avantLaVirgule="."+sur1000+avantLaVirgule;
				partieEntiere=partieEntiere/1000;
			}
			avantLaVirgule=partieEntiere+avantLaVirgule;
		}
		nombre=nombre-(long)nombre;
		for (int d = 0; d<caracteresApresLaVirgule; d++) {
			apresLaVirgule= apresLaVirgule+(  (int)(nombre*10.0));
			nombre=nombre*10.0;
			nombre=nombre-(int)(nombre);
		}
		return avantLaVirgule+","+apresLaVirgule;

	}

	public static String doubleSur(double nombre, int caracteresAvantLaVirgule, int caracteresApresLaVirgule) {
		String avantLaVirgule = "";
		int longueurAvantLaVirgule=0;
		String apresLaVirgule = "";
		long partieEntiere = (long)nombre;
		if (partieEntiere==0) {
			avantLaVirgule="0";
		} else {
			while (partieEntiere>0) {
				if (partieEntiere<10) { // 1 chiffre
					avantLaVirgule="&nbsp;&nbsp;&nbsp;"+(partieEntiere%10)+avantLaVirgule;
				} else if (partieEntiere<100) { // 2 chiffres
					avantLaVirgule="&nbsp;&nbsp;"+(partieEntiere/10)+""+(partieEntiere%10)+avantLaVirgule;
				} else {
					avantLaVirgule="&nbsp;"+((partieEntiere%1000)/100)+(partieEntiere%100/10)+""+(partieEntiere%10)+avantLaVirgule;
				}
				longueurAvantLaVirgule+=4;
				//				System.out.println(">>>"+avantLaVirgule+"<<<");
				partieEntiere=partieEntiere/1000;
			}
		}
		int caracteresAvantLaVirguleNBSP = caracteresAvantLaVirgule+(5*((caracteresAvantLaVirgule)/4));
		if (avantLaVirgule.length()>caracteresAvantLaVirguleNBSP) {
			avantLaVirgule=avantLaVirgule.substring(avantLaVirgule.length()-caracteresAvantLaVirguleNBSP);
		} else if (longueurAvantLaVirgule<caracteresAvantLaVirgule) {
			int espacesAAjouter= caracteresAvantLaVirgule-longueurAvantLaVirgule;
			//			System.out.println("longueur="+longueurAvantLaVirgule+" --> "+espacesAAjouter+" espaces a ajouter");
			for (int i=0; i<espacesAAjouter; i++) {
				avantLaVirgule="&nbsp;"+avantLaVirgule;
			}
		}
		double decimales = nombre-(long)nombre;
		for (int i = 0; i<caracteresApresLaVirgule; i++) {
			//			System.out.println("i="+i+" -> "+apresLaVirgule+" dec="+decimales);
			if (i!=0 && i%4==3) {
				apresLaVirgule=apresLaVirgule+"&nbsp;";
			} else {
				int chiffre =(int) (decimales*10.0);
				decimales=(decimales*10.0)-chiffre;
				apresLaVirgule=apresLaVirgule+chiffre;
			}
		}

		if (caracteresApresLaVirgule==0) {
			return avantLaVirgule;
		} else {
			return avantLaVirgule+","+apresLaVirgule;
		}
	}

	public void addObserver(PropertyChangeListener obs) {
		pcs.addPropertyChangeListener(obs);
	}

	public void notifyObservers() {
		pcs.firePropertyChange("endOfNext",null,null);
	}
}
