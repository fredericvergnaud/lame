package controleurs.operations.liste.ajoutmessages.forum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import modeles.TopicModel;

public class FindMessagesPagesUrls {

	private Document doc;
	private TopicModel topic;
	private int topicWrapperType;

	public FindMessagesPagesUrls(Document doc, TopicModel topic, int topicWrapperType) {
		this.doc = doc;
		this.topic = topic;
		this.topicWrapperType = topicWrapperType;
	}

	public List<String> getListMessagesPagesUrls() {

		// Recherche des liens de pagination

		String toFind = "", toFind2 = "";
		int idTopic = topic.getId();
		String urlTopic = topic.getUrl();

		System.out.println("\t\t\t\t\tTopic " + topic.getTitle() + " | " + idTopic + " | topicWrapperType " + topicWrapperType
				+ " | url : " + urlTopic);

		Elements messagesPagesLinks = new Elements();
		
		if (topicWrapperType == 1 || topicWrapperType == 6) {
			// http://forum.openstreetmap.fr/ <- typePagesTopics = 1
			// http://thepunksite.com/forum/ <- typePagesTopics = 1
			// http://scenari-platform.org/forum/ <- typePagesTopics = 6
			// http://www.cichlid-forum.com/phpBB/ <- typePagesTopics = 1
			toFind = "t=" + idTopic;
			messagesPagesLinks = doc.select("a[href*=" + toFind + "][href*=start=]").not("a[href*=facebook][href*=twitter]");			
		} else if (topicWrapperType == 3 || topicWrapperType == 4) {
			// http://www.filsantejeunes.com/forum/ <- typePagesTopics = 4
			// http://www.forum-politique.org <- typePagesTopics = 3
			toFind = "t" + idTopic;
			toFind2 = "topic" + idTopic;
			messagesPagesLinks = doc.select("a[href*=" + toFind + "], a[href*=" + toFind2 + "]");
		} else if (topicWrapperType == 5) {
			// https://bitcointalk.org/index.php?board=13.0
			toFind = "topic=" + idTopic;
			messagesPagesLinks = doc.select("a[href*=" + toFind + "]");
		} else if (topicWrapperType == 7) {
			// http://forum.doctissimo.fr/grossesse-bebe/liste_categorie.htm
			toFind = "sujet_" + idTopic;
			messagesPagesLinks = doc.select("a[href*=" + toFind + "]").not("a.Topic");			
		} else if (topicWrapperType == 8) {
			// http://www.lesimpatientes.com/forums-cancer-sein-categories.asp
			toFind = "id=" + idTopic + "&pagecur=";
			// System.out.println("elemTopic = " + elemTopic);
			// String aTopicAbsUrl = elemTopic.absUrl("href");
			// System.out.println("aTopicAbsUrl = " + aTopicAbsUrl);
			messagesPagesLinks = doc.select("a[href*=" + toFind + "]");
		} else if (topicWrapperType == 9) {
			// https://reseau.batiactu.com/forum/category-accessibilite-et-securite-1452?page=0
			// pas de pages de messages
		}
		
//		System.out.println("\nmessagesPagesLinks : " + messagesPagesLinks);

		Elements falseLinks = messagesPagesLinks.select("a[href*=#wrapheader], a.postlink-internal, a.postlink");
		messagesPagesLinks.removeAll(falseLinks);
		
//		System.out.println("\nmessagesPagesLinks après : " + messagesPagesLinks);
		
		// // System.out.println("toFind = " + toFind + " | toFind2 = " +
		// toFind2);
		// String select = "";
		// Elements aUrlsPagesMessages = null;
		//
		// if (!toFind2.equals(""))
		// select = "a[href*=" + toFind + "], a[href*=" + toFind2 + "]";
		// else
		// select = "a[href*=" + toFind + "]";
		//
		// aUrlsPagesMessages =
		// elemTopic.select(select).not("a[href*=#wrapheader]").not("a.postlink-internal")
		// .not("a.postlink");
		//
		// // System.out.println("aUrlsPagesMessages : ");
		// // System.out.println(aUrlsPagesMessages);

		List<String> listMessagesPagesUrls = new ArrayList<String>();

		if (messagesPagesLinks.size() == 0) {
			System.out.println("\t\t\t\t\t\tUne seule page de Messages");
			listMessagesPagesUrls.add(urlTopic);
		} else {
			System.out.println("\t\t\t\t\t\tPlusieurs pages de Messages");
			Set<Integer> setNumPages = new TreeSet<Integer>();
			
			// Pour DOCTISSIMO : on n'ajoute pas de page 0
			if (topicWrapperType != 7)
				setNumPages.add(0);
			// Pour LES IMPATIENTES, on ajoute la page current, car c'est la dernière page qui est en premier
			if (topicWrapperType == 8)
				listMessagesPagesUrls.add(urlTopic);
			
			for (Element messagesPagesLink : messagesPagesLinks) {
				String urlPageMessages = messagesPagesLink.attr("href");
//				System.out.println("url page messages = " + urlPageMessages);
				int numPage = 0;
				if (topicWrapperType == 3 || topicWrapperType == 4) {
					// http://www.filsantejeunes.com/forum/
					// http://www.forum-politique.org
					String txtNumPage = urlPageMessages.substring(urlPageMessages.lastIndexOf("-") + 1,
							urlPageMessages.indexOf(".html"));
					if (isInteger(txtNumPage)) {
						numPage = Integer.parseInt(txtNumPage);
						setNumPages.add(numPage);
					}
				} else if (topicWrapperType == 1 || topicWrapperType == 6) {
					// http://forum.openstreetmap.fr/ <- typePagesTopics = 1
					// http://thepunksite.com/forum/ <- typePagesTopics = 1
					// http://www.cichlid-forum.com/phpBB/ <- typePagesTopics =
					// 1
					// http://scenari-platform.org/forum/ <- typePagesTopics = 6
					// (sid= après start=)
					String txtNumPage = "";
					if (urlPageMessages.indexOf("sid=") < urlPageMessages.indexOf("start="))
						txtNumPage = urlPageMessages.substring(urlPageMessages.lastIndexOf("start=") + 6);
					else
						txtNumPage = urlPageMessages.substring(urlPageMessages.lastIndexOf("start=") + 6,
								urlPageMessages.lastIndexOf("sid=") - 1);
					if (isInteger(txtNumPage)) {
						numPage = Integer.parseInt(txtNumPage);
						setNumPages.add(numPage);
					}
				} else if (topicWrapperType == 5) {
					// https://bitcointalk.org/index.php?board=13.0
					String txtNumPage = urlPageMessages.substring(urlPageMessages.lastIndexOf(".") + 1);
					if (isInteger(txtNumPage)) {
						numPage = Integer.parseInt(txtNumPage);
						setNumPages.add(numPage);
					}
				} else if (topicWrapperType == 7) {
					// http://forum.doctissimo.fr/grossesse-bebe/liste_categorie.htm
					String txtNumPage = urlPageMessages.substring(urlPageMessages.lastIndexOf("_") + 1,
							urlPageMessages.lastIndexOf("."));
					if (isInteger(txtNumPage)) {
						numPage = Integer.parseInt(txtNumPage);
						if (numPage != 1)
							setNumPages.add(numPage);
					}
				} else if (topicWrapperType == 8) {
					// http://www.lesimpatientes.com/forums-cancer-sein-categories.asp
					String txtNumPage = urlPageMessages.substring(urlPageMessages.lastIndexOf("pagecur=") + 8);
					if (isInteger(txtNumPage)) {
						numPage = Integer.parseInt(txtNumPage);
						setNumPages.add(numPage);
					}
				}
			}
//			System.out.println("setNumPages = " + setNumPages);
			List<Integer> listNumPages = new ArrayList<Integer>(setNumPages);
			if (topicWrapperType != 7) {
				int lastPagesMessages = 1, nbreMessagesPage = 0;
				if (listNumPages.size() > 1) {
					if (listNumPages.size() == 2) {
						nbreMessagesPage = listNumPages.get(1);
						lastPagesMessages = listNumPages.get(listNumPages.size() - 1);
					} else {
						nbreMessagesPage = listNumPages.get(listNumPages.size() - 1)
								- listNumPages.get(listNumPages.size() - 2);
						lastPagesMessages = listNumPages.get(listNumPages.size() - 1);
					}
					// Construction
					// des URL des Pages
					int i = 0;
					while (i <= lastPagesMessages) {
						String urlPageMessages = "";
						if (topicWrapperType == 3 || topicWrapperType == 4) {
							// http://www.filsantejeunes.com/forum/ <-
							// typePagesTopics == 4
							// http://www.forum-politique.org <- typePagesTopics
							// ==
							// 3
							// if (urlTopic.indexOf("?sid=") != -1) {
							urlPageMessages = urlTopic.substring(0, urlTopic.indexOf(".html")) + "-" + i + ".html";
							// }
							// else {
							// ArrayList<String> listPagesTopics = new
							// ArrayList<String>(setUrlsPagesTopics);
							// String urlFirstPageTopics =
							// listPagesTopics.get(0);
							// if (urlFirstPageTopics.indexOf("/page")
							// != -1)
							// urlPageMessages = urlTopic + "page" + i +
							// ".html";
							// else if
							// (urlFirstPageTopics.indexOf("/index-s")
							// != -1)
							// urlPageMessages = urlTopic + "index-s" +
							// i + ".html";
							// }
						} else if (topicWrapperType == 1 || topicWrapperType == 6) {
							// http://forum.openstreetmap.fr/ <- typePagesTopics
							// = 1
							// http://thepunksite.com/forum/ <- typePagesTopics
							// = 1
							// http://www.cichlid-forum.com/phpBB/ <-
							// typePagesTopics = 1
							// http://scenari-platform.org/forum/ <-
							// typePagesTopics
							// = 6
							urlPageMessages = urlTopic + "&start=" + i;
						} else if (topicWrapperType == 5) {
							// https://bitcointalk.org/index.php?board=13.0
							urlPageMessages = urlTopic.substring(0, urlTopic.lastIndexOf(".") + 1) + i;
						} else if (topicWrapperType == 8) {
							// http://www.lesimpatientes.com/forums-cancer-sein-categories.asp
							urlPageMessages = urlTopic + "&pagecur=" + i;
						}

						listMessagesPagesUrls.add(urlPageMessages);
						//System.out.println("\t\t\tPage Messages : " + urlPageMessages);
						i = i + nbreMessagesPage;
						// System.out.println("\t\t\ti passe à : " + i);
					}
				} else
					listMessagesPagesUrls.add(urlTopic);
			} else {
				// http://forum.doctissimo.fr/grossesse-bebe/liste_categorie.htm
				// -> ne fait apparaitre que la derniere page
				if (listNumPages.size() > 0) {
					int lastPagesMessages = listNumPages.get(listNumPages.size() - 1);
					String urlPageMessages = "";
					for (int i = 1; i < lastPagesMessages + 1; i++) {
						urlPageMessages = urlTopic.substring(0, urlTopic.lastIndexOf("_") + 1) + i + ".htm";
						listMessagesPagesUrls.add(urlPageMessages);
//						System.out.println("\t\t\tPage Messages : " + urlPageMessages);
					}
				} else
					listMessagesPagesUrls.add(urlTopic);
			}

			// System.out.println("\t\t\tNombre messages / page = " +
			// nbreMessagesPage);
			// System.out.println("\t\t\tNombre temporaire de pages de messages
			// = "
			// + tempNbrePagesMessages);
			// System.out.println("\t\t\tDernière page de messages = " +
			// lastPagesMessages);

		}

		System.out.println("\t\t\t\t\t\tListe des pages de messages FINAL : ");
		int i = 1;
		for (String messagesPageUrl : listMessagesPagesUrls) {
			System.out.println("\t\t\t\t\t\tPage n°" + i + " : url = " + messagesPageUrl);
			i++;
		}

		return listMessagesPagesUrls;
	}

	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
