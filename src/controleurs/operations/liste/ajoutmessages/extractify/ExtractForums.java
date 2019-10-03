package controleurs.operations.liste.ajoutmessages.extractify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import modeles.ForumModel;
import modeles.TopicModel;

public class ExtractForums {

	private JSONArray jsonLevels;
	private Map<String, String> matchingForumProperties;
	private List<ForumModel> forumsList = new ArrayList<ForumModel>();
	private Map<String, String> forumParseErrors = new HashMap<String, String>();
	private ResourceBundle bundleOperationsListe;

	public ExtractForums(JSONArray jsonLevels, Map<String, String> matchingForumProperties, ResourceBundle bundleOperationsListe) {
		this.jsonLevels = jsonLevels;
		this.matchingForumProperties = matchingForumProperties;
		this.bundleOperationsListe = bundleOperationsListe;
	}

	public void extract() {
		List<String> forumsTitles = new ArrayList<String>();
		for (Object item : jsonLevels) {
			JSONObject forumObject = (JSONObject) item;
			ForumModel forum = new ForumModel("url");
			for (Object property : forumObject.keySet()) {
				Object value = forumObject.get(property);
				// System.out.println("property: " + property + " value: " + value);
				if (!property.equals("type") && !property.equals("deeperLevel")) {
					String matchingLameProperty = matchingForumProperties.get(property);
					// System.out.println("matchingLameProperty = " + matchingLameProperty);
					if (matchingLameProperty != null) {
						switch (matchingLameProperty) {
						case "nomForum":
							forum.setNom((String) value);
							break;
						case "urlForum":
							String forumUrl = (String) value;
							if (forumUrl.startsWith("http"))
								forum.setUrl(forumUrl);
							else
								forumParseErrors.put(bundleOperationsListe.getString("txt_Forum") + " > " + bundleOperationsListe.getString("txt_UrlForum"), bundleOperationsListe.getString("txt_UrlFormatError"));
							break;
							default :
								forumParseErrors.put(bundleOperationsListe.getString("txt_Forum"), bundleOperationsListe.getString("txt_MatchingFieldsError"));
						}
					}
				} else if (property.equals("deeperLevel")) {
					forum.setDeeperLevel((JSONArray) value);
				}
			}
			String forumTitle = forum.getNom();
			if (forumTitle != null && !forumsTitles.contains(forumTitle)) {
				forumsList.add(forum);
				forumsTitles.add(forumTitle);
			}
		}
	}

	public List<ForumModel> getForumsList() {
		return forumsList;
	}
	
	public Map<String, String> getForumParseErrors() {
		return forumParseErrors;
	}
}
