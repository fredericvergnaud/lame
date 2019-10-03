package controleurs.operations.liste.ajoutmessages.bal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeUtility;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.stream.EntityState;
import org.apache.james.mime4j.stream.MimeTokenStream;

import controleurs.operations.liste.CalculSujetTronque;
import controleurs.vuesabstraites.ProjetView;
import modeles.MessageModel;

public class ExtractionMessages {

	private Set<String> setFichiers;
	private ResourceBundle bundleOperationsListe;
	private ProjetView activitesView;
	private int newNbreMessages = 0;

	public ExtractionMessages(Set<String> setFichiers, ResourceBundle bundleOperationsListe, ProjetView activitesView) {
		this.setFichiers = setFichiers;
		this.bundleOperationsListe = bundleOperationsListe;
		this.activitesView = activitesView;
	}

	public Map<String, MessageModel> getNewMapIdMessage() {
		int i = 0;

		MessageModel newMessage = null;

		int nbreFichiersATraiter = setFichiers.size();
		System.out.println("ExtractionMessages - getNewMapIdMessages : nbreFichiersATraiter = " + nbreFichiersATraiter);
		activitesView.setStepProgress(nbreFichiersATraiter);
		activitesView.resetProgress();
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_AjoutMessages"));
		Map<String, MessageModel> mapIdMessages = new TreeMap<String, MessageModel>();

		for (String nomFichierTraite : setFichiers) {
			activitesView.updateProgress();
			File fichierTraite = new File(nomFichierTraite);
			System.out.println(i + " - fichier traité : " + nomFichierTraite);
			String identifiant = "", numero = "", nomLocuteur = bundleOperationsListe.getString("txt_Indefini"), mailLocuteur = bundleOperationsListe.getString("txt_Indefini"), profilYahoo = bundleOperationsListe
					.getString("txt_Indefini"), groupPostYahoo = bundleOperationsListe.getString("txt_Indefini"), idGoogle = bundleOperationsListe.getString("txt_Indefini"), fichier = fichierTraite
					.toString(), sujet = "", sujetTronque = "", inReplyTo = "", corpsMessage = "", cTypeMimeSubType = "", cTypeCharset = "", cTransfertEncoding = "";
			Date dDateParseUS = null;
			Set<String> setReferences = new HashSet<String>();

			if (fichierTraite.exists()) {
				if (fichierTraite.length() > 0) {
					try {
						InputStream source = new FileInputStream(fichierTraite);
						// MimeMultipart multipart = null;
						// ArrayList<Part> parts = new ArrayList<Part>();
						try {
							try {
								// MimeConfig config = new MimeConfig();
								// config.setMaxLineLen(-1);
								// config.setMaxContentLen(-1);
								// if (mbx == true)
								// config.setMaxHeaderLen(-1);
								// config.setMalformedHeaderStartsBody(true);
								// MimeTokenStream stream = new MimeTokenStream(
								// config);
								MimeTokenStream stream = new MimeTokenStream();
								stream.parse(source);
								for (EntityState state = stream.getState(); state != EntityState.T_END_OF_STREAM; state = stream.next()) {
									switch (state) {
									case T_FIELD:
										// System.out
										// .println("Header field detected: "
										// + stream.getField()
										// .getName());
										String nomHeader = stream.getField().getName();
										String bodyHeader = stream.getField().getBody();
										Pattern pattern = Pattern.compile("\\<(.*?)\\>");
										Matcher m;
										// FROM ET MAIL
										if (("From").equalsIgnoreCase(nomHeader)) {
											String sFrom = bodyHeader;
											if (sFrom.indexOf("<") != -1) {
												m = pattern.matcher(sFrom);
												while (m.find()) {
													mailLocuteur = m.group(1);
												}
												nomLocuteur = sFrom.substring(0, sFrom.indexOf("<"));
												nomLocuteur = nomLocuteur.replace("\"", "");
												nomLocuteur = MimeUtility.decodeText(nomLocuteur);
												nomLocuteur = nomLocuteur.replace("'", "").trim();
											} else {
												nomLocuteur = sFrom;
												nomLocuteur = nomLocuteur.replace("\"", "");
												nomLocuteur = MimeUtility.decodeText(nomLocuteur);
												nomLocuteur = nomLocuteur.replace("'", "").trim();
												mailLocuteur = nomLocuteur;
											}
										}

										// MESSAGE-ID
										else if (("Message-Id").equalsIgnoreCase(nomHeader)) {
											identifiant = bodyHeader;
											m = pattern.matcher(identifiant);
											while (m.find()) {
												identifiant = m.group(1);
											}
										}

										// CONTENT-TYPE
										else if (("Content-Type").equalsIgnoreCase(nomHeader)) {
											String ct = bodyHeader;
											System.out.println("Content-Type : " + ct);
											if (ct.indexOf(";") != -1) {
												String[] tabCt = ct.split(";");
												cTypeMimeSubType = tabCt[0].trim();
												if (tabCt.length > 1) {
													cTypeCharset = tabCt[1];
													if (cTypeCharset.indexOf("=") != -1) {
														String charset = cTypeCharset.substring(0, cTypeCharset.indexOf("="));
														// System.out
														// .println("charset : "+charset);
														if (("charset").equals(charset)) {
															String cTypeCharset2 = cTypeCharset.substring(cTypeCharset.indexOf("=") + 1);
															cTypeCharset = cTypeCharset2;
														} else
															cTypeCharset = "";
													}
													cTypeCharset = cTypeCharset.replace("\"", "").trim();
												}
											} else
												cTypeMimeSubType = ct.trim();
										}

										// CONTENT-TRANSFERT-ENCODING
										else if (("Content-Transfer-Encoding").equalsIgnoreCase(nomHeader)) {
											cTransfertEncoding = bodyHeader;
										}

										// PROFIL YAHOO
										else if (("X-Yahoo-Profile").equalsIgnoreCase(nomHeader)) {
											profilYahoo = bodyHeader.trim();
										}

										// GROUP POST YAHOO
										else if (("X-Yahoo-Group-Post").equalsIgnoreCase(nomHeader)) {
											String sGp = bodyHeader;
											String[] tab = sGp.split(";");
											for (String s : tab)
												if (s.indexOf("u=") != -1)
													groupPostYahoo = s.substring(3).trim();
										}

										// ID GOOGLE
										else if (("X-Google-Group-Id").equalsIgnoreCase(nomHeader)) {
											idGoogle = bodyHeader;
											idGoogle = idGoogle.trim();
										}

										// DATE
										else if (("Date").equalsIgnoreCase(nomHeader)) {
											String sDate = bodyHeader.trim();
											DateFormat formatUS = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
											dDateParseUS = formatUS.parse(sDate);
										}

										// SUJET
										else if (("Subject").equalsIgnoreCase(nomHeader)) {
											sujet = bodyHeader;
											sujet = MimeUtility.decodeText(sujet);
											// NETTOYAGE

											sujet = sujet.replaceAll("\\bRe\\b", "");
											sujet = sujet.replaceAll("\\bRE\\b", "");

											sujet = sujet.replaceAll("\\bRef\\b", "");
											sujet = sujet.replaceAll("\\bRéf\\b", "");

											sujet = sujet.replaceAll("\\bfwd\\b", "");
											sujet = sujet.replaceAll("\\bfw\\b", "");
											sujet = sujet.replaceAll("\\bFwd\\b", "");
											sujet = sujet.replaceAll("\\bFw\\b", "");
											sujet = sujet.replaceAll("\\bFWD\\b", "");
											sujet = sujet.replaceAll("\\bFW\\b", "");

											sujet = sujet.replaceAll("\\btr\\b", "");
											sujet = sujet.replaceAll("\\bTr\\b", "");
											sujet = sujet.replaceAll("\\bTR\\b", "");
											sujet = sujet.replaceAll("\\bTrans\\b", "");

											// TABULATIONS
											sujet = sujet.replaceAll("\t", " ");

											// ESPACE INSECABLE
											sujet = sujet.replace("\u00A0", "");

											// CERTAINS CARACTERES
											sujet = sujet.replaceAll("\\p{Cntrl}", "?");

											// CERTAINES PONCTUATIONS
											sujet = sujet.replaceAll("[:._\"»«]", "");

											// ESPACES CONCECUTIFS
											sujet = sujet.replaceAll("( ){2,}", " ");

											sujet = sujet.trim();
										}

										// IN-REPLY-TO
										else if (("In-Reply-To").equalsIgnoreCase(nomHeader)) {
											String sIrt = bodyHeader;
											m = pattern.matcher(sIrt);
											while (m.find()) {
												inReplyTo = m.group(1);
											}
										}

										// REFERENCES
										else if (("References").equalsIgnoreCase(nomHeader)) {
											String references = bodyHeader;
											m = pattern.matcher(references);
											while (m.find()) {
												setReferences.add(m.group(1));
											}
										}

										break;
									default:
										break;
									} // END SWITCH
								} // END FOR ENTITY
							} catch (MimeException e) {
								e.printStackTrace();
							} catch (ParseException e) {
								System.out.println("ExtractionMessage/ParseException : erreur de parsing de la date de message : " + e);
							}
						} finally {
							source.close();
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}// END IF path.lenght > 0
				else
					System.out.println("FICHIER VIDE");
			} // END IF fichier exists
			else
				System.out.println("FICHIER N'EXISTE PAS");
			// if (nomLocuteur.equals(""))
			// System.out.println("MESSAGE CHELOU - nom vide : "
			// + fichier);
			// if (mailLocuteur.equals(""))
			// System.out.println("MESSAGE CHELOU - mail vide : "
			// + fichier);
			// if (profil.equals(""))
			// System.out.println("MESSAGE CHELOU - profil vide : "
			// + fichier);
			// if (groupPost.equals(""))
			// System.out.println("MESSAGE CHELOU - gp vide : "
			// + fichier);
			// if (fichier.equals(""))
			// System.out.println("MESSAGE CHELOU - fichier vide : "
			// + fichier);
			// if (dDateParseUS == null)
			// System.out.println("MESSAGE CHELOU - date null : "
			// + fichier);
			// if (sujet.equals(""))
			// System.out.println("MESSAGE CHELOU - sujet vide : "
			// + fichier);
			// if (sujetTronque2.equals(""))
			// System.out
			// .println("MESSAGE CHELOU - sujetTronque vide : "
			// + fichier);
			// if (inReplyTo.equals(""))
			//
			// System.out.println("MESSAGE CHELOU - inreplyto vide : "
			// + fichier);

			// NUMERO
			String n1 = nomFichierTraite.substring(nomFichierTraite.lastIndexOf("/") + 1);
			numero = n1.substring(0, n1.indexOf("."));

			// System.out.println("1");

			// // SUJET TRONQUE
			CalculSujetTronque cst = new CalculSujetTronque(sujet);
			sujetTronque = cst.getSujetTronque();

			// System.out.println("2");

			// MANIP SUR NOM LOCUTEUR ET MAIL LOCUTEUR SI
			// VIDE
			if (nomLocuteur.equals("")) {
				if (mailLocuteur.equals("")) {
					if (profilYahoo.equals("")) {
						nomLocuteur = groupPostYahoo;
					} else {
						nomLocuteur = profilYahoo;
					}
				} else {
					nomLocuteur = mailLocuteur;
				}
			}
			System.out.println("Extraction : " + nomLocuteur + " | " + mailLocuteur + " | " + identifiant + " | " + profilYahoo + " | " + groupPostYahoo + " | " + idGoogle + " | " + dDateParseUS
					+ " | " + sujet + " | " + sujetTronque + " | " + inReplyTo + " | " + setReferences.size() + " références | cTransfertEncoding = " + cTransfertEncoding + " | cTypeMimeSubType = "
					+ cTypeMimeSubType + " | cTypeCharset = " + cTypeCharset);

			if (!identifiant.equals(null) && !identifiant.equals("") && dDateParseUS != null) {
				newMessage = new MessageModel();
				newMessage.setIdentifiant(identifiant);
				newMessage.setNumero(numero);
				newMessage.setExpediteur(nomLocuteur);
				newMessage.setMail(mailLocuteur);
				newMessage.setProfilYahoo(profilYahoo);
				newMessage.setGroupPostYahoo(groupPostYahoo);
				newMessage.setIdGoogle(idGoogle);
				newMessage.setFichier(fichier);
				newMessage.setDateUS(dDateParseUS);
				newMessage.setSujet(sujet);
				newMessage.setSujetTronque(sujetTronque);
				newMessage.setInReplyTo(inReplyTo);
				newMessage.setInReplyToRegroupe("");
				newMessage.setSetReferences(setReferences);
				newMessage.setCorps(corpsMessage);
				newMessage.setcTransfertEncoding(cTransfertEncoding);
				newMessage.setcTypeMimeSubtype(cTypeMimeSubType);
				newMessage.setcTypeCharset(cTypeCharset);
				// Forum
//				newMessage.setfNumDansConversation(0);
//				newMessage.setFName("");
//				newMessage.setIdLocuteur(Integer.parseInt(groupPostYahoo));
//				newMessage.setfRoleLocuteur("");
//				newMessage.setfStatPositionLocuteur("");
//				newMessage.setfStatActivityLocuteur(0);
//				newMessage.setfStarsLocuteur(0);
//				newMessage.setfNbreVuesTopic(0);
//				newMessage.setfStatNbrePostsLocuteur(0);
//				newMessage.setfStatDateRegistreredLocuteur(null);
//				newMessage.setfStatEMailLocuteur("");
//				newMessage.setfStatWebsiteLocuteur("");
//				newMessage.setfStatGenderLocuteur("");
//				newMessage.setfStatAgeLocuteur(0);
//				newMessage.setfStatLocationLocuteur("");
//				newMessage.setfStatSignatureLocuteur("");
				// newMessage.setMbx(mbx);
				mapIdMessages.put(identifiant, newMessage);
				newNbreMessages++;
				i++;
				System.out.println("mapIdMessages passe à " + mapIdMessages.size());
				System.out.println("newNbreMessages passe à " + newNbreMessages);
			} else
				System.out.println("FICHIER A IDENTIFIANT NULL OU VIDE OU A DATE NULL");

		} // END FOR
		return mapIdMessages;
	}

	public int getNewNbreMessages() {
		return newNbreMessages;
	}

}
