package controleurs.operations.projet;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JOptionPane;

import modeles.ListeModel;
import vue.dialog.DialogPanelMergeListe;

public class MergeListes {

	private Set<ListeModel> setListes = null;
	private ResourceBundle bundleProjetController;
	private List<Integer> listListesSelected;

	public MergeListes(Set<ListeModel> setListes,
			ResourceBundle bundleProjetController) {
		this.setListes = setListes;
		this.bundleProjetController = bundleProjetController;
	}

	public void displayDialog() {
		if (setListes.size() < 2) {
			JOptionPane.showMessageDialog(null,
					bundleProjetController.getString("txt_FusionImpossible"),
					bundleProjetController.getString("txt_FusionListes"),
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		DialogPanelMergeListe optPanel = new DialogPanelMergeListe(setListes,
				bundleProjetController);
		int result = JOptionPane.showOptionDialog(null, optPanel,
				bundleProjetController.getString("txt_FusionListes"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			List<Integer> listListesSelected = optPanel.getListListesSelected();
			// System.out.println("taille de listListesSelected = "
			// + listListesSelected.size());
			if (listListesSelected.size() > 1) {
				int diag = JOptionPane
						.showOptionDialog(
								null,
								bundleProjetController
										.getString("txt_PoursuivreFusionListes")
										+ " ?", bundleProjetController
										.getString("txt_FusionListes"),
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (diag == JOptionPane.OK_OPTION) {
					setListListesSelected(listListesSelected);
				}
			} else {
				JOptionPane.showMessageDialog(null, bundleProjetController
						.getString("txt_Selectionner2Listes"),
						bundleProjetController.getString("txt_FusionListes"),
						JOptionPane.INFORMATION_MESSAGE);
				displayDialog();
			}
		}
	}

	public List<Integer> getListListesSelected() {
		return listListesSelected;
	}

	public void setListListesSelected(List<Integer> listListesSelected) {
		this.listListesSelected = listListesSelected;
	}
}
