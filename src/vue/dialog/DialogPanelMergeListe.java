package vue.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import modeles.ListeModel;

public class DialogPanelMergeListe extends JPanel {

	private static final long serialVersionUID = 1L;
	private Set<ListeModel> tabListes;
	private final List<Integer> listListesSelected = new ArrayList<Integer>();
	private ResourceBundle bundleProjetController;

	public DialogPanelMergeListe(Set<ListeModel> tabListes,
			ResourceBundle bundleProjetController) {
		this.tabListes = tabListes;
		this.bundleProjetController = bundleProjetController;
		createGui();
	}

	public void createGui() {
		JPanel panelLabel = new JPanel();
		panelLabel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelLabel.setOpaque(true);
		panelLabel.setMaximumSize(new Dimension(400, 30));
		JLabel label = new JLabel(
				bundleProjetController.getString("txt_CocherListesFusionner")
						+ " : ");

		panelLabel.add(label);

		JPanel panelChk = new JPanel();
		panelChk.setLayout(new BoxLayout(panelChk, BoxLayout.PAGE_AXIS));
		panelChk.setOpaque(true);

		for (ListeModel liste : tabListes) {
			final int numListe = liste.getNumero();
			String nomListe = liste.getNom();
			JCheckBox box = new JCheckBox("(" + numListe + ") " + nomListe+ " (" + liste.getNbreMessages() + " "
					+ bundleProjetController.getString("txt_Messages")+")");
			box.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED)
						listListesSelected.add(numListe);
					else {
						for (int i = 0; i < listListesSelected.size(); i++) {
							int numListeSelectedExistante = listListesSelected
									.get(i);
							if (numListeSelectedExistante == numListe)
								listListesSelected.remove(i);
						}
					}
				}
			});
			panelChk.add(box);
		}

		JScrollPane scrollChk = new JScrollPane(panelChk);
		scrollChk.setMaximumSize(new Dimension(400, 100));
		scrollChk.setMinimumSize(new Dimension(400, 100));
		scrollChk.setPreferredSize(new Dimension(400, 100));

		add(panelLabel);
		add(scrollChk);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setOpaque(true);
		setMaximumSize(new Dimension(400, 140));
	}

	public List<Integer> getListListesSelected() {
		return listListesSelected;
	}
}
