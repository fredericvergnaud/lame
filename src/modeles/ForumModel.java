package modeles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONArray;

public class ForumModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nom, url, relativeUrl, urlId, httpProxyAdress, httpProxyPort;
	private TreeMap<Integer, String> 
			treeMapNumPageMessagesUrlPageMessages = new TreeMap<Integer, String>();
	private TreeMap<Integer, String> treeMapNumTopicUrlTopic = new TreeMap<Integer, String>(),
			treeMapNumTopicTitreTopic = new TreeMap<Integer, String>();
	private int wrapperType, topicsPresentationType, typePagesMessages, nbreTopicsParPage, nbrePagesTopics,
			lastPagesTopics, nbreTopicsEnviron;
	private int numPageTopicsEnCours, numTopicEnCours, numPageMessagesEnCours, numMessagesEnCours;
	private boolean downloadMessagesForumInterrupted = false;
	private Map<String, TopicModel> topicsMap = new HashMap<String, TopicModel>();
	private List<String> listTopicsPagesUrl = new ArrayList<String>();
	private Object deeperLevel;

	public ForumModel(String url) {
		this.url = url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public String getHttpProxyAdress() {
		return httpProxyAdress;
	}

	public Map<String, TopicModel> getTopicsMap() {
		return topicsMap;
	}

	public void setTopicsMap(Map<String, TopicModel> topicsMap) {
		this.topicsMap = topicsMap;
	}

	public void setHttpProxyAdress(String httpProxyAdress) {
		this.httpProxyAdress = httpProxyAdress;
	}

	public String getHttpProxyPort() {
		return httpProxyPort;
	}

	public void setHttpProxyPort(String httpProxyPort) {
		this.httpProxyPort = httpProxyPort;
	}

	public int getNumMessagesEnCours() {
		return numMessagesEnCours;
	}

	public void setNumMessagesEnCours(int numMessagesEnCours) {
		this.numMessagesEnCours = numMessagesEnCours;
		// System.out.println("NumMessageEnCours passe à " numMessagesEnCours);
	}

	public void setTreeMapNumPageMessagesUrlPageMessages(
			TreeMap<Integer, String> treeMapNumPageMessagesUrlPageMessages) {
		this.treeMapNumPageMessagesUrlPageMessages = treeMapNumPageMessagesUrlPageMessages;
	}

	public TreeMap<Integer, String> getTreeMapNumPageMessagesUrlPageMessages() {
		return treeMapNumPageMessagesUrlPageMessages;
	}

	public void setTreeMapNumTopicUrlTopic(TreeMap<Integer, String> treeMapNumTopicUrlTopic) {
		this.treeMapNumTopicUrlTopic = treeMapNumTopicUrlTopic;
	}

	public void setTreeMapNumTopicTitreTopic(TreeMap<Integer, String> treeMapNumTopicTitreTopic) {
		this.treeMapNumTopicTitreTopic = treeMapNumTopicTitreTopic;
	}

	public TreeMap<Integer, String> getTreeMapNumTopicUrlTopic() {
		return treeMapNumTopicUrlTopic;
	}

	public TreeMap<Integer, String> getTreeMapNumTopicTitreTopic() {
		return treeMapNumTopicTitreTopic;
	}

	public int getNumPageTopicsEnCours() {
		return numPageTopicsEnCours;
	}

	public void setNumPageTopicsEnCours(int numPageTopicsEnCours) {
		this.numPageTopicsEnCours = numPageTopicsEnCours;
		// System.out.println("numPageTopicsEnCours passe � "
		// + numPageTopicsEnCours);
	}

	public int getNumTopicEnCours() {
		return numTopicEnCours;
	}

	public void setNumTopicEnCours(int numTopicEnCours) {
		this.numTopicEnCours = numTopicEnCours;
		// System.out.println("numTopicEnCours passe à " + numTopicEnCours);
	}

	public int getNumPageMessagesEnCours() {
		return numPageMessagesEnCours;
	}

	public void setNumPageMessagesEnCours(int numPageMessagesEnCours) {
		this.numPageMessagesEnCours = numPageMessagesEnCours;
		// System.out.println("numPageMessagesEnCours passe à "
		// + numPageMessagesEnCours);
	}

	public int getWrapperType() {
		return wrapperType;
	}

	public void setWrapperType(int wrapperType) {
		this.wrapperType = wrapperType;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getUrl() {
		return url;
	}

	public void setRelativeUrl(String relativeUrl) {
		this.relativeUrl = relativeUrl;
	}

	public String getRelativeUrl() {
		return relativeUrl;
	}

	public List<String>  getListTopicsPagesUrls() {
		return listTopicsPagesUrl;
	}

	public void setListTopicsPagesUrls(List<String> listTopicsPagesUrl) {
		this.listTopicsPagesUrl = listTopicsPagesUrl;
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

	public int getLastPagesTopics() {
		return lastPagesTopics;
	}

	public void setLastPagesTopics(int lastPagesTopics) {
		this.lastPagesTopics = lastPagesTopics;
	}

	public int getNbreTopicsEnviron() {
		return nbreTopicsEnviron;
	}

	public void setNbreTopicsEnviron() {
		int nbreTopicsEnviron;
		if (nbrePagesTopics == 1)
			nbreTopicsEnviron = nbreTopicsParPage;
		else
			nbreTopicsEnviron = (nbrePagesTopics * nbreTopicsParPage) - nbreTopicsParPage;
		this.nbreTopicsEnviron = nbreTopicsEnviron;
	}

	public void setNbreTopicsEnviron(int nbreTopics) {
		this.nbreTopicsEnviron = nbreTopics;
	}

	public void setDownloadMessagesForumInterrupted(boolean downloadMessagesForumInterrupted) {
		this.downloadMessagesForumInterrupted = downloadMessagesForumInterrupted;
	}

	public boolean getDownloadMessagesForumInterrupted() {
		return this.downloadMessagesForumInterrupted;
	}

	public int getTopicsPresentationType() {
		return topicsPresentationType;
	}

	public void setTopicsPresentationType(int topicsPresentationType) {
		this.topicsPresentationType = topicsPresentationType;
	}

	public int getTypePagesMessages() {
		return typePagesMessages;
	}

	public void setTypePagesMessages(int typePagesMessages) {
		this.typePagesMessages = typePagesMessages;
	}

	public void setDeeperLevel(Object deeperLevel) {
		this.deeperLevel = deeperLevel;		
	}

	public Object getDeeperLevel() {
		return deeperLevel;
	}
}