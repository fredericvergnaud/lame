package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import modeles.ListeModel;
import modeles.evenements.ProjetChangedEvent;
import modeles.evenements.ProjetListeAddedEvent;
import controleurs.ListeController;
import controleurs.ProjetController;
import controleurs.vuesabstraites.ProjetView;

public class MenuBar extends ProjetView implements ActionListener {

	private ResourceBundle menus;

	public MenuBar(ProjetController projetController, ResourceBundle menus) {
		super(projetController);
		this.menus = menus;
	}

	// CREATION DE LA BARRE DE MENU
	@Override
	public JMenuBar getMenuBar() {

		JMenuBar menuBar = new JMenuBar();

		// MENU PROJET
		JMenu menuProjet = new JMenu(menus.getString("txt_Projet"));
		menuProjet.setName("menuProjet");
		menuBar.add(menuProjet);

		JMenuItem itemProjetNouveau = new JMenuItem(menus.getString("txt_Nouveau"));
		itemProjetNouveau.setName("projetNouveau");
		itemProjetNouveau.addActionListener(this);
		menuProjet.add(itemProjetNouveau);

		JMenuItem itemProjetOuvrir = new JMenuItem(menus.getString("txt_Ouvrir"));
		itemProjetOuvrir.setName("projetOuvrir");
		itemProjetOuvrir.addActionListener(this);
		menuProjet.add(itemProjetOuvrir);

		menuProjet.addSeparator();

		JMenuItem itemProjetAjouterListe = new JMenuItem(menus.getString("txt_AjouterListe"));
		itemProjetAjouterListe.setName("projetAjouterListe");
		itemProjetAjouterListe.addActionListener(this);
		menuProjet.add(itemProjetAjouterListe);

		JMenuItem itemProjetImporterListes = new JMenuItem(menus.getString("txt_ImporterListe"));
		itemProjetImporterListes.setName("projetImporterListe");
		itemProjetImporterListes.addActionListener(this);
		menuProjet.add(itemProjetImporterListes);

		JMenuItem itemProjetExporterListes = new JMenuItem(menus.getString("txt_ExporterListe"));
		itemProjetExporterListes.setName("projetExporterListe");
		itemProjetExporterListes.addActionListener(this);
		menuProjet.add(itemProjetExporterListes);

		menuProjet.addSeparator();

		JMenuItem itemProjetSupprimerListe = new JMenuItem(menus.getString("txt_SupprimerListe"));
		itemProjetSupprimerListe.setName("projetSupprimerListe");
		itemProjetSupprimerListe.addActionListener(this);
		menuProjet.add(itemProjetSupprimerListe);

		JMenuItem itemProjetMergeListe = new JMenuItem(menus.getString("txt_FusionnerListe"));
		itemProjetMergeListe.setName("projetFusionnerListe");
		itemProjetMergeListe.addActionListener(this);
		menuProjet.add(itemProjetMergeListe);

		menuProjet.addSeparator();

		JMenuItem itemProjetQuitter = new JMenuItem(menus.getString("txt_Quitter"));
		itemProjetQuitter.setName("projetQuitter");
		itemProjetQuitter.addActionListener(this);
		menuProjet.add(itemProjetQuitter);

		// MENU LISTE
		JMenu menuListe = new JMenu(menus.getString("txt_Liste"));
		menuListe.setName("menuListe");
		menuBar.add(menuListe);

		JMenuItem itemListeScinder = new JMenuItem(menus.getString("txt_ScinderListe"));
		itemListeScinder.setName("listeScinder");
		itemListeScinder.addActionListener(this);
		menuListe.add(itemListeScinder);

		menuListe.addSeparator();

		JMenuItem itemListeAjouterMessages = new JMenuItem(menus.getString("txt_AjouterMessages"));
		itemListeAjouterMessages.setName("listeAjouterMessages");
		itemListeAjouterMessages.addActionListener(this);
		menuListe.add(itemListeAjouterMessages);

		JMenuItem itemListeRegrouperMessages = new JMenuItem(menus.getString("txt_RegrouperMessages"));
		itemListeRegrouperMessages.setName("listeRegrouperMessages");
		itemListeRegrouperMessages.addActionListener(this);
		menuListe.add(itemListeRegrouperMessages);

		JMenuItem itemListeCalculerConversations = new JMenuItem(menus.getString("txt_Analyse"));
		itemListeCalculerConversations.setName("listeAnalyser");
		itemListeCalculerConversations.addActionListener(this);
		menuListe.add(itemListeCalculerConversations);

		// JMenuItem itemListeCartographier = new JMenuItem("Cartographier");
		// itemListeCartographier.setName("listeCartographier");
		// itemListeCartographier.addActionListener(this);
		// menuListe.add(itemListeCartographier);

		JMenuItem itemListeCartographier = new JMenuItem(menus.getString("txt_Cartographier"));
		itemListeCartographier.setName("listeCartographier");
		itemListeCartographier.addActionListener(this);
		menuListe.add(itemListeCartographier);

		// MENU MESSAGES
		JMenu menuMessages = new JMenu(menus.getString("txt_Messages"));
		menuMessages.setName("menuMessages");
		menuBar.add(menuMessages);

		JMenu menuMessagesNettoyer = new JMenu(menus.getString("txt_Nettoyer"));
		menuMessagesNettoyer.setName("menuMessagesNettoyer");
		menuMessages.add(menuMessagesNettoyer);

		JMenu menuMessagesNettoyerLocuteurs = new JMenu(menus.getString("txt_Locuteurs"));
		menuMessagesNettoyerLocuteurs.setName("menuMessagesNettoyerLocuteurs");
		menuMessagesNettoyer.add(menuMessagesNettoyerLocuteurs);

		JMenuItem itemMessagesNettoyerLocuteursAuto = new JMenuItem(menus.getString("txt_Automatiquement"));
		itemMessagesNettoyerLocuteursAuto.setName("messagesNettoyerLocuteursAuto");
		itemMessagesNettoyerLocuteursAuto.addActionListener(this);
		menuMessagesNettoyerLocuteurs.add(itemMessagesNettoyerLocuteursAuto);

		JMenuItem itemMessagesNettoyerLocuteursManuel = new JMenuItem(menus.getString("txt_Manuellement"));
		itemMessagesNettoyerLocuteursManuel.setName("messagesNettoyerLocuteursManuel");
		itemMessagesNettoyerLocuteursManuel.addActionListener(this);
		menuMessagesNettoyerLocuteurs.add(itemMessagesNettoyerLocuteursManuel);

		JMenuItem itemMessagesNettoyerLocuteursFichier = new JMenuItem(menus.getString("txt_ChargerFichierCorrespondance"));
		itemMessagesNettoyerLocuteursFichier.setName("messagesNettoyerLocuteursFichier");
		itemMessagesNettoyerLocuteursFichier.addActionListener(this);
		menuMessagesNettoyerLocuteurs.add(itemMessagesNettoyerLocuteursFichier);

		JMenu menuMessagesNettoyerSujets = new JMenu(menus.getString("txt_Sujets"));
		menuMessagesNettoyerSujets.setName("menuMessagesNettoyerSujets");
		menuMessagesNettoyer.add(menuMessagesNettoyerSujets);

		JMenuItem itemMessagesNettoyerSujetsAuto = new JMenuItem(menus.getString("txt_Automatiquement"));
		itemMessagesNettoyerSujetsAuto.setName("messagesNettoyerSujetsAuto");
		itemMessagesNettoyerSujetsAuto.addActionListener(this);
		menuMessagesNettoyerSujets.add(itemMessagesNettoyerSujetsAuto);

		JMenuItem itemMessagesNettoyerSujetsManuel = new JMenuItem(menus.getString("txt_Manuellement"));
		itemMessagesNettoyerSujetsManuel.setName("messagesNettoyerSujetsManuel");
		itemMessagesNettoyerSujetsManuel.addActionListener(this);
		menuMessagesNettoyerSujets.add(itemMessagesNettoyerSujetsManuel);

		// // Affichage
		JMenu menuAffichage = new JMenu(menus.getString("txt_Affichage"));
		menuAffichage.setName("menuAffichage");
		menuBar.add(menuAffichage);

		JMenuItem itemAffichageActivites = new JMenuItem(menus.getString("txt_FenetreActivites"));
		itemAffichageActivites.setName("affichageActivites");
		itemAffichageActivites.addActionListener(this);
		menuAffichage.add(itemAffichageActivites);

		return menuBar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem) (e.getSource());
		String nomItem = source.getName();

		// NOUVEAU PROJET
		if (nomItem == "projetNouveau") {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle(menus.getString("txt_NouveauProjet"));
			int returnVal = fc.showSaveDialog(fc.getParent());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String nomFichier = file.getName();
				String repertoire = file.getPath();
				getProjetController().notifyCreateNewProjet(nomFichier, repertoire);
			}
		}

		else if (nomItem == "projetOuvrir") {
			getProjetController().notifyOpenProjet();
		}

		else if (nomItem.equals("projetQuitter")) {
			getProjetController().notifyProjetQuit();
		}

		else if (nomItem == "projetAjouterListe") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				getProjetController().notifyAddListe();
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "projetExporterListe") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				if (getProjetController().getProjetCharge().getSetListes().size() > 0)
					getProjetController().notifyExportListes();
				else
					JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetVide"), "Information", JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "projetImporterListe") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				getProjetController().notifyImportListes();
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "listeAjouterMessages") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				ListeController listeController = getProjetController().getListeController();
				if (listeController != null) {
					int numListe = listeController.getListeSelected().getNumero();
					// System.out
					// .println("MenuBar - listeAjouterMessages : numListe = "
					// + numListe);
					if (numListe != 0) {
						listeController.notifyAddMessages(getProjetController().getRepertoire());
					} else
						JOptionPane.showMessageDialog(null, menus.getString("txt_ListeNonSelectionnee"), "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "listeRegrouperMessages") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				ListeModel listeSelected = getProjetController().getListeSelected();
				if (listeSelected != null)
					if (listeSelected.getNumero() != 0) {
						if (listeSelected.getNbreMessages() != 0) {
							getProjetController().getListeController().notifyRegroupeMessages();
						} else
							JOptionPane.showMessageDialog(null, menus.getString("txt_ListeVide"), "Information", JOptionPane.INFORMATION_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null, menus.getString("txt_ListeNonSelectionnee"), "Information", JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "projetSupprimerListe") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				if (getProjetController().getSetListes().size() > 0)
					getProjetController().notifyRemoveListe();
				else
					JOptionPane.showMessageDialog(null, getProjetController().getBundleProjetController().getString("txt_SuppressionImpossible"), getProjetController().getBundleProjetController()
							.getString("txt_SupprimerListes"), JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "projetFusionnerListe") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				getProjetController().notifyMergeListes();
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "listeScinder") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				ListeModel listeSelected = getProjetController().getListeSelected();
				if (listeSelected != null)
					if (listeSelected.getNumero() != 0) {
						if (listeSelected.getNbreMessages() != 0) {
							getProjetController().getListeController().notifyListeSplit();
						} else
							JOptionPane.showMessageDialog(null, menus.getString("txt_ListeVide"), "Information", JOptionPane.INFORMATION_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null, menus.getString("txt_ListeNonSelectionnee"), "Information", JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "listeAnalyser") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				ListeModel listeSelected = getProjetController().getListeSelected();
				if (listeSelected != null)
					if (listeSelected.getNumero() != 0) {
						if (listeSelected.getNbreMessages() != 0) {
							System.out.println("MenuBar - listeAnalyser : listeController = " + getProjetController().getListeController() + " | nomListe = "
									+ getProjetController().getListeController().getNom());
							getProjetController().getListeController().notifyAnalyseData();
						} else
							JOptionPane.showMessageDialog(null, menus.getString("txt_ListeVide"), "Information", JOptionPane.INFORMATION_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null, menus.getString("txt_ListeNonSelectionnee"), "Information", JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		// else if (nomItem == "listeCartographier") {
		// if (getProjetController().getProjetCharge() != null &&
		// getProjetController().getProjetCharge().getNom() != null) {
		// ListeModel listeSelected = getProjetController().getListeSelected();
		// if (listeSelected != null)
		// if (listeSelected.getNumero() != 0) {
		// if (listeSelected.getNbreMessages() != 0) {
		// if (listeSelected.getNbreConversations() != 0) {
		// getProjetController().getListeController().notifyCartographie();
		// } else
		// JOptionPane.showMessageDialog(null,
		// menus.getString("txt_AnalyseNonEffectuee"), "Information",
		// JOptionPane.INFORMATION_MESSAGE);
		// } else
		// JOptionPane.showMessageDialog(null, menus.getString("txt_ListeVide"),
		// "Information", JOptionPane.INFORMATION_MESSAGE);
		// } else
		// JOptionPane.showMessageDialog(null,
		// menus.getString("txt_ListeNonSelectionnee"), "Information",
		// JOptionPane.INFORMATION_MESSAGE);
		// } else
		// JOptionPane.showMessageDialog(null,
		// menus.getString("txt_ProjetNonCharge"), "Information",
		// JOptionPane.INFORMATION_MESSAGE);
		//
		// }
		else if (nomItem == "listeCartographier") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				ListeModel listeSelected = getProjetController().getListeSelected();
				if (listeSelected != null)
					if (listeSelected.getNumero() != 0) {
						if (listeSelected.getNbreMessages() != 0) {
							if (listeSelected.getNbreConversations() > 0 && listeSelected.getNbreLocuteurs() > 0) {
								if (listeSelected.getNbreLocuteurs() > 1)
									getProjetController().getListeController().notifyCartographie();
								else
									JOptionPane.showMessageDialog(null, menus.getString("txt_PasAssezLocuteurs"), "Information", JOptionPane.INFORMATION_MESSAGE);
							} else
								JOptionPane.showMessageDialog(null, menus.getString("txt_AnalyseNonEffectuee"), "Information", JOptionPane.INFORMATION_MESSAGE);
						} else
							JOptionPane.showMessageDialog(null, menus.getString("txt_ListeVide"), "Information", JOptionPane.INFORMATION_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null, menus.getString("txt_ListeNonSelectionnee"), "Information", JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "messagesNettoyerLocuteursAuto") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				ListeModel listeSelected = getProjetController().getListeSelected();
				if (listeSelected != null)
					if (listeSelected.getNumero() != 0) {
						getProjetController().getListeController().notifyNettoyage("locuteursAuto");
					} else
						JOptionPane.showMessageDialog(null, menus.getString("txt_ListeNonSelectionnee"), "Information", JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "messagesNettoyerLocuteursManuel") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				ListeModel listeSelected = getProjetController().getListeSelected();
				if (listeSelected != null)
					if (listeSelected.getNumero() != 0) {
						getProjetController().getListeController().notifyNettoyage("locuteursManuel");
					} else
						JOptionPane.showMessageDialog(null, menus.getString("txt_ListeNonSelectionnee"), "Information", JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "messagesNettoyerLocuteursFichier") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				ListeModel listeSelected = getProjetController().getListeSelected();
				if (listeSelected != null)
					if (listeSelected.getNumero() != 0) {
						getProjetController().getListeController().notifyNettoyage("locuteursFichier");
					} else
						JOptionPane.showMessageDialog(null, menus.getString("txt_ListeNonSelectionnee"), "Information", JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "messagesNettoyerSujetsAuto") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				ListeModel listeSelected = getProjetController().getListeSelected();
				if (listeSelected != null)
					if (listeSelected.getNumero() != 0) {
						getProjetController().getListeController().notifyNettoyage("sujetsAuto");
					} else
						JOptionPane.showMessageDialog(null, menus.getString("txt_ListeNonSelectionnee"), "Information", JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "messagesNettoyerSujetsManuel") {
			if (getProjetController().getProjetCharge() != null && getProjetController().getProjetCharge().getNom() != null) {
				ListeModel listeSelected = getProjetController().getListeSelected();
				if (listeSelected != null)
					if (listeSelected.getNumero() != 0) {
						getProjetController().getListeController().notifyNettoyage("sujetsManuel");
					} else
						JOptionPane.showMessageDialog(null, menus.getString("txt_ListeNonSelectionnee"), "Information", JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showMessageDialog(null, menus.getString("txt_ProjetNonCharge"), "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (nomItem == "affichageActivites") {
			getProjetController().getFrameActivites().showActivites();
		}

	}

	@Override
	public JPanel getInfosProjetPanel() {
		return null;
	}

	@Override
	public JPanel getListesListePanel() {
		return null;
	}

	@Override
	public void setLabelProgress(String string) {
	}

	@Override
	public void showActivites() {
	}

	@Override
	public void updateProgress() {
	}

	@Override
	public void setStepProgress(int step) {
	}

	@Override
	public void close() {
	}

	@Override
	public void resetProgress() {
	}

	@Override
	public void appendTxtArea(String txt) {
	}

	@Override
	public JProgressBar getProgressBar() {
		return null;
	}

	// @Override
	// public void projetCreated(ProjetCreatedEvent event) {
	//
	// }
	//
	// @Override
	// public void projetListeAdded(ProjetListeAddedEvent event) {
	// }
	//
	// @Override
	// public void projetListeSelected(ProjetListeSelectedEvent event) {
	// }

	@Override
	public void projetChanged(ProjetChangedEvent event) {
	}

	@Override
	public void projetListeAdded(ProjetListeAddedEvent event) {
		// TODO Auto-generated method stub

	}

}
