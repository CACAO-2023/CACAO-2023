package abstraction.eqXRomu;

import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.appelsOffres.IVendeurAO;
import abstraction.eqXRomu.appelsOffres.PropositionAchatAO;
import abstraction.eqXRomu.appelsOffres.SuperviseurVentesAO;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class RomuATVBABVendeurAppelOffre extends RomuATVBAcheteurBourse implements IVendeurAO {
	protected SuperviseurVentesAO superviseurVentesAO;

	public void initialiser() {
		super.initialiser();
		this.superviseurVentesAO = (SuperviseurVentesAO)(Filiere.LA_FILIERE.getActeur("Sup.AO"));
	}

	public void next() {
		super.next();
		// === Lancement si possible d'un appel d'offre
		if (this.superviseurVentesAO!=null) {
			boolean tg = false;//Math.random()*100.0 >=50.0 ? true : false;
			List<Chocolat> chocoEnStock = new LinkedList<Chocolat>();
			for (Chocolat c : this.stockChoco.keySet()) {
				if (this.stockChoco.get(c)>=2.0) {
					chocoEnStock.add(c);
				}
			}
			if (chocoEnStock.size()>0) {
				int alea = (int)(Math.random()*chocoEnStock.size());
				Chocolat cho = chocoEnStock.get(alea);
				int pourcentageCacao =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+cho.getGamme()).getValeur());
				ChocolatDeMarque choc = new ChocolatDeMarque(cho, "Villors", pourcentageCacao,0);
				PropositionAchatAO propositionRetenue = this.superviseurVentesAO.vendreParAO(this, this.cryptogramme, choc, 2, tg);
				if (propositionRetenue!=null) {
					this.journal.ajouter(COLOR_LLGRAY, COLOR_GREEN, "   AOV : vente de 2T de "+propositionRetenue.getOffre().getChocolat()+" a "+propositionRetenue.getAcheteur().getNom());
					this.stockChoco.put(cho, this.stockChoco.get(cho)-250.0);
					this.journal.ajouter(COLOR_LLGRAY, COLOR_GREEN, "   AOV : stock("+cho+") -->"+this.stockChoco.get(cho));
				}
			}
		}
	}
	//========================================================
	//                        IVendeurAO
	//========================================================
	public PropositionAchatAO choisir(List<PropositionAchatAO> propositions) {
		this.journal.ajouter(COLOR_LLGRAY, COLOR_GREEN, "   AOV : propositions recues : "+propositions);
		PropositionAchatAO retenue = propositions.size()>0 ? propositions.get(0) : null;
		int index=0;
		while (retenue!=null && index<propositions.size()-1 && retenue.getAcheteur()==this) {
			index++;
			retenue = propositions.get(index);
		}
		return retenue; 
	}

}
