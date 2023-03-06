package presentation.secondaire;
import java.awt.*;  
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.Locale;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import abstraction.eqXRomu.general.Courbe;

/**
 * Classe modelisant une fenetre presentant un graphique vu comme la reunion de courbes.
 * 
 * Vous n'aurez pas, a priori, a utiliser directement cette classe
 * 
 * @author Romuald Debruyne
 */
public class FenetreGraphique extends JFrame implements MouseListener, ComponentListener{
	private static final long serialVersionUID = 1l;
	private static final int NB_MARQUES = 240;

	// VARIABLES D'INSTANCES
	/**
	 * les bornes de la partie observable de la fonction.
	 */
	private double minX, maxX, minY, maxY; 
	/**
	 * les bornes de la partie observable fix�es par l'utilisateur.
	 */
	private double minXU, maxXU, minYU, maxYU; 
	/**
	 * le pas separant deux graduations des echelles horizontales et verticales.
	 */
	private double uniteAbs, uniteOrd;
	/**
	 * taille et style de la police d'ecriture
	 */
	private int policeSize, policeStyle; 
	/**
	 * vrais si l'utilisateur desire des traites horizontaux/verticaux pour quadriller le graphique
	 */
	private boolean traitsHorizonaux, traitsVerticaux; 
	/**
	 * vrai si l'utilisateur desire un cadre autour du graphique (faux --> uniquement les axes)
	 */
	private boolean encadre; 
	/**
	 * nombre de chiffres max apres la virgule pour les axes.
	 */
	private int maxFractionDigits;
	/**
	 * les limites gauche, droite, haute et basse de la zone de trace.
	 */
	private int gauche, droite, haute, basse; 
	/**
	 * la largeur et la hauteur de la fenetre.
	 */
	private int largeur, hauteur; 
	/**
	 * titre de la fenetre
	 */
	private String titre; 
	/**
	 * largeur en pixels des traits dans la legende
	 */
	private int largeurTraitLegende;
	/**
	 * La liste des courbes composant le graphique
	 */
	private ArrayList<Courbe> lesCourbes;
	/**
	 * image sur laquelle seront tracees les courbes (l'image est ensuite affichee)
	 */
	private Image image;
	/**
	 * Les images correspondant aux marques reperant les differentes courbes
	 */
	private BufferedImage[] marques;

	private Graphics gc;



	/**
	 * Constructeur initialisant le graphique avec t pour titre, une largeur de l pixels et une hauteur de h pixels. Par defaut, les unites seront choisies par le programme, le graphique est encadre, il y a des traits horizontaux et verticaux realisant un quadrillage, les nombres sur les axes n'ont que deux chiffres apres la virgule, la taille de la police est de 12, la largeur des traits de legende est de 25 et le graphique ne comporte aucune courbe. 
	 * @param t String, titre du graphique 
	 * @param l int, largeur en pixels du graphique
	 * @param h int, hauteur en pixels du graphique 
	 */
	public FenetreGraphique(String t, int l, int h) {
		this.titre = t;
		this.largeur = l;
		this.hauteur = h;
		this.minXU=Double.MIN_VALUE;
		this.maxXU=Double.MAX_VALUE;
		this.minYU=Double.MIN_VALUE;
		this.maxYU=Double.MAX_VALUE;
		this.uniteAbs=Double.MAX_VALUE;
		this.uniteOrd=Double.MAX_VALUE;
		this.traitsHorizonaux=true;
		this.traitsVerticaux=true;
		this.encadre=true; //false;
		this.maxFractionDigits=2;
		this.policeSize=12;
		this.setNormale();
		this.largeurTraitLegende=25;
		this.image=null;
		this.marques = new BufferedImage[NB_MARQUES];
		this.lesCourbes = new ArrayList<Courbe>();
		this.setTitle(this.titre);
		this.setSize(largeur, hauteur);
		this.addMouseListener(this);
	/*	this.addWindowListener(new WindowAdapter() {public void windowClosing(WindowEvent e) {
			Graphique gg = (Graphique)(e.getSource());
			gg.setVisible(false);

			if (gg.checkBox!=null) {gg.checkBox.setSelected(false);} 
		}});
		*/
		this.addComponentListener(this);
	}
/*	public void setCheckBox(JCheckBox checkBox) {
		this.checkBox = checkBox;
	}
	*/
	/**
	 * Accesseur permettant de modifier la largeur des traits de legende
	 * @param l int, largeur en pixels des traits de legende 
	 */
	public void setLargeurTraitLegende(int l) {
		this.largeurTraitLegende=l;
	}
	/**
	 * Accesseur permettant de preciser si la zone de tracage sera ou non encadree
	 * @param b boolean, true si la zone de tracage doit etre encadree, false sinon. 
	 */
	public void setEncadre(boolean b) {
		this.encadre = b;
	}
	/**
	 * Accesseur permettant de preciser si des traits horizontaux doivent quadriller la zone de tracage 
	 * @param b boolean, true si des traits horizontaux doivent quadriller la zone de tracage, false sinon. 
	 */
	public void setTraitsHorizontaux(boolean b) {
		this.traitsHorizonaux=b;
	}
	/**
	 * Accesseur permettant de preciser si des traits verticaux doivent quadriller la zone de tracage 
	 * @param b boolean, true si des traits verticaux doivent quadriller la zone de tracage, false sinon. 
	 */
	public void setTraitsVerticaux(boolean b) {
		this.traitsVerticaux=b;
	}
	/**
	 * Accesseur permettant de preciser le pas devant separer deux graduations sur l'echelle horizontale 
	 * @param u double, le pas devant separer deux graduations sur l'echelle horizontale. 
	 */
	public void setUniteAbs(double u) {
		this.uniteAbs=u;
	}
	/**
	 * Accesseur permettant de preciser le pas devant separer deux graduations sur l'echelle verticale 
	 * @param u double, le pas devant separer deux graduations sur l'echelle verticale. 
	 */
	public void setUniteOrd(double u) {
		this.uniteOrd=u;
	}

	/**
	 * Permet de preciser que l'ecriture sur le graphique doit etre en gras. 
	 * 
	 */
	public void setGras() {
		this.policeStyle=Font.BOLD;
	}
	/**
	 * Permet de preciser que l'ecriture sur le graphique doit etre en italique. 
	 * 
	 */
	public void setItalique() {
		this.policeStyle=Font.ITALIC;
	}
	/**
	 * Permet de preciser que l'ecriture sur le graphique doit etre normale (ni gras, ni italique). 
	 * 
	 */
	public void setNormale() {
		this.policeStyle=Font.PLAIN;
	}
	/**
	 * Permet de preciser la taille de l'ecriture sur le graphique 
	 * @param size int, la taille de la police de caracteres a utiliser.
	 */

	public void setPoliceSize(int size) {
		this.policeSize=size;
	}
	/**
	 * Permet de preciser l'abscisse minimale visible (par defaut, cette abscisse correspond a l'abscisse minimale sur l'ensemble des courbes du graphique) 
	 * @param minX double, l'abscisse minimale visible sur le graphique. 
	 */
	public void setMinX(double minX) {
		this.minXU=minX;
	}
	/**
	 * Permet de preciser l'abscisse maximale visible (par defaut, cette abscisse correspond a l'abscisse maximale sur l'ensemble des courbes du graphique) 
	 * @param maxX double, l'abscisse maximale visible sur le graphique. 
	 */
	public void setMaxX(double maxX) {
		this.maxXU=maxX;
	}
	/**
	 * Permet de preciser l'ordonnee minimale visible (par defaut, cette ordonnee correspond a l'ordonnee minimale sur l'ensemble des courbes du graphique) 
	 * @param minY double, l'ordonnee minimale visible sur le graphique. 
	 */
	public void setMinY(double minY) {
		this.minYU=minY;
	}
	/**
	 * Permet de preciser l'ordonnee maximale visible (par defaut, cette ordonnee correspond a l'ordonnee maximale sur l'ensemble des courbes du graphique) 
	 * @param maxY double, l'ordonnee maximale visible sur le graphique. 
	 */
	public void setMaxY(double maxY) {
		this.maxYU=maxY;
	}
	/** 
	 * Permet d'ajouter une courbe au graphique
	 * @param c Courbe, la courbe a ajouter
	 */
	public void ajouter(Courbe c) {
		this.lesCourbes.add(c);
	}
	/**
	 * Retourne le titre du graphique
	 * @return String, le titre du graphique
	 */
	public String getTitre() {
		return this.titre;
	}
	/**
	 * Retourne le nombre de courbes.
	 * @return int, le nombre de courbes que comporte le graphique
	 */
	public int nbCourbes() {
		return this.lesCourbes.size();
	}
	/**
	 * Retourne la courbe d'index index (null si index n'est pas un index valide)
	 * @param index int, l'index de la courbe a retourner
	 * @return la courbe d'index index (null si index n'est pas valide)
	 */
	public Courbe getCourbe(int index) {
		if (index>=0 && index<this.nbCourbes()) {
			return this.lesCourbes.get(index);
		}
		else {
			return null;
		}
	}
	/**
	 * Affiche le graphique
	 */
	public void montrer() {
		this.setVisible(true);
	}
	/**
	 * Cache le graphique
	 */
	public void cacher() {
		this.setVisible(false);
	}
	/**
	 * Permet (via une regle de proportionnalite) de convertir une abscisse utilisateur en une abscisse a l'ecran
	 * @param x double, un double de [minX, maxX]
	 * @return l'abscisse a l'ecran correspondant a l'abcisse x.
	 */
	private int abscisseEcran( double x ) {
		int largeur = this.droite - this.gauche;
		return this.gauche+(int)(((x-this.minX)*largeur)/(this.maxX-this.minX));
	}

	/**
	 * Permet (via une regle de proportionnalite) de convertir une ordonnee utilisateur en une ordonnee a l'ecran
	 * @param y double, un double de [minY, maxY]
	 * @return l'ordonnee a l'ecran correspondant a l'ordonnee y.
	 */
	private int ordonneeEcran( double y ) {
		int hauteur = this.basse-this.haute;
		return (int)( this.basse-((y-this.minY)*hauteur)/(this.maxY-this.minY));
	}

	/**
	 * Methode tracant le graphisme sur l'instance de Graphics passee en parametre
	 * (Cette methode est appelee automatiquement lorsque le contenu de la fenetre doit etre redessine).
	 */ 
	public void paint(Graphics g) {
		// Pour toutes les courbes qui n'ont pas de couleur definie, on attribue une couleur en divisant le spectre.
		//System.out.println("paint ");
		if (this.getCourbe(0)!=null) {
			if (this.image==null) {
				this.image = createImage ( 2048, 1200);//this.largeur, this.hauteur);
				this.gc = this.image.getGraphics ();
			}
			if (this.marques[0]==null) {
				Image toutesLesMarques= Toolkit.getDefaultToolkit().getImage("marques.png");
				BufferedImage bi;
				if(toutesLesMarques instanceof BufferedImage) { // si c'est deja une BufferedImage il n'y a rien a faire
					bi=(BufferedImage)toutesLesMarques;
				}
				else {
					toutesLesMarques = new ImageIcon(toutesLesMarques).getImage();
					BufferedImage bufferedImage = new BufferedImage(toutesLesMarques.getWidth(null),toutesLesMarques.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR );//.TYPE_INT_RGB );
					Graphics gx = bufferedImage.createGraphics();
					gx.drawImage(toutesLesMarques,0,0,null);
					gx.dispose();
					bi =bufferedImage;
				}

				for (int i=0; i<NB_MARQUES; i++) {
					this.marques[i]=new BufferedImage(9,9, BufferedImage.TYPE_4BYTE_ABGR );
					for (int x=0; x<9; x++) {
						for (int y=0; y<9; y++) {
							this.marques[i].setRGB(x,y,bi.getRGB(x+1+(10*i), y+1));
						}
					}
				}
			}
			// on efface l'image
			gc.setColor(Color.WHITE);
			gc.fillRect(0,0,this.largeur, this.hauteur);
			Font f = new Font( "Arial",this.policeStyle,this.policeSize);
			gc.setFont(f); 
			Color[] couleurs = new Color[ this.nbCourbes()];
			for (int i=0; i<this.nbCourbes(); i++) {
				couleurs[i]=this.getCourbe(i).getCouleur();
				if (couleurs[i]==null) {
					couleurs[i]= Color.getHSBColor(0.75f*(float)((float)i/this.nbCourbes()), .9f, .9f);
				}
			}

			// Determination des bornes.
			// si l'utilisateur n'en a pas precisees
			if (this.minXU==Double.MIN_VALUE) {
				this.minX = this.getCourbe(0).getX(0);
			}
			else {
				this.minX=this.minXU;
			}
			if (this.maxXU==Double.MAX_VALUE) {
				this.maxX = this.minX;
			}
			else {
				this.maxX=this.maxXU;
			}
			if (this.minYU==Double.MIN_VALUE) {
				this.minY = this.getCourbe(0).getY(0);
			}
			else {
				this.minY=this.minYU;
			}
			if (this.maxYU==Double.MAX_VALUE) {
				this.maxY = this.minY;
			}
			else {
				this.maxY=this.maxYU;
			}
			Courbe c;
			for (int i=0; i<this.nbCourbes(); i++) {
				c = this.getCourbe(i);
				for (int j=0; j<c.getNbPoints(); j++) {
					if ((c.getX(j)<minX)&&(this.minX!=this.minXU)) {
						minX=c.getX(j);
					}
					if ((c.getX(j)>maxX)&&(this.maxX!=this.maxXU)) {
						maxX=c.getX(j);
					}
					if ((c.getY(j)-c.getErr(j)<this.minY)&&(this.minY!=this.minYU)) {
						minY=c.getY(j)-c.getErr(j);
					}
					if ((c.getY(j)+c.getErr(j)>this.maxY)&&(this.maxY!=this.maxYU)) {
						maxY=c.getY(j)+c.getErr(j);
					}
				}

			}
			// determination largeur etiquettes axe ord
			NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
			nf.setMaximumFractionDigits(this.maxFractionDigits);

			int largeurMax=(int)(gc.getFontMetrics().getStringBounds(nf.format(this.minY), gc).getBounds().getWidth());
			double pasOrd=this.uniteOrd;
			if (pasOrd==Double.MAX_VALUE) {
				pasOrd=((double)(this.maxY-this.minY))/10;
			}
			

			double y=(int)(this.minY+pasOrd);
			while (pasOrd>0 && y<this.maxY) {
				if ((int)(gc.getFontMetrics().getStringBounds(nf.format(y), gc).getBounds().getWidth())>largeurMax) {
					largeurMax=(int)(gc.getFontMetrics().getStringBounds(nf.format(y), gc).getBounds().getWidth());
				}
				y+=pasOrd;
			}

			this.gauche=10+largeurMax;//50;
			this.haute=50;
			this.droite = this.largeur-10-(int)(gc.getFontMetrics().getStringBounds(nf.format(this.maxX), gc).getBounds().getWidth()/2);

			// Legende
			int maxTitre=0;
			for (int i=0; i<this.nbCourbes(); i++) {
				if ((int)(gc.getFontMetrics().getStringBounds(this.getCourbe(i).getTitre(), gc).getBounds().getWidth())>maxTitre) {
					maxTitre = (int)(gc.getFontMetrics().getStringBounds(this.getCourbe(i).getTitre(), gc).getBounds().getWidth());
				}
			}
			int nbTitresParligne = Math.max(1, (this.droite-this.gauche)/(this.largeurTraitLegende+6+maxTitre));
			int largeurTitre = (this.droite-this.gauche)/nbTitresParligne;
			int nbLignes = this.nbCourbes()/nbTitresParligne;
			if (nbLignes*nbTitresParligne != this.nbCourbes()) {
				nbLignes++;
			}
			int hauteurLigne =(int) gc.getFontMetrics().getStringBounds("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_,;/%", gc).getBounds().getHeight();

			this.basse = this.hauteur -10-(int)((nbLignes+1)*hauteurLigne);

			// on trace les courbes
			for (int i=0; i<this.nbCourbes(); i++) {
				gc.setColor(couleurs[i]);
				c = this.getCourbe(i);
				for (int j=0; j<c.getNbPoints()-1; j++) {
					dessineTrapeze(this.image, gc, couleurs[i], c.getTransparence(), 
							abscisseEcran( c.getX(j)), 
							ordonneeEcran( c.getY(j)+c.getErr(j)),
							ordonneeEcran( c.getY(j)-c.getErr(j)), 
							abscisseEcran( c.getX(j+1)), 
							ordonneeEcran( c.getY(j+1)+c.getErr(j+1)),
							ordonneeEcran( c.getY(j+1)-c.getErr(j+1)));
					gc.setColor(couleurs[i]);
					gc.drawLine( abscisseEcran( c.getX(j)), ordonneeEcran( c.getY(j)),
							abscisseEcran( c.getX(j+1)), ordonneeEcran( c.getY(j+1)));
					if (c.getMarque()!=Courbe.AUCUNE) {
						gc.drawImage(marques[c.getMarque()%NB_MARQUES],abscisseEcran( c.getX(j))-4, ordonneeEcran( c.getY(j))-4, null);
					}
				}
			}

			////////////////////
			gc.setColor(Color.WHITE);
			gc.fillRect(0,0,this.largeur, this.haute);
			gc.fillRect(0,this.basse,this.largeur, this.hauteur-this.basse);
			gc.fillRect(0,0, this.gauche, this.hauteur);
			gc.fillRect(this.droite,0, this.largeur-this.droite, this.hauteur);
			////////////////////
			int courb=0;
			for (int lig=0; lig<nbLignes; lig++) {
				for (int col=0; col<nbTitresParligne; col++) {
					if (courb<this.nbCourbes()) {
						dessineTrapeze(this.image, gc, couleurs[courb], this.getCourbe(courb).getTransparence(), 2+this.gauche+(col*largeurTitre), this.basse+hauteurLigne+2+(lig*hauteurLigne)+(hauteurLigne/2)-3, this.basse+hauteurLigne+2+(lig*hauteurLigne)+(hauteurLigne/2)+3, 2+this.gauche+(col*largeurTitre)+this.largeurTraitLegende+1, this.basse+hauteurLigne+2+(lig*hauteurLigne)+(hauteurLigne/2)-3, this.basse+hauteurLigne+2+(lig*hauteurLigne)+(hauteurLigne/2)+3);
						gc.setColor(couleurs[courb]);
						gc.drawLine(2+this.gauche+(col*largeurTitre), this.basse+hauteurLigne+2+(lig*hauteurLigne)+(hauteurLigne/2),2+this.gauche+(col*largeurTitre)+this.largeurTraitLegende,this.basse+hauteurLigne+2+(lig*hauteurLigne)+(hauteurLigne/2));
						if (this.getCourbe(courb).getMarque()!=Courbe.AUCUNE) {
							gc.drawImage(marques[this.getCourbe(courb).getMarque()%NB_MARQUES],2+this.gauche+(col*largeurTitre)+(+this.largeurTraitLegende/2)-4,this.basse+hauteurLigne+2+(lig*hauteurLigne)+(hauteurLigne/2)-4, null);
						}
						gc.setColor(Color.BLACK);
						gc.drawString(this.getCourbe(courb).getTitre(), 2+this.gauche+(col*largeurTitre)+this.largeurTraitLegende+2, (int)(this.basse+hauteurLigne+((lig+1)*hauteurLigne)));
						courb++;
					}
				}
			}

			int minAxeAbs=abscisseEcran(this.minX);
			int maxAxeAbs=abscisseEcran(this.maxX);
			int minAxeOrd=ordonneeEcran(this.minY);
			int maxAxeOrd=ordonneeEcran(this.maxY);

			// Trace du contour ou des axes
			g.setColor(Color.BLACK);
			if (this.encadre) {
				gc.drawRect(minAxeAbs,maxAxeOrd,maxAxeAbs-minAxeAbs,minAxeOrd-maxAxeOrd);
			} else {
				gc.drawLine(minAxeAbs,maxAxeOrd, minAxeAbs, minAxeOrd);
				gc.drawLine(minAxeAbs, minAxeOrd, maxAxeAbs, minAxeOrd);
			}
			//			Axe des ordonnees

			gc.drawString(nf.format(this.maxY),minAxeAbs-3-(int)(gc.getFontMetrics().getStringBounds(nf.format(this.maxY), gc).getBounds().getWidth()), maxAxeOrd+(int)(g.getFontMetrics().getStringBounds(nf.format(this.maxY), g).getBounds().getHeight()/2));
			y=(this.minY);
			int min=minAxeOrd+(int)(gc.getFontMetrics().getStringBounds(nf.format(y), gc).getBounds().getHeight()/2);
			while (pasOrd>0 && y<this.maxY) {
				if (this.traitsHorizonaux && y!=this.minY) {
					gc.setColor(Color.LIGHT_GRAY);
					gc.drawLine(minAxeAbs, ordonneeEcran(y), maxAxeAbs, ordonneeEcran(y));
					gc.setColor(Color.BLACK); 
				}

				gc.drawLine(minAxeAbs-2, ordonneeEcran(y), minAxeAbs+2, ordonneeEcran(y));
				if ((min>=ordonneeEcran(y)+(int)(gc.getFontMetrics().getStringBounds(nf.format(y), gc).getBounds().getHeight()/2) ) && (ordonneeEcran(y)-(int)(gc.getFontMetrics().getStringBounds(nf.format(y), gc).getBounds().getHeight()/2)>=maxAxeOrd+(int)(gc.getFontMetrics().getStringBounds(nf.format(this.maxY), gc).getBounds().getHeight()/2))) {
					gc.drawString(nf.format(y),minAxeAbs-3-(int)(gc.getFontMetrics().getStringBounds(nf.format(y), gc).getBounds().getWidth()), ordonneeEcran(y)+(int)(gc.getFontMetrics().getStringBounds(nf.format(y), gc).getBounds().getHeight()/2));
					gc.drawLine(minAxeAbs-4, ordonneeEcran(y), minAxeAbs+4, ordonneeEcran(y));
					min=ordonneeEcran(y)-(int)(gc.getFontMetrics().getStringBounds(nf.format(y), gc).getBounds().getHeight()/2);
				}

				y+=pasOrd;

			}
			gc.drawLine(minAxeAbs-2, maxAxeOrd, minAxeAbs+2, maxAxeOrd);

			// Axe des abscisses
			double pasAbs=this.uniteAbs;
			if (pasAbs==Double.MAX_VALUE) {
				pasAbs=(this.maxX-this.minX)/10;
			}
			gc.drawString(nf.format(this.minX), minAxeAbs-(int)(gc.getFontMetrics().getStringBounds(nf.format(this.minX), gc).getBounds().getHeight()/2), (int)(minAxeOrd+gc.getFontMetrics().getStringBounds(nf.format(this.minX), gc).getBounds().getHeight()));
			gc.drawString(nf.format(this.maxX),(int)(maxAxeAbs-(gc.getFontMetrics().getStringBounds(nf.format(this.maxX), gc).getBounds().getWidth()/2)), (int)(this.basse+gc.getFontMetrics().getStringBounds(nf.format(this.minX), gc).getBounds().getHeight()));
			minAxeAbs+=gc.getFontMetrics().getStringBounds(nf.format(this.minX), gc).getBounds().getWidth();

			double x=this.minX;
			while (pasAbs>0 && x<this.maxX) {
				if (this.traitsVerticaux && x!=this.minX) {
					gc.setColor(Color.LIGHT_GRAY);
					gc.drawLine(abscisseEcran(x), maxAxeOrd, abscisseEcran(x), minAxeOrd);
					gc.setColor(Color.BLACK); 
				}
				gc.drawLine(abscisseEcran(x),minAxeOrd-2,abscisseEcran(x), minAxeOrd+2);
				if (((abscisseEcran(x)-(int)(gc.getFontMetrics().getStringBounds(nf.format(x), gc).getBounds().getWidth()/2))>=minAxeAbs) 
						&& (abscisseEcran(x)+(int)(gc.getFontMetrics().getStringBounds(nf.format(x), gc).getBounds().getWidth()/2)<=(maxAxeAbs-(int)(gc.getFontMetrics().getStringBounds(nf.format(this.maxX), gc).getBounds().getWidth())))){
					gc.drawString(nf.format(x), abscisseEcran(x)-(int)(gc.getFontMetrics().getStringBounds(nf.format(x), gc).getBounds().getWidth()/2), (int)(minAxeOrd+gc.getFontMetrics().getStringBounds(""+x, gc).getBounds().getHeight()));
					gc.drawLine(abscisseEcran(x),minAxeOrd-4,abscisseEcran(x), minAxeOrd+4);
					minAxeAbs=abscisseEcran(x)+(int)(gc.getFontMetrics().getStringBounds(nf.format(x), gc).getBounds().getWidth()/2);
				}
				x+=pasAbs;
			}
			gc.drawLine(maxAxeAbs,minAxeOrd-2,maxAxeAbs, minAxeOrd+2);
			g.drawImage(image, 0, 0, null);
		}

	}
	private void dessineLigneVert(Image img, Graphics g, Color c, double trans, int x, int y0, int y1) {
		Color pix;
		int red = c.getRed();
		int green = c.getGreen();
		int blue = c.getBlue();

		for (int y=y0; y<y1; y++) {
			if ((x>=0) && (x<this.largeur) && (y>=0) && (y<this.hauteur)) {

				pix = new Color(((BufferedImage)img).getRGB(x,y));
				red =(int) ((pix.getRed()*(1-trans)) + (c.getRed()*trans));		
				green =(int) ((pix.getGreen()*(1-trans)) + (c.getGreen()*trans));		
				blue =(int) ((pix.getBlue()*(1-trans)) + (c.getBlue()*trans));
				g.setColor(new Color(red, green, blue));
				g.drawLine(x,y,x,y);

			}
		}
	}
	private void dessineTrapeze(Image img, Graphics g, Color c, double trans, int x0, int y00, int y01, int x1, int y10, int y11) {
		int y1, y2;
		for (int x=x0; x<x1; x++) {
			y1 =(int)( y00+(((double)y10-y00)/((double)x1-x0))*(x-x0));
			y2 =(int)( y01+(((double)y11-y01)/((double)x1-x0))*(x-x0));
			dessineLigneVert(img, g, c, trans, x, y1, y2);
		}

	}
	/**
	 * Permet d'enregistrer une image au format PNG du graphique
	 * @param nom String, le nom du fichier
	 */
	public void enregistrer(String nom) {
		Image im = this.image;
		BufferedImage bi;
		if(im instanceof BufferedImage) { // si c'est deja une BufferedImage il n'y a rien a faire
			bi=(BufferedImage)im;
		}
		else {
			im = new ImageIcon(im).getImage();
			BufferedImage bufferedImage = new BufferedImage(im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR );//.TYPE_INT_RGB );
			Graphics g = bufferedImage.createGraphics();
			g.drawImage(im,0,0,null);
			g.dispose();
			bi =bufferedImage;
		}
		try { ImageIO.write(bi, "png", new File(nom));

		} catch (Exception e) {System.out.println(e);}

	}
	public void 	mouseClicked(MouseEvent e) {}
	public void 	mouseEntered(MouseEvent e) {}
	public void 	mouseExited(MouseEvent e) {}

	public void 	mousePressed(MouseEvent e) {
		File f = new File("");
		JFileChooser select=new JFileChooser( f.getAbsolutePath() );

		select.setDialogTitle("Specifiez le nom du fichier PNG a creer");
		select.setFileFilter(new FichiersPNG());

		int reponse=select.showSaveDialog(this);
		if (reponse==JFileChooser.APPROVE_OPTION) {
			File fichier =select.getSelectedFile();
			if (!FichiersPNG.accepts(fichier)) {
				fichier=new File(fichier.getAbsolutePath()+".png");
			}
			this.enregistrer(fichier.getAbsolutePath());
		}
	}
	public void 	mouseReleased(MouseEvent e) {} 

	public static void main(String[] parametresLigneCommande) {

		// On cree un graphique nomme "Essai" de 800 pixels de large par 600 de haut
		FenetreGraphique g=new FenetreGraphique("Essai",800,600);
		Courbe c;
		// Le graphique comporte 20 courbes de 21 points chacune
		// La courbe d'index i porte le nom "courbe"+i et a la marque d'index i
		for (int i=0; i<20; i++) {
			c=new Courbe("courbe"+i, i); 
			for (int j=0; j<=20; j++) { 
				c.ajouter(j, (i+i*j-j*j)*Math.cos((i+j)/2.5), ((10-i)/(j+1)));
			}
			g.ajouter(c);
		}
		// Une autre courbe "sinus" de 201 points
		c=new Courbe("sinus");
		for (int j=0; j<=200; j++) {
			c.ajouter(((j*10.0)/100), (300-j)*Math.sin(j*Math.PI/20), j/5);
		}
		g.ajouter(c);
		// On affiche le graphique
		g.montrer();

		// On cree un graphique nomme "Testons" de 640 pixels de large par 480 de haut
		// Le graphique comporte 2 courbes: "une belle courbe" et "puissance 1.4"
		// La seconde courbe possede une marge d'erreur sur l'erreur
		FenetreGraphique g2=new FenetreGraphique("Testons", 640, 480);
		c=new Courbe("un belle courbe",226); 
		for (int x=0; x<=50; x++) {
			c.ajouter(4*x, 3*x*Math.sin(x/4.0));
		}
		g2.ajouter(c);
		c=new Courbe("puissance 1.4",140);
		for (int x=0; x<=50; x++) {
			c.ajouter(4*x, Math.pow(x,1.4), Math.abs(Math.random()*x));
		}
		g2.ajouter(c);

		// on limite la portion visible a [-50, 300] pour les abcisses et a (0, 200] pour les ordonnees
		g2.setMinX(-50);
		g2.setMaxX(300);
		g2.setMinY(0);
		g2.setMaxY(200);
		// differents options d'affichage
		g2.setGras();
		g2.setUniteAbs(50);
		g2.setUniteOrd(50);
		g2.setTraitsHorizontaux(false);
		g2.setTraitsVerticaux(false);
		g2.setPoliceSize(20);
		// on affiche
		g2.montrer();
	}
	public void componentHidden(ComponentEvent arg0) {
	}
	public void componentMoved(ComponentEvent arg0) {
	}
	public void componentResized(ComponentEvent arg0) {
		if (this.getSize().width>=300) {
			this.largeur = this.getSize().width;
		}
		if (this.getSize().height>=300) {
			this.hauteur = this.getSize().height;
		}
	}
	public void componentShown(ComponentEvent arg0) {
	}
}

class FichiersPNG extends FileFilter {
	// Classe servant a n'afficher que les fichiers PNG lors de la sauvegarde de l'image	
	public static boolean accepts(File f) {
		String n=f.getName().toLowerCase();
		int l=n.length();
		char ex0=n.charAt(l-4);
		char ex1=n.charAt(l-3);
		char ex2=n.charAt(l-2);
		char ex3=n.charAt(l-1);
		return (ex0=='.' && ex1=='p' && ex2=='n' && ex3=='g');
	}
	public boolean 	accept(File f) {
		if (f.isDirectory()) {
			return true; 
		}
		String n=f.getName().toLowerCase();
		int l=n.length();

		char ex0=n.charAt(l-4);
		char ex1=n.charAt(l-3);
		char ex2=n.charAt(l-2);
		char ex3=n.charAt(l-1);
		return (ex0=='.' && ex1=='p' && ex2=='n' && ex3=='g');
	}
	public String 	getDescription() {
		return "fichiers PNG";
	}
}

