package abstraction.eq4Transformateur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;

public class Transformateur1Acteur implements IActeur {
	
	protected int cryptogramme;

	public Transformateur1Acteur() {
	}
	
	public void initialiser() {
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ4";
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() {
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(229, 243, 157); 
	}

	public String getDescription() {
		return "Pour les produits bas de gamme vendus sous marque distributeur, notre objectif est de maximiser les ventes en proposant des prix compétitifs. Nous comparons les prix proposés par tous les producteurs et la bourse afin d'acheter le cacao au meilleur prix. Les producteurs sont sélectionnés par contrat cadre ainsi que les distributeurs. Nous envisageons également des principes de solde et de promotion pour écouler les stocks plus rapidement, à choisir entre la période des fêtes ou non.\r\n"
				+ "Pour les produits haut de gamme, nous souhaitons vendre directement sous notre marque en tant que transfo-distributeur responsable en matière de RSE. Nous signons des contrats cadres avec les producteurs sélectionnés pour garantir la qualité de notre cacao. Nous prenons également en compte l'avis des clients pour décider si nous optons pour une approche haut de gamme ou moyenne gamme en matière de RSE. En cas de surplus de stock, nous pouvons envisager de vendre à la bourse. Notre objectif n'est pas de faire de grosses marges, mais de vendre en quantité suffisante pour maintenir notre entreprise durable et responsable.";
	}

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		return res;
	}

	// Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	// Renvoie les journaux
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		return res;
	}

	////////////////////////////////////////////////////////
	//               En lien avec la Banque               //
	////////////////////////////////////////////////////////

	// Appelee en debut de simulation pour vous communiquer 
	// votre cryptogramme personnel, indispensable pour les
	// transactions.
	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}

	// Appelee lorsqu'un acteur fait faillite (potentiellement vous)
	// afin de vous en informer.
	public void notificationFaillite(IActeur acteur) {
	}

	// Apres chaque operation sur votre compte bancaire, cette
	// operation est appelee pour vous en informer
	public void notificationOperationBancaire(double montant) {
	}
	
	// Renvoie le solde actuel de l'acteur
	public double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme);
	}

	////////////////////////////////////////////////////////
	//        Pour la creation de filieres de test        //
	////////////////////////////////////////////////////////

	// Renvoie la liste des filieres proposees par l'acteur
	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		return(filieres);
	}

	// Renvoie une instance d'une filiere d'apres son nom
	public Filiere getFiliere(String nom) {
		return Filiere.LA_FILIERE;
	}

}
