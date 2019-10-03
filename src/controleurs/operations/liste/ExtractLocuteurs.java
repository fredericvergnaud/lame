package controleurs.operations.liste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import modeles.LocuteurModel;
import modeles.MessageModel;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;

import comparators.LocuteurIntensiteComparator;
import comparators.MessageDateUsComparator;
import controleurs.vuesabstraites.ProjetView;

public class ExtractLocuteurs {

	private ProjetView activitesView;
	private ResourceBundle bundleOperationsListe;
	private ArrayList<MessageModel> listMessages;

	public ExtractLocuteurs(ProjetView activitesView, ResourceBundle bundleOperationsListe, ArrayList<MessageModel> listMessages) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.listMessages = listMessages;
	}

	public SortedSet<LocuteurModel> getSetLocuteurs() {
		SortedSet<LocuteurModel> newSetLocuteurs = new TreeSet<LocuteurModel>(new LocuteurIntensiteComparator());
		activitesView.resetProgress();
		activitesView.setStepProgress(listMessages.size());
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_ExtractLocuteurs"));
		Map<String, List<MessageModel>> mapL = new HashMap<String, List<MessageModel>>();
		for (MessageModel message : listMessages) {
			// MAPL
			String nomL = message.getExpediteur();
			if (!mapL.containsKey(nomL)) {
				List<MessageModel> listMessagesMap = new ArrayList<MessageModel>();
				listMessagesMap.add(message);
				mapL.put(nomL, listMessagesMap);
			} else {
				List<MessageModel> listMessagesMap = mapL.get(nomL);
				listMessagesMap.add(message);
				mapL.put(nomL, listMessagesMap);
			}
			activitesView.updateProgress();			
		}
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_ExtractLocuteurs") + " " + bundleOperationsListe.getString("txt_Effectue") + "\n");
		activitesView.resetProgress();
		activitesView.setStepProgress(mapL.size());
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_EnregistrementLocuteurs"));
		int idLocuteur = 1;
		for (Entry<String, List<MessageModel>> e : mapL.entrySet()) {
			List<MessageModel> listMessagesLocuteur = e.getValue();
			LocuteurModel newLocuteur = new LocuteurModel();
			// ID
			int idLocuteurMessage = listMessagesLocuteur.get(0).getIdLocuteur();
			if (idLocuteurMessage == 0) {
				idLocuteurMessage = idLocuteur;
				// on met à jour les messages
				for (MessageModel message : listMessagesLocuteur)
					message.setIdLocuteur(idLocuteurMessage);
				idLocuteur ++;
			}
			newLocuteur.setId(idLocuteurMessage);			
			// NOM
			String nomLocuteur = e.getKey();
			// System.out.println("Locuteur : " + nomLocuteur);
			newLocuteur.setNom(nomLocuteur);
			// NOMBRE DE MESSAGES
			int nbreMessagesLocuteurs = listMessagesLocuteur.size();
			newLocuteur.setNbreMessages(nbreMessagesLocuteurs);
			// DATE DEBUT - DATE FIN
			Comparator<MessageModel> byDateUs = new MessageDateUsComparator();
			Collections.sort(listMessagesLocuteur, byDateUs);
			Date dateDebut = listMessagesLocuteur.get(0).getDateUS();
			Date dateFin = listMessagesLocuteur.get(listMessagesLocuteur.size() - 1).getDateUS();
			// System.out.println("     date first message : " + dateDebut);
			// System.out.println("     date last message : " + dateFin);
			newLocuteur.setDateDebut(dateDebut);
			newLocuteur.setDateFin(dateFin);
			// INTENSITE - SELON DUREE DE PARTICIPATION EN MOIS
			int nbreMoisParticipation = Months.monthsBetween(new DateTime(dateDebut), new DateTime(dateFin)).getMonths();
			float intensite = (float) nbreMessagesLocuteurs / (float) nbreMoisParticipation;
			// System.out.println("     nbre mois de participation : "
			// + nbreMoisParticipation);
			// System.out.println("     nbre messages : " +
			// nbreMessagesLocuteurs);
			if (String.valueOf(intensite).equals("Infinity") == true)
				intensite = 1;
			// System.out.println("     intensite : " + intensite);
			newLocuteur.setIntensite(intensite);
			// DUREE DE PARTICIPATION EN JOURS
			int nbreJoursParticipation = Days.daysBetween(new DateTime(dateDebut), new DateTime(dateFin)).getDays() + 1;
			newLocuteur.setDuree(nbreJoursParticipation);
			// ATTRIBUTS FORUM
			newLocuteur.setfRole(listMessagesLocuteur.get(0).getfRoleLocuteur());
			newLocuteur.setfStatPosition(listMessagesLocuteur.get(0).getfStatPositionLocuteur());
			newLocuteur.setfStatActivity(listMessagesLocuteur.get(0).getfStatActivityLocuteur());
			newLocuteur.setfStars(listMessagesLocuteur.get(0).getfStarsLocuteur());
			newLocuteur.setfStatsNbrePosts(listMessagesLocuteur.get(0).getfStatNbrePostsLocuteur());
			newLocuteur.setfStatDateRegistrered(listMessagesLocuteur.get(0).getfStatDateRegistreredLocuteur());
			newLocuteur.setfStatEMail(listMessagesLocuteur.get(0).getfStatEMailLocuteur());
			newLocuteur.setfStatWebsite(listMessagesLocuteur.get(0).getfStatWebsiteLocuteur());
			newLocuteur.setfStatGender(listMessagesLocuteur.get(0).getfStatGenderLocuteur());
			newLocuteur.setfStatAge(listMessagesLocuteur.get(0).getfStatAgeLocuteur());
			newLocuteur.setfStatLocation(listMessagesLocuteur.get(0).getfStatLocationLocuteur());
			newLocuteur.setfStatSignature(listMessagesLocuteur.get(0).getfStatSignatureLocuteur());

			// AJOUT DANS LA LISTE
			newSetLocuteurs.add(newLocuteur);
			activitesView.updateProgress();
			
		}
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_EnregistrementLocuteurs") + " " + bundleOperationsListe.getString("txt_Accompli") + "\n");
		return newSetLocuteurs;
	}
}
