package controleurs.operations.liste.ajoutmessages.extractify;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import controleurs.operations.liste.CalculSujetTronque;
import controleurs.operations.liste.ajoutmessages.forum.FormatDate;
import modeles.ForumModel;
import modeles.MessageModel;
import modeles.TopicModel;

public class ExtractMessages {

	private TopicModel topic;
	private Map<String, String> matchingMessageProperties;
	private Map<String, String> matchingAuteurProperties;
	private TreeMap<String, MessageModel> messagesMap = new TreeMap<String, MessageModel>();
	private int idMessage;
	private ForumModel forum;
	private JSONArray jsonMessages;
	private Map<String, String> messageParseErrors = new HashMap<String, String>();
	private ResourceBundle bundleOperationsListe;

	public ExtractMessages(int idMessage, JSONArray jsonMessages, ForumModel forum, TopicModel topic,
			Map<String, String> matchingMessageProperties, Map<String, String> matchingAuteurProperties, ResourceBundle bundleOperationsListe) {
		this.idMessage = idMessage;
		this.jsonMessages = jsonMessages;
		this.forum = forum;
		this.topic = topic;
		this.matchingMessageProperties = matchingMessageProperties;
		this.matchingAuteurProperties = matchingAuteurProperties;
		this.bundleOperationsListe = bundleOperationsListe;
	}

	public void extract() {
		Map<String, String> matchingMessageAndAuteurProperties = new HashMap<String, String>();
		matchingMessageAndAuteurProperties.putAll(matchingMessageProperties);
		matchingMessageAndAuteurProperties.putAll(matchingAuteurProperties);
		// List<MessageModel> messages = new ArrayList<MessageModel>();

		for (Object deeperLevelObject : jsonMessages) {
			MessageModel message = new MessageModel();
			String nom = null, date = null, post = null, urlUserProfile = "∅", roleUser = "∅", positionUser = "∅",
					dateRegistered = null, email = "∅", website = "∅", gender = "∅", location = "∅", signature = "∅",
					titreMessage = "";
			int idUser = 0, activiyUser = 0, nbrePosts = 0, age = 0, numDansTopic = 0, readTimesTopic = 0;
			double nbreEtoilesUser = 0;
			JSONObject messageObject = (JSONObject) deeperLevelObject;
			for (Object property : messageObject.keySet()) {
				Object value = messageObject.get(property);
				// System.out.println("property: " + property + " value: " + value);
				if (!property.equals("type")) {
					String matchingLameProperty = matchingMessageAndAuteurProperties.get(property);
					// System.out.println("matchingLameProperty = " + matchingLameProperty);
					if (matchingLameProperty != null) {
						FormatDate fd;
						switch (matchingLameProperty) {
						case "dateMessage":
							date = (String) value;
							if (!date.equals("")) {
								fd = new FormatDate(date);
								Date dDateParseUS = fd.getDateFormatted();
								if (dDateParseUS != null)
									message.setDateUS(dDateParseUS);
								else
									messageParseErrors.put(bundleOperationsListe.getString("txt_Message") + " > " + bundleOperationsListe.getString("txt_DateMessage"), bundleOperationsListe.getString("txt_DateFormatError"));
							}
							break;
						case "corpsMessage":
							post = (String) value;
							if (!post.equals("")) {
								if (message.getText() != null)
									message.setCorps(message.getText() + "\n " + "***** " + post);
								else
									message.setCorps(post);
							}
							break;
						case "nomAuteur":
							nom = (String) value;
							if (!nom.equals(""))
								message.setExpediteur(nom);
							break;
						case "localisationAuteur":
							location = (String) value;
							if (!location.equals(""))
								message.setfStatLocationLocuteur(location);
							break;
						case "nbreMessagesAuteur":
							if (!((String)value).equals("")) {
								try {
									nbrePosts = Integer.parseInt(((String) value).replaceAll(" ", ""));
								} catch (NumberFormatException e) {
									messageParseErrors.put(bundleOperationsListe.getString("txt_Auteur") + " > " + bundleOperationsListe.getString("txt_NbreMessagesAuteur"), bundleOperationsListe.getString("txt_NumberFormatError"));
								} finally {
									message.setfStatNbrePostsLocuteur(nbrePosts);
								}
							}
							break;
						case "dateInscriptionAuteur":
							dateRegistered = (String) value;
							if (!dateRegistered.equals("")) {
								fd = new FormatDate(dateRegistered);
								Date dDateRegistered = fd.getDateFormatted();
								if (dDateRegistered != null)
									message.setfStatDateRegistreredLocuteur(dateRegistered);
								else 
									messageParseErrors.put(bundleOperationsListe.getString("txt_Auteur") + " > " + bundleOperationsListe.getString("txt_DateInscriptionAuteur"), bundleOperationsListe.getString("txt_DateFormatError"));
							}
							break;
						case "statutAuteur":
							roleUser = (String) value;
							if (!roleUser.equals(""))
								message.setfRoleLocuteur(roleUser);
							break;
						case "reputationAuteur":
							if (!((String)value).equals("")) {
								try {
									nbreEtoilesUser = Integer.parseInt((String) value);
								} catch (NumberFormatException e) {
									messageParseErrors.put(bundleOperationsListe.getString("txt_Auteur") + " > " + bundleOperationsListe.getString("txt_ReputationAuteur"), bundleOperationsListe.getString("txt_NumberFormatError"));
								} finally {
									message.setfStarsLocuteur(nbreEtoilesUser);
								}
							}
							break;
						case "genreAuteur":
							gender = (String) value;
							if (!gender.equals(""))
								message.setfStatGenderLocuteur(gender);
							break;
						case "ageAuteur":
							if (!((String)value).equals("")) {
								try {
									age = Integer.parseInt((String) value);
								} catch (NumberFormatException e) {
									messageParseErrors.put(bundleOperationsListe.getString("txt_Auteur") + " > " + bundleOperationsListe.getString("txt_AgeAuteur"), bundleOperationsListe.getString("txt_NumberFormatError"));
								} finally {
									message.setfStatAgeLocuteur(age);
								}
							}
							break;
						case "signatureAuteur":
							signature = (String) value;
							if (!signature.equals(""))
								message.setfStatSignatureLocuteur(signature);
							break;
						case "positionAuteur":
							positionUser = (String) value;
							if (!positionUser.equals(""))
								message.setfStatPositionLocuteur(positionUser);
							break;
						case "emailAuteur":
							email = (String) value;
							if (!email.equals(""))
								message.setfStatEMailLocuteur(email);
							break;
						case "websiteAuteur":
							website = (String) value;
							if (!website.equals(""))
								message.setfStatWebsiteLocuteur(website);
							break;
						case "activiteAuteur":
							if (!((String)value).equals("")) {
								try {
									activiyUser = Integer.parseInt((String) value);
								} catch (NumberFormatException e) {
									messageParseErrors.put(bundleOperationsListe.getString("txt_Auteur") + " > " + bundleOperationsListe.getString("txt_ActiviteAuteur"), bundleOperationsListe.getString("txt_NumberFormatError"));
								} finally {
									message.setfStatActivityLocuteur(activiyUser);
								}
							}
							break;
						case "deeperLevel":
							break;
						default :
							messageParseErrors.put(bundleOperationsListe.getString("txt_Message") + " / " + bundleOperationsListe.getString("txt_Auteur"), bundleOperationsListe.getString("txt_MatchingFieldsError"));
						}
					}
				}
			}
			if (message.getExpediteur() != null && message.getDateUS() != null && message.getText() != null) {
				String sIdMessage = String.valueOf(idMessage);
				message.setIdentifiant(sIdMessage);
				message.setIdConversation(topic.getId());
				message.setSujet(topic.getTitle());
				CalculSujetTronque cst = new CalculSujetTronque(topic.getTitle());
				String sujetTronque = cst.getSujetTronque();
				message.setSujetTronque(sujetTronque);
				// obligatoire
				message.setNumero("");
				message.setfNumDansConversation(0);
				message.setFName(forum.getNom());
				message.setFichier("forum");
				message.setIdLocuteur(0);
				// prévu
				message.setfNbreVuesTopic(topic.getNbreVues());

				message.setMail("∅");
				message.setProfilYahoo("∅");
				message.setGroupPostYahoo("∅");
				message.setIdGoogle("∅");
				message.setSetReferences(new HashSet<String>());
				message.setcTransfertEncoding("∅");
				message.setcTypeMimeSubtype("∅");
				message.setcTypeCharset("∅");

				messagesMap.put(String.valueOf(idMessage), message);

				idMessage++;
			}
		}
	}

	public TreeMap<String, MessageModel> getMapMessages() {
		return messagesMap;
	}

	public int getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(int idMessage) {
		this.idMessage = idMessage;
	}

	public Map<String, String> getMessageParseErrors() {
		return messageParseErrors;
	}

}
