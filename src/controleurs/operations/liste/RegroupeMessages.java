package controleurs.operations.liste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections4.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.Days;

import comparators.MessageDateUsComparator;
import comparators.MessageNumeroComparator;
import comparators.MessageSujetTronqueComparator;
import controleurs.vuesabstraites.ProjetView;
import extra.CalculLevenshtein;
import modeles.MessageModel;

public class RegroupeMessages {

	private Map<String, MessageModel> mapIdMessages;
	private int typeParam, valParam, paramLevenshtein;
	private ResourceBundle bundleOperationsListe;
	private ProjetView activitesView;
	private int nbreMessagesRegroupes = 0;

	public RegroupeMessages(ProjetView activitesView, ResourceBundle bundleOperationsListe, Map<String, MessageModel> mapIdMessages, int typeParam, int valParam, int paramLevenshtein) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.mapIdMessages = mapIdMessages;
		this.typeParam = typeParam;
		this.valParam = valParam;
		this.paramLevenshtein = paramLevenshtein;
	}

	public void start() {
		ArrayList<MessageModel> listMessages = new ArrayList<MessageModel>(mapIdMessages.values());
		System.out.println("typeParam = " + typeParam);
		if (typeParam == 0 || typeParam == 1) {
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_RegroupeMessages") + " - " + bundleOperationsListe.getString("txt_Patientez"));
			// System.out.println("1 ok");
			activitesView.getProgressBar().setIndeterminate(true);
			activitesView.getProgressBar().setStringPainted(false);
			activitesView.updateProgress();
			ComparatorChain<MessageModel> chain = new ComparatorChain<MessageModel>();
			chain.addComparator(new MessageSujetTronqueComparator());
			if (typeParam == 0)
				chain.addComparator(new MessageDateUsComparator());
			else
				chain.addComparator(new MessageNumeroComparator());
			Collections.sort(listMessages, chain);
			activitesView.getProgressBar().setIndeterminate(false);
			activitesView.getProgressBar().setStringPainted(true);
			activitesView.resetProgress();
			activitesView.setStepProgress(listMessages.size());
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_RegroupeMessages"));
			for (int i = 1; i < listMessages.size(); i++) {
				MessageModel message = listMessages.get(i);
				String inReplyToRegroupeExistant = message.getInReplyToRegroupe();
				MessageModel messagePrecedent = listMessages.get(i - 1);
				// DISTANCE LEVENSHTEIN
				String sujetTronque = message.getSujetTronque();
				String sujetTronquePrecedent = messagePrecedent.getSujetTronque();
				CalculLevenshtein cl = new CalculLevenshtein();
				int distanceLevenshtein = cl.calcul(sujetTronquePrecedent, sujetTronque);

				// VALEUR ABSOLUE
				int valeurAbsolue;
				if (typeParam == 0) {
					DateTime date = new DateTime(message.getDateUS());
					DateTime datePrecedente = new DateTime(messagePrecedent.getDateUS());
					valeurAbsolue = Math.abs(Days.daysBetween(date, datePrecedente).getDays());
				} else {
					int numero = Integer.valueOf(message.getNumero());
					int numeroPrecedent = Integer.valueOf(messagePrecedent.getNumero());
					valeurAbsolue = Math.abs(numero - numeroPrecedent);
				}
				// SI LES SUJETS TRONQUES SONT EGAUX
				// System.out.println(sujetTronque + " comparé à " +
				// sujetTronquePrecedent + " : ");
				if (sujetTronque.equals(sujetTronquePrecedent)) {
					// System.out.println("\tSujets tronqués égaux : " +
					// sujetTronque
					// + " = " + sujetTronquePrecedent);
					// SI LA VALEUR DEMANDEE EST INFERIEURE AU PARAMETRE
					if (valeurAbsolue <= valParam) {
						// ALORS MEME CONVERSATION
						// System.out.println("\t\tla valeur absolue " +
						// valeurAbsolue
						// + " est <= à " + valParam + " => MEME CONVERSATION");
						if (inReplyToRegroupeExistant == null || inReplyToRegroupeExistant.isEmpty()) {
							String idMessagePrecedent = messagePrecedent.getIdentifiant();
							message.setInReplyToRegroupe(idMessagePrecedent);
							message.setIdConversation(0);
							nbreMessagesRegroupes++;
						}
					}
					// else
					// System.out.println("\t\tla valeur absolue " +
					// valeurAbsolue +
					// " est > à " + valParam +
					// " => CONVERSATIONS DIFFERENTES");
				} else { // SI LES SUJETS TRONQUES SONT DIFFERENTS
					// System.out.println("\tSujets tronqués différents : " +
					// sujetTronque
					// + " <> " + sujetTronquePrecedent);
					// SI CETTE DIFFERENCE EST INFERIEURE AU PARAMETRE DE
					// LEVENSHTEIN
					if (distanceLevenshtein <= paramLevenshtein) {
						// System.out.println("\t\tla distance " +
						// distanceLevenshtein
						// + " est <= au parametre " + paramLevenshtein);
						// SI LA VALEUR DEMANDEE EST INFERIEURE AU PARAMETRE
						if (valeurAbsolue <= valParam) {
							// ALORS MEME CONVERSATION
							// System.out.print("\t\tla valeur absolue " +
							// valeurAbsolue + " est <= à " + valParam +
							// " => MEME CONVERSATION");
							if (inReplyToRegroupeExistant == null || inReplyToRegroupeExistant.isEmpty()) {
								String idMessagePrecedent = messagePrecedent.getIdentifiant();
								message.setInReplyToRegroupe(idMessagePrecedent);
								message.setIdConversation(0);
								nbreMessagesRegroupes++;
							}
						}
						// else
						// System.out.println("\t\tla valeur absolue " +
						// valeurAbsolue
						// + " est <= à " + valParam +
						// " => CONVERSATIONS DIFFERENTES");
					}
					// else
					// System.out.println("\t\tla distance " +
					// distanceLevenshtein +
					// " est > au parametre " + paramLevenshtein +
					// " => CONVERSATIONS DIFFERENTES");
				}
				activitesView.updateProgress();
			}
		} else {
			activitesView.setStepProgress(listMessages.size());
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_SuppressionRegroupeMessages"));
			for (MessageModel message : listMessages) {
				String inReplyToRegroupeExistant = message.getInReplyToRegroupe();
				 System.out.println("RegroupeMessages - inReplyTo = "+message.getInReplyTo()+" | inReplyToRegroupeExistant = "+inReplyToRegroupeExistant);
				 
				if (inReplyToRegroupeExistant != null && !inReplyToRegroupeExistant.isEmpty()) {
					message.setIdConversation(0);
					message.setInReplyToRegroupe("");
					nbreMessagesRegroupes++;
				}
				activitesView.updateProgress();
			}
		}
	}

	public int getNbreMessagesRegroupes() {
		return nbreMessagesRegroupes;
	}

}
