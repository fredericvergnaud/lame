package vue.dialog;

import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class DialogPanelParametresAnalyse extends JPanel {

	private static final long serialVersionUID = 1L;
	private float paramLocuteursDominants, paramSujetsCollectifs;
	private boolean isLocuteursSC;
	private DialogPanelParametreCalculLD dialogParamLD;
	private DialogPanelParametreCalculSC dialogParamSC;
	private ResourceBundle bundleOperationsListe;

	public DialogPanelParametresAnalyse(float paramLocuteursDominants,
			float paramSujetsCollectifs, boolean isLocuteursSC, 
			ResourceBundle bundleOperationsListe) {
		this.paramLocuteursDominants = paramLocuteursDominants;
		this.paramSujetsCollectifs = paramSujetsCollectifs;
		this.isLocuteursSC = isLocuteursSC;
		this.bundleOperationsListe = bundleOperationsListe;
	}

	public JPanel getPanel() {
		dialogParamLD = new DialogPanelParametreCalculLD(
				paramLocuteursDominants, bundleOperationsListe);
		add(dialogParamLD.getPanel());

		dialogParamSC = new DialogPanelParametreCalculSC(paramSujetsCollectifs,
				isLocuteursSC, bundleOperationsListe);
		add(dialogParamSC.getPanel());

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);

		return this;
	}

	public DialogPanelParametreCalculLD getDiagParamLD() {
		return dialogParamLD;
	}

	public DialogPanelParametreCalculSC getDiagParamSC() {
		return dialogParamSC;
	}
}
