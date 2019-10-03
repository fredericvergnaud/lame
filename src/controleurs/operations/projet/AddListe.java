package controleurs.operations.projet;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import vue.dialog.DialogPanelAjoutListe;

public class AddListe {

	private ResourceBundle bundleProjetController;
	private String nomListe = null;

	public AddListe(ResourceBundle bundleProjetController) {
		super();
		this.bundleProjetController = bundleProjetController;
	}

	public void displayDialog() {
		DialogPanelAjoutListe optPanel = new DialogPanelAjoutListe(
				bundleProjetController, "");
		int result = JOptionPane.showOptionDialog(null, optPanel,
				bundleProjetController.getString("txt_AjoutListe"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			String nomListeSaisi = optPanel.getNomListe();
			if (nomListeSaisi.equals("")) {
				JOptionPane.showMessageDialog(null,
						bundleProjetController.getString("txt_SaisirNom"),
						bundleProjetController.getString("txt_AjoutListe"),
						JOptionPane.ERROR_MESSAGE);
				displayDialog();
			} else {
				setNomListe(nomListeSaisi);
			}
		}
	}

	public String getNomListe() {
		return nomListe;
	}

	public void setNomListe(String nomListe) {
		this.nomListe = nomListe;
	}

}
