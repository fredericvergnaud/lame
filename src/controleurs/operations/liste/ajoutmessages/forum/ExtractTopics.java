package controleurs.operations.liste.ajoutmessages.forum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import modeles.TopicModel;

public class ExtractTopics {

	private int topicWrapperType;
	private Elements topicsWrappers;
	private Map<String, TopicModel> listTopics = new HashMap<String, TopicModel>();

	public ExtractTopics(int topicWrapperType, Elements topicsWrappers) {
		this.topicWrapperType = topicWrapperType;
		this.topicsWrappers = topicsWrappers;
	}

	private void fillTopicsList(int idTopic, String titleTopic, String urlTopic, Date dateLastPost, int nbreVues) {
		System.out.println(
				"\t\t\t\t" + idTopic + " | " + titleTopic + " | " + urlTopic + " | " + dateLastPost + " | " + nbreVues);

		TopicModel newTopic = new TopicModel(urlTopic);
		newTopic.setTitle(titleTopic);
		newTopic.setId(idTopic);
		newTopic.setDateLastPost(dateLastPost);
		newTopic.setNbreVues(nbreVues);
		listTopics.put(urlTopic, newTopic);

	}

	public void extract() {

		switch (topicWrapperType) {
		case 1:
		case 4:
			// http://forum.openstreetmap.fr/ <- case 1
			// http://www.cichlid-forum.com/phpBB/ <- case 1
			// http://www.filsantejeunes.com/forum/ <- case 4
			for (Element ulTopic : topicsWrappers) {
				String titleTopic = null, urlTopic = null, lastPost = null;
				int idTopic = 0, nbreVues = 0;
				Date dateLastPost = null;
				Element aTopicTitle = ulTopic.select("a.topictitle").first();
				Element ddLastPostTopic = ulTopic.select("dd.lastpost").first();
				Element ddTopicViews = ulTopic.select("dd.views").first();
				if (aTopicTitle != null && ddLastPostTopic != null && ddTopicViews != null) {
					titleTopic = aTopicTitle.text();
					urlTopic = aTopicTitle.absUrl("href");
					String txtIdTopic = "";
					if (urlTopic.indexOf("&t=") != -1 && urlTopic.indexOf("&sid=") != -1) {
						txtIdTopic = urlTopic.substring(urlTopic.indexOf("&t=") + 3, urlTopic.indexOf("&sid="));

					} else if (urlTopic.indexOf("-t") != -1 && urlTopic.indexOf(".html?sid=") != -1) {
						txtIdTopic = urlTopic.substring(urlTopic.lastIndexOf("-") + 2, urlTopic.indexOf(".html?sid="));
					}
					if (!txtIdTopic.isEmpty() && isInteger(txtIdTopic))
						idTopic = Integer.parseInt(txtIdTopic);
					// ddLastPostTopic.select("a").remove();
					// ddLastPostTopic.select("img").remove();
					// ddLastPostTopic.select("dfn").remove();
					// // Retirer les tags, mais pas le texte
					// ddLastPostTopic.select("span").unwrap();
					String[] parts = null;
					parts = ddLastPostTopic.html().split("<br />");
					if (parts.length < 2)
						parts = ddLastPostTopic.html().split("<br>");
					if (parts.length == 2)
						for (String part : parts) {
							// System.out.println("part = " + part);
							if (part.indexOf(":") != 1) {
								lastPost = part.trim();
							}
						}
					if (lastPost != null) {
						Document docLastPost = Jsoup.parse(lastPost);
						lastPost = docLastPost.text();
						if (lastPost.indexOf("Today") != -1) {
							SimpleDateFormat formater = new java.text.SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a",
									Locale.US);
							Date today = new Date();
							lastPost = formater.format(today);
						}
						FormatDate fd = new FormatDate(lastPost);
						dateLastPost = fd.getDateFormatted();
					}
					ddTopicViews.select("dfn").remove();
					String txtTopicViews = ddTopicViews.text().trim();
					if (isInteger(txtTopicViews))
						nbreVues = Integer.parseInt(txtTopicViews);

					fillTopicsList(idTopic, titleTopic, urlTopic, dateLastPost, nbreVues);
				}
			}
			break;
		case 2:
			for (Element trTopic : topicsWrappers) {
				String titleTopic = null, urlTopic = null, lastPost = null;
				int idTopic = 0, nbreVues = 0;
				Date dateLastPost = null;
				Element aTopicTitle = trTopic.select("a.topictitle").first();
				Elements psTopicDetails = trTopic.select("p.topicdetails");
				if (aTopicTitle != null && psTopicDetails.size() > 0) {
					titleTopic = aTopicTitle.text();
					urlTopic = aTopicTitle.absUrl("href");
					if (urlTopic.indexOf("&t=") != -1 && urlTopic.indexOf("&sid=") != -1) {
						String txtIdTopic = urlTopic.substring(urlTopic.indexOf("&t=") + 3, urlTopic.indexOf("&sid="));
						if (isInteger(txtIdTopic))
							idTopic = Integer.parseInt(txtIdTopic);
					}
					// System.out.println("psTopicDetails : " +
					// psTopicDetails.text());
					// System.out.println();
					String txtTopicViews = psTopicDetails.get(1).text().trim();
					if (isInteger(txtTopicViews))
						nbreVues = Integer.parseInt(txtTopicViews);
					lastPost = psTopicDetails.get(2).text().trim();
					FormatDate fd = new FormatDate(lastPost);
					dateLastPost = fd.getDateFormatted();

					fillTopicsList(idTopic, titleTopic, urlTopic, dateLastPost, nbreVues);
				}
			}
			break;
		case 3:
			// http://www.forum-politique.org/
			for (Element trTopic : topicsWrappers) {
				String titleTopic = null, urlTopic = null, lastPost = null;
				int idTopic = 0, nbreVues = 0;
				Date dateLastPost = null;
				Element aTopicTitle = trTopic.select("a.topictitle").first();
				Elements pTopicsDetails = trTopic.select("p.topicdetails");
				if (aTopicTitle != null && pTopicsDetails.size() > 0) {
					titleTopic = aTopicTitle.text();
					urlTopic = aTopicTitle.absUrl("href");
					if (urlTopic.indexOf("-t") != -1) {
						if (urlTopic.indexOf("-t") != -1) {
							String txtIdTopic = urlTopic.substring(urlTopic.indexOf("-t") + 2,
									urlTopic.indexOf(".html"));
							if (isInteger(txtIdTopic))
								idTopic = Integer.parseInt(txtIdTopic);
							else {
								txtIdTopic = txtIdTopic.replaceAll("\\D+", "").trim();
								if (isInteger(txtIdTopic))
									idTopic = Integer.parseInt(txtIdTopic);
							}
						}
					} else if (urlTopic.indexOf("/topic") != -1) {
						String txtIdTopic = urlTopic.substring(urlTopic.indexOf("/topic") + 6,
								urlTopic.indexOf(".html"));
						if (isInteger(txtIdTopic))
							idTopic = Integer.parseInt(txtIdTopic);
						else {
							txtIdTopic = txtIdTopic.replaceAll("\\D+", "").trim();
							if (isInteger(txtIdTopic))
								idTopic = Integer.parseInt(txtIdTopic);
						}
					}
					// System.out.println("pTopicsDetails = \n" +
					// pTopicsDetails);
					pTopicsDetails.remove(pTopicsDetails.size() - 1);
					// System.out.println("pTopicsDetails après remove de last()
					// = \n"
					// + pTopicsDetails);
					lastPost = pTopicsDetails.last().text();
					FormatDate fd = new FormatDate(lastPost);
					dateLastPost = fd.getDateFormatted();
					pTopicsDetails.remove(pTopicsDetails.size() - 1);
					String txtNbreVues = pTopicsDetails.last().text();
					if (isInteger(txtNbreVues))
						nbreVues = Integer.parseInt(txtNbreVues);

					fillTopicsList(idTopic, titleTopic, urlTopic, dateLastPost, nbreVues);
				}
			}
			break;
		case 5:
			// https://bitcointalk.org/index.php?board=13.0
			for (Element trTopic : topicsWrappers) {
				String titleTopic = null, urlTopic = null, lastPost = null;
				int idTopic = 0, nbreVues = 0;
				Date dateLastPost = null;
				Element aTopicTitle = trTopic.select("span[id*=msg] > a").first();
				Element tdLastPostTopic = trTopic.select("td.lastpostcol").first();
				Element tdNbreVues = trTopic.select("td.windowbg").last();
				if (aTopicTitle != null && tdLastPostTopic != null && tdNbreVues != null) {
					titleTopic = aTopicTitle.text();
					urlTopic = aTopicTitle.absUrl("href");
					if (urlTopic.indexOf("topic=") != -1) {
						String txtIdTopic = urlTopic.substring(urlTopic.indexOf("topic=") + 6, urlTopic.indexOf(".0"));
						if (isInteger(txtIdTopic))
							idTopic = Integer.parseInt(txtIdTopic);
					}
					String txtLastPost = tdLastPostTopic.text();
					lastPost = txtLastPost.substring(0, txtLastPost.indexOf(" by ")).trim();
					if (lastPost.indexOf("Today") != -1) {
						SimpleDateFormat formater = new java.text.SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a",
								Locale.US);
						Date today = new Date();
						lastPost = formater.format(today);
					}
					FormatDate fd = new FormatDate(lastPost);
					dateLastPost = fd.getDateFormatted();
					String txtNbreVues = tdNbreVues.text();
					if (isInteger(txtNbreVues))
						nbreVues = Integer.parseInt(txtNbreVues);

					fillTopicsList(idTopic, titleTopic, urlTopic, dateLastPost, nbreVues);
				}
			}
			break;

		case 6:
			// http://scenari-platform.org/forum/
			for (Element ulTopic : topicsWrappers) {
				String titleTopic = null, urlTopic = null, lastPost = null;
				int idTopic = 0, nbreVues = 0;
				Date dateLastPost = null;
				Element aTopicTitle = ulTopic.select("a.topictitle").first();
				Element spanLastPostTopic = ulTopic.select("span.postdetails").last();
				if (aTopicTitle != null && spanLastPostTopic != null) {
					titleTopic = aTopicTitle.text();
					urlTopic = aTopicTitle.absUrl("href");
					if (urlTopic.indexOf("viewtopic.php?t=") != -1) {
						String txtIdTopic = "";
						txtIdTopic = urlTopic.substring(urlTopic.indexOf("viewtopic.php?t=") + 16,
								urlTopic.indexOf("&sid"));
						if (isInteger(txtIdTopic))
							idTopic = Integer.parseInt(txtIdTopic);
					}
					lastPost = spanLastPostTopic.text();
					FormatDate fd = new FormatDate(lastPost);
					dateLastPost = fd.getDateFormatted();
					spanLastPostTopic.remove();
					Element spanNbreVuesTopic = ulTopic.select("span.postdetails").last();
					String txtNbreVues = spanNbreVuesTopic.text().trim();
					if (isInteger(txtNbreVues))
						nbreVues = Integer.parseInt(txtNbreVues);

					fillTopicsList(idTopic, titleTopic, urlTopic, dateLastPost, nbreVues);
				}
			}
			break;
		case 7:
			// http://forum.doctissimo.fr/grossesse-bebe/liste_categorie.htm
			for (Element trTopic : topicsWrappers) {
				String titleTopic = null, urlTopic = null, lastPost = null;
				int idTopic = 0, nbreVues = 0;
				Date dateLastPost = null;
				Element aTopicTitle = trTopic.select("td.sujetCase3 a.cCatTopic").first();
				Element spanLastPostTopic = trTopic.select("td.sujetCase9 span").first();
				if (aTopicTitle != null && spanLastPostTopic != null) {
					titleTopic = aTopicTitle.text();
					urlTopic = aTopicTitle.absUrl("href");
					String idTopicTxt = aTopicTitle.attr("id").trim();
					idTopicTxt = idTopicTxt.substring(idTopicTxt.lastIndexOf("_") + 1);
					idTopic = Integer.parseInt(idTopicTxt);
					// System.out.println(" idTopicTxt = " + idTopicTxt);
					// System.out.println(" titleTopic = " + titleTopic);
					// System.out.println(" urlTopic = " + urlTopic);
					String lastPostTxt = spanLastPostTopic.text().trim();
					// System.out.println(" lastPostTxt = " + lastPostTxt);
					if (lastPostTxt.indexOf(" à ") != -1)
						lastPost = lastPostTxt.replace(" à ", " ");
					else
						lastPost = lastPostTxt;
					FormatDate fd = new FormatDate(lastPost);
					dateLastPost = fd.getDateFormatted();
					// System.out.println(" dateLastPost = " + dateLastPost);

					Element tdNbreVuesTopic = trTopic.select("td.sujetCase8").first();
					String txtNbreVues = tdNbreVuesTopic.text().trim();
					// System.out.println(" txtNbreVues = " + txtNbreVues);
					if (isInteger(txtNbreVues))
						nbreVues = Integer.parseInt(txtNbreVues);

					fillTopicsList(idTopic, titleTopic, urlTopic, dateLastPost, nbreVues);
				}
			}
		case 8:
			// http://www.lesimpatientes.com/forums-cancer-sein-categories.asp
			for (Element aTopic : topicsWrappers) {
				String titleTopic = null, urlTopic = null, lastPost = null;
				int idTopic = 0, nbreVues = 0;
				Date dateLastPost = null;
				titleTopic = aTopic.text();
				urlTopic = aTopic.absUrl("href");
				if (urlTopic.indexOf("id=") != -1) {
					String idTopicTxt = urlTopic.substring(urlTopic.lastIndexOf("id=") + 3);
					idTopic = Integer.parseInt(idTopicTxt);
				}

				if (!titleTopic.isEmpty() && !urlTopic.isEmpty() && idTopic != 0)
					fillTopicsList(idTopic, titleTopic, urlTopic, dateLastPost, nbreVues);
			}
		case 9:
			// https://reseau.batiactu.com/forum/
			for (Element topic : topicsWrappers) {
				String titleTopic = null, urlTopic = null, lastPost = null;
				int idTopic = 0, nbreVues = 0;
				Date dateLastPost = null;
				Element aTopic = topic.select("div.col-sm-8 a").first();
				titleTopic = aTopic.text();
				urlTopic = aTopic.absUrl("href");
				String idTopicTxt = urlTopic.substring(urlTopic.lastIndexOf("-") + 1);
				idTopic = Integer.parseInt(idTopicTxt);
				if (!titleTopic.isEmpty() && !urlTopic.isEmpty() && idTopic != 0)
					fillTopicsList(idTopic, titleTopic, urlTopic, dateLastPost, nbreVues);
			}
		default:
			break;
		}
	}

	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public Map<String, TopicModel> getListTopics() {
		return listTopics;
	}

}
