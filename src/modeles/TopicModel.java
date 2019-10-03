package modeles;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TopicModel {

	private String url, title;
	private int id, nbreVues;
	private Date dateLastPost;
	private List<String> listMessagesPagesUrls;
	private Object deeperLevel;
	private TreeMap<String, MessageModel> messagesMap;

	public TopicModel(String url) {
		this.url = url;
	}

	public TopicModel() {
	}

	public String getUrl() {
		return url;
	}

	public void setDateLastPost(Date dateLastPost) {
		this.dateLastPost = dateLastPost;
	}

	public void setNbreVues(int nbreVues) {
		this.nbreVues = nbreVues;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNbreVues() {
		return nbreVues;
	}

	public Date getDateLastPost() {
		return dateLastPost;
	}

	public void setListMessagesPagesUrls(List<String> listMessagesPagesUrls) {
		this.listMessagesPagesUrls = listMessagesPagesUrls;
	}

	public List<String> getListMessagesPagesUrls() {
		return listMessagesPagesUrls;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Object getDeeperLevel() {
		return deeperLevel;
	}

	public void setDeeperLevel(Object deeperLevel) {
		this.deeperLevel = deeperLevel;
	}

	public TreeMap<String, MessageModel> getMessagesMap() {
		return messagesMap;
	}

	public void setMessagesMap(TreeMap<String, MessageModel> messagesMap) {
		this.messagesMap = messagesMap;
	}
}
