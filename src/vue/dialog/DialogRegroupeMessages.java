package vue.dialog;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class DialogRegroupeMessages {

	private ResourceBundle bundleOperationsListe;
	private int paramJours;
	private int paramMessages;
	private int paramLevenshtein;
	private boolean startRegroupement = false, paramNoRegroupementMessages;

	public DialogRegroupeMessages(ResourceBundle bundleOperationsListe, int paramJours, int paramMessages, int paramLevenshtein, boolean paramNoRegroupementMessages) {
		this.bundleOperationsListe = bundleOperationsListe;
		this.paramJours = paramJours;
		this.paramMessages = paramMessages;
		this.paramLevenshtein = paramLevenshtein;
		this.paramNoRegroupementMessages = paramNoRegroupementMessages;
	}

	public void displayDialog() {
		int paramJoursOrig = paramJours, paramMessagesOrig = paramMessages, paramLevenshteinOrig = paramLevenshtein;
		boolean paramNoRegroupementMessagesOrig = paramNoRegroupementMessages;
		DialogPanelParametresRegroupementMessages dialog = new DialogPanelParametresRegroupementMessages(paramJoursOrig, paramMessagesOrig, paramLevenshteinOrig, paramNoRegroupementMessagesOrig,
				bundleOperationsListe);
		int result = JOptionPane.showOptionDialog(null, dialog.getPanel(), bundleOperationsListe.getString("txt_RegroupementMessages"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			System.out.println("dialog.getParamNoRegroupementMessages() = " + dialog.getParamNoRegroupementMessages());
			setParamNoRegroupementMessages(dialog.getParamNoRegroupementMessages());
			if (dialog.getParamNoRegroupementMessages()) {
				setParamJours(30);
				setParamMessages(-1);
				setParamLevenshtein(1);
				setStartRegroupement(true);
			} else {
				// TESTS SUR SAISIE PARAMETRES CONVERSATIONS JOURS OU MESSAGES
				if ((!dialog.getTxtParametreJours().getText().equals("-1") && !dialog.getTxtParametreMessages().getText().equals("-1"))
						|| (dialog.getTxtParametreJours().getText().equals("-1") && dialog.getTxtParametreMessages().getText().equals("-1"))
						|| (dialog.getTxtParametreJours().getText().equals("") && dialog.getTxtParametreMessages().getText().equals(""))
						|| (dialog.getTxtParametreJours().getText().equals("") || dialog.getTxtParametreMessages().getText().equals(""))) {
					JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ManqueParametre"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
					displayDialog();
					return;
				} else {
					setParamJours(Integer.parseInt(dialog.getTxtParametreJours().getText()));
					setParamMessages(Integer.parseInt(dialog.getTxtParametreMessages().getText()));
					// TEST SUR SAISIE PARAMETRE LEVENSHTEIN
					if (dialog.getTxtParametreLevenshtein().getText().equals("") || Integer.parseInt(dialog.getTxtParametreLevenshtein().getText()) >= 15) {
						JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ErreurParamLevenshtein"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
						displayDialog();
						return;
					} else {
						setParamLevenshtein(Integer.parseInt(dialog.getTxtParametreLevenshtein().getText()));
						setStartRegroupement(true);
					}
				}
			}
		}
	}

	public int getParamJours() {
		return paramJours;
	}

	public void setParamJours(int paramJours) {
		this.paramJours = paramJours;
	}

	public int getParamMessages() {
		return paramMessages;
	}

	public void setParamMessages(int paramMessages) {
		this.paramMessages = paramMessages;
	}

	public int getParamLevenshtein() {
		return paramLevenshtein;
	}

	public void setParamLevenshtein(int paramLevenshtein) {
		this.paramLevenshtein = paramLevenshtein;
	}

	public boolean getStartRegroupement() {
		return startRegroupement;
	}

	public void setStartRegroupement(boolean startRegroupement) {
		this.startRegroupement = startRegroupement;
	}

	public boolean getParamNoRegroupementMessages() {
		return paramNoRegroupementMessages;
	}

	public void setParamNoRegroupementMessages(boolean paramNoRegroupementMessages) {
		this.paramNoRegroupementMessages = paramNoRegroupementMessages;
	}

}
