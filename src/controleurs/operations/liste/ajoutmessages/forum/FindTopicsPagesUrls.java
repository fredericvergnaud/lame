package controleurs.operations.liste.ajoutmessages.forum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import modeles.ForumModel;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FindTopicsPagesUrls {

	// @set MAP url topics / TopicModel pour chaque forum
	// @set typeTopics pour chaque forum
	// @return nbreTopics
	private Document doc;
	private ForumModel forum;
	
	public FindTopicsPagesUrls(Document doc, ForumModel forum) {
		this.doc = doc;
		this.forum = forum;
	}

	public List<String> getListTopicsPagesUrls() {

		// Recherche de la manière dont sont présentés les topics
		// 2 cas :
		// - les topics se trouvent tous sur la page fournie dans doc
		// => cas des forums impatientes ou des forums où l'on ne trouve pas de
		// page de pagination
		// (http://www.lesimpatientes.com/forums-cancer-sein-categories.asp)
		// - les topics se trouvent sur plusieurs pages
		// => cas des autres

		int forumWrapperType = forum.getWrapperType();
		String forumUrl = forum.getUrl();

		System.out.println(
				"Forum " + forum.getNom() + " | forumWrapperType " + forumWrapperType + " | url : " + forumUrl);

		// Recherche des liens de pagination
		Elements topicsPagesLinks = new Elements();
		// System.out.println("doc = "+ doc);
		String forumUrl2 = "";
		if (forumWrapperType == 2) {
			// http://www.forum-politique.org/
			// http://www.filsantejeunes.com/forum/
			topicsPagesLinks = doc.select("a[href*=/page][href*=.html]").not("a[href*=/page-]");
			if (topicsPagesLinks.size() == 0) {
				topicsPagesLinks = doc.select("a[href*=/index-s][href*=.html]");
				if (topicsPagesLinks.size() == 0) {
					if (forumUrl.indexOf(".html") > forumUrl.lastIndexOf("/") + 1) {
						forumUrl2 = forumUrl.substring(forumUrl.lastIndexOf("/") + 1, forumUrl.indexOf(".html"));
						System.out.println("forumUrl2 = " + forumUrl2);
						topicsPagesLinks = doc.select("a[href*=" + forumUrl2 + "-]");
					}
				}
			}
		} else if (forumWrapperType == 1)
			// http://forum.openstreetmap.fr/
			// http://thepunksite.com/forum/
			// http://www.cichlid-forum.com/phpBB/
			// http://scenari-platform.org/forum/
			topicsPagesLinks = doc.select("a[href*=viewforum.php][href*=start]").not("a[href*=start=0]");
		else if (forumWrapperType == 3)
			// https://bitcointalk.org/index.php?board=13.0
			topicsPagesLinks = doc.select("a[href*=index.php?board=]").not("a[href*=.0]").not("a[href*=sort]");
		else if (forumWrapperType == 4) {
			// http://forum.doctissimo.fr/grossesse-bebe/liste_categorie.htm
			topicsPagesLinks = doc.select("div.pagination_main_visible");
			topicsPagesLinks = topicsPagesLinks.select("a");
		}
		// typeForum = 5
		// (http://www.lesimpatientes.com/forums-cancer-sein-categories.asp)
		// => pas de pages de pagination
		else if (forumWrapperType == 6) {
			// https://reseau.batiactu.com/forum
			topicsPagesLinks = doc.select("a[href*=?page=]");
			topicsPagesLinks = topicsPagesLinks.select("a");
		}
		List<String> listTopicsPagesUrls = new ArrayList<String>();

		if (topicsPagesLinks.size() == 0) {
			// Aucun lien de pagination n'a été trouvé
			// => forum avec une seule page de topic
			// => récupération des liens de topics
			System.out.println("\tUne seule page de Topics");
			listTopicsPagesUrls.add(forumUrl);

			 // On set les variables de forum
			 forum.setNbreTopicsParPage(1);
			 forum.setNbrePagesTopics(1);
			 forum.setLastPagesTopics(1);
			 forum.setNbreTopicsEnviron(1);

		} else {
			// Des liens de pagination ont été trouvé
			// => plusieurs pages de topics
			System.out.println("\tPlusieurs pages de Topics");

			// On range les urls des éléments de pagination dans un set
			Set<String> setTopicsPagesUrl = new HashSet<String>();
			for (Element topicsPageLink : topicsPagesLinks) {
				String topicPageUrl = topicsPageLink.attr("abs:href");
				// System.out.println("\ttopicPageUrl : " + topicPageUrl);
				setTopicsPagesUrl.add(topicPageUrl);
			}

			// on va calculer le nombre de topics par page
			// et le nombre de pages de Topics
			int nbreTopicsPage = 0;
			int nbrePagesTopics = 1;
			int lastPagesTopics = 1;

			// On récupère les numéros de pages de topics dans l'url pour
			// remplir un set de numéros de page
			Set<Integer> setNumPages = new TreeSet<Integer>();
			Iterator<String> it = setTopicsPagesUrl.iterator();
			while (it.hasNext()) {
				String topicPageUrl = it.next();
//				System.out.println("\ttopicPageUrl : " + topicPageUrl);
				int numPage = 0;
				if (forumWrapperType == 2) {
					if (topicPageUrl.indexOf("/page") != -1)
						numPage = Integer.parseInt(topicPageUrl.substring(topicPageUrl.indexOf("/page") + 5,
								topicPageUrl.indexOf(".html")));
					else if (topicPageUrl.indexOf("/index-s") != -1)
						numPage = Integer.parseInt(topicPageUrl.substring(topicPageUrl.indexOf("/index-s") + 8,
								topicPageUrl.indexOf(".html")));
					else if (!forumUrl2.equals("")) {
						if (topicPageUrl.indexOf(forumUrl2 + "-") != -1)
							numPage = Integer.parseInt(topicPageUrl.substring(topicPageUrl.lastIndexOf("-") + 1,
									topicPageUrl.indexOf(".html")));
					}

				} else if (forumWrapperType == 1) {
					String[] varTab = topicPageUrl.split("&");
					for (String var : varTab) {
						if (var.indexOf("start=") != -1) {
							numPage = Integer.parseInt(var.substring(6));
							break;
						}
					}
				} else if (forumWrapperType == 3) {
					if (topicPageUrl.indexOf("index.php?board=") != -1)
						numPage = Integer.parseInt(topicPageUrl.substring(topicPageUrl.lastIndexOf(".") + 1));

				} else if (forumWrapperType == 4) {
					if (topicPageUrl.indexOf("liste_sujet") != -1)
						numPage = Integer.parseInt(topicPageUrl.substring(topicPageUrl.lastIndexOf("-") + 1,
								topicPageUrl.lastIndexOf(".htm")));
				} else if (forumWrapperType == 6) {
					if (topicPageUrl.indexOf("?page=") != -1)
						numPage = Integer.parseInt(topicPageUrl.substring(topicPageUrl.lastIndexOf("?page=") + 6));
				}
				setNumPages.add(numPage);
			}

//			System.out.println("\ttaille de setNumPages = " + setNumPages.size());
//			System.out.println(setNumPages);

			List<Integer> listNumPages = new ArrayList<Integer>(setNumPages);

			if (forumWrapperType == 4) {
				// Pour
				// http://forum.doctissimo.fr/grossesse-bebe/liste_categorie.htm,
				// les liens vont de 1 en 1, mais il y a 50 topics par page
				nbreTopicsPage = 50;
				lastPagesTopics = listNumPages.get(listNumPages.size() - 1);
				nbrePagesTopics = lastPagesTopics;
			} else if (forumWrapperType == 6) {
				// Pour
				// https://reseau.batiactu.com/forum/category-actualites-0
				// les liens vont de 1 en 1, mais il y a 20 topics par page
				nbreTopicsPage = 20;
				lastPagesTopics = listNumPages.get(listNumPages.size() - 1);
				nbrePagesTopics = lastPagesTopics;
			} else {
				nbreTopicsPage = listNumPages.get(0);
				lastPagesTopics = listNumPages.get(listNumPages.size() - 1);
				// Nombre de pages de Topics > 2
				if (listNumPages.size() > 1)
					nbrePagesTopics = (lastPagesTopics / nbreTopicsPage) + 1;
				else
					nbrePagesTopics = 2;
			}

			System.out.println("\t\tNombre de Topics par page = " + nbreTopicsPage);
			System.out.println("\t\tNombre de pages de Topics = " + nbrePagesTopics);
			System.out.println("\t\tDernière page de Topics = " + lastPagesTopics);

			forum.setNbreTopicsParPage(nbreTopicsPage);
			forum.setNbrePagesTopics(nbrePagesTopics);
			forum.setLastPagesTopics(lastPagesTopics);
			forum.setNbreTopicsEnviron();

			// Reconstruction manuelle
			// des URL des pages de Topics, car la pagination peut aller d'abord
			// de 1 en 1, puis de 10 en 10

			if (nbrePagesTopics > 1 && nbreTopicsPage > 0 && lastPagesTopics > 0) {
				int i = 0;
				if (forumWrapperType == 4)
					i = 1;
				while (i <= lastPagesTopics) {
					String urlPageTopics = "";
					if (forumWrapperType == 2) {
						if (forumUrl.indexOf("?sid=") != -1) {
							if (forumUrl2.equals(""))
								urlPageTopics = forumUrl.substring(0, forumUrl.lastIndexOf("/") + 1) + "page" + i
										+ ".html";
							else {
								urlPageTopics = forumUrl.substring(0, forumUrl.lastIndexOf("/") + 1) + forumUrl2 + "-"
										+ i + ".html";
							}
						} else {
							ArrayList<String> listPagesTopics = new ArrayList<String>(setTopicsPagesUrl);
							String urlFirstPageTopics = listPagesTopics.get(0);
							if (urlFirstPageTopics.indexOf("/page") != -1)
								urlPageTopics = forumUrl + "page" + i + ".html";
							else if (urlFirstPageTopics.indexOf("/index-s") != -1)
								urlPageTopics = forumUrl + "index-s" + i + ".html";
						}
					} else if (forumWrapperType == 1)
						urlPageTopics = forumUrl + "&start=" + i;
					else if (forumWrapperType == 3)
						urlPageTopics = forumUrl.substring(0, forumUrl.lastIndexOf(".") + 1) + i;
					else if (forumWrapperType == 4)
						urlPageTopics = forumUrl.substring(0, forumUrl.lastIndexOf("-") + 1) + i + ".htm";
					else if (forumWrapperType == 6)
						urlPageTopics = forumUrl + "?page=" + i;

					listTopicsPagesUrls.add(urlPageTopics);
//					System.out.println("\tPage Topics : " + urlPageTopics);
					if (forumWrapperType == 4 || forumWrapperType == 6 )
						i++;
					else
						i = i + nbreTopicsPage;					
				}
			} else {
				String urlPageTopics = forumUrl;
				// System.out.println("\tPage Topics : " +
				// urlPageTopics);
				listTopicsPagesUrls.add(urlPageTopics);				
			}
		}
		return listTopicsPagesUrls;
	}

}
