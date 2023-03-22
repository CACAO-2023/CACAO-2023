package presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controle.CtrlBtnFaillite;
import controle.CtrlBtnNext;
import controle.CtrlCheckBoxGraphique;
import controle.CtrlCheckBoxHistorique;
import controle.CtrlCheckBoxJournal;
import controle.CtrlJTextField;
import controle.CtrlLabelEtape;
import abstraction.eq1Producteur1.Producteur1;
import abstraction.eq2Producteur2.Producteur2;
import abstraction.eq3Producteur3.Producteur3;
import abstraction.eq4Transformateur1.Transformateur1;
import abstraction.eq5Transformateur2.Transformateur2;
import abstraction.eq6Transformateur3.Transformateur3Stocks;
import abstraction.eq7Distributeur1.Distributeur1;
import abstraction.eq8Distributeur2.Distributeur2;
import abstraction.eq9Distributeur3.Distributeur3;
import abstraction.eqXRomu.Romu;
import abstraction.eqXRomu.filiere.Banque;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.FiliereParDefaut;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import presentation.secondaire.FenetreGraphique;
import presentation.secondaire.FenetreHistorique;
import presentation.secondaire.FenetreJournal;

/**
 * Classe modelisant la fenetre principale de l'interface.
 * C'est cette classe qui comporte la methode main de l'application.
 * 
 * @author Romuald Debruyne
 */
public class FenetrePrincipale extends JFrame {

	public static FenetrePrincipale LA_FENETRE_PRINCIPALE;
	private static ImageIcon UNCHECKED_IMAGE = new ImageIcon("unche18.gif");
	private static ImageIcon CHECKED_IMAGE = new ImageIcon("checkd18.gif");
	private static ImageIcon CHART_IMAGE  = new ImageIcon("chart.png");
	private static ImageIcon HISTORY_IMAGE= new ImageIcon("history.png");

	private static final long serialVersionUID = 1L;

	private HashMap<IActeur, JButton>boutonsFaillite;
	private HashMap<IActeur, Integer> cryptos; // Pour acceder aux variables a acces restreint
	
	public FenetrePrincipale(String[] args) {
		super("CACAO 2023");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		LA_FENETRE_PRINCIPALE = this;
		if (args.length==0) { // Filiere par defaut
			Filiere.LA_FILIERE = new FiliereParDefaut();
		} else {
			String filiereChoisie = args[0];
			System.out.println(filiereChoisie);
			IActeur[] equipes = { new Producteur1(), new Producteur2(), new Producteur3(), new Transformateur1(), new Transformateur2(), new Transformateur3Stocks(), new Transformateur3Stocks(), new Distributeur1(), new Distributeur2(), new Distributeur3(), new Romu()};
			int i=0; 
			while (i<equipes.length && !equipes[i].getNomsFilieresProposees().contains(filiereChoisie)) {
				i++;
			}
			if (i<equipes.length) {
				Filiere.LA_FILIERE = equipes[i].getFiliere(filiereChoisie);
			} else {
				System.out.println("Aucune equipe ne propose la filiere \""+filiereChoisie+"\"");
				System.out.println("Les filieres proposees sont : ");
				for (IActeur eq : equipes) {
					System.out.println("equipe "+eq.getNom()+" : "+eq.getNomsFilieresProposees());
				}
				System.exit(0);
			}
		}
		Filiere.LA_FILIERE.initialiser();
		//Filiere.LA_FILIERE.peupler();
		this.setLayout(new BorderLayout());

		// LABEL Etape indiquant l'etape a laquelle on est.
		JLabel labelEtape = new JLabel("Etape : "+Filiere.LA_FILIERE.getEtape()+" = "+(Filiere.LA_FILIERE==null ? "" : Filiere.LA_FILIERE.getDate()));
		Filiere.LA_FILIERE.addObserver(new CtrlLabelEtape(labelEtape));
		this.add(labelEtape, BorderLayout.NORTH);
		
		this.boutonsFaillite=new HashMap<IActeur, JButton>();

		// Indicateurs
		JPanel pIndicateurs = new JPanel();
		pIndicateurs.setLayout(new BoxLayout(pIndicateurs, BoxLayout.Y_AXIS));
		pIndicateurs.setBorder(BorderFactory.createTitledBorder("Indicateurs"));

		JPanel pParametres = new JPanel();
		pParametres.setLayout(new BoxLayout(pParametres, BoxLayout.Y_AXIS));
		pParametres.setBorder(BorderFactory.createTitledBorder("Parametres"));

		pIndicateurs.add(Box.createVerticalGlue());
		//List<Variable> indicateurs = Filiere.LA_FILIERE.getIndicateurs();

		JPanel panelIcones = new JPanel(new FlowLayout(FlowLayout.TRAILING));

		JLabel iChart = new JLabel(CHART_IMAGE);
		iChart.setBorder(new EmptyBorder(0, 1, 0, 1));
		panelIcones.add(iChart);

		JLabel iHistory = new JLabel(HISTORY_IMAGE);
		iHistory.setBorder(new EmptyBorder(0, 1, 0, 1));
		panelIcones.add(iHistory);

		pIndicateurs.add(panelIcones);

		JPanel panelIndicateurs = new JPanel();
		panelIndicateurs.setLayout(new BoxLayout(panelIndicateurs, BoxLayout.Y_AXIS));

		JPanel panelParametres = new JPanel();
		panelParametres.setLayout(new BoxLayout(panelParametres, BoxLayout.Y_AXIS));

		List<IActeur> acteurs = Filiere.LA_FILIERE.getActeurs();
		for (IActeur acteur : acteurs) {
			if (!(acteur instanceof Banque)) {
				List<Variable> indicateurs = Filiere.LA_FILIERE.getIndicateurs(acteur);
				for (Variable i : indicateurs){
					JPanel pIndic = new JPanel();
					pIndic.setBackground(acteur.getColor());
					pIndic.setLayout(new BorderLayout());
					JPanel pReste = new JPanel();
					pReste.setBackground(acteur.getColor());

					// Nom de l'indicateur
					JLabel lIndic = new JLabel( i.getNom());
					if (i.getInfobulle()!=null && i.getInfobulle().length()>0) {
						lIndic.setToolTipText(i.getInfobulle());
					}
					lIndic.setAlignmentX(RIGHT_ALIGNMENT);
					pReste.add(lIndic);

					// Champ de saisie permettant de modifier la valeur de l'indicateur
					JTextField tIndic = new JTextField(12);
					tIndic.setHorizontalAlignment(SwingConstants.RIGHT);
					NumberFormat dc = NumberFormat.getInstance(Locale.FRANCE);
					dc.setMaximumFractionDigits(2);
					dc.setMinimumFractionDigits(2);
					String formattedText = dc.format(i.getValeur(this.cryptos.get(i.getCreateur())));
					if (i.getMin(this.cryptos.get(i.getCreateur()))!=-Double.MAX_VALUE || i.getMax(this.cryptos.get(i.getCreateur()))!=Double.MAX_VALUE) {
						tIndic.setToolTipText("["+dc.format(i.getMin(this.cryptos.get(i.getCreateur())))+", "+dc.format(i.getMax(this.cryptos.get(i.getCreateur())))+"]");
					}
					tIndic.setText(formattedText);
					CtrlJTextField controlJTextField = new CtrlJTextField(tIndic, i);
					tIndic.addActionListener(controlJTextField);
					i.addObserver(controlJTextField);
					tIndic.setMinimumSize(new Dimension(400,tIndic.getSize().height));
					tIndic.setAlignmentX(RIGHT_ALIGNMENT);
					pReste.add(tIndic);
					pIndic.add(pReste, BorderLayout.EAST);

					// Case a cocher "Graphique" permettant d'afficher/cacher le graphique de l'indicateur
					JCheckBox cGraphiqueIndic = new JCheckBox(); 
					cGraphiqueIndic.setIcon(UNCHECKED_IMAGE);
					cGraphiqueIndic.setSelectedIcon(CHECKED_IMAGE);
					cGraphiqueIndic.setToolTipText("Montrer/Cacher le graphique");
					FenetreGraphique graphique = new FenetreGraphique(i.getNom(), 800, 600);
					graphique.ajouter(i.getCourbe(this.cryptos.get(i.getCreateur())));
					// Controleur permettant quand on clique sur la fermeture 
					// de la fenetre graphique de mettre a jour la case a cocher "graphique"
					// et quand on clique sur la case a cocher d'afficher/masquer le graphique
					CtrlCheckBoxGraphique ctg = new CtrlCheckBoxGraphique(graphique, cGraphiqueIndic);
					i.addObserver(ctg);
					cGraphiqueIndic.addActionListener(ctg);
					graphique.addWindowListener(ctg);
					cGraphiqueIndic.setAlignmentX(RIGHT_ALIGNMENT);
					cGraphiqueIndic.setBorder(BorderFactory.createEmptyBorder());

					pReste.add(cGraphiqueIndic);

					// Case a cocher "Historique" permettant d'afficher/cacher l'historique de l'indicateur
					JCheckBox cHistorique = new JCheckBox();
					cHistorique.setToolTipText("Montrer/Cacher l'historique");		
					cHistorique.setIcon(UNCHECKED_IMAGE);
					cHistorique.setSelectedIcon(CHECKED_IMAGE);
					//cHistorique.setIcon(new ImageIcon("unch18.gif"));
					FenetreHistorique fenetreHistorique = new FenetreHistorique(i,this.cryptos.get(i.getCreateur()));
					CtrlCheckBoxHistorique cth = new CtrlCheckBoxHistorique(i.getHistorique(this.cryptos.get(i.getCreateur())), cHistorique, fenetreHistorique);
					fenetreHistorique.addWindowListener(cth);
					i.getHistorique(this.cryptos.get(i.getCreateur())).addObserver(cth);
					cHistorique.addActionListener(cth);
					cHistorique.setAlignmentX(RIGHT_ALIGNMENT);
					cHistorique.setBorder(BorderFactory.createEmptyBorder());

					pReste.add(cHistorique);

					pIndicateurs.add(Box.createVerticalGlue());
					pIndicateurs.add(pIndic);
				}
			}
			List<Variable> parametres = Filiere.LA_FILIERE.getParametres(acteur);
			for (Variable i : parametres){
				JPanel pParam = new JPanel();
				pParam.setBackground(acteur.getColor());
				pParam.setLayout(new BorderLayout());
				JPanel pReste = new JPanel();
				pReste.setBackground(acteur.getColor());

				// Nom du parametre
				JLabel lIndic = new JLabel( i.getNom());
				lIndic.setToolTipText(i.getInfobulle());
				lIndic.setAlignmentX(RIGHT_ALIGNMENT);
				pReste.add(lIndic);

				// Champ de saisie permettant de modifier la valeur du parametre
				JTextField tParam = new JTextField(12);
				tParam.setHorizontalAlignment(SwingConstants.RIGHT);
				NumberFormat dc = NumberFormat.getInstance(Locale.FRANCE);
				dc.setMaximumFractionDigits(2);
				dc.setMinimumFractionDigits(2);
				String formattedText = dc.format(i.getValeur(this.cryptos.get(i.getCreateur())));
				tParam.setText(formattedText);
				if (i.getMin(this.cryptos.get(i.getCreateur()))!=-Double.MAX_VALUE || i.getMax(this.cryptos.get(i.getCreateur()))!=Double.MAX_VALUE) {
					tParam.setToolTipText("["+dc.format(i.getMin(this.cryptos.get(i.getCreateur())))+", "+dc.format(i.getMax(this.cryptos.get(i.getCreateur())))+"]");
				}
				CtrlJTextField controlJTextField = new CtrlJTextField(tParam, i);
				tParam.addActionListener(controlJTextField);
				i.addObserver(controlJTextField);
				tParam.setMinimumSize(new Dimension(400,tParam.getSize().height));
				tParam.setAlignmentX(RIGHT_ALIGNMENT);
				pReste.add(tParam);
				pParam.add(pReste, BorderLayout.EAST);

				// Case a cocher "Graphique" permettant d'afficher/cacher le graphique de l'indicateur
				JCheckBox cGraphiqueParam = new JCheckBox(); 
				FenetreGraphique graphique = new FenetreGraphique(i.getNom(), 800, 600);
				cGraphiqueParam.setIcon(UNCHECKED_IMAGE);
				cGraphiqueParam.setSelectedIcon(CHECKED_IMAGE);
				graphique.ajouter(i.getCourbe(this.cryptos.get(i.getCreateur())));
				// Controleur permettant quand on clique sur la fermeture 
				// de la fenetre graphique de mettre a jour la case a cocher "graphique"
				// et quand on clique sur la case a cocher d'afficher/masquer le graphique
				CtrlCheckBoxGraphique ctg = new CtrlCheckBoxGraphique(graphique, cGraphiqueParam);
				i.addObserver(ctg);
				cGraphiqueParam.addActionListener(ctg);
				graphique.addWindowListener(ctg);
				cGraphiqueParam.setAlignmentX(RIGHT_ALIGNMENT);
				cGraphiqueParam.setBorder(BorderFactory.createEmptyBorder());

				pReste.add(cGraphiqueParam);

				// Case a cocher "Historique" permettant d'afficher/cacher l'historique de l'indicateur
				JCheckBox cHistorique = new JCheckBox();
				cHistorique.setIcon(UNCHECKED_IMAGE);
				cHistorique.setSelectedIcon(CHECKED_IMAGE);
				FenetreHistorique fenetreHistorique = new FenetreHistorique(i,this.cryptos.get(i.getCreateur()));
				CtrlCheckBoxHistorique cth = new CtrlCheckBoxHistorique(i.getHistorique(this.cryptos.get(i.getCreateur())), cHistorique, fenetreHistorique);
				fenetreHistorique.addWindowListener(cth);
				i.getHistorique(this.cryptos.get(i.getCreateur())).addObserver(cth);
				cHistorique.addActionListener(cth);
				cHistorique.setAlignmentX(RIGHT_ALIGNMENT);
				cHistorique.setBorder(BorderFactory.createEmptyBorder());

				pReste.add(cHistorique);

				pParametres.add(Box.createVerticalGlue());
				pParametres.add(pParam);
			}

		}
		JPanel pGauche = new JPanel();
		//pGauche.setBorder(BorderFactory.createTitledBorder("Indicateurs"));
		//	pIndicateurs.setToolTipText("bla bla bla");

		pGauche.setLayout(new BoxLayout(pGauche, BoxLayout.Y_AXIS));
		pGauche.add(pIndicateurs);		
		pGauche.add(Box.createVerticalGlue());
		pGauche.add(pParametres);		
		//pGauche.add(Box.createVerticalGlue());


		JPanel pDroit = new JPanel();

		JPanel pComptes = new JPanel();
		pComptes.setLayout(new BoxLayout(pComptes, BoxLayout.Y_AXIS));
		pComptes.setBorder(BorderFactory.createTitledBorder("Comptes Bancaires"));

		JPanel panelIconesComptes = new JPanel(new FlowLayout(FlowLayout.TRAILING));

		JLabel imgChart = new JLabel(CHART_IMAGE);
		imgChart.setBorder(new EmptyBorder(0, 1, 0, 1));
		panelIconesComptes.add(imgChart);

		JLabel imgHistory = new JLabel(HISTORY_IMAGE);
		imgHistory.setBorder(new EmptyBorder(0, 1, 0, 1));
		panelIconesComptes.add(imgHistory);
		pComptes.add(panelIconesComptes);
		pComptes.add(Box.createVerticalGlue());



		List<Variable> comptes = Filiere.LA_FILIERE.getBanque().getIndicateurs();
		for (Variable i : comptes){
			JPanel pIndic = new JPanel();
			pIndic.setBackground(i.getCreateur().getColor());
			pIndic.setLayout(new BorderLayout());
			JPanel pReste = new JPanel();
			pReste.setBackground(i.getCreateur().getColor());

			// Nom de l'indicateur
			JLabel lIndic = new JLabel( i.getNom());
			lIndic.setAlignmentX(RIGHT_ALIGNMENT);
			lIndic.setToolTipText(i.getCreateur().getDescription());
			pReste.add(lIndic);

			// Champ de saisie permettant de modifier la valeur de l'indicateur
			JTextField tIndic = new JTextField(12);
			tIndic.setHorizontalAlignment(SwingConstants.RIGHT);
			NumberFormat dc = NumberFormat.getInstance(Locale.FRANCE);
			dc.setMaximumFractionDigits(2);
			dc.setMinimumFractionDigits(2);
			String formattedText = dc.format(i.getValeur(this.cryptos.get(i.getCreateur())));
			tIndic.setText(formattedText);
			if (i.getMin(this.cryptos.get(i.getCreateur()))!=-Double.MAX_VALUE || i.getMax(this.cryptos.get(i.getCreateur()))!=Double.MAX_VALUE) {
				tIndic.setToolTipText("["+dc.format(i.getMin(this.cryptos.get(i.getCreateur())))+", "+dc.format(i.getMax(this.cryptos.get(i.getCreateur())))+"]");
			}
			CtrlJTextField controlJTextField = new CtrlJTextField(tIndic, i);
			tIndic.addActionListener(controlJTextField);
			i.addObserver(controlJTextField);
			tIndic.setMinimumSize(new Dimension(400,tIndic.getSize().height));
			tIndic.setAlignmentX(RIGHT_ALIGNMENT);
			pReste.add(tIndic);
			pIndic.add(pReste, BorderLayout.EAST);

			// Bouton Faillite
			JButton bFaillite = new JButton("Faillite");
			bFaillite.setPreferredSize(new Dimension(86,18));
			bFaillite.addActionListener(new CtrlBtnFaillite(bFaillite, i.getCreateur()));
			boutonsFaillite.put(i.getCreateur(), bFaillite);
			pReste.add(bFaillite);
			

			// Case a cocher "Graphique" permettant d'afficher/cacher le graphique de l'indicateur
			JCheckBox cGraphiqueIndic = new JCheckBox(); 
			cGraphiqueIndic.setIcon(UNCHECKED_IMAGE);
			cGraphiqueIndic.setSelectedIcon(CHECKED_IMAGE);
			cGraphiqueIndic.setToolTipText("Montrer/Cacher le graphique");
			FenetreGraphique graphique = new FenetreGraphique(i.getNom(), 800, 600);
			graphique.ajouter(i.getCourbe(this.cryptos.get(i.getCreateur())));
			// Controleur permettant quand on clique sur la fermeture 
			// de la fenetre graphique de mettre a jour la case a cocher "graphique"
			// et quand on clique sur la case a cocher d'afficher/masquer le graphique
			CtrlCheckBoxGraphique ctg = new CtrlCheckBoxGraphique(graphique, cGraphiqueIndic);
			i.addObserver(ctg);
			cGraphiqueIndic.addActionListener(ctg);
			graphique.addWindowListener(ctg);
			cGraphiqueIndic.setAlignmentX(RIGHT_ALIGNMENT);
			cGraphiqueIndic.setBorder(BorderFactory.createEmptyBorder());
			pReste.add(cGraphiqueIndic);

			// Case a cocher "Historique" permettant d'afficher/cacher l'historique de l'indicateur
			JCheckBox cHistorique = new JCheckBox();
			cHistorique.setToolTipText("Montrer/Cacher l'historique");		
			cHistorique.setIcon(UNCHECKED_IMAGE);//new ImageIcon("unche18.gif"));
			cHistorique.setSelectedIcon(CHECKED_IMAGE);//new ImageIcon("checkd18.gif"));
			//cHistorique.setIcon(new ImageIcon("unch18.gif"));
			FenetreHistorique fenetreHistorique = new FenetreHistorique(i,this.cryptos.get(i.getCreateur()));
			CtrlCheckBoxHistorique cth = new CtrlCheckBoxHistorique(i.getHistorique(this.cryptos.get(i.getCreateur())), cHistorique, fenetreHistorique);
			fenetreHistorique.addWindowListener(cth);
			i.getHistorique(this.cryptos.get(i.getCreateur())).addObserver(cth);
			cHistorique.addActionListener(cth);
			cHistorique.setAlignmentX(RIGHT_ALIGNMENT);
			cHistorique.setBorder(BorderFactory.createEmptyBorder());
			pReste.add(cHistorique);

			pComptes.add(Box.createVerticalGlue());
			pComptes.add(pIndic);
		}



		JPanel pJournaux = new JPanel();
		pJournaux.setLayout(new BoxLayout(pJournaux, BoxLayout.Y_AXIS));
		pJournaux.setBorder(BorderFactory.createTitledBorder("Journaux"));
		for (IActeur acteur : acteurs) {
			for (Journal j : Filiere.LA_FILIERE.getJournaux(acteur)) {
				JPanel pJournal = new JPanel();
				pJournal.setLayout(new BorderLayout());
				JPanel pReste = new JPanel();
				pReste.setBackground(acteur.getColor());
				pJournal.setBorder(null);//new EmptyBorder(0, 0, 0, 0));//10));
				pJournal.setBackground(acteur.getColor());

				JLabel lJournal = new JLabel(j.getNom());
				lJournal.setAlignmentX(RIGHT_ALIGNMENT);
				//	lJournal.setAlignmentX(RIGHT_ALIGNMENT);
				//lJournal.setToolTipText(acteur.getDescription());
				pReste.add(lJournal);
				//		pJournal.add(lJournal, BorderLayout.CENTER);
				JCheckBox cJournal = new JCheckBox();
				cJournal.setBackground(acteur.getColor());
				cJournal.setToolTipText("Montrer/Cacher le journal");		
				cJournal.setIcon(UNCHECKED_IMAGE);//new ImageIcon("unche18.gif"));
				cJournal.setSelectedIcon(CHECKED_IMAGE);//new ImageIcon("checkd18.gif"));

				FenetreJournal fenetreJournal = new FenetreJournal(j);
				fenetreJournal.setCheckBox(cJournal);
				CtrlCheckBoxJournal controlJournal = new CtrlCheckBoxJournal(j, cJournal, fenetreJournal);
				//JPanel pCheck = new JPanel();
				//pCheck.setBackground(acteur.getColor());
				//pCheck.add(cJournal);
				pReste.add(cJournal);
				j.addObserver(controlJournal);
				cJournal.addActionListener(controlJournal);

				cJournal.setAlignmentX(RIGHT_ALIGNMENT);
				pJournal.add(pReste,
						//pCheck,
						//cJournal,
						BorderLayout.EAST);//.WEST);
				pJournaux.add(Box.createVerticalGlue());
				pJournaux.add(pJournal);
			}
		}
		pDroit.setLayout(new BoxLayout(pDroit, BoxLayout.Y_AXIS));
		pDroit.add(pComptes);		
		pDroit.add(Box.createVerticalGlue());
		pDroit.add(pJournaux);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(pGauche), new JScrollPane(pDroit));
		splitPane.setResizeWeight(0.5);
		Dimension minimumSize = new Dimension(100, 100);
		pGauche.setMinimumSize(minimumSize);
		pJournaux.setMinimumSize(minimumSize);
		this.add(splitPane, BorderLayout.CENTER);

		JPanel pSouth = new JPanel();
		pSouth.setLayout(new GridLayout(1,3));
		JButton btnNext = new JButton("Next");
//		btnNext.setBackground(Color.DARK_GRAY);
//		btnNext.setForeground(Color.CYAN);
		btnNext.addActionListener(new CtrlBtnNext(Filiere.LA_FILIERE, 1));
		pSouth.add(btnNext);
		JButton btnNext10 = new JButton("10 Nexts");
		btnNext10.addActionListener(new CtrlBtnNext(Filiere.LA_FILIERE, 10));
		pSouth.add(btnNext10);
		JButton btnNext100 = new JButton("100 Nexts");
		btnNext100.addActionListener(new CtrlBtnNext(Filiere.LA_FILIERE,100));
		pSouth.add(btnNext100);

		this.add(pSouth, BorderLayout.SOUTH);
		this.pack();
		Dimension dim = this.getSize();
		this.setSize(new Dimension((int)dim.getWidth()+50,(int)dim.getHeight()-100));
	}
	public void notificationFaillite(IActeur acteur) {
		this.boutonsFaillite.get(acteur).setEnabled(false);
	}
	public void setCryptos(HashMap<IActeur, Integer> cryptos) {
		if (this.cryptos==null) { // Les cryptogrammes ne sont indique qu'une fois par la banque : si la methode est appelee une seconde fois c'est que l'auteur de l'appel n'est pas la banque et qu'on cherche a "pirater" l'acteur
			this.cryptos= cryptos;
		}
	}

	public static void main(String[] args) {
		LA_FENETRE_PRINCIPALE=new FenetrePrincipale(args);
		LA_FENETRE_PRINCIPALE.setVisible(true);
	}
}
