package abstraction.eqXRomu.clients;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class ExempleTransformateurImproductif implements IFabricantChocolatDeMarque {
	private static int NB_INSTANCES = 0; // Afin d'attribuer un nom different a toutes les instances
	protected int numero;
	protected Integer cryptogramme;
	private ChocolatDeMarque choco;

	public ExempleTransformateurImproductif(ChocolatDeMarque choco) {
		NB_INSTANCES++;
		this.numero=NB_INSTANCES;
		this.choco = choco;
	}
	
	public String getNom() {
		return "Transf"+numero;
	}

	public String toString() {
		return this.getNom();
	}

	public String getDescription() {
		return "transformateur improductif";
	}

	public Color getColor() {
		return Color.white;
	}

	public void initialiser() {
	}

	public void next() {
	}

	public List<String> getNomsFilieresProposees() {
		return new ArrayList<String>();
	}

	public Filiere getFiliere(String nom) {
		return null;
	}

	public List<Variable> getIndicateurs() {
		return new ArrayList<Variable>();
	}

	public List<Variable> getParametres() {
		return new ArrayList<Variable>();
	}

	public List<Journal> getJournaux() {
		return new ArrayList<Journal>();
	}

	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}

	public void notificationFaillite(IActeur acteur) {
	}

	public void notificationOperationBancaire(double montant) {
	}

	public List<ChocolatDeMarque> getChocolatsProduits() {
		List<ChocolatDeMarque>res=new ArrayList<ChocolatDeMarque>();
		res.add(choco);
		return res;
	}

}
