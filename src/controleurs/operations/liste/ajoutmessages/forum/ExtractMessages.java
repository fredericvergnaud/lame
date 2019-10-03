package controleurs.operations.liste.ajoutmessages.forum;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modeles.MessageModel;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import controleurs.operations.liste.CalculSujetTronque;

public class ExtractMessages {

	private Map<String, MessageModel> newMapIdMessages = new HashMap<String, MessageModel>();
	private int idTopic;
	private String titreTopic;
	private Map<String, ArrayList<String>> mapUrlUserProfileListStats = new HashMap<String, ArrayList<String>>();
	private int sleeptime;
	private String nomForum;
	private int exception;
	private String exceptionMsg = "";
	private int messageWrapperType = 0;
	private Elements messagesWrappers;
	private int nbreVuesTopic = 0;

	public ExtractMessages(Elements messagesWrappers, int sleeptime, int messageWrapperType, int idTopic,
			String titreTopic, int nbreVuesTopic, String nomForum) {
		this.sleeptime = sleeptime;
		this.messageWrapperType = messageWrapperType;
		this.messagesWrappers = messagesWrappers;
		this.nbreVuesTopic = nbreVuesTopic;
		this.idTopic = idTopic;
		this.titreTopic = titreTopic;
		this.nomForum = nomForum;
		// System.out.println("GetTopicMessages - Constructeur : idTopic = " +
		// idTopic);
		// System.out.println("GetTopicMessages - Constructeur : titreTopic = "
		// + titreTopic);
		// System.out.println("GetTopicMessages - Constructeur : nomForum = " +
		// nomForum);
		// System.out.println("GetTopicMessages - Constructeur : nbreVuesTopic =
		// "
		// + nbreVuesTopic);
		// System.out.println("GetTopicMessages - Constructeur :
		// typePagesMessages = "
		// + typePagesMessages);
		// System.out.println("GetTopicMessages - Constructeur : sleepTime = " +
		// sleepTime);
		// this.idTopic = idTopic;
		// this.titreTopic = titleTopic;
		// this.typePageMessages = typePageMessages;
		// this.rows = rows;
		// this.activitesView = activitesView;
		// this.bundleOperationsListe = bundleOperationsListe;
		// this.sleepTime = sleepTime;
		// this.nomForum = nomForum;
		// this.doc = doc;
		// this.mapMessagesPage = mapMessagesPage;
		// this.mapLocuteursPage = mapLocuteursPage;
		// this.mapUrlUserProfileListStats = mapUrlUserProfileListStats;
		// this.treeMapIdTopicNbreVuesTopic = treeMapIdTopicNbreVuesTopic;
	}

	public void extract() {

		switch (messageWrapperType) {
		case 1:
			for (Element rowPost : messagesWrappers) {
				String idMessage = null, nom = null, date = null, post = null, urlUserProfile = "∅", roleUser = "∅",
						positionUser = "∅", dateRegistered = null, email = "∅", website = "∅", gender = "∅",
						location = "∅", signature = "∅", titreMessage = null;
				int readTimesTopic = 0, idUser = 0, activiyUser = 0, nbrePosts = 0, age = 0, numDansTopic = 0;
				double nbreEtoilesUser = 0;
				Elements trs = rowPost.select("tr.row1, tr.row2");
				for (int m = 0; m < trs.size() - 1; m++) {
					// System.out.println(trRow1);
					Element bAuteur = trs.get(m).select("div.postauthor").first();
					Element bPost = trs.get(m).select("div.postbody").first();
					Element bDate = trs.get(m + 1).select("td.postbottom").first();
					if (bAuteur != null && bDate != null) {
						// System.out.println(bAuteur);
						// System.out.println(bDate);
						// System.out
						// .println("###########################################");
						nom = bAuteur.text().trim();
						date = bDate.text().replace(",", "").trim();
						bPost.select("div").remove();
						post = Jsoup.parse(bPost.html().replaceAll("(?i)<br[^>]*>", "br2n")).text();
						post = post.replaceAll("br2n", "\n").trim();
						addMessageForum(idMessage, nom, date, post, idUser, urlUserProfile, roleUser, positionUser,
								activiyUser, nbreEtoilesUser, readTimesTopic, nbrePosts, dateRegistered, email, website,
								gender, age, location, signature, numDansTopic, titreMessage);
					}
				}
			}
			break;

		case 2:
			// http://www.forum-politique.org/
			// System.out.println(rows);

			for (Element rowPost : messagesWrappers) {
				// System.out.println("rowPost = :\n"+rowPost);
				String idMessage = null, nom = null, date = null, post = null, urlUserProfile = "∅", roleUser = "∅",
						positionUser = "∅", dateRegistered = null, email = "∅", website = "∅", gender = "∅",
						location = "∅", signature = "∅", titreMessage = null;
				int readTimesTopic = 0, idUser = 0, activiyUser = 0, nbrePosts = 0, age = 0, numDansTopic = 0;
				double nbreEtoilesUser = 0;
				Element bAuthor = rowPost.select("b.postauthor").first();
				Element tdDate = rowPost.select("td.gensmall").first();
				Element spanDetails = rowPost.select("span.postdetails").first();
				Element divPost = rowPost.select("div.postbody").first();
				Element imgRank = rowPost.select("img[src*=ranks]").first();
				Element tdRole = rowPost.select("td.profile table tr td.postdetails").first();
				if (bAuthor != null && tdDate != null && spanDetails != null && divPost != null) {
					// Nbre de vues
					readTimesTopic = nbreVuesTopic;

					// Nom
					nom = Jsoup.parse(bAuthor.html()).text();
					// System.out.println("nom = " + nom);
					// Date
					Element divDate = tdDate.select("div").last();
					String sujetDate = Jsoup.parse(divDate.html()).text();
					if (sujetDate.indexOf(":") != -1 && sujetDate.indexOf(",") != -1) {
						String[] sujetDateTab = sujetDate.split(": ");
						for (String s : sujetDateTab)
							if (s.indexOf(",") != -1 && s.indexOf(":") != -1)
								date = s;
						date = date.replace("\u00A0", "").replace(",", "").trim();
						// System.out.println("date = " + date);
					}
					// idMessage
					String urlPost = divDate.select("a").attr("href");
					// System.out.println("urlPost = "+urlPost);
					if (urlPost.indexOf("#p") != -1)
						idMessage = urlPost.substring(urlPost.indexOf("#p") + 2);
					// System.out.println("idMessage = " + idMessage);
					// Rang
					if (imgRank != null) {
						String srcImgRank = imgRank.attr("src");
						if (srcImgRank.indexOf("ranks") != -1) {
							String rankTxt = "";
							if (srcImgRank.indexOf(".gif") != -1)
								rankTxt = srcImgRank.substring(srcImgRank.lastIndexOf("/") + 1,
										srcImgRank.lastIndexOf(".gif"));
							else if (srcImgRank.indexOf(".jpg") != -1)
								rankTxt = srcImgRank.substring(srcImgRank.lastIndexOf("/") + 1,
										srcImgRank.lastIndexOf(".jpg"));
							if (!rankTxt.equals("")) {
								rankTxt = rankTxt.replaceAll("\\D+", "").trim();
								// System.out.println("rankTxt = " + rankTxt);
								if (isInteger(rankTxt))
									nbreEtoilesUser = Integer.parseInt(rankTxt);
							}
						}
					}
					// System.out.println("nbreEtoilesUser = " +
					// nbreEtoilesUser);
					// Position
					if (tdRole != null)
						positionUser = Jsoup.parse(tdRole.html()).text();
					// System.out.println("positionUser = " + positionUser);
					// Post details
					String[] parts = spanDetails.html().split("<br />");
					for (String part : parts) {
						part = part.replace("</b>", "");
						if (part.indexOf("Inscrit") != -1 || part.indexOf("Joined") != -1) {
							dateRegistered = part.substring(part.indexOf(":") + 1).trim();
							dateRegistered = Jsoup.parse(dateRegistered).text();
						} else if (part.indexOf("Message") != -1 || part.indexOf("Post") != -1) {
							String txtNbrePosts = part.substring(part.indexOf(":") + 1).trim();
							// System.out.println("txtNbrePosts = " +
							// txtNbrePosts);
							nbrePosts = Integer.parseInt(txtNbrePosts);
						} else if (part.indexOf("Localisation") != -1 || part.indexOf("Location") != -1) {
							location = part.substring(part.indexOf(":") + 1).trim();
							location = Jsoup.parse(location).text();
						}
					}

					// System.out.println("dateRegistered = " + dateRegistered);
					// System.out.println("nbrePosts = " + nbrePosts);
					// System.out.println("location = " + location);
					// Genre
					Element imgGender = spanDetails.select("img[src*=gender]").first();
					if (imgGender != null) {
						String txtSrcImgGender = imgGender.attr("src");
						// System.out.println("txtSrcImgGender = " +
						// txtSrcImgGender);
						String txtIconGender = txtSrcImgGender.substring(txtSrcImgGender.lastIndexOf("/") + 1,
								txtSrcImgGender.indexOf(".gif"));
						if (txtIconGender.indexOf("_x") == -1) {
							if (txtIconGender.indexOf("_m") != -1)
								gender = "M";
							else
								gender = "F";
						}
					}
					// System.out.println("gender = " + gender);
					// idUser
					Element aThanks = spanDetails.select("a[href*=author_id]").first();
					if (aThanks != null) {
						String txtAThanks = aThanks.attr("href");
						if (txtAThanks.indexOf("author_id") != -1) {
							String txtIdUser = txtAThanks.substring(txtAThanks.indexOf("author_id=") + 10,
									txtAThanks.indexOf("&give"));
							if (isInteger(txtIdUser))
								idUser = Integer.parseInt(txtIdUser);
						}
					} else {
						Random r = new Random();
						idUser = 1000000 + r.nextInt(2000000 - 1000000);
					}
					// System.out.println("idUser = " + idUser);
					// Post
					divPost.select("div.quotetitle").remove();
					Elements divQuotes = divPost.select("div.quotecontent");
					if (divQuotes.size() > 0) {
						divQuotes.after("<br></br>");
						divQuotes.prepend("| ");
						Elements brQuotes = divQuotes.select("br");
						if (brQuotes.size() > 0)
							brQuotes.prepend("| ");
					}
					post = Jsoup.parse(divPost.html().replaceAll("(?i)<br[^>]*>", "br2n")).text();
					post = post.replaceAll("br2n", "\n").trim();

					// Signature
					if (post.lastIndexOf("_________________") != -1) {
						signature = post.substring(post.lastIndexOf("_________________") + 17).replace("\n", " ");
						post = post.substring(0, post.lastIndexOf("_________________"));
					}
					// System.out.println("signature = " + signature);
					// System.out.println("post = " + post.length());

					System.out.println(nom + " | " + idUser + " | " + date + " | " + idMessage + " | " + nbrePosts
							+ " | " + dateRegistered + " | " + location + " | " + website + " | " + positionUser + " | "
							+ nbreEtoilesUser + " | " + gender);
					addMessageForum(idMessage, nom, date, post, idUser, urlUserProfile, roleUser, positionUser,
							activiyUser, nbreEtoilesUser, readTimesTopic, nbrePosts, dateRegistered, email, website,
							gender, age, location, signature, numDansTopic, titreMessage);
				}
			}
			break;

		case 3:
			// http://forum.openstreetmap.fr/
			// http://thepunksite.com/forum/
			// http://www.cichlid-forum.com/phpBB/
			// messagesRows = doc.select("div.post");
			// System.out.println("taille de messagesWrappers = " +
			// messagesWrappers.size());
			for (Element rowPost : messagesWrappers) {
				// System.out.println("rowPost = " + rowPost);
				String idMessage = null, nom = null, date = null, post = null, urlUserProfile = "∅", roleUser = "∅",
						positionUser = "∅", dateRegistered = null, email = "∅", website = "∅", gender = "∅",
						location = "∅", signature = "∅", titreMessage = null;
				int readTimesTopic = 0, idUser = 0, activiyUser = 0, nbrePosts = 0, age = 0, numDansTopic = 0;
				double nbreEtoilesUser = 0;
				// System.out.println("idTopic = " + idTopic);
				// System.out.println("taille de treeMapIdTopicNbreVuesTopic = "
				// + treeMapIdTopicNbreVuesTopic.size());

				// System.out.println("readTimesTopic = " + readTimesTopic);
				// Element aAuteur =
				// rowPost.select("p.author a[href*=viewprofile]").first();
				Element pAuteurDate = rowPost.select("p.author").first();
				Element divPost = rowPost.select("div.content").first();
				Element aMessage = rowPost.select("div.postbody a[href*=viewtopic.php]").first();
				Element dlProfile = rowPost.select("dl.postprofile").first();
				Element divSignature = rowPost.select("div.signature").first();
				// System.out.println("pAuteurDate = \n" + pAuteurDate);
				// System.out.println("divPost = \n" + divPost);
				// System.out.println("aMessage = \n" + aMessage);
				// System.out.println("dlProfile = \n" + dlProfile);
				if (pAuteurDate != null && divPost != null && aMessage != null
				// && dlProfile != null
				) {
					// Nbre de vues
					readTimesTopic = nbreVuesTopic;
					// Date
					String auteurDate = pAuteurDate.text();
					// System.out.println(auteurDate);
					if (auteurDate.indexOf("\u00BB") != -1 && auteurDate.indexOf(" ") != -1) {
						String[] auteurDateTab = auteurDate.split("\u00BB");
						date = auteurDateTab[1].replace(",", "").trim();
					} else if (auteurDate.indexOf(" ") != -1 && auteurDate.indexOf(",") != -1) {
						date = auteurDate.substring(auteurDate.indexOf(",") - 10).replace(",", "").trim();
					}
					// idMessage
					String txtAMessage = aMessage.attr("href");
					// System.out.println("txtAMessage = "+txtAMessage);
					if (txtAMessage.indexOf("viewtopic.php?p=") != -1 && txtAMessage.indexOf("&sid=") != -1) {
						idMessage = txtAMessage.substring(txtAMessage.indexOf("viewtopic.php?p=") + 16,
								txtAMessage.indexOf("&sid"));
					}
					// System.out.println("idMessage = "+idMessage);
					// Post
					divPost.select("cite").remove();
					Elements divQuotes = divPost.select("blockquote");
					if (divQuotes.size() > 0) {
						divQuotes.after("<br></br>");
						divQuotes.prepend("| ");
						Elements brQuotes = divQuotes.select("br");
						if (brQuotes.size() > 0)
							brQuotes.prepend("| ");
					}
					// System.out.println("divPost : \n" + divPost);
					post = Jsoup.parse(divPost.html().replaceAll("(?i)<br[^>]*>", "br2n")).text();
					post = post.replaceAll("br2n", "\n").trim();
					// Profil
					// Nom auteur
					nom = Jsoup.parse(dlProfile.select("dt").first().html()).text();
					// Url + idUser auteur
					Element aUser = dlProfile.select("dt").select("a").first();
					if (aUser != null) {
						urlUserProfile = dlProfile.select("dt").select("a").attr("href");
						if (urlUserProfile.indexOf("viewprofile&u=") != -1 && urlUserProfile.indexOf("&sid=") != -1) {
							String txtIdUser = urlUserProfile.substring(urlUserProfile.indexOf("viewprofile&u=") + 14,
									urlUserProfile.indexOf("&sid=")).trim();
							if (isInteger(txtIdUser))
								idUser = Integer.parseInt(txtIdUser);
						}
					} else {
						Random r = new Random();
						idUser = 1000000 + r.nextInt(2000000 - 1000000);
					}
					// Nbre messages
					Elements ddsProfile = dlProfile.select("dd");
					for (Element ddProfile : ddsProfile) {
						String ddTxt = ddProfile.text();
						// Nombre total de messages
						if (ddTxt.indexOf("Message") != -1 || ddTxt.indexOf("Post") != -1) {
							String txtNbreMessages = ddTxt.substring(ddTxt.indexOf(":") + 1).trim();
							if (isInteger(txtNbreMessages))
								nbrePosts = Integer.parseInt(txtNbreMessages);
						}
						// Date
						else if (ddTxt.indexOf("Inscription") != -1 || ddTxt.indexOf("Joined") != -1) {
							dateRegistered = ddTxt.substring(ddTxt.indexOf(":") + 1).trim();
							dateRegistered = Jsoup.parse(dateRegistered).text();
						}
						// Localisation
						else if (ddTxt.indexOf("Localisation") != -1 || ddTxt.indexOf("Location") != -1) {
							location = ddTxt.substring(ddTxt.indexOf(":") + 1).trim();
							location = Jsoup.parse(location).text();
						}
						// WebSite
						else if (ddTxt.indexOf("Site Internet") != -1 || ddTxt.indexOf("Website") != -1) {
							website = ddProfile.select("a").attr("href");
						} else {
							// Role
							if (!ddTxt.equals("\u00a0") && ddProfile.select("span").text().indexOf("YIM") == -1) {
								// System.out.println("ddTxt = " + ddTxt);
								roleUser = ddTxt;
								roleUser = Jsoup.parse(roleUser).text();
							}
						}
						// Position
						if (ddProfile.select("img").size() > 0) {
							positionUser = ddTxt.trim();
							positionUser = Jsoup.parse(positionUser).text();
							String imgTxt = ddProfile.select("img").attr("src");
							// System.out.println("imgTxt = "+imgTxt);
							if (imgTxt.indexOf("/images/ranks/") != -1) {
								String rankTxt = imgTxt.substring(imgTxt.indexOf("ranks/") + 6);
								// on ne garde que les digits
								rankTxt = rankTxt.replaceAll("\\D+", "").trim();
								// System.out.println("rankTxt = "+rankTxt);
								if (isInteger(rankTxt))
									nbreEtoilesUser = Integer.parseInt(rankTxt);
							}
						}
					}
					// Signature
					if (divSignature != null) {
						signature = divSignature.text();
						signature = Jsoup.parse(signature).text();
					}
					System.out.println(nom + " | " + idUser + " | " + date + " | " + idMessage + " | " + nbrePosts
							+ " | " + dateRegistered + " | " + location + " | " + website + " | " + positionUser + " | "
							+ nbreEtoilesUser + " | " + post.length() + " car.");
					addMessageForum(idMessage, nom, date, post, idUser, urlUserProfile, roleUser, positionUser,
							activiyUser, nbreEtoilesUser, readTimesTopic, nbrePosts, dateRegistered, email, website,
							gender, age, location, signature, numDansTopic, titreMessage);
				} else
					System.out.println("UN ELEMENT EST NULL");
			}
			break;

		case 4:
			// http://scenari-platform.org/forum/

			// System.out.println("rows size = " + rows.size());
			// System.out.println("rows = "+rows);
			// System.out.println("authors size = "+authors.size());

			String[] rows = messagesWrappers.html().split(
					"<td class=\"spaceRow\" colspan=\"2\" height=\"1\"><img src=\"templates/subSilver/images/spacer.gif\" alt=\"\" width=\"1\" height=\"1\" /></td>");
			for (String row : rows) {
				// System.out.println("########### ROW = ############\n"+row);
				Document rowPost = Jsoup.parse(row);
				// System.out.println("######## rowPost = #######\n" + rowPost);
				String idMessage = null, nom = null, date = null, post = null, urlUserProfile = "∅", roleUser = "∅",
						positionUser = "∅", dateRegistered = null, email = "∅", website = "∅", gender = "∅",
						location = "∅", signature = "∅", titreMessage = null;
				int readTimesTopic = 0, idUser = 0, activiyUser = 0, nbrePosts = 0, age = 0, numDansTopic = 0;
				double nbreEtoilesUser = 0;
				Element spanName = rowPost.select("span.name").first();
				Element spanDetails = rowPost.select("span.postdetails").first();
				// Element spanDate =
				// rowPost.select("td.row1 table tr td, td.row2 table tr
				// td").first();
				Element spanProfil = rowPost.select("td:has(a[href*=viewprofile])").first();
				Element spanDate = rowPost.select("td:has(a[href*=viewtopic])").first();
				Elements spanPost = rowPost.select("td:has(span.postbody)");
				// Element aWebsiteUser =
				// rowPost.select("a[target*=_userwww]").first();
				// System.out.println("spanName : "+spanName);
				// System.out.println("spanProfil : "+spanProfil);
				// System.out.println("spanPost : "+spanPost);
				// System.out.println("spanDate : "+spanDate);
				if (spanName != null && spanDetails != null && spanPost.size() > 0 && spanDate != null) {
					// System.out.println("spanName : "+spanName);
					// System.out.println("spanProfil : "+spanProfil);
					// System.out.println("spanPost : "+spanPost);
					// System.out.println("spanDate : "+spanDate);

					// Nbre de vues
					readTimesTopic = nbreVuesTopic;

					// Nom + idMessage
					nom = Jsoup.parse(spanName.html()).text();
					idMessage = spanName.select("a").attr("name");
					// Profil
					// System.out.println("spanProfil = " + spanProfil);
					String[] parts = spanDetails.html().split("<br />");
					for (String part : parts) {
						if (part.indexOf("Inscrit") != -1 || part.indexOf("Joined") != -1) {
							dateRegistered = part.substring(part.indexOf(":") + 1).trim();
							dateRegistered = Jsoup.parse(dateRegistered).text();
						} else if (part.indexOf("Message") != -1 || part.indexOf("Post") != -1) {
							String txtNbrePosts = part.substring(part.indexOf(":") + 1).trim();
							if (isInteger(txtNbrePosts))
								nbrePosts = Integer.parseInt(txtNbrePosts);
						} else if (part.indexOf("Localisation") != -1 || part.indexOf("Location") != -1) {
							location = part.substring(part.indexOf(":") + 1).trim();
							location = Jsoup.parse(location).text();
						}
					}
					// // url + id + website User
					if (spanProfil != null) {
						Element aUrlProfil = spanProfil.select("a[href*=viewprofile]").first();
						// System.out.println("aUrlProfil = "+aUrlProfil);
						urlUserProfile = aUrlProfil.attr("href");
						// System.out.println("urlUserProfile =
						// "+urlUserProfile);
						if (urlUserProfile.indexOf("viewprofile&u=") != -1 && urlUserProfile.indexOf("&sid=") != -1) {
							String txtIdUser = urlUserProfile.substring(urlUserProfile.indexOf("viewprofile&u=") + 14,
									urlUserProfile.indexOf("&sid=")).trim();
							if (isInteger(txtIdUser))
								idUser = Integer.parseInt(txtIdUser);
						} else {
							Random r = new Random();
							idUser = 1000000 + r.nextInt(2000000 - 1000000);
						}
						Element aWebsite = spanProfil.select("a[target=_userwww]").first();
						if (aWebsite != null)
							website = aWebsite.attr("href");
					} else {
						Random r = new Random();
						idUser = 1000000 + r.nextInt(2000000 - 1000000);
					}

					if (spanDate.text().indexOf("\u00A0") != -1 && spanDate.text().indexOf(":") != -1) {
						// System.out.println("pas de probleme sur date");
						String[] dateTab = spanDate.text().split("\u00A0");
						String[] dateTab2 = dateTab[0].split(": ");
						date = dateTab2[1].replace(",", "").trim();
					}
					// else
					// System.out.println("probleme sur date");
					// System.out.println(date);

					// Post
					spanPost.select("span.genmed").remove();
					Elements tdQuotes = spanPost.select("td.quote");
					if (tdQuotes.size() > 0) {
						tdQuotes.before("<br></br>");
						tdQuotes.after("<br></br>");
						tdQuotes.prepend("| ");
						Elements brQuotes = tdQuotes.select("br");
						if (brQuotes.size() > 0)
							brQuotes.prepend("| ");
					}
					post = Jsoup.parse(spanPost.html().replaceAll("(?i)<br[^>]*>", "br2n")).text();
					post = post.replaceAll("br2n", "\n").trim();
					// Signature
					if (post.lastIndexOf("_________________") != -1) {
						signature = post.substring(post.lastIndexOf("_________________") + 17).replace("\n", " ");
						post = post.substring(0, post.lastIndexOf("_________________"));
					}

					System.out.println(nom + " | " + idUser + " | " + date + " | " + idMessage + " | " + nbrePosts
							+ " | " + dateRegistered + " | " + location + " | " + website + " | " + positionUser + " | "
							+ nbreEtoilesUser + " | " + post.length() + " car.");

					addMessageForum(idMessage, nom, date, post, idUser, urlUserProfile, roleUser, positionUser,
							activiyUser, nbreEtoilesUser, readTimesTopic, nbrePosts, dateRegistered, email, website,
							gender, age, location, signature, numDansTopic, titreMessage);
				}
			}
			break;

		case 5:
			// System.out.println(rows);
			// for (Element rowPost : messagesRows) {
			// String idMessage = null, nom = null, date = null, post = null,
			// urlUserProfile = "∅", roleUser = "∅", positionUser = "∅",
			// dateRegistered = null, email = "∅", website = "∅", gender = "∅",
			// location = "∅", signature = "∅";
			// int readTimesTopic = 0, idUser = 0, activiyUser = 0, nbrePosts =
			// 0, age = 0, numDansTopic = 0;
			// double nbreEtoilesUser = 0;
			// Elements trs = rowPost.select("tr.row1, tr.row2");
			// for (int m = 0; m < trs.size() - 1; m++) {
			// Element bAuteur = trs.get(m).select("b.postauthor").first();
			// // System.out.println("Auteur : " + bAuteur);
			// Element bDate = trs.get(m).select("td.row3h").first();
			// // System.out.println("Date : " + bDate);
			// Element bPost = trs.get(m + 1).select("div.postbody").first();
			// // System.out.println("Post : " + bPost);
			// if (bAuteur != null && bDate != null && bPost != null) {
			// // System.out.println(bAuteur);
			// // System.out.println(bDate);
			// // System.out
			// //
			// // .println("###########################################");
			// nom = bAuteur.text().trim();
			// if (bDate.text().indexOf(":") != -1 &&
			// bDate.text().indexOf("\u00A0") != -1) {
			// date = bDate.text().substring(bDate.text().lastIndexOf(": ") +
			// 1).replace("\u00A0", "").trim();
			// }
			// bPost.select("div").remove();
			// post = Jsoup.parse(bPost.html().replaceAll("(?i)<br[^>]*>",
			// "br2n")).text();
			// post = post.replaceAll("br2n", "\n").trim();
			// addMessageForum(idMessage, nom, date, post, idTopic, titreTopic,
			// idUser, urlUserProfile, roleUser, positionUser, activiyUser,
			// nbreEtoilesUser, readTimesTopic, nbrePosts,
			// dateRegistered, email, website, gender, age, location, signature,
			// numDansTopic);
			// }
			// }
			// }
			break;
		case 6:
			// https://bitcointalk.org/index.php?board=13.0
			for (Element rowPost : messagesWrappers) {
				String idMessage = null, nom = null, date = null, post = null, urlUserProfile = "∅", roleUser = "∅",
						positionUser = "∅", dateRegistered = null, email = "∅", website = "∅", gender = "∅",
						location = "∅", signature = "∅", titreMessage = null;
				int idUser = 0, activiyUser = 0, nbrePosts = 0, age = 0, numDansTopic = 0, readTimesTopic = 0;
				double nbreEtoilesUser = 0;
				// System.out.println("rowPost = \n" + rowPost);

				Element tdAuthor = rowPost.select("td.poster_info").first();
				// if (tdAuthor != null)
				// System.out.println("\ttdAuthor : " + tdAuthor.text());
				Element divDate = rowPost.select("td.td_headerandpost").select("div.smalltext").first();
				// spanDate.addAll(rowPost.select("div.smalltext"));
				// if (divDate != null)System.out.println("\tdivDate: " +
				// divDate.text());
				Element divPost = rowPost.select("div.post").first();
				// if (divPost != null)System.out.println("\tdivPost: " +
				// divPost.text());
				Element aMessageNumber = rowPost.select("a.message_number").first();
				// if (aMessageNumber !=
				// null)System.out.println("\taMessageNumber: " +
				// aMessageNumber.text());
				if (tdAuthor != null && divDate != null && divDate.text().indexOf(":") != -1
						&& divDate.text().indexOf(",") != -1 && divPost != null && aMessageNumber != null) {
					// System.out.println("tdAuthor : " + tdAuthor.text());
					// System.out.println("divDate: " + divDate.text());
					// System.out.println("divPost: " + divPost.text());
					// System.out.println("aMessageNumber: " +
					// aMessageNumber.text());
					// Nbre de vues
					readTimesTopic = nbreVuesTopic;
					urlUserProfile = tdAuthor.select("a").attr("href");
					// System.out.println("tdAuthor : " + tdAuthor);
					// System.out.println("urlUserProfile = "+urlUserProfile);
					ArrayList<String> listStats = null;
					if (!urlUserProfile.equals("")) {
						if (!mapUrlUserProfileListStats.containsKey(urlUserProfile)) {
							// System.out.println();
							// System.out.println("N'EXISTE PAS : URL Profile
							// User = "
							// + urlUserProfile);
							// Page des stats
							listStats = getUserProfile(urlUserProfile);
						} else {
							// System.out.println();
							// System.out.println("EXISTE = " + urlUserProfile);
							listStats = mapUrlUserProfileListStats.get(urlUserProfile);
						}
						// System.out.println("listStats = " + listStats);

					} else
						positionUser = "Guest";
					if (listStats != null) {
						if (listStats.size() > 0) {
							// System.out.println("listStats.size = " +
							// listStats.size());
							for (String statTxt : listStats) {
								// System.out.println("statTxt = " +
								// statTxt);
								if (statTxt.indexOf("Name") != -1) {
									nom = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("nom = " + nom);
								} else if (statTxt.indexOf("Posts") != -1) {
									String txtNbrePosts = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("txtNbrePosts = " +
									// txtNbrePosts);
									if (isInteger(txtNbrePosts))
										nbrePosts = Integer.parseInt(txtNbrePosts);
								} else if (statTxt.indexOf("Activity") != -1) {
									String txtActivity = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("txtActivity = " +
									// txtActivity);
									if (isInteger(txtActivity))
										activiyUser = Integer.parseInt(txtActivity);
								} else if (statTxt.indexOf("Position") != -1) {
									positionUser = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("positionUser = " +
									// positionUser);
								} else if (statTxt.indexOf("Date Registered") != -1) {
									dateRegistered = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("dateRegistered = " +
									// dateRegistered);
								} else if (statTxt.indexOf("Email") != -1 && statTxt.indexOf("hidden") == -1) {
									email = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("email = " + email);
								} else if (statTxt.indexOf("Website") != -1 && statTxt.length() > 8) {
									website = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("website = " +
									// website);
								} else if (statTxt.indexOf("Gender") != -1 && statTxt.length() > 7) {
									gender = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("gender = " + gender);
								} else if (statTxt.indexOf("Age") != -1 && statTxt.indexOf("N/A") == -1) {
									String txtAge = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("txtAge = " + txtAge);
									if (isInteger(txtAge))
										age = Integer.parseInt(txtAge);
								} else if (statTxt.indexOf("Location") != -1 && statTxt.length() > 9) {
									location = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("location = " +
									// location);
								} else if (statTxt.indexOf("Signature") != -1 && statTxt.length() > 10) {
									signature = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("signature = " +
									// signature);
								}
								mapUrlUserProfileListStats.put(urlUserProfile, listStats);
							}
							// Id user
							if (urlUserProfile.indexOf("u=") != -1) {
								String txtIdUser = urlUserProfile.substring(urlUserProfile.indexOf("u=") + 2);
								if (isInteger(txtIdUser))
									idUser = Integer.parseInt(txtIdUser);
								// System.out.println("Id User = " + idUser);
							}
						}
					}
					// else
					// break;
					// Numéro dans Topic
					String txtAMessageNumber = aMessageNumber.text().trim();
					if (txtAMessageNumber.indexOf("#") != -1)
						txtAMessageNumber = txtAMessageNumber.replace("#", "");
					if (isInteger(txtAMessageNumber))
						numDansTopic = Integer.parseInt(txtAMessageNumber);
					// IdMessage
					String hrefAMessageNumber = aMessageNumber.attr("href");
					if (hrefAMessageNumber.indexOf("#msg") != -1)
						idMessage = hrefAMessageNumber.substring(hrefAMessageNumber.indexOf("#") + 1);
					// Etoiles
					Elements imgStars = tdAuthor.select("img[src$=.gif]");
					nbreEtoilesUser = 0;
					for (Element imgStar : imgStars) {
						String imgSrc = imgStar.attr("src").substring(imgStar.attr("src").lastIndexOf("/") + 1,
								imgStar.attr("src").lastIndexOf(".gif"));
						if (imgSrc.equals("star"))
							nbreEtoilesUser++;
						// else
						// System.out.println("imgSrc = " + imgSrc);
					}
					// System.out.println("nbre Etoiles = " +
					// nbreEtoilesUser);
					// Role
					if (positionUser.equals("Staff")) {
						Element divSmallText = tdAuthor.select("div.smalltext").first();
						if (divSmallText != null) {
							String smallText = divSmallText.text().trim();
							if (smallText.indexOf("Offline") != -1)
								smallText = smallText.replace("Offline", "|");
							else if (smallText.indexOf("Online") != -1)
								smallText = smallText.replace("Online", "|");
							if (smallText.indexOf("Ignore") != -1)
								smallText = smallText.replace("Ignore", "");

							smallText = smallText.trim();
							// System.out.println("smallText = " + smallText);
							if (smallText.contains("|")) {
								String[] tabSmallText = smallText.split("\\|");
								// System.out.println("tabSmallText.length = " +
								// tabSmallText.length);
								if (tabSmallText.length > 1) {
									String firstTxt = tabSmallText[0].trim();
									if (firstTxt.indexOf(" ") != -1) {
										positionUser = firstTxt.substring(firstTxt.indexOf(" ")).trim();
										roleUser = firstTxt.substring(0, firstTxt.indexOf(" ")).trim();
										nbreEtoilesUser = 4.5;
									}
									String txtActivity = tabSmallText[1];
									if (txtActivity.indexOf("Activity:") != -1) {
										txtActivity = txtActivity.replace("Activity:", "");
										txtActivity = txtActivity.trim();
										// System.out.println("txtActivity = " +
										// txtActivity);
										if (txtActivity.indexOf(" ") != -1) {
											if (isInteger(txtActivity.substring(0, txtActivity.indexOf(" "))))
												activiyUser = Integer
														.parseInt(txtActivity.substring(0, txtActivity.indexOf(" ")));
										} else {
											if (isInteger(txtActivity))
												activiyUser = Integer.parseInt(txtActivity);
										}
									}
								}
							}
							// System.out.println("positionUser = " +
							// positionUser);
							// System.out.println("activiyUser = " +
							// activiyUser);
						}
					}

					date = divDate.text().trim();
					// Projet Bitcoins :
					// On supprime le quote header
					divPost.select("div.quoteheader").remove();
					// Mais on garde le div quote pour ajouter un | au texte
					// du
					// div
					Elements divQuotes = divPost.select("div.quote");
					if (divQuotes.size() > 0) {
						divQuotes.after("<br></br>");
						divQuotes.prepend("| ");
						Elements brQuotes = divQuotes.select("br");
						if (brQuotes.size() > 0)
							brQuotes.prepend("| ");
					}
					// System.out.println("divPost : \n" + divPost);
					post = Jsoup.parse(divPost.html().replaceAll("(?i)<br[^>]*>", "br2n")).text();
					post = post.replaceAll("br2n", "\n").trim();
					// System.out.println("post : \n" + post);
					System.out.println(nom + " | " + idUser + " | " + date + " | " + idMessage + " | " + nbrePosts
							+ " | " + dateRegistered + " | " + location + " | " + website + " | " + positionUser + " | "
							+ nbreEtoilesUser + " | " + post.length() + " car.");

					addMessageForum(idMessage, nom, date, post, idUser, urlUserProfile, roleUser, positionUser,
							activiyUser, nbreEtoilesUser, readTimesTopic, nbrePosts, dateRegistered, email, website,
							gender, age, location, signature, numDansTopic, titreMessage);
				}
			}
			break;
		case 7:
			// http://www.filsantejeunes.com/forum/
			// messagesRows = doc.select("div.posts-obj");
			// System.out.println("taille de rows = " + rows.size());
			// Dates
			Elements divsDate = messagesWrappers.select("div.box02 > div.cols2 > div.col1");
			List<String> listDates = new ArrayList<String>();
			for (Element divDate : divsDate) {
				listDates.add(divDate.text());
			}
			// Elements divsPost = messagesRows.select("div.postbody");
			// System.out.println("taille de listDates = " + listDates.size());
			// System.out.println("taille de divsPost = " + divsPost.size());
			messagesWrappers = messagesWrappers.select("div.postbody");
			for (int i = 0; i < messagesWrappers.size(); i++) {
				Element rowPost = messagesWrappers.get(i);
				String idMessage = null, nom = null, date = null, post = null, urlUserProfile = "∅", roleUser = "∅",
						positionUser = "∅", dateRegistered = null, email = "∅", website = "∅", gender = "∅",
						location = "∅", signature = "∅", titreMessage = null;
				int idUser = 0, activiyUser = 0, nbrePosts = 0, age = 0, numDansTopic = 0, readTimesTopic = 0;
				double nbreEtoilesUser = 0;
				// System.out.println("idTopic = " + idTopic);
				// System.out.println("taille de treeMapIdTopicNbreVuesTopic = "
				// + treeMapIdTopicNbreVuesTopic.size());
				readTimesTopic = nbreVuesTopic;
				// System.out.println("readTimesTopic = " + readTimesTopic);
				// Element aAuteur =
				// rowPost.select("p.author a[href*=viewprofile]").first();
				Element divPost = rowPost.select("div.detail").first();
				Element divProfile = rowPost.select("div[id*=profile]").first();
				// System.out.println("divDate = \n" + divDate);
				// System.out.println("divAuteur = \n" + divAuteur);
				// System.out.println("divPost = \n" + divPost);
				// System.out.println("divProfile = \n" + divProfile);
				if (divPost != null && divProfile != null) {
					// Date message
					date = listDates.get(i);
					// // idMessage
					String txtProfile = divProfile.id();
					idMessage = txtProfile.replaceAll("\\D+", "").trim();
					// Post
					divPost.select("cite").remove();
					divPost.select("span").remove();
					Elements divQuotes = divPost.select("blockquote");
					if (divQuotes.size() > 0) {
						divQuotes.after("<br></br>");
						divQuotes.prepend("| ");
						Elements brQuotes = divQuotes.select("br");
						if (brQuotes.size() > 0)
							brQuotes.prepend("| ");
					}
					// System.out.println("divPost : \n" + divPost);
					post = Jsoup.parse(divPost.html().replaceAll("(?i)<br[^>]*>", "br2n")).text();
					post = post.replaceAll("br2n", "\n").trim();
					// Profil
					// Nom auteur
					nom = Jsoup.parse(divProfile.select("div.name").first().html()).text();
					// Url + idUser auteur
					Element aUser = divProfile.select("div.name").select("a").first();
					if (aUser != null) {
						urlUserProfile = divProfile.select("div.name").select("a").attr("href");
						if (urlUserProfile.indexOf("/") != -1 && urlUserProfile.indexOf(".html") != -1) {
							String txtIdUser = urlUserProfile
									.substring(urlUserProfile.lastIndexOf("/") + 1, urlUserProfile.indexOf(".html"))
									.trim();
							txtIdUser = txtIdUser.replaceAll("\\D+", "").trim();
							// System.out.println("txtIdUser = " + txtIdUser);
							if (isInteger(txtIdUser))
								idUser = Integer.parseInt(txtIdUser);
						}
					} else {
						Random r = new Random();
						idUser = 1000000 + r.nextInt(2000000 - 1000000);
					}
					// Date d'inscription + Nbre messages
					Element ulInfo = divProfile.select("ul.info").first();
					Elements lisInfo = ulInfo.select("li");
					for (Element liInfo : lisInfo) {
						String liTxt = liInfo.text();
						// System.out.println("liTxt = " + liTxt);
						// Inscription
						if (liTxt.indexOf("Inscription") != -1 || liTxt.indexOf("Joined") != -1) {
							dateRegistered = liTxt.substring(liTxt.indexOf(":") + 1).trim();
							dateRegistered = Jsoup.parse(dateRegistered).text();
						}
						// // Nombre total de messages
						else if (liTxt.indexOf("Message") != -1 || liTxt.indexOf("Post") != -1) {
							String txtNbrePosts = liTxt.substring(liTxt.indexOf(":") + 1).trim();
							if (isInteger(txtNbrePosts))
								nbrePosts = Integer.parseInt(txtNbrePosts);
						} // Localisation
						else if (liTxt.indexOf("Localisation") != -1 || liTxt.indexOf("Location") != -1) {
							location = liTxt.substring(liTxt.indexOf(":") + 1).trim();
							location = Jsoup.parse(location).text();
						}
						// WebSite
						else if (liTxt.indexOf("Site Internet") != -1 || liTxt.indexOf("Website") != -1) {
							website = liInfo.select("a").attr("href");
						}
					}
					// Position
					if (divProfile.select("div.role").size() > 0) {
						positionUser = divProfile.select("div.role").text().trim();
						positionUser = Jsoup.parse(positionUser).text();
						String imgTxt = divProfile.select("div.role").select("img").attr("src");
						// System.out.println("imgTxt = "+imgTxt);
						if (imgTxt.indexOf("/images/ranks/") != -1) {
							String rankTxt = imgTxt.substring(imgTxt.indexOf("ranks/") + 6);
							rankTxt = rankTxt.replaceAll("\\D+", "").trim();
							// System.out.println("rankTxt = "+rankTxt);
							if (isInteger(rankTxt))
								nbreEtoilesUser = Integer.parseInt(rankTxt);
						}
					}
					// Signature
					Element divSignature = rowPost.select("div.signature").first();
					if (divSignature != null) {
						signature = Jsoup.parse(divSignature.html()).text();
					}
					System.out.println(nom + " | " + idUser + " | " + date + " | " + idMessage + " | " + nbrePosts
							+ " | " + dateRegistered + " | " + location + " | " + website + " | " + positionUser + " | "
							+ nbreEtoilesUser);

					addMessageForum(idMessage, nom, date, post, idUser, urlUserProfile, roleUser, positionUser,
							activiyUser, nbreEtoilesUser, readTimesTopic, nbrePosts, dateRegistered, email, website,
							gender, age, location, signature, numDansTopic, titreMessage);
				}
				// else {
				// System.out.println("UN ELEMENT EST NULL");
				// activitesView.setStepProgress(1);
				// activitesView.updateProgress();
				// JOptionPane.showMessageDialog(null,
				// bundleOperationsListe.getString("txt_ErreurFormatPage"),
				// bundleOperationsListe.getString("txt_ErreurFormat"),
				// JOptionPane.ERROR_MESSAGE);
				// }
			}
			break;
		case 8:
			// http://forum.doctissimo.fr/
			for (Element rowPost : messagesWrappers) {
				String idMessage = null, nom = null, date = null, post = null, urlUserProfile = "∅", roleUser = "∅",
						positionUser = "∅", dateRegistered = null, email = "∅", website = "∅", gender = "∅",
						location = "∅", signature = "∅", titreMessage = null;
				int idUser = 0, activiyUser = 0, nbrePosts = 0, age = 0, numDansTopic = 0, readTimesTopic = 0;
				double nbreEtoilesUser = 0;
				// System.out.println("rowPost = \n" + rowPost);

				Element tdAuthor = rowPost.select("td.messCase1").first();
				Element spanDate = rowPost.select("span.topic_posted").first();
				Element divPost = rowPost.select("div.post_content").first();
				Element aMessageNumber = rowPost.select("a[href*=#t]").first();
				if (tdAuthor != null && spanDate != null && spanDate.text().indexOf(":") != -1
						&& spanDate.text().indexOf("à") != -1 && divPost != null && aMessageNumber != null) {
					// System.out.println("tdAuthor : " + tdAuthor.text());
					// System.out.println("spanDate: " + spanDate.text());
					// System.out.println("divPost: " + divPost.text());
					// System.out.println("aMessageNumber href: " +
					// aMessageNumber.attr("href"));
					// Nbre de vues
					readTimesTopic = nbreVuesTopic;
					// Auteur
					Element bAuthor = tdAuthor.select("b.s2").first();
					nom = bAuthor.text().trim();
					Element spanMoodStatus = tdAuthor.select("span.MoodStatus").first();
					if (spanMoodStatus != null) {
						String spanMoodStatusHtml = spanMoodStatus.html();
						if (spanMoodStatusHtml.indexOf("<br>") != -1) {
							String[] spanMoodStatusHtmlTab = spanMoodStatusHtml.split("<br>");
							signature = spanMoodStatusHtmlTab[0];
							roleUser = spanMoodStatusHtmlTab[1].replace("<b>Profil : </b>", "").trim();
						} else
							roleUser = spanMoodStatusHtml.replace("<b>Profil : </b>", "").trim();
					}

					ArrayList<String> listStats = null;
					if (!nom.equals("Profil supprimé")) {
						String userProfileTxt = nom.replaceAll("(?![_-])\\p{P}", "").replaceAll(" ", "-").toLowerCase();
						urlUserProfile = "http://club.doctissimo.fr/" + userProfileTxt;
						// System.out.println("urlUserProfile = " +
						// urlUserProfile);
						if (!mapUrlUserProfileListStats.containsKey(urlUserProfile)) {
							// System.out.println();
							// System.out.println("N'EXISTE PAS : URL Profile
							// User = "
							// + urlUserProfile);
							// Page des stats
							listStats = getUserProfile(urlUserProfile);
						} else {
							// System.out.println();
							// System.out.println("EXISTE = " + urlUserProfile);
							listStats = mapUrlUserProfileListStats.get(urlUserProfile);
						}
						// System.out.println("listStats = " + listStats);

					}
					if (listStats != null) {
						if (listStats.size() > 0) {
							// System.out.println("listStats.size = " +
							// listStats.size());
							for (String statTxt : listStats) {
								// System.out.println("statTxt = " +
								// statTxt);
								if (statTxt.indexOf("Gender") != -1) {
									gender = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("gender = " + gender);
								} else if (statTxt.indexOf("Age") != -1) {
									String txtAge = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("txtAge = " + txtAge);
									if (isInteger(txtAge))
										age = Integer.parseInt(txtAge);
								} else if (statTxt.indexOf("Posts") != -1) {
									String txtNbrePosts = statTxt.substring(statTxt.indexOf(":") + 1).trim();
									// System.out.println("txtAge = " + txtAge);
									if (isInteger(txtNbrePosts))
										nbrePosts = Integer.parseInt(txtNbrePosts);
								}
								mapUrlUserProfileListStats.put(urlUserProfile, listStats);
							}
						}
					}
					// System.out.println("gender = " + gender);
					// System.out.println("age = " + age);
					// System.out.println("posts = " + nbrePosts);

					// IdMessage
					if (!aMessageNumber.equals("")) {
						String aMessageNumberTxt = aMessageNumber.attr("href").trim();
						if (aMessageNumberTxt.indexOf("#t") != -1)
							idMessage = aMessageNumberTxt.substring(aMessageNumberTxt.indexOf("#t") + 2);
					}
					// System.out.println("idMessage = " + idMessage);

					// IdUser
					Element spanIdUser = tdAuthor.select("span[data-id_user]").first();
					// System.out.println("spanIdUser = " + spanIdUser);
					if (spanIdUser != null) {
						String spanIdUserTxt = spanIdUser.attr("data-id_user").trim();
						if (isInteger(spanIdUserTxt))
							idUser = Integer.parseInt(spanIdUserTxt);
					}
					// System.out.println("idUser = " + idUser);

					// Signature
					Element spanSignature = divPost.select("span.signature").first();
					if (spanSignature != null)
						signature = spanSignature.text();

					String spanDateTxt = spanDate.text().replace("\u00A0", " ").trim();
					// System.out.println("spanDateTxt = " + spanDateTxt);
					date = spanDateTxt.replaceAll("Posté le ", "");
					// System.out.println("date = " + date);
					date = date.replaceAll("à ", "").trim();

					// Post
					divPost.select("div.edited").remove();
					divPost.select("span.signature").remove();
					Elements divQuotes = divPost.select("table.citation");
					if (divQuotes.size() > 0) {
						divQuotes.after("<br></br>");
						divQuotes.prepend("| ");
						Elements brQuotes = divQuotes.select("br");
						if (brQuotes.size() > 0)
							brQuotes.prepend("| ");
					}
					// System.out.println("divPost : \n" + divPost);
					post = Jsoup.parse(divPost.html().replaceAll("(?i)<br[^>]*>", "br2n")).text();
					post = post.replaceAll("br2n", "\n").trim();
					// System.out.println(nom + " | " + idUser + " | " + date +
					// " | " + idMessage + " | " + nbrePosts
					// + " | " + dateRegistered + " | " + location + " | " +
					// website + " | " + positionUser + " | "
					// + nbreEtoilesUser + " | " + post.length() + " car." + " |
					// " + age + " | " + gender);
					//
					addMessageForum(idMessage, nom, date, post, idUser, urlUserProfile, roleUser, positionUser,
							activiyUser, nbreEtoilesUser, readTimesTopic, nbrePosts, dateRegistered, email, website,
							gender, age, location, signature, numDansTopic, titreMessage);
				}
			}
			break;
		case 9:
			// http://www.lesimpatientes.com/forums-cancer-sein-categories.asp
			System.out.println("messagesWrappers avant : \n" + messagesWrappers);
			// On nettoie les bugs : des div.contrib à l'interieur de rowPost
			// (basé sur div.contrib)
			Elements innerDivContrib = messagesWrappers.select("div.contrib > div.contrib");
			if (innerDivContrib.size() > 0) {
				// on les supprime de là où ils sont, c'est à dire à l'intérieur
				// d'un div.contrib
				messagesWrappers.removeAll(innerDivContrib);
			}
			System.out.println("messagesWrappers après : \n" + messagesWrappers);
			int i;
			Element rowPost;
			for (i = 0; i < messagesWrappers.size(); i++) {
				rowPost = messagesWrappers.get(i);
				String idMessage = null, nom = null, date = null, post = null, urlUserProfile = "∅", roleUser = "∅",
						positionUser = "∅", dateRegistered = null, email = "∅", website = "∅", gender = "∅",
						location = "∅", signature = "∅", titreMessage = "";
				int idUser = 0, activiyUser = 0, nbrePosts = 0, age = 0, numDansTopic = 0, readTimesTopic = 0;
				double nbreEtoilesUser = 0;
				// System.out.println("rowPost = \n" + rowPost);

				Element spanTitleMessage = rowPost.select("span.contrib-title").first();
				Element spanSign = rowPost.select("span.contrib-sign").first();

				// System.out.println("spanTitleMessage = " + spanTitleMessage);
				// System.out.println("spanSign = " + spanSign);

				if (spanTitleMessage != null && spanSign != null) {

					// Auteur
					Element bAuthor = spanSign.select("b").first();
					nom = bAuthor.text().replace("par", "").replace(",", "").trim();

					// Id message
					String spanSignTxt = spanSign.text();
					Pattern pattern = Pattern.compile("\\[(.*?)\\]");
					Matcher matcher = pattern.matcher(spanSignTxt);
					matcher.find();
					idMessage = matcher.group(1);

					// Date
					if (spanSignTxt.indexOf("posté le") != -1)
						spanSignTxt = spanSignTxt
								.substring(spanSignTxt.indexOf("posté le") + 8, spanSignTxt.indexOf("par")).trim();
					date = spanSignTxt;

					// Titre message
					titreMessage = spanTitleMessage.text().trim();

					// post
					Elements elemsToRemoveFromPost = rowPost.select("span");
					elemsToRemoveFromPost.remove();
					post = rowPost.text();

					System.out.println(titreMessage + " | " + nom + " | " + idUser + " | " + date + " | " + idMessage
							+ " | " + nbrePosts + " | " + dateRegistered + " | " + location + " | " + website + " | "
							+ positionUser + " | " + nbreEtoilesUser + " | " + post.length() + " car." + " | " + age
							+ " | " + gender);
					//
					addMessageForum(idMessage, nom, date, post, idUser, urlUserProfile, roleUser, positionUser,
							activiyUser, nbreEtoilesUser, readTimesTopic, nbrePosts, dateRegistered, email, website,
							gender, age, location, signature, numDansTopic, titreMessage);
				}
			}
			break;
		case 10:
			// https://reseau.batiactu.com/forum/sujet-est-il-possible-de-seulement-changer-le-cylindre-dune-serrure-5-points-1452-8079
			// Element rowPost;
			for (i = 0; i < messagesWrappers.size(); i++) {
				rowPost = messagesWrappers.get(i);
				String idMessage = null, nom = null, date = null, post = null, urlUserProfile = "∅", roleUser = "∅",
						positionUser = "∅", dateRegistered = null, email = "∅", website = "∅", gender = "∅",
						location = "∅", signature = "∅", titreMessage = "";
				int idUser = 0, activiyUser = 0, nbrePosts = 0, age = 0, numDansTopic = 0, readTimesTopic = 0;
				double nbreEtoilesUser = 0;
				// System.out.println("rowPost = \n" + rowPost);

				titreMessage = titreTopic;

				// Auteur
				// pour les réponses
				Elements messageNameElements = rowPost.select("div.inn-simi a");
				if (messageNameElements != null) {
					for (Element messageNameElement : messageNameElements) {
						if (messageNameElement.text() != null) {
							urlUserProfile = messageNameElement.absUrl("href");
							nom = messageNameElement.text();
						}
					}
				}
				if (nom == null) {
					// pour la question
					// on en profite pour ajouter date, id profil et url profil
					Element questionHeader = rowPost.selectFirst("div.sujet-head-ago");
					String questionHeaderNomDate = questionHeader.text();
					nom = questionHeaderNomDate.substring(questionHeaderNomDate.indexOf("par ") + 4,
							questionHeaderNomDate.indexOf("il y a")).trim();
					date = questionHeaderNomDate.substring(questionHeaderNomDate.indexOf("il y a") + 6).trim();
					Element questionHeaderLink = questionHeader.selectFirst("a");
					if (questionHeaderLink != null) {
						urlUserProfile = questionHeaderLink.absUrl("href");
					}
				}

				// id message
				Element button = rowPost.selectFirst("button.replies");
				idMessage = button.attr("data-abuse-id");

				// Date
				// si date null car pas trouvée dans question
				if (date == null) {
					date = rowPost.selectFirst("div.head-with-btn i").text();
					date = date.substring(date.indexOf("il y a") + 6).trim();
				}

				// post
				// question
				Element postElement = rowPost.selectFirst("div.texte");
				if (postElement == null)
					postElement = rowPost.selectFirst("div.readonly-" + idMessage);
				post = postElement.text();

				// profil
				if (!urlUserProfile.equals("∅") && !urlUserProfile.equals("")) {
					ArrayList<String> listStats = null;

					if (!mapUrlUserProfileListStats.containsKey(urlUserProfile)) {
						// System.out.println();
						// System.out.println("N'EXISTE PAS : URL Profile
						// User = "
						// + urlUserProfile);
						// Page des stats
						listStats = getUserProfile(urlUserProfile);
					} else {
						// System.out.println();
						// System.out.println("EXISTE = " + urlUserProfile);
						listStats = mapUrlUserProfileListStats.get(urlUserProfile);
					}
					if (listStats != null) {
						if (listStats.size() > 0) {
							// System.out.println("listStats.size = " +
							// listStats.size());
							for (String statTxt : listStats) {
								// System.out.println("statTxt = " +
								// statTxt);
								String txt = statTxt.substring(statTxt.indexOf(":") + 1).trim();
								if (statTxt.indexOf("Description") != -1)
									signature = txt;
								else if (statTxt.indexOf("SavoirFaire") != -1)
									roleUser = txt;
								else if (statTxt.indexOf("Formations") != -1)
									positionUser = txt;
								else if (statTxt.indexOf("Experiences") != -1)
									gender = txt;
								mapUrlUserProfileListStats.put(urlUserProfile, listStats);
							}
						}
					}
				}
				// System.out.println("titre = " + titreMessage + " | nom = " + nom + " |
				// idMessage = " + idMessage
				// + " | date = " + date + " | post = " + post.length() + " | urlUserProfile = "
				// + urlUserProfile
				// + " | signature = " + signature + " | role = " + roleUser + " | position = "
				// + positionUser
				// + " | gender = " + gender);
				addMessageForum(idMessage, nom, date, post, idUser, urlUserProfile, roleUser, positionUser, activiyUser,
						nbreEtoilesUser, readTimesTopic, nbrePosts, dateRegistered, email, website, gender, age,
						location, signature, numDansTopic, titreMessage);
			}
			break;
		default:
			break;
		}
	}

	private ArrayList<String> getUserProfile(String urlUserProfile) {
		ArrayList<String> listUserProfile = new ArrayList<String>();
		Response response = null;
		Document doc = null;
		try {
			try {
				Thread.sleep(sleeptime);
			} catch (Exception e) {
				// declenche uniquement si interrompu par un autre
				// thread :
				// ne devrait pas arriver
			}
			// Connexion page profil
			Connection connection = Jsoup.connect(urlUserProfile).userAgent(
					"Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5")
					.timeout(10000).ignoreHttpErrors(true);
			response = connection.execute();
			// System.out.println("getUserProfile : response.statusCode() = " +
			// response.statusCode());
			if (response.statusCode() == 200) {
				doc = connection.get();
				if (messageWrapperType == 6) {
					// https://bitcointalk.org/index.php?board=13.0
					Elements trsWindowBg = doc.select("td.windowbg table tr");
					// System.out.println("trsWindowBg.size = " +
					// trsWindowBg.size());
					if (trsWindowBg.size() > 0) {
						for (Element tr : trsWindowBg) {
							String trTxt = tr.text();
							// System.out.println("trTxt = " + trTxt);
							if (trTxt.indexOf("Name") != -1 || trTxt.indexOf("Posts") != -1
									|| trTxt.indexOf("Activity") != -1 || trTxt.indexOf("Position") != -1
									|| trTxt.indexOf("Date Registered") != -1 || trTxt.indexOf("Email") != -1
									|| trTxt.indexOf("Website") != -1 || trTxt.indexOf("Gender") != -1
									|| trTxt.indexOf("Age") != -1 || trTxt.indexOf("Location") != -1
									|| trTxt.indexOf("Signature") != -1)
								listUserProfile.add(tr.text());
						}
					}
				} else if (messageWrapperType == 8) {
					// http://forum.doctissimo.fr/
					Element divLeft = doc.select("div.small_column_left").first();
					if (divLeft != null) {
						divLeft.select("div#more_informations").remove();
						Element divLeftPanel = divLeft.select("div.small_column_left_panel").last();
						if (divLeftPanel != null) {
							divLeftPanel.select("span.space_about").remove();
							divLeftPanel.select("img").remove();
							String divLeftPanelHtml = divLeftPanel.html();
							if (divLeftPanelHtml.indexOf("<br>") != -1) {
								// System.out.println("divLeftPanelHtml = " +
								// divLeftPanelHtml);
								String[] divLeftPanelTab = divLeftPanelHtml.split("<br>");
								// System.out.println("divLeftPanelTab length =
								// " + divLeftPanelTab.length);
								Element elementGender = Jsoup.parse(divLeftPanelTab[0]);
								// System.out.println("elementGender = " +
								// elementGender);
								String genderTxt = "∅";
								if (elementGender != null) {
									Element bGender = elementGender.select("b").first();
									if (bGender != null) {
										genderTxt = bGender.text().trim();
										if (genderTxt.equals("Femme"))
											genderTxt = "F";
										else
											genderTxt = "H";
									}
								}
								// System.out.println("\t\tdivLeftPanelTab[1] =
								// " + divLeftPanelTab[1]);
								Element elementNaissance = Jsoup.parse(divLeftPanelTab[1]);
								int age = 0;
								if (elementNaissance != null) {
									elementNaissance.select("b").remove();
									elementNaissance.select("div.children").remove();
									String elementNaissanceTxt = elementNaissance.text();
									if (elementNaissanceTxt.indexOf(" ") != -1) {
										String anneeNaissance = elementNaissanceTxt
												.substring(elementNaissanceTxt.lastIndexOf(" ") + 1);
										if (isInteger(anneeNaissance)) {
											int anneeN = Integer.parseInt(anneeNaissance);
											int anneeEnCours = Calendar.getInstance().get(Calendar.YEAR);
											age = anneeEnCours - anneeN;
										}
									}
								}
								listUserProfile.add("Gender : " + genderTxt);
								listUserProfile.add("Age : " + age);
							}
						}
						Elements lisPosts = divLeft.select("li");
						// System.out.println("lisPosts = " + lisPosts);
						if (!lisPosts.equals("")) {
							for (Element liPosts : lisPosts) {
								if (liPosts.text().indexOf("messages") != -1) {
									String liPostsTxt = liPosts.text();
									String posts = liPostsTxt.substring(0, liPostsTxt.indexOf("messages")).trim();
									listUserProfile.add("Posts : " + posts);
									break;
								}
							}
						}
					}
				} else if (messageWrapperType == 10) {
					String description = "", experiencesPro = "", formations = "", savoirFaire = "";
					// description (=> signature)
					Element descriptionElement = doc.selectFirst("p.text-description");
					if (descriptionElement != null)
						description = descriptionElement.text();
					// autres
					Elements sidebarsElements = doc.select("div.sidebar");
					if (sidebarsElements != null) {
						for (Element sidebarElement : sidebarsElements) {
							Element sidebarTitleElement = sidebarElement.selectFirst("h2.main-title");
							String sidebarTitle = "";
							if (sidebarTitleElement != null)
								sidebarTitle = sidebarElement.selectFirst("h2.main-title").text();
							Elements listings = sidebarElement.select("div.listing-ver-3");
							for (Element listing : listings) {
								String listingTitle = "", listingContentCompany = "", listingContentLocation = "",
										listingContentDescription = "";
								Element listingTitleElement = listing.selectFirst("div.listing-heading h2");
								if (listingTitleElement != null)
									listingTitle = listingTitleElement.text();
								Element listingContent = listing.selectFirst("div.listing-content");
								if (listingContent != null) {
									Element listingContentCompanyElement = listingContent
											.selectFirst("h2.title-company");
									if (listingContentCompanyElement != null)
										listingContentCompany = listingContentCompanyElement.text();
									Element listingContentLocationElement = listingContent.selectFirst("span.location");
									if (listingContentLocationElement != null)
										listingContentLocation = listingContentLocationElement.text();
									Elements ps = listingContent.children().select("p");
									Elements divs = listingContent.children().select("div");
									for (Element div : divs) {
										if (div.text().indexOf("Secteur d'activité") != -1)
											ps.add(div);
									}
									for (Element p : ps) {
										listingContentDescription += p.text() + "**";
									}
								}
								String content = listingTitle + "**" + listingContentCompany + "**"
										+ listingContentLocation + "**" + listingContentDescription + "****";

								if (sidebarTitle.equals("Expérience professionnelle"))
									experiencesPro += content;
								else if (sidebarTitle.equals("Formation"))
									formations += content;
							}
							if (sidebarTitle.equals("Savoir faire")) {
								Element savoirFaireElement = sidebarElement.selectFirst("blockquote");
								if (savoirFaireElement != null)
									savoirFaire = savoirFaireElement.text();
							}
						}
					}
					listUserProfile.add("Description:" + description);
					listUserProfile.add("Experiences:" + experiencesPro);
					listUserProfile.add("Formations:" + formations);
					listUserProfile.add("SavoirFaire:" + savoirFaire);
				}
			}
		} catch (SocketTimeoutException ste) {
			setException(1);
			setExceptionMsg(ste.getMessage());
			// countExceptionsPagesTopics(numPageTopics);
			// System.out.println("TimedOut exceptionMsg = " +
			// exceptionMsg);
			return null;
		}

		catch (IOException ioe) {
			if (!ioe.getMessage().equals("Not in GZIP format")) {
				setException(2);
				setExceptionMsg(ioe.getMessage());
				return null;
			}
		}
		return listUserProfile;
	}

	private void addMessageForum(String idMessage, String nom, String date, String post, int idUser,
			String urlUserProfile, String roleUser, String positionUser, int activiyUser, double nbreEtoilesUser,
			int readTimesTopic, int nbrePostsUser, String dateRegisteredUser, String emailUser, String websiteUser,
			String genderUser, int ageUser, String locationUser, String signatureUser, int numDansTopic,
			String titreMessage) {
		// System.out.println("idMessage : " + idMessage);
		// System.out.println("\tAuteur : " + nom);
		// System.out.println("\tDate : " + date);
		// System.out.println("\turlProfileUser : " + urlUserProfile);
		// System.out.println("\tidUser : " + idUser);
		// System.out.println("\tpositionUser : " + positionUser);
		// System.out.println("\tactiviyUser : " + activiyUser);
		// System.out.println("\tPost : " + post.length() + " car");
		if (idMessage != null && nom != null && date != null && post != null) {
			// int numPlus = 0;
			MessageModel newMessage = new MessageModel();

			// SUJET + SUJET TRONQUE
			if (titreMessage != null && !titreMessage.isEmpty())
				newMessage.setfSujet(titreMessage);
			newMessage.setSujet(titreTopic);
			CalculSujetTronque cst = new CalculSujetTronque(titreTopic);
			String sujetTronque = cst.getSujetTronque();
			newMessage.setSujetTronque(sujetTronque);
			// NUMERO
			// System.out.println("\t\t\tnumMessageEnCoursListe = "
			// + numMessageEnCoursListe);
			// System.out.println("\t\t\tnumMessage = " + numMessage);
			// numero = String.valueOf(numMessage
			// + numMessageEnCoursListe
			// );
			// System.out.println("\t\t\tnumero = " + numero);

			newMessage.setNumero("");
			// EXPEDITEUR
			newMessage.setExpediteur(nom);
			// Date
			// Gestion des Today et des null ...
			if (date.indexOf("Today") != -1) {
				SimpleDateFormat formater = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a", Locale.US);
				Date today = new Date();
				date = formater.format(today);
			}
			if (dateRegisteredUser == null)
				dateRegisteredUser = date;
			if (dateRegisteredUser.indexOf("Today") != -1) {
				SimpleDateFormat formater = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a", Locale.US);
				Date today = new Date();
				dateRegisteredUser = formater.format(today);
			}
			FormatDate fd = new FormatDate(date);
			Date dDateParseUS = fd.getDateFormatted();
			fd = new FormatDate(dateRegisteredUser);
			Date fStatDateRegistreredLocuteur = fd.getDateFormatted();

			newMessage.setDateUS(dDateParseUS);
			// CORPS MESSAGE
			newMessage.setCorps(post);
			// // Identifiant
			// DateFormat formatId = new SimpleDateFormat("ddMMyyyyHHmm");
			// String dateFormatId = formatId.format(dDateParseUS).toString();
			// identifiant = dateFormatId + nomLocuteur;
			newMessage.setIdentifiant(idMessage);
			newMessage.setIdConversation(idTopic);
			// AUTRES
			newMessage.setfNumDansConversation(numDansTopic);
			newMessage.setFName(nomForum);
			newMessage.setIdLocuteur(idUser);
			newMessage.setfRoleLocuteur(roleUser);
			newMessage.setfStatPositionLocuteur(positionUser);
			newMessage.setfStatActivityLocuteur(activiyUser);
			newMessage.setfStarsLocuteur(nbreEtoilesUser);
			newMessage.setfNbreVuesTopic(readTimesTopic);
			newMessage.setfStatNbrePostsLocuteur(nbrePostsUser);
			newMessage.setfStatDateRegistreredLocuteur(fStatDateRegistreredLocuteur);
			newMessage.setfStatEMailLocuteur(emailUser);
			newMessage.setfStatWebsiteLocuteur(websiteUser);
			newMessage.setfStatAgeLocuteur(ageUser);
			newMessage.setfStatGenderLocuteur(genderUser);
			newMessage.setfStatLocationLocuteur(locationUser);
			newMessage.setfStatSignatureLocuteur(signatureUser);
			newMessage.setMail(emailUser);
			newMessage.setProfilYahoo("∅");
			newMessage.setGroupPostYahoo("∅");
			newMessage.setIdGoogle("∅");
			newMessage.setFichier("forum");
			newMessage.setSetReferences(new HashSet<String>());
			newMessage.setcTransfertEncoding("∅");
			newMessage.setcTypeMimeSubtype("∅");
			newMessage.setcTypeCharset("∅");
			System.out.println("\t\t\t\t\t\t\t\tMessage id : " + idMessage);
			System.out.println("\t\t\t\t\t\t\t\t\tAuteur : " + nom);
			System.out.println("\t\t\t\t\t\t\t\t\tDate : " + date);
			System.out.println("\t\t\t\t\t\t\t\t\turlProfileUser : " + urlUserProfile);
			System.out.println("\t\t\t\t\t\t\t\t\tidUser : " + idUser);
			System.out.println("\t\t\t\t\t\t\t\t\troleUser : " + roleUser);
			System.out.println("\t\t\t\t\t\t\t\t\tpositionUser : " + positionUser);
			System.out.println("\t\t\t\t\t\t\t\t\tactiviyUser : " + activiyUser);
			System.out.println("\t\t\t\t\t\t\t\t\tnbrePosts : " + nbrePostsUser);
			System.out.println("\t\t\t\t\t\t\t\t\tdateRegistered : " + fStatDateRegistreredLocuteur);
			System.out.println("\t\t\t\t\t\t\t\t\temail : " + emailUser);
			System.out.println("\t\t\t\t\t\t\t\t\twebsite : " + websiteUser);
			System.out.println("\t\t\t\t\t\t\t\t\tgender : " + genderUser);
			System.out.println("\t\t\t\t\t\t\t\t\tage : " + ageUser);
			System.out.println("\t\t\t\t\t\t\t\t\tlocation : " + locationUser);
			System.out.println("\t\t\t\t\t\t\t\t\tsignature : " + signatureUser);
			System.out.println("\t\t\t\t\t\t\t\t\tnbreEtoilesUser : " + nbreEtoilesUser);
			System.out.println("\t\t\t\t\t\t\t\t\tPost : " + post.length() + " car");
			System.out.println("\t\t\t\t\t\t\t\t\tNbreVuesTopic : " + readTimesTopic);
			System.out.println("\t\t\t\t\t\t\t\t\tNumDansTopic : " + numDansTopic);
			System.out.println("Ajout du message " + idMessage);
			newMapIdMessages.put(idMessage, newMessage);
			System.out.println("newMapIdMessages passe à " + newMapIdMessages.size());
		} else
			System.out.println("addMessageForum : UN ELEMENT EST NULL");
	}

	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public int getException() {
		return exception;
	}

	public void setException(int exception) {
		this.exception = exception;
	}

	public Map<String, MessageModel> getMapIdMessages() {
		return newMapIdMessages;
	}
}
