package controleurs.operations.liste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.SortedSet;

import org.joda.time.DateTime;

import controleurs.vuesabstraites.ProjetView;
import modeles.LocuteurModel;
import modeles.MessageModel;

public class CalculSurLocuteurs {

	private ProjetView activitesView;
	private ResourceBundle bundleOperationsListe;
	private SortedSet<LocuteurModel> setLocuteurs;
	private ArrayList<MessageModel> listMessages;

	private int nbreLocuteurs, nbreLocuteursUnSeulMessage;
	private float nbreMoyenMessagesLocuteurMois;

	public CalculSurLocuteurs(ProjetView activitesView, ResourceBundle bundleOperationsListe, SortedSet<LocuteurModel> setLocuteurs, ArrayList<MessageModel> listMessages) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.setLocuteurs = setLocuteurs;
		this.listMessages = listMessages;
	}

	public void calcule() {
		// // NOMBRE DE LOCUTEURS
		activitesView.resetProgress();
		activitesView.setStepProgress(1);
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_CalculNbreLocuteurs"));
		setNbreLocuteurs(setLocuteurs.size());
		activitesView.updateProgress();
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_CalculNbreLocuteurs") + " " + bundleOperationsListe.getString("txt_Effectue") + "\n");
		activitesView.resetProgress();
		// // NOMBRE DE LOCUTEURS AYANT ENVOYER UN SEUL MESSAGE
		activitesView.setStepProgress(1);
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_CalculNbreLocuteursUnSeulMessage"));
		int i = 0;
		for (LocuteurModel locuteur : setLocuteurs) {
			if (locuteur.getNbreMessages() == 1)
				i++;			
		}
		setNbreLocuteursUnSeulMessage(i);
		activitesView.updateProgress();
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_CalculNbreLocuteursUnSeulMessage") + " " + bundleOperationsListe.getString("txt_Effectue") + "\n");
		activitesView.resetProgress();
		// MAP ANNEE
		Map<Integer, Map<Integer, Map<String, Integer>>> mapAnnee = new HashMap<Integer, Map<Integer, Map<String, Integer>>>();
		for (MessageModel message : listMessages) {
			String nomL = message.getExpediteur();
			DateTime dt = new DateTime(message.getDateUS());
			int year = dt.getYear();
			int month = dt.getMonthOfYear();
			if (!mapAnnee.containsKey(year)) {
				Map<Integer, Map<String, Integer>> mapMois = new HashMap<Integer, Map<String, Integer>>();
				Map<String, Integer> mapLocuteur = new HashMap<String, Integer>();
				mapLocuteur.put(nomL, 1);
				mapMois.put(month, mapLocuteur);
				mapAnnee.put(year, mapMois);
			} else {
				Map<Integer, Map<String, Integer>> mapMois = mapAnnee.get(year);
				if (!mapMois.containsKey(month)) {
					Map<String, Integer> mapLocuteur = new HashMap<String, Integer>();
					mapLocuteur.put(nomL, 1);
					mapMois.put(month, mapLocuteur);
				} else {
					Map<String, Integer> mapLocuteur = mapMois.get(month);
					if (!mapLocuteur.containsKey(nomL)) {
						mapLocuteur.put(nomL, 1);
					} else {
						int nbreMessages = mapLocuteur.get(nomL);
						mapLocuteur.put(nomL, nbreMessages + 1);
					}
				}
			}
		}
		// AFFICHAGE mapAnnee
		// for (Entry<Integer, Map<Integer, Map<String, Integer>>> e1 : mapAnnee
		// .entrySet()) {
		// int annee = e1.getKey();
		// System.out.println("Année " + annee);
		// Map<Integer, Map<String, Integer>> mapMois = e1.getValue();
		// for (Entry<Integer, Map<String, Integer>> e2 : mapMois.entrySet()) {
		// int month = e2.getKey();
		// System.out.println("== Mois " + month);
		// Map<String, Integer> mapLocuteur = e2.getValue();
		// for (Entry<String, Integer> e3 : mapLocuteur.entrySet()) {
		// System.out.println("==== " + e3.getKey() + " | "
		// + e3.getValue() + " messages");
		// }
		// }
		// }

		// // NOMBRE MOYEN DE MESSAGES PAR LOCUTEURS ET PAR MOIS
		activitesView.setStepProgress(mapAnnee.size());
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_CalculNbreMoyenMessagesLocuteursMois"));

		float totalMoyenne = 0;
		int nbreMois = 0;
		for (Entry<Integer, Map<Integer, Map<String, Integer>>> e1 : mapAnnee.entrySet()) {
			activitesView.updateProgress();
			// int annee = e1.getKey();
			// System.out.println("Annee " + annee);
			Map<Integer, Map<String, Integer>> mapMois = e1.getValue();
			nbreMois += mapMois.size();
			// System.out.println("nbreMois = " + nbreMois);
			for (Entry<Integer, Map<String, Integer>> e2 : mapMois.entrySet()) {
				// int month = e2.getKey();
				// System.out.println("== Mois " + month);
				Map<String, Integer> mapLocuteur = e2.getValue();
				int nbreLocuteurs = mapLocuteur.size();
				int nbreMessages = 0;
				for (Entry<String, Integer> e3 : mapLocuteur.entrySet()) {
					nbreMessages += e3.getValue();
				}
				float moyenne = (float) nbreMessages / (float) nbreLocuteurs;
				totalMoyenne += moyenne;
				// System.out.println("==== Moyenne = " + (float)nbreMessages +
				// "/" + (float)nbreLocuteurs+" = "+moyenne);
			}
		}
		// System.out.println(totalMoyenne + "/" + nbreMois + " = " +
		// totalMoyenne
		// / nbreMois);
		setNbreMoyenMessagesLocuteurMois(totalMoyenne / nbreMois);
		// System.out.println("CalculsSurLocuteurs : nbreMoyenMessagesLocuteurMois = "
		// + listeSelected.getNbreMoyenMessagesLocuteurMois());
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_CalculNbreMoyenMessagesLocuteursMois") + " " + bundleOperationsListe.getString("txt_Effectue") + "\n");
	}

	public int getNbreLocuteurs() {
		return nbreLocuteurs;
	}

	public void setNbreLocuteurs(int nbreLocuteurs) {
		this.nbreLocuteurs = nbreLocuteurs;
	}

	public int getNbreLocuteursUnSeulMessage() {
		return nbreLocuteursUnSeulMessage;
	}

	public void setNbreLocuteursUnSeulMessage(int nbreLocuteursUnSeulMessage) {
		this.nbreLocuteursUnSeulMessage = nbreLocuteursUnSeulMessage;
	}

	public float getNbreMoyenMessagesLocuteurMois() {
		return nbreMoyenMessagesLocuteurMois;
	}

	public void setNbreMoyenMessagesLocuteurMois(float nbreMoyenMessagesLocuteurMois) {
		this.nbreMoyenMessagesLocuteurMois = nbreMoyenMessagesLocuteurMois;
	}

}
