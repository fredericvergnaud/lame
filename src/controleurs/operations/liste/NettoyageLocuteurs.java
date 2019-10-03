package controleurs.operations.liste;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import vue.dialog.DialogPanelNettoyageAutoLocuteurs;
import vue.dialog.DialogPanelNettoyageManuelLocuteurs;
import comparators.StringArobaseLengthComparator;
import modeles.MessageModel;
import controleurs.vuesabstraites.ProjetView;
import extra.LocuteurPourNettoyage;

public class NettoyageLocuteurs {

	private ProjetView activitesView;
	private ResourceBundle bundleOperationsListe;
	private List<MessageModel> listMessages;

	int nbreModifs = 0;

	public NettoyageLocuteurs(ProjetView activitesView, ResourceBundle bundleOperationsListe, List<MessageModel> listMessages) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.listMessages = listMessages;
	}

	public void nettoyageAutoLocuteurs() {
		activitesView.resetProgress();
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_NettoyageLocuteurs") + " - " + bundleOperationsListe.getString("txt_Patientez"));
		activitesView.getProgressBar().setIndeterminate(true);
		activitesView.getProgressBar().setStringPainted(false);
		activitesView.updateProgress();
		Map<String, Set<String>> mapGpXNom = new HashMap<String, Set<String>>();
		Map<String, List<MessageModel>> mapGpXMessages = new HashMap<String, List<MessageModel>>();
		Map<String, Set<String>> mapPXNom = new HashMap<String, Set<String>>();
		Map<String, List<MessageModel>> mapPXMessages = new HashMap<String, List<MessageModel>>();
		Map<String, Set<String>> mapMXNom = new HashMap<String, Set<String>>();
		Map<String, List<MessageModel>> mapMXMessages = new HashMap<String, List<MessageModel>>();
		Set<Set<String>> setGPPMXNoms = new HashSet<Set<String>>();

		for (MessageModel message : listMessages) {
			String nomL = message.getExpediteur();
			String mL = message.getMail();
			String pL = message.getProfilYahoo();
			String gpL = message.getGroupPostYahoo();

			// System.out.println(mL + " | " + pL + " | " + gpL);

			if (!gpL.equals(bundleOperationsListe.getString("txt_Indefini"))) {
				if (!mapGpXNom.containsKey(gpL)) {
					Set<String> newSetNoms = new HashSet<String>();
					newSetNoms.add(nomL + " $$$ " + mL + " $$$ " + pL + " $$$ " + gpL);
					mapGpXNom.put(gpL, newSetNoms);
					List<MessageModel> newListMessages = new ArrayList<MessageModel>();
					newListMessages.add(message);
					mapGpXMessages.put(gpL, newListMessages);
				} else {
					Set<String> setNoms = mapGpXNom.get(gpL);
					setNoms.add(nomL + " $$$ " + mL + " $$$ " + pL + " $$$ " + gpL);
					mapGpXNom.put(gpL, setNoms);
					List<MessageModel> oldListMessages = mapGpXMessages.get(gpL);
					oldListMessages.add(message);
					mapGpXMessages.put(gpL, oldListMessages);
				}
			}

			if (!pL.equals(bundleOperationsListe.getString("txt_Indefini"))) {
				if (!mapPXNom.containsKey(pL)) {
					Set<String> newSetNoms = new HashSet<String>();
					newSetNoms.add(nomL + " $$$ " + mL + " $$$ " + pL + " $$$ " + gpL);
					mapPXNom.put(pL, newSetNoms);
					List<MessageModel> newListMessages = new ArrayList<MessageModel>();
					newListMessages.add(message);
					mapPXMessages.put(pL, newListMessages);
				} else {
					Set<String> setNoms = mapPXNom.get(pL);
					setNoms.add(nomL + " $$$ " + mL + " $$$ " + pL + " $$$ " + gpL);
					mapPXNom.put(pL, setNoms);
					List<MessageModel> oldListMessages = mapPXMessages.get(pL);
					oldListMessages.add(message);
					mapPXMessages.put(pL, oldListMessages);
				}
			}

			if (!mL.equals(bundleOperationsListe.getString("txt_Indefini"))) {
				if (!mapMXNom.containsKey(mL)) {
					Set<String> newSetNoms = new HashSet<String>();
					newSetNoms.add(nomL + " $$$ " + mL + " $$$ " + pL + " $$$ " + gpL);
					mapMXNom.put(mL, newSetNoms);
					List<MessageModel> newListMessages = new ArrayList<MessageModel>();
					newListMessages.add(message);
					mapMXMessages.put(mL, newListMessages);
				} else {
					Set<String> setNoms = mapMXNom.get(mL);
					setNoms.add(nomL + " $$$ " + mL + " $$$ " + pL + " $$$ " + gpL);
					mapMXNom.put(mL, setNoms);
					List<MessageModel> oldListMessages = mapMXMessages.get(mL);
					oldListMessages.add(message);
					mapMXMessages.put(mL, oldListMessages);
				}
			}
		}
		System.out.println();
		System.out.println("taille de mapGpXNom = " + mapGpXNom.size() + " : " + mapGpXNom);
		System.out.println("taille de mapPXNom = " + mapPXNom.size() + " : " + mapPXNom);
		System.out.println("taille de mapMXNom = " + mapMXNom.size() + " : " + mapMXNom);

		// AJOUT DE MAPGPXNOM
		for (Entry<String, Set<String>> e : mapGpXNom.entrySet()) {
			Set<String> newSetNoms = new HashSet<String>(e.getValue());
			setGPPMXNoms.add(newSetNoms);
		}
		// System.out.println("SETGPPMXNOMS APRES AJOUT DE MAPGPXNOM");
		// for (Set<String> setNoms : setGPPMXNoms) {
		// System.out.print(setNoms + "\n");
		// }
		// System.out.println();

		// AJOUT DE MAPPXNOM
		for (Entry<String, Set<String>> e : mapPXNom.entrySet()) {
			Set<String> newSetNoms = new HashSet<String>(e.getValue());
			for (String newNom : newSetNoms) {
				// System.out.print(newNom + " : ");
				for (Set<String> setNoms : setGPPMXNoms) {
					if (setNoms.contains(newNom) && !setNoms.equals(newSetNoms)) {
						// System.out.print(newSetNoms + " ajouté à " +
						// setNoms);
						setNoms.addAll(newSetNoms);
						// System.out.print(" => devient " + setNoms);
						// System.out.println();
						break;
					}
				}
			}
		}

		Set<TreeSet<String>> newSetGPPMXNoms = new HashSet<TreeSet<String>>();
		for (Set<String> setNoms : setGPPMXNoms) {
			TreeSet<String> sortedSetNoms = new TreeSet<String>(setNoms);
			newSetGPPMXNoms.add(sortedSetNoms);
		}
		// System.out.println();
		// System.out.println("SETGPPMXNOMS APRES AJOUT DE MAPPXNOM");
		// for (Set<String> newSetNoms : newSetGPPMXNoms) {
		// System.out.print(newSetNoms);
		// System.out.println();
		// }
		// System.out.println();

		// AJOUT DE MAPMXNOM
		for (Entry<String, Set<String>> e : mapMXNom.entrySet()) {
			Set<String> newSetNoms = new HashSet<String>(e.getValue());
			for (String newNom : newSetNoms) {
				// System.out.print(newNom + " : ");
				for (Set<String> setNoms : setGPPMXNoms) {
					if (setNoms.contains(newNom) && !setNoms.equals(newSetNoms)) {
						// System.out.print(newSetNoms + " ajouté à " +
						// setNoms);
						setNoms.addAll(newSetNoms);
						// System.out.print(" => devient " + setNoms);
						// System.out.println();
						break;
					}
				}

			}
		}

		newSetGPPMXNoms = new HashSet<TreeSet<String>>();
		for (Set<String> setNoms : setGPPMXNoms) {
			TreeSet<String> sortedSetNoms = new TreeSet<String>(setNoms);
			newSetGPPMXNoms.add(sortedSetNoms);
		}
		// System.out.println();
		// System.out.println("SETGPPMXNOMS APRES AJOUT DE MAPMXNOM");
		// for (Set<String> newSetNoms : newSetGPPMXNoms) {
		// System.out.print(newSetNoms);
		// System.out.println();
		// }
		// System.out.println();

		// NETTOYAGE
		Iterator<TreeSet<String>> iSetNoms = newSetGPPMXNoms.iterator();
		while (iSetNoms.hasNext()) {
			TreeSet<String> setNoms = iSetNoms.next();
			if (setNoms.size() < 2)
				iSetNoms.remove();
		}

		// System.out.println();
		// System.out.println("SETGPPMXNOMS APRES NETTOYAGE");
		// for (Set<String> newSetNoms : newSetGPPMXNoms) {
		// ArrayList<String> listJustNom = new ArrayList<String>();
		// for (String nom : newSetNoms) {
		// String[] justNom = nom.split("\\$\\$\\$");
		// String n = justNom[0];
		// // System.out.println(n);
		// listJustNom.add(n);
		// }
		// System.out.print(listJustNom);
		// System.out.println();
		// }
		// System.out.println();

		// AFFICHAGE FINAL + MAP AFFICHAGE
		Map<String, List<String>> mapAffichage = new HashMap<String, List<String>>();
		System.out.println("SETGPPMXNOMS FINAL");
		for (Set<String> newSetNoms : newSetGPPMXNoms) {
			List<String> newListNoms = new ArrayList<String>(newSetNoms);
			Collections.sort(newListNoms, new StringArobaseLengthComparator());
			String nomAGarder = newListNoms.get(0);
			mapAffichage.put(nomAGarder, newListNoms);
			System.out.print(newListNoms);
			System.out.println();
		}
		System.out.println();

		if (mapAffichage.size() != 0) {
			DialogPanelNettoyageAutoLocuteurs optPanel = new DialogPanelNettoyageAutoLocuteurs(mapAffichage, bundleOperationsListe);
			int result = JOptionPane.showOptionDialog(null, optPanel, bundleOperationsListe.getString("txt_NettoyageLocuteurs"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					null, null);
			if (result == JOptionPane.OK_OPTION) {
				JPanel mainPanelChk = optPanel.getMainPanelChk();
				System.out.println("nom panel = " + mainPanelChk.getName());
				Component[] tabComp = mainPanelChk.getComponents();
				nbreModifs = 0;
				for (int i = 0; i < tabComp.length; i++) {
					JPanel panelChk = (JPanel) tabComp[i];
					Component[] tabComp2 = panelChk.getComponents();
					JCheckBox box = (JCheckBox) tabComp2[0];
					if (box.isSelected()) {
						JTextField txtField = (JTextField) tabComp2[1];
						String newName = txtField.getText();
						String nomBox = box.getName();
						// System.out.println("nom à rechercher dans la MAP : "
						// + nomBox + " | nouveau nom : " + newName);
						for (Entry<String, List<String>> e : mapAffichage.entrySet()) {
							if (e.getKey().equals(nomBox)) {
								String[] tabNomBox = nomBox.split("\\$\\$\\$");
								String nomAGarder = newName.trim();
								String mailAGarder = tabNomBox[1].trim();
								String profilAGarder = tabNomBox[2].trim();
								String grouPostAGarder = tabNomBox[3].trim();
								// System.out.println("A GARDER = " + nomAGarder
								// + " | " + mailAGarder + " | "
								// + profilAGarder + " | "
								// + grouPostAGarder);
								List<String> listAEnlever = e.getValue();
								for (String aEnlever : listAEnlever) {
									String[] tabAEnlever = aEnlever.split("\\$\\$\\$");
									String mailAEnlever = tabAEnlever[1].trim();
									String profilAEnlever = tabAEnlever[2].trim();
									String groupPostAEnlever = tabAEnlever[3].trim();
									// System.out.println("A ENLEVER = "
									// + nomAEnlever + " | "
									// + mailAEnlever + " | "
									// + profilAEnlever + " | "
									// + groupPostAEnlever);
									if (mapGpXMessages.containsKey(groupPostAEnlever)) {
										for (MessageModel message : mapGpXMessages.get(groupPostAEnlever)) {
											message.setExpediteur(nomAGarder);
											message.setMail(mailAGarder);
											message.setProfilYahoo(profilAGarder);
											message.setGroupPostYahoo(grouPostAGarder);
											nbreModifs++;
										}
									} else if (mapPXMessages.containsKey(profilAEnlever)) {
										for (MessageModel message : mapPXMessages.get(profilAEnlever)) {
											message.setExpediteur(nomAGarder);
											message.setMail(mailAGarder);
											message.setProfilYahoo(profilAGarder);
											message.setGroupPostYahoo(grouPostAGarder);
											nbreModifs++;
										}

									} else if (mapMXMessages.containsKey(mailAEnlever)) {
										for (MessageModel message : mapMXMessages.get(mailAEnlever)) {
											message.setExpediteur(nomAGarder);
											message.setMail(mailAGarder);
											message.setProfilYahoo(profilAGarder);
											message.setGroupPostYahoo(grouPostAGarder);
											nbreModifs++;
										}
									}
								}
							}
						}
					}
				}
			}
		} else
			JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_NettoyageAutomatiqueImpossible"), bundleOperationsListe.getString("txt_NettoyageLocuteurs"),
					JOptionPane.INFORMATION_MESSAGE);
	}

	public void nettoyageManuelLocuteurs() {
		activitesView.resetProgress();
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_NettoyageLocuteurs") + " - " + bundleOperationsListe.getString("txt_Patientez"));
		activitesView.getProgressBar().setIndeterminate(true);
		activitesView.getProgressBar().setStringPainted(false);
		activitesView.updateProgress();
		DialogPanelNettoyageManuelLocuteurs optPanel = new DialogPanelNettoyageManuelLocuteurs(listMessages, bundleOperationsListe, nbreModifs);
		JTable tableLocuteurs = optPanel.getTableLocuteurs();
		// TableModel model = tabLocuteurs.getModel();
		String[] options = new String[4];
		options[3] = bundleOperationsListe.getString("txt_Valider");
		options[2] = bundleOperationsListe.getString("txt_Exporter");
		options[1] = bundleOperationsListe.getString("txt_Actualiser");
		options[0] = bundleOperationsListe.getString("txt_Annuler");
		int result = JOptionPane.showOptionDialog(null, optPanel, bundleOperationsListe.getString("txt_NettoyageLocuteurs"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
				null);
		Set<LocuteurPourNettoyage> setNewLocuteurs = optPanel.getNewListLocuteurs();
		if (result == 0) {
			return;
		} else if (result == 1) {
			nbreModifs = optPanel.getNbreModifsTotal();
			modifListMessages(setNewLocuteurs);
			nettoyageManuelLocuteurs();
		} else if (result == 2)
			exportTableauNettoyageLocuteurs(tableLocuteurs);
		else {
			// System.out.println("Liste des locuteurs dans listMessage : ");
			// for (MessageModel message : listMessages) {
			// String locuteur = message.getExpediteur();
			// System.out.println("locuteur = " + locuteur);
			// }
			// System.out.println("Liste des locuteurs dans listLocuteurs récupérée de DialogPanelNettoyageManuelLocuteurs : ");
			// for (LocuteurPourNettoyage locuteur :
			// optPanel.getNewListLocuteurs()) {
			// System.out.println("locuteur : ancien nom = " +
			// locuteur.getAncienNom() + " | nouveau nom = " +
			// locuteur.getNouveauNom());
			// }
			modifListMessages(setNewLocuteurs);
		}
	}

	private void modifListMessages(Set<LocuteurPourNettoyage> setNewLocuteurs) {
		for (MessageModel message : listMessages) {
			String nomLocuteur = message.getExpediteur();
			for (LocuteurPourNettoyage locuteurPourNettoyage : setNewLocuteurs) {
				String ancienNomLocuteur = locuteurPourNettoyage.getAncienNom();
				String nouveauNomLocuteur = locuteurPourNettoyage.getNouveauNom();
				String nouveauGroupPost = locuteurPourNettoyage.getGroupPost();
				String nouveauProfil = locuteurPourNettoyage.getProfil();
				String nouveauMail = locuteurPourNettoyage.getMail();
				if (ancienNomLocuteur.equals(nomLocuteur) && !nouveauNomLocuteur.equals(nomLocuteur)) {
					message.setExpediteur(nouveauNomLocuteur);
					message.setGroupPostYahoo(nouveauGroupPost);
					message.setProfilYahoo(nouveauProfil);
					message.setMail(nouveauMail);
					nbreModifs++;
				}
			}
		}
	}

	public void exportTableauNettoyageLocuteurs(JTable tableLocuteurs) {
		PrintWriter out = null;
		JFileChooser filechoose = new JFileChooser(bundleOperationsListe.getString("txt_ExportTabCorresLoc"));
		int resultatEnregistrer = filechoose.showSaveDialog(filechoose);
		if (resultatEnregistrer == JFileChooser.APPROVE_OPTION) {
			String monFichier = new String(filechoose.getSelectedFile().toString());
			// Recuperer le nom du fichier et ajouter .csv si pas
			// specifie
			if (!(monFichier.endsWith(".csv") || monFichier.endsWith(".CSV")))
				monFichier = monFichier + ".csv";
			// System.out.println("default charset = " +
			// Charset.defaultCharset()
			// + " # alias = " + Charset.defaultCharset().aliases());
			// System.out.println("OS Name = "
			// + System.getProperties().get("os.name"));
			// String charset = null;
			// if (System.getProperties().get("os.name").equals("Mac OS X"))
			// charset = "MacRoman";
			// else
			// charset = "ISO-8859-1";
			String charset = "ISO-8859-1";
			try {
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(monFichier), charset)));
				for (int i = 0; i < tableLocuteurs.getModel().getRowCount(); i++) {
					for (int j = 0; j < 2; j++) {
						out.write("\"" + String.valueOf(tableLocuteurs.getModel().getValueAt(i, j)) + "\";");
					}
					out.write("\n");
				}
				out.close();
				JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ListeLocuteursExportee") + "\n" + monFichier, bundleOperationsListe.getString("txt_NettoyageLocuteurs"),
						JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			nettoyageManuelLocuteurs();
		}
	}

	public void importNettoyageLocuteurs() {
		activitesView.resetProgress();
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_NettoyageLocuteurs") + " - " + bundleOperationsListe.getString("txt_Patientez"));
		activitesView.getProgressBar().setIndeterminate(true);
		activitesView.getProgressBar().setStringPainted(false);
		activitesView.updateProgress();
		Map<String, String> mapImport = new HashMap<String, String>();
		FileFilter objFilter = new FileNameExtensionFilter("Fichiers CSV", "csv");
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(objFilter);
		fc.setDialogTitle(bundleOperationsListe.getString("txt_ImportTabCorresLoc"));
		int returnVal = fc.showOpenDialog(fc.getParent());
		String charset = null;
		if (System.getProperties().get("os.name").equals("Mac OS X"))
			charset = "MacRoman";
		else
			charset = "UTF8";
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String nomFichierNettoyageLocuteurs = file.getName();
			String cheminFichierNettoyageLocuteurs = file.getPath();
			System.out.println("chemin fichier = " + cheminFichierNettoyageLocuteurs);
			System.out.println("taille = " + file.length());
			try {
				InputStream ips = new FileInputStream(file);
				InputStreamReader ipsr = new InputStreamReader(ips, charset);
				BufferedReader br = new BufferedReader(ipsr);
				String ligne;
				while ((ligne = br.readLine()) != null) {
					// System.out.println(ligne);
					String[] tabSplit = ligne.split(";");
					String ancienNom = tabSplit[0].replace("\"", "");
					String nouveauNom = tabSplit[1].replace("\"", "");
					// if (!ancienNom.equals("") &&
					// !ancienNom.equals(nouveauNom)) {
					System.out.println(ancienNom + "|" + nouveauNom);
					if (!mapImport.containsKey(ancienNom))
						mapImport.put(ancienNom, nouveauNom);
					// }
				}
				br.close();
				int result = JOptionPane.showOptionDialog(null,
						bundleOperationsListe.getString("txt_AppliquerTabCorresLoc") + " " + nomFichierNettoyageLocuteurs + " \n" + bundleOperationsListe.getString("txt_AListeLoc") + " ?",
						bundleOperationsListe.getString("txt_NettoyageLocuteurs"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

				if (result == JOptionPane.OK_OPTION) {
					Map<String, List<MessageModel>> mapNomLXMessages = new HashMap<String, List<MessageModel>>();
					for (MessageModel message : listMessages) {
						String nomL = message.getExpediteur();
						if (!mapNomLXMessages.containsKey(nomL)) {
							List<MessageModel> newListMessages = new ArrayList<MessageModel>();
							newListMessages.add(message);
							mapNomLXMessages.put(nomL, newListMessages);
						} else {
							List<MessageModel> oldListMessages = mapNomLXMessages.get(nomL);
							oldListMessages.add(message);
							mapNomLXMessages.put(nomL, oldListMessages);
						}
					}
					for (Entry<String, String> e : mapImport.entrySet()) {
						String ancienNom = e.getKey();
						String nouveauNom = e.getValue();
						if (mapNomLXMessages.containsKey(ancienNom)) {
							// System.out.println(nouveauNom
							// + " remplace " + ancienNom);
							nbreModifs++;
							for (MessageModel message : mapNomLXMessages.get(ancienNom)) {
								message.setExpediteur(nouveauNom);
								nbreModifs++;
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	public int getNbreModifs() {
		return nbreModifs;
	}
}
