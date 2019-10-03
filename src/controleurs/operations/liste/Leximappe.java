package controleurs.operations.liste;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;

import org.apache.commons.lang3.StringUtils;

import modeles.MessageModel;
import modeles.TermeModel;
import controleurs.vuesabstraites.ProjetView;

public class Leximappe {

	private ProjetView activitesView;
	private ResourceBundle bundleOperationsListe;
	private ArrayList<MessageModel> listMessages;

	public Leximappe(ProjetView activitesView,
			ResourceBundle bundleOperationsListe,
			ArrayList<MessageModel> listMessages) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.listMessages = listMessages;

	}

	public void indexWordsConversation() {
		new SwingWorker<Object, Object>() {
			@Override
			public Object doInBackground() {
				System.out.println("Nombre de messages = "
						+ listMessages.size());

				// Creation d'un setStopWords<String>
				// selon differents fichiers txt
				Set<String> setStopWords = new HashSet<String>();

				try {
					// Stopwords antidictionnaire
					Scanner scanStopWords = new Scanner(
							new File(
									"/Users/frederic/Documents/L@ME/stopwords-utf8.txt"));
					while (scanStopWords.hasNext()) {
						String stopword = scanStopWords.next();
						setStopWords.add(stopword);
					}
					scanStopWords.close();

					// Stopwords html
					Scanner scanStopHtmlWords = new Scanner(new File(
							"/Users/frederic/Documents/L@ME/stophtmlwords.txt"));
					while (scanStopHtmlWords.hasNext()) {
						String stophtmlword = scanStopHtmlWords.next();
						setStopWords.add(stophtmlword);
					}
					scanStopHtmlWords.close();

					// Stopwords mimemessage
					Scanner scanStopMimeMessagelWords = new Scanner(
							new File(
									"/Users/frederic/Documents/L@ME/stopmimemessagewords.txt"));
					while (scanStopMimeMessagelWords.hasNext()) {
						String stopmimemessageword = scanStopMimeMessagelWords
								.next();
						setStopWords.add(stopmimemessageword);
					}
					scanStopMimeMessagelWords.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				System.out.println("Taille de setStopWords = "
						+ setStopWords.size());
				int nbrMotsEnleves = 0;
				activitesView.setStepProgress(listMessages.size());
				activitesView.resetProgress();
				activitesView
						.setLabelProgress("Indexation et nettoyage des corps de message");

				// Parcours de la listMessages pour extraire les mots des corps
				// de message
				// -> Creation des objets Terme
				// -> Creation d'un Set de Termes
				// -> Creation d'un Set de mots pour la lemmatisation
				Set<TermeModel> setTermes = new HashSet<TermeModel>();
				Set<String> setMots = new HashSet<String>();

				for (MessageModel message : listMessages) {
					activitesView.updateProgress();
					
					// System.out.println("Message traite : "
					// + message.getFichier());
					String identifiantMessage = message.getIdentifiant();
					String numMessage = message.getNumero();
					String corpsMessage = message.getCorps();

					corpsMessage = message.getTextSsHTML(corpsMessage)
							.toLowerCase();
					// corpsMessage = corpsMessage.replaceAll("\\p{Cntrl}",
					// "?");

					// Split du corps de message selon la ponctuation et les
					// espaces
					String[] tabMots = corpsMessage.split("[\\p{Punct}\\s]+");

					// String[] tabMots =
					// StringUtils.splitByWholeSeparator(corpsMessage, null,
					// 0);

					List<String> listMotsMessage = new ArrayList<String>(
							Arrays.asList(tabMots));
					int sizeAvantSW = listMotsMessage.size();
					listMotsMessage.removeAll(setStopWords);
					int sizeApresSW = listMotsMessage.size();
					nbrMotsEnleves += sizeAvantSW - sizeApresSW;
					// Nettoyage de mots HTML :
					// px, pt, em
					String strPx = "\\dpx";
					Pattern patternPx = Pattern.compile(strPx);
					String strPt = "\\dpt";
					Pattern patternPt = Pattern.compile(strPt);
					String strEm = "\\dem";
					Pattern patternEm = Pattern.compile(strEm);

					for (String mot : listMotsMessage) {
						mot = mot.trim();
						Matcher matcherPx = patternPx.matcher(mot);
						Matcher matcherPt = patternPt.matcher(mot);
						Matcher matcherEm = patternEm.matcher(mot);
						if (mot.length() > 1 && !StringUtils.isNumeric(mot)
								&& !matcherPx.find() && !matcherPt.find()
								&& !matcherEm.find()) {
							TermeModel terme = null;
							if (!setMots.contains(mot)) {
								terme = new TermeModel(mot);
								setTermes.add(terme);
								setMots.add(mot);
							} else {
								for (TermeModel existingTerme : setTermes) {
									if (existingTerme.getMot().equals(mot)) {
										terme = existingTerme;
										break;
									}
								}
							}
							if (!terme.equals(null)) {
								int frequence = terme.getFrequence() + 1;
								terme.setFrequence(frequence);
								terme.addNumMessage(numMessage);
								terme.addIdentifiantMessage(identifiantMessage);
							}
						}
					}
				}
				System.out
						.println("Nombre de mots enlevés = " + nbrMotsEnleves);

				// Affichage
				System.out.println("Taille de setTermes = " + setTermes.size());
				System.out
						.println("Terme | Fréquence | MapNumMessagesFrequence");
				for (TermeModel terme : setTermes) {
					System.out.println(terme.getMot() + " | "
							+ terme.getFrequence() + " | "
							+ terme.getMapNumMessageFrequence());
				}

				// // Lemmatisation
				// // On fait �a sur un tableau global de tous les Mots trouves
				// car
				// // cela va BEAUCOUP PLUS VITE
				// // -> Creation d'une mapLemmes mot associe a un lemme
				// final Map<String, String> mapLemmes = new HashMap<String,
				// String>();
				//
				// activitesView
				// .setLabelProgress("Lemmatisation des index. Veuillez patienter.");
				// activitesView.getProgressBar().setIndeterminate(true);
				// activitesView.getProgressBar().setStringPainted(false);
				// activitesView.updateProgress();
				//
				// System.setProperty("treetagger.home",
				// "/Users/frederic/Documents/L@ME/LAME Librairies JAVA/tree-tagger");
				// TreeTaggerWrapper<String> tt = new
				// TreeTaggerWrapper<String>();
				//
				// try {
				// tt.setModel("/Users/frederic/Documents/L@ME/LAME Librairies JAVA/tree-tagger/models/fr-iso8859.par:iso8859-1");
				// tt.setHandler(new TokenHandler<String>() {
				// @Override
				// public void token(String token, String pos, String lemma) {
				// // System.out.println(token + "\t" + pos + "\t"
				// // + lemma);
				// mapLemmes.put(token, lemma);
				// }
				// });
				// tt.process(mapMotsListNumMessage.keySet());
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (TreeTaggerException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } finally {
				// tt.destroy();
				// }
				//
				// activitesView.getProgressBar().setIndeterminate(false);
				// activitesView.getProgressBar().setStringPainted(true);
				// activitesView.setStepProgress(mapMotsListNumMessage.size());
				// activitesView.setLabelProgress("Indexation des lemmes obtenus");
				//
				// // Remplacement des mots par leur lemme
				// // -> Creation d'une mapLemmeListNumMessage : lemme associe a
				// // une liste de numeros de message
				// // -> Creation d'une mapNumMessageListLemme : numMessage
				// associe a
				// // une liste de lemmes
				// // -> Creation d'une mapLemmeFrequence : lemme associe a leur
				// // frequence
				//
				// TreeMap<String, TreeSet<String>> mapLemmeListNumMessage = new
				// TreeMap<String, TreeSet<String>>();
				// TreeMap<String, TreeSet<String>> mapNumMessageListLemme = new
				// TreeMap<String, TreeSet<String>>();
				// TreeMap<String, Integer> mapLemmeFrequence = new
				// TreeMap<String, Integer>();
				//
				// for (String mot : mapMotsListNumMessage.keySet()) {
				// activitesView.updateProgress();
				// TreeSet<String> setNumMessage = mapMotsListNumMessage
				// .get(mot);
				// int frequence = mapMotsFrequence.get(mot);
				// String lemme = mapLemmes.get(mot);
				// if (!mapLemmeListNumMessage.containsKey(lemme)) {
				// mapLemmeListNumMessage.put(lemme, setNumMessage);
				// mapLemmeFrequence.put(lemme, frequence);
				// } else {
				// TreeSet<String> existingSetNumMessage =
				// mapLemmeListNumMessage
				// .get(lemme);
				// existingSetNumMessage.addAll(setNumMessage);
				// int existingFrequence = mapLemmeFrequence.get(lemme);
				// // la frequence du lemme est egale a la somme des
				// // frequences du mot qu'il remplace
				// existingFrequence += frequence;
				// mapLemmeFrequence.put(lemme, existingFrequence);
				// }
				//
				// }
				//
				// // R�sultats
				// System.out
				// .println("ON AVAIT taille de mapMotsListNumMessage = "
				// + mapMotsListNumMessage.size());
				// System.out.println(mapMotsListNumMessage);
				// // for (String mot : mapMotsListNumMessage.keySet()){
				// // System.out.print("Mot " + mot + " : ");
				// // TreeSet<String> setNumMessage = mapMotsListNumMessage
				// // .get(mot);
				// // System.out.print(setNumMessage);
				// // System.out.println();
				// // }
				// // System.out.println();
				//
				// System.out
				// .println("ON A MAINTENANT taille de newMapMotsListNumMessage = "
				// + mapLemmeListNumMessage.size());
				// // System.out.println(mapLemmeListNumMessage);
				// System.out.println("Lemme | Fr�quence");
				// for (String lemme : mapLemmeListNumMessage.keySet()) {
				// System.out.print(lemme + " | ");
				// int frequence = mapLemmeFrequence.get(lemme);
				// System.out.print(frequence);
				// // TreeSet<String> setNumMessage = mapLemmeListNumMessage
				// // .get(lemme);
				// // System.out.print(setNumMessage);
				// System.out.println();
				// }
				//
				// // Calcul des cooccurences

				return null;
			}

			@Override
			public void done() {

			}
		}.execute();
	}
}