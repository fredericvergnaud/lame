package controleurs.operations.liste.ajoutmessages.bal;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import modeles.MessageModel;

import org.apache.commons.io.output.FileWriterWithEncoding;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import controleurs.vuesabstraites.ProjetView;

public class AddMessagesFromBalLocale {

	private ResourceBundle bundleOperationsListe;
	private ProjetView activitesView;
	private Map<String, MessageModel> mapIdMessages;
	private String repertoire;

	public AddMessagesFromBalLocale(ResourceBundle bundleOperationsListe,
			ProjetView activitesView, String repertoire) {
		this.bundleOperationsListe = bundleOperationsListe;
		this.activitesView = activitesView;
		this.repertoire = repertoire;
	}

	public void displayDialog() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setDialogTitle(bundleOperationsListe
				.getString("txt_AjouterMessages"));
		int returnVal = fc.showOpenDialog(fc.getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			final File file = fc.getSelectedFile();
			final Set<String> setFichiers = new HashSet<String>();
//			new SwingWorker<Object, Object>() {
//				@Override
//				public Object doInBackground() {
					activitesView.resetProgress();
					activitesView.setLabelProgress(bundleOperationsListe
							.getString("txt_TraitementRepertoire")
							+ " - "
							+ bundleOperationsListe.getString("txt_Patientez"));
					activitesView.getProgressBar().setIndeterminate(true);
					activitesView.getProgressBar().setStringPainted(false);
					activitesView.updateProgress();

					if (file.isDirectory()) {
						String[] listeFichiers = file.list();
						for (int i = 0; i < listeFichiers.length; i++) {
							if (listeFichiers[i].endsWith(".eml")
									|| listeFichiers[i].endsWith(".emlx")) {
								// transformation des noms (si accents) de
								// fichiers en utf-8
								// --> A FAIRE
								setFichiers.add(file.toString() + "/"
										+ listeFichiers[i]);
							}
						}
					} else if (file.isFile()) {
						setFichiers.addAll(getSetFichiers(file));
					}

					activitesView.setLabelProgress(bundleOperationsListe
							.getString("txt_TraitementRepertoire"));
					activitesView.appendTxtArea(bundleOperationsListe
							.getString("txt_TraitementRepertoire")
							+ " "
							+ bundleOperationsListe.getString("txt_Accompli")
							+ "\n");
					activitesView.getProgressBar().setIndeterminate(false);
					activitesView.getProgressBar().setStringPainted(true);
//					return null;
//				}
//
//				@Override
//				public void done() {
					// System.out.println("nbre de fichiers = " + nbreFileEML);

					// System.out.println("MenuBar - actionAddMessages : liste selected = "
					// +
					// projetController.getListeSelected().getNom()+" | nbreMessages = "+oldNbreMessages);
					if (setFichiers.size() != 0) {
						System.out.println("taille de setFichiers = "
								+ setFichiers.size());
//						new SwingWorker<Object, Object>() {
//							@Override
//							public Object doInBackground() {
								// System.out
								// .println("projetController.getFrameActivites() = "
								// + projetController
								// .getFrameActivites());
								// System.out.println("listeController = "
								// + listeController);
								ExtractionMessages extract = new ExtractionMessages(setFichiers, bundleOperationsListe, activitesView
										);
								setNewMapIdMessages(extract.getNewMapIdMessage());
//								return null;
//							}
//
//							@Override
//							public void done() {
//								System.out
//										.println("AddMessagesForum : taille de newMapIdMessages = "
//												+ getNewMapIdMessages().size());
//								setNewNbreMessagesExtraits(getNewMapIdMessages()
//										.size());
//								activitesView
//										.appendTxtArea(getNewNbreMessagesExtraits()
//												+ " "
//												+ bundleOperationsListe
//														.getString("txt_MessagesExtraits")
//												+ "\n");
//								setOldNbreMessages(getNbreMessages());
//								activitesView.setStepProgress(1);
//								activitesView
//										.setLabelProgress(bundleOperationsListe
//												.getString("txt_AjoutMessagesListe"));
//								listeSelected
//										.addMapIdMessages(getNewMapIdMessages());
//								projetController.notifyListeAddedMessages();
//								setNewNbreMessages(getNbreMessages());
//
//								activitesView.setStepProgress(1);
//								activitesView.updateProgress();
//								verifyNewMapIdMessagesAdded();

//							}
//						}.execute();

					} else {
						JOptionPane
								.showMessageDialog(
										null,
										bundleOperationsListe
												.getString("txt_RepertoireAucunFichier"),
										bundleOperationsListe
												.getString("txt_AjoutMessages"),
										JOptionPane.ERROR_MESSAGE);
						displayDialog();
						return;
					}
//				}
//			}.execute();
		}
	}
	
	public void setNewMapIdMessages(Map<String, MessageModel> newMapIdMessages) {
		this.mapIdMessages = newMapIdMessages;
	}

	public Map<String, MessageModel> getNewMapIdMessages() {
		return mapIdMessages;
	}
	
	private Set<String> getSetFichiers(File file) {
		Set<String> setFichiers = new HashSet<String>();
		System.out.println("fichier mbx / mboxrd");
		// CHARSET
		// System.out.println("Default Charset="
		// + Charset.defaultCharset());
		// System.out.println("file.encoding="
		// + System.getProperty("file.encoding"));

		// BufferedReader reader = null;
		// Map<String, Integer> mapCharset = new HashMap<String, Integer>();
		// try {
		// reader = new BufferedReader(new FileReader(file));
		// String currentLine;
		// while ((currentLine = reader.readLine()) != null) {
		// if (currentLine.indexOf("Content-Type:")!=-1 &&
		// currentLine.indexOf("charset=")!=-1)
		// System.out.println(currentLine);
		// if
		// }
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// finally {
		// try {
		// reader.close();
		// } catch (Exception e2) {
		// // TODO: handle exception
		// }
		// }

		String charset = null;
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			CharsetDetector cd = new CharsetDetector();
			cd.setText(bis);
			CharsetMatch cm = cd.detect();
			charset = cm.getName();

			CharsetMatch matches[];
			matches = cd.detectAll();
			for (int m = 0; m < matches.length; m += 1) {
				System.out.println("\ncm.matche=" + m + " name="
						+ matches[m].getName() + " confidence="
						+ matches[m].getConfidence());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("charset icu = " + charset);

		// CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		// detector.add(new ParsingDetector(false));
		// detector.add(JChardetFacade.getInstance());
		// detector.add(ASCIIDetector.getInstance());
		// detector.add(UnicodeDetector.getInstance());
		// Charset charset2 = null;
		// try {
		// charset2 = detector.detectCodepage(new BufferedInputStream(
		// new FileInputStream(file)), 100);
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// if (charset2 != null) {
		// System.out.println(file.getName() + " encoding is:"
		// + charset2.name());
		// } else {
		// System.out.println(file.getName() + "unknown");
		// }
		// String charset = charset2.name();

		// Nettoyage du fichier
		// Writer w = null;
		// try {
		// FileInputStream src = new FileInputStream(file);
		// BufferedReader r = new BufferedReader(new InputStreamReader(src,
		// charset));
		//
		// FileOutputStream dest = new FileOutputStream(file.getAbsolutePath()
		// + "_modif");
		// w = new BufferedWriter(new OutputStreamWriter(dest, "utf-8"));
		//
		// String ligne;
		// while ((ligne = r.readLine()) != null) {
		// // if (ligne.indexOf("^M") != -1) {
		// // System.out.println("^M trouv�");
		// // ligne.replace("\\r$", "\n");
		// // }
		// // if (ligne.indexOf("From - ") != -1) {
		// // System.out.println("From - trouv�");
		// // ligne.replace("From - ", "From \n");
		// // }
		// w.write(ligne);
		// w.flush();
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// w.close();
		// } catch (Throwable e) {
		// }
		//
		// }

		try {
			Scanner scanner = null;
			try {
				String from = "From ";
				String from2 = "From - ";
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis, "iso-8859-1");
				BufferedReader br = new BufferedReader(isr);
				scanner = new Scanner(br);
				// scanner.useDelimiter("\\s" + from + "\\s|\\s+" + from2 +
				// "\\s");
				scanner.useDelimiter("From ");
				// Creation du repertoire qui accueille les
				// fichiers
				// messages
				File rep = new File(repertoire, "Messages " + file.getName());
				rep.mkdir();

				int i = 1;
				String sMessage = null;
				while (scanner.hasNext()) {
					sMessage = scanner.next();
					// System.out.println("taille de sMessage " + i + " = "
					// + sMessage.length());
					// // TRAITEMENT FICHIER MBX
					// // SUPRESSION DES TABULATIONS
					// sMessage = sMessage.replaceAll("\t", " ");
					// // SUPRESSION DES ESPACES CONSECUTIFS
					// sMessage = sMessage.replaceAll("( ){2,}", " ");
					// // SUPPRESSION DES CARACTERES OBTENUS
					// // AVEC CTRL
					// sMessage = sMessage.replaceAll("\\p{Cntrl}", "\n");
					sMessage = sMessage.trim();
					if (sMessage.length() > 0) {
						// System.out.println("MESSAGE NUMERO "
						// + j);
						// System.out.println(sMessage.substring(0,
						// 50));
						// System.out
						// .println("#################################");
						// listSMessages.add(sMessage);

						// FileWriterWithEncoding writer = null;
						// try {
						//
						// writer = new FileWriterWithEncoding(rep + "/" + i
						// + ".eml", "UTF-8");
						// writer.write(sMessage, 0, sMessage.length());
						// writer.close();
						// setFichiers.add(rep + "/" + i + ".eml");
						// } catch (IOException ex) {
						// ex.printStackTrace();
						// }
						BufferedWriter mbrWriter = new BufferedWriter(
								new FileWriterWithEncoding(rep + "/" + i
										+ ".eml", "iso-8859-1"));
						mbrWriter.write(sMessage);
						mbrWriter.close();
						setFichiers.add(rep + "/" + i + ".eml");
					}
					i++;
				}
			} finally {
				if (scanner != null)
					scanner.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// int count = 0;
		// CharsetEncoder ENCODER = Charset.forName("UTF-8").newEncoder();
		//
		// try {
		// for (CharBufferWrapper message :
		// MboxIterator.fromFile(file).charset(ENCODER.charset()).build()) {
		// // saveMessageToFile(count, buf);
		// //System.out.println(messageSummary(message.asInputStream(ENCODER.charset())));
		// count++;
		// }
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println("Found " + count + " messages");
		// try {
		// FileInputStream stream = new FileInputStream(file);
		// MboxrdStorageReader reader = new MboxrdStorageReader(stream, false);
		// int i=1;
		// while (reader.readNextMessage() != null) {
		// MailMessage message = reader.readNextMessage();
		// // Manipulate message - show contents
		// System.out.println("Message "+i+" | subject: " +
		// message.getSubject());
		// // Save this message in EML or MSG format
		// // message.save(message.getSubject() + ".eml",
		// // MailMessageSaveType.getEmlFormat());
		// // message.save(message.getSubject() + ".msg",
		// // MailMessageSaveType.getOutlookMessageFormatUnicode());
		//
		// // Get the next message
		// i++;
		// message = reader.readNextMessage();
		// }
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		return setFichiers;
	}
}
