package controleurs.operations.projet;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JOptionPane;

import modeles.ListeModel;
import vue.dialog.DialogPanelListeListes;

public class RemoveListes {

	private ResourceBundle bundleProjetController;
	private Set<ListeModel> setListes;
	private List<Integer> listNumListes = null;

	public RemoveListes(ResourceBundle bundleProjetController,
			Set<ListeModel> setListes) {
		super();
		this.bundleProjetController = bundleProjetController;
		this.setListes = setListes;
	}

	public void displayDialog() {
		DialogPanelListeListes optPanel = new DialogPanelListeListes(
				setListes, bundleProjetController, "remove");
		int result = JOptionPane.showOptionDialog(null, optPanel,
				bundleProjetController.getString("txt_SupprimerListes"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			List<Integer> listNumListesSelected = optPanel
					.getListNumListesSelected();
//			System.out
//					.println("RemoveListe - action() : taille de listNumListesSelected = "
//							+ listNumListesSelected.size());
			if (listNumListesSelected.size() != 0) {
				int diag = JOptionPane
						.showOptionDialog(
								null,
								bundleProjetController
										.getString("txt_PoursuivreSuppression")
										+ " ?", bundleProjetController
										.getString("txt_SupprimerListes"),
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (diag == JOptionPane.OK_OPTION) {
					setListNumListes(listNumListesSelected);					
				}
			} else {
				JOptionPane
						.showMessageDialog(null, bundleProjetController
								.getString("txt_Selectionner1Liste"),
								bundleProjetController
										.getString("txt_SupprimerListes"),
								JOptionPane.INFORMATION_MESSAGE);
				displayDialog();
			}
		}
	}

	public List<Integer> getListNumListes() {
		return listNumListes;
	}

	public void setListNumListes(List<Integer> listNumListes) {
		this.listNumListes = listNumListes;
	}

}
