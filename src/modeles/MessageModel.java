package modeles;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.stream.EntityState;
import org.apache.james.mime4j.stream.MimeTokenStream;
import org.jsoup.Jsoup;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import controleurs.operations.liste.ajoutmessages.forum.FormatDate;

public class MessageModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int idConversation, idLocuteur;
	private String numero, fichier, expediteur, mail, profilYahoo, groupPostYahoo, idGoogle, sujet, corps, identifiant,
			inReplyTo, inReplyToRegroupe, sujetTronque, cTransfertEncoding, cTypeMimeSubtype, cTypeCharset;
	private Date dateUS;
	private Set<String> setReferences;
	private boolean mbx = false;
	private LocuteurModel locuteur;
	// Attributs forum
	private String fSujet;
	private String fName;
	private int fNumDansConversation;
	private int fNbreVuesTopic;
	private String fRoleLocuteur;
	private double fStarsLocuteur;
	// Stats
	private int fStatNbrePostsLocuteur;
	private int fStatActivityLocuteur;
	private String fStatPositionLocuteur;
	private Date fStatDateRegistreredLocuteur;
	private String fStatEMailLocuteur;
	private String fStatWebsiteLocuteur;
	private String fStatGenderLocuteur;
	private int fStatAgeLocuteur;
	private String fStatLocationLocuteur;
	private String fStatSignatureLocuteur;
	// Extractify
	private Object deeperLevel;

	public MessageModel() {
	}

	public int getfStatActivityLocuteur() {
		return fStatActivityLocuteur;
	}

	public LocuteurModel getLocuteur() {
		return locuteur;
	}

	public void setLocuteur(LocuteurModel locuteur) {
		this.locuteur = locuteur;
	}

	public int getIdLocuteur() {
		return idLocuteur;
	}

	public void setIdLocuteur(int idLocuteur) {
		this.idLocuteur = idLocuteur;
	}

	public void setfStatActivityLocuteur(int fStatActivityLocuteur) {
		this.fStatActivityLocuteur = fStatActivityLocuteur;
	}

	public String getfStatPositionLocuteur() {
		return fStatPositionLocuteur;
	}

	public void setfStatPositionLocuteur(String fStatPositionLocuteur) {
		this.fStatPositionLocuteur = fStatPositionLocuteur;
	}

	public String getfStatEMailLocuteur() {
		return fStatEMailLocuteur;
	}

	public void setfStatEMailLocuteur(String fStatEMailLocuteur) {
		this.fStatEMailLocuteur = fStatEMailLocuteur;
	}

	public String getfStatWebsiteLocuteur() {
		return fStatWebsiteLocuteur;
	}

	public void setfStatWebsiteLocuteur(String fStatWebsiteLocuteur) {
		this.fStatWebsiteLocuteur = fStatWebsiteLocuteur;
	}

	public String getfStatGenderLocuteur() {
		return fStatGenderLocuteur;
	}

	public void setfStatGenderLocuteur(String fStatGenderLocuteur) {
		this.fStatGenderLocuteur = fStatGenderLocuteur;
	}

	public int getfStatAgeLocuteur() {
		return fStatAgeLocuteur;
	}

	public void setfStatAgeLocuteur(int ageUser) {
		this.fStatAgeLocuteur = ageUser;
	}

	public String getfStatLocationLocuteur() {
		return fStatLocationLocuteur;
	}

	public void setfStatLocationLocuteur(String fStatLocationLocuteur) {
		this.fStatLocationLocuteur = fStatLocationLocuteur;
	}

	public String getfStatSignatureLocuteur() {
		return fStatSignatureLocuteur;
	}

	public void setfStatSignatureLocuteur(String fStatSignatureLocuteur) {
		this.fStatSignatureLocuteur = fStatSignatureLocuteur;
	}

	public int getfNumDansConversation() {
		return fNumDansConversation;
	}

	public void setfNumDansConversation(int fNumDansConversation) {
		this.fNumDansConversation = fNumDansConversation;
	}

	public int getfNbreVuesTopic() {
		return fNbreVuesTopic;
	}

	public void setfNbreVuesTopic(int fNbreVuesTopic) {
		this.fNbreVuesTopic = fNbreVuesTopic;
	}

	public String getfRoleLocuteur() {
		return fRoleLocuteur;
	}

	public void setfRoleLocuteur(String fRoleLocuteur) {
		this.fRoleLocuteur = fRoleLocuteur;
	}

	public double getfStarsLocuteur() {
		return fStarsLocuteur;
	}

	public void setfStarsLocuteur(double fStarsLocuteur) {
		this.fStarsLocuteur = fStarsLocuteur;
	}

	public int getfStatNbrePostsLocuteur() {
		return fStatNbrePostsLocuteur;
	}

	public void setfStatNbrePostsLocuteur(int fStatNbrePostsLocuteur) {
		this.fStatNbrePostsLocuteur = fStatNbrePostsLocuteur;
	}

	public Date getfStatDateRegistreredLocuteur() {
		return fStatDateRegistreredLocuteur;
	}

	public void setfStatDateRegistreredLocuteur(Date fStatDateRegistreredLocuteur) {
		this.fStatDateRegistreredLocuteur = fStatDateRegistreredLocuteur;
	}

	public void setfStatDateRegistreredLocuteur(String sFStatDateRegistreredLocuteur) {
		FormatDate fd = new FormatDate(sFStatDateRegistreredLocuteur);
		Date dDateParseUS = fd.getDateFormatted();
		this.fStatDateRegistreredLocuteur = dDateParseUS;
	}

	public String getFName() {
		return fName;
	}

	public void setFName(String fName) {
		this.fName = fName;
	}

	public boolean isMbx() {
		return mbx;
	}

	public void setMbx(boolean mbx) {
		this.mbx = mbx;
	}

	public String getcTransfertEncoding() {
		return cTransfertEncoding;
	}

	public void setcTransfertEncoding(String cTransfertEncoding) {
		this.cTransfertEncoding = cTransfertEncoding;
	}

	public String getcTypeMimeSubtype() {
		return cTypeMimeSubtype;
	}

	public void setcTypeMimeSubtype(String cTypeMimeSubtype) {
		this.cTypeMimeSubtype = cTypeMimeSubtype;
	}

	public String getcTypeCharset() {
		return cTypeCharset;
	}

	public void setcTypeCharset(String cTypeCharset) {
		this.cTypeCharset = cTypeCharset;
	}

	public String getSujet() {
		return sujet;
	}

	public void setSujet(String sujet) {
		this.sujet = sujet;
	}

	public Date getDateUS() {
		return dateUS;
	}

	public void setDateUS(Date dateUS) {
		this.dateUS = dateUS;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getInReplyTo() {
		return inReplyTo;
	}

	public void setInReplyTo(String inReplyTo) {
		this.inReplyTo = inReplyTo;
	}

	public String getInReplyToRegroupe() {
		return inReplyToRegroupe;
	}

	public void setInReplyToRegroupe(String inReplyToRegroupe) {
		this.inReplyToRegroupe = inReplyToRegroupe;
	}

	public String getFichier() {
		return fichier;
	}

	public void setFichier(String fichier) {
		this.fichier = fichier;
	}

	public String getExpediteur() {
		return expediteur;
	}

	public void setExpediteur(String expediteur) {
		this.expediteur = expediteur;
	}

	public String getText() {
		return corps;
	}

	public Object getDeeperLevel() {
		return deeperLevel;
	}

	public void setDeeperLevel(Object deeperLevel) {
		this.deeperLevel = deeperLevel;
	}

	public String getCorps() {
		System.out.println("MessageModel - getCorps() : getFichier() = " + getFichier());
		if (!getFichier().equals("forum")) {
			// System.out.println("Message : content-transfert-encoding = "
			// + cTransfertEncoding + " | cTypeMimeSubtype = "
			// + cTypeMimeSubtype + " | cTypeCharset = " + cTypeCharset);
			if (cTypeMimeSubtype.indexOf("multipart/") == -1) {
				try {
					InputStream source = new FileInputStream(fichier);
					try {
						try {
							MimeTokenStream stream = new MimeTokenStream();
							stream.parse(source);
							for (EntityState state = stream.getState(); state != EntityState.T_END_OF_STREAM; state = stream.next()) {
								switch (state) {
								case T_BODY:
									String mimeType = stream.getBodyDescriptor().getMimeType();
									// System.out.println("BODY : mimetype = "
									// + mimeType + " | charset = "
									// + charset + " | bodydescriptor = "
									// + stream.getBodyDescriptor());
									if (mimeType.equalsIgnoreCase("text/plain")) {
										StringWriter writer = null;
										try {
											writer = new StringWriter();
											IOUtils.copy(MimeUtility.decode(stream.getDecodedInputStream(), "quoted-printable"), writer, "ISO-8859-1");
											corps += writer.toString() + "\n";
										} catch (NullPointerException e) {
											e.printStackTrace();
										} catch (IOException e) {
											e.printStackTrace();
										} catch (UnsupportedCharsetException e) {
											e.printStackTrace();
										} catch (MessagingException e) {
											e.printStackTrace();
										}
									}
									break;
								default:
									break;
								}
							}
						} catch (MimeException e) {
							e.printStackTrace();
						}
					} finally {
						source.close();
					}
				} catch (FileNotFoundException e1) {
					corps = "Fichier non trouvé  / File not found : " + getFichier();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				MimeMultipart multipart = null;
				try {
					InputStream source = new FileInputStream(fichier);
					try {
						try {
							MimeTokenStream stream = new MimeTokenStream();
							stream.parse(source);
							for (EntityState state = stream.getState(); state != EntityState.T_END_OF_STREAM; state = stream.next()) {
								switch (state) {
								case T_BODY:
									DataSource ds = (new FileDataSource(fichier));
									multipart = new MimeMultipart(ds);
									break;
								default:
									break;
								}
							}
						} catch (MimeException e) {
							e.printStackTrace();
						} catch (MessagingException e1) {
							e1.printStackTrace();
						}
					} finally {
						source.close();
					}
				} catch (FileNotFoundException e1) {
					corps = "Fichier non trouvé / File not found : " + getFichier();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// TRAITEMENT DES PARTS
				if (multipart != null) {
					try {
						// System.out.println("Nombre de Parts = "
						// + multipart.getCount());
						for (int j = 0; j < multipart.getCount(); j++) {
							Part partOfMP = multipart.getBodyPart(j);
							String type = partOfMP.getContentType();
							// System.out.println("Content-type = " + type);
							// if (type.indexOf("text/plain") != -1) {
							corps += getTextFromMimeMessage(partOfMP);

						}
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						InputStream source = new FileInputStream(fichier);
						try {
							try {
								MimeTokenStream stream = new MimeTokenStream();
								stream.parse(source);
								for (EntityState state = stream.getState(); state != EntityState.T_END_OF_STREAM; state = stream.next()) {
									switch (state) {
									case T_START_MULTIPART:
										Scanner s = new Scanner(stream.getInputStream()).useDelimiter("\\A");
										corps = s.hasNext() ? s.next() : "";
										break;
									default:
										break;
									}
								}
							} catch (MimeException e) {
								e.printStackTrace();
							}
						} finally {
							source.close();
						}
					} catch (FileNotFoundException e1) {
						corps = "Fichier non trouvé / File not found";
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			// System.out.println("longueur de corps = " + corps.length());
			corps = getTextSsHTML(corps);
		}
		// Conversion en UTF-8
		System.out.println("corps size = "+corps.length());
		String charset = null;
		try {
			InputStream in = new ByteArrayInputStream(corps.getBytes());
			CharsetDetector cd = new CharsetDetector();
			cd.setText(in);
			CharsetMatch cm = cd.detect();
			charset = cm.getName();

			// CharsetMatch matches[];
			// matches = cd.detectAll();
			// for (int m = 0; m < matches.length; m += 1) {
			// System.out.println("\ncm.matche=" + m + " name="
			// + matches[m].getName() + " confidence="
			// + matches[m].getConfidence());
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("charset icu = " + charset);
		if (charset.indexOf("_") != -1)
			charset = charset.substring(0, charset.indexOf("_"));
		byte[] tab = null;
		try {
			tab = corps.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			corps = new String(tab, charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return corps;
	}

	public String getTextFromMimeMessage(Part p) {
		System.setProperty("mail.mime.base64.ignoreerrors", "true");
		try {
			if (p.isMimeType("text/*")) {
				String s = (String) p.getContent();
				return s;
			}

			if (p.isMimeType("multipart/alternative")) {
				// prefer html text over plain text
				Multipart mp = (Multipart) p.getContent();
				String text = null;
				for (int i = 0; i < mp.getCount(); i++) {
					Part bp = mp.getBodyPart(i);
					if (bp.isMimeType("text/plain")) {
						if (text == null)
							text = getTextFromMimeMessage(bp);
						continue;
					} else if (bp.isMimeType("text/html")) {
						String s = getTextFromMimeMessage(bp);
						if (s != null)
							return s;
					} else {
						return getTextFromMimeMessage(bp);
					}
				}
				return text;
			} else if (p.isMimeType("multipart/*")) {
				Multipart mp = (Multipart) p.getContent();
				for (int i = 0; i < mp.getCount(); i++) {
					String s = getTextFromMimeMessage(mp.getBodyPart(i));
					if (s != null)
						return s;
				}
			}
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}

	// private String getCorpsSsHexa(String corpsMessage) {
	// Pattern pattern = Pattern.compile("(=[A-Fa-f0-9]{2}|(=(\r\n)))");
	// Matcher matcher = pattern.matcher(corpsMessage);
	// HashMap<String, String> replacements = new HashMap<String, String>();
	// replacements.put("=\r\n", "");
	// replacements.put("=E0", "�");
	// replacements.put("=E2", "�");
	// replacements.put("=E4", "�");
	// replacements.put("=E6", "�");
	// replacements.put("=E7", "�");
	// replacements.put("=E8", "�");
	// replacements.put("=E9", "�");
	// replacements.put("=EA", "�");
	// replacements.put("=EB", "�");
	// replacements.put("=EE", "�");
	// replacements.put("=EF", "�");
	// replacements.put("=F4", "�");
	// replacements.put("=F6", "�");
	// replacements.put("=F9", "�");
	// replacements.put("=FB", "�");
	//
	// StringBuilder builder = new StringBuilder();
	// int i = 0;
	// while (matcher.find()) {
	// // System.out.println(matcher.group(1));
	// String replacement = replacements.get(matcher.group(1));
	// builder.append(corpsMessage.substring(i, matcher.start()));
	// if (replacement == null)
	// builder.append(matcher.group(0));
	// else
	// builder.append(replacement);
	// i = matcher.end();
	// }
	// builder.append(corpsMessage.substring(i, corpsMessage.length()));
	// String newCorpsMessage = builder.toString();
	// return newCorpsMessage;
	// }

	public void setCorps(String corps) {
		this.corps = corps;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getProfilYahoo() {
		return profilYahoo;
	}

	public void setProfilYahoo(String profilYahoo) {
		this.profilYahoo = profilYahoo;
	}

	public String getGroupPostYahoo() {
		return groupPostYahoo;
	}

	public void setGroupPostYahoo(String groupPostYahoo) {
		this.groupPostYahoo = groupPostYahoo;
	}

	public String getIdGoogle() {
		return idGoogle;
	}

	public void setIdGoogle(String idGoogle) {
		this.idGoogle = idGoogle;
	}

	public String getSujetTronque() {
		return sujetTronque;
	}

	public void setSujetTronque(String sujetTronque) {
		this.sujetTronque = sujetTronque;
	}

	public Set<String> getSetReferences() {
		return setReferences;
	}

	public void setSetReferences(Set<String> setReferences) {
		this.setReferences = setReferences;
	}

	public int getIdConversation() {
		return idConversation;
	}

	public void setIdConversation(int idConversation) {
		this.idConversation = idConversation;
	}

	public String getfSujet() {
		return fSujet;
	}

	public void setfSujet(String fSujet) {
		this.fSujet = fSujet;
	}

	public String getSsOriginalMessage(String newCorpsMessage) {
		String newCorpsSsOriginalMessage = "";
		List<String> listLines = new ArrayList<String>(getCorpsToTab(newCorpsMessage).keySet());
		int i = 1;
		for (String line : listLines) {
			line = line.replace("\n", "");
			// System.out.println("line = " + line);
			String newLine = "";
			if (line.startsWith("|") || line.startsWith("From:") || line.startsWith("From :") || line.startsWith("De :")
					|| line.startsWith("Date:") || line.startsWith("Date :") || line.startsWith("To:")
					|| line.startsWith("To :") || line.startsWith("à:") || line.startsWith("à :")
					|| line.startsWith("Subject:") || line.startsWith("Subject :") || line.startsWith("Objet:")
					|| line.startsWith("Objet :") || line.startsWith("Répondre à :") || line.indexOf("wrote") != -1
					|| line.indexOf("a écrit") != -1 || line.indexOf("@", 1) != -1) {
				newLine = "*** " + i + " ***";
				i++;
			} else
				newLine = line;

			// System.out.println("newLine = " + newLine);
			newCorpsSsOriginalMessage += newLine + "\n";
		}
		// System.out.println("MessageModel - getSsOriginalMessage :
		// newCorpsSsOriginalMessage = \n"
		// + newCorpsSsOriginalMessage);
		newCorpsSsOriginalMessage = newCorpsSsOriginalMessage.trim();
		if (newCorpsSsOriginalMessage.indexOf("---") != -1) {
			String[] tab = newCorpsSsOriginalMessage.split("---");
			newCorpsSsOriginalMessage = tab[0];
		}
		cleanString(newCorpsSsOriginalMessage);
		return newCorpsSsOriginalMessage;
	}

	public void cleanString(String corpsMessage) {
		// TABULATIONS
		corpsMessage = corpsMessage.replaceAll("\t", " ");
		// ESPACES CONCECUTIFS
		corpsMessage = corpsMessage.replaceAll("( ){2,}", "\n");
	}

	public Map<String, String> getCorpsToTab(String s) {
		Map<String, String> mapStringStyles = new LinkedHashMap<String, String>();
		String regular = "regular";
		String blue = "blue";
		String green = "green";
		String red = "red";
		Scanner scanner = new Scanner(s);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			line = line.trim();
			if (line.startsWith(">")) {
				line = line.replaceFirst(">", "|");
				if (line.indexOf(">") == 1)
					line = line.replaceFirst(">", "|");
			}
			line = line.trim();
			line += "\n";

			if (!mapStringStyles.containsKey(line))
				mapStringStyles.put(line, new String());

			if (line.startsWith("|")) {
				if (line.indexOf("|", 1) == 1 || line.indexOf("|", 1) == 2)
					mapStringStyles.put(line, green);
				else
					mapStringStyles.put(line, blue);
			} else if (line.startsWith("--"))
				mapStringStyles.put(line, red);
			else
				mapStringStyles.put(line, regular);

		}
		scanner.close();
		return mapStringStyles;
	}

	public String getTextSsHTML(String html) {
		String corpsSsHTML = null;
		if (html.indexOf("<br>") != -1 || html.indexOf("<div>") != -1 || html.indexOf("<BR>") != -1
				|| html.indexOf("<DIV>") != -1) {
			html = html.replaceAll("(?i)<br[^>]*>", "br2n");
			Jsoup.parse(html);
			corpsSsHTML = html.replaceAll("br2n", "\n");
		} else
			corpsSsHTML = html;
		corpsSsHTML = corpsSsHTML.replaceAll("\\<.*?\\>", "");
		return corpsSsHTML;
	}

	// public void affiche() {
	// System.out.println("MESSAGE NUMERO " + numero + " : identifiant = " +
	// identifiant + " | inReplyTo = " + inReplyTo);
	// }

	// Part mbp = null;
	// System.out.println("MULTIPART : ");
	// //
	// System.out.println(stream.getInputStream());
	// try {
	// mbp = new MimeBodyPart(
	// stream.getInputStream());
	// // multipart
	// // .addBodyPart((javax.mail.BodyPart) mbp);
	// // multipart = (Multipart)
	// // mbp.getContent();
	// //
	// // System.out.println("Part ContentType = "
	// // + mbp.getContentType());
	// parts.add(mbp);
	// // if (mbp.isMimeType("text/plain"))
	// // corpsMessage =
	// // mbp.getContent().toString();
	// } catch (MessagingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }

	// break;

	// System.out.println("MULTIPART : "
	// + stream.getBodyDescriptor());
	// String typeMime = stream
	// .getBodyDescriptor()
	// .getMimeType();
	//
	// String enc =
	// stream.getBodyDescriptor()
	// .getTransferEncoding();

	// String[] parts = stream
	// .getInputStream()
	// .toString()
	// .split(stream
	// .getBodyDescriptor()
	// .getBoundary());
	// System.out.println("taille de parts : "
	// + parts.length);

	// InputStream decodedStream;
	// if (MimeUtil.ENC_BASE64.equals(enc))
	// {
	// decodedStream = new
	// Base64InputStream(
	// stream.getInputStream());
	// } else if
	// (MimeUtil.ENC_QUOTED_PRINTABLE
	// .equals(enc)) {
	// decodedStream = new
	// QuotedPrintableInputStream(
	// stream.getInputStream());
	// } else {
	// decodedStream = stream
	// .getInputStream();
	// }
	//
	// mbp = null;
	// for (String s : parts) {
	// if (s.indexOf("Content-Type: text/")
	// != -1) {
	// try {
	// mbp = new MimeBodyPart(
	// decodedStream);
	// // corpsMessage +=
	// // getTextFromMimeMessage(mbp)+
	// // "\n";
	// } catch (MessagingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }

	// }
	// }
	// corpsMessage =
	// getTextFromMimeMessage(mbp);

	// System.out.println("typeMime = "+typeMime+" | ENCODAGE = "
	// + enc);
	// Scanner s = new
	// Scanner(stream.getInputStream()).useDelimiter("\\A");
	// corpsMessage = s.hasNext() ? s.next()
	// : "";
	// InputStream decodedStream = null;
	// try {
	// decodedStream = MimeUtility.decode(
	// stream.getInputStream(),
	// enc);
	// } catch (MessagingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// if (MimeUtil.ENC_BASE64.equals(enc))
	// {
	// decodedStream = new
	// Base64InputStream(
	// stream.getDecodedInputStream());
	// } else if
	// (MimeUtil.ENC_QUOTED_PRINTABLE
	// .equals(enc)) {
	// decodedStream = new
	// QuotedPrintableInputStream(
	// stream.getDecodedInputStream());
	// } else {
	// decodedStream = stream
	// .getDecodedInputStream();
	// }
	// if (!decodedStream.equals(null)) {

	// String[] parts = decodedStream
	// .toString()
	// .split(stream
	// .getBodyDescriptor()
	// .getBoundary());
	// System.out
	// .println("taille de parts : "
	// + parts.length);
	// for (String s : parts) {
	// if (s.indexOf("Content-Type: text/")
	// != -1) {
	// System.out
	// .println("Content-Type: text/");
	// corpsMessage += s + "\n";
	// }
	// }
	// }

	//
	// System.out.println("MULTIPART : "
	// + decodedStream);

	// if (typeMime.indexOf("text/") != -1)
	// {
	// try {
	// corpsMessage =
	// getTextFromMimeMessage(new
	// MimeMessage(
	// null,
	// stream.getDecodedInputStream()));
	// } catch (MessagingException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// StringBuffer sb = new
	// StringBuffer();
	// BufferedReader br = new
	// BufferedReader(
	// new InputStreamReader(
	// stream.getInputStream()));
	// String line = null;
	// while ((line = br.readLine()) !=
	// null) {
	// sb.append(new String(
	// DecoderUtil
	// .decodeEncodedWords(line,
	// DecodeMonitor.STRICT))+"\n");
	// }
	// corpsMessage = sb.toString();
	// } else {
	// }

	// System.out
	// .println("Body detected, contents = "
	// + stream.getInputStream()
	// + ", header data = "
	// + stream.getBodyDescriptor());
	// Scanner s = new
	// Scanner(stream.getInputStream()).useDelimiter("\\A");
	// corpsMessage = s.hasNext() ? s.next()
	// : "";
	// corpsMessage =
	// MimeUtility.decodeText(corpsMessage);
	// StringWriter writer = new
	// StringWriter();
	// IOUtils.copy(stream.getInputStream(),
	// writer);
	// corpsMessage = writer.toString();
	// corpsMessage =
	// MimeUtility.decodeText(corpsMessage);
	// StringBuilder sb = new
	// StringBuilder();
	// try {
	// Reader r = stream.getReader();
	// int c;
	// while ((c = r.read()) != -1) {
	// sb.append((char) c);
	// }
	// } catch (IOException ex) {
	// ex.printStackTrace();
	// }
	// corpsMessage = sb.toString();
	// Corps Message
	// try {
	// corpsMessage =

	// break;
	// case T_BODY:
	// System.out.println("BODY : "
	// + stream.getBodyDescriptor()
	// .getMimeType());
	// String typeMime = stream
	// .getBodyDescriptor()
	// .getMimeType();
	// if (typeMime.indexOf("text/") != -1)
	// {
	// try {
	// corpsMessage =
	// getTextFromMimeMessage(new
	// MimeMessage(
	// null,
	// stream.getDecodedInputStream()));
	// } catch (MessagingException e) {
	// e.printStackTrace();
	// }
	// }

	// NETTOYAGE
	// // A ECRIT
	// String newCorps1 = "";
	// if (corps.indexOf("a écrit") != -1) {
	// String[] tabCorps1 = corps.split("a écrit");
	// // Première partie
	// String[] p1 = tabCorps1[0].split("\n");
	// for (int i = 0; i < p1.length - 1; i++) {
	// newCorps1 += p1[i];
	// }
	//
	// String newCorps2 = "";
	// String[] corps2 = tabCorps1[1].split("\n");
	// for (String sCorps2 : corps2) {
	// newCorps2 += "## " + sCorps2 + "\n";
	// }
	// newCorps1 += "\n" + newCorps2;
	// } else
	// newCorps1 = corps;
	// newCorps1 = corps;

	// // LIGNES COMMENCANT PAR >
	// String[] tabCorps1 = corps.split("\n");
	// String newCorps1 = "";
	// for (String ligne : tabCorps1) {
	// ligne = ligne.trim();
	// String newLigne;
	// if (ligne.length() != 0) {
	// if (ligne.indexOf(">") == 0
	// // || ligne.indexOf("De :") == 0
	// // || ligne.indexOf("From :") == 0
	// // || ligne.indexOf("Date :") == 0
	// // || ligne.indexOf("Envoyé :") == 0
	// // || ligne.indexOf("Sent :") == 0
	// // || ligne.indexOf("À :") == 0
	// // || ligne.indexOf("To :") == 0
	// // || ligne.indexOf("Objet :") == 0
	// // || ligne.indexOf("Object :") == 0
	// // || ligne.indexOf("Subject :") == 0
	// // || ligne.indexOf("De:") == 0
	// // || ligne.indexOf("From:") == 0
	// // || ligne.indexOf("Date:") == 0
	// // || ligne.indexOf("Envoyé:") == 0
	// // || ligne.indexOf("Sent:") == 0
	// // || ligne.indexOf("À:") == 0
	// // || ligne.indexOf("To:") == 0
	// // || ligne.indexOf("Objet:") == 0
	// // || ligne.indexOf("Object:") == 0
	// // || ligne.indexOf("Subject:") == 0
	// )
	// newLigne = "## " + ligne;
	// else
	// newLigne = ligne;
	// newCorps1 += newLigne + "\n";
	// }
	// }
}
