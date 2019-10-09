package controleurs.operations.projet;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JOptionPane;

import modeles.ListeModel;
import vue.dialog.DialogPanelListeListes;

public class ImportListes {

	private ResourceBundle bundleProjetController;
	private Set<ListeModel> setListes;
	private List<ListeModel> listListes = null;

	public ImportListes(Set<ListeModel> setListes, ResourceBundle bundleProjetController) {
		super();
		this.bundleProjetController = bundleProjetController;
		this.setListes = setListes;
	}

	public void displayDialog() {
		DialogPanelListeListes optPanel = new DialogPanelListeListes(setListes, bundleProjetController, "import");
		int result = JOptionPane.showOptionDialog(null, optPanel, bundleProjetController.getString("txt_ImporterListes"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			List<ListeModel> listListesSelected = optPanel.getListListesSelected();
			 System.out
			 .println("RemoveListe - action() : taille de listNumListesSelected = "
			 + listListesSelected.size());
			setListListes(listListesSelected);
		}
	}

	public List<ListeModel> getListListes() {
		return listListes;
	}

	public void setListListes(List<ListeModel> listListes) {
		this.listListes = listListes;
	}

}
