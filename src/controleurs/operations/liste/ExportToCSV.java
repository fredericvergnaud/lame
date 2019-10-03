package controleurs.operations.liste;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import modeles.ConversationModel;
import modeles.LocuteurModel;
import modeles.MessageModel;
import vue.dialog.DialogPanelExportToCsv;
import vue.dialog.DialogPanelExportToCsvOptions;
import comparators.MessageNumeroComparator;
import controleurs.vuesabstraites.ProjetView;
import extra.CustomJFileChooser;
import extra.predicates.ConversationPredicates;
import extra.predicates.LocuteurPredicates;
import extra.predicates.MessagePredicates;
import extra.predicates.Predicate;

public class ExportToCSV {

	private DialogPanelExportToCsv exportToCsvPanel;
	private String repertoireProjet;
	private ProjetView activitesView;
	private ResourceBundle bundleOperationsListe;
	private SortedSet<LocuteurModel> setLocuteurs;
	private ArrayList<MessageModel> listMessages;
	private SortedSet<ConversationModel> setConversations;
	// Formats
	private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private String charset = "ISO-8859-1";

	public ExportToCSV(ProjetView activitesView, ResourceBundle bundleOperationsListe, String repertoireProjet,
			SortedSet<LocuteurModel> setLocuteurs, ArrayList<MessageModel> listMessages,
			SortedSet<ConversationModel> setConversations) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.repertoireProjet = repertoireProjet;
		this.setLocuteurs = setLocuteurs;
		this.listMessages = listMessages;
		this.setConversations = setConversations;
		// System.out.println("ExportToCSV - Constructeur : lancé !");
	}

	public void displayDialogExportToCsv() {
		exportToCsvPanel = new DialogPanelExportToCsv(setLocuteurs, bundleOperationsListe);
		int result = JOptionPane.showOptionDialog(null, exportToCsvPanel,
				bundleOperationsListe.getString("txt_ListeCsv"), JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			// System.out.println("ok");
			exportSelonCriteres();
		}
	}

	private void exportSelonCriteres() {
		// System.out.println("exportMessagesCritered lance");
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_ListeCsv") + " - "
				+ bundleOperationsListe.getString("txt_Patientez"));
		// System.out.println("1 ok");
		activitesView.getProgressBar().setIndeterminate(true);
		activitesView.getProgressBar().setStringPainted(false);
		activitesView.updateProgress();
		List<MessageModel> tempListMToExport = new ArrayList<MessageModel>();
		List<LocuteurModel> tempListLToExport = new ArrayList<LocuteurModel>();
		List<ConversationModel> tempListCToExport = new ArrayList<ConversationModel>();
		List<MessageModel> listMToExport = new ArrayList<MessageModel>();
		Set<LocuteurModel> listLToExport = new HashSet<LocuteurModel>();
		Set<ConversationModel> listCToExport = new HashSet<ConversationModel>();
		boolean isExporterToutIsSelected = exportToCsvPanel.getExporterToutIsSelected();
		System.out.println("isExporterToutIsSelected = " + isExporterToutIsSelected);
		if (isExporterToutIsSelected) {

		} else {
			// RECUPERATION DES CRITERES D'EXPORTATION DU PANEL EXPORT
			// + FILTRES (PREDICATES)
			List<Predicate<MessageModel>> messagesPredicates = new ArrayList<Predicate<MessageModel>>();
			List<Predicate<ConversationModel>> conversationPredicates = new ArrayList<Predicate<ConversationModel>>();
			List<Predicate<LocuteurModel>> locuteurPredicates = new ArrayList<Predicate<LocuteurModel>>();
			// Liste de locuteurs
			String locuteursSel = exportToCsvPanel.getTxtALocuteursSelectionnes().getText();
			if (!locuteursSel.equals("")) {
				String[] tabLocuteursSelectionnes = null;
				if (locuteursSel.indexOf(";") != -1)
					tabLocuteursSelectionnes = locuteursSel.replace("\n", "").split(";");
				else {
					tabLocuteursSelectionnes = new String[1];
					tabLocuteursSelectionnes[0] = locuteursSel;
				}
				if (tabLocuteursSelectionnes.length > 0)
					locuteurPredicates.add(LocuteurPredicates.findAuteur(tabLocuteursSelectionnes));
			}
			// Date 1 et 2
			String sDate1Sel = exportToCsvPanel.getTxtFDate1().getText();
			String sDate2Sel = exportToCsvPanel.getTxtFDate2().getText();
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date1Sel = null;
			Date date2Sel = null;
			try {
				if (!sDate1Sel.equals("") && sDate2Sel.equals("")) {
					date1Sel = formatter.parse(sDate1Sel);
					// System.out.println("date saisie = "+date1);
					messagesPredicates.add(MessagePredicates.supDate1(date1Sel));
				}
				// Date 2
				else if (sDate1Sel.equals("") && !sDate2Sel.equals("")) {
					date2Sel = formatter.parse(sDate2Sel);
					// System.out.println("date saisie = "+date1);
					messagesPredicates.add(MessagePredicates.infDate2(date2Sel));
				} 
				// Date1 && Date2
				else if (!sDate1Sel.equals("") && !sDate2Sel.equals("")) {
					date1Sel = formatter.parse(sDate1Sel);
					date2Sel = formatter.parse(sDate2Sel);
					messagesPredicates.add(MessagePredicates.supDate1EtInfDate2(date1Sel, date2Sel));
				}
			} catch (ParseException e) {
				activitesView.setLabelProgress(bundleOperationsListe.getString("txt_ListeCsv"));
				activitesView.appendTxtArea(bundleOperationsListe.getString("txt_ListeCsv") + " "
						+ bundleOperationsListe.getString("txt_Echouee"));
				activitesView.getProgressBar().setIndeterminate(false);
				activitesView.getProgressBar().setStringPainted(true);
				JOptionPane.showMessageDialog(null, "ERREUR DE FORMAT DE SAISIE DE date1 ou date2",
						bundleOperationsListe.getString("txt_ListeCsv"), JOptionPane.INFORMATION_MESSAGE);
				exportToCsvPanel = new DialogPanelExportToCsv(setLocuteurs, bundleOperationsListe);
				int result = JOptionPane.showOptionDialog(null, exportToCsvPanel,
						bundleOperationsListe.getString("txt_ListeCsv"), JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (result == JOptionPane.OK_OPTION) {
					exportSelonCriteres();
				}
				return;
			}
			// Numéros de messages
			String numerosMessagesSel = exportToCsvPanel.getTxtFNumerosMessages().getText();
			if (!numerosMessagesSel.equals("")) {
				// FIRSTNUMM ET LASTNUMM
				Comparator<MessageModel> byNumero = new MessageNumeroComparator();
				Collections.sort(listMessages, byNumero);
				String sFirstNumM = listMessages.get(0).getNumero();
				int firstNumM = Integer.parseInt(sFirstNumM);
				String sLastNumM = listMessages.get(listMessages.size() - 1).getNumero();
				int lastNumM = Integer.parseInt(sLastNumM);
				messagesPredicates.add(MessagePredicates.findNumero(numerosMessagesSel, firstNumM, lastNumM));
			}
			// Mots dans sujet
			String motsSujetSel = exportToCsvPanel.getTxtFMotsSujet().getText();
			Boolean etOuMotsSujetSel = exportToCsvPanel.getChkEtOuMotsSujet().isSelected();
			if (!motsSujetSel.equals("")) {
				messagesPredicates.add(MessagePredicates.findMotDansSujet(motsSujetSel, etOuMotsSujetSel));
			}
			String motsCorpsSel = exportToCsvPanel.getTxtFMotsCorps().getText();
			Boolean etOuMotsCorpsSel = exportToCsvPanel.getChkEtOuMotsCorps().isSelected();
			Boolean nonIntegrationMessageOriginalMotsCorpsSel = exportToCsvPanel.getChkNonIntegrationMessageOriginal()
					.isSelected();
			if (!motsCorpsSel.equals("")) {
				messagesPredicates.add(MessagePredicates.findMotDansCorps(motsCorpsSel, etOuMotsCorpsSel,
						nonIntegrationMessageOriginalMotsCorpsSel));
			}
			// Conversation : nombre de locuteurs 1
			// Conversation : nombre de locuteurs 2
			// Conversation : nombre de messages 1
			// Conversation : nombre de messages 2
			String nbreLocuteurs1Sel = exportToCsvPanel.getTxtFNbreLocuteurs1().getText();
			String nbreLocuteurs2Sel = exportToCsvPanel.getTxtFNbreLocuteurs2().getText();
			String nbreMessages1Sel = exportToCsvPanel.getTxtFNbreMessages1().getText();
			String nbreMessages2Sel = exportToCsvPanel.getTxtFNbreMessages2().getText();
			try {
				if (!nbreLocuteurs1Sel.equals("")) {
					int iNbreLocuteurs1Sel;
					iNbreLocuteurs1Sel = Integer.parseInt(nbreLocuteurs1Sel);
					conversationPredicates.add(ConversationPredicates.findNbreLocuteurs1(iNbreLocuteurs1Sel));

				}
				if (!nbreLocuteurs2Sel.equals("")) {
					int iNbreLocuteurs2Sel;
					iNbreLocuteurs2Sel = Integer.parseInt(nbreLocuteurs2Sel);
					conversationPredicates.add(ConversationPredicates.findNbreLocuteurs2(iNbreLocuteurs2Sel));

				}
				if (!nbreMessages1Sel.equals("")) {
					int iNbreMessages1Sel;
					iNbreMessages1Sel = Integer.parseInt(nbreMessages1Sel);
					conversationPredicates.add(ConversationPredicates.findNbreMessages1(iNbreMessages1Sel));
				}
				if (!nbreMessages2Sel.equals("")) {
					int iNbreMessages2Sel;
					iNbreMessages2Sel = Integer.parseInt(nbreMessages2Sel);
					conversationPredicates.add(ConversationPredicates.findNbreMessages2(iNbreMessages2Sel));
				}
			} catch (NumberFormatException nfe) {
				activitesView.setLabelProgress(bundleOperationsListe.getString("txt_ListeCsv"));
				activitesView.appendTxtArea(bundleOperationsListe.getString("txt_ListeCsv") + " "
						+ bundleOperationsListe.getString("txt_Echouee"));
				activitesView.getProgressBar().setIndeterminate(false);
				activitesView.getProgressBar().setStringPainted(true);
				JOptionPane.showMessageDialog(null,
						"ERREUR DE FORMAT DE SAISIE DE nbreLocuteurs1Sel, nbreLocuteurs2Sel, nbreMessages1Sel ou nbreMessages2Sel",
						bundleOperationsListe.getString("txt_ListeCsv"), JOptionPane.INFORMATION_MESSAGE);
				exportToCsvPanel = new DialogPanelExportToCsv(setLocuteurs, bundleOperationsListe);
				int result = JOptionPane.showOptionDialog(null, exportToCsvPanel,
						bundleOperationsListe.getString("txt_ListeCsv"), JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (result == JOptionPane.OK_OPTION) {
					exportSelonCriteres();
				}
				return;
			}

			tempListMToExport = filterMessages(listMessages, messagesPredicates);
			tempListCToExport = filterConversations(setConversations, conversationPredicates);
			tempListLToExport = filterLocuteurs(setLocuteurs, locuteurPredicates);
			System.out.println("taille de tempListMToExport = " + tempListMToExport.size());
			System.out.println("taille de tempListCToExport = " + tempListCToExport.size());
			System.out.println("taille de tempListLToExport = " + tempListLToExport.size());

			// } else {
			// activitesView.setLabelProgress(bundleOperationsListe.getString("txt_ListeCsv"));
			// activitesView.appendTxtArea(bundleOperationsListe.getString("txt_ListeCsv")
			// + " " + bundleOperationsListe.getString("txt_Echouee"));
			// activitesView.getProgressBar().setIndeterminate(false);
			// activitesView.getProgressBar().setStringPainted(true);
			// JOptionPane.showMessageDialog(null,
			// bundleOperationsListe.getString("txt_RequeteNoResults"),
			// bundleOperationsListe.getString("txt_ListeCsv"),
			// JOptionPane.INFORMATION_MESSAGE);
			// exportToCsvPanel = new DialogPanelExportToCsv(setLocuteurs,
			// bundleOperationsListe);
			// int result = JOptionPane.showOptionDialog(null, exportToCsvPanel,
			// bundleOperationsListe.getString("txt_ListeCsv"),
			// JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
			// null, null);
			// if (result == JOptionPane.OK_OPTION) {
			// exportSelonCriteres();
			// }
			// }
		}

		// FUSION DES FILTRES
		if (tempListMToExport.size() > 0 && tempListCToExport.size() > 0 && tempListLToExport.size() > 0) {
			for (MessageModel message : tempListMToExport) {
				int idConversation = message.getIdConversation();
				String expediteur = message.getExpediteur();
				// System.out.println("Message : idConversation = " +
				// idConversation
				// + " | expediteur = " + expediteur);
				for (ConversationModel conversation : tempListCToExport) {
					if (conversation.getId() == idConversation) {
						// System.out.println("\tConversation : idConversation = "
						// +
						// conversation.getId());
						for (LocuteurModel locuteur : tempListLToExport) {
							if (locuteur.getNom().equals(expediteur)) {
								// System.out.println("\t\tLocuteur : locuteur = "
								// +
								// locuteur.getNom());
								listMToExport.add(message);
								listCToExport.add(conversation);
								listLToExport.add(locuteur);
							}
						}
					}
				}
			}
		} else {
			listMToExport.addAll(listMessages);
			listCToExport.addAll(setConversations);
			listLToExport.addAll(setLocuteurs);
		}

		System.out.println("Il y a " + listMToExport.size() + " messages, " + listCToExport.size()
				+ " conversations, et " + listLToExport.size() + " locuteurs à exporter");

		if (listMToExport.size() > 0 && listCToExport.size() > 0 && listLToExport.size() > 0) {
			exportToCsv(listMToExport, listCToExport, listLToExport);
		} else {
			JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_RequeteNoResults"),
					bundleOperationsListe.getString("txt_ListeCsv"), JOptionPane.INFORMATION_MESSAGE);
			exportToCsvPanel = new DialogPanelExportToCsv(setLocuteurs, bundleOperationsListe);
			int result = JOptionPane.showOptionDialog(null, exportToCsvPanel,
					bundleOperationsListe.getString("txt_ListeCsv"), JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (result == JOptionPane.OK_OPTION) {
				exportSelonCriteres();
			}
		}
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_ListeCsv"));
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_ListeCsv") + " "
				+ bundleOperationsListe.getString("txt_Accomplie") + "\n");
		activitesView.getProgressBar().setIndeterminate(false);
		activitesView.getProgressBar().setStringPainted(true);
	}

	private void exportToCsv(List<MessageModel> listMToExport, Set<ConversationModel> listCToExport,
			Set<LocuteurModel> listLToExport) {
		PrintWriter out = null;
		DateFormat formatterDateFile = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		// JFileChooser
		CustomJFileChooser customChooser = new CustomJFileChooser("ListeToCsv", bundleOperationsListe, repertoireProjet,
				true);
		customChooser.show();
		if (customChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			String today = formatterDateFile.format(new Date());
			String nomTabExported = bundleOperationsListe.getString("txt_MessagesMin");
			String nomFichier = customChooser.getSelectedFile() + "/" + nomTabExported + "_"
					+ activitesView.getProjetController().getListeSelected().getNom() + "_" + today + ".csv";
			// Export des messages
			List<Integer> listSel = getExportMessagesOptions();
			if (listSel != null) {
				try {
					out = new PrintWriter(
							new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nomFichier), charset)));
					exportMessages(listMToExport, out, listSel);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} finally {
					if (out != null)
						out.close();
					JOptionPane.showMessageDialog(null,
							listMToExport.size() + " " + bundleOperationsListe.getString("txt_MessagesExportes") + "\n"
									+ nomFichier + ".",
							bundleOperationsListe.getString("txt_ListeCsv"), JOptionPane.INFORMATION_MESSAGE);
				}
				// Export des conversations
				nomTabExported = bundleOperationsListe.getString("txt_Conversations");
				nomFichier = customChooser.getSelectedFile() + "/" + nomTabExported + "_"
						+ activitesView.getProjetController().getListeSelected().getNom() + "_" + today + ".csv";
				try {
					out = new PrintWriter(
							new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nomFichier), charset)));
					exportConversations(listCToExport, out);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} finally {
					if (out != null)
						out.close();
					JOptionPane.showMessageDialog(null,
							listCToExport.size() + " " + bundleOperationsListe.getString("txt_ConversationsExportes")
									+ "\n" + nomFichier + ".",
							bundleOperationsListe.getString("txt_ListeCsv"), JOptionPane.INFORMATION_MESSAGE);

				}
				// Export des locuteurs
				nomTabExported = bundleOperationsListe.getString("txt_Locuteurs");
				nomFichier = customChooser.getSelectedFile() + "/" + nomTabExported + "_"
						+ activitesView.getProjetController().getListeSelected().getNom() + "_" + today + ".csv";
				try {
					out = new PrintWriter(
							new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nomFichier), charset)));
					exportLocuteurs(listLToExport, out);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} finally {
					if (out != null)
						out.close();
					JOptionPane.showMessageDialog(null,
							listLToExport.size() + " " + bundleOperationsListe.getString("txt_LocuteursExportes") + "\n"
									+ nomFichier + ".",
							bundleOperationsListe.getString("txt_ListeCsv"), JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

	private List<Integer> getExportMessagesOptions() {
		List<Integer> listSel = new ArrayList<Integer>();
		DialogPanelExportToCsvOptions exportPanelOptions = new DialogPanelExportToCsvOptions(bundleOperationsListe);
		int result = JOptionPane.showOptionDialog(null, exportPanelOptions,
				bundleOperationsListe.getString("txt_ListeCsv"), JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			Boolean corpsSel = exportPanelOptions.getChkCorpsMessage().isSelected();
			if (corpsSel == true)
				listSel.add(6);
			Boolean corpsSsHTMLSel = exportPanelOptions.getChkCorpsMessageSsHTML().isSelected();
			if (corpsSsHTMLSel == true)
				listSel.add(7);
			Boolean corpsSsOriginalMessageSel = exportPanelOptions.getChkCorpsMessageSsOriginalMessage().isSelected();
			if (corpsSsOriginalMessageSel == true)
				listSel.add(8);
			Collections.sort(listSel);
			return listSel;
		} else
			return null;

	}

	private void exportMessages(List<MessageModel> listMToExport, PrintWriter out, List<Integer> listSel) {
		// Dialog Options d'export pour les messages

		// NOMS DE COLONNES
		out.write("\"" + bundleOperationsListe.getString("txt_CsvIdentifiant") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_CsvDateMessage") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_CsvExpediteurMessage") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_CsvIdLocMessage") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_CsvSujetMessage") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_CsvSujetTronqueMessage") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_CsvIdConvMessage") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_CsvFName") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_CsvFNumDansTopicMessage") + "\";");
		if (listSel.size() > 0) {
			out.write("\"" + bundleOperationsListe.getString("txt_CsvTxtMessage") + "\";");
		}
		out.write("\n");
		// MESSAGES
		for (MessageModel message : listMToExport) {
			out.write("\"" + message.getIdentifiant() + "\";");
			out.write("\"" + formatter.format(message.getDateUS()) + "\";");
			out.write("\"" + message.getExpediteur() + "\";");
			out.write("\"" + message.getIdLocuteur() + "\";");
			String newSujet = message.getSujet();
			if (newSujet != null && newSujet.indexOf("\"") != -1)
				newSujet = newSujet.replace("\"", "");
			out.write("\"" + newSujet + "\";");
			out.write("\"" + message.getSujetTronque() + "\";");
			out.write("\"" + message.getIdConversation() + "\";");
			out.write("\"" + message.getFName() + "\";");
			out.write("\"" + message.getfNumDansConversation() + "\";");
			if (listSel.size() > 0) {
				for (Integer i : listSel) {
					if (i == 6) {
						String corps = null;
						String corpsMessage = message.getCorps();

						if (listSel.size() > 1) {
							if (listSel.size() > 2) {
								corps = message.getTextSsHTML(corpsMessage);
								corps = message.getSsOriginalMessage(corps);
							} else {
								corps = message.getTextSsHTML(corpsMessage);
							}
						} else {
							if (listSel.size() > 2) {
								corps = message.getSsOriginalMessage(corpsMessage);
							} else {
								corps = corpsMessage;
							}
						}

						if (corps != null && corps != "" && corps.length() > 32767)
							corps = corps.substring(0, 32767 - 20);
						if (corps != null) {
							if (corps.indexOf(";") != -1)
								corps = corps.replace(";", ".");
							if (corps.indexOf("\"") != -1)
								corps = corps.replace("\"", "");
						}

						out.write("\"" + corps + "\";");
					}
				}
			}
			out.write("\n");
		}
	}

	private void exportConversations(Set<ConversationModel> listCToExport, PrintWriter out) {
		// NOMS DE COLONNES
		out.write("\"" + bundleOperationsListe.getString("txt_CsvIdentifiant") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_Debut") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_Fin") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_Duree") + "("
				+ bundleOperationsListe.getString("txt_Jours") + ")" + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_SujetPremierMessage") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_CsvNbreMessages") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_CsvNbreLocuteursConversation") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_Lanceur") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_SC") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_CsvFName") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_CsvFNbreVueTopicConversation") + "\";");
		out.write("\n");
		// MESSAGES
		for (ConversationModel conversation : listCToExport) {
			out.write("\"" + conversation.getId() + "\";");
			out.write("\"" + formatter.format(conversation.getDateDebut()) + "\";");
			out.write("\"" + formatter.format(conversation.getDateFin()) + "\";");
			out.write("\"" + conversation.getDuree() + "\";");
			String newSujetPremierMessage = conversation.getSujetPremierMessage();
			if (newSujetPremierMessage != null && newSujetPremierMessage.indexOf("\"") != -1)
				newSujetPremierMessage = newSujetPremierMessage.replace("\"", "");
			out.write("\"" + newSujetPremierMessage + "\";");
			out.write("\"" + conversation.getNbreMessages() + "\";");
			out.write("\"" + conversation.getNbreLocuteurs() + "\";");
			String newLanceur = conversation.getLanceur();
			if (newLanceur != null && newLanceur.indexOf("\"") != -1)
				newLanceur = newLanceur.replace("\"", "");
			out.write("\"" + newLanceur + "\";");
			out.write("\"" + conversation.isSc() + "\";");
			String newFName = conversation.getfName();
			if (newFName != null && newFName.indexOf("\"") != -1)
				newFName = newFName.replace("\"", "");
			out.write("\"" + newFName + "\";");
			out.write("\"" + conversation.getfNbreVues() + "\";");
			out.write("\n");
		}
	}

	private void exportLocuteurs(Set<LocuteurModel> listLToExport, PrintWriter out) {
		// NOMS DE COLONNES
		out.write("\"" + bundleOperationsListe.getString("txt_CsvIdentifiant") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_Nom") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_Debut") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_Fin") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_DureeParticipation") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_CsvNbreMessages") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_Intensite") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_LocuteurDominant") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_Nombre") + " "
				+ bundleOperationsListe.getString("txt_Conversations") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_Nombre") + " "
				+ bundleOperationsListe.getString("txt_ParticipationsSC") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_Nombre") + " "
				+ bundleOperationsListe.getString("txt_SCLances") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_Nombre") + " "
				+ bundleOperationsListe.getString("txt_MessagesSC") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_PourcentTotalMessages") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_FRole") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_FPosition") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_FNbreEtoiles") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_FActivity") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_FNbrePostsTotal") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_FDateRegistered") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_FEmail") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_FWebsite") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_FGender") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_FAge") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_FLocation") + "\";");
		out.write("\"" + bundleOperationsListe.getString("txt_FSignature") + "\";");
		out.write("\n");
		// MESSAGES
		for (LocuteurModel locuteur : listLToExport) {
			// System.out.println("id locuteur = "+locuteur.getId());
			out.write("\"" + locuteur.getId() + "\";");
			String newNom = locuteur.getNom();
			if (newNom != null && newNom.indexOf("\"") != -1)
				newNom = newNom.replace("\"", "");
			out.write("\"" + newNom + "\";");
			out.write("\"" + formatter.format(locuteur.getDateDebut()) + "\";");
			out.write("\"" + formatter.format(locuteur.getDateFin()) + "\";");
			out.write("\"" + locuteur.getDuree() + "\";");
			out.write("\"" + locuteur.getNbreMessages() + "\";");
			out.write("\"" + locuteur.getIntensite() + "\";");
			out.write("\"" + locuteur.isLd() + "\";");
			out.write("\"" + locuteur.getNbreConversations() + "\";");
			out.write("\"" + locuteur.getNbreSujetsCollectifs() + "\";");
			out.write("\"" + locuteur.getNbreSujetsCollectifsLances() + "\";");
			out.write("\"" + locuteur.getNbreMessagesSC() + "\";");
			out.write("\"" + locuteur.getPourcentMessagesSC() + "\";");
			out.write("\"" + locuteur.getfRole() + "\";");
			out.write("\"" + locuteur.getfStatPosition() + "\";");
			out.write("\"" + locuteur.getfStars() + "\";");
			out.write("\"" + locuteur.getfStatActivity() + "\";");
			out.write("\"" + locuteur.getfStatNbrePosts() + "\";");
			Date newDate = locuteur.getfStatDateRegistrered();
			String sNewDate = "";
			if (newDate != null)
				sNewDate = formatter.format(newDate);
			out.write("\"" + sNewDate + "\";");
			out.write("\"" + locuteur.getfStatEMail() + "\";");
			String newWebsite = locuteur.getfStatWebsite();
			if (newWebsite != null && newWebsite.indexOf("\"") != -1)
				newWebsite = newWebsite.replace("\"", "");
			out.write("\"" + newWebsite + "\";");
			out.write("\"" + locuteur.getfStatGender() + "\";");
			out.write("\"" + locuteur.getfStatAge() + "\";");
			String newLocation = locuteur.getfStatLocation();
			if (newLocation != null && newLocation.indexOf("\"") != -1)
				newLocation = newLocation.replace("\"", "");
			out.write("\"" + newLocation + "\";");
			String newSignature = locuteur.getfStatSignature();
			if (newSignature != null && newSignature.indexOf("\"") != -1)
				newSignature = newSignature.replace("\"", "");
			out.write("\"" + newSignature + "\";");
			out.write("\n");
		}
	}

	private List<MessageModel> filterMessages(Iterable<MessageModel> values,
			Iterable<Predicate<MessageModel>> predicates) {
		List<MessageModel> result = new ArrayList<MessageModel>();
		// On parcours toutes les valeurs :
		for (MessageModel value : values) {
			// Si elle correspondent aux "Predicates" :
			if (evalAllMessages(value, predicates)) {
				// On ajoute la valeur aux resultats :
				result.add(value);
			}
		}
		return result;
	}

	private boolean evalAllMessages(MessageModel value, Iterable<Predicate<MessageModel>> predicates) {
		// On evalue tout les "Predicates"
		for (Predicate<MessageModel> p : predicates) {
			if (!p.eval(value)) {
				// Si un seul est faux, on arrete immediatement
				return false;
			}
		}
		// Sinon c'est OK :
		return true;
	}

	private <T> List<T> filterConversations(Iterable<T> values, Iterable<Predicate<T>> predicates) {
		List<T> result = new ArrayList<T>();
		// On parcours toutes les valeurs :
		for (T value : values) {
			// Si elle correspondent aux "Predicates" :
			if (evalAllConversations(value, predicates)) {
				// On ajoute la valeur aux resultats :
				result.add(value);
			}
		}
		return result;
	}

	private <T> boolean evalAllConversations(T value, Iterable<Predicate<T>> predicates) {
		// On evalue tout les "Predicates"
		for (Predicate<T> p : predicates) {
			if (!p.eval(value)) {
				// Si un seul est faux, on arrete immediatement
				return false;
			}
		}
		// Sinon c'est OK :
		return true;
	}

	private <T> List<T> filterLocuteurs(Iterable<T> values, Iterable<Predicate<T>> predicates) {
		List<T> result = new ArrayList<T>();
		// On parcours toutes les valeurs :
		for (T value : values) {
			// Si elle correspondent aux "Predicates" :
			if (evalAllLocuteurs(value, predicates)) {
				// On ajoute la valeur aux resultats :
				result.add(value);
			}
		}
		return result;
	}

	private <T> boolean evalAllLocuteurs(T value, Iterable<Predicate<T>> predicates) {
		// On evalue tout les "Predicates"
		for (Predicate<T> p : predicates) {
			if (!p.eval(value)) {
				// Si un seul est faux, on arrete immediatement
				return false;
			}
		}
		// Sinon c'est OK :
		return true;
	}

}
