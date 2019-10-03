package controleurs.operations.liste.ajoutmessages.forum;

import java.util.ArrayList;
import java.util.List;
import modeles.ForumModel;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FindForums {

	// @Return map de forums

	private Document doc;
	private int forumWrapperType;
	private String siteUrl;

	public FindForums(Document doc, int forumWrapperType, String siteUrl) {
		this.doc = doc;
		this.forumWrapperType = forumWrapperType;
		this.siteUrl = siteUrl;
	}

	public List<ForumModel> getListForums() {

		List<ForumModel> listForums = new ArrayList<ForumModel>();

		if (forumWrapperType != 0) {
			// Récupération des liens de forum

			Elements forumsLinks = new Elements();

			// PHPBB
			if (forumWrapperType == 1 || forumWrapperType == 2) {
				// Sélection des liens a de class forumlink ou forumtitle
				forumsLinks = doc.select("a.forumlink, a.forumtitle");
			} else if (forumWrapperType == 3) {
				// Sélection des liens a qui ont un attribut href contenant
				// index.php?board=
				// et contenant .0 mais qui ne contient pas sort
				// -> Forums SMF : index.php?board=47.0 -> name="b47"
				forumsLinks = doc.select("a[href*=index.php?board=][href*=.0]").not("a[href*=sort]");
			} else if (forumWrapperType == 4) {
				// Sélection des liens a de classe cCatTopic
				// http://forum.doctissimo.fr/grossesse-bebe/liste_categorie.htm
				forumsLinks = doc.select("a.cCatTopic");
			} else if (forumWrapperType == 5) {
				// Sélection des liens a qui contiennent forumcat
				// http://www.lesimpatientes.com/forums-cancer-sein-categories.asp
				forumsLinks = doc.select("a[href*=forumcat]");
			} else if (forumWrapperType == 6) {
				// https://reseau.batiactu.com/forum
				forumsLinks = doc.select("a[href*=forum/category-]");
			}
			System.out.println("forumsLinks.size = " + forumsLinks.size());
			System.out.println("forumsLinks = \n" + forumsLinks);
			if (forumsLinks.size() > 0)
				for (Element forumLink : forumsLinks) {
					// URL et URL relative du forum
					String forumUrl = "", forumRelativeUrl = "", urlId = "";
					if (forumWrapperType == 5) {
						// Les forums sont tous sur la même page
						// Et sont sous une forme de lien que l'on déploie pour
						// faire apparaitre les topics
						// => on ne garde que l'identifiant utilisé par le
						// javascript pour déployer les topics = urlId
						urlId = forumLink.attr("href");
						if (urlId.indexOf("'forumcat") != -1)
							urlId = urlId.substring(urlId.indexOf("'") + 1, urlId.lastIndexOf("'"));
						System.out.println("urlId = " + urlId);
						forumUrl = siteUrl;
					} else {
						forumUrl = forumLink.attr("abs:href");
						String forumRelativeUrlTxt = forumLink.attr("href");
						if (!forumRelativeUrlTxt.equals("")) {
							if (forumRelativeUrlTxt.indexOf("?") != -1)
								forumRelativeUrl = forumRelativeUrlTxt.substring(0, forumRelativeUrlTxt.indexOf("?"));
						}

					}
					ForumModel forum = new ForumModel(forumUrl);
					forum.setWrapperType(forumWrapperType);
					forum.setRelativeUrl(forumRelativeUrl);
					forum.setUrlId(urlId);

					// Titre Forum
					String forumTitle = forumLink.text();
					forum.setNom(forumTitle);

					listForums.add(forum);
				}
		}
		return listForums;
	}
}
