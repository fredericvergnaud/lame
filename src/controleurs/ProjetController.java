package controleurs;

import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import controleurs.operations.liste.ModifyNomListe;
import controleurs.operations.projet.AddListe;
import controleurs.operations.projet.ExportListes;
import controleurs.operations.projet.ImportListes;
import controleurs.operations.projet.MergeListes;
import controleurs.operations.projet.OpenProjet;
import controleurs.operations.projet.RemoveListes;
import controleurs.vuesabstraites.ListeView;
import controleurs.vuesabstraites.ProjetView;
import extra.CustomJFileChooser;
import vue.Activites;
import vue.MainFrame;
import vue.MenuBar;
import vue.ToolBar;
import vue.projetPanelInf.TabConversationsPanel;
import vue.projetPanelInf.FilsListePanel;
import vue.projetPanelInf.AnalysePanel;
import vue.projetPanelInf.TabLocuteursPanel;
import vue.projetPanelInf.TabMessagesPanel;
import vue.projetPanelSup.InfosListePanel;
import vue.projetPanelSup.InfosProjetPanel;
import vue.projetPanelSup.TreeListesProjetPanel;
import modeles.ForumModel;
import modeles.ListeModel;
import modeles.MessageModel;
import modeles.ProjetModel;

public class ProjetController {
	private ProjetView panelListesListeView = null;
	private ListeView panelInfosListeView = null;
	private ListeView panelFilsListeView = null;
	private ListeView panelTabMessagesListeView = null;
	private ListeView panelTabConversationsListeView = null;
	private ListeView panelTabLocuteursListeView = null;
	private ListeView panelAnalyseListeView = null;
	private ProjetView panelInfosProjetView = null;
	private ProjetView menuBarView = null;
	private ListeView toolBarView = null;
	private ProjetModel projet = null;
	private MainFrame mainFrame;
	private ListeController listeController;
	private ProjetView activitesView = null;
	private ResourceBundle bundleOperationsListe;
	private ListeModel listeSelected = null;
	protected static ResourceBundle bundleProjetController;

	public ProjetController(MainFrame mainFrame, int choixLang, ProjetModel projet) {
		this.mainFrame = mainFrame;
		this.projet = projet;
		Locale locale;
		if (choixLang == 0) {
			locale = new Locale("fr", "FR");
			mainFrame.setTitle("L@ME | Logiciel d'Analyse de Messages Électroniques");
		} else {
			locale = new Locale("en", "GB");
			mainFrame.setTitle("L@ME | E-Messages Analysis Software");
		}
		bundleProjetController = ResourceBundle.getBundle("ProjetController", locale);
		bundleOperationsListe = ResourceBundle.getBundle("OperationsListe", locale);

		ResourceBundle menusPanel = ResourceBundle.getBundle("MenuPanel", locale);
		ResourceBundle activitesPanel = ResourceBundle.getBundle("ActivitesPanel", locale);
		ResourceBundle infosListePanel = ResourceBundle.getBundle("InfosListePanel", locale);
		ResourceBundle filsListePanel = ResourceBundle.getBundle("FilsListePanel", locale);
		ResourceBundle tabMessagesListePanel = ResourceBundle.getBundle("TabMessagesListePanel", locale);
		ResourceBundle tabConversationsListePanel = ResourceBundle.getBundle("TabConversationsListePanel", locale);
		ResourceBundle analyseListePanel = ResourceBundle.getBundle("AnalyseListePanel", locale);
		ResourceBundle tabLocuteursListePanel = ResourceBundle.getBundle("TabLocuteursListePanel", locale);
		ResourceBundle infosProjetPanel = ResourceBundle.getBundle("InfosProjetPanel", locale);
		ResourceBundle infosListeListesPanel = ResourceBundle.getBundle("InfosListeListesPanel", locale);

		panelInfosListeView = new InfosListePanel(null, infosListePanel);
		panelFilsListeView = new FilsListePanel(null, filsListePanel);
		panelTabMessagesListeView = new TabMessagesPanel(null, tabMessagesListePanel);
		panelTabConversationsListeView = new TabConversationsPanel(null, tabConversationsListePanel);
		panelTabLocuteursListeView = new TabLocuteursPanel(null, tabLocuteursListePanel);
		panelAnalyseListeView = new AnalysePanel(null, analyseListePanel);
		panelInfosProjetView = new InfosProjetPanel(this, infosProjetPanel);
		panelListesListeView = new TreeListesProjetPanel(this, infosListeListesPanel);
		menuBarView = new MenuBar(this, menusPanel);
		toolBarView = new ToolBar(null);
		activitesView = new Activites(this, activitesPanel);
	}

	// // // // // // // // LISTENERS // // // // // // // // // // // // // //

	private void addListenersToModel() {
		projet.addProjetListener(menuBarView);
		projet.addProjetListener(panelInfosProjetView);
		projet.addProjetListener(panelListesListeView);
		projet.addProjetListener(activitesView);
	}

	public void addViews() {
		// VIEWS PROJET
		mainFrame.setJMenuBar(menuBarView.getMenuBar());
		mainFrame.getPanelToolBar().add(toolBarView.getToolBar());
		mainFrame.getPanelSup().add(panelInfosProjetView.getInfosProjetPanel());
		mainFrame.getPanelSup().add(panelListesListeView.getListesListePanel());
		// VIEWS LISTE
		mainFrame.getPanelSup().add(panelInfosListeView.getInfosListePanel());
		// ONGLETS
		mainFrame.getOnglet().addTab(bundleProjetController.getString("txt_StructureListe"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icones/liste.png"))),
				panelFilsListeView.getFilsListePanel(), bundleProjetController.getString("txt_StructureListe"));
		mainFrame.getOnglet().addTab(bundleProjetController.getString("txt_TableauBord"),
				new ImageIcon(
						Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icones/analyse.png"))),
				panelAnalyseListeView.getAnalysePanel(), bundleProjetController.getString("txt_Analyse"));
		activitesView.showActivites();
		// PANELS TABLEAUX
		panelTabMessagesListeView.getTabMessagesPanel();
		panelTabConversationsListeView.getTabConversationsPanel();
		panelTabLocuteursListeView.getTabLocuteursPanel();

		// UIDefaults dialogTheme = new UIDefaults();
		// dialogTheme.put("TabbedPane:TabbedPaneTab[Enabled].backgroundPainter",
		// new Painter(Painter.BACKGROUND_ENABLED));
		// dialogTheme.put("TabbedPane:TabbedPaneTab[Disabled].backgroundPainter",
		// new Painter(Painter.BACKGROUND_DISABLED));
		// dialogTheme.put("TabbedPane:TabbedPaneTab[Enabled+MouseOver].backgroundPainter",
		// new Painter(Painter.BACKGROUND_ENABLED_MOUSEOVER));
		// dialogTheme.put("TabbedPane:TabbedPaneTab[Enabled+Pressed].backgroundPainter",
		// new Painter(Painter.BACKGROUND_ENABLED_PRESSED));
		// dialogTheme.put("TabbedPane:TabbedPaneTab[Selected].backgroundPainter",
		// new Painter(Painter.BACKGROUND_SELECTED));
		// dialogTheme.put("TabbedPane:TabbedPaneTab[Disabled+Selected].backgroundPainter",
		// new Painter(Painter.BACKGROUND_SELECTED_DISABLED));
		// dialogTheme.put("TabbedPane:TabbedPaneTab[Focused+Selected].backgroundPainter",
		// new Painter(Painter.BACKGROUND_SELECTED_FOCUSED));
		// dialogTheme.put("TabbedPane:TabbedPaneTab[MouseOver+Selected].backgroundPainter",
		// new Painter(Painter.BACKGROUND_SELECTED_MOUSEOVER));
		// dialogTheme.put("TabbedPane:TabbedPaneTab[Focused+MouseOver+Selected].backgroundPainter",
		// new Painter(Painter.BACKGROUND_SELECTED_MOUSEOVER_FOCUSED));
		// dialogTheme.put("TabbedPane:TabbedPaneTab[Pressed+Selected].backgroundPainter",
		// new Painter(Painter.BACKGROUND_SELECTED_PRESSED));
		// dialogTheme.put("TabbedPane:TabbedPaneTab[Focused+Pressed+Selected].backgroundPainter",
		// new Painter(Painter.BACKGROUND_SELECTED_PRESSED_FOCUSED));
		// dialogTheme.put("TabbedPane:TabbedPaneTabArea[Disabled].backgroundPainter",
		// new AreaPainter(AreaPainter.BACKGROUND_DISABLED));
		// dialogTheme.put("TabbedPane:TabbedPaneTabArea[Enabled+MouseOver].backgroundPainter",
		// new AreaPainter(AreaPainter.BACKGROUND_ENABLED_MOUSEOVER));
		// dialogTheme.put("TabbedPane:TabbedPaneTabArea[Enabled+Pressed].backgroundPainter",
		// new AreaPainter(AreaPainter.BACKGROUND_ENABLED_PRESSED));
		// dialogTheme.put("TabbedPane:TabbedPaneTabArea[Enabled].backgroundPainter",
		// new AreaPainter(AreaPainter.BACKGROUND_ENABLED));
		// dialogTheme.put("nimbusOrange", new Color(57, 105, 138));
		// mainFrame.getOnglet().putClientProperty("Nimbus.Overrides.InheritDefaults",
		// Boolean.TRUE);
		// mainFrame.getOnglet().putClientProperty("Nimbus.Overrides",
		// dialogTheme);

	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // NOTIFY SUR LISTE // // // // // // // // // // //

	public void notifyListeSelected(int numListeSelected) {
		// Recherche de la liste selected
		if (numListeSelected != 0) {
			Set<ListeModel> setListes = projet.getSetListes();
			for (ListeModel liste : setListes) {
				int numListe = liste.getNumero();
				if (numListe == numListeSelected) {
					this.listeSelected = liste;
					projet.setListeSelected(listeSelected);
					break;
				}
			}
		} else {
			this.listeSelected = new ListeModel(0);
		}
		this.listeController = new ListeController(this, this.listeSelected, bundleOperationsListe, panelInfosListeView,
				panelFilsListeView, panelAnalyseListeView, panelTabMessagesListeView, panelTabConversationsListeView,
				panelTabLocuteursListeView, activitesView, toolBarView);
		panelFilsListeView.setListeController(listeController);
		panelAnalyseListeView.setListeController(listeController);
		panelTabMessagesListeView.setListeController(listeController);
		panelTabLocuteursListeView.setListeController(listeController);
		panelTabConversationsListeView.setListeController(listeController);
		toolBarView.setListeController(listeController);
	}

	public void notifyListeAddedMessages() {
		// System.out.println("ProjetController - notifyListeAddedMessages :
		// listeSelected = "+listeSelected.getNom());
		projet.setNewCumul();
		// listeController.notifyAnalyseMessages();
	}

	public void notifyListeAnalyzed() {
		projet.save();
	}

	public void notifyListeCleaned() {
		projet.save();
	}

	// public void notifyListeNameChanged(String newNom) {
	// projet.save();
	// }

	// public void notifyListeAddedForum() {
	// projet.save();
	// }

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // NOTIFY SUR PROJET // // // // // // // // // // //

	public void notifyCreateNewProjet(String nomFichier, String repertoire) {
		projet = new ProjetModel();
		addListenersToModel();
		projet.createNewProjet(nomFichier, repertoire);
		notifyAddListe();
	}

	public void notifyOpenProjet() {
		new SwingWorker<Object, Object>() {
			ProjetModel openedProjet = null;
			OpenProjet openProjet;

			@Override
			public Object doInBackground() {
				activitesView.setLabelProgress(bundleProjetController.getString("txt_PatientezChargementProjet"));
				activitesView.getProgressBar().setIndeterminate(true);
				activitesView.getProgressBar().setStringPainted(false);
				activitesView.setStepProgress(1);
				activitesView.updateProgress();
				openProjet = new OpenProjet(bundleProjetController);
				openProjet.displayDialog();
				openedProjet = openProjet.getOpenedProjet();
				return null;
			}

			@Override
			public void done() {
				if (openedProjet != null) {
					projet = openedProjet;
					// REPERTOIRE TRANSCIENT
					String repertoire = openProjet.getFile().getAbsolutePath();
					if (repertoire.indexOf("/") != -1)
						repertoire = repertoire.substring(0, repertoire.lastIndexOf("/"));
					else
						repertoire = repertoire.substring(0, repertoire.lastIndexOf("\\"));
					addListenersToModel();
					projet.setTranscientRepertoire(repertoire);
					activitesView
							.appendTxtArea(bundleProjetController.getString("txt_RestaurationProjetAccomplie") + "\n");
					activitesView.setLabelProgress(bundleProjetController.getString("txt_ChargementProjet"));
				} else {
					activitesView.setLabelProgress("");
					activitesView.resetProgress();
				}
				activitesView.getProgressBar().setIndeterminate(false);
				activitesView.getProgressBar().setStringPainted(true);
			}
		}.execute();

	}

	public void notifyAddListe() {
		new SwingWorker<Object, Object>() {
			String nomListe = null;
			int oldNbreListe = projet.getNbreListes();

			@Override
			public Object doInBackground() {
				activitesView.setLabelProgress(bundleProjetController.getString("txt_PatientezAjoutListe"));
				activitesView.getProgressBar().setIndeterminate(true);
				activitesView.getProgressBar().setStringPainted(false);
				activitesView.setStepProgress(1);
				activitesView.updateProgress();
				AddListe addListe = new AddListe(bundleProjetController);
				addListe.displayDialog();
				nomListe = addListe.getNomListe();
				if (nomListe != null) {
					projet.addListe(nomListe);
				}
				return null;
			}

			@Override
			public void done() {
				int newNbreListe = projet.getNbreListes();
				if (newNbreListe == oldNbreListe + 1) {
					notifyListeSelected(projet.getNewListe().getNumero());
					listeController.notifyAddMessages(projet.getRepertoire());
					activitesView.appendTxtArea(bundleProjetController.getString("txt_Liste") + " " + nomListe + " "
							+ bundleProjetController.getString("txt_AjoutListeSucces") + "\n");
					activitesView.setLabelProgress(bundleProjetController.getString("txt_AjoutListe"));
				} else {
					activitesView.setLabelProgress("");
					activitesView.resetProgress();
				}
				activitesView.getProgressBar().setIndeterminate(false);
				activitesView.getProgressBar().setStringPainted(true);
			}
		}.execute();
	}

	public void notifyProjetQuit() {
		new SwingWorker<Object, Object>() {
			@Override
			public Object doInBackground() {
				mainFrame.dispose();
				return null;
			}

			@Override
			public void done() {
				System.exit(0);
			}
		}.execute();

	}

	public void notifyMessageToShowSelected() {
		// System.out
		// .println("ProjetController - notifyMessageToShowSelected : identifiant = "
		// + identifiant);
		mainFrame.getOnglet().setSelectedIndex(0);
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // IMPORT - EXPORT // // // // // // // // // // // //

	public void notifyExportListes() {
		new SwingWorker<Object, Object>() {
			ProjetModel newProjet = null;

			@Override
			public Object doInBackground() {
				activitesView.setLabelProgress(bundleProjetController.getString("txt_PatientezExportationListes"));
				activitesView.getProgressBar().setIndeterminate(true);
				activitesView.getProgressBar().setStringPainted(false);
				activitesView.setStepProgress(1);
				activitesView.updateProgress();
				ExportListes exportListes = new ExportListes(projet.getSetListes(), getBundleProjetController());
				exportListes.displayDialog();
				List<ListeModel> listListes = exportListes.getListListes();
				if (listListes != null) {
					CustomJFileChooser customChooser = new CustomJFileChooser("ExportListe", bundleOperationsListe,
							getRepertoire(), true);
					customChooser.show();
					if (customChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						String repertoireSelected = customChooser.getSelectedFile().toString();
						System.out.println(
								"ProjetController - notifyExportListes : repertoireSelected = " + repertoireSelected);
						DateFormat formatterDateFile = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
						String today = formatterDateFile.format(new Date());
						for (ListeModel listeToExport : listListes) {
							System.out.println("ProjetController - notifyExportListes : liste à exporter = "
									+ listeToExport.getNom());
							String nomProjetExporte = listeToExport.getNom() + "_" + "_" + today;
							newProjet = new ProjetModel();
							newProjet.setNom(nomProjetExporte);
							newProjet.setRepertoire(repertoireSelected);
							ListeModel newListe = new ListeModel();
							newListe.exportListe(listeToExport);
							newProjet.addListe(newListe);
							newProjet.setNewCumul();
							newProjet.save();
							activitesView.appendTxtArea(
									bundleProjetController.getString("txt_Projet") + " " + listeToExport.getNom() + " "
											+ bundleProjetController.getString("txt_ProjetExportationSucces") + " "
											+ repertoireSelected + "\n");
						}
						activitesView.setLabelProgress(bundleProjetController.getString("txt_ExporterListes"));
					}
				}
				return null;
			}

			@Override
			public void done() {
				if (newProjet != null) {
					activitesView.getProgressBar().setIndeterminate(false);
					activitesView.getProgressBar().setStringPainted(true);
					JOptionPane.showMessageDialog(null, bundleProjetController.getString("txt_ExportationSucces"),
							bundleProjetController.getString("txt_ExporterListes"), JOptionPane.INFORMATION_MESSAGE);
				} else {
					activitesView.setLabelProgress("");
					activitesView.resetProgress();
				}
				activitesView.getProgressBar().setIndeterminate(false);
				activitesView.getProgressBar().setStringPainted(true);
			}
		}.execute();

	}

	public void notifyImportListes() {
		new SwingWorker<Object, Object>() {
			int oldNbreListes = projet.getNbreListes();
			int nbreListesImportees = 0;

			@Override
			public Object doInBackground() {
				activitesView.setLabelProgress(bundleProjetController.getString("txt_PatientezImportationListes"));
				activitesView.getProgressBar().setIndeterminate(true);
				activitesView.getProgressBar().setStringPainted(false);
				activitesView.setStepProgress(1);
				activitesView.updateProgress();
				OpenProjet openProjet = new OpenProjet(getBundleProjetController());
				openProjet.displayDialog();
				ProjetModel openedProjet = openProjet.getOpenedProjet();
				if (openedProjet != null) {
					int nbreListe = openedProjet.getNbreListes();
					System.out.println("Nombre de listes du projet ouvert = " + nbreListe);
					ImportListes importListes = new ImportListes(openedProjet.getSetListes(),
							getBundleProjetController());
					importListes.displayDialog();
					List<ListeModel> listListes = importListes.getListListes();
					if (listListes != null) {
						nbreListesImportees = listListes.size();
						for (ListeModel liste : listListes) {
							System.out.println("Liste à ajouter : " + liste.getNom());
							int newNumeroListe = 1;
							if (projet.getSetListes().size() > 0)
								newNumeroListe = ((SortedSet<ListeModel>) projet.getSetListes()).last().getNumero() + 1;
							liste.setNumero(newNumeroListe);
							projet.addListe(liste);
							notifyListeSelected(newNumeroListe);
							getListeController().notifyExtractData();
							projet.setNewCumul();							
							activitesView.appendTxtArea(
									bundleProjetController.getString("txt_Liste") + " " + liste.getNom() + " "
											+ bundleProjetController.getString("txt_ListeImportationSucces") + "\n");
						}
						activitesView.setLabelProgress(bundleProjetController.getString("txt_ImporterListes"));
					}
				}
				return null;
			}

			@Override
			public void done() {
				int newNbreListes = projet.getNbreListes();
				if (newNbreListes == (oldNbreListes + nbreListesImportees) && newNbreListes > oldNbreListes) {
					activitesView.getProgressBar().setIndeterminate(false);
					activitesView.getProgressBar().setStringPainted(true);
					JOptionPane.showMessageDialog(null, bundleProjetController.getString("txt_ImportationSucces"),
							bundleProjetController.getString("txt_ImporterListes"), JOptionPane.INFORMATION_MESSAGE);
				} else {
					activitesView.setLabelProgress("");
					activitesView.resetProgress();
				}
				activitesView.getProgressBar().setIndeterminate(false);
				activitesView.getProgressBar().setStringPainted(true);
			}
		}.execute();

	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // SUPPRESSION - FUSION // // // // // // // // // //

	public void notifyRemoveListe() {
		new SwingWorker<Object, Object>() {
			int oldNbreListes = projet.getNbreListes();
			int nbreListesSupprimees = 0;

			@Override
			public Object doInBackground() {
				activitesView.setLabelProgress(bundleProjetController.getString("txt_PatientezSuppressionListes"));
				activitesView.getProgressBar().setIndeterminate(true);
				activitesView.getProgressBar().setStringPainted(false);
				activitesView.setStepProgress(1);
				activitesView.updateProgress();
				RemoveListes removeListe = new RemoveListes(bundleProjetController, getSetListes());
				removeListe.displayDialog();
				List<Integer> listNumListes = removeListe.getListNumListes();
				if (listNumListes != null) {
					nbreListesSupprimees = listNumListes.size();
					for (Integer i : listNumListes) {
						ListeModel listeToRemove = getListeFromNum(i);
						activitesView.appendTxtArea(
								bundleProjetController.getString("txt_Liste") + " " + listeToRemove.getNom() + " "
										+ bundleProjetController.getString("txt_ListeSupprimerSucces") + "\n");
						projet.removeListe(listeToRemove);
					}
					activitesView.setLabelProgress(bundleProjetController.getString("txt_SupprimerListes"));
				}
				return null;
			}

			@Override
			public void done() {
				int newNbreListes = projet.getNbreListes();
				if (newNbreListes == (oldNbreListes - nbreListesSupprimees) && newNbreListes < oldNbreListes) {
					activitesView.getProgressBar().setIndeterminate(false);
					activitesView.getProgressBar().setStringPainted(true);
					JOptionPane.showMessageDialog(null, bundleProjetController.getString("txt_SuppressionSucces"),
							bundleProjetController.getString("txt_SupprimerListes"), JOptionPane.INFORMATION_MESSAGE);
				} else {
					activitesView.setLabelProgress("");
					activitesView.resetProgress();
				}
				activitesView.getProgressBar().setIndeterminate(false);
				activitesView.getProgressBar().setStringPainted(true);
			}
		}.execute();

	}

	public void notifyMergeListes() {
		new SwingWorker<Object, Object>() {
			int oldNbreListes = projet.getNbreListes();
			int nbreListesAjoutee = 0;
			int nbreListeSupprimee = 0;

			@Override
			public Object doInBackground() {
				activitesView.setLabelProgress(bundleProjetController.getString("txt_PatientezFusionListes"));
				activitesView.getProgressBar().setIndeterminate(true);
				activitesView.getProgressBar().setStringPainted(false);
				activitesView.setStepProgress(1);
				activitesView.updateProgress();
				MergeListes mergeListes = new MergeListes(projet.getSetListes(), bundleProjetController);
				mergeListes.displayDialog();
				List<Integer> listListesSelected = mergeListes.getListListesSelected();
				if (listListesSelected != null) {
					AddListe addListe = new AddListe(bundleProjetController);
					addListe.displayDialog();
					final String nomListe = addListe.getNomListe();
					if (nomListe != null) {
						projet.addListe(nomListe);
						nbreListesAjoutee++;
						System.out.println("ProjetController - notifyMergeListes - done() : listeSelected = "
								+ listeSelected.getNom());
						if (listeSelected != null) {
							notifyProjetFusionListes(listListesSelected);
							int diag = JOptionPane.showOptionDialog(null,
									bundleProjetController.getString("txt_SupprimerListesFusionnees") + " ?",
									bundleProjetController.getString("txt_SupprimerListes"), JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE, null, null, null);
							if (diag == JOptionPane.OK_OPTION) {
								int diag2 = JOptionPane.showOptionDialog(null,
										bundleProjetController.getString("txt_PoursuivreSuppressionListesFusionnees")
												+ " ?",
										bundleProjetController.getString("txt_SupprimerListes"),
										JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
								if (diag2 == JOptionPane.OK_OPTION) {
									for (Integer i : listListesSelected) {
										ListeModel listeToRemove = getListeFromNum(i);
										projet.removeListe(listeToRemove);
										nbreListeSupprimee++;
									}
								}
							}
						}
					}
					activitesView.setLabelProgress(bundleProjetController.getString("txt_FusionListes"));
				}
				return null;
			}

			@Override
			public void done() {
				int newNbreListes = projet.getNbreListes();
				if (newNbreListes == (oldNbreListes + nbreListesAjoutee - nbreListeSupprimee)
						&& newNbreListes != oldNbreListes) {
					activitesView.getProgressBar().setIndeterminate(false);
					activitesView.getProgressBar().setStringPainted(true);
					JOptionPane.showMessageDialog(null, bundleProjetController.getString("txt_FusionSucces"),
							bundleProjetController.getString("txt_FusionListes"), JOptionPane.INFORMATION_MESSAGE);
					listeController.notifyExtractData();
					listeController.notifyAnalyseData();
				} else {
					activitesView.setLabelProgress("");
					activitesView.resetProgress();
				}
				activitesView.getProgressBar().setIndeterminate(false);
				activitesView.getProgressBar().setStringPainted(true);
			}
		}.execute();

	}

	public void notifyProjetFusionListes(List<Integer> listNumListes) {
		List<ListeModel> listListesToMerge = new ArrayList<ListeModel>();
		for (int i = 0; i < listNumListes.size(); i++) {
			ListeModel listeToMerge = projet.getListe(listNumListes.get(i));
			listListesToMerge.add(listeToMerge);
		}
		listeController.notifyAddMapIdMessagesFromListes(listListesToMerge);
		projet.setNewCumul();
	}

	public void notifyProjetCreateListFromSelectedMessages(final ListeModel fromListe,
			final List<String> fromListSelectedIdMessages) {

		new SwingWorker<Object, Object>() {

			String nomListe = null;
			int oldNbreListe = projet.getNbreListes();

			@Override
			public Object doInBackground() {
				activitesView.setLabelProgress(bundleProjetController.getString("txt_PatientezAjoutListe"));
				activitesView.getProgressBar().setIndeterminate(true);
				activitesView.getProgressBar().setStringPainted(false);
				activitesView.setStepProgress(1);
				activitesView.updateProgress();

				AddListe addListe = new AddListe(bundleProjetController);
				addListe.displayDialog();
				nomListe = addListe.getNomListe();
				if (nomListe != null) {
					projet.addListe(nomListe);
					// ListeModel listeSelected = projet.getListeSelected();
					Map<String, MessageModel> mapNew = new TreeMap<String, MessageModel>();
					for (String idMessageToAdd : fromListSelectedIdMessages) {
						if (fromListe.getMapIdMessage().containsKey(idMessageToAdd)) {
							MessageModel messageToAdd = fromListe.getMapIdMessage().get(idMessageToAdd);
							mapNew.put(messageToAdd.getIdentifiant(), messageToAdd);
						}
					}
					if (mapNew.size() > 0) {
						getListeController().notifyAddMapIdMessages(mapNew);
						projet.setNewCumul();
					}
				}
				return null;
			}

			@Override
			public void done() {
				int newNbreListe = projet.getNbreListes();
				if (newNbreListe == oldNbreListe + 1) {
					activitesView.appendTxtArea(bundleProjetController.getString("txt_Liste") + " " + nomListe + " "
							+ bundleProjetController.getString("txt_AjoutListeSucces") + "\n");
					activitesView.setLabelProgress(bundleProjetController.getString("txt_AjoutListe"));
					getListeController().notifyExtractData();
					getListeController().notifyAnalyseData();
				} else {
					activitesView.setLabelProgress("");
					activitesView.resetProgress();
				}
				activitesView.getProgressBar().setIndeterminate(false);
				activitesView.getProgressBar().setStringPainted(true);
			}
		}.execute();
	}

	public void notifyModifyNomListe(String oldName) {
		ModifyNomListe modifyNomListe = new ModifyNomListe(oldName, bundleOperationsListe);
		modifyNomListe.displayDialog();
		String newNomListe = modifyNomListe.getNewName();
		if (newNomListe != null) {
			listeController.notifyListeNameChanged(newNomListe);
			projet.save();
		}
	}

	public void notifyProjetCreateListFromJsonForum(ForumModel forum, TreeMap<String, MessageModel> messagesMap) {

		String nomListe = null;
		int oldNbreListe = projet.getNbreListes();

		activitesView.setLabelProgress(bundleProjetController.getString("txt_PatientezAjoutListe"));
		activitesView.getProgressBar().setIndeterminate(true);
		activitesView.getProgressBar().setStringPainted(false);
		activitesView.setStepProgress(1);
		activitesView.updateProgress();

		nomListe = forum.getNom();
		if (nomListe != null) {
			projet.addListe(nomListe, messagesMap);
		}

		int newNbreListe = projet.getNbreListes();
		if (newNbreListe == oldNbreListe + 1) {
			activitesView.appendTxtArea(bundleProjetController.getString("txt_Liste") + " " + nomListe + " "
					+ bundleProjetController.getString("txt_AjoutListeSucces") + "\n");
			activitesView.setLabelProgress(bundleProjetController.getString("txt_AjoutListe"));
		} else {
			activitesView.setLabelProgress("");
			activitesView.resetProgress();
		}
		activitesView.getProgressBar().setIndeterminate(false);
		activitesView.getProgressBar().setStringPainted(true);
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // GET SUR MODEL PROJET // // // // // // // // // //

	public ListeModel getListeFromNum(int numListeRecherche) {
		ListeModel listeToFind = null;
		for (ListeModel listeExistante : projet.getSetListes()) {
			int numListeExistant = listeExistante.getNumero();
			if (numListeExistant == numListeRecherche) {
				listeToFind = listeExistante;
				break;
			}
		}
		return listeToFind;
	}

	public ListeModel getListeSelected() {
		return listeController.getListeSelected();
	}

	public String getNomProjet() {
		return projet.getNom();
	}

	public String getRepertoire() {
		return projet.getRepertoire();
	}

	public Set<ListeModel> getSetListes() {
		return projet.getSetListes();
	}

	public ProjetModel getProjetCharge() {
		return projet;
	}

	public ProjetView getFrameActivites() {
		return activitesView;
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // GETTER // // // // // // // // // // // // // // //

	public ListeController getListeController() {
		return listeController;
	}

	public ProjetController getProjetController() {
		return this;
	}

	public ResourceBundle getBundleProjetController() {
		return bundleProjetController;
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // SETTER // // // // // // // // // // // // // // //

}