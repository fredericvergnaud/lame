package controleurs.operations.liste;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import vue.dialog.DialogPanelAjoutListe;

public class ModifyNomListe {
	
	private String oldName = null, newName = null;
	private ResourceBundle bundleOperationsListe;
	
	public ModifyNomListe(String oldName, ResourceBundle bundleOperationsListe) {
		this.oldName = oldName;
		this.bundleOperationsListe = bundleOperationsListe;
	}

	public void displayDialog() {
		DialogPanelAjoutListe optPanel = new DialogPanelAjoutListe(
				bundleOperationsListe, oldName);
		int result = JOptionPane.showOptionDialog(null, optPanel,
				bundleOperationsListe.getString("txt_ModifierNomListe"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			String newNomListe = optPanel.getNomListe();
			if (newNomListe.equals("")) {
				JOptionPane.showMessageDialog(null,
						bundleOperationsListe.getString("txt_SaisirNom"),
						bundleOperationsListe.getString("txt_ModifierNomListe"),
						JOptionPane.ERROR_MESSAGE);
				displayDialog();
			} else {
				setNewName(newNomListe);						
			}
		}
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	
}
