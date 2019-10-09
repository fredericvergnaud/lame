package controleurs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import vue.NetworkGraphFrame;
import vue.dialog.DialogAnalyseMessages;
import vue.dialog.DialogRegroupeMessages;
import vue.dialog.DialogSplitListe;
import controleurs.operations.liste.CalculSurConversations;
import controleurs.operations.liste.CalculSurLocuteurs;
import controleurs.operations.liste.CalculSurMessages;
import controleurs.operations.liste.ExtractConversations;
import controleurs.operations.liste.ExtractLocuteurs;
import controleurs.operations.liste.ExportToCSV;
import controleurs.operations.liste.ExportToPDF;
import controleurs.operations.liste.GraphLocuteursXLocuteursInFils;
import controleurs.operations.liste.NettoyageLocuteurs;
import controleurs.operations.liste.NettoyageSujets;
import controleurs.operations.liste.RegroupeMessages;
import controleurs.operations.liste.ajoutmessages.AddMessages;
import controleurs.operations.liste.ajoutmessages.bal.AddMessagesFromBalLocale;
import controleurs.operations.liste.ajoutmessages.extractify.AddMessagesFromExtractify;
import controleurs.operations.liste.ajoutmessages.forum.AddMessagesForum;
import controleurs.operations.liste.analyse.AnalyseLocuteursDominants;
import controleurs.operations.liste.analyse.AnalyseSujetsCollectifs;
import controleurs.vuesabstraites.ListeView;
import controleurs.vuesabstraites.ProjetView;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import extra.SimpleSwingWorker;
import modeles.EdgeModel;
import modeles.ListeModel;
import modeles.LocuteurModel;
import modeles.MessageModel;
import modeles.VertexModel;

public class ListeController {
	private ListeView panelInfosListeView = null;
	private ListeView panelFilsListeView = null;
	private ListeView panelAnalyseListeView = null;
	private ListeModel listeSelected = null;
	private ListeView panelTabMessagesListeView = null;
	private ListeView panelTabConversationsListeView = null;
	private ListeView panelTabLocuteursListeView = null;
	private ProjetView activitesView = null;
	private ListeView toolBarView = null;
	private ProjetController projetController;
	protected static ResourceBundle bundleOperationsListe;
	private int newNbreMessagesExtraits, oldNbreMessages, newNbreMessages;
	private ArrayList<MessageModel> listMessages;

	public ListeController(ProjetController projetController, ListeModel listeSelected,
			ResourceBundle bundleOperationsListe, ListeView panelInfosListeView, ListeView panelFilsListeView,
			ListeView panelAnalyseListeView, ListeView panelTabMessagesListeView,
			ListeView panelTabConversationsListeView, ListeView panelTabLocuteursListeView, ProjetView activitesView,
			ListeView toolBarView) {
		this.projetController = projetController;
		this.listeSelected = listeSelected;
		this.panelInfosListeView = panelInfosListeView;
		this.panelFilsListeView = panelFilsListeView;
		this.panelAnalyseListeView = panelAnalyseListeView;
		this.panelTabMessagesListeView = panelTabMessagesListeView;
		this.panelTabConversationsListeView = panelTabConversationsListeView;
		this.panelTabLocuteursListeView = panelTabLocuteursListeView;
		ListeController.bundleOperationsListe = bundleOperationsListe;
		this.activitesView = activitesView;
		this.toolBarView = toolBarView;
		removeListenersFromModel();
		addListenersToModel();
		notifyListeSelected();
	}

	public ListeView getPanelTabMessagesListeView() {
		return panelTabMessagesListeView;
	}

	public ListeView getPanelTabLocuteursListeView() {
		return panelTabLocuteursListeView;
	}

	public ListeView getPanelTabConversationsListeView() {
		return panelTabConversationsListeView;
	}

	// // // // // // // // LISTENERS // // // // // // // // // // // // // //

	public void addListenersToModel() {
		listeSelected.addListeListener(panelInfosListeView);
		listeSelected.addListeListener(panelFilsListeView);
		listeSelected.addListeListener(panelAnalyseListeView);
		listeSelected.addListeListener(panelTabMessagesListeView);
		listeSelected.addListeListener(panelTabConversationsListeView);
		listeSelected.addListeListener(panelTabLocuteursListeView);
		listeSelected.addListeListener(toolBarView);
	}

	public void removeListenersFromModel() {
		listeSelected.removeListeListener(panelInfosListeView);
		listeSelected.removeListeListener(panelFilsListeView);
		listeSelected.removeListeListener(panelAnalyseListeView);
		listeSelected.removeListeListener(panelTabMessagesListeView);
		listeSelected.removeListeListener(panelTabConversationsListeView);
		listeSelected.removeListeListener(panelTabLocuteursListeView);
		listeSelected.removeListeListener(toolBarView);
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // NOTIFY SUR LISTE // // // // // // // // // // // //

	public void notifyListeSelected() {
		listeSelected.fireListeChanged();
		listeMessagesChanged();
	}

	public void notifyListeNameChanged(String newNom) {
		listeSelected.setNom(newNom);
	}

	public void notifyListeSplit() {
		new SwingWorker<Object, Object>() {
			int oldNbreListes = getProjetController().getProjetCharge().getNbreListes();

			@Override
			public Object doInBackground() {
				DialogSplitListe split = new DialogSplitListe(bundleOperationsListe, listeSelected.getSetLocuteurs());
				split.displayDialogParamSplit();
				String paramSplit1 = split.getParamSplit1();
				String paramSplit2 = split.getParamSplit2();
				System.out.println("ListeController - notifySplitListes : paramSplit1 = " + paramSplit1
						+ " | paramSplit2 = " + paramSplit2);
				if (paramSplit1 != null && paramSplit2 != null) {
					splitListe(paramSplit1, paramSplit2);
				}
				return null;
			}

			@Override
			public void done() {
				int newNbreListes = getProjetController().getProjetCharge().getNbreListes();
				if (newNbreListes > oldNbreListes)
					JOptionPane.showMessageDialog(null,
							bundleOperationsListe.getString("txt_ScissionListe") + " "
									+ bundleOperationsListe.getString("txt_ScissionSucces"),
							bundleOperationsListe.getString("txt_ScissionListe"), JOptionPane.INFORMATION_MESSAGE);

			}
		}.execute();

	}

	public void splitListe(String paramSplit1, String paramSplit2) {
		TreeMap<String, Map<String, MessageModel>> mapNew = new TreeMap<String, Map<String, MessageModel>>();
		if (paramSplit1.equals(bundleOperationsListe.getString("txt_ParamDate"))) {
			DateTimeFormatter fmtYear = DateTimeFormat.forPattern("yyyy");
			DateTimeFormatter fmtMonth = DateTimeFormat.forPattern("yyyy-MM");
			DateTimeFormatter fmtDay = DateTimeFormat.forPattern("yyyy-MM-dd");

			for (MessageModel message : listeSelected.getMapIdMessage().values()) {
				DateTime dt = new DateTime(message.getDateUS());
				String sDate;
				if (paramSplit2.equals(bundleOperationsListe.getString("txt_ParamSplitList_Annees")))
					sDate = dt.toString(fmtYear);
				else if (paramSplit2.equals(bundleOperationsListe.getString("txt_ParamSplitList_Mois")))
					sDate = dt.toString(fmtMonth);
				else
					sDate = dt.toString(fmtDay);
				Map<String, MessageModel> mapIdMessage;
				if (!mapNew.containsKey(sDate)) {
					mapIdMessage = new HashMap<String, MessageModel>();
					mapIdMessage.put(message.getIdentifiant(), message);
					mapNew.put(sDate, mapIdMessage);
				} else {
					mapIdMessage = mapNew.get(sDate);
					mapIdMessage.put(message.getIdentifiant(), message);
				}
			}
		} else {
			for (MessageModel message : listeSelected.getMapIdMessage().values()) {
				String nomLocuteur = message.getExpediteur();
				Map<String, MessageModel> mapIdMessage;
				if (nomLocuteur.equals(paramSplit2))
					if (!mapNew.containsKey(nomLocuteur)) {
						mapIdMessage = new HashMap<String, MessageModel>();
						mapIdMessage.put(message.getIdentifiant(), message);
						mapNew.put(nomLocuteur, mapIdMessage);
					} else {
						mapIdMessage = mapNew.get(nomLocuteur);
						mapIdMessage.put(message.getIdentifiant(), message);
					}
			}

		}
		// System.out.println("ListeController - listeScission : taille de mapNew = "
		// + mapNew.size());
		if (mapNew.size() > 0) {
			for (Entry<String, Map<String, MessageModel>> e1 : mapNew.entrySet()) {
				String newNomListe = null;
				if (paramSplit1.equals(bundleOperationsListe.getString("txt_ParamDate"))) {
					String sDate = e1.getKey();
					newNomListe = listeSelected.getNom() + "_" + sDate;
				} else {
					String nomLocuteur = e1.getKey();
					newNomListe = listeSelected.getNom() + "_" + nomLocuteur;
				}
				if (newNomListe != null) {
					projetController.getProjetCharge().addListe(newNomListe, e1.getValue());
					notifyExtractData();					
				}
			}
		}
		getProjetController().getProjetCharge().setNewCumul();
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // NOTIFY SUR PROJET // // // // // // // // // // //

	public void notifyMessageToShow(String identifiantMessageToShow) {
		// System.out
		// .println("ListeController - notifyMessageToShow : Identifiant message clique
		// = "
		// + identifiantToShow);
		panelFilsListeView.setIdentifiantMessageToShow(identifiantMessageToShow);
		projetController.notifyMessageToShowSelected();

	}

	public void notifyLocuteurToShow(String identifiantLocuteurToShow) {
		System.out.println(
				"ListeController - notifyLocuteurToShow : Identifiant locuteur clique = " + identifiantLocuteurToShow);
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // MESSAGES // // // // // // // // // // // // // //

	public void listeMessagesChanged() {
		listMessages = new ArrayList<MessageModel>(listeSelected.getMapIdMessage().values());

	}

	public void notifyAddMessages(final String repertoire) {

		new SimpleSwingWorker() {
			Map<String, MessageModel> mapIdMessages = null;
			AddMessages addMessages;
			int typeMessagesListeSelected = 0;

			@Override
			public Void doInBackground() {
				addMessages = new AddMessages(bundleOperationsListe);
				addMessages.displayDialog();
				typeMessagesListeSelected = addMessages.getTypeMessagesListeSelected();
				if (typeMessagesListeSelected == 1) {
					// Test pour savoir si forum unique dans la liste
					// => pour mettre à jour
					Map<String, MessageModel> existingMapIdMessages = activitesView.getProjetController()
							.getListeSelected().getMapIdMessage();
					Date dateFinListe = null;
					String nomForumEnCours = "";
					if (existingMapIdMessages.size() > 0) {
						Set<String> setNomsForumsEnCours = new HashSet<String>();
						Set<Date> setDatesMessages = new HashSet<Date>();
						for (Entry<String, MessageModel> entry : existingMapIdMessages.entrySet()) {
							MessageModel message = entry.getValue();
							String nomForum = message.getFName();
							Date dateMessage = message.getDateUS();
							setNomsForumsEnCours.add(nomForum);
							setDatesMessages.add(dateMessage);
						}
						ArrayList<String> listNomsForumsEnCours = new ArrayList<String>(setNomsForumsEnCours);
						ArrayList<Date> listDatesMessages = new ArrayList<Date>(setDatesMessages);
						Collections.sort(listDatesMessages);
						dateFinListe = listDatesMessages.get(listDatesMessages.size() - 1);
						if (listNomsForumsEnCours.size() == 1)
							nomForumEnCours = listNomsForumsEnCours.get(0);
					}
					AddMessagesForum addMessagesForum = new AddMessagesForum(bundleOperationsListe, activitesView,
							listeSelected.getMapIdMessage().size(), nomForumEnCours, dateFinListe);
					addMessagesForum.displayDialogConnexion();
					mapIdMessages = addMessagesForum.getNewMapIdMessages();
				} else if (typeMessagesListeSelected == 4) {
					AddMessagesFromExtractify addMessagesFromExtractify = new AddMessagesFromExtractify(
							bundleOperationsListe, activitesView, repertoire);
					addMessagesFromExtractify.extractJsonData();
					mapIdMessages = addMessagesFromExtractify.getNewMapIdMessages();
				} else if (typeMessagesListeSelected == 2) {
					AddMessagesFromBalLocale addMessagesFromBalLocale = new AddMessagesFromBalLocale(
							bundleOperationsListe, activitesView, repertoire);
					addMessagesFromBalLocale.displayDialog();
					mapIdMessages = addMessagesFromBalLocale.getNewMapIdMessages();
				}
				return null;
			}

			@Override
			public void done() {
				if (mapIdMessages != null) {
					if (typeMessagesListeSelected != 4) {
						// si pas extractify : on ajoute messages à la liste, on extrait les data et on lance l'analyse
						if (mapIdMessages.size() > 0) {
							System.out.println("ListeController - notifyAddMessages - done() : mapIdMessages.size() = "
									+ mapIdMessages.size());
							System.out.println("ListeController - notifyAddMessages : liste sélectionnée = "
									+ listeSelected.getNom());
							int oldNbreMessages = getNbreMessages();
							listeSelected.addMapIdMessages(mapIdMessages);
							int newNbreMessages = getNbreMessages();
							if (newNbreMessages != oldNbreMessages) {
								activitesView.appendTxtArea(mapIdMessages.size() + " "
										+ bundleOperationsListe.getString("txt_MessagesExtraits") + "\n");
								listeMessagesChanged();
								notifyExtractData();
								notifyAnalyseData();
							} else {
								JOptionPane.showMessageDialog(null,
										bundleOperationsListe.getString("txt_FichiersExistentDeja"),
										bundleOperationsListe.getString("txt_AjoutMessages"),
										JOptionPane.INFORMATION_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(null,
									bundleOperationsListe.getString("txt_ZeroMessageExtrait"),
									bundleOperationsListe.getString("txt_AjoutMessages"),
									JOptionPane.INFORMATION_MESSAGE);
							activitesView.setStepProgress(1);
							activitesView.updateProgress();
							activitesView.appendTxtArea(
									bundleOperationsListe.getString("txt_AucunMessagesAjoutesListe") + "\n");
						}
					}
				}
			}
		}.execute();
	}

	public void notifyExtractData() {
		notifyCalculSurMessages();
		notifyExtractLocuteurs();
		notifyExtractConversations();
		projetController.notifyListeAddedMessages();
	}

	public void notifyCalculSurMessages() {
		// System.out.println("ListeController - notifyCalculSurMessages :
		// listMessages.size = "
		// + listMessages.size());
		CalculSurMessages csm = new CalculSurMessages(activitesView, bundleOperationsListe, listMessages);
		csm.calculeSuivi();
		Date debut = csm.getDebut();
		Date fin = csm.getFin();
		if (debut != null && fin != null) {
			DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
			DateTime dtDebut = new DateTime(debut);
			DateTime dtFin = new DateTime(fin);
			Period period = new Period(dtDebut, dtFin, PeriodType.yearMonthDayTime());

			String dureeSuivi = fmt.print(dtDebut) + " > " + fmt.print(dtFin) + " " + "(" + period.getYears() + " "
					+ bundleOperationsListe.getString("txt_Annees") + " " + period.getMonths() + " "
					+ bundleOperationsListe.getString("txt_Mois") + " " + period.getDays() + " "
					+ bundleOperationsListe.getString("txt_Jours") + " " + period.getHours() + " "
					+ bundleOperationsListe.getString("txt_Heures") + ")";
			activitesView.updateProgress();
			activitesView.appendTxtArea(bundleOperationsListe.getString("txt_DureeSuiviCalculee") + "\n");
			listeSelected.setCalculSurMessages(debut, fin, dureeSuivi);
		}
		csm.calculeNumerotation();
		activitesView.updateProgress();
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_CalculNumerotationTerminee") + "\n");
	}

	public void notifyExtractLocuteurs() {
		System.out.println("ListeController - notifyExtractLocuteurs : listMessages.size = " + listMessages.size());
		ExtractLocuteurs extractLocuteurs = new ExtractLocuteurs(activitesView, bundleOperationsListe, listMessages);
		SortedSet<LocuteurModel> setLocuteurs = extractLocuteurs.getSetLocuteurs();
		if (setLocuteurs != null && setLocuteurs.size() != 0) {
			listeSelected.setSetLocuteurs(setLocuteurs);
			notifyCalculSurLocuteurs();
		}
	}

	public void notifyExtractConversations() {
		// System.out.println("ListeController - notifyExtractLocuteurs :
		// mapIdMessages.size = "
		// + listeSelected.getMapIdMessage().size());
		ExtractConversations extractConversations = new ExtractConversations(activitesView, bundleOperationsListe,
				listeSelected.getMapIdMessage());
		extractConversations.extract();
		if (extractConversations.getSetConversations() != null) {
			listeSelected.setSetConversations(extractConversations.getSetConversations());
			notifyCalculSurConversations();
		}
	}

	private void notifyCalculSurLocuteurs() {
		CalculSurLocuteurs csl = new CalculSurLocuteurs(activitesView, bundleOperationsListe,
				listeSelected.getSetLocuteurs(), listMessages);
		csl.calcule();
		int nbreLocuteurs = csl.getNbreLocuteurs();
		int nbreLocuteursUnSeulMessage = csl.getNbreLocuteursUnSeulMessage();
		float nbreMoyenMessagesLocuteurMois = csl.getNbreMoyenMessagesLocuteurMois();
		listeSelected.setCalculSurLocuteurs(nbreLocuteurs, nbreLocuteursUnSeulMessage, nbreMoyenMessagesLocuteurMois);
	}

	private void notifyCalculSurConversations() {
		CalculSurConversations csc = new CalculSurConversations(activitesView, bundleOperationsListe,
				listeSelected.getSetConversations(), listMessages.size(), listeSelected.getSetLocuteurs());
		csc.calcule();
		int nbreConversations = csc.getNbreConversations();
		float nbreMoyenMessagesConversation = csc.getNbreMoyenMessagesConversation();
		float nbreMoyenLocuteursDifferentsSujet = csc.getNbreMoyenLocuteursDifferentsSujet();
		listeSelected.setCalculSurConversations(nbreConversations, nbreMoyenMessagesConversation,
				nbreMoyenLocuteursDifferentsSujet);
	}

	public void notifyAddMapIdMessagesFromListes(List<ListeModel> listListesToMerge) {
		for (ListeModel listeToMerge : listListesToMerge) {
			System.out.println("taille de mapIdMessages à merger = " + listeToMerge.getMapIdMessage().size());
			listeSelected.addMapIdMessages(listeToMerge.getMapIdMessage());
		}
	}

	public void notifyAddMapIdMessages(Map<String, MessageModel> newMapIdMessages) {
		listeSelected.addMapIdMessages(newMapIdMessages);
	}

	public void notifyDeleteSelectedMessages(final List<String> listIdMessagesToDelete) {
		new SwingWorker<Object, Object>() {

			int nbreDeletedMessages = listIdMessagesToDelete.size();

			@Override
			public Object doInBackground() {
				activitesView.setLabelProgress(bundleOperationsListe.getString("txt_PatientezSuppressionMessages"));
				activitesView.getProgressBar().setIndeterminate(true);
				activitesView.getProgressBar().setStringPainted(false);
				listeSelected.deleteMessages(listIdMessagesToDelete);
				activitesView.setLabelProgress(bundleOperationsListe.getString("txt_SuppressionMessages"));
				activitesView.getProgressBar().setIndeterminate(false);
				activitesView.getProgressBar().setStringPainted(true);
				activitesView.setStepProgress(1);
				activitesView.updateProgress();
				activitesView.appendTxtArea(
						nbreDeletedMessages + " " + bundleOperationsListe.getString("txt_MessagesSupprimes") + "\n");
				return null;
			}

			@Override
			public void done() {
				JOptionPane.showMessageDialog(null,
						nbreDeletedMessages + " " + bundleOperationsListe.getString("txt_MessagesSupprimes"),
						bundleOperationsListe.getString("txt_SuppressionMessages"), JOptionPane.INFORMATION_MESSAGE);
				listeMessagesChanged();
				getProjetController().getProjetCharge().setNewCumul();
				if (listMessages.size() > 0) {
					notifyExtractData();
					notifyAnalyseData();
				}

			}
		}.execute();
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // // // REGROUPEMENT MESSAGES // // // // // // //

	public void notifyRegroupeMessages() {
		int paramJours = listeSelected.getParamJours();
		int paramMessages = listeSelected.getParamMessages();
		int paramLevenshtein = listeSelected.getParamLevenshtein();
		boolean paramNoRegroupementMessages = listeSelected.getParamNoRegroupementMessages();

		final DialogRegroupeMessages dialogRegroupeMessages = new DialogRegroupeMessages(bundleOperationsListe,
				paramJours, paramMessages, paramLevenshtein, paramNoRegroupementMessages);
		dialogRegroupeMessages.displayDialog();

		if (dialogRegroupeMessages.getStartRegroupement()) {
			listeSelected.setParamNoRegroupementMessages(dialogRegroupeMessages.getParamNoRegroupementMessages());
			new SwingWorker<Object, Object>() {
				RegroupeMessages regroupeMessages;
				int typeParam = 0;
				int valParam = 0;

				@Override
				public Object doInBackground() {
					int newParamJours, newParamMessages, newParamLevenshtein;
					String txtFinish1 = "", txtFinish2 = "", txtParam = "";
					if (!dialogRegroupeMessages.getParamNoRegroupementMessages()) {
						newParamJours = dialogRegroupeMessages.getParamJours();
						newParamMessages = dialogRegroupeMessages.getParamMessages();
						newParamLevenshtein = dialogRegroupeMessages.getParamLevenshtein();
						// Type parametre
						// type 0 : paramètre JOURS
						// type 1 : paramètre MESSAGES
						// type 2 : suppression regroupement
						String sType;
						if (newParamJours != -1) {
							typeParam = 0;
							valParam = newParamJours;
							sType = bundleOperationsListe.getString("txt_ParamTypeJours");
						} else {
							typeParam = 1;
							valParam = newParamMessages;
							sType = bundleOperationsListe.getString("txt_ParamTypeMessages");
						}
						txtParam = valParam + " " + sType + " " + bundleOperationsListe.getString("txt_Entre2Messages");
						txtFinish1 = bundleOperationsListe.getString("txt_RegroupeMessages");
						txtFinish2 = bundleOperationsListe.getString("txt_MessagesRegroupes");
					} else {
						typeParam = 2;
						newParamJours = 30;
						newParamMessages = -1;
						newParamLevenshtein = 1;
						// System.out.println("SUPPRESSION DU REGROUPEMENT");
						txtParam = bundleOperationsListe.getString("txt_Entre2MessagesAucun");
						txtFinish1 = bundleOperationsListe.getString("txt_SuppressionRegroupeMessages");
						txtFinish2 = bundleOperationsListe.getString("txt_MessagesRegroupesRetires");
					}
					regroupeMessages = new RegroupeMessages(activitesView, bundleOperationsListe,
							listeSelected.getMapIdMessage(), typeParam, valParam, newParamLevenshtein);
					regroupeMessages.start();
					listeSelected.setParametresRegroupement(newParamJours, newParamMessages, newParamLevenshtein);
					listeSelected.setTypeParamRegroupementMessages(typeParam);
					listeSelected.setsParamRegroupementMessages(txtParam);
					activitesView.appendTxtArea(txtFinish1 + " " + bundleOperationsListe.getString("txt_Accompli")
							+ " : " + regroupeMessages.getNbreMessagesRegroupes() + " " + txtFinish2 + "\n");
					notifyExtractData();
					return null;
				}

				@Override
				public void done() {
					listeSelected.initBeforeStat();
					projetController.notifyListeAddedMessages();
					notifyAnalyseData();
				}
			}.execute();
		}
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // // // ANALYSE // // // // // // // // // // // //

	public void notifyAnalyseData() {
		notifyCalculSurMessages();
		float paramLocuteursDominants = listeSelected.getParamLocuteursDominants();
		float paramSujetsCollectifs = listeSelected.getParamSujetsCollectifs();
		boolean isLocuteursSC = listeSelected.getParamLocuteursSC();

		DialogAnalyseMessages dialogAnalyseMessages = new DialogAnalyseMessages(bundleOperationsListe,
				paramLocuteursDominants, paramSujetsCollectifs, isLocuteursSC);
		dialogAnalyseMessages.displayDialog();
		if (dialogAnalyseMessages.startAnalyse() == true) {
			float newParamLocuteursDominants = dialogAnalyseMessages.getParamLocuteursDominants();
			float newParamSujetsCollectifs = dialogAnalyseMessages.getParamSujetsCollectifs();
			boolean newParamLocuteursSC = dialogAnalyseMessages.getParamLocuteursSC();
			listeSelected.setParametresAnalyse(newParamLocuteursDominants, newParamSujetsCollectifs,
					newParamLocuteursSC);
			new SwingWorker<Object, Object>() {
				@Override
				public Object doInBackground() {
					notifyStartListeAnalyse();
					return null;
				}

				@Override
				public void done() {
					getProjetController().notifyListeAnalyzed();
				}
			}.execute();
		}
	}

	public void notifyStartListeAnalyse() {
		listeSelected.initBeforeStat();
		notifyAnalyseLocuteursDominants();
		notifyAnalyseSujetsCollectifs();
	}

	private void notifyAnalyseLocuteursDominants() {
		AnalyseLocuteursDominants ald = new AnalyseLocuteursDominants(activitesView, bundleOperationsListe,
				listeSelected.getSetLocuteurs(), listeSelected.getDebut(), listeSelected.getFin(),
				listeSelected.getParamLocuteursDominants(), listeSelected.getNbreMessages());
		ald.analyse();
		float moyenneIntensite = ald.getMoyenneIntensite();
		float pourcentLocuteursDominants3PremiersMois = ald.getPourcentLocuteursDominants3PremiersMois();
		float dureeMoyenneParticipationLocuteursDominants = ald.getDureeMoyenneParticipationLocuteursDominants();
		float ecartTypeParticipationLocuteursDominants = ald.getEcartTypeParticipationLocuteursDominants();
		float pourcentParticipationLocuteursDominants = ald.getPourcentParticipationLocuteursDominants();
		float pourcentPL = ald.getPourcentPL();
		float pourcentMessagesLD = ald.getPourcentMessagesLD();
		float pourcentLocuteursDominants = ald.getPourcentLocuteursDominants();
		float pourcentMessagesLocuteursDominants = ald.getPourcentMessagesLocuteursDominants();
		float pourcentLocuteursDominantsSaufPremier = ald.getPourcentLocuteursDominantsSaufPremier();
		float pourcentMessagesLocuteursDominantsSaufPremier = ald.getPourcentMessagesLocuteursDominantsSaufPremier();
		int nbreLocuteursDominants3PremiersMois = ald.getNbreLocuteursDominants3PremiersMois();
		int nbrePL = ald.getNbrePL();
		int nbreMessagesLocuteursDominants = ald.getNbreMessagesLocuteursDominants();
		int nbreMessagesLD = ald.getNbreMessagesLD();
		int nbreLocuteursDominants = ald.getNbreLocuteursDominants();
		int nbreMessagesLocuteursDominantsSaufPremier = ald.getNbreMessagesLocuteursDominantsSaufPremier();
		String locuteurDominant = ald.getLocuteurDominant();

		listeSelected.setCalculLocuteursDominants(moyenneIntensite, pourcentLocuteursDominants3PremiersMois,
				dureeMoyenneParticipationLocuteursDominants, ecartTypeParticipationLocuteursDominants,
				pourcentParticipationLocuteursDominants, pourcentPL, pourcentMessagesLD, pourcentLocuteursDominants,
				pourcentMessagesLocuteursDominants, pourcentLocuteursDominantsSaufPremier,
				pourcentMessagesLocuteursDominantsSaufPremier, nbreLocuteursDominants3PremiersMois, nbrePL,
				nbreMessagesLocuteursDominants, nbreMessagesLD, nbreLocuteursDominants,
				nbreMessagesLocuteursDominantsSaufPremier, locuteurDominant);
	}

	private void notifyAnalyseSujetsCollectifs() {
		AnalyseSujetsCollectifs asc = new AnalyseSujetsCollectifs(activitesView, bundleOperationsListe,
				listeSelected.getNbreMoyenMessagesConversation(), listeSelected.getParamSujetsCollectifs(),
				listeSelected.getNbreMoyenLocuteursDifferentsSujet(), listeSelected.getNbreMessages(),
				listeSelected.getSetConversations(), listeSelected.getParamLocuteursSC(),
				listeSelected.getSetLocuteurs(), listeSelected.getLocuteurDominant(),
				listeSelected.getNbreLocuteursDominants());
		asc.analyse();
		int nbreSC = asc.getNbreSC();
		int nbreMessagesSC = asc.getNbreMessagesSC();
		int nbreMessagesLocuteursDominantsSC = asc.getNbreMessagesLocuteursDominantsSC();
		int nbreMessagesLDSC = asc.getNbreMessagesLDSC();
		int nbreMessagesLocuteursDominantsSaufPremierSC = asc.getNbreMessagesLocuteursDominantsSaufPremierSC();
		int nbrePLSC = asc.getNbrePLSC();
		int nbreLocuteursDominantsSC = asc.getNbreLocuteursDominantsSC();
		int nbreMessagesPLSC = asc.getNbreMessagesPLSC();
		int nbreLanceursSC = asc.getNbreLanceursSC();
		int nbreLocuteursDominantsLanceursSC = asc.getNbreLocuteursDominantsLanceursSC();
		int nbreSCLancesLD = asc.getNbreSCLancesLD();
		int nbreSCLancesLocuteursDominantsSaufPremier = asc.getNbreSCLancesLocuteursDominantsSaufPremier();
		int nbreSCLancesPL = asc.getNbreSCLancesPL();
		float pourcentSC = asc.getPourcentSC();
		float pourcentMessagesSC = asc.getPourcentMessagesSC();
		float pourcentMessagesLocuteursDominantsSC = asc.getPourcentMessagesLocuteursDominantsSC();
		float pourcentMessagesLDSC = asc.getPourcentMessagesLDSC();
		float pourcentMessagesLocuteursDominantsSaufPremierSC = asc
				.getPourcentMessagesLocuteursDominantsSaufPremierSC();
		float pourcentPLSC = asc.getPourcentPLSC();
		float pourcentMessagesPLSC = asc.getPourcentMessagesPLSC();
		float pourcentLanceursSC = asc.getPourcentLanceursSC();
		float pourcentLocuteursDominantsLanceursSC1 = asc.getPourcentLocuteursDominantsLanceursSC1();
		float pourcentLocuteursDominantsLanceursSC2 = asc.getPourcentLocuteursDominantsLanceursSC2();
		float pourcentSCLancesLD = asc.getPourcentSCLancesLD();
		float pourcentSCLancesLocuteursDominantsSaufPremier = asc.getPourcentSCLancesLocuteursDominantsSaufPremier();
		float pourcentSCLancesPL = asc.getPourcentSCLancesPL();
		float pourcentLocuteursDominantsSC = asc.getPourcentLocuteursDominantsSC();
		listeSelected.setCalculSujetsCollectifs(nbreSC, nbreMessagesSC, nbreMessagesLocuteursDominantsSC,
				nbreMessagesLDSC, nbreMessagesLocuteursDominantsSaufPremierSC, nbrePLSC, nbreMessagesPLSC,
				nbreLanceursSC, nbreLocuteursDominantsLanceursSC, nbreSCLancesLD,
				nbreSCLancesLocuteursDominantsSaufPremier, nbreSCLancesPL, pourcentSC, pourcentMessagesSC,
				pourcentMessagesLocuteursDominantsSC, pourcentMessagesLDSC,
				pourcentMessagesLocuteursDominantsSaufPremierSC, pourcentPLSC, pourcentMessagesPLSC, pourcentLanceursSC,
				pourcentLocuteursDominantsLanceursSC1, pourcentLocuteursDominantsLanceursSC2, pourcentSCLancesLD,
				pourcentSCLancesLocuteursDominantsSaufPremier, pourcentSCLancesPL, nbreLocuteursDominantsSC,
				pourcentLocuteursDominantsSC);
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // // // EXPORT // // // // // // // // // // // // //

	public void notifyExportToCSV() {
		ExportToCSV e2csv = new ExportToCSV(activitesView, bundleOperationsListe, getProjetController().getRepertoire(),
				listeSelected.getSetLocuteurs(), listMessages, listeSelected.getSetConversations());
		e2csv.displayDialogExportToCsv();
	}

	public void notifyExportLocuteursToPdf() {
		ExportToPDF e2pdf = new ExportToPDF(activitesView, bundleOperationsListe, getProjetController().getNomProjet(),
				getProjetController().getRepertoire(), listeSelected.getNom(), listeSelected.getSetLocuteurs(),
				listeSelected.getSetConversations());
		String locuteursTxtToPdf = panelTabLocuteursListeView.getLocuteursTxtToPdf();
		e2pdf.exportLocuteursToPdf(locuteursTxtToPdf);
	}

	public void notifyExportConversationsToPdf() {
		ExportToPDF e2pdf = new ExportToPDF(activitesView, bundleOperationsListe, getProjetController().getNomProjet(),
				getProjetController().getRepertoire(), listeSelected.getNom(), listeSelected.getSetLocuteurs(),
				listeSelected.getSetConversations());
		String conversationsTxtToPdf = panelTabConversationsListeView.getConversationsTxtToPdf();
		e2pdf.exportConversationsToPdf(conversationsTxtToPdf);
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // // // NETTOYAGE // // // // // // // // // // // //

	public void notifyNettoyage(String typeNettoyage) {
		int nbreModifs = 0;
		if (typeNettoyage.equals("locuteursAuto")) {
			NettoyageLocuteurs nl = new NettoyageLocuteurs(activitesView, bundleOperationsListe, listMessages);
			nl.nettoyageAutoLocuteurs();
			nbreModifs = nl.getNbreModifs();
		} else if (typeNettoyage.equals("locuteursManuel")) {
			NettoyageLocuteurs nl = new NettoyageLocuteurs(activitesView, bundleOperationsListe, listMessages);
			nl.nettoyageManuelLocuteurs();
			nbreModifs = nl.getNbreModifs();
		} else if (typeNettoyage.equals("locuteursFichier")) {
			NettoyageLocuteurs nl = new NettoyageLocuteurs(activitesView, bundleOperationsListe, listMessages);
			nl.importNettoyageLocuteurs();
			nbreModifs = nl.getNbreModifs();
		} else if (typeNettoyage.equals("sujetsAuto")) {
			NettoyageSujets ns = new NettoyageSujets(activitesView, bundleOperationsListe, listMessages);
			ns.nettoyageAutoSujets();
			nbreModifs = ns.getNbreModifs();
		} else if (typeNettoyage.equals("sujetsManuel")) {
			NettoyageSujets ns = new NettoyageSujets(activitesView, bundleOperationsListe, listMessages);
			ns.nettoyageManuelSujets();
			nbreModifs = ns.getNbreModifs();
		}

		if (nbreModifs > 0) {
			listeSelected.initAfterNewMapIdMessages();
			String txtDiag = "";
			if (typeNettoyage.equals("locuteursAuto") || typeNettoyage.equals("locuteursManuel")
					|| typeNettoyage.equals("locuteursFichier"))
				txtDiag = bundleOperationsListe.getString("txt_NettoyageLocuteurs");
			else
				txtDiag = bundleOperationsListe.getString("txt_NettoyageSujets");
			JOptionPane.showMessageDialog(null,
					nbreModifs + " " + bundleOperationsListe.getString("txt_ModificationsEffectuees"), txtDiag,
					JOptionPane.INFORMATION_MESSAGE);
			activitesView.setLabelProgress(txtDiag);
			activitesView.appendTxtArea(txtDiag + " " + bundleOperationsListe.getString("txt_Accompli") + "\n");
			notifyExtractData();
			getProjetController().notifyListeCleaned();
			activitesView.getProgressBar().setIndeterminate(false);
			activitesView.getProgressBar().setStringPainted(true);
			notifyAnalyseData();
		} else {
			activitesView.setLabelProgress("");
			activitesView.resetProgress();
			activitesView.getProgressBar().setIndeterminate(false);
			activitesView.getProgressBar().setStringPainted(true);
		}
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // // // CARTOGRAPHIE // // // // // // // // // // //

	// public void notifyCartographie() {
	// Leximappe leximappe = new
	// Leximappe(activitesView,bundleOperationsListe,listMessages);
	// leximappe.indexWordsConversation();
	// }

	// public void notifyCartographie() {
	// GraphLocuteursXLocuteursInConversations
	// graphLocuteursXLocuteursInConversations = new
	// GraphLocuteursXLocuteursInConversations(
	// listeSelected.getSetConversations(),
	// listeSelected.getSetLocuteurs());
	// SparseGraph<VertexModel, EdgeModel> graph =
	// graphLocuteursXLocuteursInConversations
	// .getGraph();
	// if (graph != null) {
	// NetworkGraphFrame networkGraphFrame = new NetworkGraphFrame(
	// activitesView, bundleOperationsListe,
	// "Liens entre locuteurs selon conversations communes",
	// getProjetController().getRepertoire(), graph,
	// graphLocuteursXLocuteursInConversations.getMapEdgeFrequence());
	// networkGraphFrame.setVisible(true);
	// }
	// }

	public void notifyCartographie() {
		GraphLocuteursXLocuteursInFils graphLocuteursXLocuteursInFils = new GraphLocuteursXLocuteursInFils(
				listeSelected.getMapIdMessage(), listeSelected.getSetLocuteurs());
		graphLocuteursXLocuteursInFils.createGraphs();
		// DirectedSparseGraph<VertexModel, EdgeModel> directedGraph =
		// graphLocuteursXLocuteursInFils
		// .getDirectedGraph();
		UndirectedSparseGraph<VertexModel, EdgeModel> undirectedGraph = graphLocuteursXLocuteursInFils
				.getUndirectedGraph();
		if (
		// directedGraph != null &&
		undirectedGraph != null) {
			NetworkGraphFrame networkGraphFrame = new NetworkGraphFrame(activitesView, bundleOperationsListe,
					bundleOperationsListe.getString("txt_NetworkFrame"), getProjetController().getRepertoire(),
					// directedGraph,
					undirectedGraph);
			networkGraphFrame.setVisible(true);
		}
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // // // GETTER // // // // // // // // // // // // //

	// public ExtractionMessagesForum getExtractionMessagesForum() {
	// return extractMessagesForum;
	// }

	public int getNewNbreMessages() {
		return newNbreMessages;
	}

	public int getOldNbreMessages() {
		return oldNbreMessages;
	}

	// public Map<String, MessageModel> getNewMapIdMessages() {
	// return newMapIdMessages;
	// }

	public int getNewNbreMessagesExtraits() {
		return newNbreMessagesExtraits;
	}

	public ProjetController getProjetController() {
		return projetController;
	}

	public ListeModel getListeSelected() {
		return listeSelected;
	}

	public ProjetView getActivitesView() {
		return activitesView;
	}

	public int getNbreMessages() {
		return getListeSelected().getNbreMessages();
	}

	public String getNom() {
		return getListeSelected().getNom();
	}

	public Set<LocuteurModel> getSetLocuteurs() {
		return listeSelected.getSetLocuteurs();
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // // // SETTER // // // // // // // // // // // // //

	public void setNewNbreMessages(int newNbreMessages) {
		this.newNbreMessages = newNbreMessages;
	}

	public void setOldNbreMessages(int oldNbreMessages) {
		this.oldNbreMessages = oldNbreMessages;
	}

	// public void setNewMapIdMessages(Map<String, MessageModel>
	// newMapIdMessages) {
	// this.newMapIdMessages = newMapIdMessages;
	// }

	public void setNewNbreMessagesExtraits(int newNbreMessagesExtraits) {
		this.newNbreMessagesExtraits = newNbreMessagesExtraits;
	}

	public ResourceBundle getBundleOperationsListe() {
		return bundleOperationsListe;
	}

	public void setBundleOperationsListe(ResourceBundle bundleOperationsListe) {
		ListeController.bundleOperationsListe = bundleOperationsListe;
	}

	public void setPanelInfosListeView(ListeView panelInfosListeView) {
		this.panelInfosListeView = panelInfosListeView;
	}

	public void setPanelFilsListeView(ListeView panelFilsListeView) {
		this.panelFilsListeView = panelFilsListeView;
	}

	public void setPanelTabMessagesListeView(ListeView panelTabMessagesListeView) {
		this.panelTabMessagesListeView = panelTabMessagesListeView;
	}

	public void setPanelTabConversationsListeView(ListeView panelTabConversationsListeView) {
		this.panelTabConversationsListeView = panelTabConversationsListeView;
	}

	public void setPanelTabLocuteursListeView(ListeView panelTabLocuteursListeView) {
		this.panelTabLocuteursListeView = panelTabLocuteursListeView;
	}

	public void setActivitesView(ProjetView activitesView) {
		this.activitesView = activitesView;
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

}