package controleurs.operations.liste;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.Map.Entry;

import controleurs.vuesabstraites.ProjetView;
import modeles.ConversationModel;
import modeles.LocuteurModel;

public class CalculSurConversations {

	private ProjetView activitesView;
	private ResourceBundle bundleOperationsListe;
	private SortedSet<ConversationModel> setConversations;
	private SortedSet<LocuteurModel> setLocuteurs;
	private int nbreMessages, nbreConversations;
	private float nbreMoyenMessagesConversation, nbreMoyenLocuteursDifferentsSujet;

	public CalculSurConversations(ProjetView activitesView, ResourceBundle bundleOperationsListe, SortedSet<ConversationModel> setConversations, int nbreMessages, SortedSet<LocuteurModel> setLocuteurs) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.setConversations = setConversations;
		this.nbreMessages = nbreMessages;
		this.setLocuteurs = setLocuteurs;
		System.out.println("CalculSurConversations - constructeur : nbreMessages = " + nbreMessages);
	}

	public void calcule() {
		// NOMBRE DE CONVERSATIONS
		setNbreConversations(setConversations.size());
		// // NOMBRE MOYEN DE MESSAGES PAR SUJET
		activitesView.resetProgress();
		activitesView.setStepProgress(1);
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_CalculNbreMoyenMessagesConv"));
		setNbreMoyenMessagesConversation((float) nbreMessages / (float) setConversations.size());
		activitesView.updateProgress();
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_CalculNbreMoyenMessagesConv") + " " + bundleOperationsListe.getString("txt_Effectue") + "\n");
		activitesView.resetProgress();
		// // NOMBRE MOYEN DE LOCUTEURS PAR SUJET
		activitesView.setStepProgress(1);
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_CalculNbreMoyenLocuteursConv"));
		int totalNbreLocuteursDifferents = 0;
		for (ConversationModel conversation : setConversations) {
			int nbreLocuteursDifferents = conversation.getNbreLocuteurs();
			totalNbreLocuteursDifferents += nbreLocuteursDifferents;
		}
		setNbreMoyenLocuteursDifferentsSujet((float) totalNbreLocuteursDifferents / (float) setConversations.size());
		activitesView.updateProgress();
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_CalculNbreMoyenLocuteursConv") + " " + bundleOperationsListe.getString("txt_Effectue") + "\n");
		// COMPLETION TABLEAU LOCUTEURS : NOMBRE DE CONVERSATIONS
		activitesView.setStepProgress(setConversations.size());
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_CalculNbreConvLocuteur"));
		for (ConversationModel conversation : setConversations) {
			activitesView.updateProgress();
			Map<String, Integer> mapLocuteursNbreMessages = conversation.getMapLocuteurNbreMessages();
			for (Entry<String, Integer> e : mapLocuteursNbreMessages.entrySet()) {
				String nomL = e.getKey();
				for (LocuteurModel locuteur : setLocuteurs)
					if (locuteur.getNom().equals(nomL))
						locuteur.setNbreConversations(locuteur.getNbreConversations() + 1);
			}
		}
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_CalculNbreConvLocuteur") + " " + bundleOperationsListe.getString("txt_Effectue") + "\n");
	}

	public int getNbreConversations() {
		return nbreConversations;
	}

	public void setNbreConversations(int nbreConversations) {
		this.nbreConversations = nbreConversations;
	}

	public float getNbreMoyenMessagesConversation() {
		return nbreMoyenMessagesConversation;
	}

	public void setNbreMoyenMessagesConversation(float nbreMoyenMessagesConversation) {
		this.nbreMoyenMessagesConversation = nbreMoyenMessagesConversation;
	}

	public float getNbreMoyenLocuteursDifferentsSujet() {
		return nbreMoyenLocuteursDifferentsSujet;
	}

	public void setNbreMoyenLocuteursDifferentsSujet(float nbreMoyenLocuteursDifferentsSujet) {
		this.nbreMoyenLocuteursDifferentsSujet = nbreMoyenLocuteursDifferentsSujet;
	}

}
