package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DialogPanelSplitListeDate extends JPanel {

	private static final long serialVersionUID = 1L;
	private JComboBox datesListe;
	private ResourceBundle bundleProjetController;

	public DialogPanelSplitListeDate(ResourceBundle bundleProjetController) {
		this.bundleProjetController = bundleProjetController;
		createGui();
	}

	public void createGui() {
		JPanel panelDate = new JPanel();
		panelDate.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelDate.setOpaque(true);
		JLabel labelDate = new JLabel(bundleProjetController.getString("txt_DecoupageListeSelon") + " : ");
		String[] date = { "", bundleProjetController.getString("txt_ParamSplitList_Jours"), bundleProjetController.getString("txt_ParamSplitList_Mois"),
				bundleProjetController.getString("txt_ParamSplitList_Annees") };
		datesListe = new JComboBox(date);
		datesListe.setPreferredSize(new Dimension(150, 30));

		panelDate.add(labelDate);
		panelDate.add(datesListe);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);

		add(panelDate);

	}

	public String getParamSplit2() {
		return datesListe.getSelectedItem().toString();
	}

}
