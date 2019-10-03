package controleurs.operations.liste.ajoutmessages;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import comparators.MapStringArraySizeComparator;
import comparators.MapStringIntegerComparator;
import comparators.MessageDateUsComparator;
import controleurs.operations.liste.CalculSujetTronque;
import modeles.ForumModel;
import modeles.LevelModel;
import modeles.MessageModel;
import modeles.TopicModel;
import modeles.LevelModel.Pagination;
import modeles.LevelModel.Row;
import modeles.LevelModel.Row.Col;

public class AddMessagesUtils {

	private Elements topicsWrappers = null, messagesWrappers;
	private int topicWrapperType = 0, messageWrapperType;
	private int nbreTopicsParPage, nbrePagesTopics, lastPageTopics;
	private HashMap<String, TopicModel> listTopics;
	private TreeMap<String, MessageModel> listMessages;
	private String messageDateFormat = null;
	private String messageDatePattern = null;
	private String messageTimeFormat = null;
	private String messageTimePattern = null;
	private int elementNum, locuteurId = 0;
	private Map<String, Integer> mapNomIdLocuteur = new HashMap<String, Integer>();

	public AddMessagesUtils() {
	}

	public void extractInformationsFromPage(Document doc, LevelModel level, ResourceBundle bundleOperationsListe,
			int elementNum, AddMessagesUtils utils, String topicTitle, int topicId, int topicViews, String forumName) {
		Elements elementsTags = new Elements();
		listTopics = new HashMap<String, TopicModel>();
		listMessages = new TreeMap<String, MessageModel>();
		this.elementNum = elementNum;

		for (Row row : level.getRows()) {
			String rowTagName = row.getTagName();
			String rowClassName = row.getClassName();
			elementsTags.addAll(doc.select(rowTagName + rowClassName));
		}

		// System.out.println("Taille de elementsTags = " + elementsTags.size() + " :\n"
		// + elementsTags);

		if (elementsTags.size() > 0) {
			// TOPICS
			Set<String> topicTitleTagsAndClasses = new HashSet<String>();
			Set<String> topicViewsTagsAndClasses = new HashSet<String>();
			Set<String> topicTitleTags = new HashSet<String>();
			Set<String> topicViewsTags = new HashSet<String>();
			Set<String> topicTitleClasses = new HashSet<String>();
			Set<String> topicViewsClasses = new HashSet<String>();
			// MESSAGES
			Set<String> messageAuteurTagsAndClasses = new HashSet<String>();
			Set<String> messageDateTagsAndClasses = new HashSet<String>();
			Set<String> messagePostTagsAndClasses = new HashSet<String>();
			Set<String> messageAuteurTags = new HashSet<String>();
			Set<String> messageDateTags = new HashSet<String>();
			Set<String> messagePostTags = new HashSet<String>();
			Set<String> messageAuteurClasses = new HashSet<String>();
			Set<String> messageDateClasses = new HashSet<String>();
			Set<String> messagePostClasses = new HashSet<String>();
			for (Row row : level.getRows()) {
				for (Col col : row.getCols()) {
					String colTitle = col.getTitle();
					// System.out.println("\t\tcol title = " + colTitle + " VS " +
					// bundleOperationsListe.getString("txt_TitreColonneTopic"));
					String colTagName = col.getTagName();
					String colClassName = col.getClassName();
					String colTagAndClass = "";

					// Si la classe contient plusieurs points(.) c'est qu'il y a plusieurs classes
					String[] colClassNames = colClassName.split("\\.");
					List<String> colClassNamesList = new ArrayList<String>(Arrays.asList(colClassNames));
					colClassNamesList.removeAll(Collections.singleton(null));
					colClassNamesList.removeAll(Collections.singleton(""));
					// System.out.println("\tcolClassNamesList : " + colClassNamesList);
					if (colClassNamesList.size() > 1) {
						for (int i = 0; i < colClassNamesList.size(); i++) {
							// Si la classe contient un tiret, on ne garde que le début après le point, et
							// jusqu'au tiret
							colClassName = colClassNamesList.get(i);
							if (colClassName.indexOf("-") != -1) {
								colClassName = colClassName.substring(1, colClassName.indexOf("-"));
								colTagAndClass = colTagName + "[class*=" + colClassName + "]";
							} else
								colTagAndClass += colTagName + "." + colClassName + ",";
						}
					} else
						colTagAndClass = colTagName + colClassName;

					// On supprime la virgule finale éventuelle
					// System.out.println("colTagAndClass = " + colTagAndClass);
					if (colTagAndClass.endsWith(","))
						colTagAndClass = colTagAndClass.substring(0, colTagAndClass.length() - 1);

					if (colTitle.equals(bundleOperationsListe.getString("txt_TitreColonneTopic"))) {
						topicTitleTagsAndClasses.add(colTagAndClass);
						topicTitleTags.add(colTagName);
						topicTitleClasses.add(colClassName);
					} else if (colTitle.equals(bundleOperationsListe.getString("txt_NbreVuesTopic"))) {
						topicViewsTagsAndClasses.add(colTagAndClass);
						topicViewsTags.add(colTagName);
						topicViewsClasses.add(colClassName);
					} else if (colTitle.equals(bundleOperationsListe.getString("txt_TitreColonneAuteur"))) {
						messageAuteurTagsAndClasses.add(colTagAndClass);
						messageAuteurTags.add(colTagName);
						messageAuteurClasses.add(colClassName);
					} else if (colTitle.equals(bundleOperationsListe.getString("txt_TitreColonneMessage"))) {
						messagePostTagsAndClasses.add(colTagAndClass);
						messagePostTags.add(colTagName);
						messagePostClasses.add(colClassName);
					} else if (colTitle.equals(bundleOperationsListe.getString("txt_TitreColonneDateMessage"))) {
						messageDateTagsAndClasses.add(colTagAndClass);
						messageDateTags.add(colTagName);
						messageDateClasses.add(colClassName);
					}
				}
			}

			String topicTitleTagAndClass = StringUtils.join(topicTitleTagsAndClasses, ",");
			String topicViewsTagAndClass = StringUtils.join(topicViewsTagsAndClasses, ",");
			String messageAuteurTagAndClass = StringUtils.join(messageAuteurTagsAndClasses, ",");
			String messagePostTagAndClass = StringUtils.join(messagePostTagsAndClasses, ",");
			String messageDateTagAndClass = StringUtils.join(messageDateTagsAndClasses, ",");

			System.out.println("\t\ttags and classes : topicTitleTagAndClass : " + topicTitleTagAndClass
					+ " | topicViewsTagAndClass : " + topicViewsTagAndClass + " | messageAuteurTagAndClass : "
					+ messageAuteurTagAndClass + " | messagePostTagAndClass : " + messagePostTagAndClass
					+ " | messageDateTagAndClass : " + messageDateTagAndClass);

			for (Element elementTag : elementsTags) {

				String topicUrl = "";
				String messageAuteur = "∅", messageDate = null, messagePost = "∅";
				Date messageFormattedDate = null;

				Element validElement = null;

				if (level.getType().equals("Topic")) {

					// TITRE TOPIC
					if (!topicTitleTagAndClass.equals("")) {
						validElement = getValidElement("Topic title", elementTag, topicTitleTagAndClass, topicTitleTags,
								topicTitleClasses);
						if (validElement != null) {
							topicTitle = validElement.text();
							topicUrl = validElement.absUrl("href");
						}
					}
					// NOMBRE DE VUES TOPIC
					if (!topicViewsTagAndClass.equals("")) {
						validElement = getValidElement("Topic views", elementTag, topicViewsTagAndClass, topicViewsTags,
								topicViewsClasses);
						if (validElement != null) {
							String viewsElemTxt = validElement.text();
							viewsElemTxt = viewsElemTxt.replaceAll("[^0-9]", "");
							if (NumberUtils.isNumber(viewsElemTxt))
								topicViews = Integer.parseInt(viewsElemTxt);
						}
					}
					// ON AJOUTE A LA LISTE
					if (!topicTitle.equals("") && !topicUrl.equals("")) {
						fillTopicsList(this.elementNum, topicTitle, topicUrl, topicViews);
						this.elementNum++;
					} else
						System.out.println("AJOUT DE TOPIC : UN ELEMENT EST VIDE");

				} else if (level.getType().equals("Message")) {

					// AUTEUR MESSAGE
					if (!messageAuteurTagAndClass.equals("")) {
						validElement = getValidElement("Message author", elementTag, messageAuteurTagAndClass,
								messageAuteurTags, messageAuteurClasses);
						if (validElement != null) {
							messageAuteur = validElement.text();
						}
					}
					// DATE MESSAGE
					if (!messageDateTagAndClass.equals("")) {
						validElement = getValidElement("Message date", elementTag, messageDateTagAndClass,
								messageDateTags, messageDateClasses);
						if (validElement != null) {
							messageDate = validElement.text();
							messageDate = messageDate.replaceAll("\u00A0", " ");
							messageDate = messageDate.replaceAll(",", " ");
							messageDate = messageDate.replaceAll("h", ":");
							messageDate = messageDate.trim().replaceAll(" +", " ");
							messageDate = cleanFrMonth(messageDate);
							// System.out.println("\t\t\tDate avant : " + messageDate);
							setMessageFormatDate(messageDate);
							setMessageFormatTime(messageDate);
							// System.out.println("\t\t\tmessageDateFormat : " + messageDateFormat
							// + " | messageDatePattern : " + messageDatePattern + " | tmessageTimeFormat :
							// "
							// + messageTimeFormat + " | tmessageTimePattern : " + messageTimePattern);
							if (messageDateFormat != null && messageDatePattern != null) {
								DateFormat frFormat = null;
								if (messageTimeFormat != null && messageTimePattern != null) {
									frFormat = new SimpleDateFormat(messageDateFormat + " " + messageTimeFormat,
											Locale.FRENCH);
									messageDatePattern += " " + messageTimePattern;
								} else
									frFormat = new SimpleDateFormat(messageDateFormat, Locale.FRENCH);
								try {
									messageFormattedDate = frFormat.parse(messageDatePattern);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
					// POST MESSAGE
					if (!messagePostTagAndClass.equals("")) {
						validElement = getValidElement("Message post", elementTag, messagePostTagAndClass,
								messagePostTags, messagePostClasses);
						if (validElement != null) {
							messagePost = validElement.text();
						}
					}

					if (String.valueOf(this.elementNum) != null && messageAuteur != null && messageFormattedDate != null
							&& messagePost != null) {
						fillMessagesList(topicTitle, topicId, topicViews, forumName, String.valueOf(this.elementNum),
								messageAuteur, messageFormattedDate, messagePost);
						this.elementNum++;
					} else
						System.out.println("AJOUT DE MESSAGE : UN ELEMENT EST NULL");
				}
			}
		}
	}

	public int getElementNum() {
		return elementNum;
	}

	private Element getValidElement(String elementType, Element elementTag, String elementTagAndClass,
			Set<String> elementTags, Set<String> elementClasses) {
		Element validElement = elementTag.select(elementTagAndClass).first();
		if (validElement == null) {
			// System.out.println("\t" + elementType + " : validElement est null");
			ArrayList<String> elementTagsArray = new ArrayList<String>(elementTags);
			String elemTag = elementTagsArray.get(0);
			ArrayList<String> elementClassesArray = new ArrayList<String>(elementClasses);
			for (int i = 0; i < elementClassesArray.size(); i++) {
				String elemClass = elementClassesArray.get(i);
				if (!elemClass.equals("")) {
					// System.out.println("\t\t=> elemTag = " + elemTag + " | elemClass = " +
					// elemClass);
					validElement = elementTag.select(elemClass + " " + elemTag).first();
					if (validElement != null)
						break;
				}
			}
		}
		// else
		// System.out.println("\t" + elementType + " : validElement n'est pas null");
		return validElement;
	}

	private void fillTopicsList(int topicNum, String topicTitle, String topicUrl, int topicViews) {
		TopicModel newTopic = new TopicModel(topicUrl);
		newTopic.setTitle(topicTitle);
		newTopic.setId(topicNum);
		newTopic.setNbreVues(topicViews);
		if (!listTopics.containsKey(topicUrl)) {
			listTopics.put(topicUrl, newTopic);
			System.out.println(
					"\t\t\t\tTopic n°" + topicNum + " : " + topicTitle + " | " + topicUrl + " | " + topicViews);

		}
	}

	private void fillMessagesList(String topicTitle, int topicId, int topicViews, String forumName, String messageNum,
			String messageAuteur, Date messageFormattedDate, String messagePost) {
		MessageModel newMessage = new MessageModel();

		// SUJET + SUJET TRONQUE
		newMessage.setSujet(topicTitle);
		CalculSujetTronque cst = new CalculSujetTronque(topicTitle);
		String sujetTronque = cst.getSujetTronque();
		newMessage.setSujetTronque(sujetTronque);

		// Identifiant message
		newMessage.setIdentifiant(messageNum);
		newMessage.setNumero("");

		// EXPEDITEUR
		newMessage.setExpediteur(messageAuteur);

		// ID LOCUTEUR
		if (!mapNomIdLocuteur.containsKey(messageAuteur)) {
			mapNomIdLocuteur.put(messageAuteur, locuteurId);
			newMessage.setIdLocuteur(locuteurId);
			// System.out.println("Nouveau locuteur \"" + messageAuteur + "\" avec l'id " +
			// locuteurId);
			locuteurId++;

		} else {
			// System.out.println("Locuteur existant \"" + messageAuteur + "\" avec l'id " +
			// mapNomIdLocuteur.get(messageAuteur));
			newMessage.setIdLocuteur(mapNomIdLocuteur.get(messageAuteur));
		}

		// DATE
		newMessage.setDateUS(messageFormattedDate);

		// CORPS MESSAGE
		newMessage.setCorps(messagePost);

		System.out.println("\t\t\tMessage n°" + messageNum + " : " + messageAuteur + " | " + messageFormattedDate
				+ " | " + messagePost.length() + " car." + " | locuteur id : " + newMessage.getIdLocuteur());

		// AUTRES
		newMessage.setIdentifiant(messageNum);
		newMessage.setIdConversation(topicId);
		newMessage.setfNumDansConversation(0);
		newMessage.setFName(forumName);
		newMessage.setfRoleLocuteur("∅");
		newMessage.setfStatPositionLocuteur("∅");
		newMessage.setfStatActivityLocuteur(0);
		newMessage.setfStarsLocuteur(0);
		newMessage.setfNbreVuesTopic(topicViews);
		newMessage.setfStatNbrePostsLocuteur(0);
		newMessage.setfStatDateRegistreredLocuteur("");
		newMessage.setfStatEMailLocuteur("∅");
		newMessage.setfStatWebsiteLocuteur("∅");
		newMessage.setfStatAgeLocuteur(0);
		newMessage.setfStatGenderLocuteur("∅");
		newMessage.setfStatLocationLocuteur("∅");
		newMessage.setfStatSignatureLocuteur("∅");
		newMessage.setMail("");
		newMessage.setProfilYahoo("∅");
		newMessage.setGroupPostYahoo("∅");
		newMessage.setIdGoogle("∅");
		newMessage.setFichier("forum");
		newMessage.setSetReferences(new HashSet<String>());
		newMessage.setcTransfertEncoding("∅");
		newMessage.setcTypeMimeSubtype("∅");
		newMessage.setcTypeCharset("∅");

		listMessages.put(messageNum, newMessage);
	}

	public Map<String, Integer> getMapNomIdLocuteur() {
		return mapNomIdLocuteur;
	}

	private void setMessageFormatTime(String dateString) {

		// Maps de regex dates selon
		// https://help.talend.com/reader/f7_Igxda4W5myTdc5yN2xw/OdzUMuVkKkL4x~h6zp36~A

		Map<String, String> regexFrFormats = new HashMap<String, String>() {
			{
				// ORDRE : jours dd et/ou EEE puis mois puis année
				// 4:57:02 am, 04:57:02 am
				put("\\d{1,2}:\\d{2}:\\d{2}\\sam\\b", "hh:mm:ss a");
				// 4:57:02 pm, 04:57:02 pm
				put("\\d{1,2}:\\d{2}:\\d{2}\\spm\\b", "hh:mm:ss a");
				// 4:57 am, 04:57 am
				put("\\d{1,2}:\\d{2}\\sam\\b", "hh:mm a");
				// 4:57 pm, 04:57 pm
				put("\\d{1,2}:\\d{2}\\spm\\b", "hh:mm a");
				// 4:57:02, 16:57:02
				put("\\d{1,2}:\\d{2}:\\d{2}\\b", "HH:mm:ss");
				// 4:57, 16:57
				put("\\d{1,2}:\\d{2}\\b", "HH:mm");

			}

		};

		for (String regexp : regexFrFormats.keySet()) {
			Pattern pattern = Pattern.compile(regexp, Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(dateString);
			if (matcher.find()) {
				messageTimeFormat = regexFrFormats.get(regexp);
				messageTimePattern = matcher.group(0);
				break;
			}
		}
	}

	private void setMessageFormatDate(String dateString) {

		// Maps de regex dates selon
		// https://help.talend.com/reader/f7_Igxda4W5myTdc5yN2xw/OdzUMuVkKkL4x~h6zp36~A

		Map<String, String> regexFrFormats = new HashMap<String, String>() {
			{
				// ORDRE : jours dd et/ou EEE puis mois puis année
				// 10 jui. 2018
				put("\\d{1,2}\\s\\p{L}{3}\\.\\s\\d{4}\\b", "dd MMM yyyy");
				// 10 juillet 2018
				put("\\d{1,2}\\s\\p{L}{3,}\\s\\d{4}\\b", "dd MMMM yyyy");
				// mardi 10 juillet 2018
				put("\\p{L}{5,}\\s\\d{1,2}\\s\\p{L}{3,}\\s\\d{4}\\b", "EEEE dd MMMM yyyy");
				// mardi 10 jui. 2018, mardi 10 juil. 2018
				put("\\p{L}{5,}\\s\\d{1,2}\\s\\p{L}{3,4}\\.\\s\\d{4}\\b", "EEEE dd MMM yyyy");
				// mar. 10 juillet 2018
				put("\\p{L}{3}\\.\\s\\d{1,2}\\s\\p{L}{3,}\\s\\d{4}\\\b", "EEE dd MMMM yyyy");
				// mar. 10 jui. 2018,mar. 10 juil. 2018
				put("\\p{L}{3}\\.\\s\\d{1,2}\\s\\p{L}{3,4}\\.\\s\\d{4}\\\b", "EEE dd MMM yyyy");
				// 10/07/18
				put("\\d{1,2}/\\d{1,2}/\\d{2}\\b", "dd/MM/yy");
				// 10/7/18
				put("\\d{1,2}/\\d{1}/\\d{2}\\b", "dd/M/yy");
				// 10/07/2018
				put("\\d{1,2}/\\d{1,2}/\\d{4}\\b", "dd/MM/yyyy");
				// 10/7/2018
				put("\\d{1,2}/\\d{1}/\\d{4}\\b", "dd/M/yyyy");
				// 10-07-18
				put("\\d{1,2}-\\d{1,2}-\\d{2}\\b", "dd-MM-yy");
				// 10-7-18
				put("\\d{1,2}-\\d{1}-\\d{2}\\b", "dd-M-yy");
				// 10-07-2018
				put("\\d{1,2}-\\d{1,2}-\\d{4}\\b", "dd-MM-yyyy");
				// 10-7-2018
				put("\\d{1,2}-\\d{1}-\\d{4}\\b", "dd-M-yyyy");

				// ORDRE : jours EEE puis mois puis jour dd puis année
				// mardi juillet 10 2018, mardi mai 10 2018
				put("\\p{L}{5,}\\s\\p{L}{3,}\\s\\d{1,2}\\s\\d{4}\\b", "EEEE MMMM dd yyyy");
				// mardi jui. 10 2018, mardi juil. 10 2018
				put("\\p{L}{5,}\\s\\p{L}{3,4}\\.\\s\\d{1,2}\\s\\d{4}\\b", "EEEE MMM dd yyyy");
				// mar. juillet 10 2018
				put("\\p{L}{3}\\.\\s\\p{L}{3,}\\s\\d{1,2}\\s\\d{4}\\b", "EEE MMMM dd yyyy");
				// mar. jui. 10 2018, mar. juil. 10 2018
				put("\\p{L}{3}\\.\\s\\p{L}{3,4}\\.\\s\\d{1,2}\\s\\d{4}\\b", "EEE MMM dd yyyy");

				// put("\\d{1,2}-\\d{1,2}-\\d{4}\\\b", "dd/MM/yy"); // 10-07-2018
				//
				// put("\\d{8}$", "yyyyMMdd"); // 20180710
				// put("\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy"); // 10-07-2018
				//
				// put("\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd"); // 2018-07-07
				// put("\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy"); // 07/10/2018

				// put("^\\d{12}$", "yyyyMMddHHmm");
				// put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
				// put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
				// put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
				// put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
				// put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
				// put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
				// put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy
				// HH:mm");
				// put("^\\d{14}$", "yyyyMMddHHmmss");
				// put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
				// put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy
				// HH:mm:ss");
				// put("\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd/MM/yyyy
				// HH:mm:ss");
				// put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd
				// HH:mm:ss");
				// put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy
				// HH:mm:ss");
				// put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd
				// HH:mm:ss");
				// put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy
				// HH:mm:ss");
				// put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy
				// HH:mm:ss");

			}

		};

		// String regexp = "\\p{L}{3}\\.\\s\\p{L}{3,4}\\.\\s\\d{1,2}\\s\\d{4}\\b";
		// dateString = "Message par vvoyer » lun. janv. 23 2017 2:45 pm";
		for (String regexp : regexFrFormats.keySet()) {
			Pattern pattern = Pattern.compile(regexp, Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(dateString);
			if (matcher.find()) {
				messageDateFormat = regexFrFormats.get(regexp);
				messageDatePattern = matcher.group(0);
				break;
			}
		}
	}

	public HashMap<String, TopicModel> getListTopics() {
		return listTopics;
	}

	public TreeMap<String, MessageModel> getListMessages() {
		return listMessages;
	}

	public List<String> getPaginationPages(Document doc, String higherLevelUrlString, LevelModel level) {

		// on récupère les informations sur l'url du niveau supérieur pour pouvoir la
		// comparer aux href des éléments de pagination

		URL higherLevelUrl = null;
		String higherLevelUrlFile = null, higherLevelUrlQuery = null;
		try {
			higherLevelUrl = new URL(higherLevelUrlString);
			higherLevelUrlFile = higherLevelUrl.getFile();
			higherLevelUrlQuery = higherLevelUrl.getQuery();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String higherLevelUrlPage = null;
		if (higherLevelUrlQuery != null) {
			higherLevelUrlPage = higherLevelUrlFile.replace(higherLevelUrlQuery, "");
			higherLevelUrlPage = higherLevelUrlPage.replace("?", "");
		}

		System.out.println("\tElements de l'URL du level supérieur : " + higherLevelUrlFile + " | "
				+ higherLevelUrlQuery + " | " + higherLevelUrlPage);

		List<String> paginationPagesUrls = new ArrayList<String>();

		String levelType = level.getType();

		// nombre de topics / messages par page
		Elements rowElements = new Elements();

		for (Row row : level.getRows()) {
			String rowTagName = row.getTagName();
			String rowClassName = row.getClassName();
			rowElements.addAll(doc.select(rowTagName + rowClassName));
		}

		int nbreElementsParPage = rowElements.size();

		int numPageElements = 1;
		int nbrePagesElements = 1;
		int lastPageElements = 1;

		// Recherche des liens de pagination

		Elements pagesUrls = new Elements();

		// Est-ce qu'une pagination existe ?

		Pagination pagination = level.getPagination();

		if (pagination != null) {
			String paginationClassName = pagination.getClassName();
			String paginationTagName = pagination.getTagName();
			System.out.println("\tUne pagination existe : " + paginationTagName + paginationClassName);

			// On sélectionne les éléments de pagination
			// # on ne prend pas les liens internes
			pagesUrls.addAll(doc.select(paginationTagName + paginationClassName + " a").not("a[href*=#]"));

			// On essaye d'extraire le xHref
			if (pagesUrls.size() > 0) {
				Map<String, TreeSet<Integer>> xHrefPagesNum = new HashMap<String, TreeSet<Integer>>();
				Map<String, TreeSet<Integer>> xHrefPagesNumTxt = new HashMap<String, TreeSet<Integer>>();
				TreeSet<Integer> newPagesNums = new TreeSet<Integer>();
				newPagesNums.add(1);
				xHrefPagesNum.put(higherLevelUrlString, newPagesNums);
				TreeSet<Integer> newPagesNumsTxt = new TreeSet<Integer>();
				newPagesNumsTxt.add(0);
				xHrefPagesNumTxt.put(higherLevelUrlString, newPagesNumsTxt);
				for (Element pageUrl : pagesUrls) {
					// texte de l'élément de pagination
					String txt = pageUrl.text();
					// href en url ABSOLUE
					String href = pageUrl.attr("abs:href");
					System.out.println("\tpageUrl : " + pageUrl + " | href absolu = " + href);
					if (href != "") {
						URL hrefUrl = null;
						String hrefUrlFile = null, hrefUrlQuery = null;
						try {
							hrefUrl = new URL(href);
							hrefUrlFile = hrefUrl.getFile();
							hrefUrlQuery = hrefUrl.getQuery();
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							// System.out.println("Erreur : URL malformée");
						}

						if (hrefUrl != null) {

							String hrefUrlPage = null;
							if (hrefUrlQuery != null) {
								hrefUrlPage = hrefUrlFile.replace(hrefUrlQuery, "");
								hrefUrlPage = hrefUrlPage.replace("?", "");
							}

							System.out.println("\tElements de l'URL du href: " + hrefUrlFile + " | " + hrefUrlQuery
									+ " | " + hrefUrlPage);

							if (
//									(Objects.equals(hrefUrlPage, higherLevelUrlPage)
//									|| Objects.equals(hrefUrlPage, higherLevelUrlFile)) && 
									NumberUtils.isNumber(txt)) {
								// xHref
								String xHref = href.replaceAll("[0-9]+(?!.*[0-9])", "x***x");
								// page num dans l'url
								int pageNum = 0;
								Pattern p = Pattern.compile("[0-9]+(?!.*[0-9])");
								Matcher m = p.matcher(href);
								if (m.find())
									pageNum = Integer.parseInt(m.group(0));

								// pageNum txt
								int pageNumTxt = Integer.parseInt(txt);

								System.out.println(
										"\t\txHref et pageNum found : " + xHref + " | " + pageNum + " | " + pageNumTxt);

								if (!xHrefPagesNum.containsKey(xHref)) {
									TreeSet<Integer> pagesNums = new TreeSet<Integer>();
									pagesNums.add(pageNum);
									xHrefPagesNum.put(xHref, pagesNums);
									TreeSet<Integer> pagesNumsTxt = new TreeSet<Integer>();
									pagesNumsTxt.add(pageNumTxt);
									xHrefPagesNumTxt.put(xHref, pagesNumsTxt);
								} else {
									Set<Integer> pagesNums = xHrefPagesNum.get(xHref);
									pagesNums.add(pageNum);
									Set<Integer> pagesNumsTxt = xHrefPagesNumTxt.get(xHref);
									pagesNumsTxt.add(pageNumTxt);
								}
							}
						}
					}
				}
				// On trie la map selon la taille du tableau de numéros de page si size > 1
				if (xHrefPagesNum.size() > 1) {
					Comparator<Object> comparator1 = new MapStringArraySizeComparator(xHrefPagesNum);
					Comparator<Object> comparator2 = new MapStringArraySizeComparator(xHrefPagesNumTxt);
					TreeMap<String, TreeSet<Integer>> xHrefPagesNumTrie = new TreeMap<String, TreeSet<Integer>>(
							comparator1);
					TreeMap<String, TreeSet<Integer>> xHrefPagesNumTxtTrie = new TreeMap<String, TreeSet<Integer>>(
							comparator2);
					xHrefPagesNumTrie.putAll(xHrefPagesNum);
					xHrefPagesNumTxtTrie.putAll(xHrefPagesNumTxt);
					xHrefPagesNum = xHrefPagesNumTrie;
					xHrefPagesNumTxt = xHrefPagesNumTxtTrie;
					System.out.println("\tOn a trié les maps");
				}
				// Affichage de la map
				System.out.println("\tAffichage de la map xHrefPagesNum : ");
				for (Entry<String, TreeSet<Integer>> entry : xHrefPagesNum.entrySet()) {
					System.out.println("\t" + entry.getKey() + " | " + entry.getValue());
				}
				System.out.println("\tAffichage de la map xHrefPagesNumTxt : ");
				for (Entry<String, TreeSet<Integer>> entry : xHrefPagesNumTxt.entrySet()) {
					System.out.println("\t" + entry.getKey() + " | " + entry.getValue());
				}
				Entry<String, TreeSet<Integer>> entry1 = xHrefPagesNum.entrySet().iterator().next();
				Entry<String, TreeSet<Integer>> entry2 = xHrefPagesNumTxt.entrySet().iterator().next();
				String xHref = entry1.getKey();
				// on change le xHref pour ce level car l'ancien était consécutif du choix de
				// lien pour un forum
				level.getPagination().setxHref(xHref);
				// on créé la liste des pages
				TreeSet<Integer> pagesNums = entry1.getValue();
				TreeSet<Integer> pagesNumsTxt = entry2.getValue();
				// System.out.println("pagesNums : " + pagesNums + " | pagesNumsTxt : " +
				// pagesNumsTxt);

				lastPageElements = pagesNums.last();
				nbrePagesElements = pagesNumsTxt.last();

				System.out.println("\t\tNombre de " + levelType + " par page = " + nbreElementsParPage);
				System.out.println("\t\tNombre de pages de " + levelType + " = " + nbrePagesElements);
				System.out.println("\t\tDernière page de " + levelType + " = " + lastPageElements);

				// Construction
				// des URL des Topics

				// On ajoute la première page
				paginationPagesUrls.add(higherLevelUrlString);
				// Calcul de la différence minimum des numéros de pages
				if (nbrePagesElements > 1) {
					ArrayList<Integer> numPages = new ArrayList<Integer>(pagesNums);
					int minDiff = 0;
					if (numPages.size() > 1)
						minDiff = minDiff(numPages);
					else
						minDiff = numPages.get(0);
					// System.out.println("\t\t\tminDiff = " + minDiff);
					// si minDiff = 1 => on commence à la page 2, donc i = 2; sinon i = minDiff
					int i = 0;
					if (minDiff == 1)
						i = 2;
					else
						i = minDiff;
					while (i <= lastPageElements) {
						String sI = String.valueOf(i);
						String urlPageElements = xHref.replaceAll("x\\*\\*\\*x", sI);
						System.out.println("\t\t\tpage #" + numPageElements + " : urlPageTopics = " + urlPageElements);
						paginationPagesUrls.add(urlPageElements);
						i += minDiff;
						numPageElements++;
					}
				} else {
					// 1 seule page de topics
					System.out.println("\t1 seule page de " + levelType);
					paginationPagesUrls.add(higherLevelUrlString);
					numPageElements++;
				}
			} else {
				// 1 seule page de elements
				System.out.println("\t1 seule page de " + levelType);
				paginationPagesUrls.add(higherLevelUrlString);
				numPageElements++;
			}
		} else {
			// 1 seule page de elements
			System.out.println("\t1 seule page de " + levelType);
			paginationPagesUrls.add(higherLevelUrlString);
			numPageElements++;
		}

		if (levelType.equals("Topic")) {
			setNbreTopicsParPage(nbreElementsParPage);
			setNbrePagesTopics(nbrePagesElements);
			setLastPageTopics(lastPageElements);
		}

		System.out.println("\t\tPages de pagination finale : ");
		for (String paginationPageUrl : paginationPagesUrls)
			System.out.println("\t\t" + paginationPageUrl);

		return paginationPagesUrls;

	}

	public int getNbreTopicsParPage() {
		return nbreTopicsParPage;
	}

	public void setNbreTopicsParPage(int nbreTopicsParPage) {
		this.nbreTopicsParPage = nbreTopicsParPage;
	}

	public int getNbrePagesTopics() {
		return nbrePagesTopics;
	}

	public void setNbrePagesTopics(int nbrePagesTopics) {
		this.nbrePagesTopics = nbrePagesTopics;
	}

	public int getLastPageTopics() {
		return lastPageTopics;
	}

	public void setLastPageTopics(int lastPageTopics) {
		this.lastPageTopics = lastPageTopics;
	}

	public int getForumWrapperType(Document doc) {

		// TypeForum
		// PHPBB : 2 types (type 1 et type 2)
		// type = 1 : viewforum.php
		// type = 2 : Réécriture de l'URL : pas de viewforum.php dans le
		// corps de page mais html
		// SMF : 1 type (type 3)
		// type = 3 : index.php?board=
		// DOCTISSIMO : 1 type (type 4)
		// type = 4 : liste_sujet-1.htm
		// Les impatientes : type 5
		// batiactu : type 6

		int forumWrapperType = 0;
		Elements forumWrappersType1 = doc.select("a[href*=viewforum.php]");
		Elements forumWrappersType2 = doc.select("a.forumlink, a.forumtitle");
		Elements forumWrappersType3 = doc.select("a[href*=index.php?board=]");
		Elements forumWrappersType4 = doc.select("a[href*=liste_sujet-1.htm]");
		Elements forumWrappersType5 = doc.select("a[href*=forumcat]");
		Elements forumWrappersType6 = doc.select("a[href*=forum/category-]");
		
		if (forumWrappersType1.size() > 0)
			forumWrapperType = 1;
		else if (forumWrappersType2.size() > 0)
			forumWrapperType = 2;
		else if (forumWrappersType3.size() > 0)
			forumWrapperType = 3;
		else if (forumWrappersType4.size() > 0)
			forumWrapperType = 4;
		else if (forumWrappersType5.size() > 0)
			forumWrapperType = 5;
		else if (forumWrappersType6.size() > 0)
			forumWrapperType = 6;
		
//		// Si il y a dans le doc un lien a qui contient viewforum.php
//		Elements forumWrappers = doc.select("a[href*=viewforum.php]");
//		if (forumWrappers.size() > 0)
//			// http://forum.openstreetmap.fr/
//			// http://thepunksite.com/forum/
//			// http://scenari-platform.org/forum/
//			// http://www.cichlid-forum.com/phpBB/
//			forumWrapperType = 1;
//		else {
//			// Si il y a dans le doc un lien a qui contient index.php?board=
//			forumWrappers = doc.select("a[href*=index.php?board=]");
//			if (forumWrappers.size() > 0)
//				// https://bitcointalk.org/index.php?board=13.0
//				forumWrapperType = 3;
//			else {
//				forumWrappers = doc.select("a.forumlink, a.forumtitle");
//				if (forumWrappers.size() > 0) {
//					// http://www.filsantejeunes.com/forum/
//					// http://www.forum-politique.org/
//					forumWrapperType = 2;
//				} else {
//					forumWrappers = doc.select("a[href*=liste_sujet-1.htm]");
//					if (forumWrappers.size() > 0) {
//						// http://forum.doctissimo.fr/grossesse-bebe/liste_categorie.htm
//						forumWrapperType = 4;
//					} else {
//						forumWrappers = doc.select("a[href*=forumcat]");
//						if (forumWrappers.size() > 0) {
//							// http://www.lesimpatientes.com/forums-cancer-sein-categories.asp
//							// Les forums n'ont pas d'adresses, mais juste un
//							// lien que l'on clique pour déployer les topics
//							forumWrapperType = 5;
//						}
//					}
//				}
//			}
//		}
		return forumWrapperType;
	}

	public void setTopicWrapperType(int forumWrapperType, String urlId, Document doc) {
		if (forumWrapperType == 1) {
			topicsWrappers = doc.select("ul.topiclist li");
			if (topicsWrappers.size() > 0)
				// http://forum.openstreetmap.fr/
				// http://thepunksite.com/forum/
				// http://www.cichlid-forum.com/phpBB/
				topicWrapperType = 1;
			else {
				topicsWrappers = doc.select("div#pagecontent table tr");
				if (topicsWrappers.size() > 0)
					topicWrapperType = 2;
				else {
					// http://scenari-platform.org/forum/
					topicsWrappers = doc.select("table.forumline tr");
					topicWrapperType = 6;
				}
			}
		} else if (forumWrapperType == 2) {
			topicsWrappers = doc.select("table tr");
			if (topicsWrappers.size() > 0) {
				// http://www.forum-politique.org/
				topicWrapperType = 3;
			} else {
				// http://www.filsantejeunes.com/forum/
				topicsWrappers = doc.select("ul.topiclist li");
				topicWrapperType = 4;
			}
		} else if (forumWrapperType == 3) {
			// https://bitcointalk.org/index.php?board=13.0
			topicsWrappers = doc.select("div.tborder table tr");
			topicWrapperType = 5;
		} else if (forumWrapperType == 4) {
			// http://forum.doctissimo.fr/grossesse-bebe/liste_categorie.htm
			topicsWrappers = doc.select("tr.sujet");
			topicWrapperType = 7;
		} else if (forumWrapperType == 5) {
			// http://www.lesimpatientes.com/forums-cancer-sein-categories.asp
			String select = "div#" + urlId;
			topicsWrappers = doc.select(select + " a");
			topicWrapperType = 8;
		} else if (forumWrapperType == 6) {
			// https://reseau.batiactu.com/forum/
			topicsWrappers = doc.select("div.forum-list ul li");
			topicWrapperType = 9;
		}
	}

	public int getTopicWrapperType() {
		return topicWrapperType;
	}

	public Elements getTopicsWrappers() {
		return topicsWrappers;
	}

	public void setMessageWrapperType(int forumWrapperType, Document doc) {
		// Type 1 à 5 : PHPBB
		// -> Test sur les rows et l'auteur pour trouver le type d'architecture
		// TYPE 1 : TR.ROW1 + TR.ROW2 - DIV.POSTAUTHOR - TD.POSTBOTTOM -
		// DIV.POSTBODY
		// TYPE 2 : TR.ROW1 + TR.ROW2 - B.POSTAUTHOR - TD.GENSMALL -
		// DIV.POSTBODY
		// TYPE 3 : DIV.POST - P.AUTHOR - P.AUTHOR - DIV.CONTENT
		// TYPE 4 : TR - SPAN.NAME - SPAN.POSTDETAILS - SPAN.POSTBODY
		// TYPE 5 : TABLE.IZB-ARAY
		// Type 6 : SMF : td.windowbg, td.windowbg2
		// Type 7 : doctissimo : table.tablemessage
		// Type 8 : les impatientes : div.contrib
		// Type 9 : batiactu : div.panel pour la question / div.media pour les réponses

		Elements authors;

		if (forumWrapperType == 1 || forumWrapperType == 2) {
			messagesWrappers = doc.select("table.izb-array");
			if (messagesWrappers.size() > 0) {
				messageWrapperType = 5;
			} else {
				messagesWrappers = doc.select("table.tablebg");
				if (messagesWrappers.size() > 0) {
					authors = messagesWrappers.select("div.postauthor");
					if (authors.size() > 0)
						messageWrapperType = 1;
					else {
						authors = messagesWrappers.select("b.postauthor");
						if (authors.size() > 0)
							// http://www.forum-politique.org/
							messageWrapperType = 2;
					}
				} else {
					messagesWrappers = doc.select("div.post");
					if (messagesWrappers.size() > 0) {
						authors = messagesWrappers.select("p.author");
						if (authors.size() > 0)
							// http://forum.openstreetmap.fr/
							// http://thepunksite.com/forum/
							// http://www.cichlid-forum.com/phpBB/
							messageWrapperType = 3;
						else {
							authors = messagesWrappers.select("div.name");
							if (authors.size() > 0)
								// http://www.filsantejeunes.com/forum/
								messageWrapperType = 7;
						}
					} else {
						messagesWrappers = doc.select("table.forumline");
						if (messagesWrappers.size() > 0) {
							authors = messagesWrappers.select("span.name");
							if (authors.size() > 0)
								// http://scenari-platform.org/forum/
								messageWrapperType = 4;
						}
					}
				}
			}
		} else if (forumWrapperType == 3) {
			// https://bitcointalk.org/index.php?board=13.0
			messagesWrappers = doc.select("td.windowbg, td.windowbg2");
			// authors = rows.select("td.poster_info");
			// System.out.println("rows.size = " + rows.size());
			// System.out.println("authors.size = "+authors.size());
			// System.out.println("rows = \n"+rows);
			messageWrapperType = 6;
		} else if (forumWrapperType == 4) {
			System.out.println("typeForum = 4");
			// http://forum.doctissimo.fr/grossesse-bebe/liste_categorie.htm
			messagesWrappers = doc.select("table.messagetable");
			messageWrapperType = 8;
		} else if (forumWrapperType == 5) {
			// http://www.lesimpatientes.com/forums-cancer-sein-categories.asp
			messagesWrappers = doc.select("div.contrib");
			messageWrapperType = 9;
		} else if (forumWrapperType == 6) {
			// https://reseau.batiactu.com/forum/sujet-est-il-possible-de-seulement-changer-le-cylindre-dune-serrure-5-points-1452-8079
			Element question = doc.select("div.panel").first();
			Elements answers = doc.select("div.media");
			messagesWrappers = new Elements();
			messagesWrappers.add(question);
			messagesWrappers.addAll(answers);
			messageWrapperType = 10;
		}
	}

	public Elements getMessagesWrappers() {
		return messagesWrappers;
	}

	public int getMessageWrapperType() {
		return messageWrapperType;
	}

	public TreeMap<String, MessageModel> getTreeMapMessagesTopicWithInReplyTo(
			TreeMap<String, MessageModel> treeMapMessagesTopic) {
		// TreeMap<String, MessageModel> newTreeMapMessagesTopic = new TreeMap<String,
		// MessageModel>(treeMapMessagesTopic);
		// List<MessageModel> listMessages = new
		// ArrayList<MessageModel>(treeMapMessagesTopic.values());
		// Comparator<MessageModel> byDateUs = new MessageDateUsComparator();
		// Collections.sort(listMessages, byDateUs);
		// MessageModel firstMessage = listMessages.get(0);
		// String idFirstMessage = firstMessage.getIdentifiant();
		// int numDansTopic = 1;
		// // System.out.println("premier messages identifiant = " +
		// // firstMessage.getIdentifiant());
		// for (String idMessage : treeMapMessagesTopic.keySet()) {
		// // System.out.println("autre identifiant = " + idMessage);
		// MessageModel message = newTreeMapMessagesTopic.get(idMessage);
		// message.setInReplyTo(idFirstMessage);
		// message.setInReplyToRegroupe("");
		// if (message.getfNumDansConversation() == 0) {
		// message.setfNumDansConversation(numDansTopic);
		// numDansTopic++;
		// }
		// }
		// newTreeMapMessagesTopic.get(idFirstMessage).setInReplyTo("");
		// return newTreeMapMessagesTopic;

		TreeMap<String, MessageModel> newTreeMapMessagesTopic = new TreeMap<String, MessageModel>(treeMapMessagesTopic);

		// on classe les messages par date
		List<MessageModel> listMessages = new ArrayList<MessageModel>(treeMapMessagesTopic.values());
		Comparator<MessageModel> byDateUs = new MessageDateUsComparator();
		Collections.sort(listMessages, byDateUs);
		// on récupère l'identifiant du premier message (le plus ancien)
		MessageModel firstMessage = listMessages.get(0);
		String idFirstMessage = firstMessage.getIdentifiant();
		// System.out.println(
		// "Identifiant du premier message : " + idFirstMessage + " | date : " +
		// firstMessage.getDateUS());
		// on boucle sur les messages classés pour les avoir dans l'ordre et on leur
		// ajoute numDansTopic
		int numDansTopic = 1;
		for (MessageModel message : listMessages) {
			MessageModel messageInMap = newTreeMapMessagesTopic.get(message.getIdentifiant());
			messageInMap.setInReplyTo(idFirstMessage);
			messageInMap.setInReplyToRegroupe("");
			if (messageInMap.getfNumDansConversation() == 0) {
				messageInMap.setfNumDansConversation(numDansTopic);
				numDansTopic++;
			}
			// System.out.println("Identifiant messageInMap : " +
			// messageInMap.getIdentifiant() + " | date : "
			// + messageInMap.getDateUS() + " | inReplyTo : " + messageInMap.getInReplyTo()
			// + " | numDansConversation : " + messageInMap.getfNumDansConversation());

		}
		// finalement on met le inReplyTo à vide pour le 1er message
		newTreeMapMessagesTopic.get(idFirstMessage).setInReplyTo("");
		return newTreeMapMessagesTopic;
	}

	public int minDiff(ArrayList<Integer> numbers) {
		int diff = Math.abs(numbers.get(1) - numbers.get(0));
		for (int i = 1; i < numbers.size() - 1; i++)
			if (Math.abs(numbers.get(i + 1) - numbers.get(i)) < diff)
				diff = Math.abs(numbers.get(i + 1) - numbers.get(i));
		return diff;
	}

	private String removeFrDay(String frDate) {
		String englishDate = frDate.toLowerCase();
		String[] replacements = { "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi", "dimanche", "lund.",
				"mard.", "merd.", "jeud.", "vend.", "same.", "dima.", "lun.", "mar.", "mer.", "jeu.", "ven.", "sam.",
				"dim." };
		for (String replacement : replacements) {
			englishDate = englishDate.replace(replacement, "");
		}
		return englishDate;
	}
	
	private String cleanFrMonth(String frDate) {
		String cleanFrDate = frDate;
		String[][] replacements = { { "Jan", "Janv." }, { "Fév", "Févr." }, { "Avr", "Avr." },
				{ "Juil", "Juil." }, { "Sep", "Sept." }, { "Oct", "Oct." }, { "Nov", "Nov." },
				{ "Déc", "Déc." } };
		for (String[] replacement : replacements) {
			cleanFrDate = cleanFrDate.replace(replacement[0], replacement[1]);
		}
		return cleanFrDate;
	}

	private String getEnMonth(String frDate) {
		String englishDate = frDate.toLowerCase();
		String[][] replacements = { { "janvier", "january" }, { "janv", "january" }, { "février", "february" },
				{ "févr", "february" }, { "fév", "february" }, { "mars", "march" }, { "avril", "april" },
				{ "avr", "april" }, { "mai", "may" }, { "juin", "june" }, { "juillet", "july" }, { "juil", "july" },
				{ "août", "august" }, { "septembre", "september" }, { "octobre", "october" },
				{ "novembre", "november" }, { "décembre", "december" }, { "déc", "december" }, { "/01/", "/january/" },
				{ "/02/", "/february/" }, { "/03/", "/march/" }, { "/04/", "/april/" }, { "/05/", "/may/" },
				{ "/06/", "/june/" }, { "/07/", "/july/" }, { "/08/", "/august/" }, { "/09/", "/september/" },
				{ "/10/", "/october/" }, { "/11/", "/november/" }, { "/12/", "/december/" } };
		for (String[] replacement : replacements) {
			englishDate = englishDate.replace(replacement[0], replacement[1]);
		}
		return englishDate;
	}

	public String getEnDay(String frDate) {
		String englishDate = frDate.toLowerCase();
		String[][] replacements = { { "hier", "yesterday" }, { "aujourd'hui", "today" } };
		for (String[] replacement : replacements) {
			englishDate = englishDate.replace(replacement[0], replacement[1]);
		}
		return englishDate;
	}

}
