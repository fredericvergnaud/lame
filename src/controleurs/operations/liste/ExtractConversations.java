package controleurs.operations.liste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import modeles.ConversationModel;
import modeles.MessageModel;

import org.joda.time.DateTime;
import org.joda.time.Days;

import comparators.ConversationIdComparator;
import comparators.MessageDateUsComparator;
import controleurs.vuesabstraites.ProjetView;

public class ExtractConversations {

	private ProjetView activitesView;
	private ResourceBundle bundleOperationsListe;
	private Map<String, MessageModel> mapIdMessages;
	private SortedSet<ConversationModel> setConversations;

	public ExtractConversations(ProjetView activitesView, ResourceBundle bundleOperationsListe, Map<String, MessageModel> mapIdMessages) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.mapIdMessages = mapIdMessages;
	}

	public void extract() {
		// Pour messages de forums ou messages de BAL, même méthode : on se base
		// sur InReplyTo
		List<MessageModel> listMessages = new ArrayList<MessageModel>(mapIdMessages.values());
		MessageDateUsComparator comparator = new MessageDateUsComparator();
		Collections.sort(listMessages, comparator);
		Map<String, Integer> mapIdMessageIdConversation = new HashMap<String, Integer>();
		int numConversation = 1;
		activitesView.resetProgress();
		activitesView.setStepProgress(listMessages.size());
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_ExtractConversations"));
		for (MessageModel message : listMessages) {
			String idMessage = message.getIdentifiant();
			String inReplyTo = message.getInReplyTo();
//			System.out.println("ExtractConversations - inReplyTo = "+message.getInReplyTo());
//			System.out.println("ExtractConversations - inReplyToRegroupe = "+message.getInReplyToRegroupe());
			
			if (inReplyTo.equals(""))
				inReplyTo = message.getInReplyToRegroupe();
			int idConversation = message.getIdConversation();
			if (idConversation == 0) {
				idConversation = numConversation;
			}
			if (mapIdMessageIdConversation.containsKey(inReplyTo))
				mapIdMessageIdConversation.put(idMessage, mapIdMessageIdConversation.get(inReplyTo));
			else {
				mapIdMessageIdConversation.put(idMessage, idConversation);
				numConversation++;
			}
			activitesView.updateProgress();			
		}
//		for (Entry<String, Integer> entry : mapIdMessageIdConversation.entrySet())
//			System.out.println("ExtractConversations : "+entry.getKey()+" : "+entry.getValue());
		
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_ExtractConversations") + " " + bundleOperationsListe.getString("txt_Effectue") + "\n");
		activitesView.resetProgress();
		activitesView.setStepProgress(mapIdMessageIdConversation.size());
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_MiseAJourMessagesConversations"));
		// Mise à jour des messages et création des 2 maps
		// mapIdConversationListMessages et mapIdConversationSetNomLocuteur
		Map<Integer, ArrayList<MessageModel>> mapIdConversationListMessages = new HashMap<Integer, ArrayList<MessageModel>>();
		Map<Integer, HashSet<String>> mapIdConversationSetNomLocuteur = new HashMap<Integer, HashSet<String>>();
		for (Entry<String, Integer> entry : mapIdMessageIdConversation.entrySet()) {
			String idMessage = entry.getKey();
			MessageModel message = mapIdMessages.get(idMessage);
			int idConversation = entry.getValue();
			message.setIdConversation(idConversation);
			String expediteurMessage = message.getExpediteur();
			// System.out.println("Message de " + message.getExpediteur() +
			// " envoyé le " + message.getDateUS() + " => conversation n°" +
			// message.getIdConversation());
			if (!mapIdConversationListMessages.containsKey(idConversation)) {
				ArrayList<MessageModel> newListMessages = new ArrayList<MessageModel>();
				newListMessages.add(message);
				mapIdConversationListMessages.put(idConversation, newListMessages);
				HashSet<String> newSetExpediteurs = new HashSet<String>();
				newSetExpediteurs.add(expediteurMessage);
				mapIdConversationSetNomLocuteur.put(idConversation, newSetExpediteurs);
			} else {
				ArrayList<MessageModel> oldListMessages = mapIdConversationListMessages.get(idConversation);
				oldListMessages.add(message);
				mapIdConversationListMessages.put(idConversation, oldListMessages);
				HashSet<String> oldSetExpediteurs = mapIdConversationSetNomLocuteur.get(idConversation);
				oldSetExpediteurs.add(expediteurMessage);
				mapIdConversationSetNomLocuteur.put(idConversation, oldSetExpediteurs);
			}
			activitesView.updateProgress();
		}

		// Map<Integer, ArrayList<MessageModel>> mapIdConversationListMessages =
		// new HashMap<Integer, ArrayList<MessageModel>>();
		// Map<Integer, HashSet<String>> mapIdConversationSetNomLocuteur = new
		// HashMap<Integer, HashSet<String>>();
		//
		// for (MessageModel message : listMessages) {
		// activitesView.updateProgress();
		// int idConversation = message.getIdConversation();
		// //
		// System.out.println("CalculConversations - getSetConversations : idConversation = "
		// // + idConversation);
		// String nomLocuteur = message.getExpediteur();
		// if (!mapIdConversationListMessages.containsKey(idConversation)) {
		// ArrayList<MessageModel> newListMessages = new
		// ArrayList<MessageModel>();
		// newListMessages.add(message);
		// mapIdConversationListMessages.put(idConversation, newListMessages);
		// HashSet<String> newSetLocuteurs = new HashSet<String>();
		// newSetLocuteurs.add(nomLocuteur);
		// mapIdConversationSetNomLocuteur.put(idConversation, newSetLocuteurs);
		// } else {
		// ArrayList<MessageModel> oldListMessages =
		// mapIdConversationListMessages.get(idConversation);
		// oldListMessages.add(message);
		// mapIdConversationListMessages.put(idConversation, oldListMessages);
		// HashSet<String> oldSetLocuteurs =
		// mapIdConversationSetNomLocuteur.get(idConversation);
		// oldSetLocuteurs.add(nomLocuteur);
		// mapIdConversationSetNomLocuteur.put(idConversation, oldSetLocuteurs);
		// }
		// }
		// System.out.println("taille de mapSujetsMessageModel = " +
		// mapIdConversationListMessages.size());
		// // POUR AFFICHAGE
		// // for (Entry<String, HashSet<String>> entry :
		// // mapSujetSetNomLocuteur.entrySet()) {
		// // System.out.println("Conversation : " + entry.getKey());
		// // for (String nomLocuteur : entry.getValue()) {
		// // System.out.println("\tLocuteur : " + nomLocuteur);
		// // }
		// // }
		// for (Entry<Integer, ArrayList<MessageModel>> entry :
		// mapIdConversationListMessages.entrySet()) {
		// mapIdConversationListMessages.put(entry.getKey(), entry.getValue());
		// mapIdConversationSetNomLocuteur.put(entry.getKey(),
		// mapIdConversationSetNomLocuteur.get(entry.getKey()));
		// }
		//
		setConversations = new TreeSet<ConversationModel>(new ConversationIdComparator());
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_MiseAJourMessagesConversations") + " " + bundleOperationsListe.getString("txt_Effectue") + "\n");
		activitesView.resetProgress();
		activitesView.setStepProgress(mapIdConversationListMessages.size());
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_EnregistrementConversations"));
		
		// int totalNbreLocuteursDifferents = 0;
		// int i = 0;
		for (Entry<Integer, ArrayList<MessageModel>> idConversationListMessages : mapIdConversationListMessages.entrySet()) {
			ConversationModel newConversation = new ConversationModel();
			// ID
			int idConversation = idConversationListMessages.getKey();
			// System.out.println("Conversation n°" + idConversation);
			newConversation.setId(idConversation);
			List<MessageModel> listMessagesConversation = idConversationListMessages.getValue();
			// NOMBRE DE MESSAGES
			int nbreMessagesConversation = listMessagesConversation.size();
			newConversation.setNbreMessages(nbreMessagesConversation);
			// System.out.println("     nbre messages : "
			// + nbreMessagesConversation);
			// SUJET PREMIER MESSAGE - NUMERO PREMIER MESSAGE - DATE DEBUT -
			// LANCEUR - NOM FORUM + NOMBRE DE VUES FORUM
			Comparator<MessageModel> byDateUs = new MessageDateUsComparator();
			Collections.sort(listMessagesConversation, byDateUs);
			MessageModel firstMessage = listMessagesConversation.get(0);
			Date dateDebut = firstMessage.getDateUS();
			newConversation.setSujetPremierMessage(firstMessage.getSujet());
			newConversation.setNumeroPremierMessage(firstMessage.getNumero());
			newConversation.setDateDebut(dateDebut);
			newConversation.setLanceur(firstMessage.getExpediteur());
			newConversation.setfName(firstMessage.getFName());
			newConversation.setfNbreVues(firstMessage.getfNbreVuesTopic());
			// System.out.println("     sujet 1er message : "
			// + firstMessage.getSujet());
			// System.out.println("     numero 1er message : "
			// + firstMessage.getNumero());
			// System.out
			// .println("     lanceur : " + firstMessage.getExpediteur());

			// DATE FIN - DUREE EN JOURS
			MessageModel lastMessage;
			Date dateFin = null;
			if (listMessagesConversation.size() > 1) {
				lastMessage = listMessagesConversation.get(listMessagesConversation.size() - 1);
				dateFin = lastMessage.getDateUS();
			} else {
				lastMessage = firstMessage;
				dateFin = dateDebut;
			}
			int duree = Days.daysBetween(new DateTime(dateDebut), new DateTime(dateFin)).getDays() + 1;
			newConversation.setDateFin(dateFin);
			newConversation.setDuree(duree);
			// System.out.println("     date debut : " + dateDebut);
			// System.out.println("     date fin : " + dateFin);

			// LOCUTEURS : nombre
			newConversation.setNbreLocuteurs(mapIdConversationSetNomLocuteur.get(idConversation).size());
			// totalNbreLocuteursDifferents += mapConversationLocuteurs.get(
			// idConversation).size();
			// System.out.println("     nbre locuteurs : "
			// + mapConversations.get(idConversation).size());
			// APPARTENANCE DU MESSAGE A LA CONVERSATION + LOCUTEURS : nombre de
			// messages
			Map<String, Integer> mapLocuteursNbreMessages = new HashMap<String, Integer>();
			for (MessageModel message : listMessagesConversation) {
				message.setIdConversation(idConversation);
				String nomL = message.getExpediteur();
				if (!mapLocuteursNbreMessages.containsKey(nomL)) {
					int nbreMessages = 1;
					mapLocuteursNbreMessages.put(nomL, nbreMessages);
				} else {
					int nbreMessages = mapLocuteursNbreMessages.get(nomL);
					nbreMessages += 1;
					mapLocuteursNbreMessages.put(nomL, nbreMessages);
				}
			}
			newConversation.setMapLocuteurNbreMessages(mapLocuteursNbreMessages);

			// AJOUT DANS LA LISTE
			setConversations.add(newConversation);
			activitesView.updateProgress();			
		}

		System.out.println("taille de listConversation = " + setConversations.size());
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_EnregistrementConversations") + " " + bundleOperationsListe.getString("txt_Accompli") + "\n");

	}

	public SortedSet<ConversationModel> getSetConversations() {
		return setConversations;
	}
}
