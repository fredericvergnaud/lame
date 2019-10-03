package extra.nettoyage;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import extra.LocuteurPourNettoyage;
import modeles.MessageModel;
import modeles.tableaux.TabNettoyageLocuteursMessagesModel;
import vue.dialog.DialogPanelModificationLocuteursMessages;

public class NomLocuteurCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	private static final long serialVersionUID = 1L;
	private String newNom;
	private JButton bouton;
	private JTable table = new JTable();
	private int row;
	private TableModel model;
	private int colGP = 2, colP = 3, colM = 4;
	private int nbreLigne;
	private List<MessageModel> listMessages;
	private int nbreModifsTotal, colSelect;
	private ResourceBundle bundleOperationsListe;

	public NomLocuteurCellEditor(List<MessageModel> listMessages, int nbreModifsTotal, int colSelect, ResourceBundle bundleOperationsListe) {
		super();
		this.listMessages = listMessages;
		this.nbreModifsTotal = nbreModifsTotal;
		this.colSelect = colSelect;
		this.bundleOperationsListe = bundleOperationsListe;
		bouton = new JButton(bundleOperationsListe.getString("txt_Modification") + " ...");
		bouton.setActionCommand("change");
		bouton.addActionListener(this);
		bouton.setBorderPainted(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("change")) {
			DialogPanelModificationLocuteursMessages diag = new DialogPanelModificationLocuteursMessages(newNom, colSelect, bundleOperationsListe);
			int result = JOptionPane.showOptionDialog(null, diag.getPanel(), bundleOperationsListe.getString("txt_NettoyageLocuteurs"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, null, null);
			int nbreModifs = 0;

			if (result == JOptionPane.OK_OPTION) {
				System.out.println("nbreModifs = " + nbreModifs + " | nbreModifsTotal = " + nbreModifsTotal);

				// Recuperation des infos du dialog
				String selection = diag.getSelection();
				System.out.println("selection = " + selection);
				newNom = diag.getNewNom();
				String ancienNom = diag.getAncienNom();
				String sIndefini = bundleOperationsListe.getString("txt_Indefini");
				String sVide = "";
				String sPublic = "public";

				// Recuperation de la s�lection
				int viewRow = table.getSelectedRow();
				int modelRow = table.convertRowIndexToModel(viewRow);
				// System.out.println("row selected du model = " + modelRow);
				String mSelect = (String) model.getValueAt(modelRow, colM);
				String gPSelect = (String) model.getValueAt(modelRow, colGP);
				String pSelect = (String) model.getValueAt(modelRow, colP);

				if (selection.equals("nom_seul")) {
					// TabNettoyageLocuteursMessagesModel newModel =
					// getNewModel(
					// ancienNom, newNom, newGp, "", "");
					for (MessageModel message : listMessages) {
						String nomL = message.getExpediteur();
						if (nomL.equals(ancienNom)) {
							// message.setExpediteur(newNom);
							nbreModifs++;
						}
					}
					TabNettoyageLocuteursMessagesModel newModel = getNewModel(ancienNom, newNom, "", 1);
					table.setModel(newModel);

				} else if (selection.equals("meme_mail")) {
					try {
						if (sIndefini.equals(mSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurMailIndefini"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else if (sVide.equals(mSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurMailVide"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else if (sPublic.equals(mSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurMailPublic"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else {
							for (MessageModel message : listMessages) {
								String mailL = message.getMail();
								if (mailL.equals(mSelect)) {
									// message.setExpediteur(newNom);
									nbreModifs++;
								}
							}
							TabNettoyageLocuteursMessagesModel newModel = getNewModel(ancienNom, newNom, mSelect, 4);
							table.setModel(newModel);
						}
					} catch (NullPointerException ex) {
						JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurMailVide"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
					}
				} else if (selection.equals("meme_profil")) {
					try {
						String profilSelect = (String) model.getValueAt(row, colM);
						System.out.println("profilSelect = " + profilSelect);
						if (sIndefini.equals(profilSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurProfilYahooIndefini"), bundleOperationsListe.getString("txt_Erreur"),
									JOptionPane.ERROR_MESSAGE);
						} else if (sVide.equals(profilSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurProfilYahooVide"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else if (sPublic.equals(profilSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurProfilYahooPublic"), bundleOperationsListe.getString("txt_Erreur"),
									JOptionPane.ERROR_MESSAGE);
						} else {
							for (MessageModel message : listMessages) {
								String profilL = message.getProfilYahoo();
								if (profilL.equals(profilSelect)) {
									// message.setExpediteur(newNom);
									nbreModifs++;
								}
							}
							TabNettoyageLocuteursMessagesModel newModel = getNewModel(ancienNom, newNom, profilSelect, 3);
							table.setModel(newModel);
						}
					} catch (NullPointerException ex) {
						JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurProfilYahooVide"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
					}

				} else if (selection.equals("meme_group")) {
					try {
						if (sIndefini.equals(gPSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurGPYahooIndefini"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else if (sVide.equals(gPSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurGPYahooVide"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else if (sPublic.equals(gPSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurGPYahooPublic"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else {
							for (MessageModel message : listMessages) {
								String gPL = message.getGroupPostYahoo();
								if (gPL.equals(gPSelect)) {
									// message.setExpediteur(newNom);
									nbreModifs++;
								}
							}
							TabNettoyageLocuteursMessagesModel newModel = getNewModel(ancienNom, newNom, gPSelect, 2);
							table.setModel(newModel);
						}
					} catch (NullPointerException ex) {
						JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurGPYahooVide"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
					}

				} else if (selection.equals("ligne_nom_seul")) {
					for (MessageModel message : listMessages) {
						String nomL = message.getExpediteur();
						if (nomL.equals(ancienNom)) {
							// message.setExpediteur(newNom);
							// message.setMail(mSelect);
							// message.setGroupPostYahoo(gPSelect);
							// message.setProfilYahoo(pSelect);
							nbreModifs++;
						}
					}
					TabNettoyageLocuteursMessagesModel newModel = getNewModel(ancienNom, newNom, gPSelect, pSelect, mSelect, 1);
					table.setModel(newModel);
				} else if (selection.equals("ligne_meme_mail")) {
					try {

						if (sIndefini.equals(mSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurMailIndefini"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else if (sVide.equals(mSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurMailVide"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else if (sPublic.equals(mSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurMailPublic"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else {
							for (MessageModel message : listMessages) {
								String mailL = message.getMail();
								if (mailL.equals(mSelect)) {
									// message.setExpediteur(newNom);
									// message.setGroupPostYahoo(gPSelect);
									// message.setProfilYahoo(pSelect);
									nbreModifs++;
								}
							}
							TabNettoyageLocuteursMessagesModel newModel = getNewModel(ancienNom, newNom, gPSelect, pSelect, mSelect, 4);
							table.setModel(newModel);
						}
					} catch (NullPointerException ex) {
						JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurMailVide"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
					}

				} else if (selection.equals("ligne_meme_profil")) {
					try {
						// System.out.println("nouveau nom = " + nouveauNom);
						// String profilLocuteur = (String)
						// table.getValueAt(row,
						// 2);
						// System.out.println("profil ds la clause where = "
						// + profilLocuteur);

						if (sIndefini.equals(pSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurProfilYahooIndefini"), bundleOperationsListe.getString("txt_Erreur"),
									JOptionPane.ERROR_MESSAGE);
						} else if (sVide.equals(pSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurProfilYahooVide"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else if (sPublic.equals(pSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurProfilYahooPublic"), bundleOperationsListe.getString("txt_Erreur"),
									JOptionPane.ERROR_MESSAGE);
						} else {
							for (MessageModel message : listMessages) {
								String pL = message.getProfilYahoo();
								if (pL.equals(pSelect)) {
									// message.setExpediteur(newNom);
									// message.setMail(mSelect);
									// message.setGroupPostYahoo(gPSelect);
									nbreModifs++;
								}
							}
							TabNettoyageLocuteursMessagesModel newModel = getNewModel(ancienNom, newNom, gPSelect, pSelect, mSelect, 3);
							table.setModel(newModel);
						}
					} catch (NullPointerException ex) {
						JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurProfilYahooVide"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
					}

				} else if (selection.equals("ligne_meme_group")) {
					System.out.println("mailSelect = " + mSelect + " | gPSelect = " + gPSelect + " | pSelect = " + pSelect + " | sIndefini = " + sIndefini + " | sVide = " + sVide + " | sPublic = "
							+ sPublic);

					try {
						if (sIndefini.equals(gPSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurGPYahooIndefini"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else if (sVide.equals(gPSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurGPYahooVide"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else if (sPublic.equals(gPSelect)) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurGPYahooPublic"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						} else {
							for (MessageModel message : listMessages) {
								String gPL = message.getGroupPostYahoo();
								System.out.println("gPL = " + gPL);
								if (gPL.equals(gPSelect)) {
									// message.setExpediteur(newNom);
									// message.setMail(mSelect);
									// message.setProfilYahoo(pSelect);
									nbreModifs++;
								}
							}
							TabNettoyageLocuteursMessagesModel newModel = getNewModel(ancienNom, newNom, gPSelect, pSelect, mSelect, 2);
							table.setModel(newModel);
						}
					} catch (NullPointerException ex) {
						JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ValeurGPYahooVide"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
					}
				}

				// table.changeSelection(model.getRowCount() + 1, 0, false,
				// false);
				// System.out.println("nbreModifs pour la selection = "
				// + nbreModifs);
				nbreModifsTotal += nbreModifs;
				// System.out
				// .println("nbreModifsTotal passe e " + nbreModifsTotal);
				fireEditingStopped();
				JOptionPane.showMessageDialog(null, nbreModifs + " " + bundleOperationsListe.getString("txt_ModificationsEffectuees"), bundleOperationsListe.getString("txt_NettoyageLocuteurs"),
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	@Override
	public Object getCellEditorValue() {
		// newListLocuteurs = new ArrayList<LocuteurModel>();
		// TabNettoyageLocuteursMessagesModel newModel =
		// (TabNettoyageLocuteursMessagesModel) table
		// .getModel();
		// List<LocuteurPourNettoyage> listLocuteursPourNettoyage = new
		// ArrayList<LocuteurPourNettoyage>(newModel
		// .getSetLocuteursPourNettoyage());
		// for (int i = 0; i < nbreLigne; i++) {
		// LocuteurPourNettoyage locuteurNettoye = listLocuteursPourNettoyage
		// .get(i);
		// String newNom = locuteurNettoye.getNouveauNom();
		// Set<String> newSetGp = new HashSet<String>();
		// newSetGp.add(locuteurNettoye.getGroupPost());
		// Set<String> newSetP = new HashSet<String>();
		// newSetP.add(locuteurNettoye.getProfil());
		// Set<String> newSetM = new HashSet<String>();
		// newSetM.add(locuteurNettoye.getMail());
		// LocuteurModel newLocuteur = new LocuteurModel();
		// newLocuteur.setNom(newNom);
		// newLocuteur.setSetGroupPosts(newSetGp);
		// newLocuteur.setSetProfils(newSetP);
		// newLocuteur.setSetMails(newSetM);
		// //System.out.println("newLocuteur = "+newLocuteur.getNom());
		// newSetLocuteurs.add(newLocuteur);
		// }
		// System.out.println("CELLEDITOR - LISTE DES LOCUTEURS PRESENTS ");
		// for (LocuteurModel locuteur : newListLocuteurs)
		// System.out.println(locuteur.getNom());
		return newNom;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
		this.table = table;
		this.model = table.getModel();
		this.nbreLigne = model.getRowCount();
		this.row = row;
		newNom = (String) value;
		return bouton;
	}

	// public TabNettoyageLocuteursMessagesModel getNewModel(String ancienNom,
	// String newNom) {
	// // table.getModel().setValueAt(ancienNom, 0, 0);
	// TabNettoyageLocuteursMessagesModel newModel =
	// (TabNettoyageLocuteursMessagesModel) table
	// .getModel();
	// List<LocuteurPourNettoyage> listLocuteursPourNettoyage = new
	// ArrayList<LocuteurPourNettoyage>(
	// newModel.getSetLocuteursPourNettoyage());
	// // System.out.println("valeur de ancienNom = " + ancienNom);
	// for (int i = 0; i < nbreLigne; i++) {
	// // String val = (String) table.getValueAt(i, colAncienNom);
	// // System.out.println("valeur de val = "+val);
	// LocuteurPourNettoyage locuteurSelected = listLocuteursPourNettoyage
	// .get(i);
	// if (locuteurSelected.getAncienNom().equals(ancienNom)) {
	// locuteurSelected.setAncienNom(newNom);
	// locuteurSelected.setNouveauNom(newNom);
	// }
	// }
	// return newModel;
	// }
	//
	// public TabNettoyageLocuteursMessagesModel getNewModel(String ancienNom,
	// String newNom, String mailSelect) {
	// // table.getModel().setValueAt(ancienNom, 0, 0);
	// TabNettoyageLocuteursMessagesModel newModel =
	// (TabNettoyageLocuteursMessagesModel) table
	// .getModel();
	// List<LocuteurPourNettoyage> listLocuteursPourNettoyage = new
	// ArrayList<LocuteurPourNettoyage>(
	// newModel.getSetLocuteursPourNettoyage());
	// // System.out.println("valeur de ancienNom = " + ancienNom);
	// for (int i = 0; i < nbreLigne; i++) {
	// // String val = (String) table.getValueAt(i, colAncienNom);
	// // System.out.println("valeur de val = "+val);
	// LocuteurPourNettoyage locuteurSelected = listLocuteursPourNettoyage
	// .get(i);
	// if (locuteurSelected.getMail().equals(mailSelect)) {
	// locuteurSelected.setAncienNom(newNom);
	// locuteurSelected.setNouveauNom(newNom);
	// }
	// }
	// return newModel;
	// }

	public TabNettoyageLocuteursMessagesModel getNewModel(String ancienNom, String newNom, String select, int indexSelect) {
		// table.getModel().setValueAt(ancienNom, 0, 0);
		TabNettoyageLocuteursMessagesModel newModel = (TabNettoyageLocuteursMessagesModel) table.getModel();
		List<LocuteurPourNettoyage> listLocuteursPourNettoyage = new ArrayList<LocuteurPourNettoyage>(newModel.getSetLocuteursPourNettoyage());
		// System.out.println("valeur de ancienNom = " + ancienNom);
		for (int i = 0; i < nbreLigne; i++) {
			// String val = (String) table.getValueAt(i, colAncienNom);
			// System.out.println("valeur de val = "+val);
			LocuteurPourNettoyage locuteurSelected = listLocuteursPourNettoyage.get(i);
			if (indexSelect == 1) {
				if (locuteurSelected.getAncienNom().equals(ancienNom)) {
//					locuteurSelected.setAncienNom(newNom);
					locuteurSelected.setNouveauNom(newNom);
				}
			} else if (indexSelect == 4) {
				String mailSelect = select;
				if (locuteurSelected.getMail().equals(mailSelect)) {
//					locuteurSelected.setAncienNom(newNom);
					locuteurSelected.setNouveauNom(newNom);
				}
			} else if (indexSelect == 3) {
				String profilSelect = select;
				if (locuteurSelected.getProfil().equals(profilSelect)) {
//					locuteurSelected.setAncienNom(newNom);
					locuteurSelected.setNouveauNom(newNom);
				}
			} else if (indexSelect == 2) {
				String gPSelect = select;
				if (locuteurSelected.getGroupPost().equals(gPSelect)) {
//					locuteurSelected.setAncienNom(newNom);
					locuteurSelected.setNouveauNom(newNom);
				}
			}
		}
		// newModel.fireTableRowsUpdated(0, listLocuteursPourNettoyage.size() -
		// 1);
		return newModel;
	}

	public TabNettoyageLocuteursMessagesModel getNewModel(String ancienNom, String newNom, String newGp, String newP, String newM, int indexSelect) {
		// table.getModel().setValueAt(ancienNom, 0, 0);
		TabNettoyageLocuteursMessagesModel newModel = (TabNettoyageLocuteursMessagesModel) table.getModel();
		List<LocuteurPourNettoyage> listLocuteursPourNettoyage = new ArrayList<LocuteurPourNettoyage>(newModel.getSetLocuteursPourNettoyage());
		// System.out.println("valeur de ancienNom = " + ancienNom);
		for (int i = 0; i < nbreLigne; i++) {
			// String val = (String) table.getValueAt(i, colAncienNom);
			// System.out.println("valeur de val = "+val);
			LocuteurPourNettoyage locuteurSelected = listLocuteursPourNettoyage.get(i);
			if (indexSelect == 1) {
				if (locuteurSelected.getAncienNom().equals(ancienNom)) {
//					locuteurSelected.setAncienNom(newNom);
					locuteurSelected.setNouveauNom(newNom);
					locuteurSelected.setGroupPost(newGp);
					locuteurSelected.setProfil(newP);
					locuteurSelected.setMail(newM);
				}
			} else if (indexSelect == 4) {
				if (locuteurSelected.getMail().equals(newM)) {
//					locuteurSelected.setAncienNom(newNom);
					locuteurSelected.setNouveauNom(newNom);
					locuteurSelected.setGroupPost(newGp);
					locuteurSelected.setProfil(newP);
				}
			} else if (indexSelect == 3) {
				if (locuteurSelected.getProfil().equals(newP)) {
//					locuteurSelected.setAncienNom(newNom);
					locuteurSelected.setNouveauNom(newNom);
					locuteurSelected.setGroupPost(newGp);
					locuteurSelected.setMail(newM);
				}
			} else if (indexSelect == 2) {
				if (locuteurSelected.getGroupPost().equals(newGp)) {
//					locuteurSelected.setAncienNom(newNom);
					locuteurSelected.setNouveauNom(newNom);
					locuteurSelected.setProfil(newP);
					locuteurSelected.setMail(newM);
				}
			}
		}
		// newModel.fireTableRowsUpdated(0, listLocuteursPourNettoyage.size() -
		// 1);
		return newModel;
	}

	public int getNbreModifsTotal() {
		return nbreModifsTotal;
	}
}
