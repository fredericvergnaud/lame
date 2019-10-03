package controleurs.operations.liste.ajoutmessages.extractify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import modeles.ForumModel;
import modeles.TopicModel;

public class ExtractTopics {

	private Map<String, String> matchingTopicProperties;
	private int idTopic = 0;
	private Map<String, TopicModel> topicsMap = new HashMap<String, TopicModel>();
	private JSONArray jsonTopics;
	private Map<String, String> topicParseErrors = new HashMap<String, String>();
	private ResourceBundle bundleOperationsListe;

	public ExtractTopics(JSONArray jsonTopics, Map<String, String> matchingTopicProperties, ResourceBundle bundleOperationsListe) {
		this.jsonTopics = jsonTopics;
		this.matchingTopicProperties = matchingTopicProperties;
		this.bundleOperationsListe = bundleOperationsListe;
	}

	public void extract() {
		List<String> topicsTitles = new ArrayList<String>();
		for (Object jsonTopic : jsonTopics) {
			JSONObject topicObject = (JSONObject) jsonTopic;
			TopicModel topic = new TopicModel();
			for (Object property : topicObject.keySet()) {
				Object value = topicObject.get(property);
				// System.out.println("property: " + property + " value: " + value);
				if (!property.equals("type") && !property.equals("deeperLevel")) {
					String matchingLameProperty = matchingTopicProperties.get(property);
					// System.out.println("matchingLameProperty = " + matchingLameProperty);
					if (matchingLameProperty != null) {
						switch (matchingLameProperty) {
						case "titreTopic":
							topic.setTitle((String) value);
							break;
						case "urlTopic":
							String topicUrl = (String) value;
							if (topicUrl.startsWith("http"))
								topic.setUrl(topicUrl);
							else
								topicParseErrors.put(bundleOperationsListe.getString("txt_Topic") + " > " + bundleOperationsListe.getString("txt_UrlTopic"), bundleOperationsListe.getString("txt_UrlFormatError"));
							break;
						case "nbrVuesTopic":
							int nbrVues = 0;
							try {
								nbrVues = Integer.parseInt(((String) value).replaceAll(" ", ""));
							} catch (NumberFormatException e) {
								topicParseErrors.put(bundleOperationsListe.getString("txt_Topic") + " > " + bundleOperationsListe.getString("txt_NbrVuesTopic"), bundleOperationsListe.getString("txt_NumberFormatError"));
							} finally {
								topic.setNbreVues(nbrVues);
							}
							break;
							default :
								topicParseErrors.put(bundleOperationsListe.getString("txt_Topic"), bundleOperationsListe.getString("txt_MatchingFieldsError"));
						}
					}
				} else if (property.equals("deeperLevel")) {
					topic.setDeeperLevel((JSONArray) value);
				}
			}
			String topicTitle = topic.getTitle();
			if (topicTitle != null && !topicsTitles.contains(topicTitle)) {
				topic.setId(idTopic);
				topicsMap.put(String.valueOf(idTopic), topic);
				topicsTitles.add(topicTitle);
				idTopic++;
			}
		}
	}

	public Map<String, TopicModel> getTopicsMap() {
		return topicsMap;
	}

	public Map<String, String> getTopicParseErrors() {
		return topicParseErrors;
	}
}
