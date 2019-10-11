package controleurs.operations.liste.ajoutmessages.extractify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Document;

import comparators.MapTopicUrlTopicNumComparator;
import modeles.ForumModel;
import modeles.LevelModel;
import modeles.MessageModel;
import modeles.TopicModel;
import modeles.LevelModel.Pagination;
import modeles.LevelModel.Row;
import modeles.LevelModel.Row.Col;
import modeles.LevelModel.Row.Depth;
import vue.dialog.DialogPanelAjoutForum;
import vue.dialog.DialogPanelAjoutTopic;
import vue.dialog.DialogPanelChoixForum;
import vue.dialog.DialogPanelExtractifyFieldsMatching;
import vue.dialog.DialogPanelSetProxy;
import controleurs.operations.liste.CalculSujetTronque;
import controleurs.operations.liste.ajoutmessages.AddMessagesUtils;
import controleurs.operations.liste.ajoutmessages.forum.ConnectToUrl;
import controleurs.operations.projet.AddListe;
import controleurs.vuesabstraites.ProjetView;

public class AddMessagesFromExtractify {

	private ResourceBundle bundleOperationsListe;
	private ProjetView activitesView;
	private Map<String, MessageModel> newMapIdMessages;
//	private ForumModel selectedForum;
//	private String nomForumEnCours;
//	private ConnectToUrl connectToUrl = new ConnectToUrl();
//	private Document doc = null;
//	private LevelModel forumLevel, topicLevel, messageLevel;
	private String repertoire;
//	private String exceptionMsg;
//	private Set<String> exceptionsSet;
//	private List<ForumModel> listForums;
	private AddMessagesUtils utils;
	private List<LevelObject> levels = new ArrayList<LevelObject>();
	private JSONArray jsonLevels;
	private Map<String, List<LameProperty>> lameTypePropertiesMap = new LinkedHashMap<String, List<LameProperty>>();
	private int idMessage = 0;
	private Map<String, String> parseErrors = new HashMap<String, String>();
	private String topicTitle = "";

	public AddMessagesFromExtractify(ResourceBundle bundleOperationsListe, ProjetView activitesView,
			String repertoire) {
		this.bundleOperationsListe = bundleOperationsListe;
		this.activitesView = activitesView;
		this.repertoire = repertoire;
		utils = new AddMessagesUtils();

		List<LameProperty> forumTypeProperties = new ArrayList<LameProperty>();

		forumTypeProperties.add(new LameProperty("nomForum", "\t\t" + bundleOperationsListe.getString("txt_NomForum")));
		forumTypeProperties.add(new LameProperty("urlForum", "\t\t" + bundleOperationsListe.getString("txt_UrlForum")));

		lameTypePropertiesMap.put("*" + bundleOperationsListe.getString("txt_Forum"), forumTypeProperties);

		List<LameProperty> topicTypeProperties = new ArrayList<LameProperty>();

		topicTypeProperties
				.add(new LameProperty("titreTopic", "\t\t" + bundleOperationsListe.getString("txt_TitreTopic")));
		topicTypeProperties.add(new LameProperty("urlTopic", "\t\t" + bundleOperationsListe.getString("txt_UrlTopic")));
		topicTypeProperties
				.add(new LameProperty("nbrVuesTopic", "\t\t" + bundleOperationsListe.getString("txt_NbrVuesTopic")));

		lameTypePropertiesMap.put("*" + bundleOperationsListe.getString("txt_Topic"), topicTypeProperties);

		List<LameProperty> messageTypeProperties = new ArrayList<LameProperty>();

		messageTypeProperties
				.add(new LameProperty("dateMessage", "\t\t" + bundleOperationsListe.getString("txt_DateMessage")));
		messageTypeProperties
				.add(new LameProperty("corpsMessage", "\t\t" + bundleOperationsListe.getString("txt_CorpsMessage")));

		lameTypePropertiesMap.put("*" + bundleOperationsListe.getString("txt_Message"), messageTypeProperties);

		List<LameProperty> authorTypeProperties = new ArrayList<LameProperty>();

		authorTypeProperties
				.add(new LameProperty("nomAuteur", "\t\t" + bundleOperationsListe.getString("txt_NomAuteur")));
		authorTypeProperties.add(new LameProperty("localisationAuteur",
				"\t\t" + bundleOperationsListe.getString("txt_LocalisationAuteur")));
		authorTypeProperties.add(new LameProperty("nbreMessagesAuteur",
				"\t\t" + bundleOperationsListe.getString("txt_NbreMessagesAuteur")));
		authorTypeProperties.add(new LameProperty("dateInscriptionAuteur",
				"\t\t" + bundleOperationsListe.getString("txt_DateInscriptionAuteur")));
		authorTypeProperties
				.add(new LameProperty("statutAuteur", "\t\t" + bundleOperationsListe.getString("txt_StatutAuteur")));
		authorTypeProperties.add(
				new LameProperty("reputationAuteur", "\t\t" + bundleOperationsListe.getString("txt_ReputationAuteur")));
		authorTypeProperties
				.add(new LameProperty("genreAuteur", "\t\t" + bundleOperationsListe.getString("txt_GenreAuteur")));
		authorTypeProperties
				.add(new LameProperty("ageAuteur", "\t\t" + bundleOperationsListe.getString("txt_AgeAuteur")));
		authorTypeProperties.add(
				new LameProperty("signatureAuteur", "\t\t" + bundleOperationsListe.getString("txt_SignatureAuteur")));
		authorTypeProperties.add(
				new LameProperty("positionAuteur", "\t\t" + bundleOperationsListe.getString("txt_PositionAuteur")));
		authorTypeProperties
				.add(new LameProperty("emailAuteur", "\t\t" + bundleOperationsListe.getString("txt_EmailAuteur")));
		authorTypeProperties
				.add(new LameProperty("websiteAuteur", "\t\t" + bundleOperationsListe.getString("txt_WebsiteAuteur")));
		authorTypeProperties.add(
				new LameProperty("activiteAuteur", "\t\t" + bundleOperationsListe.getString("txt_ActiviteAuteur")));

		lameTypePropertiesMap.put("*" + bundleOperationsListe.getString("txt_Auteur"), authorTypeProperties);
	}

	public void extractJsonData() {
//		forumLevel = null;
//		topicLevel = null;
//		messageLevel = null;

		activitesView.resetProgress();
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_TraitementRepertoire") + " - "
				+ bundleOperationsListe.getString("txt_Patientez"));
		activitesView.getProgressBar().setIndeterminate(true);
		activitesView.getProgressBar().setStringPainted(false);
		activitesView.updateProgress();
		JFileChooser fc = new JFileChooser();
		FileFilter objFilter = new FileNameExtensionFilter(bundleOperationsListe.getString("txt_FichiersJSon"), "json");
		fc.setFileFilter(objFilter);
		fc.setCurrentDirectory(new File(repertoire));
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setDialogTitle(bundleOperationsListe.getString("txt_AjouterMessages"));
		int returnVal = fc.showOpenDialog(fc.getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			final File file = fc.getSelectedFile();
			if (file.isFile()) {
				if (file.getName().endsWith(".json")) {
					// on essaye de parser le fichier
					JSONParser parser = new JSONParser();
					System.out.println("jsonFile = " + file.getPath());
					Object object = null;
					try {
						object = parser.parse(new FileReader(file));
						// System.out.println("read object = " + object);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					// on récupère les types du premier objet pour le field matching
					if (object != null && object instanceof JSONArray) {
						jsonLevels = (JSONArray) object;
						JSONObject firstLevel = (JSONObject) jsonLevels.get(0);
						parseFirstLevelType(firstLevel);

						if (levels.size() > 0) {
							System.out.println("levelObjects size = " + levels.size());
							for (LevelObject level : levels) {
								System.out.println(
										"type = " + level.getType() + " | properties = " + level.getProperties());
							}
							activitesView.setLabelProgress(bundleOperationsListe.getString("txt_TraitementRepertoire"));
							activitesView.appendTxtArea(bundleOperationsListe.getString("txt_TraitementRepertoire")
									+ " " + bundleOperationsListe.getString("txt_Accompli") + "\n");
							activitesView.getProgressBar().setIndeterminate(false);
							activitesView.getProgressBar().setStringPainted(true);
							displayFieldsMatching();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null,
							bundleOperationsListe.getString("txt_MauvaisFormatFichier") + ".",
							bundleOperationsListe.getString("txt_AjoutMessages"), JOptionPane.ERROR_MESSAGE);
					activitesView.setLabelProgress(bundleOperationsListe.getString("txt_TraitementRepertoire"));
					activitesView.appendTxtArea(bundleOperationsListe.getString("txt_TraitementRepertoire") + " "
							+ bundleOperationsListe.getString("txt_Echoue") + "\n");
					activitesView.getProgressBar().setIndeterminate(false);
					activitesView.getProgressBar().setStringPainted(true);
					extractJsonData();
				}
			} else {
				JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_MauvaisFormatFichier") + ".",
						bundleOperationsListe.getString("txt_AjoutMessages"), JOptionPane.ERROR_MESSAGE);
				activitesView.setLabelProgress(bundleOperationsListe.getString("txt_TraitementRepertoire"));
				activitesView.appendTxtArea(bundleOperationsListe.getString("txt_TraitementRepertoire") + " "
						+ bundleOperationsListe.getString("txt_Echoue") + "\n");
				activitesView.getProgressBar().setIndeterminate(false);
				activitesView.getProgressBar().setStringPainted(true);
				extractJsonData();
			}
		} else {
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_TraitementRepertoire"));
			activitesView.appendTxtArea(bundleOperationsListe.getString("txt_TraitementRepertoire") + " "
					+ bundleOperationsListe.getString("txt_Echoue") + "\n");
			activitesView.getProgressBar().setIndeterminate(false);
			activitesView.getProgressBar().setStringPainted(true);
		}
	}

	@SuppressWarnings("unchecked")
	private void parseFirstLevelType(JSONObject firstLevel) {
		String levelType = (String) firstLevel.get("type");
		LevelObject level = new LevelObject(levelType);
		ArrayList<String> levelProperties = new ArrayList<String>();
		level.setProperties(levelProperties);
		for (Object property : firstLevel.keySet()) {
			Object value = firstLevel.get(property);
			System.out.println("property: " + property + " value: " + value);
			if (!property.equals("type")) {
				if (!property.equals("deeperLevel")) {
					level.getProperties().add((String) property);
				} else {
					JSONArray deeperLevelArray = (JSONArray) value;
					if (deeperLevelArray.size() > 0) {
						JSONObject firstDeeperLevel = (JSONObject) deeperLevelArray.get(0);
						parseFirstLevelType((JSONObject) firstDeeperLevel);
					}
				}
			}
		}
		levels.add(level);
	}

	public void displayFieldsMatching() {
		DialogPanelExtractifyFieldsMatching fieldsMatchingDialog = new DialogPanelExtractifyFieldsMatching(
				bundleOperationsListe, levels, lameTypePropertiesMap);
		int result = JOptionPane.showOptionDialog(null, fieldsMatchingDialog,
				bundleOperationsListe.getString("txt_JsonLameFieldsMatching"), JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			List<JComboBox<Integer>> comboList = fieldsMatchingDialog.getComboList();
			Map<String, String> matchingForumProperties = new HashMap<String, String>();
			Map<String, String> matchingTopicProperties = new HashMap<String, String>();
			Map<String, String> matchingMessageProperties = new HashMap<String, String>();
			Map<String, String> matchingAuteurProperties = new HashMap<String, String>();
			for (JComboBox<Integer> combo : comboList) {
				Object lamePropertyObject = combo.getSelectedItem();
				if (lamePropertyObject instanceof LameProperty) {
					String lameProperty = ((LameProperty) lamePropertyObject).getName().replaceAll("\t", "");
					String levelProperty = combo.getName();
					if (lameProperty.indexOf("Forum") != -1) {
						if (!matchingForumProperties.containsKey(levelProperty))
							matchingForumProperties.put(levelProperty, lameProperty);
					} else if (lameProperty.indexOf("Topic") != -1) {
						if (!matchingTopicProperties.containsKey(levelProperty))
							matchingTopicProperties.put(levelProperty, lameProperty);
					} else if (lameProperty.indexOf("Message") != -1) {
						if (!matchingMessageProperties.containsKey(levelProperty))
							matchingMessageProperties.put(levelProperty, lameProperty);
					} else if (lameProperty.indexOf("Auteur") != -1) {
						if (!matchingAuteurProperties.containsKey(levelProperty))
							matchingAuteurProperties.put(levelProperty, lameProperty);
					}
				}
			}
			System.out.println("matchingForumProperties : " + matchingForumProperties);
			System.out.println("matchingTopicProperties : " + matchingTopicProperties);
			System.out.println("matchingMessageProperties : " + matchingMessageProperties);
			System.out.println("matchingAuteurProperties : " + matchingAuteurProperties);

			if (matchingMessageProperties.size() == 0) {
				JOptionPane.showMessageDialog(null,
						bundleOperationsListe.getString("txt_AucuneProprieteMessagesSelectionnee"), "Information",
						JOptionPane.INFORMATION_MESSAGE);
				displayFieldsMatching();
			} else {
				List<String> messagesProperties = new ArrayList<String>(matchingMessageProperties.values());
				List<String> authorProperties = new ArrayList<String>(matchingAuteurProperties.values());
				if (!messagesProperties.contains("dateMessage") || !messagesProperties.contains("corpsMessage")
						|| !authorProperties.contains("nomAuteur")) {
					JOptionPane.showMessageDialog(null,
							bundleOperationsListe.getString("txt_TropPeuProprieteMessagesSelectionnee"), "Information",
							JOptionPane.INFORMATION_MESSAGE);
					displayFieldsMatching();
				} else {
					if (matchingForumProperties.size() > 0) {
						List<ForumModel> forumsList = getJsonForums(matchingForumProperties, matchingTopicProperties,
								matchingMessageProperties, matchingAuteurProperties);
						if (forumsList.size() > 0) {
							System.out.println(forumsList.size() + " forums récupérés");

							activitesView.resetProgress();
							activitesView.setStepProgress(forumsList.size());

							int i = 1;
							for (ForumModel forum : forumsList) {
								activitesView.setLabelProgress(bundleOperationsListe.getString("txt_ExtractionForum")
										+ " : Forum " + i + " / " + forumsList.size());
								System.out.println("forum : " + forum.getNom());
								Map<String, TopicModel> topicsMap = getJsonTopics(forum, matchingTopicProperties,
										matchingMessageProperties, matchingAuteurProperties);
								TreeMap<String, MessageModel> forumMessageMap = new TreeMap<String, MessageModel>();
								forum.setTopicsMap(topicsMap);
								if (topicsMap.size() > 0) {
									System.out.println("topics : (" + topicsMap.size() + " éléments)");
									int j = 0;
									activitesView
											.setLabelProgress(bundleOperationsListe.getString("txt_ExtractionTopic")
													+ " : Forum " + i + " > Topic " + j + " / " + topicsMap.size());

									for (Entry<String, TopicModel> entry : topicsMap.entrySet()) {
										TreeMap<String, MessageModel> topicMessageMap;
										TopicModel topic = entry.getValue();
										System.out.println(
												topic.getId() + " | " + topic.getUrl() + " | " + topic.getTitle()
														+ " | " + topic.getUrl() + " | " + topic.getNbreVues());
										topicMessageMap = getJsonMessages(forum, topic, matchingMessageProperties,
												matchingAuteurProperties);
										if (topicMessageMap.size() > 0) {
											for (Entry<String, MessageModel> entry2 : topicMessageMap.entrySet()) {
												MessageModel message = entry2.getValue();
												System.out.println("\t\tmessage n°" + message.getIdentifiant()
														+ " (topic n°" + message.getIdConversation() + ") : "
														+ message.getExpediteur() + " | " + message.getDateUS() + " | "
														+ message.getText().length() + " car. ");
											}
											TreeMap<String, MessageModel> topicMessagesMapWithInReplyTo = utils
													.getTreeMapMessagesTopicWithInReplyTo(topicMessageMap);
											idMessage += topicMessageMap.size();
											forumMessageMap.putAll(topicMessagesMapWithInReplyTo);
										}
										activitesView.updateProgress();
										j++;
									}
								} else
									parseErrors.put(bundleOperationsListe.getString("txt_Topic"),
											bundleOperationsListe.getString("txt_MatchingFieldsError"));
								if (forumMessageMap.size() > 0) {
									activitesView.appendTxtArea(bundleOperationsListe.getString("txt_Forum") + " "
											+ forum.getNom() + " : " + forumMessageMap.size() + " "
											+ bundleOperationsListe.getString("txt_MessagesExtraits") + "\n");
									activitesView.getProjetController().notifyProjetCreateListFromJsonForum(forum,
											forumMessageMap);
									activitesView.getProjetController().getListeController().notifyExtractData();
								}
								activitesView.updateProgress();
								i++;
							}
						} else
							parseErrors.put(bundleOperationsListe.getString("txt_Forum"),
									bundleOperationsListe.getString("txt_MatchingFieldsError"));
					} else if (matchingTopicProperties.size() > 0) {
						ForumModel forum = new ForumModel("forumUrl");
						forum.setNom(activitesView.getProjetController().getListeSelected().getNom());
						System.out.println("forum : " + forum.getNom());
						Map<String, TopicModel> topicsMap = getJsonTopics(forum, matchingTopicProperties,
								matchingMessageProperties, matchingAuteurProperties);
						TreeMap<String, MessageModel> forumMessageMap = new TreeMap<String, MessageModel>();
						forum.setTopicsMap(topicsMap);
						if (topicsMap.size() > 0) {
							System.out.println("topics : (" + topicsMap.size() + " éléments)");
							activitesView.resetProgress();
							activitesView.setStepProgress(topicsMap.size());
							int j = 1;

							for (Entry<String, TopicModel> entry : topicsMap.entrySet()) {
								activitesView.setLabelProgress(bundleOperationsListe.getString("txt_ExtractionTopic")
										+ " : Forum " + forum.getNom() + " > Topic " + j + " / " + topicsMap.size());
								TreeMap<String, MessageModel> topicMessageMap;
								TopicModel topic = entry.getValue();
								System.out.println(topic.getId() + " | " + topic.getUrl() + " | " + topic.getTitle()
										+ " | " + topic.getUrl() + " | " + topic.getNbreVues());
								topicMessageMap = getJsonMessages(forum, topic, matchingMessageProperties,
										matchingAuteurProperties);
								if (topicMessageMap.size() > 0) {
									for (Entry<String, MessageModel> entry2 : topicMessageMap.entrySet()) {
										MessageModel message = entry2.getValue();
										System.out.println("\t\tmessage n°" + message.getIdentifiant() + " (topic n°"
												+ message.getIdConversation() + ") : " + message.getExpediteur() + " | "
												+ message.getDateUS() + " | " + message.getText().length() + " car. ");
									}
									TreeMap<String, MessageModel> topicMessagesMapWithInReplyTo = utils
											.getTreeMapMessagesTopicWithInReplyTo(topicMessageMap);
									idMessage += topicMessageMap.size();
									forumMessageMap.putAll(topicMessagesMapWithInReplyTo);
								}
								activitesView.updateProgress();
								j++;
							}
						}
						if (forumMessageMap.size() > 0) {
							activitesView.appendTxtArea(bundleOperationsListe.getString("txt_Forum") + " "
									+ forum.getNom() + " : " + forumMessageMap.size() + " "
									+ bundleOperationsListe.getString("txt_MessagesExtraits") + "\n");
							activitesView.getProjetController().getListeSelected().addMapIdMessages(forumMessageMap);
							activitesView.getProjetController().getListeController().listeMessagesChanged();
							activitesView.getProjetController().getListeController().notifyExtractData();
							activitesView.getProjetController().getListeController().notifyAnalyseData();
						}
					} else if (matchingMessageProperties.size() > 0) {
						ForumModel forum = new ForumModel("forumUrl");
						forum.setNom(activitesView.getProjetController().getListeSelected().getNom());
						openDialogTopicTitle(bundleOperationsListe);
						if (!getTopicTitle().equals("")) {
							TopicModel topic = new TopicModel();
							topic.setTitle(getTopicTitle());
							topic.setUrl("topicUrl");
							activitesView.resetProgress();
							TreeMap<String, MessageModel> forumMessageMap;
							forumMessageMap = getJsonMessages(forum, topic, matchingMessageProperties,
									matchingAuteurProperties);
							if (forumMessageMap.size() > 0) {
								for (Entry<String, MessageModel> entry2 : forumMessageMap.entrySet()) {
									MessageModel message = entry2.getValue();
									System.out.println("\t\tmessage n°" + message.getIdentifiant() + " (topic n°"
											+ message.getIdConversation() + ") : " + message.getExpediteur() + " | "
											+ message.getDateUS() + " | " + message.getText().length() + " car. ");
								}
								TreeMap<String, MessageModel> topicMessagesMapWithInReplyTo = utils
										.getTreeMapMessagesTopicWithInReplyTo(forumMessageMap);
								idMessage += forumMessageMap.size();
								forumMessageMap.putAll(topicMessagesMapWithInReplyTo);
								activitesView.appendTxtArea(bundleOperationsListe.getString("txt_Forum") + " "
										+ forum.getNom() + " : " + forumMessageMap.size() + " "
										+ bundleOperationsListe.getString("txt_MessagesExtraits") + "\n");
								activitesView.getProjetController().getListeSelected()
										.addMapIdMessages(forumMessageMap);
								activitesView.getProjetController().getListeController().listeMessagesChanged();
								activitesView.getProjetController().getListeController().notifyExtractData();
								activitesView.getProjetController().getListeController().notifyAnalyseData();
							}
							activitesView.updateProgress();
						}
					}
					if (parseErrors.size() > 0) {
						String s = "";
						for (Entry<String, String> entry : parseErrors.entrySet()) {
							s += entry.getKey() + " : " + entry.getValue() + "\n";
						}
						JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_Erreurs") + s,
								"Information", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
	}

	private void openDialogTopicTitle(ResourceBundle bundleOperationsListe) {
		DialogPanelAjoutTopic ajoutTopicDialog = new DialogPanelAjoutTopic(bundleOperationsListe);
		int result = JOptionPane.showOptionDialog(null, ajoutTopicDialog,
				bundleOperationsListe.getString("txt_SaisirTitreTopic"), JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			if (!ajoutTopicDialog.getTopicTitle().equals(""))
				setTopicTitle(ajoutTopicDialog.getTopicTitle());
			else {
				JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ErreurTitreTopic"),
						"Information", JOptionPane.INFORMATION_MESSAGE);
				openDialogTopicTitle(bundleOperationsListe);
			}
		}
	}

	private String getTopicTitle() {
		return topicTitle;
	}

	private void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}

	private List<ForumModel> getJsonForums(Map<String, String> matchingForumProperties,
			Map<String, String> matchingTopicProperties, Map<String, String> matchingMessageProperties,
			Map<String, String> matchingAuteurProperties) {
		ExtractForums extractForums = new ExtractForums(jsonLevels, matchingForumProperties, bundleOperationsListe);
		extractForums.extract();
		parseErrors.putAll(extractForums.getForumParseErrors());
		List<ForumModel> forumsList = extractForums.getForumsList();
		return forumsList;
	}

	private Map<String, TopicModel> getJsonTopics(ForumModel forum, Map<String, String> matchingTopicProperties,
			Map<String, String> matchingMessageProperties, Map<String, String> matchingAuteurProperties) {

		JSONArray jsonTopics = (JSONArray) forum.getDeeperLevel();
		if (jsonTopics == null)
			jsonTopics = jsonLevels;

		ExtractTopics extractTopics = new ExtractTopics(jsonTopics, matchingTopicProperties, bundleOperationsListe);
		extractTopics.extract();
		parseErrors.putAll(extractTopics.getTopicParseErrors());
		Map<String, TopicModel> topicsMap = extractTopics.getTopicsMap();

		return topicsMap;
	}

	private TreeMap<String, MessageModel> getJsonMessages(ForumModel forum, TopicModel topic,
			Map<String, String> matchingMessageProperties, Map<String, String> matchingAuteurProperties) {

		JSONArray jsonMessages = (JSONArray) topic.getDeeperLevel();
		if (jsonMessages == null)
			jsonMessages = jsonLevels;

		ExtractMessages extractMessages = new ExtractMessages(idMessage, jsonMessages, forum, topic,
				matchingMessageProperties, matchingAuteurProperties, bundleOperationsListe);
		extractMessages.extract();
		parseErrors.putAll(extractMessages.getMessageParseErrors());
		TreeMap<String, MessageModel> messagesMap = extractMessages.getMapMessages();

		return messagesMap;
	}

	public void setNewMapIdMessages(Map<String, MessageModel> newMapIdMessages) {
		this.newMapIdMessages = newMapIdMessages;
	}

	public Map<String, MessageModel> getNewMapIdMessages() {
		return newMapIdMessages;
	}

	public class LevelObject {
		private String type;
		private ArrayList<String> properties;

		public LevelObject(String type) {
			this.type = type;
		}

		public void setProperties(ArrayList<String> levelProperties) {
			this.properties = levelProperties;
		}

		public String getType() {
			return type;
		}

		public ArrayList<String> getProperties() {
			return properties;
		}
	}

	public class LameProperty {
		private String name;
		private String value;

		public LameProperty(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		public String toString() {
			return name;
		}

		public String getSuffixSelectedItem() {
			String suffix = "";
			String propertyName = getName();
			if (propertyName.endsWith(bundleOperationsListe.getString("txt_Auteur")))
				suffix = " (" + bundleOperationsListe.getString("txt_AuteurMin") + ")";
			else if (propertyName.endsWith(bundleOperationsListe.getString("txt_Message")))
				suffix = " (" + bundleOperationsListe.getString("txt_MessageMin") + ")";
			else if (propertyName.endsWith(bundleOperationsListe.getString("txt_Topic")))
				suffix = " (" + bundleOperationsListe.getString("txt_TopicMin") + ")";
			else if (propertyName.endsWith(bundleOperationsListe.getString("txt_Forum")))
				suffix = " (" + bundleOperationsListe.getString("txt_ForumMin") + ")";
			return getValue() + suffix;
		}
	}

}
