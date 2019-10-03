package vue.dialog;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class DialogAnalyseMessages {

	private ResourceBundle bundleOperationsListe;
	private float paramLocuteursDominants;
	private float paramSujetsCollectifs;
	private boolean paramLocuteursSC, startAnalyse = false;

	public DialogAnalyseMessages(ResourceBundle bundleOperationsListe, float paramLocuteursDominants, float paramSujetsCollectifs, boolean paramLocuteursSC) {
		this.bundleOperationsListe = bundleOperationsListe;
		this.paramLocuteursDominants = paramLocuteursDominants;
		this.paramSujetsCollectifs = paramSujetsCollectifs;
		this.paramLocuteursSC = paramLocuteursSC;
	}

	public void displayDialog() {
		float paramLocuteursDominantsOrig = paramLocuteursDominants, paramSujetsCollectifsOrig = paramSujetsCollectifs;
		boolean paramLocuteursSCOrig = paramLocuteursSC;
		// System.out.println("AnalyseMessages - displayDialog : enableParamJoursMessagesLevenshteinOrig = "
		// + enableParamJoursMessagesLevenshteinOrig);
		DialogPanelParametresAnalyse dialog = new DialogPanelParametresAnalyse(paramLocuteursDominantsOrig, paramSujetsCollectifsOrig, paramLocuteursSCOrig, bundleOperationsListe);
		int result = JOptionPane.showOptionDialog(null, dialog.getPanel(), bundleOperationsListe.getString("txt_CalculStats"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
				null);
		if (result == JOptionPane.OK_OPTION) {
			// TEST SUR SAISIE PARAMETRES LOCUTEURS DOMINANTS
			if (dialog.getDiagParamLD().getTxtParamLD().getText().equals("") || dialog.getDiagParamLD().getTxtParamLD().getText().equals("0")) {
				JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ErreurParamLD"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
				displayDialog();
				return;
			} else {
				setParamLocuteursDominants(Float.parseFloat(dialog.getDiagParamLD().getTxtParamLD().getText()));
				// TEST SUR SAISIE PARAMETRES SUJETS COLLECTIFS
				if (dialog.getDiagParamSC().getTxtParamSC().getText().equals("") || dialog.getDiagParamSC().getTxtParamSC().getText().equals("0")) {
					JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ErreurParamSC"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
					displayDialog();
					return;
				} else {
					setParamSujetsCollectifs(Float.parseFloat(dialog.getDiagParamSC().getTxtParamSC().getText()));
					setLocuteursSC(dialog.getDiagParamSC().getChkParametreSC());
					startAnalyse = true;
				}
			}
		}
	}

	public float getParamLocuteursDominants() {
		return paramLocuteursDominants;
	}

	public void setParamLocuteursDominants(float paramLocuteursDominants) {
		this.paramLocuteursDominants = paramLocuteursDominants;
	}

	public float getParamSujetsCollectifs() {
		return paramSujetsCollectifs;
	}

	public void setParamSujetsCollectifs(float paramSujetsCollectifs) {
		this.paramSujetsCollectifs = paramSujetsCollectifs;
	}

	public boolean getParamLocuteursSC() {
		return paramLocuteursSC;
	}

	public void setLocuteursSC(boolean isLocuteursSC) {
		this.paramLocuteursSC = isLocuteursSC;
	}

	public boolean startAnalyse() {
		return startAnalyse;
	}

}
