package vue.dialog;

import java.util.ResourceBundle;
import java.util.SortedSet;

import javax.swing.JOptionPane;

import modeles.LocuteurModel;

public class DialogSplitListe {

	private ResourceBundle bundleOperationsListe;
	private String paramSplit1 = null, paramSplit2 = null;
	private SortedSet<LocuteurModel> setLocuteurs;

	public DialogSplitListe(ResourceBundle bundleOperationsListe, SortedSet<LocuteurModel> setLocuteurs) {
		this.bundleOperationsListe = bundleOperationsListe;
		this.setLocuteurs = setLocuteurs;
	}

	public void displayDialogParamSplit() {
		DialogPanelSplitListeParam optPanel = new DialogPanelSplitListeParam(bundleOperationsListe);
		int result = JOptionPane.showOptionDialog(null, optPanel, bundleOperationsListe.getString("txt_ScissionListe"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			String paramSplit1 = optPanel.getParamSplit1();
			setParamSplit1(paramSplit1);
			if (paramSplit1.equals(bundleOperationsListe.getString("txt_ParamDate"))) {
				DialogPanelSplitListeDate datePanel = new DialogPanelSplitListeDate(bundleOperationsListe);
				int result2 = JOptionPane.showOptionDialog(null, datePanel, bundleOperationsListe.getString("txt_ScissionListe"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						null, null);
				if (result2 == JOptionPane.OK_OPTION) {
					String paramSplit2 = datePanel.getParamSplit2();
					if (paramSplit2.equals("")) {
						JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_SelectionnerParamDate"), bundleOperationsListe.getString("txt_ScissionListe"),
								JOptionPane.ERROR_MESSAGE);
						displayDialogParamSplit();
						return;
					} else {
						System.out.println("Split - displayDialogParamSplit : paramSplit2 = " + paramSplit2);
						setParamSplit2(paramSplit2);
					}
				}
			} else if (paramSplit1.equals(bundleOperationsListe.getString("txt_ParamLocuteur"))) {
				if (setLocuteurs.size() > 0) {
					DialogPanelSplitListeLocuteur locuteurPanel = new DialogPanelSplitListeLocuteur(bundleOperationsListe, setLocuteurs);
					int result2 = JOptionPane.showOptionDialog(null, locuteurPanel, bundleOperationsListe.getString("txt_ScissionListe"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
							null, null, null);
					if (result2 == JOptionPane.OK_OPTION) {
						String paramSplit2 = locuteurPanel.getParamSplit2();
						if (paramSplit2.equals("")) {
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_SelectionnerParamLocuteur"), bundleOperationsListe.getString("txt_ScissionListe"),
									JOptionPane.ERROR_MESSAGE);
							displayDialogParamSplit();
							return;
						} else {
							System.out.println("Split - displayDialogParamSplit : paramSplit2 = " + paramSplit2);
							setParamSplit2(paramSplit2);
						}
					}
				} else
					JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ScissionImpossibleNoLocuteurs"), bundleOperationsListe.getString("txt_ScissionListe"),
							JOptionPane.INFORMATION_MESSAGE);

			}
		}
	}

	public String getParamSplit1() {
		return paramSplit1;
	}

	public void setParamSplit1(String paramSplit1) {
		this.paramSplit1 = paramSplit1;
		System.out.println("Split - setParamSplit1 : paramSplit1 = " + paramSplit1);

	}

	public String getParamSplit2() {
		return paramSplit2;
	}

	public void setParamSplit2(String paramSplit2) {
		this.paramSplit2 = paramSplit2;
	}

}
