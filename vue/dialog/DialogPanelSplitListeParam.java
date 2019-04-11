package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ResourceBundle;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DialogPanelSplitListeParam extends JPanel {

	private static final long serialVersionUID = 1L;
//	private Set<ListeModel> tabListes;
	private JComboBox listesListe, parametresListe;
	private ResourceBundle bundleProjetController;

	public DialogPanelSplitListeParam(
//			Set<ListeModel> tabListes,
			ResourceBundle bundleProjetController) {
//		this.tabListes = tabListes;
		this.bundleProjetController = bundleProjetController;
		createGui();
	}

	public void createGui() {
//		JPanel panelLabel = new JPanel();
//		panelLabel.setLayout(new FlowLayout(FlowLayout.LEFT));
//		panelLabel.setOpaque(true);
//		JLabel label = new JLabel(
//				bundleProjetController.getString("txt_SelectionListeScinder")
//						+ " : ");
//
//		panelLabel.add(label);
//
//		JPanel panelListe = new JPanel();
//		panelListe.setLayout(new FlowLayout(FlowLayout.LEFT));
//		panelListe.setOpaque(true);
//		String[] tListes = new String[tabListes.size() + 1];
//		tListes[0] = "";
//		int i = 1;
//		for (ListeModel liste : tabListes) {
//			int numListe = liste.getNumero();
//			String nomListe = liste.getNom();
//			tListes[i] = "(" + numListe + ") " + nomListe;
//			i++;
//		}
//		listesListe = new JComboBox(tListes);
//		listesListe.setPreferredSize(new Dimension(150, 30));
//
//		panelListe.add(listesListe);

		JPanel panelLabel2 = new JPanel();
		panelLabel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelLabel2.setOpaque(true);
		JLabel label2 = new JLabel(
				bundleProjetController.getString("txt_SelectionParamScinder")
						+ " : ");

		panelLabel2.add(label2);

		JPanel panelParametre = new JPanel();
		panelParametre.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelParametre.setOpaque(true);
		String[] tabParam = new String[3];
		tabParam[0] = "";
		tabParam[1] = bundleProjetController.getString("txt_ParamDate");
		tabParam[2] = bundleProjetController.getString("txt_ParamLocuteur");

		parametresListe = new JComboBox(tabParam);
		parametresListe.setPreferredSize(new Dimension(150, 30));

		panelParametre.add(parametresListe);

		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

//		add(panelLabel);
//		add(panelListe);
		add(panelLabel2);
		add(panelParametre);
	}

//	public String getListesListe() {
//		return listesListe.getSelectedItem().toString();
//	}

	public String getParamSplit1() {
		return parametresListe.getSelectedItem().toString();
	}
}
