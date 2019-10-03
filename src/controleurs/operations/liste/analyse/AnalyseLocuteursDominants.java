package controleurs.operations.liste.analyse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;
import org.joda.time.Months;

import comparators.LocuteurIntensiteComparator;
import controleurs.vuesabstraites.ProjetView;
import modeles.LocuteurModel;

public class AnalyseLocuteursDominants {

	private ProjetView activitesView;
	private ResourceBundle bundleOperationsListe;
	private SortedSet<LocuteurModel> setLocuteurs;
	private Date debut;
	private Date fin;
	private float paramLocuteursDominants;
	private float moyenneIntensite, pourcentLocuteursDominants3PremiersMois,
			dureeMoyenneParticipationLocuteursDominants,
			ecartTypeParticipationLocuteursDominants,
			pourcentParticipationLocuteursDominants, pourcentPL,
			pourcentMessagesLD, pourcentLocuteursDominants,
			pourcentMessagesLocuteursDominants,
			pourcentLocuteursDominantsSaufPremier,
			pourcentMessagesLocuteursDominantsSaufPremier;
	private int nbreMessages, nbreLocuteursDominants3PremiersMois, nbrePL,
			nbreMessagesLocuteursDominants, nbreMessagesLD,
			nbreLocuteursDominants, nbreMessagesLocuteursDominantsSaufPremier;
	private String locuteurDominant;

	public AnalyseLocuteursDominants(ProjetView activitesView,
			ResourceBundle bundleOperationsListe,
			SortedSet<LocuteurModel> setLocuteurs, Date debut, Date fin,
			float paramLocuteursDominants, int nbreMessages) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.setLocuteurs = setLocuteurs;
		this.debut = debut;
		this.fin = fin;
		this.paramLocuteursDominants = paramLocuteursDominants;
		this.nbreMessages = nbreMessages;
	}

	// public SortedSet<LocuteurModel> getSetLocuteursDominants() {
	public void analyse() {
		// CALCUL DES LOCUTEURS DOMINANTS
		// 1. initialisation du tableau des locuteurs : initLocuteursLD(AA)
		// initLocuteursLD(AA);
		// 2. setting du parametre paramLocuteursDominants passe en parametre
		// setParamLocuteursDominants(paramLocuteursDominants);
		// 3. calcul de la liste des Locuteurs Dominants
		// en fonction de la superiorite au produit
		// intensite moyenne de participation par mois de tous les locuteurs X
		// parametre
		SortedSet<LocuteurModel> setLocuteursDominants = new TreeSet<LocuteurModel>(
				new LocuteurIntensiteComparator());
		activitesView.setStepProgress(1);
		activitesView.setLabelProgress(bundleOperationsListe
				.getString("txt_CalculLD"));
		Map<Float, List<LocuteurModel>> mapIntensiteLocuteurs = new TreeMap<Float, List<LocuteurModel>>();
		// Calcul de l'intensite totale et creation d'une MAP intensite X
		// locuteurs
		float intensiteTotale = 0;
		for (LocuteurModel locuteur : setLocuteurs) {
			float intensite = locuteur.getIntensite();
			intensiteTotale += intensite;
			if (!mapIntensiteLocuteurs.containsKey(intensite)) {
				List<LocuteurModel> newListLocuteurs = new ArrayList<LocuteurModel>();
				newListLocuteurs.add(locuteur);
				mapIntensiteLocuteurs.put(intensite, newListLocuteurs);
			} else {
				List<LocuteurModel> listLocuteurs = mapIntensiteLocuteurs
						.get(intensite);
				listLocuteurs.add(locuteur);
				mapIntensiteLocuteurs.put(intensite, listLocuteurs);
			}
		}
		// Calcul de la moyenne des intensites
		int nbreLocuteurs = setLocuteurs.size();
		float moyenneIntensite = intensiteTotale / setLocuteurs.size();
		setMoyenneIntensite(moyenneIntensite);
		// Calcul du seuil d'intensite pour Dominance
		float intensiteParticipationDominante = paramLocuteursDominants
				* moyenneIntensite;
		System.out.println("nombre de locuteurs = " + nbreLocuteurs + " | moyenneIntensite = " + moyenneIntensite + " | intensiteParticipationDominante = "
				+ intensiteParticipationDominante);

		// Affichage mapIntensiteLocuteurs
		// for (Entry<Float, List<Locuteur>> e :
		// mapIntensiteLocuteurs.entrySet()) {
		// System.out.print(e.getKey() + " => [");
		// for (Locuteur locuteur : e.getValue()) {
		// System.out.print(locuteur.getNom() + ", ");
		// }
		// System.out.print("]");
		// System.out.println();
		// }

		
		
		// Creation d'une SortedMap a partir de la TreeMap intensite X locuteurs
		// et application d'un filtre pour ne garder que les intensites
		// superieures au seuil d'intensite pour la Dominance
		SortedMap<Float, List<LocuteurModel>> sortedMapIntensiteLocuteursDominants = ((TreeMap<Float, List<LocuteurModel>>) mapIntensiteLocuteurs)
				.tailMap(intensiteParticipationDominante);
		System.out.println("sortedMapIntensiteLocuteursDominants size = " + sortedMapIntensiteLocuteursDominants.size());
		// 4. Si on obtient une liste avec au moins 2 intensites correspondante au
		// parametre de selection
		// - On modifie la liste des Locuteurs en modifiant leur LD qui passe a
		// TRUE
		// - On rempli la liste listLocuteursDominants
		// - On calcule le nombre de messages total des locuteurs dominants
		// - On rempli une MAP dateDebut - LocuteurDominant et une MAP duree -
		// LocuteurDominant
		if (sortedMapIntensiteLocuteursDominants.size() > 1) {
			// Affichage mapIntensiteLocuteursDominants
			// + Creation Map Locuteurs dominants selon Date
			// + Creation Map Locuteurs dominants selon Duree en mois
			Map<DateTime, List<LocuteurModel>> mapDateLocuteursDominants = new TreeMap<DateTime, List<LocuteurModel>>();
			Map<Integer, List<LocuteurModel>> mapDureeMoisLocuteursDominants = new TreeMap<Integer, List<LocuteurModel>>();
			int i = 0;
			// System.out.println("Locuteurs dominants : ");
			for (Entry<Float, List<LocuteurModel>> e : sortedMapIntensiteLocuteursDominants
					.entrySet()) {
				System.out.print(e.getKey() + " => [");
				for (LocuteurModel locuteurDominant : e.getValue()) {
					System.out.print(locuteurDominant.getNom() + ", ");
					setLocuteursDominants.add(locuteurDominant);
					locuteurDominant.setLd(true);
					i += locuteurDominant.getNbreMessages();
					// map date
					DateTime dtDebut = new DateTime(
							locuteurDominant.getDateDebut());
					DateTime dtFin = new DateTime(locuteurDominant.getDateFin());
					int dureeMois = Months.monthsBetween(dtDebut, dtFin)
							.getMonths();
					if (!mapDateLocuteursDominants.containsKey(dtDebut)) {
						List<LocuteurModel> newListLocuteurs = new ArrayList<LocuteurModel>();
						newListLocuteurs.add(locuteurDominant);
						mapDateLocuteursDominants
								.put(dtDebut, newListLocuteurs);
					} else {
						List<LocuteurModel> listLocuteurs = mapDateLocuteursDominants
								.get(dtDebut);
						listLocuteurs.add(locuteurDominant);
						mapDateLocuteursDominants.put(dtDebut, listLocuteurs);
					}
					// map duree en mois
					if (!mapDureeMoisLocuteursDominants.containsKey(dureeMois)) {
						List<LocuteurModel> newListLocuteurs = new ArrayList<LocuteurModel>();
						newListLocuteurs.add(locuteurDominant);
						mapDureeMoisLocuteursDominants.put(dureeMois,
								newListLocuteurs);
					} else {
						List<LocuteurModel> listLocuteurs = mapDureeMoisLocuteursDominants
								.get(dureeMois);
						listLocuteurs.add(locuteurDominant);
						mapDureeMoisLocuteursDominants.put(dureeMois,
								listLocuteurs);
					}
				}
				 System.out.print("]");
				 System.out.println();
			}
			int nbreLocuteursDominants = setLocuteursDominants.size();

			// Locuteurs dominants dans les 3 premiers mois
			DateTime dtDebutPlus3Mois = new DateTime(debut).plusMonths(3);
			// System.out.println("dateDebutPlus3Mois = " + dtDebutPlus3Mois);
			// TreeMap<DateTime, List<LocuteurModel>>
			// sortedMapDateLocuteursDominants = new TreeMap<DateTime,
			// List<LocuteurModel>>(
			// mapDateLocuteursDominants);
			// Affichage map Date / LD
			// for (Entry<DateTime, List<Locuteur>> e :
			// sortedMapDateLocuteursDominants
			// .entrySet()) {
			// System.out.print(e.getKey() + " => [");
			// for (Locuteur locuteurDominant : e.getValue()) {
			// System.out.print(locuteurDominant.getNom()+", ");
			// }
			// System.out.print("]");
			// System.out.println();
			// }
			SortedMap<DateTime, List<LocuteurModel>> mapLocuteursDominants3PremiersMois = ((TreeMap<DateTime, List<LocuteurModel>>) mapDateLocuteursDominants)
					.headMap(dtDebutPlus3Mois);

			// Setting du nombre de locuteurs dominants presents dans les 3
			// premiers mois + leur % par rapport a l'ensemble des locuteurs
			// dominants
			int nbreLocuteursDominants3PremiersMois = mapLocuteursDominants3PremiersMois
					.size();
			setNbreLocuteursDominants3PremiersMois(nbreLocuteursDominants3PremiersMois);
			float pourcentLocuteursDominants3PremiersMois = (float) nbreLocuteursDominants3PremiersMois
					/ (float) nbreLocuteursDominants * 100;
			setPourcentLocuteursDominants3PremiersMois(pourcentLocuteursDominants3PremiersMois);

			// Duree moyenne participation en mois
			// Affichage map Duree en mois / LD + Calcul de la duree totale en
			// mois
			int dureeMoisTotale = 0;
			for (Entry<Integer, List<LocuteurModel>> e : mapDureeMoisLocuteursDominants
					.entrySet()) {
				List<LocuteurModel> value = e.getValue();
				for (int j = 0; j < value.size(); j++) {
					// System.out.print(locuteurDominant.getNom() + ", ");
					dureeMoisTotale += e.getKey();
				}
			}

			// Setting du nombre de locuteurs dominants presents dans les 3
			// premiers mois + leur % par rapport a l'ensemble des locuteurs
			// dominants
			float dureeMoyenneParticipationLocuteursDominants = (float) dureeMoisTotale
					/ (float) nbreLocuteursDominants;
			setDureeMoyenneParticipationLocuteursDominants(dureeMoyenneParticipationLocuteursDominants);

			// Ecart-type
			double ec2 = 0;
			for (Entry<Integer, List<LocuteurModel>> e : mapDureeMoisLocuteursDominants
					.entrySet()) {
				List<LocuteurModel> value = e.getValue();
				for (int j = 0; j < value.size(); j++) {
					double ec0 = e.getKey()
							- dureeMoyenneParticipationLocuteursDominants;
					double ec1 = Math.pow(ec0, 2);
					ec2 += ec1;
				}
			}
			double ec3 = Math.sqrt(ec2 / setLocuteursDominants.size());

			// Setting Ecart-Type participation des Locuteur Dominants
			float ecartTypeParticipationLocuteursDominants = (float) ec3;
			setEcartTypeParticipationLocuteursDominants(ecartTypeParticipationLocuteursDominants);

			// Pourcent participation locuteurs dominants
			int nbreMoisSuivi = Months.monthsBetween(new DateTime(debut),
					new DateTime(fin)).getMonths();

			// Setting pourcent participation locuteurs dominants
			float pourcentParticipationLocuteursDominants = dureeMoyenneParticipationLocuteursDominants
					/ nbreMoisSuivi * 100;
			if (pourcentParticipationLocuteursDominants > 0)
				setPourcentParticipationLocuteursDominants(pourcentParticipationLocuteursDominants);
			else
				setPourcentParticipationLocuteursDominants(0);

			// Petits locuteurs
			// Setting nombre de petits locuteurs + leur % par rapport au nombre
			// total de locuteurs
			int nbrePL = nbreLocuteurs - nbreLocuteursDominants;
			setNbrePL(nbrePL);
			float pourcentPL = ((float) nbrePL) / nbreLocuteurs * 100;
			setPourcentPL(pourcentPL);

			// AUTRE CALCULS
			// - Setting du nombre de messages des Locuteurs Dominants
			// - Setting du Locuteur dominant et du nombre de ses messages (+ %
			// du
			// nombre total de messages)
			// - Setting du nombre de Locuteurs Dominants + % du nombre total de
			// locuteurs + % de leurs messages par rapport au nombre total de
			// messages)
			// - Setting % de locuteurs dominants sauf le premier + leur nombre
			// de
			// messages + leur % de messages par rapport a tous les messages
			// sauf ceux du premier
			// - Setting du nombre de locuteurs dominants presents dans les 3
			// premiers mois + leur % par rapport a l'ensemble des locuteurs
			// dominants
			// - Calcul de la duree totale en mois
			// - Setting du nombre de locuteurs dominants presents dans les 3
			// premiers mois + leur % par rapport a l'ensemble des locuteurs
			// dominants
			// - Setting Ecart-Type participation des Locuteur Dominants
			// - Setting pourcent participation locuteurs dominants
			// - Setting nombre de petits locuteurs + leur % par rapport au
			// nombre
			// total de locuteurs

			// Setting du nombre de messages des Locuteurs Dominants
			int nbreMessagesLocuteursDominants = i;
			setNbreMessagesLocuteursDominants(nbreMessagesLocuteursDominants);

			// Setting du Locuteur dominant et du nombre de ses messages (+ % du
			// nombre total de messages)
			// LocuteurModel lD =
			// setLocuteursDominants.get(setLocuteursDominanmapIntensiteLocuteursts
			// .size() - 1);
			LocuteurModel lD = setLocuteursDominants.first();
			String locuteurDominant = lD.getNom();
			int nbreMessagesLD = lD.getNbreMessages();
			setLocuteurDominant(locuteurDominant);
			setNbreMessagesLD(nbreMessagesLD);
			float pourcentMessagesLD = (float) nbreMessagesLD
					/ (float) nbreMessages * 100;
			setPourcentMessagesLD(pourcentMessagesLD);
			System.out.println("Locuteur dominant : " + locuteurDominant + " | nbre de messages : " + nbreMessagesLD + " | % de ses messages par rapport au nbre total de messages : " + pourcentMessagesLD);
			

			// Locuteurs dominants
			// Setting du nombre de Locuteurs Dominants (+ % du nombre total de
			// locuteurs)
			// + % de leurs messages par rapport au nombre total de messages
			setNbreLocuteursDominants(nbreLocuteursDominants);
			float pourcentLocuteursDominants = (float) nbreLocuteursDominants
					/ (float) nbreLocuteurs * 100;
			setPourcentLocuteursDominants(pourcentLocuteursDominants);
			float pourcentMessagesLocuteursDominants = (float) nbreMessagesLocuteursDominants
					/ (float) nbreMessages * 100;
			setPourcentMessagesLocuteursDominants(pourcentMessagesLocuteursDominants);

			// Locuteurs dominants sauf premier
			// Setting % de locuteurs dominants sauf le premier + leur nombre de
			// messages + leur % de messages par rapport à tous les messages
			// sauf ceux du premier
			float pourcentLocuteursDominantsSaufPremier = (nbreLocuteursDominants - 1)
					/ (float) nbreLocuteurs * 100;
			setPourcentLocuteursDominantsSaufPremier(pourcentLocuteursDominantsSaufPremier);
			int nbreMessagesLocuteursDominantsSaufPremier = nbreMessagesLocuteursDominants
					- nbreMessagesLD;
			setNbreMessagesLocuteursDominantsSaufPremier(nbreMessagesLocuteursDominantsSaufPremier);
			float pourcentMessagesLocuteursDominantsSaufPremier = (float) nbreMessagesLocuteursDominantsSaufPremier
					/ (float) (nbreMessages - nbreMessagesLD) * 100;
			setPourcentMessagesLocuteursDominantsSaufPremier(pourcentMessagesLocuteursDominantsSaufPremier);
			System.out.println("nbre de locs doms = " + nbreLocuteursDominants);
			System.out.println("% de locs doms sauf 1er = "
					+ pourcentLocuteursDominantsSaufPremier);
			System.out.println("nbre et % de messages des locs doms sauf 1er = "
					+ nbreMessagesLocuteursDominantsSaufPremier + " | " + pourcentMessagesLocuteursDominantsSaufPremier);
		} else {
			JOptionPane.showMessageDialog(null,
					bundleOperationsListe.getString("txt_AucunLDTrouve"),
					bundleOperationsListe.getString("txt_CalculLD"),
					JOptionPane.ERROR_MESSAGE);
			// Petits locuteurs
			// Setting nombre de petits locuteurs + leur % par rapport au nombre
			// total de locuteurs
			int nbrePL = nbreLocuteurs;
			setNbrePL(nbrePL);
			float pourcentPL = ((float) nbrePL) / nbreLocuteurs * 100;
			setPourcentPL(pourcentPL);
		}

		activitesView.updateProgress();
		activitesView.appendTxtArea(bundleOperationsListe
				.getString("txt_CalculLD")
				+ " "
				+ bundleOperationsListe.getString("txt_Effectue") + "\n");
	}

	public float getMoyenneIntensite() {
		return moyenneIntensite;
	}

	public void setMoyenneIntensite(float moyenneIntensite) {
		this.moyenneIntensite = moyenneIntensite;
	}

	public float getPourcentLocuteursDominants3PremiersMois() {
		return pourcentLocuteursDominants3PremiersMois;
	}

	public void setPourcentLocuteursDominants3PremiersMois(
			float pourcentLocuteursDominants3PremiersMois) {
		this.pourcentLocuteursDominants3PremiersMois = pourcentLocuteursDominants3PremiersMois;
	}

	public float getDureeMoyenneParticipationLocuteursDominants() {
		return dureeMoyenneParticipationLocuteursDominants;
	}

	public void setDureeMoyenneParticipationLocuteursDominants(
			float dureeMoyenneParticipationLocuteursDominants) {
		this.dureeMoyenneParticipationLocuteursDominants = dureeMoyenneParticipationLocuteursDominants;
	}

	public float getEcartTypeParticipationLocuteursDominants() {
		return ecartTypeParticipationLocuteursDominants;
	}

	public void setEcartTypeParticipationLocuteursDominants(
			float ecartTypeParticipationLocuteursDominants) {
		this.ecartTypeParticipationLocuteursDominants = ecartTypeParticipationLocuteursDominants;
	}

	public float getPourcentParticipationLocuteursDominants() {
		return pourcentParticipationLocuteursDominants;
	}

	public void setPourcentParticipationLocuteursDominants(
			float pourcentParticipationLocuteursDominants) {
		this.pourcentParticipationLocuteursDominants = pourcentParticipationLocuteursDominants;
	}

	public float getPourcentPL() {
		return pourcentPL;
	}

	public void setPourcentPL(float pourcentPL) {
		this.pourcentPL = pourcentPL;
	}

	public float getPourcentMessagesLD() {
		return pourcentMessagesLD;
	}

	public void setPourcentMessagesLD(float pourcentMessagesLD) {
		this.pourcentMessagesLD = pourcentMessagesLD;
	}

	public float getPourcentLocuteursDominants() {
		return pourcentLocuteursDominants;
	}

	public void setPourcentLocuteursDominants(float pourcentLocuteursDominants) {
		this.pourcentLocuteursDominants = pourcentLocuteursDominants;
	}

	public float getPourcentMessagesLocuteursDominants() {
		return pourcentMessagesLocuteursDominants;
	}

	public void setPourcentMessagesLocuteursDominants(
			float pourcentMessagesLocuteursDominants) {
		this.pourcentMessagesLocuteursDominants = pourcentMessagesLocuteursDominants;
	}

	public float getPourcentLocuteursDominantsSaufPremier() {
		return pourcentLocuteursDominantsSaufPremier;
	}

	public void setPourcentLocuteursDominantsSaufPremier(
			float pourcentLocuteursDominantsSaufPremier) {
		this.pourcentLocuteursDominantsSaufPremier = pourcentLocuteursDominantsSaufPremier;
	}

	public float getPourcentMessagesLocuteursDominantsSaufPremier() {
		return pourcentMessagesLocuteursDominantsSaufPremier;
	}

	public void setPourcentMessagesLocuteursDominantsSaufPremier(
			float pourcentMessagesLocuteursDominantsSaufPremier) {
		this.pourcentMessagesLocuteursDominantsSaufPremier = pourcentMessagesLocuteursDominantsSaufPremier;
	}

	public int getNbreLocuteursDominants3PremiersMois() {
		return nbreLocuteursDominants3PremiersMois;
	}

	public void setNbreLocuteursDominants3PremiersMois(
			int nbreLocuteursDominants3PremiersMois) {
		this.nbreLocuteursDominants3PremiersMois = nbreLocuteursDominants3PremiersMois;
	}

	public int getNbrePL() {
		return nbrePL;
	}

	public void setNbrePL(int nbrePL) {
		this.nbrePL = nbrePL;
	}

	public int getNbreMessagesLocuteursDominants() {
		return nbreMessagesLocuteursDominants;
	}

	public void setNbreMessagesLocuteursDominants(int nbreMessagesLocuteursDominants) {
		this.nbreMessagesLocuteursDominants = nbreMessagesLocuteursDominants;
	}

	public int getNbreMessagesLD() {
		return nbreMessagesLD;
	}

	public void setNbreMessagesLD(int nbreMessagesLD) {
		this.nbreMessagesLD = nbreMessagesLD;
	}

	public int getNbreLocuteursDominants() {
		return nbreLocuteursDominants;
	}

	public void setNbreLocuteursDominants(int nbreLocuteursDominants) {
		this.nbreLocuteursDominants = nbreLocuteursDominants;
	}

	public int getNbreMessagesLocuteursDominantsSaufPremier() {
		return nbreMessagesLocuteursDominantsSaufPremier;
	}

	public void setNbreMessagesLocuteursDominantsSaufPremier(
			int nbreMessagesLocuteursDominantsSaufPremier) {
		this.nbreMessagesLocuteursDominantsSaufPremier = nbreMessagesLocuteursDominantsSaufPremier;
	}

	public String getLocuteurDominant() {
		return locuteurDominant;
	}

	public void setLocuteurDominant(String locuteurDominant) {
		this.locuteurDominant = locuteurDominant;
	}
	
	

}
