package vue.dialog;

import java.awt.Component;
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

public class DialogPanelListeListes extends JPanel {

	private static final long serialVersionUID = 1L;
	private Set<ListeModel> tabListes;
	private ResourceBundle bundleProjetController;
	private List<Integer> listNumListesSelected = new ArrayList<Integer>();
	private List<ListeModel> listListesSelected = new ArrayList<ListeModel>();
	private String type;

	public DialogPanelListeListes(Set<ListeModel> tabListes, ResourceBundle bundleProjetController, String type) {
		this.tabListes = tabListes;
		this.bundleProjetController = bundleProjetController;
		this.type = type;
		createGui();
	}

	public void createGui() {
		JPanel panelLabel = new JPanel();
		panelLabel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelLabel.setOpaque(true);
		panelLabel.setMaximumSize(new Dimension(400, 30));
		JLabel label;
		if (type.equals("remove"))
			label = new JLabel(bundleProjetController.getString("txt_SelectionListesSupprimer") + " : ");
		else if (type.equals("import"))
			label = new JLabel(bundleProjetController.getString("txt_SelectionListesImporter") + " : ");
		else
			label = new JLabel(bundleProjetController.getString("txt_SelectionListesExporter") + " : ");
		panelLabel.add(label);

		final JPanel panelChk = new JPanel();
		panelChk.setLayout(new BoxLayout(panelChk, BoxLayout.PAGE_AXIS));
		panelChk.setOpaque(true);

		JCheckBox boxAllSelected = new JCheckBox(bundleProjetController.getString("txt_ToutSelectionner"));
		boxAllSelected.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Component[] chkListe = panelChk.getComponents();
					for (int i = 1; i < chkListe.length; i++) {
						JCheckBox chkBox = (JCheckBox) chkListe[i];
						chkBox.setSelected(true);
					}
				} else {
					Component[] chkListe = panelChk.getComponents();
					for (int i = 1; i < chkListe.length; i++) {
						JCheckBox chkBox = (JCheckBox) chkListe[i];
						chkBox.setSelected(false);
					}
				}
			}
		});
		panelChk.add(boxAllSelected);

		for (final ListeModel liste : tabListes) {
			JCheckBox box;
			if (type.equals("remove")) {
				final int numListe = liste.getNumero();
				String nomListe = liste.getNom();
				box = new JCheckBox(numListe + ". " + nomListe + " (" + liste.getNbreMessages() + " " + bundleProjetController.getString("txt_Messages") + ")");
				box.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED)
							listNumListesSelected.add(numListe);
						else {
							for (int i = 0; i < listNumListesSelected.size(); i++) {
								int numListeSelectedExistante = listNumListesSelected.get(i);
								if (numListeSelectedExistante == numListe)
									listNumListesSelected.remove(i);
							}
						}
					}
				});
			} else {
				String nomListe = liste.getNom();
				box = new JCheckBox(nomListe + " (" + liste.getNbreMessages() + " " + bundleProjetController.getString("txt_Messages") + ")");
				box.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED)
							listListesSelected.add(liste);
						else {
							for (int i = 0; i < listListesSelected.size(); i++) {
								ListeModel listeSelectedExistante = listListesSelected.get(i);
								if (listeSelectedExistante == liste)
									listListesSelected.remove(i);
							}
						}
					}
				});
			}
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

	public List<Integer> getListNumListesSelected() {
		return listNumListesSelected;
	}
	
	public List<ListeModel> getListListesSelected() {
		return listListesSelected;
	}
}
