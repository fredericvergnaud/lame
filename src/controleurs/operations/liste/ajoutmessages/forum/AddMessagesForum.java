package controleurs.operations.liste.ajoutmessages.forum;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JOptionPane;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import modeles.ForumModel;
import modeles.MessageModel;
import modeles.TopicModel;
import vue.dialog.DialogPanelAjoutForum;
import vue.dialog.DialogPanelChoixForum;
import controleurs.operations.liste.ajoutmessages.AddMessagesUtils;
import controleurs.vuesabstraites.ProjetView;

public class AddMessagesForum {

	private String httpProxyAdress = "", httpProxyPort = "", siteUrl = "";
	private ResourceBundle bundleOperationsListe;
	private ProjetView activitesView;
	private int numMessageEnCoursListe;
	private Map<String, MessageModel> newMapIdMessages;
	private int forumWrapperType;
	private ForumModel forumSelected;
	private int sleepTime = 0;
	private String nomForumEnCours;
	private Date dateFinListe;
	private ConnectToUrl connectToUrl = new ConnectToUrl();
	private Document doc = null;
	private AddMessagesUtils utils;
	

	public AddMessagesForum(ResourceBundle bundleOperationsListe, ProjetView activitesView, int numMessageEnCoursListe,
			String nomForumEnCours, Date dateFinListe) {
		this.bundleOperationsListe = bundleOperationsListe;
		this.activitesView = activitesView;
		this.numMessageEnCoursListe = numMessageEnCoursListe;
		this.nomForumEnCours = nomForumEnCours;
		this.dateFinListe = dateFinListe;
		utils = new AddMessagesUtils();
	}

	public void displayDialogConnexion() {
		// Display du dialog Ajout de Forum
		DialogPanelAjoutForum optPanel = new DialogPanelAjoutForum(bundleOperationsListe, httpProxyAdress,
				httpProxyPort, siteUrl, sleepTime);
		int result = JOptionPane.showOptionDialog(null, optPanel, bundleOperationsListe.getString("txt_AjoutForum"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			// Tests si proxy sélectionné et proxy + port sont vides
			// Return si échec
			// Si ok, on définit le proxy
			boolean isProxySelected = optPanel.getProxySelected();
			httpProxyAdress = optPanel.getHttpProxyAdress();
			httpProxyPort = optPanel.getHttpProxyPort();
			siteUrl = optPanel.getForumAdress();
			// // Test si besoin de proxy
			// URL url;
			// int code = 0;
			if (isProxySelected == true) {
				if (httpProxyAdress.equals("") || httpProxyPort.equals("")) {
					JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ProxyNonValide"),
							bundleOperationsListe.getString("txt_ErreurProxy"), JOptionPane.ERROR_MESSAGE);
					displayDialogConnexion();
					return;
				}
				System.setProperty("http.proxyHost", httpProxyAdress);
				System.setProperty("http.proxyPort", httpProxyPort);
				// try {
				// url = new URL(siteUrl);
				// HttpURLConnection connection = (HttpURLConnection)
				// url.openConnection();
				// connection.connect();
				// code = connection.getResponseCode();
				// String line = null;
				// StringBuffer tmp = new StringBuffer();
				// BufferedReader in = new BufferedReader(new
				// InputStreamReader(connection.getInputStream()));
				// while ((line = in.readLine()) != null) {
				// tmp.append(line);
				// }
				// } catch (MalformedURLException e) {
				// } catch (IOException e) {
				// }

			}
			// else {
			// try {
			// url = new URL(siteUrl);
			// HttpURLConnection connection = (HttpURLConnection)
			// url.openConnection();
			// connection.connect();
			// code = connection.getResponseCode();
			// String line = null;
			// StringBuffer tmp = new StringBuffer();
			// BufferedReader in = new BufferedReader(new
			// InputStreamReader(connection.getInputStream()));
			// while ((line = in.readLine()) != null) {
			// tmp.append(line);
			// }
			// } catch (MalformedURLException e) {
			// } catch (IOException e) {
			// }
			// }
			// if (code != 200) {
			// JOptionPane.showMessageDialog(null,
			// bundleOperationsListe.getString("txt_ImpossibleAccederAdresseForum")
			// + "\n" + siteUrl,
			// bundleOperationsListe.getString("txt_ErreurProxy403"),
			// JOptionPane.ERROR_MESSAGE);
			// displayDialogConnexion();
			// return;
			// }

			sleepTime = Integer.parseInt(optPanel.getSleepTime());
			connectToUrl.setSleeptime(sleepTime);
			

			// Test si oubli du http ou https
			if (!siteUrl.startsWith("http://") && !siteUrl.startsWith("https://"))
				siteUrl = "http://" + siteUrl;

			// Test si url = vide ou url = seulement http:// ou http://www + id
			// avec https
			// Return si échec
			// Si ok, on cherche les forums à l'adresse indiquée
			if (siteUrl.equals("") || siteUrl.equals("http://") || siteUrl.equals("http://www")
					|| siteUrl.equals("https://") || siteUrl.equals("https://www")) {
				JOptionPane.showMessageDialog(null,
						bundleOperationsListe.getString("txt_ImpossibleAccederAdresseForum") + "\n" + siteUrl,
						bundleOperationsListe.getString("txt_ErreurForumAdress"), JOptionPane.ERROR_MESSAGE);
				displayDialogConnexion();
				return;
			}

			// Recherche du type d'encapsulation des forums
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AnalyseUrl") + " - "
					+ bundleOperationsListe.getString("txt_Patientez"));
			activitesView.getProgressBar().setIndeterminate(true);
			activitesView.getProgressBar().setStringPainted(false);
			activitesView.updateProgress();

			doc = connectToUrl.getDocumentFromUrl(siteUrl);
			// System.out.println("doc = " + doc);
			if (doc != null) {
				forumWrapperType = utils.getForumWrapperType(doc);
				System.out.println("forumWrapperType = " + forumWrapperType);
			} else
				System.out.println("doc est null");

			activitesView.getProgressBar().setIndeterminate(false);
			activitesView.getProgressBar().setStringPainted(true);
			activitesView.setStepProgress(1);
			activitesView.updateProgress();
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AnalyseUrl"));

			// on analyse les exceptions de connexion
			// on affiche l'erreur puis le dialogue de saisie de l'url puis on
			// sort
			if (connectToUrl.getException() != 0) {
				if (connectToUrl.getException() == 1) {
					// Exception 1 = SocketTimeoutException
					JOptionPane.showMessageDialog(null,
							bundleOperationsListe.getString("txt_ErreurTimedOut") + "\n"
									+ connectToUrl.getExceptionMsg(),
							bundleOperationsListe.getString("txt_ErreurTimedOut"), JOptionPane.ERROR_MESSAGE);
				} else if (connectToUrl.getException() == 2) {
					// Exception 2 = IOException
					JOptionPane.showMessageDialog(null,
							bundleOperationsListe.getString("txt_ErreurIO") + " : \n" + connectToUrl.getExceptionMsg(),
							bundleOperationsListe.getString("txt_ErreurIO"), JOptionPane.ERROR_MESSAGE);
				} else if (connectToUrl.getException() == 3) {
					// Exception 503 = Surcharge ou maintenance du
					// serveur
					JOptionPane.showMessageDialog(null,
							bundleOperationsListe.getString("txt_AugmenterLatenceTelechargement"),
							bundleOperationsListe.getString("txt_ErreurSurcharge"), JOptionPane.ERROR_MESSAGE);
				} else
					JOptionPane.showMessageDialog(null,
							bundleOperationsListe.getString("txt_ErreurHttp") + " " + connectToUrl.getExceptionMsg(),
							bundleOperationsListe.getString("txt_ErreurHttp"), JOptionPane.ERROR_MESSAGE);
				displayDialogConnexion();
				return;
			}

			// Si forumWrapperType = 0 -> wrapper type de forum non pris en
			// charge
			if (forumWrapperType == 0) {
				JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_PageNonValide"),
						bundleOperationsListe.getString("txt_ErreurPage"), JOptionPane.ERROR_MESSAGE);
				displayDialogConnexion();
				return;
			}

			// On récupère la liste des forums selon le type de wrapper
			// déterminé
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AnalyseUrl") + " - "
					+ bundleOperationsListe.getString("txt_Patientez"));
			activitesView.getProgressBar().setIndeterminate(true);
			activitesView.getProgressBar().setStringPainted(false);
			activitesView.updateProgress();

			connectToUrl = new ConnectToUrl();
			connectToUrl.setSleeptime(0);
			doc = connectToUrl.getDocumentFromUrl(siteUrl);
			List<ForumModel> listForums = new ArrayList<ForumModel>();
			Set<String> exceptionsSet = new HashSet<String>();

			if (doc != null) {
				FindForums findForums = new FindForums(doc, forumWrapperType, siteUrl);
				listForums.addAll(findForums.getListForums());
			}

			String exceptionMsg = connectToUrl.getExceptionMsg();
			exceptionsSet.add(exceptionMsg);

			activitesView.getProgressBar().setIndeterminate(false);
			activitesView.getProgressBar().setStringPainted(true);
			activitesView.setStepProgress(1);
			activitesView.updateProgress();
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AnalyseUrl"));

			// Si la liste de forums est vide, forum non pris en charge ou
			// erreurs de
			// connexion
			if (listForums.size() == 0) {
				// Traitement des erreurs de connexions
				if (exceptionsSet.size() > 0) {
					displayConnexionErrors(exceptionsSet);
				} else {
					JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_PageNonValide"),
							bundleOperationsListe.getString("txt_ErreurPage"), JOptionPane.ERROR_MESSAGE);
				}
				displayDialogConnexion();
				return;
			}

			// Set pour chaque forum de sa liste de pointeurs vers des pages de
			// topics : 2 cas
			// - soit les pointeurs sont des urls de pages de topics
			// - soit le pointeur est l'url fournie par l'utilisateur = siteurl
			// : cas de
			// http://www.lesimpatientes.com/forums-cancer-sein-categories.asp
			// où les topics sont directement sur la première page de
			// présentation des forums

			int nbreTotalTopicsPagesUrls = 0;

			activitesView.appendTxtArea(
					listForums.size() + " " + bundleOperationsListe.getString("txt_ForumsRecuperes") + "\n");
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AnalyseForums") + " - "
					+ bundleOperationsListe.getString("txt_Patientez"));
			activitesView.getProgressBar().setIndeterminate(true);
			activitesView.getProgressBar().setStringPainted(false);
			activitesView.updateProgress();
			exceptionsSet = new HashSet<String>();

			System.out.println();

			for (ForumModel forum : listForums) {
				String forumURL = forum.getUrl();
				doc = connectToUrl.getDocumentFromUrl(forumURL);
				if (doc != null) {
					List<String> listTopicsPagesUrls = new ArrayList<String>();
					FindTopicsPagesUrls findTopicsPagesUrls = new FindTopicsPagesUrls(doc, forum);
					listTopicsPagesUrls = findTopicsPagesUrls.getListTopicsPagesUrls();
					System.out.println("\t\t\tlistTopicsPagesUrls : " + listTopicsPagesUrls);
					forum.setListTopicsPagesUrls(listTopicsPagesUrls);
					nbreTotalTopicsPagesUrls += listTopicsPagesUrls.size();
				}
				exceptionMsg = connectToUrl.getExceptionMsg();
				exceptionsSet.add(exceptionMsg);
			}

			// System.out.println("exceptionsArray : \n" + exceptionsArray);

			activitesView.getProgressBar().setIndeterminate(false);
			activitesView.getProgressBar().setStringPainted(true);
			activitesView.setStepProgress(1);
			activitesView.updateProgress();
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AnalyseForums"));

			System.out.println("nbreTotalTopicsPagesUrls = " + nbreTotalTopicsPagesUrls);

			// Si nbreTotalTopicsPagesUrls == 0 => veut dire qu'il y a
			// eu des erreurs de connexions ou qu'on n'a trouvé aucune page de
			// topic sur l'ensemble des forums cherchés
			// => Return
			if (nbreTotalTopicsPagesUrls == 0) {
				// Traitement des erreurs de connexions
				if (exceptionsSet.size() > 0) {
					displayConnexionErrors(exceptionsSet);
				} else {
					JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_PageNonValide"),
							bundleOperationsListe.getString("txt_ErreurPage"), JOptionPane.ERROR_MESSAGE);
				}
				displayDialogConnexion();
				return;
			}
			displayDialogForums(listForums);
		}
	}

	private void displayDialogForums(List<ForumModel> listForums) {
		// Affichage du dialogue de choix du forum à télécharger
		DialogPanelChoixForum optPanel2 = new DialogPanelChoixForum(bundleOperationsListe, listForums, nomForumEnCours);
		int result2 = JOptionPane.showOptionDialog(null, optPanel2,
				bundleOperationsListe.getString("txt_ListeForums") + " " + siteUrl, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (result2 == JOptionPane.OK_OPTION) {
			if (optPanel2.getForumSelected() == null) {
				JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_SelectionForumErreur"),
						bundleOperationsListe.getString("txt_SelectionForum"), JOptionPane.ERROR_MESSAGE);
				displayDialogForums(listForums);
				return;
			}
			String nomForumSelected = optPanel2.getForumSelected();
			boolean chkUpdateSelected = optPanel2.chkUpdateSelected();
			for (ForumModel forum : listForums) {
				String nomForum = forum.getNom();
				if (nomForum.equals(nomForumSelected)) {
					setForumSelected(forum);
				}
			}

			System.out.println("Forum sélectionné = " + forumSelected.getNom() + " | " + forumSelected.getUrl());

			// Recherche du type de wrapper de topics sur la première page
			// de topic = première page du forum

			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AnalyseUrl") + " - "
					+ bundleOperationsListe.getString("txt_Patientez"));
			activitesView.getProgressBar().setIndeterminate(true);
			activitesView.getProgressBar().setStringPainted(false);
			activitesView.updateProgress();

			int topicWrapperType = 0;
			Set<String> exceptionsSet = new HashSet<String>();
			String forumUrl = forumSelected.getUrl();

			doc = connectToUrl.getDocumentFromUrl(forumUrl);

			if (doc != null) {
				utils.setTopicWrapperType(forumWrapperType, forumSelected.getUrlId(), doc);
				topicWrapperType = utils.getTopicWrapperType();
			}

			String exceptionMsg = connectToUrl.getExceptionMsg();
			exceptionsSet.add(exceptionMsg);

			activitesView.getProgressBar().setIndeterminate(false);
			activitesView.getProgressBar().setStringPainted(true);
			activitesView.setStepProgress(1);
			activitesView.updateProgress();
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AnalyseUrl"));

			// Si topicWrapperType = 0, type de page non pris en charge
			// -> Return
			if (topicWrapperType == 0) {
				JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_PageNonValide"),
						bundleOperationsListe.getString("txt_ErreurPage"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			System.out.println("topicWrapperType = " + topicWrapperType);

			// Parcours des urls de pages de Topics pour extraire les topics

			activitesView.resetProgress();
			activitesView.setStepProgress(forumSelected.getListTopicsPagesUrls().size());

			Map<String, TopicModel> listTopics = new HashMap<String, TopicModel>();

			int numPageTopics = 0;
			for (String topicsPageUrl : forumSelected.getListTopicsPagesUrls()) {
				System.out.println(
						"\t\t\t> Connexion à la page de topics n°" + numPageTopics + " (" + topicsPageUrl + ")");
				activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AnalysePagesTopics")
						+ " : Page de topics " + numPageTopics + " / " + forumSelected.getListTopicsPagesUrls().size());
				doc = connectToUrl.getDocumentFromUrl(topicsPageUrl);
				if (doc != null) {
					utils.setTopicWrapperType(forumWrapperType, forumSelected.getUrlId(), doc);
					Elements topicsWrappers = utils.getTopicsWrappers();
					if (topicsWrappers.size() > 0) {
						ExtractTopics extractTopics = new ExtractTopics(topicWrapperType, topicsWrappers);
						extractTopics.extract();
						listTopics.putAll(extractTopics.getListTopics());
					}
				}
				exceptionMsg = connectToUrl.getExceptionMsg();
				exceptionsSet.add(exceptionMsg);
				numPageTopics++;
				activitesView.updateProgress();
			}

			// Si la liste de topics est vide => veut dire
			// qu'il y a eu
			// des erreurs de connexions ou qu'on n'a trouvé aucun topic
			// sur les pages de topics cherchés
			// => Return
			if (listTopics.size() == 0) {
				if (exceptionsSet.size() > 0) {
					displayConnexionErrors(exceptionsSet);
				} else {
					JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_PageNonValide"),
							bundleOperationsListe.getString("txt_ErreurPage"), JOptionPane.ERROR_MESSAGE);
				}
				displayDialogConnexion();
				return;
			}

			activitesView.appendTxtArea(
					listTopics.size() + " " + bundleOperationsListe.getString("txt_TopicsRecuperes") + "\n");
			activitesView.resetProgress();

			forumSelected.setTopicsMap(listTopics);
			System.out.println("Le forum a " + forumSelected.getTopicsMap().size() + " topics :");

			// Set pour chaque topic de sa liste de pointeurs vers des pages de
			// messages : 2 cas
			// - soit les pointeurs sont des urls de pages de messages
			// - soit ... ?

			int nbreTotalMessagesPagesUrls = 0;

			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AnalyseTopics") + " - "
					+ bundleOperationsListe.getString("txt_Patientez"));
			activitesView.getProgressBar().setIndeterminate(true);
			activitesView.getProgressBar().setStringPainted(false);
			activitesView.updateProgress();

			exceptionsSet = new HashSet<String>();

			System.out.println();

			for (Entry<String, TopicModel> topicEntry : listTopics.entrySet()) {
				String topicUrl = topicEntry.getKey();
				TopicModel topic = topicEntry.getValue();
				doc = connectToUrl.getDocumentFromUrl(topicUrl);
				if (doc != null) {
					List<String> listMessagesPagesUrls = new ArrayList<String>();
					FindMessagesPagesUrls findMessagesPagesUrls = new FindMessagesPagesUrls(doc, topic,
							topicWrapperType);
					listMessagesPagesUrls = findMessagesPagesUrls.getListMessagesPagesUrls();
					topic.setListMessagesPagesUrls(listMessagesPagesUrls);
					nbreTotalMessagesPagesUrls += listMessagesPagesUrls.size();
				}
				exceptionMsg = connectToUrl.getExceptionMsg();
				exceptionsSet.add(exceptionMsg);
			}

			activitesView.getProgressBar().setIndeterminate(false);
			activitesView.getProgressBar().setStringPainted(true);
			activitesView.setStepProgress(1);
			activitesView.updateProgress();
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AnalyseTopics"));

			System.out.println("nbreTotalMessagesPagesUrls = " + nbreTotalMessagesPagesUrls);

			// Si nbreTotalMessagesPagesUrls == 0 => veut dire qu'il y a
			// eu des erreurs de connexions ou qu'on n'a trouvé aucune page de
			// messages sur l'ensemble des topics cherchés
			// => Return
			if (nbreTotalMessagesPagesUrls == 0) {
				// Traitement des erreurs de connexions
				if (exceptionsSet.size() > 0) {
					displayConnexionErrors(exceptionsSet);
				} else {
					JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_PageNonValide"),
							bundleOperationsListe.getString("txt_ErreurPage"), JOptionPane.ERROR_MESSAGE);
				}
				displayDialogConnexion();
				return;
			}

			activitesView.appendTxtArea(nbreTotalMessagesPagesUrls + " "
					+ bundleOperationsListe.getString("txt_PagesMessagesRecuperees") + "\n");
			activitesView.resetProgress();

			// Recherche du type de wrapper de messages sur la première page
			// de messages = page du topic

			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AnalyseUrl") + " - "
					+ bundleOperationsListe.getString("txt_Patientez"));
			activitesView.getProgressBar().setIndeterminate(true);
			activitesView.getProgressBar().setStringPainted(false);
			activitesView.updateProgress();

			int messageWrapperType = 0;
			exceptionsSet = new HashSet<String>();
			Entry<String,TopicModel> firstEntry = listTopics.entrySet().iterator().next();
			TopicModel firstTopic = firstEntry.getValue();
			String firstTopicUrl = firstTopic.getUrl();

			doc = connectToUrl.getDocumentFromUrl(firstTopicUrl);

			if (doc != null) {
				utils.setMessageWrapperType(forumWrapperType, doc);
				messageWrapperType = utils.getMessageWrapperType();
			}

			exceptionMsg = connectToUrl.getExceptionMsg();
			exceptionsSet.add(exceptionMsg);

			// Si messageWrapperType = 0, type de page non pris en charge
			// -> Return
			if (messageWrapperType == 0) {
				if (exceptionsSet.size() > 0) {
					displayConnexionErrors(exceptionsSet);
				} else {
					JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_PageNonValide"),
							bundleOperationsListe.getString("txt_ErreurPage"), JOptionPane.ERROR_MESSAGE);
				}
				displayDialogConnexion();
				return;
			}

			System.out.println("messageWrapperType = " + messageWrapperType);

			activitesView.getProgressBar().setIndeterminate(false);
			activitesView.getProgressBar().setStringPainted(true);
			activitesView.setStepProgress(1);
			activitesView.updateProgress();
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AnalyseUrl"));

			// Parcours des urls de pages de Messages

			activitesView.resetProgress();
			activitesView.setStepProgress(listTopics.size());

			int numTopic = 1;
			exceptionsSet = new HashSet<String>();
			newMapIdMessages = new HashMap<String, MessageModel>();

			for (Entry<String, TopicModel> topicEntry : listTopics.entrySet()) {
				TopicModel topic = topicEntry.getValue();
				activitesView.updateProgress();
				List<String> listMessagesPagesUrls = new ArrayList<String>(topic.getListMessagesPagesUrls());
				int idTopic = topic.getId();
				int nbreVuesTopic = topic.getNbreVues();
				String titreTopic = topic.getTitle();
				int nbrePagesMessages = listMessagesPagesUrls.size();
				System.out.println("\t\t\t\t\t\t\t\t> Connexion au topic n°" + numTopic + " (" + titreTopic + ")");
				activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AjoutMessages") + " : Topic "
						+ numTopic + " / " + listTopics.size());
				TreeMap<String, MessageModel> newMapMessagesFromMessagesPage = new TreeMap<String, MessageModel>();
				int numPageMessages = 1;
				for (String messagesPageUrl : listMessagesPagesUrls) {
					System.out.println("\t\t\t\t\t\t\t\t\t> Connexion à la page de messages n°" + numPageMessages + " ("
							+ messagesPageUrl + ")");

					if (listMessagesPagesUrls.size() > 1)
						activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AjoutMessages")
								+ " : Topic " + numTopic + " / " + listTopics.size() + " | "
								+ bundleOperationsListe.getString("txt_AjoutMessagesPageMessages") + " "
								+ numPageMessages + "/" + nbrePagesMessages);

					doc = connectToUrl.getDocumentFromUrl(messagesPageUrl);

					if (doc != null) {
						utils.setMessageWrapperType(forumWrapperType, doc);
						Elements messagesWrappers = utils.getMessagesWrappers();
						if (messagesWrappers.size() > 0) {
							ExtractMessages extractMessages = new ExtractMessages(messagesWrappers, sleepTime,
									messageWrapperType, idTopic, titreTopic, nbreVuesTopic, forumSelected.getNom());
							extractMessages.extract();
							// System.out.println(extractMessages.getMapIdMessages().size()
							// + " messages ont été extraits du topic d'id " +
							// idTopic);
							newMapMessagesFromMessagesPage.putAll(extractMessages.getMapIdMessages());
							// System.out.println(
							// "newMapMessagesFromMessagesPage passe à " +
							// newMapMessagesFromMessagesPage.size());
						}
					}
					exceptionMsg = connectToUrl.getExceptionMsg();
					exceptionsSet.add(exceptionMsg);
					numPageMessages++;
				}
				numTopic++;

				if (newMapMessagesFromMessagesPage.size() > 0) {
					newMapIdMessages.putAll(utils.getTreeMapMessagesTopicWithInReplyTo(newMapMessagesFromMessagesPage));
				}			
			}

			System.out.println("newMapIdMessages passe à " + newMapIdMessages.size());	
			
			// Si la MAP Finale de messages est vide => veut dire
			// qu'il y a eu
			// des erreurs de connexions ou qu'on n'a trouvé aucun
			// message sur les pages de messages recherchées
			// => Return

			if (newMapIdMessages.size() == 0) {
				// System.out.println("exceptionsArray size = " +
				// exceptionsArray.size());
				// Traitement des erreurs de connexions
				if (exceptionsSet.size() > 0) {
					displayConnexionErrors(exceptionsSet);
				} else {
					System.out.println("Aucun message n'a été trouvé ");
					JOptionPane.showMessageDialog(null, "Aucun message trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
				displayDialogForums(listForums);
				return;
			} else
				setNewMapIdMessages(newMapIdMessages);
		}
	}

	public void setNewMapIdMessages(Map<String, MessageModel> newMapIdMessages) {
		this.newMapIdMessages = newMapIdMessages;
	}

	public Map<String, MessageModel> getNewMapIdMessages() {
		return newMapIdMessages;
	}

	public ForumModel getForumSelected() {
		return forumSelected;
	}

	private void setForumSelected(ForumModel forum) {
		this.forumSelected = forum;
	}

	private void displayConnexionErrors(Set<String> exceptionsSet) {
		// // on analyse les exceptions de connexion
		// // on affiche l'erreur puis le dialogue de saisie de l'url puis on
		// // sort
		// if (connectToUrl.getException() != 0) {
		// if (connectToUrl.getException() == 1) {
		// // Exception 1 = SocketTimeoutException
		// JOptionPane.showMessageDialog(null,
		// bundleOperationsListe.getString("txt_ErreurTimedOut") + "\n"
		// + connectToUrl.getExceptionMsg(),
		// bundleOperationsListe.getString("txt_ErreurTimedOut"),
		// JOptionPane.ERROR_MESSAGE);
		// } else if (connectToUrl.getException() == 2) {
		// // Exception 2 = IOException
		// JOptionPane.showMessageDialog(null,
		// bundleOperationsListe.getString("txt_ErreurIO") + " : \n" +
		// connectToUrl.getExceptionMsg(),
		// bundleOperationsListe.getString("txt_ErreurIO"),
		// JOptionPane.ERROR_MESSAGE);
		// } else if (connectToUrl.getException() == 3) {
		// // Exception 503 = Surcharge ou maintenance du
		// // serveur
		// JOptionPane.showMessageDialog(null,
		// bundleOperationsListe.getString("txt_AugmenterLatenceTelechargement"),
		// bundleOperationsListe.getString("txt_ErreurSurcharge"),
		// JOptionPane.ERROR_MESSAGE);
		// } else
		// JOptionPane.showMessageDialog(null,
		// bundleOperationsListe.getString("txt_ErreurHttp") + " " +
		// connectToUrl.getExceptionMsg(),
		// bundleOperationsListe.getString("txt_ErreurHttp"),
		// JOptionPane.ERROR_MESSAGE);
		// displayDialogConnexion();
		// return;
		// }
		String errorMessage = "Des erreurs sont apparues. Le processus qui vient de s'achever est peut être incomplet.\n Messages d'erreur : \n";
		for (String exception : exceptionsSet)
			if (exception != null)
				errorMessage += exception + "\n";
		JOptionPane.showMessageDialog(null, errorMessage, bundleOperationsListe.getString("txt_ErreurIO"),
				JOptionPane.ERROR_MESSAGE);
	}
}
