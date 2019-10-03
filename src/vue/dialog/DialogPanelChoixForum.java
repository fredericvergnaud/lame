package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import modeles.ForumModel;

public class DialogPanelChoixForum extends JPanel {

	private static final long serialVersionUID = 1L;
	private String forumSelected, nomForumEnCours;
	private ResourceBundle bundleOperationsListe;
	private List<ForumModel> listForums;
	private JCheckBox chkUpdate = new JCheckBox();

	public DialogPanelChoixForum(ResourceBundle bundleOperationsListe, List<ForumModel> listForums, String nomForumEnCours) {
		this.bundleOperationsListe = bundleOperationsListe;
		this.listForums = listForums;
		this.nomForumEnCours = nomForumEnCours;
		createGui();
	}

	public void createGui() {
		JPanel panelLabel = new JPanel();
		panelLabel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelLabel.setOpaque(true);
		panelLabel.setMinimumSize(new Dimension(400, 30));
		JLabel labChoixForum = new JLabel(bundleOperationsListe.getString("txt_ChoixForum") + " : ");
		panelLabel.add(labChoixForum);

		JPanel panelRadios = new JPanel();
		panelRadios.setLayout(new BoxLayout(panelRadios, BoxLayout.Y_AXIS));
		panelRadios.setOpaque(true);
		panelRadios.setAlignmentY(Component.LEFT_ALIGNMENT);

		ButtonGroup groupB = new ButtonGroup();

		for (final ForumModel forum : listForums) {
			final JRadioButton rb = new JRadioButton(forum.getNom());
			rb.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent ie) {
					if (ie.getStateChange() == ItemEvent.SELECTED)
						if (forum.getNom().equals(nomForumEnCours)) {
							forumSelected = rb.getText();
							chkUpdate.setSelected(true);
						} else {
							forumSelected = rb.getText();
							chkUpdate.setSelected(false);
						}
					else
						chkUpdate.setSelected(false);
				}
			});
			groupB.add(rb);
			JPanel panelRadio = new JPanel();
			panelRadio.setLayout(new FlowLayout(FlowLayout.LEFT));
			panelRadio.setOpaque(true);
			JLabel labNbreTopics = new JLabel("(" + forum.getNbreTopicsEnviron() + " " + bundleOperationsListe.getString("txt_TopicsEnviron") + ")");
			panelRadio.add(rb);
			if (forum.getNom().equals(nomForumEnCours)) {
				rb.setSelected(true);
				chkUpdate.setText(bundleOperationsListe.getString("txt_UpdateForum"));				
				chkUpdate.setSelected(true);
				panelRadio.add(chkUpdate);
			} else
				panelRadio.add(labNbreTopics);

			panelRadios.add(panelRadio);
		}

		add(panelLabel);
		JScrollPane scrollRadios = new JScrollPane(panelRadios);
		scrollRadios.setMinimumSize(new Dimension(400, 200));
		scrollRadios.setMaximumSize(new Dimension(400, 200));
		scrollRadios.setPreferredSize(new Dimension(400, 200));
		add(scrollRadios);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
		setMinimumSize(new Dimension(400, 230));
		setMaximumSize(new Dimension(400, 230));
		setPreferredSize(new Dimension(400, 230));
	}

	public String getForumSelected() {
		return forumSelected;
	}

	public boolean chkUpdateSelected() {
		return chkUpdate.isSelected();
	}

}
